import request from '@/utils/axios.js';

export function getAuditExecutionPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/getAuditExecutionPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    });
}

export function getJudgeList(data, notify) {
    return request({
        url: `/safewizpro/evaluation/getJudgeList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    });
}

export function saveAuditExecutionPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/saveAuditExecutionPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function deleteAuditExecutionPlan(data, notify) {
    return request({
        url: `/safewizpro/evaluation/deleteAuditExecutionPlan`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function getAuditExecutionPlanReport(param, notify) {
    let responseType = 'blob';
    return request({
        url: `/safewizpro/evaluation/getAuditExecutionPlanReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        notify: { kind: notify },
        responseType: responseType
    });
}
