import request from '@/utils/axios.js'

// CCTV 그룹 조회
export const getGroupList = (param, isNotify) => {
  return request({
    url: `/cctv/profile_crud/profiles/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// CCTV 그룹 정보 저장
export const saveGroup = (param, isNotify) => {
  return request({
    url: `/cctv/profile_crud/profile`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}

export const getCCTVList = (param, isNotify) => {
  return request({
    url: `/cctv/cctv_crud/cctvs/${param.userCd}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// CCTV 그룹 정보 삭제
export const deleteGroup = (param, isNotify) => {
  return request({
    url: `/cctv/profile_crud/profile`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'deleted' }, //searched, saved,deleted
    data: param
  })
}

export const getMonitoringList = (param, isNotify) => {
  return request({
    url: `/cctv/pro_detail_crud/group_pro_detail/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

export const setMonitoringList = (param, isNotify) => {
  return request({
    url: `/cctv/pro_detail_crud/pro_detail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}

export const deleteMonitoringList = (param, isNotify) => {
  return request({
    url: `/cctv/pro_detail_crud/pro_detail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}
