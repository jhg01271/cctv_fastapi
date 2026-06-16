<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-model="continualImprovementStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="continualImprovementStore.btnSearch(true)" />
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
            <!-- <template v-for="(menu, index) in continualImprovementStore.menuList" :key="menu.router"> -->
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="continualImprovementStore.menuList" @click="btnDetail" />
                </div>
            </div>

            <!-- </template> -->
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

//-----------------------------------------------
// [스토어]

import { useContinualImprovementStore } from '@/stores/safewiz/improvement/continualImprovement.js';
const continualImprovementStore = useContinualImprovementStore();
/*
import { useSignatureStore } from '@/stores/signature';
const sigatureStore = useSignatureStore();
*/
//-----------------------------------------------
onMounted(async () => {
    continualImprovementStore.btnSearch(true);
    console.log('년도', continualImprovementStore.searchParam.searchText);
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
    if ((route = '/ObjectivesAndActionPlan')) {
        //actionPlanStore.writeYear = continualImprovementStore.searchParam.searchText;
    } else if ((route = '/DetailedActionPlan')) {
        //detailedActionStore.writeYear = continualImprovementStore.searchParam.searchText;
    }
};
btnSearch(() => {
    continualImprovementStore.btnSearch(true);
});
</script>
