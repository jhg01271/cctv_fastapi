<template>
    <div class="contents form ui df fdc" id="form">
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
            <div class="pa2-2rem bcFFFFFF bd1pxsolidE1E6ED br4px">
                <div class="row flex gutters1rem">
                    <div class="grid12-2 lg-grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="">작성년도</label>
                            <input :value="correctiveRequestStore.inputForm.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                        </div>
                    </div>
                    <div class="grid12-4 lg-grid12-6 us-grid12-12">
                        <div class="field">
                            <label required>
                                <span>안전보건 내부심사 결과보고서</span>
                            </label>
                            <i-chips required :chips="[{ nm: correctiveRequestStore.inputForm.auditNm, id: correctiveRequestStore.inputForm.docNo }]" @popup="openPopup" :readonly="correctiveRequestStore.cmd == 'U'"></i-chips>
                        </div>
                    </div>

                    <div class="grid12-2 lg-grid12-6 us-grid12-12">
                        <div class="field">
                            <label for>심사 번호</label>
                            <input type="text" class="radius" v-input disabled :value="`${correctiveRequestStore.inputForm.writeYear}${correctiveRequestStore.inputForm.docNo}${correctiveRequestStore.inputForm.docSeq}`" />
                        </div>
                    </div>

                    <div class="grid12-2 lg-grid12-6">
                        <div class="field">
                            <label>
                                <span>조직</span>
                            </label>
                            <i-chips readonly :chips="[{ nm: correctiveRequestStore.inputForm.orgnNm, id: correctiveRequestStore.inputForm.orgnId }]"></i-chips>
                        </div>
                    </div>

                    <div class="grid12-2 lg-grid12-6">
                        <div class="field">
                            <label for>사용여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="'사용'" type="checkbox" class="df switch" true-value="Y" false-value="N" :checked="correctiveRequestStore.inputForm.useYn == 'Y'" @change="toggleUseYn" />
                            </div>
                        </div>
                    </div>

                    <div class="grid12-5 lg-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for>심사 팀원</label>
                            <i-chips :chips="correctiveRequestStore.hrList" readonly></i-chips>
                        </div>
                    </div>

                    <div class="grid12-3 lg-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for>문서발행번호</label>
                            <input type="text" class="radius" v-input disabled :value="correctiveRequestStore.cmd == 'U' ? `${correctiveRequestStore.inputForm.writeYear}${correctiveRequestStore.inputForm.docNo}${correctiveRequestStore.inputForm.docSeq}${correctiveRequestStore.inputForm.docDetailSeq}` : ''" />
                        </div>
                    </div>

                    <div class="grid12-2 lg-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for>심사 일시</label>
                            <input type="text" class="radius datepicker" :min="correctiveRequestStore.inputForm.writeYear + '.01.01'" :max="correctiveRequestStore.inputForm.writeYear + '.12.31'" v-calendar="getDateFormat()" v-model="correctiveRequestStore.inputForm.writeDt" />
                        </div>
                    </div>

                    <div class="grid12-2 lg-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for>회신요구일</label>
                            <input type="text" class="radius datepicker" v-calendar="getDateFormat()" v-model="correctiveRequestStore.inputForm.reqDt" />
                        </div>
                    </div>
                </div>
                <div class="mt2rem bd1pxsolidE1E6ED br4px pa2-2rem">
                    <div class="field">
                        <h3 class="wbka">
                            부적합 사항<br class="dn es-db" />
                            ( 규정&middot;요건 대비 부적합사항을 중심으로 관련 증거 구체적으로 작성 )
                        </h3>
                        <textarea class="radius minh10rem" v-model="correctiveRequestStore.inputForm.nonconformities"></textarea>
                    </div>
                </div>

                <div class="mt2rem bd1pxsolidE1E6ED br4px pa2-2rem">
                    <div class="row flex gutters2rem">
                        <div class="grid12-10 lg-grid12-12">
                            <div class="field h100p df fdc">
                                <div class="field h100p df fdc">
                                    <h3 class="wbka">
                                        원인 분석 및 재발 방지대책<br class="dn es-db" />
                                        ( 구체적으로 작성하시고 조치사항 자료 첨부 )
                                    </h3>
                                    <textarea class="radius minh10rem fg1" v-model="correctiveRequestStore.inputForm.measures"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for>조직장 확인</label>
                                <ISignature ref="signatureComponent1" :types="signatureType1" :is-writer="false" targetType="OCAR" :cmd="correctiveRequestStore.inputForm.cmd" :writeYear="correctiveRequestStore.inputForm.writeYear" :docNo="correctiveRequestStore.inputForm.docNo" :docSeq="correctiveRequestStore.inputForm.docSeq" :docDetailSeq="correctiveRequestStore.inputForm.docDetailSeq" type="orgnHead"></ISignature>
                            </div>
                        </div>
                    </div>
                    <div class="field mt2rem">
                        <IFileList ref="fileList" targetType="OCAR" :targetId="correctiveRequestStore.inputForm.files" />
                    </div>
                </div>

                <div class="mt2rem bd1pxsolidE1E6ED br4px pa2-2rem">
                    <div class="row flex gutters2rem">
                        <div class="grid12-10 lg-grid12-12">
                            <div class="field h100p df fdc">
                                <h3 for>시정 조치 결과 유효성 확인</h3>
                                <div class="row flex fg1">
                                    <div class="grid12-4 lg-grid12-6 us-grid12-12">
                                        <div class="field">
                                            <label for>유효성 확인 결과</label>
                                            <div class="h4-4rem df aic">
                                                <input class="mr2rem" type="checkbox" v-input="'적합'" v-model="correctiveRequestStore.inputForm.validate" true-value="Y" false-value="D" />
                                                <input type="checkbox" v-input="'부적합'" v-model="correctiveRequestStore.inputForm.validate" true-value="N" false-value="D" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid12-4 lg-grid12-6 us-grid12-12">
                                        <div class="field">
                                            <label for>유효성 점검 필요 여부</label>
                                            <div class="h4-4rem df aic">
                                                <input class="mr2rem" type="checkbox" v-input="'필요'" v-model="correctiveRequestStore.inputForm.required" true-value="Y" false-value="D" />
                                                <input type="checkbox" v-input="'불필요'" v-model="correctiveRequestStore.inputForm.required" true-value="N" false-value="D" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid12-4 lg-grid12-12">
                                        <div class="field">
                                            <label for>최종 점검일자</label>
                                            <input type="text" class="radius datepicker" v-calendar="getDateFormat()" v-model="correctiveRequestStore.inputForm.finalCheckDt" :disabled="correctiveRequestStore.inputForm.required != 'Y'" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for>조치사항 확인</label>
                                <ISignature ref="signatureComponent2" :types="signatureType2" :is-writer="false" targetType="OCAR" :cmd="correctiveRequestStore.inputForm.cmd" :writeYear="correctiveRequestStore.inputForm.writeYear" :docNo="correctiveRequestStore.inputForm.docNo" :docSeq="correctiveRequestStore.inputForm.docSeq" :docDetailSeq="correctiveRequestStore.inputForm.docDetailSeq" type="teamLeader" :changeSelectPopup="true" :selectPopupTitle="'심사팀장'" :fetchData="fetchJudgeList"></ISignature>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>

    <teleport to="body">
        <i-PopupDialog ref="reportDialog">
            <div class="contents w70rem md-w100p">
                <OhsCorrectiveActionPopup :single="true" @selected="selectReport" @close="closeReportPopup"></OhsCorrectiveActionPopup>
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import ISignature from '@/components/common/iSignature.vue';
import { useCorrectiveRequestStore } from '@/stores/safewiz/evaluation/OhsCorrectiveActionRequest';
const correctiveRequestStore = useCorrectiveRequestStore();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { onMounted, ref, watchEffect } from 'vue';
import OhsCorrectiveActionPopup from './OhsCorrectiveActionPopup.vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, router, confirmMsg, btnSave, btnBack, btnDownload, btnDelete, btnSearch, formatDateForDB, setRouterParam } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnSearch', 'btnDelete', 'btnBack', 'btnDownload', 'btnSave'];
import { getJudgeList } from '@/stores/safewiz/evaluation/api/auditExecutionPlanApi.js';
import { getDateFormat } from '@/utils/dateUtil.js';

const fetchJudgeList = async () => {
    const res = await getJudgeList();
    const filtered = {
        list: res.list.filter(item => item.hrNm?.includes('내부심사팀장'))
    };

    return filtered;
};

const signatureType1 = ref([
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
const signatureType2 = ref([
    {
        name: '심사팀장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
btnDownload(() => {
    correctiveRequestStore.btnDownload([correctiveRequestStore.inputForm]);
});
btnBack(() => {
    router.push('/OhsCorrectiveActionRequest');
});
watchEffect(() => {
    if (correctiveRequestStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
        correctiveRequestStore.hrList = [];
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});

const signatureComponent1 = ref();
const signatureComponent2 = ref();
import { useRoute } from 'vue-router';
const route = useRoute();

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await correctiveRequestStore.showDetail(param);
        layoutStore.useBtnList = uButtonList;
    } else if (!correctiveRequestStore.inputForm || !correctiveRequestStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('OhsCorrectiveActionRequest');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }

    if (fileList.value && typeof fileList.value.fnSearch === 'function') {
        fileList.value.fnSearch();
    }
    setTimeout(() => {
        signatureComponent1.value.Search();
        signatureComponent2.value.Search();
    }, 20);
});
btnDelete(() => {
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction });
});
btnSearch(async () => {
    await correctiveRequestStore.showDetail(correctiveRequestStore.inputForm);
    fileList.value.fnSearch();
    await signatureComponent1.value.setApprovalInfo('OCAR', correctiveRequestStore.writeYear, correctiveRequestStore.docNo, correctiveRequestStore.docSeq, correctiveRequestStore.docDetailSeq);
    await signatureComponent2.value.setApprovalInfo('OCAR', correctiveRequestStore.writeYear, correctiveRequestStore.docNo, correctiveRequestStore.docSeq, correctiveRequestStore.docDetailSeq + 1);
    signatureComponent1.value.Search();
    signatureComponent2.value.Search();
});
const deleteAction = () => {
    deleteCorrectiveRequest([correctiveRequestStore.inputForm], true).then(res => {
        if (res.success) router.push('/OhsCorrectiveActionRequest');
    });
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction });
    }
});
const fileList = ref();
const saveAction = async () => {
    if (correctiveRequestStore.inputForm.required === 'N') {
        correctiveRequestStore.inputForm.finalCheckDt = null;
    }
    correctiveRequestStore.inputForm.writeDt = correctiveRequestStore.inputForm.writeDt ? formatDateForDB(correctiveRequestStore.inputForm.writeDt) : null;
    correctiveRequestStore.inputForm.finalCheckDt = correctiveRequestStore.inputForm.finalCheckDt ? formatDateForDB(correctiveRequestStore.inputForm.finalCheckDt) : null;
    correctiveRequestStore.inputForm.reqDt = correctiveRequestStore.inputForm.reqDt ? formatDateForDB(correctiveRequestStore.inputForm.reqDt) : null;

    const formData = new FormData();
    correctiveRequestStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
    formData.append('info', new Blob([JSON.stringify(correctiveRequestStore.inputForm)], { type: 'application/json' }));
    // formData.deleteFiles = editFiles.delete;
    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });
    const response = await saveCorrectiveRequest(formData, true);
    if (response.success) {
        await signatureComponent1.value.setApprovalInfo('OCAR', response.result.writeYear, response.result.docNo, response.result.docSeq, response.result.docDetailSeq);
        await signatureComponent2.value.setApprovalInfo('OCAR', response.result.writeYear, response.result.docNo, response.result.docSeq, response.result.docDetailSeq + 1);
        signatureComponent1.value.Search();
        signatureComponent2.value.Search();
        correctiveRequestStore.showDetail(response.result).then(() => {
            fileList.value.fnSearch();
        });
    }
};
const reportDialog = ref();
const openPopup = () => {
    reportDialog.value.onOpen();
};
const closeReportPopup = () => {
    reportDialog.value.onClose();
};
const selectReport = item => {
    correctiveRequestStore.inputForm.auditNm = item.auditNm;
    correctiveRequestStore.inputForm.writeYear = item.writeYear;
    correctiveRequestStore.inputForm.docNo = item.docNo;
    correctiveRequestStore.inputForm.docSeq = item.docSeq;
    correctiveRequestStore.inputForm.orgnNm = item.orgnNm;
    correctiveRequestStore.inputForm.orgnId = item.orgnId;
    let orgnHead = signatureComponent1.value.getSignatureData()[0];
    orgnHead.hrId = item.orgnHeadId;
    orgnHead.hrNm = item.orgnHeadNm;
    correctiveRequestStore.hrList = item.auditHrList.map(el => {
        return {
            id: el.hrId,
            nm: el.hrNm
        };
    });
    closeReportPopup();
};
const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    correctiveRequestStore.inputForm.useYn = event.target.checked ? 'Y' : 'N';
};
// ==============파일==============
import IFileList from '@/components/file/iFileList.vue';
import { deleteCorrectiveRequest, saveCorrectiveRequest } from '@/stores/safewiz/evaluation/api/OhsCorrectiveActionRequestApi';
</script>
<style lang="scss" scoped>
.contents {
    h3 {
        @media (max-width: 1300px) {
            height: auto;
        }
    }
}
</style>
