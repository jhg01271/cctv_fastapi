"""공정률 워커.

progress_process가 result_queue에 넣은 추론 결과를
백그라운드 스레드에서 읽어 SQLAlchemy로 DB에 저장한다.
"""

from __future__ import annotations

import multiprocessing as mp
import threading

from core.database.session import SessionLocal
from core.logging.logger import get_logger
from service.progress import persistence
from service.progress.processor import progress_process
from service.progress.repository import fetch_grid_coordinates, fetch_progress_cameras

logger = get_logger(__name__)


class ProgressWorker:
    """공정률 추론 프로세스 기동 + result_queue → SQLAlchemy DB 저장 워커."""

    def __init__(self) -> None:
        self._result_queue: mp.Queue = mp.Queue(maxsize=100)
        self._stop_event: mp.Event = mp.Event()
        self._heartbeat: mp.Value = mp.Value("i", 0)
        self._proc: mp.Process | None = None
        self._thread: threading.Thread | None = None
        self._stop = threading.Event()

    def start(self) -> None:
        cameras, grid_map = self._load_context()
        if not cameras:
            logger.warning("[ProgressWorker] No cameras found. Worker not started.")
            return

        self._proc = mp.Process(
            target=progress_process,
            kwargs={
                "stop_event":   self._stop_event,
                "heartbeat":    self._heartbeat,
                "cameras":      cameras,
                "grid_map":     grid_map,
                "result_queue": self._result_queue,
            },
            name="progress-worker",
            daemon=True,
        )
        self._proc.start()
        logger.info("[ProgressWorker] Process started. pid=%s", self._proc.pid)

        self._thread = threading.Thread(
            target=self._worker_loop,
            name="progress-db-worker",
            daemon=True,
        )
        self._thread.start()
        logger.info("[ProgressWorker] DB worker started.")

    def stop(self) -> None:
        self._stop.set()
        self._stop_event.set()
        if self._proc and self._proc.is_alive():
            self._proc.terminate()
            self._proc.join(timeout=10)
        logger.info("[ProgressWorker] Stopped.")

    def _load_context(self) -> tuple[list[dict], dict[str, list | None]]:
        db = SessionLocal()
        try:
            cameras = fetch_progress_cameras(db)
            grid_map = {
                cam["camera_id"]: fetch_grid_coordinates(db, cam["camera_id"])
                for cam in cameras
            }
            return cameras, grid_map
        finally:
            db.close()

    def _worker_loop(self) -> None:
        while not self._stop.is_set():
            try:
                item: dict = self._result_queue.get(timeout=1.0)
            except Exception:
                continue
            self._save(item)

    def _save(self, item: dict) -> None:
        db = SessionLocal()
        try:
            persistence.save_progress_result(
                db=db,
                camera_id=item["camera_id"],
                image_path=item["image_path"],
                timestamp=item["timestamp"],
                detections=item["detections"],
            )
        except Exception:
            logger.exception(
                "[ProgressWorker] Save failed camera=%s",
                item.get("camera_id"),
            )
        finally:
            db.close()
