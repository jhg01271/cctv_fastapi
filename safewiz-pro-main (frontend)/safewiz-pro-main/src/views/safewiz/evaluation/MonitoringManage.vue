<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-model="monitoringMngStore.searchParam.searchText" v-input type="text" year v-calendar="'yyyy'" class="datepicker w100p radius" @input="monitoringMngStore.btnSearch(true)" />
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
                    <i-card-menu :menuList="monitoringMngStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { t, onMounted, btnSearch } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';

import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();

//-----------------------------------------------
// [스토어]
import { useEvaluationReportStore } from '@/stores/safewiz/evaluation/evaluationReport.js';
const evaluationReportStore = useEvaluationReportStore();

import { useComplianceEvaluationStore } from '@/stores/safewiz/evaluation/complianceEvaluation.js';
const complianceEvaluationStore = useComplianceEvaluationStore();

import { usePerformanceEvaluationStore } from '@/stores/safewiz/evaluation/performanceEvaluation.js';
const performanceEvaluationStore = usePerformanceEvaluationStore();

import { useEvaluationCorrectiveActionRequestStore } from '@/stores/safewiz/evaluation/evaluationCorrectiveActionRequest.js';
const evaluationCorrectiveActionRequestStore = useEvaluationCorrectiveActionRequestStore();
//-----------------------------------------------

onMounted(async () => {
    monitoringMngStore.btnSearch(true);
    console.log('년도', monitoringMngStore.searchParam.searchText);
});

const btnDetail = async route => {
    router.push(route);
    //페이지 이동
    if ((route = '/EvaluationReport')) {
        evaluationReportStore.writeYear = monitoringMngStore.searchParam.searchText;
    }

    if ((route = '/ComplianceEvaluationTable')) {
        complianceEvaluationStore.writeYear = monitoringMngStore.searchParam.searchText;
    }

    if ((route = '/PerformanceEvaluationTable')) {
        performanceEvaluationStore.writeYear = monitoringMngStore.searchParam.searchText;
    }

    if ((route = '/EvaluationCorrectiveActionRequest')) {
        evaluationCorrectiveActionRequestStore.writeYear = monitoringMngStore.searchParam.searchText;
    }
};
btnSearch(() => {
    monitoringMngStore.btnSearch(true);
});
</script>
