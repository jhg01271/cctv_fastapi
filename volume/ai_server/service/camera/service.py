"""camera 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy.orm import Session

from core.ai.media_server import add_stream_path, get_stream_urls as _get_stream_urls, remove_stream_path
from core.ai.process_manager import get_manager
from core.exception.custom_exception import BadRequestException, ConflictException, NotFoundException
from core.logging.helpers import log_event
from core.logging.logger import get_logger
from core.utils.formatters.datetime import format_datetime
from service.camera import repository
from service.camera.schema import CameraGroupRead, CameraStatusResponse


SUPER_COMPANY_PREFIX = "IGNS"
logger = get_logger(__name__)


# ──────────────────────────────────────────────────────────────
# 카메라 그룹 조회
# ──────────────────────────────────────────────────────────────

def list_camera_groups(db: Session, user_cd: str) -> list[CameraGroupRead]:
    """사용자 코드 기준으로 카메라 그룹 목록을 조회한다."""
    comp_id = parse_company_id(user_cd)
    camera_groups = repository.fetch_camera_groups(db, comp_id)
    return [
        CameraGroupRead(
            comp_id=row.comp_id,
            profile_id=row.monitoring_grp_id,
            profile_name=row.grp_nm,
            created_at=format_datetime(row.created_at),
            created_by=row.created_by,
            updated_at=format_datetime(row.updated_at),
            updated_by=row.updated_by,
        )
        for row in camera_groups
    ]


def parse_company_id(user_cd: str) -> str | None:
    """사용자 코드에서 회사 식별자를 추출한다."""
    prefix = (user_cd or "").split("_", 1)[0].strip()
    if not prefix or prefix == SUPER_COMPANY_PREFIX:
        return None
    return prefix


# ──────────────────────────────────────────────────────────────
# 카메라 프로세스 관리
# ──────────────────────────────────────────────────────────────

def list_camera_status() -> list[CameraStatusResponse]:
    """현재 실행 중인 카메라 프로세스 상태 목록을 반환한다."""
    manager = get_manager()
    return [CameraStatusResponse(**cam) for cam in manager.list_cameras()]


def register_camera(camera_id: str, rtsp_url: str, db: Session) -> dict:
    """카메라를 AI 매니저에 등록하고 프로세스를 시작한다."""
    from core.ai.ws_bridge import ws_bridge
    from service.safety.worker import event_queue

    manager = get_manager()

    existing = [c for c in manager.list_cameras() if c["camera_id"] == camera_id]
    if existing:
        raise ConflictException(msg=f"이미 등록된 카메라입니다. camera_id={camera_id}")

    if not add_stream_path(camera_id, rtsp_url):
        raise BadRequestException(msg=f"MediaMTX 스트림 등록 실패. camera_id={camera_id}")

    roi_arr = repository.fetch_roi(db, camera_id)
    detection_is_run, pose_is_run = repository.fetch_detection_flags(db, camera_id)

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


def remove_camera(camera_id: str) -> dict:
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


def get_stream_urls(camera_id: str) -> dict:
    """프론트엔드에서 사용할 카메라 스트림 URL을 반환한다."""
    manager = get_manager()
    existing = [c for c in manager.list_cameras() if c["camera_id"] == camera_id]
    if not existing:
        raise NotFoundException(msg=f"등록되지 않은 카메라입니다. camera_id={camera_id}")
    return _get_stream_urls(camera_id)
