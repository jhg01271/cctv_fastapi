"""메시지 조회와 언어 해석 로직을 정의한다."""

from __future__ import annotations

from .catalog import DEFAULT_MESSAGE_KEY_BY_STATUS, LEGACY_MESSAGE_KEY_ALIASES, MESSAGES, MessageEntry


DEFAULT_LANGUAGE = "ko"
SUPPORTED_LANGUAGES = {"ko", "en"}
DEFAULT_MESSAGE_KEY = "error.http"
DEFAULT_SUCCESS_MESSAGE_KEY = "success.default"


def normalize_language(language: str | None) -> str:
    """입력된 언어 값을 지원 언어로 정규화한다."""
    if not language:
        return DEFAULT_LANGUAGE

    for token in language.split(","):
        candidate = token.split(";")[0].strip().lower()
        if not candidate:
            continue
        base_language = candidate.split("-")[0]
        if base_language in SUPPORTED_LANGUAGES:
            return base_language

    return DEFAULT_LANGUAGE


def normalize_msg_key(msg_key: str | None = None, message_key: str | None = None) -> str | None:
    """공식 메시지 키를 기준으로 레거시 키와 별칭을 정규화한다."""
    candidate = msg_key or message_key
    if candidate is None:
        return None
    return LEGACY_MESSAGE_KEY_ALIASES.get(candidate, candidate)


def normalize_http_code(code: int | str | None) -> int | None:
    """응답 code 값을 HTTP 상태 코드 정수로 정규화한다."""
    if code is None:
        return None
    if isinstance(code, int):
        return code

    normalized = code.strip()
    if not normalized or not normalized.isdigit():
        return None
    return int(normalized)


def get_message_entry(msg_key: str | None = None, *, message_key: str | None = None) -> MessageEntry:
    """메시지 키에 해당하는 정의를 반환한다."""
    normalized_msg_key = normalize_msg_key(msg_key, message_key)
    if normalized_msg_key is None:
        return MESSAGES[DEFAULT_MESSAGE_KEY]
    return MESSAGES.get(normalized_msg_key, MESSAGES[DEFAULT_MESSAGE_KEY])


def get_message_definition(message_key: str) -> MessageEntry:
    """기존 호환성을 위해 메시지 정의를 반환한다."""
    return get_message_entry(message_key=message_key)


def get_message_status_code(msg_key: str | None = None, *, message_key: str | None = None) -> int:
    """메시지 키에 연결된 HTTP 상태 코드를 반환한다."""
    return get_message_entry(msg_key, message_key=message_key)["status_code"]


def get_message_code(msg_key: str | None = None, *, message_key: str | None = None) -> str:
    """메시지 키에 연결된 바디 code 값을 반환한다."""
    return str(get_message_status_code(msg_key, message_key=message_key))


def get_message_title(msg_key: str | None = None, *, message_key: str | None = None) -> str | None:
    """메시지 키에 연결된 응답 제목을 반환한다."""
    return get_message_entry(msg_key, message_key=message_key)["title"]


def get_message_text(msg_key: str | None = None, language: str | None = None, *, message_key: str | None = None) -> str:
    """메시지 키와 언어에 맞는 문자열을 반환한다."""
    entry = get_message_entry(msg_key, message_key=message_key)
    normalized_language = normalize_language(language)
    locales = entry["locales"]
    return locales.get(normalized_language, locales[DEFAULT_LANGUAGE])


def get_message(msg_key: str | None = None, language: str = DEFAULT_LANGUAGE, *, message_key: str | None = None) -> str:
    """기존 호환성을 위해 메시지 문자열을 반환한다."""
    return get_message_text(msg_key, language, message_key=message_key)


def resolve_message(
    *,
    msg_key: str | None = None,
    message_key: str | None = None,
    language: str,
    msg: str | None = None,
) -> str:
    """직접 전달된 메시지가 있으면 우선하고, 없으면 키 기반 메시지를 반환한다."""
    if msg:
        return msg
    return get_message_text(msg_key, language, message_key=message_key)


def resolve_default_msg_key(code: int | str | None = None) -> str:
    """HTTP code 기준 기본 메시지 키를 반환한다."""
    normalized_code = normalize_http_code(code)
    if normalized_code is None:
        return DEFAULT_SUCCESS_MESSAGE_KEY
    return DEFAULT_MESSAGE_KEY_BY_STATUS.get(normalized_code, DEFAULT_MESSAGE_KEY)


def resolve_default_message_key(code: int | str | None = None) -> str:
    """기존 호환성을 위해 기본 메시지 키를 반환한다."""
    return resolve_default_msg_key(code)


def resolve_message_payload(
    *,
    language: str,
    code: int | str | None = None,
    title: str | None = None,
    msg: str | None = None,
    msg_key: str | None = None,
    message_key: str | None = None,
) -> tuple[int, str, str | None, str, str]:
    """code, 메시지 키, 기본 fallback을 기준으로 최종 응답 메타데이터를 결정한다."""
    normalized_code = normalize_http_code(code)
    resolved_msg_key = normalize_msg_key(msg_key, message_key) or resolve_default_msg_key(normalized_code)
    entry = get_message_entry(resolved_msg_key)
    resolved_status_code = normalized_code if normalized_code is not None else entry["status_code"]
    resolved_code = str(resolved_status_code)
    resolved_title = title if title is not None else entry["title"]
    resolved_msg = resolve_message(
        msg_key=resolved_msg_key,
        language=language,
        msg=msg,
    )
    return resolved_status_code, resolved_code, resolved_title, resolved_msg, resolved_msg_key
