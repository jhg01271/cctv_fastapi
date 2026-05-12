"""공정률 비즈니스 로직."""

from __future__ import annotations

from sqlalchemy.orm import Session

from core.utils.pagination.schema import PageResponse
from core.utils.pagination.utils import build_page_response, calculate_offset
from service.progress import repository
from service.progress.schema import ProgressCameraRead, ProgressResultRead


def list_results(
    db: Session,
    camera_id: str | None = None,
    page: int = 1,
    size: int = 20,
) -> PageResponse[ProgressResultRead]:
    """공정률 결과 이미지 이력을 페이지 단위로 반환한다."""
    offset = calculate_offset(page, size)
    total = repository.count_results(db, camera_id)
    rows = repository.fetch_latest_results(db, camera_id, offset=offset, size=size)
    items = [ProgressResultRead(**r) for r in rows]
    return build_page_response(items=items, total=total, page=page, size=size)


def list_cameras(db: Session) -> list[ProgressCameraRead]:
    """공정률 처리 대상 카메라 목록을 반환한다."""
    rows = repository.fetch_progress_cameras(db)
    return [ProgressCameraRead(**r) for r in rows]
