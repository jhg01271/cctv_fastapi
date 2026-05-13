"""AI 서버 도메인의 SQLAlchemy 모델을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import DateTime, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class AiServer(Base):
    """AI 서버 테이블 모델이다."""

    __tablename__ = "tb_camera_ai_server"
    __table_args__ = {"schema": "public"}

    ai_server_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    comp_id: Mapped[str] = mapped_column(String(10), nullable=False)
    server_nm: Mapped[str] = mapped_column(String(50), nullable=False)
    server_host: Mapped[str] = mapped_column(String(15), nullable=False)
    api_port: Mapped[str | None] = mapped_column(String(6), nullable=True)
    mtx_port: Mapped[str | None] = mapped_column(String(6), nullable=True)
    server_url: Mapped[str | None] = mapped_column(String(50), nullable=True)
    remark: Mapped[str | None] = mapped_column(String(100), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=False), nullable=False)
    created_by: Mapped[str] = mapped_column(String(36), nullable=False)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
