<template>
    <div class="contents form ui df fdc">
        <!-- 검색 필드 -->
        <div class="mb1-6rem es-fww">
            <div class="row flex gutters1rem">
                <div class="grid12-3 us-grid12-12">
                    <input type="text" class="radius datepicker" v-calendar="'yyyy'" v-model="annualPlanStore.writeYear" year @input="searchAction" />
                </div>
                <div class="grid12-9 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="searchAnnualPlan" />

                        <button type="submit">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="box h100p df fdc oh">
            <OverlayScrollbarsComponent
                class="pa2-2rem fg1"
                ref="overlayScrollbars"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh" id="form">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 df aife es-grid12-12 es-order1"></div>
                        <div class="grid12-9 es-grid12-12">
                            <ISignature v-if="annualPlanStore.dataList && annualPlanStore.dataList.length > 0" :key="annualPlanStore.writeYear" ref="signatureComponent" :isWriter="false" :cmd="cmd" targetType="AAP" :writeYear="annualPlanStore.writeYear" :docNo="annualPlanStore.compId"></ISignature>
                        </div>
                    </div>

                    <!-- ==========================전략기획팀 start========================== -->
                    <div class="accordion mt2rem df fdc rg1rem" v-if="annualPlanStore.dataList && annualPlanStore.dataList.length > 0">
                        <div class="list" v-for="(data, index) in annualPlanStore.dataList" :key="index" @click="focusIndex = index">
                            <!-- 🐸전략기획팀 tit -->
                            <button class="df jcsb aic" @click="toggleAccordion">
                                <span>
                                    <input class="mr8px" type="checkbox" v-input :checked="data.checked" v-model="data.checked" />
                                    <em>{{ data.orgnNm }}</em>
                                </span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <div class="pa2rem us-pa1rem bcF8F9FB">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-7 es-grid12-12">
                                            <div class="field">
                                                <label for="orgnNm" required>
                                                    <span>조직</span>
                                                </label>
                                                <i-chips :chips="[{ id: data.orgnId, nm: data.orgnNm }]" @popup="openOrgn(index)" @removeChip="removeOrgn" save :required="data.checked"></i-chips>
                                            </div>
                                        </div>

                                        <div class="grid12-3 es-grid12-6">
                                            <div class="field">
                                                <label for>등록일자</label>
                                                <input type="text" v-model="data.createdAt" class="datepicker radius" v-calendar="getDateFormat()" readonly />
                                            </div>
                                        </div>

                                        <div class="grid12-2 es-grid12-6">
                                            <div class="field">
                                                <label for>사용여부</label>
                                                <div class="df aic h4-4rem">
                                                    <input :true-value="'Y'" :false-value="'N'" :checked="data.useYn === 'Y'" v-model="data.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="onValueChanged(data)" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="field">
                                        <label for>심사일정</label>
                                        <OverlayScrollbarsComponent
                                            ref="overlayScrollbars"
                                            :options="{
                                                scrollbars: {
                                                    autoHide: 'hover',
                                                    x: 'visible',
                                                    y: 'hidden'
                                                }
                                            }"
                                        >
                                            <table class="sm-minw60rem">
                                                <thead>
                                                    <tr>
                                                        <th v-for="idx in 12" :key="idx">{{ idx }}</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td v-for="(_, idx) in checkBoxs" :key="idx" class="cp tac">
                                                            <input :true-value="'Y'" :false-value="'N'" type="checkbox" v-input v-model="data[`auditSchedule${idx + 1}`]" @change="onValueChanged(data)" />
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </OverlayScrollbarsComponent>
                                    </div>

                                    <div class="field">
                                        <label for>비고</label>
                                        <div class="bd1pxsolidE1E6ED br4px bcFFFFFF">
                                            <div class="pa1rem">
                                                <div class="row flex gutters1rem" v-for="(remark, remarkIndex) in data.remarkList" :key="remark.month">
                                                    <div class="grid12-1 df jcc aic" :id="`list_${remarkIndex}`">
                                                        <i class="cp" @click="removeRemark(data, remarkIndex)">
                                                            <svg class="sm-w18px sm-h18px" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                                <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                            </svg>
                                                        </i>
                                                    </div>
                                                    <div class="grid12-3 es-grid12-6" :key="resetKey">
                                                        <div class="field">
                                                            <label v-show="false" :for="`month${remarkIndex}`">월</label>
                                                            <!-- <div class="df aic h4-4rem"> -->
                                                            <select v-select :value="remark.month" @change="remarkMonthChanged(remark, $event)" :id="`month${remarkIndex}`" required>
                                                                <option v-for="(month, idx) in selectBoxs" :key="idx" :value="month">{{ month }}월</option>
                                                            </select>
                                                            <!-- </div> -->
                                                        </div>
                                                    </div>
                                                    <div class="grid12-8 es-grid12-5">
                                                        <input v-model="remark.remark" class="br4px" type="text" v-input placeholder="비고를 입력하세요." @change="data.checked = true" />
                                                    </div>
                                                </div>
                                                <div class="w100p df aic jcc bcF9FAFF br4px" :class="{ 'mt0-5rem': data.remarkList.length > 0 }">
                                                    <!-- 추가 버튼 입니다. -->
                                                    <i class="pa1rem db cp" @click="addRemark(data)">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                            <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                        </svg>
                                                    </i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 추가 -->
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-else>
                        <div class="card h100p df aic jcc cp" @click="addAudit">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>

                                <span class="db mt1rem fs2rem c999999">{{ t('card_AnnualOhsInternalAuditPlan') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'조직'" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, t, ref, router, getCompId, toastPopup, confirmMsg, alertMsg, onMounted, getCurrentDate, formatDate, formatDateForDB, baseDownload, validationStore, /*  t, formatDate, watch, */ setRouterParam, btnSearch, btnAdd, btnBack, btnSave, btnDelete, btnDownload } = BaseView();
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { useAnnualPlanStore } from '@/stores/safewiz/evaluation/annualPlan.js';
const annualPlanStore = useAnnualPlanStore();

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();

import { getAnnualOhsInternalAuditPlan, saveAnnualOhsInternalAuditPlan, deleteAnnualOhsInternalAuditPlan, getAnnualOhsInternalAuditPlanReport } from '@/stores/safewiz/evaluation/api/annualPlanApi';
const cmd = ref('I');
const iButtonList = ['btnBack', 'btnAdd', 'btnSave', 'btnSearch'];
const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];

const focusIndex = ref(-1);
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    annualPlanStore.compId = getCompId();
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        annualPlanStore.writeYear = param.writeYear;
    } else {
        annualPlanStore.writeYear = auditStore.writeYear;
    }

    if (!annualPlanStore.writeYear) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('InternalAudit');
        return;
    }

    await searchAction(true, true);
    focusIndex.value = -1;
});

const signatureComponent = ref(null);

// 버튼
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
});
const goBack = () => {
    router.push('/InternalAudit');
};

btnSearch(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction, param: true });
});

const searchAction = notify => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    auditStore.writeYear = annualPlanStore.writeYear;
    openLoading();
    getAnnualOhsInternalAuditPlan({ writeYear: annualPlanStore.writeYear }, notify)
        .then(res => {
            if (res.list.length > 0) {
                layoutStore.useBtnList = uButtonList;
                cmd.value = 'U';
                res.list.forEach(item => {
                    item.createdAt = formatDate(item.createdAt);
                });
            } else {
                layoutStore.useBtnList = iButtonList;
                cmd.value = 'I';
            }
            annualPlanStore.dataList = _.cloneDeep(res.list);
            originDataList.value = _.cloneDeep(res.list); // 초기엔 전체 리스트로 세팅
        })
        .finally(() => {
            //데이터가 하나도 등록되어 있지 않을 경우 ISignature도 비활성화 되므로 예외처리
            if(signatureComponent.value !== null){
                signatureComponent.value.setApprovalInfo('AAP', annualPlanStore.writeYear, annualPlanStore.compId)
                signatureComponent.value.Search()
            }
            endLoading();
        });
};

btnAdd(() => {
    searchTerm.value = '';
    searchAnnualPlan();
    addAudit();
});
const addAudit = () => {
    annualPlanStore.dataList.unshift({
        writeYear: annualPlanStore.writeYear,
        docType: 'AAP',
        cmd: 'I',
        auditSchedule1: 'N',
        auditSchedule2: 'N',
        auditSchedule3: 'N',
        auditSchedule4: 'N',
        auditSchedule5: 'N',
        auditSchedule6: 'N',
        auditSchedule7: 'N',
        auditSchedule8: 'N',
        auditSchedule9: 'N',
        auditSchedule10: 'N',
        auditSchedule11: 'N',
        auditSchedule12: 'N',
        useYn: 'Y',
        remarkList: [],
        checked: true,
        compId: getCompId(),
        createdAt: getCurrentDate()
    });
};

btnSave(async () => {
    const validData = annualPlanStore.dataList.filter(el => el.checked);
    if (validData.length === 0) {
        signatureComponent.value.setApprovalInfo('AAP', annualPlanStore.writeYear, annualPlanStore.compId);
        signatureComponent.value.Search()
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // validData.some((item, i) =>
    //     item.remarkList.some((remark, j) => {
    //         if (remark.month === null) {
    //             alertMsg('warning', `[${item.orgnNm}] 조직의 비고 월은 필수입니다.`);
    //             return true; // 내부 some 탈출
    //         }
    //         return false;
    //     })
    // );
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: validData });
    }
});
const saveAction = saveData => {
    if (saveData.length === 0) {
        return;
    }
    console.log('@@@ 저장 ', saveData);
    openLoading();
    saveAnnualOhsInternalAuditPlan(saveData, true)
        .then(res => {
            layoutStore.useBtnList = uButtonList;
        })
        .finally(() => {
            validationStore.clearInvalidClasses();
            validationStore.clearAllErrors();
            searchAction(false);
        });
};
btnDelete(() => {
    let list = annualPlanStore.dataList.filter(el => el.checked);
    console.log('@@@ 삭제');
    if (!list.length) {
        signatureComponent.value.setApprovalInfo('AAP', annualPlanStore.writeYear, annualPlanStore.compId);
        signatureComponent.value.Search()
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    if (list.some(el => el.useYn === 'N')) {
        signatureComponent.value.setApprovalInfo('AAP', annualPlanStore.writeYear, annualPlanStore.compId);
        signatureComponent.value.Search()
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: list });
});
const deleteApi = list => {
    openLoading();
    deleteAnnualOhsInternalAuditPlan(list, true)
        .then(res => {
            if (res.success) {
                searchAction(false);
            }
        })
        .catch(() => {
            endLoading();
        });
};
btnDownload(() => {
    if (annualPlanStore.dataList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    let msg = 'msgCheckedPrint';
    let list = annualPlanStore.dataList.filter(el => el.checked);
    if (list.length === 0) {
        // 전체 출력
        list = annualPlanStore.dataList
            .filter(el => el.useYn === 'Y')
            .map(el => ({
                writeYear: el.writeYear,
                docType: el.docType,
                docNo: el.docNo,
                compId: el.compId
            }));
        msg = 'msgPrint';
    } else {
        if (list.some(el => el.cmd === 'I')) {
            alertMsg('warning', t('msgUnSaved'));
            return;
        }
        // 선택된 항목 출력
        list = list.map(el => ({
            writeYear: el.writeYear,
            docType: el.docType,
            docNo: el.docNo,
            compId: el.compId
        }));
    }
    confirmMsg('warning', t(msg), ``, { fun: downloadAction, param: list });
});
const downloadAction = list => {
    let param = {
        writeYear: annualPlanStore.writeYear,
        checkedObjList: list,
        compId: getCompId()
    };
    baseDownload(getAnnualOhsInternalAuditPlanReport, param);
    // openLoading();
    // getAnnualOhsInternalAuditPlanReport(param, false)
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
};

// 조직 선택 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
const orgnPopup = ref(null);
const openOrgn = index => {
    focusIndex.value = index;
    orgnPopup.value.onOpen();
};
const removeOrgn = () => {
    annualPlanStore.dataList[focusIndex.value].orgnId = '';
    annualPlanStore.dataList[focusIndex.value].orgnNm = '';
    annualPlanStore.dataList[focusIndex.value].checked = true;
};
const closeOrgn = e => {
    if (e.length > 0) {
        const duplicatedOrgn = annualPlanStore.dataList.some(el => el.useYn === 'Y' && el.orgnId === e[0].orgnId);
        if (duplicatedOrgn) {
            alertMsg('warning', `${e[0].orgnNm} 조직은 이미 존재합니다.`);

            orgnPopup.value.onClose();
            return;
        }
        annualPlanStore.dataList[focusIndex.value].orgnId = e[0].orgnId;
        annualPlanStore.dataList[focusIndex.value].orgnNm = e[0].orgnNm;
        annualPlanStore.dataList[focusIndex.value].checked = true;
    }
    orgnPopup.value.onClose();
};

// etc event
const onValueChanged = data => {
    if (focusIndex.value !== -1) {
        // 최초 일자, 시간 세팅이 아닐 경우에만 체크
        data.checked = true;
    }
};
const resetKey = ref(0);
const remarkMonthChanged = (remark, e) => {
    const changingValue = parseInt(e.target.value);
    let validData = annualPlanStore.dataList[focusIndex.value].remarkList.findIndex(el => el.month == changingValue);
    if (validData > -1) {
        alertMsg('warning', `${changingValue}월은 이미 존재합니다.`);
        resetKey.value++;
        return;
    } else {
        remark.month = changingValue;
    }
    if (focusIndex.value !== -1) {
        annualPlanStore.dataList[focusIndex.value].checked = true;
    }
};

const removeRemark = (data, index) => {
    data.checked = true;
    data.remarkList.splice(index, 1);
};
const addRemark = data => {
    data.checked = true;
    data.remarkList.unshift({
        writeYear: annualPlanStore.writeYear,
        month: null,
        remark: null
    });
    setTimeout(() => {
        const element = document.getElementById(`list_${data.remarkList.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 퍼블리싱 영역
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import { nextTick } from 'vue';
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

//==============checkbox==============
// [ table 안 체크박스]
const checkBoxs = ref(Array(12).fill(false));

//==============select==============
const selectBoxs = ref([null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]);

//==============검색어 필터링==============
const originDataList = ref([]);
const searchTerm = ref('');
const searchAnnualPlan = () => {
    const term = searchTerm.value?.toLowerCase() || '';
    if (!term) {
        annualPlanStore.dataList = _.cloneDeep(originDataList.value);
        return;
    }

    annualPlanStore.dataList = originDataList.value.filter(item => item.orgnNm?.toLowerCase().includes(term));
};
</script>
