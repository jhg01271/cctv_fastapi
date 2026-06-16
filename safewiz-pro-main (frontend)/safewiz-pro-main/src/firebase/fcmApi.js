import request from '@/utils/axios.js';

// 라우터 이름 조회
export function getRouterNm(param, notify) {
  return request({
    url: '/safewizpro/fcm/getRouterNm',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

//아직 구현되지 않음
export function readAllAlarm (data, notify) {
  return request({
    url: `/safewizpro/fcm/readAllAlarm`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}






