"""
[Router (라우터) - ROI 도메인]
1. 도메인 기능: "안전 감시 구역 설정 (Region Of Interest)"
   - 사용자가 화면에 그린 위험 구역의 다각형 좌표 정보 및 감시 대상 AI 모델 설정(안전모 미착용 감지, 작업자 쓰러짐 등)을 관리합니다.
2. 아키텍처 역할: 사용자의 요청(HTTP Request)을 가장 먼저 받는 관문입니다.
3. 흐름: Frontend (브라우저) ➡️ Router ➡️ Service
4. 설명: 프론트엔드로부터의 API 요청(회사별 감시 구역 목록 가져오기, 특정 카메라 실시간 프레임 캡처, 감시 구역 설정 저장 등)을 접수받아 파라미터를 검증하고 Service 레이어로 전달합니다.
"""



from __future__ import annotations

from fastapi import APIRouter, Depends
from fastapi.responses import JSONResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.roi.schema import RoiRead, RoiSaveRequest
from service.roi import service

router = APIRouter(prefix="/cctv/roi_crud", tags=["ROI 설정"])


@router.get(
    "/rois/{comp_id}",
    summary="ROI 목록 조회",
    response_model=ResultResponse[list[dict]],
)
def get_rois(
    comp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[dict]]:
    """회사별 ROI 목록을 카메라 기준 그룹핑하여 조회한다."""
    result = service.list_rois(db, parse_comp_id(comp_id))
    return response(data=result, msg_key="success.read")


@router.get(
    "/get_cctv_img/{camera_id}",
    summary="CCTV 캡처 이미지 조회",
)
def get_cctv_image(
    camera_id: str,
    db: Session = Depends(get_db),
) -> JSONResponse:
    """카메라 RTSP에서 한 프레임을 캡처해 base64 이미지와 크기 정보를 반환한다.

    프론트엔드 인터셉터가 response.data.data를 추출하므로,
    이미지 데이터를 'data' 키로 반환하여 직접 접근 가능하게 한다.
    """
    result = service.capture_cctv_image(db, camera_id)
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/roi",
    summary="ROI 저장",
    response_model=ResultResponse[list[dict]],
)
def save_roi(
    body: list[RoiSaveRequest],
    db: Session = Depends(get_db),
) -> ResultResponse[list[dict]]:
    """ROI를 등록하거나 수정한다."""
    results = []
    for item in body:
        results.extend(service.save_roi(db, item.model_dump()))
    return response(data=results, msg_key="success.create")
