import request from '@/utils/axios.js'

// 위험요인분류 조회
export function getRiskImplList(param, notify) {
    return request({
        url: `/safewizpro/planning/getRiskImplList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험성평가 이행 상세 조회
export function getRiskAssessmentDetail(param, notify) {
    return request({
        url: `/safewizpro/planning/getRiskAssessmentDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험성평가 이행 상세 전체 조회
export function getRiskAssessmentDetailAll(param, notify) {
    return request({
        url: `/safewizpro/planning/getRiskAssessmentDetailAll`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}
// 위험성평가 이행 상세 팝업 전체 조회 - 위험성 평가 이전 정보 버튼
export function getRiskAssessmentDetailPopupAll(param, notify) {
    return request({
        url: `/safewizpro/planning/getRiskAssessmentDetailPopupAll`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 이전 위험성 정보 선택 팝업 - 해당 계획에서 참조한 이행 리스트
export function getReferenceRiskImplListApi(param, notify) {
    return request({
        url: '/safewizpro/planning/getReferenceRiskImplList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 위험성 평가 이행 - 공정/물질/설비 구분 정보 반영 시 제거 되는 데이터 조회
export function checkRemoveRiskImplList(param, notify) {
    return request({
        url: '/safewizpro/planning/checkRemoveRiskImplList',
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify },
    })
}

// 이행, 감소대책, 담당자 구분 저장
export function saveRiskImpl(param, notify) {
    return request({
      url: `/safewizpro/planning/saveRiskImpl`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: param,
      notify: { kind: notify }
    })
}

// 이전 위험성 정보 선택 팝업 - 이행 저장
export function saveMainRiskImpl(param, notify) {
    return request({
      url: `/safewizpro/planning/saveMainRiskImpl`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: param,
      notify: { kind: notify }
    })
}

// 감소대책 > 개선 전,후 이미지 저장
export function saveRiskImplImage(param, notify) {
    return request({
      url: `/safewizpro/planning/saveRiskImplFiles`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: param,
      notify: { kind: notify }
    })
}

// 위험성평가 이행 출력물
export function getReport (data, notify) {
    let responseType = "blob"
    return request({
      url: '/safewizpro/planning/getRiskImplReport',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      responseType: 'blob'
    })
}

// 위험성평가 이행 출력물 (전체 데이터 출력)
export function getReportAll (data, notify) {
    let responseType = "blob"
    return request({
      url: '/safewizpro/planning/getRiskImplReportAll',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      responseType: 'blob'
    })
}

//근로자 확인 팝업 - 근로자 일괄 삭제
export function deleteWorkerApprovalInfoAll(param, notify) {
    return request({
      url: `/safewizpro/planning/deleteWorkerApprovalInfoAll`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: param,
      notify: { kind: notify }
    })
}