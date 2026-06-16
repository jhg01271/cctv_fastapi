"""
================================================================================
[Repository] service/cctv/repository.py
================================================================================
- 역할: 데이터베이스(PostgreSQL)의 tb_camera 테이블에 직접 쿼리를 실행하는 데이터 접근 레이어입니다.
- 흐름: Service(service.py)의 요청에 따라 SQLAlchemy ORM 쿼리를 생성하여 DB 데이터를 조회/추가/수정/삭제합니다.
- 주요 기능: 회사/서버별 카메라 쿼리, 실행 상태인 카메라 목록 조회, 카메라 상태(PID 등) 갱신, Upsert 및 Delete 실행
================================================================================
"""

from __future__ import annotations

from sqlalchemy import select, delete as sa_delete
from sqlalchemy.orm import Session

from service.cctv.model import Camera


def fetch_cameras_by_comp(db: Session, comp_id: str) -> list[Camera]:
    """회사 식별자 기준으로 카메라 목록을 조회한다.
    
    [DB 연동 설명]:
    SQLAlchemy의 select(Camera)를 사용하여 tb_camera(Camera 모델) 테이블로부터
    특정 회사 ID(comp_id)를 가진 카메라 정보 리스트를 조회해 옵니다.
    """
    stmt = select(Camera).where(Camera.comp_id == comp_id).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())


def fetch_cameras_by_server(db: Session, ai_server_id: str) -> list[Camera]:
    """AI 서버 기준으로 카메라 목록을 조회한다."""
    stmt = select(Camera).where(Camera.ai_server_id == ai_server_id).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())


def fetch_all_cameras(db: Session) -> list[Camera]:
    """전체 카메라 목록을 조회한다."""
    stmt = select(Camera).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())


def fetch_running_cameras(db: Session) -> list[Camera]:
    """DB 기준 실행 상태인 카메라 목록을 조회한다."""
    stmt = (
        select(Camera)
        .where(Camera.pid.is_not(None), Camera.pid != "", Camera.rtsp_addr.is_not(None), Camera.rtsp_addr != "")
        .order_by(Camera.camera_id)
    )
    return list(db.scalars(stmt).all())


def update_camera_run_state(db: Session, camera_id: str, is_running: bool) -> bool:
    """카메라 실행 상태 플래그를 갱신한다."""
    camera = db.get(Camera, camera_id)
    if not camera:
        return False
    camera.pid = "1" if is_running else ""
    db.commit()
    return True


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
