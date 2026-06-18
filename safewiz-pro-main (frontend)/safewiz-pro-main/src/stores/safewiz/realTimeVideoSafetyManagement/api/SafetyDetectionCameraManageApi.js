import request from '@/utils/axios.js'

// CCTV 리스트 조회
export const getCCTVList = (param, isNotify) => {
  return request({
    url: `/cctv/cctv_crud/cctvs/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// CCTV 상세 조회
export const getCCTV = (param, isNotify) => {
  return request({
    url: `/cctv/cctv_crud/cctv/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

// CCTV 정보 저장
export const saveCCTV = (param, isNotify) => {
  return request({
    url: `/cctv/cctv_crud/cctv`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}

// CCTV 정보 삭제
export const deleteCCTV = (param, isNotify) => {
  return request({
    url: `/cctv/cctv_crud/cctv`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'deleted' }, //searched, saved,deleted
    data: param
  })
}
