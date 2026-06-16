import request from '@/utils/axios.js'

// 서버 리스트 조회
export const getAiServerList = (param, isNotify) => {
  return request({
    url: `/cctv/server_crud/servers/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// 서버 상세 조회
export const getAiServer = (param, isNotify) => {
  return request({
    url: `/cctv/server_crud/server/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// 서버 정보 저장
export const saveAiServer = (param, isNotify) => {
  return request({
    url: `/cctv/server_crud/server`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}

// 서버 정보 삭제
export const deleteAiServer = (param, isNotify) => {
  return request({
    url: `/cctv/server_crud/server`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'deleted' }, //searched, saved,deleted
    data: param
  })
}
