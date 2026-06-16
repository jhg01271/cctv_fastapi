"""이벤트 코드 매핑, 증거 이미지/영상 저장, 즉시 릴레이 동작을 모아둔 파일.

흐름에서의 위치:
  1. service/safety/processor.py가 이벤트를 확정할 때 save_event_image() 또는 save_event_clip()을 호출한다.
  2. 이 파일은 이벤트 증거 파일을 EVENT_IMAGE_DIR 아래에 저장하고 경로를 반환한다.
  3. processor.py는 반환된 파일 경로를 event_queue에 넣어 DB 저장까지 이어지게 한다.
  4. E001/E002/E004처럼 즉시 반응이 필요한 이벤트는 processor.py에서 trigger_relay()를 호출한다.

다음에 볼 파일:
  - service/safety/processor.py: 언제 JPG/MP4를 저장하고 relay를 켤지 판단한다.
  - service/safety/repository.py: 저장된 파일 경로를 tb_camera_event_hist.file_path에 기록한다.
"""

from __future__ import annotations

import os
import subprocess
import time
import threading
from datetime import datetime

import cv2
import numpy as np

from config.config import settings
from core.logging.logger import get_logger

logger = get_logger(__name__)

# 이벤트 코드 매핑  key → (code, description)
EVENT_CODE = {
    "no_helmet": ("E001", "안전모 미착용"),
    "person":    ("E002", "위험구역 출입감지"),
    "collapse":  ("E003", "작업자 쓰러짐"),
    "collision": ("E004", "위험체 접근 감지"),
}

# 릴레이 활성화 시간 (초)
RELAY_DURATION_SEC = 20.0

_relay_lock = threading.Lock()
_relay_end_time: float = 0.0


def trigger_relay() -> None:
    """E001/E002 발생 시 릴레이 20초 활성화.

    이미 활성 중이면 무시한다.
    실제 GPIO / 외부 릴레이 제어 코드로 교체할 것.
    """
    global _relay_end_time
    with _relay_lock:
        now = time.time()
        if now < _relay_end_time:
            return
        _relay_end_time = now + RELAY_DURATION_SEC
        # TODO: GPIO.output(RELAY_PIN, GPIO.HIGH)
        # threading.Timer(RELAY_DURATION_SEC, lambda: GPIO.output(RELAY_PIN, GPIO.LOW)).start()


def _event_dir(camera_id: str, event_code: str) -> str:
    """이벤트 저장 디렉토리 생성 후 경로 반환.

    경로: /data/events/{camera_id}/{event_code}/{yyyyMMdd}/
    """
    date_str = datetime.now().strftime("%Y%m%d")
    path = os.path.join(settings.EVENT_IMAGE_DIR, camera_id, event_code, date_str)
    os.makedirs(path, exist_ok=True)
    return path


def save_event_image(frame: np.ndarray, camera_id: str, event_key: str) -> str | None:
    """이벤트 프레임을 jpg로 저장하고 경로를 반환한다."""
    try:
        code, _ = EVENT_CODE.get(event_key, (event_key, event_key))
        dir_path = _event_dir(camera_id, code)
        filename = f"{datetime.now().strftime('%H%M%S_%f')[:13]}.jpg"
        filepath = os.path.join(dir_path, filename)
        cv2.imwrite(filepath, frame)
        logger.info("[EventSave] Image saved. camera=%s event=%s path=%s", camera_id, event_key, filepath)
        return filepath
    except Exception as e:
        logger.error("[EventSave] Image save failed. camera=%s event=%s error=%s", camera_id, event_key, e)
        return None


def save_event_clip(
    frames: list[np.ndarray],
    camera_id: str,
    fps: float = 10.0,
) -> str | None:
    """E003 쓰러짐 프레임 버퍼를 브라우저 재생 가능한 mp4로 저장한다.

    OpenCV가 바로 만드는 mp4는 대개 mp4v 코덱이라 Chrome video 태그에서
    검은 화면으로 멈출 수 있다. 먼저 임시 mp4를 만든 뒤 FFmpeg로 H.264(avc1)
    형식으로 변환해서 History 화면에서 바로 재생되게 한다.
    """
    if not frames:
        return None
    try:
        dir_path = _event_dir(camera_id, "E003")
        filename = f"{datetime.now().strftime('%H%M%S')}.mp4"
        filepath = os.path.join(dir_path, filename)
        temp_filepath = filepath.replace(".mp4", ".raw.mp4")
        h, w = frames[0].shape[:2]
        fourcc = cv2.VideoWriter_fourcc(*"mp4v")
        writer = cv2.VideoWriter(temp_filepath, fourcc, fps, (w, h))
        for f in frames:
            writer.write(f)
        writer.release()

        cmd = [
            "ffmpeg",
            "-y",
            "-i",
            temp_filepath,
            "-an",
            "-c:v",
            "libx264",
            "-preset",
            "veryfast",
            "-pix_fmt",
            "yuv420p",
            "-movflags",
            "+faststart",
            filepath,
        ]
        result = subprocess.run(cmd, capture_output=True, text=True)
        if result.returncode != 0:
            logger.warning(
                "[EventSave] H.264 transcode failed. Falling back to raw mp4. camera=%s error=%s",
                camera_id,
                result.stderr[-500:],
            )
            os.replace(temp_filepath, filepath)
        elif os.path.exists(temp_filepath):
            os.remove(temp_filepath)

        logger.info("[EventSave] Clip saved. camera=%s path=%s frames=%d", camera_id, filepath, len(frames))
        return filepath
    except Exception as e:
        logger.error("[EventSave] Clip save failed. camera=%s error=%s", camera_id, e)
        return None


def check_roi(point: tuple[int, int], roi_arr: list) -> bool:
    """점이 ROI 다각형 내부에 있으면 True를 반환한다."""
    if not roi_arr:
        return False
    poly = np.array(roi_arr, dtype=np.int32)
    return cv2.pointPolygonTest(poly, point, False) >= 0


def check_box_overlap(box1: list[int], box2: list[int]) -> bool:
    """두 bbox [x1,y1,x2,y2]가 겹치면 True를 반환한다."""
    x1_1, y1_1, x2_1, y2_1 = box1
    x1_2, y1_2, x2_2, y2_2 = box2
    return not (x2_1 < x1_2 or x2_2 < x1_1 or y2_1 < y1_2 or y2_2 < y1_1)
