"""AI 실시간 결과를 브라우저에 보여주는 Safety 라우터 파일.

흐름에서의 위치:
  1. service/safety/processor.py가 현재 프레임의 bbox/keypoints/events를 ws_bridge에 넣는다.
  2. core/ai/ws_bridge.py가 camera_id별 구독 Queue로 실시간 결과를 나눈다.
  3. 이 파일의 /safety/ws/{camera_id}가 브라우저 WebSocket 연결을 받고 해당 camera_id를 구독한다.
  4. /safety/debug는 이 WebSocket을 이용해 영상 위에 bbox와 skeleton을 그리는 개발자 확인 화면이다.

중요한 점:
  - 이 파일은 실시간 표시 경로다. SafetyMonitoringHistory의 DB 조회는 service/event/routes.py에서 처리한다.

다음에 볼 파일:
  - core/ai/ws_bridge.py: AI 프로세스 결과를 WebSocket용 Queue로 분배한다.
  - service/event/routes.py: DB에 저장된 이벤트 이력을 History 화면에 제공한다.
"""


from __future__ import annotations

import json

from fastapi import APIRouter, WebSocket, WebSocketDisconnect
from fastapi.responses import HTMLResponse
from pathlib import Path

from core.ai.ws_bridge import ws_bridge
from core.logging.logger import get_logger

logger = get_logger(__name__)
router = APIRouter(prefix="/safety", tags=["안전 AI 추론"])


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


@router.get("/debug", response_class=HTMLResponse, summary="개발자용 AI 실시간 모니터링 디버그 페이지")
def get_debug_page() -> HTMLResponse:
    """AI 검출 결과를 실시간으로 BBox 오버레이하여 시각적으로 검증하는 디버그 페이지를 반환한다."""
    html_path = Path(__file__).parent / "debug.html"
    if not html_path.exists():
        return HTMLResponse(
            content="<html><body><h3>디버그 페이지 파일(debug.html)이 존재하지 않습니다.</h3></body></html>",
            status_code=404,
        )
    return HTMLResponse(content=html_path.read_text(encoding="utf-8"))

