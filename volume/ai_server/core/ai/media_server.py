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
_HEALTH_CHECK_INTERVAL = 30.0  # health check 주기 (초)
_HEALTH_FAIL_THRESHOLD = 2  # 연속 실패 N회 이상이면 FFmpeg 재시작

# camera_id → FFmpeg subprocess 매핑
_ffmpeg_procs: dict[str, subprocess.Popen] = {}
_ffmpeg_lock = threading.Lock()
# camera_id → rtsp_url 매핑 (재시작 시 필요)
_ffmpeg_rtsp_urls: dict[str, str] = {}
# 명시적 중지 요청된 카메라 (재시작 방지)
_stopped_cameras: set[str] = set()
# health check 스레드 시작 여부
_health_check_started = False
# camera_id → 연속 health check 실패 횟수
_health_fail_counts: dict[str, int] = {}
# health check에서 재시작 중인 카메라 (watch_ffmpeg 재시작 방지)
_restarting_cameras: set[str] = set()


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
            # 이미 등록된 path는 삭제하지 않고 그대로 재사용
            logger.info("[MediaServer] Path already exists, reusing. camera=%s", camera_id)
            return True
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
        # "-vf", "scale=1280:720",
        # "-r", "2",
        # "-c:v", "libx264",
        # "-preset", "ultrafast",
        # "-tune", "zerolatency",
        # "-g", "4",
        "-c:v", "copy",
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

        # stderr 로그 읽기 (별도 스레드)
        threading.Thread(
            target=_read_stderr,
            args=(camera_id, proc),
            name=f"ffmpeg-stderr-{camera_id}",
            daemon=True,
        ).start()
        # 프로세스 감시 + 재시작 (별도 스레드)
        threading.Thread(
            target=_watch_ffmpeg,
            args=(camera_id, proc),
            name=f"ffmpeg-watch-{camera_id}",
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


def _read_stderr(camera_id: str, proc: subprocess.Popen) -> None:
    """FFmpeg stderr를 읽어 에러 로그를 남긴다. (로그 전용, 감시와 무관)"""
    try:
        for line in proc.stderr:
            decoded = line.decode("utf-8", errors="replace").strip()
            if "error" in decoded.lower() or "fatal" in decoded.lower():
                logger.warning("[FFmpeg:%s] %s", camera_id, decoded)
    except Exception:
        pass


def _watch_ffmpeg(camera_id: str, proc: subprocess.Popen) -> None:
    """FFmpeg 프로세스 종료를 감지하고 자동 재시작한다. (proc.wait 기반)"""
    exit_code = proc.wait()  # 프로세스가 끝날 때까지 확실히 대기

    if camera_id in _stopped_cameras:
        return

    # health check에서 이미 재시작 처리 중이면 중복 재시작 방지
    if camera_id in _restarting_cameras:
        logger.info("[FFmpeg:%s] Health check is handling restart. Skipping watch restart.", camera_id)
        return

    logger.warning(
        "[FFmpeg:%s] Process exited (code=%s). Attempting restart...",
        camera_id, exit_code,
    )

    for attempt in range(1, _MAX_RESTART_ATTEMPTS + 1):
        if camera_id in _stopped_cameras or camera_id in _restarting_cameras:
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
    _health_fail_counts.pop(camera_id, None)

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
    3. health check 스레드 자동 시작
    """
    if _is_ffmpeg_running(camera_id):
        logger.info("[MediaServer] FFmpeg already running. camera=%s", camera_id)
        return True

    if not _register_path(camera_id):
        return False

    if not _start_ffmpeg(camera_id, rtsp_url):
        return False

    _ensure_health_check()

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


# ──────────────────────────────────────────────────────────────
# Health Check — MediaMTX에서 스트림 상태를 주기적으로 확인
# ──────────────────────────────────────────────────────────────

def _ensure_health_check() -> None:
    """health check 스레드가 없으면 시작한다."""
    global _health_check_started
    if _health_check_started:
        return
    _health_check_started = True
    threading.Thread(
        target=_health_check_loop,
        name="ffmpeg-health-check",
        daemon=True,
    ).start()
    logger.info("[HealthCheck] Started. interval=%ds", int(_HEALTH_CHECK_INTERVAL))


def _check_stream_ready(camera_id: str) -> bool:
    """MediaMTX API로 해당 카메라 스트림이 실제로 publish 중인지 확인한다."""
    try:
        resp = httpx.get(
            f"{settings.MEDIA_SERVER_API_URL}/v3/paths/list",
            timeout=_TIMEOUT,
        )
        if resp.status_code != 200:
            return True  # API 실패 시 일단 정상으로 간주
        for item in resp.json().get("items") or []:
            if item.get("name") == camera_id:
                return bool(item.get("ready"))
        return False  # path 자체가 없음
    except Exception:
        return True  # 네트워크 오류 시 일단 정상으로 간주


def _health_check_loop() -> None:
    """주기적으로 FFmpeg 스트림 상태를 확인하고, 연속 실패 시 kill 후 재시작한다."""
    while True:
        time.sleep(_HEALTH_CHECK_INTERVAL)

        with _ffmpeg_lock:
            targets = {
                cid: (proc, _ffmpeg_rtsp_urls.get(cid))
                for cid, proc in _ffmpeg_procs.items()
                if cid not in _stopped_cameras
            }

        for camera_id, (proc, rtsp_url) in targets.items():
            if not rtsp_url:
                continue

            # 프로세스가 이미 죽었으면 _watch_ffmpeg가 처리하므로 skip
            if proc.poll() is not None:
                _health_fail_counts.pop(camera_id, None)
                continue

            # MediaMTX에 스트림이 publish 중인지 확인
            if _check_stream_ready(camera_id):
                if camera_id in _health_fail_counts:
                    _health_fail_counts.pop(camera_id, None)
                continue

            # 연속 실패 카운트 증가
            fail_count = _health_fail_counts.get(camera_id, 0) + 1
            _health_fail_counts[camera_id] = fail_count
            logger.warning(
                "[HealthCheck] Stream not ready. camera=%s pid=%d fail_count=%d/%d",
                camera_id, proc.pid, fail_count, _HEALTH_FAIL_THRESHOLD,
            )

            if fail_count < _HEALTH_FAIL_THRESHOLD:
                continue

            # 연속 N회 실패 — FFmpeg kill 후 재시작
            _health_fail_counts.pop(camera_id, None)
            _restarting_cameras.add(camera_id)  # _watch_ffmpeg 중복 재시작 방지
            logger.warning(
                "[HealthCheck] Consecutive failures reached threshold. Killing FFmpeg. camera=%s pid=%d",
                camera_id, proc.pid,
            )
            proc.kill()
            proc.wait()

            # path 재등록 + FFmpeg 재시작
            _register_path(camera_id)
            if _start_ffmpeg(camera_id, rtsp_url):
                logger.info("[HealthCheck] FFmpeg restarted. camera=%s", camera_id)
            else:
                logger.error("[HealthCheck] FFmpeg restart failed. camera=%s", camera_id)
            _restarting_cameras.discard(camera_id)
