<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input type="text" v-model="emergencyResponseStore.searchParam.searchText" v-calendar="'yyyy'" class="datepicker w100p radius" year @input="emergencyResponseStore.btnSearch(true)" />
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
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="emergencyResponseStore.menuList" @click="btnDetail" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, t, onMounted, formatDate, btnSearch } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';

import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();

//-----------------------------------------------
// [스토어]
//-----------------------------------------------
onMounted(async () => {
    emergencyResponseStore.btnSearch(true);
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
};
btnSearch(() => {
    emergencyResponseStore.btnSearch(true);
});
</script>
