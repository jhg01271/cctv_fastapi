import request from '@/utils/axios'

export function getFiles (param, notify) {
  return request({    
    url: `/safewizpro/common/getFile/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}
export function downloadFile (param, notify) {
  return request({
    url: `/safewizpro/common/file/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}
export function downloadFileTemplate (param, notify) {
  return request({
    url: `/safewizpro/common/fileTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}
export function getNasImage (param, notify) {
  return request({
    url: `/safewizpro/common/nasImage/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

export function deleteFile (param, notify) {
  return request({
    url: `/safewizpro/common/file/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}
export function deleteFiles (param, notify) {
  return request({
    url: `/safewizpro/common/file`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}
export function uploadFiles (param, notify) {
  return request({
    url: `/safewizpro/common/file`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: param,
    notify: { kind: notify }
  })
}