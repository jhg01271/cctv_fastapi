import request from '@/utils/axios.js'

// save
export function createCompCode (data, notify) {
    return request({
        url: `/admin/comp/master/compCode/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function modifyCompCode (compCodeSeq, data, notify) {
    return request({
        url: `/admin/comp/master/compCode/${compCodeSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function removeCompCode (compCodeSeq, notify) {
    return request({
        url: `/admin/comp/master/compCode/${compCodeSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function searchCompCode (param, notify) {
    return request({
        url: `/admin/comp/master/compCode/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function treeCompCode (param, notify) {
    return request({
        url: `/admin/comp/master/compCode/tree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getCompCode (compCodeSeq, notify) {
    return request({
        url: `/admin/comp/master/compCode/${compCodeSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

