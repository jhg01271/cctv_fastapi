"""동기 SQLAlchemy 세션의 트랜잭션 헬퍼를 제공하는 모듈이다."""

from __future__ import annotations

from collections.abc import Callable, Iterable
from functools import wraps
from typing import ParamSpec, TypeVar, cast

from sqlalchemy import inspect
from sqlalchemy.orm import Session, object_session
from sqlalchemy.orm.state import InstanceState


ModelT = TypeVar("ModelT")
FuncP = ParamSpec("FuncP")
ReturnT = TypeVar("ReturnT")


def commit(session: Session) -> None:
    """현재 트랜잭션을 커밋한다."""
    session.commit()


def rollback(session: Session) -> None:
    """현재 트랜잭션을 롤백한다."""
    session.rollback()


def commit_or_rollback(session: Session) -> None:
    """커밋 실패 시 롤백 후 예외를 다시 발생시킨다."""
    try:
        commit(session)
    except Exception:
        rollback(session)
        raise


def commit_refresh_or_rollback(session: Session, instance: ModelT) -> ModelT:
    """커밋 후 객체를 새로고침하고, 실패 시 롤백한다."""
    try:
        commit(session)
        session.refresh(instance)
        return instance
    except Exception:
        rollback(session)
        raise


def _resolve_session_from_args(args: tuple[object, ...], kwargs: dict[str, object], func_name: str) -> Session:
    """함수 인자에서 SQLAlchemy 세션 객체를 찾아 반환한다."""
    for key in ("db", "session"):
        candidate = kwargs.get(key)
        if isinstance(candidate, Session):
            return candidate

    for arg in args:
        if isinstance(arg, Session):
            return arg

    available_kwargs = ", ".join(sorted(kwargs.keys())) if kwargs else "<none>"
    positional_types = ", ".join(type(arg).__name__ for arg in args) if args else "<none>"
    raise RuntimeError(
        f"@transactional could not resolve a SQLAlchemy Session while calling '{func_name}'. "
        "Declare the service function with 'db: Session' or 'session: Session', "
        "or pass a SQLAlchemy Session as a positional argument. "
        f"Available kwargs: {available_kwargs}. Positional arg types: {positional_types}."
    )


def _is_refreshable_orm_instance(value: object, session: Session) -> bool:
    """값이 새로고침할 수 있는 ORM 엔티티인지 확인한다."""
    state = inspect(value, raiseerr=False)
    return (
        isinstance(state, InstanceState)
        and state.persistent
        and not state.deleted
        and object_session(value) is session
    )


def _iter_refresh_targets(result: object) -> Iterable[object]:
    """반환값에서 refresh 후보 객체를 순회한다."""
    if result is None:
        return ()
    if isinstance(result, list):
        return result
    return (result,)


def _refresh_result(session: Session, result: object) -> None:
    """반환값이 ORM 객체면 새로고침한다."""
    for target in _iter_refresh_targets(result):
        if _is_refreshable_orm_instance(target, session):
            session.refresh(target)


def transactional(
    *,
    refresh: bool = False,
    flush: bool = False,
) -> Callable[[Callable[FuncP, ReturnT]], Callable[FuncP, ReturnT]]:
    """서비스 계층 CUD 함수에 커밋/롤백/후처리를 적용한다."""

    def decorator(func: Callable[FuncP, ReturnT]) -> Callable[FuncP, ReturnT]:
        @wraps(func)
        def wrapper(*args: FuncP.args, **kwargs: FuncP.kwargs) -> ReturnT:
            session = _resolve_session_from_args(
                cast(tuple[object, ...], args),
                cast(dict[str, object], kwargs),
                func.__qualname__,
            )
            try:
                result = func(*args, **kwargs)
                if flush:
                    session.flush()
                commit(session)
                if refresh:
                    _refresh_result(session, result)
                return result
            except Exception:
                rollback(session)
                raise

        return wrapper

    return decorator
