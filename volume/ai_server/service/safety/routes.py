"""안전관리 라우터 — WebSocket AI 결과 스트림 + 이벤트 이력."""

from __future__ import annotations

import json

from fastapi import APIRouter, WebSocket, WebSocketDisconnect

from core.ai.ws_bridge import ws_bridge
from core.logging.logger import get_logger

logger = get_logger(__name__)
router = APIRouter(prefix="/safety", tags=["safety"])


# ──────────────────────────────────────────────────────────────
# WebSocket — 실시간 AI 결과 수신
# ──────────────────────────────────────────────────────────────

@router.websocket("/ws/{camera_id}")
async def safety_ws(websocket: WebSocket, camera_id: str) -> None:
    """카메라 AI 결과를 실시간 JSON 스트림으로 전달한다.

    연결 후 해당 camera_id의 탐지 결과(bbox, 이벤트)를 지속적으로 수신한다.

    메시지 형식:
    {
        "camera_id": "cam_01",
        "timestamp": "20260421_153000",
        "detections": [{"class": "no_helmet", "bbox": [x1,y1,x2,y2], "conf": 0.91}],
        "events":     [{"type": "E001", "message": "안전모 미착용 감지"}]
    }
    """
    await websocket.accept()
    q = ws_bridge.subscribe(camera_id)
    logger.info("[WS] Client connected: camera=%s", camera_id)

    try:
        while True:
            data = await q.get()
            await websocket.send_text(json.dumps(data, ensure_ascii=False))
    except WebSocketDisconnect:
        logger.info("[WS] Client disconnected: camera=%s", camera_id)
    except Exception as e:
        logger.error("[WS] Error: camera=%s error=%s", camera_id, e)
    finally:
        ws_bridge.unsubscribe(camera_id, q)


