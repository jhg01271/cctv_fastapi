import request from '@/utils/axios.js'

//[문서관리 조회]
export function getManageDoc (data, notify) {
  return request({
    url: `/safewizpro/support/manageDoc/getManageDoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[문서관리 상세 조회]
export function getManageDocDetail (data, notify) {
  return request({
    url: `/safewizpro/support/manageDoc/getManageDocDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

//[문서관리 저장]
export function saveManageDoc (data, notify) {
  return request({
    url: '/safewizpro/support/manageDoc/saveManageDoc',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

//[문서관리 삭제]
export function deleteManageDoc (data, notify) {
  return request({
    url: `/safewizpro/support/manageDoc/deleteManageDoc`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[문서관리 일괄 삭제]
export function deleteManageDocs (data, notify) {
  return request({
    url: `/safewizpro/support/manageDoc/deleteManageDocs`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
