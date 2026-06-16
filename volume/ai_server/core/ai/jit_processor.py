"""jit_only=True 카메라에서 AI 추론 대신 학습/검증용 이미지를 수집하는 파일.

흐름에서의 위치:
  1. service/remote/service.py의 _register_camera()가 카메라 설정의 jit_only 값을 확인한다.
  2. jit_only=True이면 service/safety/processor.py가 아니라 이 파일의 jit_process()가 AI 프로세스로 실행된다.
  3. core/ai/rtsp_reader.py가 넣은 frame_queue에서 프레임을 꺼낸다.
  4. 모델 추론과 이벤트 DB 저장은 하지 않고, 주기 또는 변화 감지 조건에 맞춰 JPG만 저장한다.

저장 주기:
  - short: 10분마다 무조건 저장
  - long:  60분마다 무조건 저장
  - auto:  이전 프레임 대비 변화 감지 시 저장 (최소 1시간 간격)

다음에 볼 파일:
  - service/remote/service.py: 어떤 카메라가 jit_only로 이 파일을 타는지 결정한다.
  - service/safety/processor.py: 일반 AI 이벤트 판단 모드는 이 파일 대신 processor.py를 탄다.
"""

from __future__ import annotations

import multiprocessing as mp
import os
import time
from datetime import datetime

import cv2
import numpy as np

from config.config import settings
from core.logging.logger import get_logger

logger = get_logger(__name__)


def _save_frame(frame: np.ndarray, save_dir: str, camera_id: str) -> str | None:
    """프레임을 jpg로 저장하고 경로를 반환한다."""
    try:
        os.makedirs(save_dir, exist_ok=True)
        filename = f"{datetime.now().strftime('%Y%m%d_%H%M%S')}.jpg"
        filepath = os.path.join(save_dir, filename)
        cv2.imwrite(filepath, frame)
        logger.info("[JIT:%s] Image saved. path=%s", camera_id, filepath)
        return filepath
    except Exception as e:
        logger.error("[JIT:%s] Image save failed. dir=%s error=%s", camera_id, save_dir, e)
        return None


def _detect_change(current: np.ndarray, previous: np.ndarray) -> bool:
    """두 프레임 간 변화를 감지한다.

    320x180 회색조로 리사이즈하여 비교 (연산 최소화).
    조건: 평균 차이 >= threshold OR 변화 픽셀 비율 >= ratio
    """
    small_size = (320, 180)
    curr_gray = cv2.cvtColor(cv2.resize(current, small_size), cv2.COLOR_BGR2GRAY)
    prev_gray = cv2.cvtColor(cv2.resize(previous, small_size), cv2.COLOR_BGR2GRAY)

    diff = cv2.absdiff(curr_gray, prev_gray)
    mean_diff = float(np.mean(diff))
    changed_pixels = np.count_nonzero(diff > 25)
    total_pixels = diff.size
    changed_ratio = changed_pixels / total_pixels

    return (
        mean_diff >= settings.JIT_AUTO_MEAN_DIFF
        or changed_ratio >= settings.JIT_AUTO_CHANGE_RATIO
    )


def jit_process(
    *,
    camera_id: str,
    rtsp_url: str,
    frame_queue: mp.Queue,
    stop_event: mp.Event,
    heartbeat: mp.Value,
    **_kwargs,
) -> None:
    """JIT 이미지 수집 프로세스 메인 루프.

    CameraProcessManager의 ai_target으로 등록된다.
    """
    base_dir = os.path.join(settings.JIT_IMAGE_DIR, camera_id)
    short_dir = os.path.join(base_dir, "short")
    long_dir = os.path.join(base_dir, "long")
    auto_dir = os.path.join(base_dir, "auto")

    last_short_time = 0.0
    last_long_time = 0.0
    last_auto_time = 0.0
    prev_frame: np.ndarray | None = None

    logger.info("[JIT:%s] Process started.", camera_id)

    while not stop_event.is_set():
        try:
            frame = frame_queue.get(timeout=1.0)
        except Exception:
            continue

        heartbeat.value += 1
        now = time.time()

        # 10분 주기 저장
        if now - last_short_time >= settings.JIT_SHORT_INTERVAL_SEC:
            _save_frame(frame, short_dir, camera_id)
            last_short_time = now

        # 60분 주기 저장
        if now - last_long_time >= settings.JIT_LONG_INTERVAL_SEC:
            _save_frame(frame, long_dir, camera_id)
            last_long_time = now

        # 자동 변화 감지 저장
        if prev_frame is not None and now - last_auto_time >= settings.JIT_AUTO_MIN_INTERVAL_SEC:
            if _detect_change(frame, prev_frame):
                _save_frame(frame, auto_dir, camera_id)
                last_auto_time = now

        prev_frame = frame.copy()

    logger.info("[JIT:%s] Process stopped.", camera_id)
