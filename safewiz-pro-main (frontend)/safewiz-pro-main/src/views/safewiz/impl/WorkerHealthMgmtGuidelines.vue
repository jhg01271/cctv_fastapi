<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- <h3>근로자 및 이해관계자</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input v-model="workerHealthMgmtStore.searchParam.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker radius" @input="workerHealthMgmtStore.getWHMgmtGuidList(true)" />
                </div>
                <div class="grid12-9 es-grid12-12">
                    <div class="df aic w100p bcffffff">
                        <input v-model="workerHealthMgmtStore.searchText" v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="workerHealthMgmtStore.dataFilter" />
                        <button type="submit" class="shrink0" @click.stop="workerHealthMgmtStore.dataFilter">
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
                <div v-for="(segment, index) in workerHealthMgmtStore.segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="workerHealthMgmtStore.toggleAccordion" :class="{ active: workerHealthMgmtStore.activeSegments[index] }">
                        <em>{{ segment.month }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, idx) in segment.data" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :modelValue="data.checked" :data="data" :type="'basic'" :title="formatDate(data.counselDt)" @handle="handleEvent" @editor="btnDetail" :disabled="data.useYn == 'N'"></i-card>
                                </template>
                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="workerHealthMgmtStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addWorkerHealthGuide') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="callPopup">
                <div class="contents form ui w40rem md-w100p">
                    <div class="form ui">
                        <h3>{{ '인원 불러오기' }}</h3>
                    </div>
                    <div class="bd1pxsolide1e6ed br4px">
                        <OverlayScrollbarsComponent class="pa1-6rem maxh30rem es-maxh30rem">
                            <form ref="myForm" id="form">
                                <div class="control-field ui form box">
                                    <div class="row flex gutters1rem">
                                        <div class="grid10-10 sm-grid12-6">
                                            <div class="field">
                                                <label for="counselDt" required>
                                                    <span>상담일자</span>
                                                </label>
                                                <input v-model="callPopupParam.counselDt" id="counselDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" required />
                                            </div>
                                        </div>
                                        <div class="grid10-10 sm-grid12-6">
                                            <div class="field">
                                                <label required>
                                                    <span>대상 조직</span>
                                                </label>
                                                <i-chips :chips="[{ id: callPopupParam.orgnId, nm: callPopupParam.orgnNm }]" @popup="addOrgnPopup" required />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </OverlayScrollbarsComponent>
                    </div>
                    <!-- <base-select-popup :title="'평가항목'" uniqueKey="evaluationId" filterKey="evaluationNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-param="complianceEvaluationStore.searchParam" :fetch-data="getUsedEvaluationChecklist" @close="closeEvaluationType" /> -->
                    <i-PopupButtonList :btnInfo="{ closeBtn: true, applyBtn: true }" @close="btnCallClose" @apply="btnCallSave" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'조직'" :inputForm="workerHealthMgmtStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgnPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
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
const { validationStore, ref, onMounted, t, formatDate, getCurrentDate, watch, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, btnCall, alertMsg, confirmMsg, getCompId, formatDateForDB } = BaseView();

import { useWorkerHealthMgmtStore } from '@/stores/safewiz/impl/workerHealthMgmt.js';
const workerHealthMgmtStore = useWorkerHealthMgmtStore();

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload', 'btnCall'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const accordionRefs = ref([]);

const toggleAccordion = async index => {
    workerHealthMgmtStore.activeSegments[index] = !workerHealthMgmtStore.activeSegments[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: workerHealthMgmtStore.activeSegments[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

/* 조회 */
btnBack(() => {
    planningCtrlStore.searchParam.searchText = workerHealthMgmtStore.searchParam.writeYear;
    router.push({ name: 'PlanningAndControl' });
});

/* 조회 */
btnSearch(() => {
    workerHealthMgmtStore.getWHMgmtGuidList(true);
});

/* 추가, 상세보기 */
btnAdd(() => {
    workerHealthMgmtStore.btnAdd();
});

btnDownload(() => {
    let checkedList = [];
    workerHealthMgmtStore.segments.forEach(el =>
        el.data.forEach(id => {
            if (id.checked) {
                checkedList.push(id.docNo);
            }
        })
    );
    if(checkedList.length === 0){
        alertMsg('warning', t('msgNoItem'));
        return;
    }         
    confirmMsg('info', '선택한 항목을 출력하시겠습니까?', '', { fun: btnDownloadAction, param: checkedList });
});

const btnDownloadAction = checkedList => {
    workerHealthMgmtStore.btnDownload(checkedList);
};

btnDelete(() => {
    workerHealthMgmtStore.btnDelete();
});

// ***************************** //
// 불러오기 팝업
// ***************************** //
import { getDateFormat } from '@/utils/dateUtil.js';
const callPopup = ref(null);
const callPopupParam = ref({});

btnCall(() => {
    callPopupParam.value = {};
    callPopupParam.value.counselDt = getCurrentDate();
    callPopup.value.onOpen();
});

const btnCallSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        const result = await workerHealthMgmtStore.validateGuidePopup(callPopupParam);
        if (result.success) {
            if (result.data.list.length === 0) {
                alertMsg('warning', `[${callPopupParam.value.orgnNm}] \n조직에 인원이 없거나, 이미 등록되어있습니다.`);
                return;
            }
            confirmMsg('info', `[${callPopupParam.value.orgnNm}] ${result.data.list.length}명이 저장됩니다. \n 저장하시겠습니까?`, '', { fun: callSaveAction, param: result.data.list });
        }
    }
};
// 불러오기 팝업 저장 버튼 클릭
const callSaveAction = params => {
    console.log('@@ saveData ==> ', params);
    let saveParam = [];
    params.forEach(el => {
        saveParam.push({
            writeYear: workerHealthMgmtStore.searchParam.writeYear,
            docType: 'WHMG',
            useYn: 'Y',
            counselDt: formatDateForDB(callPopupParam.value.counselDt),
            orgnId: callPopupParam.value.orgnId,
            orgnNm: callPopupParam.value.orgnNm,
            hrId: el.hrId,
            hrNm: el.hrNm,
            sex: el.sex,
            age: el.age,
            serviceYears: el.serviceYears
        });
    });
    workerHealthMgmtStore.saveWHMgmtGuidePopup(saveParam);
    callPopup.value.onClose();
};

// 불러오기 팝업 닫기 버튼 클릭
const btnCallClose = () => {
    callPopup.value.onClose();
    callPopupParam.value = {};
};

import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const orgnPopup = ref(null);

const addOrgnPopup = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    orgnPopup.value.onOpen();
};
const closeOrgnPopup = e => {
    if (e.length > 0) {
        callPopupParam.value.orgnId = e[0].orgnId;
        callPopupParam.value.orgnNm = e[0].orgnNm;
    }
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    orgnPopup.value.onClose();
};
onMounted(() => {
    if (!workerHealthMgmtStore.searchParam.writeYear) {
        workerHealthMgmtStore.searchParam.writeYear = getCurrentDate().substring(0, 4);
    }
    // if (!workerHealthMgmtStore.writeYear) {
    //     workerHealthMgmtStore.searchParam.writeYear = getCurrentDate().substring(0, 4);
    // } else {
    //     workerHealthMgmtStore.searchParam.writeYear = workerHealthMgmtStore.writeYear;
    // }

    workerHealthMgmtStore.getWHMgmtGuidList();
});

//상세보기 버튼
const btnDetail = e => {
    workerHealthMgmtStore.searchParam = {
        compId: getCompId(),
        writeYear: e.writeYear,
        docType: 'WHMG',
        docNo: e.docNo
    };
    workerHealthMgmtStore.goDetail();
};

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        workerHealthMgmtStore.checkedList.push(e);
    } else {
        workerHealthMgmtStore.checkedList = workerHealthMgmtStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
    }
};
</script>
<style lang="scss" scoped>
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
