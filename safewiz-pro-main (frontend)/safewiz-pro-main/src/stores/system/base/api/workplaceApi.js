import request from '@/utils/axios.js'

export function getWp (param, notify) {
  return request({
    url: `/system/base/wp/getWp`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getWpDetail (workplaceId, notify) {
  return request({
    url: `/system/base/wp/getWpDetail/${workplaceId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertWp (data, notify) {
  return request({
    url: `/system/base/wp/insertWp`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function updateWp (workplaceId, data, notify) {
  return request({
    url: `/system/base/wp/${workplaceId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteWp (workplaceId, notify) {
  return request({
    url: `/system/base/wp/${workplaceId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteWps (data, notify) {
  return request({
    url: `/system/base/wp/deleteWps`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function downloadWpExcelTemplate (param, notify) {
  return request({
    url: `/system/base/wp/downloadWpExcelTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

export function insertWpExcel(data, notify) {
  return request({
      url: `/system/base/wp/insertWpExcel`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}