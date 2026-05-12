"""사용자 수동 로그를 공통 형식으로 남기기 위한 헬퍼를 제공한다."""

from __future__ import annotations

import logging
from typing import Any


def normalize_log_level(level: int | str) -> int:
    """입력된 로그 레벨 값을 logging 표준 레벨로 정규화한다."""
    if isinstance(level, int):
        return level

    normalized = level.strip().upper()
    if normalized == "DEBUG":
        return logging.DEBUG
    if normalized == "INFO":
        return logging.INFO
    if normalized in {"WARN", "WARNING"}:
        return logging.WARNING
    if normalized == "ERROR":
        return logging.ERROR
    if normalized == "CRITICAL":
        return logging.CRITICAL
    return logging.INFO


def build_log_payload(
    *,
    event_type: str,
    message: str,
    extra: dict[str, Any],
) -> tuple[str, dict[str, Any]]:
    """공통 로그 메시지와 구조화 extra payload를 생성한다."""
    filtered_extra = {key: value for key, value in extra.items() if value is not None}

    message_parts = [
        f"event_type={event_type}",
        f"message={message}",
    ]
    if filtered_extra:
        message_parts.append(f"extra={filtered_extra}")

    payload = {
        "event_type": event_type,
        "event_message": message,
        **filtered_extra,
    }
    return " | ".join(message_parts), payload


def log_event(
    *,
    logger: logging.Logger,
    level: int | str,
    message: str,
    event_type: str,
    **extra: Any,
) -> None:
    """프로젝트 공통 형식으로 사용자 이벤트 로그를 남긴다."""
    normalized_level = normalize_log_level(level)
    log_message, payload = build_log_payload(
        event_type=event_type,
        message=message,
        extra=extra,
    )
    logger.log(normalized_level, log_message, extra=payload)
