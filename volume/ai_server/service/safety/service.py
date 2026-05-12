"""안전관리 비즈니스 로직."""

from __future__ import annotations

from sqlalchemy.orm import Session

from core.logging.logger import get_logger
from core.utils.pagination.schema import PageResponse
from core.utils.pagination.utils import build_page_response, calculate_offset
from service.safety import repository
from service.safety.schema import EventHistRead

logger = get_logger(__name__)


def list_events(
    db: Session,
    camera_id: str | None = None,
    page: int = 1,
    size: int = 20,
) -> PageResponse[EventHistRead]:
    """이벤트 이력을 페이지 단위로 반환한다."""
    offset = calculate_offset(page, size)
    total = repository.count_event_hist(db, camera_id)
    rows = repository.fetch_event_hist(db, camera_id, offset=offset, size=size)
    items = [EventHistRead(**r) for r in rows]
    return build_page_response(items=items, total=total, page=page, size=size)
