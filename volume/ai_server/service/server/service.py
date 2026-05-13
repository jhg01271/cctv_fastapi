"""AI 서버 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from datetime import datetime

from sqlalchemy.orm import Session

from service.server.model import AiServer
from service.server.repository import fetch_servers_by_comp, upsert_server, delete_server


def list_servers(db: Session, comp_id: str) -> list[AiServer]:
    """회사별 AI 서버 목록을 조회한다."""
    return fetch_servers_by_comp(db, comp_id)


def save_server(db: Session, data: dict) -> AiServer:
    """AI 서버를 등록하거나 수정한다."""
    now = datetime.now()

    existing = db.get(AiServer, data.get("ai_server_id")) if data.get("ai_server_id") else None

    if existing:
        for key, value in data.items():
            if key not in ("created_at", "created_by") and value is not None:
                setattr(existing, key, value)
        existing.updated_at = now
        return upsert_server(db, existing)

    server = AiServer(
        ai_server_id=data.get("ai_server_id", f"SI{now.strftime('%H%M%S')}"),
        comp_id=data["comp_id"],
        server_nm=data["server_nm"],
        server_host=data["server_host"],
        api_port=data.get("api_port"),
        mtx_port=data.get("mtx_port"),
        server_url=data.get("server_url"),
        remark=data.get("remark"),
        created_at=now,
        created_by=data.get("created_by", "system"),
    )
    return upsert_server(db, server)


def remove_server(db: Session, ai_server_id: str) -> bool:
    """AI 서버를 삭제한다."""
    return delete_server(db, ai_server_id)
