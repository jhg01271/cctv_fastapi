"""이벤트 이미지 데이터 관리 라우터 — 프론트엔드 /cctv/dt_crud_remote 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Request
from fastapi.responses import FileResponse

from core.response.response import ResultResponse, response
from service.save import service

router = APIRouter(prefix="/cctv/dt_crud_remote", tags=["dt_crud_remote"])


@router.get(
    "/get_remote_server_cap/{camera_id}",
    summary="서버 저장 용량 조회",
    response_model=ResultResponse[dict],
)
def get_remote_server_cap(camera_id: str) -> ResultResponse[dict]:
    """서버 디스크 용량 정보를 반환한다."""
    result = service.get_server_capacity(camera_id)
    return response(data=result, msg_key="success.read")


@router.post(
    "/get_remote_data_state/{camera_id}",
    summary="이미지 데이터 상태 조회",
    response_model=ResultResponse[dict],
)
async def get_remote_data_state(camera_id: str, request: Request) -> ResultResponse[dict]:
    """카메라 이미지 파일 수/용량 상태를 반환한다."""
    body = await request.json()
    result = service.get_data_state(camera_id, body)
    return response(data=result, msg_key="success.read")


@router.post(
    "/create_remote_zip/{camera_id}",
    summary="이미지 ZIP 압축 생성",
    response_model=ResultResponse[dict],
)
async def create_remote_zip(camera_id: str, request: Request) -> ResultResponse[dict]:
    """카메라 이미지를 ZIP으로 압축한다."""
    body = await request.json()
    result = service.create_zip(camera_id, body)
    return response(data=result, msg_key="success.create")


@router.post(
    "/delete_remote_images/{camera_id}",
    summary="이미지 파일 삭제",
    response_model=ResultResponse[dict],
)
async def delete_remote_images(camera_id: str, request: Request) -> ResultResponse[dict]:
    """카메라 이미지 파일을 삭제한다."""
    body = await request.json()
    result = service.delete_images(camera_id, body)
    return response(data=result, msg_key="success.delete")


@router.post(
    "/get_remote_zip_list/{camera_id}",
    summary="ZIP 파일 목록 조회",
    response_model=ResultResponse[list[dict]],
)
async def get_remote_zip_list(camera_id: str, request: Request) -> ResultResponse[list[dict]]:
    """ZIP 파일 목록을 반환한다."""
    body = await request.json()
    result = service.get_zip_list(camera_id, body)
    return response(data=result, msg_key="success.read")


@router.post(
    "/download_remote_zip/{camera_id}",
    summary="ZIP 파일 다운로드",
)
async def download_remote_zip(camera_id: str, request: Request) -> FileResponse:
    """ZIP 파일을 다운로드한다."""
    body = await request.json()
    file_path = service.download_zip(camera_id, body)
    return FileResponse(path=file_path, media_type="application/zip")


@router.post(
    "/delete_remote_zip/{camera_id}",
    summary="ZIP 파일 삭제",
    response_model=ResultResponse[dict],
)
async def delete_remote_zip(camera_id: str, request: Request) -> ResultResponse[dict]:
    """ZIP 파일을 삭제한다."""
    body = await request.json()
    result = service.delete_zip(camera_id, body)
    return response(data=result, msg_key="success.delete")
