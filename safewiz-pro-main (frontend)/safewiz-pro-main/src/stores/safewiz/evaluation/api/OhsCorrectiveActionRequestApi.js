import request from '@/utils/axios.js'

export function getCorrectiveRequest (data, notify) {
    return request({
      url: `/safewizpro/evaluation/getCorrectiveRequest`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
  }

  export function getCorrectiveRequestDetail (data, notify) {
    return request({
      url: `/safewizpro/evaluation/getCorrectiveRequestDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: data,
      notify: { kind: notify }
    })
  }

 

  export function saveCorrectiveRequest(data, notify) {
    return request({
      url: '/safewizpro/evaluation/saveCorrectiveRequest',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: data,  // POST 요청에 body 사용
      notify: { kind: notify }
    });
  }

  export function deleteCorrectiveRequest(data, notify) {
    return request({
      url: '/safewizpro/evaluation/deleteCorrectiveRequest',
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: data,  // POST 요청에 body 사용
      notify: { kind: notify }
    });
  }

  export function getCorrectiveRequestReport(param, notify) {
    let responseType = 'blob';
    return request({
        url: `/safewizpro/evaluation/getCorrectiveRequestReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: param,
        notify: { kind: notify },
        responseType: responseType
    });
}