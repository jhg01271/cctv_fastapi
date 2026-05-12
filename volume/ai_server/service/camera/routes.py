"""camera 도메인의 라우트를 정의한다."""

from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.camera.schema import (
    CameraGroupRead,
    CameraRegisterRequest,
    CameraStatusResponse,
    StreamUrlResponse,
)
from service.camera import service


router = APIRouter(prefix="/cameras", tags=["cameras"])


# ──────────────────────────────────────────────────────────────
# 카메라 그룹 조회
# ──────────────────────────────────────────────────────────────

@router.get(
    "/groups/{user_cd}",
    summary="카메라 그룹 목록 조회",
    description="사용자 코드 기준으로 카메라 그룹 목록을 조회한다.",
    response_model=ResultResponse[list[CameraGroupRead]],
)
def read_camera_groups(
    user_cd: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[CameraGroupRead]]:
    """카메라 그룹 목록 조회 엔드포인트다."""
    result = service.list_camera_groups(db, user_cd)
    return response(data=result, msg_key="success.read")


# ──────────────────────────────────────────────────────────────
# 카메라 프로세스 관리
# ──────────────────────────────────────────────────────────────

@router.get(
    "",
    summary="카메라 프로세스 상태 목록",
    response_model=ResultResponse[list[CameraStatusResponse]],
)
def list_cameras() -> ResultResponse[list[CameraStatusResponse]]:
    """현재 실행 중인 AI 카메라 프로세스 상태를 조회한다."""
    result = service.list_camera_status()
    return response(data=result, msg_key="success.read")


@router.post(
    "",
    summary="카메라 AI 프로세스 시작",
    description="RTSP 리더 + AI 프로세스를 시작한다.",
    status_code=status.HTTP_201_CREATED,
    response_model=ResultResponse[dict],
)
def register_camera(
    body: CameraRegisterRequest,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """RTSP 리더 + AI 프로세스를 시작한다."""
    result = service.register_camera(body.camera_id, body.rtsp_url, db)
    return response(data=result, msg_key="success.create")


@router.delete(
    "/{camera_id}",
    summary="카메라 AI 프로세스 중지",
    response_model=ResultResponse[dict],
)
def remove_camera(camera_id: str) -> ResultResponse[dict]:
    """카메라 AI 프로세스를 종료하고 등록을 해제한다."""
    result = service.remove_camera(camera_id)
    return response(data=result, msg_key="success.delete")


# ──────────────────────────────────────────────────────────────
# 스트림 URL 조회
# ──────────────────────────────────────────────────────────────

@router.get(
    "/{camera_id}/stream",
    summary="카메라 스트림 URL 조회",
    description="프론트엔드에서 영상 시청에 사용할 WebRTC/RTSP URL을 반환한다.",
    response_model=ResultResponse[StreamUrlResponse],
)
def get_stream_url(camera_id: str) -> ResultResponse[StreamUrlResponse]:
    """프론트엔드에서 카메라 영상 시청에 사용할 URL을 반환한다."""
    result = service.get_stream_urls(camera_id)
    return response(data=result, msg_key="success.read")
