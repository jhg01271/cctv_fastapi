"""AI가 확정한 이벤트를 DB에 저장하고 저장 성공 후 알림을 보내는 파일.

흐름에서의 위치:
  1. service/safety/processor.py가 E001~E004 확정 이벤트를 event_queue에 넣는다.
  2. 이 파일의 SafetyDBWorker가 백그라운드 스레드에서 event_queue를 계속 읽는다.
  3. 먼저 service/safety/repository.py의 save_event()로 tb_camera_event_hist에 저장한다.
  4. DB 저장이 성공하면 service/telegram/service.py로 텔레그램 알림을 보낸다.

중요한 점:
  - /safety/debug에 보이는 실시간 bbox와 별개로, History에 남는 이벤트는 이 워커를 통과한 것뿐이다.
  - 현재 텔레그램은 "DB 저장 후 알림" 구조다.

다음에 볼 파일:
  - service/safety/repository.py: 이벤트를 실제 DB 테이블에 insert한다.
  - service/telegram/service.py: 저장된 이벤트에 대한 텔레그램 메시지/파일을 전송한다.
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
