"""
[Service (서비스) - Safety 도메인]
1. 도메인 기능: "실시간 안전 탐지 및 위기 상황 알람"
   - CCTV 영상을 실시간 분석하여 위험 이벤트가 감지되었을 때 프론트엔드로 실시간 탐지 결과 데이터를 끊임없이 전달합니다.
2. 아키텍처 역할: 핵심 비즈니스 로직과 알고리즘을 수행하는 두뇌 역할을 합니다.
3. 흐름: Router ➡️ Service ➡️ Repository
4. 설명: 프론트엔드의 요청에 따라 과거에 검출되었던 위험 이벤트 이력 데이터를 페이지 단위로 자르고 페이징 연산(offset, size 계산)을 거쳐 
      Repository에 데이터를 요청하고, 최종 가공된 이력 응답(PageResponse)을 형성하여 반환하는 로직을 수행합니다.
"""


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
