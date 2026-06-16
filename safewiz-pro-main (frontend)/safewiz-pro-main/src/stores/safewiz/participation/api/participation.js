import request from '@/utils/axios.js'



export function getConsultationAndParticipation (data, notify) {
  return request({
    url: `/safewizpro/participation/getConsultationAndParticipation`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveParticipation (data, notify) {
  return request({
    url: `/safewizpro/participation/insertCommittee`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function getParticipation (param, notify) {
  return request({
    url: `/safewizpro/participation/getCommittee/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getParticipationDetail (param, notify) {
  return request({
    url: `/safewizpro/participation/getCommitteeDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    params: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteParticipation (param, notify) {
  return request({
    url: `/safewizpro/participation/deleteCommittee`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    data: param,
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function getReport (data, notify) {
  return request({
    url: '/safewizpro/participation/getCommitteeReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}
export function getOhsReport (data, notify) {
  return request({
    url: '/safewizpro/participation/getOhsCommitteeReport',
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    responseType: 'blob',
    notify: { kind: notify }
  })
}