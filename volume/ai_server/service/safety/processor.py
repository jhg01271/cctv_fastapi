"""안전관리 AI 처리 프로세스 진입점.

CameraProcessManager의 ai_target으로 등록된다.
RTSP 리더가 공급하는 frame_queue에서 프레임을 꺼내
YOLO 추론 → 이벤트 감지 → WebSocket 결과 전송 → event_queue 전달을 수행한다.

DB 저장은 부모 프로세스의 SafetyDBWorker가 event_queue를 읽어서 처리한다.

이벤트 정의:
  E001 안전모 미착용   — no_helmet, conf≥0.80, cooldown 600s, jpg, relay 20s
  E002 위험구역 출입   — person in ROI polygon, cooldown 600s, jpg, relay 20s
  E003 작업자 쓰러짐   — collapse 15초 지속, cooldown 600s, mp4
  E004 위험체 접근 감지 — forklift/hoist 이동벡터 → 2초 후 예측 → person 3초 지속
"""

from __future__ import annotations

import multiprocessing as mp
import time
from collections import deque
from datetime import datetime
from typing import Optional

import cv2
import numpy as np

from config.config import settings
from core.ai.model_loader import ModelType, get_class_name, load_model
from service.safety.events import (
    check_box_overlap,
    check_roi,
    save_event_clip,
    save_event_image,
    trigger_relay,
)


# ── 메인 프로세스 ─────────────────────────────────────────────────────────────

def safety_process(
    *,
    camera_id: str,
    rtsp_url: str,
    frame_queue: mp.Queue,
    stop_event: mp.Event,
    heartbeat: mp.Value,
    result_mp_queue: mp.Queue,
    event_queue: mp.Queue,
    roi_arr: list,
    detection_is_run: bool,
    pose_is_run: bool,
) -> None:
    """안전관리 AI 처리 프로세스 메인 루프.

    Args:
        camera_id: 카메라 식별자
        rtsp_url: RTSP 주소 (참조용)
        frame_queue: RTSP 리더가 공급하는 프레임 큐
        stop_event: 종료 신호
        heartbeat: 워치독용 생존 카운터
        result_mp_queue: ws_bridge.mp_queue (WebSocket 팬아웃용)
        event_queue: 이벤트 DB 저장용 큐 (SafetyDBWorker가 소비)
        roi_arr: 위험구역 ROI 좌표
        detection_is_run: YOLO Detection 모델 실행 여부
        pose_is_run: YOLO Pose 모델 실행 여부
    """
    # ── 모델 로드
    model_safety = load_model(ModelType.SAFETY, settings.MODEL_SAFETY_PT)
    model_pose   = load_model(ModelType.POSE,   settings.MODEL_POSE_PT)

    FRAME_SKIP          = max(1, settings.AI_FRAME_SKIP)
    CONF_THR            = settings.AI_CONFIDENCE_THRESHOLD
    COOLDOWN_SEC        = float(settings.AI_EVENT_COOLDOWN_SEC)
    COLLAPSE_SUSTAIN    = settings.AI_COLLAPSE_SUSTAIN_SEC
    E003_BUFFER_MAX     = settings.AI_E003_BUFFER_MAX
    E004_PREDICT        = settings.AI_E004_PREDICT_SEC
    E004_SUSTAIN        = settings.AI_E004_PERSON_SUSTAIN_SEC
    E004_HIST_MAX       = settings.AI_E004_HISTORY_MAXLEN

    # ── 쿨다운: event_code → 마지막 발화 시각
    last_fire: dict[str, float] = {}

    # ── E003: collapse 추적
    collapse_start_time: Optional[float] = None
    frame_buffer: deque[np.ndarray] = deque(maxlen=E003_BUFFER_MAX)

    # ── E004: 위험체 위치 이력 및 person 지속 추적
    # track_id → deque[(cx, cy, timestamp)]
    hazard_history: dict[int, deque] = {}
    # track_id → person이 예측 영역에 처음 진입한 시각
    hazard_person_start: dict[int, float] = {}

    # ── 루프 상태
    last_frame: Optional[np.ndarray] = None
    frame_count = 0
    switch = True  # True=Detection, False=Pose

    time.sleep(settings.AI_STARTUP_DELAY)

    while not stop_event.is_set():
        # ── 프레임 취득
        try:
            frame = frame_queue.get(timeout=2.0)
            last_frame = frame
        except Exception:
            if last_frame is None:
                continue
            frame = last_frame

        frame_count += 1
        heartbeat.value += 1

        if frame_count % FRAME_SKIP != 0:
            continue

        now       = time.time()
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        detections: list[dict] = []
        events:     list[dict] = []

        # E003 프레임 버퍼 (스킵 후 적재)
        frame_buffer.append(frame.copy())

        # ══════════════════════════════════════════════════════════════════════
        # [A] Detection 모델 (switch = True)
        # ══════════════════════════════════════════════════════════════════════
        if detection_is_run and switch:
            results = model_safety.track(frame, persist=True, verbose=False)

            person_boxes:    list[list[int]] = []
            hazard_boxes:    list[dict]      = []
            active_tids:     set[int]        = set()

            for result in results:
                boxes = result.boxes
                if boxes is None:
                    continue

                ids = boxes.id  # track id tensor 또는 None

                for i, (box, cls, conf) in enumerate(
                    zip(boxes.xyxy, boxes.cls, boxes.conf)
                ):
                    x1, y1, x2, y2 = map(int, box)
                    cls_idx  = int(cls)
                    conf_val = float(conf)
                    cls_name = get_class_name(ModelType.SAFETY, cls_idx) or "unknown"
                    track_id = int(ids[i]) if ids is not None else -1

                    detections.append({
                        "class": cls_name,
                        "bbox":  [x1, y1, x2, y2],
                        "conf":  round(conf_val, 3),
                    })

                    cx, cy = (x1 + x2) // 2, (y1 + y2) // 2

                    # ── E001: 안전모 미착용
                    if cls_name == "no_helmet" and conf_val >= CONF_THR:
                        if _can_fire(last_fire, "E001", now, COOLDOWN_SEC):
                            img_path = save_event_image(frame, camera_id, "no_helmet")
                            _put_event(event_queue, camera_id, "no_helmet", img_path, timestamp)
                            _set_fire(last_fire, "E001", now)
                            trigger_relay()
                            events.append({"type": "E001", "message": "안전모 미착용 감지"})

                    # ── E002: 위험구역 출입
                    elif cls_name == "person" and conf_val >= CONF_THR:
                        person_boxes.append([x1, y1, x2, y2])
                        if roi_arr and check_roi((cx, cy), roi_arr):
                            if _can_fire(last_fire, "E002", now, COOLDOWN_SEC):
                                img_path = save_event_image(frame, camera_id, "person")
                                _put_event(event_queue, camera_id, "person", img_path, timestamp)
                                _set_fire(last_fire, "E002", now)
                                trigger_relay()
                                events.append({"type": "E002", "message": "위험구역 출입 감지"})

                    # ── E004: 위험체 위치 이력 수집
                    elif cls_name in ("forklift", "hoist") and conf_val >= CONF_THR:
                        hazard_boxes.append({
                            "box":      [x1, y1, x2, y2],
                            "class":    cls_name,
                            "track_id": track_id,
                        })
                        if track_id >= 0:
                            active_tids.add(track_id)
                            if track_id not in hazard_history:
                                hazard_history[track_id] = deque(maxlen=E004_HIST_MAX)
                            hazard_history[track_id].append((cx, cy, now))

            # ── E004: 이동벡터 → 2초 후 예측 → person 3초 지속
            _detect_e004(
                hazard_boxes=hazard_boxes,
                person_boxes=person_boxes,
                hazard_history=hazard_history,
                hazard_person_start=hazard_person_start,
                active_tids=active_tids,
                frame=frame,
                camera_id=camera_id,
                timestamp=timestamp,
                now=now,
                event_queue=event_queue,
                last_fire=last_fire,
                events=events,
                predict_sec=E004_PREDICT,
                person_sustain_sec=E004_SUSTAIN,
                cooldown_sec=COOLDOWN_SEC,
            )

            if pose_is_run:
                switch = False

        # ══════════════════════════════════════════════════════════════════════
        # [B] Pose 모델 (switch = False)
        # ══════════════════════════════════════════════════════════════════════
        elif not switch:
            if pose_is_run:
                collapse_in_frame = False

                pose_results = model_pose(frame, verbose=False)
                for result in pose_results:
                    if result.boxes is None:
                        continue
                    for box in result.boxes:
                        cls_id   = int(box.cls[0])
                        conf_v   = float(box.conf[0])
                        cls_name = get_class_name(ModelType.POSE, cls_id) or "unknown"

                        detections.append({
                            "class": cls_name,
                            "bbox":  list(map(int, box.xyxy[0])),
                            "conf":  round(conf_v, 3),
                        })

                        if cls_name == "collapse" and conf_v >= CONF_THR:
                            collapse_in_frame = True

                # ── E003: collapse 지속 시간 판정
                if collapse_in_frame:
                    if collapse_start_time is None:
                        collapse_start_time = now
                    elif now - collapse_start_time >= COLLAPSE_SUSTAIN:
                        if _can_fire(last_fire, "E003", now, COOLDOWN_SEC):
                            clip_path = save_event_clip(list(frame_buffer), camera_id)
                            _put_event(event_queue, camera_id, "collapse", clip_path, timestamp)
                            _set_fire(last_fire, "E003", now)
                            events.append({"type": "E003", "message": "작업자 쓰러짐 감지"})
                        collapse_start_time = None  # 발화 후 초기화
                else:
                    collapse_start_time = None  # 감지 끊기면 초기화

            if detection_is_run:
                switch = True

        # ── WebSocket 결과 전송
        _send_result(result_mp_queue, camera_id, timestamp, detections, events)


# ── E004 보조 함수 ─────────────────────────────────────────────────────────────

def _detect_e004(
    *,
    hazard_boxes: list[dict],
    person_boxes: list[list[int]],
    hazard_history: dict[int, deque],
    hazard_person_start: dict[int, float],
    active_tids: set[int],
    frame: np.ndarray,
    camera_id: str,
    timestamp: str,
    now: float,
    event_queue: mp.Queue,
    last_fire: dict[str, float],
    events: list[dict],
    predict_sec: float,
    person_sustain_sec: float,
    cooldown_sec: float,
) -> None:
    """E004 위험체 접근 이벤트 감지.

    이동 벡터 계산 → predict_sec 후 위치 예측 → 예측 영역에 person person_sustain_sec 지속 시 발화.
    """
    current_person_tids: set[int] = set()

    for hb in hazard_boxes:
        tid = hb["track_id"]
        if tid < 0 or tid not in hazard_history:
            continue

        history = hazard_history[tid]
        if len(history) < 2:
            continue

        # 속도 계산 (픽셀/초): 가장 오래된 샘플과 최신 샘플 사용
        oldest_cx, oldest_cy, oldest_t = history[0]
        newest_cx, newest_cy, newest_t = history[-1]
        dt = newest_t - oldest_t
        if dt < 0.1:
            continue

        vx = (newest_cx - oldest_cx) / dt
        vy = (newest_cy - oldest_cy) / dt

        # predict_sec 후 중심 예측
        pred_cx = int(newest_cx + vx * predict_sec)
        pred_cy = int(newest_cy + vy * predict_sec)

        # 예측 bbox (원래 박스 크기 유지)
        bx1, by1, bx2, by2 = hb["box"]
        half_w = (bx2 - bx1) // 2
        half_h = (by2 - by1) // 2
        pred_box = [
            pred_cx - half_w,
            pred_cy - half_h,
            pred_cx + half_w,
            pred_cy + half_h,
        ]

        # 예측 영역에 person이 있는지 확인
        person_in_zone = any(
            check_box_overlap(pb, pred_box) for pb in person_boxes
        )

        if person_in_zone:
            current_person_tids.add(tid)
            if tid not in hazard_person_start:
                hazard_person_start[tid] = now
            elif now - hazard_person_start[tid] >= person_sustain_sec:
                if _can_fire(last_fire, "E004", now, cooldown_sec):
                    img_path = save_event_image(frame, camera_id, "collision")
                    _put_event(event_queue, camera_id, "collision", img_path, timestamp)
                    _set_fire(last_fire, "E004", now)
                    events.append({
                        "type":    "E004",
                        "message": f"작업자-{hb['class']} 위험체 접근 감지",
                    })
                hazard_person_start.pop(tid, None)  # 발화 후 초기화

    # 예측 영역에서 벗어난 track 초기화
    for tid in list(hazard_person_start.keys()):
        if tid not in current_person_tids:
            hazard_person_start.pop(tid, None)

    # 사라진 track 정리
    for tid in list(hazard_history.keys()):
        if tid not in active_tids:
            hazard_history.pop(tid, None)
            hazard_person_start.pop(tid, None)


# ── 공통 헬퍼 ─────────────────────────────────────────────────────────────────

def _can_fire(last_fire: dict[str, float], code: str, now: float, cooldown_sec: float = 600.0) -> bool:
    """쿨다운이 끝났으면 True를 반환한다."""
    return now - last_fire.get(code, 0.0) >= cooldown_sec


def _set_fire(last_fire: dict[str, float], code: str, now: float) -> None:
    """마지막 발화 시각을 기록한다."""
    last_fire[code] = now


def _put_event(
    q: mp.Queue,
    camera_id: str,
    event_key: str,
    image_path: str | None,
    timestamp: str,
) -> None:
    """event_queue에 이벤트를 전달한다."""
    try:
        q.put_nowait({
            "camera_id":  camera_id,
            "event_key":  event_key,
            "image_path": image_path,
            "timestamp":  timestamp,
        })
    except Exception:
        pass


def _send_result(
    q: mp.Queue,
    camera_id: str,
    timestamp: str,
    detections: list[dict],
    events: list[dict],
) -> None:
    """ws_bridge mp_queue에 AI 결과를 전송한다."""
    try:
        q.put_nowait({
            "camera_id":  camera_id,
            "timestamp":  timestamp,
            "detections": detections,
            "events":     events,
        })
    except Exception:
        pass
