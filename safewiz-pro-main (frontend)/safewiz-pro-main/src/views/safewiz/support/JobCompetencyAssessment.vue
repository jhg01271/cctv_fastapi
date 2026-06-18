<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 sm-grid12-6 es-grid12-12">
                    <input type="text" v-model="educationStore.searchParam.searchText" v-calendar="'yyyy'" class="datepicker w100p radius" year @input="jobCompAssessStore.btnSearch(true)" />
                </div>
                <div class="grid12-7 sm-grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="jobCompAssessStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 lg-grid12-3 sm-grid12-6 es-grid12-12">
                    <div class="toggle">
                        <button type="button" v-for="div in jobCompAssessStore.filterDivList" :key="div.id" :class="{ active: jobCompAssessStore.currentFilter === div.id }" @click="jobCompAssessStore.currentFilter = div.id">
                            <span>{{ div.name }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 조직 필터 -->
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
            <div class="df fdc gap1-2rem" v-if="jobCompAssessStore.currentFilter == 'orgn'">
                <template v-for="(key, i) in Object.keys(jobCompAssessStore.filteredByOrgnListBySearch)" :key="i">
                    <div v-if="jobCompAssessStore.filteredByOrgnListBySearch[key].length > 0" class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters1rem">
                                <template v-for="(value, index) in jobCompAssessStore.filteredByOrgnListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :type="'profile'" :title="value.hrNm" :logImg="value.logoId ? value.logoId : ''" @handle="handleEvent" @editor="btnDetail(value)" @change="chkData(value)" :btnMode="false" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="jobCompAssessStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addJobCompetencyAssessment') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
                <div class="row flex gutters1rem">
                    <div v-if="Object.keys(jobCompAssessStore.filteredByOrgnListBySearch).length === 0" class="grid12-3 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="jobCompAssessStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db mt1rem fs2rem c999999">{{ t('card_addJobCompetencyAssessment') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 직무 필터 -->
            <div class="df fdc gap1-2rem" v-else-if="jobCompAssessStore.currentFilter == 'job'">
                <template v-for="(key, i) in Object.keys(jobCompAssessStore.filteredByJobListBySearch)" :key="i">
                    <div v-if="jobCompAssessStore.filteredByJobListBySearch[key].length > 0" class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters1rem">
                                <template v-for="(value, index) in jobCompAssessStore.filteredByJobListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :data="value" :disabled="value.useYn === 'N'" :type="'profile'" :title="value.hrNm" :logImg="value.logoId ? value.logoId : ''" @handle="handleEvent" @editor="btnDetail(value)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="jobCompAssessStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addJobCompetencyAssessment') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
                <div class="row flex gutters1rem">
                    <div v-if="Object.keys(jobCompAssessStore.filteredByJobListBySearch).length === 0" class="grid12-3 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="jobCompAssessStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db mt1rem fs2rem c999999">{{ t('card_addJobCompetencyAssessment') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
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

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();

const { goRouter, t, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, btnPreYear } = BaseView();

//-----------------------------------------------
// [스토어]
import { useJobCompAssessStore } from '@/stores/safewiz/support/JobCompAssess.js';
const jobCompAssessStore = useJobCompAssessStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnPreYear'];

btnBack(() => {
    if (!jobCompAssessStore.isSaved) {
        confirmMsg('info', t('msgPreviousConfirm'), '', { fun: btnBackAction, param: '' });
        jobCompAssessStore.isSaved = true;
    } else {
        btnBackAction();
    }
});
const btnBackAction = () => {
    router.push('/EducationTraining');
};
// 조회 버튼
btnSearch(() => {
    jobCompAssessStore.btnSearch(true);
});
const btnDetail = value => {
    if (value.type === 'pre') {
        alertMsg('error', t('msgPreviousError'));
        return;
    } else if (jobCompAssessStore.isSaved === false) {
        confirmMsg('info', t('msgPreviousConfirm'), '', { fun: btnDetailAction, param: value });
    } else {
        btnDetailAction(value);
    }
};

const btnDetailAction = value => {
    jobCompAssessStore.isSaved = true;
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('JobCompetencyAssessmentDetail', param);
};
// 추가 버튼
btnAdd(() => {
    if (!jobCompAssessStore.isSaved) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    jobCompAssessStore.btnAdd();
});

// 저장 버튼
btnSave(() => {
    let param = jobCompAssessStore.getCheckedList();
    if (param.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    if (!jobCompAssessStore.isSaved && param.filter(el => el.type !== 'pre').length > 0) {
        alertMsg('error', t('msgPreviousSaveError'));
        return;
    }
    confirmMsg('info', t('msgSave'), null, { fun: save, param: true });
});
const save = notify => {
    jobCompAssessStore.btnMasterSave(notify);
};
// 삭제 버튼
btnDelete(() => {
    if (!jobCompAssessStore.isSaved) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    jobCompAssessStore.btnDelete();
});

// 출력 버튼
btnDownload(() => {
    if (!jobCompAssessStore.isSaved) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let checkedList = jobCompAssessStore.getCheckedList();
    let param = checkedList.map(el => el.docNo);

    confirmMsg('info', t('msgCheckedPrint'), '', { fun: jobCompAssessStore.btnDownload, param: param });
});

// 전년도 불러오기 버튼
btnPreYear(() => {
    if (!jobCompAssessStore.isSaved) {
        alertMsg('error', t('msgPreviousError'));
        return;
    } else {
        confirmMsg('info', t('msgPreviousLoad'), '', { fun: btnPreYearAction, param: '' });
    }
});
const btnPreYearAction = () => {
    jobCompAssessStore.btnPreYear();
};

// 초기 데이터 로딩
onMounted(async () => {
    if (!jobCompAssessStore.searchParam.writeYear) {
        //현재날짜 세팅
        // jobCompAssessStore.searchParam.writeYear = jobCompAssessStore.currentDate;
    }
    // let responses = await jobCompAssessStore.getJobAssignList(true);
    // if (responses && responses.list) {
    //     jobCompAssessStore.initData();
    // }
    jobCompAssessStore.btnSearch(true);
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { writeYear, docNo, docType, checked } = e;
    if (checked) {
        jobCompAssessStore.checkedList.push(e);
    } else {
        jobCompAssessStore.checkedList = jobCompAssessStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};

const chkData = checkData => {
    // 전년도 불러오기 모드인 경우 기존 데이터를 선택할 수 없도록
    if (!jobCompAssessStore.isSaved && checkData.type !== 'pre') {
        alertMsg('error', t('msgPreviousSaveError'));
        checkData.checked = false;
        return;
    }
};

// 검색 핸들러
const handleSearch = () => jobCompAssessStore.applyFilter();
</script>
