import request from '@/utils/axios.js'

// 시정조치 요구서 조회
export function getCorrectiveActionRequest(param, notify) {
  return request({
    url: '/safewizpro/evaluation/getCorrectiveActionRequest',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

// 시정조치 요구서 상세 조회
export function getCorrectiveActionRequestDetail(param, notify) {
  return request({
    url: '/safewizpro/evaluation/getCorrectiveActionRequestDetail',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,  // GET 요청에서도 data 사용
    notify: { kind: notify },
  });
}

// 시정조치 요구서 저장 
export function saveCorrectiveActionRequest(data, notify) {
  return request({
    url: '/safewizpro/evaluation/insertCorrectiveActionRequest',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

// 시정조치 요구서 수정
export function updateCorrectiveActionRequest(data, notify) {
  return request({
    url: '/safewizpro/evaluation/updateCorrectiveActionRequest',
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // PUT 요청에 body를 사용
    notify: { kind: notify }
  });
}

// 시정조치 요구서 단일 삭제
export function deleteCorrectiveActionRequest (param, notify) {
  return request({
    url: `/safewizpro/evaluation/deleteCorrectiveActionRequest`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 시정조치 요구서 다중 삭제
export function deleteCorrectiveActionRequests (param, notify) {
  return request({
    url: `/safewizpro/evaluation/deleteCorrectiveActionRequests`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 시정조치 요구서 리포트 출력
export function printCorrectiveActionRequests (param, notify) {
  return request({ 
    url: '/safewizpro/evaluation/printCorrectiveActionRequests',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: {kind: notify},
    responseType: 'blob'
  })
}