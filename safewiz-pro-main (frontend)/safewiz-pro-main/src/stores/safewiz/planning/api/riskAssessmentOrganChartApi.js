import request from '@/utils/axios.js'


// 위험성평가 조직 역할 구성 데이터 조회
export function getRiskOrgList(param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskOrgList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 위험성평가 조직 역할 구성 예시 데이터 조회
export function getDataSetRiskOrgList(param, notify) {
  return request({
    url: `/safewizpro/planning/getDataSetRiskOrgList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 위험성평가 조직 역할 구성 데이터 저장, 수정, 삭제
export function saveRiskOrgList(param, notify) {
  return request({
      url: `/safewizpro/planning/saveRiskOrgList`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: param,
    notify: { kind: notify }
  })
}


// 인원 조회
export function getMembers(param, notify) {
  return request({
    url: `/safewizpro/planning/getMembers`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 조직도 목록 조회
export function getRiskAssessmentOrganChartList(param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskAssessmentOrganChartList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 조직도 상세 조회
export function getRiskAssessmentOrganChartDetail (chartId, notify) {
  return request({
    url: `/safewizpro/planning/getRiskAssessmentOrganChartDetail/${chartId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

// 조직도 insert, update
export function saveRiskAssessmentOrganChart(param, notify) {
  return request({
      url: `/safewizpro/planning/saveRiskAssessmentOrganChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: param,
    notify: { kind: notify }
  })
}

// 조직도 확정여부 수정
export function confirmRiskAssessmentOrganChart(param, notify) {
  return request({
    url: `/safewizpro/planning/confirmRiskAssessmentOrganChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 조직도 삭제
export function deleteRiskAssessmentOrganChart (data, notify) {
  return request({
      url: `/safewizpro/planning/deleteRiskAssessmentOrganChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function getOrganization (param, notify) {
  return request({
    url: `/system/base/organization/getOrganization`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function insertOrganization (data, notify) {
    return request({
        url: `/system/base/organization/insertOrganization`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function updateOrganization (orgnId, data, notify) {
  return request({
      url: `/system/base/organization/${orgnId} `,
      meta: { apiVersion: '1.0.0' },
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function getRiskAssessmentOrganChartReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/planning/getRiskAssessmentOrganChartReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}