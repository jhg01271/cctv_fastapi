"""모니터링 프로필/상세 라우터 — 프론트엔드 /cctv/profile_crud, /cctv/pro_detail_crud 엔드포인트 대응."""

from __future__ import annotations

from fastapi import APIRouter, Depends
from sqlalchemy import func, select
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from core.utils.formatters.user_code import parse_comp_id
from service.profile.schema import LayoutRead, ProfileSaveItem
from service.profile.model import MonitoringLayout
from service.profile import service


def _prepare_items(items: list[ProfileSaveItem], db: Session) -> list[ProfileSaveItem]:
    """그룹 ID 채우기 + 신규 항목에 고유 item_idx 부여."""
    # 1. monitoring_grp_id 채우기
    grp_id = next((item.monitoring_grp_id for item in items if item.monitoring_grp_id), None)

    if not grp_id:
        for item in items:
            if item.camera_id:
                stmt = select(MonitoringLayout.monitoring_grp_id).where(
                    MonitoringLayout.camera_id == item.camera_id
                ).limit(1)
                grp_id = db.scalar(stmt)
                if grp_id:
                    break

    if grp_id:
        for item in items:
            if not item.monitoring_grp_id:
                item.monitoring_grp_id = grp_id

    # 2. 신규 항목(is_new=True)에 고유 item_idx 부여
    existing_max = db.scalar(
        select(func.max(MonitoringLayout.item_idx))
        .where(MonitoringLayout.monitoring_grp_id == grp_id)
    ) or 0
    # 기존 항목의 item_idx도 고려
    all_idx = [item.item_idx for item in items if item.item_idx and not item.is_new]
    next_idx = max(existing_max, max(all_idx) if all_idx else 0) + 1

    for item in items:
        if item.is_new:
            item.item_idx = next_idx
            next_idx += 1

    return items

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
    response_model=ResultResponse[list[LayoutRead]],
)
def save_profile(
    body: list[ProfileSaveItem],
    db: Session = Depends(get_db),
) -> ResultResponse[list[LayoutRead]]:
    """모니터링 레이아웃을 등록하거나 수정한다."""
    items = _prepare_items(body, db)
    results = [service.save_profile(db, item.model_dump()) for item in items]
    return response(data=results, msg_key="success.create")


@profile_router.delete(
    "/profile",
    summary="모니터링 레이아웃 삭제",
    response_model=ResultResponse[dict],
)
def delete_profile(
    body: list[str],
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """모니터링 레이아웃을 삭제한다. 프론트에서 ['CMG0007_5', ...] 형태로 전송."""
    deleted_count = 0
    for key in body:
        parts = key.rsplit("_", 1)
        if len(parts) == 2:
            data = {"monitoring_grp_id": parts[0], "item_idx": int(parts[1])}
            if service.remove_profile(db, data):
                deleted_count += 1
    return response(data={"deleted": deleted_count}, msg_key="success.delete")


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
    response_model=ResultResponse[list[LayoutRead]],
)
def save_pro_detail(
    body: list[ProfileSaveItem],
    db: Session = Depends(get_db),
) -> ResultResponse[list[LayoutRead]]:
    """레이아웃 상세를 등록하거나 수정한다."""
    items = _prepare_items(body, db)
    results = [service.save_profile(db, item.model_dump()) for item in items]
    return response(data=results, msg_key="success.create")


@pro_detail_router.delete(
    "/pro_detail",
    summary="레이아웃 상세 삭제",
    response_model=ResultResponse[dict],
)
def delete_pro_detail(
    body: list[str],
    db: Session = Depends(get_db),
) -> ResultResponse[dict]:
    """레이아웃 상세를 삭제한다. 프론트에서 ['CMG0007_5', ...] 형태로 전송."""
    deleted_count = 0
    for key in body:
        parts = key.rsplit("_", 1)
        if len(parts) == 2:
            data = {"monitoring_grp_id": parts[0], "item_idx": int(parts[1])}
            if service.remove_profile(db, data):
                deleted_count += 1
    return response(data={"deleted": deleted_count}, msg_key="success.delete")
