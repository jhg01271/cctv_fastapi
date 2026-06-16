"""DB 저장이 끝난 안전 이벤트를 텔레그램으로 보내는 파일.

흐름에서의 위치:
  1. service/safety/worker.py가 확정 이벤트를 DB에 저장한 뒤 send_telegram_alert()를 호출한다.
  2. 이 파일은 해당 camera_id가 포함된 모니터링 그룹의 알림 대상 chat_id/token을 조회한다.
  3. 이벤트 코드(E001~E004)에 맞는 메시지를 만들고, 증거 JPG/MP4가 있으면 파일과 함께 전송한다.

중요한 점:
  - 현재 텔레그램은 "감지 즉시"가 아니라 "DB 저장 성공 후" 발송된다.
  - 즉시 릴레이 반응은 service/safety/events.py의 trigger_relay() 경로를 본다.

다음에 볼 파일:
  - service/safety/worker.py: DB 저장 후 이 파일을 호출한다.
  - service/telegram/repository.py: 카메라가 속한 모니터링 그룹의 알림 대상자를 찾는다.
"""

from __future__ import annotations

import os

import requests

from sqlalchemy.orm import Session

from core.logging.logger import get_logger
from core.logging.helpers import log_event
from core.exception.custom_exception import NotFoundException
from service.telegram import repository
from service.telegram.schema import TelegramManagerSave, TelegramManagerDelete

logger = get_logger(__name__)

# 이벤트 코드 → 메시지 템플릿
_EVENT_MESSAGES: dict[str, str] = {
    "E001": "⚠️ [경고] \n (CCTV ID: {camera_id}) \n 안전모 미착용이 감지되었습니다!",
    "E002": "🚨 [경고] \n (CCTV ID: {camera_id}) \n 작업자가 위험구역에 진입했습니다!",
    "E003": "🚑 [긴급] \n (CCTV ID: {camera_id}) \n 작업자가 쓰러졌습니다!",
    "E004": "🚑 [위험] \n (CCTV ID: {camera_id}) \n 작업자와 장비 간의 충돌 위험이 감지되었습니다!",
}

_TELEGRAM_API = "https://api.telegram.org/bot{token}"


def send_telegram_alert(db: Session, camera_id: str, event_key: str, image_path: str | None = None) -> None:
    """이벤트 발생 시 텔레그램 알림을 전송한다."""
    targets = repository.fetch_chat_ids_for_camera(db, camera_id)
    if not targets:
        return

    message = _EVENT_MESSAGES.get(event_key, "📢 [알림] 이벤트가 감지되었습니다.")
    message = message.format(camera_id=camera_id)

    for chat_id, token in targets:
        if not token:
            continue
        _send_to_chat(token, chat_id, message, image_path)


def _send_to_chat(token: str, chat_id: str, message: str, image_path: str | None) -> None:
    """단일 chat_id로 텔레그램 메시지를 전송한다."""
    base_url = _TELEGRAM_API.format(token=token)

    try:
        if image_path and os.path.exists(image_path):
            _send_file(base_url, chat_id, message, image_path)
        else:
            _send_message(base_url, chat_id, message)
    except Exception:
        logger.exception("[Telegram] 전송 실패 chat_id=%s", chat_id)


def _send_message(base_url: str, chat_id: str, message: str) -> None:
    """텍스트 메시지를 전송한다."""
    url = f"{base_url}/sendMessage"
    resp = requests.post(url, data={"chat_id": chat_id, "text": message}, timeout=10)
    if resp.status_code == 200:
        log_event(logger=logger, level="INFO", event_type="telegram.send",
                  message="텔레그램 메시지 전송 성공", chat_id=chat_id)
    else:
        logger.warning("[Telegram] 메시지 전송 실패 chat_id=%s status=%s", chat_id, resp.status_code)


def _send_file(base_url: str, chat_id: str, message: str, file_path: str) -> None:
    """이미지 또는 영상 파일을 전송한다."""
    ext = os.path.splitext(file_path)[-1].lower()

    if ext in (".jpg", ".png", ".jpeg"):
        url = f"{base_url}/sendPhoto"
        file_key = "photo"
    elif ext in (".mp4", ".avi", ".mov"):
        url = f"{base_url}/sendVideo"
        file_key = "video"
    else:
        logger.warning("[Telegram] 지원되지 않는 파일 형식: %s", ext)
        return

    with open(file_path, "rb") as f:
        resp = requests.post(
            url,
            data={"chat_id": chat_id, "caption": message},
            files={file_key: f},
            timeout=30,
        )
    if resp.status_code == 200:
        log_event(logger=logger, level="INFO", event_type="telegram.send_file",
                  message="텔레그램 파일 전송 성공", chat_id=chat_id)
    else:
        logger.warning("[Telegram] 파일 전송 실패 chat_id=%s status=%s", chat_id, resp.status_code)


# ---------------------------------------------------------------------------
# 매니저 CRUD 서비스
# ---------------------------------------------------------------------------

def list_managers(db: Session) -> list[dict]:
    """모든 텔레그램 매니저 목록을 반환한다."""
    managers = repository.fetch_all(db)
    return [
        {
            "monitoring_grp_id": m.monitoring_grp_id,
            "chat_id": m.chat_id,
            "notification_on": m.notification_on,
            "token": m.token,
            "comp_id": m.comp_id,
            "created_by": m.created_by,
        }
        for m in managers
    ]


def save_managers(db: Session, items: list[TelegramManagerSave]) -> None:
    """매니저 목록을 일괄 저장한다 (신규 등록 + 수정 동시 처리)."""
    for item in items:
        is_new = not item.original_monitoring_grp_id and not item.original_chat_id
        if is_new:
            repository.insert(db, {
                "monitoring_grp_id": item.monitoring_grp_id,
                "chat_id": item.chat_id,
                "token": item.token,
                "notification_on": item.notification_on,
                "created_by": item.userCd,
            })
        else:
            repository.update_manager(
                db,
                original_monitoring_grp_id=item.original_monitoring_grp_id,
                original_chat_id=item.original_chat_id,
                data={
                    "monitoring_grp_id": item.monitoring_grp_id,
                    "chat_id": item.chat_id,
                    "token": item.token,
                    "notification_on": item.notification_on,
                    "updated_by": item.userCd,
                },
            )


def delete_managers(db: Session, items: list[TelegramManagerDelete]) -> None:
    """매니저 목록을 일괄 삭제한다."""
    for item in items:
        repository.delete(db, item.monitoring_grp_id, item.chat_id)


def run_alarm(db: Session, monitoring_grp_id: str, chat_id: str) -> None:
    """알림을 활성화한다."""
    success = repository.set_notification(db, monitoring_grp_id, chat_id, on=True)
    if not success:
        raise NotFoundException(msg=f"매니저를 찾을 수 없습니다. grp={monitoring_grp_id}, chat={chat_id}")


def stop_alarm(db: Session, monitoring_grp_id: str, chat_id: str) -> None:
    """알림을 비활성화한다."""
    success = repository.set_notification(db, monitoring_grp_id, chat_id, on=False)
    if not success:
        raise NotFoundException(msg=f"매니저를 찾을 수 없습니다. grp={monitoring_grp_id}, chat={chat_id}")


def run_all(db: Session) -> int:
    """모든 매니저의 알림을 일괄 활성화한다."""
    return repository.set_all_notification(db, on=True)
