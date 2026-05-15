"""ROI 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class RoiRead(BaseModel):
    """ROI 조회 응답 스키마."""

    model_config = ConfigDict(from_attributes=True, populate_by_name=True)

    camera_id: str = Field(serialization_alias="cctv_id")
    camera_nm: str | None = Field(default=None, serialization_alias="cctv_name")
    model_nm: str
    point: str
    is_run: bool = False
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None
