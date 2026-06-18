import request from '@/utils/axios.js'

export function getWeather (param, notify) {
  return request({
    url: '/safewizpro/utils/weather',
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    params: param,
    notify: { kind: notify }
  })
}