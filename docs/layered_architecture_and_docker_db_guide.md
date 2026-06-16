# 📖 FastAPI CCTV 백엔드 & Docker DB 심화 학습 노트

> **일자**: 2026년 6월 8일
> **주요 내용**: 도커 네트워크 포트 매핑, 볼륨 데이터 영구 보존 원리, Layered Architecture 패턴 분석

---

## 1. 🔌 DBeaver와 도커 DB의 연결 원리 (포트 포워딩)

로컬 PC(Windows)에 PostgreSQL을 별도로 설치하지 않았음에도, DBeaver에서 `localhost:5434`로 데이터베이스를 조회할 수 있는 구조입니다.

### 💡 핵심 키워드: 포트 포워딩 (Port Forwarding)
*   **도커 컨테이너**: 내 PC(Windows) 안에서 가동되는 독립된 **"미니 가상 리눅스 컴퓨터"**입니다.
*   **격리된 포트**: 포스트그레스는 이 미니 컴퓨터의 내부 포트 `5432`번에서 구동됩니다.
*   **포트 다리 놓기**: `docker-compose.yml`에서 `5434:5432`로 포트를 연결해 주었습니다.
*   **통신 흐름**:
    1.  DBeaver가 내 PC 주소(`localhost`)의 `5434` 포트에 노크합니다.
    2.  도커(Docker Desktop)가 요청을 낚아채서 **컨테이너 내부 `5432` 포트**로 안전하게 토스해 줍니다.
    3.  컨테이너 내부 포스트그레스가 쿼리를 수행하고 결과를 응답해 줍니다.

---

## 2. 💾 도커 컴포즈 재시작 시 데이터 보존 원리 (볼륨 마운트)

원래 도커 컨테이너는 삭제(`docker compose down`)되면 내부 데이터도 모두 지워지는 **"일회용 컴퓨터"**입니다. 하지만 컴포즈를 껐다 켜도 데이터가 날아가지 않는 비결이 있습니다.

### 💡 핵심 키워드: 볼륨 마운트 (Volume Mount)
*   **설정**:
    ```yaml
    volumes:
      - pgdata:/var/lib/postgresql/data
    ```
*   **동작**: 컨테이너 내부의 데이터 저장 경로(`/var/lib/postgresql/data`)를 사용자 PC 하드디스크의 안전 영역(Docker Named Volume인 `pgdata`)에 링크(연결)해 둡니다.
*   **수명**: 컨테이너가 파괴되어도 실제 테이블 구조와 로우 데이터는 사용자 PC 가상 디스크 파일(`ext4.vhdx`) 속에 영구히 보존됩니다. 다시 컨테이너를 올릴 때 기존 경로를 다시 링크하여 데이터를 그대로 이어 씁니다.

### 📁 실제 파일의 물리적 위치 (Windows)
*   도커는 내부적으로 WSL2(Linux 가상화)를 사용합니다.
*   **물리 가상 드라이브 경로**:
    `C:\Users\<사용자명>\AppData\Local\Docker\wsl\data\ext4.vhdx`
*   **윈도우 탐색기 주소창 입력 시 확인 경로**:
    `\\wsl.localhost\docker-desktop-data\data\docker\volumes\`
    *   안에 들어가면 `cctv_app_pgdata/_data` 폴더가 있고, 그 내부에 포스트그레스의 물리 이진 파일들이 존재합니다. (바이너리 압축 파일들이라 메모장으로는 읽을 수 없으며, **DBeaver**를 통해 해독해서 보아야 합니다.)

---

## 3. 🧪 Pytest 테스트 실행 시의 DB 세션 및 더미 데이터 주입

`pytest`를 실행할 때 DB 테이블이 자동으로 만들어지고, 가상의 데이터가 조작되는 과정입니다.

*   **테이블 자동 빌드**: `conftest.py`의 `Base.metadata.create_all()`이 실행되면서 테스트 DB에 빈 테이블들을 자동으로 만들어 줍니다. (이미 테이블이 존재한다면 덮어쓰지 않고 기존 테이블을 그대로 사용합니다.)
*   **기본 시드 데이터 주입**:
    *   `if not session.get(Camera, "CAM0001"):`
    *   DB 상태를 조회해서 `CAM0001` 카메라나 `SVR0001` 서버 정보가 비어 있을 때만, 테스트 구동용 기본 가상 데이터를 자동으로 인서트합니다.
*   **테스트용 임시 데이터 청소 (Clean-up)**:
    *   `TestSaveCamera` 테스트에서 임시로 생성된 카메라(`CAM_TEST_PYTEST`) 등은 테스트 파일 최하단에 정의된 `TestDeleteCamera`가 API를 호출하여 즉시 지워버립니다.
    *   덕분에 테스트가 정상 완료되고 나면 DB에는 기본 시드 데이터만 깔끔하게 남습니다.

---

## 🏗️ 4. 백엔드 아키텍처: Router ➡️ Service ➡️ Repository ➡️ DB 패턴

이 프로젝트는 웹 백엔드 설계의 표준인 **Layered Architecture(계층형 아키텍처)**를 따르고 있습니다. 각 레이어의 역할 분담과 흐름은 다음과 같습니다.

### 🍔 식당 비유를 통한 역할 정리

| 컴포넌트명 | 식당 역할 | 주요 기능 | 코드 파일 예시 |
| :--- | :--- | :--- | :--- |
| **Router** | **홀 점원 (서빙)** | 손님의 요청(HTTP request)을 알맞은 서비스로 배정하고 최종 요리를 포장하여 전달 | `service/roi/routes.py` |
| **Service** | **주방 요리사 (조리)** | 재료를 다듬고, 분류하고, 정제하여 실제 비즈니스 로직(예: 카메라 기준 그룹화 등)을 수행 | `service/roi/service.py` |
| **Repository** | **창고 관리자 (출고)** | 데이터베이스(창고)에서 날것의 데이터(원시 레코드)를 가져오거나 집어넣는 SQL 쿼리 처리 전담 | `service/roi/repository.py` |
| **DB** | **식자재 창고 (보관)** | 모든 원천 데이터가 보관되어 있는 저장소 | `PostgreSQL (tb_camera_roi)` |

### 🔄 호출 흐름 vs 데이터 흐름

#### 1) 호출 흐름 (의존성 방향): `Router ➡️ Service ➡️ Repository ➡️ DB`
점원이 요리사에게 주문하고, 요리사가 창고 관리자에게 재료를 요청하며, 관리자가 창고 문을 여는 흐름입니다.
*   `test_roi.py` ➡️ `routes.py`의 `get_rois()` ➡️ `service.py`의 `list_rois()` ➡️ `repository.py`의 `fetch_rois_by_comp()` ➡️ DB 쿼리

#### 2) 데이터 흐름 (가공 및 반환): `DB ➡️ Repository ➡️ Service ➡️ Router`
창고에서 나온 날것의 재료를 관리자가 받아 요리사에게 주고, 요리사가 피자로 요리(정제)하여 점원을 통해 손님상에 내놓는 흐름입니다.
*   `DB`에서 날것의 행 반환 ➡️ `Repository`가 딕셔너리 리스트 형태로 변환 ➡️ `Service`가 이를 받아 **카메라 ID 기준으로 그룹화하여 `model_list` 구조로 조립(정제)** ➡️ `Router`가 JSON 응답으로 최종 포장하여 반환

---

## 5. 🗂️ 프로젝트 내 도메인별 폴더 구조 일관성

이 프로젝트는 새로운 폴더를 열어도 똑같은 규칙으로 코드가 작성되어 있으므로 일관된 분석이 가능합니다.

*   `cctv/` (카메라 기본 정보 관리) ➡️ `routes.py`, `service.py`, `repository.py`, `model.py`
*   `event/` (이벤트 이력 조회) ➡️ `routes.py`, `service.py`, `repository.py`, `model.py`
*   `telegram/` (알람 매니저 제어) ➡️ `routes.py`, `service.py`, `repository.py`, `model.py`

---

## 🛠️ 6. DBeaver에서 수동 테스트 데이터 적재하는 방법

목록 조회 API 등의 정상 가공 여부를 검증하고 싶을 때, DBeaver의 SQL 편집기(`Ctrl + Alt + T` 또는 마우스 우클릭 -> `New SQL editor`)를 열고 아래 쿼리를 실행해 임의로 이벤트를 쌓을 수 있습니다.

```sql
-- 가상 쓰러짐 감지(E001) 이벤트 적재 쿼리
INSERT INTO public.tb_camera_event_hist 
(event_time, camera_id, event_type, event_desc, file_path, isread, remark)
VALUES 
(NOW(), 'CAM0001', 'E001', '가상 쓰러짐 감지 테스트', '/dummy/path.jpg', false, '테스트 비고');
```

*   **안전성**: 로컬 도커 DB 환경은 개발자 개인의 독립된 **놀이터(Sandbox)**이기 때문에 여기에 더미 데이터를 마음껏 넣고 삭제해도 실제 배포 서버(성동조선 서버 컴퓨터 등)에는 단 한 줄의 데이터도 들어가지 않으므로 안전합니다.
