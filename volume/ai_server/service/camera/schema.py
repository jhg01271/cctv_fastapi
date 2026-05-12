"""camera 도메인에서 사용하는 스키마를 정의한다."""

from __future__ import annotations

from pydantic import BaseModel, Field


class CameraGroupRead(BaseModel):
    """카메라 그룹 응답 스키마다."""

    comp_id: str = Field(description="회사 식별자")
    profile_id: str = Field(description="프로필 식별자")
    profile_name: str | None = Field(default=None, description="프로필 이름")
    created_at: str | None = Field(default=None, description="생성 일시")
    created_by: str | None = Field(default=None, description="생성자")
    updated_at: str | None = Field(default=None, description="수정 일시")
    updated_by: str | None = Field(default=None, description="수정자")


class CameraRegisterRequest(BaseModel):
    """카메라 등록 요청."""
    camera_id: str = Field(description="카메라 식별자")
    rtsp_url:  str = Field(description="RTSP 주소")


class CameraStatusResponse(BaseModel):
    """카메라 프로세스 상태."""
    camera_id:    str  = Field(description="카메라 ID")
    rtsp_url:     str  = Field(description="원본 RTSP 주소")
    stream_url:   str  = Field(description="MediaMTX 경유 스트림 URL")
    reader_alive: bool = Field(description="RTSP 리더 프로세스 생존 여부")
    ai_alive:     bool = Field(description="AI 프로세스 생존 여부")
    heartbeat:    int  = Field(description="워치독 heartbeat 카운터")


class StreamUrlResponse(BaseModel):
    """프론트엔드용 스트림 URL."""
    webrtc: str = Field(description="WebRTC URL")
    rtsp:   str = Field(description="RTSP URL")
