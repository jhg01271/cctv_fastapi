<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <div class="box df fdc">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh">
                    <!-- 검색 필드 -->
                    <div class="control-field ui form pa2-2rem" id="form">
                        <div class="row flex gutters1rem">
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for="">작성년도</label>
                                    <input :value="shInfoRegisterStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for="receiptDt" required>
                                        <span>접수일자</span>
                                    </label>
                                    <input v-model="shInfoRegisterStore.inputForm.receiptDt" id="receiptDt" :min="shInfoRegisterStore.searchParam.writeYear + '.01.01'" :max="shInfoRegisterStore.searchParam.writeYear + '.12.31'" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" required />
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-12">
                                <div class="field">
                                    <label for>접수번호</label>
                                    <input :value="shInfoRegisterStore.inputForm.docNo ? `${shInfoRegisterStore.inputForm.writeYear}-${shInfoRegisterStore.inputForm.docType}-${shInfoRegisterStore.inputForm.docNo}` : ''" type="text" v-input class="w100p radius" placeholder="저장 후 자동 채번됩니다." readonly />
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>등록일자</span>
                                    </label>
                                    <input :value="formatDate(shInfoRegisterStore.inputForm.createdAt)" v-input type="text" class="datepicker w100p radius" readonly />
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :checked="shInfoRegisterStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="shInfoRegisterStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid10-6 sm-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>작성자</span>
                                    </label>
                                    <i-chips :chips="[{ id: shInfoRegisterStore.inputForm.hrId, nm: shInfoRegisterStore.inputForm.hrNm }]" @popup="addPeople" @removeChip="removePeople" required></i-chips>
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for id="orgnNm">조직</label>
                                    <input id="orgnNm" v-model="shInfoRegisterStore.inputForm.orgnNm" type="text" v-input class="w100p radius" placeholder="인원 선택 시 자동으로 기입됩니다." readonly />
                                </div>
                            </div>
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for="jbrpNm">직위</label>
                                    <input id="jbrpNm" v-model="shInfoRegisterStore.inputForm.jbrpNm" type="text" v-input class="w100p radius" placeholder="인원 선택 시 자동으로 기입됩니다." readonly />
                                </div>
                            </div>

                            <div class="grid12-12 sm-grid12-12">
                                <div class="field">
                                    <label for="keyContent">주요내용</label>
                                    <textarea id="keyContent" v-model="shInfoRegisterStore.inputForm.keyContent" type="text" v-input class="w100p minh10rem radius" placeholder="내용을 입력하세요"></textarea>
                                </div>
                            </div>

                            <div class="grid12-6 sm-grid12-12">
                                <div class="field">
                                    <label>조치조직</label>
                                    <i-chips :chips="[{ id: shInfoRegisterStore.inputForm.actionOrgnId, nm: shInfoRegisterStore.inputForm.actionOrgnNm }]" @popup="addOrgn" @removeChip="removeOrgn"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label for="actionDt">
                                        <span>조치일자</span>
                                    </label>
                                    <input id="actionDt" v-model="shInfoRegisterStore.inputForm.actionDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label for="replyDt">
                                        <span>회신일자</span>
                                    </label>
                                    <input id="replyDt" v-model="shInfoRegisterStore.inputForm.replyDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                                </div>
                            </div>

                            <div class="grid12-12 sm-grid12-12">
                                <div class="field">
                                    <label for="actionContent">조치내용</label>
                                    <textarea id="actionContent" v-model="shInfoRegisterStore.inputForm.actionContent" type="text" v-input class="w100p minh10rem radius" placeholder="조치내용을 입력하세요"></textarea>
                                </div>
                            </div>

                            <div class="grid12-12 sm-grid12-12">
                                <div class="field">
                                    <label for="remark">비고</label>
                                    <textarea id="remark" v-model="shInfoRegisterStore.inputForm.remark" type="text" v-input class="w100p minh10rem radius"></textarea>
                                </div>
                            </div>
                            <div class="grid12-12 sm-grid12-12">
                                <div class="field mt2rem">
                                    <label for="">{{ t('ert_uploadFile') }}</label>
                                    <IFileList ref="fileList" targetType="SHR" :targetId="shInfoRegisterStore.files" @change="changeFlag = true" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser>

                    <div class="form ui tar">
                        <!-- <button type="button" class="btn w80px radius active" v-button @click="btnSave">
                        <span>저장</span>
                    </button>-->
                        <!-- <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                        <span>{{ t('close') }}</span>
                    </button> -->
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 조직 지정 팝업 -->
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조치조직'" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgnPopup" />
                    <!-- <customSelectPopup
                    :selectedIdList="equipmentStore.inputForm.orgnIdList"
                    :single="false"
                    :options="{ label: '조직', readonly: false,type:'orgn' }"
                    @close="closeCustomPopup"
                />-->
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { gsap } from 'gsap';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, confirmMsg, alertMsg, validationStore, onMounted, t, formatDate, watch, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload } = BaseView();
import CustomEase from 'gsap/CustomEase';
import router from '@/router';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import IFileList from '@/components/file/iFileList.vue';
const fileList = ref(); // 업로드 파일
//-----------------------------------------------
// [스토어]
import { useShInfoRegisterStore } from '@/stores/safewiz/support/safetyAndHealthInfoRegister.js';
const shInfoRegisterStore = useShInfoRegisterStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
import { getMyShInfoRegisterList } from '@/stores/safewiz/support/api/safetyAndHealthInfoRegisterApi.js';

import { useUserInfoStore } from '@/stores/user';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
watch(
    () => shInfoRegisterStore.inputForm.cmd,
    cmdVal => {
        if (cmdVal === 'I') {
            layoutStore.useBtnList = iButtonList;
        } else {
            layoutStore.useBtnList = uButtonList;
        }
    }
);
// ****************************************************************/
// ***************************** 버튼 *****************************/
// ****************************************************************/
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/MySafetyAndHealthInfoRegister');
};
btnSearch(() => {
    // 해당 안전보건정보 관리 대장의 정보
    const searchParam = {
        compId: shInfoRegisterStore.inputForm.compId,
        writeYear: shInfoRegisterStore.inputForm.writeYear,
        docType: shInfoRegisterStore.inputForm.docType,
        docNo: shInfoRegisterStore.inputForm.docNo,
        hrId: userInfoStore.userId,
        orgnId: userInfoStore.userOrgnId
    };

    getMyShInfoRegisterList(searchParam, true).then(res => {
        shInfoRegisterStore.inputForm = res.list[0];
        shInfoRegisterStore.inputForm.receiptDt = shInfoRegisterStore.formattingDate(shInfoRegisterStore.inputForm.receiptDt);
        shInfoRegisterStore.inputForm.actionDt = shInfoRegisterStore.formattingDate(shInfoRegisterStore.inputForm.actionDt);
        shInfoRegisterStore.inputForm.replyDt = shInfoRegisterStore.formattingDate(shInfoRegisterStore.inputForm.replyDt);
        shInfoRegisterStore.files = res.list[0].files;
    });
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        //
        confirmMsg('info', t('msgSave'), '', { fun: save, param: true });
    }
});
const save = async notify => {
    shInfoRegisterStore.fileList.editFiles.delete = fileList.value.editFiles.delete;
    shInfoRegisterStore.fileList.editFiles.insert = fileList.value.editFiles.insert;
    const result = await shInfoRegisterStore.btnMySave(notify);
    if (result) {
        // 저장 성공 시 파일 조회
        fileList.value.fnSearch();
    }
};

btnAdd(() => {
    shInfoRegisterStore.initInputForm();
});

btnDelete(() => {
    shInfoRegisterStore.btnMyDelete('D');
});
btnDownload(() => {
    shInfoRegisterStore.btnDownload([shInfoRegisterStore.inputForm.docNo], 'msgPrint');
});

// ****************************************************************/
// ***************************** 팝업 *****************************/
// ****************************************************************/
// 인원관리 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
// 팝업 열기 함수
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};
// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

// 인원 팝업
const peoplePopup = ref(null);
const addPeople = () => openPopup(peoplePopup);
const closePeoplePopup = () => closePopup(peoplePopup);
const removePeople = () => {
    shInfoRegisterStore.inputForm.hrId = '';
    shInfoRegisterStore.inputForm.hrNm = '';
    shInfoRegisterStore.inputForm.orgnId = '';
    shInfoRegisterStore.inputForm.orgnNm = '';
    shInfoRegisterStore.inputForm.jbrpId = '';
    shInfoRegisterStore.inputForm.jbrpNm = '';
};

const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e && e.hrId) {
        shInfoRegisterStore.inputForm.hrId = e.hrId;
        shInfoRegisterStore.inputForm.hrNm = e.hrNm;
        shInfoRegisterStore.inputForm.orgnId = e.orgnId; // 조직명
        shInfoRegisterStore.inputForm.orgnNm = e.orgnNm; // 조직명
        shInfoRegisterStore.inputForm.jbrpId = e.jbrp; // 직위
        shInfoRegisterStore.inputForm.jbrpNm = e.jbrpNm; // 직위
    }
};

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const orgnPopup = ref(null);
const addOrgn = () => openPopup(orgnPopup);
const closeOrgnPopup = data => {
    console.log('@@data', data);
    if (data.length > 0) {
        shInfoRegisterStore.inputForm.actionOrgnId = data[0].orgnId;
        shInfoRegisterStore.inputForm.actionOrgnNm = data[0].orgnNm;
    }
    closePopup(orgnPopup);
};
// 조직 요소 제거(x버튼 클릭)
const removeOrgn = index => {
    shInfoRegisterStore.inputForm.actionOrgnId = '';
    shInfoRegisterStore.inputForm.actionOrgnNm = '';
};
onMounted(() => {
    if (!shInfoRegisterStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (shInfoRegisterStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else {
        layoutStore.useBtnList = uButtonList;
        fileList.value.fnSearch();
    }
});
</script>
