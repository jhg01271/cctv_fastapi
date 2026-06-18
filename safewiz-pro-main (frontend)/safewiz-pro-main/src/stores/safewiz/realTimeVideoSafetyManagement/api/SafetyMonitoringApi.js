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

export const getMonitoringList = (param, isNotify) => {
  return request({
    url: `/cctv/pro_detail_crud/group_pro_detail/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}
