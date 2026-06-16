import request from '@/utils/axios.js'

//인원조회, param : Object
export function getPrcs (param, notify) {
  return request({
    url: `/system/base/prcs/getPrcs`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


export function getPrcsDetail (processId, notify) {
  return request({
    url: `/system/base/prcs/getPrcsDetail/${processId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}



export function insertPrcs (data, notify) {
  return request({
    url: `/system/base/prcs/insertPrcs`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function updatePrcs (processId, data, notify) {
  return request({
    url: `/system/base/prcs/${processId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deletePrcs (processId, notify) {
  return request({
    url: `/system/base/prcs/${processId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deletePrcss (data, notify) {
  return request({
    url: `/system/base/prcs/deletePrcss`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function downloadPrcsExcelTemplate (param, notify) {
  return request({
    url: `/system/base/prcs/downloadPrcsExcelTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

export function insertPrcsExcel(data, notify) {
  return request({
      url: `/system/base/prcs/insertPrcsExcel`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}