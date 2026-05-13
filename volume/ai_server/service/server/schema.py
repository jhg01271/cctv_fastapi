"""AI 서버 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel


class AiServerRead(BaseModel):
    """AI 서버 조회 응답 스키마."""

    ai_server_id: str
    comp_id: str
    server_nm: str
    server_host: str
    api_port: str | None = None
    mtx_port: str | None = None
    server_url: str | None = None
    remark: str | None = None
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None

    class Config:
        from_attributes = True


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
