<template>
    <!-- 설비상세 -->
    <div v-if="equipmentStore && equipmentStore.inputForm" class="contents">
        <div id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 lg-grid12-6 us-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>{{ t('typeofEquipment') }}</span>
                                </label>
                                <i-chips :chips="equipmentStore.inputForm.stdEq" @popup="addTypeofEquipment" @removeChip="removeTypeofEquipment()" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="equipmentNm" required>
                                    <span>{{ t('equipmentNm') }}</span>
                                </label>
                                <input v-model="equipmentStore.inputForm.equipmentNm" v-input type="text" class="w100p radius" id="equipmentNm" required placeholder="설비명을 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-12">
                            <div class="field">
                                <label for>등록일자</label>
                                <input v-input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(equipmentStore.inputForm.createdAt)" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for>정렬</label>
                                <input v-model="equipmentStore.inputForm.ordSeq" v-input type="number" class="w100p radius" id min="0" step="1" max="99" placeholder="99" />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="equipmentStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="equipmentStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="field h100p df fdc">
                                <label for>설비 사진</label>

                                <!-- 첨부파일 -->
                                <div class="box df aic jcc w100p fg1">
                                    <iFileImage ref="fileList" targetType="partnerLogo" :targetId="equipmentStore.inputForm.fileId" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6">
                                    <div class="field">
                                        <label for>담당자(정)</label>
                                        <i-chips :chips="equipmentStore.inputForm.headHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('head')" @removeChip="removePeople('head', $event)"></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-6">
                                    <div class="field">
                                        <label for>담당자(부)</label>
                                        <i-chips :chips="equipmentStore.inputForm.secondHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('second')" @removeChip="removePeople('second', $event)"></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-6 us-grid12-12">
                                    <div class="field">
                                        <label for>조직</label>
                                        <i-chips :chips="equipmentStore.inputForm.orgnList" @popup="addOrgn" @removeChip="removeOrgn"></i-chips>
                                    </div>
                                </div>
                                <div class="grid12-6 us-grid12-12">
                                    <div class="field">
                                        <label for>작업장</label>
                                        <i-chips :chips="equipmentStore.inputForm.workplaceList" @popup="addWorkplace" @removeChip="removeWorkplace"></i-chips>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-9 ul-grid12-9 sm-grid12-9 us-grid12-12">
                            <div class="field">
                                <label for>공정 작업 내용</label>
                                <i-chips :chips="equipmentStore.workItems" @popup="addWork" @removeChip="removeWork"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-3 us-grid12-6" :key="componentKey">
                            <div class="field">
                                <label for>사용처</label>
                                <select name id v-select v-model="equipmentStore.inputForm.usageType">
                                    <option v-for="item in equipmentStore.usageTypeList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6 us-grid12-6">
                            <div class="field">
                                <label for>
                                    <span>설치장소</span>
                                </label>
                                <input v-model="equipmentStore.inputForm.setupLocation" v-input type="text" class="w100p radius" placeholder="설치장소를 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="관리번호">
                                    <span>관리번호</span>
                                </label>
                                <input v-model="equipmentStore.inputForm.mgmtNum" v-input type="text" class="w100p radius" id placeholder="관리번호를 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="제조사">
                                    <span>제조사</span>
                                </label>
                                <input v-model="equipmentStore.inputForm.mfComp" v-input type="text" class="w100p radius" id placeholder="제조사를 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="규격 및 용량">
                                    <span>규격 및 용량</span>
                                </label>
                                <input v-model="equipmentStore.inputForm.mfSpec" v-input type="text" class="w100p radius" id placeholder="규격 및 용량을 입력해주세요." />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>제조일</label>
                                <input v-model="equipmentStore.inputForm.mfDate" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" id />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>설치일</label>
                                <input v-model="equipmentStore.inputForm.setupAt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" id value="2009.02.25" />
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label for>설비 설명</label>
                                <textarea v-model="equipmentStore.inputForm.remark" type="text" v-input class="w100p radius minh10rem" placeholder="설비 설명을 입력해주세요"></textarea>
                            </div>
                        </div>

                        <!-- TODO : 추후 개발 필요 ( QR 정보 생성 ) -->
                        <div class="grid12-6 ul-grid12-12">
                            <!-- <div class="field">
                                <label for>관리 번호</label>

                                <div class="df aic radius">
                                    <input
                                        type="text"
                                        class="w100p"
                                        placeholder="관리 번호를 입력하여 QR코드를 생성해주세요."
                                    />
                                    <button type="button " class="btn active shrink0 w7rem">
                                        <span>생성</span>
                                    </button>
                                </div>
                            </div>-->

                            <!--    <div class="box pa2rem mt2rem">
                                <div class="row flex gutters2rem">
                                    <div class="grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for>설비 QR 코드</label>
                                        </div>
                                        <div class="box pa2rem df aic jcc pr h15rem">
                                            <img
                                                src="/assets/img/common/qr.png"
                                                alt
                                                class="w100p maxw10rem maxh10rem radius4px ofc"
                                            />
                                            <button type="button" class="pa r2rem t2rem">
                                                <svg
                                                    width="12"
                                                    height="12"
                                                    viewBox="0 0 12 12"
                                                    fill="none"
                                                    xmlns="http://www.w3.org/2000/svg"
                                                >
                                                    <path
                                                        d="M11 1L1 11M11 11L1 1.00001"
                                                        stroke="black"
                                                        stroke-linecap="round"
                                                    />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for>신고 QR 코드</label>
                                        </div>
                                        <div class="box pa2rem df aic jcc tac h15rem">
                                            <dl>
                                                <dt class="fs1-5rem fw300 c9EA1A6">QR 코드 생성란</dt>
                                                <dd class="mt1rem fs1-2rem">상단 관리 번호를 입력해주세요.</dd>
                                            </dl>
                                        </div>
                                    </div>
                                </div>
                            </div>-->
                        </div>
                    </div>
                </form>
                <div v-if="equipmentStore.inputForm.equipmentId != 'temp'" class="df jcfe sticky t100p rg-t0 rg-b0-5rem tty6px">
                    <button type="button" class="btn line active radius w20rem mr8px" @click="changeEquipment('prev')" :disabled="equipmentStore.selectedEquipmentIdx <= 0">
                        <svg class="mr1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M5 1L1.20974 4.5286C0.930086 4.78894 0.930086 5.21106 1.20974 5.4714L5 9" stroke="#3248F6" stroke-linecap="round" />
                        </svg>

                        <span>이전 설비</span>
                    </button>
                    <button type="button" class="btn line active radius w20rem" @click="changeEquipment('next')" :disabled="equipmentStore.mergedEquipmentByType.length === equipmentStore.selectedEquipmentIdx + 1">
                        <span>다음 설비</span>

                        <svg class="ml1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M1 9L4.79026 5.4714C5.06991 5.21106 5.06991 4.78894 4.79026 4.5286L1 1" stroke="#3248F6" stroke-linecap="round" />
                        </svg>
                    </button>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 담당자 지정 팝업 -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <h3>인원</h3>
                    <selectUser :single="false" :selected="gubun === 'head' ? equipmentStore.inputForm.headHrList.map(el => el.hrId) : equipmentStore.inputForm.secondHrList.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 작업 내용 지정 팝업 -->
            <i-PopupDialog ref="workPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectWork :single="false" @selected="selecteWork" :selectedList="equipmentStore.inputForm.workList"></selectWork>

                    <div class="form ui mt2-2rem df gap8px jcfe">
                        <!-- <button type="button" class="btn w80px radius active" v-button @click="btnSave">
                        <span>저장</span>
                    </button>-->
                        <button type="button" v-button class="btn w74px radius active" @click="selectWorkPopup">
                            <span>{{ t('select') }}</span>
                        </button>
                        <button type="button" v-button class="btn w74px radius bright-grey" @click="closeWorkPopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>

            <!-- 설비유형 팝업 -->
            <i-PopupDialog ref="typeofEquipmentPopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="t('typeofEquipment')" :selectedIdList="[equipmentStore.inputForm.stdEqId]" uniqueKey="stdEqId" filterKey="stdEqNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getStdEquips" @close="closeTypeofEquipmentPopup" />
                </div>
            </i-PopupDialog>
            <!-- 조직 지정 팝업 -->
            <i-PopupDialog ref="orgnPopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="t('orgn')" :selectedIdList="equipmentStore.inputForm.orgnIdList" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getOrganization" @close="closeOrgnPopup" @apply="applyOrgnPopup" />
                </div>
            </i-PopupDialog>
            <!-- 작업장 지정 팝업 -->
            <i-PopupDialog ref="workplacePopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="t('workplace')" :selectedIdList="equipmentStore.inputForm.workplaceIdList" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getWp" @close="closeWorkplacePopup" @apply="applyWorkplacePopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, confirmMsg, alertMsg, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnAdd, btnSave, btnDelete } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';

import { useEquipmentStore } from '@/stores/system/base/equipment.js';
const equipmentStore = useEquipmentStore();

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import customSelectPopup from '@/views/system/base/popup/CustomSelectPopup.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import selectWork from '@/views/base/work/SelectWork/Index.vue';
import iFileImage from '@/components/file/iFileImage.vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const uButtonList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
const iButtonList = ['btnBack', 'btnSave'];
const fileList = ref();

import { useDatasetStore } from '@/stores/safewiz/dataset/dataset.js';

import { getStdEquips } from '@/stores/system/base/api/equipmentApi';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { getWp } from '@/stores/system/base/api/workplaceApi';

const componentKey = ref(0);
onMounted(async () => {
    if (equipmentStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else if (!equipmentStore.inputForm || !equipmentStore.inputForm.cmd) {
        router.push('EquipmentManage');
        return;
    }else{
        layoutStore.useBtnList = uButtonList;;
    }

    equipmentStore.fileInfo(fileList);
    //파일조회
    fileList.value.fnSearch();
    equipmentStore.inputForm.stdEq = [{ id: equipmentStore.inputForm.stdEqId, name: equipmentStore.inputForm.stdEqNm }];
});
import _ from 'lodash';
// 조회 버튼 이벤트 함수
btnSearch(() => {
    if (!_.isEqual(equipmentStore.originData, equipmentStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: searchAction, param: '' });
    } else {
        searchAction();
    }
});
const searchAction = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    equipmentStore.btnDetail(equipmentStore.inputForm.equipmentId);
    fileList.value.fnSearch();
};

btnBack(() => {
    if (!_.isEqual(equipmentStore.originData, equipmentStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: goBack, param: '' });
    } else {
        goBack();
    }
});
const goBack = () => {
    fileList.value.fnRemove();
    equipmentStore.btnSearch();
    router.go(-1);
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        if (equipmentStore.inputForm.cmd === 'I') {
            //추가
            confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: equipmentStore.inputForm });
        } else {
            equipmentStore.btnSave();
        }
    }
});
const saveAction = async param => {
    let data = await equipmentStore.insertEquipment(param);
    if (data) {
        layoutStore.useBtnList = uButtonList;
    }
};

btnAdd(() => {
    if (!_.isEqual(equipmentStore.originData, equipmentStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: addAction, param: '' });
    } else {
        addAction();
    }
});
const addAction = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    fileList.value.fnRemove();
    equipmentStore.btnAdd('temp');
    layoutStore.useBtnList = iButtonList;
    componentKey.value++;
};
btnDelete(() => {
    if (!_.isEqual(equipmentStore.originData, equipmentStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: equipmentStore.btnDelete, param: 'D' });
    } else {
        equipmentStore.btnDelete('D');
    }
});

// 인원 팝업
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();

const gubun = ref();
const peoplePopup = ref(null); // PopupDialog에 대한 ref
const addPeople = el => {
    gubun.value = el;
    peoplePopup.value.onOpen();
};
const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e.length > 0) {
        if (gubun.value === 'head') {
            equipmentStore.inputForm.headHrList = e;
        } else if (gubun.value === 'second') {
            equipmentStore.inputForm.secondHrList = e;
        }
    }
};

// 설비 유형 제거 (x버튼 클릭 시)
const removeTypeofEquipment = () => {
    equipmentStore.inputForm.stdEqId = null;
};
// 인원 제거 (x버튼 클릭 시)
const removePeople = (type, index) => {
    if (type === 'head') {
        equipmentStore.inputForm.headHrList.splice(index, 1);
    } else if (type === 'second') {
        equipmentStore.inputForm.secondHrList.splice(index, 1);
    }
};

// 작업 팝업
const workPopup = ref(null);
const addWork = () => {
    workPopup.value.onOpen();
};
const selectWorkPopup = () => {
    if (workPopup.value) {
        equipmentStore.selecteWorkValues.forEach(el => {
            el.title = el.processNm;
            el.id = el.prcsWorkId;
            el.nm = el.workContent;
        });
        equipmentStore.inputForm.workList = equipmentStore.selecteWorkValues;
        equipmentStore.workItems = equipmentStore.selecteWorkValues;

        workPopup.value.onClose();
    }
};
const closeWorkPopup = () => {
    if (workPopup.value) {
        workPopup.value.onClose();
    }
};
const selecteWork = e => {
    equipmentStore.selecteWorkValues = e;
};
const removeWork = index => {
    equipmentStore.inputForm.workList.splice(index, 1);
    // workPopup.value.onOpen();
};

// 설비 이동 버튼 이벤트
const changeEquipment = async e => {
    if (!_.isEqual(equipmentStore.originData, equipmentStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: changeEquipmentAction, param: e });
    } else {
        changeEquipmentAction(e);
    }
};
const changeEquipmentAction = async e => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    fileList.value.fnRemove();
    if (e === 'prev') {
        // 이전 작업장
        if (equipmentStore.selectedEquipmentIdx > 0) equipmentStore.selectedEquipmentIdx--;
    } else if (e === 'next') {
        // 다음 작업장
        if (equipmentStore.selectedEquipmentIdx + 1 <= equipmentStore.mergedEquipmentByType.length) equipmentStore.selectedEquipmentIdx++;
    }
    const equipmentId = equipmentStore.mergedEquipmentByType[equipmentStore.selectedEquipmentIdx].equipmentId;

    await equipmentStore.btnDetail(equipmentId, true);

    equipmentStore.fileInfo(fileList);

    //파일조회
    fileList.value.fnSearch();
};

// 설비유형 팝업
const typeofEquipmentPopup = ref(null);

const addTypeofEquipment = () => {
    typeofEquipmentPopup.value.onOpen();
};
// 설비 유형 팝업 닫기
const closeTypeofEquipmentPopup = e => {
    if (e.length > 0) {
        equipmentStore.inputForm.stdEqId = e[0].stdEqId;
        equipmentStore.inputForm.stdEq = [{ id: e[0].stdEqId, name: e[0].stdEqNm }];
    }
    typeofEquipmentPopup.value.onClose();
};

// 조직 팝업
const orgnPopup = ref(null);

const addOrgn = () => {
    orgnPopup.value.onOpen();
};

// 조직 요소 제거(x버튼 클릭)
const removeOrgn = index => {
    equipmentStore.inputForm.orgnIdList.splice(index, 1);
};
const applyOrgnPopup = data => {
    setSelectedOrgnData(data);
};
const closeOrgnPopup = () => {
    orgnPopup.value.onClose();
};

// 작업장 팝업
const workplacePopup = ref(null);

const addWorkplace = el => {
    workplacePopup.value.onOpen();
};
const applyWorkplacePopup = data => {
    setSelectedWpData(data);
};
const closeWorkplacePopup = () => {
    workplacePopup.value.onClose();
};

//
const setSelectedOrgnData = data => {
    equipmentStore.inputForm.orgnList = data;
    equipmentStore.inputForm.orgnIdList = data.map(el => (el.id = el.orgnId));
    // equipmentStore.inputForm.orgnNm = e.orgnNm;
    data.forEach(el => {
        el.id = el.orgnId;
        el.nm = el.orgnNm;
    });
    orgnPopup.value.onClose();
};
const setSelectedWpData = data => {
    equipmentStore.inputForm.workplaceList = data;
    equipmentStore.inputForm.workplaceIdList = data.map(el => el.workplaceId);
    // equipmentStore.inputForm.orgnNm = e.orgnNm;

    data.forEach(el => {
        el.id = el.workplaceId;
        el.nm = el.workplaceNm;
    });
    workplacePopup.value.onClose();
};

// 작업장 요소 제거(x버튼 클릭)
const removeWorkplace = index => {
    equipmentStore.inputForm.workplaceIdList.splice(index, 1);
};
</script>
