"""History 화면이 볼 이벤트 이력을 tb_camera_event_hist에서 조회하는 파일.

흐름에서의 위치:
  1. service/safety/repository.py가 AI 확정 이벤트를 tb_camera_event_hist에 저장한다.
  2. service/event/routes.py가 SafetyMonitoringHistory의 조회 요청을 받는다.
  3. 이 파일은 카메라, 이벤트 종류, 날짜, 모니터링 그룹 조건으로 저장된 이벤트를 조회한다.
  4. 조회 결과는 service/event/service.py에서 프론트가 쓰기 쉬운 형태로 가공된다.

다음에 볼 파일:
  - service/event/routes.py: 프론트가 호출하는 /cctv/ce API 입구다.
  - service/event/service.py: 조회된 ORM 객체를 cctv_id, event_type_name 같은 응답 필드로 바꾼다.
"""

from __future__ import annotations

from datetime import datetime, time, timedelta

from sqlalchemy import select, update
from sqlalchemy.orm import Session

from service.event.model import CameraEventHist
from service.profile.model import MonitoringLayout


def _parse_dt(value: str, *, end_of_day: bool = False) -> datetime:
    """문자열을 datetime으로 변환한다. end_of_day=True이면 날짜만 입력 시 23:59:59로 설정."""
    for fmt in ("%Y-%m-%d %H:%M:%S", "%Y-%m-%d", "%Y.%m.%d %H:%M:%S", "%Y.%m.%d", "%Y%m%d", "%Y%m%d%H%M%S"):
        try:
            dt = datetime.strptime(value, fmt)
            if end_of_day and fmt in ("%Y-%m-%d", "%Y.%m.%d", "%Y%m%d"):
                dt = datetime.combine(dt.date(), time(23, 59, 59))
            return dt
        except ValueError:
            continue
    return datetime.fromisoformat(value)


def fetch_event_detail(db: Session, event_time: str, camera_id: str) -> CameraEventHist | None:
    """단일 이벤트 상세를 조회한다."""
    if not event_time or not camera_id:
        return None
    stmt = select(CameraEventHist).where(
        CameraEventHist.event_time == _parse_dt(event_time),
        CameraEventHist.camera_id == camera_id,
    )
    return db.scalars(stmt).first()


def fetch_events(
    db: Session,
    camera_id: str | None,
    start_date: str | None,
    end_date: str | None,
    event_type: list[str] | None = None,
) -> list[CameraEventHist]:
    """이벤트 이력을 조회한다."""
    stmt = select(CameraEventHist)

    if camera_id:
        stmt = stmt.where(CameraEventHist.camera_id == camera_id)
    if event_type:
        stmt = stmt.where(CameraEventHist.event_type.in_(event_type))
    if start_date:
        stmt = stmt.where(CameraEventHist.event_time >= _parse_dt(start_date))
    if end_date:
        stmt = stmt.where(CameraEventHist.event_time <= _parse_dt(end_date, end_of_day=True))

    stmt = stmt.order_by(CameraEventHist.event_time.desc())
    return list(db.scalars(stmt).all())


def fetch_events_by_group(
    db: Session,
    group_id: str | None,
    start_date: str | None,
    end_date: str | None,
) -> list[CameraEventHist]:
    """그룹별 이벤트 이력을 조회한다."""
    stmt = select(CameraEventHist)

    if group_id:
        stmt = stmt.where(CameraEventHist.camera_id.in_(
            select(MonitoringLayout.camera_id)
            .where(MonitoringLayout.monitoring_grp_id == group_id)
        ))
    if start_date:
        stmt = stmt.where(CameraEventHist.event_time >= _parse_dt(start_date))
    if end_date:
        stmt = stmt.where(CameraEventHist.event_time <= _parse_dt(end_date, end_of_day=True))

    stmt = stmt.order_by(CameraEventHist.event_time.desc())
    return list(db.scalars(stmt).all())


def update_remark(db: Session, event_time: str, camera_id: str, remark: str) -> bool:
    """이벤트 비고를 수정한다."""
    stmt = (
        update(CameraEventHist)
        .where(
            CameraEventHist.event_time == _parse_dt(event_time),
            CameraEventHist.camera_id == camera_id,
        )
        .values(remark=remark)
    )
    result = db.execute(stmt)
    db.commit()
    return result.rowcount > 0
