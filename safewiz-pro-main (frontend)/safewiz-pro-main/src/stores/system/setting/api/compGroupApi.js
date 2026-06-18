import request from '@/utils/axios.js'

// save
export function createGroup (data) {
    return request({
        url: `/system/setting/compGroup/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: 'none' }
    })
}

export function modifyGroup (grpSeq, data) {
    return request({
        url: `/system/setting/compGroup/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: 'none' }
    })
}

export function removeGroup (grpSeq) {
    return request({
        url: `/system/setting/compGroup/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: 'none' }
    })
}

export function searchGroup (param, notify) {
    return request({
        url: `/system/setting/compGroup/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function searchGroupList (param) {
    return request({
        url: `/system/setting/compGroup/searchLists`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: 'none' }
    })
}

export function getGroup (grpSeq) {
    return request({
        url: `/system/setting/compGroup/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: 'none' }
    })
}

