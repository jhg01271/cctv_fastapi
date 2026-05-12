"""camera 도메인의 DB 접근 로직을 정의한다."""

from __future__ import annotations

import json

from sqlalchemy import select, text
from sqlalchemy.orm import Session

from service.camera.model import CameraGroup


def fetch_camera_groups(db: Session, comp_id: str | None) -> list[CameraGroup]:
    """회사 식별자 조건으로 카메라 그룹을 조회한다."""
    statement = select(CameraGroup).order_by(CameraGroup.monitoring_grp_id.asc())
    if comp_id is not None:
        statement = statement.where(CameraGroup.comp_id == comp_id)
    return list(db.scalars(statement).all())


def fetch_roi(db: Session, camera_id: str) -> list:
    """카메라의 ROI 좌표 배열을 조회한다."""
    row = db.execute(
        text("SELECT point FROM tb_camera_roi WHERE camera_id = :camera_id AND is_run = true LIMIT 1"),
        {"camera_id": camera_id},
    ).fetchone()
    if row and row[0]:
        return row[0] if isinstance(row[0], list) else json.loads(row[0])
    return []


def fetch_detection_flags(db: Session, camera_id: str) -> tuple[bool, bool]:
    """detection_is_run, pose_is_run 플래그를 반환한다."""
    row = db.execute(
        text(
            """
            SELECT detection_is_run, pose_is_run
            FROM tb_camera_config
            WHERE camera_id = :camera_id
            LIMIT 1
            """
        ),
        {"camera_id": camera_id},
    ).fetchone()
    if row:
        return bool(row[0]), bool(row[1])
    return True, False


def fetch_camera_list(db: Session) -> list[dict]:
    """카메라 기본 정보 목록을 조회한다."""
    rows = db.execute(
        text(
            """
            SELECT camera_id, rtsp_url, camera_nm
            FROM tb_camera
            ORDER BY camera_id
            """
        )
    ).fetchall()
    return [{"camera_id": r[0], "rtsp_url": r[1], "camera_nm": r[2]} for r in rows]
