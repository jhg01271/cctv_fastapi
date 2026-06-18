import request from '@/utils/axios.js'

// export function searchCompany (param) {
//   return request({
//     url: `/comp/company/site/search`,
//     meta: { apiVersion: '1.0.0' },
//     method: 'GET',
//     headers: { 'Content-Type': 'application/json' },
//     params: param,
//     notify: { kind: notify }
//   })
// }

export function getCompany (id, notify) {
  return request({
    url: `/comp/company/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}


export function searchComp (param, notify) {
  return request({
      url: `/comp/company/site/search/my`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
  })
}

export function treeComp (param, notify) {
  return request({
      url: `/comp/company/site/tree/my`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: param,
      notify: { kind: notify }
  })
}



