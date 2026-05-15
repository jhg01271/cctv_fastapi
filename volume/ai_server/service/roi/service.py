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
                "camera_nm": row.get("camera_nm", ""),
                "camera_desc": row.get("camera_desc", ""),
                "model_list": [],
            }
        grouped[cid]["model_list"].append({
            "model_nm": row["model_nm"],
            "point_arr": row["point"],
            "is_run": row["is_run"],
        })

    return list(grouped.values())


def save_roi(db: Session, data: dict) -> list[dict]:
    """ROI를 등록하거나 수정한다. model_list 배열 형태를 지원한다."""
    now = datetime.now()
    camera_id = data.get("cctv_id") or data.get("camera_id")
    model_list = data.get("model_list", [])

    if not camera_id:
        raise BadRequestException(msg="cctv_id는 필수입니다.")

    if not model_list:
        raise BadRequestException(msg="model_list는 필수입니다.")

    results = []
    for model in model_list:
        model_nm = model.get("model_nm")
        user_cd = model.get("userCd", "system")

        existing = db.get(CameraRoi, (camera_id, model_nm))
        if existing:
            existing.point = model.get("point_arr", existing.point)
            existing.is_run = model.get("is_run", existing.is_run)
            existing.updated_at = now
            existing.updated_by = user_cd
            upsert_roi(db, existing)
        else:
            roi = CameraRoi(
                camera_id=camera_id,
                model_nm=model_nm,
                point=model.get("point_arr", "{}"),
                is_run=model.get("is_run", False),
                created_at=now,
                created_by=user_cd,
            )
            upsert_roi(db, roi)

        results.append({"cctv_id": camera_id, "model_nm": model_nm})

    return results


def capture_cctv_image(db: Session, camera_id: str) -> dict:
    """카메라 RTSP에서 한 프레임을 캡처해 base64 인코딩 이미지와 크기 정보를 반환한다."""
    import base64
    import math
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

    height, width = frame.shape[:2]

    _, buffer = cv2.imencode(".jpg", frame)
    img_base64 = base64.b64encode(buffer).decode("utf-8")

    gcd = math.gcd(width, height)
    ratio = f"{width // gcd}:{height // gcd}"

    logger.info("CCTV image captured: camera_id=%s size=%dx%d", camera_id, width, height)

    return {
        "img_decode_data": img_base64,
        "img_size": {"width": width, "height": height},
        "ratio": ratio,
    }
