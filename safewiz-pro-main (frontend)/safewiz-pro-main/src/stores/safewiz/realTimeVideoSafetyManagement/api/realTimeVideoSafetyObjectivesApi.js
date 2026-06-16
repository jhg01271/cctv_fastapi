import request from '@/utils/axios.js'

export function getrealTimeVideoSafetyObjectives (data, notify) {
  return request({
    url: `/safewizpro/realTimeVideoSafetyManagement/getRealTimeVideoSafetyObjectives`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
