"""공정률 DB 접근 로직."""

from __future__ import annotations

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from config.config import settings
from core.utils.formatters.datetime import format_datetime
from service.cctv.model import Camera
from service.grid.model import CameraProcessGrid
from service.progress.model import TwinDetection, TwinImage


def count_results(db: Session, camera_id: str | None = None) -> int:
    """공정률 결과 전체 건수를 반환한다."""
    stmt = select(func.count()).select_from(TwinImage)
    if camera_id:
        stmt = stmt.where(TwinImage.camera_id == camera_id)
    row = db.execute(stmt).scalar()
    return int(row) if row else 0


def fetch_latest_results(
    db: Session,
    camera_id: str | None = None,
    offset: int = 0,
    size: int = 20,
) -> list[dict]:
    """공정률 결과 이미지 이력을 최신순으로 페이지 단위로 조회한다."""
    stmt = select(TwinImage)
    if camera_id:
        stmt = stmt.where(TwinImage.camera_id == camera_id)
    stmt = stmt.order_by(TwinImage.created_at.desc()).offset(offset).limit(size)

    rows = db.scalars(stmt).all()
    return [
        {
            "image_id":   r.image_id,
            "camera_id":  r.camera_id,
            "image_name": r.image_name,
            "created_at": format_datetime(r.created_at),
        }
        for r in rows
    ]


def save_result_record(db: Session, camera_id: str, image_path: str, timestamp: str) -> int | None:
    """공정률 결과 이미지 경로를 tb_twin_image에 저장하고 생성된 PK를 반환한다."""
    record = TwinImage(
        camera_id=camera_id,
        image_name=image_path,
        created_at=timestamp,
    )
    db.add(record)
    db.flush()
    return record.image_id


def save_detection_records(
    db: Session,
    result_id: int,
    camera_id: str,
    detections: list[dict],
    timestamp: str,
) -> None:
    """격자별 감지 결과를 tb_twin_detection에 저장한다."""
    if not detections:
        return

    for idx, d in enumerate(detections, start=1):
        det = TwinDetection(
            image_id=result_id,
            detection_id=idx,
            object_label=d.get("class"),
            grid_width=d.get("grid_width"),
            grid_height=d.get("grid_height"),
            detected_row=d.get("row"),
            detected_col=d.get("col"),
            created_at=timestamp,
            order_no=d.get("order_no"),
        )
        db.add(det)


def fetch_grid_coordinates(db: Session, camera_id: str) -> list | None:
    """tb_camera_grid에서 격자 좌표를 조회한다."""
    grid = db.get(CameraProcessGrid, camera_id)
    if grid and grid.grid_data:
        return grid.grid_data if isinstance(grid.grid_data, list) else None
    return None


def fetch_progress_cameras(db: Session) -> list[dict]:
    """공정률 처리 대상 카메라 목록을 조회한다."""
    stmt = (
        select(Camera)
        .where(Camera.jit_only == True)  # noqa: E712
        .order_by(Camera.camera_id)
    )
    rows = db.scalars(stmt).all()
    return [
        {
            "camera_id":      r.camera_id,
            "rtsp_url":       f"{settings.MEDIA_SERVER_RTSP_URL}/{r.camera_id}",
            "sort_direction": "right",
        }
        for r in rows
    ]
