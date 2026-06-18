import request from '@/utils/axios.js'

export const getEventGroupHistoryList = (param, isNotify) => {
  return request({
    url: `/cctv/ce/camera_events_by_group1`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' }, //searched, saved, deleted
    data: param
  })
}

// 안전 감지 이력 리스트 조회
export const getEventHistoryList = (param, isNotify) => {
  return request({
    url: `/cctv/ce/camera_events1`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' }, //searched, saved, deleted
    data: param
  })
}

export const getEventFiles = (param, isNotify) => {
  return request({
    url: `/cctv/ce/camera_events_get_file`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' }, //searched, saved, deleted
    data: param
  })
}

export const setEventRemark = (param, isNotify) => {
  return request({
    url: `/cctv/ce/camera_events_remark`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved, deleted
    data: param
  })
}