import request from '@/utils/axios.js';

// save
export function createGroup(data, notify) {
    return request({
        url: `/system/setting/group/create`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function createGroupMember(data, notify) {
    return request({
        url: `/system/setting/group/createMember`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function modifyGroup(grpSeq, param, notify) {
    return request({
        url: `/system/setting/group/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: param,
        notify: { kind: notify }
    });
}

export function removeGroup(grpSeq, param, notify) {
    return request({
        url: `/system/setting/group/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        params: param,
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function removeMember(param, notify) {
    return request({
        url: `/system/setting/groupMember?compId=${param.compId}&grpId=${param.grpId}&hrId=${param.hrId}`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function searchGroup(param, notify) {
    return request({
        url: `/system/setting/group/search`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function searchGroupMember(param, notify) {
    return request({
        url: `/system/setting/group/searchMember`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function searchGroupList(param, notify) {
    return request({
        url: `/system/setting/group/searchLists`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

export function getGroup(grpSeq, param, notify) {
    return request({
        url: `/system/setting/group/${grpSeq}`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}

// 데이터셋 API
export function getDatasetGroupList(param, notify) {
    return request({
        url: `/system/setting/group/dataset/getDatasetGroupList`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    });
}
export function saveGroupDataset(data, notify) {
    return request({
        url: `/system/setting/group/dataset/saveGroupDataset`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}

export function findUserGroup(data, notify) {
    return request({
        url: `/system/setting/group/findUserGroup`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    });
}
