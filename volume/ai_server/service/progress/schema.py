"""공정률 서비스 스키마."""

from __future__ import annotations

from pydantic import BaseModel, Field


class ProgressResultRead(BaseModel):
    """공정률 결과 이미지 조회."""
    result_id:  int
    camera_id:  str
    image_path: str
    created_at: str


class ProgressCameraRead(BaseModel):
    """공정률 대상 카메라 정보."""
    camera_id:      str
    rtsp_url:       str
    sort_direction: str
