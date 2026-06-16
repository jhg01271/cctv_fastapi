import request from '@/utils/axios.js'

export const saveRoi = (param, isNotify) => {
  return request({
    url: `/cctv/roi_crud/roi`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'saved' }, //searched, saved,deleted
    data: param
  })
}

// CCTV 리스트 조회(구 safewiz의 getRoiList)
export function getCCTVList(param, notify) {
  return request({
    url: `/cctv/roi_crud/rois/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify },
  });
}

export const getImage = (param, isNotify) => {
  return request({
    url: `/cctv/roi_crud/get_cctv_img/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}


