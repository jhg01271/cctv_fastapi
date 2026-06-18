import request from '@/utils/axios.js'

export function getEduTraining (data, notify) {
  return request({
    url: `/safewizpro/support/getEduTraining`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
