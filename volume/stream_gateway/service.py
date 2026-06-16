"""Stream gateway business logic."""

from __future__ import annotations

from core.ai.media_server import add_stream_path, get_stream_urls, remove_stream_path, wait_stream_ready
from core.exception.custom_exception import BadRequestException
from core.logging.logger import get_logger

logger = get_logger(__name__)


def start_stream(camera_id: str, rtsp_url: str) -> dict:
    """RTSP 카메라 영상을 브라우저가 볼 수 있는 MediaMTX 채널로 올린다.

    비유하면 stream_gateway는 방송 송출실이다. 원본 RTSP 주소(rtsp_url)를 받아
    FFmpeg를 실행하고, MediaMTX의 /CAM0001 같은 채널에 영상을 밀어 넣는다.
    그 채널이 실제로 준비될 때까지 기다린 뒤 프론트엔드가 사용할 URL을 반환한다.
    """
    ok = add_stream_path(camera_id, rtsp_url)
    if not ok:
        logger.warning("[StreamGateway] Stream start failed. camera=%s", camera_id)
        raise BadRequestException(msg=f"stream start failed: {camera_id}")
    # FFmpeg 프로세스를 시작했다고 해서 브라우저가 즉시 볼 수 있는 것은 아니다.
    # MediaMTX가 해당 camera_id 채널을 ready 상태로 표시할 때까지 짧게 기다린다.
    ready = wait_stream_ready(camera_id)
    if not ready:
        logger.warning("[StreamGateway] Stream started but not ready yet. camera=%s", camera_id)
    return {
        "camera_id": camera_id,
        "ready": ready,
        "status": "started" if ready else "starting",
        "urls": get_stream_urls(camera_id),
    }


def stop_stream(camera_id: str) -> dict:
    """Stop publishing a camera stream to MediaMTX."""
    ok = remove_stream_path(camera_id)
    if not ok:
        logger.info("[StreamGateway] Stream was already stopped or missing. camera=%s", camera_id)
    return {"camera_id": camera_id, "status": "stopped"}
