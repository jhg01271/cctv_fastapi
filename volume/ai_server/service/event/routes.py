"""SafetyMonitoringHistory가 DB에 저장된 이벤트 이력을 조회할 때 들어오는 라우터 파일.

흐름에서의 위치:
  1. service/safety/worker.py가 확정 이벤트를 tb_camera_event_hist에 저장한다.
  2. 프론트의 SafetyMonitoringHistory 화면이 /cctv/ce/camera_events1 또는 camera_events_by_group1을 호출한다.
  3. 이 파일은 요청 조건을 받아 service/event/service.py와 repository.py로 넘긴다.
  4. 이벤트 상세 파일 요청이 들어오면 JPG는 base64로, MP4는 /cctv/process/video URL로 돌려준다.

중요한 점:
  - /safety/debug의 실시간 bbox 경로와 다르다. 여기서는 이미 DB에 저장된 확정 이벤트만 조회한다.

다음에 볼 파일:
  - service/event/repository.py: tb_camera_event_hist에서 조건에 맞는 이벤트를 조회한다.
  - service/safety/worker.py: History에 나타날 이벤트를 DB에 저장하는 쪽이다.
"""

from __future__ import annotations

import base64
from pathlib import Path
from urllib.parse import quote

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
    import logging
    _log = logging.getLogger(__name__)
    _log.warning("[DEBUG] camera_events_by_group1 body=%s", body.model_dump())
    result = service.list_events_by_group(db, body.model_dump())
    _log.warning("[DEBUG] camera_events_by_group1 result_count=%d", len(result))
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
    suffix = Path(file_path).suffix.lower()

    if suffix in {".mp4", ".avi", ".mov", ".m4v"}:
        return JSONResponse(content={
            "success": True,
            "code": "200",
            "msg": "성공하였습니다.",
            "data": {
                "success": True,
                "file_type": "video",
                "media_type": "video/mp4",
                "video_url": f"/cctv/process/video?path={quote(file_path, safe='')}",
                "file_path": file_path,
                "ratio": "16:9",
            },
        })

    with open(file_path, "rb") as f:
        img_data = base64.b64encode(f.read()).decode("utf-8")

    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "성공하였습니다.",
        "data": {
                "success": True,
                "file_type": "image",
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
