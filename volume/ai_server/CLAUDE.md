# CLAUDE.md — 전진 AI 모니터링 백엔드

FastAPI 기반 건설현장 모니터링 시스템의 AI 백엔드. 안전관리(실시간 YOLO 추론)와 공정률(주기적 JIT 스냅샷) 두 도메인으로 구성된다.

---

## 기술 스택

| 계층 | 라이브러리 |
|------|-----------|
| 웹 프레임워크 | FastAPI (lifespan 방식) |
| 데이터 검증 | Pydantic v2, pydantic-settings |
| ORM / DB | SQLAlchemy (1sync) + psycopg2 |
| AI 추론 | ultralytics YOLO (YOLOv8) |
| 영상 처리 | OpenCV (`cv2`) |
| 동시 처리 | multiprocessing.Process + threading.Thread |
| 미디어 서버 | MediaMTX (RTSP 중계, WebRTC 변환) |
| 실시간 스트림 | FastAPI WebSocket |
| 비동기 | asyncio (메인 프로세스에서만) |
| 설정 관리 | pydantic-settings + `.env.{APP_ENV}` |
| 로깅 | Python logging (structlog 스타일 payload) |

> **서브프로세스에서는 SQLAlchemy 세션을 공유하지 않는다.** 서브프로세스의 추론 결과는 `mp.Queue`로 부모 프로세스에 전달하고, 부모 프로세스의 DB 워커 스레드가 SQLAlchemy로 저장한다.

---

## 프로젝트 구조

```
main/
├── main.py                          # FastAPI 앱 생성, lifespan, 라우터 등록
├── config/
│   ├── config.py                    # Settings (pydantic-settings), settings 싱글톤
│   ├── .env.dev                     # 개발 환경 실제 값 (git 제외)
│   └── .env.prod                    # 운영 환경 실제 값 (git 제외)
├── core/                            # 도메인 무관 공통 인프라
│   ├── ai/
│   │   ├── model_loader.py          # ModelType enum, YOLO 모델 로딩/캐싱 (threading.Lock)
│   │   ├── rtsp_reader.py           # rtsp_reader_process() — MediaMTX 경유 RTSP 수신 서브프로세스
│   │   ├── process_manager.py       # CameraProcessManager, CameraWorker, 워치독
│   │   ├── media_server.py          # MediaMTX REST API 연동 (path 등록/삭제, 스트림 URL)
│   │   └── ws_bridge.py             # WebSocketBridge — mp.Queue → asyncio.Queue 팬아웃
│   ├── database/
│   │   ├── session.py               # get_db(), SessionLocal, initialize_database()
│   │   └── transaction.py           # @transactional() 데코레이터
│   ├── exception/
│   │   ├── custom_exception.py      # BusinessException 계층
│   │   └── handlers.py              # 전역 예외 핸들러 등록
│   ├── logging/
│   │   ├── logger.py                # get_logger(), setup_logging()
│   │   └── helpers.py               # log_event() 구조화 로그 헬퍼
│   ├── requests/
│   │   └── http_client.py           # http_client (lifespan 관리)
│   ├── response/
│   │   └── response.py              # ResultResponse[T], response()
│   └── utils/
│       ├── formatters/
│       │   └── datetime.py          # format_datetime() → "%Y.%m.%d %H:%M:%S"
│       ├── messages/
│       │   └── catalog.py           # msg_key 카탈로그 (success.*, error.*)
│       └── pagination/
│           └── utils.py             # build_page_response(), calculate_offset()
└── service/                         # 비즈니스 도메인 로직
    ├── safety/                      # 안전관리 도메인
    │   ├── schema.py                # Pydantic DTO
    │   ├── repository.py            # DB 접근 (SQLAlchemy) — SQL만, commit 포함
    │   ├── service.py               # 비즈니스 로직
    │   ├── routes.py                # APIRouter, HTTP/WebSocket 진입점
    │   ├── processor.py             # AI 추론, 이벤트 판단, event_queue 전달
    │   ├── events.py                # 이벤트 판단 규칙, 이벤트 이미지 저장
    │   └── worker.py                # SafetyDBWorker — event_queue → repository
    ├── progress/                    # 공정률 도메인
    │   ├── schema.py
    │   ├── repository.py            # DB 접근 (SQLAlchemy) — SQL만, commit 없음
    │   ├── service.py
    │   ├── routes.py
    │   ├── processor.py             # 스냅샷 배치 추론, result_queue 전달
    │   ├── persistence.py           # @transactional() 트랜잭션 경계
    │   └── worker.py                # ProgressWorker — 추론 프로세스 + DB 저장 스레드
    ├── camera_group/                # 카메라 그룹 관리
    └── item/                        # 예제 도메인
```

### core vs service 원칙

| 위치 | 기준 | 예시 |
|------|------|------|
| `core/` | 도메인 무관 인프라 — 어떤 프로젝트에도 재사용 가능 | DB 세션, 예외, 로깅, 페이지네이션, RTSP 리더, 프로세스 매니저 |
| `service/` | 이 프로젝트의 비즈니스 로직 — safety/progress를 앎 | AI 후처리 알고리즘, 이벤트 판단, REST API |

### 각 파일 책임 원칙

| 파일 | 해야 할 것 | 하면 안 되는 것 |
|------|-----------|----------------|
| `routes.py` | HTTP 파라미터 수신, service 호출, response() 반환 | DB 직접 조회, 비즈니스 로직 |
| `service.py` | 비즈니스 로직, 예외 발생, log_event() | DB 직접 접근, HTTP 관련 코드 |
| `repository.py` | SQLAlchemy ORM 방식 DB 조회/저장 (`select()`, `update()`, `db.add()` 등). `text()` 사용 금지 | 비즈니스 판단, 예외 발생 |
| `persistence.py` | @transactional() 트랜잭션 경계, 여러 repository 조합 | SQL 직접 작성 |
| `processor.py` | AI 추론, output 생성, mp.Queue 전달 | DB 접근, HTTP 응답 |
| `worker.py` | mp.Queue 소비, persistence/repository 호출 | AI 추론, HTTP 응답 |
| `events.py` | 이벤트 판단 규칙, 이벤트 이미지 저장 | DB 접근, 추론 로직 |

---

## 코딩 컨벤션

### 공통 임포트 관행

모든 파일 최상단에 선언한다.

```python
from __future__ import annotations
```

### 응답 패턴

라우터에서 항상 `response()` 헬퍼와 `msg_key` 카탈로그를 사용한다. 직접 문자열 메시지를 `msg=`에 넣지 않는다.

```python
from core.response.response import ResultResponse, response

return response(data=result, msg_key="success.read")
return response(data=result, msg_key="success.create")
return response(data=result, msg_key="success.delete")
```

**사용 가능한 msg_key:**
- 성공: `success.default`, `success.read`, `success.create`, `success.update`, `success.delete`
- 오류: `error.bad_request`, `error.unauthorized`, `error.forbidden`, `error.not_found`, `error.conflict`, `error.validation`, `error.internal_server`

### SQLAlchemy 사용 규칙

**반드시 ORM 방식으로 작성한다.** `text()`를 사용한 raw SQL은 금지한다.

```python
# ✅ 올바른 예 — ORM 방식
from sqlalchemy import select, func
from service.cctv.model import Camera

stmt = select(Camera).where(Camera.comp_id == comp_id).order_by(Camera.camera_id)
rows = db.scalars(stmt).all()

# ✅ JOIN 예
from service.roi.model import CameraRoi
stmt = select(CameraRoi).join(Camera, CameraRoi.camera_id == Camera.camera_id).where(Camera.comp_id == comp_id)

# ✅ INSERT 예
camera = Camera(camera_id="cam_01", comp_id="JEONJIN", camera_nm="1번 카메라")
db.add(camera)
db.commit()

# ❌ 금지 — text() raw SQL
from sqlalchemy import text
db.execute(text("SELECT * FROM tb_camera WHERE comp_id = :comp_id"), {"comp_id": comp_id})
```

각 서비스 폴더에 `model.py`로 테이블 모델을 정의하고, 다른 서비스에서 JOIN이 필요하면 해당 모델을 import해서 사용한다.

### 예외 처리

`HTTPException`을 직접 사용하지 않는다. 항상 `BusinessException` 서브클래스를 사용한다.

```python
from core.exception.custom_exception import (
    NotFoundException, ConflictException, BadRequestException
)

raise NotFoundException(msg=f"리소스를 찾을 수 없습니다. id={resource_id}")
raise ConflictException(msg=f"이미 등록된 항목입니다. id={resource_id}")
```

### 로깅

```python
from core.logging.logger import get_logger
from core.logging.helpers import log_event

logger = get_logger(__name__)

log_event(
    logger=logger,
    level="INFO",
    event_type="camera.register",
    message="Camera AI process started",
    camera_id=camera_id,
)
```

### 날짜 포맷

DB에서 읽은 datetime 값은 반드시 `format_datetime()`으로 변환한다.

```python
from core.utils.formatters.datetime import format_datetime

"created_at": format_datetime(r[3]),  # → "2026.04.21 15:30:00"
```

### DB 접근 (메인 프로세스 / 워커 스레드)

`Depends(get_db)` 또는 `SessionLocal()`로 세션을 얻고, repository에서 `text()` raw SQL을 사용한다.

```python
from sqlalchemy import text
from sqlalchemy.orm import Session

def fetch_something(db: Session, camera_id: str) -> list[dict]:
    rows = db.execute(
        text("SELECT ... FROM ... WHERE camera_id = :id"),
        {"id": camera_id},
    ).fetchall()
    return [{"field": r[0]} for r in rows]
```

### 트랜잭션

단일 테이블 저장은 repository에서 `db.commit()` 처리.
여러 테이블을 원자적으로 저장할 때는 `persistence.py`에서 `@transactional()` 사용.

```python
from core.database.transaction import transactional

@transactional()
def save_result(db: Session, ...):
    repository.save_a(db, ...)
    repository.save_b(db, ...)
    # commit은 @transactional()이 자동 처리
```

### 페이지네이션

```python
from core.utils.pagination.schema import PageResponse
from core.utils.pagination.utils import build_page_response, calculate_offset

offset = calculate_offset(page, size)
total = repository.count_something(db)
items = [Schema(**r) for r in repository.fetch_something(db, offset=offset, size=size)]
return build_page_response(items=items, total=total, page=page, size=size)
```

---

## AI 파이프라인 아키텍처

### 안전관리 (실시간)

```
RTSP 카메라
    │
    ▼ (RTSP 연결 1개)
MediaMTX (미디어 서버)        # 스트림 중계, 자동 재연결, 프로토콜 변환
    │
    ├─► rtsp_reader_process()  # 서브프로세스: MediaMTX 경유 프레임 수신
    │       │
    │       ▼ frame_queue (mp.Queue)
    │   safety_process()       # 서브프로세스: YOLO/Pose 추론, 이벤트 판단
    │       │
    │       ├─► result_mp_queue  # → ws_bridge → asyncio.Queue → WebSocket 클라이언트
    │       │
    │       └─► event_queue      # → SafetyDBWorker (스레드)
    │                            #   → repository.save_event() → tb_camera_event_hist
    │
    └─► 프론트엔드 (WebRTC/HLS)  # 실시간 영상 시청
```

**roi_arr, detection_is_run, pose_is_run** — `register_camera` 시 DB 조회 후 프로세스 인자로 전달

**감지 이벤트:**
| 코드 | 설명 | 모델 |
|------|------|------|
| E001 | 안전모 미착용 | SAFETY (no_helmet) |
| E002 | ROI 구역 내 인원 감지 | SAFETY (person + ROI polygon) |
| E003 | 쓰러짐 감지 | POSE (collapse) |
| E004 | 위험물 접근 | SAFETY (person + forklift/hoist overlap) |

### 공정률 (JIT 스냅샷)

```
PROGRESS_CYCLE_SEC(60초) 주기
    │
    ▼
progress_process()          # 서브프로세스: 전체 카메라 스냅샷 → 배치 추론
    │
    └─► result_queue        # → ProgressWorker DB 저장 스레드
                            #   → persistence.save_progress_result() [@transactional]
                            #   → tb_twin_image + tb_twin_detection

```

**cameras, grid_map** — lifespan 시작 시 DB 조회 후 프로세스 인자로 전달 (1회)

### YOLO 모델 (3개)

| 설정 키 | 모델 | 클래스 |
|---------|------|--------|
| `MODEL_SAFETY_PT` | 안전관리 | person, helmet, no_helmet, forklift, hoist |
| `MODEL_POSE_PT` | 포즈 (쓰러짐) | collapse, standing |
| `MODEL_PROGRESS_PT` | 공정률 | 설비 부품 |

### 프로세스 관리

`CameraProcessManager`가 카메라당 두 개의 서브프로세스를 관리한다.
- **rtsp_reader**: RTSP 스트림 수신
- **ai_process**: AI 추론 (`ai_target` 함수로 교체 가능)
- **watchdog**: 10초 주기로 프로세스 생존 + heartbeat 확인, 비정상 시 재시작

---

## API 엔드포인트

### 안전관리 (`/safety`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| WS | `/safety/ws/{camera_id}` | 실시간 AI 추론 결과 스트림 |
| GET | `/safety/cameras` | 실행 중인 카메라 프로세스 목록 |
| POST | `/safety/cameras` | 카메라 등록 및 AI 프로세스 시작 (MediaMTX path 자동 등록) |
| DELETE | `/safety/cameras/{camera_id}` | 카메라 AI 프로세스 중지 (MediaMTX path 자동 삭제) |
| GET | `/safety/cameras/{camera_id}/stream` | 프론트 영상 시청 URL (WebRTC/RTSP) |
| GET | `/safety/events` | 이벤트 이력 조회 (`camera_id`, `page`, `size`) |

**WebSocket 메시지 형식:**
```json
{
  "camera_id": "cam_01",
  "timestamp": "20260421_153000",
  "detections": [{"class": "no_helmet", "bbox": [x1,y1,x2,y2], "conf": 0.91}],
  "events": [{"type": "E001", "message": "안전모 미착용 감지"}]
}
```

### 공정률 (`/progress`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/progress/cameras` | 공정률 대상 카메라 목록 |
| GET | `/progress/results` | 공정률 결과 이미지 이력 (`camera_id`, `page`, `size`) |
| GET | `/progress/results/latest/image` | 최신 결과 이미지 파일 반환 (FileResponse) |

---

## DB 테이블 참조

| 테이블 | 도메인 | 용도 |
|--------|--------|------|
| `tb_camera_info` | safety | 카메라 기본 정보 (camera_id, rtsp_url, camera_nm) |
| `tb_camera_config` | safety | 카메라별 AI 스위치 (detection_is_run, pose_is_run) |
| `tb_camera_roi` | safety | ROI 폴리곤 좌표 (E002 판정용) |
| `tb_camera_event_hist` | safety | 안전 이벤트 이력 |
| `tb_camera` | progress | 공정률 대상 카메라 (rtsp_url, jit_only, sort_direction) |
| `tb_camera_grid` | progress | 격자 좌표 (grid_data JSON) |
| `tb_twin_image` | progress | 공정률 결과 이미지 경로 |
| `tb_twin_detection` | progress | 격자별 감지 결과 (result_id, row, col, class, conf) |

---

## 환경 설정 주요 항목

```env
APP_ENV=dev
DB_HOST=192.168.3.139
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=!Gns@jeonjin

# MediaMTX (미디어 서버)
MEDIA_SERVER_HOST=192.168.3.42
MEDIA_SERVER_RTSP_PORT=8554
MEDIA_SERVER_WEBRTC_PORT=8889
MEDIA_SERVER_API_PORT=9997

MODEL_SAFETY_PT=./model/model1_0603.pt
MODEL_POSE_PT=./model/keypoint_best_241017.pt
MODEL_PROGRESS_PT=./model/progress_model.pt

AI_FRAME_SKIP=3
AI_CONFIDENCE_THRESHOLD=0.8
AI_EVENT_COOLDOWN_SEC=600
AI_COLLAPSE_SUSTAIN_SEC=15.0
PROGRESS_CYCLE_SEC=60
PROGRESS_RESULT_DIR=./progress_results
EVENT_IMAGE_DIR=./event_images
```
