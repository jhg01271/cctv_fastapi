import request from '@/utils/axios.js'

export function saveSAndHQualificationCertRegister (data, notify) {
  return request({
    url: `/safewizpro/support/insertSAndHQualificationCertRegister`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}


export function getSAndHQualificationCertRegister (param, notify) {
  return request({
    url: `/safewizpro/support/SAndHQualificationCertRegister`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getSAndHQualificationCertRegisterDetail (param, notify) {
  return request({
    url: `/safewizpro/support/SAndHQualificationCertRegisterDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    data: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteSAndHQualificationCertRegister (param, notify) {
  return request({
    url: `/safewizpro/support/deleteSAndHQualificationCertRegister`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    data: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getReport (data, notify) {
  return request({
    url: '/safewizpro/support/SAndHQualificationCertRegisterReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}