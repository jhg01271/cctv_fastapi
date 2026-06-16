<template>
    <div class="contents df fdc">
        <div class="tab">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: planningCtrlStore.safetyActiveTab === authMenuList[0].tabNm }" @click="changeTab(authMenuList[0].tabNm)">
                <span>안전점검표 관리</span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: planningCtrlStore.safetyActiveTab === authMenuList[1].tabNm }" @click="changeTab(authMenuList[1].tabNm)">
                <span>안전점검일지 관리</span>
            </button>
            <button v-if="authMenuList[2].visible" type="button" :class="{ active: planningCtrlStore.safetyActiveTab === authMenuList[2].tabNm }" @click="changeTab(authMenuList[2].tabNm)">
                <span>안전작업 허가서</span>
            </button>
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
                <div v-if="planningCtrlStore.safetyActiveTab === authMenuList[0].tabNm && authMenuList[0].visible">
                    <SafetyChecklist :store="safetyChecklistStore" />
                </div>
                <div v-if="planningCtrlStore.safetyActiveTab === authMenuList[1].tabNm && authMenuList[1].visible">
                    <SafetyInspectionLog :store="SafetyInspectionLog" />
                </div>
                <div v-if="planningCtrlStore.safetyActiveTab === authMenuList[2].tabNm && authMenuList[2].visible">
                    <PermitToWork :store="permitToWorkStore" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import router from '@/router';

import BaseView from '@/components/base/BaseView';
const { ref, watch, onMounted, onBeforeMount, getTabMenuAuth, confirmMsg, t, formatDate, btnSearch, btnBack, btnAdd, btnDelete, btnDownload } = BaseView();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useButtonListStore } from '@/stores/buttonList';

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];

import SafetyChecklist from '@/views/safewiz/impl/SafetyChecklist.vue';
import SafetyInspectionLog from '@/views/safewiz/impl/SafetyInspectionLog.vue';
import { useSafetyChecklistStore } from '@/stores/safewiz/impl/safetyChecklist.js';
const safetyChecklistStore = useSafetyChecklistStore();

import { useSafetyInspectionLogStore } from '@/stores/safewiz/impl/safetyInspectionLog.js';
const safetyInspectionLogStore = useSafetyInspectionLogStore();

import PermitToWork from '@/views/safewiz/impl/PermitToWork.vue';
import { usePermitToWorkStore } from '@/stores/safewiz/impl/permitToWork.js';
const permitToWorkStore = usePermitToWorkStore();

const authMenuList = ref([
    {
        menuId: 'SafetyChecklist',
        visible: false,
        tabNm: 'info'
    },
    {
        menuId: 'SafetyInspectionLog',
        visible: false,
        tabNm: 'SafetyInspectionLog'
    },
    {
        menuId: 'PermitToWork',
        visible: false,
        tabNm: 'opinion'
    }
]);

onBeforeMount(() => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (planningCtrlStore.safetyActiveTab === '' && el.visible) planningCtrlStore.safetyActiveTab = el.tabNm;
    });
});

const changeTab = tabNm => {
    planningCtrlStore.safetyActiveTab = tabNm;
    if (planningCtrlStore.safetyActiveTab === 'info') {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];
    } else if (planningCtrlStore.safetyActiveTab === 'SafetyInspectionLog') {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDownload'];
    } else if (planningCtrlStore.safetyActiveTab === 'opinion') {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];
    }
};

//목록 버튼
btnBack(() => {
    router.push({ name: 'PlanningAndControl' });
    if (planningCtrlStore.safetyActiveTab === 'info') planningCtrlStore.searchParam.searchText = safetyChecklistStore.searchParam.writeYear;
    else if (planningCtrlStore.safetyActiveTab === 'SafetyInspectionLog') planningCtrlStore.searchParam.searchText = permitToWorkStore.searchParam.writeYear;
    else if (planningCtrlStore.safetyActiveTab === 'opinion') planningCtrlStore.searchParam.searchText = permitToWorkStore.searchParam.writeYear;
});

//조회 버튼
btnSearch(() => {
    search();
});

const search = () => {
    // if (planningCtrlStore.safetyActiveTab === 'info') safetyChecklistStore.searchSafetyChecklist(true);
    // else if (planningCtrlStore.safetyActiveTab === 'SafetyInspectionLog') safetyInspectionLogStore.btnSearch(true);
    if (planningCtrlStore.safetyActiveTab === 'opinion') permitToWorkStore.getPermitToWorkList(true);
};

//추가 버튼
btnAdd(() => {
    if (planningCtrlStore.safetyActiveTab === 'info') safetyChecklistStore.btnAdd();
    // else if (planningCtrlStore.safetyActiveTab === 'SafetyInspectionLog') permitToWorkStore.btnAdd(true);
    else if (planningCtrlStore.safetyActiveTab === 'opinion') permitToWorkStore.btnAdd(true);
});

btnDelete(() => {
    if (planningCtrlStore.safetyActiveTab === 'info') safetyChecklistStore.btnDelete();
    // else if (planningCtrlStore.safetyActiveTab === 'SafetyInspectionLog') permitToWorkStore.btnDelete();
    else if (planningCtrlStore.safetyActiveTab === 'opinion') permitToWorkStore.btnDelete();
});

onMounted(async () => {
    console.log('Pre risk onMounted', planningCtrlStore.safetyActiveTab, history.state);
    if (!planningCtrlStore.safetyActiveTab) planningCtrlStore.safetyActiveTab = 'info';
    if (history.state?.activeTab) {
        planningCtrlStore.safetyActiveTab = history.state.activeTab;
    }
    search();
});
</script>
