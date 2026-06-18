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

// 마스터 조회
export function getSafetyAndHealthInfoSurvey (param, notify) {
  return request({
    url: `/safewizpro/planning/getSafetyAndHealthInfoSurvey`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 상세 조회 
export function getSafetyAndHealthInfoSurveyDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getSafetyAndHealthInfoSurveyDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 공정 선택 시 조회
export function getProcessDataDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getProcessDataDetail`,
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

// 안전보건정보 조사 마스터추가
export function addSafetyAndHealthInfoSurvey (data, notify) {
  return request({
      url: `/safewizpro/planning/setSafetyAndHealthInfoSurvey`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  })
}

// 안전보건정보 조사 상세추가
export function addSafetyAndHealthInfoSurveyDetail (data, notify) {
  return request({
      url: `/safewizpro/planning/setSafetyAndHealthInfoSurveyDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
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

export function confirmSafetyAndHealthInfoSurvey (data, notify) {
  return request({
      url: `/safewizpro/planning/confirmSafetyAndHealthInfoSurvey`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  })
}

export function deleteSafetyAndHealthInfoSurvey (data, notify) {
  return request({
      url: `/safewizpro/planning/delSafetyAndHealthInfoSurvey`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  })
}

export function deleteSafetyAndHealthInfoSurveyDetail (data, notify) {
  return request({
      url: `/safewizpro/planning/delSafetyAndHealthInfoSurveyDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  })
}

export function getReport (data) {
  return request({
    url: '/safewizpro/planning/getSafetyAndHealthInfoSurveyReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}

export function getReports (data) {
  return request({
    url: '/safewizpro/planning/getSafetyAndHealthInfoSurveyReports',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}