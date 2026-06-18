import request from '@/utils/axios.js'

export function getHseObjectives (data, notify) {
  return request({
    url: `/safewizpro/participation/getHseObjectives`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getHseOrganizationChart (data, notify) {
  return request({
    url: `/safewizpro/participation/getHseOrganizationChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}