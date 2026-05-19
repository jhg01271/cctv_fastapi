"""격자 관련 DB 모델."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, String, Text
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class CameraGrid(Base):
    """공정률 격자 (tb_camera_grid)."""

    __tablename__ = "tb_camera_grid"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    grid_data: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    img_data: Mapped[str | None] = mapped_column(Text, nullable=True)
    org_img_data: Mapped[str | None] = mapped_column(Text, nullable=True)
    sort_direction: Mapped[str | None] = mapped_column(String(10), nullable=True)
    grid_unit: Mapped[str | None] = mapped_column(String(50), nullable=True)
    extend_count: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    bwts_pro_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    scr_pro_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    bwts_mat_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    scr_mat_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    nox_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    pre_absorber_run: Mapped[bool | None] = mapped_column(Boolean, nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    created_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)


class CameraSafetyGrid(Base):
    """안전 격자 (tb_camera_safety_grid)."""

    __tablename__ = "tb_camera_safety_grid"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    grid_data: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    sort_direction: Mapped[str | None] = mapped_column(String(10), nullable=True)
    grid_unit: Mapped[str | None] = mapped_column(String(50), nullable=True)
    point_list_data: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    set_mode: Mapped[str | None] = mapped_column(String(5), nullable=True)
    extend_count: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    created_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
