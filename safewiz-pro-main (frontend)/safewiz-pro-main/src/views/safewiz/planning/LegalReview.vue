<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="LegalReviewStore.searchTerm" @keyup.enter="LegalReviewStore.applyFilter" />
                <button type="submit" class="shrink0" @click.stop="LegalReviewStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
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
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px">
                <div v-for="(segment, index) in LegalReviewStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="LegalReviewStore.toggleAccordion" :class="{ active: LegalReviewStore.legalReviewSegments[index] }">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.dataList" :key="idx">
                                    <i-card :v-model="data.docNo" :title="data.legalReviewNm" :data="data" :useApprovalStatus="true" :approvalStatus="data.approvalStatus" :type="'basic'" :disabled="data.useYn === 'N'" @handle="handleEvent" @editor="btnDetail" />
                                </template>
                                <!-- 추가 -->
                                <!-- <div v-if="isCanAdd(segment.year)" class="grid12-4 ul-grid12-6 lg-grid12-12"> -->
                                <!-- 연도제한이 걸려있었으나 해제 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addRegalReview') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 데이터가 없을시 추가버튼 생성 -->
                <div v-if="!Object.values(LegalReviewStore.segments).length > 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db mt1rem fs2rem c999999">{{ t('card_addRegalReview') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import router from '@/router/index.js';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { ref, getCurrentDate, onMounted, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, t, confirmMsg, goRouter, setRouterParam } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

import { useLegalReviewStore } from '@/stores/safewiz/planning/LegalReview.js';
const LegalReviewStore = useLegalReviewStore();

import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

const accordionRefs = ref([]);

const handleEvent = e => {
    if (e.checked) {
        //console.log('check_in')
        LegalReviewStore.checkedList.push(e);
    } else {
        //console.log('check_out')
        LegalReviewStore.checkedList = LegalReviewStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
    }
};

//상세보기 버튼
const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };
    LegalReviewStore.btnDetail(e);
    goRouter('LegalReviewDetail', param);

};

onMounted(async () => {
    const param = setRouterParam(); // 메뉴에서 선택한 년도 호출

    await LegalReviewStore.initSearchDate();
    await LegalReviewStore.btnSearch(true, param?.writeYear);
});

btnBack(() => {
    router.push('/LegalMgmtAndComplianceEvaluation');
});

btnSearch(() => {
    LegalReviewStore.btnSearch(true);
    LegalReviewStore.searchTerm = '';
});

btnAdd(() => {
    LegalReviewStore.btnDetail();
    router.push('/LegalReviewDetail');
});

const btnNew = () => {
    LegalReviewStore.btnDetail();
    router.push('/LegalReviewDetail');
};

btnDelete(async () => {
    LegalReviewStore.btnDelete();
});

btnDownload(() => {
    LegalReviewStore.downloadReport();
});

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const isCanAdd = year => {
    if (year.substring(0, 4) === getCurrentDate().substring(0, 4)) return true;
    else return false;
};
</script>
