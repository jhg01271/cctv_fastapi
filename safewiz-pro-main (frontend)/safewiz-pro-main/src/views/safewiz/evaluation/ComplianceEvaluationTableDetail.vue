<template>
    <div id="form" class="contents form ui df fdc wbka">
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
                    <ISignature ref="signatureComponent" :cmd="complianceEvaluationStore.signCmd" :writeYear="complianceEvaluationStore.searchParam.writeYear" :docNo="complianceEvaluationStore.searchParam.docNo" targetType="LCE"></ISignature>
                </div>
                <hr />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for>작성년도</label>
                                <input v-model="complianceEvaluationStore.searchParam.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="legalEvaluationDt" required>
                                    <span>평가일자</span>
                                </label>
                                <input id="legalEvaluationDt" type="text" :min="complianceEvaluationStore.searchParam.writeYear + '.01.01'" :max="complianceEvaluationStore.searchParam.writeYear + '.12.31'" class="datepicker radius" v-calendar="getDateFormat()" v-model="complianceEvaluationStore.inputForm.legalEvaluationDt" required @change="changeEvaluation" />
                            </div>
                        </div>

                        <div class="grid12-5 es-grid12-9">
                            <div class="field">
                                <label for="legalEvaluationNm" required>
                                    <span>제목</span>
                                </label>
                                <input id="legalEvaluationNm" type="text" class="radius" v-input v-model="complianceEvaluationStore.inputForm.legalEvaluationNm" required @change="changeEvaluation" />
                            </div>
                        </div>

                        <div class="grid12-1 es-grid12-3">
                            <div class="field">
                                <label for>사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :true-value="'Y'" :false-value="'N'" :checked="complianceEvaluationStore.inputForm.useYn === 'Y'" v-model="complianceEvaluationStore.inputForm.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="changeEvaluation" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="mt2rem df fww bd1pxsolidE1E6ED br4px" v-for="(item, index) in complianceEvaluationStore.legalComplianceEvaluationDetailList" :id="`list_${index}`" :key="index">
                        <div class="w5rem df aic jcc bdr1pxsolidE1E6ED tac es-w100p es-h5rem es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                            <input type="checkbox" v-input v-model="item.checked" />
                        </div>
                        <div class="w100pcalc5rem pa2-2rem es-w100p">
                            <div class="row flex gutters1rem">
                                <div class="grid12-8 es-grid12-12">
                                    <div class="field">
                                        <label required>
                                            <span>법규 조항</span>
                                        </label>
                                        <i-chips :chips="[{ id: item.reviewDocNo, name: `${item.legalNm} - ${item.legalArticleNm}` }]" @removeChip="removeEvaluationType(index)" @popup="addEmergencyType(index)" required></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for style="display: flex; justify-content: space-between; align-items: center">
                                            법규 검토서
                                            <span>
                                                <a @click="pageMove(item)" class="fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                                    법규 검토서 화면으로 이동
                                                    <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                                </a>
                                            </span>
                                        </label>
                                        <input v-input type="text" class="w100p radius" v-model="item.legalReviewNm" disabled />
                                    </div>
                                </div>

                                <div class="grid12-8 es-grid12-12">
                                    <div class="field">
                                        <label class="fs1-5rem pb1rem">조직</label>
                                        <i-chips :chips="item.legalComplianceDetailOrgnList" @popup="addOrgn(index)" @removeChip="removeOrgn(index)" class="w100p" />
                                    </div>
                                </div>

                                <div class="grid12-2 es-grid12-8">
                                    <div class="field">
                                        <label for>평가</label>
                                        <div class="h4-4rem df aic gap2rem">
                                            <input type="checkbox" :checked="item.legalReviewYn === 'Y'" @change="onCheckboxChange(item, $event.target.checked, 'Y')" @input="chkData(index)" class="radius" v-input="'적합'" />
                                            <input type="checkbox" :checked="item.legalReviewYn === 'N'" @change="onCheckboxChange(item, $event.target.checked, 'N')" @input="chkData(index)" class="radius" v-input="'위반'" />
                                        </div>
                                    </div>
                                </div>

                                <div class="grid12-2 es-grid12-4">
                                    <div class="field">
                                        <label for="useYn">사용여부</label>
                                        <div class="df aic h4-4rem">
                                            <input :true-value="'Y'" :false-value="'N'" :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="item.useYn" @change="chkData(index)" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="field">
                                <label :for="`contents${index}`" required>
                                    <span>평가내용</span>
                                </label>
                                <textarea :id="`contents${index}`" class="radius minh10rem" placeholder="점검내용을 입력하세요." v-model="item.contents" @input="chkData(index)" required></textarea>
                            </div>

                            <div class="field">
                                <label for="files">확인사항(문서 및 현장)</label>
                                <div class="bd1pxsolidE1E6ED br4px df aic jcc fg1 pa2rem">
                                    <iFileList id="files" :ref="el => (fileListRefs[index] = el)" targetType="LCE" :targetId="item.files && item.files.length > 0 ? item.files : []" @change="chkData(index)" />
                                </div>
                            </div>

                            <div class="field">
                                <label for="remark">비고</label>
                                <textarea id="remark" class="radius minh10rem" placeholder="비고를 입력하세요." v-model="item.remark" @input="chkData(index)"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="df aic jcc py2rem" @click="complianceEvaluationStore.btnAdd()">
                        <!-- 추가 버튼 입니다. -->
                        <i class="pa1rem db cp">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                            </svg>
                        </i>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="evaluationPopup">
                <div class="contents w800px md-w100p">
                    <LegalComplianceEvaluationPopup @close="closeEvaluationType" @selected="onSelectLawData" />
                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeEvaluationType">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" :selectedIdList="complianceEvaluationStore.legalComplianceEvaluationDetailList[selectIndex].legalComplianceDetailOrgnList?.map(el => el.id)" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-param="complianceEvaluationStore.orgnSearchParam[selectIndex]" :fetch-data="getLegalReviewDetailOrgnList" @apply="applyOrgn" @close="closeOrgn" />
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

import BaseView from '@/components/base/BaseView';
const { confirmMsg, onMounted, validationStore, btnSearch, btnBack, btnSave, btnAdd, btnDelete, btnDownload, setRouterParam, getCompId, t, alertMsg, goRouter } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import LegalComplianceEvaluationPopup from '@/views/safewiz/evaluation/popup/LegalComplianceEvaluationPopup.vue';
import { getLegalReviewDetailOrgnList } from '@/stores/safewiz/planning/api/LegalReviewApi.js';

// 버튼 세팅
import { useButtonListStore } from '@/stores/buttonList';
import IFileList from '@/components/file/iFileList.vue';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnAdd', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnAdd', 'btnSave'];

// [스토어]
import { useComplianceEvaluationStore } from '@/stores/safewiz/evaluation/complianceEvaluation.js';
const complianceEvaluationStore = useComplianceEvaluationStore();

// ==============파일==============
const fileListRefs = ref([]);

const evaluationPopup = ref(null);
// ISignature 컴포넌트 참조를 위한 ref
const signatureComponent = ref();
//-----------------------------------------------

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    complianceEvaluationStore.legalComplianceEvaluationDetailList = []; // 리스트 초기화
    complianceEvaluationStore.isChangeEvaluation = false;
    if (param) {
        complianceEvaluationStore.searchParam = {
            compId: getCompId(),
            writeYear: param.writeYear,
            docType: 'LCE',
            docNo: param.docNo
        };

        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        complianceEvaluationStore.getLegalEvaluationDetailList(false);
        layoutStore.useBtnList = uButtonList;
    } else if (!complianceEvaluationStore.inputForm || !complianceEvaluationStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push({ name: 'ComplianceEvaluationTable' });
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }

    complianceEvaluationStore.signature = signatureComponent.value;
    complianceEvaluationStore.file(fileListRefs);
});

const changeEvaluation = () => {
    complianceEvaluationStore.isChangeEvaluation = true;
};

//-----------------------------------------------
// ==============법규 조항 팝업==============
const addEmergencyType = id => {
    selectIndex.value = id;
    evaluationPopup.value.onOpen();
};

// 법규 조항 제거(x버튼 클릭)
const removeEvaluationType = index => {
    const targetItem = complianceEvaluationStore.legalComplianceEvaluationDetailList[index];

    targetItem.legalArticleNm = null;
    targetItem.reviewWriteYear = null;
    targetItem.reviewDocNo = null;
    targetItem.reviewDocSeq = null;
    targetItem.legalReviewNm = null;
    targetItem.currentlaws = null;

    complianceEvaluationStore.legalComplianceEvaluationDetailList[index].legalComplianceDetailOrgnList = []; // 조직 초기화
    chkData(index);
};

const closeEvaluationType = () => {
    // 초기화
    evaluationPopup.value.onClose();
};

// 법규 조항 선택
const onSelectLawData = async el => {
    clearValidationStore();
    const index = selectIndex.value;

    // 선택된 평가 항목에 법규 정보 세팅
    const targetItem = complianceEvaluationStore.legalComplianceEvaluationDetailList[index];

    targetItem.reviewWriteYear = el.writeYear;
    targetItem.reviewDocNo = el.docNo;
    targetItem.reviewDocSeq = el.docSeq;
    targetItem.legalNm = el.legalNm;
    targetItem.legalArticleNm = el.legalArticleNm;
    targetItem.currentlaws = el.currentlaws;
    targetItem.legalReviewNm = el.legalReviewNm;

    // orgnSearchParam에 해당 항목 세팅 (조직 팝업용)
    complianceEvaluationStore.orgnSearchParam[index] = {
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
        docSeq: el.docSeq
    };

    // 선택한 법규 검토서에 해당하는 조직 데이터를 모두 호출
    const targetOrgn = await getLegalReviewDetailOrgnList(complianceEvaluationStore.orgnSearchParam[index]);

    if (targetOrgn.list.length > 0) {
        targetOrgn.list.forEach(orgn => {
            (orgn.id = orgn.orgnId), (orgn.nm = orgn.orgnNm);
        });
    }

    // 조직 초기화
    targetItem.legalComplianceDetailOrgnList = targetOrgn.list;

    // 체크 상태 유지
    targetItem.checked = true;
    evaluationPopup.value.onClose();
};

const pageMove = item => {
    const param = {
        writeYear: item.reviewWriteYear,
        docType: 'LGR',
        docNo: item.reviewDocNo,
        docSeq: item.reviewDocSeq
    };
    goRouter('LegalReviewDetail', param);
};
//-----------------------------------------------
// ==============조직 팝업==============
const orgnPopup = ref(null);
const selectIndex = ref(0);

// 조직 팝업 오픈
const addOrgn = index => {
    selectIndex.value = index;

    if (complianceEvaluationStore.legalComplianceEvaluationDetailList[selectIndex.value].reviewDocNo === null) {
        alertMsg('warning', `법규 조항을 선택해주세요.`);
        return;
    }
    orgnPopup.value.onOpen();
};

const applyOrgn = e => {
    if (e.length > 0) {
        complianceEvaluationStore.legalComplianceEvaluationDetailList[selectIndex.value].legalComplianceDetailOrgnList = e.map(el => ({ id: el.orgnId, nm: el.orgnNm }));
        chkData(selectIndex.value);
    }
    orgnPopup.value.onClose();
};

const closeOrgn = e => {
    orgnPopup.value.onClose();
};

const removeOrgn = index => {
    chkData(index);
};

//-----------------------------------------------

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(complianceEvaluationStore.originData);
    const currentData = toRaw(complianceEvaluationStore.inputForm);
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

btnBack(() => {
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', complianceEvaluationStore.goBack);
});

/* 조회 */
btnSearch(async () => {
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    clearValidationStore();
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', complianceEvaluationStore.getLegalEvaluationDetailList, true);
});

/* 저장 */
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await complianceEvaluationStore.btnSave(true);
});

/* 삭제 */
btnDelete(() => {
    complianceEvaluationStore.btnDetailDelete(true);
});

/* 출력 */
btnDownload(() => {
    const checkedList = [complianceEvaluationStore.inputForm.docNo];
    let checkedObjList = [];

    // 체크된 리스트 추출
    complianceEvaluationStore.legalComplianceEvaluationDetailList.forEach(el => {
        if (el.checked) {
            checkedObjList.push({
                writeYear: el.writeYear,
                docNo: el.docNo,
                docType: el.docType,
                docSeq: el.docSeq
            });
        }
    });

    const searchVO = {
        writeYear: complianceEvaluationStore.searchParam.writeYear,
        docType: complianceEvaluationStore.searchParam.docType,
        checkedObjList: checkedObjList.length > 0 ? checkedObjList : complianceEvaluationStore.legalComplianceEvaluationDetailList,
        checkedList: checkedList
    };

    complianceEvaluationStore.btnDetailDownload(searchVO);
});

/* 추가 */
btnAdd(() => {
    complianceEvaluationStore.btnAdd();
});

//-----------------------------------------------
//-----------------------------------------------
// 체크박스 변경 이벤트 핸들러
const onCheckboxChange = (item, isChecked, value) => {
    if (isChecked) {
        item.legalReviewYn = value;
    } else {
        item.legalReviewYn = null;
    }
};
//-----------------------------------------------
//-----------------------------------------------

// 자동체크함수
const chkData = index => {
    complianceEvaluationStore.legalComplianceEvaluationDetailList[index].checked = true;
};

//-----------------------------------------------
//-----------------------------------------------

// validation 초기화
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>
