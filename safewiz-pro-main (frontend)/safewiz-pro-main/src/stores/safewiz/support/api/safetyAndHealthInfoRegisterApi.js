import request from '@/utils/axios.js'

export function getShInfoRegisterList (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthInfoRegister/getShInfoRegisterList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function getMyShInfoRegisterList (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthInfoRegister/getMyShInfoRegisterList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function saveShInfoRegister (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthInfoRegister/saveShInfoRegister`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteShInfoRegister (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthInfoRegister/deleteShInfoRegister`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getShInfoRegisterReport (param, notify) {
  let responseType = 'blob';
  return request({
    url: `/safewizpro/support/safetyAndHealthInfoRegister/getShInfoRegisterReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  });
}

