"""카메라 이벤트 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel


class EventRead(BaseModel):
    """이벤트 이력 조회 응답 스키마."""

    event_time: datetime
    camera_id: str
    event_type: str | None = None
    event_desc: str
    file_path: str | None = None
    isread: bool | None = None
    remark: str | None = None

    class Config:
        from_attributes = True
