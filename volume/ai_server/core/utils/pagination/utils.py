"""공통 페이지네이션 계산 유틸을 정의한다."""

from __future__ import annotations

from typing import TypeVar

from core.utils.pagination.schema import PageResponse


PageItemT = TypeVar("PageItemT")
VALID_SORT_DIRECTIONS = {"asc", "desc"}



def normalize_sort_direction(sort_direction: str | None) -> str:
    """정렬 방향을 asc 또는 desc로 정규화한다."""
    if not sort_direction:
        return "asc"

    normalized = sort_direction.strip().lower()
    if normalized in VALID_SORT_DIRECTIONS:
        return normalized
    return "asc"



def calculate_offset(page: int, size: int) -> int:
    """페이지와 크기를 기준으로 조회 시작 위치를 계산한다."""
    normalized_page = page if page > 0 else 1
    normalized_size = size if size > 0 else 10
    return (normalized_page - 1) * normalized_size



def calculate_total_page(total: int, size: int) -> int:
    """전체 데이터 수와 페이지 크기를 기준으로 전체 페이지 수를 계산한다."""
    if total <= 0:
        return 0

    normalized_size = size if size > 0 else 10
    return (total + normalized_size - 1) // normalized_size



def build_page_response(
    *,
    items: list[PageItemT],
    total: int,
    page: int = 1,
    size: int = 10,
    keyword: str | None = None,
    sort_by: str | None = None,
    sort_direction: str | None = None,
) -> PageResponse[PageItemT]:
    """페이지 응답 스키마를 생성한다."""
    normalized_page = page if page > 0 else 1
    normalized_size = size if size > 0 else 10
    normalized_sort_direction = normalize_sort_direction(sort_direction)

    return PageResponse[PageItemT](
        items=items,
        total=total,
        page=normalized_page,
        size=normalized_size,
        total_page=calculate_total_page(total, normalized_size),
        keyword=keyword,
        sort_by=sort_by,
        sort_direction=normalized_sort_direction,
    )
