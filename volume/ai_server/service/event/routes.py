"""카메라 이벤트 라우터 — 프론트엔드 /cctv/ce 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends, Request
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.event.schema import EventRead
from service.event import service

router = APIRouter(prefix="/cctv/ce", tags=["camera_event"])


@router.post(
    "/camera_event",
    summary="이벤트 상세 조회",
    response_model=ResultResponse[EventRead],
)
async def get_camera_event(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[EventRead]:
    """단일 이벤트 상세를 조회한다."""
    body = await request.json()
    result = service.get_event(db, body)
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events1",
    summary="이벤트 이력 조회",
    response_model=ResultResponse[list[EventRead]],
)
async def list_camera_events(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[list[EventRead]]:
    """이벤트 이력을 조회한다."""
    body = await request.json()
    result = service.list_events(db, body)
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events_by_group1",
    summary="그룹별 이벤트 이력 조회",
    response_model=ResultResponse[list[EventRead]],
)
async def list_camera_events_by_group(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[list[EventRead]]:
    """그룹별 이벤트 이력을 조회한다."""
    body = await request.json()
    result = service.list_events_by_group(db, body)
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events_get_file",
    summary="이벤트 이미지 파일 조회",
)
async def get_camera_event_file(
    request: Request,
    db: Session = Depends(get_db),
) -> FileResponse:
    """이벤트 이미지 파일을 반환한다."""
    body = await request.json()
    file_path = service.get_event_file(db, body)
    return FileResponse(path=file_path, media_type="image/jpeg")


@router.post(
    "/camera_events_remark",
    summary="이벤트 비고 수정",
    response_model=ResultResponse[dict],
)
async def update_camera_event_remark(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """이벤트 비고를 수정한다."""
    body = await request.json()
    result = service.save_remark(db, body)
    return response(data=result, msg_key="success.update")
