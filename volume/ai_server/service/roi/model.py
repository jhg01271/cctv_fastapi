"""
[Model (모델) - ROI 도메인]
1. 도메인 기능: "안전 감시 구역 설정 (Region Of Interest)"
   - 사용자가 화면에 그린 위험 구역의 다각형 좌표 정보 및 감시 대상 AI 모델 설정(안전모 미착용 감지, 작업자 쓰러짐 등)을 관리합니다.
2. 아키텍처 역할: 데이터베이스(DB) 테이블의 구조를 파이썬 클래스로 정의하는 스키마 지도입니다.
3. 흐름: Repository ➡️ DB (Model 정의 기반으로 테이블과 매핑됨)
4. 설명: 데이터베이스의 public.tb_camera_roi 테이블과 매핑되는 파이썬 클래스 구조를 정의합니다.
      카메라 ID(camera_id), 알고리즘 모델명(model_nm), 다각형 좌표쌍(point), 구역 활성화 여부(is_run) 등의 컬럼 타입을 설계합니다.
"""



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
