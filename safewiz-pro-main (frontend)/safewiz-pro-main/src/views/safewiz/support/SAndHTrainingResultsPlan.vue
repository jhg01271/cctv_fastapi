<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input type="text" v-model="sAndHTrainingResultStore.writeYear" @input="sAndHTrainingResultStore.btnSearch(true)" v-calendar="'yyyy'" class="datepicker w100p radius" year />
                </div>
                <div class="grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="sAndHTrainingResultStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <div class="toggle">
                        <button type="button" v-for="div in sAndHTrainingResultStore.filterDivList" :key="div.id" :class="{ active: sAndHTrainingResultStore.currentFilter === div.id }" @click="sAndHTrainingResultStore.currentFilter = div.id">
                            <span>{{ div.name }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 캘린더 필터 -->
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="df fdc gap1-2rem" v-if="sAndHTrainingResultStore.currentFilter == 'calendar'">
                <template v-for="(key, i) in Object.keys(sAndHTrainingResultStore.filteredByOrgnListBySearch)" :key="i">
                    <div class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(value, index) in sAndHTrainingResultStore.filteredByOrgnListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :title="value.title" :logImg="value.logoId ? value.logoId : ''" :useApprovalStatus="true" :approvalStatus="value.approvalStatus" @handle="handleEvent" @editor="btnDetail(value)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="sAndHTrainingResultStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSAndHTrainingResultsPlan') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>

            <!-- 과목/과정 필터 -->
            <div class="df fdc gap1-2rem" v-else-if="sAndHTrainingResultStore.currentFilter == 'subjects'">
                <template v-for="(key, i) in Object.keys(sAndHTrainingResultStore.filteredByJobListBySearch)" :key="i">
                    <div class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(value, index) in sAndHTrainingResultStore.filteredByJobListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :title="value.title" :logImg="value.logoId ? value.logoId : ''" :useApprovalStatus="true" :approvalStatus="value.approvalStatus" @handle="handleEvent" @editor="btnDetail(value)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="sAndHTrainingResultStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSAndHTrainingResultsPlan') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { onMounted } from 'vue';
import { useButtonListStore } from '@/stores/buttonList';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';

const { goRouter, alertMsg, t, btnBack, btnSearch, btnAdd, btnDelete, btnDownload } = BaseView();

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();

//-----------------------------------------------
//-----------------------------------------------
// [스토어]
import { useSAndHTrainingResultStore } from '@/stores/safewiz/support/SAndHTrainingResult.js';
const sAndHTrainingResultStore = useSAndHTrainingResultStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

btnBack(() => {
    router.push('/EducationTraining');
    educationStore.searchParam.searchText = sAndHTrainingResultStore.searchParam.writeYear;
});

// 조회 버튼
btnSearch(() => {
    sAndHTrainingResultStore.btnSearch(true);
});

const btnDetail = value => {
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('SAndHTrainingResultsPlanDetail', param);
};
// 추가 버튼
btnAdd(() => {
    if (sAndHTrainingResultStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    sAndHTrainingResultStore.btnAdd();
});

// 저장 버튼 - 전년도 불러오기 기능 삭제로 필요없음
// btnSave(() => {
//     if (!sAndHTrainingResultStore.loadPreviousYn) {
//         alertMsg('error', t('msgPreviousSave'));
//         return;
//     }

//     let param = sAndHTrainingResultStore.getCheckedList();
//     if (param.length === 0) {
//         alertMsg('error', t('msgDeletedItem'));
//         return;
//     }
//     if (param.filter(el => el.writeYear === sAndHTrainingResultStore.searchParam.writeYear).length > 0) {
//         alertMsg(t('msgPreviousSaveError'), 'error');
//         return;
//     }
//     sAndHTrainingResultStore.btnSavePreviousYear();
// });

// 삭제 버튼
btnDelete(() => {
    if (sAndHTrainingResultStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    sAndHTrainingResultStore.btnDelete();
});

// 출력 버튼
btnDownload(() => {
    if (sAndHTrainingResultStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let checkedList = sAndHTrainingResultStore.getCheckedList();
    let param = checkedList.map(el => el.docNo);
    sAndHTrainingResultStore.btnDownload(param);
});

// 전년도 불러오기 버튼 - 전년도 불러오기 기능 삭제로 필요없음
// btnPreYear(() => {
//     if (sAndHTrainingResultStore.loadPreviousYn) {
//         alertMsg('error', t('msgPreviousError'));
//         return;
//     }
//     sAndHTrainingResultStore.btnPreYear();
// });

// 초기 데이터 로딩
onMounted(async () => {
    if (!sAndHTrainingResultStore.searchParam.writeYear) {
        //현재날짜 세팅
        sAndHTrainingResultStore.searchParam.writeYear = sAndHApiStore.currentDate;
    }
    sAndHTrainingResultStore.btnSearch(true);
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { writeYear, docNo, docType, checked } = e;
    if (checked) {
        sAndHTrainingResultStore.checkedList.push(e);
    } else {
        sAndHTrainingResultStore.checkedList = sAndHTrainingResultStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};

// 검색 핸들러
const handleSearch = () => sAndHTrainingResultStore.applyFilter();
</script>
