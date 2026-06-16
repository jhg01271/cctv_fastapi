import request from '@/utils/axios.js'

export function modifyContract (compId, contractId, data, notify) {
    return request({
        url: `/admin/fems/factory/site/${compId}/contract/${contractId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function getContract (compId, notify) {
    return request({
        url: `/admin/fems/factory/site/${compId}/contarct/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function createContract (compId, data, notify) {
    return request({
        url: `/admin/fems/factory/site/${compId}/contract/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function modifyPreferences (compId, data, notify) {
    return request({
        url: `/admin/fems/factory/site/${compId}/preferences`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function getPreferences (compId) {
    return request({
        url: `/admin/fems/factory/site/${compId}/preferences`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

