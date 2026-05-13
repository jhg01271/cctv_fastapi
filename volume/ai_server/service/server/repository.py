"""AI 서버 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import select, delete as sa_delete
from sqlalchemy.orm import Session

from service.server.model import AiServer


def fetch_servers_by_comp(db: Session, comp_id: str) -> list[AiServer]:
    """회사 식별자 기준으로 AI 서버 목록을 조회한다."""
    stmt = select(AiServer).where(AiServer.comp_id == comp_id).order_by(AiServer.ai_server_id)
    return list(db.scalars(stmt).all())


def upsert_server(db: Session, server: AiServer) -> AiServer:
    """AI 서버를 등록하거나 수정한다."""
    merged = db.merge(server)
    db.commit()
    db.refresh(merged)
    return merged


def delete_server(db: Session, ai_server_id: str) -> bool:
    """AI 서버를 삭제한다."""
    result = db.execute(
        sa_delete(AiServer).where(AiServer.ai_server_id == ai_server_id)
    )
    db.commit()
    return result.rowcount > 0
