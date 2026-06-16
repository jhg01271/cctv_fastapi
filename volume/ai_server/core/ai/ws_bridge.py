"""AI 프로세스의 실시간 결과를 WebSocket 구독자에게 나눠주는 브리지 파일.

흐름에서의 위치:
  1. service/safety/processor.py가 _send_result()로 현재 프레임의 detections/events를 mp_queue에 넣는다.
  2. 이 파일의 WebSocketBridge가 mp_queue를 읽어 camera_id별 asyncio.Queue로 분배한다.
  3. service/safety/routes.py의 /safety/ws/{camera_id} WebSocket 핸들러가 해당 Queue를 구독한다.
  4. /safety/debug 같은 화면은 이 WebSocket 메시지를 받아 bbox/keypoints를 그린다.

중요한 점:
  - 이 경로는 실시간 표시용이다. DB 저장은 service/safety/worker.py의 event_queue 경로가 담당한다.

다음에 볼 파일:
  - service/safety/routes.py: 브라우저 WebSocket 연결을 받고 이 브리지를 구독한다.
"""

from __future__ import annotations

import asyncio
import multiprocessing as mp
import threading
from collections import defaultdict

from core.logging.logger import get_logger

logger = get_logger(__name__)


class WebSocketBridge:
    """mp.Queue → 카메라별 asyncio.Queue 팬아웃 브리지."""

    def __init__(self) -> None:
        # AI 프로세스들이 공유하는 단일 mp.Queue
        self.mp_queue: mp.Queue = mp.Queue(maxsize=500)

        # camera_id → [asyncio.Queue, ...]  (WebSocket 연결마다 하나씩)
        self._subscribers: dict[str, list[asyncio.Queue]] = defaultdict(list)
        self._lock = threading.Lock()
        self._loop: asyncio.AbstractEventLoop | None = None
        self._thread: threading.Thread | None = None

    # ------------------------------------------------------------------
    # 생명주기
    # ------------------------------------------------------------------

    def start(self, loop: asyncio.AbstractEventLoop) -> None:
        """이벤트 루프를 받아 브리지 스레드를 시작한다."""
        self._loop = loop
        self._thread = threading.Thread(
            target=self._bridge_loop,
            name="ws-bridge",
            daemon=True,
        )
        self._thread.start()
        logger.info("[WSBridge] Started.")

    def stop(self) -> None:
        """브리지를 정지한다 (스레드는 daemon이므로 프로세스 종료 시 자동 정리)."""
        self._loop = None
        logger.info("[WSBridge] Stopped.")

    # ------------------------------------------------------------------
    # AI 프로세스 측 (mp.Queue에 데이터 전송)
    # ------------------------------------------------------------------

    def put(self, data: dict) -> None:
        """AI 자식 프로세스에서 결과를 큐에 넣는다.

        data 필수 키: camera_id (str)
        """
        try:
            self.mp_queue.put_nowait(data)
        except Exception:
            pass  # 큐 가득 찬 경우 드랍

    # ------------------------------------------------------------------
    # WebSocket 핸들러 측 (asyncio.Queue 구독)
    # ------------------------------------------------------------------

    def subscribe(self, camera_id: str) -> asyncio.Queue:
        """카메라 구독 큐를 생성하고 반환한다."""
        q: asyncio.Queue = asyncio.Queue(maxsize=60)
        with self._lock:
            self._subscribers[camera_id].append(q)
        logger.debug("[WSBridge] Subscribed camera=%s", camera_id)
        return q

    def unsubscribe(self, camera_id: str, q: asyncio.Queue) -> None:
        """구독을 해제한다."""
        with self._lock:
            subs = self._subscribers.get(camera_id, [])
            if q in subs:
                subs.remove(q)
        logger.debug("[WSBridge] Unsubscribed camera=%s", camera_id)

    # ------------------------------------------------------------------
    # 내부 브리지 루프 (백그라운드 스레드)
    # ------------------------------------------------------------------

    def _bridge_loop(self) -> None:
        """mp.Queue를 폴링하여 카메라별 asyncio.Queue로 분배한다."""
        while True:
            try:
                item: dict = self.mp_queue.get(timeout=1.0)
            except Exception:
                continue

            camera_id = item.get("camera_id")
            if not camera_id or self._loop is None:
                continue

            with self._lock:
                queues = list(self._subscribers.get(camera_id, []))

            for q in queues:
                asyncio.run_coroutine_threadsafe(
                    self._safe_put(q, item), self._loop
                )

    @staticmethod
    async def _safe_put(q: asyncio.Queue, item: dict) -> None:
        """asyncio.Queue에 드랍 없이 넣는다 (가득 차면 오래된 것 제거)."""
        if q.full():
            try:
                q.get_nowait()
            except Exception:
                pass
        await q.put(item)


# 전역 싱글톤 — main.py lifespan에서 start() 호출
ws_bridge = WebSocketBridge()
