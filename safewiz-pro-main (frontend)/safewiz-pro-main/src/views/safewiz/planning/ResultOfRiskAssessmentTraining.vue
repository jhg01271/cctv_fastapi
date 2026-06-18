<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row gutters1rem">
                <div class="grid12-3 sm-grid12-4 us-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="resultOfRiskAssessmentTrainingStore.searchParam.searchText" class="datepicker w20rem radius es-w100p" />
                </div>
                <div class="grid12-9 sm-grid12-8 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="resultOfRiskAssessmentTrainingStore.searchParam.searchText2" @keyup.enter="search" />
                        <button type="button" class="shrink0" @click="search">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent>
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px">
                <!-- 1 -->
                <div v-for="(segment, index) in resultOfRiskAssessmentTrainingStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ segment.month }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="item in segment.monthList" :key="item.writeYear + item.docNo">
                                    <i-card :v-model="item.checked" :data="item" :disabled="item.useYn === 'N'" :title="formatDate(item.asmntDate)" @editor="btnDetail" />
                                </template>

                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="resultOfRiskAssessmentTrainingStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addResultOfRiskAssessmentTraining') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 리스트가 없을시 버튼만 나오도록 -->
                <div v-if="resultOfRiskAssessmentTrainingStore.segments.length < 1">
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="resultOfRiskAssessmentTrainingStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addResultOfRiskAssessmentTraining') }}</span>
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
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { goRouter, ref, watch, btnSearch, btnAdd, btnDelete, confirmMsg, btnDownload, alertMsg, t, btnBack, router, formatDate } = BaseView();
import { useResultOfRiskAssessmentTraining } from '@/stores/safewiz/planning/ResultOfRiskAssessmentTraining.js';
import { nextTick, onMounted } from 'vue';
const resultOfRiskAssessmentTrainingStore = useResultOfRiskAssessmentTraining();
const riskStore = useRiskAStore();
OverlayScrollbarsComponent.options = {
    scrollbars: {
        autoHide: 'hover',
        x: 'hidden',
        y: 'visible'
    }
};
onMounted(async () => {
    // 맵핑 받아오기
    resultOfRiskAssessmentTrainingStore.searchParam.searchText = riskStore.searchParam.searchText;
    search(true);
    console.log('@@@activeSegments.value[index]', activeSegments.value[0]);
});

// 아코디언 이벤트
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

// 검색
const search = (mounted = false) => {
    resultOfRiskAssessmentTrainingStore.getResultOfRiskAssessmentTrainingList().then(res => {
        if (res.result.length > 0 && mounted) {
            toggleAccordion(0);
        }
    });
};

// 년도 검색 변경시에 조회
watch(
    () => resultOfRiskAssessmentTrainingStore.searchParam.searchText,
    () => {
        // 맵핑 던져주기
        riskStore.searchParam.searchText = resultOfRiskAssessmentTrainingStore.searchParam.searchText;
        search();
    },
    { deep: true }
);

// 우측 버튼 레이아웃 창 생성
const layoutStore = useButtonListStore();
layoutStore.useBtnList.push('btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload');
import { useButtonListStore } from '@/stores/buttonList.js';
import { gsap } from 'gsap';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';

// 우측 버튼 레이아웃 목록 버튼
btnBack(() => {
    router.push({ name: 'RiskAssessment' });
});
// 우측 버튼 레이아웃 조회 버튼
btnSearch(() => {
    search();
});

// 우측 버튼 레이아웃 추가 버튼
btnAdd(() => {
    resultOfRiskAssessmentTrainingStore.btnAdd();
});

// 우측 버튼 레이아웃 삭제 버튼
btnDelete(() => {
    // 체크한 값이 없을경우 메세지 호출
    if (resultOfRiskAssessmentTrainingStore.resultOfRiskAssessmentTrainingList.filter(item => item.checked === true).length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // 체크한 값만 삭제
    confirmMsg('warning', t('msgDelete'), '', {
        fun: resultOfRiskAssessmentTrainingStore.deleteResultOfRiskAssessmentTraining,
        param: resultOfRiskAssessmentTrainingStore.resultOfRiskAssessmentTrainingList.filter(item => item.checked === true)
    });
});

// 우측 버튼 레이아웃 출력 버튼
btnDownload(() => {
    // 체크한 값이 없을경우 메세지 호출
    if (resultOfRiskAssessmentTrainingStore.resultOfRiskAssessmentTrainingList.filter(item => item.checked === true).length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // 체크한 값만 다운로드
    confirmMsg('info', t('msgCheckedPrint'), null, { fun: btnDownloadDetail });
    
});

const btnDownloadDetail = () => {
    resultOfRiskAssessmentTrainingStore.getResultOfRiskAssessmentTrainingReport(resultOfRiskAssessmentTrainingStore.resultOfRiskAssessmentTrainingList.filter(item => item.checked === true));
}

// 카드 상세보기 버튼
const btnDetail = async value => {
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('ResultOfRiskAssessmentTrainingDetail', param);
};
</script>
