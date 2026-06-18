<template>
    <div class="contents form ui">
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
            <div class="box" id="form">
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 df aife es-grid12-12 es-order1">
                            <div class="field w100p">
                                <label for="">작성년도</label>
                                <input :value="internalAuditStore.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>

                        <div class="grid12-9 es-grid12-12">
                            <div class="field">
                                <ISignature ref="signatureComponent" :cmd="auditStore.inputForm.cmd" targetType="AXP" :writeYear="auditStore.inputForm.writeYear" :docNo="auditStore.inputForm.docNo"></ISignature>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 df aife es-grid12-12">
                            <div class="field w100p">
                                <label for="writeDt" required><span> 작성일자 </span></label>
                                <input v-model="auditStore.inputForm.writeDt" class="br4px datepicker" :min="auditStore.writeYear + '.01.01'" :max="auditStore.writeYear + '.12.31'" v-calendar="getDateFormat()" type="text" id="writeDt" required />
                            </div>
                        </div>
                        <div class="grid12-5 es-grid12-12">
                            <div class="field w100p">
                                <label for="auditNm" required> <span> 심사명 </span></label>
                                <input v-model="auditStore.inputForm.auditNm" class="br4px" type="text" id="auditNm" v-input required />
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-8">
                            <div class="field">
                                <label for="auditDiv" required>
                                    <span>심사 구분</span>
                                </label>
                                <select v-select required v-model="auditStore.inputForm.auditDiv" id="auditDiv">
                                    <option v-for="item in auditStore.auditDivList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-4">
                            <div class="field">
                                <label for="">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :true-value="'Y'" :false-value="'N'" :checked="auditStore.inputForm.useYn === 'Y'" v-model="auditStore.inputForm.useYn" v-input="'사용'" type="checkbox" class="df switch" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-9"></div>
                    </div>

                    <div class="field">
                        <label for="">심사 목적 및 범위</label>
                        <textarea v-model="auditStore.inputForm.auditPurposeScope" class="minh10rem br4px"></textarea>
                    </div>

                    <div class="field">
                        <label for="">심사 준비 요청 사항</label>
                        <textarea v-model="auditStore.inputForm.auditRequest" class="minh10rem br4px"></textarea>
                    </div>
                    <div class="field">
                        <label for="">배포처</label>
                        <input v-model="auditStore.inputForm.distribute" class="br4px" type="text" v-input />
                    </div>
                    <div class="field mt2rem">
                        <label for="">심사일정</label>
                        <div class="accordion mb2rem df fdc rg1rem">
                            <div class="list" v-for="(item, itemIndex) in auditStore.inputForm.auditOrgnList" :key="itemIndex" @click="focusIndex = itemIndex">
                                <!-- 🐸전략기획팀 tit -->
                                <button class="df jcsb aic" @click="toggleAccordion">
                                    <div class="init df aic gap2rem">
                                        <input type="checkbox" v-input v-model="item.checked" :checked="item.checked" />
                                        <em>{{ item.orgnNm }}</em>
                                    </div>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <!--🦖 전략기획팀 cont -->
                                <div class="segment oh">
                                    <div class="pa2rem bcF8F9FB">
                                        <div class="row flex gutters1rem">
                                            <div class="grid12-5 es-grid12-6 us-grid12-6">
                                                <div class="field">
                                                    <label :for="'auditDt' + itemIndex" required><span> 심사 일자 </span></label>
                                                    <input :id="'auditDt' + itemIndex" v-model="item.auditDt" type="text" class="datepicker radius" v-calendar="getDateFormat()" @input="onValueChanged(item)" required />
                                                </div>
                                            </div>

                                            <div class="grid12-5 es-grid12-6 us-grid12-6">
                                                <div class="field">
                                                    <label for="">심사 시간</label>
                                                    <input v-model="item.auditTime" type="text" class="datepicker radius" startTime endTime v-calendar="''" @input="onValueChanged(item)" />
                                                </div>
                                            </div>

                                            <div class="grid12-2 es-grid12-6 us-grid12-12">
                                                <div class="field">
                                                    <label for="">사용여부</label>
                                                    <div class="df aic h4-4rem">
                                                        <input :true-value="'Y'" :false-value="'N'" :checked="item.useYn === 'Y'" v-model="item.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="onValueChanged(item)" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="grid12-5 es-grid12-6 us-grid12-12">
                                                <div class="field">
                                                    <label :for="'orgnNm' + itemIndex" required><span> 대상 조직 </span></label>
                                                    <i-chips :chips="[{ id: item.orgnId, nm: item.orgnNm }]" @popup="openOrgn(itemIndex)" @removeChip="removeOrgn" save :required="item.checked"></i-chips>
                                                </div>
                                            </div>
                                            <div class="grid12-7 es-grid12-12">
                                                <div class="field">
                                                    <label for="">심사 팀원</label>
                                                    <i-chips :chips="item.auditHrList" @popup="openJudge(itemIndex)" @removeChip="removeJudge"> </i-chips>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="field">
                                            <label for="">심사 내용</label>
                                            <textarea v-model="item.auditContent" class="minh10rem br4px" @change="onValueChanged(item)"></textarea>
                                        </div>

                                        <div class="field">
                                            <label for="">비고</label>
                                            <textarea v-model="item.remark" class="minh10rem br4px" @change="onValueChanged(item)"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="w100p df aic jcc py1rem">
                        <!-- 추가 버튼 입니다. -->
                        <i class="pa1rem db cp">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none" @click="addAudit">
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
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'조직'" :inputForm="auditStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="judgePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'심사 팀원'" :inputForm="auditStore.inputForm" filterKey="hrNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getJudgeList" :selectedIdList="auditStore.inputForm.auditOrgnList[focusIndex]?.auditHrList?.map(el => el.hrId)" uniqueKey="hrId" @close="closeJudge" @apply="applyJudge" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, t, ref, router, getCompId, toastPopup, confirmMsg, alertMsg, onMounted, getCurrentDate, formatDate, validationStore, setRouterParam, btnSample, btnSearch, btnAdd, btnBack, btnSave, btnDelete, btnDownload, formatDateForDB } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';

import { useAuditExecutionPlanStore } from '@/stores/safewiz/evaluation/auditExecutionPlan.js';
const auditStore = useAuditExecutionPlanStore();

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const internalAuditStore = useAuditStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnAdd', 'btnSave'];

import { saveAuditExecutionPlan } from '@/stores/safewiz/evaluation/api/auditExecutionPlanApi.js';

const signatureComponent = ref();

// [시스템코드 조회]
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
const setSystemCode = () => {
    getSystemCode({
        majorCd: 'C0042',
        compId: getCompId()
    }).then(res => {
        auditStore.auditDivList = res.list;
        // if (!auditStore.inputForm.auditDiv) auditStore.inputForm.auditDiv = res.list[0].minorCd;
    });
};

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        auditStore.btnDetail(param, true);
        layoutStore.useBtnList = uButtonList;
    } else if (!auditStore.inputForm || !auditStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('OhsInternalAuditExecutionPlan');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }

    setSystemCode();
    setTimeout(() => {
        signatureComponent.value.Search();
    }, 20);
    focusIndex.value = -1;
});
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
});
const goBack = () => {
    router.push('/OhsInternalAuditExecutionPlan');
};

btnSearch(async () => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction, param: true });
});
const searchAction = notify => {
    let param = {
        writeYear: auditStore.inputForm.writeYear,
        docType: auditStore.inputForm.docType,
        docNo: auditStore.inputForm.docNo
    };
    auditStore.btnDetail(param, notify);
    signatureComponent.value.setApprovalInfo('AXP', auditStore.inputForm.writeYear, auditStore.inputForm.docNo);
    signatureComponent.value.Search();
};

btnAdd(() => {
    addAudit();
});
const addAudit = () => {
    auditStore.inputForm.auditOrgnList.unshift({
        cmd: 'I',
        writeYear: auditStore.inputForm.writeYear,
        docType: auditStore.inputForm.docType,
        docNo: auditStore.inputForm.docNo,
        auditDt: getCurrentDate(),
        auditHrList: [],
        orgnId: null,
        orgnNm: '',
        useYn: 'Y',
        checked: true
    });
    console.log(auditStore.inputForm.auditOrgnList);
};

btnSave(async () => {
    // const duplicatedOrgnList = auditStore.inputForm.auditOrgnList.map(el => el.orgnId);
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        // for (let i = 0; i < auditStore.inputForm.auditOrgnList.filter(el => el.checked).length; i++) {
        //     if (duplicatedOrgnList.includes(auditStore.inputForm.auditOrgnList[i].orgnId)) {
        //         alertMsg('warning', `[${auditStore.inputForm.auditOrgnList[i].orgnNm}] 은(는)이미 존재하는 조직입니다.`);
        //         return;
        //     }
        // }
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
    }
});
const saveAction = notify => {
    auditStore.inputForm.auditOrgnList = auditStore.inputForm.auditOrgnList.filter(el => el.checked);
    auditStore.inputForm.writeDt = auditStore.inputForm.writeDt ? formatDateForDB(auditStore.inputForm.writeDt) : null;

    auditStore.inputForm.auditOrgnList.forEach(item => {
        item.auditDt = item.auditDt ? formatDateForDB(item.auditDt) : null;
    });

    openLoading();
    saveAuditExecutionPlan(auditStore.inputForm, notify)
        .then(res => {
            auditStore.inputForm = res.result;
            focusIndex.value = -1;
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            searchAction(false);
            layoutStore.useBtnList = uButtonList;
            endLoading();
        });
};
btnDelete(() => {
    auditStore.btnDelete([auditStore.inputForm]);
});
btnDownload(() => {
    const checkedList = auditStore.inputForm.auditOrgnList.filter(el => el.checked).map(el => el.docSeq);
    if (auditStore.inputForm.auditOrgnList.filter(el => el.checked).some(el => el.cmd === 'I')) {
        alertMsg('warning', t('msgUnSaved'));
        return;
    }
    auditStore.btnDownload([auditStore.inputForm], checkedList, checkedList.length > 0 ? 'msgCheckedPrint' : 'msgPrint');
});

//-----------------------------------------------
// [조직 팝업]
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
const orgnPopup = ref(null);
const focusIndex = ref(-1);
const openOrgn = index => {
    focusIndex.value = index;
    orgnPopup.value.onOpen();
};
const removeOrgn = () => {
    auditStore.inputForm.auditOrgnList[focusIndex.value].orgnId = '';
    auditStore.inputForm.auditOrgnList[focusIndex.value].orgnNm = '';
    auditStore.inputForm.auditOrgnList[focusIndex.value].checked = true;
};
const closeOrgn = e => {
    if (e.length > 0) {
        const duplicatedOrgn = auditStore.inputForm.auditOrgnList.some(el => el.useYn === 'Y' && el.orgnId === e[0].orgnId);
        if (duplicatedOrgn) {
            alertMsg('warning', `${e[0].orgnNm} 조직은 이미 존재합니다.`);
            orgnPopup.value.onClose();
            return;
        }
        auditStore.inputForm.auditOrgnList[focusIndex.value].orgnId = e[0].orgnId;
        auditStore.inputForm.auditOrgnList[focusIndex.value].orgnNm = e[0].orgnNm;
        auditStore.inputForm.auditOrgnList[focusIndex.value].checked = true;
    }
    orgnPopup.value.onClose();
};

//-----------------------------------------------
// [심사 팀원 팝업]
import { getJudgeList } from '@/stores/safewiz/evaluation/api/auditExecutionPlanApi.js';
const judgePopup = ref(null);
const openJudge = index => {
    focusIndex.value = index;
    judgePopup.value.onOpen();
};
const removeJudge = () => {
    // auditStore.inputForm.auditOrgnList[focusIndex.value].orgnId = '';
    // auditStore.inputForm.auditOrgnList[focusIndex.value].orgnNm = '';
    auditStore.inputForm.auditOrgnList[focusIndex.value].checked = true;
};
const closeJudge = () => {
    judgePopup.value.onClose();
};

const applyJudge = e => {
    if (e.length > 0) {
        auditStore.inputForm.auditOrgnList[focusIndex.value].auditHrList = e.map(el => ({ id: el.hrId, nm: el.hrNm, hrId: el.hrId, hrNm: el.hrNm }));
    }
    judgePopup.value.onClose();
};
const onValueChanged = (item, type) => {
    if (focusIndex.value !== -1) {
        // 최초 일자, 시간 세팅이 아닐 경우에만 체크
        item.checked = true;
    }
};
import { /* ref, */ nextTick } from 'vue';
import gsap from 'gsap';

// ==============아코디언==============
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
</script>
