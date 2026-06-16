"""
================================================================================
[Schema / DTO] service/cctv/schema.py
================================================================================
- 역할: 클라이언트(Frontend)와 서버 간 주고받는 데이터의 포맷 및 유효성을 검증하는 Pydantic 모델(DTO) 정의 파일입니다.
- 흐름: Router(routes.py)로 들어오는 데이터(Request Body) 및 나가는 데이터(Response Body)의 필드 매핑 및 타입 체크를 수행합니다.
- 주요 객체: CameraRead(조회용), CameraCreate(생성/수정용), CameraDelete(삭제용)
================================================================================
"""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class CameraRead(BaseModel):
    """카메라 조회 응답 스키마."""

    model_config = ConfigDict(from_attributes=True, populate_by_name=True)

    camera_id: str = Field(serialization_alias="cctv_id")
    comp_id: str
    camera_nm: str = Field(serialization_alias="cctv_name")
    camera_desc: str | None = None
    ai_server_id: str | None = Field(default=None, serialization_alias="server_id")
    rtsp_addr: str | None = Field(default=None, serialization_alias="rtsp_add")
    out_path: str | None = None
    pid: str | None = None
    remark: str | None = None
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None
    jit_only: bool = False
    port_number: int | None = None
    location: str | None = None


class CameraCreate(BaseModel):
    """카메라 등록/수정 요청 스키마."""

    camera_id: str | None = None
    comp_id: str
    camera_nm: str
    camera_desc: str | None = None
    ai_server_id: str | None = None
    rtsp_addr: str | None = None
    out_path: str | None = None
    remark: str | None = None
    jit_only: bool = False
    port_number: int | None = None
    location: str | None = None
    created_by: str | None = None
    updated_by: str | None = None


class CameraDelete(BaseModel):
    """카메라 삭제 요청 스키마."""

    camera_id: str
