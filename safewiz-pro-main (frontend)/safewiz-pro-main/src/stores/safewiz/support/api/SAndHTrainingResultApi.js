import request from '@/utils/axios.js'

// [안전보건 교육 결과 보고서 조회]
export function getTrainingResult (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/getTrainingResult`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전보건 교육 결과 보고서 상세 조회]
export function getTrainingResultReportDetail (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/getTrainingResultReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveTrainingResultReport (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/saveTrainingResultReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteTrainingResultReport (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/deleteTrainingResultReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteTrainingResultReports (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/deleteTrainingResultReports`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 교육실시 계획 보고서]
export function getTrainingResultReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/trainingResultReport/getTrainingResultReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//[전년도 및 현년도 안전보건 교육결과 조회]
export function geTtrainingResultCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/getCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[전년도 안전보건 교육결과 불러오기 저장]
export function saveTrainingResultCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/trainingResultReport/saveCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}