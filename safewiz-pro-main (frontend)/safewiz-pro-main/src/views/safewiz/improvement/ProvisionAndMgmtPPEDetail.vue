<template>
    <div id="form" class="contents">
        <div class="control-field ui form box df fdc">
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
                <form @submit.prevent ref="myForm">
                    <div class="pa2-2rem bcFFFFFF">
                        <div id="mainForm" class="row flex gutters1rem">
                            <div class="grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="ppeId" required>
                                        <span>품목</span>
                                    </label>
                                    <i-chips :chips="[{ id: provisionAndMgmtPPEStore.inputForm.ppeId, nm: provisionAndMgmtPPEStore.inputForm.ppeNm }]" @popup="openPPEManage" required></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for="">지급기준</label>
                                    <input class="br4px" type="text" v-input readonly placeholder="품목 선택시 자동으로 입력 됩니다" v-model="provisionAndMgmtPPEStore.inputForm.provisionStandard" />
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for="">지급대상</label>
                                    <input class="br4px" type="text" v-input readonly placeholder="품목 선택시 자동으로 입력 됩니다" v-model="provisionAndMgmtPPEStore.inputForm.provisionTarget" />
                                </div>
                            </div>
                            <!-- 현재 수불 정보 입력 폼 -->
                            <div class="grid12-3 ul-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="receiptDt" required><span>수불일자</span></label>
                                    <input id="receiptDt" class="datepicker br4px" type="text" v-calendar="getDateFormat()" v-model="provisionAndMgmtPPEStore.inputReceiptDt" required maxlength="10" />
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="peopleSign" required><span>담당자</span></label>
                                    <i-hr-chips-sign required id="peopleSign" name="담당자" type="company" ref="insertPeopleSign" :cmd="provisionAndMgmtPPEStore.inputForm.cmd" targetType="PPE" :writeYear="provisionAndMgmtPPEStore.inputForm.writeYear" :docNo="provisionAndMgmtPPEStore.inputForm.docNo" :docSeq="provisionAndMgmtPPEStore.inputForm.ppeId" :single="true" @chip-removed="deselectPeople" @popup="peoplePopupOpen('companyMember')" />
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6">
                                <div class="field">
                                    <label for="orgnId" required><span>조직</span></label>
                                    <i-chips :chips="[{ id: provisionAndMgmtPPEStore.inputForm.orgnId, nm: provisionAndMgmtPPEStore.inputForm.orgnNm }]" :readonly="true"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6">
                                <div class="field">
                                    <label for="">비고</label>
                                    <input class="br4px" type="text" v-model="provisionAndMgmtPPEStore.inputForm.remark" />
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for="">현재 재고량</label>
                                    <input class="br4px" type="text" readonly placeholder="품목 선택시 자동으로 입력 됩니다" v-model="provisionAndMgmtPPEStore.inputForm.lastStoreQty" />
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for="">입고량</label>
                                    <input type="number" min="0" class="br4px" v-model="provisionAndMgmtPPEStore.inputForm.inQty" @input="calcStoreQty" />
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for="">지급량</label>
                                    <input type="number" min="0" class="br4px" v-model="provisionAndMgmtPPEStore.inputForm.outQty" @input="calcStoreQty" />
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for=""> 수불 후 재고량</label>
                                    <input class="br4px" type="text" readonly v-model="provisionAndMgmtPPEStore.inputForm.storeQty" />
                                </div>
                            </div>
                        </div>

                        <!-- 이전 수불 정보 리스트 -->
                        <div class="row flex">
                            <div class="grid12-12" v-if="detailList.length > 0">
                                <div class="accordion mt2rem">
                                    <div class="list">
                                        <button id="btnAccordion" type="button" class="radius w15rem df jcsb aic" @click="toggleAccordion" :class="{ active: activeAccordion }">
                                            <!--🐸 title -->
                                            <div class="df gap2rem">
                                                <em>{{ provisionAndMgmtPPEStore.inputForm.writeYear + '년 ' + (provisionAndMgmtPPEStore.inputForm.receiptMonth ? provisionAndMgmtPPEStore.inputForm.receiptMonth.substring(4, 6) + '월' : '') + ' 입출고내역(' + detailList.length + '건)' }}</em>
                                            </div>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <div class="segment">
                                            <div class="df fdc oh pa2-2rem bcF8F9FB">
                                                <!-- 컨텐츠 반복 -->
                                                <div class="df fdc rg8px bcFFFFFF">
                                                    <template v-for="(value, index) in detailList" :key="index">
                                                        <div :class="['df bd1pxsolidE1E6ED br4px lg-fww', { 'contents-detail': detailList.some(item => item.isLast === 'Y') }]">
                                                            <div class="w5rem df jcc aic bdr1pxsolidE1E6ED lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED shrink0" v-if="value.isLast == 'Y'">
                                                                <button type="button" v-button @click="beforeDelete">
                                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                        <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                                    </svg>
                                                                </button>
                                                            </div>

                                                            <div class="pa2-2rem lg-w100p">
                                                                <!-- [grid 미사용] -->
                                                                <div class="row flex gutters1rem">
                                                                    <div class="grid12-3 ul-grid12-6 es-grid12-12">
                                                                        <div class="field">
                                                                            <label for="">수불일자</label>
                                                                            <input class="datepicker br4px" type="text" v-calendar="getDateFormat()" placeholder="2024.10.20" :value="value.receiptDt" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 ul-grid12-6 es-grid12-12">
                                                                        <div class="field">
                                                                            <label for="">담당자</label>
                                                                            <i-hr-chips-sign type="company" ref="peopleSignList" cmd="U" targetType="PPE" :writeYear="value.writeYear" :docNo="value.docNo" :docSeq="value.ppeId" :single="true" :signOnly="true" />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 ul-grid12-6">
                                                                        <div class="field">
                                                                            <label for="">조직</label>
                                                                            <input class="br4px" type="text" :value="value.orgnNm" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 ul-grid12-6">
                                                                        <div class="field">
                                                                            <label for="">비고</label>
                                                                            <input class="br4px" type="text" :value="value.remark" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 el-grid12-6">
                                                                        <div class="field">
                                                                            <label for="">이전 재고량</label>
                                                                            <input class="br4px" type="text" placeholder="47" :value="value.lastStoreQty" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 el-grid12-6">
                                                                        <div class="field">
                                                                            <label for>입고량</label>
                                                                            <input type="number" class="br4px" :value="value.inQty" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 el-grid12-6">
                                                                        <div class="field">
                                                                            <label for>지급량</label>
                                                                            <input type="number" class="br4px" :value="value.outQty" readonly />
                                                                        </div>
                                                                    </div>

                                                                    <div class="grid12-3 el-grid12-6">
                                                                        <div class="field">
                                                                            <label for="">재고량</label>
                                                                            <input class="br4px" type="text" placeholder="47" :value="value.storeQty" readonly />
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </template>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
    </div>

    <!-- 안전보호구 품목 선택 -->
    <teleport to="body">
        <i-PopupDialog ref="PPEPop">
            <div class="contents w500px md-w100p">
                <!--            <PPEManagement :single="true" :type="'usage'" :searchDt="provisionAndMgmtPPEStore.inputForm.receiptMonth" @close="closePPEManage" @select="selectPPE" />-->
                <base-select-popup title="안전보호구 품목" filterKey="ppeNm" useYnKey="useYn" excludedValue="N" :single="true" :fetch-data="getDatasetPPEList" uniqueKey="ppeId" @close="selectPPE" />
            </div>
        </i-PopupDialog>
    </teleport>
    <!-- 담당자 선택 -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
            </div>
        </i-PopupDialog>
    </teleport>
    <!-- 조직 선택 -->
    <i-PopupDialog ref="orgnPopup">
        <div class="contents form ui sm" style="max-height: 800px; min-height: 500px; overflow-y: auto">
            <base-select-popup :title="'조직'" :selectedIdList="provisionAndMgmtPPEStore.inputForm.orgnIdList" :uniqueKey="[provisionAndMgmtPPEStore.inputForm.orgnId]" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="orgnPopupClose" />
            <!-- 버튼 콤포넌트 영역 시작 -->
        </div>
    </i-PopupDialog>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { onMounted } from 'vue';
import _ from 'lodash';
import { useButtonListStore } from '@/stores/buttonList';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import router from '@/router';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useProvisionAndMgmtPPEStore } from '@/stores/safewiz/improvement/provisionAndMgmtPPE.js';
import { getDatasetPPEList } from '@/stores/safewiz/improvement/api/provisionAndMgmtPPEApi.js';
import { getDateFormat } from '@/utils/dateUtil.js';

import { deleteApprovalInfo, searchApprovalInfo } from '@/api/base/common/utils';
import { getFormattedTargetId } from '@/utils/documentUtils.js';

const { ref, t, alertMsg, confirmMsg, toastPopup, btnBack, btnSearch, btnSave, btnDelete, btnDownload, getCurrentDate, validationStore, setRouterParam, getCompId, formatDateForDB } = BaseView();
const layoutStore = useButtonListStore();
const PPEPop = ref(null);
const orgnPopup = ref(null);
const peoplePopup = ref(null);
const insertPeopleSign = ref();
const detailList = ref([]);
const provisionAndMgmtPPEStore = useProvisionAndMgmtPPEStore();
const activeAccordion = ref(false);
//-----------------------------------------------
// [버튼]
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
const route = useRoute();

// 초기 데이터 로딩
onMounted(async () => {
    insertPeopleSign.value.initPeople([]);
    provisionAndMgmtPPEStore.inputForm.orgnId = null;
    provisionAndMgmtPPEStore.inputForm.orgnNm = null;
    provisionAndMgmtPPEStore.inputForm.inQty = 0;
    provisionAndMgmtPPEStore.inputForm.outQty = 0;
    detailList.value = [];
    activeAccordion.value = false;
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        console.log('##param', param);
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        let searchParam = {
            writeYear: param.writeYear,
            receiptMonth: formatDateForDB(param.date).substring(0, 6),
            docNo: param.docNo,
            ppeId: param.docSeq,
            compId: getCompId()
        };

        provisionAndMgmtPPEStore.btnDetail(searchParam).then(async res => {
            detailList.value = res.result;
            detailList.value.forEach(item => {
                item.docDetailSeq = formatDateForDB(item.receiptDt);
            });
            await nextTick();
            const button = document.getElementById('btnAccordion'); // 버튼을 직접 선택
            if (button) {
                button.click();
            }
            insertPeopleSign.value.initPeople([]); // 서명 초기화
            // 담당자 서명 리스트 출력
            res.result.forEach((el, index) => {
                const param = {
                    targetType: el.docType,
                    targetId: el.writeYear + el.docNo + el.ppeId,
                    type: 'company'
                };
                peopleSignList.value[index].Search(param);
            });

            provisionAndMgmtPPEStore.inputForm.lastStoreQty = detailList.value[0].storeQty;
            provisionAndMgmtPPEStore.inputForm.storeQty = detailList.value[0].storeQty;
            provisionAndMgmtPPEStore.inputForm.minReceiptDt = detailList.value[0].receiptDt;
        });

        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (!provisionAndMgmtPPEStore.inputForm || !provisionAndMgmtPPEStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('ProvisionAndMgmtPPE');
        return;
    } else {
        // 추가버튼으로 왔을 때
        searchData();
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
});

// 목록으로 버튼
btnBack(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (!provisionAndMgmtPPEStore.changedData()) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
    } else {
        goBack();
    }
});

// 조회 버튼
btnSearch(async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    if (!provisionAndMgmtPPEStore.changedData()) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchData });
    } else {
        searchData();
    }
});
// 저장 버튼
btnSave(async () => {
    // validataion check
    const isValid = await validationStore.validateAllFields('mainForm', true);

    if (!isValid) {
        return;
    }
    // validataion check
    if (insertPeopleSign.value.userList.length === 0) {
        alertMsg('warning', '담당자를 선택하십시오.');
        return;
    } else if (provisionAndMgmtPPEStore.inputReceiptDt < provisionAndMgmtPPEStore.inputForm.minReceiptDt) {
        alertMsg('warning', '수불일자는 가장 최근에 저장된 수불일자보다 \n 이전일 수 없습니다.');
        return;
    }

    confirmMsg('info', t('msgSave'), '', { fun: saveProvisionAndMgmtPPES });
});

const saveProvisionAndMgmtPPES = () => {
    provisionAndMgmtPPEStore.btnSave().then(async res => {
        provisionAndMgmtPPEStore.inputForm.lastStoreQty = res.result.storeQty;
        // 저장된 데이터 내용으로 입출고내역 다시 조회 ( searchParam 기준으로 조회 되어있어서 변경 )
        provisionAndMgmtPPEStore.inputForm.writeYear = res.result.receiptDt.substring(0, 4);
        // provisionAndMgmtPPEStore.inputForm.docNo = res.result.docNo;
        provisionAndMgmtPPEStore.inputForm.lastReceiptDt = res.result.receiptDt;
        provisionAndMgmtPPEStore.inputForm.receiptMonth = res.result.receiptDt;
        await insertPeopleSign.value.setHrChipsApprovalInfo('PPE', res.result.writeYear, res.result.docNo, res.result.ppeId);

        searchData(false);
        insertPeopleSign.value.initPeople([]); // 서명 초기화
    });
};

btnDownload(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (!provisionAndMgmtPPEStore.changedData()) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: fnDownload });
    } else {
        confirmMsg('info', t('msgPrint'), '', { fun: fnDownload });
    }
});

const fnDownload = () => {
    let param = _.cloneDeep(provisionAndMgmtPPEStore.inputForm);
    param.receiptMonth = param.receiptMonth.substring(4, 6);
    provisionAndMgmtPPEStore.btnDownload([param]);
};

// 삭제 버튼
btnDelete(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (!provisionAndMgmtPPEStore.changedData()) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: beforeDelete });
    } else {
        beforeDelete();
    }
});
const peopleSignList = ref([]);
const searchData = async notify => {
    await insertPeopleSign.value.Search();
    activeAccordion.value = false;
    provisionAndMgmtPPEStore.btnSearchDetail(notify).then(async res => {
        detailList.value = [];
        if (res.result.length == 0) {
            layoutStore.useBtnList = ['btnBack', 'btnSave'];
            provisionAndMgmtPPEStore.inputForm.minReceiptDt = null;
        } else {
            detailList.value = res.result;
            detailList.value.forEach(item => {
                item.docDetailSeq = formatDateForDB(item.receiptDt);
            });
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
            await nextTick();
            const button = document.getElementById('btnAccordion'); // 버튼을 직접 선택
            if (button) {
                button.click();
            }
            // 담당자 서명 리스트 출력
            res.result.forEach((el, index) => {
                const param = {
                    targetType: el.docType,
                    targetId: el.writeYear + el.docNo + el.ppeId,
                    type: 'company'
                };
                peopleSignList.value[index].Search(param);
            });

            provisionAndMgmtPPEStore.inputForm.lastStoreQty = detailList.value[0].storeQty;
            provisionAndMgmtPPEStore.inputForm.storeQty = detailList.value[0].storeQty;
            provisionAndMgmtPPEStore.inputForm.remark = '';
            provisionAndMgmtPPEStore.inputForm.orgnId = '';
            provisionAndMgmtPPEStore.inputForm.orgnNm = '';
            provisionAndMgmtPPEStore.inputForm.minReceiptDt = detailList.value[0].receiptDt;
            insertPeopleSign.value.initPeople([]);
        }
    });
};

const beforeDelete = () => {
    confirmMsg('warning', t('msgDelete'), '', { fun: deleteDetail });
};

const deleteDetail = () => {
    provisionAndMgmtPPEStore.btnDelete(detailList.value[0], true).then(async res => {
        if (res.success) {
            if (detailList.value.length === 1) {
                alertMsg('warning', provisionAndMgmtPPEStore.inputForm.receiptMonth + '월 입출고내역이 없습니다.\n 목록으로 이동합니다.');
                goBack();
                return;
            }
            // 서명 삭제
            const targetId = detailList.value[0].writeYear + detailList.value[0].docNo + detailList.value[0].ppeId;
            const param = {
                targetType: detailList.value[0].docType,
                targetId: targetId,
                type: 'company'
            };
            const searchRes = await searchApprovalInfo(param);
            if (searchRes.list.length > 0) {
                const formattedTargetId = getFormattedTargetId(detailList.value[0].docType, detailList.value[0].writeYear, detailList.value[0].docNo + detailList.value[0].ppeId);
                for (const [index, el] of searchRes.list.entries()) {
                    el.formattedTargetId = formattedTargetId;
                    await deleteApprovalInfo(el);
                }
            }
            searchData(false);
        }
    });
};

const goBack = () => {
    router.push('/ProvisionAndMgmtPPE');
};
// 품목 목록 열기
const openPPEManage = () => openPopup(PPEPop);
// 품목 목록 닫기
const closePPEManage = () => closePopup(PPEPop);

// 담당자 목록 열기
const div = ref();
const peoplePopupOpen = param => {
    // validationStore.clearInvalidClasses();
    // validationStore.clearAllErrors();
    div.value = param;
    peoplePopup.value.onOpen();
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
// 담당자 목록 닫기
const closePeoplePopup = () => closePopup(peoplePopup);
// 담당자 선택
const selectPeople = item => {
    insertPeopleSign.value.selectPeople();
    provisionAndMgmtPPEStore.inputForm.hrId = item.hrId;
    provisionAndMgmtPPEStore.inputForm.orgnId = item.orgnId;
    provisionAndMgmtPPEStore.inputForm.orgnNm = item.orgnNm;
    provisionAndMgmtPPEStore.inputForm.manager = '';
    peoplePopup.value.onClose();
};

// 담당자 선택 취소
const deselectPeople = () => {
    provisionAndMgmtPPEStore.inputForm.manager = null;
    insertPeopleSign.value.initPeople([]); // 서명 초기화
};

const orgnPopupClose = e => {
    if (e.length !== 0) {
        provisionAndMgmtPPEStore.inputForm.orgnId = e[0].orgnId;
        provisionAndMgmtPPEStore.inputForm.orgnNm = e[0].orgnNm;
    }
    closePopup(orgnPopup);
};

// 팝업 열기 공통 함수
const openPopup = popupRef => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 공통 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};
//------------------------------------------------
// [동작]
// 팝업 안전보호구 품목 선택
const selectPPE = selectedItem => {
    if (selectedItem.length > 0) {
        provisionAndMgmtPPEStore.inputForm.ppeId = selectedItem[0].ppeId;
        provisionAndMgmtPPEStore.inputForm.ppeNm = selectedItem[0].ppeNm;
        provisionAndMgmtPPEStore.inputForm.provisionStandard = selectedItem[0].provisionStandard;
        provisionAndMgmtPPEStore.inputForm.provisionTarget = selectedItem[0].provisionTarget;
        provisionAndMgmtPPEStore.inputForm.lastStoreQty = selectedItem[0].storeQty;
        provisionAndMgmtPPEStore.inputForm.inQty = 0;
        provisionAndMgmtPPEStore.inputForm.outQty = 0;
        provisionAndMgmtPPEStore.inputForm.storeQty = selectedItem[0].storeQty;
        provisionAndMgmtPPEStore.inputForm.remark = '';
        if (selectedItem[0].lastReceiptDt != null) {
            provisionAndMgmtPPEStore.inputForm.lastReceiptDt = formattingDate(selectedItem[0].lastReceiptDt);
            // provisionAndMgmtPPEStore.inputForm.receiptDt = formattingDate(selectedItem[0].lastReceiptDt + 1);
            provisionAndMgmtPPEStore.inputForm.receiptMonth = selectedItem[0].lastReceiptDt.substring(0, 6);
        } else {
            provisionAndMgmtPPEStore.inputForm.lastReceiptDt = getCurrentDate();
            // provisionAndMgmtPPEStore.inputForm.receiptDt = getCurrentDate();
        }
    }
    provisionAndMgmtPPEStore.inputForm.receiptDt = getCurrentDate();
    closePPEManage();
    searchData();
};

// 날짜 형변환
const formattingDate = date => {
    if (!date) {
        return '';
    }
    return `${date.substring(0, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}`;
};

const calcStoreQty = () => {
    const inputForm = provisionAndMgmtPPEStore.inputForm;

    // 숫자 필터링 (소수점 및 음수 방지)
    const sanitizeInput = value => {
        // 숫자로 변환 후 정수만 반환 (0 이하 값은 0으로 처리)
        const parsedValue = parseInt(value, 10);
        return isNaN(parsedValue) || parsedValue < 0 ? 0 : parsedValue;
    };

    // 정리된 값으로 필드 업데이트
    inputForm.inQty = sanitizeInput(inputForm.inQty);
    inputForm.outQty = sanitizeInput(inputForm.outQty);
    inputForm.lastStoreQty = sanitizeInput(inputForm.lastStoreQty);

    // 계산
    inputForm.storeQty = inputForm.lastStoreQty + inputForm.inQty - inputForm.outQty;
};

//-----------------------------------------------
// [UI]
import { /* ref, */ nextTick } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useRoute } from 'vue-router';

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
    activeAccordion.value = !activeAccordion.value;

    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
</script>
<style scoped lang="scss">
.contents-detail:not(:first-of-type) {
    margin-left: 5rem;
    @media (max-width: 1400px) {
        margin-left: 0;
    }
}
</style>
