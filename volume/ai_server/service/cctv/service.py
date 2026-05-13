"""CCTV 카메라 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.cctv.repository import fetch_cameras_by_comp, fetch_cameras_by_server, upsert_camera, delete_camera


def list_cameras_by_comp(db: Session, comp_id: str) -> list[Camera]:
    """회사별 카메라 목록을 조회한다."""
    return fetch_cameras_by_comp(db, comp_id)


def list_cameras_by_server(db: Session, ai_server_id: str) -> list[Camera]:
    """AI 서버별 카메라 목록을 조회한다."""
    return fetch_cameras_by_server(db, ai_server_id)


def save_camera(db: Session, data: dict) -> Camera:
    """카메라를 등록하거나 수정한다."""
    now = datetime.now()

    existing = db.get(Camera, data.get("camera_id")) if data.get("camera_id") else None

    if existing:
        for key, value in data.items():
            if key not in ("created_at", "created_by") and value is not None:
                setattr(existing, key, value)
        existing.updated_at = now
        return upsert_camera(db, existing)

    camera = Camera(
        camera_id=data["camera_id"],
        comp_id=data["comp_id"],
        camera_nm=data["camera_nm"],
        camera_desc=data.get("camera_desc"),
        ai_server_id=data.get("ai_server_id"),
        rtsp_addr=data.get("rtsp_addr"),
        out_path=data.get("out_path"),
        pid=data.get("pid"),
        remark=data.get("remark"),
        jit_only=data.get("jit_only", False),
        port_number=data.get("port_number"),
        location=data.get("location"),
        created_at=now,
        created_by=data.get("created_by", "system"),
    )
    return upsert_camera(db, camera)


def remove_camera(db: Session, camera_id: str) -> bool:
    """카메라를 삭제한다."""
    return delete_camera(db, camera_id)
