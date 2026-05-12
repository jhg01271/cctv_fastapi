"""안전관리 DB 접근 로직."""

from __future__ import annotations

from sqlalchemy import text
from sqlalchemy.orm import Session

from core.utils.formatters.datetime import format_datetime


def count_event_hist(db: Session, camera_id: str | None = None) -> int:
    """이벤트 이력 전체 건수를 반환한다."""
    where = "WHERE camera_id = :camera_id" if camera_id else ""
    params: dict = {}
    if camera_id:
        params["camera_id"] = camera_id

    row = db.execute(
        text(f"SELECT COUNT(*) FROM tb_camera_event_hist {where}"),
        params,
    ).fetchone()
    return int(row[0]) if row else 0


def fetch_event_hist(
    db: Session,
    camera_id: str | None = None,
    offset: int = 0,
    size: int = 20,
) -> list[dict]:
    """이벤트 이력을 최신순으로 페이지 단위로 조회한다."""
    where = "WHERE camera_id = :camera_id" if camera_id else ""
    params: dict = {"offset": offset, "size": size}
    if camera_id:
        params["camera_id"] = camera_id

    rows = db.execute(
        text(
            f"""
            SELECT event_id, camera_id, event_type, event_desc,
                   image_path, created_at, is_checked
            FROM tb_camera_event_hist
            {where}
            ORDER BY created_at DESC
            LIMIT :size OFFSET :offset
            """
        ),
        params,
    ).fetchall()
    return [
        {
            "event_id":   r[0],
            "camera_id":  r[1],
            "event_type": r[2],
            "event_desc": r[3],
            "image_path": r[4],
            "created_at": format_datetime(r[5]),
            "is_checked": r[6],
        }
        for r in rows
    ]


def save_event(
    db: Session,
    camera_id: str,
    event_key: str,
    image_path: str | None,
    timestamp: str,
) -> None:
    """이벤트 이력을 tb_camera_event_hist에 저장한다."""
    from service.safety.events import EVENT_CODE
    code, desc = EVENT_CODE.get(event_key, ("E999", event_key))
    db.execute(
        text(
            """
            INSERT INTO tb_camera_event_hist
                (camera_id, event_type, event_desc, image_path, created_at, is_checked, checked_by)
            VALUES (:camera_id, :event_type, :event_desc, :image_path, :timestamp, false, null)
            """
        ),
        {
            "camera_id":  camera_id,
            "event_type": code,
            "event_desc": desc,
            "image_path": image_path,
            "timestamp":  timestamp,
        },
    )
    db.commit()
