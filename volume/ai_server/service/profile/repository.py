"""모니터링 프로필(레이아웃) 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import delete as sa_delete, select
from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.profile.model import MonitoringGrp, MonitoringLayout


def fetch_groups_by_comp(db: Session, comp_id: str) -> list[MonitoringGrp]:
    """회사 식별자 기준으로 모니터링 그룹 목록을 조회한다."""
    stmt = (
        select(MonitoringGrp)
        .where(MonitoringGrp.comp_id == comp_id)
        .order_by(MonitoringGrp.monitoring_grp_id)
    )
    return list(db.scalars(stmt).all())


def fetch_layouts_by_group(db: Session, monitoring_grp_id: str) -> list[tuple[MonitoringLayout, Camera | None]]:
    """그룹 ID 기준으로 레이아웃 목록을 카메라 정보와 함께 조회한다."""
    stmt = (
        select(MonitoringLayout, Camera)
        .outerjoin(Camera, MonitoringLayout.camera_id == Camera.camera_id)
        .where(MonitoringLayout.monitoring_grp_id == monitoring_grp_id)
        .order_by(MonitoringLayout.item_idx)
    )
    return list(db.execute(stmt).all())


def upsert_layout(db: Session, layout: MonitoringLayout) -> MonitoringLayout:
    """레이아웃을 등록하거나 수정한다."""
    merged = db.merge(layout)
    db.commit()
    db.refresh(merged)
    return merged


def delete_layout(db: Session, monitoring_grp_id: str, item_idx: int) -> bool:
    """레이아웃을 삭제한다."""
    result = db.execute(
        sa_delete(MonitoringLayout).where(
            MonitoringLayout.monitoring_grp_id == monitoring_grp_id,
            MonitoringLayout.item_idx == item_idx,
        )
    )
    db.commit()
    return result.rowcount > 0
