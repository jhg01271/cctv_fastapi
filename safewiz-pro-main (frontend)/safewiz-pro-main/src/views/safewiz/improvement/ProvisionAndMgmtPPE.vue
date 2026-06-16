<template>
    <div class="contents df fdc">
        <div class="control-field form ui mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input type="text" v-model="provisionAndMgmtPPEStore.searchParam.writeYear" v-calendar="'yyyy'" class="datepicker w100p radius" year @input="searchData" />
                </div>
                <div class="grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <button type="button" class="btn base radius w100p" @click="openPPEManage">
                        <span>{{ '안전보호구 품목 관리' }}</span>
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
            <div class="accordion df fdc rg8px">
                <div v-for="(key, i) in Object.keys(filteredByMonthListBySearch)" :key="i" class="list">
                    <button :id="'btn' + i" type="button" class="df jcsb aic" @click="toggleAccordion(i)" :class="{ active: activeSegments[i] }">
                        <em>{{ key }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <template v-for="(value, index) in filteredByMonthListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :data="value" :disabled="value.useYn === 'N'" :type="'basic'" :title="value.cardTitle" @handle="handleEvent" @editor="btnDetail" />
                                </template>

                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="provisionAndMgmtPPEStore.btnAdd(filteredByMonthListBySearch[key][0])">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addPPEMgmt') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-if="registerList.length === 0">
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="provisionAndMgmtPPEStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addPPEMgmt') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>

    <!-- 팝업 컴포넌트 -->
    <teleport to="body">
        <i-PopupDialog ref="PPEPop">
            <div class="contents w80rem md-w100p">
                <PPEManagement :single="false" :type="'manage'" @close="closePPEManage" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { nextTick, onMounted } from 'vue';
import { useButtonListStore } from '@/stores/buttonList';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import _ from 'lodash';
// [팝업]
// 안전보호구 품목 팝업
import PPEManagement from '@/views/system/base/popup/PPEManagement.vue';
//-----------------------------------------------
// [스토어]
import { useProvisionAndMgmtPPEStore } from '@/stores/safewiz/improvement/provisionAndMgmtPPE.js';
import { useContinualImprovementStore } from '@/stores/safewiz/improvement/continualImprovement.js';

const { ref, t, formatDate, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, alertMsg, confirmMsg, goRouter } = BaseView();

const PPEPop = ref(null);
const filteredByMonthListBySearch = ref({});
const filteredByMonthList = ref({});
const registerList = ref([]);

const provisionAndMgmtPPEStore = useProvisionAndMgmtPPEStore();
const continualImprovementStore = useContinualImprovementStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 기타 변수 선언
const activeSegments = ref({});
const accordionRefs = ref([]);
const searchTerm = ref('');

// 아코디언 열림, 닫힘 작업
const toggleAccordion = async index => {
    activeSegments.value[index] = !activeSegments.value[index];
    await nextTick(); // DOM 업데이트 후 실행
    const segment = accordionRefs.value[index];

    // null 체크
    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

// 목록으로 버튼
btnBack(() => router.push('/ContinualImprovement'));

// 조회 버튼
btnSearch(() => {
    searchData();
});
// 추가 버튼
btnAdd(() => {
    provisionAndMgmtPPEStore.btnAdd();
});
// 삭제 버튼
btnDelete(() => {
    provisionAndMgmtPPEStore.btnDelete();
});

// // 출력 버튼
btnDownload(() => {
    let checkedList = getCheckedList();

    if (checkedList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgCheckedPrint'), null, { fun: provisionAndMgmtPPEStore.btnDownload, param: checkedList });
});

const openPPEManage = () => openPopup(PPEPop);
const closePPEManage = () => {
    closePopup(PPEPop);
    searchData();
};

// 팝업 열기 함수
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

// 초기 데이터 로딩
onMounted(() => {
    if (!provisionAndMgmtPPEStore.searchParam.writeYear) {
        //현재날짜 세팅
        // provisionAndMgmtPPEStore.searchParam.writeYear = provisionAndMgmtPPEStore.currentDate;
    }
    if (continualImprovementStore.searchParam.searchText) {
        provisionAndMgmtPPEStore.searchParam.writeYear = continualImprovementStore.searchParam.searchText;
    }
    searchData();
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { writeYear, docNo, docType, checked } = e;
    if (checked) {
        provisionAndMgmtPPEStore.checkedList.push(e);
    } else {
        provisionAndMgmtPPEStore.checkedList = provisionAndMgmtPPEStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};

const btnDetail = async e => {
    provisionAndMgmtPPEStore.setSearchParam(e);

    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo,
        docSeq: e.ppeId,
        date: e.receiptDt
    };
    goRouter('ProvisionAndMgmtPPEDetail', param);
};

const searchData = () => {
    continualImprovementStore.searchParam.searchText = provisionAndMgmtPPEStore.searchParam.writeYear;
    activeSegments.value = [];
    provisionAndMgmtPPEStore.btnSearch(true).then(async res => {
        res.result.forEach(el => {
            el.docTitle = `${el.writeYear}-${el.docType}-${el.docNo}`;
            el.detail = {
                '최근 수불일자': el.receiptDt ? formatDate(el.receiptDt) : '-',
                '수불 건 수': el.receiptCnt
            };
            el.cardTitle = el.ppeNm;
            el.receiptDt = formatDate(el.receiptDt);
        });
        registerList.value = res.result;
        addMonthList();
        await nextTick();
        await toggleAccordion(0);
    });
};

// 선택된 항목 받기
const getCheckedList = () => {
    let checkedData = [];
    Object.keys(filteredByMonthListBySearch.value).forEach(el => {
        checkedData = [...checkedData, ...filteredByMonthListBySearch.value[el]];
    });
    checkedData = checkedData.filter(el => el.checked);
    return checkedData;
};

const applyFilter = data => {
    return data.filter(item => item.receiptDt.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.ppeNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.receiptCnt.toLowerCase().includes(searchTerm.value.toLowerCase()));
};

// 월별로 분할
const addMonthList = () => {
    filteredByMonthList.value = {};
    const result = {};
    // 데이터 처리 함수
    applyFilter(registerList.value).forEach(list => {
        // 각 부모의 list를 순회하며 그룹화 진행
        if (result[list.receiptDt.substring(5, 7) + '월']) {
            result[list.receiptDt.substring(5, 7) + '월'].push(list);
        } else {
            result[list.receiptDt.substring(5, 7) + '월'] = [list];
        }
    });
    filteredByMonthList.value = _.cloneDeep(result);
    filteredByMonthListBySearch.value = _.cloneDeep(result);
    activeSegments.value = new Array(filteredByMonthListBySearch.value.length).fill(false);
};

// 검색 핸들러
const handleSearch = () => {
    addMonthList();
    toggleAccordion(0);
};
</script>
