"""공정률 도메인의 SQLAlchemy 모델을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, Float, Integer, String, Text
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class TwinImage(Base):
    """공정률 이미지 테이블 모델이다."""

    __tablename__ = "tb_twin_image"
    __table_args__ = {"schema": "public"}

    image_id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    camera_id: Mapped[str] = mapped_column(String(50), nullable=False)
    image_name: Mapped[str | None] = mapped_column(String(100), nullable=True)
    result_image_base64: Mapped[str | None] = mapped_column(Text, nullable=True)
    blank_image_base64: Mapped[str | None] = mapped_column(Text, nullable=True)
    set_mode: Mapped[str | None] = mapped_column(String(10), nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)


class TwinDetection(Base):
    """공정률 감지 결과 테이블 모델이다."""

    __tablename__ = "tb_twin_detection"
    __table_args__ = {"schema": "public"}

    image_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    detection_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    object_label: Mapped[str | None] = mapped_column(String(50), nullable=True)
    grid_width: Mapped[int | None] = mapped_column(Integer, nullable=True)
    grid_height: Mapped[int | None] = mapped_column(Integer, nullable=True)
    detected_row: Mapped[int | None] = mapped_column(Integer, nullable=True)
    detected_col: Mapped[int | None] = mapped_column(Integer, nullable=True)
    detected_coords: Mapped[dict | None] = mapped_column(JSONB, nullable=True)
    base64_image: Mapped[str | None] = mapped_column(Text, nullable=True)
    created_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    order_no: Mapped[str | None] = mapped_column(String(50), nullable=True)


class CameraGrid(Base):
    """카메라 격자 테이블 모델이다."""

    __tablename__ = "tb_camera_grid"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    grid_data: Mapped[dict] = mapped_column(JSONB, nullable=False)
    img_data: Mapped[str] = mapped_column(Text, nullable=False)
    sort_direction: Mapped[str] = mapped_column(String(10), nullable=False)
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
