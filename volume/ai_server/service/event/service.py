"""카메라 이벤트 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from pathlib import Path

from sqlalchemy.orm import Session

from core.exception.custom_exception import NotFoundException
from service.event.repository import fetch_event_detail, fetch_events, fetch_events_by_group, update_remark

EVENT_TYPE_NAMES: dict[str, str] = {
    "E001": "안전모 미착용",
    "E002": "위험구역 출입",
    "E003": "작업자 쓰러짐",
    "E004": "지게차 접근 감지",
}


def _enrich_event(event) -> dict:
    """ORM 이벤트 객체에 event_type_name을 추가하여 dict로 반환한다."""
    return {
        "event_time": event.event_time,
        "camera_id": event.camera_id,
        "event_type": event.event_type,
        "event_type_name": EVENT_TYPE_NAMES.get(event.event_type, event.event_type or ""),
        "event_desc": event.event_desc,
        "file_path": event.file_path,
        "remark": event.remark,
    }


def get_event(db: Session, data: dict) -> dict:
    """단일 이벤트 상세를 조회한다."""
    event = fetch_event_detail(db, data.get("event_time"), data.get("camera_id") or data.get("cctv_id"))
    if not event:
        raise NotFoundException(msg="이벤트를 찾을 수 없습니다.")
    return _enrich_event(event)


def list_events(db: Session, data: dict) -> list[dict]:
    """이벤트 이력을 조회한다."""
    events = fetch_events(
        db,
        camera_id=data.get("camera_id") or data.get("cctv_id"),
        start_date=data.get("start_date"),
        end_date=data.get("end_date"),
    )
    return [_enrich_event(e) for e in events]


def list_events_by_group(db: Session, data: dict) -> list[dict]:
    """그룹별 이벤트 이력을 조회한다."""
    events = fetch_events_by_group(
        db,
        group_id=data.get("group_id") or data.get("monitoring_grp_id") or data.get("profile_id"),
        start_date=data.get("start_date"),
        end_date=data.get("end_date"),
    )
    return [_enrich_event(e) for e in events]


def get_event_file(db: Session, data: dict) -> str:
    """이벤트 이미지 파일 경로를 반환한다.

    프론트엔드는 { cctv_id, file_path }를 전송한다.
    file_path가 있으면 직접 파일을 반환하고, 없으면 event_time + camera_id로 DB 조회한다.
    """
    file_path = data.get("file_path")

    if file_path:
        p = Path(file_path)
        if not p.exists():
            raise NotFoundException(msg=f"이미지 파일이 존재하지 않습니다. path={file_path}")
        return str(p)

    camera_id = data.get("camera_id") or data.get("cctv_id")
    event_time = data.get("event_time")
    event = fetch_event_detail(db, event_time, camera_id)

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
