import request from '@/utils/axios.js'
// 평가기준표 메인조회
export function getRiskMain (param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskMain`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 평가기준표 메인조회
export function getRiskAndOpsAsmtCriteria (param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskAndOpsAsmtCriteria`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 차수 max 조회
export function getMaxAsmtCriteria (param, notify) {
  return request({
    url: `/safewizpro/planning/getMaxAsmtCriteria`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 평가기준표 디테일조회
export function getRiskAndOpsAsmtCriteriaDetailList (param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskAndOpsAsmtCriteriaDetailList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
// 평가기준표 데이터셋조회
export function getDatasetAsmtList (param, notify) {
  return request({
    url: `/safewizpro/planning/getDatasetAsmtList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
export function getRisksAndOpportunities (param, notify) {
  return request({
    url: `/safewizpro/planning/getRisksAndOpportunities`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getRiskDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getRiskDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
export function getOppDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getOppDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
export function getParDetail (param, notify) {
  return request({
    url: `/safewizpro/planning/getParDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function saveRiskAndOpsAsmtCriteria(data) {
    return request({
      url: `/safewizpro/planning/saveRiskAndOpsAsmtCriteria`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}

export function saveMainConfirm(data) {
    return request({
      url: `/safewizpro/planning/saveMainConfirm`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}

export function saveRisksAndOpportunities(data) {
    return request({
      url: `/safewizpro/planning/saveRisksAndOpportunities`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}

export function deleteRiskAndOpsAsmtCriteriaMain(data) {
    return request({
      url: `/safewizpro/planning/deleteRiskAndOpsAsmtCriteriaMain`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}
export function deleteRiskAndOpsAsmtCriteriaDetail(data) {
    return request({
      url: `/safewizpro/planning/deleteRiskAndOpsAsmtCriteriaDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}



export function deleteRisksAndOpportunitesMain(data) {
    return request({
      url: `/safewizpro/planning/deleteRisksAndOpportunitesMain`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}
export function deleteRisksAndOpportunitesDetail(data) {
    return request({
      url: `/safewizpro/planning/deleteRisksAndOpportunitesDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: false }
    })
}

// 평가기준표 메인조회
export function getAsmtApi (param, notify) {
  return request({
    url: `/safewizpro/planning/getAsmtApi`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//출력물
export function getRisksAndOpportunitiesReport (param, notify) {
  console.log('param', param)
  return request({
    url: `/safewizpro/planning/getRisksAndOpportunitiesReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}

//출력물(card)
export function getRisksAndOpportunitiesCardReport (param, notify) {
  console.log('param', param)
  return request({
    url: `/safewizpro/planning/getRisksAndOpportunitiesCardReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    responseType: 'blob'
  })
}
