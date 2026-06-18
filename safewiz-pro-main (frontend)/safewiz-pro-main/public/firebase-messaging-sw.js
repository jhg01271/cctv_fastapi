// CDN 방식으로 Firebase 모듈 로드
importScripts('https://www.gstatic.com/firebasejs/11.5.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/11.5.0/firebase-messaging-compat.js');
// Firebase 구성 정보 (웹 앱 등록 시 받은 설정값)
firebase.initializeApp({
  apiKey: "AIzaSyBThVqGaxgSeUoO6xQA244sVQAjBZ6pY28",
  authDomain: "safewiz-pro.firebaseapp.com",
  projectId: "safewiz-pro",
  messagingSenderId: "618183838195",
  appId: "1:618183838195:web:8bc85c0f9dba04532a2a86"
});
// Firebase 메시징 객체 가져오기
//###################################################################### 이 파일을 수정한 경우, 사용자 정보 변경에서 Safewiz Pro 알림을 차단 후 재허용해야 수정사항이 적용됩니다. #############################################################################/
const messaging = firebase.messaging();
let params = ''    //json data
let alarmType = '' //알람 호출 경로
//백그라운드 웹 푸시 클릭 이벤트
self.addEventListener('notificationclick', async (event) => {
  const url = `${self.location.origin}#/${alarmType}?fromPush=true&fcmPayload=${event.notification.data}`;
  //현재 서비스 워커가 실행 중인 사이트의 기본 URL을 저장함
  const urlToOpen = new URL(self.origin)
    //비동기 작업이 끝날 때 까지 이벤트가 종료되지 않도록 함
    event.waitUntil(
      //같은 오리진에서 열린 창만 가져옴
      clients.matchAll({type:"window", includeUncontrolled:true}).then(windowClients => {
        let foundWindowClient = null
        //같은 오리진에서 열린 창의 데이터를 client에 삽입
        for (let i = 0; i < windowClients.length; i++) {
          const client = windowClients[i];
          //열린 브라우저 창 목록에서 특정 도메인("safewizpro")이 포함된 창을 찾음
          //개발일때는 includes('localhost'), 운영일때는 includes('safewizpro')로 변경, 새 창이 열리게 하고싶다면 ''
          if((new URL(client.url).hostname.includes("localhost")) &&"focus" in client){
            foundWindowClient = client;
            break;
          }
        }
        //만약 특정 도메인이 포함된 창이 존재한다면
        if (foundWindowClient) {
          // 해당 탭을 focus하여 이동시킴
          return foundWindowClient.focus().then((focusedClient) => {
              if ("navigate" in focusedClient) {
                  focusedClient.postMessage(params)
              }
          });
          
      // 그게 아니라면 새 창을 열어서 원하는 URL로 이동시킴 
      } else if (clients.openWindow) {
          return clients.openWindow(url);
        }
    })
    )
  event.notification.close();
})

messaging.onBackgroundMessage( async payload => {
  console.log('[firebase-messaging-sw.js] 백그라운드 메시지 수신:', payload);
  const {title, body} = payload.data
  const bodyOptions = {
    body: body, //푸시 알람 텍스트
    icon: '/assets/img/common/icon_safewiz_alarm.png',  //푸시알람 아이콘
    data : payload.data.params //데이터
    // actions: [  //푸시 알람 하단 버튼 (self.addEventListener 이벤트에서 event.action으로 컨트롤 가능)
    //   { action: "open", title: "열람하기" },
    //   { action: "dismiss", title: "닫기" }
    // ]
  };
  alarmType = payload.data.alarmType
  params = payload.data.params
  //백그라운드 웹 푸시 알람 발송
  self.registration.showNotification(title, bodyOptions);
});