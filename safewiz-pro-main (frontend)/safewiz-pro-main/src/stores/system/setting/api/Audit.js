import request from '@/utils/axios.js'


export function searchAudit (param, notify) {
    return request({
        url: `/system/setting/audit/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        params: param,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    })
}
