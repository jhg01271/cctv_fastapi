import request from '@/utils/axios.js'

export function searchYear (param, notify) {
  return request({
    url: `/safewizpro/orgstatus/searchYear`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function searchEmployeesStakeholders (param, notify) {
  return request({
    url: `/safewizpro/orgstatus/searchEmployeesStakeholders`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


export function deleteEmployeesStakeholdersList (data, notify) {
  return request({
    url: `/safewizpro/orgstatus/deleteEmployeesStakeholdersList`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//Detail 조회
export function searchEmployeesStakeholdersDetail (param, notify) {
  return request({
    url: `/safewizpro/orgstatus/searchEmployeesStakeholdersDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

//Detail 추가
export function addEmployeesStakeholdersDetail (data, notify) {
  return request({
    url: `/safewizpro/orgstatus/saveEmployeesStakeholdersDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//Detail 수정
export function modifyEmployeesStakeholdersDetail (data, notify) {
  return request({
    url: `/safewizpro/orgstatus/modifyEmployeesStakeholdersDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//Detail 삭제
export function deleteEmployeesStakeholdersDetail (data, notify) {
  return request({
    url: `/safewizpro/orgstatus/deleteEmployeesStakeholdersDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

//출력물
export function getReport (data, notify) {
  let responseType = "blob"
  return request({
    url: '/safewizpro/orgstatus/getEmployeesReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob'
  })
}
