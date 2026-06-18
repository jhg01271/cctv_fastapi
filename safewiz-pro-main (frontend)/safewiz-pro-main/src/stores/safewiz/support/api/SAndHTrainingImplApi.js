import request from '@/utils/axios.js'

//[교육장소 관리 팝업 데이터 조회]
export function getLocMngList (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getLocMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[교육장소 관리 팝업 예시 데이터 조회]
export function getDataSetLocMngList (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getDataSetLocMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[교육장소 관리 데이터 저장]
export function saveLocMngList (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/saveLocMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[교육장소 관리 데이터 삭제(사용안함)]
export function deleteLocMngList (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/deleteLocMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// [안전보건 연간교육 계획 조회]
export function getTrainingPlanImpl (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getTrainingPlanImpl`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 상세 조회]
export function getTrainingPlanImplDetail (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getTrainingPlanImplDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveTrainingPlanImpl (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/saveTrainingPlanImpl`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteTrainingPlanImpl (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/deleteTrainingPlanImpl`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteTrainingPlanImpls (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/deleteTrainingPlanImpls`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 교육실시 계획 보고서]
export function getTrainingPlanImplReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getTrainingPlanImplReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//[전년도 및 현년도 안전보건 교육실시 조회]
export function getTrainingPlanImplCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/getCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[전년도 안전보건 연간교육 교육실시 저장]
export function saveTrainingPlanImplCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/trainingPlanImpl/saveCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}