import request from '@/utils/axios.js'

//[안전점검표 조회]
export function getSafetyChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/getSafetyChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전점검표 항목 및 사항 조회]
export function getSafetyChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/getSafetyChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전점검표 항목 조회]
export function getSafetyChecklistItem (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/getSafetyChecklistItem`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [안전점검표 항목 및 사항 저장]
export function saveSafetyChecklist (data, notify) {
  return request({
    url: '/safewizpro/impl/safetyChecklist/saveSafetyChecklist',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[안전점검표 삭제]
export function deleteSafetyChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/deleteSafetyChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// [안전점검표 항목 및 사항 삭제]
export function deleteSafetyChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/deleteSafetyChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[설비유형별 안전점검표 존재유무 확인]
export function hasSafetyChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/hasSafetyChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전점검 담당자 조회]
export function getSafetyCheckInspector (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/getSafetyCheckInspector`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전점검 담당자 조회]
export function getEqInspector (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyChecklist/getEqInspector`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [안전점검 담당자 저장]
export function saveSafetyCheckInspector (data, notify) {
  return request({
    url: '/safewizpro/impl/safetyChecklist/saveSafetyCheckInspector',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}