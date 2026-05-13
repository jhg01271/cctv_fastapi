"""모니터링 프로필(레이아웃) 도메인의 SQLAlchemy 모델을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import DateTime, Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class MonitoringLayout(Base):
    """모니터링 레이아웃 테이블 모델이다."""

    __tablename__ = "tb_camera_monitoring_layout"
    __table_args__ = {"schema": "public"}

    monitoring_grp_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    item_idx: Mapped[int] = mapped_column(Integer, primary_key=True)
    coordinate_x: Mapped[int] = mapped_column(Integer, nullable=False)
    coordinate_y: Mapped[int] = mapped_column(Integer, nullable=False)
    item_width: Mapped[int] = mapped_column(Integer, nullable=False)
    item_height: Mapped[int] = mapped_column(Integer, nullable=False)
    camera_id: Mapped[str] = mapped_column(String(36), nullable=False)
    sort: Mapped[int | None] = mapped_column(Integer, nullable=True)
    title: Mapped[str | None] = mapped_column(String(50), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=False), nullable=False)
    created_by: Mapped[str] = mapped_column(String(36), nullable=False)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
