<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="risksAndOppStore.searchTerm" @keyup.enter="applyFilter" />
                <button type="submit" class="shrink0">
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
            <div class="oh">
                <div class="row flex gutters2rem">
                    <div class="grid12-12 h100p ul-ha ul-grid12-12">
                        <div class="oh title-box">
                            <p class="c3248F6">확정차수</p>
                            <div class="row flex gutters2rem">
                                <div class="grid12-4 el-grid12-6 es-grid12-12" v-for="(item, index) in risksAndOppStore.confirmedYList" :key="index">
                                    <div v-for="gubun in gubunList" :key="gubun.minorCd">
                                        <i-card-order v-if="gubun.minorCd === item.criteriaType" :v-model="item.isChecked" :confirm="true" :title="gubun.minorNm" :disabled="item.useYn === 'N'" :data="item" @confirm="btnChanger(item)" @editor="risksAndOppStore.goAsmtDetail(item)" :usePreview="false" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="oh title-box mt2-2rem">
                            <p>차수 리스트</p>
                            <div class="row flex gutters2rem">
                                <div class="grid12-4 el-grid12-6 es-grid12-12" v-for="(item, index) in risksAndOppStore.confirmedNList" :key="index">
                                    <div v-for="gubun in gubunList" :key="gubun.minorCd">
                                        <i-card-order v-if="gubun.minorCd === item.criteriaType" :v-model="item.isChecked" :confirm="false" :title="gubun.minorNm" :disabled="item.useYn === 'N'" :data="item" :confirmable="risksAndOppStore.confirmedYList.filter(i => i.criteriaType === item.criteriaType).length === 0" @confirm="btnChanger(item)" @editor="risksAndOppStore.goAsmtDetail(item)" :usePreview="false"/>
                                    </div>
                                </div>
                                <!-- 추가 -->
                                <div class="grid12-4 el-grid12-6 es-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addRiskAndOpsAsmtCriteria') }}</span>
                                        </div>
                                    </div>
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
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, alertMsg, computed, getCurrentDate, validationStore, onMounted, t, confirmMsg, getCompId, formatDate, watch, btnSearch, btnBack, btnAdd, btnDelete } = BaseView();
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete'];
const compId = getCompId();
import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
const risksAndOppStore = useRisksAndOppStore();
const gubunList = ref([]);
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const getToday = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
};

onMounted(async () => {
    getCodeList();
    risksAndOppStore.btnSearch(true);
});
const getCodeList = async () => {
    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0028',
            compId: compId
        })
    ]);
    gubunList.value = responses[0].list;
};

// 조회 버튼 이벤트 함수
btnSearch(() => {
    risksAndOppStore.btnSearch(true);
});

btnBack(() => {
    router.push('/RisksAndOpportunitiesMain');
});

// ---------------------차수제어------------------------
const confirmList = ref([]);
const isConfirmedR = computed(() => risksAndOppStore.filteredRiskAndOpsAsmtList.some(data => data.criteriaType === 'R' && data.confirmedYn === 'Y'));
const isConfirmedO = computed(() => risksAndOppStore.filteredRiskAndOpsAsmtList.some(data => data.criteriaType === 'O' && data.confirmedYn === 'Y'));

const btnChanger = item => {
    confirmList.value.push(item);
    const confirmData = confirmList.value.map(item => item);

    if (item.confirmedYn === 'Y') {
        confirmMsg('info', '해당 기준을 확정취소 하시겠습니까?', null, {
            fun: risksAndOppStore.btnMainConfirm,
            param: confirmData
        });
    } else {
        confirmMsg('info', '해당 기준을 확정 하시겠습니까?', null, {
            fun: risksAndOppStore.btnMainConfirm,
            param: confirmData
        });
    }
};
const shouldDisableButton = item => {
    if (isConfirmedR.value && isConfirmedO.value) {
        return true;
    }
    if (item.criteriaType === 'R' && !isConfirmedR.value) {
        return false;
    }
    if (item.criteriaType === 'O' && !isConfirmedO.value) {
        return false;
    }
    if (item.criteriaType === 'R' && isConfirmedR.value && item.confirmedYn === 'N') {
        return true;
    }
    if (item.criteriaType === 'O' && isConfirmedO.value && item.confirmedYn === 'N') {
        return true;
    }
    return false;
};
// -----------------------------------------------------

btnDelete(() => {
    var deleteList = risksAndOppStore.confirmedNList.filter(item => item.isChecked);

    if (deleteList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const deleteData = deleteList.map(item => item);
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: risksAndOppStore.btnDelete, param: deleteData });
});

btnAdd(async () => {
    const today = getToday();

    risksAndOppStore.detailParam = {
        compId: risksAndOppStore.compId,
        criteriaId: '',
        createdAt: today,
        useYn: 'Y',
        confirmedYn: 'N',
        confirmedDt: ''
    };
    risksAndOppStore.detailData = [];
    risksAndOppStore.inputForm.cmd = 'I';
    risksAndOppStore.confirmedYn = 'N';
    router.push('/RiskAndOpsAsmtCriteriaDetail');
});
const btnNew = async () => {
    const today = getToday();

    risksAndOppStore.detailParam = {
        compId: risksAndOppStore.compId,
        criteriaId: '',
        createdAt: today,
        useYn: 'Y',
        confirmedYn: 'N',
        confirmedDt: ''
    };
    //confirmedYn 초기화
    risksAndOppStore.confirmedYn = risksAndOppStore.detailParam.confirmedYn;
    risksAndOppStore.detailData = [];

    router.push('/RiskAndOpsAsmtCriteriaDetail');
};

const applyFilter = () => {
    const filteredYData = risksAndOppStore.riskAndOpsAsmtList.filter(item => item.confirmedYn === 'Y' && (item.criteriaId.toLowerCase().includes(risksAndOppStore.searchTerm) || item.createdAt.toLowerCase().includes(risksAndOppStore.searchTerm) || item.confirmedDt.toLowerCase().includes(risksAndOppStore.searchTerm) || gubunList.value.find(gubun => gubun.minorCd === item.criteriaType && gubun.minorNm.toLowerCase().includes(risksAndOppStore.searchTerm))));

    // criteriaId 및 minorNm으로 필터링된 데이터 (confirmedYn이 'N'인 경우)
    const filteredNData = risksAndOppStore.riskAndOpsAsmtList.filter(item => item.confirmedYn === 'N' && (item.criteriaId.toLowerCase().includes(risksAndOppStore.searchTerm) || item.createdAt.toLowerCase().includes(risksAndOppStore.searchTerm) || item.confirmedDt.toLowerCase().includes(risksAndOppStore.searchTerm) || gubunList.value.find(gubun => gubun.minorCd === item.criteriaType && gubun.minorNm.toLowerCase().includes(risksAndOppStore.searchTerm))));
    risksAndOppStore.confirmedYList = filteredYData;
    risksAndOppStore.confirmedNList = filteredNData;
    risksAndOppStore.initConfirmedList(risksAndOppStore.confirmedYList);
    risksAndOppStore.initConfirmedList(risksAndOppStore.confirmedNList);
};
// const checkedCard = item => {
//     item.checked = item.checked === true ? false : true;
// };
</script>
