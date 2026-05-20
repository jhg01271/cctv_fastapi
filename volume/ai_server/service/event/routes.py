"""카메라 이벤트 라우터 — 프론트엔드 /cctv/ce 엔드포인트 대응."""

from __future__ import annotations

import base64

from fastapi import APIRouter, Depends
from fastapi.responses import JSONResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.event.schema import (
    EventDetailRequest,
    EventFileRequest,
    EventGroupRequest,
    EventListRequest,
    EventRead,
    EventRemarkRequest,
)
from service.event import service

router = APIRouter(prefix="/cctv/ce", tags=["안전 이벤트 이력"])


@router.post(
    "/camera_event",
    summary="이벤트 상세 조회",
    response_model=ResultResponse[EventRead],
)
def get_camera_event(
    body: EventDetailRequest,
    db: Session = Depends(get_db),
) -> ResultResponse[EventRead]:
    """단일 이벤트 상세를 조회한다."""
    result = service.get_event(db, body.model_dump())
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events1",
    summary="이벤트 이력 조회",
    response_model=ResultResponse[list[EventRead]],
)
def list_camera_events(
    body: EventListRequest,
    db: Session = Depends(get_db),
) -> ResultResponse[list[EventRead]]:
    """이벤트 이력을 조회한다."""
    result = service.list_events(db, body.model_dump())
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events_by_group1",
    summary="그룹별 이벤트 이력 조회",
    response_model=ResultResponse[list[EventRead]],
)
def list_camera_events_by_group(
    body: EventGroupRequest,
    db: Session = Depends(get_db),
) -> ResultResponse[list[EventRead]]:
    """그룹별 이벤트 이력을 조회한다."""
    result = service.list_events_by_group(db, body.model_dump())
    return response(data=result, msg_key="success.read")


@router.post(
    "/camera_events_get_file",
    summary="이벤트 이미지 파일 조회",
)
def get_camera_event_file(
    body: EventFileRequest,
    db: Session = Depends(get_db),
) -> JSONResponse:
    """이벤트 이미지를 base64로 인코딩하여 반환한다.

    프론트엔드 기대 형식: { success, img_decode_data, ratio }
    """
    file_path = service.get_event_file(db, body.model_dump())

    with open(file_path, "rb") as f:
        img_data = base64.b64encode(f.read()).decode("utf-8")

    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "성공하였습니다.",
        "data": {
            "success": True,
            "img_decode_data": img_data,
            "ratio": "16:9",
        },
    })


@router.post(
    "/camera_events_remark",
    summary="이벤트 비고 수정",
    response_model=ResultResponse[dict],
)
def update_camera_event_remark(
    body: EventRemarkRequest,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """이벤트 비고를 수정한다."""
    result = service.save_remark(db, body.model_dump())
    return response(data=result, msg_key="success.update")
