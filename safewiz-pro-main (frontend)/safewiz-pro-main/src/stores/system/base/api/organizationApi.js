import request from '@/utils/axios.js'

export function getOrganization (param, notify) {
  return request({
    url: `/system/base/organization/getOrganization`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


export function getOrganizationDetail (orgnId, notify) {
  return request({
    url: `/system/base/organization/getOrganizationDetail/${orgnId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertOrganization (data, notify) {
    return request({
        url: `/system/base/organization/insertOrganization`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function insertOrganizationChart (data, notify) {
  return request({
      url: `/system/base/organization/insertOrganizationChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}
export function getOrganizationChart (param, notify) {
  return request({
    url: `/system/base/organization/getOrganizationChart`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
export function getOrganizationChartDetail (chartId, notify) {
  return request({
    url: `/system/base/organization/getOrganizationChartDetail/${chartId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function updateOrganization (orgnId, data, notify) {
  return request({
      url: `/system/base/organization/${orgnId} `,
      meta: { apiVersion: '1.0.0' },
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function updateOrganizationChart (data, notify) {
  return request({
      url: `/system/base/organizationChart/${data.chartId} `,
      meta: { apiVersion: '1.0.0' },
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function confirmOrganizationChart (data, notify) {
  return request({
      url: `/system/base/confirmOrganizationChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function deleteOrganization (orgnId, notify) {
  return request({
      url: `/system/base/organization/${orgnId}`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
  })
}

export function deleteOrganizations (data, notify) {
  return request({
      url: `/system/base/organization/deleteOrganizations`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}
export function deleteOrganizationChart (data, notify) {
  return request({
      url: `/system/base/organization/deleteOrganizationChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function getOrgChartReport (data, notify) {
  let responseType = "blob"
  return request({
    url: `/system/base/organization/getOrgChartReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: responseType
  })
}

//엑셀 양식 다운로드
export function downloadOrgnExcelTemplate (param, notify) {
  return request({
    url: `/system/base/organization/downloadOrgnExcelTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

//엑셀 업로드
export function insertOrgnExcel(data, notify) {
  return request({
      url: `/system/base/organization/insertOrgnExcel`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}