import request from '@/utils/axios.js'

export function getLegalManageList(param, notify) {
  return request({
    url: `/safewizpro/planning/getLegalManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 법규 관리 팝업
export function getLegalManageTypeList(param, notify) {
  return request({
    url: `/safewizpro/planning/getLegalManageTypeList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// 법규 관리 팝업 예시
export function getDatasetLegalManageType(param, notify) {
  return request({
    url: `/safewizpro/planning/getDatasetLegalManageType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  });
}

// 안전보건법령 목록 조회
export function getLegalList(param, isNotify) {
  return request({
    url: `/safewizpro/planning/getLegalList`,
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// 법규 검토서/위험성평가 팝업 조회
export function getLegalManageListPopup(param, notify) {
  return request({
    url: `/safewizpro/planning/getLegalManageListPopup`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getLegalManageDetailMasterList(param, notify) {
  return request({
    url: `/safewizpro/planning/getLegalManageDetailMasterList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
export function getLegalManageDetail(data, notify) {
  return request({
    url: `/safewizpro/planning/getLegalManageDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getSystemCode(param, notify) {
  return request({
    url: `/system/setting/systemCodeAdmin/${param.majorCd}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}



export function getSampleLegalManage(notify) {
  return request({
    url: `/safewizpro/planning/getSampleLegalManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function saveLegalManage(data, notify) {
  return request({
    url: `/safewizpro/planning/saveLegalManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    //headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

// 차수 수정
export function confirmLegalManage(data, notify) {
  return request({
    url: `/safewizpro/planning/confirmLegalManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function saveLegalManageType(data, notify) {
  return request({
    url: `/safewizpro/planning/saveLegalManageType`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    data: data,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  });
}

export function deleteLegalManage(data, notify) {
  return request({
    url: `/safewizpro/planning/deleteLegalManage`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteLegalManageType(data, notify) {
  return request({
    url: `/safewizpro/planning/deleteLegalManageType`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    data: data,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  });
}

export function deleteLegalManageDetail(data, notify) {
  return request({
    url: `/safewizpro/planning/deleteLegalManageDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getDocNo(param, notify) {
  return request({
    url: `/safewizpro/planning/getDocNo`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getLegalNm(param, notify) {
  return request({
    url: `/safewizpro/planning/getLegalNm`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getValidLegalDivFg(param, notify) {
  return request({
    url: `/safewizpro/planning/getValidLegalDivFg`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getLegalManageReport(data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/planning/getLegalManageReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    //headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    //notify: { kind: notify },
    responseType: responseType
  })
}

export function getLegalManageReportAll(data, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/planning/getLegalManageReportCard`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    //notify: { kind: notify },
    responseType: responseType
  })
}
