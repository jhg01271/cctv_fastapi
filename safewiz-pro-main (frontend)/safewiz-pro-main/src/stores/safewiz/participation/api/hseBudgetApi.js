import request from '@/utils/axios.js'

//[조직별 실적 및 예산 조회]
export function getActionPerformance (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPerformance/getActionPerformance`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getActionPerformanceDetail(param, notify) {
  return request({
    url: `/safewizpro/participation/actionPerformance/getActionPerformanceDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function saveActionPerformance(data, notify) {
  return request({
    url: `/safewizpro/participation/actionPerformance/saveActionPerformance`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    //headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteActionPerformance(data, notify) {
  return request({
    url: `/safewizpro/participation/actionPerformance/deleteActionPerformance`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteActionPerformanceDetail(data, notify) {
  return request({
    url: `/safewizpro/participation/actionPerformance/deleteActionPerformanceDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function getHseBudgetReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/participation/actionPerformance/getHseBudgetReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}
