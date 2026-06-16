import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import { treeMenu } from '@/stores/system/setting/api/Menu.js';
// import { getFavoritesList, addFavoritesMenu } from '@/stores/system/base/api/favoritesApi';
import { useFavoritesMenuStore } from '@/stores/favoritesMenu.js';
import { AesEncrypt, AesDecrypt } from '@/utils/aes256';
import { customCookies } from '@/utils/token';
import { gsap } from 'gsap';

/**
 * 사업장 데이터를 가져오는 함수
 */
import { i18n } from '@/main';
const t = param => {
    return i18n.global.t(param);
};
import { getCompListByHr } from '@/stores/system/setting/api/compApi';

export const useMenuStore = defineStore('menu', () => {
    const menu = ref([]);
    // const menuFavorites = ref([]);
    const menuFavorites = computed(() => favoritesMenuStore.favoritesMenuList);

    const isTaskRoute = ref(false);
    const selectedCompany = ref('');
    const selectedCompanyName = ref('');

    const companyTree = ref([]);

    // 좌측 네비 탭 상태 (true: MENU, false: 즐겨찾기)
    // localStorage에 저장하지 않아 새로고침 시 기본값(MENU)으로 초기화됨
    const isMenuTab = ref(true);
    const setIsMenuTab = val => {
        isMenuTab.value = !!val;
    };

    const setMenu = async () => {
        await fetchCompanyData();
        treeMenu().then(res => {
            if (res.list) {
                menu.value = res.list;
            }
        });
    };

    const searchParam = ref({ listSize: 1000, curPage: 1, searchText: '' });

    const fetchCompanyData = async () => {
        try {
            new Promise(resolve => {
                getCompListByHr(searchParam.value).then(res => {
                    if (res.success) {
                        if (res.list.length > 0) {
                            const compId = res.list[0].compId;
                            const compNm = res.list[0].compNm;
                            const encryptedCompId = AesEncrypt(compId);
                            if (selectedCompany.value == '') {
                                if (!sessionStorage.getItem('COMP_ID')) {
                                    sessionStorage.setItem('COMP_ID', encryptedCompId);
                                }
                                selectedCompany.value = AesDecrypt(sessionStorage.getItem('COMP_ID'));
                                selectedCompanyName.value = res.list.find(el => el.compId === AesDecrypt(sessionStorage.getItem('COMP_ID'))).compNm;
                            } else if (!sessionStorage.getItem('COMP_ID')) {
                                sessionStorage.setItem('COMP_ID', encryptedCompId);
                            }
                        }
                        resolve({ list: res.list, totalCount: res.totalCount });
                    }
                });
            }).then(res => {
                if (res) {
                    companyTree.value = res.list;
                }
            });
        } catch (error) {
            console.error('Error:', error);
        }
    };

    // --- 탭 네비게이션 로직 시작 ---
    const menuNaviList = ref([]);
    const activeTabPath = ref('');

    // 탭 네비게이션 추가
    const setTabNaviMenu = to => {
        if (!to.meta.menuNm && to.path != '/dashboard') {
            return;
        }
        const isExist = menuNaviList.value.some(item => item.path === to.path); // 이미 등록되어있는지
        if (!isExist) {
            if (to.path === '/dashboard') to.meta.menuNm = t('대시보드');
            menuNaviList.value.push(to);
        }
        activeTabPath.value = to.path;
    };

    // 탭 네비게이션 삭제
    const removeTab = (tab, router) => {
        const tabIndex = menuNaviList.value.findIndex(item => item.path === tab.path);
        if (tabIndex === -1) return;

        // if (activeTabPath.value === tab.path) {
        // 현재 활성화된 메뉴 삭제 시
        const nextTab = menuNaviList.value[tabIndex + 1] || menuNaviList.value[tabIndex - 1];
        if (nextTab) {
            // 이전 탭이 있을 경우에만
            activeTabPath.value = nextTab.path;
            router.push(nextTab.path);
        }
        if (menuNaviList.value.length > 1) menuNaviList.value.splice(tabIndex, 1);
        // }
    };
    // --- 탭 네비게이션 로직 끝 ---
    //2025.12.24 TagView 로직 추가
    // const selected = ref({ title: '', id: '' })
    // const setSelectedMenu = (title, id) => {
    //   selected.value.title = title
    //   selected.value.id = id
    //   sessionStorage.setItem('parents', title)
    //   sessionStorage.setItem('parents_id', id)
    // };
    return {
        menu,
        setMenu,
        fetchCompanyData,
        isTaskRoute,
        selectedCompany,
        selectedCompanyName,
        companyTree,
        // 좌측 네비 탭
        isMenuTab,
        setIsMenuTab,
        // 탭 네비게이션용
        menuNaviList,
        activeTabPath,
        setTabNaviMenu,
        removeTab,
        // setSelectedMenu
    };
});
