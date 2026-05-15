"""AI 서버 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class AiServerRead(BaseModel):
    """AI 서버 조회 응답 스키마."""

    model_config = ConfigDict(from_attributes=True, populate_by_name=True)

    ai_server_id: str = Field(serialization_alias="server_id")
    comp_id: str
    server_nm: str = Field(serialization_alias="server_name")
    server_host: str = Field(serialization_alias="server_ip")
    api_port: str | None = Field(default=None, serialization_alias="restapi_port")
    mtx_port: str | None = Field(default=None, serialization_alias="mediamtx_port")
    server_url: str | None = None
    remark: str | None = None
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None


class AiServerCreate(BaseModel):
    """AI 서버 등록/수정 요청 스키마."""

    ai_server_id: str | None = None
    comp_id: str
    server_nm: str
    server_host: str
    api_port: str | None = None
    mtx_port: str | None = None
    server_url: str | None = None
    remark: str | None = None
    created_by: str | None = None
    updated_by: str | None = None


class AiServerDelete(BaseModel):
    """AI 서버 삭제 요청 스키마."""

    ai_server_id: str
