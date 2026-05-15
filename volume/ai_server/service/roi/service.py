"""ROI 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

import cv2
from datetime import datetime
from pathlib import Path

from sqlalchemy.orm import Session

from core.exception.custom_exception import BadRequestException, NotFoundException
from core.logging.logger import get_logger
from service.roi.model import CameraRoi
from service.roi.repository import fetch_rois_by_comp, upsert_roi

logger = get_logger(__name__)


def list_rois(db: Session, comp_id: str) -> list[dict]:
    """회사별 ROI 목록을 카메라 기준으로 그룹핑하여 반환한다."""
    rows = fetch_rois_by_comp(db, comp_id)

    grouped: dict[str, dict] = {}
    for row in rows:
        cid = row["camera_id"]
        if cid not in grouped:
            grouped[cid] = {
                "cctv_id": cid,
                "cctv_name": row.get("camera_nm", ""),
                "model_list": [],
            }
        grouped[cid]["model_list"].append({
            "model_nm": row["model_nm"],
            "point_arr": row["point"],
            "is_run": row["is_run"],
        })

    return list(grouped.values())


def save_roi(db: Session, data: dict) -> CameraRoi:
    """ROI를 등록하거나 수정한다."""
    now = datetime.now()
    camera_id = data.get("camera_id")
    model_nm = data.get("model_nm")

    if not camera_id or not model_nm:
        raise BadRequestException(msg="camera_id와 model_nm은 필수입니다.")

    existing = db.get(CameraRoi, (camera_id, model_nm))

    if existing:
        existing.point = data.get("point", existing.point)
        existing.is_run = data.get("is_run", existing.is_run)
        existing.updated_at = now
        existing.updated_by = data.get("updated_by")
        return upsert_roi(db, existing)

    roi = CameraRoi(
        camera_id=camera_id,
        model_nm=model_nm,
        point=data.get("point", "{}"),
        is_run=data.get("is_run", False),
        created_at=now,
        created_by=data.get("created_by", "system"),
    )
    return upsert_roi(db, roi)


def capture_cctv_image(db: Session, camera_id: str) -> str:
    """카메라 RTSP에서 한 프레임을 캡처해 이미지 파일 경로를 반환한다."""
    from service.cctv.model import Camera

    camera = db.get(Camera, camera_id)

    if not camera or not camera.rtsp_addr:
        raise NotFoundException(msg=f"카메라를 찾을 수 없습니다. camera_id={camera_id}")

    rtsp_url = camera.rtsp_addr
    cap = cv2.VideoCapture(rtsp_url)

    if not cap.isOpened():
        raise BadRequestException(msg=f"RTSP 연결에 실패했습니다. camera_id={camera_id}")

    ret, frame = cap.read()
    cap.release()

    if not ret:
        raise BadRequestException(msg=f"프레임 캡처에 실패했습니다. camera_id={camera_id}")

    save_dir = Path("./capture_images")
    save_dir.mkdir(parents=True, exist_ok=True)
    file_path = save_dir / f"{camera_id}.jpg"
    cv2.imwrite(str(file_path), frame)
    logger.info("CCTV image captured: camera_id=%s path=%s", camera_id, file_path)

    return str(file_path)
