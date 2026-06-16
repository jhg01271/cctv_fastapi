# 🏗️ FastAPI CCTV 프로젝트 왕초보 가이드

> **대상**: Flask 기반 프로젝트(`flask/`)를 공부하다가 FastAPI 기반(`cctv_app/`)으로 전환해야 하는 왕초보
> **목표**: 전체 그림을 이해하고, 책임님이 내준 미션을 단계별로 해낼 수 있는 로드맵 제공

---

## 1. 🤔 왜 이렇게 헷갈리는 걸까? — 상황 정리

지금 일주의 상황을 정리하면 이렇습니다:

```
[Flask 시절]  flask/ 폴더 → 단일 Python 앱, 도커 컴포즈 없음
    ↓ (지영선임님: "Flask는 문제가 많았다")
[FastAPI 시절] cctv_app/ 폴더 → 여러 서비스가 Docker Compose로 묶임
```

**비유: 노점상 → 푸드코트**

| Flask 시절 (노점상) | FastAPI 시절 (푸드코트) |
|---|---|
| 주인 1명이 주문도 받고, 요리도 하고, 서빙도 함 | **매장마다 역할이 다름** — 프론트 카팅터, 주방, 창고, CCTV 모니터실이 따로 있음 |
| 가게 하나 열면 끝 | **푸드코트 관리자(Docker Compose)**가 모든 매장을 한번에 열고 닫음 |
| 문제가 생기면 가게 전체를 닫아야 함 | 주방만 리부팅해도 됨 — 나머지는 멀쩡 |

> [!NOTE]
> **"이 차이가 동기/비동기 때문이야?"** → 아닙니다! 이건 **아키텍처(구조) 차이**입니다.
> - **동기/비동기**는 "주문을 하나씩 처리하나 vs 여러 개 동시에 처리하나"의 차이 (Flask도 비동기 가능)
> - **노점상 vs 푸드코트**는 "서비스를 어떻게 나눠서 관리하나"의 차이 (Docker Compose로 분리 운영)
> - 즉, FastAPI로 바꾼 이유는 속도보다는 **유지보수성, 안정성, 독립 배포** 때문!

---

## 2. 🐳 Docker Compose가 뭔데? — 핵심 비유

### 비유: "푸드코트 관리 시스템"

`docker-compose.yml`은 **푸드코트의 설계도**입니다.

```
"내 푸드코트에는 이런 매장들이 있어":
  - nginx (정문 안내데스크) → 손님을 적절한 매장으로 안내
  - postgres (창고/금고) → 모든 데이터를 보관
  - frontend (전시관) → 웹 화면을 보여줌
  - backend (사무실) → Java Spring 기반 관리 시스템
  - mediamtx (CCTV 중계소) → 카메라 영상을 모아서 뿌려줌
  - stream_gateway (CCTV 접수처) → "이 카메라 켜줘/꺼줘" 처리
  - ai_server (AI 감시실) ← ★ 이게 일주가 담당하는 곳! ★
```

`docker compose up` 하면 → 푸드코트 전체가 한번에 오픈!
`docker compose down` 하면 → 전체 폐점!
`docker compose restart ai_server` → AI 감시실만 리부팅!

### 비유 그림으로 보는 전체 흐름

```
┌─────────────────── 푸드코트 (Docker Compose) ───────────────────┐
│                                                                   │
│  [정문: nginx]                                                    │
│       ↓                                                           │
│  [전시관: frontend] ←──── 손님이 웹에서 보는 화면                  │
│       ↓ (API 호출)                                                │
│  [사무실: backend] ←──── Java Spring, DB 읽기/쓰기               │
│       ↓                                                           │
│  [창고: postgres] ←──── 모든 데이터 저장 (테이블들)               │
│                                                                   │
│  ─── CCTV 라인 (일주 담당 영역) ────────────────────────────     │
│                                                                   │
│  [실제 CCTV] ──RTSP──→ [CCTV 중계소: mediamtx]                   │
│                              ↓                                    │
│                    [CCTV 접수처: stream_gateway]                   │
│                              ↓                                    │
│                    [★ AI 감시실: ai_server ★]                     │
│                         │         │                               │
│                    AI 추론    이벤트 발생!                         │
│                         │         │                               │
│                    WebSocket  DB에 이벤트 저장                    │
│                    (실시간)    + 텔레그램 알림                     │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

---

## 3. 🗂️ 프로젝트 폴더 구조 이해하기

### cctv_app/ 전체 구조

```
cctv_app/
├── .env                            ← 포트 설정 (다른 프로젝트와 충돌 방지)
├── docker-compose.yml              ← ★ 푸드코트 설계도 (운영용)
├── docker-compose.override.yml     ← ★ 개발용 설정 오버라이드 (GPU 끔 등)
├── backend.Dockerfile              ← Java 사무실 빌드 레시피
│
└── volume/                         ← ★★★ 실제 코드가 여기 있음! ★★★
    ├── ai_server/                  ← 일주 담당! FastAPI AI 서버
    │   ├── main.py                 ← 앱 진입점 (레스토랑 매니저)
    │   ├── config/                 ← 환경설정 (.env.dev, .env.prod)
    │   ├── core/                   ← 공통 인프라 (도구함)
    │   │   ├── ai/                 ← YOLO 모델, RTSP 리더, 프로세스 관리
    │   │   ├── database/           ← DB 연결, 세션 관리
    │   │   └── ...                 ← 로깅, 예외처리, HTTP 클라이언트 등
    │   ├── service/                ← ★ 비즈니스 로직 (일주가 주로 건드릴 곳)
    │   │   ├── safety/             ← 안전관리 (헬멧, 쓰러짐, 위험접근)
    │   │   ├── roi/                ← ROI 설정 (감시 구역)
    │   │   ├── event/              ← 이벤트 이력
    │   │   ├── cctv/               ← 카메라 기본 정보
    │   │   ├── progress/           ← 공정률
    │   │   └── ...
    │   ├── model/                  ← YOLO .pt 모델 파일들
    │   └── Dockerfile              ← AI 서버 빌드 레시피
    │
    ├── stream_gateway/             ← CCTV 접수처 (카메라 ON/OFF)
    ├── frontend/                   ← Vue.js 프론트엔드 빌드 결과
    ├── backend/                    ← Java Spring .jar 파일
    ├── nginx/                      ← 웹서버 설정
    └── postgres/                   ← DB 초기화 스크립트
```

### 비유: ai_server 내부 = "AI 감시실의 부서 구조"

```
AI 감시실 (ai_server/)
├── 매니저 (main.py)          → 직원들 출근시키고, 퇴근시킴 (lifespan)
├── 설정 파일함 (config/)     → "DB 주소가 뭐지?", "모델 파일은 어디?"
├── 도구함 (core/)            → 누구나 쓰는 공용 도구
│   ├── AI 장비 (core/ai/)    → YOLO 모델, 영상 읽기, 프로세스 관리
│   └── DB 열쇠 (core/database/) → DB 접속 도구
└── 부서들 (service/)         → 각 업무 담당
    ├── 안전부서 (safety/)    → "헬멧 안 썼어!", "사람 쓰러졌어!"
    ├── ROI부서 (roi/)        → "여기가 위험 구역이야" 표시
    ├── 이벤트부서 (event/)   → "언제 무슨 일이 있었는지" 기록
    └── CCTV부서 (cctv/)      → 카메라 등록/관리
```

---

## 4. 🔄 Flask vs FastAPI — 뭐가 달라졌나?

### 비유: "동네 식당 → 체인 레스토랑"

| 항목 | Flask (동네 식당) | FastAPI (체인 레스토랑) |
|---|---|---|
| **속도** | 주문 1개씩 처리 (동기) | 여러 주문 동시 처리 (비동기/async) |
| **메뉴판** | 종이에 손글씨 (문서 없음) | 자동 생성 메뉴판 — `/docs` 접속하면 API 테스트 가능! |
| **주문서 검증** | 주인이 눈으로 확인 | Pydantic이 자동 검증 ("가격이 숫자가 아니잖아!") |
| **운영** | 주인 혼자 운영 | Docker Compose로 체계적 운영 |
| **모니터링** | 없음 | 구조화 로깅, 헬스체크 |

### 코드 비교 예시

```python
# Flask (옛날 방식)
@app.route('/cameras', methods=['GET'])
def get_cameras():
    result = db.execute("SELECT * FROM tb_camera")  # raw SQL 😱
    return jsonify(result)

# FastAPI (새 방식)
@router.get("/cameras")
def get_cameras(db: Session = Depends(get_db)):
    stmt = select(Camera).order_by(Camera.camera_id)  # ORM 방식 ✅
    cameras = db.scalars(stmt).all()
    return response(data=cameras, msg_key="success.read")
```

### 🤔 "SQL문은 어디에 있어? ORM이 뭔데?"

> ORM = **"파이썬 코드로 SQL을 대신 쓰는 것"**

**비유: 통역사**

```
[너 (Python)] → "Camera 테이블에서 comp_id가 'JEONJIN'인 것 조회해줘"
       ↓
[통역사 (SQLAlchemy ORM)] → SQL로 번역!
       ↓
[DB (PostgreSQL)] ← "SELECT * FROM tb_camera WHERE comp_id = 'JEONJIN'"
```

즉, **SQL문을 따로 파일에 저장하는 게 아니라**, 파이썬 코드가 실행될 때 SQLAlchemy가 **자동으로 SQL을 생성**합니다!

**실제 코드로 보기** — [cctv/repository.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/cctv/repository.py):

```python
# repository.py — "SQL 대신 파이썬으로 DB 조회"

# 이 파이썬 코드가:
def fetch_cameras_by_comp(db: Session, comp_id: str):
    stmt = select(Camera).where(Camera.comp_id == comp_id).order_by(Camera.camera_id)
    return list(db.scalars(stmt).all())

# SQLAlchemy가 이 SQL로 자동 번역:
# SELECT * FROM tb_camera WHERE comp_id = 'JEONJIN' ORDER BY camera_id
```

**Camera는 어디서 온 거야?** → [cctv/model.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/cctv/model.py)에 정의됨:

```python
# model.py — "DB 테이블을 파이썬 클래스로 정의"
class Camera(Base):
    __tablename__ = "tb_camera"          # ← 이 클래스 = tb_camera 테이블!
    camera_id = mapped_column(String(36), primary_key=True)  # ← 컬럼 정의
    comp_id = mapped_column(String(10), nullable=False)
    camera_nm = mapped_column(String(50), nullable=False)
    # ... 나머지 컬럼들
```

**정리: 코드 흐름**
```
model.py   = "이 테이블은 이런 컬럼을 가지고 있어" (테이블 설계도)
    ↓
repository.py = "이 테이블에서 이런 데이터를 가져와" (조회/저장 함수 모음)
    ↓
service.py = "가져온 데이터로 이런 비즈니스 로직을 처리해" (판단/가공)
    ↓
routes.py  = "이 URL로 요청이 오면 service를 호출해" (API 진입점)
```

---

## 5. 📡 AI 파이프라인 — "CCTV 영상이 이벤트가 되기까지"

### 비유: "택배 분류 센터"

```
1️⃣ 카메라가 영상을 보냄 (택배차 도착)
          ↓
2️⃣ MediaMTX가 영상을 중계 (물류 허브)
          ↓
3️⃣ rtsp_reader가 프레임을 읽음 (택배 내리기)
          ↓ frame_queue (컨베이어 벨트)
4️⃣ safety_process가 YOLO로 분석 (X-ray 검사기)
    "이 프레임에 헬멧 없는 사람이 있다!"
          ↓
    ┌─────┴─────┐
    ↓           ↓
5a️⃣ 실시간 화면    5b️⃣ DB에 이벤트 저장
   (WebSocket)       + 텔레그램 알림
   → 웹 대시보드      → 현장소장 폰에 알림
```

### 🗺️ 위 그림의 각 단계 = 실제 코드 파일 매핑

> 아래 표를 보면서 각 파일을 열어보면 **"아 이 코드가 저 단계를 하는 거구나!"** 라고 이해할 수 있습니다.
> 소스 파일에 `# 📘 [가이드]` 주석을 달아놓았으니 파일을 열면 바로 찾을 수 있어요!

| 단계 | 역할 | 실제 파일 | 핵심 함수 |
|---|---|---|---|
| 🔧 전체 관리 | 카메라 프로세스 시작/중지/감시 | [process_manager.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/core/ai/process_manager.py) | `CameraProcessManager.start_camera()` |
| 3️⃣ 프레임 읽기 | RTSP 스트림에서 프레임 추출 | [rtsp_reader.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/core/ai/rtsp_reader.py) | `rtsp_reader_process()` |
| 4️⃣ AI 추론 | YOLO 모델로 객체 감지 + 이벤트 판단 | [safety/processor.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/processor.py) | `safety_process()` |
| 4️⃣ 이벤트 규칙 | "이 상황이 이벤트인가?" 판단 | [safety/events.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/events.py) | `check_events()` |
| 4️⃣ 모델 로딩 | YOLO .pt 파일 로드 | [model_loader.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/core/ai/model_loader.py) | `load_model()` |
| 5a️⃣ 실시간 전송 | AI 결과 → WebSocket → 프론트 | [ws_bridge.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/core/ai/ws_bridge.py) | `WebSocketBridge` |
| 5b️⃣ DB 저장 | 이벤트를 DB에 기록 + 텔레그램 발송 | [safety/worker.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/worker.py) | `SafetyDBWorker` |
| 5b️⃣ DB 저장 | WebSocket 라우터 + 이벤트 조회 API | [safety/routes.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/routes.py) | `websocket_endpoint()` |

**추천 학습 순서**: `process_manager.py` → `rtsp_reader.py` → `processor.py` → `events.py` → `worker.py` → `ws_bridge.py`

### 이벤트 종류 (책임님이 말한 것들!)

| 코드 | 이벤트 | 저장 방식 | 저장 코드 위치 | 비유 |
|---|---|---|---|---|
| **E001** | 안전모 미착용 | 📸 **캡처** 1장 | [events.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/events.py) → `save_event_image()` | "사진 찍어서 증거 남겨" |
| **E002** | 위험구역 침입 | 📸 **캡처** 1장 | 동일 | "출입금지 구역에 들어갔다!" |
| **E003** | 쓰러짐 감지 | 🎬 **영상 30초** (앞뒤) | [events.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/events.py) → 프레임 버퍼 | "응급상황! 영상 증거 필수!" |
| **E004** | 위험물 접근 | 📸 **캡처** 1장 | 동일 | "지게차 근처에 사람이!" |

> [!IMPORTANT]
> 책임님 요구사항: **쓰러짐(E003)은 앞뒤 30초 영상**, **나머지는 캡처본만** 저장!

---

## 6. 🗄️ 데이터베이스 — "어떤 정보가 어디에 저장되나?"

### 비유: "회사 서류철 캐비닛"

```
📁 tb_camera          = "카메라 명부"       → camera ID, 이름, RTSP 주소
📁 tb_camera_roi      = "위험구역 지도"     → ROI 좌표, AI 모델 ON/OFF
📁 tb_camera_event_hist = "사건 일지"       → 언제, 어디서, 무슨 이벤트
📁 tb_camera_grid     = "공정률 격자판"     → 격자 좌표 데이터
📁 tb_camera_safety_grid = "안전 격자판"    → 안전 격자 좌표
📁 tb_telegram_managers = "알림 수신자 명부" → 누가 알림 받을지
```

### ROI 좌표 & 키값 — 개발자 선임님께 전달할 정보

> [!TIP]
> 책임님이 "ROI 좌표랑 어떤 키값으로 할 것인지 정리해서 전달하라"고 하셨습니다.

**테이블: `tb_camera_roi`**

| 컬럼 | 설명 | 예시 |
|---|---|---|
| `roi_id` (PK) | ROI 고유 식별자 | 자동 생성 |
| `camera_id` (FK) | 어떤 카메라의 ROI인지 | "cam_01" |
| `comp_id` | 회사 코드 | "JEONJIN" |
| `model_nm` | 어떤 AI 모델용 ROI인지 | "Detection" / "Pose" |
| `is_run` | 이 모델 켤지/끌지 | true / false |
| `roi_data` | ROI 폴리곤 좌표 (JSON) | `[[100,200],[300,200],[300,400],[100,400]]` |

---

## 7. ⚠️ 알람 시스템 — "2가지 경우를 모두 상정"

### 비유: "화재 알람 방식"

책임님이 말씀하신 2가지 케이스:

```
케이스 1: 🔥 "불 나자마자 바로 알림!" (모델 단에서 알람)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
YOLO가 "헬멧 미착용!" 감지
    → 즉시 WebSocket으로 프론트에 전송
    → 동시에 텔레그램 알림 발송
    ✅ 장점: 초고속 (1초 이내)
    ⚠️ 단점: DB에 아직 기록 안 됨

케이스 2: 📝 "사건 보고서 쓴 다음 알림!" (DB 저장 후 알람)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
YOLO가 "헬멧 미착용!" 감지
    → event_queue에 이벤트 넣음
    → SafetyDBWorker가 DB에 저장
    → DB 저장 완료 후 알림 발송
    ✅ 장점: 신뢰성 (DB에 기록 보장)
    ⚠️ 단점: 약간 느림 (수초 지연)
```

**현재 cctv_app의 구현:**
- 실제로 **둘 다** 구현되어 있습니다!
- [processor.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/processor.py) → 모델 추론 후 `result_mp_queue`로 **즉시** WebSocket 전송
- [worker.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/safety/worker.py) → `event_queue`에서 꺼내서 **DB 저장 후** 텔레그램 알림

---

## 8. 🎯 책임님 미션 — 단계별 실습 로드맵

> 책임님이 내주신 미션을 하나씩 쪼개면 이렇습니다.
> 학습은 **2단계**로 나눕니다: **먼저 가볍게 pytest로 코드 이해** → **그다음 Docker로 실제 통합 테스트**

### 🤔 "모델도 영상도 없이 어떻게 테스트해?"

좋은 질문! `tests/`의 pytest는 **전체 시스템 테스트가 아니라, API(웹 요청/응답) 레벨만 테스트**합니다.

```
비유: 자동차 공장의 검사 라인

[pytest가 테스트하는 것]              [pytest가 테스트 안 하는 것]
━━━━━━━━━━━━━━━━━━━━━                ━━━━━━━━━━━━━━━━━━━━━━━━
    "카메라 등록 API 호출하면         ❌ "YOLO가 헬멧을 정확히 감지하나?"
   DB에 잘 저장되나?"               ❌ "RTSP 영상이 잘 들어오나?"
    "ROI 좌표 저장하면                ❌ "실시간 WebSocket이 잘 동작하나?"
   올바른 형식으로 들어가나?"
    "이벤트 조회 시 날짜 필터가       → 이건 STEP B (Docker 통합 테스트)에서!
   제대로 작동하나?"
```

**즉, STEP A (pytest)** = API 동작 이해 + 코드 구조 파악 (가볍고 빠름)
**STEP B (Docker)** = 실제 영상 + 모델 + DB 적재 전체 통합 테스트 (무겁지만 완전함)

**둘 다 해야** 책임님 미션이 완성됩니다!

```
🎓 학습 흐름 전체 그림

 STEP A: pytest로 가볍게 (YOLO 모델, 영상 필요 없음!)
 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  "시험지로 각 API가 어떻게 동작하는지 파악"
  → DB만 있으면 됨, Docker 전체를 띄울 필요 없음
  → 코드를 읽고 → 테스트 돌려보고 → "아 이렇게 동작하는구나" 이해

         ↓ 코드 구조를 이해했으면

 STEP B: Docker Compose로 전체 통합 테스트
 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  "실제 푸드코트를 오픈해서 영상 연결 + AI 모델 가동"
  → RTSP 영상 연결, ROI 설정, 이벤트 발생 → DB 적재 확인
```

---

### 🅰️ STEP A: pytest로 가볍게 코드 이해하기

> **비유**: 자동차를 사기 전에 **설명서 읽고, 시뮬레이터로 연습**하는 단계
> YOLO 모델도, CCTV 영상도, Docker도 필요 없습니다!

#### Phase A-0: 환경 준비

> [!NOTE]
> **"이미 로컬에 가상환경이 있는데 왜 또 만들어?"**
> → 프로젝트마다 **필요한 라이브러리 버전이 다르기 때문**입니다!
> - 비유: 요리사가 한식/양식/중식 도구를 **한 서랍에 다 넣으면** 뒤죽박죽 → 서랍을 나눠야 함
> - 이 프로젝트 전용 가상환경 = "성동조선 전용 도구 서랍"
> - `requirements.txt`에 적힌 **정확한 버전**만 설치 → 나중에 Docker에 올릴 때도 같은 환경 보장!
>
> **"uv로 만들어도 되나?"** → 네, 전혀 상관없습니다! `uv`가 `venv`보다 빠르니까 더 좋아요.

-  **Python 가상환경 만들기** (ai_server의 의존성 설치)
  ```powershell
  # 방법 1: venv (기본)
  cd C:\iljoowork\seongdong\cctv_app\volume
  python -m venv .venv
  .\.venv\Scripts\Activate.ps1
  pip install -r ai_server\requirements.txt
  pip install pytest

  # 방법 2: uv (더 빠름, 이걸 추천!)
  cd C:\iljoowork\seongdong\cctv_app\volume
  uv venv .venv
  .\.venv\Scripts\Activate.ps1
  uv pip install -r ai_server\requirements.txt
  uv pip install pytest
  ```
-  **DB만 띄우기** (Docker Desktop → postgres 컨테이너 1개만)
  ```powershell
  cd C:\iljoowork\seongdong\cctv_app
  docker compose up postgres -d
  ```
-  **DBeaver로 DB 접속 확인**
  - Host: `localhost`, Port: `5434`
  - User: `postgres`, Password: `!Gns@jeonjin`
  - Database: `postgres`

#### Phase A-1: 첫 번째 테스트 돌려보기 — "카메라 CRUD"

```
목표: "카메라 등록/조회/삭제 API가 어떻게 동작하는지" 이해
비유: "식당 메뉴판에 메뉴 추가/확인/삭제해보기"
```

-  테스트 코드 먼저 읽기: [test_cctv.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_cctv.py)
  ```python
  # 이 코드가 하는 일을 이해해보세요:
  def test_성공_카메라_목록_조회(self, client):
      resp = client.get("/cctv/cctv_crud/cctvs/JEONJIN")  # API 호출!
      assert resp.status_code == 200                        # 성공했나?
      data = resp.json()
      assert data["success"] is True                        # 응답이 올바른가?
      assert isinstance(data["list"], list)                 # 목록인가?
  ```
-  그 다음 실제 서비스 코드 따라가기:
  - [cctv/routes.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/cctv/routes.py) → API 진입점
  - [cctv/service.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/cctv/service.py) → 비즈니스 로직
  - [cctv/repository.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/cctv/repository.py) → DB 접근
-  테스트 실행해보기:
  ```powershell
  cd C:\iljoowork\seongdong\cctv_app\volume
  $env:PYTHONPATH="ai_server"; python -m pytest tests/test_cctv.py -v --tb=short
  ```

> [!TIP]
> **학습 패턴**: `test_*.py 읽기` → `routes.py 따라가기` → `service.py 이해` → `repository.py 확인` → `pytest 실행`
> 이 순서를 **모든 서비스에 반복**하면 전체 코드를 자연스럽게 이해하게 됩니다!

#### Phase A-2: ROI 테스트 — "위험구역 API 이해"

```
목표: ROI 좌표가 어떤 형식으로 저장되는지 파악
비유: "지도에 위험구역을 그리는 API 동작 이해"
```

-  [test_roi.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_roi.py) 읽기 → ROI 저장 요청의 JSON 형식 파악
  ```python
  # ROI 저장 시 이런 형태의 데이터가 필요하구나!
  {
      "cctv_id": "CAM0001",
      "model_list": [{
          "model_nm": "safety",          # 어떤 AI 모델용인지
          "point_arr": "{}",             # ROI 좌표 (폴리곤)
          "is_run": False,               # 모델 켤지/끌지
          "userCd": "test",
      }]
  }
  ```
-  [roi/routes.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/roi/routes.py) → [roi/service.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/roi/service.py) → [roi/repository.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/roi/repository.py) 순서로 따라가기
-  테스트 실행:
  ```powershell
  $env:PYTHONPATH="ai_server"; python -m pytest tests/test_roi.py -v --tb=short
  ```

#### Phase A-3: 이벤트 테스트 — "사건 일지 API 이해"

```
목표: 이벤트가 DB에 어떤 형태로 저장되고 조회되는지 파악
비유: "사건 일지를 검색하는 방법 이해"
```

-  [test_event.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_event.py) 읽기
  ```python
  # 이벤트 조회 시 이런 필터가 가능하구나!
  # - camera_id로 특정 카메라 이벤트만
  # - start_date/end_date로 기간 필터
  # - event_type으로 E001, E002 등 필터
  ```
-  [event/routes.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/event/routes.py) → [event/model.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/service/event/model.py) 순서로 확인
-  테스트 실행:
  ```powershell
  $env:PYTHONPATH="ai_server"; python -m pytest tests/test_event.py -v --tb=short
  ```

#### Phase A-4: 나머지 테스트 전체 실행

-  전체 테스트 한번에 돌리기:
  ```powershell
  $env:PYTHONPATH="ai_server"; python -m pytest tests/ -v --tb=short
  ```
-  결과를 보면서 **어떤 테스트가 성공/실패했는지** 확인
-  실패한 테스트가 있다면 → **왜 실패했는지** 분석 (DB에 데이터가 없어서? FK 제약?)

> [!IMPORTANT]
> **STEP A를 끝내면 이런 것들을 알게 됩니다:**
> - 각 API의 URL 경로와 요청/응답 형식
> - `routes → service → repository` 코드 흐름
> - DB 테이블 구조와 관계
> - ROI 좌표 키값 형식 (→ 개발자 선임님께 정리해서 전달 가능!)

---

### 🅱️ STEP B: Docker Compose로 전체 통합 테스트

> **비유**: 이제 설명서를 다 읽었으니 **실제로 자동차를 도로에서 운전**하는 단계
> CCTV 영상 + AI 모델 + 전체 서비스를 한번에 가동!

> [!WARNING]
> 지영선임님 조언: "기존 `flask/`에서 개발자선임님이 UI 등을 바꾼 게 있을 수 있으니 YAML 파일 동기화가 필요하다"
> → **먼저 flask/ 쪽의 최신 변경사항을 확인**하고, `cctv_app/`의 docker-compose.yml과 비교해야 합니다.

#### Phase B-0: Docker 전체 서비스 띄우기

-  **Docker Desktop 실행 확인**
-  전체 서비스 올리기:
  ```powershell
  cd C:\iljoowork\seongdong\cctv_app
  docker compose up -d
  ```
-  상태 확인:
  ```powershell
  docker compose ps
  ```
-  `http://localhost:8082`로 nginx 접속 테스트
-  `http://localhost:8000/docs`로 AI 서버 API 문서 확인 (자동 생성!)

#### Phase B-1: 테스트 영상 연결 (CCTV 연결하기)

```
목표: 테스트용 영상을 MediaMTX에 연결
비유: "가짜 CCTV를 중계소에 연결하기"
```

-  ffmpeg으로 로컬 MP4를 RTSP로 변환하여 MediaMTX에 전송
  ```powershell
  ffmpeg -re -stream_loop -1 -i test_video.mp4 -c copy -f rtsp rtsp://localhost:8554/test_cam
  ```
-  또는 stream_gateway API로 스트림 시작
  ```powershell
  curl -X POST http://localhost:8010/streams/test_cam/start `
    -H "Content-Type: application/json" `
    -d '{"rtsp_url": "rtsp://카메라주소"}'
  ```

#### Phase B-2: 카메라 등록 + 웹에서 영상 확인

-  카메라 등록 API 호출 (STEP A에서 배운 API!)
  ```powershell
  curl -X POST http://localhost:8000/safety/cameras `
    -H "Content-Type: application/json" `
    -d '{"camera_id": "test_cam", "rtsp_url": "rtsp://mediamtx:8554/test_cam"}'
  ```
-  `http://localhost:8082` 프론트엔드에서 영상 나오는지 확인

#### Phase B-3: ROI 설정 + AI 모델 실행

-  프론트엔드에서 ROI(감시 영역) 그리기
-  ROI 저장 (STEP A에서 배운 JSON 형식!)
-  AI 서버 로그에서 모델 추론 확인:
  ```powershell
  docker compose logs -f ai_server
  ```

#### Phase B-4: 이벤트 발생 → DB 적재 확인 (🎯 최종 목표!)

```
목표: YOLO가 이벤트를 감지하면 DB에 기록되는지 확인
비유: "감시 카메라가 사건을 포착하면 사건 일지에 기록되는지 확인"
```

-  AI 서버 로그에서 이벤트 감지 확인
-  DBeaver에서 `tb_camera_event_hist` 테이블 조회
-  이벤트 조회 API 호출 (STEP A에서 배운 API!):
  ```powershell
  curl http://localhost:8000/cctv/ce/camera_events1 `
    -X POST -H "Content-Type: application/json" `
    -d '{"camera_id": "test_cam"}'
  ```

---

## 9. 🧪 tests/ 폴더 — "시험지 세트 사용법"

### 비유: "기능별 시험지"

`volume/tests/` 폴더에는 지영선임님이 만들어 놓은 **자동 테스트 코드**가 있습니다.
AI 모델, MediaMTX, 영상 없이 **API 동작만 빠르게 검증**할 수 있는 도구예요!

| 시험지 | 검증 대상 | 학습 포인트 |
|---|---|---|
| [test_cctv.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_cctv.py) | 카메라 CRUD | API URL 구조, JSON 요청/응답 형식 |
| [test_roi.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_roi.py) | ROI 설정 | **ROI 좌표 형식** ← 개발자 선임님 전달용! |
| [test_event.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_event.py) | 이벤트 이력 | 필터 조건 (날짜, 이벤트 타입) |
| [test_grid.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_grid.py) | 격자 설정 | 공정률/안전 격자 API |
| [test_telegram.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_telegram.py) | 텔레그램 알림 | 매니저 등록, 알림 ON/OFF |
| [test_safety_ws.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_safety_ws.py) | WebSocket | 실시간 연결 (브릿지 없으면 skip) |
| [test_server.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_server.py) | 서버 상태 | 서버 정보 조회 |
| [test_profile.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_profile.py) | 프로필 | 프로필 CRUD |
| [test_save.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_save.py) | 이미지 저장 | 이벤트 이미지 저장 경로 |
| [test_progress.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_progress.py) | 공정률 | 공정률 결과 조회 |
| [test_remote.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/test_remote.py) | 원격 제어 | 카메라 시작/중지 (skip 처리됨) |

### 핵심: [conftest.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/conftest.py)가 하는 일

```python
# conftest.py 핵심 = "시험 환경 세팅"
# 1. 테스트용 DB에 연결 (localhost:5434)
# 2. AI 모델/MediaMTX/워커 없이 가벼운 앱 생성
# 3. 모든 test_*.py가 이 환경을 공유

# 비유: "모의고사 교실" — 실제 공장 가동 없이 API만 테스트
```

### 테스트 실행 명령어 모음

```powershell
cd C:\iljoowork\seongdong\cctv_app\volume

#  전체 시험 (모든 test_*.py)
$env:PYTHONPATH="ai_server"; python -m pytest tests/ -v --tb=short

#  카메라 시험만
$env:PYTHONPATH="ai_server"; python -m pytest tests/test_cctv.py -v --tb=short

#  특정 시험 문제 하나만
$env:PYTHONPATH="ai_server"; python -m pytest tests/test_cctv.py::TestGetCameras::test_성공_카메라_목록_조회 -v --tb=long

#  첫 실패에서 바로 멈추기
$env:PYTHONPATH="ai_server"; python -m pytest tests/ -v --tb=short -x
```

---

## 10. 🔑 핵심 파일 — "이 파일만 이해하면 70%는 끝"

| 우선순위 | 파일 | 역할 | 비유 |
|---|---|---|---|
| ⭐⭐⭐ | [main.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/main.py) | 앱 시작/종료, 모든 서비스 등록 | "매니저가 출근하면 하는 일" |
| ⭐⭐⭐ | [config.py](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/config/config.py) | 모든 설정값 정의 | "회사 설정 매뉴얼" |
| ⭐⭐⭐ | [conftest.py](file:///c:/iljoowork/seongdong/cctv_app/volume/tests/conftest.py) | 테스트 환경 설정 | "모의고사 교실 세팅" |
| ⭐⭐⭐ | [docker-compose.yml](file:///c:/iljoowork/seongdong/cctv_app/docker-compose.yml) | 전체 서비스 구성 | "푸드코트 설계도" |
| ⭐⭐ | [.env.dev](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/config/.env.dev) | 개발 환경 변수 | "개발용 설정 메모" |
| ⭐⭐ | [docker-compose.override.yml](file:///c:/iljoowork/seongdong/cctv_app/docker-compose.override.yml) | 개발 시 자동 오버라이드 | "개발 모드 스위치" |
| ⭐ | [CLAUDE.md](file:///c:/iljoowork/seongdong/cctv_app/volume/ai_server/CLAUDE.md) | 프로젝트 전체 문서 | "프로젝트 백과사전" |

---

## 11. 🧩 Docker Compose 핵심 명령어

### 비유: "푸드코트 관리 리모컨"

```powershell
#  항상 cctv_app/ 폴더에서 실행!
cd C:\iljoowork\seongdong\cctv_app

#  전체 오픈 (백그라운드)
docker compose up -d

#  전체 폐점
docker compose down

#  현재 영업 상태 확인
docker compose ps

#  AI 감시실 로그 보기 (실시간)
docker compose logs -f ai_server

#  AI 감시실만 리부팅
docker compose restart ai_server

#  AI 서버 코드 변경 후 재빌드
docker compose up -d --build ai_server

#  DB만 띄우기 (STEP A용)
docker compose up postgres -d
```

> [!TIP]
> **override 파일의 마법**: `docker-compose.override.yml`이 같은 폴더에 있으면, Docker Compose가 **자동으로** 둘을 합칩니다.
> - 운영(prod): GPU 사용, 소스코드 복사
> - 개발(dev): GPU 없이, 소스코드를 마운트 (수정 즉시 반영!)

---

## 12. 📊 Flask 프로젝트와 FastAPI 프로젝트 폴더 비교

헷갈리지 않도록 두 프로젝트의 "같은 역할 = 다른 위치"를 정리합니다:

| 역할 | Flask (`flask/`) | FastAPI (`cctv_app/`) |
|---|---|---|
| AI 서버 코드 | `flask/ai_server/` | `cctv_app/volume/ai_server/` |
| 모델 파일 | `flask/ai_server/model/` | `cctv_app/volume/ai_server/model/` |
| DB 설정 | 코드 내부 하드코딩 | `.env.dev` / `.env.prod` |
| 서비스 관리 | 수동으로 각각 실행 | `docker compose up -d` 한 방 |
| API 문서 | 없음 | `http://localhost:8000/docs` (자동 생성!) |
| **테스트** | **없음** | `volume/tests/` **pytest 자동 테스트!** |

---

## 13. 🚀 지금 당장 해야 할 일 (체크리스트)

### 이번 주 TODO

**Day 1-2: STEP A (가벼운 테스트로 코드 이해)**
-  1. `cctv_app/`의 git 상태 확인 (`git status`, `git log`)
-  2. Python 가상환경 만들고 의존성 설치
-  3. `docker compose up postgres -d`로 DB만 띄우기
-  4. DBeaver로 PostgreSQL (localhost:5434) 접속 테스트
-  5. `test_cctv.py` 읽기 → `cctv/routes.py` 따라가기 → pytest 실행
-  6. `test_roi.py` 읽기 → ROI 좌표 형식 파악 → pytest 실행
-  7. `test_event.py` 읽기 → 이벤트 구조 이해 → pytest 실행
-  8. 전체 테스트 실행 → 결과 분석

**Day 3-4: STEP B (Docker 전체 통합 테스트)**
-  9. `flask/`의 최신 변경사항 확인 (YAML 동기화)
-  10. `docker compose up -d`로 전체 서비스 띄우기
-  11. 테스트 영상(MP4) 준비 → ffmpeg으로 RTSP 스트림 생성
-  12. 카메라 등록 → 웹에서 영상 확인
-  13. ROI 설정해보기
-  14. 이벤트 발생 → DB 적재 확인

**Day 5: 정리 및 보고**
-  15. ROI 좌표 & 키값 정리 → 개발자 선임님께 전달
-  16. 테스트 결과 정리

---

> [!NOTE]
> **학습의 핵심 패턴**: `test_*.py 읽기` → `해당 service/ 코드 따라가기` → `pytest 실행` → `"아 이렇게 동작하는구나!"` 
> 이 사이클을 반복하면 Docker 없이도 전체 코드를 빠르게 이해할 수 있습니다!
