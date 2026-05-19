"""격자 DB 접근 레이어."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy.orm import Session

from service.grid.model import CameraGrid, CameraSafetyGrid


# ---------------------------------------------------------------------------
# 공정률 격자 (tb_camera_grid)
# ---------------------------------------------------------------------------

def fetch_grid_by_camera(db: Session, camera_id: str) -> CameraGrid | None:
    """카메라 ID로 공정률 격자를 조회한다."""
    return db.get(CameraGrid, camera_id)


def upsert_grid(db: Session, camera_id: str, data: dict) -> CameraGrid:
    """공정률 격자를 저장 또는 업데이트한다."""
    existing = db.get(CameraGrid, camera_id)
    now = datetime.now()

    if existing:
        for key, value in data.items():
            if key != "camera_id":
                setattr(existing, key, value)
        existing.updated_at = now
        db.commit()
        db.refresh(existing)
        return existing

    grid = CameraGrid(camera_id=camera_id, created_at=now, updated_at=now, **data)
    db.add(grid)
    db.commit()
    db.refresh(grid)
    return grid


# ---------------------------------------------------------------------------
# 안전 격자 (tb_camera_safety_grid)
# ---------------------------------------------------------------------------

def fetch_safety_grid_by_camera(db: Session, camera_id: str) -> CameraSafetyGrid | None:
    """카메라 ID로 안전 격자를 조회한다."""
    return db.get(CameraSafetyGrid, camera_id)


def upsert_safety_grid(db: Session, camera_id: str, data: dict) -> CameraSafetyGrid:
    """안전 격자를 저장 또는 업데이트한다."""
    existing = db.get(CameraSafetyGrid, camera_id)
    now = datetime.now()

    if existing:
        for key, value in data.items():
            if key != "camera_id":
                setattr(existing, key, value)
        existing.updated_at = now
        db.commit()
        db.refresh(existing)
        return existing

    grid = CameraSafetyGrid(camera_id=camera_id, created_at=now, updated_at=now, **data)
    db.add(grid)
    db.commit()
    db.refresh(grid)
    return grid
