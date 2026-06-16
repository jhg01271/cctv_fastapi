"""프론트에서 CCTV AI 시작/중지 버튼을 눌렀을 때 처음 들어오는 라우터 파일.

흐름에서의 위치:
  1. Frontend가 /cctv/remote/run_all, /run_cctv/{camera_id}, /stop_cctv/{camera_id}를 호출한다.
  2. 이 파일은 HTTP 요청을 받아 service/remote/service.py의 실행/중지 함수로 넘긴다.
  3. 실제 stream_gateway 요청, jit_only 분기, 프로세스 등록은 service.py에서 처리한다.

다음에 볼 파일:
  - service/remote/service.py: 카메라 정보를 읽고 CameraProcessManager에 등록한다.
"""

from __future__ import annotations

from fastapi import APIRouter, BackgroundTasks, Depends
from sqlalchemy.orm import Session

from core.database.session import get_db
from core.response.response import ResultResponse, response
from service.remote import service

router = APIRouter(prefix="/cctv/remote", tags=["AI 프로세스 제어"])


@router.get(
    "/run_all",
    summary="전체 카메라 AI 시작",
    response_model=ResultResponse[dict],
)
def run_all(background_tasks: BackgroundTasks, db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """전체 카메라 AI 프로세스 시작 작업을 백그라운드로 예약한다."""
    total = service.count_cameras(db)
    if not service.mark_run_all_started():
        result = {"status": "already_running", "total": total}
        return response(data=result, msg_key="success.read")

    background_tasks.add_task(service.run_all_background)
    result = {"status": "queued", "total": total}
    return response(data=result, msg_key="success.read")


@router.get(
    "/stop_all",
    summary="전체 카메라 AI 중지",
    response_model=ResultResponse[dict],
)
def stop_all(background_tasks: BackgroundTasks, db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """전체 카메라 AI 프로세스 중지 작업을 백그라운드로 예약한다."""
    total = service.count_running_cameras()
    background_tasks.add_task(service.stop_all_background)
    result = {"status": "queued", "total": total}
    return response(data=result, msg_key="success.read")


@router.get(
    "/run_cctv/{camera_id}",
    summary="개별 카메라 AI 시작",
    response_model=ResultResponse[dict],
)
def run_cctv(camera_id: str, background_tasks: BackgroundTasks, db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """개별 카메라 AI 프로세스 시작을 백그라운드로 예약한다."""
    service.update_camera_run_state_only(db, camera_id, True)
    background_tasks.add_task(service.run_cctv_background, camera_id)
    return response(data={"camera_id": camera_id, "status": "queued"}, msg_key="success.read")


@router.get(
    "/stop_cctv/{camera_id}",
    summary="개별 카메라 AI 중지",
    response_model=ResultResponse[dict],
)
def stop_cctv(camera_id: str, background_tasks: BackgroundTasks, db: Session = Depends(get_db)) -> ResultResponse[dict]:
    """개별 카메라 AI 프로세스 중지를 백그라운드로 예약한다."""
    service.update_camera_run_state_only(db, camera_id, False)
    background_tasks.add_task(service.stop_cctv_background, camera_id)
    return response(data={"camera_id": camera_id, "status": "queued"}, msg_key="success.read")


@router.get(
    "/cctv_pid_chk",
    summary="AI 프로세스 PID 상태 확인",
    response_model=ResultResponse[dict],
)
def cctv_pid_chk() -> ResultResponse[dict]:
    """현재 실행 중인 AI 프로세스 목록을 반환한다."""
    result = service.check_pid()
    return response(data=result, msg_key="success.read")


@router.get(
    "/get_cctv_play_url/{camera_id}",
    summary="카메라 WebRTC 재생 URL 조회",
    response_model=ResultResponse[dict],
)
def get_cctv_play_url(camera_id: str) -> ResultResponse[dict]:
    """카메라의 WebRTC 재생 URL을 반환한다."""
    result = service.get_play_url(camera_id)
    return response(data=result, msg_key="success.read")
