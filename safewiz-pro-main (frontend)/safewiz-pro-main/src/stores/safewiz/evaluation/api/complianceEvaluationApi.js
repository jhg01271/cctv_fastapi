import request from '@/utils/axios.js'
//[모니터링, 성과측정 법규 준수평가표 조회]
export function getLegalComplianceEvaluationList (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getLegalComplianceEvaluationList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 법규 준수평가표 상세 조회]
export function getLegalComplianceEvaluationDetailList (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getLegalComplianceEvaluationDetailList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 법규 준수평가표 저장]
export function saveLegalComplianceEvaluation (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveLegalComplianceEvaluation',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[모니터링, 성과측정 법규 준수평가표 상세 리스트 저장]
export function saveLegalComplianceEvaluationDetail (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveLegalComplianceEvaluationDetail',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[모니터링, 성과측정 법규 준수평가표 삭제]
export function deleteLegalComplianceEvaluation (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteLegalComplianceEvaluation`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 법규 준수평가표 상세 삭제]
export function deleteLegalComplianceEvaluationDetail (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteLegalComplianceEvaluationDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// 보고서
export function getEvaluationComplianceReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/evaluation/evaluationReport/getEvaluationComplianceReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}
