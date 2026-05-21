"""격자 DB 접근 레이어."""

from __future__ import annotations

from datetime import datetime
from typing import TypeVar

from sqlalchemy.orm import Session

from service.grid.model import CameraProcessGrid, CameraSafetyGrid

T = TypeVar("T", CameraProcessGrid, CameraSafetyGrid)


# ---------------------------------------------------------------------------
# 공통 헬퍼
# ---------------------------------------------------------------------------

def _upsert_by_camera_id(db: Session, model: type[T], camera_id: str, data: dict) -> T:
    """camera_id 기준으로 격자 레코드를 upsert 한다."""
    existing = db.get(model, camera_id)
    now = datetime.now()

    if existing:
        for key, value in data.items():
            if key != "camera_id":
                setattr(existing, key, value)
        existing.updated_at = now
        db.commit()
        db.refresh(existing)
        return existing

    record = model(camera_id=camera_id, created_at=now, updated_at=now, **data)
    db.add(record)
    db.commit()
    db.refresh(record)
    return record


# ---------------------------------------------------------------------------
# 공정률 격자 (tb_camera_grid)
# ---------------------------------------------------------------------------

def fetch_grid_by_camera(db: Session, camera_id: str) -> CameraProcessGrid | None:
    """카메라 ID로 공정률 격자를 조회한다."""
    return db.get(CameraProcessGrid, camera_id)


def upsert_grid(db: Session, camera_id: str, data: dict) -> CameraProcessGrid:
    """공정률 격자를 저장 또는 업데이트한다."""
    return _upsert_by_camera_id(db, CameraProcessGrid, camera_id, data)


# ---------------------------------------------------------------------------
# 안전 격자 (tb_camera_safety_grid)
# ---------------------------------------------------------------------------

def fetch_safety_grid_by_camera(db: Session, camera_id: str) -> CameraSafetyGrid | None:
    """카메라 ID로 안전 격자를 조회한다."""
    return db.get(CameraSafetyGrid, camera_id)


def upsert_safety_grid(db: Session, camera_id: str, data: dict) -> CameraSafetyGrid:
    """안전 격자를 저장 또는 업데이트한다."""
    return _upsert_by_camera_id(db, CameraSafetyGrid, camera_id, data)
