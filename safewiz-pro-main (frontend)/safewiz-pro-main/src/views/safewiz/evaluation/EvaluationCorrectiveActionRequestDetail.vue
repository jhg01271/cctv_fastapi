<template>
    <div class="contents">
        <div v-if="evaluationCorrectiveActionRequestStore && evaluationCorrectiveActionRequestStore.inputForm" id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <!-- 문서발행번호 -->
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for="">작성년도</label>
                                <input :value="evaluationCorrectiveActionRequestStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label>{{ t('corrective_docNo') }}</label>
                                <input :value="evaluationCorrectiveActionRequestStore.inputForm.cmd == 'U' ? evaluationCorrectiveActionRequestStore.inputForm.writeYear + '-' + evaluationCorrectiveActionRequestStore.inputForm.docType + '-' + evaluationCorrectiveActionRequestStore.inputForm.docNo : ''" readonly v-input type="text" class="w100p radius" id="docNo" />
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="field">
                                <!-- 제목 -->
                                <label for="title" required>
                                    <span>{{ t('corrective_title') }}</span>
                                </label>
                                <input v-input v-model="evaluationCorrectiveActionRequestStore.inputForm.title" type="text" class="w100p radius" id="title" required placeholder="제목을 입력해주세요." />
                            </div>
                        </div>
                        <!-- 사용여부 -->
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label>{{ t('corrective_useAt') }}</label>
                                <div class="h4-4rem df aic">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="evaluationCorrectiveActionRequestStore.inputForm.useYn == 'Y'" @change="evaluationCorrectiveActionRequestStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-12">
                            <!-- 주관조직 -->
                            <div class="field">
                                <label required>
                                    <span>{{ t('corrective_hostOrg') }}</span>
                                </label>
                                <i-chips :chips="evaluationCorrectiveActionRequestStore.ctrlOrgnItem" @popup="addCtrlOrgn" @removeChip="removeCtrlOrgn" required></i-chips>
                            </div>
                        </div>

                        <!-- 조치조직 -->
                        <div class="grid12-3 es-grid12-12">
                            <div class="field">
                                <label>{{ t('corrective_actionOrg') }}</label>
                                <i-chips :chips="evaluationCorrectiveActionRequestStore.actionOrgnItem" @popup="addActionOrgn" @removeChip="removeActionOrgn"></i-chips>
                            </div>
                        </div>

                        <!-- 작성일 -->
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>{{ t('corrective_regDt') }}</label>
                                <input v-input v-model="evaluationCorrectiveActionRequestStore.inputForm.writeDt" :min="evaluationCorrectiveActionRequestStore.searchParam.writeYear + '.01.01'" :max="evaluationCorrectiveActionRequestStore.searchParam.writeYear + '.12.31'" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                            </div>
                        </div>

                        <!-- 회신요구일 -->
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>{{ t('corrective_returnDt') }}</label>
                                <input v-input v-model="evaluationCorrectiveActionRequestStore.inputForm.returnDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                            </div>
                        </div>

                        <!-- 부적합 사항(시정조치 요구사항) -->
                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 sm-grid12-12">
                                        <div class="field">
                                            <label>{{ t('corrective_check') }}</label>
                                            <textarea v-model="evaluationCorrectiveActionRequestStore.inputForm.actionRequestContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                        </div>
                                    </div>

                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="mt4-4rem sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature type="nonconformity" ref="signatureComponent1" :cmd="evaluationCorrectiveActionRequestStore.inputForm.cmd" :is-writer="false" :types="signatureType1" :writeYear="evaluationCorrectiveActionRequestStore.inputForm.writeYear" :docNo="evaluationCorrectiveActionRequestStore.inputForm.docNo" targetType="CAR" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 원인 분석 및 재발 방지 대책-->
                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 sm-grid12-12">
                                        <div class="field">
                                            <label>{{ t('corrective_analyticAndPrevent') }}</label>
                                            <textarea v-model="evaluationCorrectiveActionRequestStore.inputForm.analyticPreventContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="mt4-4rem sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature type="rootCauseAnalysis" ref="signatureComponent2" :cmd="evaluationCorrectiveActionRequestStore.inputForm.cmd" :is-writer="false" :types="signatureType2" :writeYear="evaluationCorrectiveActionRequestStore.inputForm.writeYear" :docNo="evaluationCorrectiveActionRequestStore.inputForm.docNo" targetType="CAR" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 lg-grid12-12">
                                        <div class="df aic jcsb fww">
                                            <div class="field es-w100p">
                                                <label>{{ t('corrective_validate_check') }}</label>
                                                <div class="h4-4rem df aic fww gap1rem">
                                                    <input type="radio" v-input="'미조치'" v-model="evaluationCorrectiveActionRequestStore.inputForm.resultValidationYn" value="N" />
                                                    <input type="radio" v-input="'조치'" v-model="evaluationCorrectiveActionRequestStore.inputForm.resultValidationYn" value="S" />
                                                    <input type="radio" v-input="'부적합'" v-model="evaluationCorrectiveActionRequestStore.inputForm.resultValidationYn" value="F" />
                                                </div>
                                            </div>
                                            <div class="field es-w100p">
                                                <label>{{ t('corrective_checkDt') }}</label>
                                                <input v-input v-model="evaluationCorrectiveActionRequestStore.inputForm.checkDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="grid12-8 sm-grid12-12">
                                        <!-- 시정조치 결과 유효성 확인-->
                                        <textarea v-model="evaluationCorrectiveActionRequestStore.inputForm.resultValidationContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                    </div>
                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature type="correctiveAction" ref="signatureComponent3" :cmd="evaluationCorrectiveActionRequestStore.inputForm.cmd" :is-writer="false" :types="signatureType3" :writeYear="evaluationCorrectiveActionRequestStore.inputForm.writeYear" :docNo="evaluationCorrectiveActionRequestStore.inputForm.docNo" targetType="CAR" class="w100" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>

            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <teleport to="body">
                <i-PopupDialog ref="ctrlPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w500px md-w100p">
                        <base-select-popup :title="'조직'" :inputForm="evaluationCorrectiveActionRequestStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="evaluationCorrectiveActionRequestStore.closeOrgnCtrl" />
                    </div>
                </i-PopupDialog>
                <i-PopupDialog ref="actionPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w500px md-w100p">
                        <base-select-popup :title="'조직'" :inputForm="evaluationCorrectiveActionRequestStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="evaluationCorrectiveActionRequestStore.closeOrgnAction" />
                    </div>
                </i-PopupDialog>
            </teleport>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { ref } from 'vue';
const { validationStore, onMounted, t, btnSearch, btnBack, btnSave, btnDelete, btnDownload, toastPopup, setRouterParam } = BaseView();
import router from '@/router';
import { getDateFormat } from '@/utils/dateUtil.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { useEvaluationCorrectiveActionRequestStore } from '@/stores/safewiz/evaluation/evaluationCorrectiveActionRequest.js';

const evaluationCorrectiveActionRequestStore = useEvaluationCorrectiveActionRequestStore();
//서명 컴포넌트
import ISignature from '@/components/common/iSignature.vue';

const contentTextarea = ref(null);

import { useRoute } from 'vue-router';
const route = useRoute();

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    console.log('########### param', param);
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await evaluationCorrectiveActionRequestStore.btnDetail(param.writeYear, param.docNo);
    } else if (!evaluationCorrectiveActionRequestStore.inputForm || !evaluationCorrectiveActionRequestStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('EvaluationCorrectiveActionRequest');
        return;
    }

    //등록,수정일 때 우측 레이아웃
    if (evaluationCorrectiveActionRequestStore.inputForm.cmd === 'I') {
        //등록
        evaluationCorrectiveActionRequestStore.layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        //수정
        evaluationCorrectiveActionRequestStore.layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
    }

    evaluationCorrectiveActionRequestStore.setRefsCtrl(ctrlPopup);
    evaluationCorrectiveActionRequestStore.setRefsAction(actionPopup);
    evaluationCorrectiveActionRequestStore.signature1 = signatureComponent1.value;
    evaluationCorrectiveActionRequestStore.signature2 = signatureComponent2.value;
    evaluationCorrectiveActionRequestStore.signature3 = signatureComponent3.value;
});

//목록 버튼
btnBack(async () => {
    await evaluationCorrectiveActionRequestStore.changedDataCheck('Back', '', '');
    //router.push({ name: 'CorrectiveActionRequest' });
});

// 조회 버튼
btnSearch(async () => {
    await signatureComponent1.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await signatureComponent2.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await signatureComponent3.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await evaluationCorrectiveActionRequestStore.changedDataCheck('Search', evaluationCorrectiveActionRequestStore.inputForm.writeYear, evaluationCorrectiveActionRequestStore.inputForm.docNo);
    //evaluationCorrectiveActionRequestStore.btnDetail(evaluationCorrectiveActionRequestStore.inputForm.writeYear, evaluationCorrectiveActionRequestStore.inputForm.docNo);
});

//출력 버튼
btnDownload(async () => {
    await evaluationCorrectiveActionRequestStore.changedDataCheck('Down', evaluationCorrectiveActionRequestStore.inputForm.writeYear, evaluationCorrectiveActionRequestStore.inputForm.docNo);
});

const signatureComponent1 = ref();
const signatureComponent2 = ref();
const signatureComponent3 = ref();

const signatureType1 = ref([
    {
        // code: 'reviewer',
        name: '담당',
        sysCodeOrdSeq: 1
    },
    {
        // code: 'approver',
        name: '조직장',
        sysCodeOrdSeq: 2
    }
]);
const signatureType2 = ref([
    {
        // code: 'reviewer',
        name: '담당',
        sysCodeOrdSeq: 3
    },
    {
        // code: 'approver',
        name: '조직장',
        sysCodeOrdSeq: 4
    }
]);
const signatureType3 = ref([
    {
        // code: 'reviewer',
        name: '담당',
        sysCodeOrdSeq: 5
    },
    {
        // code: 'approver',
        name: '조직장',
        sysCodeOrdSeq: 6
    }
]);

//저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    console.log('@@ isValid', isValid);
    if (isValid) {
        //주관 조직의 validation
        if (evaluationCorrectiveActionRequestStore.ctrlOrgnItem.length == 0) {
            toastPopup('주관조직', '주관조직은 필수값입니다.');
        } else {
            //주관 조직
            if (evaluationCorrectiveActionRequestStore.ctrlOrgnItem.length !== 0) {
                evaluationCorrectiveActionRequestStore.inputForm.ctrlOrgnId = evaluationCorrectiveActionRequestStore.ctrlOrgnItem[0].id;
                evaluationCorrectiveActionRequestStore.inputForm.ctrlOrgnNm = evaluationCorrectiveActionRequestStore.ctrlOrgnItem[0].name;
            }

            //조치 조직
            if (evaluationCorrectiveActionRequestStore.actionOrgnItem.length !== 0) {
                evaluationCorrectiveActionRequestStore.inputForm.actionOrgnId = evaluationCorrectiveActionRequestStore.actionOrgnItem[0].id;
                evaluationCorrectiveActionRequestStore.inputForm.actionOrgnNm = evaluationCorrectiveActionRequestStore.actionOrgnItem[0].name;
            } else {
                evaluationCorrectiveActionRequestStore.inputForm.actionOrgnId = '';
                evaluationCorrectiveActionRequestStore.inputForm.actionOrgnNm = '';
            }

            // if(evaluationCorrectiveActionRequestStore.inputForm.writeDt === ""){
            //   evaluationCorrectiveActionRequestStore.inputForm.value.writeDt = getCurrentDate();
            // }

            await evaluationCorrectiveActionRequestStore.btnSave();
        }
    }
});

btnDelete(async () => {
    await evaluationCorrectiveActionRequestStore.changedDataCheck('Delete', evaluationCorrectiveActionRequestStore.inputForm.writeYear, evaluationCorrectiveActionRequestStore.inputForm.docNo);
});

//주관조직
const ctrlPopup = ref(null);
const addCtrlOrgn = () => {
    ctrlPopup.value.onOpen();
};

//조치조직
const actionPopup = ref(null);
const addActionOrgn = () => {
    actionPopup.value.onOpen();
};

const removeCtrlOrgn = () => {
    signatureComponent3.value.setHrInfo(1, null, null);
};

const removeActionOrgn = () => {
    signatureComponent2.value.setHrInfo(1, null, null);
};
</script>
