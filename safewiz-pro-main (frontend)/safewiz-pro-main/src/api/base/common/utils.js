import request from '@/utils/axios'
/**
 * 👉 Business Utils
 *  작성자 : 이경수
 *  일자 : 2024-10-24
 *  
 *  1. 서명 관련 내용 : 서명자/서명 조회, 추가, 수정, 삭제
 * 
 */
//고객사의 사업장 조회
export function searchCompList (param, notify) {
  return request({
    url: `/safewizpro/utils/searchCompList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 서명자 정보 조회 - 사용자 및 이미지
export function searchApprovalInfo (param, notify) {
  return request({
    url: `/safewizpro/utils/searchApprovalInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 서명자 정보 조회 - 단순 서명정보
export function searchApprovalInfoSimple (param, notify) {
  return request({
    url: `/safewizpro/utils/searchApprovalInfoSimple`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 서명자 정보 (단건) 저장
export function insertApprovalInfo (param, notify) {
  return request({
    url: `/safewizpro/utils/insertApprovalInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 신규 문서 서명자 목록 정보 저장
export function insertApprovalInfoList (param, notify) {
  return request({
    url: `/safewizpro/utils/insertApprovalInfoList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}
// 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌
export function updateTaskUseYnApi (param, notify) {
  return request({
    url: `/safewizpro/utils/updateTaskUseYn`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// export function insertApprovalInfoListMulti (param, notify) {
//   return request({
//     url: `/safewizpro/utils/insertApprovalInfoListMulti`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data: param,
//     notify: { kind: notify }
//   })
// }

// 서명자 정보 삭제
export function deleteApprovalInfo (param, notify) {
  return request({
    url: `/safewizpro/utils/deleteApprovalInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}
// 서명자 정보 삭제(ISignature 에서는 삭제 없이 인원 정보를 제외함)
export function nullUpdateApprovalInfo (param, notify) {
  return request({
    url: `/safewizpro/utils/nullUpdateApprovalInfo`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}
// 서명자 서명 등록 안에 Task 정보가 없어서 수정
// export function updateApprovalInfo (param, notify) {
//   return request({
//     url: `/safewizpro/utils/updateApprovalInfo`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data: param,
//     notify: { kind: notify }
//   })
// }
// 서명자 서명 등록
export function updateApprovalInfoSign (param, notify) {
  return request({
    url: `/safewizpro/utils/updateApprovalInfoSign`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 서명자 서명 등록 취소
export function updateApprovalInfoSignCancel (param, notify) {
  return request({
    url: `/safewizpro/utils/updateApprovalInfoSignCancel`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 사용자 서명 등록
export function updateUserSignature (param, notify) {
  return request({
    url: `/safewizpro/utils/updateUserSignature`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

//위치정보로 날씨 조회
export function getWeather (params) {
  return request({
    url: '/safewizpro/utils/weather',
    method: 'GET',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    params: params,
    meta: { apiVersion: '1.0.0' }
  })
}