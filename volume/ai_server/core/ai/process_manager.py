"""카메라 1대에 필요한 reader/AI 프로세스 묶음을 만들고 감시하는 파일.

흐름에서의 위치:
  1. service/remote/service.py의 _register_camera()가 add_camera()를 호출한다.
  2. 이 파일은 카메라마다 frame_queue를 만들고 RTSP reader 프로세스를 먼저 시작한다.
  3. reader가 첫 프레임을 받았는지 확인한 뒤 safety_process 또는 jit_process를 시작한다.
  4. watchdog이 reader/AI 프로세스와 프레임 수신 상태를 감시하다가 문제가 있으면 재시작한다.

다음에 볼 파일:
  - core/ai/rtsp_reader.py: RTSP에서 프레임을 읽어 frame_queue에 넣는다.
  - service/safety/processor.py: frame_queue에서 프레임을 꺼내 모델 추론과 이벤트 판단을 한다.
"""

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
# 프레임 수신이 이 시간 이상 없으면 스트림 끊김으로 판단(초)
FRAME_STALE_TIMEOUT = 30


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
    # RTSP reader가 마지막으로 프레임을 큐에 넣은 시각 (epoch seconds)
    last_frame_at: mp.Value = field(default_factory=lambda: mp.Value("d", 0.0))
    # 프로세스가 시작된 시각 (restart cooldown 판단용)
    started_at: float = 0.0


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
        # 워치독 루프 자체의 heartbeat (마지막 루프 완료 시각)
        self._watchdog_heartbeat: float = 0.0

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
                    "last_frame_at": w.last_frame_at.value,
                }
                for w in self._workers.values()
            ]

    @property
    def watchdog_heartbeat(self) -> float:
        """워치독 루프의 마지막 실행 시각 (epoch seconds)."""
        return self._watchdog_heartbeat

    # ------------------------------------------------------------------
    # Internal helpers
    # ------------------------------------------------------------------

    def _build_stream_url(self, camera_id: str) -> str:
        """MediaMTX 경유 RTSP URL을 생성한다."""
        return f"{settings.MEDIA_SERVER_RTSP_URL}/{camera_id}"

    def _create_worker(self, camera_id: str, rtsp_url: str, ai_target: Callable | None = None, **ai_kwargs) -> CameraWorker:
        # 브라우저 재생은 MediaMTX의 CAM000X 채널을 쓰지만, AI reader는 필요하면 원본 RTSP를 직접 읽을 수 있다.
        # 개발 환경에서 6개 중간 채널을 동시에 OpenCV로 열면 지연이 커져 source 모드를 사용한다.
        stream_url = rtsp_url if settings.AI_READER_INPUT == "source" else self._build_stream_url(camera_id)
        worker = CameraWorker(
            camera_id=camera_id,
            rtsp_url=rtsp_url,
            stream_url=stream_url,
            # 실시간 화면은 오래된 프레임을 쌓는 것보다 최신 프레임만 남기는 편이 중요하다.
            frame_queue=mp.Queue(maxsize=max(1, settings.AI_FRAME_QUEUE_SIZE)),
            stop_event=mp.Event(),
        )
        worker._ai_kwargs = ai_kwargs
        worker._ai_target = ai_target or self._ai_target
        return worker

    def _start_worker(self, worker: CameraWorker) -> None:
        from core.ai.rtsp_reader import rtsp_reader_process

        worker.stop_event.clear()
        worker.started_at = time.time()

        worker.reader_process = mp.Process(
            target=rtsp_reader_process,
            args=(worker.stream_url, worker.frame_queue, worker.stop_event, worker.last_frame_at),
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

        first_frame_timeout = max(1, settings.AI_READER_FIRST_FRAME_TIMEOUT_SEC)
        deadline = time.time() + first_frame_timeout
        while time.time() < deadline:
            if worker.last_frame_at.value > 0:
                logger.info("[ProcessManager] Reader received first frame: %s", worker.camera_id)
                break
            if worker.reader_process and not worker.reader_process.is_alive():
                logger.warning("[ProcessManager] Reader exited before first frame: %s", worker.camera_id)
                break
            time.sleep(0.25)
        else:
            logger.warning(
                "[ProcessManager] Reader first frame timeout. camera=%s timeout=%ss",
                worker.camera_id,
                first_frame_timeout,
            )

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
        restarting: set[str] = set()

        while self._running:
            time.sleep(WATCHDOG_INTERVAL)

            try:
                with self._lock:
                    snapshot = list(self._workers.items())

                now = time.time()

                for camera_id, worker in snapshot:
                    # (1) 이미 재시작 진행 중이면 스킵 — 카메라당 1개만 실행
                    if camera_id in restarting:
                        logger.debug("[Watchdog] Skipping camera=%s (restart in progress)", camera_id)
                        continue

                    # (2) startup grace — 모델 로딩 중인 프로세스를 성급하게 재시작하지 않는다.
                    elapsed = now - worker.started_at
                    startup_grace = max(30, settings.AI_WORKER_STARTUP_GRACE_SEC)
                    if worker.started_at > 0 and elapsed < startup_grace:
                        logger.debug(
                            "[Watchdog] Skipping camera=%s (cooldown %.0fs remaining)",
                            camera_id, startup_grace - elapsed,
                        )
                        continue

                    # (3) 프로세스 alive 확인
                    reader_alive = worker.reader_process and worker.reader_process.is_alive()
                    ai_alive = worker.ai_process and worker.ai_process.is_alive()

                    # (4) heartbeat 확인 (AI 프로세스 루프 동작 여부)
                    current_hb = worker.heartbeat.value
                    prev_hb = last_heartbeats.get(camera_id, -1)
                    last_frame = worker.last_frame_at.value
                    # heartbeat은 AI가 실제 프레임을 받은 뒤에야 증가한다.
                    # 아직 프레임이 한 장도 들어오지 않았으면 heartbeat_stuck이 아니라 reader 쪽 문제로 본다.
                    hb_stuck = (prev_hb == current_hb) and ai_alive and last_frame > 0

                    # (5) last_frame_at 확인 (RTSP 스트림 프레임 수신 여부)
                    frame_never_received = reader_alive and last_frame <= 0
                    frame_stale = (
                        reader_alive
                        and last_frame > 0
                        and (now - last_frame) > FRAME_STALE_TIMEOUT
                    )

                    restart_reason = None
                    if not reader_alive:
                        restart_reason = "reader_dead"
                    elif not ai_alive:
                        restart_reason = "ai_dead"
                    elif hb_stuck:
                        restart_reason = "heartbeat_stuck"
                    elif frame_never_received:
                        restart_reason = "frame_never_received"
                    elif frame_stale:
                        restart_reason = "frame_stale"

                    logger.debug(
                        "[Watchdog] camera=%s reader=%s ai=%s hb=%d prev_hb=%d stuck=%s last_frame=%.0f frame_stale=%s",
                        camera_id, reader_alive, ai_alive, current_hb, prev_hb, hb_stuck,
                        last_frame, frame_stale,
                    )

                    if restart_reason:
                        logger.warning(
                            "[Watchdog] Restarting camera=%s reason=%s",
                            camera_id, restart_reason,
                        )
                        restarting.add(camera_id)
                        t = threading.Thread(
                            target=self._restart_worker,
                            args=(camera_id, worker.rtsp_url, worker._ai_target, worker._ai_kwargs, restarting),
                            name=f"restart-{camera_id}",
                            daemon=True,
                        )
                        t.start()
                        last_heartbeats[camera_id] = 0
                    else:
                        last_heartbeats[camera_id] = current_hb

                # (6) 워치독 루프 자체 heartbeat 갱신
                self._watchdog_heartbeat = time.time()

            except Exception:
                logger.exception("[Watchdog] Error in watchdog loop")

    def _restart_worker(
        self,
        camera_id: str,
        rtsp_url: str,
        ai_target: Callable,
        ai_kwargs: dict,
        restarting: set[str],
    ) -> None:
        """워치독이 감지한 비정상 프로세스를 재시작한다. lock 점유를 최소화한다."""
        try:
            time.sleep(RESTART_DELAY)

            # 1) lock 짧게 잡고 stop만 수행
            with self._lock:
                if camera_id not in self._workers:
                    return
                self._stop_worker(camera_id)

            # 2) MediaMTX stream path 재등록 (path가 사라졌을 수 있음)
            try:
                from service.remote.service import _start_stream
                _start_stream(camera_id, rtsp_url)
                logger.info("[Watchdog] MediaMTX stream re-registered: camera=%s", camera_id)
            except Exception:
                logger.warning("[Watchdog] MediaMTX stream re-register failed: camera=%s (continuing)", camera_id)

            # 3) lock 밖에서 새 worker 생성 + 프로세스 시작 (블로킹 구간)
            new_worker = self._create_worker(camera_id, rtsp_url, ai_target=ai_target, **ai_kwargs)
            self._start_worker(new_worker)

            # 4) lock 짧게 잡고 등록만
            with self._lock:
                self._workers[camera_id] = new_worker

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
