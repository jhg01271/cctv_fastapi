import request from '@/utils/axios.js'

// 공지사항 조회
export function getNotice(param, notify) {
  return request({
    url: '/safewizpro/notice/getNotice',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

// 공지사항 상세 조회
export function getNoticeDetail(param, notify) {
  return request({
    url: '/safewizpro/notice/getNoticeDetail',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}
// 공지사항 추가
// export function insertNotice(data) {
//   return request({
//     url: '/safewizpro/notice/insertNotice',
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data: data,  // POST 요청에 body를 사용
//   });
// }
//
// // 공지사항 수정
// export function updateNotice(data) {
//   return request({
//     url: '/safewizpro/notice/updateNotice',
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data: data,  // POST 요청에 body를 사용
//   });
// }

// 공지사항 저장 (추가 및 수정 통합)
export function saveNotice(data, notify) {
  return request({
    url: '/safewizpro/notice/saveNotice',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}
// 공지사항 삭제
export function deleteNotice(data,notify) {
  return request({
    url: '/safewizpro/notice/deleteNotice',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body를 사용
    notify: { kind: notify }
  });
}

//공지사항 웹,앱 푸시 알람 발송
export function sendPushMsg(data, menuId, notify) {
  return request({
    url: `/safewizpro/notice/sendPushMsg/${menuId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}
