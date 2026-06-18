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
            <div class="box">
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" targetType="ARR" :cmd="auditResultReportStore.inputForm.cmd" :writeYear="auditResultReportStore.inputForm.writeYear" :docNo="auditResultReportStore.inputForm.docNo" :docSeq="auditResultReportStore.inputForm.docSeq"></ISignature>
                </div>
                <hr />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid10-2 lg-grid12-6">
                            <div class="field">
                                <label for>심사 번호</label>
                                <input type="text" class="radius" v-input :value="`${auditResultReportStore.inputForm.writeYear}${auditResultReportStore.inputForm.docNo}`" disabled />
                            </div>
                        </div>

                        <div class="grid10-2 lg-grid12-6">
                            <div class="field">
                                <label for>대상 조직</label>
                                <input type="text" class="radius" v-input v-model="auditResultReportStore.inputForm.orgnNm" disabled />
                            </div>
                        </div>

                        <div class="grid10-2 lg-grid12-6">
                            <div class="field">
                                <label for="">작성년도</label>
                                <input :value="auditResultReportStore.inputForm.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>
                        <div class="grid10-2 lg-grid12-6">
                            <div class="field">
                                <label for="auditDt" required>
                                    <span>심사일자</span>
                                </label>
                                <input type="text" id="auditDt" required class="datepicker radius" v-calendar="getDateFormat()" placeholder="2024.09.10" v-model="auditResultReportStore.inputForm.auditDt" @input="changeAuditResultDetail()" />
                            </div>
                        </div>

                        <div class="grid10-2 lg-grid12-6">
                            <div class="field">
                                <label for="auditTime" required>
                                    <span>심사시간</span>
                                </label>
                                <input type="text" id="auditTime" required class="datepicker radius" startTime endTime v-calendar placeholder="10:00 ~ 12:00" v-model="auditResultReportStore.inputForm.auditTime" @input="changeAuditResultDetail()" />
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label required>
                                    <span>심사 팀원</span>
                                </label>
                                <i-chips required :chips="auditResultReportStore.hrList" @popup="addPeople()" @removeChip="removePeople()"></i-chips>
                            </div>
                        </div>
                    </div>
                    <div class="mt2rem df bd1pxsolidE1E6ED br4px es-fww" v-for="(item, index) in auditResultReportStore.inputForm.detail" :key="index">
                        <div class="w5rem df aic jcc bdr1pxsolidE1E6ED tac es-h5rem es-w100p es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                            <input type="checkbox" v-input class="radius" v-model="item.checked" />
                        </div>
                        <div class="w100pcalc5rem pa2-2rem es-w100p">
                            <div class="row flex gutters2rem">
                                <div class="grid12-10 sm-grid12-10 es-grid12-12">
                                    <div class="field">
                                        <label :for="`targetJob${index}`" required>
                                            <span>대상 업무</span>
                                        </label>
                                        <input :required="item.checked" :id="`targetJob${index}`" class="radius" type="text" v-input placeholder="대상 업무를 입력하세요." v-model="item.targetJob" @input="chkData(index)" />
                                    </div>
                                </div>

                                <!-- <div class="grid12-3 es-grid12-8">
                                    <div class="field">                                        
                                        <label for>시정조치 여부</label>
                                        <div class="h4-4rem df aic gap2rem">
                                            <div class="df aic h4-4rem">
                                                <input :id="`actionYn${index}`" v-input="''" type="checkbox" class="df switch" v-model="item.actionYn" true-value="Y" false-value="N" :checked="item.actionYn == 'Y'" @click="item.checked = true" />
                                            </div>
                                        </div>
                                    </div>
                                </div> -->

                                <div class="grid12-2 sm-grid12-2 es-grid12-12">
                                    <div class="field">
                                        <label for>사용여부</label>
                                        <div class="df aic h4-4rem">
                                            <input v-input="'사용'" type="checkbox" class="df switch" v-model="item.useYn" true-value="Y" false-value="N" :checked="item.useYn == 'Y'" @click="item.checked = true" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="field">
                                <label for>
                                    <span>심사 결과 및 결론</span>
                                </label>
                                <textarea class="radius minh10rem" placeholder="내용을 입력하세요." v-model="item.result" @input="chkData(index)"></textarea>
                            </div>

                            <div class="field">
                                <label for>
                                    <span>비고</span>
                                </label>
                                <textarea class="radius minh10rem" placeholder="비고를 입력하세요." v-model="item.remark" @input="chkData(index)"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="df aic jcc py1rem">
                        <!-- 추가 버튼 입니다. -->
                        <i class="pa1rem db cp" @click="auditResultReportStore.addDetail()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                            </svg>
                        </i>
                    </div>

                    <div class="field">
                        <label for>심사 특기사항 및 권고사항</label>
                        <textarea class="radius minh10rem" placeholder="내용을 입력하세요." v-model="auditResultReportStore.inputForm.remark"></textarea>
                    </div>

                    <div class="field">
                        <label for>첨부 자료</label>
                        <IFileList ref="fileList" targetType="ARR" :targetId="auditResultReportStore.inputForm.files" />
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'심사 팀원'" :inputForm="auditResultReportStore.inputForm" filterKey="hrNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getJudgeList" :selectedIdList="auditResultReportStore.hrList?.map(el => el.id)" uniqueKey="hrId" @close="closePeoplePopup" @apply="selectPeopleMulty" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import { getJudgeList } from '@/stores/safewiz/evaluation/api/auditExecutionPlanApi.js';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import { ref, watchEffect } from 'vue';
import { useAuditResultReportStore } from '@/stores/safewiz/evaluation/auditResultReport';
const auditResultReportStore = useAuditResultReportStore();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const iButtonList = ['btnDelete', 'btnBack', 'btnSave'];
const uButtonList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnBack', 'btnDownload', 'btnSave'];
watchEffect(() => {
    if (auditResultReportStore.inputForm.cmd == 'I') {
        layoutStore.useBtnList = iButtonList;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});

import BaseView from '@/components/base/BaseView';
const { validationStore, openLoading, endLoading, router, onMounted, btnBack, btnSave, btnDelete, alertMsg, confirmMsg, btnSearch, btnDownload, t, btnAdd, formatDateForDB, setRouterParam, getCompId } = BaseView();
btnBack(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    router.push('/OhsInternalAuditResultsReport');
});
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await auditResultReportStore.showDetail(param);
        layoutStore.useBtnList = uButtonList;
    } else if (!auditResultReportStore.inputForm || !auditResultReportStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('OhsInternalAuditResultsReport');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }

    if (fileList.value && typeof fileList.value.fnSearch === 'function') {
        fileList.value.fnSearch();
    }
    setTimeout(() => {
        signatureComponent.value.Search();
    }, 20)
});
const peoplePopup = ref();
const addPeople = () => {
    peoplePopup.value.onOpen();
};

btnSearch(async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    openLoading();
    auditResultReportStore
        .showDetail(auditResultReportStore.inputForm)
        .then(() => {
        fileList.value.fnSearch();
        signatureComponent.value.setApprovalInfo('ARR', auditResultReportStore.inputForm.writeYear, auditResultReportStore.inputForm.docNo, auditResultReportStore.inputForm.docSeq);
        signatureComponent.value.Search()
    })
    .finally(() => {
        endLoading();
    });
});

btnAdd(() => {
    auditResultReportStore.addDetail(); // 내부 카드 추가
});

const closePeoplePopup = () => {
    peoplePopup.value.onClose();
};

btnSave(async () => {
    const checkedDetails = auditResultReportStore.inputForm.detail.filter(el => el.checked);
    const isValid = await validationStore.validateAllFields('form', true);

    // 체크된 데이터나 심사일자, 심사시간, 심사인원이 변경된 경우 저장 수행
    if (checkedDetails.length > 0 || isAuditResultChanged.value || isPeopleChanged.value) {
        if (isValid) {
            confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction });
        }
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
});
const signatureComponent = ref();
const saveAction = async () => {
    openLoading();

    // 체크된 항목만 필터링
    const checkedDetails = auditResultReportStore.inputForm.detail.filter(el => el.checked);

    // 심사일자, 심사시간, 심사팀원이 변경된 경우 저장
    if (isAuditResultChanged.value || isPeopleChanged.value || auditResultReportStore.inputForm.cmd === 'I') {
        auditResultReportStore.inputForm.auditHrList = auditResultReportStore.hrList.map(el => ({ hrId: el.id }));
        saveAuditHr(auditResultReportStore.inputForm, false);
    }

    auditResultReportStore.inputForm.auditDt = auditResultReportStore.inputForm.auditDt ? formatDateForDB(auditResultReportStore.inputForm.auditDt) : null;

    // inputForm의 detail을 체크된 항목만 대체
    const formToSave = {
        ...auditResultReportStore.inputForm,
        detail: checkedDetails
    };

    auditResultReportStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
    const formData = new FormData();
    formData.append('info', new Blob([JSON.stringify(formToSave)], { type: 'application/json' }));

    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file);
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' }));
        }
    });
    const response = await saveAuditResult(formData, true);
    
    if (response.success) {
        auditResultReportStore
            .showDetail(response.result)
            .then(() => {
                fileList.value.fnSearch();
                signatureComponent.value.setApprovalInfo('ARR', auditResultReportStore.inputForm.writeYear, auditResultReportStore.inputForm.docNo, auditResultReportStore.inputForm.docSeq);
                signatureComponent.value.Search()
                isAuditResultChanged.value = false;
                isPeopleChanged.value = false;
            })
            .finally(() => {
                endLoading();
            });
    } else {
        endLoading();
    }
};

btnDelete(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    let param = auditResultReportStore.inputForm.detail.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', '선택한 항목을 삭제하시겠습니까?', null, { fun: deleteAction });
});

const deleteAction = () => {
    auditResultReportStore.inputForm.detail = auditResultReportStore.inputForm.detail.filter(el => {
        if (el.checked) {
            if (el.cmd === 'U') {
                el.useYn = 'N';
            }
        }
        return !el.checked || el.cmd === 'U';
    });
    saveAction();
};
btnDownload(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    auditResultReportStore.btnDownload([auditResultReportStore.inputForm]);
});

const isPeopleChanged = ref(false); // 심사 팀원 변경 여부
const selectPeopleMulty = e => {
    if (Array.isArray(e) && e.length > 0) {
        isPeopleChanged.value = true;
        auditResultReportStore.hrList = e.map(el => {
            // 팝업 텍스트에 이름만 남길 수 있도록 수정
            const fullName = el.hrNm || '';
            const noParentheses = fullName.replace(/\([^)]*\)/g, '').trim(); // 괄호 제거
            const matched = noParentheses.match(/.*[^가-힣a-zA-Z0-9]+([가-힣a-zA-Z0-9]+)\s*$/);
            const cleanName = matched ? matched[1] : noParentheses;

            return {
                id: el.hrId,
                nm: cleanName,
                hrId: el.hrId,
                hrNm: el.hrNm
            };
        });
    }

    closePeoplePopup();
};

const removePeople = () => {
    isPeopleChanged.value = true;
};

const isAuditResultChanged = ref(false); // 심사일자, 심사시간 변경 여부
const changeAuditResultDetail = () => {
    isAuditResultChanged.value = true;
};

// 자동체크함수
const chkData = index => {
    auditResultReportStore.inputForm.detail[index].checked = true;
};

// ==============파일==============
import IFileList from '@/components/file/iFileList.vue';
import { saveAuditResult, saveAuditHr } from '@/stores/safewiz/evaluation/api/auditResultReportApi';
const fileList = ref();
</script>
