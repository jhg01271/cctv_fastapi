<template>
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row gutters1rem">
                <div class="grid12-12 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="classProcessStore.searchTerm" @keyup.enter="handleSearch" />
                        <!-- @input="handleSearch" -->
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
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in confirmedClassProcs" :key="index">
                                <i-card-order :useCheck="true" v-model="item.selected" :confirm="true" :title="item.orgnNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @editor="btnDetail(confirmedClassProcs[index])" :usePreview="false" />
                            </div>
                        </div>
                    </div>
                    <div class="title-box fg1">
                        <p>차수 리스트</p>
                        <div class="row flex gutters2rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in unConfirmedClassProcs" :key="index">
                                <i-card-order v-model="item.selected" :confirm="false" :title="item.orgnNm" :data="item" :disabled="item.useYn === 'N'" :confirmable="confirmedClassProcs.filter(i => i.orgnId === item.orgnId).length === 0" @confirm="confirm(item, $event)" @editor="btnDetail(unConfirmedClassProcs[index])" :usePreview="false" />
                            </div>

                            <!-- 추가 -->
                            <div class="grid12-4 lg-grid12-6 es-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="addDetail">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addClassificationProcessEquipMsds') }}</span>
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
import { onMounted, watch, ref, onBeforeMount } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import _ from 'lodash';

const { toastPopup, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, getCompId, alertMsg, t, confirmMsg, computed, goRouter } = BaseView();
import { useClassProcEquipMsdsStore } from '@/stores/safewiz/planning/classificationProcessEquipMsds.js';
const classProcessStore = useClassProcEquipMsdsStore();

// Button List
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// calendar year
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
const riskAStore = useRiskAStore();
// API
import { getClassProcEquipMsdsList } from '@/stores/safewiz/planning/api/classificationProcessEquipMsdsApi';

// 조직 삭제 시 전체인원 검색
watch(
    () => classProcessStore.writeYear,
    (newItem, oldItem) => {
        if (newItem) {
            classProcessStore.searchParam = { writeYear: newItem, docType: 'CPE', compId: getCompId() };
            classProcessStore.getClassProcList();
        }
    },
    { deep: true }
);

// mounted되기 전 writeYear setting
onBeforeMount(() => {
    if (riskAStore.searchParam.searchText) {
        classProcessStore.writeYear = riskAStore.searchParam.searchText;
    }
});

// 초기 데이터 로딩
onMounted(() => {
    classProcessStore.getClassProcList(true);
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { orgnId, checked } = e;
    if (checked) {
        classProcessStore.checkedList.push(e);
    } else {
        classProcessStore.checkedList = classProcessStore.checkedList.filter(item => item.orgnId !== orgnId);
    }
};

// 검색 핸들러
const handleSearch = () => classProcessStore.applyFilter();

// 공정/설비/물질 구분 차수 제어
const confirmedClassProcs = computed(() => classProcessStore.confirmedClassProcs);
const unConfirmedClassProcs = computed(() => classProcessStore.unConfirmedClassProcs);

// 차수 변경
const confirm = (item, param) => {
    const data = _.cloneDeep(item);
    data.confirmYn = param;
    confirmMsg('info', param == 'Y' ? `${item.orgnNm} 을(를) 확정 하시겠습니까?` : `${item.orgnNm} 을(를) 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: data });
};

// 차수 적용
const confirmAction = item => {
    classProcessStore.updateConfirmClassProcs(item);
};

// 상세보기 버튼
const btnDetail = async e => {
    // 상세 조회 시 필요 파라미터
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };
    classProcessStore.inputForm = [];

    goRouter('ClassificationProcessEquipMsdsDetail', param);
};

/*****************************
 * 우측 버튼 list
 *****************************/
btnBack(() => {
    router.push('/RiskAssessment');
});
// 조회 버튼
btnSearch(() => {
    // 조회
    classProcessStore.getClassProcList(true);
});
// 추가버튼
const addDetail = () => {
    classProcessStore.cmd = 'I';
    classProcessStore.orgnItem = []; // 조직 정보 초기화
    classProcessStore.headItem = []; // 조직장 정보 초기화
    classProcessStore.inputForm = []; // 상세 정보 초기화
    classProcessStore.processList = []; // 공정분석 정보 초기화
    classProcessStore.participantList = []; // 참여자 정보 초기화
    router.push('/ClassificationProcessEquipMsdsDetail');
};
// 추가 버튼
btnAdd(() => {
    addDetail();
});
// 삭제 버튼
btnDelete(() => classProcessStore.btnDelete());
// 출력 버튼
btnDownload(async () => {
    const unConfirmedParam = unConfirmedClassProcs.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    const confirmedParam = confirmedClassProcs.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴

    // 선택된 항목이 없는 경우
    if (!unConfirmedParam.length && !confirmedParam.length) {
        return alertMsg('warning', t('msgNoItem'));
    }
    // 선택된 항목이 있는 경우
    else {
        const reportData = [...unConfirmedParam, ...confirmedParam];

        confirmMsg('info', t('msgCheckedPrint'), null, { fun: classProcessStore.reportDownload, param: reportData });
    }
});
</script>
