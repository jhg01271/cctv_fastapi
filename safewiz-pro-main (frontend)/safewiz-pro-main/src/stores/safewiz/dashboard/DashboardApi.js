import request from '@/utils/axios.js'

export function getDashboard(params, notify) {
    return request({
      url: `/safewizpro/dashboard/getDashboard`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params:params,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    });
  }