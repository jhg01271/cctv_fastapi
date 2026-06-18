import request from '@/utils/axios.js'

export function getInvestigationFormList (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getInvestigationFormList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getFinalUseInspectionType (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getFinalUseInspectionType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function addPartner (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/insertPartner`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function saveInvestigationForm (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/saveInvestigationForm`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteInvestigationForm (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/deleteInvestigationForm`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// 비상사태 유형 관리 팝업 관련 API
export function getInspectionType (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getInspectionType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// 점검사항 관리 팝업의 점검사항 조회
export function getInspectionTypeDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getInspectionTypeDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getInspectionTypeDataset (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getInspectionTypeDataset`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    param: data,
    notify: { kind: notify }
  })
}

export function saveInspectionType (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/saveInspectionType`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// 점검사항 관리 팝업의 점검항목 삭제
export function deleteInspectionType (data, notify) {
  return request({
    url: `/safewizpro/impl/investigation/deleteInspectionType`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getInvestigationReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/investigation/getInvestigationReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

// 비상사태 유형 관리 팝업 관련 API
export function getPassScore (param, notify) {
  return request({
    url: `/safewizpro/impl/investigation/getPassScore`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  });
}

export function savePassScore (data, notify) {
  console.log("savePassScoreAPI",data)
  return request({
    url: `/safewizpro/impl/investigation/savePassScore`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify }
  })
}