import request from '@/utils/axios.js'

//[안전작업 허가서 조회]
export function getPermitToWork (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getPermitToWork`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전작업 허가서 상세 조회]
export function getPermitToWorkDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getPermitToWorkDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전작업 허가서 저장]
export function savePermitToWork (data, notify) {
  return request({
    url: '/safewizpro/impl/permitToWork/savePermitToWork',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[안전작업 허가서 파일 저장]
export function savePermitToWorkFiles (data, notify) {
  return request({
    url: '/safewizpro/impl/permitToWork/savePermitToWorkFiles',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[안전작업 허가서 삭제]
export function deletePermitToWorkList (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/deletePermitToWorkList`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전작업 허가서 점검사항 삭제]
export function deletePermitToWork (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/deletePermitToWork`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// 보고서
export function getPermitToWorkReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/permitToWork/getPermitToWorkReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//[점검사항 관리 조회]
export function getSafetyWorkChecklist (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getSafetyWorkChecklist`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[점검사항 관리 항목 조회]
export function getSafetyWorkChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getSafetyWorkChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[점검사항 관리 저장]
export function saveSafetyWorkChecklist (data, notify) {
  return request({
    url: '/safewizpro/impl/permitToWork/saveSafetyWorkChecklist',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[점검사항 항목 삭제]
export function deleteSafetyWorkChecklistDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/deleteSafetyWorkChecklistDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[안전기구 조회]
export function getSafetyEquipment (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getSafetyEquipment`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[안전기구 항목 조회]
export function getSafetyEquipmentDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/getSafetyEquipmentDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[점검사항 관리 저장]
export function saveSafetyEquipment (data, notify) {
  return request({
    url: '/safewizpro/impl/permitToWork/saveSafetyEquipment',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[안전기구 항목 삭제]
export function deleteSafetyEquipmentDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/permitToWork/deleteSafetyEquipmentDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}