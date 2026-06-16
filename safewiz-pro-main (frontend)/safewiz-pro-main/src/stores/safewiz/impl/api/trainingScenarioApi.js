import request from '@/utils/axios.js'

export function getTrainingList (data, notify) {
  return request({
    url: `/safewizpro/impl/scenario/getTrainingList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getTrainingDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/scenario/getTrainingDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveTraining (data, notify) {
  return request({
    url: `/safewizpro/impl/scenario/saveTraining`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteTraining (data, notify) {
  return request({
    url: `/safewizpro/impl/scenario/deleteTraining`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getTrainingReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/scenario/getTrainingReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}
