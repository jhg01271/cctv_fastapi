"""MediaMTX REST API 연동 — 스트림 path 동적 등록/삭제."""

from __future__ import annotations

import httpx

from config.config import settings
from core.logging.helpers import log_event
from core.logging.logger import get_logger

logger = get_logger(__name__)

# MediaMTX API 타임아웃 (초)
_TIMEOUT = 5.0


def _api_url(path: str) -> str:
    """MediaMTX API 엔드포인트 URL을 생성한다."""
    return f"{settings.MEDIA_SERVER_API_URL}/v3/config/paths/{path}"


def add_stream_path(camera_id: str, rtsp_url: str) -> bool:
    """MediaMTX에 카메라 스트림 path를 등록한다.

    Args:
        camera_id: path 이름으로 사용할 카메라 ID
        rtsp_url: 원본 카메라 RTSP URL (MediaMTX가 소스로 연결)

    Returns:
        성공 여부
    """
    url = _api_url(f"add/{camera_id}")
    payload = {"source": rtsp_url}

    try:
        resp = httpx.post(url, json=payload, timeout=_TIMEOUT)
        if resp.status_code in (200, 201):
            log_event(
                logger=logger,
                level="INFO",
                event_type="media_server.path_add",
                message="MediaMTX path added",
                camera_id=camera_id,
            )
            return True
        logger.warning(
            "[MediaServer] Failed to add path. camera=%s status=%d body=%s",
            camera_id, resp.status_code, resp.text,
        )
        return False
    except httpx.RequestError as e:
        logger.error("[MediaServer] Connection error on add. camera=%s error=%s", camera_id, e)
        return False


def remove_stream_path(camera_id: str) -> bool:
    """MediaMTX에서 카메라 스트림 path를 제거한다.

    Args:
        camera_id: 제거할 path 이름

    Returns:
        성공 여부
    """
    url = _api_url(f"delete/{camera_id}")

    try:
        resp = httpx.delete(url, timeout=_TIMEOUT)
        if resp.status_code in (200, 204):
            log_event(
                logger=logger,
                level="INFO",
                event_type="media_server.path_remove",
                message="MediaMTX path removed",
                camera_id=camera_id,
            )
            return True
        logger.warning(
            "[MediaServer] Failed to remove path. camera=%s status=%d body=%s",
            camera_id, resp.status_code, resp.text,
        )
        return False
    except httpx.RequestError as e:
        logger.error("[MediaServer] Connection error on remove. camera=%s error=%s", camera_id, e)
        return False


def get_stream_urls(camera_id: str) -> dict:
    """프론트엔드에서 사용할 스트림 URL들을 반환한다."""
    host = settings.MEDIA_SERVER_HOST
    return {
        "webrtc": f"http://{host}:{settings.MEDIA_SERVER_WEBRTC_PORT}/{camera_id}",
        "rtsp": f"rtsp://{host}:{settings.MEDIA_SERVER_RTSP_PORT}/{camera_id}",
    }
