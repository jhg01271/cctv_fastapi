import request from '@/utils/axios.js'

export function getMonitoringCtrl (data, notify) {
  return request({
    url: `/safewizpro/evaluation/getMonitoringCtrl`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
