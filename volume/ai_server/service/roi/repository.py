"""ROI 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import select, text
from sqlalchemy.orm import Session

from service.roi.model import CameraRoi


def fetch_rois_by_comp(db: Session, comp_id: str) -> list[CameraRoi]:
    """회사 식별자 기준으로 ROI 목록을 조회한다 (카메라 테이블 JOIN)."""
    stmt = (
        select(CameraRoi)
        .join_from(
            CameraRoi,
            text("tb_camera ON tb_camera_roi.camera_id = tb_camera.camera_id"),
        )
        .where(text("tb_camera.comp_id = :comp_id"))
        .params(comp_id=comp_id)
        .order_by(CameraRoi.camera_id, CameraRoi.model_nm)
    )
    return list(db.scalars(stmt).all())


def upsert_roi(db: Session, roi: CameraRoi) -> CameraRoi:
    """ROI를 등록하거나 수정한다."""
    merged = db.merge(roi)
    db.commit()
    db.refresh(merged)
    return merged
