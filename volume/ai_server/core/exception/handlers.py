"""FastAPI 전역 예외 핸들러를 정의하고 등록한다."""

from __future__ import annotations

import logging
from typing import Any, TypedDict

from fastapi import FastAPI, HTTPException, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

from core.exception.custom_exception import BusinessException
from core.logging.logger import get_logger
from core.response.response import response
from core.utils.messages import normalize_language, resolve_message_payload


logger = get_logger(__name__)
LOG_LEVEL_BY_TYPE: dict[str, int] = {
    "DEBUG": logging.DEBUG,
    "INFO": logging.INFO,
    "WARN": logging.WARNING,
    "ERROR": logging.ERROR,
    "CRITICAL": logging.CRITICAL,
}


class HttpExceptionDetail(TypedDict, total=False):
    """HTTPException.detail에서 허용하는 확장 포맷이다."""

    code: int | str
    title: str | None
    msg_key: str
    message_key: str
    msg: str
    data: Any


def log_exception(
    *,
    request: Request,
    log_type: str,
    message: str,
    exc: Exception | None = None,
    **extra: Any,
) -> None:
    """지정한 로그 레벨로 예외 정보를 남긴다."""
    level = LOG_LEVEL_BY_TYPE.get(log_type, logging.ERROR)
    log_message = "%s | path=%s | method=%s | extra=%s" % (
        message,
        request.url.path,
        request.method,
        extra,
    )

    if level >= logging.ERROR:
        logger.log(level, log_message, exc_info=exc)
        return
    logger.log(level, log_message)


def resolve_response_metadata(
    *,
    request: Request,
    code: int | str | None = None,
    title: str | None = None,
    msg_key: str | None = None,
    msg: str | None = None,
) -> tuple[str, int, str, str | None, str, str]:
    """응답 입력값과 catalog를 합쳐 최종 메타데이터를 계산한다."""
    language = normalize_language(request.headers.get("Accept-Language"))
    resolved_status_code, resolved_code, resolved_title, resolved_msg, resolved_msg_key = resolve_message_payload(
        language=language,
        code=code,
        title=title,
        msg=msg,
        msg_key=msg_key,
    )
    return language, resolved_status_code, resolved_code, resolved_title, resolved_msg, resolved_msg_key


def build_exception_response(
    *,
    request: Request,
    code: int | str | None = None,
    msg_key: str | None = None,
    msg: str | None = None,
    data: Any = None,
) -> JSONResponse:
    """예외 정보를 공통 실패 응답 입력값으로 변환한다."""
    language = normalize_language(request.headers.get("Accept-Language"))
    result = response(
        code=code,
        msg=msg,
        msg_key=msg_key,
        data=data,
        language=language,
    )
    if isinstance(result, JSONResponse):
        return result
    return JSONResponse(status_code=int(result.code), content=result.model_dump())


def resolve_http_exception_payload(
    exc: HTTPException,
) -> tuple[int | str, str | None, str | None, str | None, Any]:
    """HTTPException을 공통 응답 입력값으로 해석한다."""
    code: int | str = exc.status_code
    detail = exc.detail

    if isinstance(detail, str):
        if detail.startswith("error.") or detail.startswith("success."):
            return code, detail, None, None, None
        return code, None, None, detail, None

    if isinstance(detail, dict):
        payload: HttpExceptionDetail = detail
        return (
            payload.get("code", code),
            payload.get("msg_key") or payload.get("message_key"),
            payload.get("title"),
            payload.get("msg"),
            payload.get("data"),
        )

    return code, None, None, None, None


async def business_exception_handler(
    request: Request,
    exc: BusinessException,
) -> JSONResponse:
    """비즈니스 예외를 공통 실패 응답으로 변환한다."""
    _, resolved_status_code, resolved_code, resolved_title, _, resolved_msg_key = resolve_response_metadata(
        request=request,
        code=exc.code,
        title=exc.title,
        msg_key=exc.msg_key,
        msg=exc.msg,
    )
    log_exception(
        request=request,
        log_type=exc.log_type,
        message="Business exception handled",
        exc=exc if LOG_LEVEL_BY_TYPE.get(exc.log_type, logging.ERROR) >= logging.ERROR else None,
        status_code=resolved_status_code,
        code=resolved_code,
        title=resolved_title,
        msg_key=resolved_msg_key,
    )
    return build_exception_response(
        request=request,
        code=exc.code,
        msg_key=exc.msg_key,
        msg=exc.msg,
        data=exc.data,
    )


async def validation_exception_handler(
    request: Request,
    exc: RequestValidationError,
) -> JSONResponse:
    """요청 검증 오류를 공통 실패 응답으로 변환한다."""
    _, resolved_status_code, resolved_code, resolved_title, _, resolved_msg_key = resolve_response_metadata(
        request=request,
        code=422,
    )
    log_exception(
        request=request,
        log_type="WARN",
        message="Validation exception handled",
        status_code=resolved_status_code,
        code=resolved_code,
        title=resolved_title,
        msg_key=resolved_msg_key,
        errors=exc.errors(),
    )
    return build_exception_response(
        request=request,
        code=422,
        data={"errors": exc.errors()},
    )


async def http_exception_handler(
    request: Request,
    exc: HTTPException,
) -> JSONResponse:
    """FastAPI HTTP 예외를 공통 실패 응답으로 변환한다."""
    code, msg_key, title, msg, data = resolve_http_exception_payload(exc)
    _, resolved_status_code, resolved_code, resolved_title, _, resolved_msg_key = resolve_response_metadata(
        request=request,
        code=code,
        title=title,
        msg_key=msg_key,
        msg=msg,
    )
    log_exception(
        request=request,
        log_type="WARN",
        message="HTTP exception handled",
        status_code=resolved_status_code,
        code=resolved_code,
        title=resolved_title,
        msg_key=resolved_msg_key,
        detail=exc.detail,
    )
    return build_exception_response(
        request=request,
        code=code,
        msg_key=msg_key,
        msg=msg,
        data=data,
    )


async def unhandled_exception_handler(
    request: Request,
    exc: Exception,
) -> JSONResponse:
    """처리되지 않은 예외를 공통 실패 응답으로 변환한다."""
    _, resolved_status_code, resolved_code, resolved_title, _, resolved_msg_key = resolve_response_metadata(
        request=request,
        code=500,
    )
    log_exception(
        request=request,
        log_type="ERROR",
        message="Unhandled exception occurred",
        exc=exc,
        status_code=resolved_status_code,
        code=resolved_code,
        title=resolved_title,
        msg_key=resolved_msg_key,
        exception_type=type(exc).__name__,
    )
    return build_exception_response(
        request=request,
        code=500,
    )


def register_exception_handlers(app: FastAPI) -> None:
    """애플리케이션에 전역 예외 핸들러를 등록한다."""
    app.add_exception_handler(BusinessException, business_exception_handler)
    app.add_exception_handler(RequestValidationError, validation_exception_handler)
    app.add_exception_handler(HTTPException, http_exception_handler)
    app.add_exception_handler(Exception, unhandled_exception_handler)
