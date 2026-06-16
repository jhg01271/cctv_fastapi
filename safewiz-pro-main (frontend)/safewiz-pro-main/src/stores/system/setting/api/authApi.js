import request from '@/utils/axios.js'

// save
export function saveAuth (data, notify) {
    return request({
        url: `/system/setting/auth/save`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function saveAuthGroup (data, notify) {
    return request({
        url: `/system/setting/auth/saveGroup`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function saveAuthMember (data, notify) {
    return request({
        url: `/system/setting/auth/saveMember`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function refresh() {
    return request({
      url: `/igns/auth/refresh`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: 'none' }
    })
  }

export function saveGroupAuth (data, notify) {
    return request({
        url: `/system/setting/auth/saveGroup`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function removeAuth (grpSeq, notify) {
    return request({
        url: `/system/setting/auth/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function searchAuth (param, notify) {
    return request({
        url: `/system/setting/auth/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}
export function treeAuth (param, notify) {
    return request({
        url: `/system/setting/auth/tree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}
export function groupTreeAuth (param, notify) {
    return request({
        url: `/system/setting/auth/groupTree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function userTreeAuth (param, notify) {
    return request({
        url: `/system/setting/auth/userTree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getAuth (menuSeq, notify) {
    return request({
        url: `/system/setting/auth/${menuSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

