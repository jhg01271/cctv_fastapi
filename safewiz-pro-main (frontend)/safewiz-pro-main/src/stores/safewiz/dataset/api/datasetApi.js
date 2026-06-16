import request from '@/utils/axios.js'

// export function getTypeofbusinessList (notify) {
//   return request({
//     url: `/safewizpro/dataset/getTypeofbusiness`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json' },
//     notify: { kind: notify }
//   })
// };
export function getTypeofbusinessList(params, notify) {
  return request({
    url: `/safewizpro/dataset/getTypeofbusiness`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  });
}

export function getTypeofEquipmentList (param, notify) {
  return request({
    url: `/safewizpro/dataset/getTypeofEquipment`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
};


// [공통 디테일 조회]
export function getSystemCode (param, notify) {
  return request({
    url: `/system/setting/systemCodeAdmin/${param.majorCd}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

// [커스텀 디테일 조회(esg 스키마)]
export function getSystemCodeCustom (param, notify) {
  return request({
    url: `/system/setting/systemCodeCustom/getSystemCodeDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    params: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

// [커스텀 디테일 저장]
export function saveEsgDetailCode (data, notify) {
  return request({
    url: `/system/setting/systemCodeCustom/saveEsgDetailCode`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

// [커스텀 디테일 삭제(완전 삭제)]
export function deleteEsgDetailCode (data, notify) {
  return request({
    url: `/system/setting/systemCodeCustom/deleteEsgDetailCode`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

