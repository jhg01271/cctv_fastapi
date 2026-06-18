<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="hseKeyPerformanceStore.writeYear" class="datepicker w20rem radius es-w100p" @input="init()" />
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
                    <i-card-menu :menuList="menuList" @click="btnDetail" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router/index.js';
import BaseView from '@/components/base/BaseView';
import { watchEffect } from 'vue';
import { getHsePerformance } from '@/stores/safewiz/impl/api/HseKeyPerformanceResultsApi';
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSearch'];
import { useHseKeyPerformanceStore } from '@/stores/safewiz/impl/HseKeyPerformanceResults';
const hseKeyPerformanceStore = useHseKeyPerformanceStore();

const { t, onMounted, btnSearch, computed, ref } = BaseView();

const menuList = ref([]);

onMounted(async () => {
    init();
});

const init = async () => {
    await getHsePerformance({ writeYear: hseKeyPerformanceStore.writeYear }, true).then(res => {
        menuList.value = res.list;
    });
};

btnSearch(() => {
    init();
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
};
</script>
