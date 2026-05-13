"""CCTV 카메라 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import select, delete as sa_delete
from sqlalchemy.orm import Session

from service.cctv.model import Camera


def fetch_cameras_by_comp(db: Session, comp_id: str) -> list[Camera]:
    """회사 식별자 기준으로 카메라 목록을 조회한다."""
    stmt = select(Camera).where(Camera.comp_id == comp_id).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())


def fetch_cameras_by_server(db: Session, ai_server_id: str) -> list[Camera]:
    """AI 서버 기준으로 카메라 목록을 조회한다."""
    stmt = select(Camera).where(Camera.ai_server_id == ai_server_id).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())


def upsert_camera(db: Session, camera: Camera) -> Camera:
    """카메라를 등록하거나 수정한다."""
    merged = db.merge(camera)
    db.commit()
    db.refresh(merged)
    return merged


def delete_camera(db: Session, camera_id: str) -> bool:
    """카메라를 삭제한다."""
    result = db.execute(
        sa_delete(Camera).where(Camera.camera_id == camera_id)
    )
    db.commit()
    return result.rowcount > 0
