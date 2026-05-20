"""ROI 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

import json
from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field, field_validator


class RoiModelItem(BaseModel):
    """ROI 모델 항목."""

    model_nm: str
    point_arr: str = "{}"
    is_run: bool = False
    userCd: str = "system"

    @field_validator("point_arr")
    @classmethod
    def validate_point_arr(cls, v: str) -> str:
        """point_arr이 유효한 JSON인지 검증한다."""
        try:
            json.loads(v)
        except (json.JSONDecodeError, TypeError):
            raise ValueError(f"point_arr이 유효한 JSON이 아닙니다: {v[:100]}")
        return v


class RoiSaveRequest(BaseModel):
    """ROI 저장 요청 스키마."""

    cctv_id: str
    model_list: list[RoiModelItem]


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
