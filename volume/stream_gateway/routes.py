"""Stream gateway routes."""

from __future__ import annotations

from fastapi import APIRouter
from pydantic import BaseModel

from stream_gateway import service

router = APIRouter(tags=["stream_gateway"])


class StreamStartRequest(BaseModel):
    rtsp_url: str


@router.get("/health")
def health() -> dict:
    """Return stream gateway health."""
    return {"status": "ok"}


@router.post("/streams/{camera_id}/start")
def start_stream(camera_id: str, payload: StreamStartRequest) -> dict:
    """Start publishing a camera stream."""
    return service.start_stream(camera_id, payload.rtsp_url)


@router.delete("/streams/{camera_id}")
def stop_stream(camera_id: str) -> dict:
    """Stop publishing a camera stream."""
    return service.stop_stream(camera_id)
