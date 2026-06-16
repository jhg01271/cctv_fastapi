import request from '@/utils/axios.js'

export function getVoluntarySafetyInspectorListApi(params, notify) {
    return request({
        url: '/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: params,
        notify: { kind: notify },
    })
}

export function saveVoluntarySafetyInspectorListApi(params, notify) {
    return request({
        url: '/safewizpro/improvement/voluntarySafetyInspector/saveVoluntarySafetyInspectorList',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: params,
        notify: { kind: notify },
    })
}

export function deleteVoluntarySafetyInspectorApi(params, notify) {
    return request({
        url: '/safewizpro/improvement/voluntarySafetyInspector/deleteVoluntarySafetyInspector',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: params,
        notify: { kind: notify },
    })
}

export function getVoluntarySafetyInspectorReportApi(params, notify) {
    return request({
        url: '/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorReport',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: params,
        responseType: 'blob',
        notify: { kind: notify },
    })
}

export function getVoluntarySafetyInspectorReportApichk(params, notify) {
    return request({
        url: '/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorReportApichk',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: params,
        responseType: 'blob',
        notify: { kind: notify },
    })
}