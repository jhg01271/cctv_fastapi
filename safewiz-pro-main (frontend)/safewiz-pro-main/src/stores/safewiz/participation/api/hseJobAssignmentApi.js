import request from '@/utils/axios.js'

export function getJobAssign(param, notify) {
  return request({
    url: `/safewizpro/participation/JobAssign/getJobAssign`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getJobAssignDetail(param, notify) {
  return request({
    url: `/safewizpro/participation/JobAssign/getJobAssignDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
      params: param,
    notify: { kind: notify }
  })
}

export function getMyJobAssign(param, notify) {
  console.log("파람확인",param)
  return request({
    url: `/safewizpro/participation/JobAssign/getMyJobAssign`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function saveJobAssign(data, notify) {
    return request({
        url: `/safewizpro/participation/JobAssign/saveJobAssign`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
      notify: { kind: notify }
    })
}

export function deleteJobAssign(data, notify) {
    return request({
        url: `/safewizpro/participation/JobAssign/deleteJobAssign`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteJobAssigns(data, notify) {
    return request({
        url: `/safewizpro/participation/JobAssign/deleteJobAssigns`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}
