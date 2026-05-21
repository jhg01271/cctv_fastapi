"""텔레그램 매니저 DB 접근 레이어."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import select, update
from sqlalchemy.orm import Session

from service.profile.model import MonitoringLayout
from service.telegram.model import TelegramManager


def fetch_all(db: Session) -> list[TelegramManager]:
    """모든 텔레그램 매니저를 조회한다."""
    stmt = select(TelegramManager)
    return list(db.scalars(stmt).all())


def fetch_by_key(db: Session, monitoring_grp_id: str, chat_id: str) -> TelegramManager | None:
    """복합키로 매니저를 조회한다."""
    return db.get(TelegramManager, (monitoring_grp_id, chat_id))


def insert(db: Session, data: dict) -> TelegramManager:
    """새 매니저를 추가한다."""
    manager = TelegramManager(
        created_at=datetime.now(),
        **data,
    )
    db.add(manager)
    db.commit()
    db.refresh(manager)
    return manager


def update_manager(
    db: Session,
    original_monitoring_grp_id: str,
    original_chat_id: str,
    data: dict,
) -> bool:
    """매니저 정보를 업데이트한다."""
    existing = db.get(TelegramManager, (original_monitoring_grp_id, original_chat_id))
    if not existing:
        return False

    for key, value in data.items():
        setattr(existing, key, value)
    existing.updated_at = datetime.now()
    db.commit()
    return True


def delete(db: Session, monitoring_grp_id: str, chat_id: str) -> bool:
    """매니저를 삭제한다."""
    existing = db.get(TelegramManager, (monitoring_grp_id, chat_id))
    if not existing:
        return False
    db.delete(existing)
    db.commit()
    return True


def set_notification(db: Session, monitoring_grp_id: str, chat_id: str, on: bool) -> bool:
    """알림 활성화/비활성화를 설정한다."""
    stmt = (
        update(TelegramManager)
        .where(
            TelegramManager.monitoring_grp_id == monitoring_grp_id,
            TelegramManager.chat_id == chat_id,
        )
        .values(notification_on=on)
    )
    result = db.execute(stmt)
    db.commit()
    return result.rowcount > 0


def set_all_notification(db: Session, on: bool) -> int:
    """모든 매니저의 알림을 일괄 설정한다."""
    stmt = (
        update(TelegramManager)
        .where(TelegramManager.notification_on != on)
        .values(notification_on=on)
    )
    result = db.execute(stmt)
    db.commit()
    return result.rowcount


def fetch_chat_ids_for_camera(db: Session, camera_id: str) -> list[tuple[str, str]]:
    """카메라 ID에 연결된 알림 활성 chat_id와 token 목록을 조회한다."""
    stmt = (
        select(TelegramManager.chat_id, TelegramManager.token)
        .join(
            MonitoringLayout,
            TelegramManager.monitoring_grp_id == MonitoringLayout.monitoring_grp_id,
        )
        .where(
            MonitoringLayout.camera_id == camera_id,
            TelegramManager.notification_on == True,  # noqa: E712
        )
        .distinct()
    )
    return list(db.execute(stmt).all())
