<template>
    <div id="form" class="contents form ui df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="box">
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="performanceEvaluationStore.signCmd" targetType="ERP" :writeYear="performanceEvaluationStore.searchParam.writeYear" :docNo="performanceEvaluationStore.searchParam.docNo"></ISignature>
                </div>
                <hr />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for>작성년도</label>
                                <input v-model="performanceEvaluationStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for>평가일자</label>
                                <input type="text" class="datepicker radius" v-calendar="getDateFormat()" v-model="performanceEvaluationStore.inputForm.evaluationDt" :disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="field">
                                <label for>평가대상</label>
                                <input type="text" class="radius" v-input v-model="performanceEvaluationStore.inputForm.evaluationTarget" :disabled="true" />
                            </div>
                        </div>
                    </div>
                    <div div class="mt8px"></div>
                    <div class="accordion mt8px grid12-12 sm-grid12-12" v-for="(item, mainIdx) in performanceEvaluationStore.inputForm.detailTitle" :key="mainIdx">
                        <div class="list">
                            <button type="button" class="radius w15rem df jcsb aic es-minw50px" :id="`${item}`" @click="toggleAccordion">
                                <!--🐸 title -->
                                <div class="df aic">
                                    <em>{{ item }}</em>
                                    <span class="w1px h1-3rem mx1-2rem bce1e6ed"></span>
                                    <span>{{ performanceEvaluationStore.inputForm.detailList.filter(i => i.evaluationNm === item && i.useYn === 'Y').length }}</span>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <!--🦖 contents -->
                            <div class="segment oh">
                                <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                    <div class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(subItem, subIdx) in performanceEvaluationStore.inputForm.detailList.filter(i => i.evaluationNm === item && i.useYn === 'Y')" :key="String(mainIdx) + String(subIdx)" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: subItem.checked }]">
                                        <div class="w5rem df jcc aic bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdr0pxsolidE1E6ED sm-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="subItem.checked" />
                                        </div>
                                        <div class="w100pcalc5rem sm-w100p">
                                            <div class="pa2-2rem">
                                                <div class="row flex gutters1rem">
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label required>
                                                                <span>평가사항</span>
                                                            </label>
                                                            <input class="br4px" type="text" v-input :id="'evaluationItemNm' + subIdx" :required="subItem.evaluationItemNm" v-model="subItem.evaluationItemNm" @input="() => (subItem.checked = true)" disabled="true" />
                                                        </div>
                                                    </div>

                                                    <div class="grid12-8 es-grid12-12">
                                                        <div class="field">
                                                            <label>평가결과</label>
                                                            <div class="df aic h4-4rem us-gap8px" :key="subItem.resultYn">
                                                                <input type="checkbox" class="w10rem us-wsn" true-value="Y" false-value="N" v-input="'적합'" v-model="subItem.resultYn" @click="resultYnChanged('OK', subIdx)" />
                                                                <input type="checkbox" class="w30rem" true-value="N" false-value="Y" v-input="'부적합'" v-model="subItem.resultYn" @click="resultYnChanged('NG', subIdx)" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 es-grid12-6">
                                                        <div class="field h100p df aife">
                                                            <button class="btn active radius w100p" @click="btnInfo(subItem, 'FUNC', mainIdx)" :disabled="!subItem.infoConfirmDisabled">정보 확인</button>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 es-grid12-6">
                                                        <div class="field h100p df aife">
                                                            <button class="btn active radius w100p" @click="toggleCardAccordion(String(mainIdx) + String(subIdx))" :disabled="!subItem.relatedScreenDisabled">관련 화면</button>
                                                        </div>
                                                        <div class="pr accordion">
                                                            <div class="list not-accordion">
                                                                <div :id="`accordion${String(mainIdx) + String(subIdx)}`" class="pa t1rem r0 segment oh zi2">
                                                                    <div class="df fdc jcfe bd1pxsolid3248F6 br4px oh">
                                                                        <button v-for="(menu, menuIdx) in subItem.menuList.filter(i => i.linkDiv == 'REF')" :key="menuIdx" type="button" v-button class="w20rem bcFFFFFF c3248F6 pa1rem" :class="[{ bdb1pxsolid3248F6: menuIdx !== subItem.menuList.filter(i => i.linkDiv == 'REF').length - 1 }]" @click="btnInfo(menu, menu.linkDiv, mainIdx, subIdx)">
                                                                            {{ menu.menuNm }}
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label for>평가기준</label>
                                                            <textarea class="br4px minh10rem" v-model="subItem.evaluationCriteria" @input="() => (subItem.checked = true)"></textarea>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label for>평가방법</label>
                                                            <textarea class="br4px minh10rem" v-model="subItem.evaluationMethod" @input="() => (subItem.checked = true)"></textarea>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label for>비고</label>
                                                            <textarea class="br4px minh8rem" v-model="subItem.remark" @input="() => (subItem.checked = true)"></textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
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
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import { ref, nextTick, toRaw } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

import router from '@/router';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
import BaseView from '@/components/base/BaseView';
const { confirmMsg, onMounted, validationStore, btnSearch, btnBack, btnSave, btnDownload, t, setRouterParam, getCompId, alertMsg } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
import _ from 'lodash';
// 버튼 세팅
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload'];

//-----------------------------------------------
// [스토어]
import { usePerformanceEvaluationStore } from '@/stores/safewiz/evaluation/performanceEvaluation.js';
const performanceEvaluationStore = usePerformanceEvaluationStore();
// ====================================
// [ 아코디언 ]

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
// =====================================

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출

    if (param) {
        performanceEvaluationStore.searchParam = {
            compId: getCompId(),
            writeYear: param.writeYear,
            docType: 'ER',
            docNo: param.docNo
        };
        await performanceEvaluationStore.getEvaluationReportPerformanceList(false);
        if (param.docSeq !== undefined) {
            //모니터링, 성과측정 및 평가 결과서 상세에서 넘어왔을 경우 해당 아코디언의 정보와 일치하는 아코디언만 펼침
            const accordion = document.getElementById(param.docSeq);
            const container = accordion.parentNode;
            const segment = container.querySelector('.segment.oh');
            const isOpen = accordion.classList.toggle('active');
            const alreadyOpen = accordion.classList.toggle('expanded');
            if (alreadyOpen === true) {
                animateAccordion(segment, isOpen);
            }
        } else {
            //그렇지 않을 경우 최상위 아코디언만 펼침
            const accordion = document.getElementById(performanceEvaluationStore.inputForm.detailList[0].evaluationNm);
            const container = accordion.parentNode;
            const segment = container.querySelector('.segment.oh');
            const isOpen = accordion.classList.toggle('active');
            const alreadyOpen = accordion.classList.toggle('expanded');
            if (alreadyOpen === true) {
                animateAccordion(segment, isOpen);
            }
        }
    } else if (!performanceEvaluationStore.inputForm || !performanceEvaluationStore.inputForm.cmd) {
        router.push('PerformanceEvaluationTable');
        return;
    }

    performanceEvaluationStore.signature = signatureComponent.value;

    performanceEvaluationStore.inputForm.detailTitle = [...new Set(performanceEvaluationStore.inputForm.detailList.map(item => item.evaluationNm))];
});

//-----------------------------------------------

//-----------------------------------------------

// ISignature 컴포넌트 참조를 위한 ref
const signatureComponent = ref();

//-----------------------------------------------

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(performanceEvaluationStore.originData);
    const currentData = toRaw(performanceEvaluationStore.inputForm);
    return JSON.stringify(initialData) !== JSON.stringify(currentData);
};

// 공통 메시지 확인 처리 함수
const handleDataChange = (confirmMessage, callback, param = null) => {
    if (isDataChanged()) {
        confirmMsg('info', confirmMessage, '', { fun: callback, param });
    } else {
        callback(param); // 변경 사항이 없으면 바로 실행
    }
};

btnBack(() => {
    if (!_.isEqual(performanceEvaluationStore.currentInputForm, performanceEvaluationStore.inputForm)) {
        handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', performanceEvaluationStore.goBack);
    } else {
        performanceEvaluationStore.goBack();
    }
});

/* 조회 */
btnSearch(async () => {
    if (!_.isEqual(performanceEvaluationStore.currentInputForm, performanceEvaluationStore.inputForm)) {
        await handleDataChange('저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', btnSearchAction, true);
    } else {
        btnSearchAction();
    }
});

const btnSearchAction = async () => {
    performanceEvaluationStore.getEvaluationReportPerformanceList();
    await signatureComponent.value.Search();
};

/* 저장 */
btnSave(async () => {
    //const isValid = await validationStore.validateAllFields('form', true);
    //if (isValid)
    await performanceEvaluationStore.btnSave(true);
});

/* 출력 */
btnDownload(() => {
    const checkedList = performanceEvaluationStore.inputForm.detailList.filter(item => item.checked);
    if (checkedList.length == 0) {
        confirmMsg('info', t('msgPrint'), '', { fun: btnDownloadAction });
    } else {
        confirmMsg('info', '선택한 항목을 출력하시겠습니까?', '', { fun: btnDownloadAction });
    }
});

const btnDownloadAction = () => {
    performanceEvaluationStore.btnDetailDownload(true);
};
//-----------------------------------------------
// 체크박스 변경 이벤트 핸들러
const onCheckboxChange = (item, isChecked, value) => {
    if (isChecked) {
        item.resultYn = value;
    } else {
        item.resultYn = null;
    }
};
//-----------------------------------------------

//-----------------------------------------------

//카드 관련 이벤트

const toggleCardAccordion = async index => {
    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
};

const accordionSet = (index, duration) => {
    const segmentId = `accordion${index}`;
    const segment = document.getElementById(segmentId);
    const isOpen = segment.classList.toggle('active');
    if (segment) {
        gsap.to(segment, {
            height: isOpen ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    }
};

//적합, 부적합 클릭 이벤트
const resultYnChanged = (type, subIdx) => {
    const item = performanceEvaluationStore.inputForm.detailList[subIdx];
    item.checked = true;
    if (type === 'OK' && item.resultYn == 'Y') {
        item.resultYn = null;
    } else if (type === 'NG' && item.resultYn == 'N') {
        item.resultYn = null;
    }
};

//카드의 정보확인 버튼 클릭 이벤트
const btnInfo = async (item, type, mainIdx, subIdx) => {
    let param = {};
    let filteredData = {};
    if (type === 'FUNC') {
        //정보 확인 버튼 클릭
        filteredData = {
            ...item,
            menuList: item.menuList.filter(el => el.linkDiv === type),
            type: 'FUNC'
        };
    } else {
        // 관련 화면 클릭=
        const title = performanceEvaluationStore.inputForm.detailTitle[mainIdx];
        const detailList = performanceEvaluationStore.inputForm.detailList.filter(item => item.evaluationNm == title);
        const menuData = detailList.flatMap(item => item.menuList).find(val => val.funcPath === item.funcPath);

        filteredData = {
            ...detailList[subIdx],
            menuList: [menuData],
            type: ''
        };
    }
    if (filteredData.menuList.length > 0) {
        param = {
            funcPath: filteredData.menuList[0].funcPath,
            writeYear: filteredData.writeYear,
            docNo: filteredData.docNo,
            docType: filteredData.menuList[0].sendDocType,
            menuId: filteredData.menuList[0].menuId,
            type: filteredData.type
        };
        confirmMsg('info', '[' + filteredData.menuList[0].menuNm + ']\n화면으로 이동하시겠습니까?', '', { fun: btnInfoAction, param: param });
    } else {
        alertMsg('error', '정보가 없습니다.');
        return;
    }
};

const btnInfoAction = param => {
    performanceEvaluationStore.btnInfo(param);
};

//-----------------------------------------------
</script>
