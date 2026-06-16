

import request from '@/utils/axios.js'

export function getQualificationManagement (param, notify) {
  return request({
    url: `/safewizpro/support/QualificationCertification/getQualificationManagement`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

  export function saveDetail (data, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/saveDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
    })
  }

  export function getList (param, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/search`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params: param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }
  
  export function getDetail (param, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/searchDetail`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params: param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  export function getReport (data, notify) {
    return request({
      url: '/safewizpro/support/QualificationCertification/getReport',
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=utf8' },
      data: data,
      responseType: 'blob',
      notify: { kind: notify }
    })
  }

  export function deleteList(param,notify){
    return request({
      url: `/safewizpro/support/QualificationCertification/delete`,
      meta: { apiVersion: '1.0.0' },
      method: 'DELETE',
      data: param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  
  export function getEvaluationList (data, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/getEvaluationList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  export function getEvaluationDetailList (param, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/getEvaluationDetailList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params : param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

    export function getAllEvaluationDetailList (param, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/getAllEvaluationDetailList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params : param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }

  export function getEvaluationDataSetList (param, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/getEvaluationDataSetList`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      params : param,
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
    })
  }


  export function saveEvaluationList (data, notify) {
    return request({
      url: `/safewizpro/support/QualificationCertification/saveEvaluationList`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
    })
  }

  

  