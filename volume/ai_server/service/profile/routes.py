"""모니터링 프로필/상세 라우터 — 프론트엔드 /cctv/profile_crud, /cctv/pro_detail_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends, Request
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.profile import service

profile_router = APIRouter(prefix="/cctv/profile_crud", tags=["profile_crud"])
pro_detail_router = APIRouter(prefix="/cctv/pro_detail_crud", tags=["pro_detail_crud"])


# ── profile_crud ─────────────────────────────────────────────

@profile_router.get(
    "/profiles/{comp_id}",
    summary="모니터링 그룹 목록 조회",
    response_model=ResultResponse[list[dict]],
)
def get_profiles(
    comp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[dict]]:
    """회사별 모니터링 그룹 목록을 조회한다."""
    result = service.list_profiles(db, parse_comp_id(comp_id))
    return response(data=result, msg_key="success.read")


@profile_router.get(
    "/profile/{monitoring_grp_id}",
    summary="단일 그룹 레이아웃 조회",
    response_model=ResultResponse[list[dict]],
)
def get_profile(
    monitoring_grp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[dict]]:
    """단일 그룹의 레이아웃 목록을 조회한다."""
    result = service.list_group_details(db, monitoring_grp_id)
    return response(data=result, msg_key="success.read")


@profile_router.post(
    "/profile",
    summary="모니터링 레이아웃 등록/수정",
    response_model=ResultResponse[dict],
)
async def save_profile(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """모니터링 레이아웃을 등록하거나 수정한다."""
    body = await request.json()
    result = service.save_profile(db, body)
    return response(data=result, msg_key="success.create")


@profile_router.delete(
    "/profile",
    summary="모니터링 레이아웃 삭제",
    response_model=ResultResponse[dict],
)
async def delete_profile(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """모니터링 레이아웃을 삭제한다."""
    body = await request.json()
    deleted = service.remove_profile(db, body)
    return response(data={"deleted": deleted}, msg_key="success.delete")


# ── pro_detail_crud ──────────────────────────────────────────

@pro_detail_router.get(
    "/group_pro_detail/{monitoring_grp_id}",
    summary="그룹별 레이아웃 상세 조회",
    response_model=ResultResponse[list[dict]],
)
def get_group_pro_detail(
    monitoring_grp_id: str,
    db: Session = Depends(get_db),
) -> ResultResponse[list[dict]]:
    """그룹별 모니터링 레이아웃 상세를 조회한다."""
    result = service.list_group_details(db, monitoring_grp_id)
    return response(data=result, msg_key="success.read")


@pro_detail_router.post(
    "/pro_detail",
    summary="레이아웃 상세 등록/수정",
    response_model=ResultResponse[dict],
)
async def save_pro_detail(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """레이아웃 상세를 등록하거나 수정한다."""
    body = await request.json()
    result = service.save_profile(db, body)
    return response(data=result, msg_key="success.create")


@pro_detail_router.delete(
    "/pro_detail",
    summary="레이아웃 상세 삭제",
    response_model=ResultResponse[dict],
)
async def delete_pro_detail(
    request: Request,
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """레이아웃 상세를 삭제한다."""
    body = await request.json()
    deleted = service.remove_profile(db, body)
    return response(data={"deleted": deleted}, msg_key="success.delete")
