"""안전관리 DB 접근 로직."""

from __future__ import annotations

import json

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from core.utils.formatters.datetime import format_datetime
from service.event.model import CameraEventHist
from service.roi.model import CameraRoi


def count_event_hist(db: Session, camera_id: str | None = None) -> int:
    """이벤트 이력 전체 건수를 반환한다."""
    stmt = select(func.count()).select_from(CameraEventHist)
    if camera_id:
        stmt = stmt.where(CameraEventHist.camera_id == camera_id)
    row = db.execute(stmt).scalar()
    return int(row) if row else 0


def fetch_event_hist(
    db: Session,
    camera_id: str | None = None,
    offset: int = 0,
    size: int = 20,
) -> list[dict]:
    """이벤트 이력을 최신순으로 페이지 단위로 조회한다."""
    stmt = select(CameraEventHist)
    if camera_id:
        stmt = stmt.where(CameraEventHist.camera_id == camera_id)
    stmt = stmt.order_by(CameraEventHist.event_time.desc()).offset(offset).limit(size)

    rows = db.scalars(stmt).all()
    return [
        {
            "event_time":  format_datetime(r.event_time),
            "camera_id":   r.camera_id,
            "event_type":  r.event_type,
            "event_desc":  r.event_desc,
            "file_path":   r.file_path,
            "isread":      r.isread,
            "remark":      r.remark,
        }
        for r in rows
    ]


def fetch_roi(db: Session, camera_id: str) -> list:
    """카메라의 ROI 좌표 배열을 조회한다."""
    stmt = (
        select(CameraRoi.point)
        .where(CameraRoi.camera_id == camera_id, CameraRoi.is_run == True)  # noqa: E712
        .limit(1)
    )
    row = db.execute(stmt).scalar()
    if row:
        return row if isinstance(row, list) else json.loads(row)
    return []


def fetch_safety_grid(db: Session, camera_id: str) -> list:
    """카메라의 안전 격자 데이터를 조회한다."""
    from service.grid.model import CameraSafetyGrid

    grid = db.get(CameraSafetyGrid, camera_id)
    if grid and grid.grid_data:
        data = grid.grid_data
        if isinstance(data, str):
            data = json.loads(data)
        return data if isinstance(data, list) else []
    return []


def fetch_detection_flags(db: Session, camera_id: str) -> tuple[bool, bool]:
    """detection_is_run, pose_is_run 플래그를 반환한다."""
    stmt = select(CameraRoi.model_nm, CameraRoi.is_run).where(CameraRoi.camera_id == camera_id)
    rows = db.execute(stmt).all()
    detection_is_run = False
    pose_is_run = False
    for model_nm, is_run in rows:
        if model_nm == "Detection":
            detection_is_run = bool(is_run)
        elif model_nm == "Pose":
            pose_is_run = bool(is_run)
    return detection_is_run, pose_is_run


def save_event(
    db: Session,
    camera_id: str,
    event_key: str,
    image_path: str | None,
    timestamp: str,
) -> None:
    """이벤트 이력을 tb_camera_event_hist에 저장한다."""
    from service.safety.events import EVENT_CODE
    code, desc = EVENT_CODE.get(event_key, ("E999", event_key))
    event = CameraEventHist(
        event_time=timestamp,
        camera_id=camera_id,
        event_type=code,
        event_desc=desc,
        file_path=image_path,
        isread=False,
        remark=None,
    )
    db.add(event)
    db.commit()
