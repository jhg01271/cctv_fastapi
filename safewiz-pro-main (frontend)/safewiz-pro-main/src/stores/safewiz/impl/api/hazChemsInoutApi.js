import request from '@/utils/axios.js'

// [작업장 위험물/유해화학물질 입출고 조회]
export function getHazmatInout (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatInout/getHazmatInout`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [작업장 위험물/유해화학물질 입출고 내역 조회]
export function getHazmatInoutDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatInout/getHazmatInoutDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

// [작업장별 유해화학물질 msds 조회]
export function getMsdsByWorkplace (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatChecklist/getMsdsByWorkplace`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: data,
    notify: { kind: notify }
  })
}

export function saveHazmatInout (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatInout/saveHazmatInout`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}


export function deleteHazmatInout (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatInout/deleteHazmatInout`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}



export function deleteHazmatInoutDetail (data, notify) {
  return request({
    url: `/safewizpro/impl/hazmatInout/deleteHazmatInoutDetail`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getHazmatInoutReport (param, notify) {
  let responseType = "blob"
  return request({
    url: `/safewizpro/impl/hazmatInout/getHazmatInoutReport`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    notify: { kind: notify },
    responseType: responseType
  })
}
