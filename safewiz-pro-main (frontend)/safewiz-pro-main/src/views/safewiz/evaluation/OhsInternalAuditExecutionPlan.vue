<template>
    <div class="contents df fdc">
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
                <div class="control-field ui form mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-12">
                            <input type="text" v-calendar="'yyyy'" v-model="internalAuditStore.writeYear" class="datepicker w100p radius" year @input="auditStore.btnSearch(true)" />
                        </div>
                        <div class="grid12-9 es-grid12-12">
                            <div class="df bcffffff">
                                <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                                <button type="button" class="shrink0" @click="applyFilter">
                                    <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="df fdc gap1-2rem">
                    <div class="row flex gutters2rem">
                        <template v-for="(data, dataIndex) in auditStore.filteredData" :key="dataIndex">
                            <i-card :v-model="data.checked" :data="data" :disabled="data.useYn === 'N'" :type="'basic'" :title="data.auditNm" :useApprovalStatus="true" :approvalStatus="data.approvalStatus" @editor="btnDetail" />
                        </template>
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="btnNew(data)">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addOhsInternalAuditExecutionPlan') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import _ from 'lodash';
import BaseView from '@/components/base/BaseView';
const { t, getCompId, onMounted, formatDate, router, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, downloadReport, goRouter } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

import { useAuditExecutionPlanStore } from '@/stores/safewiz/evaluation/auditExecutionPlan.js';
const auditStore = useAuditExecutionPlanStore();

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const internalAuditStore = useAuditStore();

onMounted(async () => {
    auditStore.btnSearch(true);
});

// 버튼
btnBack(() => {
    router.push('/InternalAudit');
});

btnSearch(() => {
    auditStore.btnSearch(true);
});

const btnDetail = data => {
    let param = {
        writeYear: data.writeYear,
        docType: data.docType,
        docNo: data.docNo
    };

    goRouter('OhsInternalAuditExecutionPlanDetail', param);
};
btnAdd(async () => {
    await auditStore.initInputForm();
    router.push('/OhsInternalAuditExecutionPlanDetail');
});
const btnNew = async item => {
    await auditStore.initInputForm();
    router.push('/OhsInternalAuditExecutionPlanDetail');
};
btnDelete(() => {
    let checkedList = auditStore.filteredData.filter(el => el.checked);
    auditStore.btnDelete(checkedList);
});
btnDownload(() => {
    let checkedList = auditStore.filteredData.filter(el => el.checked);
    auditStore.btnDownload(checkedList);
});

const searchTerm = ref('');
const applyFilter = () => {
    let filteringData = [];
    filteringData = auditStore.dataList.filter(data => data.auditNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(data.writeDt).toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.auditDivNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.auditOrgnList.length.toString().toLowerCase().includes(searchTerm.value.toLowerCase()));
    auditStore.filteredData = filteringData;
};
</script>
