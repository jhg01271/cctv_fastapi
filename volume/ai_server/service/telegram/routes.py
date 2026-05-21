"""텔레그램 매니저 라우터 — 프론트엔드 /cctv/manager_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.telegram.schema import TelegramManagerSave, TelegramManagerDelete
from service.telegram import service

router = APIRouter(prefix="/cctv/manager_crud", tags=["텔레그램 매니저"])


@router.get("/lists", response_model=ResultResponse, summary="텔레그램 매니저 목록 조회")
def list_managers(db: Session = Depends(get_db)):
    """모든 텔레그램 매니저 목록을 조회한다."""
    result = service.list_managers(db)
    return response(data=result, msg_key="success.read")


@router.post("/manager", response_model=ResultResponse, summary="텔레그램 매니저 저장")
def save_managers(body: list[TelegramManagerSave], db: Session = Depends(get_db)):
    """매니저 목록을 일괄 저장한다 (신규 등록 + 수정 동시 처리)."""
    service.save_managers(db, body)
    return response(msg_key="success.create")


@router.delete("/manager", response_model=ResultResponse, summary="텔레그램 매니저 삭제")
def delete_managers(body: list[TelegramManagerDelete], db: Session = Depends(get_db)):
    """매니저 목록을 일괄 삭제한다."""
    service.delete_managers(db, body)
    return response(msg_key="success.delete")


@router.get("/run_alarm/{composite_key}", response_model=ResultResponse, summary="알림 활성화")
def run_alarm(composite_key: str, db: Session = Depends(get_db)):
    """특정 매니저의 알림을 활성화한다."""
    monitoring_grp_id, chat_id = _parse_composite_key(composite_key)
    service.run_alarm(db, monitoring_grp_id, chat_id)
    return response(msg_key="success.update")


@router.get("/stop_alarm/{composite_key}", response_model=ResultResponse, summary="알림 비활성화")
def stop_alarm(composite_key: str, db: Session = Depends(get_db)):
    """특정 매니저의 알림을 비활성화한다."""
    monitoring_grp_id, chat_id = _parse_composite_key(composite_key)
    service.stop_alarm(db, monitoring_grp_id, chat_id)
    return response(msg_key="success.update")


@router.get("/run_all", response_model=ResultResponse, summary="알림 일괄 활성화")
def run_all(db: Session = Depends(get_db)):
    """모든 매니저의 알림을 일괄 활성화한다."""
    count = service.run_all(db)
    return response(data={"updated_count": count}, msg_key="success.update")


def _parse_composite_key(composite_key: str) -> tuple[str, str]:
    """composite_key를 monitoring_grp_id와 chat_id로 분리한다."""
    from core.exception.custom_exception import BadRequestException
    parts = composite_key.rsplit("_", 1)
    if len(parts) != 2:
        raise BadRequestException(msg=f"잘못된 키 형식입니다. key={composite_key}")
    return parts[0], parts[1]
