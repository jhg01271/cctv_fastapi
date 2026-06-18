<template>
    <!-- 콘텐츠 영역 -->
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row gutters1rem">
                <div class="grid12-12 sm-grid12-12 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="safetyHealthInfoStore.searchTerm" @keyup.enter="handleSearch" />
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
            <div class="oh df fdc minh100p ul-ha">
                <div class="df fdc rg2-2rem h100p fg1">
                    <div class="title-box fg1">
                        <p class="c3248F6">확정차수</p>

                        <div class="row flex gutters2rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in confirmedSafetyHealthInfo" :key="index">
                                <i-card-order :useCheck="true" v-model="item.selected" :confirm="true" :title="item.processNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @editor="btnDetail(confirmedSafetyHealthInfo[index])" :usePreview="false" />
                            </div>
                        </div>
                    </div>
                    <div class="title-box fg1">
                        <p>차수 리스트</p>
                        <div class="row flex gutters2rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in unConfirmedSafetyHealthInfo" :key="index">
                                <i-card-order v-model="item.selected" :confirm="false" :title="item.processNm" :data="item" :disabled="item.useYn === 'N'" :confirmable="confirmedSafetyHealthInfo.filter(i => i.processId === item.processId && i.revNo === item.revNo).length === 0" @confirm="confirm(item, $event)" @editor="btnDetail(unConfirmedSafetyHealthInfo[index])" :usePreview="false" />
                            </div>

                            <!-- 추가 -->
                            <div class="grid12-4 lg-grid12-6 es-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="safetyHealthInfoStore.btnAdd()">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyAndHealthInfoSurvey') }}</span>
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
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { onMounted, defineProps } from 'vue';
import BaseView from '@/components/base/BaseView';
import { useSafetyHealthInfoStore } from '@/stores/safewiz/planning/safetyAndHealthInfoSurvey.js';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
import router from '@/router';
import _ from 'lodash';
import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const { t, confirmMsg, btnSearch, btnBack, btnAdd, btnDelete, btnDownload, computed } = BaseView();
const safetyHealthInfoStore = useSafetyHealthInfoStore();
const props = defineProps({
    store: { type: Object, default: Object } // Store
});
const riskAStore = useRiskAStore();

// 안전 보건 정보 조사 차수 제어
const confirmedSafetyHealthInfo = computed(() => safetyHealthInfoStore.confirmedSafetyHealthInfo);
const unConfirmedSafetyHealthInfo = computed(() => safetyHealthInfoStore.unConfirmedSafetyHealthInfo);

// 차수 변경
const confirm = (item, param) => {
    const data = _.cloneDeep(item);
    data.confirmYn = param;
    confirmMsg('info', param == 'Y' ? `${item.processNm} 을(를) 확정 하시겠습니까?` : `${item.processNm} 을(를) 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: data });
};

// 차수 적용
const confirmAction = item => {
    safetyHealthInfoStore.updateConfirmSafetyHealthInfoManage(item);
};

// 초기 데이터 로딩
onMounted(async () => {
    if (riskAStore.searchParam.searchText) {
        safetyHealthInfoStore.searchParam.writeYear = riskAStore.searchParam.searchText;
    }
    if (!safetyHealthInfoStore.searchParam.writeYear) {
        //현재날짜 세팅
        safetyHealthInfoStore.searchParam.writeYear = safetyHealthInfoStore.currentDate.slice(0, 4);
    }
    let responses = await safetyHealthInfoStore.getSafetyHealthInfoList(true);
    if (responses && responses.list) {
        safetyHealthInfoStore.initData();
    }
});
//목록 버튼
btnBack(() => {
    router.push({ name: 'RiskAssessment' });
});

//조회 버튼
btnSearch(() => {
    safetyHealthInfoStore.getSafetyHealthInfoList(true);
});

//추가 버튼
btnAdd(() => {
    safetyHealthInfoStore.btnAdd(true);
});

btnDelete(() => {
    safetyHealthInfoStore.btnDelete();
});

btnDownload(() => {
    safetyHealthInfoStore.btnPrint();
});

// 마스터 테이블 재 조회
const searchMain = () => {
    safetyHealthInfoStore.getSafetyHealthInfoList(true);
    riskAStore.searchParam.searchText = safetyHealthInfoStore.searchParam.writeYear;
};

// 검색 핸들러
const handleSearch = () => safetyHealthInfoStore.applyFilter();

// 상세보기 버튼
const btnDetail = async e => {
    safetyHealthInfoStore.tempAccordion = [];
    safetyHealthInfoStore.btnDetail(e);
};
</script>
