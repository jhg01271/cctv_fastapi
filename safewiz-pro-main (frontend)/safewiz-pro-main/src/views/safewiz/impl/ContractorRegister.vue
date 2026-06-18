<template>
    <!-- 검색 필드 -->
    <div class="control-field form ui mb2rem es-fww">
        <div class="row flex gutters1rem">
            <div class="grid12-3 df aife us-grid12-8 us-order1">
                <div class="field w100p">
                    <input v-input type="text" year v-calendar="'yyyy'" class="datepicker w100p radius" v-model="contractorRegisterStore.writeYear" @input="searchAction(true)" />
                </div>
            </div>
            <div class="grid12-2 df aife us-grid12-4 us-order2">
                <div class="field">
                    <div class="df aic h4-4rem">
                        <input v-input="['사용', '미사용 포함']" type="checkbox" class="df switch" :checked="contractorRegisterStore.inputForm.useYn === 'Y'" @change="changedUseYn" />
                    </div>
                </div>
            </div>
            <div class="grid12-7 us-grid12-12">
                <div class="df bcffffff">
                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="contractorRegisterStore.inputForm.searchText" @keyup.enter="applyFilter" />

                    <button type="submit">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="oh box h100p df fdc">
        <OverlayScrollbarsComponent
            class="pa2-2rem fg1"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <div class="control-field mb2-2rem">
                    <div>
                        <div v-if="contractorRegisterStore.dataList.length > 0">
                            <ISignature :key="contractorRegisterStore.writeYear" ref="signatureComponent" :cmd="cmd" :is-writer="false" targetType="CTR" :writeYear="contractorRegisterStore.writeYear" :docNo="contractorRegisterStore.compId"></ISignature>
                        </div>
                    </div>
                </div>

                <div class="accordion mt2rem df fdc rg1rem" id="form" v-if="contractorRegisterStore.dataList && contractorRegisterStore.dataList.length > 0">
                    <div class="accordion form ui df fdc gap gap1-2rem">
                        <div v-for="(data, index) in contractorRegisterStore.dataList" :key="index" class="list ctr" :class="{ disabled: data.useYn === 'N' }" @click="focusIndex = index">
                            <button class="df jcsb aic" @click="toggleAccordion">
                                <div class="init df aic gap2rem">
                                    <input type="checkbox" v-input v-model="data.checked" />
                                    <em>{{ data.cmd === 'I' ? data.partCompNm : data.title }}</em>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <div class="pa2rem bcF8F9FB">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-3 lg-grid12-8 us-grid12-12">
                                            <div class="field">
                                                <label :for="`partCompNm${index}`">
                                                    <span>협력업체명</span>
                                                </label>
                                                <i-chips v-if="!data.isCompDirectlyWrite" :chips="[{ id: data.partCompNm, nm: data.partCompNm }]" @popup="addPartner(index)" readonly></i-chips>
                                                <input v-if="data.isCompDirectlyWrite" v-model="data.partCompNm" type="text" class="br4px" :id="`partCompNm${index}`" nreadonly />
                                            </div>
                                        </div>

                                        <div class="grid12-3 lg-grid12-12">
                                            <div class="field">
                                                <label :for="`writeDt${index}`">
                                                    <span>등록일자</span>
                                                </label>
                                                <input v-model="data.regDt" type="text" class="datepicker radius" v-calendar="getDateFormat()" :id="`regDt${index}`" readonly />
                                            </div>
                                        </div>

                                        <div class="grid12-3 lg-grid12-12">
                                            <div class="field">
                                                <label for>품목</label>
                                                <i-chips :chips="[{ id: data.partCompItem, name: data.partCompItemNm }]" readonly></i-chips>
                                            </div>
                                        </div>

                                        <div class="grid12-3 lg-grid12-12">
                                            <div class="field">
                                                <label for>전화번호</label>
                                                <input class="br4px" v-input type="text" id="telNo" v-model="data.telNo" readonly />
                                            </div>
                                        </div>

                                        <div class="grid12-3 lg-grid12-12">
                                            <div class="field">
                                                <label for>우편번호</label>
                                                <div class="pr df aic w100p br4px">
                                                    <input v-input type="text" class="w100p radius search" id="zipCd" v-model="data.zipCd" :readonly="true" />
                                                    <button type="button" class="pa r0 shrink0">
                                                        <img src="/assets/img/common/icon_search.svg" alt />
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="grid12-3 lg-grid12-12">
                                            <div class="field">
                                                <label>주소</label>
                                                <input v-input type="text" class="w100p radius" id="addrs1" v-model="data.addrs1" :readonly="true" />
                                            </div>
                                        </div>

                                        <div class="grid12-6 lg-grid12-12">
                                            <div class="field">
                                                <label for>상세 주소</label>
                                                <input v-input type="text" class="w100p radius br4px" id="addrs1" v-model="data.addrs2" :readonly="true" />
                                            </div>
                                        </div>
                                        <div class="grid10-2 lg-grid12-12">
                                            <div class="field">
                                                <label for>본사 담당자(정)</label>
                                                <i-hrChips :chips="data.headItem" readonly></i-hrChips>
                                            </div>
                                        </div>
                                        <div class="grid10-2 lg-grid12-12">
                                            <div class="field">
                                                <label for>본사 담당자(부)</label>
                                                <i-hrChips :chips="data.subItem" readonly></i-hrChips>
                                            </div>
                                        </div>
                                        <div class="grid10-2 lg-grid12-12">
                                            <div class="field">
                                                <label for>협력사 담당자(정)</label>
                                                <i-hrChips :chips="data.partnerHead" readonly></i-hrChips>
                                            </div>
                                        </div>
                                        <div class="grid10-2 lg-grid12-12">
                                            <div class="field">
                                                <label for>협력사 담당자(부)</label>
                                                <i-hrChips :chips="data.partnerSub" readonly></i-hrChips>
                                            </div>
                                        </div>
                                        <div class="grid10-2 lg-grid12-12">
                                            <div class="field">
                                                <label for>협력사 안전보건 관리 책임자</label>
                                                <i-hrChips :chips="data.partShHr" readonly></i-hrChips>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- <div class="row flex gutters2rem">
                <template v-for="(data, dataIndex) in filteredData" :key="dataIndex">
                <i-card :v-model="data.checked" :data="data" :disabled="data.useYn === 'N'" :type="'basic'" :title="data.trainingNm" :useApprovalStatus="true" :approvalStatus="data.approvalStatus" @editor="btnDetail(data)" />
            </template>
                
        </div>-->
                    <!-- <div class="grid12-4 ul-grid12-6 lg-grid12-12 pt8px">
            <div class="card h100p df aic jcc cp" @click="btnNew(data)">
                <div class="tac">
                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    <span class="fs1-5rem">{{ '신규 등록대장 추가' }}</span>
                </div>
            </div>
        </div>-->
                </div>
                <!-- 추가 -->
                <!-- <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-else>
                <div class="card h100p df aic jcc cp" @click="add">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>

                        <span class="db mt1rem fs2rem c999999">{{ t('card_ContractorRegisterRegister') }}</span>
                    </div>
                </div>
            </div> -->

                <teleport to="body">
                    <!-- 협력사 업체 선택 팝업 -->
                    <i-PopupDialog ref="partnerPopup">
                        <div class="contents w500px md-w100p">
                            <base-select-popup :title="'업체'" filterKey="partCompNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartner" />
                        </div>
                    </i-PopupDialog>
                    <!-- 담당자 인원 선택 팝업 -->
                    <i-PopupDialog ref="managerPopup">
                        <!-- 단일 그리드 -->
                        <div class="contents w500px md-w100p">
                            <selectUser @close="closeManagerPopup" :selected="contractorRegisterStore.dataList[focusIndex]?.hrList?.map(el => el.hrId)" @select="applyManagerPopup"></selectUser>
                            <!-- <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser> -->
                        </div>
                    </i-PopupDialog>
                    <i-PopupDialog ref="itemPopup">
                        <!-- 단일 그리드 -->

                        <div class="contents w500px md-w100p">
                            <SystemPopup :dataList="contractorRegisterStore.dataList" :options="{ label: '거래품목', readonly: false, majorCd: 'C0007' }" @close="closeSystemPopup" />
                            <!-- 버튼 콤포넌트 영역 시작 -->

                            <div class="form ui mt1rem df jcsb">
                                <button type="button" class="btn w7-4rem radius delete">
                                    <span>삭제</span>
                                </button>

                                <div class="df gap8px">
                                    <button type="button" class="btn w7-4rem radius bright-grey">
                                        <span>조회</span>
                                    </button>
                                    <button type="button" class="btn w7-4rem radius active">
                                        <span>저장</span>
                                    </button>
                                    <button type="button" v-button class="btn w7-4rem radius bright-grey" @click="closeSystemPopup">
                                        <span>{{ t('close') }}</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </i-PopupDialog>
                    <i-PopupDialog ref="dialogPostNo">
                        <i-post-code @complete="onPostcodeComplete" @close="closePostcodeDialog" :readonly="true"></i-post-code>
                    </i-PopupDialog>
                </teleport>
                <!-- <i-PopupDialog ref="managerPopup">
        <div class="contents w500px md-w100p">
            <selectUser single @close="closeManagerPopup" @selected="selectPeople"></selectUser>
            <div class="form ui tar mt2-5rem">
                <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeManagerPopup">
                    <span>{{ t('close') }}</span>
                </button>
            </div>
        </div>
    </i-PopupDialog>-->
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref } from 'vue';
import gsap from 'gsap';

import BaseView from '@/components/base/BaseView';
const { t, openLoading, endLoading, getCompId, validationStore, alertMsg, onBeforeMount, formatDate, getCurrentDate, onMounted, router, confirmMsg, nextTick, btnBack, btnSearch, btnDownload, baseDownload } = BaseView();
// 날짜 유틸 함수
import { getDateFormat } from '@/utils/dateUtil.js';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

const uButtonList = ['btnBack', 'btnSearch', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSearch'];

import { useContractorRegisterStore } from '@/stores/safewiz/impl/contractorRegister.js';
const contractorRegisterStore = useContractorRegisterStore();

import _ from 'lodash';
import { getConstractorRegisterList, getConstractorRegisterReport } from '@/stores/safewiz/impl/api/contractorRegisterApi.js';
const focusIndex = ref(-1);
const cmd = ref('I');
onMounted(async () => {
    contractorRegisterStore.writeYear = _.cloneDeep(planningCtrlStore.searchParam.searchText);
    contractorRegisterStore.compId = _.cloneDeep(getCompId());
    searchAction(true, true);
    focusIndex.value = -1;
});

// 서명 -----------------------------------------------
import ISignature from '@/components/common/iSignature.vue';
const signatureComponent = ref();
// 버튼
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
});
const goBack = () => {
    router.push('/PlanningAndControl');
};

btnSearch(async () => {
    if (contractorRegisterStore.dataList && contractorRegisterStore.dataList.length !== 0) {
        // 문서가 있을 때 서명과 함께 조회
        await signatureComponent.value.Search();
    }
    searchAction();
});
const searchAction = notify => {
    planningCtrlStore.searchParam.searchText = contractorRegisterStore.writeYear;
    openLoading();
    let params = {
        compId: getCompId(),
        writeYear: contractorRegisterStore.writeYear,
        useYn: contractorRegisterStore.inputForm.useYn,
        searchText: contractorRegisterStore.inputForm.searchText
    };
    getConstractorRegisterList(params, notify)
        .then(res => {
            if (res.list.length > 0) {
                layoutStore.useBtnList = uButtonList;
                cmd.value = 'U';
            } else {
                layoutStore.useBtnList = iButtonList;
                cmd.value = 'I';
            }
            res.list.forEach(el => {
                el.regDt = formatDate(el.regDt);
            });
            contractorRegisterStore.dataList = _.cloneDeep(res.list);
            currentDataList.value = _.cloneDeep(res.list);
            for (let i = 0; i < contractorRegisterStore.dataList.length; i++) {
                getPartnerDetail(contractorRegisterStore.dataList[i].partCompId).then(res => {
                    if (res.list?.hrListH !== null) {
                        contractorRegisterStore.dataList[i].headItem = res.list.hrListH?.map(el => ({ id: el.hrId, name: el.hrNm, fileId: el.logoId }));
                    } //본사 담당자 (정)
                    if (res.list?.hrListS !== null) {
                        contractorRegisterStore.dataList[i].subItem = res.list.hrListS?.map(el => ({ id: el.hrId, name: el.hrNm, fileId: el.logoId }));
                    } //본사 담당자 (부)
                    if (res.list?.partnerHrListH !== null) {
                        contractorRegisterStore.dataList[i].partnerHead = res.list.partnerHrListH?.map(el => ({ id: el.hrId, name: el.hrNm, fileId: el.logoId }));
                    } //협력사 담당자 (정)
                    if (res.list?.partnerHrListS !== null) {
                        contractorRegisterStore.dataList[i].partnerSub = res.list.partnerHrListS?.map(el => ({ id: el.hrId, name: el.hrNm, fileId: el.logoId }));
                    } //협력사 담당자 (부)
                    contractorRegisterStore.dataList[i].partShHr = [{ fileId: res.list.partShHrImgId, id: res.list.partShHrId, name: res.list.partShHrNm }]; //협력사 담당자 (부)
                });
            }
        })
        .finally(() => {
            endLoading();
        });
};

const isPartCompDuplicated = partComp => {
    // 중복된 사업장 유무를 판단하는 메서드, 기존,신규 데이터들 모두 검사함
    const legacyList = contractorRegisterStore.dataList.filter(el => el.cmd === 'U' && el.docNo !== partComp.docNo);
    const newList = contractorRegisterStore.dataList.filter(el => el.cmd !== 'U' && el.checked);
    if (legacyList.filter(el => el.partCompNm === partComp.partCompNm).length > 0) {
        return true;
    }
    if (newList.filter(el => el.partCompNm === partComp.partCompNm).length > 1) {
        return true;
    }
    return false;
};
btnDownload(() => {
    if (contractorRegisterStore.dataList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const printList = contractorRegisterStore.dataList.filter(el => el.checked);
    let checkedList = [];
    contractorRegisterStore.dataList.forEach(val => {
        if (val.checked == true) {
            checkedList.push(val);
        } else if (printList.length == 0) {
            checkedList.push(val);
        }
    });
    // 선택된 항목이 없을 때 ==> useYn이 Y인 항목만 출력
    // 선택된 항목이 있을 때 ==> useYn관계없이 선택된 항목 출력
    if (checkedList.includes(null) || checkedList.includes(undefined)) {
        alertMsg('warning', t('저장하지 않은 데이터가 있습니다.'));
        return;
    }
    if (printList.length > 0) confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadAction, param: checkedList });
    else confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: checkedList });
});
const downloadAction = checkedList => {
    let nameList = [];
    checkedList.forEach(val => {
        nameList.push({
            partCompId: val.partCompId,
            hrNm: val.headItem[0]?.name,
            partnerNm: val.partnerHead[0]?.name,
            hrSubNm: val.subItem[0]?.name,
            partnerSubNm: val.partnerSub[0]?.name,
            partShHrNm: val.partShHr[0]?.name
        });
    });
    let checkedPartCompId = checkedList.map(el => el.partCompId).join(',');
    checkedList = checkedList.map(el => el.partCompId);
    let searchVO = { writeYear: contractorRegisterStore.writeYear, checkedList: checkedList, searchText: checkedPartCompId, reportNmList: nameList };
    baseDownload(getConstractorRegisterReport, searchVO);
    // openLoading();
    // getConstractorRegisterReport({ writeYear: contractorRegisterStore.writeYear, checkedList: checkedList }, false)
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .catch(() => {
    //         endLoading();
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
};

// **************************************************** 주소 검색
import iPostCode from '@/components/common/iPostCode.vue';
const dialogPostNo = ref(null);
const postNoIndex = ref(0);
const currentDataList = ref([]); //기존 데이터 값
const addPostNoPopup = index => {
    postNoIndex.value = index;
    dialogPostNo.value.onOpen();
};

const onPostcodeComplete = data => {
    contractorRegisterStore.dataList[postNoIndex.value].zipCd = data.zonecode;
    contractorRegisterStore.dataList[postNoIndex.value].addrs1 = data.address;

    dialogPostNo.value.onClose();
};

const closePostcodeDialog = () => {
    dialogPostNo.value.onClose();
};

// **************************************************** 업체 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getPartner, getPartnerDetail } from '@/stores/system/base/api/partnerApi.js';

const partnerPopup = ref(null);
// const addPartner = index => {
//     focusIndex.value = index;
//     partnerPopup.value.onOpen();
// };

// **************************************************** 품목 팝업
import SystemPopup from '@/views/system/base/popup/SystemPopup.vue';

const itemPopup = ref(null);

// **************************************************** 인원 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();

const managerPopup = ref(null);
// const addManager = () => {
//     selectUserStore.currentTab = 'partComp';
//     managerPopup.value.onOpen();
// };

// // 인원 요소 제거(x버튼 클릭)
// const removePeople = () => {
//     if (contractorRegisterStore.dataList[focusIndex.value].useYn === 'Y') {
//         return;
//     }
//     contractorRegisterStore.dataList[focusIndex.value].checked = true;
// };

const chkData = data => {
    data.checked = true;
};
// const applyManagerPopup = () => {
//     const users = selectUserStore.getSelectedUser();
//     console.log('@@ user', users);
//     contractorRegisterStore.dataList[focusIndex.value].hrList = users.map(el => ({
//         id: el.hrId,
//         nm: el.hrNm,
//         hrId: el.hrId,
//         hrNm: el.hrNm
//     }));
//     contractorRegisterStore.dataList[focusIndex.value].checked = true;
//     managerPopup.value.onClose();
//     selectUserStore.currentTab = 'orgn';
// };
// const closeManagerPopup = () => {
//     // console.log('peoplePopup.value', selectUserStore.getSelectedUser())

//     if (managerPopup.value) {
//         managerPopup.value.onClose();
//         selectUserStore.currentTab = 'orgn';
//     }
// };

// const onValueChanged = data => {
//     data.checked = true;
// };
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

const changedUseYn = () => {
    if (contractorRegisterStore.inputForm.useYn == 'Y') {
        contractorRegisterStore.inputForm.useYn = 'N';
    } else {
        contractorRegisterStore.inputForm.useYn = 'Y';
    }
    searchAction();
};

const applyFilter = () => {
    contractorRegisterStore.dataList = currentDataList.value;
    let filterData = contractorRegisterStore.dataList.filter(item => item.partCompNm.includes(contractorRegisterStore.inputForm.searchText.toUpperCase()));
    for (let i = 0; i < filterData.length; i++) {
        getPartnerDetail(filterData[i].partCompId).then(res => {
            filterData[i].headItem = [{ fileId: res.list.hrListH.logoId, id: res.list.hrListH.hrId, name: res.list.hrListH.hrNm }]; //본사 담당자 (정)
            filterData[i].subItem = [{ fileId: res.list.hrListS.logoId, id: res.list.hrListS.hrId, name: res.list.hrListS.hrNm }]; //본사 담당자 (부)
            filterData[i].partnerHead = [{ fileId: res.list.partnerHrListH.logoId, id: res.list.partnerHrListH.hrId, name: res.list.partnerHrListH.hrNm }]; //협력사 담당자 (정)
            filterData[i].partnerSub = [{ fileId: res.list.partnerHrListS.logoId, id: res.list.partnerHrListS.hrId, name: res.list.partnerHrListS.hrNm }]; //협력사 담당자 (부)
            filterData[i].partShHr = [{ fileId: res.list.partShHrImgId, id: res.list.partShHrId, name: res.list.partShHrNm }]; //협력사 담당자 (부)
        });
    }
    contractorRegisterStore.dataList = filterData;
};
</script>
