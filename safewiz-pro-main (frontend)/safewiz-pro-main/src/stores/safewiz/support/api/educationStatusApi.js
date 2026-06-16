import request from '@/utils/axios.js'

export function getEducationStatus (data, notify) {
  return request({
    url: `/safewizpro/support/getEducationStatus`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
