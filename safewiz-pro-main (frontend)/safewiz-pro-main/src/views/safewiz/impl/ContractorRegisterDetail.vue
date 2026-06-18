<template>
    <div id="form" class="contents df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p pa2-2rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <form ref="myForm">
                <div class="control-field ui form">
                    <div class="row flex gutters2rem">
                        <div class="grid12-6 sm-grid12-6">
                            <div class="field">
                                <label for required>
                                    <span>협력업체명</span>
                                </label>
                                <!-- 직접입력 checkbox 클릭시 input 내용 초기화 하고 직접 작성하게끔 요청하는 것 같습니다.  -->
                                <div class="df gap2rem aic">
                                    <i-chips></i-chips>
                                    <input class="w20p" type="checkbox" v-input="'직접입력'" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for required>
                                    <span>작성일자</span>
                                </label>
                                <input class="br4px datepicker" v-calendar type="text" />
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-6">
                            <div class="field">
                                <label for> 담당자</label>
                                <!-- 직접입력 checkbox 클릭시 input 내용 초기화 하고 직접 작성하게끔 요청하는 것 같습니다.  -->
                                <div class="df gap2rem aic">
                                    <i-chips></i-chips>
                                    <input class="w20p" type="checkbox" v-input="'직접입력'" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>작성일자</label>
                                <i-chips></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>전화번호</label>
                                <input class="br4px" v-input type="text" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>우편번호</label>
                                <i-chips></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>주소</label>
                                <input class="br4px" v-input type="text" readonly />
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-6">
                            <div class="field">
                                <label for>상세 주소</label>
                                <input class="br4px" v-input type="text" />
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label for="">비고</label>
                                <textarea class="minh10rem br4px"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <!-- 인원 검색 팝업 컴포넌트 시작 (단일선택) -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>

                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->

            <!-- 인원 검색 팝업 컴포넌트 시작 (멀티선택) -->
            <i-PopupDialog ref="multiPeoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser @close="closeMultiPeoplePopup"></selectUser>

                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeMultiPeoplePopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { ref, onMounted /* nextTick */ } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
// import router from '@/router';

// 스토어
import { useJobCompAssessStore } from '@/stores/safewiz/support/JobCompAssess.js';
const jobCompAssessStore = useJobCompAssessStore();

import { useWorkerHealthMgmtStore } from '@/stores/safewiz/impl/workerHealthMgmt.js';
const workerHealthMgmtStore = useWorkerHealthMgmtStore();
// =====================================================

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const { t, validationStore, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload /* formatDate, watch, calculateMinutes, toastPopup, getCompId */ } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];

// =====================================================

const signatureComponent = ref(null);
onMounted(async () => {
    jobCompAssessStore.signature = signatureComponent.value;
    // if (!workerHealthMgmtStore.inputForm.cmd) {
    //     // 새로고침시 이전 화면으로 이동.
    //     router.go(-1);
    //     return;
    // }
    if (workerHealthMgmtStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
        workerHealthMgmtStore.inputForm.counselDt = workerHealthMgmtStore.currentDate;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});

btnBack(() => {
    confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: workerHealthMgmtStore.goBack });
});

btnAdd(() => {
    confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: workerHealthMgmtStore.btnAdd });
});

btnSearch(() => {
    workerHealthMgmtStore.getWHMgmtGuideDetailList();
});
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    // console.log('유효성', form);
    if (isValid) {
        await workerHealthMgmtStore.btnSave();
    }
});
btnDelete(() => {
    workerHealthMgmtStore.btnDetailDelete();
});
btnDownload(() => {
    if (workerHealthMgmtStore.inputForm.cmd === 'U') {
        let list = [workerHealthMgmtStore.inputForm.docNo];
        workerHealthMgmtStore.btnDetailDownload(list);
    } else {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
    }
});
//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref

// const addPeople = idx => {
//     console.log('팝업열기', peopleIdx.value);
//     if (workerHealthMgmtStore.inputForm.cmd !== 'U') {
//         peoplePopup.value.onOpen();
//     }
// };

//인원 팝업 선택
const selectPeople = e => {
    console.log('selectPeople', e);

    workerHealthMgmtStore.inputForm.hrId = e.hrId;
    workerHealthMgmtStore.inputForm.hrNm = e.hrNm;
    workerHealthMgmtStore.inputForm.sex = e.sexDiv === 'M' ? '남' : e.sexDiv === 'W' ? '여' : e.sexDiv;
    workerHealthMgmtStore.inputForm.orgnId = e.orgnId;
    workerHealthMgmtStore.inputForm.orgnNm = e.orgnNm;
    if (e.birthDt) {
        workerHealthMgmtStore.inputForm.age = calculateAge(e.birthDt);
    }
    if (e.workingAt) {
        workerHealthMgmtStore.inputForm.serviceYears = calculateYearsOfService(e.workingAt);
    }

    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업 (멀티)]

const multiPeoplePopup = ref(null); // PopupDialog에 대한 ref
const peopleIdx = ref('');

// const addMultPeople = idx => {
//     peopleIdx.value = idx;
//     console.log('팝업열기', peopleIdx.value);
//     // 팝업 오픈시 자동 체크
//     if (peopleIdx.value === '1') {
//         workerHealthMgmtStore.inputForm.counselChecked = 'Y';
//     } else if (peopleIdx.value === '2') {
//         workerHealthMgmtStore.inputForm.requestChecked = 'Y';
//     } else if (peopleIdx.value === '3') {
//         workerHealthMgmtStore.inputForm.requestChecked = 'Y';
//     }

//     multiPeoplePopup.value.onOpen();
// };

const closeMultiPeoplePopup = () => {
    if (multiPeoplePopup.value) {
        multiPeoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();

        console.log('유저', workerHealthMgmtStore.inputForm);
        if (peopleIdx.value == 1) {
            users.forEach(user => {
                workerHealthMgmtStore.inputForm.hrListH.push({
                    id: user.hrId,
                    name: user.hrNm,
                    hrId: user.hrId,
                    hrNm: user.hrNm,
                    targetType: 'WHMG'
                });
            });
        } else if (peopleIdx.value == 2) {
            users.forEach(user => {
                workerHealthMgmtStore.inputForm.hrListR.push({
                    id: user.hrId,
                    name: user.hrNm,
                    hrId: user.hrId,
                    hrNm: user.hrNm,
                    targetType: 'WHMG'
                });
            });
        } else if (peopleIdx.value == 3) {
            users.forEach(user => {
                workerHealthMgmtStore.inputForm.hrListC.push({
                    id: user.hrId,
                    name: user.hrNm,
                    hrId: user.hrId,
                    hrNm: user.hrNm,
                    targetType: 'WHMG'
                });
            });
        }
    }
};
//-----------------------------------------------
//-----------------------------------------------
// 근속년수를 계산하는 함수
const calculateYearsOfService = workingAt => {
    const startDate = new Date(parseInt(workingAt.substring(0, 4)), parseInt(workingAt.substring(4, 6)) - 1, parseInt(workingAt.substring(6, 8)));
    const today = new Date();
    const yearsOfService = today.getFullYear() - startDate.getFullYear();

    // 정확한 년수를 계산하기 위해 월과 일을 비교
    const hasHadAnniversary = today.getMonth() > startDate.getMonth() || (today.getMonth() === startDate.getMonth() && today.getDate() >= startDate.getDate());

    return hasHadAnniversary ? yearsOfService : yearsOfService - 1;
};

//-----------------------------------------------
//-----------------------------------------------
// 나이를 계산하는 함수
const calculateAge = birthDt => {
    const birthDate = new Date(parseInt(birthDt.substring(0, 4)), parseInt(birthDt.substring(4, 6)) - 1, parseInt(birthDt.substring(6, 8)));
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();

    // 정확한 나이를 계산하기 위해 월과 일을 비교
    const hasHadBirthdayThisYear = today.getMonth() > birthDate.getMonth() || (today.getMonth() === birthDate.getMonth() && today.getDate() >= birthDate.getDate());

    return hasHadBirthdayThisYear ? age : age - 1;
};

//-----------------------------------------------
//-----------------------------------------------

// 자동체크함수
// const chkData = field => {
//     if (field === 'counsel') {
//         workerHealthMgmtStore.inputForm.counselChecked = 'Y';
//     } else {
//         workerHealthMgmtStore.inputForm.requestChecked = 'Y';
//     }
// };

//-----------------------------------------------

//시간 체크
// const onDateInput = (index, event) => {
//     workerHealthMgmtStore.inputForm.requestChecked = 'Y';
//     workerHealthMgmtStore.inputForm.counselTime = event.target.value; // 실제 데이터는 YYYYMMDD 형식
// };

//-----------------------------------------------
</script>
