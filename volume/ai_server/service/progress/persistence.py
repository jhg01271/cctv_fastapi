"""공정률 저장 전용 비즈니스 로직.

repository는 SQL 실행만 담당하고,
이 모듈이 트랜잭션 경계를 관리한다.
"""

from __future__ import annotations

from sqlalchemy.orm import Session

from core.database.transaction import transactional
from service.progress import repository


@transactional()
def save_progress_result(
    db: Session,
    camera_id: str,
    image_path: str,
    timestamp: str,
    detections: list[dict],
) -> int | None:
    """공정률 결과 이미지와 detection 레코드를 한 트랜잭션으로 저장한다."""
    result_id = repository.save_result_record(db, camera_id, image_path, timestamp)
    if result_id is None:
        return None

    repository.save_detection_records(
        db=db,
        result_id=result_id,
        camera_id=camera_id,
        detections=detections,
        timestamp=timestamp,
    )
    return result_id
