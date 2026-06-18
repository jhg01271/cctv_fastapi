import request from '@/utils/axios.js'

// save
export function createCompany (data, notify) {
  return request({
    url: `/admin/comp/company/site/create`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function searchCompany (param, notify) {
  return request({
    url: `/admin/comp/company/site/search`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function treeCompany (param, notify) {
  return request({
    url: `/admin/comp/company/site/tree`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getCompany (id, notify) {
  return request({
    url: `/admin/comp/company/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function modifyCompany (id, data, notify) {
  return request({
    url: `/admin/comp/company/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function removeCompany (id, notify) {
  return request({
    url: `/admin/comp/company/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function modifyInviteKey (id, notify) {
  return request({
    url: `/admin/comp/company/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}