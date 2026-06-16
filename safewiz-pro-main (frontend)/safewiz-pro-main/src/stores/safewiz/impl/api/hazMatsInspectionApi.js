import request from '@/utils/axios.js'

// [작업장 위험물/유해화학물질 점검 조회]
export function getHazmatChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getHazmatChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [작업장 위험물/유해화학물질 점검사항 조회]
export function getHazmatChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getHazmatChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


// [마지막 점검사항 불러오기]
export function getLastChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getLastChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [예시 불러오기]
export function getBaseDatasetHazmatChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getBaseDatasetHazmatChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}



//[작업장 위험물/유해화학물질 점검사항 저장]
export function saveHazmatChecklist (data, notify) {
  return request({
    url: '/safewizpro/impl/hazmatChecklist/saveHazmatChecklist',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[작업장 위험물/유해화학물질 점검 삭제]
export function deleteHazmatChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/deleteHazmatChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[작업장 위험물/유해화학물질 점검사항 초기화]
export function deleteHazmatChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/deleteHazmatChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// [점검사항 관리 조회]
export function getDatasetHazmatChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getDatasetHazmatChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [점검사항 관리 항목 조회]
export function getDatasetHazmatChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getDatasetHazmatChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[작업장 위험물/유해화학물질 점검사항 저장]
export function saveDatasetHazmatChecklist (data, notify) {
  return request({
    url: '/safewizpro/impl/hazmatChecklist/saveDatasetHazmatChecklist',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[점검사항 항목 삭제]
export function deleteDatasetHazmatChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/deleteDatasetHazmatChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getHazmatStatusReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getHazmatStatusReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

export function getHazmatChecklistReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getHazmatChecklistReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

