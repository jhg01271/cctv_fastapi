"""ROI 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import select
from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.roi.model import CameraRoi


def fetch_rois_by_comp(db: Session, comp_id: str) -> list[dict]:
    """회사 식별자 기준으로 ROI 목록을 조회한다 (카메라 이름 포함)."""
    stmt = (
        select(CameraRoi, Camera.camera_nm, Camera.camera_desc)
        .join(Camera, CameraRoi.camera_id == Camera.camera_id)
        .where(Camera.comp_id == comp_id)
        .order_by(CameraRoi.camera_id, CameraRoi.model_nm)
    )
    results = []
    for roi, camera_nm, camera_desc in db.execute(stmt).all():
        roi_dict = {
            "camera_id": roi.camera_id,
            "camera_nm": camera_nm,
            "camera_desc": camera_desc,
            "model_nm": roi.model_nm,
            "point": roi.point,
            "is_run": roi.is_run,
            "created_at": roi.created_at,
            "created_by": roi.created_by,
            "updated_at": roi.updated_at,
            "updated_by": roi.updated_by,
        }
        results.append(roi_dict)
    return results


def upsert_roi(db: Session, roi: CameraRoi) -> CameraRoi:
    """ROI를 등록하거나 수정한다."""
    merged = db.merge(roi)
    db.commit()
    db.refresh(merged)
    return merged
