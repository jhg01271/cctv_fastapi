import request from '@/utils/axios.js';

export function getAnnualOhsInternalAuditPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/getAnnualOhsInternalAuditPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    });
}
export function saveAnnualOhsInternalAuditPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/saveAnnualOhsInternalAuditPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}
export function deleteAnnualOhsInternalAuditPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/deleteAnnualOhsInternalAuditPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function getAnnualOhsInternalAuditPlanReport(param, notify) {
    let responseType = 'blob';
    return request({
        url: `/safewizpro/evaluation/getAnnualOhsInternalAuditPlanReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        notify: { kind: notify },
        responseType: responseType
    });
}
