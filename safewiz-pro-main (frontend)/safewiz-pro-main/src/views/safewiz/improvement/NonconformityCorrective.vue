<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="nonconformityCStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" @input="nonconformityCStore.getCardListAndSearch()" />
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="nonconformityCStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useNonconformityCStore } from '@/stores/safewiz/improvement/nonconformityCorrective.js';
import router from '@/router/index.js';
import { watchEffect } from 'vue';
const { t, onMounted, btnSearch, toastPopup } = BaseView();
const nonconformityCStore = useNonconformityCStore();
//오른쪽 layout
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];

onMounted(async () => {
    await nonconformityCStore.getCardListAndSearch();
});

watchEffect(() => {});

const btnDetail = async route => {
    router.push(route);
};

btnSearch(async () => {
    await nonconformityCStore.getCardListAndSearch();
});
</script>
