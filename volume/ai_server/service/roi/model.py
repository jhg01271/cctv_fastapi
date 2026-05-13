"""ROI 도메인의 SQLAlchemy 모델을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class CameraRoi(Base):
    """카메라 ROI 테이블 모델이다."""

    __tablename__ = "tb_camera_roi"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    model_nm: Mapped[str] = mapped_column(String(20), primary_key=True)
    point: Mapped[str] = mapped_column(String(1000), nullable=False)
    is_run: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=False), nullable=False)
    created_by: Mapped[str] = mapped_column(String(36), nullable=False)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
