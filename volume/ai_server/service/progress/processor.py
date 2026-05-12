"""공정률 AI 처리 프로세스.

JIT(Just-In-Time) 방식: 실시간 스트리밍 없이
PROGRESS_CYCLE_SEC 주기로 카메라 스냅샷 수집 → 배치 추론 → 결과를 큐에 전달한다.

배치 추론: 전체 카메라 프레임을 한 번에 model(batch) 호출 → GPU 활용 극대화
DB 저장: 추론 결과를 mp.Queue에 넣으면 부모 프로세스(FastAPI)가 SQLAlchemy로 저장한다.
"""

from __future__ import annotations

import multiprocessing as mp
import os
import time
from datetime import datetime

import cv2
import numpy as np

from config.config import settings
from core.ai.model_loader import load_model, ModelType

# 스냅샷 캡처 프레임 수 (안정된 프레임 선택용)
SNAPSHOT_FRAMES = 5
# 추론 신뢰도 임계값
CONF_THR = 0.8
# 학습용 자동 저장 주기 (ms)
AUTO_SAVE_INTERVAL_MS = 3_600_000  # 1시간


# ──────────────────────────────────────────────────────────────
# 격자 셀 판정
# ──────────────────────────────────────────────────────────────

def _find_grid_cell(cx: int, cy: int, grid_data: list | None) -> tuple[int, int]:
    """감지 중심점(cx, cy)이 속하는 격자 (row, col)을 반환한다.

    grid_data: [{"row": int, "col": int, "polygon": [[x,y], ...]}, ...]
    grid_data가 없으면 (0, 0), 매칭되는 격자가 없으면 (-1, -1) 반환.
    """
    if not grid_data:
        return 0, 0

    for cell in grid_data:
        polygon = cell.get("polygon")
        if not polygon:
            continue
        pts = np.array(polygon, dtype=np.int32)
        if cv2.pointPolygonTest(pts, (cx, cy), False) >= 0:
            return cell.get("row", -1), cell.get("col", -1)

    return -1, -1


def _extract_detections(result, grid_data: list | None) -> list[dict]:
    """YOLO 결과에서 감지 항목을 추출하고 격자 셀을 판정한다.

    반환: [{"row": int, "col": int, "class": str, "conf": float}, ...]
    """
    if result.boxes is None:
        return []

    detections = []
    for box, cls, conf in zip(result.boxes.xyxy, result.boxes.cls, result.boxes.conf):
        x1, y1, x2, y2 = map(int, box)
        conf_val = round(float(conf), 3)

        if conf_val < CONF_THR:
            continue

        cx = (x1 + x2) // 2
        cy = (y1 + y2) // 2
        row, col = _find_grid_cell(cx, cy, grid_data)

        detections.append({
            "row":   row,
            "col":   col,
            "class": result.names[int(cls)],
            "conf":  conf_val,
        })

    return detections


# ──────────────────────────────────────────────────────────────
# RTSP 스냅샷
# ──────────────────────────────────────────────────────────────

def _capture_frames(rtsp_url: str, count: int = SNAPSHOT_FRAMES) -> list[np.ndarray]:
    """RTSP에서 count개의 프레임을 캡처하고 반환한다."""
    cap = cv2.VideoCapture(rtsp_url, cv2.CAP_FFMPEG)
    if not cap.isOpened():
        print(f"[Progress] RTSP open failed: {rtsp_url}")
        return []
    frames = []
    for _ in range(count):
        ret, frame = cap.read()
        if ret:
            frames.append(frame)
    cap.release()
    return frames


# ──────────────────────────────────────────────────────────────
# 결과 이미지 생성
# ──────────────────────────────────────────────────────────────

def _draw_detections(frame: np.ndarray, result) -> np.ndarray:
    """YOLO 결과를 프레임에 그린다."""
    annotated = frame.copy()
    if result is None or result.boxes is None:
        return annotated
    for box, cls, conf in zip(result.boxes.xyxy, result.boxes.cls, result.boxes.conf):
        x1, y1, x2, y2 = map(int, box)
        label = f"{result.names[int(cls)]} {float(conf):.2f}"
        cv2.rectangle(annotated, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(annotated, label, (x1, y1 - 6),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)
    return annotated


def _save_result_image(
    frame: np.ndarray,
    result,
    camera_id: str,
    timestamp: str,
) -> str | None:
    """추론 결과를 프레임에 그려 저장하고 경로를 반환한다."""
    try:
        os.makedirs(settings.PROGRESS_RESULT_DIR, exist_ok=True)
        annotated = _draw_detections(frame, result)
        filename = f"{camera_id}_{timestamp}.jpg"
        filepath = os.path.join(settings.PROGRESS_RESULT_DIR, filename)
        cv2.imwrite(filepath, annotated)
        return filepath
    except Exception as e:
        print(f"[Progress] Image save failed camera={camera_id}: {e}")
        return None


# ──────────────────────────────────────────────────────────────
# 이미지 변화 감지 (학습용 자동 저장)
# ──────────────────────────────────────────────────────────────

def _is_frame_changed(prev: np.ndarray | None, curr: np.ndarray) -> bool:
    """이전 프레임과 현재 프레임의 차이가 임계값을 넘으면 True."""
    if prev is None:
        return True
    curr_gray = cv2.cvtColor(cv2.resize(curr, (320, 180)), cv2.COLOR_BGR2GRAY)
    prev_gray = cv2.cvtColor(cv2.resize(prev, (320, 180)), cv2.COLOR_BGR2GRAY)
    diff = cv2.absdiff(curr_gray, prev_gray)
    return float(diff.mean()) >= 10.0 or float((diff > 15).mean()) >= 0.05


def _save_training_image(camera_id: str, frame: np.ndarray, timestamp: str) -> None:
    """학습용 이미지를 progress_results/training/ 하위에 저장한다."""
    try:
        out_dir = os.path.join(settings.PROGRESS_RESULT_DIR, "training", camera_id)
        os.makedirs(out_dir, exist_ok=True)
        filepath = os.path.join(out_dir, f"{timestamp}.jpg")
        cv2.imwrite(filepath, frame)
    except Exception as e:
        print(f"[Progress] Training image save failed camera={camera_id}: {e}")


# ──────────────────────────────────────────────────────────────
# 프로세스 진입점
# ──────────────────────────────────────────────────────────────

def progress_process(
    *,
    stop_event: mp.Event,
    heartbeat: mp.Value,
    cameras: list[dict],
    grid_map: dict[str, list | None],
    result_queue: mp.Queue,
) -> None:
    """공정률 AI 처리 메인 루프.

    cameras, grid_map은 FastAPI lifespan에서 조회 후 인자로 전달받는다.
    추론 결과를 result_queue로 전달하고, 저장은 외부 런타임이 담당한다.
    """
    cycle = settings.PROGRESS_CYCLE_SEC
    last_auto_saved: dict[str, int] = {}
    last_auto_frames: dict[str, np.ndarray] = {}

    # 모델을 프로세스 시작 시 1번만 로드
    model = load_model(ModelType.PROGRESS, settings.MODEL_PROGRESS_PT)

    first_run = True

    while not stop_event.is_set():
        if not first_run:
            time.sleep(cycle)
        first_run = False

        now_ms = int(datetime.now().timestamp() * 1000)
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

        # ── Phase 1: 전체 카메라 스냅샷 수집 ─────────────────
        snapshots: list[tuple[dict, np.ndarray, list | None]] = []
        for cam in cameras:
            frames = _capture_frames(cam["rtsp_url"])
            if not frames:
                continue
            grid_data = grid_map.get(cam["camera_id"])
            snapshots.append((cam, frames[0], grid_data))

        if not snapshots:
            heartbeat.value += 1
            continue

        # ── Phase 2: 배치 추론 ────────────────────────────────
        batch_frames = [frame for _, frame, _ in snapshots]
        batch_results = model(batch_frames, verbose=False)

        # ── Phase 3: 결과 이미지 저장 + 결과 전달 ─────────────
        for (cam, frame, grid_data), result in zip(snapshots, batch_results):
            camera_id = cam["camera_id"]
            img_path = _save_result_image(frame, result, camera_id, timestamp)
            if not img_path:
                continue

            detections = _extract_detections(result, grid_data)
            try:
                result_queue.put_nowait({
                    "camera_id": camera_id,
                    "image_path": img_path,
                    "timestamp": timestamp,
                    "detections": detections,
                })
            except Exception:
                pass

        # ── 학습용 자동 저장 (1시간 주기 + 변화 감지) ──────────
        for cam, frame, _ in snapshots:
            camera_id = cam["camera_id"]
            prev_frame = last_auto_frames.get(camera_id)
            last_saved = last_auto_saved.get(camera_id, 0)

            if (now_ms - last_saved) >= AUTO_SAVE_INTERVAL_MS and _is_frame_changed(prev_frame, frame):
                _save_training_image(camera_id, frame, timestamp)
                last_auto_frames[camera_id] = frame.copy()
                last_auto_saved[camera_id] = now_ms

        heartbeat.value += 1
