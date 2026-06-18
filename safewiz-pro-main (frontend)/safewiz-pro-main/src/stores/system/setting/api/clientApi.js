import request from '@/utils/axios.js'

export function getClientList (param, notify) {
  return request({
    url: `/system/setting/client/getClient`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getClientDetail (clntId, notify) {
  return request({
    url: `/system/setting/client/getClientDetail/${clntId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertClient (data, notify) {
    return request({
        url: `/system/setting/client/insertClient`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function updateClient (clntId, data, notify) {
    return request({
        url: `/system/setting/client/${clntId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
      notify: { kind: notify }
    })
}

export function deleteClient (clntId, notify) {
    return request({
        url: `/system/setting/client/${clntId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
}

export function deleteClients (data, notify) {
    return request({
        url: `/system/setting/client/deleteClients`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}


