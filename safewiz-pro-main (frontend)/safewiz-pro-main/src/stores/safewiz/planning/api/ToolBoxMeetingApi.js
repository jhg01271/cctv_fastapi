import request from '@/utils/axios.js'
const baseURL = '/safewizpro/planning/toolboxmeeting'

export function searchDataApi (param, notify) {
    return request({
      url: `${baseURL}/searchData`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
}

export function searchDataDetailApi (param, notify) {
    return request({
      url: `${baseURL}/searchDataDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
}

export function searchSetting (param, notify) {
    return request({
      url: `${baseURL}/searchSetting`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
}

export function saveSetting (data, notify) {
  return request({
    url: `${baseURL}/saveSetting`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function saveDataApi (data, notify) {
  return request({
    url: `${baseURL}/saveData`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteDataApi (data, notify) {
  return request({
    url: `${baseURL}/deleteData`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getReportApi (data, notify) {
  return request({
    url: `${baseURL}/getReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}