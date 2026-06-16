import request from '@/utils/axios.js'

// 위험요인분류 조회
export function getHazardsList(param, notify) {
    return request({
        url: `/safewizpro/planning/getHazardsList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험성 평가 이행 - 위험요인분류 조회
export function selectHazardsList(param, notify) {
    return request({
        url: `/safewizpro/planning/selectHazardsList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 상세 공정 조회
export function getProcessList(param, notify) {
    return request({
        url: `/safewizpro/planning/getProcessList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 상세 조회
export function getHazardsDetail(param, notify) {
    return request({
        url: `/safewizpro/planning/getHazardsDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 상세 공정에 해당하는 설비,위험물질 조회
export function getProcessEquipmentAndMsds(param, notify) {
    return request({
        url: `/safewizpro/planning/getProcessEquipmentAndMsds`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 상세 공정에 해당하는 설비,위험물질 조회
export function getSavedFactorList(param, notify) {
    return request({
        url: `/safewizpro/planning/getSavedFactorList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 상세 저장
export function saveHazards(param, notify) {
    return request({
        url: `/safewizpro/planning/saveHazards`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 전년도 불러오기 저장
export function savePrevHazards(param, notify) {
    return request({
        url: `/safewizpro/planning/savePrevHazards`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

// 위험요인분류 삭제
export function deleteHazards(param, notify) {
    return request({
        url: `/safewizpro/planning/deleteHazards`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify },
    })
}

//위험요인분류 출력
export function getClassificationOfHazardsReports(param, notify) {
    return request({
        url: '/safewizpro/planning/getClassificationOfHazardsReports',
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        responseType: 'blob',
        notify: { kind: notify },
    })
}

// 유해위험요인 분류 예시 데이터 조회
export function getSampleClassData(param, notify) {
    return request({
        url: `/safewizpro/planning/getSampleClassData`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}
// 유해위험요인 분류 데이터 조회
export function getClassData(param, notify) {
    return request({
        url: `/safewizpro/planning/getClassData`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}
// 유해위험요인 분류 데이터 삭제
export function deleteClassData(data, notify) {
    return request({
        url: `/safewizpro/planning/deleteClassData`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify },
    })
}
// 유해위험요인 분류 데이터 저장
export function saveClassData(data, notify) {
    return request({
        url: `/safewizpro/planning/saveClassData`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify },
    })
}