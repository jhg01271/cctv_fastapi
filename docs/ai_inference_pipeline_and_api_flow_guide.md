# SafetyMonitoring 영상 재생 흐름 한눈에 보기

> 이 문서는 `http://localhost:8082/#/SafetyMonitoring`에 들어갔을 때 CCTV 영상이 어떻게 준비되고 화면에 뜨는지 이해하기 위한 짧은 안내서다.

---

## 1. 전체 구조를 먼저 이해하기

이 시스템은 "방송국"처럼 생각하면 쉽다.

| 실제 구성요소 | 비유 | 하는 일 |
| :--- | :--- | :--- |
| CCTV / RTSP | 현장 카메라 원본선 | 카메라가 원본 영상을 계속 보낸다. |
| FFmpeg | 영상 변환기 겸 송출 장비 | 원본 영상을 브라우저가 보기 좋은 H264, 1280x720, 10fps 형태로 바꿔 다시 송출한다. |
| stream_gateway | 방송 송출실 직원 | ai_server의 요청을 받아 FFmpeg를 켜고, MediaMTX 채널에 영상이 올라가도록 관리한다. |
| MediaMTX | 방송국 채널 서버 | `/CAM0001/`, `/CAM0002/` 같은 채널을 열어두고, 브라우저와 AI가 영상을 읽을 수 있게 중계한다. |
| Nginx | 방송국 안내 데스크 | 브라우저가 요청한 `/CAM0001/` 주소를 MediaMTX 쪽으로 넘겨준다. |
| SafetyMonitoring iframe | 시청자 화면 | MediaMTX 채널에 접속해서 영상을 재생한다. |
| AI worker | 분석 요원 | MediaMTX에 올라온 영상을 읽어 안전모, 쓰러짐 같은 이벤트를 판단한다. |

즉, 브라우저가 원본 RTSP 카메라에 직접 붙는 것이 아니다.

```text
현장 카메라 원본선(RTSP)
  -> 송출 장비(FFmpeg)
  -> 방송국 채널 서버(MediaMTX /CAM000X/)
  -> 시청자 화면(SafetyMonitoring iframe)

그리고 AI worker는 같은 MediaMTX 채널을 옆에서 읽어 분석한다.
```

---

## 2. 스토리로 보는 호출 흐름

사용자가 `SafetyMonitoring` 화면에 들어온다.  
프론트는 백엔드에 "이 모니터링 그룹에 보여줄 CCTV 목록 줘"라고 요청한다.

1. 프론트 화면 JS가 CCTV 목록 API를 호출한다.  
   [SafetyMonitoring JS](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/frontend/dist/js/chunk-fcdfb540.89a82a99.js:1)

2. 요청은 FastAPI router에 도착한다.  
   여기서 `get_group_pro_detail()`이 실행되고, 서비스 함수로 넘긴다.  
   [profile/routes.py](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/profile/routes.py:125)

3. `list_group_details()`가 DB에서 카메라 목록과 화면 배치 정보를 가져온다.  
   그런데 바로 프론트에 돌려주지 않고, 먼저 영상 채널들이 준비됐는지 확인한다.  
   [profile/service.py - list_group_details](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/profile/service.py:77)

4. `_ensure_group_streams_ready()`가 여러 카메라를 병렬로 준비한다.  
   6개 카메라를 한 대씩 순서대로 기다리면 첫 화면이 느려지기 때문이다.  
   [profile/service.py - 그룹 스트림 준비](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/profile/service.py:39)

5. `_ensure_camera_stream_ready()`가 카메라 하나씩 MediaMTX에서 볼 수 있는 상태인지 확인한다.  
   이미 준비됐으면 넘어가고, 준비되지 않았으면 stream_gateway에 송출 시작을 요청한다.  
   [profile/service.py - 카메라별 준비 확인](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/profile/service.py:18)

6. ai_server는 직접 FFmpeg를 켜지 않는다.  
   `_start_stream()`이 stream_gateway에 "이 카메라 송출 시작해줘"라고 HTTP 요청을 보낸다.  
   [remote/service.py - stream_gateway 요청](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/service/remote/service.py:53)

7. stream_gateway가 요청을 받는다.  
   [stream_gateway/routes.py](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/stream_gateway/routes.py:23)

8. stream_gateway의 `start_stream()`이 실제 송출 준비를 한다.  
   FFmpeg publish를 시작하고, MediaMTX 채널이 진짜 ready인지 기다린다.  
   [stream_gateway/service.py](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/stream_gateway/service.py:12)

9. FFmpeg가 원본 RTSP 영상을 WebRTC 재생에 맞는 형태로 바꿔 MediaMTX에 밀어 넣는다.  
   [media_server.py - FFmpeg 실행](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/media_server.py:77)

10. `wait_stream_ready()`가 MediaMTX에 "이제 `/CAM000X/` 채널 방송 중이야?"라고 확인한다.  
    [media_server.py - ready 확인](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/ai_server/core/ai/media_server.py:329)

11. 준비가 끝나면 `list_group_details()`가 프론트에 `cctv_play_url`을 포함한 목록을 돌려준다.

```json
{
  "cctv_id": "CAM0001",
  "cctv_play_url": "/CAM0001/",
  "title": "A동 (가짜1)"
}
```

12. 프론트는 그 URL로 iframe을 만든다.

```html
<iframe src="/CAM0001/?controls=0"></iframe>
```

13. 브라우저의 `/CAM0001/` 요청은 Nginx를 거쳐 MediaMTX로 전달된다.  
    [nginx conf - CAM 프록시](C:/iljoowork/seongdong/cctv_app2/cctv_app/volume/nginx/conf.d/jeonjin-camera.i-gns.co.kr.conf:21)

---

## 3. 한 줄 요약

`SafetyMonitoring`은 단순히 CCTV 목록만 가져오는 것이 아니라, 목록에 들어갈 MediaMTX 방송 채널들이 실제로 켜져 있는지 확인한 뒤 `/CAM000X/` URL을 프론트에 돌려준다.
