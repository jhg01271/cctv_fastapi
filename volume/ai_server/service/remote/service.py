"""AI 프로세스 원격 제어 비즈니스 로직을 정의한다."""

from __future__ import annotations

import threading

import httpx
from sqlalchemy.orm import Session

from config.config import settings
from core.database.session import SessionLocal
from core.ai.process_manager import get_manager
from core.exception.custom_exception import BadRequestException
from core.logging.helpers import log_event
from core.logging.logger import get_logger
from service.cctv.model import Camera
from service.cctv.repository import fetch_all_cameras, fetch_running_cameras, update_camera_run_state
from service.safety.repository import fetch_roi, fetch_detection_flags, fetch_safety_grid

logger = get_logger(__name__)

_GATEWAY_TIMEOUT = 10.0
_run_all_lock = threading.Lock()
_run_all_running = False


# ──────────────────────────────────────────────────────────────
# 내부 헬퍼
# ──────────────────────────────────────────────────────────────

def _fetch_camera_list(db: Session) -> list[dict]:
    """카메라 기본 정보 목록을 조회한다."""
    rows = fetch_all_cameras(db)
    return [{"camera_id": r.camera_id, "rtsp_addr": r.rtsp_addr, "camera_nm": r.camera_nm} for r in rows]


def count_cameras(db: Session) -> int:
    """등록된 카메라 수를 반환한다."""
    return len(_fetch_camera_list(db))


def update_camera_run_state_only(db: Session, camera_id: str, is_running: bool) -> None:
    """카메라 실행 상태(pid)만 즉시 업데이트한다."""
    update_camera_run_state(db, camera_id, is_running)


def _stream_gateway_base_url() -> str | None:
    """설정된 stream gateway URL을 반환한다."""
    url = settings.STREAM_GATEWAY_URL.strip().rstrip("/")
    return url or None


def _start_stream(camera_id: str, rtsp_url: str) -> None:
    """stream gateway로 MediaMTX publish를 시작한다."""
    gateway_url = _stream_gateway_base_url()
    if gateway_url is None:
        raise BadRequestException(msg="STREAM_GATEWAY_URL 설정이 필요합니다.")

    try:
        resp = httpx.post(
            f"{gateway_url}/streams/{camera_id}/start",
            json={"rtsp_url": rtsp_url},
            timeout=_GATEWAY_TIMEOUT,
        )
    except httpx.RequestError as exc:
        raise BadRequestException(
            msg=f"Stream gateway 연결 실패. camera_id={camera_id}",
            data={"gateway_url": gateway_url},
            cause=exc,
        ) from exc

    if resp.status_code == 409:
        logger.info("Stream already publishing: camera_id=%s", camera_id)
        return
    if resp.status_code >= 400:
        raise BadRequestException(
            msg=f"Stream gateway 스트림 등록 실패. camera_id={camera_id}",
            data={"status_code": resp.status_code, "body": resp.text},
        )


def _stop_stream(camera_id: str) -> None:
    """stream gateway로 MediaMTX publish를 중지한다."""
    gateway_url = _stream_gateway_base_url()
    if gateway_url is None:
        raise BadRequestException(msg="STREAM_GATEWAY_URL 설정이 필요합니다.")

    try:
        resp = httpx.delete(
            f"{gateway_url}/streams/{camera_id}",
            timeout=_GATEWAY_TIMEOUT,
        )
    except httpx.RequestError as exc:
        raise BadRequestException(
            msg=f"Stream gateway 연결 실패. camera_id={camera_id}",
            data={"gateway_url": gateway_url},
            cause=exc,
        ) from exc

    if resp.status_code >= 400:
        raise BadRequestException(
            msg=f"Stream gateway 스트림 중지 실패. camera_id={camera_id}",
            data={"status_code": resp.status_code, "body": resp.text},
        )


def _register_camera(camera_id: str, rtsp_url: str, db: Session, jit_only: bool = False) -> dict:
    """카메라를 AI 매니저에 등록하고 프로세스를 시작한다."""
    from core.ai.ws_bridge import ws_bridge
    from service.safety.worker import event_queue

    manager = get_manager()

    existing = [c for c in manager.list_cameras() if c["camera_id"] == camera_id]
    if existing:
        _start_stream(camera_id, rtsp_url)
        return {"camera_id": camera_id, "status": "already_started"}

    _start_stream(camera_id, rtsp_url)

    if jit_only:
        from core.ai.jit_processor import jit_process

        manager.add_camera(camera_id, rtsp_url, ai_target=jit_process)
        log_event(
            logger=logger,
            level="INFO",
            event_type="camera.register",
            message="Camera JIT image collection started",
            camera_id=camera_id,
            rtsp_url=rtsp_url,
        )
        return {"camera_id": camera_id, "status": "jit_started"}

    roi_arr = fetch_roi(db, camera_id)
    detection_is_run, pose_is_run = fetch_detection_flags(db, camera_id)
    safety_grid_arr = fetch_safety_grid(db, camera_id)

    manager.add_camera(
        camera_id,
        rtsp_url,
        result_mp_queue=ws_bridge.mp_queue,
        event_queue=event_queue,
        roi_arr=roi_arr,
        detection_is_run=detection_is_run,
        pose_is_run=pose_is_run,
        safety_grid_arr=safety_grid_arr,
    )
    log_event(
        logger=logger,
        level="INFO",
        event_type="camera.register",
        message="Camera AI process started",
        camera_id=camera_id,
        rtsp_url=rtsp_url,
    )
    return {"camera_id": camera_id, "status": "started"}


def _remove_camera(camera_id: str) -> dict:
    """카메라 AI 프로세스를 종료하고 등록을 해제한다."""
    manager = get_manager()

    existing = [c for c in manager.list_cameras() if c["camera_id"] == camera_id]
    if not existing:
        _stop_stream(camera_id)
        return {"camera_id": camera_id, "status": "stopped"}

    manager.remove_camera(camera_id)
    _stop_stream(camera_id)
    log_event(
        logger=logger,
        level="INFO",
        event_type="camera.remove",
        message="Camera AI process stopped",
        camera_id=camera_id,
    )
    return {"camera_id": camera_id, "status": "stopped"}


# ──────────────────────────────────────────────────────────────
# 공개 API
# ──────────────────────────────────────────────────────────────

def run_cctv(camera_id: str, db: Session) -> dict:
    """개별 카메라 AI 프로세스를 시작한다."""
    camera = db.get(Camera, camera_id)

    if not camera or not camera.rtsp_addr:
        return {"camera_id": camera_id, "status": "error", "message": "카메라 또는 RTSP 주소를 찾을 수 없습니다."}

    try:
        result = _register_camera(camera_id, camera.rtsp_addr, db, jit_only=camera.jit_only)
        update_camera_run_state(db, camera_id, True)
        return {"camera_id": camera_id, "status": "started", **result}
    except Exception:
        update_camera_run_state(db, camera_id, False)
        raise


def run_cctv_background(camera_id: str) -> None:
    """개별 카메라 AI 프로세스 시작을 백그라운드에서 수행한다."""
    logger.info("run_cctv_background started: camera_id=%s", camera_id)
    db = SessionLocal()
    try:
        result = run_cctv(camera_id, db)
        logger.info("run_cctv_background completed: camera_id=%s result=%s", camera_id, result)
    except Exception:
        logger.exception("Background run_cctv failed: camera_id=%s", camera_id)
    finally:
        db.close()


def stop_cctv(camera_id: str, db: Session | None = None) -> dict:
    """개별 카메라 AI 프로세스를 중지한다."""
    result = _remove_camera(camera_id)
    if db is not None:
        update_camera_run_state(db, camera_id, False)
    return {"camera_id": camera_id, "status": "stopped", **result}


def stop_cctv_background(camera_id: str) -> None:
    """개별 카메라 AI 프로세스 중지를 백그라운드에서 수행한다."""
    db = SessionLocal()
    try:
        stop_cctv(camera_id, db)
    except Exception:
        logger.exception("Background stop_cctv failed: camera_id=%s", camera_id)
    finally:
        db.close()


def run_all(db: Session) -> dict:
    """전체 카메라 AI 프로세스를 시작한다."""
    cameras = _fetch_camera_list(db)
    started = []
    skipped = []
    failed = []

    for cam in cameras:
        camera_id = cam["camera_id"]
        rtsp_url = cam.get("rtsp_addr")
        if not rtsp_url:
            failed.append({"camera_id": camera_id, "reason": "RTSP 주소 없음"})
            continue
        try:
            camera = db.get(Camera, camera_id)
            is_jit = camera.jit_only if camera else False
            if not is_jit:
                detection_is_run, pose_is_run = fetch_detection_flags(db, camera_id)
                if not detection_is_run and not pose_is_run:
                    skipped.append({"camera_id": camera_id, "reason": "AI detection/pose disabled"})
                    continue
            _register_camera(camera_id, rtsp_url, db, jit_only=is_jit)
            update_camera_run_state(db, camera_id, True)
            started.append(camera_id)
        except Exception as e:
            logger.error("Failed to start camera %s: %s", camera_id, e)
            update_camera_run_state(db, camera_id, False)
            failed.append({"camera_id": camera_id, "reason": str(e)})

    return {"started": started, "skipped": skipped, "failed": failed, "total": len(cameras)}


def mark_run_all_started() -> bool:
    """전체 시작 백그라운드 작업을 실행 중으로 표시한다. 이미 실행 중이면 False를 반환한다."""
    global _run_all_running
    with _run_all_lock:
        if _run_all_running:
            return False
        _run_all_running = True
        return True


def run_all_background() -> None:
    """새 DB 세션으로 전체 카메라 AI 프로세스 시작 작업을 백그라운드에서 수행한다."""
    global _run_all_running
    db = SessionLocal()
    try:
        result = run_all(db)
        log_event(
            logger=logger,
            level="INFO",
            event_type="camera.run_all.background",
            message="Background run_all finished",
            started=len(result["started"]),
            skipped=len(result["skipped"]),
            failed=len(result["failed"]),
            total=result["total"],
        )
    except Exception:
        logger.exception("Background run_all failed")
    finally:
        db.close()
        with _run_all_lock:
            _run_all_running = False


def check_pid() -> dict:
    """현재 실행 중인 AI 프로세스 상태를 반환한다."""
    manager = get_manager()
    cameras = manager.list_cameras() if manager else []
    return {"cameras": cameras, "total": len(cameras)}


def get_play_url(camera_id: str) -> dict:
    """카메라의 WebRTC 재생 URL을 반환한다."""
    from config.config import settings
    url = f"http://{settings.MEDIA_SERVER_HOST}:{settings.MEDIA_SERVER_WEBRTC_PORT}/{camera_id}"
    return {"camera_id": camera_id, "play_url": url}


def count_running_cameras() -> int:
    """현재 실행 중인 카메라 수를 반환한다."""
    manager = get_manager()
    return len(manager.list_cameras()) if manager else 0


def stop_all(db: Session | None = None) -> dict:
    """전체 카메라 AI 프로세스를 중지한다."""
    manager = get_manager()
    camera_ids = [c["camera_id"] for c in manager.list_cameras()] if manager else []
    stopped = []

    for camera_id in camera_ids:
        try:
            _remove_camera(camera_id)
            stopped.append(camera_id)
        except Exception as e:
            logger.error("Failed to stop camera %s: %s", camera_id, e)

    if db is not None:
        for camera in fetch_running_cameras(db):
            update_camera_run_state(db, camera.camera_id, False)

    return {"stopped": stopped, "total": len(stopped)}


def stop_all_background() -> None:
    """전체 카메라 AI 프로세스 중지를 백그라운드에서 수행한다."""
    db = SessionLocal()
    try:
        result = stop_all(db)
        log_event(
            logger=logger,
            level="INFO",
            event_type="camera.stop_all.background",
            message="Background stop_all finished",
            stopped=len(result["stopped"]),
            total=result["total"],
        )
    except Exception:
        logger.exception("Background stop_all failed")
    finally:
        db.close()


def restore_running_cameras() -> dict:
    """DB 기준 실행 상태인 카메라를 서버 시작 시 복구한다."""
    db = SessionLocal()
    restored = []
    skipped = []
    failed = []
    try:
        cameras = fetch_running_cameras(db)
        for camera in cameras:
            try:
                if not camera.rtsp_addr:
                    skipped.append({"camera_id": camera.camera_id, "reason": "RTSP 주소 없음"})
                    continue
                _register_camera(camera.camera_id, camera.rtsp_addr, db, jit_only=camera.jit_only)
                restored.append(camera.camera_id)
            except Exception as exc:
                logger.error("Failed to restore camera %s: %s", camera.camera_id, exc)
                failed.append({"camera_id": camera.camera_id, "reason": str(exc)})

        log_event(
            logger=logger,
            level="INFO",
            event_type="camera.restore",
            message="DB running cameras restored",
            restored=len(restored),
            skipped=len(skipped),
            failed=len(failed),
            total=len(cameras),
        )
        return {"restored": restored, "skipped": skipped, "failed": failed, "total": len(cameras)}
    finally:
        db.close()


def restore_running_cameras_background() -> None:
    """서버 시작 시 실행 상태 카메라 복구를 백그라운드에서 수행한다."""
    try:
        restore_running_cameras()
    except Exception:
        logger.exception("Background camera restore failed")
