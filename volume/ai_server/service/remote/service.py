"""AI 프로세스 원격 제어 비즈니스 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import select
from sqlalchemy.orm import Session

from core.ai.media_server import add_stream_path, remove_stream_path
from core.ai.process_manager import get_manager
from core.exception.custom_exception import BadRequestException, ConflictException, NotFoundException
from core.logging.helpers import log_event
from core.logging.logger import get_logger
from service.cctv.model import Camera
from service.safety.repository import fetch_roi, fetch_detection_flags

logger = get_logger(__name__)


# ──────────────────────────────────────────────────────────────
# 내부 헬퍼
# ──────────────────────────────────────────────────────────────

def _fetch_camera_list(db: Session) -> list[dict]:
    """카메라 기본 정보 목록을 조회한다."""
    stmt = select(Camera).order_by(Camera.camera_id)
    rows = db.scalars(stmt).all()
    return [{"camera_id": r.camera_id, "rtsp_addr": r.rtsp_addr, "camera_nm": r.camera_nm} for r in rows]


def _register_camera(camera_id: str, rtsp_url: str, db: Session) -> dict:
    """카메라를 AI 매니저에 등록하고 프로세스를 시작한다."""
    from core.ai.ws_bridge import ws_bridge
    from service.safety.worker import event_queue

    manager = get_manager()

    existing = [c for c in manager.list_cameras() if c["camera_id"] == camera_id]
    if existing:
        raise ConflictException(msg=f"이미 등록된 카메라입니다. camera_id={camera_id}")

    if not add_stream_path(camera_id, rtsp_url):
        raise BadRequestException(msg=f"MediaMTX 스트림 등록 실패. camera_id={camera_id}")

    roi_arr = fetch_roi(db, camera_id)
    detection_is_run, pose_is_run = fetch_detection_flags(db, camera_id)

    manager.add_camera(
        camera_id,
        rtsp_url,
        result_mp_queue=ws_bridge.mp_queue,
        event_queue=event_queue,
        roi_arr=roi_arr,
        detection_is_run=detection_is_run,
        pose_is_run=pose_is_run,
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
        raise NotFoundException(msg=f"등록되지 않은 카메라입니다. camera_id={camera_id}")

    manager.remove_camera(camera_id)
    remove_stream_path(camera_id)
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

    result = _register_camera(camera_id, camera.rtsp_addr, db)
    return {"camera_id": camera_id, "status": "started", **result}


def stop_cctv(camera_id: str) -> dict:
    """개별 카메라 AI 프로세스를 중지한다."""
    result = _remove_camera(camera_id)
    return {"camera_id": camera_id, "status": "stopped", **result}


def run_all(db: Session) -> dict:
    """전체 카메라 AI 프로세스를 시작한다."""
    cameras = _fetch_camera_list(db)
    started = []
    failed = []

    for cam in cameras:
        camera_id = cam["camera_id"]
        rtsp_url = cam.get("rtsp_addr")
        if not rtsp_url:
            failed.append({"camera_id": camera_id, "reason": "RTSP 주소 없음"})
            continue
        try:
            _register_camera(camera_id, rtsp_url, db)
            started.append(camera_id)
        except Exception as e:
            logger.error("Failed to start camera %s: %s", camera_id, e)
            failed.append({"camera_id": camera_id, "reason": str(e)})

    return {"started": started, "failed": failed, "total": len(cameras)}


def stop_all() -> dict:
    """전체 카메라 AI 프로세스를 중지한다."""
    manager = get_manager()
    camera_ids = list(manager.workers.keys()) if manager else []
    stopped = []

    for camera_id in camera_ids:
        try:
            _remove_camera(camera_id)
            stopped.append(camera_id)
        except Exception as e:
            logger.error("Failed to stop camera %s: %s", camera_id, e)

    return {"stopped": stopped, "total": len(stopped)}
