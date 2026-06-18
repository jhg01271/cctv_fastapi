import request from '@/utils/axios.js'
const baseURL = '/safewizpro/planning/improvementandimplementation'

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