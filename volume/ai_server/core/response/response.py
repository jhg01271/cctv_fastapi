"""프로젝트 공통 Result 응답 모델과 응답 생성 헬퍼를 정의한다."""

from __future__ import annotations

from typing import Any, Generic, TypeVar

from fastapi.responses import JSONResponse
from pydantic import BaseModel, ConfigDict, Field

from core.utils.messages import DEFAULT_LANGUAGE, normalize_http_code, normalize_msg_key, resolve_message_payload


ResponseDataT = TypeVar("ResponseDataT")
DEFAULT_SUCCESS_CODE = "200"


class ResultResponse(BaseModel, Generic[ResponseDataT]):
    """성공과 실패를 포괄 표현하는 공통 응답 모델이다."""

    model_config = ConfigDict(arbitrary_types_allowed=True)

    success: bool = Field(description="요청 성공 여부")
    code: str = Field(description="HTTP 상태 코드 문자열")
    msg: str = Field(description="응답 메시지")
    data: ResponseDataT | None = Field(default=None, description="응답 데이터 본문")


def is_success_status(code: int | str) -> bool:
    """HTTP code 기준으로 성공 여부를 판별한다."""
    normalized_code = normalize_http_code(code)
    if normalized_code is None:
        return False
    return 200 <= normalized_code < 300


def build_response(
    *,
    code: int | str,
    msg: str,
    data: Any = None,
) -> ResultResponse[Any]:
    """공통 응답 객체를 생성한다."""
    normalized_code = normalize_http_code(code)
    if normalized_code is None:
        raise ValueError("code must be a valid HTTP status code")

    return ResultResponse[Any](
        success=is_success_status(normalized_code),
        code=str(normalized_code),
        msg=msg,
        data=data,
    )


def response(
    data: Any = None,
    *,
    code: int | str | None = None,
    msg: str | None = None,
    msg_key: str | None = None,
    message_key: str | None = None,
    language: str = DEFAULT_LANGUAGE,
) -> ResultResponse[Any] | JSONResponse:
    """code 와 메시지 설정을 기준으로 최종 공통 응답을 생성한다."""
    normalized_msg_key = normalize_msg_key(msg_key, message_key)
    fallback_code = code if code is not None else (DEFAULT_SUCCESS_CODE if normalized_msg_key is None else None)

    _, resolved_code, _, resolved_msg, _ = resolve_message_payload(
        language=language,
        code=fallback_code,
        msg=msg,
        msg_key=normalized_msg_key,
    )

    result = build_response(
        code=resolved_code,
        msg=resolved_msg,
        data=data,
    )
    if result.success:
        return result
    return JSONResponse(status_code=int(result.code), content=result.model_dump())


def success_response(
    data: Any = None,
    *,
    code: int | str | None = None,
    msg: str | None = None,
    msg_key: str | None = None,
    message_key: str | None = None,
    language: str = DEFAULT_LANGUAGE,
) -> ResultResponse[Any] | JSONResponse:
    """성공 응답 생성을 위한 공통 헬퍼다."""
    return response(
        data=data,
        code=code,
        msg=msg,
        msg_key=msg_key,
        message_key=message_key,
        language=language,
    )
