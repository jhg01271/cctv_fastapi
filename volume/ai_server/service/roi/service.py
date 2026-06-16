"""
[Service (서비스) - ROI 도메인]
1. 도메인 기능: "안전 감시 구역 설정 (Region Of Interest)"
   - 사용자가 화면에 그린 위험 구역의 다각형 좌표 정보 및 감시 대상 AI 모델 설정(안전모 미착용 감지, 작업자 쓰러짐 등)을 관리합니다.
2. 아키텍처 역할: 핵심 비즈니스 로직과 알고리즘을 수행하는 두뇌 역할을 합니다.
3. 흐름: Router ➡️ Service ➡️ Repository
4. 설명: DB에서 조회한 원본 중괄호 데이터`{}`를 프론트엔드가 파싱하기 편한 대괄호`[]` 형태의 JSON 데이터로 변환하고,
      실시간 카메라 화면을 캡처해 base64로 디코딩하거나, 설정된 좌표값을 가공해 Repository를 통해 DB에 영속화하는 비즈니스 연산을 전담합니다.
"""



from __future__ import annotations

import cv2
from datetime import datetime
from pathlib import Path

from sqlalchemy.orm import Session

from sqlalchemy.exc import IntegrityError

from config.config import settings
from core.exception.custom_exception import BadRequestException, NotFoundException
from core.logging.logger import get_logger
from service.roi.model import CameraRoi
from service.roi.repository import fetch_rois_by_comp, upsert_roi

logger = get_logger(__name__)


def list_rois(db: Session, comp_id: str) -> list[dict]:
    """회사별 ROI 목록을 카메라 기준으로 그룹핑하여 반환한다."""
    rows = fetch_rois_by_comp(db, comp_id)

    grouped: dict[str, dict] = {}
    for row in rows:
        cid = row["camera_id"]
        if cid not in grouped:
            grouped[cid] = {
                "cctv_id": cid,
                "cctv_name": row.get("camera_nm", ""),
                "camera_nm": row.get("camera_nm", ""),
                "camera_desc": row.get("camera_desc", ""),
                "model_list": [],
            }
        
        # 중괄호 형식 ({})의 좌표 데이터를 유효한 JSON 배열 포맷 ([])으로 치환하여 프론트엔드 JSON.parse 크래시 방지
        point_str = row["point"] or "[]"
        point_str = point_str.replace("{", "[").replace("}", "]")
        
        grouped[cid]["model_list"].append({
            "model_nm": row["model_nm"],
            "point_arr": point_str,
            "is_run": row["is_run"],
        })

    return list(grouped.values())


def save_roi(db: Session, data: dict) -> list[dict]:
    """ROI를 등록하거나 수정한다. model_list 배열 형태를 지원한다."""
    now = datetime.now()
    camera_id = data.get("cctv_id") or data.get("camera_id")
    model_list = data.get("model_list", [])

    if not camera_id:
        raise BadRequestException(msg="cctv_id는 필수입니다.")

    if not model_list:
        raise BadRequestException(msg="model_list는 필수입니다.")

    results = []
    for model in model_list:
        model_nm = model.get("model_nm")
        if not model_nm:
            raise BadRequestException(msg="model_nm은 필수입니다.")
        user_cd = model.get("userCd", "system")

        existing = db.get(CameraRoi, (camera_id, model_nm))
        try:
            if existing:
                existing.point = model.get("point_arr", existing.point)
                existing.is_run = model.get("is_run", existing.is_run)
                existing.updated_at = now
                existing.updated_by = user_cd
                upsert_roi(db, existing)
            else:
                roi = CameraRoi(
                    camera_id=camera_id,
                    model_nm=model_nm,
                    point=model.get("point_arr", "{}"),
                    is_run=model.get("is_run", False),
                    created_at=now,
                    created_by=user_cd,
                )
                upsert_roi(db, roi)
        except IntegrityError:
            db.rollback()
            raise BadRequestException(
                msg=f"유효하지 않은 model_nm입니다. model_nm={model_nm}"
            )

        results.append({"cctv_id": camera_id, "model_nm": model_nm})

    return results


def capture_cctv_image(db: Session, camera_id: str) -> dict:
    """카메라 이미지를 반환한다.

    1) 로컬 캐시 디렉토리(service/grid/img/)에서 저장된 이미지가 있는지 우선 검사하여 즉시 반환합니다.
    2) 캐시 파일이 없는 경우에만 실시간 RTSP 캡처를 수행하고 캐시로 파일화하여 저장합니다.
    3) 실패 시 검은색 더미 이미지를 생성해 무한 로딩을 방지합니다.
    """
    import base64
    import math
    import os
    from pathlib import Path
    from PIL import Image
    import io
    import numpy as np
    from service.cctv.model import Camera

    # -------------------------------------------------------------------------
    # [DB 조회] SQLAlchemy 세션(db)을 사용하여 tb_camera 테이블(Camera 모델)에서
    # 특정 camera_id에 해당하는 카메라 레코드 정보를 직접 조회하는 지점입니다.
    # -------------------------------------------------------------------------
    camera = db.get(Camera, camera_id)
    if not camera:
        raise NotFoundException(msg=f"카메라를 찾을 수 없습니다. camera_id={camera_id}")

    img_base64 = None
    width = 1920
    height = 1080
    img_dir = Path(__file__).parent.parent / "grid" / "img"
    candidate = img_dir / f"{camera_id}.jpg"

    # 1) 로컬 파일 캐시 우선 확인 (우선순위 1)
    if candidate.exists():
        try:
            with open(candidate, "rb") as f:
                img_bytes = f.read()
                img_base64 = base64.b64encode(img_bytes).decode("utf-8")
            img = Image.open(candidate)
            width, height = img.size
            gcd = math.gcd(width, height)
            ratio = f"{width // gcd}:{height // gcd}"
            logger.info("Using cached image for camera_id=%s path=%s", camera_id, candidate)
            return {
                "img_decode_data": img_base64,
                "img_size": {"width": width, "height": height},
                "ratio": ratio,
            }
        except Exception as e:
            logger.error("Failed to read cached image for camera_id=%s, error=%s", camera_id, e)

    # FFMPEG 연결 및 읽기 타임아웃 설정 (2초 = 2,000,000us)
    # stimeout: 연결 타임아웃, rw_timeout: 데이터 전송 타임아웃
    os.environ["OPENCV_FFMPEG_CAPTURE_OPTIONS"] = "rtsp_transport;tcp|stimeout;2000000|rw_timeout;2000000"

    # 2) 로컬 캐시가 없는 경우 실시간 RTSP 캡처 시도 (우선순위 2)
    # -------------------------------------------------------------------------
    # [RTSP 주소 추출] DB에서 조회한 Camera 객체(camera)의 'rtsp_addr' 컬럼 값을 꺼내어
    # 카메라 기기에 실시간으로 비디오 프레임을 캡처할 RTSP URL 경로로 사용합니다.
    # -------------------------------------------------------------------------
    rtsp_url = camera.rtsp_addr if camera.rtsp_addr else f"{settings.MEDIA_SERVER_RTSP_URL}/{camera_id}"
    logger.info("Cache miss. Attempting RTSP capture for camera_id=%s URL=%s", camera_id, rtsp_url)
    cap = cv2.VideoCapture(rtsp_url)
    if cap.isOpened():
        ret, frame = cap.read()
        cap.release()
        if ret:
            height, width = frame.shape[:2]
            _, buffer = cv2.imencode(".jpg", frame)
            img_base64 = base64.b64encode(buffer).decode("utf-8")
            logger.info("CCTV image captured: camera_id=%s size=%dx%d", camera_id, width, height)
            
            # 다음 요청 시 무부하 조회를 위해 로컬 파일 캐시로 저장
            try:
                img_dir.mkdir(parents=True, exist_ok=True)
                with open(candidate, "wb") as f:
                    f.write(buffer)
                logger.info("Successfully cached captured image to: %s", candidate)
            except Exception as e:
                logger.error("Failed to write captured image to cache: %s", e)
        else:
            logger.warning("RTSP opened but failed to read frame: camera_id=%s", camera_id)
    else:
        logger.warning("RTSP capture failed to open: camera_id=%s URL=%s", camera_id, rtsp_url)
        cap.release()


    # 2) 실시간 캡처 실패 시 테스트 이미지로 폴백 (우선순위 2)
    if img_base64 is None:
        img_dir = Path(__file__).parent.parent / "grid" / "img"
        for ext in ("jpg", "png"):
            candidate = img_dir / f"{camera_id}.{ext}"
            if candidate.exists():
                with open(candidate, "rb") as f:
                    img_base64 = base64.b64encode(f.read()).decode("utf-8")
                img = Image.open(candidate)
                width, height = img.size
                logger.info("Test image used as fallback: camera_id=%s path=%s", camera_id, candidate)
                break

    # 3) 둘 다 실패 시 검은색 더미 이미지 제공 (무한 로딩 방지)
    if img_base64 is None:
        logger.warning("RTSP capture and test image failed. Generating dummy black image for camera_id=%s", camera_id)
        # 1920x1080 검은색 이미지 생성
        black_img = np.zeros((1080, 1920, 3), dtype=np.uint8)
        cv2.putText(black_img, "Camera Connection Failed (RTSP Timeout)", (300, 540),
                    cv2.FONT_HERSHEY_SIMPLEX, 1.5, (0, 0, 255), 3)
        _, buffer = cv2.imencode(".jpg", black_img)
        img_base64 = base64.b64encode(buffer).decode("utf-8")
        width, height = 1920, 1080

    gcd = math.gcd(width, height)
    ratio = f"{width // gcd}:{height // gcd}"

    return {
        "img_decode_data": img_base64,
        "img_size": {"width": width, "height": height},
        "ratio": ratio,
    }


def update_cctv_image_cache(db: Session, camera_id: str) -> bool:
    """지정된 카메라의 실시간 이미지를 강제로 캡처하여 로컬 캐시 디렉토리에 저장(갱신)한다."""
    import base64
    import os
    import cv2
    from pathlib import Path
    from service.cctv.model import Camera

    camera = db.get(Camera, camera_id)
    if not camera:
        logger.warning("[CacheUpdate] Camera not found: camera_id=%s", camera_id)
        return False

    img_dir = Path(__file__).parent.parent / "grid" / "img"
    candidate = img_dir / f"{camera_id}.jpg"

    os.environ["OPENCV_FFMPEG_CAPTURE_OPTIONS"] = "rtsp_transport;tcp|stimeout;2000000|rw_timeout;2000000"
    rtsp_url = camera.rtsp_addr if camera.rtsp_addr else f"{settings.MEDIA_SERVER_RTSP_URL}/{camera_id}"
    logger.info("[CacheUpdate] Fetching live frame for update: camera_id=%s URL=%s", camera_id, rtsp_url)
    
    cap = cv2.VideoCapture(rtsp_url)
    if cap.isOpened():
        ret, frame = cap.read()
        cap.release()
        if ret:
            try:
                img_dir.mkdir(parents=True, exist_ok=True)
                _, buffer = cv2.imencode(".jpg", frame)
                with open(candidate, "wb") as f:
                    f.write(buffer)
                logger.info("[CacheUpdate] Successfully updated image cache: %s", candidate)
                return True
            except Exception as e:
                logger.error("[CacheUpdate] Failed to write captured image to cache: %s", e)
        else:
            logger.warning("[CacheUpdate] Opened RTSP but failed to read frame: camera_id=%s", camera_id)
    else:
        logger.warning("[CacheUpdate] Failed to open RTSP: camera_id=%s", camera_id)
        cap.release()
    return False


def initialize_all_camera_captures_background(db_session_factory) -> None:
    """서버 최초 가동 시 모든 카메라의 캡처 이미지를 로컬 캐시 경로에 저장하는 백그라운드 배치 프로세스이다."""
    import time
    from service.cctv.model import Camera

    def run_sync():
        # uvicorn 기동 직후 DB 커넥션 획득 지연을 피하기 위해 5초 대기
        time.sleep(5.0)
        logger.info("[ImageCacheInit] Started background synchronization batch for all cameras...")
        db = db_session_factory()
        try:
            cameras = db.query(Camera).all()
            logger.info("[ImageCacheInit] Found %d cameras in database.", len(cameras))
            for cam in cameras:
                img_dir = Path(__file__).parent.parent / "grid" / "img"
                candidate = img_dir / f"{cam.camera_id}.jpg"
                
                # 이미 캐시 파일이 존재하는 경우 최초 초기화에서는 건너뜁니다. (중복 방지)
                if candidate.exists():
                    logger.info("[ImageCacheInit] Cache already exists for camera_id=%s, skipping initialization.", cam.camera_id)
                    continue

                logger.info("[ImageCacheInit] Initializing cache for camera_id=%s...", cam.camera_id)
                success = update_cctv_image_cache(db, cam.camera_id)
                if not success:
                    logger.warning("[ImageCacheInit] Failed to initialize cache for camera_id=%s (RTSP timeout/error)", cam.camera_id)
                
                # 카메라 기기와 네트워크의 과도한 순차 부하 방지를 위해 1초 대기
                time.sleep(1.0)
            logger.info("[ImageCacheInit] Background synchronization batch completed.")
        except Exception as e:
            logger.exception("[ImageCacheInit] Unexpected error during image cache initialization: %s", e)
        finally:
            db.close()

    import threading
    threading.Thread(
        target=run_sync,
        name="image-cache-initializer",
        daemon=True
    ).start()

