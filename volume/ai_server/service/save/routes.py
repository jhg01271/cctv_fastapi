"""학습 이미지 데이터 관리 라우터 — 프론트엔드 /cctv/dt_crud_remote 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter
from fastapi.responses import FileResponse, JSONResponse

from service.save import service
from service.save.schema import CollectionPeriodRequest, ZipFileRequest

router = APIRouter(prefix="/cctv/dt_crud_remote", tags=["학습 데이터 관리"])


@router.get(
    "/get_remote_server_cap/{camera_id}",
    summary="서버 저장 용량 조회",
)
def get_remote_server_cap(camera_id: str) -> JSONResponse:
    """서버 디스크 용량 정보를 반환한다."""
    result = service.get_server_capacity(camera_id)
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "성공하였습니다.",
        "data": result,
    })


@router.post(
    "/get_remote_data_state/{camera_id}",
    summary="이미지 데이터 상태 조회",
)
def get_remote_data_state(camera_id: str, body: CollectionPeriodRequest = CollectionPeriodRequest()) -> JSONResponse:
    """카메라 이미지 파일 수/용량 상태를 반환한다."""
    result = service.get_data_state(camera_id, body.model_dump())
    return JSONResponse(content={
        "success": True,
        **result,
    })


@router.post(
    "/create_remote_zip/{camera_id}",
    summary="이미지 ZIP 압축 생성",
)
def create_remote_zip(camera_id: str, body: CollectionPeriodRequest = CollectionPeriodRequest()) -> JSONResponse:
    """카메라 이미지를 ZIP으로 압축한다."""
    result = service.create_zip(camera_id, body.model_dump())
    return JSONResponse(content={
        "success": True,
        **result,
    })


@router.post(
    "/delete_remote_images/{camera_id}",
    summary="이미지 파일 삭제",
)
def delete_remote_images(camera_id: str, body: CollectionPeriodRequest = CollectionPeriodRequest()) -> JSONResponse:
    """카메라 이미지 파일을 삭제한다."""
    result = service.delete_images(camera_id, body.model_dump())
    return JSONResponse(content={
        "success": True,
        **result,
    })


@router.post(
    "/get_remote_zip_list/{camera_id}",
    summary="ZIP 파일 목록 조회",
)
def get_remote_zip_list(camera_id: str, body: CollectionPeriodRequest = CollectionPeriodRequest()) -> JSONResponse:
    """ZIP 파일 목록을 반환한다."""
    result = service.get_zip_list(camera_id, body.model_dump())
    return JSONResponse(content={
        "success": True,
        "code": "200",
        "msg": "성공하였습니다.",
        "zip_files": result,
    })


@router.post(
    "/download_remote_zip/{camera_id}",
    summary="ZIP 파일 다운로드",
)
def download_remote_zip(camera_id: str, body: ZipFileRequest) -> FileResponse:
    """ZIP 파일을 다운로드한다."""
    file_path = service.download_zip(camera_id, body.model_dump())
    return FileResponse(path=file_path, media_type="application/zip")


@router.post(
    "/delete_remote_zip/{camera_id}",
    summary="ZIP 파일 삭제",
)
def delete_remote_zip(camera_id: str, body: ZipFileRequest) -> JSONResponse:
    """ZIP 파일을 삭제한다."""
    result = service.delete_zip(camera_id, body.model_dump())
    return JSONResponse(content={
        "success": True,
        "code": "200",
        **result,
    })
