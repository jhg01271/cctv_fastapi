import request from '@/utils/axios.js'

export function getRisksAndOpp (param, notify) {
  return request({
    url: `/system/base/partner/getPartner`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getRisksAndOppDetail (partCompId, notify) {
  return request({
    url: `/system/base/partner/getPartnerDetail/${partCompId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

// 유해 위험 근로자의견 조회
export function getWorkersOpinionsOnHazards (param, notify) {
  return request({
    url: `/safewizpro/planning/getWorkersOpinionsOnHazards`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 유해 위험 근로자의견 상세조회
export function getWorkersOpinionsOnHazardsDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getWorkersOpinionsOnHazardsDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 유해 위험 근로자의견 사용자 별 상세조회
export function getWorkerHr (param, notify) {
  return request({
    url: `/safewizpro/planning/getWorkerHr`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 유해 위험 근로자의견 리포트
export function getReport (data, notify) {
  return request({
    url: '/safewizpro/planning/getWorkerReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

// 유해 위험 근로자의견 상세 리포트
export function getReportDetail (data, notify) {
  return request({
    url: '/safewizpro/planning/getWorkerReportDetail',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

export function addRisksAndOpp (data, notify) {
  return request({
    url: `/safewizpro/planning/addRisksAndOpp`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function updateRisksAndOpp (msdsId, data, notify) {
  return request({
    url: `/safewizpro/planning/updateRisksAndOpp/${msdsId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function setWorkerOpinionsOnHazardsDetail (data, notify) {
  return request({
      url: `/safewizpro/planning/setWorkerOpinionsOnHazardsDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  })
}

export function deleteRisksAndOpp (msdsId, notify) {
  return request({
    url: `/safewizpro/planning/deleteRisksAndOpp/${msdsId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteRisksAndOpps (data, notify) {
  return request({
    url: `/safewizpro/planning/deleteRisksAndOpps`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteWorkerOpinonsOnHarzards (data, notify) {
  return request({
    url: `/safewizpro/planning/deleteWorkerOpinonsOnHarzards`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteWorkerOpinonsOnHarzardsDetail (data, notify) {
  return request({
    url: `/safewizpro/planning/deleteWorkerOpinonsOnHarzardsDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}