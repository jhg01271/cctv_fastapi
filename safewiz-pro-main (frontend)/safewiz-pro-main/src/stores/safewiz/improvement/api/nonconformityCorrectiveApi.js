import request from '@/utils/axios.js'

export function getNonParticipation (param, notify) {
  return request({
    url: `/safewizpro/improvement/getNonParticipation`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    params: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

//아차사고 보고서 조회
export function getNearMissReport (param, notify) {
  return request({
    url: `/safewizpro/improvement/getNearMissReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 디테일 조회
export function getNearMissReportDetail (param, notify) {
  return request({
    url: `/safewizpro/improvement/getNearMissReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 추가
export function insertNearMissReport (param, notify) {
  
  return request({
    url: `/safewizpro/improvement/insertNearMissReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 수정
export function updateNearMissReport (param, notify) {
  return request({
    url: `/safewizpro/improvement/updateNearMissReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 삭제
export function deleteNearMissReport (param, notify) {
  console.log("파람",param)
  return request({
    url: `/safewizpro/improvement/deleteNearMissReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 다중 삭제
export function deleteNearMissReports (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteNearMissReports`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//아차사고 보고서 리포트 출력
export function printNearMissReports (param, notify) {
  return request({ 
    url: '/safewizpro/improvement/printNearMissReports',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: 'blob'
  })
}


//아차사고 보고서 조치 조직장의 id 및 이름 가져오기
export function getOrgnHead (param, notify) {
  return request({
    url: `/safewizpro/improvement/getOrgnHead`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241111 재해발생보고서 조회
export function getIncidentList (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241112 재해발생보고서 상세조회
export function getIncidentDetailList (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentDetailReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
