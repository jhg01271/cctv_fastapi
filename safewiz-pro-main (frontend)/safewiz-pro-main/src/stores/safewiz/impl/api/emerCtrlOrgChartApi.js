import request from '@/utils/axios.js'

export function getEmergencyOrgChartList (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyOrgChartList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getEmergencyTypeList (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyTypeList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getEmergencyRoleList (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyRoleList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function confirmEmergencyOrgChart (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/confirmEmergencyOrgChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function saveEmergencyOrgChart (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/saveEmergencyOrgChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteEmergencyOrgChart (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/deleteEmergencyOrgChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// 비상사태 유형 관리 팝업 관련 API
export function getEmergencyType (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function getEmergencyTypeRole (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyTypeRole`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


export function getEmergencyTypeDataset (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/getEmergencyTypeDataset`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    param: data,
    notify: { kind: notify }
  })
}
export function deleteEmergencyType (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/deleteEmergencyType`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function saveEmergencyType (data, notify) {
  return request({
    url: `/safewizpro/impl/chart/saveEmergencyType`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function getEmergencyOrgChartReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/chart/getEmergencyOrgChartReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}