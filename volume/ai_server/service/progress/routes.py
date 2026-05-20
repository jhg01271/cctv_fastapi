"""공정률 라우터 — REST API."""

from __future__ import annotations

from fastapi import APIRouter, Depends, Query
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.exception.custom_exception import NotFoundException
from core.response.response import ResultResponse, response
from core.utils.pagination.schema import PageResponse
from service.progress.schema import ProgressCameraRead, ProgressResultRead
from service.progress import service

router = APIRouter(prefix="/progress", tags=["공정률 AI"])


@router.get(
    "/cameras",
    summary="공정률 대상 카메라 목록",
    response_model=ResultResponse[list[ProgressCameraRead]],
)
def list_cameras(db: Session = Depends(get_db)) -> ResultResponse[list[ProgressCameraRead]]:
    """DB에 설정된 공정률 처리 대상 카메라 목록을 조회한다."""
    result = service.list_cameras(db)
    return response(data=result, msg_key="success.read")


@router.get(
    "/results",
    summary="공정률 결과 이미지 이력 조회",
    description="공정률 AI 처리 결과 이미지 이력을 최신순으로 페이지 단위로 조회한다.",
    response_model=ResultResponse[PageResponse[ProgressResultRead]],
)
def list_results(
    camera_id: str | None = Query(default=None, description="카메라 ID 필터"),
    page: int = Query(default=1, ge=1, description="페이지 번호"),
    size: int = Query(default=20, ge=1, le=100, description="페이지 크기"),
    db: Session = Depends(get_db),
) -> ResultResponse[PageResponse[ProgressResultRead]]:
    """공정률 AI 처리 결과 이미지 이력을 최신순으로 페이지 단위로 조회한다."""
    result = service.list_results(db, camera_id, page=page, size=size)
    return response(data=result, msg_key="success.read")


@router.get(
    "/results/latest/image",
    summary="최신 공정률 결과 이미지 파일 반환",
)
def get_latest_image(
    camera_id: str = Query(description="카메라 ID"),
    db: Session = Depends(get_db),
) -> FileResponse:
    """카메라의 가장 최근 공정률 결과 이미지 파일을 반환한다."""
    page_result = service.list_results(db, camera_id, page=1, size=1)
    if not page_result.items:
        raise NotFoundException(msg=f"공정률 결과 이미지가 없습니다. camera_id={camera_id}")
    return FileResponse(
        path=page_result.items[0].image_path,
        media_type="image/jpeg",
        filename=f"{camera_id}_latest.jpg",
    )
