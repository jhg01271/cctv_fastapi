<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit" class="shrink0" @click="applyFilter">
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
                <div v-for="(item, index) in risksAndOppStore.riskSegments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="risksAndOppStore.toggleAccordion" :class="{ active: activeSegments[index] }">
                        <em>{{ item.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div :id="'accordion' + item.year.substring(0, 4)" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in item.dataList" :key="idx">
                                    <i-card :v-model="data.orgnId" :title="data.orgnNm" :data="data" :type="'basic'" useApprovalStatus :approvalStatus="data.approvalStatus" :disabled="data.useYn === 'N'" @handle="handleEvent" @editor="btnDetail" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="addDetail(item)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addRiskOpportunities') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-if="risksAndOppStore.riskSegments.length === 0">
                <div class="card h100p df aic jcc cp" @click="addDetail()">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        <span class="db fs2rem c999999">{{ t('card_addRiskOpportunities') }}</span>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <!-- 추가 버튼 들어갈 자리 -->
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { ref, alertMsg, confirmMsg, onMounted, t, btnSearch, btnBack, btnAdd, btnDelete, btnDownload, goRouter } = BaseView();
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnDownload'];

import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
const risksAndOppStore = useRisksAndOppStore();
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const searchTerm = ref('');
const checkedRow = ref([]);

// 아코디언
const activeSegments = ref({});

// 초기 활성화 상태 설정
onMounted(async () => {
    await risksAndOppStore.btnSearchRisk(true);
});

// 조회 버튼 이벤트 함수
btnSearch(() => {
    risksAndOppStore.btnSearchRisk(true);
});

btnBack(() => {
    router.push('/RisksAndOpportunitiesMain');
});

btnAdd(() => {
    risksAndOppStore.initInputForm();
    risksAndOppStore.inputForm.cmd = 'I'; // 신규
    risksAndOppStore.addBtnType = 'R';
    risksAndOppStore.getAsmtList();
    router.push({ name: 'RisksAndOpportunitiesManageDetail' });
});

const addDetail = item => {
    risksAndOppStore.initInputForm();
    risksAndOppStore.inputForm.cmd = 'I'; // 신규
    if (item) risksAndOppStore.inputForm.writeYear = item.year.substring(0, 4);
    risksAndOppStore.addBtnType = 'C';
    risksAndOppStore.getAsmtList();
    router.push({ name: 'RisksAndOpportunitiesManageDetail' });
};

btnDelete(() => {
    if (checkedRow.value.length === 0) {
        // toastPopup('삭제에 실패하였습니다','체크된 항목이 없습니다.', 'error');
        alertMsg('error', t('msgNoItem'));
        return;
    }

    confirmMsg('info', '선택한 데이터를 삭제하시겠습니까?', null, { fun: risksAndOppStore.deleteMainAction, param: checkedRow.value });
});

const btnDetail = async el => {
    await risksAndOppStore.goDetail(el);
    const param = {
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo
    };

    goRouter('RisksAndOpportunitiesManageDetail', param);
};

btnDownload(() => {
    if (checkedRow.value.length === 0) {
        alertMsg('error', t('msgNoItem'));
        return;
    }
    confirmMsg('info', t('msgCheckedPrint'), null, { fun: downAction, param: null });
});

const downAction = () => {
    console.log('checkedRow', checkedRow.value);
    risksAndOppStore.downloadCardReport(checkedRow.value);
};

// 체크박스 이벤트
const handleEvent = el => {
    if (el.checked) {
        checkedRow.value.push(el);
    } else {
        const index = checkedRow.value.indexOf(el);
        if (index > -1) {
            checkedRow.value.splice(index, 1); // 배열에서 해당 요소 제거
        }
    }
};

//상단 검색 필터
const applyFilter = () => {
    risksAndOppStore.searchClientGrid(risksAndOppStore.riskOpportunitiesList);

    const filteredSegments = []; // 필터된 세그먼트를 저장할 새로운 배열

    // 검색어 빈 값일 시 모든 데이터 보여줌.
    if (searchTerm.value == '' || searchTerm.value == null) {
        risksAndOppStore.searchClientGrid(risksAndOppStore.riskOpportunitiesList);
        return;
    }

    for (let el of risksAndOppStore.riskSegments) {
        // 새로운 객체 생성
        const filteredData = {
            year: el.year,
            dataList: el.dataList.filter(item => {
                const searchValue = searchTerm.value.toLowerCase();

                // 각 필드에서 검색어가 포함되는지 확인
                return (item.orgnNm && item.orgnNm.toLowerCase().includes(searchValue)) || (item.createdAt && item.createdAt.toLowerCase().includes(searchValue)) || (item.riskDetailCount && item.riskDetailCount.toString().includes(searchValue)) || (item.oppDetailCount && item.oppDetailCount.toString().includes(searchValue)) || (item.parDetailCount && item.parDetailCount.toString().includes(searchValue));
            })
        };

        // 필터된 결과가 존재할 경우만 추가
        if (filteredData.dataList.length > 0) {
            filteredSegments.push(filteredData); // 새로운 배열에 추가
        }
    }

    risksAndOppStore.riskSegments = filteredSegments; // segments를 필터된 배열로 업데이트
};
</script>
