"""유저 코드에서 회사 ID를 추출하는 유틸리티."""

from __future__ import annotations


def parse_comp_id(user_code: str) -> str:
    """유저 코드(예: 'JEONJIN_manager')에서 회사 ID('JEONJIN')를 추출한다."""
    return (user_code or "").split("_", 1)[0].strip()
