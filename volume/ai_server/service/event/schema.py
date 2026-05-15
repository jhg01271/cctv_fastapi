"""카메라 이벤트 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class EventRead(BaseModel):
    """이벤트 이력 조회 응답 스키마."""

    model_config = ConfigDict(from_attributes=True, populate_by_name=True)

    event_time: datetime
    camera_id: str = Field(serialization_alias="cctv_id")
    event_type: str | None = None
    event_type_name: str | None = None
    event_desc: str
    file_path: str | None = None
    remark: str | None = None
