"""격자 관리 라우터 — 프론트엔드 /grid/grid_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from fastapi.responses import JSONResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.grid import service
from service.grid.schema import (
    InitializeCoordinatesRequest,
    LoadGridRequest,
    LoadSafetyGridRequest,
    GetRawImgRequest,
    PointListViewRequest,
    ProcessGridRequest,
    SaveGridRequest,
    SaveGridUnitRequest,
    SaveSafetyGridRequest,
    UpdateSortDirectionRequest,
)

router = APIRouter(prefix="/grid/grid_crud", tags=["grid_crud"])


@router.post(
    "/initialize_coordinates",
    summary="격자 초기화 (빨간 사각형 탐지)",
)
def initialize_coordinates(body: InitializeCoordinatesRequest) -> JSONResponse:
    """이미지에서 빨간 사각형을 탐지하여 격자를 초기화한다."""
    result = service.initialize_coordinates(
        image_base64=body.image_base64,
        click_coordinates=body.click_coordinates,
    )
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/update_sort_direction",
    summary="정렬 방향 변경",
)
def update_sort_direction(body: UpdateSortDirectionRequest) -> ResultResponse:
    """격자의 정렬 방향을 업데이트한다."""
    service.update_sort_direction(
        unique_id=body.unique_id,
        sort_direction=body.sort_direction,
    )
    return response(data={"sort_direction": body.sort_direction}, msg_key="success.update")


@router.post(
    "/process_grid",
    summary="격자 확장/축소",
)
def process_grid(body: ProcessGridRequest) -> JSONResponse:
    """격자 확장/축소 작업을 처리한다."""
    result = service.process_grid(
        unique_id=body.unique_id,
        operations=body.operations,
    )
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/save_grid",
    summary="공정률 격자 저장",
)
def save_grid(
    body: SaveGridRequest,
    db: Session = Depends(get_db),
) -> ResultResponse:
    """현재 격자 상태를 DB에 저장한다."""
    result = service.save_grid(
        db=db,
        unique_id=body.unique_id,
        camera_id=body.camera_id,
        grid_unit=body.grid_unit,
        created_by=body.created_by,
        updated_by=body.updated_by,
        bwts_pro_run=body.bwts_pro_run,
        scr_pro_run=body.scr_pro_run,
        bwts_mat_run=body.bwts_mat_run,
        scr_mat_run=body.scr_mat_run,
        nox_run=body.nox_run,
        pre_absorber_run=body.pre_absorber_run,
    )
    return response(data=result, msg_key="success.create")


@router.post(
    "/load_grid",
    summary="공정률 격자 로드",
)
def load_grid(
    body: LoadGridRequest,
    db: Session = Depends(get_db),
) -> JSONResponse:
    """DB에서 격자를 로드한다."""
    result = service.load_grid(db=db, camera_id=body.camera_id)
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/get_raw_img",
    summary="원본 이미지 조회",
)
def get_raw_img(body: GetRawImgRequest) -> JSONResponse:
    """메모리에서 원본 이미지 정보를 반환한다."""
    result = service.get_raw_img(unique_id=body.unique_id)
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/save_grid_unit",
    summary="격자 단위 저장",
)
def save_grid_unit(body: SaveGridUnitRequest) -> ResultResponse:
    """격자 단위를 메모리에 저장한다."""
    result = service.save_grid_unit(
        unique_id=body.unique_id,
        grid_unit=body.grid_unit,
    )
    return response(data=result, msg_key="success.update")


@router.post(
    "/point_list_view",
    summary="안전 격자 미리보기",
)
def point_list_view(body: PointListViewRequest) -> JSONResponse:
    """포인트 목록으로 안전 격자를 생성하고 미리보기를 반환한다."""
    result = service.point_list_view(
        point_list=body.point_list,
        image_base64=body.image_base64,
        sort_direction=body.sort_direction,
    )
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })


@router.post(
    "/save_safety_grid",
    summary="안전 격자 저장",
)
def save_safety_grid(
    body: SaveSafetyGridRequest,
    db: Session = Depends(get_db),
) -> ResultResponse:
    """안전 격자를 DB에 저장한다."""
    result = service.save_safety_grid(
        db=db,
        camera_id=body.camera_id,
        sort_direction=body.sort_direction,
        point_list_data=body.point_list_data,
        grid_unit=body.grid_unit,
        set_mode=body.setMode,
        created_by=body.created_by,
        updated_by=body.updated_by,
        unique_id=body.unique_id,
    )
    return response(data=result, msg_key="success.create")


@router.post(
    "/load_safety_grid",
    summary="안전 격자 로드",
)
def load_safety_grid(
    body: LoadSafetyGridRequest,
    db: Session = Depends(get_db),
) -> JSONResponse:
    """DB에서 안전 격자를 로드한다."""
    result = service.load_safety_grid(
        db=db,
        camera_id=body.camera_id,
        image_base64=body.image_base64,
    )
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "조회에 성공하였습니다.",
        "data": result,
    })
