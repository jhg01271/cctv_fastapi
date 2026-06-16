<template>
    <!-- 콘텐츠 영역 -->
    <div id="form" class="contents df fdc">
        <!-- <h3>근로자 및 이해관계자</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input v-model="monitoringMngStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="evaluationReportStore.getEvaluationReport(true)" />
                </div>
                <div class="grid12-9 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-model="evaluationReportStore.searchText" v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" @keyup.enter="evaluationReportStore.dataFilter" />
                        <button type="submit" class="shrink0" @click.stop="evaluationReportStore.dataFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
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
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px">
                <!-- 1 -->
                <div v-for="(segment, index) in evaluationReportStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="evaluationReportStore.toggleAccordion" :class="{ active: evaluationReportStore.activeSegments[index] }">
                        <em>{{ segment.month }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem" :key="segment.data">
                                <!--  -->
                                <template v-for="(data, idx) in segment.data" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :modelValue="data.checked" :data="data" :type="'basic'" :title="data.evaluationTarget" @handle="handleEvent" @editor="btnDetail" :disabled="data.useYn == 'N'" :useApprovalStatus="true" :approvalStatus="data.approvalStatusR"></i-card>
                                </template>
                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="evaluationReportStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addEvaluationReport') }}</span>
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
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const { ref, onMounted, t, formatDate, getCurrentDate, watch, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, toastPopup, alertMsg, confirmMsg, goRouter } = BaseView();
//-----------------------------------------------
// [스토어]
import { useEvaluationReportStore } from '@/stores/safewiz/evaluation/evaluationReport.js';
const evaluationReportStore = useEvaluationReportStore();

import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();
// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const accordionRefs = ref([]);

/* 조회 */
btnBack(() => {
    router.push({ name: 'MonitoringManage' });
});

/* 조회 */
btnSearch(() => {
    evaluationReportStore.getEvaluationReport(true);
});

/* 추가, 상세보기 */
btnAdd(() => {
    evaluationReportStore.btnAdd();
});

btnDownload(async () => {
    let checkedList = [];
    await evaluationReportStore.segments.forEach(el =>
        el.data.forEach(id => {
            if (id.checked) {
                checkedList.push(id.docNo);
            }
        })
    );
    if (checkedList.length == 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    } else {
        confirmMsg('info', '선택한 항목을 출력하시겠습니까?', '', { fun: btnDownloadAction, param: checkedList });
    }
});

const btnDownloadAction = checkedList => {
    evaluationReportStore.btnDownload(checkedList);
};

btnDelete(() => {
    evaluationReportStore.btnDelete();
});

const deleteData = param => {};

/* 년도 클릭시 패널 컨트롤 */
const togglePanel = idx => {
    const panelContents = document.querySelectorAll('.panel-content');
    const panelContent = panelContents[idx];

    // 패널 콘텐츠의 현재 상태를 확인하고 전환
    if (panelContent.classList.contains('open')) {
        panelContent.classList.remove('open');
    } else {
        // 모든 패널을 닫습니다.
        panelContents.forEach(content => content.classList.remove('open'));
        panelContent.classList.add('open');
    }
};

onMounted(async () => {
    evaluationReportStore.getEvaluationReport();
});

//상세보기 버튼
const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: 'ER',
        docNo: e.docNo
    };
    goRouter('EvaluationReportDetail', param);
};

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        evaluationReportStore.checkedList.push(e);
    } else {
        evaluationReportStore.checkedList = evaluationReportStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
    }
};
</script>
<style lang="scss" scoped>
.list {
    text-align: center;
}
.year {
    width: 95%;
}
.expansion-panel {
    border: 1px solid #ddd;
    margin: 10px;
    border-radius: 4px;
    overflow: hidden;
}

.panel-header {
    /* background-color: #f5f5f5; */
    background-color: #43d491;
    border: none;
    padding: 10px 15px;
    text-align: center;
    width: 100%;
    cursor: pointer;
    outline: none;
    font-size: 16px;
}

.panel-content {
    padding: 15px;
    display: none; /* 기본적으로 숨김 */
    background-color: white;
}

.panel-content.open {
    display: block; /* 열렸을 때 표시 */
}
</style>
