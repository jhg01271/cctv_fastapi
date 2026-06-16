"""RTSP 영상에서 프레임을 계속 읽어 AI가 볼 frame_queue에 넣는 파일.

흐름에서의 위치:
  1. core/ai/process_manager.py가 카메라별 reader 프로세스로 rtsp_reader_process()를 실행한다.
  2. 이 파일은 OpenCV VideoCapture로 원본 RTSP 또는 MediaMTX RTSP 주소에 연결한다.
  3. 프레임을 읽을 때마다 frame_queue에 넣고, 큐가 가득 차면 오래된 프레임을 버린다.
  4. 연결이 끊기면 재연결을 시도해서 AI 프로세스가 다시 프레임을 받을 수 있게 한다.

다음에 볼 파일:
  - service/safety/processor.py: frame_queue에서 최신 프레임을 꺼내 Detection/Pose 모델을 돌린다.
"""

from __future__ import annotations

import multiprocessing as mp
import os
import time

import cv2

from core.logging.logger import get_logger

logger = get_logger(__name__)

# MediaMTX 재연결 시도 간격(초) — MediaMTX 자체 장애 시에만 동작
RECONNECT_DELAY = 5
# 재연결 간격 최대값(초) — 지수 백오프 상한
RECONNECT_MAX_DELAY = 30
# 큐가 가득 찼을 때 대기 시간(초)
QUEUE_PUT_TIMEOUT = 1


def _open_capture(stream_url: str) -> cv2.VideoCapture | None:
    """MediaMTX RTSP URL에 연결하고 VideoCapture를 반환한다. 실패 시 None을 반환한다."""
    # OpenCV가 내부적으로 쓰는 FFmpeg 옵션이다. RTSP 연결이 오래 멈추지 않도록 TCP와 짧은 timeout을 강제한다.
    os.environ.setdefault(
        "OPENCV_FFMPEG_CAPTURE_OPTIONS",
        "rtsp_transport;tcp|stimeout;5000000|max_delay;500000",
    )
    cap = cv2.VideoCapture(stream_url, cv2.CAP_FFMPEG)
    if cap.isOpened():
        # OpenCV 내부 버퍼도 작게 유지해서 AI가 몇 초 전 영상이 아니라 최신 영상에 가깝게 추론한다.
        cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
        return cap
    cap.release()
    return None


def _reconnect(stream_url: str, stop_event: "mp.Event | None" = None) -> cv2.VideoCapture | None:
    """MediaMTX 연결을 성공할 때까지 무한 재시도한다. stop_event가 set되면 중단."""
    attempt = 0
    delay = RECONNECT_DELAY
    while True:
        if stop_event is not None and stop_event.is_set():
            logger.info("[RTSP] Stop requested during reconnect. url=%s", stream_url)
            return None

        attempt += 1
        logger.warning("[RTSP] Reconnect attempt %d url=%s (delay=%.0fs)", attempt, stream_url, delay)
        time.sleep(delay)

        cap = _open_capture(stream_url)
        if cap is not None:
            logger.info("[RTSP] Reconnected successfully after %d attempts. url=%s", attempt, stream_url)
            return cap

        # 지수 백오프: 5 → 10 → 20 → 30(상한)
        delay = min(delay * 2, RECONNECT_MAX_DELAY)


def rtsp_reader_process(
    stream_url: str,
    frame_queue: mp.Queue,
    stop_event: mp.Event,
    last_frame_at: mp.Value | None = None,
) -> None:
    """MediaMTX 경유 RTSP 스트림을 읽어 frame_queue에 프레임을 공급하는 프로세스 함수.

    Args:
        stream_url: MediaMTX RTSP 주소 (예: rtsp://localhost:8554/cam01)
        frame_queue: AI 처리 프로세스와 공유하는 프레임 큐
        stop_event: 외부에서 프로세스 종료를 요청하는 이벤트
        last_frame_at: 마지막 프레임 수신 시각 (워치독 모니터링용, epoch seconds)
    """
    cap = _open_capture(stream_url)
    if cap is None:
        logger.error("[RTSP] Initial connection failed. url=%s", stream_url)
        cap = _reconnect(stream_url, stop_event)
        if cap is None:
            return

    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = int(cap.get(cv2.CAP_PROP_FPS)) or 25
    logger.info("[RTSP] Connected. url=%s resolution=%dx%d fps=%d", stream_url, width, height, fps)

    try:
        while not stop_event.is_set():
            ret, frame = cap.read()

            if not ret:
                logger.warning("[RTSP] Frame read failed. Attempting reconnect. url=%s", stream_url)
                cap.release()
                cap = _reconnect(stream_url, stop_event)
                if cap is None:
                    break
                continue

            # 큐가 가득 차면 가장 오래된 프레임을 버리고 최신 프레임 유지
            if frame_queue.full():
                try:
                    frame_queue.get_nowait()
                except Exception:
                    pass

            try:
                frame_queue.put(frame, timeout=QUEUE_PUT_TIMEOUT)
                if last_frame_at is not None:
                    last_frame_at.value = time.time()
            except Exception:
                pass

    except Exception as e:
        logger.error("[RTSP] Fatal error in reader. url=%s error=%s", stream_url, e)
    finally:
        if cap is not None:
            cap.release()
        logger.info("[RTSP] Stream closed. url=%s", stream_url)


def get_stream_info(stream_url: str) -> dict | None:
    """RTSP 스트림의 해상도와 FPS 정보를 반환한다. 연결 실패 시 None을 반환한다."""
    cap = _open_capture(stream_url)
    if cap is None:
        return None
    info = {
        "width": int(cap.get(cv2.CAP_PROP_FRAME_WIDTH)),
        "height": int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)),
        "fps": int(cap.get(cv2.CAP_PROP_FPS)) or 25,
    }
    cap.release()
    return info
