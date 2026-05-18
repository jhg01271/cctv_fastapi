"""환경별 .env 파일을 로드하고 애플리케이션 설정을 관리한다."""

from __future__ import annotations

import os
from pathlib import Path
from typing import Any

from pydantic import Field, computed_field, field_validator, model_validator
from pydantic_settings import BaseSettings, SettingsConfigDict
from sqlalchemy import URL


BASE_DIR = Path(__file__).resolve().parent.parent
CONFIG_DIR = BASE_DIR / "config"
DEFAULT_APP_ENV = "dev"
SUPPORTED_APP_ENVS = {"dev", "prod"}
ENV_FILE_BY_APP_ENV = {
    "dev": CONFIG_DIR / ".env.dev",
    "prod": CONFIG_DIR / ".env.prod",
}
TRUE_VALUES = {"1", "true", "yes", "y", "on"}
FALSE_VALUES = {"0", "false", "no", "n", "off", "release", "prod", "production"}


def resolve_app_env() -> str:
    """부트스트랩에 사용할 APP_ENV 값을 결정한다."""
    app_env = os.getenv("APP_ENV", DEFAULT_APP_ENV).strip().lower()
    if app_env not in SUPPORTED_APP_ENVS:
        return DEFAULT_APP_ENV
    return app_env


class Settings(BaseSettings):
    """애플리케이션 전역 설정을 검증하고 보관한다."""

    APP_NAME: str = Field(default="FASTAPI Framework")
    APP_ENV: str = Field(default=DEFAULT_APP_ENV)
    DEBUG: bool = Field(default=True)
    APP_HOST: str = Field(default="127.0.0.1")
    APP_PORT: int = Field(default=8000)
    DEFAULT_LANGUAGE: str = Field(default="ko")
    DB_HOST: str = Field(default="127.0.0.1")
    DB_PORT: int = Field(default=5432)
    DB_NAME: str = Field(default="fastapi")
    DB_USER: str = Field(default="postgres")
    DB_PASSWORD: str = Field(default="postgres")
    DB_ECHO: bool = Field(default=False)
    DB_POOL_SIZE: int = Field(default=5)
    DB_MAX_OVERFLOW: int = Field(default=10)
    LOG_LEVEL: str = Field(default="INFO")
    HTTP_TIMEOUT: float = Field(default=10.0)
    STREAM_GATEWAY_URL: str = Field(default="")

    # MediaMTX (미디어 서버)
    MEDIA_SERVER_HOST: str = Field(default="127.0.0.1")
    MEDIA_SERVER_EXTERNAL_HOST: str = Field(default="localhost")
    MEDIA_SERVER_RTSP_PORT: int = Field(default=8554)
    MEDIA_SERVER_WEBRTC_PORT: int = Field(default=8889)
    MEDIA_SERVER_API_PORT: int = Field(default=9997)

    # AI 모델 경로
    MODEL_SAFETY_PT: str = Field(default="./model/yolo12m4.pt")
    MODEL_POSE_PT: str = Field(default="./model/collapes.pt")
    MODEL_PROGRESS_PT: str = Field(default="./model/progress_model.pt")

    # AI 처리 설정
    AI_FRAME_SKIP: int = Field(default=3)
    AI_CONFIDENCE_THRESHOLD: float = Field(default=0.8)
    AI_STARTUP_DELAY: int = Field(default=10)

    # 이벤트 쿨다운 (초) — 모든 이벤트 공통
    AI_EVENT_COOLDOWN_SEC: int = Field(default=600)

    # E003
    AI_COLLAPSE_SUSTAIN_SEC: float = Field(default=15.0) # collapse 지속 시간 임계값 (초)
    AI_E003_BUFFER_MAX: int = Field(default=300) # 프레임 버퍼 최대 크기

    # E004
    AI_E004_PREDICT_SEC: float = Field(default=2.0) # 이동 벡터 예측 시간 (초)
    AI_E004_PERSON_SUSTAIN_SEC: float = Field(default=3.0) # 예측 영역 내 person 지속 시간 임계값 (초)
    AI_E004_HISTORY_MAXLEN: int = Field(default=10) #  위험체 위치 이력 최대 샘플 수

    # 저장 경로
    EVENT_IMAGE_DIR: str = Field(default="./event_images")
    JIT_IMAGE_DIR: str = Field(default="./jit_images")
    PROGRESS_RESULT_DIR: str = Field(default="./progress_results")

    # JIT 학습용 이미지 수집 주기 (초)
    JIT_SHORT_INTERVAL_SEC: int = Field(default=600)      # 10분
    JIT_LONG_INTERVAL_SEC: int = Field(default=3600)       # 60분
    JIT_AUTO_MIN_INTERVAL_SEC: int = Field(default=3600)   # 자동 변화 감지 최소 간격 (1시간)
    JIT_AUTO_MEAN_DIFF: float = Field(default=10.0)        # 자동 변화 감지 평균 차이 임계값
    JIT_AUTO_CHANGE_RATIO: float = Field(default=0.05)     # 자동 변화 감지 변화율 임계값 (5%)

    # 공정률 처리 주기
    PROGRESS_CYCLE_SEC: int = Field(default=60)

    model_config = SettingsConfigDict(
        env_file=ENV_FILE_BY_APP_ENV[resolve_app_env()],
        env_file_encoding="utf-8",
        case_sensitive=True,
        extra="ignore",
    )

    @field_validator("APP_ENV", mode="before")
    @classmethod
    def validate_app_env(cls, value: Any) -> str:
        app_env = str(value or DEFAULT_APP_ENV).strip().lower()
        if app_env not in SUPPORTED_APP_ENVS:
            return DEFAULT_APP_ENV
        return app_env

    @field_validator("DEBUG", "DB_ECHO", mode="before")
    @classmethod
    def validate_boolean(cls, value: Any) -> bool:
        if isinstance(value, bool):
            return value
        normalized = str(value or "").strip().lower()
        if normalized in TRUE_VALUES:
            return True
        if normalized in FALSE_VALUES:
            return False
        return False

    @model_validator(mode="after")
    def normalize_settings(self) -> "Settings":
        self.DEFAULT_LANGUAGE = self.DEFAULT_LANGUAGE.lower()
        self.LOG_LEVEL = self.LOG_LEVEL.upper()
        if self.APP_ENV == "prod":
            self.DEBUG = False
            self.DB_ECHO = False
        return self

    @computed_field  # type: ignore[prop-decorator]
    @property
    def MEDIA_SERVER_RTSP_URL(self) -> str:
        """MediaMTX RTSP 베이스 URL을 계산한다."""
        return f"rtsp://{self.MEDIA_SERVER_HOST}:{self.MEDIA_SERVER_RTSP_PORT}"

    @computed_field  # type: ignore[prop-decorator]
    @property
    def MEDIA_SERVER_API_URL(self) -> str:
        """MediaMTX REST API 베이스 URL을 계산한다."""
        return f"http://{self.MEDIA_SERVER_HOST}:{self.MEDIA_SERVER_API_PORT}"

    @computed_field  # type: ignore[prop-decorator]
    @property
    def DATABASE_URL(self) -> str:
        """PostgreSQL 접속 URL을 계산한다."""
        return URL.create(
            drivername="postgresql+psycopg",
            username=self.DB_USER,
            password=self.DB_PASSWORD,
            host=self.DB_HOST,
            port=self.DB_PORT,
            database=self.DB_NAME,
        ).render_as_string(hide_password=False)


settings = Settings()
