"""안전관리 DB 저장 워커.

safety_process가 event_queue에 넣은 이벤트를
백그라운드 스레드에서 읽어 SQLAlchemy로 DB에 저장한다.
"""

from __future__ import annotations

import multiprocessing as mp
import threading

from core.database.session import SessionLocal
from core.logging.logger import get_logger
from service.safety import repository
from service.telegram.service import send_telegram_alert

logger = get_logger(__name__)

# 전역 event_queue — safety_process와 SafetyDBWorker가 공유
event_queue: mp.Queue = mp.Queue(maxsize=500)


class SafetyDBWorker:
    """event_queue → SQLAlchemy DB 저장 워커."""

    def __init__(self) -> None:
        self._thread: threading.Thread | None = None
        self._stop = threading.Event()

    def start(self) -> None:
        self._thread = threading.Thread(
            target=self._worker_loop,
            name="safety-db-worker",
            daemon=True,
        )
        self._thread.start()
        logger.info("[SafetyDBWorker] Started.")

    def stop(self) -> None:
        self._stop.set()
        logger.info("[SafetyDBWorker] Stopped.")

    def _worker_loop(self) -> None:
        while not self._stop.is_set():
            try:
                item: dict = event_queue.get(timeout=1.0)
            except Exception:
                continue
            self._save(item)

    def _save(self, item: dict) -> None:
        db = SessionLocal()
        try:
            repository.save_event(
                db=db,
                camera_id=item["camera_id"],
                event_key=item["event_key"],
                image_path=item["image_path"],
                timestamp=item["timestamp"],
            )
            # 이벤트 저장 성공 시 텔레그램 알림 전송
            try:
                send_telegram_alert(
                    db=db,
                    camera_id=item["camera_id"],
                    event_key=item["event_key"],
                    image_path=item.get("image_path"),
                )
            except Exception:
                logger.exception(
                    "[SafetyDBWorker] Telegram alert failed camera=%s event=%s",
                    item.get("camera_id"),
                    item.get("event_key"),
                )
        except Exception:
            logger.exception(
                "[SafetyDBWorker] Save failed camera=%s event=%s",
                item.get("camera_id"),
                item.get("event_key"),
            )
        finally:
            db.close()
