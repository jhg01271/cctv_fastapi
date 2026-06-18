<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input type="text" v-model="sAndHTrainingImplStore.writeYear" @input="sAndHTrainingImplStore.btnSearch(true)" v-calendar="'yyyy'" class="datepicker w100p radius" year />
                </div>
                <div class="grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="sAndHTrainingImplStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <div class="toggle">
                        <button type="button" class="radius" v-for="div in sAndHTrainingImplStore.filterDivList" :key="div.id" :class="{ active: sAndHTrainingImplStore.currentFilter === div.id }" @click="sAndHTrainingImplStore.currentFilter = div.id">
                            <span>{{ div.name }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 캘린더 필터 -->
        <OverlayScrollbarsComponent
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="df fdc gap1-2rem" v-if="sAndHTrainingImplStore.currentFilter == 'calendar'">
                <template v-for="(key, i) in Object.keys(sAndHTrainingImplStore.filteredByMonthListBySearch)" :key="i">
                    <div class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(value, index) in sAndHTrainingImplStore.filteredByMonthListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :title="value.title" :logImg="value.logoId ? value.logoId : ''" :useApprovalStatus="true" :approvalStatus="value.approvalStatus" @handle="handleEvent" @editor="btnDetail(value)" @click="chkData(value)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="sAndHTrainingImplStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSAndHTrainingImplPlan') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>

            <!-- 과목/과정 필터 -->
            <div class="df fdc gap1-2rem" v-else-if="sAndHTrainingImplStore.currentFilter == 'subjects'">
                <template v-for="(key, i) in Object.keys(sAndHTrainingImplStore.filteredByTrainingTypeListBySearch)" :key="i">
                    <div class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(value, index) in sAndHTrainingImplStore.filteredByTrainingTypeListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :title="value.title" :logImg="value.logoId ? value.logoId : ''" :useApprovalStatus="true" :approvalStatus="value.approvalStatus" @handle="handleEvent" @editor="btnDetail(value)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="sAndHTrainingImplStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSAndHTrainingImplPlan') }}</span>
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

const { goRouter, t, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, btnPreYear, confirmMsg } = BaseView();

//-----------------------------------------------
// [스토어]
import { useSAndHTrainingImplStore } from '@/stores/safewiz/support/SAndHTrainingImpl.js';
const sAndHTrainingImplStore = useSAndHTrainingImplStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnPreYear'];

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();

//목록으로
btnBack(() => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'),'', { fun: btnBackAction, param : ''})
    }else{
        btnBackAction()
    }
});

const btnBackAction = () => {
    router.push('/EducationTraining');
    educationStore.searchParam.searchText = sAndHTrainingImplStore.searchParam.writeYear;
}

// 조회 버튼
btnSearch(() => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'),'', { fun: btnSearchAction, param : ''})
    }else{
        sAndHTrainingImplStore.btnSearch(true);
    }
});

const btnSearchAction = () => {
    sAndHTrainingImplStore.loadPreviousYn = false
    sAndHTrainingImplStore.btnSearch(true);
}
// 저장 버튼
btnSave(() => {
    if (!sAndHTrainingImplStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousSave'));
        return;
    }
    let param = sAndHTrainingImplStore.getCheckedList();
    if (param.length === 0) {
        alertMsg('error', t('msgNoItem'));
        return;
    }
    if (param.filter(el => el.writeYear === sAndHTrainingImplStore.searchParam.writeYear).length > 0) {
        alertMsg('error', t('msgPreviousSaveError'));
        return;
    }
    sAndHTrainingImplStore.btnSavePreviousYear();
});
const btnDetail = value => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }else{
        btnDetailAction(value)
    }
};
const btnDetailAction = value => {
    sAndHTrainingImplStore.loadPreviousYn = false
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('SAndHTrainingImplPlanDetail', param);
}

// 추가 버튼
btnAdd(() => {
    sAndHTrainingImplStore.btnAdd();
});

// 삭제 버튼
btnDelete(() => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    sAndHTrainingImplStore.btnDelete();
});

// 출력 버튼
btnDownload(() => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let checkedList = sAndHTrainingImplStore.getCheckedList();
    let param = checkedList.map(el => el.docNo);
    sAndHTrainingImplStore.btnDownload(param);
});

// 전년도 불러오기 버튼
btnPreYear(() => {
    if (sAndHTrainingImplStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }else{
        confirmMsg('info', t('msgPreviousLoad'),'', { fun: sAndHTrainingImplStore.btnPreYear, param : ''})
    }
});

// 초기 데이터 로딩
onMounted(async () => {
    sAndHTrainingImplStore.loadPreviousYn = false
    if (!sAndHTrainingImplStore.searchParam.writeYear) {
        //현재날짜 세팅
        sAndHTrainingImplStore.searchParam.writeYear = sAndHApiStore.currentDate;
    }
    sAndHTrainingImplStore.btnSearch(true);
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { writeYear, docNo, docType, checked } = e;
    if (checked) {
        sAndHTrainingImplStore.checkedList.push(e);
    } else {
        sAndHTrainingImplStore.checkedList = sAndHTrainingImplStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};

// 값이 바뀌면 해당 데이터 checked
const chkData = async (data) => {
    if(sAndHTrainingImplStore.loadPreviousYn && data.writeYear === sAndHTrainingImplStore.searchParam.writeYear){
        alertMsg('error', t('msgPreviousSaveError'));
        setTimeout(() => { data.checked = false}, 1)
    }
};

// 검색 핸들러
const handleSearch = () => sAndHTrainingImplStore.applyFilter();
</script>
