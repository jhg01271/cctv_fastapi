"""CCTV 카메라 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from datetime import datetime
from typing import Any

from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.cctv.repository import (
    delete_camera,
    fetch_cameras_by_comp,
    fetch_cameras_by_server,
    update_camera_run_state,
    upsert_camera,
)


def _sync_running_display(db: Session, cameras: list[Camera]) -> list[Camera]:
    """실제 실행 중인 카메라를 목록 응답의 실행 상태에 반영한다."""
    try:
        from core.ai.process_manager import get_manager

        running_ids = {camera["camera_id"] for camera in get_manager().list_cameras()}
    except Exception:
        running_ids = set()

    for camera in cameras:
        if camera.camera_id in running_ids and not camera.pid:
            update_camera_run_state(db, camera.camera_id, True)
            camera.pid = "1"
    return cameras


def list_cameras_by_comp(db: Session, comp_id: str) -> list[Camera]:
    """회사별 카메라 목록을 조회한다."""
    return _sync_running_display(db, fetch_cameras_by_comp(db, comp_id))


def get_camera(db: Session, camera_id: str) -> Camera:
    """단일 카메라 정보를 조회한다."""
    from core.exception.custom_exception import NotFoundException
    camera = db.get(Camera, camera_id)
    if not camera:
        raise NotFoundException(msg=f"카메라를 찾을 수 없습니다. id={camera_id}")
    return camera


def list_cameras_by_server(db: Session, ai_server_id: str) -> list[Camera]:
    """AI 서버별 카메라 목록을 조회한다."""
    return _sync_running_display(db, fetch_cameras_by_server(db, ai_server_id))


def get_server_by_camera(db: Session, camera_id: str) -> dict:
    """카메라 ID로 연결된 AI 서버 정보를 조회한다."""
    from core.exception.custom_exception import NotFoundException
    from service.server.model import AiServer

    camera = db.get(Camera, camera_id)
    if not camera or not camera.ai_server_id:
        raise NotFoundException(msg=f"카메라 또는 서버 정보를 찾을 수 없습니다. camera_id={camera_id}")

    server = db.get(AiServer, camera.ai_server_id)
    if not server:
        raise NotFoundException(msg=f"AI 서버를 찾을 수 없습니다. server_id={camera.ai_server_id}")

    return {
        "server_host": server.server_host,
        "api_port": server.api_port,
        "mediamtx_port": server.mtx_port,
        "cctv_id": camera_id,
    }


def _save_camera_item(db: Session, data: dict[str, Any]) -> Camera:
    """카메라 1건을 등록하거나 수정한다."""
    now = datetime.now()

    existing = db.get(Camera, data.get("camera_id")) if data.get("camera_id") else None

    if existing:
        for key, value in data.items():
            if key not in ("created_at", "created_by") and value is not None:
                setattr(existing, key, value)
        existing.updated_at = now
        return upsert_camera(db, existing)

    camera = Camera(
        camera_id=data.get("camera_id", ""),
        comp_id=data.get("comp_id", ""),
        camera_nm=data.get("camera_nm", ""),
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


def save_camera(db: Session, data: dict[str, Any] | list[dict[str, Any]]) -> list[Camera]:
    """카메라 1건 또는 여러 건을 등록하거나 수정한다."""
    if isinstance(data, list):
        return [_save_camera_item(db, item) for item in data]
    return [_save_camera_item(db, data)]


def remove_camera(db: Session, camera_id: str) -> bool:
    """카메라를 삭제한다."""
    return delete_camera(db, camera_id)
