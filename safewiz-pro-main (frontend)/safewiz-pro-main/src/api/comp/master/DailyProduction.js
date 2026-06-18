import request from '@/utils/axios.js'

export function createDailyProduction (data, compId, notify) {
    return request({
        url: `/fems/ext/site/${compId}/dailyProduction/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function searchDailyProduction (param, notify) {
    return request({
        url: `/fems/ext/site/${param.compId}/dailyProduction/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getDailyProduction (id, compId, notify) {
    return request({
        url: `/fems/ext/site/${compId}/dailyProduction/${id}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function modifyDailyProduction (id, data, compId, notify) {
    return request({
        url: `/fems/ext/site/${compId}/dailyProduction/${id}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function removeDailyProduction (id, compId) {
    return request({
        url: `/fems/ext/site/${compId}/dailyProduction/${id}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
