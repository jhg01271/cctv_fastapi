# 경광등 설정 API

## 목적

프론트엔드는 경광등을 직접 켜고 끄지 않는다. 프론트엔드는 `tb_camera`에
저장되는 카메라별 경광등 설정값을 조회/변경하고, AI 서버는 이벤트 발생 시
이 값을 읽어 Modbus/TCP 신호를 보낼지 결정한다.

## 전체 흐름

```text
프론트 CCTV 설정 화면 토글
  -> PUT /cctv/warning_light/settings
  -> tb_camera의 warning_light_* 컬럼 저장
  -> AI 서버가 위험 이벤트 감지
  -> warning_light_enabled=true이면 경광등 장비에 Modbus ON 전송
  -> warning_light_duration_sec 뒤 Modbus OFF 전송
```

여기서 `enabled` 또는 `warning_light_enabled`는 "현재 경광등이 물리적으로
켜져 있는지"가 아니다. 정확한 의미는 "AI가 위험을 감지했을 때 이 CCTV에
매칭된 경광등을 울려도 되는지"이다.

## tb_camera에 컬럼을 둔 이유

회의 기준으로 CCTV와 경광등은 1:1 매칭이다. 따라서 카메라 등록/수정 시
해당 카메라에 붙은 경광등 IP와 사용 여부를 같이 관리하는 방식이 가장
단순하다.

```text
warning_light_enabled      위험 감지 시 경광등을 울릴지 여부
warning_light_ip           실제 I/O 제어기 IP
warning_light_port         Modbus/TCP 포트
warning_light_unit_id      Modbus 장비 ID
warning_light_coil_address 경광등 출력 채널 주소
warning_light_duration_sec 몇 초 동안 울릴지
```

## 설정 목록 조회

```http
GET /cctv/warning_light/settings?comp_id=IGNS_esg_team
```

## 카메라별 설정 조회

```http
GET /cctv/warning_light/settings/CAM0001
```

## 설정 저장

```http
PUT /cctv/warning_light/settings
Content-Type: application/json
```

```json
{
  "cctv_id": "CAM0001",
  "comp_id": "IGNS",
  "enabled": true,
  "device_ip": "192.168.151.111",
  "port": 502,
  "unit_id": 1,
  "coil_address": 11,
  "duration_sec": 20,
  "user_id": "system"
}
```

## 필드 의미

- `enabled`: 위험 감지 시 경광등 신호를 보낼지 여부
- `device_ip`: I/O 제어기 IP. PDF 기준 `192.168.151.111~116`
- `port`: Modbus/TCP 포트. PDF 기준 `502`
- `unit_id`: Modbus Unit ID. PDF 예시 기준 `1`
- `coil_address`: 경광등 출력 coil 주소. PDF의 `00011`은 실제 전송값
  `0x000B`, 즉 10진수 `11`
- `duration_sec`: ON 신호를 보낸 뒤 OFF 신호를 보내기까지의 시간

## AI 서버 동작

1. E001/E002/E004 이벤트 감지
2. `tb_camera.camera_id` 기준 카메라 설정 조회
3. `warning_light_enabled=false`면 Modbus 신호를 보내지 않음
4. `warning_light_enabled=true`이고 `warning_light_ip`가 있으면
   `FC 05 Write Single Coil` ON 전송
5. `warning_light_duration_sec` 후 OFF 전송

## 아직 구현하지 않은 것

PDF 마지막 페이지의 Digital Input, 즉 부저 정지 버튼 상태 읽기는 이번
범위에 넣지 않았다. 현재 구현은 회의록에서 합의된 기본 범위인
"프론트 설정값 + AI 서버의 Output ON/OFF 제어"까지다.
