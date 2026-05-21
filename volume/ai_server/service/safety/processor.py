"""안전관리 AI 처리 프로세스 진입점.

CameraProcessManager의 ai_target으로 등록된다.
RTSP 리더가 공급하는 frame_queue에서 프레임을 꺼내
YOLO 추론 → 이벤트 감지 → WebSocket 결과 전송 → event_queue 전달을 수행한다.

DB 저장은 부모 프로세스의 SafetyDBWorker가 event_queue를 읽어서 처리한다.

이벤트 정의:
  E001 안전모 미착용   — no_helmet, conf≥0.80, cooldown 600s, jpg, relay 20s
  E002 위험구역 출입   — person in ROI polygon, cooldown 600s, jpg, relay 20s
  E003 작업자 쓰러짐   — collapse 15초 지속, cooldown 600s, mp4
  E004 위험체 접근 감지 — 안전 격자 기반 이동벡터 → 2초 후 예측 격자 → person 존재 시 발화
"""

from __future__ import annotations

import math
import multiprocessing as mp
import time
from collections import deque
from datetime import datetime
from typing import Optional

import cv2
import numpy as np

from config.config import settings
from core.ai.model_loader import ModelType, get_class_name, load_model
from core.logging.logger import get_logger
from service.safety.events import (
    check_roi,
    save_event_clip,
    save_event_image,
    trigger_relay,
)

logger = get_logger(__name__)


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
    safety_grid_arr: list | None = None,
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
        safety_grid_arr: 안전 격자 좌표 (E004 격자 기반 감지용)
    """
    # ── 모델 로드
    model_safety = load_model(ModelType.SAFETY, settings.MODEL_SAFETY_PT)
    model_pose   = load_model(ModelType.POSE,   settings.MODEL_POSE_PT)

    FRAME_SKIP          = max(1, settings.AI_FRAME_SKIP)
    CONF_THR            = settings.AI_CONFIDENCE_THRESHOLD
    COOLDOWN_SEC        = float(settings.AI_EVENT_COOLDOWN_SEC)
    COLLAPSE_SUSTAIN    = settings.AI_COLLAPSE_SUSTAIN_SEC
    E003_BUFFER_MAX     = settings.AI_E003_BUFFER_MAX

    # ── 안전 격자 → 4차 좌표 변환 (OpenCV 5차 중첩 → 일반 2D)
    safety_grid: list = _convert_grid_coords(safety_grid_arr) if safety_grid_arr else []

    # ── 쿨다운: event_code → 마지막 발화 시각
    last_fire: dict[str, float] = {}

    # ── E003: collapse 추적
    collapse_start_time: Optional[float] = None
    frame_buffer: deque[np.ndarray] = deque(maxlen=E003_BUFFER_MAX)

    # ── E004: 안전 격자 기반 위험체 추적 상태
    # key: "{row}_{col}_{class}" → list of tracked items
    grid_st: dict[str, list[dict]] = {}

    # ── 루프 상태
    last_frame: Optional[np.ndarray] = None
    frame_count = 0
    switch = True  # True=Detection, False=Pose

    logger.info(
        "[SafetyAI:%s] Process started. detection=%s pose=%s roi_count=%d grid_cells=%s",
        camera_id, detection_is_run, pose_is_run,
        len(roi_arr) if roi_arr else 0,
        f"{len(safety_grid)}rows" if safety_grid else "none",
    )

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
        now_ms    = int(now * 1000)
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")
        detections: list[dict] = []
        events:     list[dict] = []

        # E003 프레임 버퍼 (스킵 후 적재)
        frame_buffer.append(frame.copy())

        # ══════════════════════════════════════════════════════════════════════
        # [A] Detection 모델 (switch = True)
        # ══════════════════════════════════════════════════════════════════════
        if detection_is_run and switch:
            results = model_safety.track(frame, persist=True, verbose=False)

            person_boxes: list[list[int]] = []
            hazard_items: list[dict] = []

            for result in results:
                boxes = result.boxes
                if boxes is None:
                    continue

                for i, (box, cls, conf) in enumerate(
                    zip(boxes.xyxy, boxes.cls, boxes.conf)
                ):
                    x1, y1, x2, y2 = map(int, box)
                    cls_idx  = int(cls)
                    conf_val = float(conf)
                    cls_name = get_class_name(ModelType.SAFETY, cls_idx) or "unknown"

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
                            logger.info("[SafetyAI:%s] E001 안전모 미착용 감지 conf=%.3f", camera_id, conf_val)

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
                                logger.info("[SafetyAI:%s] E002 위험구역 출입 감지 conf=%.3f", camera_id, conf_val)

                    # ── E004: 위험체 수집 (격자 기반 추적용)
                    elif cls_name in ("forklift", "hoist") and conf_val >= CONF_THR:
                        hazard_items.append({
                            "class":  cls_name,
                            "center": (cx, cy),
                        })

            # ── E004: 안전 격자 기반 위험체 접근 감지
            if safety_grid and hazard_items:
                _update_grid_state(grid_st, hazard_items, safety_grid, now_ms)
                _detect_e004_grid(
                    grid_st=grid_st,
                    person_boxes=person_boxes,
                    safety_grid=safety_grid,
                    frame=frame,
                    camera_id=camera_id,
                    timestamp=timestamp,
                    now=now,
                    now_ms=now_ms,
                    event_queue=event_queue,
                    last_fire=last_fire,
                    events=events,
                    cooldown_sec=COOLDOWN_SEC,
                )
            elif safety_grid and not hazard_items:
                # 위험체 미검출 시 stale 항목 정리
                _cleanup_stale(grid_st, now_ms)

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
                            logger.info("[SafetyAI:%s] E003 작업자 쓰러짐 감지 sustain=%.1fs", camera_id, COLLAPSE_SUSTAIN)
                        collapse_start_time = None  # 발화 후 초기화
                else:
                    collapse_start_time = None  # 감지 끊기면 초기화

            if detection_is_run:
                switch = True

        # ── WebSocket 결과 전송
        _send_result(result_mp_queue, camera_id, timestamp, detections, events)

    logger.info("[SafetyAI:%s] Process stopped.", camera_id)


# ── 안전 격자 기반 E004 ───────────────────────────────────────────────────────

def _convert_grid_coords(data: list) -> list:
    """OpenCV 컨투어 스타일 (5차 중첩) → 일반 2D 좌표 (4차 중첩)로 변환.

    DB 저장 형식: data[row][col][point][0] = [x, y]
    변환 후:      data[row][col][point] = [x, y]
    """
    if not data:
        return []
    # 이미 4차 중첩인지 확인 (첫 번째 셀의 첫 번째 포인트가 리스트이고 길이 2면 변환 불필요)
    try:
        sample = data[0][0][0]
        if isinstance(sample, (list, tuple)) and len(sample) == 1:
            # 5차 중첩 → 4차로 변환
            return [
                [
                    [point[0] for point in polygon]
                    for polygon in row
                ]
                for row in data
            ]
        # 이미 4차 중첩
        return data
    except (IndexError, TypeError):
        return data


def _point_in_polygon(polygon: list, point: tuple) -> bool:
    """점이 다각형 내부에 있으면 True."""
    poly = np.array(polygon, dtype=np.int32).reshape(-1, 1, 2)
    return cv2.pointPolygonTest(poly, point, False) >= 0


def _find_grid_cell(safety_grid: list, point: tuple) -> tuple[int, int, list] | None:
    """점이 속하는 격자 셀을 찾아 (row_idx, col_idx, polygon) 반환."""
    for row_idx, row in enumerate(safety_grid):
        for col_idx, cell_polygon in enumerate(row):
            if len(cell_polygon) != 4:
                continue
            if _point_in_polygon(cell_polygon, point):
                return row_idx, col_idx, cell_polygon
    return None


def _arrays_equal(a: list, b: list) -> bool:
    """두 폴리곤 좌표가 동일한지 비교."""
    try:
        arr_a = np.array(a, dtype=np.int32).reshape(-1, 2)
        arr_b = np.array(b, dtype=np.int32).reshape(-1, 2)
        return arr_a.shape == arr_b.shape and np.array_equal(arr_a, arr_b)
    except (ValueError, TypeError):
        return False


def _update_grid_state(
    grid_st: dict[str, list[dict]],
    hazard_items: list[dict],
    safety_grid: list,
    now_ms: int,
) -> None:
    """안전 격자 기반 위험체 추적 상태를 업데이트한다.

    Flask의 grid_st_update 로직을 FastAPI에 맞게 포팅.
    """
    # 1. 신규 검출 → 격자 셀에 매핑
    new_by_cell: dict[str, list[dict]] = {}
    for det in hazard_items:
        cx, cy = det["center"]
        cls_name = det["class"]
        cell = _find_grid_cell(safety_grid, (cx, cy))
        if cell is None:
            continue
        row_idx, col_idx, cell_polygon = cell
        key = f"{row_idx}_{col_idx}_{cls_name}"
        if key not in new_by_cell:
            new_by_cell[key] = []
        new_by_cell[key].append({
            "center": (cx, cy),
            "ms": now_ms,
            "grid_polygon": cell_polygon,
        })

    # 2. 기존 추적 항목과 매칭 + 벡터 계산
    for key, new_items in new_by_cell.items():
        if key not in grid_st:
            # 신규 트랙 생성
            grid_st[key] = [{
                "now_center": item["center"],
                "now_ms": item["ms"],
                "now_grid_polygon": item["grid_polygon"],
                "predicted_center_2s": None,
                "predicted_grid_polygon_2s": None,
                "creation_time": item["ms"],
            } for item in new_items]
        else:
            # 기존 트랙과 최근접 매칭
            remaining = list(new_items)
            for old_item in grid_st[key]:
                if not remaining:
                    break
                # 최근접 신규 검출 찾기
                min_dist = float("inf")
                min_idx = 0
                for i, new_item in enumerate(remaining):
                    d = math.hypot(
                        old_item["now_center"][0] - new_item["center"][0],
                        old_item["now_center"][1] - new_item["center"][1],
                    )
                    if d < min_dist:
                        min_dist = d
                        min_idx = i

                matched = remaining.pop(min_idx)

                # 2초 예측 계산
                prev_pt = old_item["now_center"]
                prev_ms = old_item["now_ms"]
                cur_pt = matched["center"]
                cur_ms = matched["ms"]
                dt_ms = max(cur_ms - prev_ms, 1)
                vx = (cur_pt[0] - prev_pt[0]) / dt_ms  # px/ms
                vy = (cur_pt[1] - prev_pt[1]) / dt_ms
                horizon_ms = 2000  # 2초
                pred_center = (
                    int(cur_pt[0] + vx * horizon_ms),
                    int(cur_pt[1] + vy * horizon_ms),
                )

                # 예측 위치의 격자 셀 찾기
                pred_cell = _find_grid_cell(safety_grid, pred_center)
                pred_grid_polygon = pred_cell[2] if pred_cell else None

                # 상태 업데이트
                old_item["now_center"] = cur_pt
                old_item["now_ms"] = cur_ms
                old_item["predicted_center_2s"] = pred_center
                old_item["predicted_grid_polygon_2s"] = pred_grid_polygon

            # 매칭 안 된 신규 항목 추가
            for item in remaining:
                grid_st[key].append({
                    "now_center": item["center"],
                    "now_ms": item["ms"],
                    "now_grid_polygon": item["grid_polygon"],
                    "predicted_center_2s": None,
                    "predicted_grid_polygon_2s": None,
                    "creation_time": item["ms"],
                })

    # 3. Stale 항목 정리
    _cleanup_stale(grid_st, now_ms)


def _cleanup_stale(grid_st: dict[str, list[dict]], now_ms: int, stale_ms: int = 300) -> None:
    """일정 시간 미갱신된 추적 항목을 제거한다."""
    to_delete = []
    for key, items in grid_st.items():
        items[:] = [item for item in items if (now_ms - item["now_ms"]) <= stale_ms]
        if not items:
            to_delete.append(key)
    for key in to_delete:
        del grid_st[key]


def _detect_e004_grid(
    *,
    grid_st: dict[str, list[dict]],
    person_boxes: list[list[int]],
    safety_grid: list,
    frame: np.ndarray,
    camera_id: str,
    timestamp: str,
    now: float,
    now_ms: int,
    event_queue: mp.Queue,
    last_fire: dict[str, float],
    events: list[dict],
    cooldown_sec: float,
) -> None:
    """안전 격자 기반 E004 위험체 접근 이벤트 감지.

    로직:
    1. 위험체의 2초 후 예측 격자가 현재 격자와 다른 경우 (이동 중)
    2. 예측 격자 내에 person 중심점이 존재하는지 확인
    3. 해당 위험체가 3초 이내에 감지된 것인지 확인 (정지체 제외)
    4. 조건 충족 시 E004 발화
    """
    if not person_boxes:
        return

    for key, items in grid_st.items():
        for item in items:
            pred_polygon = item.get("predicted_grid_polygon_2s")
            cur_polygon = item.get("now_grid_polygon")

            if pred_polygon is None or cur_polygon is None:
                continue

            # 예측 격자와 현재 격자가 같으면 skip (정지 또는 같은 셀 내 이동)
            if _arrays_equal(pred_polygon, cur_polygon):
                continue

            # 위험체가 3초 이내에 감지된 것인지 확인 (정지체 제외)
            if (now_ms - item["creation_time"]) > 3000:
                continue

            # 예측 격자 내에 person 존재 확인
            person_in_zone = False
            for pb in person_boxes:
                px1, py1, px2, py2 = pb
                person_center = ((px1 + px2) // 2, (py1 + py2) // 2)
                if _point_in_polygon(pred_polygon, person_center):
                    person_in_zone = True
                    break

            if person_in_zone:
                if _can_fire(last_fire, "E004", now, cooldown_sec):
                    img_path = save_event_image(frame, camera_id, "collision")
                    _put_event(event_queue, camera_id, "collision", img_path, timestamp)
                    _set_fire(last_fire, "E004", now)
                    trigger_relay()
                    cls_name = key.rsplit("_", 1)[-1] if "_" in key else "hazard"
                    events.append({
                        "type": "E004",
                        "message": f"작업자-{cls_name} 위험체 접근 감지",
                    })
                    logger.info(
                        "[SafetyAI:%s] E004 위험체 접근 감지 (격자 기반) hazard=%s",
                        camera_id, cls_name,
                    )
                return  # 한 프레임에 한 번만 발화


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
