import request from '@/utils/axios.js';

//인원조회, param : Object
export function getHr(param, notify) {
    return request({
        url: `/system/base/hr/getHr`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function getHrDetail(hrId, notify) {
    return request({
        url: `/system/base/hr/getHrDetail/${hrId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function checkExceedMaxHrCount(param, notify) {
    return request({
        url: `/system/base/hr/checkExceedMaxHrCount`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function getOrgHistoryList(hrId, notify) {
    return request({
        url: `/system/base/hr/getOrgHistory/${hrId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function insertHrExcel(data, notify) {
    return request({
        url: `/system/base/hr/insertHrExcel`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}
export function downloadHrExcelTemplate (param, notify) {
    return request({
      url: `/system/base/hr/downloadHrExcelTemplate/${param}`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      responseType: 'blob',
      notify: { kind: notify }
    })
  }

export function insertHr(data, notify) {
    return request({
        url: `/system/base/hr/insertHr`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
        notify: { kind: notify }
    });
}

export function updateHr(hrId, data, notify) {
    return request({
        url: `/system/base/hr/${hrId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'multipart/form-data' },
        data: data,
        notify: { kind: notify }
    });
}

export function deleteHr(hrId, notify) {
    return request({
        url: `/system/base/hr/${hrId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function deleteHrs(data, notify) {
    return request({
        url: `/system/base/hr/deleteHrs`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function getHrJbrpByCompId(compId, notify) {
  return request({
      url: `/system/base/hr/getHrJbrpByCompId/${compId}`,
      meta: { apiVersion: '1.0.0' },
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      notify: { kind: notify }
  });
}

//#region 직위 관리 팝업창
export function getHrJbrp(notify) {
    console.log(notify);
    return request({
        url: `/system/base/hr/getHrJbrp`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function getDatasetHrJbrp(notify) {
    return request({
        url: `/system/base/hr/getDatasetHrJbrp`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function saveHrJbrp(data, notify) {
    return request({
        url: `/system/base/hr/saveHrJbrp`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function deleteHrJbrp(data, notify) {
    return request({
        url: `/system/base/hr/deleteHrJbrp`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function resetPassword(data, notify) {
    return request({
        url: `/system/base/hr/resetPassword`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        data: data,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}
//#endregion
