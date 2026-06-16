import request from '@/utils/axios.js'

export function getClient (param, notify) {
    return request({
        url: `/system/setting/clientComp/getClient`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
       notify: { kind: notify }
    })
}

export function getCompByClient (param, notify) {
    return request({
        url: `/system/setting/clientComp/getCompByClient`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params:param,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function saveCompByClient (data, notify) {
    return request({
        url: `/system/setting/clientComp/saveCompByClient`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data:data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function deleteCompByClient (data, notify) {
    return request({
        url: `/system/setting/clientComp/deleteCompByClient`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
       notify: { kind: notify }
    })
}
