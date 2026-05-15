"""모니터링 프로필(레이아웃) 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class LayoutRead(BaseModel):
    """모니터링 레이아웃 조회 응답 스키마."""

    model_config = ConfigDict(from_attributes=True, populate_by_name=True)

    monitoring_grp_id: str
    item_idx: int
    coordinate_x: int
    coordinate_y: int
    item_width: int
    item_height: int
    camera_id: str = Field(serialization_alias="cctv_id")
    sort: int | None = None
    title: str | None = None
    created_at: datetime
    created_by: str
    updated_at: datetime | None = None
    updated_by: str | None = None
