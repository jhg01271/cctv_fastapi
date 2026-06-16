import request from '@/utils/axios.js'

export function getLoginHistoryList(param, notify) {
  return request({
    url: `/system/setting/loginHistory/getLoginHistory`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//유저 ID를 기반으로 마지막 접속날짜, 마지막 접속OS, 로그인 실패 횟수를 조회하는 기능
//작성자 : 류원진, 작성일 : 2025.03.06
export function getLastLoginDetails(param, notify) {
  return request({
    url: `/system/setting/loginHistory/getLastLoginDetails`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}
