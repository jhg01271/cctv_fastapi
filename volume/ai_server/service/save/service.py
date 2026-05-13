"""이벤트 이미지 데이터 관리 비즈니스 로직을 정의한다."""

from __future__ import annotations

import os
import shutil
import zipfile
from datetime import datetime
from pathlib import Path

from core.exception.custom_exception import NotFoundException, BadRequestException
from core.logging.logger import get_logger
from config.config import settings

logger = get_logger(__name__)

EVENT_IMAGE_DIR = Path(getattr(settings, "EVENT_IMAGE_DIR", "./event_images"))


def _camera_dir(camera_id: str) -> Path:
    """카메라별 이미지 저장 디렉토리 경로를 반환한다."""
    return EVENT_IMAGE_DIR / camera_id


def _zip_dir(camera_id: str) -> Path:
    """카메라별 ZIP 저장 디렉토리 경로를 반환한다."""
    d = EVENT_IMAGE_DIR / camera_id / "zip"
    d.mkdir(parents=True, exist_ok=True)
    return d


def get_server_capacity(camera_id: str) -> dict:
    """서버 저장 용량 정보를 반환한다."""
    cam_dir = _camera_dir(camera_id)
    total, used, free = shutil.disk_usage(cam_dir if cam_dir.exists() else EVENT_IMAGE_DIR)
    return {
        "camera_id": camera_id,
        "total_gb": round(total / (1024**3), 2),
        "used_gb": round(used / (1024**3), 2),
        "free_gb": round(free / (1024**3), 2),
    }


def get_data_state(camera_id: str, data: dict) -> dict:
    """카메라 이미지 데이터 상태(파일 수, 용량)를 반환한다."""
    cam_dir = _camera_dir(camera_id)
    if not cam_dir.exists():
        return {"camera_id": camera_id, "file_count": 0, "size_mb": 0}

    files = [f for f in cam_dir.iterdir() if f.is_file() and f.suffix in (".jpg", ".jpeg", ".png")]
    total_size = sum(f.stat().st_size for f in files)
    return {
        "camera_id": camera_id,
        "file_count": len(files),
        "size_mb": round(total_size / (1024**2), 2),
    }


def create_zip(camera_id: str, data: dict) -> dict:
    """카메라 이미지를 ZIP으로 압축한다."""
    cam_dir = _camera_dir(camera_id)
    if not cam_dir.exists():
        raise NotFoundException(msg=f"이미지 디렉토리가 없습니다. camera_id={camera_id}")

    files = [f for f in cam_dir.iterdir() if f.is_file() and f.suffix in (".jpg", ".jpeg", ".png")]
    if not files:
        raise BadRequestException(msg="압축할 이미지가 없습니다.")

    zip_dir = _zip_dir(camera_id)
    zip_name = f"{camera_id}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.zip"
    zip_path = zip_dir / zip_name

    with zipfile.ZipFile(zip_path, "w", zipfile.ZIP_DEFLATED) as zf:
        for f in files:
            zf.write(f, f.name)

    logger.info("ZIP created: camera_id=%s path=%s files=%d", camera_id, zip_path, len(files))
    return {"camera_id": camera_id, "zip_name": zip_name, "file_count": len(files)}


def delete_images(camera_id: str, data: dict) -> dict:
    """카메라 이미지 파일을 삭제한다."""
    cam_dir = _camera_dir(camera_id)
    if not cam_dir.exists():
        return {"camera_id": camera_id, "deleted_count": 0}

    files = [f for f in cam_dir.iterdir() if f.is_file() and f.suffix in (".jpg", ".jpeg", ".png")]
    for f in files:
        f.unlink()

    logger.info("Images deleted: camera_id=%s count=%d", camera_id, len(files))
    return {"camera_id": camera_id, "deleted_count": len(files)}


def get_zip_list(camera_id: str, data: dict) -> list[dict]:
    """ZIP 파일 목록을 반환한다."""
    zip_dir = _zip_dir(camera_id)
    zips = sorted(zip_dir.glob("*.zip"), key=lambda f: f.stat().st_mtime, reverse=True)
    return [
        {
            "zip_name": z.name,
            "size_mb": round(z.stat().st_size / (1024**2), 2),
            "created_at": datetime.fromtimestamp(z.stat().st_mtime).strftime("%Y-%m-%d %H:%M:%S"),
        }
        for z in zips
    ]


def download_zip(camera_id: str, data: dict) -> str:
    """ZIP 파일 경로를 반환한다."""
    zip_name = data.get("zip_name")
    if not zip_name:
        raise BadRequestException(msg="zip_name은 필수입니다.")

    zip_path = _zip_dir(camera_id) / zip_name
    if not zip_path.exists():
        raise NotFoundException(msg=f"ZIP 파일을 찾을 수 없습니다. {zip_name}")

    return str(zip_path)


def delete_zip(camera_id: str, data: dict) -> dict:
    """ZIP 파일을 삭제한다."""
    zip_name = data.get("zip_name")
    if not zip_name:
        raise BadRequestException(msg="zip_name은 필수입니다.")

    zip_path = _zip_dir(camera_id) / zip_name
    if not zip_path.exists():
        raise NotFoundException(msg=f"ZIP 파일을 찾을 수 없습니다. {zip_name}")

    zip_path.unlink()
    logger.info("ZIP deleted: camera_id=%s zip=%s", camera_id, zip_name)
    return {"camera_id": camera_id, "zip_name": zip_name, "deleted": True}
