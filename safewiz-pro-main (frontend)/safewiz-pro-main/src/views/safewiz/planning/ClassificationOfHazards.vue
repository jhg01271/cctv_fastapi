<!--
작성자 : SI3팀 김현재
작성일 : 2024.10.28
화면명 : 유해 위험요인 분류
-->
<template>
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="mb2-2rem">
            <div class="form ui row flex gutters1rem">
                <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="classificationOfHardsStore.writeYear" @input="searchData" class="datepicker w20rem radius es-w100p" @mousedown="modifyYearFilter" />
                    </div>
                </div>
                <div class="grid12-9 ul-grid12-8 lg-grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="handleSearch" v-model="searchTerm" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <div class="row flex gutters1rem">
                <template v-for="classificationOfHards in classificationOfHardsStore.dataFilterList" :key="classificationOfHards.docNo">
                    <i-card :v-model="classificationOfHards.checked" :modelValue="classificationOfHards.checked" :data="classificationOfHards" :type="'basic'" :title="classificationOfHards.processNm" @handle="handleEvent" @editor="btnDetail" :disabled="classificationOfHards.useYn == 'N'" @change="chkData(classificationOfHards)" />
                </template>

                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="addDetail">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <span class="db fs2rem c999999">{{ t('card_addClassificationHazards') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import ClassificationOfHazardsDetail from './ClassificationOfHazardsDetail.vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useClassificationOfHardsStore } from '@/stores/safewiz/planning/classificationOfHazrds';
import { searchSystemCode } from '@/stores/system/setting/api/SystemCode';
import customSelectPopup from '@/views/system/base/popup/CustomSelectPopup.vue';
import { defineProps } from 'vue';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
const layoutStore = useButtonListStore();
const classificationOfHardsStore = useClassificationOfHardsStore();
const { ref, t, onMounted, confirmMsg, getCurrentDate, watch, btnSearch, btnAdd, btnDelete, btnBack, btnDownload, btnPreYear, alertMsg, goRouter, btnSave } = BaseView();
const searchTerm = ref('');
const riskAStore = useRiskAStore();

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnPreYear'];

const props = defineProps({
    store: { type: Object, default: Object } // Store
});

onMounted(async () => {
    sessionStorage.setItem('riskAssessmentTab', 'info');
    if (riskAStore.searchParam.searchText) {
        classificationOfHardsStore.writeYear = riskAStore.searchParam.searchText;
    } else {
        classificationOfHardsStore.writeYear = await getCurrentDate().substring(0, 4);
    }
    classificationOfHardsStore.mainSearch();
});

const handleEvent = e => {
    //체크했을경우
    if (e.checked) {
        classificationOfHardsStore.checkedList.push(e);
    } else {
        classificationOfHardsStore.checkedList = classificationOfHardsStore.checkedList.filter(x => x.processId != e.processId);
    }
};

//목록 버튼
btnBack(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'), '', { fun: classificationOfHardsStore.btnBack });
        return;
    }
    classificationOfHardsStore.btnBack();
});

//조회버튼
btnSearch(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'), '', { fun: searchData });
        return;
    }
    searchData();
});

//추가버튼
btnAdd(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'), '', { fun: addDetail });
        return;
    }
    addDetail();
});

//삭제버튼
btnDelete(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let param = classificationOfHardsStore.dataFilterList.filter(item => item.checked);
    if (!param.length) {
        return alertMsg('warning', t('msgNoItem'));
    }
    classificationOfHardsStore.btnDelete();
});

// 전년도 불러오기 버튼
btnPreYear(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    } else {
        confirmMsg('info', t('msgPreviousLoad'), '', { fun: classificationOfHardsStore.mainPrevSearch });
    }
});

// 전년도 불러오기 저장 버튼
btnSave(() => {
    if (!classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousSave'));
        return;
    }

    const param = classificationOfHardsStore.dataFilterList.filter(item => item.checked === true);
    if (param.length === 0) {
        alertMsg('error', t('msgNoItem'));
        return;
    }

    classificationOfHardsStore.btnSave();
});

const searchData = () => {
    riskAStore.searchParam.searchText = classificationOfHardsStore.writeYear;
    classificationOfHardsStore.mainSearch();
};

//  유해위험요인 분류 버튼
const addDetail = () => {
    classificationOfHardsStore.prcsList = [];
    classificationOfHardsStore.selectedProcessEquipMsds = {
        equipment: [],
        msds: []
    };
    classificationOfHardsStore.factorList = [];
    classificationOfHardsStore.cmd = 'I';
    classificationOfHardsStore.useYn = 'Y';
    classificationOfHardsStore.prcsListItem = [];
    classificationOfHardsStore.writeDt = classificationOfHardsStore.writeYear + getCurrentDate().replace(/^\d{4}/, '');
    classificationOfHardsStore.btnAdd();
};

//상세보기
const btnDetail = async e => {
    if (classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }

    classificationOfHardsStore.factorList = [];
    classificationOfHardsStore.checkedList = [];
    classificationOfHardsStore.saveFactorData = [];
    classificationOfHardsStore.prcsList[0] = {
        id: e.processId,
        docNo: e.docNo
    };
    classificationOfHardsStore.cmd = 'U';

    const param = {
        docNo: e.docNo,
        docType: e.docType,
        writeYear: e.writeYear
    };

    goRouter('ClassificationOfHazardsDetail', param);
};

// 출력 버튼
btnDownload(() => {
    if (classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let param = classificationOfHardsStore.dataFilterList.filter(item => item.checked);
    // 선택된 항목이 없는 경우
    if (!param.length) {
        return alertMsg('warning', t('msgNoItem'));
    }
    classificationOfHardsStore.currentPageName = router.currentRoute.value.name;

    confirmMsg('info', '출력하시겠습니까?', '', { fun: classificationOfHardsStore.btnPrint });
});

const chkData = classificationOfHards => {
    // 전년도 불러오기 모드인 경우 기존 데이터를 선택할 수 없도록
    if (classificationOfHardsStore.loadPreviousYn && classificationOfHards.writeYear === classificationOfHardsStore.writeYear) {
        alertMsg('error', t('msgPreviousSaveError'));
        classificationOfHards.checked = false;
        return;
    }
};

// 년도 변경
const modifyYearFilter = () => {
    if (classificationOfHardsStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
    }
};

//입력값 필터링
const handleSearch = () => {
    applyFilter();
};

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = classificationOfHardsStore.dataList.filter(item => item.processNm.includes(searchTerm.value) || item.hazardsClassificationCnt == searchTerm.value || item.createdAt.toLowerCase().includes(searchTerm.value.toLowerCase()));
    classificationOfHardsStore.dataFilterList = filteredData;
};
</script>
