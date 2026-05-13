"""카메라 이벤트 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

from sqlalchemy import text
from sqlalchemy.orm import Session


def fetch_event_detail(db: Session, event_time: str, camera_id: str) -> dict | None:
    """단일 이벤트 상세를 조회한다."""
    row = db.execute(
        text(
            """
            SELECT event_time, camera_id, event_type, event_desc, file_path, isread, remark
            FROM tb_camera_event_hist
            WHERE event_time = :event_time AND camera_id = :camera_id
            """
        ),
        {"event_time": event_time, "camera_id": camera_id},
    ).fetchone()
    if not row:
        return None
    return {
        "event_time": row[0], "camera_id": row[1], "event_type": row[2],
        "event_desc": row[3], "file_path": row[4], "isread": row[5], "remark": row[6],
    }


def fetch_events(db: Session, camera_id: str | None, start_date: str | None, end_date: str | None) -> list[dict]:
    """이벤트 이력을 조회한다."""
    conditions = []
    params: dict = {}

    if camera_id:
        conditions.append("camera_id = :camera_id")
        params["camera_id"] = camera_id
    if start_date:
        conditions.append("event_time >= :start_date")
        params["start_date"] = start_date
    if end_date:
        conditions.append("event_time <= :end_date")
        params["end_date"] = end_date

    where_clause = " AND ".join(conditions) if conditions else "1=1"
    rows = db.execute(
        text(
            f"""
            SELECT event_time, camera_id, event_type, event_desc, file_path, isread, remark
            FROM tb_camera_event_hist
            WHERE {where_clause}
            ORDER BY event_time DESC
            """
        ),
        params,
    ).fetchall()

    return [
        {
            "event_time": r[0], "camera_id": r[1], "event_type": r[2],
            "event_desc": r[3], "file_path": r[4], "isread": r[5], "remark": r[6],
        }
        for r in rows
    ]


def fetch_events_by_group(db: Session, group_id: str | None, start_date: str | None, end_date: str | None) -> list[dict]:
    """그룹별 이벤트 이력을 조회한다."""
    conditions = []
    params: dict = {}

    if group_id:
        conditions.append(
            """
            e.camera_id IN (
                SELECT camera_id FROM tb_camera_monitoring_layout
                WHERE monitoring_grp_id = :group_id
            )
            """
        )
        params["group_id"] = group_id
    if start_date:
        conditions.append("e.event_time >= :start_date")
        params["start_date"] = start_date
    if end_date:
        conditions.append("e.event_time <= :end_date")
        params["end_date"] = end_date

    where_clause = " AND ".join(conditions) if conditions else "1=1"
    rows = db.execute(
        text(
            f"""
            SELECT e.event_time, e.camera_id, e.event_type, e.event_desc,
                   e.file_path, e.isread, e.remark
            FROM tb_camera_event_hist e
            WHERE {where_clause}
            ORDER BY e.event_time DESC
            """
        ),
        params,
    ).fetchall()

    return [
        {
            "event_time": r[0], "camera_id": r[1], "event_type": r[2],
            "event_desc": r[3], "file_path": r[4], "isread": r[5], "remark": r[6],
        }
        for r in rows
    ]


def update_remark(db: Session, event_time: str, camera_id: str, remark: str) -> bool:
    """이벤트 비고를 수정한다."""
    result = db.execute(
        text(
            """
            UPDATE tb_camera_event_hist
            SET remark = :remark
            WHERE event_time = :event_time AND camera_id = :camera_id
            """
        ),
        {"remark": remark, "event_time": event_time, "camera_id": camera_id},
    )
    db.commit()
    return result.rowcount > 0
