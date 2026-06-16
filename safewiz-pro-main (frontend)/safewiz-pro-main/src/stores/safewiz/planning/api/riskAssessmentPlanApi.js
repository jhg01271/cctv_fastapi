import request from '@/utils/axios.js'

export function getRiskAssessmentPlanListApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 조직 팝업 - 공정/설비/구분에 저장된 조직
export function getRiskAssessmentPlanOrgnPopupListApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanOrgnPopupList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 이전 위험성 정보 선택 팝업 - 해당 조직의 계획 리스트
export function getRiskAssessmentPlanPrevPopupListApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanPrevPopupList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

export function insertRiskAssessmentPrevPlanApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/insertRiskAssessmentPrevPlan',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

export function insertRiskAssessmentPlanApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/insertRiskAssessmentPlan',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

export function getRiskAssessmentPlanDetailApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanDetail',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

export function updateRiskAssessmentPlanApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/updateRiskAssessmentPlan',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

export function deleteRiskAssessmentPlanApi(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/deleteRiskAssessmentPlan',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

// 계획 > 유해위험요인, 감소대책 건수 및 평균 위험도 조회
export function getRiskAvgCount(param, notify) {
    return request({
        url: '/safewizpro/planning/riskAssessmentPlan/getRiskAvgCount',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}