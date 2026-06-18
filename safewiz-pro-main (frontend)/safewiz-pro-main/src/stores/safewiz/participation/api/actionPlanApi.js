import request from '@/utils/axios.js'

//[전사 목표 및 중점 추진사항 문서 조회]
export function getActPlanMaster (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getActPlanMaster`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//[전사 목표 및 중점 추진사항 조회]
export function getActPlan (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getActPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//[전사 목표 및 중점 추진사항 상세 조회]
export function getActPlanDetail (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getActPlanDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


//[전사 목표 및 중점 추진사항 수정/등록]
export function saveActPlan (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/saveActPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[전사 목표 및 중점 추진사항 삭제]
export function deleteActPlan (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/deleteActPlan`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//[전사 목표 다수 삭제]
export function deleteActPlans (data, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/deleteActPlans`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


//[전사 목표 리스트 조회]
export function getObjective (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getObjective`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


export function getActionObjective (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getActionObjective`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getActionObjectiveOrgnList (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getActionObjectiveOrgnList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getSafetyHealthObjOrgnList (param, notify) {
  return request({
    url: `/safewizpro/participation/actionPlan/getSafetyHealthObjOrgnList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getActionObjectiveReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/participation/actionPlan/getActionObjectiveReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}

