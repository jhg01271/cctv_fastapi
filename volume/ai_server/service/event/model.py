"""카메라 이벤트 도메인의 SQLAlchemy 모델을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class CameraEventHist(Base):
    """카메라 이벤트 이력 테이블 모델이다."""

    __tablename__ = "tb_camera_event_hist"
    __table_args__ = {"schema": "public"}

    event_time: Mapped[datetime] = mapped_column(DateTime(timezone=False), primary_key=True)
    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    event_type: Mapped[str | None] = mapped_column(String(4), nullable=True)
    event_desc: Mapped[str] = mapped_column(String(100), nullable=False)
    file_path: Mapped[str | None] = mapped_column(String(100), nullable=True)
    isread: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    remark: Mapped[str | None] = mapped_column(String(100), nullable=True)
