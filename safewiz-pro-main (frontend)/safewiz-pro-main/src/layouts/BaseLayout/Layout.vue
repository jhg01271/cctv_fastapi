<template>
    <div id="wrap">
        <BaseNavigation />
        <Header />
        <!-- 검토용 네비게이션 -->
        <!-- <TabNavigation /> -->
        <loadingPanel />
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover'
                }
            }"
        >
            <div class="container df fdc">
                <!-- 공통 타이틀 및 로케이션(breadcrumbs) -->
                <div class="location">
                    <div class="row flex">
                        <div class="grid12-4 sm-grid12-12">
                            <!--                            <h2>{{ router.currentRoute.value.meta.menuNm ? t(router.currentRoute.value.meta.menuNm) : '' }}</h2>-->
                            <h2 class="dif lg-aic gap6px">
                                {{ router.currentRoute.value.meta.menuNm }}
                                <div v-if="router.currentRoute.value.meta.menuId != null && favoritesMenuStore.isFavorite && router.currentRoute.value.meta.hasChildren" class="dif lg-aic gap6px ul-pr md-dib">
                                    <button :class="{ isyfm: isFavorites, isnfm: !isFavorites }" type="button" @click="favoritesIconClick">
                                        <svg v-show="!isFavorites" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#cccccc">
                                            <path
                                                d="m305-704 112-145q12-16 28.5-23.5T480-880q18
                                0 34.5 7.5T543-849l112 145 170 57q26 8 41
                                29.5t15 47.5q0 12-3.5 24T866-523L756-367l4
                                164q1 35-23 59t-56 24q-2 0-22-3l-179-50-179
                                50q-5 2-11 2.5t-11 .5q-32 0-56-24t-23-59l4-165L95-523
                                q-8-11-11.5-23T80-570q0-25 14.5-46.5T135-647l170-57Z"
                                            ></path>
                                        </svg>
                                        <svg v-show="isFavorites" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#ffcc00">
                                            <path
                                                d="m305-704 112-145q12-16 28.5-23.5T480-880q18
                                0 34.5 7.5T543-849l112 145 170 57q26 8 41
                                29.5t15 47.5q0 12-3.5 24T866-523L756-367l4
                                164q1 35-23 59t-56 24q-2 0-22-3l-179-50-179
                                50q-5 2-11 2.5t-11 .5q-32 0-56-24t-23-59l4-165L95-523
                                q-8-11-11.5-23T80-570q0-25 14.5-46.5T135-647l170-57Z"
                                            ></path>
                                        </svg>
                                    </button>
                                </div>
                            </h2>
                        </div>
                        <div class="grid12-7 sm-dn">
                            <ul class="df jcfe aic gap3-6rem pr3rem md-dn">
                                <li>
                                    <a style="color: black">
                                        {{ router.currentRoute.value.meta.fullPath }}
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="grid12-1 sm-dn">
                            <ul class="df jcfe aic gap3-6rem">
                                <li>
                                    <a class="init m" @click="goDashboard">
                                        <svg width="20" height="19" viewBox="0 0 20 19" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M2 7.5V7.5C1.10883 7.5 0.550134 6.53727 0.992278 5.76351L3.07865 2.11236C3.41422 1.52512 3.582 1.2315 3.81776 1.01789C4.02635 0.828892 4.27249 0.686053 4.54007 0.598714C4.84251 0.5 5.18068 0.5 5.85703 0.5H14.143C14.8193 0.5 15.1575 0.5 15.4599 0.598714C15.7275 0.686053 15.9737 0.828892 16.1822 1.01789C16.418 1.2315 16.5858 1.52512 16.9213 2.11236L19.0077 5.76351C19.4499 6.53727 18.8912 7.5 18 7.5V7.5M2 7.5V15.3C2 16.4201 2 16.9802 2.21799 17.408C2.40973 17.7843 2.71569 18.0903 3.09202 18.282C3.51984 18.5 4.0799 18.5 5.2 18.5H8M2 7.5H10H18M18 7.5V15.3C18 16.4201 18 16.9802 17.782 17.408C17.5903 17.7843 17.2843 18.0903 16.908 18.282C16.4802 18.5 15.9201 18.5 14.8 18.5H13H12M8 18.5V13.1C8 12.5399 8 12.2599 8.10899 12.046C8.20487 11.8578 8.35785 11.7049 8.54601 11.609C8.75992 11.5 9.03995 11.5 9.6 11.5H10.4C10.9601 11.5 11.2401 11.5 11.454 11.609C11.6422 11.7049 11.7951 11.8578 11.891 12.046C12 12.2599 12 12.5399 12 13.1V18.5M8 18.5H12" stroke="#999999" stroke-linecap="round" />
                                        </svg>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <RouterView v-slot="{ Component, route }" :key="viewKey">
                    <component :is="Component" :key="route?.fullPath" />
                </RouterView>
                <buttonList />
                <iQuickMenuBtn />
            </div>
            <teleport to="body">
                <i-PopupDialog ref="favoritesPopup">
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup title="즐겨찾기" single :fetch-data="getFavorites" @close="addFavoritesPopup" filterKey="menuNm" useYnKey="useYn" :excluded-value="'N'" />
                    </div>
                </i-PopupDialog>
            </teleport>
        </OverlayScrollbarsComponent>
    </div>
    <ToastPopup />
</template>

<script setup>
import router from '@/router';
import buttonList from '@/components/common/iButtonList.vue';
import iQuickMenuBtn from '@/components/common/iQuickMenuBtn.vue';
import loadingPanel from '@/components/common/loadingPanel.vue';
import Header from './Header.vue';
import BaseNavigation from './BaseNavigation.vue';
import { useFavoritesMenuStore } from '@/stores/favoritesMenu.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getFavoritesSide } from '@/stores/system/base/api/favoritesApi';

import BaseView from '@/components/base/BaseView';
const { t, endLoading, openLoading, confirmMsg } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
onMounted(() => {
    // console.log(router);
});
const { ref } = BaseView();

const viewKey = ref(0);
const goDashboard = () => {
    router.push('/dashboard');
};

import ToastPopup from '@/sdm/components/popup/ToastPopup.vue';
import { onMounted, computed } from 'vue';
//----- 즐겨찾기 추가
const favoritesMenuStore = useFavoritesMenuStore();

const isFavorites = computed(() => {
    const menuId = router.currentRoute.value.meta.menuId;
    if (!menuId) return false;

    return favoritesMenuStore.checkFavoritesMenu(menuId);
});

const favoritesPopup = ref(null);
const favoritesIconClick = () => {
    if (!isFavorites.value) {
        favoritesPopup.value.onOpen();
    } else {
        confirmMsg('warning', t('msgDelete'), '', { fun: favoritesMenuStore.deleteFavorites, param: router.currentRoute.value.meta.menuId });
    }
};
//---------- 저장
const addFavoritesPopup = async menu => {
    if (menu.length == 0) {
        favoritesPopup.value.onClose();
        favoritesMenuStore.setFavoritesMenu();
        return;
    }
    const newData = [
        {
            menuId: router.currentRoute.value.meta.menuId,
            menuIcon: router.currentRoute.value.meta.menuIcon,
            menuNm: router.currentRoute.value.meta.menuNm,
            menuNmEng: router.currentRoute.value.meta.menuNmEng,
            menuOdr: router.currentRoute.value.meta.menuOdr,
            param: router.currentRoute.value.meta.param,
            routerNm: router.currentRoute.value.meta.routerNm,
            routerPath: router.currentRoute.value.meta.routerPath,
            upMenuId: router.currentRoute.value.meta.upMenuId,
            viewPath: router.currentRoute.value.meta.viewPath,
            useYn: 'Y',
            newYn: 'N',
            compId: router.currentRoute.value.meta.compId,
            hrId: router.currentRoute.value.meta.hrId,
            parentsMenuId: menu[0].menuId,
            parentsMenuNm: menu[0].menuNm,
            parentsMenuOdr: menu[0].menuOdr
        }
    ];
    saveActionFavoritesPopup(newData);
};

const saveActionFavoritesPopup = async selectMenu => {
    try {
        openLoading();
        await favoritesMenuStore.addFavorites(selectMenu);
        favoritesPopup.value.onClose();
    } catch (e) {
        endLoading();
    } finally {
        endLoading();
    }
};

const searchParam = ref({
    searchCd: 'ADD'
});
const getFavorites = async () => {
    const responses = await getFavoritesSide(searchParam.value);
    return responses;
};
</script>
<style lang="scss" scoped>
.isnfm {
    > i {
        background: #fff;
        border-color: #fff;

        :deep(svg *) {
            &[fill] {
                fill: #3248f6 !important;
            }

            &[stroke] {
                stroke: #3248f6 !important;
            }
        }
    }
}

.isyfm {
    > i {
        background: #fff;
        border-color: #fff;

        :deep(svg *) {
            &[fill] {
                fill: #f1e20d !important;
            }

            &[stroke] {
                stroke: #f1e20d !important;
            }
        }
    }
}
</style>
