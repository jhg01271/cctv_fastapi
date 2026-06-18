import request from '@/utils/axios.js'


export function searchAccesslog (param, notify) {
    return request({
        url: `/my/accesslog/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params: param,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function removeAccesslog (refreshToken, notify) {
    return request({
        url: `/my/accesslog/${refreshToken}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'text/plain' },
        notify: { kind: notify }
    })
}