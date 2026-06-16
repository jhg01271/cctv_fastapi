import request from '@/utils/axios.js'

export function getShCommPlanList (param, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function saveShCommPlan (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/saveShCommPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteShCommPlan (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/deleteShCommPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function deleteShCommPlanDetail (data, notify) {
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/deleteShCommPlanDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getShCommPlanReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

export function getShCommPlanDetailReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanDetailReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

// export function getHsePolicyDetail (data, notify) {
//   return request({
//     url: `/safewizpro/participation/getHsePolicyDetail`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json' },
//     params: data,
//     notify: { kind: notify }
//   })
// }

// export function getSampleHsePolicy (notify) {
//   return request({
//     url: `/safewizpro/participation/getSampleHsePolicy`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json' },
//     notify: { kind: notify }
//   })
// }

// export function saveHsePolicy (data, notify) {
//   return request({
//     url: `/safewizpro/participation/saveHsePolicy`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'multipart/form-data' },
//     data: data,
//     notify: { kind: notify }
//   })
// }

// export function deleteHsePolicy (data, notify) {
//   return request({
//     url: `/safewizpro/participation/deleteHsePolicy`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'DELETE',
//     headers: { 'Content-Type': 'application/json' },
//     data: data,
//     notify: { kind: notify }
//   })
// }

// export function getHsePolicyReport (data, notify) {
//   let responseType = "blob"
//   return request({
//     url: `/safewizpro/participation/getHsePolicyReport`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json;charset=utf8' },
//     params: data,
//     notify: { kind: notify },
//     responseType: responseType
//   })
// }
// /*************** 근로자 의견 ******************/
// export function getHsePolicySug (data, notify) {
//   return request({
//     url: `/safewizpro/participation/suggestion/getHsePolicySug`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json' },
//     params: data,
//     notify: { kind: notify }
//   })
// }
// export function saveHsePolicySug (data, notify) {
//   return request({
//     url: `/safewizpro/participation/suggestion/saveHsePolicySug`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     data: data,
//     notify: { kind: notify }
//   })
// }
// export function deleteHsePolicySug (data, notify) {
//   return request({
//     url: `/safewizpro/participation/suggestion/deleteHsePolicySug`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'DELETE',
//     headers: { 'Content-Type': 'application/json' },
//     data: data,
//     notify: { kind: notify }
//   })
// }

