import request from '@/utils/axios.js'

export function getPartner (param, notify) {
  return request({
    url: `/system/base/partner/getPartner`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getPartnerDetail (partCompId, notify) {
  return request({
    url: `/system/base/partner/getPartnerDetail/${partCompId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}


export function addPartner (data, notify) {
    return request({
        url: `/system/base/partner/insertPartner`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function updatePartner (partCompId, data, notify) {
    return request({
        url: `/system/base/partner/${partCompId} `,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function deletePartner (partCompId, notify) {
    return request({
        url: `/system/base/partner/${partCompId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function deletePartners (data, notify) {
    return request({
        url: `/system/base/partner/deletePartners`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

//엑셀 양식 다운로드
export function downloadPartnerExcelTemplate (param, notify) {
  return request({
    url: `/system/base/partner/downloadPartnerExcelTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

//엑셀 업로드
export function insertPartnerExcel(data, notify) {
  return request({
      url: `/system/base/partner/insertPartnerExcel`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}

// 팝업 관련
export function getPartCompItemDataset(data, notify) {
  return request({
      url: `/system/base/partner/getPartCompItemDataset`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
  });
}
export function getSamplePartCompItemDataset(data, notify) {
  return request({
      url: `/system/base/partner/getSamplePartCompItemDataset`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
  });
}

export function savePartCompItemDataset(data, notify) {
  return request({
      url: `/system/base/partner/savePartCompItemDataset`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}

export function deletePartCompItemDataset (data, notify) {
  return request({
      url: `/system/base/partner/deletePartCompItemDataset`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}