<template>
    <!-- 콘텐츠 영역 -->
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row gutters1rem">
                <div class="grid12-3 sm-grid12-4 us-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="workerOpinionStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" @input="searchMain" />
                </div>
                <div class="grid12-9 sm-grid12-8 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="workerOpinionStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
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
            <div class="row flex gutters2rem">
                <template v-for="workerOpinion in workerOpinionStore.dataFilterList" :key="workerOpinion.jobAssignId">
                    <i-card :v-model="workerOpinion.checked" :data="workerOpinion" :disabled="workerOpinion.useYn === 'N'" :title="workerOpinion.hrNm" @handle="handleEvent" @editor="btnDetail(workerOpinion)" :approvalStatus="workerOpinion.approvalStatus" />
                </template>

                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="workerOpinionStore.btnAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <span class="db fs2rem c999999">{{ t('card_addWorkersOpinionsOnHazard') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { onMounted, defineProps } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const { goRouter, t, btnSearch, btnBack, btnAdd, btnDelete, btnDownload } = BaseView();
const props = defineProps({
    store: { type: Object, default: Object } // Store
});

const workerOpinionStore = props.store;
const riskAStore = useRiskAStore();
// 초기 데이터 로딩
onMounted(async () => {
    if (riskAStore.searchParam.searchText) {
        workerOpinionStore.searchParam.writeYear = riskAStore.searchParam.searchText;
    }
    if (!workerOpinionStore.searchParam.writeYear) {
        //현재날짜 세팅
        workerOpinionStore.searchParam.writeYear = workerOpinionStore.currentDate.slice(0, 4);
    }
    let responses = await workerOpinionStore.getWorkerOpinionList(true);
    if (responses && responses.list) {
        workerOpinionStore.initData();
    }
});
//목록 버튼
btnBack(() => {
    router.push({ name: 'RiskAssessment' });
});

//조회 버튼
btnSearch(() => {
    workerOpinionStore.getWorkerOpinionList(true);
});

//추가 버튼
btnAdd(() => {
    workerOpinionStore.btnAdd(true);
});

btnDelete(() => {
    workerOpinionStore.btnDelete();
});

btnDownload(() => {
    workerOpinionStore.btnPrint();
});

// 마스터 테이블 재조회
const searchMain = () => {
    workerOpinionStore.getWorkerOpinionList(true);
    riskAStore.searchParam.searchText = workerOpinionStore.searchParam.writeYear;
};

// 검색 핸들러
const handleSearch = () => workerOpinionStore.applyFilter();

// 상세보기 버튼
const btnDetail = async value => {
    workerOpinionStore.initInputForm();
    workerOpinionStore.searchParam.hrId = value.hrId;
    workerOpinionStore.inputForm.cmd = 'U';
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('WorkersOpinionsOnHazardsDetail', param);
};
</script>
