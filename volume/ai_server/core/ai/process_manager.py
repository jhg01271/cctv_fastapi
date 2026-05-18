"""카메라별 AI 프로세스를 생성·감시·종료하는 매니저."""

from __future__ import annotations

import multiprocessing as mp
import time
import threading
from dataclasses import dataclass, field
from typing import Callable

from config.config import settings
from core.logging.logger import get_logger

logger = get_logger(__name__)

# 워치독 체크 주기(초)
WATCHDOG_INTERVAL = 10
# 프로세스 종료 대기 최대 시간(초)
PROCESS_JOIN_TIMEOUT = 5
# 프로세스 재시작 전 대기 시간(초)
RESTART_DELAY = 3


@dataclass
class CameraWorker:
    """카메라 한 대에 대응하는 프로세스 묶음."""

    camera_id: str
    rtsp_url: str  # 원본 카메라 RTSP URL
    stream_url: str  # MediaMTX 경유 URL (실제 reader가 사용)
    frame_queue: mp.Queue
    stop_event: mp.Event
    reader_process: mp.Process | None = None
    ai_process: mp.Process | None = None
    # 워치독이 생존 여부를 확인하는 카운터 (AI 프로세스가 주기적으로 증가)
    heartbeat: mp.Value = field(default_factory=lambda: mp.Value("i", 0))


class CameraProcessManager:
    """등록된 카메라에 대해 RTSP 리더·AI 프로세스를 관리한다.

    사용 방법:
        manager = CameraProcessManager(ai_target_fn)
        manager.start()
        manager.add_camera("cam_01", "rtsp://...", extra_kwargs)
        ...
        manager.stop()
    """

    def __init__(self, ai_target: Callable) -> None:
        """
        Args:
            ai_target: AI 처리 프로세스의 진입 함수.
                       시그니처: ai_target(camera_id, rtsp_url, frame_queue, stop_event, heartbeat, **kwargs)
        """
        self._ai_target = ai_target
        self._workers: dict[str, CameraWorker] = {}
        self._lock = threading.Lock()
        self._watchdog_thread: threading.Thread | None = None
        self._running = False

    # ------------------------------------------------------------------
    # Public API
    # ------------------------------------------------------------------

    def start(self) -> None:
        """매니저와 워치독 스레드를 시작한다."""
        self._running = True
        self._watchdog_thread = threading.Thread(
            target=self._watchdog_loop,
            name="ai-watchdog",
            daemon=True,
        )
        self._watchdog_thread.start()
        logger.info("[ProcessManager] Started.")

    def stop(self) -> None:
        """모든 카메라 프로세스와 워치독을 종료한다."""
        self._running = False
        with self._lock:
            for camera_id in list(self._workers):
                self._stop_worker(camera_id)
        logger.info("[ProcessManager] Stopped.")

    def add_camera(self, camera_id: str, rtsp_url: str, ai_target: Callable | None = None, **ai_kwargs) -> None:
        """카메라를 등록하고 프로세스를 시작한다.

        rtsp_url은 원본 카메라 주소이며, 실제 reader는 MediaMTX 경유 URL을 사용한다.
        ai_target을 지정하면 기본 AI 프로세스 대신 해당 함수를 사용한다.
        """
        with self._lock:
            if camera_id in self._workers:
                logger.warning("[ProcessManager] Camera already registered: %s", camera_id)
                return
            worker = self._create_worker(camera_id, rtsp_url, ai_target=ai_target, **ai_kwargs)
            self._workers[camera_id] = worker
            self._start_worker(worker)
        logger.info("[ProcessManager] Camera added: %s stream_url=%s", camera_id, worker.stream_url)

    def remove_camera(self, camera_id: str) -> None:
        """카메라를 제거하고 관련 프로세스를 종료한다."""
        with self._lock:
            if camera_id not in self._workers:
                return
            self._stop_worker(camera_id)
            del self._workers[camera_id]
        logger.info("[ProcessManager] Camera removed: %s", camera_id)

    def list_cameras(self) -> list[dict]:
        """등록된 카메라 목록과 프로세스 상태를 반환한다."""
        with self._lock:
            return [
                {
                    "camera_id": w.camera_id,
                    "rtsp_url": w.rtsp_url,
                    "stream_url": w.stream_url,
                    "reader_alive": w.reader_process.is_alive() if w.reader_process else False,
                    "ai_alive": w.ai_process.is_alive() if w.ai_process else False,
                    "heartbeat": w.heartbeat.value,
                }
                for w in self._workers.values()
            ]

    # ------------------------------------------------------------------
    # Internal helpers
    # ------------------------------------------------------------------

    def _build_stream_url(self, camera_id: str) -> str:
        """MediaMTX 경유 RTSP URL을 생성한다."""
        return f"{settings.MEDIA_SERVER_RTSP_URL}/{camera_id}"

    def _create_worker(self, camera_id: str, rtsp_url: str, ai_target: Callable | None = None, **ai_kwargs) -> CameraWorker:
        stream_url = self._build_stream_url(camera_id)
        worker = CameraWorker(
            camera_id=camera_id,
            rtsp_url=rtsp_url,
            stream_url=stream_url,
            frame_queue=mp.Queue(maxsize=30),
            stop_event=mp.Event(),
        )
        worker._ai_kwargs = ai_kwargs
        worker._ai_target = ai_target or self._ai_target
        return worker

    def _start_worker(self, worker: CameraWorker) -> None:
        from core.ai.rtsp_reader import rtsp_reader_process

        worker.stop_event.clear()

        worker.reader_process = mp.Process(
            target=rtsp_reader_process,
            args=(worker.stream_url, worker.frame_queue, worker.stop_event),
            name=f"rtsp-{worker.camera_id}",
            daemon=True,
        )

        worker.ai_process = mp.Process(
            target=worker._ai_target,
            kwargs={
                "camera_id": worker.camera_id,
                "rtsp_url": worker.rtsp_url,
                "frame_queue": worker.frame_queue,
                "stop_event": worker.stop_event,
                "heartbeat": worker.heartbeat,
                **worker._ai_kwargs,
            },
            name=f"ai-{worker.camera_id}",
            daemon=True,
        )

        worker.reader_process.start()
        time.sleep(2)  # RTSP 리더가 먼저 연결되도록 대기
        worker.ai_process.start()

    def _stop_worker(self, camera_id: str) -> None:
        """worker의 모든 프로세스를 안전하게 종료한다. _lock 내부에서 호출한다."""
        worker = self._workers.get(camera_id)
        if worker is None:
            return

        worker.stop_event.set()

        for proc in (worker.ai_process, worker.reader_process):
            if proc and proc.is_alive():
                proc.terminate()
                proc.join(timeout=PROCESS_JOIN_TIMEOUT)
                if proc.is_alive():
                    proc.kill()

        # 큐 정리
        _drain_queue(worker.frame_queue)

    def _watchdog_loop(self) -> None:
        """주기적으로 프로세스 생존 여부를 확인하고 다운된 경우 재시작한다."""
        last_heartbeats: dict[str, int] = {}
        restarting: set[str] = set()  # 재시작 진행 중인 camera_id

        while self._running:
            time.sleep(WATCHDOG_INTERVAL)

            with self._lock:
                snapshot = list(self._workers.items())

            for camera_id, worker in snapshot:
                if camera_id in restarting:
                    continue

                current_hb = worker.heartbeat.value
                prev_hb = last_heartbeats.get(camera_id, -1)

                reader_alive = worker.reader_process and worker.reader_process.is_alive()
                ai_alive = worker.ai_process and worker.ai_process.is_alive()
                hb_stuck = (prev_hb == current_hb) and ai_alive  # heartbeat 멈춤

                if not reader_alive or not ai_alive or hb_stuck:
                    logger.warning(
                        "[Watchdog] Restarting camera=%s reader=%s ai=%s hb_stuck=%s",
                        camera_id, reader_alive, ai_alive, hb_stuck,
                    )
                    restarting.add(camera_id)
                    t = threading.Thread(
                        target=self._restart_worker,
                        args=(camera_id, worker.rtsp_url, worker._ai_kwargs, restarting),
                        name=f"restart-{camera_id}",
                        daemon=True,
                    )
                    t.start()
                    last_heartbeats[camera_id] = 0
                else:
                    last_heartbeats[camera_id] = current_hb

    def _restart_worker(
        self,
        camera_id: str,
        rtsp_url: str,
        ai_kwargs: dict,
        restarting: set[str],
    ) -> None:
        """워치독이 감지한 비정상 프로세스를 락 밖에서 재시작한다."""
        try:
            time.sleep(RESTART_DELAY)
            with self._lock:
                if camera_id not in self._workers:
                    return
                self._stop_worker(camera_id)
                new_worker = self._create_worker(camera_id, rtsp_url, **ai_kwargs)
                self._workers[camera_id] = new_worker
                self._start_worker(new_worker)
            logger.info("[Watchdog] Restarted camera=%s", camera_id)
        except Exception:
            logger.exception("[Watchdog] Restart failed: camera=%s", camera_id)
        finally:
            restarting.discard(camera_id)


def _drain_queue(q: mp.Queue) -> None:
    """큐에 남은 항목을 모두 비운다."""
    try:
        while not q.empty():
            q.get_nowait()
    except Exception:
        pass


# 전역 싱글톤 (main.py lifespan에서 초기화)
_manager: CameraProcessManager | None = None


def get_manager() -> CameraProcessManager:
    """전역 CameraProcessManager 인스턴스를 반환한다."""
    if _manager is None:
        raise RuntimeError("CameraProcessManager not initialized. Call init_manager() first.")
    return _manager


def init_manager(ai_target: Callable) -> CameraProcessManager:
    """전역 CameraProcessManager를 초기화하고 반환한다."""
    global _manager
    _manager = CameraProcessManager(ai_target)
    return _manager
