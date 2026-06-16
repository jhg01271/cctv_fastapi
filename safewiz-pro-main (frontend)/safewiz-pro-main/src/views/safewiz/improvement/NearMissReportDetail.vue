<template>
    <!-- NearMissReport상세 -->
    <div class="contents">
        <div v-if="nearStore && nearStore.inputForm" id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2rem pb10rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div class="df jcsb mb1-2rem">
                        <div class="field">
                            <div class="field">
                                <label for="">작성년도</label>
                                <input :value="nearStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>
                        <div class="field">
                            <label>사용여부</label>
                            <div class="h4-4rem aic">
                                <input v-input="'사용'" type="checkbox" class="df switch" :checked="nearStore.inputForm.useYn === 'Y'" @change="nearStore.toggleUseYn" />
                            </div>
                        </div>
                    </div>
                    <div class="row flex gutters1rem">
                        <!--grid12-ㅁ <<요소 길이 -->
                        <div class="grid12-6 ul-grid12-12">
                            <div class="pa2rem bd1pxsolide1e6ed br4px">
                                <div class="w100p field">
                                    <label>
                                        <span>작성조직</span>
                                    </label>
                                </div>
                                <div class="row flex gutters1rem">
                                    <div class="grid12-5 ul-grid12-8 lg-grid12-12">
                                        <div class="field">
                                            <!-- <input v-model="userStore.userOrgnNm" v-input type="text" class="w100p radius" required readonly oninvalid="return false;" oninput="return false;" placeholder="작성조직명 표시" /> -->
                                            <input v-model="nearStore.inputForm.creatOrgnNm" v-input type="text" class="w100p radius" required readonly oninvalid="return false;" oninput="return false;" placeholder="작성조직명 표시" />
                                        </div>
                                        <div class="pt1rem">
                                            <!-- <input v-model="userStore.userName" v-input type="text" class="w100p radius" required readonly oninvalid="return false;" oninput="return false;" placeholder="작성자명 표시" /> -->
                                            <input v-model="nearStore.inputForm.hrNm" v-input type="text" class="w100p radius" required readonly oninvalid="return false;" oninput="return false;" placeholder="작성자명 표시" />
                                        </div>
                                        <div class="pt1rem field df">
                                            <label required class="minw6rem">
                                                <span>작성일</span>
                                            </label>
                                            <input required id="작성일" v-input v-model="nearStore.inputForm.writeDt" type="text" :min="nearStore.searchParam.writeYear + '.01.01'" :max="nearStore.searchParam.writeYear + '.12.31'" v-calendar="getDateFormat()" @update:model-value="dateValidate('writeDt')" class="datepicker w100p br4px" />
                                        </div>
                                    </div>
                                    <div class="grid12-7 ul-grid12-4 lg-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="el-shrink0 lg-w30rem es-w100p">
                                            <ISignature ref="creatSignatureComponent" :cmd="nearStore.inputForm.cmd" :types="signatureType" targetType="NMR" type="CREAT_NEAR" :is-writer="nearStore.inputForm.cmd === 'I'" :writeYear="nearStore.inputForm.writeYear" :docNo="nearStore.inputForm.docNo" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 ul-grid12-12">
                            <div class="pa2rem bd1pxsolidE1E6ED br4px">
                                <div class="w100p field">
                                    <label>조치조직</label>
                                </div>
                                <div class="row flex gutters1rem">
                                    <div class="grid12-5 ul-grid12-8 lg-grid12-12">
                                        <div class="field">
                                            <div class="field df">
                                                <i-chips class="minw10rem" :chips="nearStore.orgnItem" @popup="addOrgn"></i-chips>
                                            </div>
                                        </div>
                                        <div class="py1rem field df">
                                            <label class="minw6rem">접수일</label>
                                            <input v-input v-model="nearStore.inputForm.receiptDt" type="text" v-calendar="getDateFormat()" @update:model-value="dateValidate('receiptDt')" class="datepicker w100p br4px" />
                                        </div>
                                        <div class="field df">
                                            <label class="minw6rem">완료일</label>
                                            <input v-input v-model="nearStore.inputForm.actionDt" type="text" v-calendar="getDateFormat()" @update:model-value="dateValidate('actionDt')" class="datepicker w100p br4px" />
                                        </div>
                                    </div>
                                    <div class="grid12-7 ul-grid12-4 lg-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="el-shrink0 lg-w30rem es-w100p">
                                            <ISignature class="minw20px" ref="actionSignatureComponent" :cmd="nearStore.inputForm.cmd" :types="signatureType1" targetType="NMR" type="ACTION_NEAR" :is-writer="false" :writeYear="nearStore.inputForm.writeYear" :docNo="nearStore.inputForm.docNo" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row flex gutters2rem">
                        <!--grid12-ㅁ <<요소 길이 -->
                        <div class="grid12-12">
                            <div class="field mt2-2rem bcF9FAFF pa2-2rem br4px">
                                <label for="NM">
                                    <span>아차사고</span>
                                </label>
                                <textarea v-model="nearStore.inputForm.accidentContent" v-input type="text" id="NM" class="w100p radius minh10rem" oninvalid="return false;" oninput="return false;" placeholder="아차사고 내용(누가/언제/어디서/무엇을/어떻게...)"></textarea>

                                <label for="IMSBTA-first">
                                    <span>작성자가 생각하는 개선대책</span>
                                </label>
                                <textarea v-model="nearStore.inputForm.accidentImproved" v-input type="text" id="IMSBTA-first" class="w100p radius minh10rem" oninvalid="return false;" oninput="return false;" placeholder="개선대책 내용을 입력하세요."></textarea>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="grid12-12">
                                <div class="field bcF9FAFF pa2-2rem br4px">
                                    <label for="HHD">
                                        <span>유해위험발굴</span>
                                    </label>
                                    <textarea v-model="nearStore.inputForm.dangerContent" rows="4" v-input type="text" id="HHD" class="w100p radius minh10rem" oninvalid="return false;" oninput="return false;" placeholder="1) 유해위험대상(설비/기계, 장소 등) &#10;2) 내용:"></textarea>

                                    <label for="IMSBTA-second">
                                        <span>작성자가 생각하는 개선대책</span>
                                    </label>
                                    <textarea v-model="nearStore.inputForm.dangerImproved" v-input type="text" id="IMSBTA-second" class="w100p radius minh10rem" oninvalid="return false;" oninput="return false;" placeholder="개선대책 내용을 입력하세요."></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label>참고사항(사진 등)</label>
                                <!-- 첨부파일 -->
                                <div class="bd1pxsolidE1E6ED br4px pa2rem">
                                    <iFileList ref="fileList" targetType="NEAR" :targetId="nearStore.inputForm.files" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="grid12-12">
                                <div class="field">
                                    <label>
                                        <span>조치결과(조치조직)</span>
                                    </label>
                                    <textarea v-model="nearStore.inputForm.actionContent" v-input type="text" class="w100p radius minh10rem" oninvalid="return false;" oninput="return false;" placeholder="개선대책 내용을 입력하세요."></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <teleport to="body">
                <i-PopupDialog ref="orgnPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :title="'조직'" :inputForm="nearStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                        <!-- 버튼 콤포넌트 영역 시작 -->
                    </div>
                </i-PopupDialog>
            </teleport>
        </div>
    </div>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import BaseView from '@/components/base/BaseView';
const { ref, validationStore, onMounted, t, alertMsg, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, confirmMsg, formatDate, formatDateForDB, setRouterParam, saveSignAsync } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { useButtonListStore } from '@/stores/buttonList';
import ISignature from '@/components/common/iSignature.vue'; //서명 컴포넌트
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getDateFormat } from '@/utils/dateUtil.js';

import { insertNearMissReport, updateNearMissReport, getOrgnHead } from '@/stores/safewiz/improvement/api/nonconformityCorrectiveApi';
//store
import { useNearMissReportStore } from '@/stores/safewiz/improvement/nearMissReport.js';
const nearStore = useNearMissReportStore();

// 사용자 정보
import { useUserInfoStore } from '@/stores/user.js';

import { useNonconformityCStore } from '@/stores/safewiz/improvement/nonconformityCorrective.js';
const nonconformityCStore = useNonconformityCStore();

import _ from 'lodash';

// loading panel
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
import { useRoute } from 'vue-router';
import { useCorrectiveActionRequestStore } from '@/stores/safewiz/improvement/correctiveActionRequest.js';

const userInfoStore = useUserInfoStore(); // 현재 사용자 정보
const loadingPanelStore = useLoadingPanelStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = [];
const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const creatSignatureComponent = ref(); //작성조직 서명 컴포넌트
const actionSignatureComponent = ref(); //조치조직 서명 컴포넌트
const orgnPopup = ref(null);
//파일
const fileList = ref();

const fileInfo = file => {
    fileInfo.value = file.value;
};

const route = useRoute();

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        const res = nearStore.btnDetail(param.docNo);
        if (res) {
            await creatSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
            await actionSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        }
        layoutStore.useBtnList = uButtonList;
    } else if (!nearStore.inputForm || !nearStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('NearMissReport');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }

    // 신규 입력 시
    if (!nearStore.inputForm.docNo) {
        nearStore.initInputForm();
    }

    nearStore.setRefs(orgnPopup);
    fileInfo(fileList);
    //파일조회
    fileList.value.fnSearch();
});

// //input창 v-model

const addOrgn = () => {
    orgnPopup.value.onOpen();
};

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    if (isChange()) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: searchAction, param: '' });
    } else {
        searchAction();
    }
});

const searchAction = async () => {
    loadingPanelStore.openLoading();

    await creatSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await actionSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    nearStore.btnDetail(nearStore.inputForm.docNo);
    fileList.value.fnSearch();
};

btnBack(() => {
    if (isChange()) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
    } else {
        goBack();
    }

    //router.go(-1);
});

const goBack = () => {
    nonconformityCStore.searchParam.writeYear = nearStore.searchParam.writeYear;
    router.push('/NearMissReport');
    // fileList.value.fnRemove();
    nearStore.btnSearch();
};

//저장 버튼 클릭 이벤트
btnSave(async () => {
    loadingPanelStore.openLoading();
    const isValid = await validationStore.validateAllFields('form', true);
    nearStore.inputForm.writeYear = nearStore.searchParam.writeYear;
    nearStore.inputForm.writeDt = nearStore.inputForm.writeDt ? formatDateForDB(nearStore.inputForm.writeDt) : null;
    nearStore.inputForm.receiptDt = nearStore.inputForm.receiptDt ? formatDateForDB(nearStore.inputForm.receiptDt) : null;
    nearStore.inputForm.actionDt = nearStore.inputForm.actionDt ? formatDateForDB(nearStore.inputForm.actionDt) : null;
    let editFiles = fileInfo.value.editFiles;
    if (isValid) {
        if (nearStore.orgnItem.length !== 0) {
            nearStore.inputForm.actionOrgnId = nearStore.orgnItem[0].id;
            nearStore.inputForm.actionOrgnNm = nearStore.orgnItem[0].name;
        } else {
            nearStore.inputForm.actionOrgnId = '';
            nearStore.inputForm.actionOrgnNm = '';
        }
        const formData = new FormData();
        nearStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
        await formData.append('info', new Blob([JSON.stringify(nearStore.inputForm)], { type: 'application/json' }));
        formData.deleteFiles = editFiles.delete;
        fileList.value.editFiles.insert.forEach(file => {
            if (file) {
                formData.append('files', file); // 파일이 있을 경우 파일 추가
            } else {
                formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
            }
        });
        //조치 조직의 조직 데이터 있을 경우에만 데이터 삽입
        await confirmMsg('info', t('msgSave'), '', { fun: insertReportData, param: formData });

        loadingPanelStore.endLoading();
    }
});

const insertReportData = async param => {
    if (nearStore.inputForm.cmd === 'I') {
        insertNearMissReport(param, true).then(async res => {
            if (res && res.success) {
                nearStore.inputForm.docNo = res.result.docNo;

                const refList = [
                    {
                        ref: creatSignatureComponent.value,
                        docType: 'NMR',
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    },
                    {
                        ref: actionSignatureComponent.value,
                        docType: 'NMR',
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    }
                ];

                let saveSignSuccess = await saveSignAsync('iSignature', refList);
                if (saveSignSuccess) {
                    await nearStore.btnDetail(res.result.docNo);
                    nearStore.inputForm.cmd = 'U';
                    layoutStore.useBtnList = uButtonList;
                    fileInfo.value.editFiles.insert = []; //데이터 저장 시 insert에 들어있는 데이터 초기화 하여 중복 이미지 저장 방지
                }
            }
        });
    } else {
        await updateNearMissReport(param, true).then(async res => {
            if (res && res.success) {
                // 재조회 로직
                fileInfo.value.editFiles.insert = []; //데이터 저장 시 insert에 들어있는 데이터 초기화 하여 중복 이미지 저장 방지
                // 재조회
                await nearStore.btnDetail(nearStore.inputForm.docNo);
                await fileList.value.fnSearch();
            }
        });
    }
};

btnAdd(() => {
    if (isChange()) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: addAction, param: '' });
    } else {
        addAction();
    }
});

const addAction = async () => {
    await fileList.value.editFiles.deleteFileId.forEach(() => {
        fileList.value.fnRemove(0, 'U');
    });

    nearStore.inputForm.cmd = 'I';
    layoutStore.useBtnList = iButtonList;
    const isDone = await nearStore.initInputForm();
    if (isDone) {
        await creatSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await actionSignatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }
    // await nearStore.btnAdd();
};

btnDelete(() => {
    if (isChange()) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: deleteAction, param: '' });
    } else {
        confirmMsg('info', t('msgDelete'), null, { fun: deleteAction });
    }
});

const deleteAction = async () => {
    await loadingPanelStore.openLoading();
    nearStore.btnDelete('D');
    loadingPanelStore.endLoading();
};

//출력 버튼
btnDownload(() => {
    if (isChange()) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: printAction, param: '' });
    } else {
        confirmMsg('info', t('msgPrint'), null, { fun: printAction });
    }
    // nearStore.btnPrint('detail');
});

const printAction = async () => {
    await nearStore.btnPrint('detail');
};

const signatureType = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2, //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
        hrId: userInfoStore.userOrgnHeadId,
        hrNm: userInfoStore.userOrgnHeadNm
    }
]);
const signatureType1 = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 3 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 4 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);

// 조치조직의 조직 팝업 데이터
const closeOrgn = e => {
    orgnPopup.value.onClose();
    if (e && e.length) {
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm
        }));
        nearStore.orgnItem = refinedItems;

        //데이터 선택시 선택된 조직의 조직장을 찾아 조치조직 사인란의 조직장에 조직장 추가
        if (nearStore.orgnItem.length !== 0) {
            getOrgnHead({ actionOrgnId: nearStore.orgnItem[0].id }, true).then(res => {
                if (res.list) {
                    actionSignatureComponent.value.setHrInfo(1, res.list.orgnHeadId, res.list.orgnHeadNm);
                } else {
                    actionSignatureComponent.value.setHrInfo(1, null, null);
                }
            });
        }
    }
};

//날짜 유효성 검사
const dateValidate = date => {
    if (date === 'writeDt') {
        if (nearStore.inputForm.writeDt > nearStore.inputForm.receiptDt && !nullCheck(nearStore.inputForm.receiptDt)) {
            alertMsg('warning', t('작성일은 접수일보다 높을 수 없습니다.'));
            nearStore.inputForm.writeDt = nearStore.inputForm.receiptDt;
        }
    } else if (date === 'receiptDt') {
        if (nearStore.inputForm.receiptDt > nearStore.inputForm.actionDt && !nullCheck(nearStore.inputForm.actionDt)) {
            alertMsg('warning', t('접수일은 완료일보다 높을 수 없습니다.'));
            nearStore.inputForm.receiptDt = nearStore.inputForm.actionDt;
        }
        if (nearStore.inputForm.receiptDt < nearStore.inputForm.writeDt && !nullCheck(nearStore.inputForm.writeDt)) {
            alertMsg('warning', t('접수일은 작성일보다 낮을 수 없습니다.'));
            nearStore.inputForm.receiptDt = nearStore.inputForm.writeDt;
        }
    } else if (date === 'actionDt') {
        if (nearStore.inputForm.actionDt < nearStore.inputForm.receiptDt && !nullCheck(nearStore.inputForm.receiptDt)) {
            alertMsg('warning', t('완료일은 접수일보다 낮을 수 없습니다.'));
            nearStore.inputForm.actionDt = nearStore.inputForm.receiptDt;
        }
    }
};

const writeDt = _.cloneDeep(nearStore.inputForm.writeDt);
const orgn = _.cloneDeep(nearStore.orgnItem);
const receiptDt = _.cloneDeep(nearStore.inputForm.receiptDt);
const actionDt = _.cloneDeep(nearStore.inputForm.actionDt);
const accidentContent = _.cloneDeep(nearStore.inputForm.accidentContent);
const accidentImproved = _.cloneDeep(nearStore.inputForm.accidentImproved);
const dangerContent = _.cloneDeep(nearStore.inputForm.dangerContent);
const dangerImproved = _.cloneDeep(nearStore.inputForm.dangerImproved);
const actionContent = _.cloneDeep(nearStore.inputForm.actionContent);

// 값 변경 여부 감지
const isChange = () => {
    // 작성일
    if (writeDt != nearStore.inputForm.writeDt) {
        return true;
    }
    // 조치 조직 - 조직
    if ((orgn?.[0]?.id ?? '') !== (nearStore?.orgnItem?.[0]?.id ?? '')) {
        return true;
    }

    // 조치 조직 - 접수일
    if (receiptDt != nearStore.inputForm.receiptDt) {
        return true;
    }
    // 조치 조직 - 완료일일
    if (actionDt != nearStore.inputForm.actionDt) {
        return true;
    }
    // 아차사고
    if (accidentContent != nearStore.inputForm.accidentContent) {
        return true;
    }
    // 작성자가 생각하는 개선대책
    if (accidentImproved != nearStore.inputForm.accidentImproved) {
        return true;
    }
    // 유해위험발굴
    if (dangerContent != nearStore.inputForm.dangerContent) {
        return true;
    }
    // 작성자가 생각하는 개선대책
    if (dangerImproved != nearStore.inputForm.dangerImproved) {
        return true;
    }
    // 조치결과(조치조직)
    if (actionContent != nearStore.inputForm.actionContent) {
        return true;
    }

    return false;
};

const nullCheck = data => {
    return data == null || data == '' || data == undefined;
};
</script>
