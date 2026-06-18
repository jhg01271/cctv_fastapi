import request from '@/utils/axios.js'

export function getCardJobManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getCardJobManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getJobManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getJobManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getDataSetJobManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getDataSetJobManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveJobManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/saveJobManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteJobManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/deleteJobManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getJobLevelManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getJobLevelManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getDataSetJobLevelManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getDataSetJobLevelManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveJobLevelManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/saveJobLevelManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteJobLevelManageList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/deleteJobLevelManageList`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getJobCompAssessList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getJobCompAssessList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function getPreJobCompAssessList (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getPreJobCompAssessList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveJobCompAssess (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/saveJobCompAssess`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function saveJobCompAssessDetail (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/saveJobCompAssessDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function savePreJobCompAssess (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/savePreJobCompAssess`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteJobCompAssess (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/deleteJobCompAssess`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteJobCompAssessDetail (data, notify) {
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/deleteJobCompAssessDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function getJobCompAssessReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/support/JobCompetencyAssessment/getJobCompAssessReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}

