"""텔레그램 매니저 API 요청/응답 스키마."""

from __future__ import annotations

from pydantic import BaseModel


class TelegramManagerSave(BaseModel):
    monitoring_grp_id: str
    chat_id: str
    token: str | None = None
    notification_on: bool = False
    userCd: str | None = None
    original_monitoring_grp_id: str | None = None
    original_chat_id: str | None = None


class TelegramManagerDelete(BaseModel):
    monitoring_grp_id: str
    chat_id: str
