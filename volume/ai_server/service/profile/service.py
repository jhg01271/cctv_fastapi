"""모니터링 프로필(레이아웃) 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy.orm import Session

from service.profile.model import MonitoringLayout
from service.profile.repository import fetch_layouts_by_comp, fetch_layouts_by_group, upsert_layout, delete_layout


def list_profiles(db: Session, comp_id: str) -> list[MonitoringLayout]:
    """회사별 레이아웃 목록을 조회한다."""
    return fetch_layouts_by_comp(db, comp_id)


def list_group_details(db: Session, monitoring_grp_id: str) -> list[MonitoringLayout]:
    """그룹별 레이아웃 상세를 조회한다."""
    return fetch_layouts_by_group(db, monitoring_grp_id)


def save_profile(db: Session, data: dict) -> MonitoringLayout:
    """레이아웃을 등록하거나 수정한다."""
    now = datetime.now()
    monitoring_grp_id = data.get("monitoring_grp_id")
    item_idx = data.get("item_idx")

    existing = db.get(MonitoringLayout, (monitoring_grp_id, item_idx)) if monitoring_grp_id and item_idx else None

    if existing:
        for key, value in data.items():
            if key not in ("created_at", "created_by") and value is not None:
                setattr(existing, key, value)
        existing.updated_at = now
        return upsert_layout(db, existing)

    layout = MonitoringLayout(
        monitoring_grp_id=monitoring_grp_id,
        item_idx=item_idx,
        coordinate_x=data.get("coordinate_x", 0),
        coordinate_y=data.get("coordinate_y", 0),
        item_width=data.get("item_width", 15),
        item_height=data.get("item_height", 6),
        camera_id=data["camera_id"],
        sort=data.get("sort"),
        title=data.get("title"),
        created_at=now,
        created_by=data.get("created_by", "system"),
    )
    return upsert_layout(db, layout)


def remove_profile(db: Session, data: dict) -> bool:
    """레이아웃을 삭제한다."""
    return delete_layout(db, data.get("monitoring_grp_id"), data.get("item_idx"))
