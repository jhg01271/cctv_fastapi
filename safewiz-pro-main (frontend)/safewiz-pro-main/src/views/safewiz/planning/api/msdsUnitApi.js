import request from '@/utils/axios.js'

export function getMsdsUnit (param, notify) {
  return request({
    url: `/safewizpro/planning/getMsdsUnit`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getMsdsUnitDataset (param, notify) {
  return request({
    url: `/safewizpro/planning/getMsdsUnitDataset`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function insertMsdsUnit (data, notify) {
  return request({
    url: `/safewizpro/planning/insertMsdsUnit`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function updateMsdsUnit (msdsUnitId, data, notify) {
  return request({
    url: `/safewizpro/planning/updateMsdsUnit/${msdsUnitId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteMsdsUnit (data, notify) {
  return request({
    url: `/safewizpro/planning/deleteMsdsUnit/${data[0].compId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
