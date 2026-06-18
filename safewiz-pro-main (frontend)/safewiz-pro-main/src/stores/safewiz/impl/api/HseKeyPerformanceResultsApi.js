import request from '@/utils/axios.js'

// 메인
export function getHsePerformance(param, notify) {
  return request({
    url: `/safewizpro/impl/getHsePerformance`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 메인
export function getSafetyAndHealthObjectives(param, notify) {
  return request({
    url: `/safewizpro/impl/getSafetyAndHealthObjectives`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 상세
export function getSafetyAndHealthObjectivesDetail(param, notify) {
  return request({
    url: `/safewizpro/impl/getSafetyAndHealthObjectivesDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 저장
export function saveHseKeyPerformanceResult(data) {
  return request({
    url: `/safewizpro/impl/saveHseKeyPerformanceResult`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: true }
  })
}

// 상세
export function delHseKeyPerformanceResult(data) {
  return request({
    url: `/safewizpro/impl/delHseKeyPerformanceResult`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: true }
  })
}

//출력물
export function getHseKeyPerformanceResultReport (data, notify) {
  return request({
    url: `/safewizpro/impl/getHseKeyPerformanceResultReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}

export function getHseKeyPerformanceResultReportList (data, notify) {
  return request({
    url: `/safewizpro/impl/getHseKeyPerformanceResultReportList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}