"""
================================================================================
[Service] service/cctv/service.py
================================================================================
- 역할: CCTV 카메라 도메인의 핵심 비즈니스 로직을 처리하는 서비스 레이어입니다.
- 흐름: Router(routes.py)로부터 데이터를 전달받아 검증, 포맷팅, 캐시 갱신 및 DB 접근(repository.py)을 지시합니다.
- 주요 기능:
  1. 회사/서버별 카메라 목록 조회 및 단일 카메라 정보 추출
  2. 카메라 정보 등록 및 수정 (특히 카메라 정보가 변경될 때 스냅샷 이미지 캐시를 비동기로 갱신하는 트리거 역할 수행)
  3. 카메라 삭제 처리
================================================================================
"""

from __future__ import annotations

from datetime import datetime
from typing import Any

from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.cctv.repository import (
    delete_camera,
    fetch_cameras_by_comp,
    fetch_cameras_by_server,
    upsert_camera,
)


def list_cameras_by_comp(db: Session, comp_id: str) -> list[Camera]:
    """회사별 카메라 목록을 조회한다."""
    return fetch_cameras_by_comp(db, comp_id)


def get_camera(db: Session, camera_id: str) -> Camera:
    """단일 카메라 정보를 조회한다."""
    from core.exception.custom_exception import NotFoundException
    camera = db.get(Camera, camera_id)
    if not camera:
        raise NotFoundException(msg=f"카메라를 찾을 수 없습니다. id={camera_id}")
    return camera


def list_cameras_by_server(db: Session, ai_server_id: str) -> list[Camera]:
    """AI 서버별 카메라 목록을 조회한다."""
    return fetch_cameras_by_server(db, ai_server_id)


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
        cameras = [_save_camera_item(db, item) for item in data]
    else:
        cameras = [_save_camera_item(db, data)]

    # 변경되거나 등록된 카메라들의 실시간 캡처 스냅샷 이미지 캐시 갱신 (비동기 스레드)
    if cameras:
        from service.roi.service import update_cctv_image_cache
        from core.database.session import SessionLocal
        import threading
        
        def update_task(cam_ids):
            db_sync = SessionLocal()
            try:
                for cid in cam_ids:
                    update_cctv_image_cache(db_sync, cid)
            except Exception as e:
                import logging
                logging.getLogger(__name__).error(f"[CCTVImageCacheSync] Failed to update cache: {e}")
            finally:
                db_sync.close()

        threading.Thread(
            target=update_task,
            args=([cam.camera_id for cam in cameras],),
            name="cctv-cache-updater",
            daemon=True
        ).start()

    return cameras


def remove_camera(db: Session, camera_id: str) -> bool:
    """카메라를 삭제한다."""
    return delete_camera(db, camera_id)
