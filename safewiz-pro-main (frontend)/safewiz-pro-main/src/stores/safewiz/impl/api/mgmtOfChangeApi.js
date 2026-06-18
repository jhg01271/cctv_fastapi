import request from '@/utils/axios.js'

export function saveMgmtOfChange(data, notify) {
    return request({
      url: '/safewizpro/impl/saveMgmtOfChange',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: data,  // POST 요청에 body 사용
      notify: { kind: notify }
    });
  }

  export function getMgmtOfChange (notify) {
    return request({
      url: `/safewizpro/impl/getMgmtOfChange`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  export function deleteMgmtOfChange (param,notify) {
    return request({
      url: `/safewizpro/impl/deleteMgmtOfChange`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      data:param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }
  
  export function getMgmtOfChangeDetail (param, notify) {
    return request({
      url: `/safewizpro/impl/getMgmtOfChangeDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      data: param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  export function getMgmtOfChangeReport (data, notify) {
    return request({
      url: '/safewizpro/impl/getMgmtOfChangeReport',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      responseType: 'blob',
      notify: { kind: notify }
    })
  }
  