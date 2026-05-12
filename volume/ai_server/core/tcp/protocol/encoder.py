"""TCP 요청 인코딩 유틸을 정의한다."""

from __future__ import annotations

import json
from typing import Any



def encode_text(message: str, encoding: str = "utf-8") -> bytes:
    """문자열 메시지를 바이트로 인코딩한다."""
    return message.encode(encoding)



def encode_json(payload: dict[str, Any], encoding: str = "utf-8") -> bytes:
    """딕셔너리 데이터를 JSON 바이트로 인코딩한다."""
    message = json.dumps(payload, ensure_ascii=False)
    return encode_text(message, encoding=encoding)
