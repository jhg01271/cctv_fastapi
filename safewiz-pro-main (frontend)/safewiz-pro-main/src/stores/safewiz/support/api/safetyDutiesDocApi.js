import request from '@/utils/axios.js'

//안전업무 구분 데이터 조회
export function getSafetyDutyType (param, notify) {
  return request({
    url: `/safewizpro/support/getSafetyDutyType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function saveSafetyDutiesDoc (data, notify) {
  return request({
    url: `/safewizpro/support/insertSafetyDutiesDoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function getSafetyDutiesDoc (param, notify) {
  return request({
    url: `/safewizpro/support/SafetyDutiesDoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getSafetyDutiesDocDataset (param, notify) {
    return request({
      url: `/safewizpro/support/SafetyDutiesDocDataset`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params : param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

export function getSafetyDutiesDocDetail (param, notify) {
  return request({
    url: `/safewizpro/support/SafetyDutiesDocDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    data: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteSafetyDutiesDoc (param, notify) {
  return request({
    url: `/safewizpro/support/deleteSafetyDutiesDoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    data: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getReport (data, notify) {
  return request({
    url: '/safewizpro/support/SafetyDutiesDocReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}