import request from '@/utils/axios.js'
// 공지사항 조회
export function getAlarm(param, notify) {
  return request({
    url: '/safewizpro/alarm/getAlarm',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

//공지사항 웹,앱 푸시 열람
export function updateReadAlarm(data, notify) {
  return request({
    url: '/safewizpro/alarm/updateReadAlarm',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//비상상황 발생 푸시 알람
export function sendEmergencyMsg(data, notify) {
  return request({
    url: '/safewizpro/alarm/sendEmergencyMsg',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//안전보건 교육 실시 계획서 상세 푸시 알람
export function sendTrainingPlanImplMsg(data, title, menuId, notify) {
  return request({
    url:  `/safewizpro/alarm/sendTrainingPlanImplMsg/${title}/${menuId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

// 선택된 인원의 토큰 조회
export function getSelectedTokenUserInfo(param, notify) {
  return request({
    url: '/safewizpro/alarm/getSelectedTokenUserInfo',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}