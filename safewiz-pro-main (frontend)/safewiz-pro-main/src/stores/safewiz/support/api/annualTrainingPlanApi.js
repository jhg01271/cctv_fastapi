import request from '@/utils/axios.js'

//[과목/과정 관리 팝업 데이터 조회]
export function getSubjectsMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getSubjectsMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[과목/과정 관리 팝업 예시 데이터 조회]
export function getDataSetSubjectsMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getDataSetSubjectsMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[과목/과정 관리 데이터 저장]
export function saveSubjectsMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/saveSubjectsMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[과목/과정 관리 데이터 삭제(사용안함)]
export function deleteSubjectsMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/deleteSubjectsMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


//[교육기관 관리 팝업 데이터 조회]
export function getEduMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getEduMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[교육기관 관리 팝업 예시 데이터 조회]
export function getDataSetEduMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getDataSetEduMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[교육기관 관리 데이터 저장]
export function saveEduMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/saveEduMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[교육기관 관리 데이터 삭제(사용안함)]
export function deleteEduMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/deleteEduMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[강사 관리 팝업 데이터 조회]
export function getInstMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getInstMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[강사 관리 팝업 예시 데이터 조회]
export function getDataSetInstMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getDataSetInstMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[강사 관리 데이터 저장]
export function saveInstMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/saveInstMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[강사 관리 데이터 삭제(사용안함)]
export function deleteInstMngList (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/deleteInstMngList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 조회]
export function getAnnualTrainingPlan (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getAnnualTrainingPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 저장]
export function saveAnnualTrainingPlan (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/saveAnnualTrainingPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 삭제]
export function deleteAnnualTrainingPlan (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/deleteAnnualTrainingPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 일괄 삭제]
export function deleteAnnualTrainingPlans (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/deleteAnnualTrainingPlans`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전보건 연간교육 계획 보고서]
export function getAnnualReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/getAnnualReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//[전년도 및 현년도 안전보건 연간교육 조회]
export function getAnnualTrainingCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/getCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[전년도 안전보건 연간교육 불러오기 저장]
export function saveAnnualTrainingCurrentAndPreviousYear (data, notify) {
  return request({
    url: `/safewizpro/support/annualTrainingPlan/saveCurrentAndPreviousYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}