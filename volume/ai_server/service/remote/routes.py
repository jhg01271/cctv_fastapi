"""AI 프로세스 원격 제어 라우터 — 프론트엔드 /cctv/remote 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.remote import service

router = APIRouter(prefix="/cctv/remote", tags=["remote"])


@router.get(
    "/run_all",
    summary="전체 카메라 AI 시작",
    response_model=ResultResponse[dict],
)
def run_all(db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """전체 카메라 AI 프로세스를 시작한다."""
    result = service.run_all(db)
    return response(data=result, msg_key="success.read")


@router.get(
    "/stop_all",
    summary="전체 카메라 AI 중지",
    response_model=ResultResponse[dict],
)
def stop_all() -> ResultResponse[dict]:
    """전체 카메라 AI 프로세스를 중지한다."""
    result = service.stop_all()
    return response(data=result, msg_key="success.read")


@router.get(
    "/run_cctv/{camera_id}",
    summary="개별 카메라 AI 시작",
    response_model=ResultResponse[dict],
)
def run_cctv(camera_id: str, db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """개별 카메라 AI 프로세스를 시작���다."""
    result = service.run_cctv(camera_id, db)
    return response(data=result, msg_key="success.read")


@router.get(
    "/stop_cctv/{camera_id}",
    summary="개별 카메라 AI 중지",
    response_model=ResultResponse[dict],
)
def stop_cctv(camera_id: str) -> ResultResponse[dict]:
    """개별 카메라 AI 프로세스를 중지한다."""
    result = service.stop_cctv(camera_id)
    return response(data=result, msg_key="success.read")
