"""애플리케이션과 uvicorn 로그를 함께 관리하는 공통 로거 모듈이다."""
#커스텀하게 로그를 작성할수 있는 기능 필요!!
import logging
import logging.config

from config.config import settings


DEFAULT_LOG_FORMAT = "%(asctime)s | %(levelname)s | %(name)s | %(message)s"


def build_logging_config() -> dict:
    """공통 로깅 설정 딕셔너리를 생성한다."""
    log_level = settings.LOG_LEVEL
    return {
        "version": 1,
        "disable_existing_loggers": False,
        "formatters": {
            "default": {"format": DEFAULT_LOG_FORMAT},
            "uvicorn": {"format": DEFAULT_LOG_FORMAT},
        },
        "handlers": {
            "console": {
                "class": "logging.StreamHandler",
                "formatter": "default",
                "level": log_level,
            },
        },
        "loggers": {
            "": {"handlers": ["console"], "level": log_level},
            "uvicorn": {
                "handlers": ["console"],
                "level": log_level,
                "propagate": False,
            },
            "uvicorn.error": {
                "handlers": ["console"],
                "level": log_level,
                "propagate": False,
            },
            "uvicorn.access": {
                "handlers": ["console"],
                "level": log_level,
                "propagate": False,
            },
        },
    }


def setup_logging() -> None:
    """공통 로깅 설정을 적용한다."""
    logging.config.dictConfig(build_logging_config())


def get_logger(name: str) -> logging.Logger:
    """지정한 이름의 로거를 공통 설정과 함께 반환한다."""
    if not logging.getLogger().handlers:
        setup_logging()
    return logging.getLogger(name)
