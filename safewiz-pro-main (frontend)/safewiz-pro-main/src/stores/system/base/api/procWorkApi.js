import request from '@/utils/axios.js'

//[공정별 작업 내용 차수 리스트 조회]
export function getPrcsRevList (processId, notify) {
  return request({
    url: `/system/base/prcs/getPrcsRev/${processId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

//[공정별 확정 차수 작업 내용 조회]
export function getPrcsCnfmWorkList (processId, notify) {
  return request({
    url: `/system/base/prcs/getPrcsCnfmWork/${processId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

//[공정/차수별 작업 내용 리스트 조회]
export function getPrcsWorkList (param, notify) {
  return request({
    url: `/system/base/prcs/getPrcsWork`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//[공정별 차수 추가]
export function insertPrcsRev (data, notify) {
  return request({
    url: `/system/base/prcs/insertPrcsRev`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[공정별 차수 수정]
export function updatePrcsRev (data, notify) {
  return request({
    url: `/system/base/prcs/updatePrcsRev`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[공정/차수별 작업 내용 추가/수정]
export function updatePrcsWorks (data, notify) {
  return request({
    url: `/system/base/prcs/updatePrcsWorks`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[공정별 차수 확정/확정취소]
export function updatePrcsRevCnfm (data, notify) {
  return request({
    url: `/system/base/prcs/updatePrcsRevCnfm`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
