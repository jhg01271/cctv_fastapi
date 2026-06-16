import request from '@/utils/axios.js'

export function getProvisionAndMgmtPPEList (data, notify) {
  return request({
    url: `/safewizpro/improvement/getProvisionAndMgmtPPEList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getProvisionAndMgmtPPEDetailList (data, notify) {
  return request({
    url: `/safewizpro/improvement/getProvisionAndMgmtPPEDetailList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}



export function insertProvisionAndMgmtPPE (data, notify) {
  return request({
    url: `/safewizpro/improvement/insertProvisionAndMgmtPPE`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


export function deleteProvisionAndMgmtPPE (data, notify) {
  return request({
    url: `/safewizpro/improvement/deleteProvisionAndMgmtPPE`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getReportProvisionAndMgmtPPE (data, notify) {
  return request({
    url: '/safewizpro/improvement/getReportProvisionAndMgmtPPE',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

// 안전보호구 품목 관리 출력
export function getReportPPEManagement (data, notify) {
  return request({
    url: '/safewizpro/improvement/getReportPPEManagement',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

// 선택된 안전보호구 품목 관리 출력
export function getReportPPEManagementchk (data, notify) {
  return request({
    url: '/safewizpro/improvement/getReportPPEManagementchk',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

export function getDatasetPPEList (data, notify) {
  return request({
    url: `/safewizpro/improvement/getDatasetPPEList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveDatasetPPE (data, notify) {
  return request({
    url: `/safewizpro/improvement/saveDatasetPPE`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteDatasetPPE (data, notify) {
  return request({
    url: `/safewizpro/improvement/deleteDatasetPPE`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}