"""날짜/시간 포맷 공통 유틸을 정의한다."""

from __future__ import annotations

from datetime import datetime


DEFAULT_DATETIME_FORMAT = "%Y.%m.%d %H:%M:%S"



def format_datetime(
    value: datetime | None,
    *,
    pattern: str = DEFAULT_DATETIME_FORMAT,
) -> str | None:
    """일시 값을 지정된 문자열 형식으로 변환한다."""
    if value is None:
        return None
    return value.strftime(pattern)
