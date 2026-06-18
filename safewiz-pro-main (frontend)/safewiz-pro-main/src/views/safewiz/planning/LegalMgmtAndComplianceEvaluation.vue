<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="field">
                        <!--                        <label for>조회년도</label>-->
                        <input v-model="LegalMgmtAndComplianceStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="LegalMgmtAndComplianceStore.btnSearch(true)" />
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
                    <i-card-menu :menuList="LegalMgmtAndComplianceStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import router from '@/router/index.js';
const { btnSearch, onMounted, t, goRouter } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];

import { useLegalMgmtAndComplianceStore } from '@/stores/safewiz/planning/LegalMgmtAndComplianceEvaluation.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const LegalMgmtAndComplianceStore = useLegalMgmtAndComplianceStore();

const btnDetail = async route => {
    const param = {
        writeYear: LegalMgmtAndComplianceStore.searchParam.searchText
    }
    goRouter(`${route.toString().replace('/', '')}`, param);

};

onMounted(async () => {
    await LegalMgmtAndComplianceStore.btnSearch(true);
});

// 조회 버튼
btnSearch(() => LegalMgmtAndComplianceStore.btnSearch(true));
</script>

<style scoped>
dl.no-before dt::before {
    display: none; /* ::before 스타일 제거 */
}
</style>
