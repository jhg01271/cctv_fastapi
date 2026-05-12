"""애플리케이션 전반에서 사용하는 공통 비즈니스 예외를 정의한다."""

from __future__ import annotations

from typing import Any

from core.utils.messages import normalize_http_code, normalize_msg_key


class BusinessException(Exception):
    """공통 비즈니스 예외 부모 클래스다."""

    default_code: int = 500
    default_msg_key: str = "error.internal_server"
    default_log_type: str = "ERROR"
    default_title: str | None = None

    def __init__(
        self,
        *,
        code: int | str | None = None,
        msg: str | None = None,
        data: Any | None = None,
        title: str | None = None,
        msg_key: str | None = None,
        message_key: str | None = None,
        log_type: str | None = None,
        cause: Exception | None = None,
    ) -> None:
        resolved_code = normalize_http_code(code if code is not None else self.default_code)
        if resolved_code is None:
            raise ValueError("BusinessException code must be a valid HTTP status code")

        self.code = str(resolved_code)
        self.title = title if title is not None else self.default_title
        self.msg_key = normalize_msg_key(msg_key, message_key) or self.default_msg_key
        self.msg = msg
        self.data = data
        self.log_type = self.normalize_log_type(log_type or self.default_log_type)
        self.cause = cause
        super().__init__(msg or self.msg_key)

    @property
    def message_key(self) -> str:
        """기존 호환성을 위해 message_key 별칭을 제공한다."""
        return self.msg_key

    @property
    def status_code(self) -> int:
        """기존 호환성을 위해 정수 상태 코드 별칭을 제공한다."""
        return int(self.code)

    @staticmethod
    def normalize_log_type(log_type: str) -> str:
        """로그 레벨 문자열을 표준 형식으로 정규화한다."""
        normalized = log_type.strip().upper()
        if normalized == "WARNING":
            return "WARN"
        if normalized in {"DEBUG", "INFO", "WARN", "ERROR", "CRITICAL"}:
            return normalized
        return "ERROR"

    def __str__(self) -> str:
        """예외 핵심 정보를 문자열로 반환한다."""
        return (
            f"{self.__class__.__name__}("
            f"code={self.code!r}, "
            f"title={self.title!r}, "
            f"msg_key={self.msg_key!r}, "
            f"msg={self.msg!r}, "
            f"log_type={self.log_type!r}, "
            f"data={self.data!r}, "
            f"cause={self.cause!r}"
            f")"
        )

    def __repr__(self) -> str:
        """디버깅용 예외 표현을 반환한다."""
        return self.__str__()


class BadRequestException(BusinessException):
    """잘못된 요청에 대한 예외다."""

    default_code = 400
    default_msg_key = "error.bad_request"
    default_log_type = "WARN"


class UnauthorizedException(BusinessException):
    """인증이 필요한 요청에 대한 예외다."""

    default_code = 401
    default_msg_key = "error.unauthorized"
    default_log_type = "WARN"


class ForbiddenException(BusinessException):
    """권한이 없는 요청에 대한 예외다."""

    default_code = 403
    default_msg_key = "error.forbidden"
    default_log_type = "WARN"


class NotFoundException(BusinessException):
    """리소스를 찾지 못한 경우의 예외다."""

    default_code = 404
    default_msg_key = "error.not_found"
    default_log_type = "WARN"


class ConflictException(BusinessException):
    """중복 또는 상태 충돌에 대한 예외다."""

    default_code = 409
    default_msg_key = "error.conflict"
    default_log_type = "WARN"


class ExternalServiceException(BusinessException):
    """외부 서비스가 오류 응답을 반환한 경우의 예외다."""

    default_code = 502
    default_msg_key = "error.external_service"
    default_log_type = "ERROR"


class ExternalServiceConnectionException(BusinessException):
    """외부 서비스 연결 실패에 대한 예외다."""

    default_code = 503
    default_msg_key = "error.external_connection"
    default_log_type = "ERROR"


class ExternalServiceTimeoutException(BusinessException):
    """외부 서비스 응답 지연에 대한 예외다."""

    default_code = 504
    default_msg_key = "error.external_timeout"
    default_log_type = "ERROR"
