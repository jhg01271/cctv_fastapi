import request from '@/utils/axios.js'

export function getCompList(param,notify) {
  return request({
    url: `/system/setting/comp/getComp`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getCompListByHr(param,notify) {
    return request({
        url: `/system/setting/comp/getCompByHr`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getCompListByClnt(param,notify) {
    return request({
        url: `/system/setting/comp/getCompByClnt`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

//유저 ID로 해당 유저의 사업장 가져오기
export function getCompListByUserId(param,notify) {
  return request({
      url: `/system/setting/comp/getCompListByUserId`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
  })
}

export function getCompDetail (compId, notify) {
  return request({
    url: `/system/setting/comp/getCompDetail/${compId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertComp (data, notify) {
    return request({
        url: `/system/setting/comp/insertComp`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function updateComp (compId, data, notify) {
    return request({
        url: `/system/setting/comp/${compId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function deleteComp (compId, data, notify) {
    return request({
        url: `/system/setting/comp/${compId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify}
    })
}

export function deleteComps (data, notify) {
    return request({
        url: `/system/setting/comp/deleteComps`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}