"""카메라 이벤트 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from pathlib import Path

from sqlalchemy.orm import Session

from core.exception.custom_exception import NotFoundException
from service.event.repository import fetch_event_detail, fetch_events, fetch_events_by_group, update_remark


def get_event(db: Session, data: dict) -> dict:
    """단일 이벤트 상세를 조회한다."""
    event = fetch_event_detail(db, data.get("event_time"), data.get("camera_id"))
    if not event:
        raise NotFoundException(msg="이벤트를 찾을 수 없습니다.")
    return event


def list_events(db: Session, data: dict) -> list[dict]:
    """이벤트 이력을 조회한다."""
    return fetch_events(
        db,
        camera_id=data.get("camera_id"),
        start_date=data.get("start_date"),
        end_date=data.get("end_date"),
    )


def list_events_by_group(db: Session, data: dict) -> list[dict]:
    """그룹별 이벤트 이력을 조회한다."""
    return fetch_events_by_group(
        db,
        group_id=data.get("group_id") or data.get("monitoring_grp_id"),
        start_date=data.get("start_date"),
        end_date=data.get("end_date"),
    )


def get_event_file(db: Session, data: dict) -> str:
    """이벤트 이미지 파일 경로를 반환한다."""
    event = fetch_event_detail(db, data.get("event_time"), data.get("camera_id"))
    if not event or not event.file_path:
        raise NotFoundException(msg="이벤트 이미지를 찾을 수 없습니다.")

    if not Path(event.file_path).exists():
        raise NotFoundException(msg=f"이미지 파일이 존재하지 않습니다. path={event.file_path}")

    return event.file_path


def save_remark(db: Session, data: dict) -> dict:
    """이벤트 비고를 수정한다."""
    updated = update_remark(
        db,
        event_time=data.get("event_time"),
        camera_id=data.get("camera_id"),
        remark=data.get("remark", ""),
    )
    return {"updated": updated}
