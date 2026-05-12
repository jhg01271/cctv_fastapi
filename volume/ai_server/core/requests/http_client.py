"""외부 HTTP 호출에 사용하는 공통 비동기 클라이언트 모듈이다."""

from __future__ import annotations

from typing import Any

import httpx

from config.config import settings
from core.exception.custom_exception import (
    ExternalServiceConnectionException,
    ExternalServiceException,
    ExternalServiceTimeoutException,
)
from core.logging.helpers import log_event
from core.logging.logger import get_logger


logger = get_logger(__name__)


class AsyncHttpClient:
    """재사용 가능한 httpx 비동기 클라이언트를 관리한다."""

    def __init__(
        self,
        timeout: float | None = None,
        *,
        transport: httpx.AsyncBaseTransport | None = None,
    ) -> None:
        self._timeout = timeout or settings.HTTP_TIMEOUT
        self._transport = transport
        self._client: httpx.AsyncClient | None = None

    async def startup(self) -> None:
        """공통 HTTP 클라이언트를 초기화한다."""
        if self._client is None:
            self._client = httpx.AsyncClient(timeout=self._timeout, transport=self._transport)

    async def shutdown(self) -> None:
        """공통 HTTP 클라이언트를 종료한다."""
        if self._client is not None:
            await self._client.aclose()
            self._client = None

    async def get(
        self,
        url: str,
        params: dict[str, Any] | None = None,
        headers: dict[str, str] | None = None,
        *,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 설정을 사용해 GET 요청을 전송한다."""
        return await self._request(
            "GET",
            url,
            params=params,
            headers=headers,
            external_system=external_system,
        )

    async def post(
        self,
        url: str,
        json: dict[str, Any] | list[Any] | None = None,
        data: Any = None,
        headers: dict[str, str] | None = None,
        *,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 설정을 사용해 POST 요청을 전송한다."""
        return await self._request(
            "POST",
            url,
            json=json,
            data=data,
            headers=headers,
            external_system=external_system,
        )

    async def put(
        self,
        url: str,
        json: dict[str, Any] | list[Any] | None = None,
        data: Any = None,
        headers: dict[str, str] | None = None,
        *,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 설정을 사용해 PUT 요청을 전송한다."""
        return await self._request(
            "PUT",
            url,
            json=json,
            data=data,
            headers=headers,
            external_system=external_system,
        )

    async def patch(
        self,
        url: str,
        json: dict[str, Any] | list[Any] | None = None,
        data: Any = None,
        headers: dict[str, str] | None = None,
        *,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 설정을 사용해 PATCH 요청을 전송한다."""
        return await self._request(
            "PATCH",
            url,
            json=json,
            data=data,
            headers=headers,
            external_system=external_system,
        )

    async def delete(
        self,
        url: str,
        params: dict[str, Any] | None = None,
        headers: dict[str, str] | None = None,
        *,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 설정을 사용해 DELETE 요청을 전송한다."""
        return await self._request(
            "DELETE",
            url,
            params=params,
            headers=headers,
            external_system=external_system,
        )

    async def _request(
        self,
        method: str,
        url: str,
        *,
        params: dict[str, Any] | None = None,
        json: dict[str, Any] | list[Any] | None = None,
        data: Any = None,
        headers: dict[str, str] | None = None,
        external_system: str | None = None,
    ) -> httpx.Response:
        """공통 요청/응답 로깅과 예외 변환을 적용해 HTTP 요청을 전송한다."""
        client = await self._get_client()
        request_preview = self._build_request_preview(params=params, json=json, data=data)
        action = method.lower()

        log_event(
            logger=logger,
            level="INFO",
            message="외부 HTTP 요청을 전송합니다.",
            event_type="HTTP_REQUEST_START",
            service="http_client",
            action=action,
            external_system=external_system,
            method=method,
            url=url,
            **request_preview,
        )

        try:
            response = await client.request(
                method=method,
                url=url,
                params=params,
                json=json,
                data=data,
                headers=headers,
            )
            response.raise_for_status()
        except httpx.TimeoutException as exc:
            log_event(
                logger=logger,
                level="ERROR",
                message="외부 HTTP 요청이 시간 초과되었습니다.",
                event_type="HTTP_REQUEST_TIMEOUT",
                service="http_client",
                action=action,
                external_system=external_system,
                method=method,
                url=url,
                **request_preview,
            )
            raise ExternalServiceTimeoutException(
                data={
                    "method": method,
                    "url": url,
                    "external_system": external_system,
                },
                cause=exc,
            ) from exc
        except httpx.HTTPStatusError as exc:
            status_code = exc.response.status_code if exc.response is not None else None
            log_event(
                logger=logger,
                level="ERROR",
                message="외부 HTTP 응답이 실패 상태 코드를 반환했습니다.",
                event_type="HTTP_RESPONSE_ERROR",
                service="http_client",
                action=action,
                external_system=external_system,
                method=method,
                url=url,
                response_status_code=status_code,
                **request_preview,
            )
            raise ExternalServiceException(
                data={
                    "method": method,
                    "url": url,
                    "external_system": external_system,
                    "response_status_code": status_code,
                },
                cause=exc,
            ) from exc
        except httpx.RequestError as exc:
            log_event(
                logger=logger,
                level="ERROR",
                message="외부 HTTP 연결 오류가 발생했습니다.",
                event_type="HTTP_REQUEST_CONNECTION_ERROR",
                service="http_client",
                action=action,
                external_system=external_system,
                method=method,
                url=url,
                **request_preview,
            )
            raise ExternalServiceConnectionException(
                data={
                    "method": method,
                    "url": url,
                    "external_system": external_system,
                },
                cause=exc,
            ) from exc

        log_event(
            logger=logger,
            level="INFO",
            message="외부 HTTP 응답을 수신했습니다.",
            event_type="HTTP_RESPONSE_SUCCESS",
            service="http_client",
            action=action,
            external_system=external_system,
            method=method,
            url=url,
            response_status_code=response.status_code,
        )
        return response

    @staticmethod
    def _build_request_preview(
        *,
        params: dict[str, Any] | None = None,
        json: dict[str, Any] | list[Any] | None = None,
        data: Any = None,
    ) -> dict[str, Any]:
        """로그에 남길 최소 요청 미리보기 정보를 생성한다."""
        preview: dict[str, Any] = {}
        if params:
            preview["params"] = params
        if isinstance(json, dict):
            preview["json_keys"] = sorted(json.keys())
        elif isinstance(json, list):
            preview["json_length"] = len(json)
        if data is not None and not json:
            preview["has_data"] = True
        return preview

    async def _get_client(self) -> httpx.AsyncClient:
        """필요 시 클라이언트를 생성하고 반환한다."""
        if self._client is None:
            await self.startup()
        if self._client is None:
            raise RuntimeError("HTTP client is not initialized.")
        return self._client


http_client = AsyncHttpClient()
