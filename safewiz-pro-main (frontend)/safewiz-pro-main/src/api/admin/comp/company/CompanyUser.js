import request from '@/utils/axios.js'

export function saveCompanyUser (data, notify) {
    return request({
        url: `/admin/comp/company/save`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function removeCompanyUser (userId, notify) {
    return request({
        url: `/admin/comp/company/${userId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function searchCompanyUser (param, notify) {
    return request({
        url: `/admin/comp/company/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function treeCompanyUser (param, notify) {
    return request({
        url: `/admin/comp/company/tree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function searchByCompIdAndUserId (param, notify) {
    return request({
        url: `/admin/comp/company/searchByCompIdAndUserId`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}