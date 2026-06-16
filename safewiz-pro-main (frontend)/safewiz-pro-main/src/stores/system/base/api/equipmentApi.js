import request from '@/utils/axios.js'

export function getEquip (param, notify) {
  return request({
    url: `/system/base/equip/getEquip`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function getEquipDetail (equipId, notify) {
  return request({
    url: `/system/base/equip/getEquipDetail/${equipId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function insertEquip (data, notify) {
  return request({
    url: `/system/base/equip/insertEquip`,
    meta: { apiVersion: '1.0.0' },
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function updateEquip (equipmentId, data, notify) {
  return request({
    url: `/system/base/equip/${equipmentId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data,
    notify: { kind: notify }
  })
}

export function deleteEquip (equipmentId, notify) {
  return request({
    url: `/system/base/equip/${equipmentId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    notify: { kind: notify }
  })
}

export function deleteEquips (data, notify) {
  return request({
    url: `/system/base/equip/deleteEquips`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getStdEquips (data, notify) {
  return request({
    url: `/system/base/equip/std/getStdEquips`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function saveStdEquips (compId, data, notify) {
  return request({
    url: `/system/base/equip/std/${compId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}
export function deleteStdEquips (compId, data, notify) {
  return request({
    url: `/system/base/equip/std/delete/${compId}`,
    meta: { apiVersion: '1.0.0' },
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    data: data,
    notify: { kind: notify }
  })
}

export function getEquipmentByType (param, notify) {
  return request({
    url: `/system/base/equip/getEquipmentByType`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}


export function getStdEqList (param, notify) {
  return request({
    url: `/system/base/equip/getStdEqList`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    params: param,
    notify: { kind: notify }
  })
}

export function downloadEquipExcelTemplate (param, notify) {
  return request({
    url: `/system/base/equip/downloadEquipExcelTemplate/${param}`,
    meta: { apiVersion: '1.0.0' },
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    notify: { kind: notify }
  })
}

export function insertEquipExcel(data, notify) {
  return request({
      url: `/system/base/equip/insertEquipExcel`,
      meta: { apiVersion: '1.0.0' },
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: data,
      notify: { kind: notify }
  });
}