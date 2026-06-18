import request from '@/utils/axios.js'

export function insertOrganizationChart (data, notify) {
    return request({
        url: `/safewizpro/participation/insertOrganizationChart`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
  }
  
  //안전보건 조직구성 데이터 조회
  export function getSafetyOrgList (param, notify) {
    return request({
      url: `/safewizpro/participation/getSafetyOrgList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
  }

  //안전보건 조직구성 예시 불러오기
  export function getDataSetSafetyOrgList (param, notify) {
    return request({
      url: `/safewizpro/participation/getDataSetSafetyOrgList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
  }

  //안전보건 조직구성 데이터 저장, 수정, 삭제
  export function saveSafetyOrgList (data, notify) {
    return request({
        url: `/safewizpro/participation/saveSafetyOrgList`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
  }

  export function getOrganizationChart (param, notify) {
    return request({
      url: `/safewizpro/participation/getOrganizationChart`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
  }
  export function getOrganizationChartDetail (chartId, notify) {
    return request({
      url: `/safewizpro/participation/getOrganizationChartDetail/${chartId}`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }
  export function deleteOrganizationChart (data, notify) {
    return request({
        url: `/safewizpro/participation/deleteOrganizationChart`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
  }
  export function confirmOrganizationChart (data, notify) {
    return request({
        url: `/safewizpro/participation/confirmOrganizationChart`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
  }
  export function updateOrganizationChart (data, notify) {
    return request({
        url: `/safewizpro/participation/organizationChart/${data.chartId} `,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
  }

  export function getShOrgChartReport (data, notify) {
    let responseType = "blob"
    return request({
      url: `/safewizpro/participation/getShOrgChartReport`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      notify: { kind: notify },
      responseType: responseType
    })
  }

  //아이디로 해당 조직 ID 검색
  export function getSafetyOrgnListById (idList, notify) {
    return request({
      url: `/safewizpro/participation/getSafetyOrgnListById?idList=${idList}`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }