import request from '@/utils/axios.js'

//[추진 계획(월별) 및 실적 조회]
export function getActionMonthly (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/getActionMonthly`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//[추진 계획(월별) 및 실적 상세 조회]
export function getActionMonthlyDetail (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/${param.detailItemId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


//[세부항목 수정]
export function saveActionDetailItem (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/saveActionDetailItem`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
//[추진 계획(월별) 및 실적 등록]
export function saveActionMonthly (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/saveActionMonthly`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


//[추진 계획(월별) 및 실적 항목 일괄 삭제]
export function deleteDetailItems (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/deleteDetailItems`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[추진 계획(월별) 및 실적 일괄 삭제]
export function deleteActionMonthlys (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/deleteActionMonthlys`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function getDetailItem (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlanDetail/getDetailItem`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getDetailedActionPlanReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/participation/actionPlan/getDetailedActionPlanReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}