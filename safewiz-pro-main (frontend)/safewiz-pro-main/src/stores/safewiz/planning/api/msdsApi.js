import request from '@/utils/axios.js'

export function getMsds (param, notify) {
  return request({
    url: `/safewizpro/planning/getMsds`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getMsdsDetail (msdsId, notify) {
  return request({
    url: `/safewizpro/planning/getMsdsDetail/${msdsId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertMsds (data, notify) {
  return request({
    url: `/safewizpro/planning/insertMsds`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function updateMsds (msdsId, data, notify) {
  return request({
    url: `/safewizpro/planning/updateMsds/${msdsId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteMsds (msdsId, notify) {
  return request({
    url: `/safewizpro/planning/deleteMsds/${msdsId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteMsdss (data, notify) {
  return request({
    url: `/safewizpro/planning/deleteMsdss`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//물질안전보건자료 MSDS 
//화학물질목록 조회
export function getChemList(param, notify) {
  return request({
    url: '/safewizpro/planning/getChemData',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//msds 관리대장 출력
export function getMsdsReport(param, isNotify) {
  let responseType = "blob"
  return request({
    url: '/safewizpro/planning/getMsdsReport',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    responseType: responseType,
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}