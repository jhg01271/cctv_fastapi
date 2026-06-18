import request from '@/utils/axios.js';

// 즐겨찾기 리스트 조회

export function getFavoritesSide(param, notify) {
    return request({
        url: `/safewizpro/base/getFavoritesSide`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function getFavoritesList(notify) {
    return request({
        url: `/system/setting/menu/treeFavorites?isWeb=true`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        // params: param,
        notify: { kind: notify }
    })
}

export function saveFavoritesExample(data, notify) {
    return request({
        url: `/safewizpro/base/saveFavoritesExample`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: data,
        notify: { kind: notify }
    })
}

export function addFavoritesFolder(param, notify) {
    return request({
        url: `/safewizpro/base/addFavoritesFolder`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}

export function addFavoritesMenu(data, notify) {
    return request({
        url: `/safewizpro/base/addFavoritesMenu`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        // headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function updateFavoritesMenu(data, notify) {
    return request({
        url: `safewizpro/base/updateFavoritesFolder`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        // headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}

export function updateFavoritesOdrs(data, notify) {
    return request({
        url: `safewizpro/base/updateFavoritesOdr`,
        meta: { apiVersion: '1.0.0' },
        method: 'POST',
        data: data,
        // headers: { 'Content-Type': 'application/json' },
        notify: { kind: notify }
    });
}


export function deleteFavoritesMenu(data, notify) {
    return request({
        url: `safewizpro/base/deleteFavoritesMenu`,
        meta: { apiVersion: '1.0.0' },
        method: 'DELETE',
        // headers: { 'Content-Type': 'application/json' },/
        data: data,
        notify: { kind: notify }
    })
}



export function getFavoritesExample(param, notify) {
    return request({
        url: `/safewizpro/base/favoritesMenuExample`,
        meta: { apiVersion: '1.0.0' },
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        params: param,
        notify: { kind: notify }
    })
}


//#endregion
