<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" v-model="searchTerm" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilter" />
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
            <!-- 아코디언 영역 아무런 데이터가 없을때는 목표아이콘이 보이고 데이터가 있으면 아코디언 보이기-->
            <div class="accordion df fdc gap8px" v-if="safetyHealthStore.segments.length !== 0">
                <div v-for="(segment, index) in safetyHealthStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="safetyHealthStore.toggleAccordion" :class="{ active: activeSegments[index] }">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.dataList" :key="data.orgnId + idx">
                                    <i-card :v-model="data.orgnId" :title="data.orgnNm" :data="data" :type="'basic'" useApprovalStatus :approvalStatus="data.approvalStatus" :disabled="data.useYnMain === 'N'" @handle="handleEvent" @editor="btnDetail" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="addDetail(segment.year)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSafetyAndHealthObjectives') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-else class="card h100p df aic jcc cp" @click="addDetail()">
                <div class="tac">
                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    <span class="db fs2rem c999999">{{ t('card_addSafetyAndHealthObjectives') }}</span>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
// Overlay
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
// BaseView
import BaseView from '@/components/base/BaseView';
// Button List
import { useButtonListStore } from '@/stores/buttonList';
// Function Store
import { useSafetyHealthStore } from '@/stores/safewiz/planning/safetyAndHealthObjectives';
// Router
import router from '@/router';
import { gsap } from 'gsap';
import { delSafetyAndHealthObjectives } from '@/stores/safewiz/planning/api/safetyAndHealthObjectives';
const { ref, onMounted, nextTick, confirmMsg, alertMsg, btnSearch, btnAdd, btnDelete, getCurrentDate, btnDownload, t, goRouter } = BaseView();
const searchTerm = ref('');
const checkedRow = ref([]);
const layoutStore = useButtonListStore();
const safetyHealthStore = useSafetyHealthStore();

// 아코디언
const segments = ref([]);
const activeSegments = ref({});
const accordionRefs = ref([]);

layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

onMounted(() => {
    safetyHealthStore.searchMain();
});

/* --------- 버튼 클릭 이벤트 --------- */
btnSearch(() => {
    safetyHealthStore.searchMain();
});

// 추가
btnAdd(() => {
    addDetail();
});

const addDetail = el => {
    safetyHealthStore.initInputForm();
    safetyHealthStore.inputForm.cmd = 'I'; // 신규
    safetyHealthStore.isSaved = false;
    //아코디언내부 아이콘 클릭했을때(아코디언에 표시된 년도를 상세페이지 작성년도에 넣는다)
    if (el) {
        safetyHealthStore.inputForm.writeYear = el.substring(0, 4);
    } else {
        //우측 추가버튼을 클릭했을때(올해년도를 상세페이지 작성년도에 넣는다.)
        safetyHealthStore.inputForm.writeYear = getCurrentDate().substring(0, 4);
    }
    safetyHealthStore.readonlyValue = false; //2024.12.10 김현재 작성 추가버튼 클릭시 작성년도 수정가능하도록
    safetyHealthStore.readonlyValueRevisedDt = true;

    router.push({ name: 'SafetyAndHealthObjectivesDetail' });
};

btnDelete(() => {
    if (checkedRow.value.length === 0) {
        return alertMsg('warning', '선택된 데이터가 없습니다.', '');
    }

    confirmMsg('info', '선택한 데이터를 삭제하시겠습니까?', '', {
        fun: safetyHealthStore.deleteMainAction,
        param: checkedRow.value
    });
});

/* ------------------------------------- */
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

// 상세정보 보기
const btnDetail = async el => {
    safetyHealthStore.readonlyValue = true; //2024.12.10 김현재 작성 상세버튼 클릭시 작성년도 수정불가능하도록
    safetyHealthStore.isSaved = false;
    safetyHealthStore.goDetail(el);

    const param = {
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo
    };

    goRouter('SafetyAndHealthObjectivesDetail', param);
};

// 상단 검색 필터
const applyFilter = () => {
    safetyHealthStore.searchClientGrid(safetyHealthStore.mainList);

    const filteredSegments = []; // 필터된 세그먼트를 저장할 새로운 배열

    // 검색어 빈 값일 시 모든 데이터 보여줌.
    if (searchTerm.value == '' || searchTerm.value == null) {
        safetyHealthStore.searchClientGrid(safetyHealthStore.mainList);
        return;
    }

    for (let el of safetyHealthStore.segments) {
        // 새로운 객체 생성
        const filteredData = {
            year: el.year,
            dataList: el.dataList.filter(item => {
                // searchTerm이 없거나 null/undefined인 경우 필터 조건에 걸리지 않도록 처리
                if (!searchTerm.value) return false;

                // 각 항목 값이 null/undefined인 경우 기본적으로 빈 문자열로 처리
                const docCount = item.docCount ?? '';
                const enactedDt = item.enactedDt ?? '';
                const revisedDt = item.revisedDt ?? '';
                const orgnNm = item.orgnNm ?? '';

                // searchTerm 각 항목에 포함되어 있는지 확인
                return docCount.toLowerCase().includes(searchTerm.value.toLowerCase()) || enactedDt.toLowerCase().includes(searchTerm.value.toLowerCase()) || revisedDt.toLowerCase().includes(searchTerm.value.toLowerCase()) || orgnNm.toLowerCase().includes(searchTerm.value.toLowerCase());
            })
        };

        // 필터된 결과가 존재할 경우만 추가
        if (filteredData.dataList.length > 0) {
            filteredSegments.push(filteredData); // 새로운 배열에 추가
        }
    }

    safetyHealthStore.segments = filteredSegments; // segments를 필터된 배열로 업데이트
};

btnDownload(() => {
    const downloadData = checkedRow.value;
    if (downloadData.length === 0) {
        alertMsg('warning', t('msgNoItem'), '');
        return false;
    } else {
        confirmMsg('info', t('msgCheckedPrint'), '', {
            fun: safetyHealthStore.downloadReportMain,
            param: downloadData
        });
    }
});
</script>
