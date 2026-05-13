"""ROI 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel


class RoiRead(BaseModel):
    """ROI 조회 응답 스키마."""

    camera_id: str
    model_nm: str
    point: str
    is_run: bool = False
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None

    class Config:
        from_attributes = True
