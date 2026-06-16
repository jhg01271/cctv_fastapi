import request from '@/utils/axios.js'

export function getResultList (data, notify) {
  return request({
    url: `/safewizpro/impl/result/getResultList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function getResultMaster (data, notify) {
  return request({
    url: `/safewizpro/impl/result/getResultMaster`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}


export function getResultDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/result/getResultDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getValidSenarioList (data, notify) {
  return request({
    url: `/safewizpro/impl/result/getValidSenarioList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveResult (data, notify) {
  return request({
    url: `/safewizpro/impl/result/saveResult`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteResult (data, notify) {
  return request({
    url: `/safewizpro/impl/result/deleteResult`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getResultReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/result/getResultReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}

export function getResultDetailReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/result/getResultDetailReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}
