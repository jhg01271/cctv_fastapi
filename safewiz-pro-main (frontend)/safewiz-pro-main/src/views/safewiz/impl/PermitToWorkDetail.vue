<template>
    <div id="form" class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                ref=" overlayScrollbars"
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form ref="myForm">
                    <div class="control-field ui form pa2-2rem us-pa1rem">
                        <div class="row flex gutters1rem">
                            <div class="grid9-2 lg-grid12-6">
                                <div class="field">
                                    <label for="workDt" required>
                                        <span>작업일자</span>
                                    </label>
                                    <input v-model="permitToWorkStore.inputForm.workDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" placeholder="2024.10.25" id="workDt" required />
                                </div>
                            </div>

                            <div class="grid9-2 lg-grid12-6">
                                <div class="field">
                                    <label for="workTime" required>
                                        <span>작업시간</span>
                                    </label>
                                    <input v-input startTime endTime type="text" v-calendar id="workTime" :value="permitToWorkStore.inputForm.workTime" @input="onDateInput('workTime', $event)" class="datepicker w100p br4px" placeholder="09:30 ~ 11:00" required />
                                </div>
                            </div>

                            <div class="grid9-2 lg-grid12-12">
                                <div class="field">
                                    <label for="workplace" required>
                                        <span>작업 장소</span>
                                    </label>
                                    <input class="br4px" type="text" v-input v-model="permitToWorkStore.inputForm.workplace" id="workplace" required />
                                </div>
                            </div>

                            <div class="grid9-2 lg-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>작업 설비</span>
                                    </label>
                                    <i-chips :chips="[{ id: permitToWorkStore.inputForm.equipmentId, name: permitToWorkStore.inputForm.equipmentNm }]" id="equipmentId" @popup="addTypeofEquipment" @removeChip="removeEquipment"></i-chips>
                                </div>
                            </div>

                            <div class="grid9-1 lg-grid12-12">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="h4-4rem df aic">
                                        <input :checked="permitToWorkStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="permitToWorkStore.toggleUseYn('useYn', $event)" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid9-9">
                                <div class="field df jcsb aic fww ul-jcfe ul-rg1rem">
                                    <label for required class="ul-w100p">
                                        <span>작업종류</span>
                                    </label>

                                    <div class="ul-w50p" v-for="(item, index) in permitToWorkStore.state.workTypeList" :key="index">
                                        <input :id="'item.checkYn' + index" :checked="item.checkYn === 'Y'" type="checkbox" name="trainingMonth1" v-input="item.workTypeNm" class="checkbox" @change="permitToWorkStore.toggleCheckYn(index, $event)" />
                                    </div>
                                    <div class="df gap2rem aic ul-w50p">
                                        <input id="additionalInfo" name="기타 작업종류" class="br4px" type="text" v-model="targetAdditionalInfo" placeholder="입력해주세요." :readonly="permitToWorkStore.state.workTypeList.some(el => el.minorCd === '0006' && el.checkYn === 'N')" :required="permitToWorkStore.state.workTypeList.some(el => el.minorCd === '0006' && el.checkYn === 'Y')" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid9-9">
                                <div class="field">
                                    <label for>작업 내용</label>
                                    <textarea class="minh10rem br4px" name id v-model="permitToWorkStore.inputForm.workContent"></textarea>
                                </div>
                            </div>

                            <div class="grid9-6 sm-grid12-12">
                                <div class="field">
                                    <label for>작업자</label>
                                    <i-chips :chips="permitToWorkStore.inputForm.hrList" @popup="addMultPeople" @removeChip="removePeople" />
                                </div>
                            </div>

                            <div class="grid9-3 sm-grid12-12">
                                <div class="field">
                                    <label for>협력사</label>
                                    <i-chips :chips="[{ id: permitToWorkStore.inputForm.partCompId, name: permitToWorkStore.inputForm.partCompNm }]" @popup="addPartner" @removeChip="removePart" />
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6 lg-grid12-12">
                                <div class="field">
                                    <label for>작업자 대표</label>
                                    <i-hr-chips-sign single type="worker" ref="workplaceRepresentative" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" @popup="peoplePopupOpen('C')" />
                                    <!-- <div class="sign-chips" :key="permitToWorkStore.inputForm.cmd">
                                        <div class="maxw100p minh6-8rem pr6-8rem dif pr aife">
                                            <i-hr-chips-sign single type="worker" ref="workplaceRepresentative" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" />
                                            <button type="button" @click="peoplePopupOpen('C')" class="pa r0 t0 minw6-8rem minh6-8rem df aic jcc bcF9FAFF br4px shrink0">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                                    <path d="M6.2832 13.3867V7.70312H0.628906V6.29688H6.2832V0.613281H7.68945V6.29688H13.373V7.70312H7.68945V13.3867H6.2832Z" fill="#3248F6" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div> -->
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6 lg-grid12-12">
                                <div class="field">
                                    <label for>현장감독자 (안전관리자)</label>
                                    <i-hr-chips-sign single type="siteSupervisor" ref="fieldSupervisor" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="1" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" @popup="peoplePopupOpen('H')" />
                                    <!-- <div class="sign-chips" :key="permitToWorkStore.inputForm.cmd">
                                        <div class="maxw100p minh6-8rem pr6-8rem dif pr aife">
                                            <i-hr-chips-sign single type="siteSupervisor" ref="fieldSupervisor" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="1" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" />
                                            <button type="button" @click="peoplePopupOpen('H')" class="pa r0 t0 minw6-8rem minh6-8rem df aic jcc bcF9FAFF br4px shrink0">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                                    <path d="M6.2832 13.3867V7.70312H0.628906V6.29688H6.2832V0.613281H7.68945V6.29688H13.373V7.70312H7.68945V13.3867H6.2832Z" fill="#3248F6" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div> -->
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                                <div class="field">
                                    <label for>수급업체 대표</label>
                                    <i-hr-chips-sign single type="subcontractorRep" ref="supplierRepresentative" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="1" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" @popup="peoplePopupOpen('S')" />
                                    <!-- <div class="sign-chips" :key="permitToWorkStore.inputForm.cmd">
                                        <div class="maxw100p minh6-8rem pr6-8rem dif pr aife">
                                            <i-hr-chips-sign single type="subcontractorRep" ref="supplierRepresentative" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="1" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" />
                                            <button type="button" @click="peoplePopupOpen('S')" class="pa r0 t0 minw6-8rem minh6-8rem df aic jcc bcF9FAFF br4px shrink0">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                                    <path d="M6.2832 13.3867V7.70312H0.628906V6.29688H6.2832V0.613281H7.68945V6.29688H13.373V7.70312H7.68945V13.3867H6.2832Z" fill="#3248F6" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div> -->
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                                <div class="field">
                                    <label for>승인권자 (작업허가 책임자)</label>
                                    <i-hr-chips-sign single type="workApprover" ref="approvalAuthority" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="2" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" @popup="peoplePopupOpen('R')" />
                                    <!-- <div class="sign-chips" :key="permitToWorkStore.inputForm.cmd">
                                        <div class="maxw100p minh6-8rem pr6-8rem dif pr aife">
                                            <i-hr-chips-sign single type="workApprover" ref="approvalAuthority" :cmd="permitToWorkStore.inputForm.cmd" targetType="PTW" :sysCodeOrdSeq="2" :writeYear="permitToWorkStore.inputForm.writeYear" :docNo="permitToWorkStore.inputForm.docNo" />
                                            <button type="button" @click="peoplePopupOpen('R')" class="pa r0 t0 minw6-8rem minh6-8rem df aic jcc bcF9FAFF br4px shrink0">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                                    <path d="M6.2832 13.3867V7.70312H0.628906V6.29688H6.2832V0.613281H7.68945V6.29688H13.373V7.70312H7.68945V13.3867H6.2832Z" fill="#3248F6" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div> -->
                                </div>
                            </div>
                            <div class="grid12-8 px1rem ul-grid12-12">
                                <div class="field">
                                    <label for required>
                                        <span>점검사항</span>
                                    </label>
                                    <i-chips :chips="permitToWorkStore.inputForm.inspectionList" chipIdKey="inspectionId" chipNameKey="inspectionNm" @removeChip="removeChecklist" @popup="addChecklist" required></i-chips>
                                </div>
                            </div>
                            <div class="grid12-4 ul-grid12-12">
                                <div class="field h100p tar df aife gap1rem">
                                    <button type="button" class="btn base radius w20rem" @click="addChecklistMngType">점검사항 관리</button>
                                    <button type="button" class="btn base radius w20rem" @click="addSafetyEqMngType">필요 안전기구 관리</button>
                                </div>
                            </div>

                            <div class="grid9-9">
                                <div class="field">
                                    <OverlayScrollbarsComponent
                                        ref=" overlayScrollbars"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden '
                                            }
                                        }"
                                    >
                                        <table class="tac sm-minw62rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w40p" />
                                                <col class="w8rem" />
                                                <col class="w8rem" />
                                                <col class="w8rem" />
                                                <col />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>번호</th>
                                                    <th>안전작업 점검사항</th>
                                                    <th>양호</th>
                                                    <th>불량</th>
                                                    <th>허용불가</th>
                                                    <th>필요안전기구</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(item, index) in (permitToWorkStore.inputForm.detailList || []).map((item, i) => ({ ...item, originalIndex: i })).filter(item => item.cmd !== 'D')" :key="item.originalIndex">
                                                    <td>{{ index + 1 }}</td>
                                                    <td class="tal">{{ item.inspectionItemNm }}</td>
                                                    <td>
                                                        <input :checked="item.acceptableYn === 'Y'" type="checkbox" name="acceptableYn" v-input class="checkbox" @change="toggleCheckYn(item.originalIndex, $event)" />
                                                    </td>
                                                    <td>
                                                        <input :checked="item.nonCompliantYn === 'Y'" type="checkbox" name="nonCompliantYn" v-input class="checkbox" @change="toggleCheckYn(item.originalIndex, $event)" />
                                                    </td>
                                                    <td>
                                                        <input :checked="item.unacceptableYn === 'Y'" type="checkbox" name="unacceptableYn" v-input class="checkbox" @change="toggleCheckYn(item.originalIndex, $event)" />
                                                    </td>
                                                    <td class="tal">
                                                        <i-chips :chips="item.safetyEqList" @removeChip="removeSafetyEqList" @popup="addSafetyEqList(item, item.originalIndex)"></i-chips>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td colspan="2">종합의견</td>
                                                    <td colspan="4">
                                                        <textarea class="minh10rem" name id placeholder="종합의견을 입력해주세요" v-model="permitToWorkStore.inputForm.overallOpinion"></textarea>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                            <div class="grid12-12 sm-grid12-12">
                                <div class="field mt2rem">
                                    <label for="">{{ t('ert_uploadFile') }}</label>
                                    <IFileList ref="fileList" targetType="PTW" :targetId="permitToWorkStore.files" @change="changeFlag = true" />
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 인원 검색 팝업 컴포넌트 시작 (멀티선택) -->
            <i-PopupDialog ref="multiPeoplePopup">
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="permitToWorkStore.inputForm.hrList.map(el => el.hrId)" @selected="selectMultiPeople" @close="closeMultiPeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
            <!-- 업체 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="partnerPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'협력사'" :inputForm="permitToWorkStore.inputForm" filterKey="partCompNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartner" @close="closePartner" />
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="checklistPopup">
                <!-- 점검사항 선택 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'점검사항 유형'" :inputForm="permitToWorkStore.inputForm.inspectionList" :selectedIdList="permitToWorkStore.inputForm.inspectionList" uniqueKey="inspectionId" filterKey="inspectionNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetchParam="{ compId: permitToWorkStore.inputForm.compId }" :fetch-data="getSafetyWorkChecklist" @close="onPopupClose" @apply="applyChecklist" />
                </div>
            </i-PopupDialog>

            <!-- 모달 팝업 콤포넌트 끝  -->
            <i-PopupDialog ref="checklistMngPopup">
                <!-- 점검사항 관리 팝업-->
                <div class="contents w1024px md-w100p">
                    <checkListTypeManage @close="closeChecklistMngType" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="safetyEquipmentPopup">
                <!-- 안전기구 선택 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'필요안전기구 유형'" uniqueKey="safetyEqId" filterKey="safetyEqNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ compId: permitToWorkStore.inputForm.compId }" :fetch-data="getSafetyEquipment" @close="closeSafetyEqList" />
                </div>
            </i-PopupDialog>

            <!-- 모달 팝업 콤포넌트 끝  -->
            <i-PopupDialog ref="safetyEquipmentMngPopup">
                <!-- 필요 안전기구 관리 팝업-->
                <div class="contents w1024px md-w100p">
                    <safetyEqTypeManage @close="closeSafetyEqMngType" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <!-- 팝업 높이 임시 수정 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="typeofEquipmentPopup">
                <!-- 설비 선택 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'설비'" uniqueKey="equipmentId" filterKey="equipmentNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ compId: permitToWorkStore.inputForm.compId, searchText: permitToWorkStore.inputForm.stdEqId }" :fetch-data="getEquip" :btnInfo="{ close: true }" @close="closeTypeofEquipmentPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, toRaw, computed } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { getEquip } from '@/stores/system/base/api/equipmentApi';
import { useButtonListStore } from '@/stores/buttonList';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getDateFormat } from '@/utils/dateUtil.js';

const layoutStore = useButtonListStore();
const { setRouterParam, confirmMsg, alertMsg, onMounted, validationStore, btnSearch, btnBack, btnSave, btnDelete, btnDownload, t, getDuplicatedData } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSave'];

import { getSafetyWorkChecklist, getSafetyEquipment } from '@/stores/safewiz/impl/api/permitToWorkApi.js';

import iHrChipsSign from '@/components/common/iHrChipsSign.vue';

import IFileList from '@/components/file/iFileList.vue';
const fileList = ref(); // 업로드 파일

//-----------------------------------------------
// [스토어]
import { usePermitToWorkStore } from '@/stores/safewiz/impl/permitToWork.js';
const permitToWorkStore = usePermitToWorkStore();

//-----------------------------------------------
// [인원 팝업]
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();
//-----------------------------------------------

//-----------------------------------------------
//[협력사 팝업]
import { getPartner } from '@/stores/system/base/api/partnerApi.js';
//-----------------------------------------------
//[점검사항 관리 팝업]
import checkListTypeManage from '@/views/safewiz/impl/CheckListTypeManage.vue';

const checklistMngPopup = ref(null);
const addChecklistMngType = () => {
    checklistMngPopup.value.onOpen();
};
const closeChecklistMngType = () => {
    checklistMngPopup.value.onClose();
};

//-----------------------------------------------
//-----------------------------------------------
//[필요 안전기구 관리 팝업]
import safetyEqTypeManage from '@/views/safewiz/impl/SafetyEqTypeManage.vue';

const safetyEquipmentMngPopup = ref(null);
const addSafetyEqMngType = () => {
    safetyEquipmentMngPopup.value.onOpen();
};
const closeSafetyEqMngType = () => {
    safetyEquipmentMngPopup.value.onClose();
};

//-----------------------------------------------
//#region [점검사항 선택 팝업]
const checklistPopup = ref(null);
const addChecklist = () => {
    checklistPopup.value.onOpen();
};
//#endregion

//#region =====[F] iChips에서 X 버튼으로 삭제시 처리 =====
const removeChecklist = (index, removeChips) => {
    // 삭제 대상의 inspectionId 확인
    const targetInspectionId = removeChips.inspectionId;

    // 점검사항의 항목에서 삭제되지 않은 항목만 남김
    permitToWorkStore.inputForm.detailList = permitToWorkStore.inputForm.detailList.map(item => (item.inspectionId === targetInspectionId ? { ...item, cmd: 'D' } : item));

    // 점검사항 항목에서 삭제되지 않은 항목만 남김
    permitToWorkStore.inputForm.inspectionList = permitToWorkStore.inputForm.inspectionList.map(item => (item.inspectionId === targetInspectionId ? { ...item, cmd: 'D' } : item));
};
//#endregion

//#region =====[F] 점검사항 팝업 선택 후 처리 =====
const applyChecklist = data => {
    if (data && data.length > 0) {
        // ===== 기존 선택된 inspectionList에서 선택 해제된 항목 제거 =====
        const currentInspectionIds = new Set(data.map(({ inspectionId }) => inspectionId));
        permitToWorkStore.inputForm.inspectionList = permitToWorkStore.inputForm.inspectionList.filter(el => currentInspectionIds.has(el.inspectionId));

        // ===== 팝업으로 선택된 항목을 점검사항 및 점검항목으로 처리 =====
        data.forEach(({ inspectionId, inspectionNm, itemList, ordSeq }) => {
            // 새로운 inspectionId 추가 (ordSeq 기준으로 정렬하여 추가)
            if (!permitToWorkStore.inputForm.inspectionList.some(el => el.inspectionId === inspectionId)) {
                permitToWorkStore.inputForm.inspectionList.push({
                    inspectionId: inspectionId,
                    inspectionNm: inspectionNm,
                    ordSeq: ordSeq
                });

                // `ordSeq` 기준으로 정렬
                permitToWorkStore.inputForm.inspectionList.sort((a, b) => a.ordSeq - b.ordSeq);
            }

            permitToWorkStore.inputForm.detailList.forEach(detail => {
                if (!permitToWorkStore.inputForm.inspectionList.some(el => el.inspectionId === detail.inspectionId)) {
                    // itemList에 존재하지 않는다면 'D'로 변경
                    detail.cmd = 'D';
                }
            });

            if (itemList?.length) {
                itemList.forEach(el => {
                    el.inspectionItem = el.inspectionItemId;
                    el.cmd = 'I'; // 기본값을 'I'로 설정
                    el.acceptableYn = 'N';
                    el.nonCompliantYn = 'N';
                    el.unacceptableYn = 'N';
                });

                itemList.forEach(el => {
                    const existingItem = permitToWorkStore.inputForm.detailList.find(detail => detail.inspectionId === el.inspectionId && detail.inspectionItem === el.inspectionItemId && detail.cmd !== 'D');

                    if (existingItem) {
                        if (existingItem.cmd === 'D') {
                            // 기존에 'D' 상태인데 writeYear가 있는 경우 'U', 없는 경우 'I'
                            // existingItem.cmd = existingItem.writeYear ? 'U' : 'I';

                            existingItem.cmd = 'I';
                        } else if (existingItem.cmd !== 'U') {
                            // 기존에 'U' 상태가 아니라면 'U'로 변경
                            console.log(existingItem.cmd);
                            // existingItem.cmd = 'U';
                        }
                    } else if (el.useYn === 'Y') {
                        // 기존에 존재하지 않으면 추가 (새로운 항목)
                        permitToWorkStore.inputForm.detailList.push({ ...el });
                    }
                });
            }
        });
    }
    onPopupClose();
};

//#endregion

//#region =====[F] 팝업 종료 처리 =====
const onPopupClose = () => {
    const popup = checklistPopup.value;
    if (popup?.onClose) {
        popup.onClose();
    }
};
//#endregion

//-----------------------------------------------
//-----------------------------------------------

//[안전기구 선택 팝업]
const safetyEquipmentPopup = ref(null);
const eqList = ref(null);
const eqIdx = ref(null);
const addSafetyEqList = (item, index) => {
    eqList.value = item;
    eqIdx.value = index;
    safetyEquipmentPopup.value.onOpen();
};

// 안전기구 요소 제거(x버튼 클릭)
const removeSafetyEqList = () => {
    permitToWorkStore.inputForm.safetyEqId = '';
    permitToWorkStore.inputForm.safetyEqNm = '';
};

const closeSafetyEqList = data => {
    if (data && data[0]) {
        data[0].itemList.forEach(el => {
            el.id = el.safetyEqItemId;
            el.name = el.safetyEqItemNm;
        });
        permitToWorkStore.inputForm.detailList[eqIdx.value].safetyEqList = data[0].itemList;

        if (data[0].itemList.length) {
            data[0].itemList.forEach(el => {
                el.safetyEqId = el.safetyEqItemId;
            });
            permitToWorkStore.inputForm.detailList[eqIdx.value].safetyEqList = data[0].itemList.filter(el => el.useYn === 'Y');
        }
    }

    const popup = safetyEquipmentPopup.value;
    if (popup?.onClose) {
        popup.onClose();
    }
};

//-----------------------------------------------
onMounted(async () => {
    //#region 1. 작업종류 시스템 코드에서 가져오기
    await permitToWorkStore.getWorkTypeList();
    //#endregion

    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    permitToWorkStore.setRefs(workplaceRepresentative, fieldSupervisor, supplierRepresentative, approvalAuthority);
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        layoutStore.useBtnList = uButtonList;
        permitToWorkStore.searchParam = param;
        await permitToWorkStore.getPermitToWorkDetailList();
        await fileList.value.fnSearch();
        setTimeout(() => {
            workplaceRepresentative.value.Search();
            fieldSupervisor.value.Search();
            supplierRepresentative.value.Search();
            approvalAuthority.value.Search();
        }, 100);
    } else if (!permitToWorkStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push({ name: 'SafetyMgmtGuidelines', state: { activeTab: 'opinion' } });
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
});

// import { computed } from "vue";

// minorCd가 "0006"인 항목의 additionalInfo를 v-model과 직접 연결
const targetAdditionalInfo = computed({
    get: () => {
        const targetItem = permitToWorkStore.state.workTypeList.find(el => el.minorCd === '0006');
        return targetItem ? targetItem.additionalInfo : '';
    },
    set: value => {
        const targetItem = permitToWorkStore.state.workTypeList.find(el => el.minorCd === '0006');
        if (targetItem) {
            targetItem.additionalInfo = value;
        }
    }
});

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(permitToWorkStore.originData);
    const currentData = toRaw(permitToWorkStore.inputForm);
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
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', permitToWorkStore.goBack);
});

btnSearch(async () => {
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', searchAction);
});

const searchAction = async () => {
    await workplaceRepresentative.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await fieldSupervisor.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await supplierRepresentative.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await approvalAuthority.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await fileList.value.fnSearch();
    permitToWorkStore.getPermitToWorkDetailList()
}

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        permitToWorkStore.fileList.editFiles.delete = fileList.value.editFiles.delete;
        permitToWorkStore.fileList.editFiles.insert = fileList.value.editFiles.insert;
        const result = await permitToWorkStore.btnSave();
        if (result) {
            searchAction()
        }
    }
});

btnDelete(() => {
    permitToWorkStore.btnDetailDelete();
});

btnDownload(() => {
    if (permitToWorkStore.inputForm.cmd === 'U') {
        handleDataChange('저장되지 않은 정보가 있습니다. 그래도 출력하시겠습니까?', permitToWorkStore.btnDetailDownload);
    } else {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
    }
});

//-----------------------------------------------
// [인원 팝업 (멀티)]

const multiPeoplePopup = ref(null); // PopupDialog에 대한 ref
const typeofEquipmentPopup = ref(null);
const addMultPeople = () => {
    multiPeoplePopup.value.onOpen();
};
const removePeople = e => {
    permitToWorkStore.inputForm.hrList.splice(e, 1);
};
const selectMultiPeople = () => {
    if (multiPeoplePopup.value) {
        multiPeoplePopup.value.onClose();
        const filteredData = getDuplicatedData(permitToWorkStore.inputForm.hrList, selectUserStore.getSelectedUser(), 'hrId')
        filteredData.deDuplicatedDataList.forEach(user => {
            permitToWorkStore.inputForm.hrList.push({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm,
                targetType: 'WHMG'
            });
        });
    }
};

const closeMultiPeoplePopup = () => {
    if (multiPeoplePopup.value) {
        multiPeoplePopup.value.onClose();
    }
};
//-----------------------------------------------

//-----------------------------------------------
const workplaceRepresentative = ref();
const fieldSupervisor = ref();
const supplierRepresentative = ref();
const approvalAuthority = ref();

const div = ref();
const peoplePopup = ref();

const peoplePopupOpen = param => {
    div.value = param;
    peoplePopup.value.onOpen();
};

const selectPeople = () => {
    if (div.value === 'C') {
        workplaceRepresentative.value.selectPeople();
    } else if (div.value === 'H') {
        fieldSupervisor.value.selectPeople();
    } else if (div.value === 'S') {
        supplierRepresentative.value.selectPeople();
    } else if (div.value === 'R') {
        approvalAuthority.value.selectPeople();
    }
    peoplePopup.value.onClose();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
//-----------------------------------------------
//-----------------------------------------------

const partnerPopup = ref(null); // PopupDialog에 대한 ref

// 폼 초기화 후 팝업 열기 2024.07.01
const addPartner = () => {
    partnerPopup.value.onOpen();
};

// 설비 요소 제거(x버튼 클릭)
const removePart = () => {
    permitToWorkStore.inputForm.partCompId = null;
    permitToWorkStore.inputForm.partCompId = null;
};

const closePartner = e => {
    if (partnerPopup.value) {
        partnerPopup.value.onClose();

        if (e && e.length) {
            console.log('e파트너', e);
            permitToWorkStore.inputForm.partCompId = e[0].partCompId;
            permitToWorkStore.inputForm.partCompNm = e[0].partCompNm;
        }
    }
};

//-----------------------------------------------
//-----------------------------------------------
//시간 체크
const onDateInput = (field, event) => {
    console.log('이벤트', event.target);
    permitToWorkStore.inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};

//-----------------------------------------------

//#region =====[F] 체크버튼 그룹별로 한개씩만 처리 되도록 수정 =====
const toggleCheckYn = (index, event) => {
    const field = event.target.name;
    const isChecked = event.target.checked;
    const item = permitToWorkStore.inputForm.detailList[index];

    // 같은 행의 모든 필드를 'N'으로 초기화
    item.acceptableYn = 'N';
    item.nonCompliantYn = 'N';
    item.unacceptableYn = 'N';

    // 현재 체크된 필드만 'Y'로 설정
    if (isChecked) {
        item[field] = 'Y';
    }
};
//#endregion

//-----------------------------------------------
//작업 설비 클릭 이벤트
const addTypeofEquipment = el => {
    typeofEquipmentPopup.value.onOpen();
};

//작업 설비 데이터 선택 후 이벤트
const closeTypeofEquipmentPopup = e => {
    if (e.length > 0) {
        permitToWorkStore.inputForm.equipmentId = e[0].equipmentId;
        permitToWorkStore.inputForm.equipmentNm = e[0].equipmentNm;
    }
    typeofEquipmentPopup.value.onClose();
};

//작업 설비 chips 제거
const removeEquipment = () => {
    permitToWorkStore.inputForm.equipmentId = '';
    permitToWorkStore.inputForm.equipmentNm = '';
};
</script>
