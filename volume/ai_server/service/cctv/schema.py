"""CCTV 카메라 도메인의 요청/응답 스키마를 정의한다."""

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
