<template>
    <div class="h100p df fdc">
        <div class="control-field form ui mb2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 sm-grid12-12 es-grid12-12">
                    <input type="text" v-calendar="'yyyy'" v-model="planningCtrlStore.searchParam.searchText" class="datepicker w100p radius" year @input="contractorInvestigationFormStore.btnSearch(true)" />
                </div>
                <div class="grid12-7 us-grid12-12">
                    <div class="df aic w100p bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                        <button type="button" class="shrink0" @click="applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 us-grid12-12">
                    <button type="button" class="btn w100p radius active lg" @click="btnPopupOpen">
                        <span>년도별 합격점수</span>
                    </button>
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
                <div class="df fdc gap1-2rem">
                    <div class="row flex gutters2rem">
                        <template v-for="(data, dataIndex) in contractorInvestigationFormStore.filteredData" :key="dataIndex">
                            <i-card :v-model="data.checked" :data="data" :disabled="data.useYn === 'N'" :type="'basic'" :title="data.partCompNm" :useApprovalStatus="true" :approvalStatus="data.approvalStatus" @editor="btnDetail(data)" />
                        </template>
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="btnNew(data)">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addContractorInvestigation') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="scorePopup">
            <div class="contents w300px md-w100p">
                <passScorePopup @yearChange="yearChanged()" @onSave="btnSave" @onClose="popupClose()"></passScorePopup>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { t, onMounted, router, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, alertMsg, confirmMsg } = BaseView();
const scorePopup = ref(null);
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { getPassScore } from '@/stores/safewiz/impl/api/contractorInvestigationFormApi.js';
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

import passScorePopup from '@/views/safewiz/impl/popup/PassScorePopup.vue';
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

import { useContractorInvestigationFormStore } from '@/stores/safewiz/impl/contractorInvestigationForm.js';
const contractorInvestigationFormStore = useContractorInvestigationFormStore();

onMounted(async () => {
    contractorInvestigationFormStore.btnSearch(true);
});

// 버튼
btnBack(() => {
    router.push('/PlanningAndControl');
});
btnSearch(() => {
    contractorInvestigationFormStore.btnSearch(true);
});
const btnDetail = param => {
    contractorInvestigationFormStore.fetchInvestigationDetail(param);
};
btnAdd(async () => {
    await contractorInvestigationFormStore.initInputForm();
    router.push('/ContractorInvestigationFormDetail');
});
const btnNew = async () => {
    await contractorInvestigationFormStore.initInputForm();
    router.push('/ContractorInvestigationFormDetail');
};
btnDelete(() => {
    let checkedList = contractorInvestigationFormStore.filteredData.filter(el => el.checked);
    if (checkedList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: btnDeleteAction, param: checkedList });
});

const btnDeleteAction = checkedList => {
    contractorInvestigationFormStore.btnDelete(checkedList);
}

btnDownload(() => {
    let checkedList = contractorInvestigationFormStore.filteredData.filter(el => el.checked);
    contractorInvestigationFormStore.btnDownload(checkedList);
});

const searchTerm = ref('');
const applyFilter = () => {
    let filteringData = [];
    filteringData = contractorInvestigationFormStore.dataList.filter(data => data.partCompNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.examDtTitle.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.investigator1NmTitle.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.investigator2NmTitle.toString().toLowerCase().includes(searchTerm.value.toLowerCase()));
    contractorInvestigationFormStore.filteredData = filteringData;
};

const btnPopupOpen = () => {
    planningCtrlStore.passScore = '';
    scorePopup.value.onOpen();

    const searchParam = {
        writeYear: planningCtrlStore.searchParam.searchText,
        compId: planningCtrlStore.searchParam.compId
    };
    getPassScore(searchParam, true).then(res => {
        planningCtrlStore.passScore = res.result;
    });
};

const yearChanged = data => {
    btnPopupOpen();
};

const btnSave = () => {
    contractorInvestigationFormStore.btnAddPassScore();
};

const popupClose = () => {
    scorePopup.value.onClose();
    contractorInvestigationFormStore.btnSearch(true);
};
</script>
