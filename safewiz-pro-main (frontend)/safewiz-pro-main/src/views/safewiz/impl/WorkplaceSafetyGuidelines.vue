<template>
    <div class="contents df fdc">
        <div class="tab">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: activeTab === authMenuList[0].tabNm }" @click="activeTab = authMenuList[0].tabNm">
                <span>위험물/유해화학물질 점검</span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: activeTab === authMenuList[1].tabNm }" @click="activeTab = authMenuList[1].tabNm">
                <span>유해화학물질 입출고</span>
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
                <div v-if="activeTab === authMenuList[0].tabNm && authMenuList[0].visible">
                    <HazMatsInspection :store="hazMatsInspectionStore" />
                </div>
                <div v-if="activeTab === authMenuList[1].tabNm && authMenuList[1].visible">
                    <HazChemsInOutManage :store="hazChemsInoutStore" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import router from '@/router';

import BaseView from '@/components/base/BaseView';
const { ref, watch, onBeforeMount, getTabMenuAuth, onMounted, confirmMsg, t, formatDate, btnSearch, btnBack, btnAdd, btnDelete } = BaseView();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];

import HazMatsInspection from '@/views/safewiz/impl/HazMatsInspection.vue';
import { useHazMatsInspectionStore } from '@/stores/safewiz/impl/hazMatsInspection.js';
const hazMatsInspectionStore = useHazMatsInspectionStore();

import HazChemsInOutManage from '@/views/safewiz/impl/HazChemsInOutManage.vue';
// [스토어]
import { useHazChemsInoutStore } from '@/stores/safewiz/impl/hazChemsInout.js';
const hazChemsInoutStore = useHazChemsInoutStore();

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

const activeTab = ref(''); // 기본 탭을 'info'로 설정
const authMenuList = ref([
    {
        menuId: 'HazMatsInspectionDetail',
        visible: false,
        tabNm: 'info'
    },
    {
        menuId: 'HazChemsInOutManageDetail',
        visible: false,
        tabNm: 'opinion'
    }
]);

onBeforeMount(() => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (activeTab.value === '' && el.visible) activeTab.value = el.tabNm;
    });
});

watch(activeTab, newVal => {
    if (newVal === 'info') {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDownload'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];
    }

    console.log('activeTab', newVal, layoutStore.useBtnList);
    // btnSearch();
});

//목록 버튼
btnBack(() => {
    if (activeTab.value === 'info') planningCtrlStore.searchParam.searchText = hazMatsInspectionStore.searchParam.writeYear;
    else planningCtrlStore.searchParam.searchText = hazChemsInoutStore.searchParam.writeYear;
    router.push({ name: 'PlanningAndControl' });
});

//조회 버튼
btnSearch(() => {
    search();
});

const search = () => {
    if (activeTab.value !== 'info') hazChemsInoutStore.btnSearch(true);
};

//추가 버튼
btnAdd(() => {
    if (activeTab.value === 'info') hazMatsInspectionStore.btnAdd(true);
    else hazChemsInoutStore.btnAdd(true);
});

btnDelete(() => {
    if (activeTab.value === 'info') hazMatsInspectionStore.btnDelete();
    else hazChemsInoutStore.btnDelete();
});

onMounted(async () => {
    console.log('Pre risk onMounted', activeTab.value, history.state.activeTab);
    activeTab.value = (await history.state.activeTab) ? history.state.activeTab : 'info';
    // search();
});
</script>
