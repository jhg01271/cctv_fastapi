<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic w100p bcffffff">
                <input v-input type="text" v-model="searchTerm" class="radius w100p search" placeholder="검색어를 입력하세요" @keyup.enter="applyFilter" />
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
                <div v-for="(segment, index) in hseKeyPerformanceStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.dataList" :key="idx">
                                    <i-card :v-model="data.orgnId" :title="data.orgnNm" :data="data" :type="'basic'" useApprovalStatus :approvalStatus="data.approvalStatus" :disabled="data.useYn === 'N'" @handle="handleEvent" @editor="btnDetail" />
                                </template>
                            </div>
                        </div>
                    </div>
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
const { goRouter, ref, onMounted, nextTick, btnSearch, btnBack, btnDownload, alertMsg, confirmMsg, t } = BaseView();
// Button List
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload'];
// Function Store
import { useHseKeyPerformanceStore } from '@/stores/safewiz/impl/HseKeyPerformanceResults';

const hseKeyPerformanceStore = useHseKeyPerformanceStore();

// Router
import router from '@/router';

import { gsap } from 'gsap';
const searchTerm = ref('');
const checkedRow = ref([]);

onMounted(async () => {
    await hseKeyPerformanceStore.searchMain();
    hseKeyPerformanceStore.segments.forEach((item, index) => {
        if (item.year.includes(hseKeyPerformanceStore.writeYear)) {
            if (!activeSegments.value[index]) {
                toggleAccordion(index, true);
            }
        }
    });
});

btnSearch(() => {
    hseKeyPerformanceStore.searchMain();
});

btnBack(() => {
    router.push('/HsePerformance');
});

btnDownload(() => {
    if (checkedRow.value.length === 0) {
        return alertMsg('warning', t('msgNoItem'));
    }

    confirmMsg('info', t('msgCheckedPrint'), null, { fun: hseKeyPerformanceStore.downloadMaster, param: checkedRow });
});

// 아코디언
const activeSegments = ref({});
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    activeSegments.value[index] = !activeSegments.value[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

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
const btnDetail = async value => {
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('HseKeyPerformanceResultsDetail', param);
};

const applyFilter = () => {
    const filteredSegments = []; // 필터된 세그먼트를 저장할 새로운 배열

    // 검색어 빈 값일 시 모든 데이터 보여줌.
    if (searchTerm.value == '' || searchTerm.value == null) {
        hseKeyPerformanceStore.searchClientGrid(hseKeyPerformanceStore.mainList);
        return;
    }

    for (let el of hseKeyPerformanceStore.segments) {
        // 새로운 객체 생성
        const filteredData = {
            year: el.year,
            dataList: el.dataList.filter(item => {
                return item.orgnNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.revisedDt?.includes(searchTerm.value) || item.enactedDt?.includes(searchTerm.value) || item.docCount?.includes(searchTerm.value) || item.resCount?.includes(searchTerm.value);
            })
        };

        // 필터된 결과가 존재할 경우만 추가
        if (filteredData.dataList.length > 0) {
            filteredSegments.push(filteredData); // 새로운 배열에 추가
        }
    }

    hseKeyPerformanceStore.segments = filteredSegments; // segments를 필터된 배열로 업데이트
};
</script>
