"""MediaMTX 경유 RTSP 스트림에서 프레임을 읽어 큐에 공급하는 프로듀서."""

from __future__ import annotations

import multiprocessing as mp
import time

import cv2

from core.logging.logger import get_logger

logger = get_logger(__name__)

# MediaMTX 재연결 시도 간격(초) — MediaMTX 자체 장애 시에만 동작
RECONNECT_DELAY = 3
# MediaMTX 재연결 최대 시도 횟수
MAX_RECONNECT_RETRIES = 10
# 큐가 가득 찼을 때 대기 시간(초)
QUEUE_PUT_TIMEOUT = 1


def _open_capture(stream_url: str) -> cv2.VideoCapture | None:
    """MediaMTX RTSP URL에 연결하고 VideoCapture를 반환한다. 실패 시 None을 반환한다."""
    cap = cv2.VideoCapture(stream_url, cv2.CAP_FFMPEG)
    if cap.isOpened():
        return cap
    cap.release()
    return None


def _reconnect(stream_url: str) -> cv2.VideoCapture | None:
    """MediaMTX 연결을 재시도한다. 최대 횟수 초과 시 None을 반환한다."""
    for attempt in range(1, MAX_RECONNECT_RETRIES + 1):
        logger.warning(
            "[RTSP] Reconnect attempt %d/%d url=%s",
            attempt, MAX_RECONNECT_RETRIES, stream_url,
        )
        time.sleep(RECONNECT_DELAY)
        cap = _open_capture(stream_url)
        if cap is not None:
            logger.info("[RTSP] Reconnected successfully. url=%s", stream_url)
            return cap
    logger.error("[RTSP] All reconnect attempts failed. url=%s", stream_url)
    return None


def rtsp_reader_process(
    stream_url: str,
    frame_queue: mp.Queue,
    stop_event: mp.Event,
) -> None:
    """MediaMTX 경유 RTSP 스트림을 읽어 frame_queue에 프레임을 공급하는 프로세스 함수.

    Args:
        stream_url: MediaMTX RTSP 주소 (예: rtsp://localhost:8554/cam01)
        frame_queue: AI 처리 프로세스와 공유하는 프레임 큐
        stop_event: 외부에서 프로세스 종료를 요청하는 이벤트
    """
    cap = _open_capture(stream_url)
    if cap is None:
        logger.error("[RTSP] Initial connection failed. url=%s", stream_url)
        cap = _reconnect(stream_url)
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
                cap = _reconnect(stream_url)
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
            except Exception:
                pass

    except Exception as e:
        logger.error("[RTSP] Fatal error in reader. url=%s error=%s", stream_url, e)
    finally:
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
