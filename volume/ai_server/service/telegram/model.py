"""텔레그램 매니저 DB 모델."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class TelegramManager(Base):
    """텔레그램 알림 매니저 테이블 모델이다."""

    __tablename__ = "tb_telegram_managers"
    __table_args__ = {"schema": "public"}

    monitoring_grp_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    chat_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    notification_on: Mapped[bool] = mapped_column(Boolean, default=False)
    token: Mapped[str | None] = mapped_column(String(255), nullable=True)
    comp_id: Mapped[str | None] = mapped_column(String(36), nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    created_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
