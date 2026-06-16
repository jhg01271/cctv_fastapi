import request from '@/utils/axios.js'

export function getResultOfRiskAssessmentTrainingListAPI(param, notify) {
    return request({
        url: '/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTraining',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function insertResultOfRiskAssessmentTrainingAPI(param, notify) {
    return request({
        url: `/safewizpro/planning/resultOfRiskAssessmentTraining/insertResultOfRiskAssessmentTraining`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: param,
        notify: { kind: notify }
    })
}

export function getResultOfRiskAssessmentTrainingAPI(param, notify) {
    return request({
        url: `/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTrainingDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function updateResultOfRiskAssessmentTrainingAPI(param, notify) {
    return request({
        url: `/safewizpro/planning/resultOfRiskAssessmentTraining/updateResultOfRiskAssessmentTraining`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: param,
        notify: { kind: notify }
    })
}

export function deleteResultOfRiskAssessmentTrainingAPI(param, notify) {
    return request({
        url: `/safewizpro/planning/resultOfRiskAssessmentTraining/deleteResultOfRiskAssessmentTraining`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify }
    })
}

export function getResultOfRiskAssessmentTrainingReportAPI(param, notify) {
    return request({
        url: '/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTrainingReport',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        responseType: 'blob',
        notify: { kind: notify }
    })
}