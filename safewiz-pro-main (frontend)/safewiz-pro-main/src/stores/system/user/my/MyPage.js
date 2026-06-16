import request from '@/utils/axios.js'


export function searchMyPage (notify) {
    return request({
        url: `/system/user/my/info`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        data: {},
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function modifyUser (data, notify) {
    return request({
        url: `/system/user/my/info`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function modifyPassword (data, notify) {
    return request({
        url: `/system/user/my/password`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function withdrawal (data, notify) {
    return request({
        url: `/system/user/my/info`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        data: data,
        headers: { 'Content-Type': 'text/plain' },
        notify: { kind: notify }
    })
}

export function searchMyInfo (notify) {
    return request({
        url: `/system/user/my/searchMyInfo`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        data: {},
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function saveMyInfo (data, notify) {
    return request({
        url: `/system/user/my/saveMyInfo`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function updateFcmToken (data, notify) {
    return request({
        url: `/system/user/my/updateFcmToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

// 토큰 관련
// user 토큰 조회
export function getUserToken (data, notify) {
    return request({
        url: `/system/user/admin/getUserToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
// user 토큰 제거
export function removeUserToken (data, notify) {
    return request({
        url: `/system/user/admin/removeUserToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

// API 전용 토큰 조회
export function getApiToken (data, notify) {
    return request({
        url: `/system/user/admin/getApiToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
// API 전용 토큰 생성
export function createApiToken (data, notify) {
    return request({
        url: `/system/user/admin/createApiToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

// API 전용 토큰 삭제 
export function removeApiToken (data, notify) {
    return request({
        url: `/system/user/admin/removeApiToken`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}