import request from '@/utils/axios.js'
import { useCompanyStore } from '@/stores/comp/master/company';

export function getCompCode (param, notify) {
  return request({
      url: `/comp/master/compCode/search`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
  })
}

export function getSubCodeList (compCode, notify) {
    const companyStore = useCompanyStore();
    const compId = companyStore.handleCurrentCompId('get');
    return request({
        url: `/comp/master/site/${compId}/compCode/${compCode}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
