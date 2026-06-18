import request from '@/utils/axios.js'

export function getLegalReviewList (param, notify) {
    return request({
        url: `/safewizpro/planning/getLegalReviewList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getLegalReviewDetailMasterList (param, notify) {
    return request({
        url: `/safewizpro/planning/getLegalReviewDetailMasterList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}


export function getLegalReviewDetailList (data, notify) {
    return request({
        url: `/safewizpro/planning/getLegalReviewDetailList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    })
}


export function getLegalReviewDetailOrgnList (data, notify) {
    return request({
        url: `/safewizpro/planning/getLegalReviewDetailOrgnList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: data,
        notify: { kind: notify }
    })
}

export function saveLegalReview (data, notify) {
    return request({
        url: `/safewizpro/planning/saveLegalReview`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
      //headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
        notify: { kind: notify }
    })
}


export function deleteLegalReview (data, notify) {
    return request({
        url: `/safewizpro/planning/deleteLegalReview`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function deleteLegalReviewDetail (data, notify) {
    return request({
        url: `/safewizpro/planning/deleteLegalReviewDetail`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function getLegalReviewReport (data) {
    let responseType = "blob"
    return request({
        url: `/safewizpro/planning/getLegalReviewReport`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: data,
        responseType: responseType
    })
}


export function getLegalReviewReportAll (data) {
    let responseType = "blob"
    return request({
        url: `/safewizpro/planning/getLegalReviewReportAll`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=utf8' },
        data: data,
        responseType: responseType
    })
}

