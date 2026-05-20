"""CCTV 카메라 관리 라우터 — 프론트엔드 /cctv/cctv_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.cctv.schema import CameraCreate, CameraDelete, CameraRead
from service.cctv import service

router = APIRouter(prefix="/cctv/cctv_crud", tags=["cctv_crud"])


@router.get(
    "/cctvs/{comp_id}",
    summary="카메라 목록 조회",
    response_model=ResultResponse[list[CameraRead]],
)
def get_cameras(
    comp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[CameraRead]]:
    """회사별 카메라 목록을 조회한다."""
    result = service.list_cameras_by_comp(db, parse_comp_id(comp_id))
    return response(data=result, msg_key="success.read")


@router.get(
    "/cctv/{camera_id}",
    summary="단일 카메라 조회",
    response_model=ResultResponse[CameraRead],
)
def get_camera(
    camera_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[CameraRead]:
    """단일 카메라 정보를 조회한다."""
    result = service.get_camera(db, camera_id)
    return response(data=result, msg_key="success.read")


@router.get(
    "/cctv_server/{camera_id}",
    summary="카메라 ID로 서버 정보 조회",
    response_model=ResultResponse[dict],
)
def get_server_by_camera(
    camera_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """카메라 ID로 연결된 AI 서버 정보를 조회한다."""
    result = service.get_server_by_camera(db, camera_id)
    return response(data=result, msg_key="success.read")


@router.post(
    "/cctv",
    summary="카메라 등록/수정",
    response_model=ResultResponse[list[CameraRead]],
)
def save_camera(
    body: CameraCreate,
    db: Session = Depends(get_db),
) -> ResultResponse[list[CameraRead]]:
    """카메라를 등록하거나 수정한다."""
    result = service.save_camera(db, body.model_dump())
    return response(data=result, msg_key="success.create")


@router.delete(
    "/cctv",
    summary="카메라 삭제",
    response_model=ResultResponse[dict],
)
def delete_camera(
    body: CameraDelete,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """카메라를 삭제한다."""
    deleted = service.remove_camera(db, body.camera_id)
    return response(data={"deleted": deleted}, msg_key="success.delete")
