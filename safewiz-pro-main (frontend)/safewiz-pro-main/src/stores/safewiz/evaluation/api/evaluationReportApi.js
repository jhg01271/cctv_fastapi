import request from '@/utils/axios.js'

//[모니터링, 성과측정 및 평가 결과서 조회]
export function getEvaluationReportList (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationReportList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 및 평가 결과서 상세 조회]
export function getEvaluationReportDetail (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 및 평가 결과서 저장]
export function saveEvaluationReport (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveEvaluationReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[모니터링, 성과측정 및 평가 결과서 삭제]
export function deleteEvaluationReportList (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteEvaluationReportList`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[모니터링, 성과측정 및 평가 결과서 상세 삭제]
export function deleteEvaluationReportListDetail (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteEvaluationReportListDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[평가항목 조회]
export function getEvaluationChecklist (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[평가항목 예시 데이터 조회]
export function getEvaluationChecklistDataSet (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDataSet`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[평가항목 조회(use_yn = y)]
export function getEvaluationChecklistByUseYn (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationChecklistByUseYn`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


//[평가사항 조회]
export function getEvaluationChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[평가사항 조회(팝업)]
export function getEvaluationChecklistDetailBySearchText (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDetailBySearchText`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}



//[예시불러오기]
export function getBaseEvaluationChecklist (data, notify) {
  return request({
      url: `/safewizpro/evaluation/monitoringManage/getBaseEvaluationChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


//[평가항목 관리 저장]
export function saveEvaluationChecklist (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveEvaluationChecklist',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[평가항목 관리 저장(팝업)]
export function saveEvaluationChecklistByPopup (data, notify) {
  return request({
    url: '/safewizpro/evaluation/monitoringManage/saveEvaluationChecklistByPopup',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[평가항목 삭제]
export function deleteEvaluationChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteEvaluationChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[평가항목 삭제(팝업)]
export function deleteEvaluationChecklist (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/deleteEvaluationChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


// 보고서
export function getEvaluationReportReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getEvaluationReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//[정보 확인 버튼 이벤트]
export function getMenuKeyInfo (data, notify) {
  return request({
    url: `/safewizpro/evaluation/monitoringManage/getMenuKeyInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}