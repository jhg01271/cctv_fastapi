"""MediaMTX REST API 연동 — FFmpeg 트랜스코딩을 통한 스트림 등록/삭제."""

from __future__ import annotations

import subprocess
import threading
import time

import httpx

from config.config import settings
from core.logging.helpers import log_event
from core.logging.logger import get_logger

logger = get_logger(__name__)

_TIMEOUT = 5.0
_MAX_RESTART_ATTEMPTS = 5
_RESTART_BASE_DELAY = 3.0  # 초기 재시작 대기 (초)

# camera_id → FFmpeg subprocess 매핑
_ffmpeg_procs: dict[str, subprocess.Popen] = {}
_ffmpeg_lock = threading.Lock()
# camera_id → rtsp_url 매핑 (재시작 시 필요)
_ffmpeg_rtsp_urls: dict[str, str] = {}
# 명시적 중지 요청된 카메라 (재시작 방지)
_stopped_cameras: set[str] = set()


def _api_url(path: str) -> str:
    """MediaMTX API 엔드포인트 URL을 생성한다."""
    return f"{settings.MEDIA_SERVER_API_URL}/v3/config/paths/{path}"


def _is_ffmpeg_running(camera_id: str) -> bool:
    """해당 카메라의 FFmpeg 프로세스가 현재 살아있는지 확인한다."""
    with _ffmpeg_lock:
        proc = _ffmpeg_procs.get(camera_id)
    return bool(proc and proc.poll() is None)


def _register_path(camera_id: str) -> bool:
    """MediaMTX에 빈 path를 등록한다 (소스 없이, publisher 대기 모드)."""
    url = _api_url(f"add/{camera_id}")
    payload = {"sourceOnDemand": False}

    try:
        resp = httpx.post(url, json=payload, timeout=_TIMEOUT)
        if resp.status_code in (200, 201):
            return True
        if resp.status_code == 400 and "path already exists" in resp.text:
            logger.info("[MediaServer] Existing path found. Recreating path. camera=%s", camera_id)
            remove_stream_path(camera_id)
            retry_resp = httpx.post(url, json=payload, timeout=_TIMEOUT)
            if retry_resp.status_code in (200, 201):
                return True
            resp = retry_resp
        logger.warning(
            "[MediaServer] Failed to add path. camera=%s status=%d body=%s",
            camera_id, resp.status_code, resp.text,
        )
        return False
    except httpx.RequestError as e:
        logger.error("[MediaServer] Connection error on add. camera=%s error=%s", camera_id, e)
        return False


def _start_ffmpeg(camera_id: str, rtsp_url: str) -> bool:
    """FFmpeg 프로세스를 시작하여 RTSP → H264 트랜스코딩 후 MediaMTX에 push한다."""
    output_url = f"{settings.MEDIA_SERVER_RTSP_URL}/{camera_id}"

    cmd = [
        "ffmpeg",
        "-rtsp_transport", "tcp",
        "-i", rtsp_url,
        "-vf", "scale=1280:720",
        "-r", "10",
        "-c:v", "libx264",
        "-preset", "ultrafast",
        "-tune", "zerolatency",
        "-g", "10",
        "-an",
        "-f", "rtsp",
        "-rtsp_transport", "tcp",
        output_url,
    ]

    try:
        proc = subprocess.Popen(
            cmd,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.PIPE,
        )
        with _ffmpeg_lock:
            _ffmpeg_procs[camera_id] = proc
            _ffmpeg_rtsp_urls[camera_id] = rtsp_url
            _stopped_cameras.discard(camera_id)

        # 별도 스레드에서 stderr 모니터링 + 재시작
        threading.Thread(
            target=_monitor_ffmpeg,
            args=(camera_id, proc),
            name=f"ffmpeg-mon-{camera_id}",
            daemon=True,
        ).start()

        log_event(
            logger=logger,
            level="INFO",
            event_type="media_server.ffmpeg_start",
            message="FFmpeg transcoding started",
            camera_id=camera_id,
            output_url=output_url,
        )
        return True
    except Exception as e:
        logger.error("[MediaServer] FFmpeg start failed. camera=%s error=%s", camera_id, e)
        return False


def _monitor_ffmpeg(camera_id: str, proc: subprocess.Popen) -> None:
    """FFmpeg 프로세스를 감시하고, 종료 시 자동 재시작한다.

    stderr를 비블로킹으로 읽으면서 프로세스 종료를 poll로 감지한다.
    """
    import select as _select

    stderr_fd = proc.stderr

    while proc.poll() is None:
        if camera_id in _stopped_cameras:
            return
        # stderr에 읽을 데이터가 있으면 읽기 (최대 1초 대기)
        try:
            ready, _, _ = _select.select([stderr_fd], [], [], 1.0)
        except (ValueError, OSError):
            break  # pipe 닫힘
        if ready:
            try:
                line = stderr_fd.readline()
                if not line:
                    break  # EOF — 프로세스 종료
                decoded = line.decode("utf-8", errors="replace").strip()
                if "error" in decoded.lower() or "fatal" in decoded.lower():
                    logger.warning("[FFmpeg:%s] %s", camera_id, decoded)
            except Exception:
                break

    # 프로세스 종료 확인
    exit_code = proc.wait()

    if camera_id in _stopped_cameras:
        return

    logger.warning(
        "[FFmpeg:%s] Process exited (code=%s). Attempting restart...",
        camera_id, exit_code,
    )

    for attempt in range(1, _MAX_RESTART_ATTEMPTS + 1):
        if camera_id in _stopped_cameras:
            return

        delay = _RESTART_BASE_DELAY * (2 ** (attempt - 1))  # 3, 6, 12, 24, 48초
        time.sleep(delay)

        if camera_id in _stopped_cameras:
            return

        rtsp_url = _ffmpeg_rtsp_urls.get(camera_id)
        if not rtsp_url:
            logger.error("[FFmpeg:%s] No RTSP URL for restart. Giving up.", camera_id)
            return

        logger.info("[FFmpeg:%s] Restart attempt %d/%d", camera_id, attempt, _MAX_RESTART_ATTEMPTS)
        if _start_ffmpeg(camera_id, rtsp_url):
            logger.info("[FFmpeg:%s] Restart successful on attempt %d", camera_id, attempt)
            return

    logger.error(
        "[FFmpeg:%s] All %d restart attempts failed. Manual intervention required.",
        camera_id, _MAX_RESTART_ATTEMPTS,
    )


def _stop_ffmpeg(camera_id: str) -> None:
    """FFmpeg 프로세스를 종료한다."""
    with _ffmpeg_lock:
        _stopped_cameras.add(camera_id)
        proc = _ffmpeg_procs.pop(camera_id, None)
        _ffmpeg_rtsp_urls.pop(camera_id, None)

    if proc and proc.poll() is None:
        proc.terminate()
        try:
            proc.wait(timeout=5)
        except subprocess.TimeoutExpired:
            proc.kill()
        log_event(
            logger=logger,
            level="INFO",
            event_type="media_server.ffmpeg_stop",
            message="FFmpeg transcoding stopped",
            camera_id=camera_id,
        )


def add_stream_path(camera_id: str, rtsp_url: str) -> bool:
    """카메라 스트림을 FFmpeg H264 트랜스코딩으로 MediaMTX에 등록한다.

    1. MediaMTX에 빈 path 등록 (publisher 대기)
    2. FFmpeg로 RTSP → H264 변환 후 MediaMTX에 push
    """
    if _is_ffmpeg_running(camera_id):
        logger.info("[MediaServer] FFmpeg already running. camera=%s", camera_id)
        return True

    if not _register_path(camera_id):
        return False

    if not _start_ffmpeg(camera_id, rtsp_url):
        return False

    log_event(
        logger=logger,
        level="INFO",
        event_type="media_server.path_add",
        message="MediaMTX path added with FFmpeg transcoding",
        camera_id=camera_id,
    )
    return True


def remove_stream_path(camera_id: str) -> bool:
    """MediaMTX에서 카메라 스트림 path를 제거하고 FFmpeg를 종료한다."""
    _stop_ffmpeg(camera_id)

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
    """프론트엔드(브라우저)에서 사용할 스트림 URL들을 반환한다."""
    ext_host = settings.MEDIA_SERVER_EXTERNAL_HOST
    return {
        "webrtc": f"http://{ext_host}:{settings.MEDIA_SERVER_WEBRTC_PORT}/{camera_id}",
        "rtsp": f"rtsp://{ext_host}:{settings.MEDIA_SERVER_RTSP_PORT}/{camera_id}",
    }
