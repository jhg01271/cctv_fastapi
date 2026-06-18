<template>
    <!-- 레프트 네비게이션 (Left Navigation Bar) -->
    <div class="lnb df fdc" ref="lnb" :class="[{ collapsed: isCollapsed && !isMobile }, { mobile: isMobile }, { open: isMobileOpen }]">
        <!-- <div class="dn md-df gap1rem">
            <div class="pr w2-4rem h2-4rem df jcc aic">
                <iAlarmBadge></iAlarmBadge>
            </div>
        </div> -->

        <!-- 로고 -->
        <h1 class="df aic jcc h9-5rem shrink0 md-dn">
            <div class="pr">
                <a @click="router.push('/dashboard')">
                    <img src="/assets/img/common/logo_vertical.svg" alt="Logo" />
                </a>
            </div>
        </h1>

        <hr class="w100p h1px bcE1E6ED ma0" />
        <OverlayScrollbarsComponent :options="options" class="segment">
            <!-- 공통 네비게이션 바 (General Navigation Bar) -->
            <div class="company-select md-h9-5rem md-df md-bcF5F6FE">
                <button class="md-pl5p es-pl10p" type="button" @click.prevent="toggleMenu">
                    <span class="fs1-4rem c9EA1A6 fw200">사업장을 선택해주세요.</span>
                    <em class="db mt6px fs1-5rem">{{ menuStore.selectedCompanyName }}</em>
                </button>
                <ul ref="companyList" v-show="showSelect">
                    <li v-if="isMasterAuth">
                        <input type="text" v-model="searchKeyword" placeholder="사업장 검색" class="pa8px br4px w100p" />
                    </li>
                    <li v-for="company in menuStore.companyTree" :key="company.compId">
                        <button v-show="company.visible !== false" type="button" @click="selectCompany(company)">
                            <!-- <img v-if="company.menuIcon" :src="`/assets/icons/${company.menuIcon}.svg`" alt="" /> -->
                            <span>{{ company.compNm }}</span>
                        </button>
                    </li>
                </ul>
            </div>

            <hr class="w100p h1px bcE1E6ED ma0" />
            <div v-if="!favoritesMenuStore.isFavorite" class="gnb">
                <em class="db mb1rem ml3rem fs1-6rem c000000 o10 fw900 md-dn">MENU</em>
                <ul>
                    <template v-for="menu in menuStore.menu" :key="menu.menuId">
                        <MenuListGroup v-if="menu._children && menu._children.length > 0" :isCollapsed="isCollapsed" :menuGroup="menu" @click="isCollapsed = false" />
                        <MenuListItem v-else :menuItem="menu" />
                    </template>
                </ul>
            </div>
            <div v-else>
                <!-- <em class="db mb1rem ml3rem fs1-6rem c000000 o10 fw900 md-dn">MENU</em> -->
                <div v-show="!isCollapsed || isMobile" class="favorite-toggle">
                    <button
                        type="button"
                        @click="
                            isMenu = true;
                            menuFix = false;
                        "
                        :class="{ active: isMenu }"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-menu-icon lucide-menu">
                            <path d="M4 5h16" />
                            <path d="M4 12h16" />
                            <path d="M4 19h16" />
                        </svg>
                        <span>MENU</span>
                    </button>
                    <button
                        type="button"
                        @click="
                            isMenu = false;
                            menuFix = true;
                            isCollapsed = false;
                        "
                        :class="{ active: !isMenu }"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-user-star-icon lucide-user-star">
                            <path d="M16.051 12.616a1 1 0 0 1 1.909.024l.737 1.452a1 1 0 0 0 .737.535l1.634.256a1 1 0 0 1 .588 1.806l-1.172 1.168a1 1 0 0 0-.282.866l.259 1.613a1 1 0 0 1-1.541 1.134l-1.465-.75a1 1 0 0 0-.912 0l-1.465.75a1 1 0 0 1-1.539-1.133l.258-1.613a1 1 0 0 0-.282-.866l-1.156-1.153a1 1 0 0 1 .572-1.822l1.633-.256a1 1 0 0 0 .737-.535z" />
                            <path d="M8 15H7a4 4 0 0 0-4 4v2" />
                            <circle cx="10" cy="7" r="4" />
                        </svg>
                        <span>즐겨찾기</span>
                    </button>
                </div>
                <!-- db mb1rem ml3rem fs1-6rem c000000 o10 fw900 md-dn -->
                <!-- <div :class="{ gnb: isMenu, gnbf: !isMenu }, 'gnb-menu'"> -->
                <!-- , gnbf: !isMenu -->
                <div v-show="!isMenu && (!isCollapsed || isMobile)" class="gnb-toolbar">
                    <button type="button" @click="favoritesRef?.expandAll()">
                        <span>전체 열기</span>
                    </button>
                    <button type="button" @click="favoritesRef?.collapseAll()">
                        <span>전체 닫기</span>
                    </button>
                </div>
                <div :class="['gnb-menu', 'gnb']">
                    <ul v-if="isMenu">
                        <template v-for="menu in menuStore.menu" :key="menu.menuId">
                            <MenuListGroup v-if="menu._children && menu._children.length > 0" :isCollapsed="isCollapsed" :menuGroup="menu" @click="isCollapsed = false" />
                            <MenuListItem v-else :menuItem="menu" />
                        </template>
                    </ul>

                    <MenuListGroupFavorites ref="favoritesRef" v-else :isCollapsed="isCollapsed" :menuGroup="favoritesMenuStore.favoritesMenuList" :isFavorites="true" @click="isCollapsed = false" @rename="updateFolder" @delete="deleteMenu" @preDragEnd="onDragEnd" />
                </div>
                <div class="gnb-footer" v-if="!isMenu">
                    <button type="button" @click="addFolder">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M12 10v6" />
                            <path d="M9 13h6" />
                            <path d="M20 20a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2h-7.9a2 2 0 0 1-1.69-.9L9.6 3.9A2 2 0 0 0 7.93 3H4a2 2 0 0 0-2 2v13a2 2 0 0 0 2 2z" />
                        </svg>
                        <span>새폴더</span>
                    </button>
                    <button type="button" @click="openFavoritesExample">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7z" />
                            <path d="M14 2v4a2 2 0 0 0 2 2h4" />
                            <path d="M10 9H8" />
                            <path d="M16 13H8" />
                            <path d="M16 17H8" />
                        </svg>
                        <span>예시불러오기</span>
                    </button>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <div class="pa b0 w100p h10rem">
            <hr class="w100p h1px bcE1E6ED ma0" />
            <div class="h100p px3-2rem df aic jcsb md-jcfe">
                <button class="toggle-btn md-dn" @click.stop="isCollapsed = !isCollapsed">
                    <template v-if="isCollapsed">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                            <path d="M1.66669 10.0002H5.83335M5.00002 8.3335L6.37206 9.70553C6.53478 9.86825 6.53478 10.1321 6.37206 10.2948L5.00002 11.6668M10 3.3335H18.3334M10 10.0002H18.3334M10 16.6668H18.3334M10 6.66683H16.6667M10 13.3335H13.3334" stroke="black" stroke-linecap="round" />
                        </svg>
                    </template>
                    <template v-else>
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                            <path d="M6.66664 10.0002H2.49998M3.33331 8.3335L1.96127 9.70553C1.79855 9.86825 1.79855 10.1321 1.96127 10.2948L3.33331 11.6668M9.99998 3.3335H18.3333M9.99998 10.0002H18.3333M9.99998 16.6668H18.3333M9.99998 6.66683H16.6666M9.99998 13.3335H13.3333" stroke="black" stroke-linecap="round" />
                        </svg>
                    </template>
                </button>
                <div class="df jcsb md-w100p">
                    <div class="func df gap1rem">
                        <div class="language dn md-db">
                            <button type="button" class="df aic">
                                <i ref="currentIcon" class="df aic oh br50p"></i>
                                <span class="ml4px">{{ currentLanguage }}</span>
                            </button>
                            <ul>
                                <li v-for="lang in languages" :key="lang.code">
                                    <button type="button" class="df aic" @click="changeLanguage(lang.code)">
                                        <i v-html="lang.icon" class="df aic oh br50p"></i>
                                        <span class="ml4px">{{ lang.name }}</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="df aic gap2rem wsn">
                        <button type="button" @click="setting">
                            <span>설정</span>
                        </button>
                        <hr class="w1px h10px bcE1E6ED ma0" />
                        <button type="button" @click="logout" class="">
                            <span>로그아웃</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="favoritesFolderPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'즐겨찾기 예시'" :single="false" :fetch-data="getExample" filterKey="menuNm" useYnKey="useYn" :excluded-value="'N'" uniqueKey="menuId" @apply="applySampleDataSetMngPopup" @close="closeFavoritesPopup" :component="BaseSelectAccordionComponent" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import router from '@/router/index';

import { ref, nextTick, onMounted, watch, onUnmounted, computed /*  defineProps */ } from 'vue';
import { gsap } from 'gsap';
import { CustomEase } from 'gsap/CustomEase';

import { useMenuStore } from '@/stores/menu.js';
import { useFavoritesMenuStore } from '@/stores/favoritesMenu.js';
import { AesEncrypt /* AesDecrypt */ } from '@/utils/aes256';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { AesDecrypt } from '@/utils/aes256.js';
import BaseView from '@/components/base/BaseView';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { getFavoritesExample, saveFavoritesExample } from '@/stores/system/base/api/favoritesApi';

const menuStore = useMenuStore();
const favoritesMenuStore = useFavoritesMenuStore();
const { t, openLoading, endLoading, confirmMsg } = BaseView();

gsap.registerPlugin(CustomEase); // GSAP CustomEase 플러그인 등록
CustomEase.create('cubicEase', '0.85, 0, 0.15, 1'); // cubicEase 커스텀 이징 함수 생성

import MenuListGroup from '@/layouts/navi/MenuListGroup.vue';
import MenuListGroupFavorites from '@/layouts/navi/MenuListGroupFavorites.vue';
import MenuListItem from '@/layouts/navi/MenuListItem.vue';
import { useUserInfoStore } from '@/stores/user.js';

const userStore = useUserInfoStore();

const showSelect = ref(false);
const companyList = ref(null);
const lnb = ref(null);
const searchKeyword = ref('');
const favoritesRef = ref(null);
const menuFix = ref(false);
// isMenu: 로컬스토리지 저장 없이, 스토어에서만 관리 (새로고침 시 MENU로 초기화)
const isMenu = computed({
    get: () => menuStore.isMenuTab,
    set: v => menuStore.setIsMenuTab(v)
});

watch(
    () => isMenu.value,
    async newVal => {
        menuFix.value = !newVal;
        // 즐겨찾기에서 MENU로 전환 시, MENU의 펼침 상태(localStorage)를 즐겨찾기와 분리하기 위해 초기화
        // (MenuListGroup.vue가 nav_openMenuIds를 기반으로 자동 펼침하므로, 전환 시점에 비워서 MENU는 닫힌 상태로 시작)
        if (newVal === true) localStorage.removeItem('nav_openMenuIds');

        // 즐겨찾기 탭으로 전환되면(데이터가 이미 로드되어 있어도) 전체 펼치기
        if (newVal === false && favoritesMenuStore.isFavorite && favoritesMenuStore.favoritesMenuList?.length) {
            await nextTick();
            favoritesRef.value?.expandAll?.();
        }
    },
    { immediate: true }
);
onMounted(() => {
    favoritesMenuStore.isFavorite = favoritesMenuStore.isUserAuth();
    favoritesMenuStore.setFavoritesMenu();
});

// 즐겨찾기 메뉴가 갱신되면(= setFavoritesMenu 완료 후) 트리를 모두 펼침
watch(
    () => favoritesMenuStore.favoritesMenuList,
    async newVal => {
        if (!newVal || newVal.length === 0) return;
        if (!favoritesMenuStore.isFavorite) return;
        if (isMenu.value) return; // 즐겨찾기 탭에서만 자동 펼치기

        await nextTick();
        favoritesRef.value?.expandAll?.();
    },
    { deep: true }
);

const favoritesFolderPopup = ref(null);

const addFolder = () => {
    favoritesMenuStore.addFolder();
};

const openFavoritesExample = () => {
    menuFix.value = true;
    favoritesFolderPopup.value.onOpen();
};

const getExample = async () => {
    const responses = await getFavoritesExample();

    // 아코디언 타이틀 및 내용 만들기 위한 반복문
    const promises = responses.list.map(async item => {
        let child = '';
        item?._children?.forEach(children => {
            child += `- ${children.menuNm}\n`;
        });
        item.desc = child;
    });

    // 반복문 완료될 때까지 기다림
    await Promise.all(promises);
    return responses;
};

const applySampleDataSetMngPopup = async dataList => {
    if (dataList && dataList.length > 0) {
        confirmMsg('warning', t('msgSaveFavoritesExample'), '', { fun: applySampleData, param: dataList });
    }
};

const applySampleData = async dataList => {
    try {
        openLoading();
        await saveFavoritesExample(dataList);
        await favoritesMenuStore.setFavoritesMenu();
        await nextTick();
        if (!isMenu.value) favoritesRef.value?.expandAll?.();
        endLoading();
    } catch (e) {
        endLoading();
    } finally {
        favoritesFolderPopup.value.onClose();
        menuFix.value = true;
    }
};

//getFavoritesList
const closeFavoritesPopup = () => {
    favoritesFolderPopup.value.onClose();
    menuFix.value = true;
};

const updateFolder = async data => {
    const newData = [
        {
            menuId: data.menuId,
            menuNm: data.newName,
            menuOdr: data.menuOdr,
            faveId: data.faveId,
            loginId: userStore.loginId
        }
    ];

    await favoritesMenuStore.updateFavorites(newData);
};

const deleteMenu = async data => {
    menuFix.value = true;
    confirmMsg('warning', t('msgDelete'), '', { fun: favoritesMenuStore.deleteFavorites, param: data });
    await waitSwalClose();
    menuFix.value = false;
};

function waitSwalClose() {
    return new Promise(resolve => {
        const exists = () => !!document.querySelector('.swal2-container');

        let seenOpen = exists();

        const obs = new MutationObserver(() => {
            const open = exists();
            if (open) seenOpen = true;
            if (seenOpen && !open) {
                obs.disconnect();
                resolve();
            }
        });

        obs.observe(document.body, { childList: true, subtree: true });
    });
}

const onDragEnd = data => {
    favoritesMenuStore.updateFavoritesOdr(data, userStore.loginId);
};

const isMasterAuth = computed(() => {
    let auth = false;
    if (window.sessionStorage.getItem('grpAuthCd')) {
        auth = AesDecrypt(window.sessionStorage.getItem('grpAuthCd')) === 'MASTER';
    }
    return auth;
});
watch(searchKeyword, newVal => {
    const keyword = newVal.toLowerCase();
    if (menuStore.companyTree) {
        menuStore.companyTree.forEach(company => {
            if (company.compNm.toLowerCase().includes(keyword)) {
                company.visible = true;
            } else {
                company.visible = false;
            }
        });
    }
});
/* 🏳️ language start */
import { useI18n } from 'vue-i18n';
const { locale } = useI18n();
import { customCookies } from '@/utils/token';

const languages = ref([
    {
        code: 'ko',
        name: 'KR',
        icon: `
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="#3D58DB" />
            <mask id="mask0_46_496" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="0" y="0" width="12" height="13">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="white" />
            </mask>
            <g mask="url(#mask0_46_496)">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2093 6.9596C12.2093 6.9596 11.8725 4.13041 9.37828 3.97682C6.88408 3.82323 6.21155 6.05158 6.06762 6.64195C5.92369 7.23233 5.55941 8.80722 2.9333 8.80722C0.307197 8.80722 0.0703125 4.8026 0.0703125 4.8026V0.0805359H12.2093V6.9596Z" fill="#E31D1C" />
            </g>
        </svg>`
    },
    {
        code: 'en',
        name: 'EN',
        icon: `
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
            <mask id="mask0_46_650" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="-4" y="0" width="16" height="13">
                <rect x="-4" y="0.5" width="16" height="12" fill="white" />
            </mask>
            <g mask="url(#mask0_46_650)">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5H12V12.5H-4V0.5Z" fill="#F7FCFF" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 7.83334V8.83334H12V7.83334H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 9.66666V10.6667H12V9.66666H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 4.16666V5.16666H12V4.16666H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 11.5V12.5H12V11.5H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 6V7H12V6H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5V1.5H12V0.5H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 2.33334V3.33334H12V2.33334H-4Z" fill="#E31D1C" />
                <rect x="-4" y="0.5" width="10" height="6.5" fill="#2E42A5" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-3.13938 1.96932L-3.50232 2.22377L-3.37979 1.77096L-3.70215 1.48401H-3.2811L-3.13989 1.11447L-2.9743 1.48401H-2.61538L-2.89739 1.77096L-2.78821 2.22377L-3.13938 1.96932ZM-1.13938 1.96932L-1.50232 2.22377L-1.37979 1.77096L-1.70215 1.48401H-1.2811L-1.13989 1.11447L-0.974304 1.48401H-0.615376L-0.897394 1.77096L-0.788213 2.22377L-1.13938 1.96932ZM0.497678 2.22377L0.860618 1.96932L1.21179 2.22377L1.10261 1.77096L1.38462 1.48401H1.0257L0.86011 1.11447L0.718896 1.48401H0.297852L0.62021 1.77096L0.497678 2.22377ZM2.86062 1.96932L2.49768 2.22377L2.62021 1.77096L2.29785 1.48401H2.7189L2.86011 1.11447L3.0257 1.48401H3.38462L3.10261 1.77096L3.21179 2.22377L2.86062 1.96932ZM-3.50232 4.22377L-3.13938 3.96932L-2.78821 4.22377L-2.89739 3.77096L-2.61538 3.48401H-2.9743L-3.13989 3.11447L-3.2811 3.48401H-3.70215L-3.37979 3.77096L-3.50232 4.22377ZM-1.13938 3.96932L-1.50232 4.22377L-1.37979 3.77096L-1.70215 3.48401H-1.2811L-1.13989 3.11447L-0.974304 3.48401H-0.615376L-0.897394 3.77096L-0.788213 4.22377L-1.13938 3.96932ZM0.497678 4.22377L0.860618 3.96932L1.21179 4.22377L1.10261 3.77096L1.38462 3.48401H1.0257L0.86011 3.11447L0.718896 3.48401H0.297852L0.62021 3.77096L0.497678 4.22377ZM2.86062 3.96932L2.49768 4.22377L2.62021 3.77096L2.29785 3.48401H2.7189L2.86011 3.11447L3.0257 3.48401H3.38462L3.10261 3.77096L3.21179 4.22377L2.86062 3.96932ZM-3.50232 6.22377L-3.13938 5.96932L-2.78821 6.22377L-2.89739 5.77096L-2.61538 5.48401H-2.9743L-3.13989 5.11447L-3.2811 5.48401H-3.70215L-3.37979 5.77096L-3.50232 6.22377ZM-1.13938 5.96932L-1.50232 6.22377L-1.37979 5.77096L-1.70215 5.48401H-1.2811L-1.13989 5.11447L-0.974304 5.48401H-0.615376L-0.897394 5.77096L-0.788213 6.22377L-1.13938 5.96932ZM0.497678 6.22377L0.860618 5.96932L1.21179 6.22377L1.10261 5.77096L1.38462 5.48401H1.0257L0.86011 5.11447L0.718896 5.48401H0.297852L0.62021 5.77096L0.497678 6.22377ZM2.86062 5.96932L2.49768 6.22377L2.62021 5.77096L2.29785 5.48401H2.7189L2.86011 5.11447L3.0257 5.48401H3.38462L3.10261 5.77096L3.21179 6.22377L2.86062 5.96932ZM4.49768 2.22377L4.86062 1.96932L5.21179 2.22377L5.10261 1.77096L5.38462 1.48401H5.0257L4.86011 1.11447L4.71889 1.48401H4.29785L4.62021 1.77096L4.49768 2.22377ZM4.86062 3.96932L4.49768 4.22377L4.62021 3.77096L4.29785 3.48401H4.71889L4.86011 3.11447L5.0257 3.48401H5.38462L5.10261 3.77096L5.21179 4.22377L4.86062 3.96932ZM4.49768 6.22377L4.86062 5.96932L5.21179 6.22377L5.10261 5.77096L5.38462 5.48401H5.0257L4.86011 5.11447L4.71889 5.48401H4.29785L4.62021 5.77096L4.49768 6.22377ZM-2.13938 2.96932L-2.50232 3.22377L-2.37979 2.77096L-2.70215 2.48401H-2.2811L-2.13989 2.11447L-1.9743 2.48401H-1.61538L-1.89739 2.77096L-1.78821 3.22377L-2.13938 2.96932ZM-0.502322 3.22377L-0.139382 2.96932L0.211787 3.22377L0.102606 2.77096L0.384624 2.48401H0.0256958L-0.13989 2.11447L-0.281104 2.48401H-0.702148L-0.37979 2.77096L-0.502322 3.22377ZM1.86062 2.96932L1.49768 3.22377L1.62021 2.77096L1.29785 2.48401H1.7189L1.86011 2.11447L2.0257 2.48401H2.38462L2.10261 2.77096L2.21179 3.22377L1.86062 2.96932ZM-2.50232 5.22377L-2.13938 4.96932L-1.78821 5.22377L-1.89739 4.77096L-1.61538 4.48401H-1.9743L-2.13989 4.11447L-2.2811 4.48401H-2.70215L-2.37979 4.77096L-2.50232 5.22377ZM-0.139382 4.96932L-0.502322 5.22377L-0.37979 4.77096L-0.702148 4.48401H-0.281104L-0.13989 4.11447L0.0256958 4.48401H0.384624L0.102606 4.77096L0.211787 5.22377L-0.139382 4.96932ZM1.49768 5.22377L1.86062 4.96932L2.21179 5.22377L2.10261 4.77096L2.38462 4.48401H2.0257L1.86011 4.11447L1.7189 4.48401H1.29785L1.62021 4.77096L1.49768 5.22377ZM3.86062 2.96932L3.49768 3.22377L3.62021 2.77096L3.29785 2.48401H3.7189L3.86011 2.11447L4.0257 2.48401H4.38462L4.10261 2.77096L4.21179 3.22377L3.86062 2.96932ZM3.49768 5.22377L3.86062 4.96932L4.21179 5.22377L4.10261 4.77096L4.38462 4.48401H4.0257L3.86011 4.11447L3.7189 4.48401H3.29785L3.62021 4.77096L3.49768 5.22377Z" fill="#F7FCFF" />
            </g>
        </svg>`
    }
]);

const currentLanguage = ref(languages.value[0].code);

const currentIcon = ref(null);

const updateIcon = iconSvg => {
    if (currentIcon.value) {
        currentIcon.value.innerHTML = iconSvg; // SVG 업데이트
    }
};

const changeLanguage = langCode => {
    const selectedLang = languages.value.find(lang => lang.code === langCode);

    if (selectedLang) {
        locale.value = selectedLang.code;
        currentLanguage.value = selectedLang.name;
        updateIcon(selectedLang.icon);
        customCookies('set', 'locale', langCode);
    }
};

onMounted(() => {
    const initialLang = languages.value.find(lang => lang.code === locale.value) || languages.value[0];
    changeLanguage(initialLang.code);
});

/* 🏳️language End */

/**
 * 메뉴 접기/열기 토글
 */

import eventBus from '@/sdm/eventBus';
const isMobile = ref(false); // 모바일 여부
const isMobileOpen = ref(false); // 모바일 메뉴 열림 여부
// 메뉴 클릭으로 사이드바를 펼친 상태에서 라우터 이동(레이아웃 전환) 시에도 열림 유지
// 새로고침이나 외부 클릭으로 접힐 때는 초기화
const NAV_EXPANDED_KEY = 'nav_sidebarExpanded';
const isCollapsed = ref(sessionStorage.getItem(NAV_EXPANDED_KEY) === 'true' ? false : isMenu.value);
// isCollapsed 변경 시 실시간으로 sessionStorage 동기화
watch(isCollapsed, newVal => {
    if (!newVal) {
        sessionStorage.setItem(NAV_EXPANDED_KEY, 'true');
    } else {
        sessionStorage.removeItem(NAV_EXPANDED_KEY);
    }
});
// 새로고침 시 sessionStorage 초기화 (SPA 내부 이동은 beforeunload 미발생)
window.addEventListener('beforeunload', () => {
    sessionStorage.removeItem(NAV_EXPANDED_KEY);
});

const mobileIsOpen = status => {
    isMobileOpen.value = status;
};
const checkMobile = status => {
    isMobile.value = status;
};
onMounted(() => {
    eventBus.on('mobileIsOpen', mobileIsOpen); // Header.vue에서 상태 보냄
    eventBus.on('checkMobile', checkMobile); // Header.vue에서 상태 보냄
});
onUnmounted(() => {
    eventBus.off('mobileIsOpen', mobileIsOpen); // Header.vue에서 상태 보냄
    eventBus.off('checkMobile', checkMobile); // Header.vue에서 상태 보냄
});

// 메뉴가 아닌 곳 클릭했을 때 메뉴 접힘
const handleClickOutside = event => {
    if (!isCollapsed.value) {
        if (!menuFix.value && lnb.value && !lnb.value.contains(event.target)) {
            isCollapsed.value = true;
            isMenu.value = true;
        }
    }
};

onMounted(() => {
    document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
    document.removeEventListener('click', handleClickOutside);
});

/**
 * 사업장의 메뉴 데이터를 가져오는 함수
 */
// const fetchCompanyMenuData = async () => {
//     try {
//         const response = await fetch('/src/layouts/navi/companyMenuTree.json');
//         const data = await response.json();
//         companyMenuTree.value = data;
//     } catch (error) {
//         console.error('Error:', error);
//     }
// };

// 컴포넌트가 마운트될 때 메뉴와 회사 데이터를 가져옴
onMounted(() => {
    //fetchCompanyMenuData();
    //router.afterEach(closeNavigation)
});

/**
 * 회사를 선택했을 때 호출되는 함수
 * @param {Object} company - 선택된 회사 객체
 */
const selectCompany = company => {
    menuStore.isTaskRoute = false;
    menuStore.selectedCompany = company.compId;
    menuStore.selectedCompanyName = company.compNm;
    const aesCompId = AesEncrypt(menuStore.selectedCompany);
    sessionStorage.setItem('COMP_ID', aesCompId);
    // console.log('현재 사업장 : ', menuStore.selectedCompanyName);
    closeMenu(); // 메뉴를 부드럽게 닫기 위해 closeMenu 함수 호출
};

/**
 * 회사 선택 메뉴를 토글하는 에니메이션
 */
const toggleMenu = async () => {
    if (companyList.value) {
        if (showSelect.value) {
            closeMenu();
        } else {
            // 메뉴 열기 애니메이션
            companyList.value.style.display = 'block';
            await nextTick();
            const height = companyList.value.scrollHeight;
            gsap.fromTo(
                companyList.value,
                { height: 0, overflow: 'hidden' },
                {
                    height: height,
                    duration: 0.5,
                    ease: 'cubicEase',
                    onComplete: () => {
                        companyList.value.style.height = '';
                        showSelect.value = true;
                    }
                }
            );
        }
    }
};

/**
 * 회사 선택 메뉴를 닫는 에니메이션
 */
const closeMenu = () => {
    if (companyList.value) {
        gsap.to(companyList.value, {
            height: 0,
            duration: 0.5,
            ease: 'cubicEase',
            onComplete: () => {
                companyList.value.style.display = 'none';
                showSelect.value = false;
            }
        });
    }
};

/**
 * overayscrollbar
 */
const options = {
    className: 'os-theme-light',
    resize: 'both',
    sizeAutoCapable: true,
    paddingAbsolute: true,
    scrollbars: {
        visibility: 'auto',
        dragScrolling: true,
        clickScrolling: true,
        touchSupport: true,
        snapHandle: false
    }
};

const setting = () => {
    router.push('/myPage');
};

const logout = () => {
    router.push('/login');
};
</script>
<style lang="scss" scoped>
.favorite-toggle {
    display: flex;
    gap: 0;
    padding: 4px;
    background: #f5f6fe;
    width: 100%;

    button {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;
        padding: 8px 0;
        border: none;
        border-radius: 4px;
        font-size: 13px;
        font-weight: 500;
        color: #999;
        background: transparent;
        cursor: pointer;
        transition: all 0.25s ease;
        outline: none;

        svg {
            flex-shrink: 0;
        }

        &:hover:not(.active) {
            color: #666;
        }

        &.active {
            background: #e4e9ff;
            color: #3248f6;
            font-weight: 600;
        }
    }
}

.gnb-toolbar {
    display: flex;
    justify-content: flex-end;
    gap: 4px;
    padding: 4px;
    border-radius: 4px;
    margin: 4px;
    border: 1px solid #eee;

    button {
        display: flex;
        align-items: center;
        justify-content: center;
        background: #fafafa;
        width: 50%;
        gap: 4px;
        padding: 4px 8px;
        border: none;
        border-radius: 4px;
        font-size: 12px;
        font-weight: 400;
        color: #999;
        cursor: pointer;
        transition: all 0.2s ease;
        outline: none;
        white-space: nowrap;

        svg {
            flex-shrink: 0;
        }

        &:hover {
            color: #3248f6;
            background: #f5f6fe;
        }
    }
}

.gnb-footer {
    display: flex;
    gap: 0;
    padding: 4px;
    background: #f5f6fe;

    button {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;
        padding: 8px 0;
        border: none;
        border-radius: 4px;
        font-size: 13px;
        font-weight: 500;
        color: #999;
        background: transparent;
        cursor: pointer;
        transition: all 0.25s ease;
        outline: none;
        white-space: nowrap;

        svg {
            flex-shrink: 0;
        }

        &:hover {
            background: #e4e9ff;
            color: #3248f6;
            font-weight: 600;
        }
    }
}
</style>
