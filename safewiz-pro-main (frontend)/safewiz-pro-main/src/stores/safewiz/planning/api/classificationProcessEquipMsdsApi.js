import request from '@/utils/axios.js'

// 공정 목록 조회
export function getClassProcList(param, notify) {
  const processIdList = [];
  if (param.processIdList != null) {
    param.processIdList.forEach(data => {
      processIdList.push(data.id)
    })
  }

  delete param.processIdList;
  
  return request({
    url: `/safewizpro/planning/getClassProcList?processIdList=${processIdList}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 공정 별 아이템(equip, msds) 조회
export function getPrcsItemList(writeYear, revNoList, processIdList, notify) {
  return request({
    url: `/safewizpro/planning/getPrcsItemList?writeYear=${writeYear}&revNoList=${revNoList}&processIdList=${processIdList}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

// 공정/설비/물질 구분 목록 조회
export function getClassProcEquipMsdsList(param, notify) {
  return request({
    url: `/safewizpro/planning/getClassProcEquipMsdsList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 조직도 상세 조회
export function getClassProcEquipMsdsDetail(param, notify) {
  return request({
    url: `/safewizpro/planning/getClassProcEquipMsdsDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 공정/설비/물질 구분 저장
export function saveClassProcEquipMsds(param, notify) {
  return request({
    url: `/safewizpro/planning/saveClassProcEquipMsds`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 공정/설비/물질 구분 확정 차수 변경
export function confirmClassProcs(param, notify) {
  return request({
    url: `/safewizpro/planning/confirmClassProc`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 공정/설비/물질 구분 삭제
export function deleteClassProcEquipMsds(param, notify) {
  return request({
    url: `/safewizpro/planning/deleteClassProcEquipMsds`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: param,
    notify: { kind: notify }
  })
}

// 공정/설비/물질 구분 출력물
export function getReport (data, notify) {
  let responseType = "blob"
  return request({
    url: '/safewizpro/planning/getClassProcReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}