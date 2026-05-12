"""TCP 응답 디코딩 유틸을 정의한다."""

from __future__ import annotations

import json
from json import JSONDecodeError
from typing import Any

from core.tcp.exceptions import TcpProtocolException



def decode_text(data: bytes, encoding: str = "utf-8") -> str:
    """바이트 응답을 문자열로 디코딩한다."""
    if not data:
        raise TcpProtocolException("TCP 응답이 비어 있습니다.")

    try:
        return data.decode(encoding)
    except UnicodeDecodeError as exc:
        raise TcpProtocolException("TCP 응답 문자열 디코딩에 실패했습니다.") from exc



def decode_json(data: bytes, encoding: str = "utf-8") -> dict[str, Any]:
    """바이트 응답을 JSON 객체로 디코딩한다."""
    text = decode_text(data, encoding=encoding)

    try:
        payload = json.loads(text)
    except JSONDecodeError as exc:
        raise TcpProtocolException("TCP 응답 JSON 파싱에 실패했습니다.") from exc

    if not isinstance(payload, dict):
        raise TcpProtocolException("TCP 응답 JSON 형식이 올바르지 않습니다.")

    return payload
