<template>
    <div id="form" class="contents form ui wbka">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p"
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
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 df aife es-grid12-12 es-order1">
                            <div class="field w100p">
                                <label for="">작성년도</label>
                                <input :value="evaluationReportStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>
                        <div class="grid12-9 es-grid12-12">
                            <div class="field">
                                <ISignature ref="signatureComponent" :cmd="evaluationReportStore.signCmd" :writeYear="evaluationReportStore.searchParam.writeYear" :docNo="evaluationReportStore.searchParam.docNo" targetType="ER"></ISignature>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-12">
                            <div class="field">
                                <label for required>
                                    <span>평가일자</span>
                                </label>
                                <input type="text" class="datepicker radius" :min="evaluationReportStore.searchParam.writeYear + '.01.01'" :max="evaluationReportStore.searchParam.writeYear + '.12.31'" v-calendar="getDateFormat()" placeholder="2024.03.20" @input="onDateInput('evaluationDt', $event)" :value="formatDate(evaluationReportStore.inputForm.evaluationDt)" required />
                            </div>
                        </div>

                        <div class="grid12-7 es-grid12-12">
                            <div class="field">
                                <label for required>
                                    <span>평가대상</span>
                                </label>
                                <input type="text" class="radius" v-input id="평가대상" required v-model="evaluationReportStore.inputForm.evaluationTarget" />
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for>사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :true-value="'Y'" :false-value="'N'" :checked="evaluationReportStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="evaluationReportStore.inputForm.useYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field h100p cp df aife">
                                <button class="w100p h4-4rem fs1-6rem pa1-2rem bcF5F6FE br4px c3248F6 bd1pxsolid3248F6" @click="openAddEvaluationListPopup">성과 평가 항목 선택</button>
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <button class="btn active radius w100p" @click="openEvaluationPopup">성과 평가 항목 관리</button>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for v-if="evaluationReportStore.inputForm.detailList && evaluationReportStore.inputForm.detailList.length < 1">*성과 평가 항목을 먼저 선택하세요.</label>
                            </div>
                        </div>
                    </div>
                    <div div class="mt8px"></div>
                    <div class="accordion mt8px grid12-12 sm-grid12-12" v-for="(item, mainIdx) in evaluationReportStore.inputForm.detailTitle" :key="mainIdx">
                        <div class="list">
                            <button type="button" class="radius w15rem df jcsb aic es-minw50px" :id="`${item}`" @click="toggleAccordion">
                                <!--🐸 title -->
                                <div class="df aic">
                                    <em :title="item">{{ item }}</em>
                                    <span class="w1px h1-3rem mx1-2rem bce1e6ed"></span>
                                    <span>{{ evaluationReportStore.inputForm.detailList.filter(i => i.evaluationNm === item && i.useYn === 'Y').length }}</span>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <!--🦖 contents -->
                            <div class="segment oh">
                                <div class="pa2-2rem df fdc us-pa1rem bcF8F9FB">
                                    <div class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(subItem, subIdx) in evaluationReportStore.inputForm.detailList.filter(i => i.evaluationNm === item)" :key="String(mainIdx) + String(subIdx)" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: subItem.checked }]">
                                        <div class="w5rem df jcc aic bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdr0pxsolidE1E6ED sm-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="subItem.checked" />
                                        </div>
                                        <div class="w100pcalc5rem sm-w100p">
                                            <div class="pa2-2rem">
                                                <div class="row flex gutters1rem">
                                                    <div class="grid12-10 lg-grid12-10 es-grid12-12">
                                                        <div class="field">
                                                            <label required>
                                                                <span>평가사항</span>
                                                            </label>
                                                            <input class="br4px" type="text" v-input :id="'evaluationItemNm' + subIdx" :required="subItem.evaluationItemNm" v-model="subItem.evaluationItemNm" @input="() => (subItem.checked = true)" disabled="true" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 lg-grid12-2 es-grid12-12">
                                                        <div class="field">
                                                            <label :for="'item.useYn' + subIdx">사용여부</label>
                                                            <div class="df aic h4-4rem" :key="subItem.useYn">
                                                                <input :true-value="'Y'" :false-value="'N'" :checked="subItem.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="subItem.useYn" :id="'item.useYn' + String(mainIdx) + String(subIdx)" @click="() => (subItem.checked = true)" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-4 lg-grid12-6 es-grid12-12">
                                                        <div class="field">
                                                            <label>
                                                                <span>평가결과</span>
                                                                <span>
                                                                    <button class="ml4px" v-tooltip="t('성과평가표에서 작성한 정보가 표시됩니다.')">
                                                                        <!-- 팝업 info 아이콘 -->
                                                                        <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                                    </button>
                                                                </span>
                                                            </label>
                                                            <div class="df aic h4-4rem" :key="subItem.resultYn">
                                                                <input type="checkbox" class="w20rem" true-value="Y" false-value="N" v-input="'적합'" v-model="subItem.resultYn" disabled="true" />
                                                                <input type="checkbox" class="w30rem" true-value="N" false-value="Y" v-input="'부적합'" v-model="subItem.resultYn" disabled="true" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 lg-grid12-6 es-grid12-12">
                                                        <div class="field">
                                                            <label>달성률(%)</label>
                                                            <input class="br4px" type="number" v-input :required="subItem.achievementRate" v-model="subItem.achievementRate" min="0" step="1" max="100" placeholder="0" @input="() => (subItem.checked = true)" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 lg-grid12-6 es-grid12-6">
                                                        <div class="field h100p df aife">
                                                            <button class="btn active radius w100p" @click="btnInfo(subItem, 'FUNC', mainIdx)" :disabled="!subItem.infoConfirmDisabled">정보 확인</button>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 lg-grid12-6 es-grid12-6">
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
                                                            <label for>내용</label>
                                                            <textarea class="br4px minh8rem" v-model="subItem.contents" @input="() => (subItem.checked = true)"></textarea>
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
        <teleport to="body">
            <i-PopupDialog ref="evaluationPopup">
                <div class="contents w1024px md-w100p">
                    <DataSetManageByItemPopup ref="managePopup" title="성과 평가 항목 관리" :masterTitle="'성과 평가 항목'" :masterReadonly="false" :masterApi="getEvaluationChecklist" :masterApiParam="{}" masterKey="evaluationId" masterDisplayKey="evaluationNm" :detailTitle="'성과 평가 사항'" :detailApi="getEvaluationChecklistDetailBySearchText" detailKey="evaluationId" :detailOption="detailOption" @save="btnEvaluationMngSave" @delete="btnEvaluationMngDelete" @close="closeEvaluationMng" @sample="btnEvaluationMngSample" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="samplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'성과 평가 항목 예시'" filterKey="evaluationNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getEvaluationChecklistDataSet" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="addEvaluationListPopup">
                <!-- 성과 평가 항목 선택 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'성과 평가 항목 선택'" uniqueKey="evaluationId" filterKey="evaluationNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :selectedIdList="evaluationReportStore.inputForm.detailList" :fetch-data="getEvaluationChecklistFunc" :fetch-param="{}" @apply="applyAddEvaluationListMngPopup" @close="closeAddEvaluationListMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import { ref, toRaw } from 'vue';
import router from '@/router';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';
import { gsap } from 'gsap';
const scrollContainer = ref(null);
import BaseView from '@/components/base/BaseView';
const { confirmMsg, onMounted, validationStore, t, formatDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, setRouterParam, getCompId, nextTick, alertMsg, getDuplicatedData } = BaseView();
// 버튼 세팅
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSave'];

// [스토어]
import { useEvaluationReportStore } from '@/stores/safewiz/evaluation/evaluationReport.js';
const evaluationReportStore = useEvaluationReportStore();

import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();
//====================================================================================
// 평가항목 관리 팝업
import { getEvaluationChecklist, saveEvaluationChecklistByPopup, getEvaluationChecklistDetailBySearchText, deleteEvaluationChecklist, getEvaluationChecklistDataSet } from '@/stores/safewiz/evaluation/api/evaluationReportApi.js';
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const evaluationPopup = ref(null);
const managePopup = ref(null);
const samplePopup = ref(null);
const addEvaluationListPopup = ref(null);

//성과 평가 항목 관리 팝업 ############################################################################################################################################
const openEvaluationPopup = () => {
    if (evaluationPopup.value) {
        evaluationPopup.value.onOpen();
    }
};

// 우측 카드 에디팅 옵션 데이터
const detailOption = ref([
    {
        fieldDisplayKey: 'evaluationItemNm',
        fieldNm: '성과 평가 사항',
        fieldKey: 'evaluationItemId',
        class: '',
        required: true
    }
]);

const btnEvaluationMngSave = async saveDataList => {
    await saveDataList.forEach(val => {
        val.evaluationTarget = val.evaluationNm;
        val.detailList = val.detailDataList;
    });
    saveEvaluationChecklistByPopup(saveDataList, true).then(res => {
        managePopup.value.searchMaster(false);
    });
};

const btnEvaluationMngDelete = async deleteDataList => {
    deleteEvaluationChecklist(deleteDataList, true).then(res => {
        managePopup.value.searchMaster(false);
    });
};

const btnEvaluationMngSample = () => {
    samplePopup.value.onOpen();
};

const closeEvaluationMng = () => {
    if (evaluationPopup.value) {
        evaluationPopup.value.onClose();
    }
};

//예시 불러오기 팝업 ############################################################################################################################################

const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(managePopup.value.masterDataList, data, 'evaluationId', 'evaluationNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
        el.detailDataList = el.detailList.map(role => ({
            ...role,
            checked: true,
            cmd: 'I'
        }));
    });
    managePopup.value.setMasterSampleData(dataList);

    samplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    samplePopup.value.onClose();
};

//성과 평가 항목 선택 팝업 ############################################################################################################################################
//팝업 데이터 세팅
const getEvaluationChecklistFunc = async () => {
    let res = await getEvaluationChecklist({}, true);
    res.list = res.list.filter(item => item.useYn === 'Y');
    return res;
};

//팝업 오픈
const openAddEvaluationListPopup = () => {
    addEvaluationListPopup.value.onOpen();
};

//데이터 적용
const applyAddEvaluationListMngPopup = async dataList => {
    dataList.forEach(main => {
        if (main.detailList) {
            main.detailList = main.detailList.filter(i => i.useYn === 'Y');
        }
    });
    await evaluationReportStore.dataMapping(dataList);
    addEvaluationListPopup.value.onClose();
};

//팝업 닫기
const closeAddEvaluationListMngPopup = () => {
    addEvaluationListPopup.value.onClose();
};
//====================================================================================

//-----------------------------------------------
//시간 체크
const onDateInput = (field, event) => {
    evaluationReportStore.inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};

//-----------------------------------------------

// ISignature 컴포넌트 참조를 위한 ref
const signatureComponent = ref();

//-----------------------------------------------

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출
    if (param) {
        evaluationReportStore.searchParam = {
            compId: getCompId(),
            writeYear: param.writeYear,
            docType: param.docType,
            docNo: param.docNo
        };

        await evaluationReportStore.getEvaluationReportDetailList(true);
        layoutStore.useBtnList = uButtonList;
    } else if (!evaluationReportStore.inputForm || !evaluationReportStore.inputForm.cmd) {
        router.push('EvaluationReport');
        return;
    } else {
        layoutStore.useBtnList = iButtonList;
        evaluationReportStore.inputForm.writeYear = monitoringMngStore.searchParam.searchText;
    }

    evaluationReportStore.signature = signatureComponent.value;

    evaluationReportStore.inputForm.detailTitle = [...new Set(evaluationReportStore.inputForm.detailList.map(item => item.evaluationNm))];
    activeAnimation();
});

//-----------------------------------------------

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(evaluationReportStore.originData);
    const currentData = toRaw(evaluationReportStore.inputForm);
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
//-----------------------------------------------

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
        // 관련 화면 클릭
        const title = evaluationReportStore.inputForm.detailTitle[mainIdx];
        const detailList = evaluationReportStore.inputForm.detailList.filter(item => item.evaluationNm == title);
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
            type: filteredData.type,
            accordionNm: evaluationReportStore.inputForm.detailList[mainIdx].evaluationNm
        };
        confirmMsg('info', '[' + filteredData.menuList[0].menuNm + ']\n화면으로 이동하시겠습니까?', '', { fun: btnInfoAction, param: param });
    } else {
        alertMsg('error', '정보가 없습니다.');
        return;
    }
};

const btnInfoAction = param => {
    evaluationReportStore.btnInfo(param);
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

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

btnBack(() => {
    if (!_.isEqual(evaluationReportStore.currentInputForm, evaluationReportStore.inputForm)) {
        handleDataChange(t('msgSaveContinue'), evaluationReportStore.goBack);
    } else {
        evaluationReportStore.goBack();
    }
});

/* 조회 */
btnSearch(async () => {
    if (!_.isEqual(evaluationReportStore.currentInputForm, evaluationReportStore.inputForm)) {
        await handleDataChange(t('msgSaveContinue'), btnSearchAction, true);
    } else {
        btnSearchAction();
    }
});

const btnSearchAction = () => {
    evaluationReportStore.getEvaluationReportDetailList(true);
    signatureComponent.value.Search();
};

/* 추가 */
btnAdd(async () => {
    if (!_.isEqual(evaluationReportStore.currentInputForm, evaluationReportStore.inputForm)) {
        handleDataChange(t('msgSaveContinue'), evaluationReportStore.btnAdd);
    } else {
        evaluationReportStore.btnAdd();
    }
});

/* 저장 */
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await evaluationReportStore.btnSave(true);
});

/* 삭제 */
btnDelete(() => {
    evaluationReportStore.btnDetailDelete(true);
});

/* 출력 */
btnDownload(() => {
    const checkedList = evaluationReportStore.inputForm.detailList.filter(item => item.checked);
    if (checkedList.length == 0) {
        confirmMsg('info', t('msgPrint'), '', { fun: btnDownloadAction });
    } else {
        confirmMsg('info', '선택한 항목을 출력하시겠습니까?', '', { fun: btnDownloadAction });
    }
});

const btnDownloadAction = () => {
    evaluationReportStore.btnDetailDownload(true);
};

const activeAnimation = () => {
    if (evaluationReportStore.inputForm.detailList.length > 0) {
        const accordion = document.getElementById(evaluationReportStore.inputForm.detailList[0].evaluationNm);
        const container = accordion.parentNode;
        const segment = container.querySelector('.segment.oh');
        const isOpen = accordion.classList.toggle('active');
        const alreadyOpen = accordion.classList.toggle('expanded');
        if (alreadyOpen === true) {
            animateAccordion(segment, isOpen);
        }
    }
};
</script>
