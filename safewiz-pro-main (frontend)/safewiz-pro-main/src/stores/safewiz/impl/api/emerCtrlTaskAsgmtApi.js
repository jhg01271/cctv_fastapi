import request from '@/utils/axios.js'

export function getEmergencyControlTaskAsgmtList (data, notify) {
  return request({
    url: `/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getEmergencyControlTaskAsgmtDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getOrgnChartData (data, notify) {
  return request({
    url: `/safewizpro/impl/asgmt/getOrgnChartData`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveEmergencyControlTaskAsgmt (data, notify) {
  return request({
    url: `/safewizpro/impl/asgmt/saveEmergencyControlTaskAsgmt`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteEmergencyControlTaskAsgmt (data, notify) {
  return request({
    url: `/safewizpro/impl/asgmt/deleteEmergencyControlTaskAsgmt`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function getEmergencyControlTaskAsgmtReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}
