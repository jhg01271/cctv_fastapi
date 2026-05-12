"""공정률 DB 접근 로직."""

from __future__ import annotations

import json

from sqlalchemy import text
from sqlalchemy.orm import Session

from core.utils.formatters.datetime import format_datetime


def count_results(db: Session, camera_id: str | None = None) -> int:
    """공정률 결과 전체 건수를 반환한다."""
    where = "WHERE camera_id = :camera_id" if camera_id else ""
    params: dict = {}
    if camera_id:
        params["camera_id"] = camera_id

    row = db.execute(
        text(f"SELECT COUNT(*) FROM tb_twin_image {where}"),
        params,
    ).fetchone()
    return int(row[0]) if row else 0


def fetch_latest_results(
    db: Session,
    camera_id: str | None = None,
    offset: int = 0,
    size: int = 20,
) -> list[dict]:
    """공정률 결과 이미지 이력을 최신순으로 페이지 단위로 조회한다."""
    where = "WHERE camera_id = :camera_id" if camera_id else ""
    params: dict = {"offset": offset, "size": size}
    if camera_id:
        params["camera_id"] = camera_id

    rows = db.execute(
        text(
            f"""
            SELECT result_id, camera_id, image_path, created_at
            FROM tb_twin_image
            {where}
            ORDER BY created_at DESC
            LIMIT :size OFFSET :offset
            """
        ),
        params,
    ).fetchall()

    return [
        {
            "result_id":  r[0],
            "camera_id":  r[1],
            "image_path": r[2],
            "created_at": format_datetime(r[3]),
        }
        for r in rows
    ]


def save_result_record(db: Session, camera_id: str, image_path: str, timestamp: str) -> int | None:
    """공정률 결과 이미지 경로를 tb_twin_image에 저장하고 생성된 PK를 반환한다."""
    row = db.execute(
        text(
            """
            INSERT INTO tb_twin_image (camera_id, image_path, created_at)
            VALUES (:camera_id, :image_path, :timestamp)
            RETURNING result_id
            """
        ),
        {"camera_id": camera_id, "image_path": image_path, "timestamp": timestamp},
    ).fetchone()
    return row[0] if row else None


def save_detection_records(
    db: Session,
    result_id: int,
    camera_id: str,
    detections: list[dict],
    timestamp: str,
) -> None:
    """격자별 감지 결과를 tb_twin_detection에 저장한다.

    detections: [{"row": int, "col": int, "class": str, "conf": float}, ...]
    """
    if not detections:
        return

    db.execute(
        text(
            """
            INSERT INTO tb_twin_detection
                (result_id, camera_id, row, col, class, conf, created_at)
            VALUES (:result_id, :camera_id, :row, :col, :class, :conf, :timestamp)
            """
        ),
        [
            {
                "result_id": result_id,
                "camera_id": camera_id,
                "row":       d["row"],
                "col":       d["col"],
                "class":     d["class"],
                "conf":      d["conf"],
                "timestamp": timestamp,
            }
            for d in detections
        ],
    )


def fetch_grid_coordinates(db: Session, camera_id: str) -> list | None:
    """tb_camera_grid에서 격자 좌표를 조회한다."""
    row = db.execute(
        text("SELECT grid_data FROM tb_camera_grid WHERE camera_id = :camera_id LIMIT 1"),
        {"camera_id": camera_id},
    ).fetchone()
    if row and row[0]:
        return row[0] if isinstance(row[0], list) else json.loads(row[0])
    return None


def fetch_progress_cameras(db: Session) -> list[dict]:
    """공정률 처리 대상 카메라 목록을 조회한다."""
    rows = db.execute(
        text(
            """
            SELECT camera_id, rtsp_url, sort_direction
            FROM tb_camera
            WHERE jit_only = true
            ORDER BY camera_id
            """
        )
    ).fetchall()

    return [
        {
            "camera_id":     r[0],
            "rtsp_url":      r[1],
            "sort_direction": r[2] or "right",
        }
        for r in rows
    ]
