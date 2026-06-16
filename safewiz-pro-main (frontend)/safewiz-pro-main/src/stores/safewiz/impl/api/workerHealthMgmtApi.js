import request from '@/utils/axios.js'

//[근로자 건강관리 지침 조회]
export function getWHMgmtGuide (data, notify) {
  return request({
    url: `/safewizpro/impl/wHMGuide/getWHMgmtGuide`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[근로자 건강관리 지침 상세 조회]
export function getWHMgmtGuideDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/wHMGuide/getWHMgmtGuideDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[근로자 건강관리 지침 저장]
export function saveWHMgmtGuide (data, notify) {
  return request({
    url: '/safewizpro/impl/wHMGuide/saveWHMgmtGuide',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

export function validateGuideByOrgn (param, notify) {
  return request({
    url: `/safewizpro/impl/wHMGuide/validateGuideByOrgn`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  });
}

export function saveWHMgmtGuideByOrgn (data, notify) {
  return request({
    url: '/safewizpro/impl/wHMGuide/saveWHMgmtGuideByOrgn',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[근로자 건강관리 지침 삭제]
export function deleteWHMgmtGuide (data, notify) {
  return request({
    url: `/safewizpro/impl/wHMGuide/deleteWHMgmtGuide`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[근로자 건강관리 지침 삭제]
export function deleteWHMgmtGuideDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/wHMGuide/deleteWHMgmtGuideDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getWHMgmtGuideReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/wHMGuide/getWHMgmtGuideReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

export function getWHMgmtGuideRequestReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/wHMGuide/getWHMgmtGuideRequestReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

