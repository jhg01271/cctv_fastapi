import request from '@/utils/axios.js'

export function getFacilityEquipManage (data, notify) {
    return request({
      url: `/safewizpro/impl/getFacilityEquipManage`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
}

export function getFacilityEquipManageDetail (data, notify) {
    return request({
      url: `/safewizpro/impl/getFacilityEquipManageDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
}

export function setFacilityEquipManage(data, notify) {
  return request({
    url: '/safewizpro/impl/setFacilityEquipManage',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}

export function deleteFacilityEquipManage(data, notify) {
  return request({
    url: '/safewizpro/impl/deleteFacilityEquipManage',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,  // POST 요청에 body 사용
    notify: { kind: notify }
  });
}