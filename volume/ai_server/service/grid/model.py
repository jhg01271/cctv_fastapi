"""격자 관련 DB 모델."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, Enum, Float, String, Text
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base

# DB에 정의된 direction_enum 타입 참조
DirectionEnum = Enum("up", "down", "left", "right", name="direction_enum", create_type=False)


class CameraProcessGrid(Base):
    """카메라 공정률 격자 테이블 모델이다."""

    __tablename__ = "tb_camera_grid"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    grid_data: Mapped[dict] = mapped_column(JSONB, nullable=False)
    img_data: Mapped[str] = mapped_column(Text, nullable=False)
    sort_direction: Mapped[str] = mapped_column(DirectionEnum, nullable=False)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=False), nullable=False)
    created_by: Mapped[str] = mapped_column(String(36), nullable=False)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    org_img_data: Mapped[str | None] = mapped_column(Text, nullable=True)
    grid_unit: Mapped[float | None] = mapped_column(Float, nullable=True)
    extend_count: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    bwts_pro_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    scr_pro_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    bwts_mat_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    scr_mat_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    nox_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    pre_absorber_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)


class CameraSafetyGrid(Base):
    """안전 격자 (tb_camera_safety_grid)."""

    __tablename__ = "tb_camera_safety_grid"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    grid_data: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    sort_direction: Mapped[str | None] = mapped_column(DirectionEnum, nullable=True)
    grid_unit: Mapped[float | None] = mapped_column(Float, nullable=True)
    point_list_data: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    set_mode: Mapped[str | None] = mapped_column(String(5), nullable=True)
    extend_count: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    created_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
