import request from '@/utils/axios.js'

export function saveSafetyMgmt(data, notify) {
    return request({
        url: `/safewizpro/improvement/saveSafetyMgmt`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
        notify: { kind: notify }
    });
}

export function insertSafetyMgmtOrgn(data, notify) {
    return request({
        url: `/safewizpro/improvement/insertSafetyMgmt`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function getSafetyMgmts(param, notify) {
    return request({
        url: `/safewizpro/improvement/getSafetyMgmts`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function getSafetyMgmtDetail(param, notify) {
    return request({
        url: `/safewizpro/improvement/getSafetyMgmtDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function deleteSafetyMgmts(data, notify) {
    return request({
        url: `/safewizpro/improvement/deleteSafetyMgmts`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function deleteSafetyMgmtInspections(data, notify) {
    return request({
        url: `/safewizpro/improvement/deleteSafetyMgmtInspections`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function getHazardousMachineryReport(data, notify) {
    return request({
        url: `/safewizpro/improvement/getHazardousMachineryReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        responseType: 'blob',
        notify: { kind: notify }
    });
}

export function getHazardousMachineryReportDetail(data, notify) {
    return request({
        url: `/safewizpro/improvement/getHazardousMachineryReportDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        responseType: 'blob',
        notify: { kind: notify }
    });
}