import request from '@/utils/axios.js'

export function getRiskAssessment (param, notify) {
    return request({
      url: `/safewizpro/planning/getRiskAssessment`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
    })
  }