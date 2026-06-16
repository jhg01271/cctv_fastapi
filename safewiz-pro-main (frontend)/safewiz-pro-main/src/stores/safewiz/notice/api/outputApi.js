import request from '@/utils/axios.js'

// 공지사항 조회
export function getOutputList(param, notify) {
  return request({
    url: '/safewizpro/hseService/getOutputList',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

export function getOutputReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/hseService/getOutputReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType,
    timeout: 1000 * 60 * 5  // 5분 타임아웃 설정
  })
}
