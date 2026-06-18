import request from '@/utils/axios.js'

// save
export function upsertMasterCode (data, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/upsertMasterCode`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}
export function upsertDetailCode (data, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/upsertDetailCode`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function searchSystemCode (param, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function treeSystemCode (param, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/tree`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getSystemCode (param, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/${param.majorCd}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
       notify: { kind: notify }
    })
}

export function getSystemCodeCustom (param, notify) {
    return request({
        url: `/system/setting/systemCodeCustom/getSystemCodeDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params:param,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}

export function updateDetailCode (data, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/updateEsgDetailCode`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data:data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}



export function getSystemCodePage (param, notify) {
    return request({
        url: `/system/setting/systemCodeAdmin/getSystemCodeDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function removeMasterCode (data, notify) {
    return request({
        url: `/system/setting/masterCode/${data.majorCd}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
       notify: { kind: notify }
    })
}

export function removeDetailCode (data, notify) {
    return request({
        url: `/system/setting/detailCode/${data.minorCd}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}
