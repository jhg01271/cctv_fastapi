<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-model="planningCtrlStore.searchParam.searchText" v-input type="text" year v-calendar="'yyyy'" class="datepicker w100p radius" @input="planningCtrlStore.btnSearch(true)" />
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
                    <i-card-menu :menuList="planningCtrlStore.menuList" @click="btnDetail" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const {onMounted,  btnSearch } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

//-----------------------------------------------
// [스토어]
import { useWorkerHealthMgmtStore } from '@/stores/safewiz/impl/workerHealthMgmt.js';
const workerHealthMgmtStore = useWorkerHealthMgmtStore();

import { useSafetyChecklistStore } from '@/stores/safewiz/impl/safetyChecklist.js';
const safetyChecklistStore = useSafetyChecklistStore();

import { usePermitToWorkStore } from '@/stores/safewiz/impl/permitToWork.js';
const permitToWorkStore = usePermitToWorkStore();

import { useHazMatsInspectionStore } from '@/stores/safewiz/impl/hazMatsInspection.js';
const hazMatsInspectionStore = useHazMatsInspectionStore();

import { useHazChemsInoutStore } from '@/stores/safewiz/impl/hazChemsInout.js';
const hazChemsInoutStore = useHazChemsInoutStore();
//-----------------------------------------------

onMounted(async () => {
    planningCtrlStore.btnSearch(true);
});

const btnDetail = async route => {
    router.push(route);
    //페이지 이동
    if ((route === '/WorkerHealthMgmtGuidelines')) {
        workerHealthMgmtStore.searchParam.writeYear = planningCtrlStore.searchParam.searchText;
    }
    if ((route === '/SafetyMgmtGuidelines')) {
        safetyChecklistStore.searchParam.writeYear = planningCtrlStore.searchParam.searchText;
        permitToWorkStore.searchParam.writeYear = planningCtrlStore.searchParam.searchText;
    }
    if ((route === '/WorkplaceSafetyGuidelines')) {
        hazMatsInspectionStore.searchParam.writeYear = planningCtrlStore.searchParam.searchText;
        hazChemsInoutStore.searchParam.writeYear = planningCtrlStore.searchParam.searchText;
    }
};
btnSearch(() => {
    planningCtrlStore.btnSearch(true);
});
</script>
