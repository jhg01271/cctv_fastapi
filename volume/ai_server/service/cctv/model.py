"""
================================================================================
[Model] service/cctv/model.py
================================================================================
- 역할: 데이터베이스의 'tb_camera' 테이블 구조와 1:1 매핑되는 SQLAlchemy ORM 엔티티 정의 파일입니다.
- 흐름: Repository(repository.py)가 DB에 쿼리를 날려 데이터를 가져올 때, 이 모델 객체 형태로 매핑하여 반환됩니다.
- 주요 속성: camera_id, comp_id, camera_nm, rtsp_addr(RTSP 스트림 주소), pid(프로세스 식별자), jit_only 등
================================================================================
"""

from __future__ import annotations

from datetime import datetime

from sqlalchemy import Boolean, DateTime, SmallInteger, String
from sqlalchemy.orm import Mapped, mapped_column

from core.database.session import Base


class Camera(Base):
    """카메라 테이블 모델이다."""

    __tablename__ = "tb_camera"
    __table_args__ = {"schema": "public"}

    camera_id: Mapped[str] = mapped_column(String(36), primary_key=True)
    comp_id: Mapped[str] = mapped_column(String(10), nullable=False)
    camera_nm: Mapped[str] = mapped_column(String(50), nullable=False)
    camera_desc: Mapped[str | None] = mapped_column(String(100), nullable=True)
    ai_server_id: Mapped[str | None] = mapped_column(String(36), nullable=True)
    rtsp_addr: Mapped[str | None] = mapped_column(String(500), nullable=True)
    out_path: Mapped[str | None] = mapped_column(String(50), nullable=True)
    pid: Mapped[str | None] = mapped_column(String(7), nullable=True)
    remark: Mapped[str | None] = mapped_column(String(100), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=False), nullable=False)
    created_by: Mapped[str] = mapped_column(String(36), nullable=False)
    updated_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=False), nullable=True)
    updated_by: Mapped[str | None] = mapped_column(String(36), nullable=True)
    jit_only: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    port_number: Mapped[int | None] = mapped_column(SmallInteger, nullable=True)
    location: Mapped[str | None] = mapped_column(String(50), nullable=True)
