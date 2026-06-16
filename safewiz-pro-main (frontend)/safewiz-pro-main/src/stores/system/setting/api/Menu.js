import request from '@/utils/axios.js'

// save
export function createMenu(data, notify) {
    return request({
        url: `/system/setting/menuAdmin/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function searchMenu(param, notify) {
    return request({
        url: `/system/setting/menu/search?isWeb=true`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}
export function searchMenuAdmin(param, notify) {
    return request({
        url: `/system/setting/menuAdmin/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function treeMenu(param, notify) {
    return request({
        url: `/system/setting/menu/tree?isWeb=true`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function treeMenuAdmin(param, notify) {
    return request({
        url: `/system/setting/menuAdmin/tree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}


export function getMenu(menuId, notify) {
    return request({
        url: `/system/setting/menuAdmin/${menuId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function modifyMenu(menuId, data, notify) {
    return request({
        url: `/system/setting/menuAdmin/${menuId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function removeMenu(menuId, notify) {
    return request({
        url: `/system/setting/menuAdmin/${menuId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
