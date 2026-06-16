<template>
    <!-- 콘텐츠 영역 -->
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="control-field mb2-2rem form ui">
            <div class="row flex gutters1rem">
                <div class="grid12-3 us-grid12-12">
                    <input class="datepicker radius" v-input type="text" v-calendar="'yyyy'" year v-model="safetyInspectionLogStore.writeYear" @input="safetyInspectionLogStore.btnSearch(true)" />
                </div>
                <div class="grid12-8 us-grid12-12">
                    <div class="df aic">
                        <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="safetyInspectionLogStore.searchTerm" @keyup.enter="safetyInspectionLogStore.applyFilter" />
                        <button type="button" class="shrink0 bcffffff" @click="safetyInspectionLogStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-1 us-grid12-12 df jcfe">
                    <div class="field">
                        <div class="df aic h4-4rem">
                            <input v-input="['My', '전체']" type="checkbox" :checked="safetyInspectionLogStore.myDataOnly" @change="safetyInspectionLogStore.changeMyDataOnly()" class="df switch" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="accordion df fdc rg8px">
                <div v-for="(stdEq, sIndex) in safetyInspectionLogStore.filteredStdEqList" :key="sIndex" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${sIndex}`" @click="safetyInspectionLogStore.toggleAccordion($event, sIndex)" :class="{ active: safetyInspectionLogStore.activeSegments[sIndex] }">
                        <em>{{ stdEq.title }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(eq, eIndex) in stdEq.dataList" :key="eIndex">
                                    <i-card :v-model="eq.checked" :data="eq" :disabled="eq.useYn === 'N'" :type="'basic'" :title="eq.equipmentNm" @editor="btnDetail(eq)" />
                                </template>
                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="addLog(stdEq)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('신규 안전점검일지 추가') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-if="safetyInspectionLogStore.stdEqList.length === 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="addLog(null)">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <span class="db mt1rem fs2rem c999999">{{ t('신규 안전점검일지 추가') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { gsap } from 'gsap';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const { nextTick, ref, confirmMsg, alertMsg, goRouter, onMounted, t, btnSearch, btnBack, btnAdd, btnDownload, getCurrentDate } = BaseView();

//-----------------------------------------------
// [스토어]

import { useSafetyInspectionLogStore } from '@/stores/safewiz/impl/safetyInspectionLog.js';
const safetyInspectionLogStore = useSafetyInspectionLogStore();

//-----------------------------------------------

//-----------------------------------------------

onMounted(async () => {
    safetyInspectionLogStore.inputForm = {};
    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDownload'];
    await safetyInspectionLogStore.initAccordion();
});
btnSearch(() => {
    safetyInspectionLogStore.btnSearch(true);
});
const btnDetail = eq => {
    goPage(eq);
};
btnAdd(() => {
    safetyInspectionLogStore.inputForm.cmd = 'I';
    goPage({ stdEqId: null, equipmentId: null });
});
const addLog = stdEq => {
    safetyInspectionLogStore.inputForm.cmd = 'I';
    goPage({ stdEqId: stdEq?.id, equipmentId: null, stdEqNm: stdEq?.title });
};
const goPage = eq => {
    const param = {
        writeYear: safetyInspectionLogStore.writeYear,
        docNo: eq.stdEqId,
        docSeq: eq.equipmentId,
        docDetailSeq: eq.checkDt,
        stdEqNm: eq.stdEqNm
    };
    goRouter('SafetyInspectionLogDetail', param);
};

btnDownload(() => {
    let checkedCardList = [];
    safetyInspectionLogStore.filteredStdEqList.forEach(el => {
        checkedCardList = [...checkedCardList, ...(el.dataList ? el.dataList.filter(data => data.checked) : [])];
    });
    if (checkedCardList.length == 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const valid = checkedCardList.some(data => data.checkDt == null);
    if (valid) {
        alertMsg('warning', '안전점검일지가 없는 설비는 출력할 수 없습니다.');
        return;
    }
    confirmMsg('warning', t('msgCheckedPrint'), '', {
        fun: downloadAction,
        param: checkedCardList
    });
});
const downloadAction = list => {
    safetyInspectionLogStore.btnDownloadList(list);
};
</script>
