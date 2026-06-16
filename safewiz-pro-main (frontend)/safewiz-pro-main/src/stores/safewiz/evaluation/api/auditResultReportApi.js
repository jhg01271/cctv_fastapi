import request from '@/utils/axios.js'

export function getAuditResult (data, notify) {
    return request({
      url: `/safewizpro/evaluation/getAuditResult`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
  }

  export function getAuditResultDetail (data, notify) {
    return request({
      url: `/safewizpro/evaluation/getAuditResultDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
  }

  export function deleteAuditResult(data, notify) {
    return request({
      url: '/safewizpro/evaluation/deleteAuditResult',
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,  // POST 요청에 body 사용
      notify: { kind: notify }
    });
  }

  export function saveAuditResult(data, notify) {
    return request({
      url: '/safewizpro/evaluation/saveAuditResult',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: data,  // POST 요청에 body 사용
      notify: { kind: notify }
    });
  }

  // 안전보건 내부심사 결과보고서 심사 팀원 저장
  export function saveAuditHr(data, notify) {
    return request({
        url: `/safewizpro/evaluation/saveAuditHr`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

  export function getAuditResultReport(param, notify) {
    let responseType = 'blob';
    return request({
        url: `/safewizpro/evaluation/getAuditResultReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        notify: { kind: notify },
        responseType: responseType
    });
}