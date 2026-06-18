import request from '@/utils/axios.js'

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
//20241112 재해발생보고서 사고경위
export function getIncidentDetailExtent (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentDetailExtent`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241112 재해발생보고서 예방대책 의견
export function getIncidentDetailOpinion (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentDetailOpinion`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241112 재해발생보고서 사고처리
export function getIncidentDetailManage (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentDetailManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241112 재해발생보고서 진술서
export function getIncidentState (param, notify) {
  return request({
    url: `/safewizpro/improvement/getIncidentState`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//20241112 재해발생보고서 진술서
export function getIcidentDocNo (param, notify) {
  return request({
    url: `/safewizpro/improvement/getdoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 추가
export function insertIncidentReport (param, notify) {
  return request({
    url: `/safewizpro/improvement/insertIncidentReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 추가
export function insertIncidentReportDetail (param, notify) {
  return request({
    url: `/safewizpro/improvement/insertIncidentReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}


//재해발생 보고서 추가
export function insertIncidentReportManage (param, notify) {
  
  return request({
    url: `/safewizpro/improvement/insertIncidentReportManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 예방대책 및 의견 추가
export function insertIncidentReportOpinion (param, notify) {
  
  return request({
    url: `/safewizpro/improvement/insertIncidentReportOpinion`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 진술서 추가
export function insertIncidentReportStatment (param, notify) {
  
  return request({
    url: `/safewizpro/improvement/insertIncidentReportStatment`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 사용여부 수정
export function updateIncidentReportuseYn (param, notify) {
  return request({
    url: `/safewizpro/improvement/updateIncidentReportuseYn`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 수정
export function updateIncidentReport (param, notify) {
  return request({
    url: `/safewizpro/improvement/updateIncidentReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 상세 수정
export function updateIncidentReportDetail (param, notify) {
  return request({
    url: `/safewizpro/improvement/updateIncidentReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 사건처리 수정
export function UpdateIncidentReportManage (param, notify) {
  return request({
    url: `/safewizpro/improvement/UpdateIncidentReportManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}


//재해발생 보고서 대책방안 및 의견 수정
export function UpdateIncidentReportOpinion (param, notify) {
  return request({
    url: `/safewizpro/improvement/UpdateIncidentReportOpinion`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 대책방안 및 의견 수정
export function updateIncidentReportStatment (param, notify) {
  return request({
    url: `/safewizpro/improvement/updateIncidentReportStatment`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 단일 삭제
export function deleteIncidentReport (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteIncidentReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 다중 삭제
export function deleteIncidentReports (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteIncidentReports`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 리포트 출력
export function IncidentReportCombine (param) {
  console.log("param:",param)
  return request({ 
    url: '/safewizpro/improvement/IncidentReportCombine',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//재해발생 보고서 리포트 출력
export function IncidentReportPrint (param) {
  console.log("param:",param)
  return request({ 
    url: '/safewizpro/improvement/IncidentReportPrint',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//재해발생 보고서 리포트 출력
export function IncidentReportStatementPrint (param) {
  console.log('list',param);
  return request({ 
    url: '/safewizpro/improvement/IncidentReportStatementPrint',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//재해발생 보고서 리포트 출력
export function WitnessReportStatementPrint (param) {
  return request({ 
    url: '/safewizpro/improvement/WitnessReportStatementPrint',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//재해발생 보고서의 사고경위 삭제
export function deleteIncidentDetail (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteIncidentDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서의 사고처리 삭제
export function deleteIncidentHospi (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteIncidentHospi`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서의 예방대책 및 관리자의견 삭제
export function deleteIncidentOpinion (param, notify) {
  return request({
    url: `/safewizpro/improvement/deleteIncidentOpinion`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//재해발생 보고서 리포트 출력
export function IncidentReportPrintchecked (param) {
  console.log("param:",param)
  return request({ 
    url: '/safewizpro/improvement/IncidentReportPrintchecked',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//재해발생 보고서 진술서 삭제
export function DeteleStatement (param, notify) {
  console.log(param);
  return request({
    url: `/safewizpro/improvement/DeteleStatement`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}
