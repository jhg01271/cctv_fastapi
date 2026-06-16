import request from '@/utils/axios'
/**
 * 👉 로그인
 */
export async function signin (param, notify) {
  const res = await fetch("https://api.ipify.org?format=json");
  const data = await res.json();
  const clientIp = data.ip;
  return request({
    url: `/base/signin`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json',  'X-Real-Client-IP': clientIp },
    data: param,
    notify: { kind: notify }
  })
}

export function layoutInit (notify) {
  return request({
    url: `/base/layout/init`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getMenu (menuCd, notify) {
  return request({
    url: `/base/menu/${menuCd}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}