<template>
    <div class="contents">
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
            <div class="bcFFFFFF br4px bd1pxsolidE1E6ED">
                <form ref="myForm">
                    <div class="control-field ui form pa2-2rem" id="form">
                        <!-- <div class="field">
                            <label for>근로자 건강 상담일지</label>
                        </div>-->

                        <div class="row flex gutters1rem">
                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label required>
                                        <span>근로자</span>
                                    </label>
                                    <i-chips :chips="[{ id: workerHealthMgmtStore.inputForm.hrId, name: workerHealthMgmtStore.inputForm.hrNm }]" @click="addPeople" :readonly="workerHealthMgmtStore.inputForm.cmd === 'U'" required></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label for>조직</label>
                                    <input class="br4px" v-model="workerHealthMgmtStore.inputForm.orgnNm" v-input type="text" />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-4">
                                <div class="field">
                                    <label for>성별</label>
                                    <input class="br4px" v-model="workerHealthMgmtStore.inputForm.sexDivNm" v-input type="text" readonly />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-4">
                                <div class="field">
                                    <label for>나이</label>
                                    <input class="br4px" v-model="workerHealthMgmtStore.inputForm.age" v-input type="text" />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-4">
                                <div class="field">
                                    <label for>근속연수</label>
                                    <input class="br4px" v-model="workerHealthMgmtStore.inputForm.serviceYears" v-input type="text" />
                                </div>
                            </div>
                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label for="counselDt" required>
                                        <span>상담일자</span>
                                    </label>
                                    <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" v-model="workerHealthMgmtStore.inputForm.counselDt" id="counselDt" required :readonly="workerHealthMgmtStore.inputForm.cmd === 'U'" />
                                </div>
                            </div>
                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :checked="workerHealthMgmtStore.inputForm.useYn === 'Y'" v-input type="checkbox" class="df switch" @change="workerHealthMgmtStore.toggleUseYn('useYn', $event)" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mt2-2rem pa2-2rem bcF9FAFF br4px">
                            <div class="field">
                                <label for>근로자 건강 상담일지</label>
                            </div>
                            <div class="df fww br4px bd1pxsolidE1E6ED bcFFFFFF">
                                <div class="df aic jcc w5rem bdr1pxsolidE1E6ED shrink0 sm-w100p sm-h5rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000" :class="[{ selected: workerHealthMgmtStore.inputForm.counselChecked }, { disabled: workerHealthMgmtStore.inputForm.counselUseYn == 'N' }]">
                                    <input type="checkbox" v-input :checked="workerHealthMgmtStore.inputForm.counselChecked === 'Y'" @change="workerHealthMgmtStore.inputForm.counselChecked = $event.target.checked ? 'Y' : 'N'" />
                                </div>
                                <div class="w100pcalc5rem sm-w100p">
                                    <div class="pa2rem">
                                        <div class="row flex gutters1rem">
                                            <div class="grid12-9 es-grid12-6">
                                                <div class="field">
                                                    <label required>
                                                        <span>상담자</span>
                                                    </label>
                                                    <i-chips :chips="workerHealthMgmtStore.inputForm.hrListH" @popup="addMultPeople('1')" :required="workerHealthMgmtStore.inputForm.counselChecked == 'Y'" />
                                                </div>
                                            </div>

                                            <div class="grid12-3 es-grid12-6">
                                                <div class="field">
                                                    <label for="counselUseYn">사용여부</label>
                                                    <div class="df aic h4-4rem">
                                                        <input :checked="!workerHealthMgmtStore.inputForm.counselUseYn ? true : workerHealthMgmtStore.inputForm.counselUseYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" id="counselUseYn" @input="chkData('counsel')" @change="workerHealthMgmtStore.toggleUseYn('counselUseYn', $event)" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>검진소견</label>
                                                    <textarea name id v-model="workerHealthMgmtStore.inputForm.healthOpinion" class="radius minh10rem" placeholder="내용을 입력하세요." @input="chkData('counsel')"></textarea>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>사후관리 소견</label>
                                                    <textarea name id v-model="workerHealthMgmtStore.inputForm.aftercareOpinion" class="radius minh10rem" placeholder="내용을 입력하세요." @input="chkData('counsel')"></textarea>
                                                </div>
                                            </div>

                                            <div class="grid12-3 lg-grid12-12">
                                                <div class="field">
                                                    <label for>업무수행 적합여부</label>
                                                    <select name id v-select v-model="workerHealthMgmtStore.inputForm.workAptitudeYn" @change="chkData('counsel')">
                                                        <option v-for="item in workerHealthMgmtStore.filteredYN" :key="item.workAptitudeYn" :value="item.workAptitudeYn">{{ item.workAptitudeYnNm }}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="grid10-10">
                                                <div class="field">
                                                    <label for>조치사항</label>
                                                    <textarea name id v-model="workerHealthMgmtStore.inputForm.correctiveAction" class="radius minh10rem" placeholder="내용을 입력하세요." @input="chkData('counsel')"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br />
                            <div class="field wsn">
                                <label for>근로자 보건 및 작업환경 개선요청 상담일지</label>
                            </div>

                            <div class="df fww br4px bd1pxsolidE1E6ED bcFFFFFF">
                                <div class="df aic jcc w5rem bdr1pxsolidE1E6ED shrink0 sm-w100p sm-h5rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000" :class="[{ selected: workerHealthMgmtStore.inputForm.requestChecked }, { disabled: workerHealthMgmtStore.inputForm.requestUseYn == 'N' }]">
                                    <input type="checkbox" v-input :checked="workerHealthMgmtStore.inputForm.requestChecked === 'Y'" @change="workerHealthMgmtStore.inputForm.requestChecked = $event.target.checked ? 'Y' : 'N'" />
                                </div>

                                <div class="w100pcalc5rem sm-w100p">
                                    <div class="pa2rem">
                                        <div class="row flex gutters1rem">
                                            <div class="grid10-2 md-grid12-6">
                                                <div class="field">
                                                    <label required>
                                                        <span>상담자</span>
                                                    </label>
                                                    <i-chips :chips="workerHealthMgmtStore.inputForm.hrListR" @popup="addMultPeople('2')" :required="workerHealthMgmtStore.inputForm.requestChecked == 'Y'" />
                                                </div>
                                            </div>

                                            <div class="grid10-2 md-grid12-6">
                                                <div class="field">
                                                    <label required>
                                                        <span>내담자</span>
                                                    </label>
                                                    <i-chips :chips="workerHealthMgmtStore.inputForm.hrListC" @popup="addMultPeople('3')" :required="workerHealthMgmtStore.inputForm.requestChecked == 'Y'"></i-chips>
                                                </div>
                                            </div>

                                            <div class="grid10-2 md-grid12-6 sm-grid12-12">
                                                <div class="field">
                                                    <label for="useYn">상담시간</label>

                                                    <input v-input startTime endTime type="text" v-calendar="''" :value="workerHealthMgmtStore.inputForm.counselTime" @input="onDateInput(index, $event)" class="datepicker w100p br4px" placeholder="09:30 ~ 11:00" id />
                                                    <!-- <span
                                                class="fs2rem"
                                                    >{{calculateMinutes(workerHealthMgmtStore.inputForm.counselTime)}}(분)</span>-->
                                                </div>
                                            </div>

                                            <div class="grid10-2 md-grid12-6 sm-grid12-12">
                                                <div class="field">
                                                    <label for="counselLocation">장소</label>
                                                    <input class="br4px" v-model="workerHealthMgmtStore.inputForm.counselLocation" v-input type="text" @input="chkData('request')" />
                                                </div>
                                            </div>
                                            <div class="grid10-2 md-grid12-6 sm-grid12-12">
                                                <div class="field">
                                                    <label for="requestUseYn">사용여부</label>
                                                    <div class="df aic h4-4rem">
                                                        <input :checked="!workerHealthMgmtStore.inputForm.requestUseYn ? true : workerHealthMgmtStore.inputForm.requestUseYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" id="requestUseYn" @input="chkData('request')" @change="workerHealthMgmtStore.toggleUseYn('requestUseYn', $event)" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="grid10-10">
                                                <div class="field">
                                                    <label for>상담경위</label>
                                                    <textarea name id class="radius minh10rem" @input="chkData('request')" v-model="workerHealthMgmtStore.inputForm.counselReason" placeholder="내용을 입력하세요."></textarea>
                                                </div>
                                            </div>

                                            <div class="grid10-10">
                                                <div class="field">
                                                    <label for>상담내용</label>
                                                    <textarea name id class="radius minh10rem" @input="chkData('request')" v-model="workerHealthMgmtStore.inputForm.counselContent" placeholder="내용을 입력하세요."></textarea>
                                                </div>
                                            </div>

                                            <div class="grid10-10">
                                                <div class="field">
                                                    <label for>상담결과</label>
                                                    <textarea name id class="radius minh10rem" @input="chkData('request')" v-model="workerHealthMgmtStore.inputForm.counselResult" placeholder="내용을 입력하세요."></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="field mt2rem">
                            <div class="bd1pxsolidE1E6ED br4px pa2rem bcF1F3F8">
                                <iFileList ref="fileList" targetType="WHMG" :targetId="workerHealthMgmtStore.inputForm.files" />
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </OverlayScrollbarsComponent>
        <!-- 인원 검색 팝업 컴포넌트 시작 (단일선택) -->
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->

            <!-- 인원 검색 팝업 컴포넌트 시작 (멀티선택) -->
            <i-PopupDialog ref="multiPeoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" @selected="selectMultiPeoplePopup" @close="closeMultiPeoplePopup" :selected="peopleIdx === '1' ? workerHealthMgmtStore.inputForm?.hrListH?.map(el => el.hrId) : peopleIdx === '2' ? workerHealthMgmtStore.inputForm?.hrListR?.map(el => el.hrId) : peopleIdx === '3' ? workerHealthMgmtStore.inputForm?.hrListC?.map(el => el.hrId) : []"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
        <!-- 인원 검색 팝업 컴포넌트 끝  -->
    </div>
</template>

<script setup>
import { ref, toRaw, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { getDateFormat } from '@/utils/dateUtil.js';
import IFileList from '@/components/file/iFileList.vue';
import _ from 'lodash';
import { useWorkerHealthMgmtStore } from '@/stores/safewiz/impl/workerHealthMgmt.js';
const workerHealthMgmtStore = useWorkerHealthMgmtStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const { t, validationStore, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, formatDateForDB } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];

// 파일 업로드 컴포넌트
const fileList = ref();

import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보

onMounted(async () => {
    if (!workerHealthMgmtStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.push('WorkerHealthMgmtGuidelines');
        return;
    }
    if (workerHealthMgmtStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
        workerHealthMgmtStore.inputForm.counselDt = workerHealthMgmtStore.currentDate;
        workerHealthMgmtStore.inputForm.hrListH = [{ hrId: userInfoStore.userId, hrNm: userInfoStore.userName, id: userInfoStore.userId, name: userInfoStore.userName, targetType: 'WHMG' }];
        workerHealthMgmtStore.inputForm.hrListR = [{ hrId: userInfoStore.userId, hrNm: userInfoStore.userName, id: userInfoStore.userId, name: userInfoStore.userName, targetType: 'WHMG' }];
    } else {
        const res = await workerHealthMgmtStore.getWHMgmtGuideDetailList(true);
        if (res) {
            fileList.value.fnSearch();
            layoutStore.useBtnList = uButtonList;
        }
    }
});

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(workerHealthMgmtStore.originData);
    const currentData = toRaw(workerHealthMgmtStore.inputForm);
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

btnBack(() => {
    if(!_.isEqual(workerHealthMgmtStore.inputForm, workerHealthMgmtStore.currentInputForm)){
        handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', workerHealthMgmtStore.goBack);
    }else{
        workerHealthMgmtStore.goBack()
    }
});

btnAdd(() => {
    if(!_.isEqual(workerHealthMgmtStore.inputForm, workerHealthMgmtStore.currentInputForm)){
        handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', workerHealthMgmtStore.btnAdd);
    }else{
        workerHealthMgmtStore.btnAdd()
    }
});

btnSearch(() => {
    if(!_.isEqual(workerHealthMgmtStore.inputForm, workerHealthMgmtStore.currentInputForm)){
        handleDataChange('저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', searchAction);
    }else{
        searchAction()
    }
});
const searchAction = () => {
    const res = workerHealthMgmtStore.getWHMgmtGuideDetailList();
    if (res) {
        fileList.value.fnSearch();
    }
};
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        const duplicatedHr = await workerHealthMgmtStore.searchedData.some(hr => hr.hrId === workerHealthMgmtStore.inputForm.hrId && hr.counselDt === formatDateForDB(workerHealthMgmtStore.inputForm.counselDt));
        if (duplicatedHr && workerHealthMgmtStore.inputForm.cmd === 'I') {
            alertMsg('warning', `[${workerHealthMgmtStore.inputForm.counselDt}][${workerHealthMgmtStore.inputForm.hrNm}] \n 해당 날짜에 이미 등록된 인원입니다.`);
            return;
        }
        confirmMsg('info', t('msgSave'), null, { fun: saveAction, param: null });
    }
});
const saveAction = async () => {
    workerHealthMgmtStore.fileInfo = fileList.value;
    const saveRes = await workerHealthMgmtStore.btnSave();
    if (saveRes) {
        const searchRes = await workerHealthMgmtStore.getWHMgmtGuideDetailList(false);
        if (searchRes) fileList.value.fnSearch();
    }
};
btnDelete(() => {
    workerHealthMgmtStore.btnDetailDelete();
});
btnDownload(() => {
    if (workerHealthMgmtStore.inputForm.cmd === 'U') {
        let list = [workerHealthMgmtStore.inputForm.docNo];
        if(!_.isEqual(workerHealthMgmtStore.inputForm, workerHealthMgmtStore.currentInputForm)){
            handleDataChange('저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', workerHealthMgmtStore.btnDetailDownload, list);
        }else{
            handleDataChange('출력하시겠습니까?', workerHealthMgmtStore.btnDetailDownload, list);
        }
    }else{
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
        return
    }
});
//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref

const addPeople = idx => {
    if (workerHealthMgmtStore.inputForm.cmd !== 'U') {
        peoplePopup.value.onOpen();
    }
};

//인원 팝업 선택
const selectPeople = e => {
    workerHealthMgmtStore.inputForm.hrId = e.hrId;
    workerHealthMgmtStore.inputForm.hrNm = e.hrNm;
    workerHealthMgmtStore.inputForm.hrListC = [{ id: e.hrId, name: e.hrNm, hrId: e.hrId, hrNm: e.hrNm, targetType: 'WHMG' }];

    workerHealthMgmtStore.inputForm.sex = e.sexDiv;
    workerHealthMgmtStore.inputForm.sexDivNm = e.sexDiv === 'M' ? t('male') : t('female');
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

const addMultPeople = idx => {
    peopleIdx.value = idx;
    if (peopleIdx.value == 1) {
        workerHealthMgmtStore.inputForm.counselChecked = 'Y';
    } else if (peopleIdx.value == 2 || peopleIdx.value == 3) {
        workerHealthMgmtStore.inputForm.requestChecked = 'Y';
    }

    multiPeoplePopup.value.onOpen();
};

const closeMultiPeoplePopup = () => {
    if (multiPeoplePopup.value) {
        multiPeoplePopup.value.onClose();
    }
};

const selectMultiPeoplePopup = () => {
    if (multiPeoplePopup.value) {
        multiPeoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();

        console.log('유저', workerHealthMgmtStore.inputForm);
        if (peopleIdx.value == 1) {
            workerHealthMgmtStore.inputForm.hrListH = users.map(user => ({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm,
                targetType: 'WHMG'
            }));
        } else if (peopleIdx.value == 2) {
            workerHealthMgmtStore.inputForm.hrListR = users.map(user => ({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm,
                targetType: 'WHMG'
            }));
        } else if (peopleIdx.value == 3) {
            workerHealthMgmtStore.inputForm.hrListC = users.map(user => ({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm,
                targetType: 'WHMG'
            }));
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
    // const hasHadAnniversary = today.getMonth() > startDate.getMonth() || (today.getMonth() === startDate.getMonth() && today.getDate() >= startDate.getDate());

    // return hasHadAnniversary ? yearsOfService : yearsOfService - 1;
    return yearsOfService;
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
const chkData = field => {
    if (field === 'counsel') {
        workerHealthMgmtStore.inputForm.counselChecked = 'Y';
    } else {
        workerHealthMgmtStore.inputForm.requestChecked = 'Y';
    }
};

//-----------------------------------------------

//시간 체크
const onDateInput = (index, event) => {
    workerHealthMgmtStore.inputForm.requestChecked = 'Y';
    workerHealthMgmtStore.inputForm.counselTime = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};

const removeHrChips = removeIndex => {
    // workerHealthMgmtStore.inputForm.hrListH.splice(removeIndex, 1);
};

//-----------------------------------------------
</script>
