import request from '@/utils/axios.js';

// 메인
export function getSafetyAndHealthObjectives(param, notify) {
    return request({
        url: `/safewizpro/planning/getSafetyAndHealthObjectives`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    });
}

// 상세 메인
export function getMainSafetyAndHealthObjective(param, notify) {
    return request({
        url: `/safewizpro/planning/getMainSafetyAndHealthObjective`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    });
}

// 상세
export function getSafetyAndHealthObjectivesDetail(param, notify) {
    return request({
        url: `/safewizpro/planning/getSafetyAndHealthObjectivesDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    });
}

// 안전보건목표에 등록되지 않은 조직 조회
/*
2024.12.12 SI3팀 김현재 선임 작성
공통으로 조직을 조회하는 API가 있으나,
조직조회하는 API를 새로만든 이유는 
공통에서 조회하는 조직은 모든 조직을 조회하지만
이 화면에서 조직을 조회할때는 작성년도에 등록된 조직을 제외하고 조회되어야 하기때문에 새로 만듬
EX) 2024년에 대외사업부가 등록이 되어있으면, 2024년도 신규작성시에 조직을 조회할때는 대외사업부가 조회가 되면 안된다.

ESG팀 배현정 수석님과 상의하여 공통단에서 처리하기보다는 새로 API를 만들어서 개발하기로 협의를 했음.
*/
export function getOrganizationInSafetyAndHealthObjectives(param, notify) {
    return request({
        url: `/safewizpro/planning/getOrganizationInSafetyAndHealthObjectives`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    });
}

// 저장
export function saveSafetyAndHealthObjectives(data) {
    return request({
        url: `/safewizpro/planning/saveSafetyAndHealthObjectives`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: true },
    });
}

//수정시 아코디언을 체크안하고 저장했을때 실행되는 함수(상단에 있는 사용여부만 업데이트됨)
export function saveSafetyAndHealthObjectivesTop(data) {
    return request({
        url: `/safewizpro/planning/saveSafetyAndHealthObjectivesTop`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: true },
    });
}

// 상세
export function delSafetyAndHealthObjectives(data) {
    return request({
        url: `/safewizpro/planning/delSafetyAndHealthObjectives`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: true },
    });
}

// 상세
export function delSafetyAndHealthObjectivesDetail(data) {
    return request({
        url: `/safewizpro/planning/delSafetyAndHealthObjectivesDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: true },
    });
}

//출력물
export function getSafetyHealthObjectivesReport(param, notify) {
    console.log('param', param);
    return request({
        url: `/safewizpro/planning/getSafetyHealthObjectivesReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        responseType: 'blob',
    });
}

//출력물
export function getSafetyHealthObjectivesReportUseYn(param, notify) {
    console.log('param', param);
    return request({
        url: `/safewizpro/planning/getSafetyHealthObjectivesReportUseYn`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        responseType: 'blob',
    });
}

//메인화면에서 출력물
export function getSafetyHealthObjectivesReportMain(param, notify) {
    console.log('param', param);
    return request({
        url: `/safewizpro/planning/getSafetyHealthObjectivesReportMain`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        responseType: 'blob',
    });
}
