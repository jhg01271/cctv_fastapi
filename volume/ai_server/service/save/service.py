"""학습 이미지 데이터 관리 비즈니스 로직을 정의한다.

JIT 이미지 디렉토리 구조:
  {JIT_IMAGE_DIR}/{camera_id}/short/   — 10분 주기
  {JIT_IMAGE_DIR}/{camera_id}/long/    — 60분 주기
  {JIT_IMAGE_DIR}/{camera_id}/auto/    — 자동 변화 감지
  {JIT_IMAGE_DIR}/{camera_id}/zip/     — ZIP 파일
"""

from __future__ import annotations

import shutil
import zipfile
from datetime import datetime
from pathlib import Path

from core.exception.custom_exception import NotFoundException, BadRequestException
from core.logging.logger import get_logger
from config.config import settings

logger = get_logger(__name__)

JIT_IMAGE_DIR = Path(getattr(settings, "JIT_IMAGE_DIR", "./jit_images"))


def _period_dir(camera_id: str, collection_period: str) -> Path:
    """수집 주기별 이미지 디렉토리 경로를 반환한다."""
    return JIT_IMAGE_DIR / camera_id / collection_period


def _zip_dir(camera_id: str) -> Path:
    """카메라별 ZIP 저장 디렉토리 경로를 반환한다."""
    d = JIT_IMAGE_DIR / camera_id / "zip"
    d.mkdir(parents=True, exist_ok=True)
    return d


def _collect_images(img_dir: Path) -> list[Path]:
    """디렉토리 내 이미지 파일을 재귀적으로 수집한다."""
    if not img_dir.exists():
        return []
    return sorted(
        [f for f in img_dir.rglob("*") if f.is_file() and f.suffix.lower() in (".jpg", ".jpeg", ".png")],
        key=lambda f: f.stat().st_mtime,
    )


def get_server_capacity(camera_id: str) -> dict:
    """서버 저장 용량 정보를 반환한다.

    프론트엔드 기대 형식: { disk: { used_tb, total_tb } }
    """
    base = JIT_IMAGE_DIR if JIT_IMAGE_DIR.exists() else Path(".")
    total, used, free = shutil.disk_usage(base)
    return {
        "disk": {
            "total_tb": round(total / (1024**4), 4),
            "used_tb": round(used / (1024**4), 4),
            "free_tb": round(free / (1024**4), 4),
        },
    }


def get_data_state(camera_id: str, data: dict) -> dict:
    """카메라 이미지 데이터 상태를 반환한다.

    프론트엔드 기대 형식: { code, msg, start_collect, end_collect, total_img }
    """
    period = data.get("collection_period", "short")
    img_dir = _period_dir(camera_id, period)
    files = _collect_images(img_dir)

    if not files:
        return {
            "code": "200",
            "msg": "데이터가 없습니다.",
            "start_collect": "",
            "end_collect": "",
            "total_img": 0,
        }

    first_time = datetime.fromtimestamp(files[0].stat().st_mtime)
    last_time = datetime.fromtimestamp(files[-1].stat().st_mtime)

    return {
        "code": "200",
        "msg": "성공하였습니다.",
        "start_collect": first_time.strftime("%Y-%m-%d %H:%M:%S"),
        "end_collect": last_time.strftime("%Y-%m-%d %H:%M:%S"),
        "total_img": len(files),
    }


def create_zip(camera_id: str, data: dict) -> dict:
    """카메라 이미지를 ZIP으로 압축한다."""
    period = data.get("collection_period", "short")
    img_dir = _period_dir(camera_id, period)
    files = _collect_images(img_dir)

    if not files:
        raise BadRequestException(msg="압축할 이미지가 없습니다.")

    zip_d = _zip_dir(camera_id)
    zip_name = f"{camera_id}_{period}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.zip"
    zip_path = zip_d / zip_name

    with zipfile.ZipFile(zip_path, "w", zipfile.ZIP_DEFLATED) as zf:
        for f in files:
            zf.write(f, f.name)

    logger.info("ZIP created: camera_id=%s path=%s files=%d", camera_id, zip_path, len(files))
    return {"message": "Successfully created zip on remote server", "camera_id": camera_id, "zip_name": zip_name, "file_count": len(files)}


def delete_images(camera_id: str, data: dict) -> dict:
    """카메라 이미지 파일을 삭제한다."""
    period = data.get("collection_period", "short")
    img_dir = _period_dir(camera_id, period)
    files = _collect_images(img_dir)

    for f in files:
        f.unlink()

    logger.info("Images deleted: camera_id=%s period=%s count=%d", camera_id, period, len(files))
    return {"message": "Successfully deleted images on remote server", "deleted_count": len(files)}


def get_zip_list(camera_id: str, data: dict) -> list[dict]:
    """ZIP 파일 목록을 반환한다.

    프론트엔드 기대 형식: { msg, zip_files: [{ create_zip_date, file_name, file_size_gb }] }
    """
    zip_d = _zip_dir(camera_id)
    zips = sorted(zip_d.glob("*.zip"), key=lambda f: f.stat().st_mtime, reverse=True)
    return [
        {
            "create_zip_date": datetime.fromtimestamp(z.stat().st_mtime).strftime("%Y-%m-%d %H:%M:%S"),
            "file_name": z.name,
            "file_size_gb": round(z.stat().st_size / (1024**3), 2),
        }
        for z in zips
    ]


def download_zip(camera_id: str, data: dict) -> str:
    """ZIP 파일 경로를 반환한다."""
    zip_name = data.get("file_name")
    if not zip_name:
        raise BadRequestException(msg="file_name은 필수입니다.")

    zip_path = _zip_dir(camera_id) / zip_name
    if not zip_path.exists():
        raise NotFoundException(msg=f"ZIP 파일을 찾을 수 없습니다. {zip_name}")

    return str(zip_path)


def delete_zip(camera_id: str, data: dict) -> dict:
    """ZIP 파일을 삭제한다."""
    zip_name = data.get("file_name")
    if not zip_name:
        raise BadRequestException(msg="file_name은 필수입니다.")

    zip_path = _zip_dir(camera_id) / zip_name
    if not zip_path.exists():
        raise NotFoundException(msg=f"ZIP 파일을 찾을 수 없습니다. {zip_name}")

    zip_path.unlink()
    logger.info("ZIP deleted: camera_id=%s zip=%s", camera_id, zip_name)
    return {"camera_id": camera_id, "file_name": zip_name, "deleted": True}
