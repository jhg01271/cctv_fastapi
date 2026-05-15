"""MediaMTX publish gateway application."""

from __future__ import annotations

from fastapi import FastAPI

from config.config import settings
from core.exception.handlers import register_exception_handlers
from core.logging.logger import setup_logging
from stream_gateway.routes import router


setup_logging()


app = FastAPI(
    title=f"{settings.APP_NAME} Stream Gateway",
    debug=settings.DEBUG,
)

register_exception_handlers(app)
app.include_router(router)
