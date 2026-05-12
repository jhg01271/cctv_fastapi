"""응답 및 예외 메시지 원본 데이터를 정의한다."""

from __future__ import annotations

from typing import TypedDict


class MessageEntry(TypedDict):
    """메시지 키별 정의 구조다."""

    status_code: int
    title: str | None
    locales: dict[str, str]


DEFAULT_MESSAGE_KEY_BY_STATUS: dict[int, str] = {
    200: "success.default",
    201: "success.create",
    400: "error.bad_request",
    401: "error.unauthorized",
    403: "error.forbidden",
    404: "error.not_found",
    409: "error.conflict",
    422: "error.validation",
    500: "error.internal_server",
    502: "error.external_service",
    503: "error.external_connection",
    504: "error.external_timeout",
}

LEGACY_MESSAGE_KEY_ALIASES: dict[str, str] = {
    "readSuccess.msg": "success.read",
    "sucess.read_success": "success.read",
}


MESSAGES: dict[str, MessageEntry] = {
    "success.default": {
        "status_code": 200,
        "title": None,
        "locales": {
            "ko": "성공하였습니다.",
            "en": "Request completed successfully.",
        },
    },
    "success.read": {
        "status_code": 200,
        "title": None,
        "locales": {
            "ko": "조회에 성공하였습니다.",
            "en": "Read completed successfully.",
        },
    },
    "success.create": {
        "status_code": 201,
        "title": None,
        "locales": {
            "ko": "생성에 성공하였습니다.",
            "en": "Created successfully.",
        },
    },
    "success.update": {
        "status_code": 200,
        "title": None,
        "locales": {
            "ko": "수정에 성공하였습니다.",
            "en": "Updated successfully.",
        },
    },
    "success.delete": {
        "status_code": 200,
        "title": None,
        "locales": {
            "ko": "삭제에 성공하였습니다.",
            "en": "Deleted successfully.",
        },
    },
    "error.bad_request": {
        "status_code": 400,
        "title": "Bad Request",
        "locales": {
            "ko": "잘못된 요청입니다.",
            "en": "Bad request.",
        },
    },
    "error.unauthorized": {
        "status_code": 401,
        "title": "Unauthorized",
        "locales": {
            "ko": "인증이 필요합니다.",
            "en": "Authentication is required.",
        },
    },
    "error.forbidden": {
        "status_code": 403,
        "title": "Forbidden",
        "locales": {
            "ko": "접근 권한이 없습니다.",
            "en": "You do not have permission to access this resource.",
        },
    },
    "error.not_found": {
        "status_code": 404,
        "title": "Not Found",
        "locales": {
            "ko": "데이터를 찾을 수 없습니다.",
            "en": "The requested resource was not found.",
        },
    },
    "error.conflict": {
        "status_code": 409,
        "title": "Conflict",
        "locales": {
            "ko": "요청이 현재 상태와 충돌합니다.",
            "en": "The request conflicts with the current state.",
        },
    },
    "error.validation": {
        "status_code": 422,
        "title": "Validation Error",
        "locales": {
            "ko": "요청 값이 올바르지 않습니다.",
            "en": "The request data is invalid.",
        },
    },
    "error.http": {
        "status_code": 400,
        "title": "HTTP Error",
        "locales": {
            "ko": "요청 처리 중 오류가 발생했습니다.",
            "en": "An error occurred while processing the request.",
        },
    },
    "error.internal_server": {
        "status_code": 500,
        "title": "Internal Server Error",
        "locales": {
            "ko": "서버 내부 오류가 발생했습니다.",
            "en": "An internal server error occurred.",
        },
    },
    "error.external_service": {
        "status_code": 502,
        "title": "Bad Gateway",
        "locales": {
            "ko": "외부 서비스 호출에 실패했습니다.",
            "en": "The external service returned an error.",
        },
    },
    "error.external_connection": {
        "status_code": 503,
        "title": "Service Unavailable",
        "locales": {
            "ko": "외부 서비스 연결에 실패했습니다.",
            "en": "The external service connection failed.",
        },
    },
    "error.external_timeout": {
        "status_code": 504,
        "title": "Gateway Timeout",
        "locales": {
            "ko": "외부 서비스 응답 시간이 초과되었습니다.",
            "en": "The external service timed out.",
        },
    },
}
