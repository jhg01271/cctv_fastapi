"""Stream gateway business logic."""

from __future__ import annotations

from core.ai.media_server import add_stream_path, get_stream_urls, remove_stream_path
from core.exception.custom_exception import BadRequestException
from core.logging.logger import get_logger

logger = get_logger(__name__)


def start_stream(camera_id: str, rtsp_url: str) -> dict:
    """Start publishing a camera stream to MediaMTX."""
    ok = add_stream_path(camera_id, rtsp_url)
    if not ok:
        logger.warning("[StreamGateway] Stream start failed. camera=%s", camera_id)
        raise BadRequestException(msg=f"stream start failed: {camera_id}")
    return {
        "camera_id": camera_id,
        "status": "started",
        "urls": get_stream_urls(camera_id),
    }


def stop_stream(camera_id: str) -> dict:
    """Stop publishing a camera stream to MediaMTX."""
    ok = remove_stream_path(camera_id)
    if not ok:
        logger.info("[StreamGateway] Stream was already stopped or missing. camera=%s", camera_id)
    return {"camera_id": camera_id, "status": "stopped"}
