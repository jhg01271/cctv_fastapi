"""모니터링 프로필(레이아웃) 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

import re
from datetime import datetime
from typing import Any

from pydantic import BaseModel, ConfigDict, Field, model_validator


def _parse_item_idx(value: Any) -> int | None:
    """'New_7' 같은 문자열에서 숫자를 추출한다."""
    if value is None:
        return None
    if isinstance(value, int):
        return value
    m = re.search(r"\d+", str(value))
    return int(m.group()) if m else None


class ProfileSaveItem(BaseModel):
    """모니터링 레이아웃 등록/수정 개별 항목 스키마."""

    monitoring_grp_id: str | None = None
    profile_id: str | None = None
    item_idx: int | None = None
    is_new: bool = False
    coordinate_x: int = 0
    coordinate_y: int = 0
    item_width: int = 15
    item_height: int = 6
    camera_id: str | None = None
    sort: int | None = None
    title: str | None = None
    created_by: str | None = None
    updated_by: str | None = None

    @model_validator(mode="before")
    @classmethod
    def _map_fields(cls, data: Any) -> Any:
        """프론트엔드 필드명을 서비스 필드명으로 매핑한다."""
        if not isinstance(data, dict):
            return data
        return {
            "monitoring_grp_id": data.get("monitoring_grp_id") or data.get("profile_id"),
            "profile_id": data.get("profile_id"),
            "item_idx": _parse_item_idx(data.get("item_idx") or data.get("i")),
            "is_new": str(data.get("item_idx") or data.get("i") or "").startswith("New"),
            "coordinate_x": data.get("coordinate_x", data.get("x", 0)),
            "coordinate_y": data.get("coordinate_y", data.get("y", 0)),
            "item_width": data.get("item_width", data.get("w", 15)),
            "item_height": data.get("item_height", data.get("h", 6)),
            "camera_id": data.get("camera_id") or data.get("cctv_id"),
            "sort": data.get("sort"),
            "title": data.get("title"),
            "created_by": data.get("created_by") or data.get("userCd"),
            "updated_by": data.get("updated_by") or data.get("userCd"),
        }


class ProfileDelete(BaseModel):
    """모니터링 레이아웃 삭제 요청 스키마."""

    monitoring_grp_id: str | None = None
    item_idx: int | None = None


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
