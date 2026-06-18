<template>
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="mb2-2rem">
            <div class="form ui row flex gutters1rem">
                <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="control-field ui form">
                        <div class="field">
                            <input v-input type="text" v-calendar="'yyyy'" year v-model="riskAssessmentPlanStore.searchParam.searchText" class="datepicker w20rem radius es-w100p" @mousedown="modifyYearFilter" />
                        </div>
                    </div>
                </div>
                <div class="grid12-9 ul-grid12-8 lg-grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="riskAssessmentPlanStore.searchParam.searchText2" @keyup.enter="search" />
                        <button type="button" class="shrink0" @click="search">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <div class="row flex gutters1rem">
                <template v-for="riskAssessmentPlan in riskAssessmentPlanStore.riskAssessmentPlanList" :key="riskAssessmentPlan.writeYear + riskAssessmentPlan.docNo">
                    <i-card :v-model="riskAssessmentPlan.checked" :modelValue="riskAssessmentPlan.checked" :data="riskAssessmentPlan" :disabled="riskAssessmentPlan.useYn === 'N'" :title="riskAssessmentPlan.planNm" @editor="btnDetail" use-approval-status :approval-status="riskAssessmentPlan.signStatus" @change="chkData(riskAssessmentPlan)"/>
                </template>

                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="riskAssessmentPlanStore.btnAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db mt1rem fs2rem c999999">{{ t('card_addRiskAssessmentPlan') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { goRouter, onMounted, confirmMsg, btnBack, btnSearch, btnAdd, btnDelete, alertMsg, t, btnPreYear, watch, btnSave } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnPreYear'];
const riskStore = useRiskAStore();
import router from '@/router';
OverlayScrollbarsComponent.options = {
    scrollbars: {
        autoHide: 'hover',
        x: 'hidden',
        y: 'visible'
    }
};

import { useRiskAssessmentPlanStore } from '@/stores/safewiz/planning/riskAssessmentPlan';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
const riskAssessmentPlanStore = useRiskAssessmentPlanStore();

watch(
    () => riskAssessmentPlanStore.searchParam.searchText,
    () => {
        // 검색년도 던져주기
        riskStore.searchParam.searchText = riskAssessmentPlanStore.searchParam.searchText;
        search();
    }
);

onMounted(async () => {
    // 새로고침해도 선택한 탭이 나오게 session 설정
    sessionStorage.setItem('riskAssessmentTab', 'plan');
    // 맵핑 받아오기
    riskAssessmentPlanStore.searchParam.searchText = riskStore.searchParam.searchText;
    await search();
});

// 년도 변경
const modifyYearFilter = () => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
    }
};

const search = async () => {
    await riskAssessmentPlanStore.initRiskAssessmentSystemCode();
    riskAssessmentPlanStore.getRiskAssessmentPlanList();
};

// 우측 목록 버튼
btnBack(() => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'),'', { fun: backAction });
        return;
    }
    backAction();
});

const backAction = () => {
    router.push({
        path: 'RiskAssessment'
    });
}

// 우측 조회 버튼
btnSearch(() => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'),'', { fun: search });
        return;
    }
    search();
});

// 우측 추가 버튼
btnAdd(() => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        confirmMsg('info', t('msgPreviousConfirm'),'', { fun: addAction });
        return;
    }
    addAction();
});

const addAction = () => {
    riskAssessmentPlanStore.initSearchParam();
    riskAssessmentPlanStore.btnAdd();
}

// 우측 삭제 버튼
btnDelete(() => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    // 체크한 값이 없을경우 메세지 호출
    if (riskAssessmentPlanStore.riskAssessmentPlanList.filter(item => item.checked === true).length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // 체크한 값만 삭제
    confirmMsg('warning', t('msgDelete'), '', { fun: riskAssessmentPlanStore.deleteRiskAssessmentPlan, param: riskAssessmentPlanStore.riskAssessmentPlanList.filter(item => item.checked === true) });
});

// 우측 전년도 불러오기 버튼
btnSave(async () => {
    if (!riskAssessmentPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousSave'));
        return;
    }

    const param = riskAssessmentPlanStore.riskAssessmentPlanList.filter(item => item.checked === true);
    if (param.length === 0) {
        alertMsg('error', t('msgNoItem'));
        return;
    }

    confirmMsg('info', t('msgSave'), '', { fun: riskAssessmentPlanStore.btnSavePreviousYear, param: param });
});

const chkData = riskAssessmentPlan => {
    // 전년도 불러오기 모드인 경우 기존 데이터를 선택할 수 없도록
    if (riskAssessmentPlanStore.loadPreviousYn && riskAssessmentPlan.writeYear === riskAssessmentPlanStore.searchParam.searchText) {
        alertMsg('error', t('msgPreviousSaveError'));
        riskAssessmentPlan.checked = false;
        return;
    }
}

// 우측 전년도 불러오기 버튼
btnPreYear(async () => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    else{
        confirmMsg('info', t('msgPreviousLoad'),'', { fun: riskAssessmentPlanStore.btnPreYear });
    }
});

// 카드 상세보기 버튼
const btnDetail = async value => {
    if (riskAssessmentPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('RiskAssessmentPlanDetail', param);
};
</script>
