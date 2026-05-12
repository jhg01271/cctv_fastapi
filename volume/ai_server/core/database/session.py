"""동기 SQLAlchemy 엔진과 세션 구성을 담당하는 모듈이다."""

from __future__ import annotations

from collections.abc import Generator

from sqlalchemy import create_engine, text
from sqlalchemy.engine import Engine
from sqlalchemy.orm import DeclarativeBase, Session, sessionmaker

from config.config import settings


class Base(DeclarativeBase):
    """공통 ORM 베이스 클래스다."""


_cached_engine: Engine | None = None
_cached_session_factory: sessionmaker[Session] | None = None


def create_engine_instance(database_url: str) -> Engine:
    """메인 DB 엔진을 생성한다."""
    return create_engine(
        database_url,
        echo=settings.DB_ECHO,
        pool_pre_ping=True,
        pool_size=settings.DB_POOL_SIZE,
        max_overflow=settings.DB_MAX_OVERFLOW,
    )


def get_engine() -> Engine:
    """메인 DB 엔진을 지연 생성 후 반환한다."""
    global _cached_engine
    if _cached_engine is None:
        _cached_engine = create_engine_instance(settings.DATABASE_URL)
    return _cached_engine


def create_session_local(engine: Engine) -> sessionmaker[Session]:
    """동기 SQLAlchemy 세션 팩토리를 생성한다."""
    return sessionmaker(bind=engine, autocommit=False, autoflush=False, class_=Session)


def get_session_local() -> sessionmaker[Session]:
    """현재 프로젝트 표준 세션 팩토리를 반환한다."""
    global _cached_session_factory
    if _cached_session_factory is None:
        _cached_session_factory = create_session_local(get_engine())
    return _cached_session_factory


SessionLocal = get_session_local()


def get_db() -> Generator[Session, None, None]:
    """FastAPI 의존성 주입용 동기 DB 세션을 제공한다."""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def initialize_database() -> None:
    """개발 단계에서 필요한 테이블을 생성한다."""
    Base.metadata.create_all(bind=get_engine())


def check_db_connection(db: Session) -> int:
    """간단한 SELECT 1로 DB 연결 상태를 확인한다."""
    return db.execute(text("SELECT 1")).scalar_one()
