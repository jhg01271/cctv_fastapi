"""카메라 이벤트 도메인의 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from datetime import datetime

from typing import Any

from pydantic import BaseModel, ConfigDict, Field, model_validator


def _unpack_list(data: dict[str, Any]) -> dict[str, Any]:
    """리스트 값을 스칼라로 변환한다. 프론트에서 cctv_id: [] 또는 ["CAM0019"] 등으로 보내는 경우 대응."""
    result = {}
    for k, v in data.items():
        if isinstance(v, list):
            result[k] = v[0] if v else None
        else:
            result[k] = v
    return result


class EventDetailRequest(BaseModel):
    """단일 이벤트 상세 조회 요청 스키마."""

    event_time: str
    camera_id: str | None = None
    cctv_id: str | None = None

    @model_validator(mode="before")
    @classmethod
    def _coerce(cls, data: Any) -> Any:
        if isinstance(data, dict):
            return _unpack_list(data)
        return data


class EventListRequest(BaseModel):
    """이벤트 이력 조회 요청 스키마."""

    camera_id: str | None = None
    cctv_id: str | None = None
    event_type: list[str] | None = None
    start_date: str | None = None
    end_date: str | None = None

    @model_validator(mode="before")
    @classmethod
    def _coerce(cls, data: Any) -> Any:
        if isinstance(data, dict):
            # event_type은 다중 선택이므로 리스트 유지
            event_type = data.get("event_type")
            result = _unpack_list(data)
            if isinstance(event_type, list):
                result["event_type"] = event_type if event_type else None
            return result
        return data


class EventGroupRequest(BaseModel):
    """그룹별 이벤트 이력 조회 요청 스키마."""

    group_id: str | None = None
    monitoring_grp_id: str | None = None
    profile_id: str | None = None
    start_date: str | None = None
    end_date: str | None = None

    @model_validator(mode="before")
    @classmethod
    def _coerce(cls, data: Any) -> Any:
        if isinstance(data, dict):
            return _unpack_list(data)
        return data


class EventFileRequest(BaseModel):
    """이벤트 이미지 파일 조회 요청 스키마."""

    file_path: str | None = None
    camera_id: str | None = None
    cctv_id: str | None = None
    event_time: str | None = None

    @model_validator(mode="before")
    @classmethod
    def _coerce(cls, data: Any) -> Any:
        if isinstance(data, dict):
            return _unpack_list(data)
        return data


class EventRemarkRequest(BaseModel):
    """이벤트 비고 수정 요청 스키마."""

    event_time: str
    camera_id: str | None = None
    cctv_id: str | None = None
    remark: str = ""

    @model_validator(mode="before")
    @classmethod
    def _coerce(cls, data: Any) -> Any:
        if isinstance(data, dict):
            return _unpack_list(data)
        return data


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
