"""
[Repository (레포지토리) - ROI 도메인]
1. 도메인 기능: "안전 감시 구역 설정 (Region Of Interest)"
   - 사용자가 화면에 그린 위험 구역의 다각형 좌표 정보 및 감시 대상 AI 모델 설정(안전모 미착용 감지, 작업자 쓰러짐 등)을 관리합니다.
2. 아키텍처 역할: 데이터베이스(DB)와의 통신(SQL 쿼리 실행)을 전담하는 매개체입니다.
3. 흐름: Service ➡️ Repository ➡️ DB (Model)
4. 설명: SQLAlchemy ORM 모델인 Camera와 CameraRoi 객체를 활용해 실제 DB 테이블(tb_camera_roi, tb_camera)의 데이터를 조회하거나
      사용자가 등록/수정한 ROI 설정 정보와 활성화 여부(is_run)를 DB에 반영(Upsert)하는 데이터 액세스 역할을 전담합니다.
"""



from __future__ import annotations

from sqlalchemy import select
from sqlalchemy.orm import Session

from service.cctv.model import Camera
from service.roi.model import CameraRoi


def fetch_rois_by_comp(db: Session, comp_id: str) -> list[dict]:
    """회사 식별자 기준으로 ROI 목록을 조회한다 (카메라 이름 포함).
    
    [DB 연동 및 JOIN 설명]:
    1. CameraRoi 모델(tb_camera_roi 테이블)과 Camera 모델(tb_camera 테이블)을
       camera_id 기준으로 inner join 처리합니다.
    2. tb_camera 테이블로부터 카메라 이름(camera_nm) 및 상세설명(camera_desc) 데이터를 매핑하여
       ROI 설정 정보와 함께 최종 가공용 리스트로 획득해 옵니다.
    """
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
