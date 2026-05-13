"""FastAPI 애플리케이션 생성과 공통 라우터 등록을 담당하는 진입 파일이다."""

import asyncio
from contextlib import asynccontextmanager
from pathlib import Path

from fastapi import FastAPI

from config.config import settings
from core.ai.process_manager import init_manager
from service.safety.processor import safety_process
from service.safety.worker import SafetyDBWorker
from service.progress.worker import ProgressWorker
from core.ai.ws_bridge import ws_bridge
from core.database.session import initialize_database
from core.exception.handlers import register_exception_handlers
from core.logging.logger import get_logger, setup_logging
from core.requests.http_client import http_client
from service.safety.routes import router as safety_router
from service.progress.routes import router as progress_router
from service.server.routes import router as server_router
from service.cctv.routes import router as cctv_router
from service.roi.routes import router as roi_router
from service.event.routes import router as event_router
from service.profile.routes import profile_router, pro_detail_router
from service.save.routes import router as save_router
from service.remote.routes import router as remote_router
import service.server.model  # noqa: F401
import service.cctv.model  # noqa: F401
import service.roi.model  # noqa: F401
import service.event.model  # noqa: F401
import service.profile.model  # noqa: F401


setup_logging()
logger = get_logger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """애플리케이션 시작과 종료 시 공통 자원을 관리한다."""
    logger.info("Application startup initiated.")

    if settings.APP_ENV == "dev":
        initialize_database()

    await http_client.startup()

    # WebSocket 브리지 시작 (mp.Queue → asyncio.Queue 팬아웃)
    loop = asyncio.get_event_loop()
    ws_bridge.start(loop)
    logger.info("WebSocket bridge started.")

    # 안전관리 DB 워커 시작 (event_queue → SQLAlchemy 저장)
    safety_db_worker = SafetyDBWorker()
    safety_db_worker.start()
    logger.info("Safety DB worker started.")

    # 카메라 프로세스 매니저 초기화 (ai_target = safety_process)
    manager = init_manager(ai_target=safety_process)
    manager.start()
    logger.info("Camera process manager started.")

    # 공정률 워커 시작 (스냅샷 추론 + result_queue → SQLAlchemy 저장)
    progress_worker: ProgressWorker | None = None
    if Path(settings.MODEL_PROGRESS_PT).exists():
        progress_worker = ProgressWorker()
        progress_worker.start()
        logger.info("Progress worker started.")
    else:
        logger.warning("Progress model not found (%s). Progress worker skipped.", settings.MODEL_PROGRESS_PT)

    try:
        logger.info("Application startup completed.")
        yield
    finally:
        logger.info("Application shutdown initiated.")
        manager.stop()
        safety_db_worker.stop()
        if progress_worker:
            progress_worker.stop()
        ws_bridge.stop()
        await http_client.shutdown()
        logger.info("Application shutdown completed.")


def register_routes(app: FastAPI) -> None:
    """서비스 라우터를 애플리케이션에 등록한다."""
    app.include_router(safety_router)
    app.include_router(progress_router)
    app.include_router(server_router)
    app.include_router(cctv_router)
    app.include_router(roi_router)
    app.include_router(event_router)
    app.include_router(profile_router)
    app.include_router(pro_detail_router)
    app.include_router(save_router)
    app.include_router(remote_router)


def create_app() -> FastAPI:
    """FastAPI 애플리케이션 인스턴스를 생성하고 공통 설정을 적용한다."""
    app = FastAPI(
        title=settings.APP_NAME,
        debug=settings.DEBUG,
        lifespan=lifespan,
    )
    register_exception_handlers(app)
    register_routes(app)
    logger.info(
        "FastAPI application created. env=%s debug=%s host=%s port=%s",
        settings.APP_ENV,
        settings.DEBUG,
        settings.APP_HOST,
        settings.APP_PORT,
    )
    return app


app = create_app()
