import request from '@/utils/axios.js';

export function getConstractorRegisterList(data, notify) {
    return request({
        url: `/safewizpro/impl/register/getConstractorRegisterList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    });
}

export function getConstractorRegisterReport(param, notify) {
    let responseType = 'blob';
    return request({
        url: `/safewizpro/impl/register/getConstractorRegisterReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        notify: { kind: notify },
        responseType: responseType
    });
}
