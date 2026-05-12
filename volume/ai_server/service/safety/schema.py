"""안전관리 서비스 스키마."""

from __future__ import annotations

from pydantic import BaseModel, Field


class Detection(BaseModel):
    """단일 객체 탐지 결과."""
    cls: str   = Field(alias="class", description="클래스명")
    bbox: list[int]  = Field(description="[x1, y1, x2, y2]")
    conf: float      = Field(description="신뢰도 0~1")

    model_config = {"populate_by_name": True}


class EventAlert(BaseModel):
    """이벤트 알림."""
    type: str    = Field(description="이벤트 코드 (E001~E004)")
    message: str = Field(description="이벤트 메시지")


class SafetyFrame(BaseModel):
    """WebSocket으로 전송하는 단일 프레임 AI 결과."""
    camera_id:  str            = Field(description="카메라 ID")
    timestamp:  str            = Field(description="YYYYmmdd_HHMMSS")
    detections: list[Detection] = Field(default_factory=list)
    events:     list[EventAlert] = Field(default_factory=list)


class EventHistRead(BaseModel):
    """이벤트 이력 조회 결과."""
    event_id:   int
    camera_id:  str
    event_type: str
    event_desc: str
    image_path: str | None
    created_at: str
    is_checked: bool
