import request from '@/utils/axios.js'

export function getEvaluationType (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getEvaluationType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function getEvaluationTypeDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getEvaluationTypeDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getEvaluationTypeDataset (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getEvaluationTypeDataset`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}
export function getEvaluationTypeDetailDataset (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getEvaluationTypeDetailDataset`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveEvaluationType (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/saveEvaluationType`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteEvaluationType (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/deleteEvaluationType`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function saveAssmtReport (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/saveAssmtReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteAssmtReport (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/deleteAssmtReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getAssmtReport (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getAssmtReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getAssmtReportDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getAssmtReportDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getReport (data, notify) {
  return request({
    url: `/safewizpro/impl/contractorAssmt/getReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: data,
    notify: { kind: notify },
    responseType: 'blob'
  })
}