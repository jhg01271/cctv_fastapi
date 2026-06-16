import request from '@/utils/axios.js'

export function getOrgnStatus (param, notify) {
  return request({
    url: `/safewizpro/orgstatus/getOrgnStatus`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getDetailOrgnStatus (param, notify) {
    return request({
      url: `/safewizpro/orgstatus/getOrgnStatusDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
  }

export function insertOrgnStatus (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/setOrgnStatus`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function insertOrgnStatusDetail (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/setOrgnStatusDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function insertNewSignature (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/insertNewSignature`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function updateOrgnStatusSignature (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/setSignature`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function updateOrgnStatus (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/updateOrgnStatus`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function updateOrgnStatusDetail (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/updateOrgnStatusDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteOrgnStatus (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/delOrgnStatus`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteOrgnStatusDetail (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/delOrgnStatusDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteSignature (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/delOrgnStatusSignature`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteOrgnStatusAll (data, notify) {
    return request({
        url: `/safewizpro/orgstatus/delOrgnStatusAll`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}


export function getReport (data, notify) {
    return request({
      url: '/safewizpro/orgstatus/getOrgnStatusReport',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      responseType: 'blob'
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
