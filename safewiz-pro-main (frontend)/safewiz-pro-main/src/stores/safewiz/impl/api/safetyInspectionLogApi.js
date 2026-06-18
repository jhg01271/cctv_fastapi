import request from '@/utils/axios.js'

//[설비별 안전점검일지 목록록 조회]
export function getSafetyInspectionLogList (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[설비별 안전점검일지 항목 및 사항 조회]
export function getSafetyInspectionLog (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLog`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [안전점검일지 저장]
export function saveSafetyInspectionLog (data, notify) {
  return request({
    url: '/safewizpro/impl/safetyInspectionLog/saveSafetyInspectionLog',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[안전점검일지 삭제]
export function deleteSafetyInspectionLog (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/deleteSafetyInspectionLog`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getSafetyInspectionLogReportList (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogReportList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

export function getSafetyInspectionLogReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}
//[안전점검일지 설비명 설비유형명 조회 (TASK 라우터 이동용)]
export function getEquipInfo (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getEquipInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getSafetyInspectionStdEqList (param, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionStdEqList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 해당설비의 점검일자 목록 조회
export function getSafetyInspectionLogDates (data, notify) {
  return request({
    url: `/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogDates`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// export function getWHMgmtGuideRequestReport (data, notify) {
//   let responseType = "blob"
//   return request({
//     url: `/safewizpro/impl/wHMGuide/getWHMgmtGuideRequestReport`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json;charset=utf8' },
//     data: data,
//     notify: { kind: notify },
//     responseType: responseType
//   })
// }

