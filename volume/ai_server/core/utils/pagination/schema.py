"""공통 페이지네이션 요청/응답 스키마를 정의한다."""

from __future__ import annotations

from typing import Generic, TypeVar

from pydantic import BaseModel, Field


PageItemT = TypeVar("PageItemT")


class PageRequest(BaseModel):
    """공통 페이지 조회 요청 스키마다."""

    page: int = Field(default=1, ge=1, description="페이지 번호")
    size: int = Field(default=10, ge=1, le=100, description="페이지 크기")
    keyword: str | None = Field(default=None, description="검색 키워드")
    sort_by: str | None = Field(default=None, description="정렬 기준 필드")
    sort_direction: str = Field(default="asc", description="정렬 방향")


class PageResponse(BaseModel, Generic[PageItemT]):
    """공통 페이지 응답 스키마다."""

    items: list[PageItemT] = Field(default_factory=list, description="페이지 데이터 목록")
    total: int = Field(default=0, ge=0, description="전체 데이터 수")
    page: int = Field(default=1, ge=1, description="현재 페이지 번호")
    size: int = Field(default=10, ge=1, description="페이지 크기")
    total_page: int = Field(default=0, ge=0, description="전체 페이지 수")
    keyword: str | None = Field(default=None, description="검색 키워드")
    sort_by: str | None = Field(default=None, description="정렬 기준 필드")
    sort_direction: str = Field(default="asc", description="정렬 방향")
