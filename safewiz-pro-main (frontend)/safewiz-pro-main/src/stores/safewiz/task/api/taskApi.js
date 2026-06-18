import request from '@/utils/axios.js'

export function getTask(param, notify) {
  return request({
    url: `/safewizpro/task/getTask`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function updateTaskChk(data, notify) {
  return request({
    url: `/safewizpro/task/updateTaskChk`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}