import request from '@/utils/axios.js'

//[모니터링, 성과측정 성과평가표 조회]
export function getEvaluationReportPerformance (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationReportPerformance`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 성과평가표 저장]
export function saveEvaluationReportPerformance (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveEvaluationReportPerformance',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

// [성과평가표 보고서]
export function getEvaluationPerformanceReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/evaluation/evaluationReport/getEvaluationPerformanceReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}
