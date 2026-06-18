<template>
    <!-- 콘텐츠 영역 -->
    <div id="form" class="contents df fdc">
        <!-- <h3>근로자 및 이해관계자</h3> -->
        <!-- 검색 필드 -->
        <div class="ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input v-model="monitoringMngStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="performanceEvaluationStore.getEvaluationReport(true)" />
                </div>
                <div class="grid12-9 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-model="performanceEvaluationStore.searchText" v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" @keyup.enter="performanceEvaluationStore.dataFilter" />
                        <button type="submit" class="shrink0" @click.stop="performanceEvaluationStore.dataFilter">
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
                <div v-for="(segment, index) in performanceEvaluationStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="performanceEvaluationStore.toggleAccordion" :class="{ active: performanceEvaluationStore.activeSegments[index] }">
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
                                    <i-card :v-model="data.checked" :modelValue="data.checked" :data="data" :type="'basic'" :title="data.evaluationTarget" @handle="handleEvent" @editor="btnDetail" :disabled="data.useYn == 'N'" :useApprovalStatus="true" :approvalStatus="data.approvalStatusP"></i-card>
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
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const { ref, onMounted, t, formatDate, getCurrentDate, watch, btnBack, btnSearch, btnDelete, btnDownload, alertMsg, toastPopup, confirmMsg, getCompId, goRouter } = BaseView();
//-----------------------------------------------
// [스토어]
import { usePerformanceEvaluationStore } from '@/stores/safewiz/evaluation/performanceEvaluation.js';
const performanceEvaluationStore = usePerformanceEvaluationStore();

import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();

const currYear = new Date().getFullYear();
// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const accordionRefs = ref([]);

const toggleAccordion = async index => {
    performanceEvaluationStore.activeSegments[index] = !performanceEvaluationStore.activeSegments[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: performanceEvaluationStore.activeSegments[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

/* 조회 */
btnBack(() => {
    router.push({ name: 'MonitoringManage' });
});

/* 조회 */
btnSearch(() => {
    performanceEvaluationStore.getEvaluationReport(true);
});

btnDownload(() => {
    let checkedList = [];
    performanceEvaluationStore.segments.forEach(el =>
        el.data.forEach(id => {
            if (id.checked) {
                checkedList.push(id.docNo);
            }
        })
    );
    if(checkedList.length == 0){
        alertMsg('warning', t('msgNoItem'));
        return
    }else{
        confirmMsg('info', '선택한 항목을 출력하시겠습니까?', '', { fun: btnDownloadAction, param : checkedList})
    }
});

const btnDownloadAction = checkedList => {
    performanceEvaluationStore.btnDownload(checkedList);
}

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

onMounted(() => {
    if (!performanceEvaluationStore.writeYear) {
        performanceEvaluationStore.searchParam.writeYear = getCurrentDate().substring(0, 4);
    } else {
        performanceEvaluationStore.searchParam.writeYear = performanceEvaluationStore.writeYear;
    }

    performanceEvaluationStore.getEvaluationReport();
});

//상세보기 버튼
const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo 
    }

    goRouter('PerformanceEvaluationTableDetail', param);
};

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        performanceEvaluationStore.checkedList.push(e);
    } else {
        performanceEvaluationStore.checkedList = performanceEvaluationStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
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
