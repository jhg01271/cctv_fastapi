"""ROI 관리 라우터 — 프론트엔드 /cctv/roi_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends, Request
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.roi.schema import RoiRead
from service.roi import service

router = APIRouter(prefix="/cctv/roi_crud", tags=["roi_crud"])


@router.get(
    "/rois/{comp_id}",
    summary="ROI 목록 조회",
    response_model=ResultResponse[list[RoiRead]],
)
def get_rois(
    comp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[RoiRead]]:
    """회사별 ROI 목록을 조회한다."""
    result = service.list_rois(db, parse_comp_id(comp_id))
    return response(data=result, msg_key="success.read")


@router.get(
    "/get_cctv_img/{camera_id}",
    summary="CCTV 캡처 이미지 조회",
)
def get_cctv_image(
    camera_id: str,
    db: Session = Depends(get_db),
) -> FileResponse:
    """카메라 RTSP에서 한 프레임을 캡처해 이미지로 반환한다."""
    file_path = service.capture_cctv_image(db, camera_id)
    return FileResponse(path=file_path, media_type="image/jpeg")


@router.post(
    "/roi",
    summary="ROI 저장",
    response_model=ResultResponse[RoiRead],
)
async def save_roi(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[RoiRead]:
    """ROI를 등록하거나 수정한다."""
    body = await request.json()
    result = service.save_roi(db, body)
    return response(data=result, msg_key="success.create")
