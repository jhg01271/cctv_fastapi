import request from '@/utils/axios.js'

export function getUserList (param, notify) {
  return request({
    url: `/system/setting/user/getUser`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getUserDetail (param, notify) {
  return request({
    url: `/system/setting/user/getUserDetail/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertUser (data, notify) {
    return request({
        url: `/system/setting/user/insertUser`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function updateUser (userId, data, notify) {
    return request({
        url: `/system/setting/user/${userId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function deleteUser (data, notify) {
    return request({
      url: `/system/setting/user/${data.userId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function deleteUsers (data, notify) {
    return request({
        url: `/system/setting/user/deleteUsers`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

//인원별 사업장 매핑 테이블 데이터 저장(작성자 : 류원진, 작성일 : 2025/03/06)
export function insertHrInfoCompMap (data, notify) {
  return request({
      url: `/system/setting/user/insertHrInfoCompMap`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}

export function updateHrInfoCompMap (data, notify) {
  return request({
      url: `/system/setting/user/updateHrInfoCompMap`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
    notify: { kind: notify }
  })
}