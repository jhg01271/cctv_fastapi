# CCTV 프레임 AI 추론 및 이벤트 저장 흐름

> 이 문서는 CCTV 프레임이 들어와서, 설정된 모드에 따라 모델을 돌리고, 이벤트를 판단해 DB와 알림까지 이어지는 흐름을 이해하기 위한 짧은 안내서다.

---

## 1. 전체 구조를 먼저 이해하기

이 기능은 "감시 요원이 있는 관제실"처럼 생각하면 쉽다.

| 실제 구성요소 | 비유 | 하는 일 |
| :--- | :--- | :--- |
| RTSP CCTV | 현장 카메라 | 영상을 계속 보낸다. |
| `rtsp_reader_process` | 화면을 계속 넘겨주는 직원 | RTSP에서 프레임을 읽어 `frame_queue`에 넣는다. |
| `frame_queue` | 최신 사진 바구니 | AI가 볼 프레임을 잠깐 담아둔다. 오래된 프레임은 버리고 최신 프레임을 우선한다. |
| `safety_process` | AI 감시 요원 | 프레임을 꺼내 Detection/Pose 모델을 돌리고 이벤트를 판단한다. |
| `ws_bridge` | 무전기 | 현재 프레임의 bbox, keypoints, 이벤트 후보를 디버그 화면으로 실시간 전달한다. |
| `event_queue` | 사고 접수함 | 실제 저장해야 할 확정 이벤트를 워커에게 넘긴다. |
| `SafetyDBWorker` | 기록 담당자 | 이벤트를 DB에 저장하고, 저장 성공 후 텔레그램 알림을 보낸다. |
| `tb_camera_event_hist` | 사고 기록부 | `SafetyMonitoringHistory`가 조회하는 이벤트 이력 테이블이다. |

한 줄로 쓰면 이렇다.

```text
CCTV RTSP
  -> rtsp_reader_process
  -> frame_queue
  -> safety_process
  -> 실시간 결과는 ws_bridge로 전달
  -> 확정 이벤트는 event_queue로 전달
  -> SafetyDBWorker가 DB 저장
  -> 저장 성공 후 Telegram 알림
```

---

## 2. 스토리로 보는 실제 처리 흐름

카메라가 켜지면 먼저 `run_cctv()` 또는 `run_all()` 흐름을 통해 카메라가 AI 매니저에 등록된다.

1. 카메라 시작 요청이 들어오면 `_register_camera()`가 실행된다.  
   여기서 `jit_only`인지 일반 AI 추론 카메라인지 먼저 나뉜다.  
   [remote/service.py - 카메라 등록](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/remote/service.py:113)

2. `jit_only=True`이면 모델 추론을 하지 않고 `jit_process`로 간다.  
   이 모드는 학습/검증용 이미지를 주기적으로 저장하는 용도다.  
   [jit_processor.py - JIT 이미지 수집](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/jit_processor.py:63)

3. 일반 모드이면 DB에서 이 카메라의 ROI, 모델 실행 여부, 안전 격자 정보를 읽는다.  
   `Detection`이 켜져 있는지, `Pose`가 켜져 있는지도 여기서 결정된다.  
   [remote/service.py - ROI/모델 플래그 전달](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/remote/service.py:137)

4. `CameraProcessManager.add_camera()`가 카메라별 프로세스를 준비한다.  
   카메라 1대당 보통 RTSP reader 프로세스 1개와 AI 프로세스 1개가 뜬다.  
   [process_manager.py - 카메라 추가](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/process_manager.py:93)

5. `_create_worker()`에서 AI가 읽을 영상 주소를 정한다.  
   설정값 `AI_READER_INPUT`에 따라 원본 RTSP를 직접 읽거나 MediaMTX 경유 주소를 읽는다.  
   [process_manager.py - reader 입력 선택](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/process_manager.py:146)

6. `_start_worker()`가 RTSP reader를 먼저 켠다.  
   reader가 첫 프레임을 받았는지 잠깐 기다린 뒤 AI 프로세스를 시작한다.  
   [process_manager.py - 프로세스 시작](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/process_manager.py:162)

7. `rtsp_reader_process()`는 RTSP에서 프레임을 계속 읽어 `frame_queue`에 넣는다.  
   큐가 가득 차면 오래된 프레임을 버리고 새 프레임을 넣는다. 실시간성 때문에 이게 중요하다.  
   [rtsp_reader.py - 프레임 공급](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/rtsp_reader.py:61)

8. `safety_process()`는 `frame_queue`에서 프레임을 꺼내 모델을 돌린다.  
   `AI_FRAME_SKIP`에 따라 모든 프레임을 보지 않고 일부 프레임만 추론한다.  
   [processor.py - AI 메인 루프](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:43)

9. 모델 결과는 두 갈래로 나간다.
   - 실시간 확인용 결과: `_send_result()` → `ws_bridge` → `/safety/ws/{camera_id}` → `/safety/debug`
   - 확정 이벤트: `_put_event()` → `event_queue` → `SafetyDBWorker`

   [processor.py - WebSocket 결과 전송](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:441)  
   [processor.py - 이벤트 큐 전달](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:747)

10. `SafetyDBWorker`는 `event_queue`에서 이벤트를 꺼내 DB에 저장한다.  
    저장 성공 후 텔레그램 알림을 보낸다.  
    [worker.py - DB 저장 후 텔레그램](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/worker.py:57)

11. DB 저장은 `tb_camera_event_hist`에 들어간다.  
    `SafetyMonitoringHistory`는 이 테이블을 조회한다.  
    [safety/repository.py - 이벤트 저장](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/repository.py:101)

---

## 3. 모델 모드: crop과 full의 차이

현재 핵심 설정은 `AI_POSE_MODE`다.  
[config.py - AI 설정](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/config/config.py:63)

| 모드 | 흐름 | 의미 |
| :--- | :--- | :--- |
| `crop` | Detection으로 사람을 찾고, 사람 영역만 잘라 Pose 모델을 돌림 | 지금 쓰는 cascade 방식이다. 사람이 있는 부분만 보니 Pose 오검출과 연산량을 줄이기 좋다. |
| `full` | Detection 프레임과 Pose 프레임을 번갈아 전체 화면 기준으로 돌림 | 전체 화면 Pose 모델을 쓸 때의 방식이다. |

`crop` 모드의 흐름은 이렇다.

```text
전체 프레임
  -> Safety Detection 모델
  -> person bbox 추출
  -> person 주변을 crop
  -> Pose crop 모델
  -> crop 좌표를 원본 프레임 좌표로 되돌림
  -> collapse/standing/keypoints 결과 생성
```

관련 코드:

- [processor.py - crop 모드 시작](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:151)
- [processor.py - 사람 crop 후 Pose 추론](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:230)
- [processor.py - keypoints 좌표 보정](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:690)

`full` 모드는 Detection과 Pose를 번갈아 실행한다.

- [processor.py - full 모드 시작](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:320)

---

## 4. 이벤트별 판단과 저장 방식

| 이벤트 | 판단 조건 | 저장 파일 | 즉시 릴레이 | DB 저장 | 텔레그램 |
| :--- | :--- | :--- | :---: | :---: | :---: |
| E001 안전모 미착용 | `no_helmet` 감지 | JPG | O | O | DB 저장 후 |
| E002 위험구역 출입 | `person` 중심점이 ROI 안에 있음 | JPG | O | O | DB 저장 후 |
| E003 작업자 쓰러짐 | `collapse`가 설정 시간 이상 지속 | MP4 | X | O | DB 저장 후 |
| E004 위험체 접근 감지 | 안전 격자 기반으로 장비 이동 예측 구역에 사람이 있음 | JPG | O | O | DB 저장 후 |

공통적으로 이벤트는 쿨다운이 있다. 기본값은 `AI_EVENT_COOLDOWN_SEC=600`이라 같은 이벤트를 너무 자주 저장하지 않는다.  
[config.py - 이벤트 쿨다운](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/config/config.py:80)

E003은 단발성 bbox만으로 바로 저장하지 않고, `collapse`가 `AI_COLLAPSE_SUSTAIN_SEC` 이상 지속되어야 저장한다.  
[processor.py - E003 지속 판정](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:304)

E004는 위험체가 어느 격자로 이동할지 예측하고, 그 예측 격자 안에 사람이 있으면 발화한다.  
[processor.py - E004 격자 판단](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:619)

---

## 5. "즉시 알림"과 "DB 저장 후 알림"은 구현되어 있나?

현재 구현 기준으로는 일부 구현되어 있다.

### 즉시 반응

E001, E002, E004는 감지 순간 `trigger_relay()`가 호출된다.  
이건 DB 저장을 기다리지 않는 즉시 반응이다.

- [processor.py - E001 릴레이](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:183)
- [processor.py - E002 릴레이](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:195)
- [processor.py - E004 릴레이](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/processor.py:675)
- [events.py - 릴레이 함수](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/events.py:33)

주의할 점은 현재 `trigger_relay()` 내부가 실제 GPIO 제어가 아니라 TODO 형태라는 점이다. 즉, 코드상 즉시 반응 지점은 있지만 실제 장비 제어는 아직 연결부 구현이 필요하다.

### DB 저장 후 알림

텔레그램은 DB 저장 후 전송된다.  
`SafetyDBWorker._save()`에서 `repository.save_event()`가 먼저 실행되고, 그 다음 `send_telegram_alert()`가 실행된다.

- [worker.py - 저장 후 알림 순서](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/worker.py:57)
- [telegram/service.py - 텔레그램 전송](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/telegram/service.py:28)

### 아직 없는 것

책임님이 말한 것처럼 "이벤트 종류별로 즉시 알림할지, DB 저장 후 알림할지 정책 테이블/설정으로 나누는 구조"는 아직 일반화되어 있지 않다.

현재는 코드에 직접 박혀 있다.

```text
E001/E002/E004: 감지 즉시 trigger_relay()
모든 이벤트: event_queue -> DB 저장 -> Telegram
E003: 즉시 relay 없음, DB 저장 후 Telegram
```

나중에 정책을 제대로 분리하려면 예를 들어 이런 구조가 필요하다.

```text
event_policy
  E001: immediate_relay=true, notify_after_db=true
  E002: immediate_relay=true, notify_after_db=true
  E003: immediate_relay=false, notify_after_db=true
  E004: immediate_relay=true, notify_after_db=true
```

그러면 `processor.py` 안에 이벤트별 알림 방식이 하드코딩되지 않고, 정책을 읽어 동작하게 만들 수 있다.

---

## 6. 디버그 화면과 히스토리 화면의 차이

`/safety/debug`는 DB를 보는 화면이 아니다.  
AI 프로세스가 지금 막 만든 실시간 결과를 WebSocket으로 받아 bbox와 skeleton을 그리는 화면이다.

- [safety/routes.py - WebSocket](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/safety/routes.py:32)
- [ws_bridge.py - 카메라별 팬아웃](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/ws_bridge.py:20)

반대로 `SafetyMonitoringHistory`는 DB에 저장된 확정 이벤트를 조회한다.

- [event/routes.py - 이벤트 이력 조회 API](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/event/routes.py:43)
- [event/repository.py - 이벤트 이력 조회](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/event/repository.py:39)

그래서 debug 화면에서 bbox가 보인다고 해서 무조건 History에 저장되는 것은 아니다.  
History에 저장되려면 쿨다운, ROI, collapse 지속 시간 같은 이벤트 확정 조건을 통과해야 한다.

