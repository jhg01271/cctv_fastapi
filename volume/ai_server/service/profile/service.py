"""모니터링 프로필(레이아웃) 도메인의 비즈니스 로직을 정의한다."""

from __future__ import annotations

from concurrent.futures import ThreadPoolExecutor, wait
from datetime import datetime

from sqlalchemy.orm import Session

from core.ai.media_server import get_stream_urls, wait_stream_ready
from core.logging.logger import get_logger
from service.profile.model import MonitoringLayout
from service.profile.repository import fetch_groups_by_comp, fetch_layouts_by_group, upsert_layout, delete_layout

logger = get_logger(__name__)


def _ensure_camera_stream_ready(camera_id: str, pid: str | None, rtsp_addr: str | None) -> None:
    """화면에 보여줄 카메라 영상이 MediaMTX에 이미 올라와 있는지 확인한다.

    pid가 비어 있으면 사용자가 실행하지 않은 카메라로 보고 건너뛴다.
    실행 중인 카메라인데 아직 MediaMTX에서 재생 준비가 안 됐다면
    stream_gateway에 "이 카메라 영상을 방송 가능한 상태로 올려줘"라고 요청한다.
    """
    if not camera_id or not pid or not rtsp_addr:
        return
    if wait_stream_ready(camera_id, timeout=0.5):
        return
    try:
        from service.remote.service import _start_stream

        # ai_server가 직접 FFmpeg를 켜지 않고, stream_gateway에 HTTP로 시작 요청을 보낸다.
        _start_stream(camera_id, rtsp_addr)
    except Exception:
        logger.exception("[Profile] Failed to ensure stream ready. camera=%s", camera_id)


def _ensure_group_streams_ready(rows) -> None:
    """모니터링 화면에 들어갈 여러 카메라 영상을 한꺼번에 준비한다.

    SafetyMonitoring은 보통 6개 영상을 동시에 띄우므로, 한 카메라씩 순서대로
    준비하면 첫 화면이 너무 늦어진다. 그래서 카메라별 준비 작업을 병렬로 실행한다.
    """
    targets = {
        camera.camera_id: {
            "camera_id": camera.camera_id,
            "pid": camera.pid,
            "rtsp_addr": camera.rtsp_addr,
        }
        for _, camera in rows
        if camera is not None
    }
    if not targets:
        return
    with ThreadPoolExecutor(max_workers=min(6, len(targets))) as executor:
        futures = [
            executor.submit(
                _ensure_camera_stream_ready,
                target["camera_id"],
                target["pid"],
                target["rtsp_addr"],
            )
            for target in targets.values()
        ]
        wait(futures)


def list_profiles(db: Session, comp_id: str) -> list[dict]:
    """회사별 모니터링 그룹 목록을 조회한다."""
    groups = fetch_groups_by_comp(db, comp_id)
    return [
        {"Profile_id": g.monitoring_grp_id, "Profile_name": g.grp_nm}
        for g in groups
    ]


def list_group_details(db: Session, monitoring_grp_id: str) -> list[dict]:
    """SafetyMonitoring 화면이 그릴 카메라 타일 목록을 만든다.

    프론트엔드는 이 응답의 cctv_play_url을 iframe src로 사용한다.
    그래서 목록을 반환하기 전에, 실행 중인 카메라의 영상 스트림이 먼저 준비됐는지 확인한다.
    """
    rows = fetch_layouts_by_group(db, monitoring_grp_id)
    _ensure_group_streams_ready(rows)
    result = []
    for layout, camera in rows:
        urls = get_stream_urls(layout.camera_id)
        result.append({
            "i": str(layout.item_idx),
            "x": layout.coordinate_x,
            "y": layout.coordinate_y,
            "w": layout.item_width,
            "h": layout.item_height,
            "cctv_id": layout.camera_id,
            "cctv_play_url": urls.get("webrtc", ""),
            "ratio": "16:9",
            "title": layout.title or "",
        })
    return result


def save_profile(db: Session, data: dict) -> MonitoringLayout:
    """레이아웃을 등록하거나 수정한다."""
    now = datetime.now()
    monitoring_grp_id = data.get("monitoring_grp_id")
    item_idx = data.get("item_idx")

    existing = db.get(MonitoringLayout, (monitoring_grp_id, item_idx)) if monitoring_grp_id and item_idx else None

    if existing:
        for key, value in data.items():
            if key not in ("created_at", "created_by") and value is not None:
                setattr(existing, key, value)
        existing.updated_at = now
        return upsert_layout(db, existing)

    layout = MonitoringLayout(
        monitoring_grp_id=monitoring_grp_id,
        item_idx=item_idx,
        coordinate_x=data.get("coordinate_x", 0),
        coordinate_y=data.get("coordinate_y", 0),
        item_width=data.get("item_width", 15),
        item_height=data.get("item_height", 6),
        camera_id=data["camera_id"],
        sort=data.get("sort"),
        title=data.get("title"),
        created_at=now,
        created_by=data.get("created_by", "system"),
    )
    return upsert_layout(db, layout)


def remove_profile(db: Session, data: dict) -> bool:
    """레이아웃을 삭제한다."""
    return delete_layout(db, data.get("monitoring_grp_id"), data.get("item_idx"))
