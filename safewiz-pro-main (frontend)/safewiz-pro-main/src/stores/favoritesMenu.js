import { ref } from 'vue';
import { defineStore } from 'pinia';
import { getFavoritesSide, deleteFavoritesMenu, addFavoritesMenu, addFavoritesFolder, updateFavoritesMenu, updateFavoritesOdrs } from '@/stores/system/base/api/favoritesApi';

import { AesDecrypt /* AesDecrypt */ } from '@/utils/aes256';
export const useFavoritesMenuStore = defineStore('FavoritesMenu', () => {
    const isUserAuth = () => {
        let auth = false;
        if (window.sessionStorage.getItem('grpAuthCd')) {
            auth = AesDecrypt(window.sessionStorage.getItem('grpAuthCd')) === 'USER';
        }        
        return auth;
    }
    // 즐겨찾기 표시 : true / 미표시 : false
    // 유저 권한일 때에만 즐겨찾기 메뉴가 표시되도록 함
    const isFavorite = true && isUserAuth();

    const favoritesMenuList = ref([]);
    const favoritesExampleMenuList = ref([]);

    const setFavoritesMenu = async () => {
        await getFavoritesSide().then(res => { //formData, true
            if (res.list) {
                favoritesMenuList.value = res.list;
            }
        });
    }

    const checkFavoritesMenu = (menuId) => {
        return favoritesMenuList.value.some(menu =>
            Array.isArray(menu._children) &&
            menu._children.some(child => child.menuId === menuId)
        )
    }

    const addFolder = async () => {
        await addFavoritesFolder().then(res => {
            if (res && res.success) {
                setFavoritesMenu();
            }
        });
    }

    const addFavorites = async (selectMenu) => {
        let formData = new FormData();
        formData.append('data', new Blob([JSON.stringify(selectMenu)], { type: 'application/json' }));
        return await addFavoritesMenu(formData, true).then(res => {
            if (res && res.success) {
                setFavoritesMenu();
            }
        })
    }

    const updateFavorites = async (menu) => {
        let formData = new FormData();
        formData.append('data', new Blob([JSON.stringify(menu)], { type: 'application/json' }));
        return await updateFavoritesMenu(formData, false).then(res => {
            if (res && res.success) {
                setFavoritesMenu();
            }
        })
    }


    const deleteFavorites = async (menuId) => {
        const newData = [{
            menuId: menuId
        }]

        let formData = new FormData();
        formData.append('data', new Blob([JSON.stringify(newData)], { type: 'application/json' }));

        return deleteFavoritesMenu(formData, true).then(res => {
            if (res && res.success) {
                setFavoritesMenu();
            }
        })
    }

    const updateFavoritesOdr = async (data, loginId) => {

        const newData = flattenMenuData(data, loginId)
        let formData = new FormData();
        formData.append('data', new Blob([JSON.stringify(newData)], { type: 'application/json' }));
        return new Promise((resolve, reject) => {
            updateFavoritesOdrs(formData, false).then(res => {
                if (res && res.success) {
                    setFavoritesMenu();
                }
            })
        })

    }


    const flattenMenuData = (data, loginId, parentId = null) => {
        let result = [];

        data.forEach((item, idx) => {
            // 부모 메뉴 추가
            result.push({
                menuId: item.menuId,
                index: idx,
                menuNm: item.menuNm,
                upMenuId: parentId,
                loginId: loginId
            });

            // 자식이 있으면 재귀 호출
            if (item._children && item._children.length > 0) {
                const children = item._children.map((child, childIdx) => ({
                    menuId: child.menuId,
                    index: childIdx,
                    menuNm: child.menuNm,
                    upMenuId: item.menuId, // 부모의 menuId
                    loginId: loginId
                }));

                result = result.concat(children);
            }
        });

        return result;
    };
    // 

    return {
        favoritesMenuList,
        setFavoritesMenu,
        checkFavoritesMenu,
        deleteFavorites,
        addFavorites,
        updateFavorites,
        updateFavoritesOdr,
        addFolder,
        favoritesExampleMenuList,
        isUserAuth,
        isFavorite
    };
});
