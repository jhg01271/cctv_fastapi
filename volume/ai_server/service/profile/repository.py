"""모니터링 프로필(레이아웃) 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import text
from sqlalchemy.orm import Session

from service.profile.model import MonitoringLayout


def fetch_layouts_by_comp(db: Session, comp_id: str) -> list[MonitoringLayout]:
    """회사 식별자 기준으로 레이아웃 목록을 조회한다 (그룹 테이블 JOIN)."""
    rows = db.execute(
        text(
            """
            SELECT l.*
            FROM tb_camera_monitoring_layout l
            JOIN tb_camera_monitoring_grp g ON l.monitoring_grp_id = g.monitoring_grp_id
            WHERE g.comp_id = :comp_id
            ORDER BY l.monitoring_grp_id, l.item_idx
            """
        ),
        {"comp_id": comp_id},
    ).fetchall()
    # raw rows → ORM 객체로 변환하지 않고 dict 반환을 위해 scalars 사용
    from sqlalchemy import select
    stmt = (
        select(MonitoringLayout)
        .join(text("tb_camera_monitoring_grp g ON tb_camera_monitoring_layout.monitoring_grp_id = g.monitoring_grp_id"))
        .where(text("g.comp_id = :comp_id"))
        .params(comp_id=comp_id)
        .order_by(MonitoringLayout.monitoring_grp_id, MonitoringLayout.item_idx)
    )
    return list(db.scalars(stmt).all())


def fetch_layouts_by_group(db: Session, monitoring_grp_id: str) -> list[MonitoringLayout]:
    """그룹 ID 기준으로 레이아웃 목록을 조회한다."""
    from sqlalchemy import select
    stmt = (
        select(MonitoringLayout)
        .where(MonitoringLayout.monitoring_grp_id == monitoring_grp_id)
        .order_by(MonitoringLayout.item_idx)
    )
    return list(db.scalars(stmt).all())


def upsert_layout(db: Session, layout: MonitoringLayout) -> MonitoringLayout:
    """레이아웃을 등록하거나 수정한다."""
    merged = db.merge(layout)
    db.commit()
    db.refresh(merged)
    return merged


def delete_layout(db: Session, monitoring_grp_id: str, item_idx: int) -> bool:
    """레이아웃을 삭제한다."""
    from sqlalchemy import delete as sa_delete
    result = db.execute(
        sa_delete(MonitoringLayout).where(
            MonitoringLayout.monitoring_grp_id == monitoring_grp_id,
            MonitoringLayout.item_idx == item_idx,
        )
    )
    db.commit()
    return result.rowcount > 0
