"""AI 서버 관리 라우터 — 프론트엔드 /cctv/server_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.server.schema import AiServerCreate, AiServerDelete, AiServerRead
from service.server import service

router = APIRouter(prefix="/cctv/server_crud", tags=["AI 서버 관리"])


@router.get(
    "/servers/{comp_id}",
    summary="AI 서버 목록 조회",
    response_model=ResultResponse[list[AiServerRead]],
)
def get_servers(
    comp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[AiServerRead]]:
    """회사별 AI 서버 목록을 조회한다."""
    result = service.list_servers(db, parse_comp_id(comp_id))
    return response(data=result, msg_key="success.read")


@router.get(
    "/server/{ai_server_id}",
    summary="단일 AI 서버 조회",
    response_model=ResultResponse[AiServerRead],
)
def get_server(
    ai_server_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[AiServerRead]:
    """단일 AI 서버 정보를 조회한다."""
    result = service.get_server(db, ai_server_id)
    return response(data=result, msg_key="success.read")


@router.post(
    "/server",
    summary="AI 서버 등록/수정",
    response_model=ResultResponse[AiServerRead],
)
def save_server(
    body: AiServerCreate,
    db: Session = Depends(get_db),
) -> ResultResponse[AiServerRead]:
    """AI 서버를 등록하거나 수정한다."""
    result = service.save_server(db, body.model_dump())
    return response(data=result, msg_key="success.create")


@router.delete(
    "/server",
    summary="AI 서버 삭제",
    response_model=ResultResponse[dict],
)
def delete_server(
    body: AiServerDelete,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """AI 서버를 삭제한다."""
    deleted = service.remove_server(db, body.ai_server_id)
    return response(data={"deleted": deleted}, msg_key="success.delete")
