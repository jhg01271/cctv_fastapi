import request from '@/utils/axios.js'

// save
export function createUser (data, notify) {
  return request({
    url: `/system/user/site/create`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function searchUser (param, notify) {
  return request({
    url: `/system/user/site/search`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getUser (id, notify) {
  return request({
    url: `/system/user/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function modifyUser (id, data, notify) {
  return request({
    url: `/system/user/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function removeUser (id, notify) {
  return request({
    url: `/system/user/site/${id}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getFindUserPs (token, notify) {
  return request({
    url: `/igns/auth/password?key=${token}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function sendLink (email, notify) {
  return request({
    url: `/igns/auth/password?email=${email}`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function initPassword (token, password, notify) {
  return request({
    url: `/igns/auth/password?key=${token}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: password,
    notify: { kind: notify }
  })
}

export function sendMail (email, notify) {
  return request({
    url: `/igns/auth/verification/${email}`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function verifyCertificaiton (data, notify) {
  return request({
    url: `/igns/auth/verification`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function findId (data, notify) {
  return request({
    url: `/igns/auth/user/findId`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function findPassword (data, notify) {
  return request({
    url: `/igns/auth/user/findPassword`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}