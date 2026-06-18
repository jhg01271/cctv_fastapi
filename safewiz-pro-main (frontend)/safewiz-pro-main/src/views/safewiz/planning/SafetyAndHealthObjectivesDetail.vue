<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p box"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="control-field ui form">
                <!-- 결재란 -->
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="safetyHealthStore.inputForm.cmd" targetType="SHO" :writeYear="safetyHealthStore.inputForm.writeYear" :docNo="safetyHealthStore.inputForm.docNo" :useYn="safetyHealthStore.inputForm.useYnMain" />
                </div>
                <hr />
                <!-- 상단보기 -->
                <div class="pa2-2rem" id="form">
                    <div class="row flex gutters1rem">
                        <!-- 작성년도 -->
                        <div class="grid10-2 el-grid12-4 sm-grid10-10">
                            <div class="field">
                                <label for="writeYear" required>
                                    <span>작성년도</span>
                                </label>
                                <input id="writeYear" required v-input type="text" v-model="safetyHealthStore.inputForm.writeYear" v-calendar="'yyyy'" class="datepicker w50p radius" year :readonly="safetyHealthStore.readonlyValue" @input="changedWriteYear" />
                            </div>
                        </div>
                        <!-- 제정일 -->
                        <div class="grid10-2 el-grid12-4 sm-grid10-10">
                            <div class="field">
                                <label :required="safetyHealthStore.requiredEnactedDt" for="enactedDt">
                                    <!-- <label :required="safetyHealthStore.requiredEnactedDt" for="enactedDt"> -->
                                    <span>제정일</span>
                                </label>
                                <input id="enactedDt" v-input v-model="model.enactedDt" :min="minDt" :max="maxDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :required="safetyHealthStore.requiredEnactedDt" :readonly="safetyHealthStore.readonlyValue || safetyHealthStore.readonlyValueEnactedDt" />
                            </div>
                        </div>
                        <!-- 개정일 -->
                        <div class="grid10-2 el-grid12-4 sm-grid10-10">
                            <div class="field">
                                <label for="revisedDt">
                                    <span>개정일</span>
                                </label>
                                <input id="revisedDt" v-input v-model="model.revisedDt" :min="minDt" :max="maxDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :readonly="safetyHealthStore.readonlyValueRevisedDt" />
                            </div>
                        </div>
                        <!-- 조직 -->
                        <div class="grid10-2 el-grid12-4 sm-grid10-10">
                            <div class="field">
                                <label for required>
                                    <span>조직</span>
                                </label>
                                <i-chips required :chips="safetyHealthStore.orgnItem" @popup="addOrgn" class="w100p" :readonly="safetyHealthStore.readonlyValue" @removeChip="resetInfo('')" />
                            </div>
                        </div>
                        <!-- 사용여부 -->
                        <div class="grid10-2 el-grid12-4 sm-grid10-10">
                            <div class="field">
                                <label>
                                    <span>사용여부</span>
                                </label>
                                <div class="df aic h4-4rem">
                                    <input type="checkbox" v-input="'사용'" class="switch" :checked="safetyHealthStore.inputForm.useYnMain === 'Y'" v-model="safetyHealthStore.inputForm.useYnMain" true-value="Y" false-value="N" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Detail -->
                    <div class="accordion df fdc gap8px mt2rem">
                        <div
                            v-for="(item, index) in safetyHealthStore.detailList"
                            :key="index"
                            class="list"
                            :class="{
                                'disabled-background': item.useYn === 'N'
                            }"
                        >
                            <!-- Accordion -->
                            <button type="button" class="df jcsb aic" @click="toggleAccordion(index, $event)" :class="{ active: activeSegments[index] }" :id="`list_${index}`">
                                <div class="df gap1rem aic es-w80p">
                                    <!-- CheckBox -->
                                    <input type="checkbox" v-input v-model="item.isChecked" @click.stop />
                                    <!-- Title -->
                                    <em class="ellipsis">{{ item.safetyHealthGoal }}</em>
                                </div>
                                <!-- Toggle -->
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <!-- Contents -->
                            <div ref="accordionRefs" class="segment oh">
                                <div class="df form fdc gap1-2rem bcF8F9FB br4px pa2-2rem" :id="item.isChecked ? 'cardForm' + index : null">
                                    <div class="w100p" @click="selectedIndex = index">
                                        <!-- 안전보건 목표 -->
                                        <div class="field mb1rem df aic jcsb us-fww">
                                            <label required :for="'safetyHealthGoal' + index">
                                                <span class="t1rem">{{ t('safetyHealthGoal') }}</span>
                                            </label>
                                            <div class="df aic gap1-6rem us-w100p us-mt1rem us-jcfe">
                                                <!-- 사용여부 -->
                                                <input type="checkbox" v-input="'사용'" class="switch" :checked="item.useYn === 'Y'" v-model="item.useYn" true-value="Y" false-value="N" @change="chkData(item)" />
                                                <!-- 추진 계획 선택 버튼 -->
                                                <button v-button class="btn active radius w13rem" @click="openSelectPlan">
                                                    <span>{{ t('selectPropulsionPlan') }}</span>
                                                </button>
                                            </div>
                                        </div>
                                        <!-- 안전보건 목표 Form -->
                                        <textarea :required="item.isChecked" :id="'safetyHealthGoal' + index" v-input v-model="item.safetyHealthGoal" type="text" class="w100p radius minh10rem" placeholder="안전보건 목표를 선택하세요" disabled @change="chkData(item)"> </textarea>
                                        <!-- 안전보건 세부목표 및 달성방법 -->
                                        <div class="field mt1rem">
                                            <label required :for="'detailGoalMethod' + index" class="pr es-pb8rem">
                                                <span>{{ t('detailGoalMethod') }}</span>

                                                <a @click="pageMove(item)" class="pa r0 fs1-5rem c3248F6 cp es-b0 us-neg-ls0-5px" v-if="item.safetyHealthGoal && item.cmd === 'U'">
                                                    중점추진사항별 세부계획 화면으로 이동
                                                    <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                                </a>
                                            </label>
                                            <textarea :required="item.isChecked" :id="'detailGoalMethod' + index" v-model="item.detailGoalMethod" type="text" class="w100p radius minh10rem" placeholder="안전보건 세부목표 및 달성방법을 입력하세요" :disabled="item.useYn === 'N'" @change="chkData(item)"> </textarea>
                                        </div>
                                        <!-- 추진일정 선택 -->
                                        <div class="field mt1rem">
                                            <label required>
                                                <span>추진일정</span>
                                            </label>
                                        </div>
                                        <div class="w100p ui">
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
                                                <table>
                                                    <thead>
                                                        <tr class="tac">
                                                            <td v-for="col in 12" :class="{ 'disabled-background': item.useYn === 'N' }" :key="'actionSchedule' + col">
                                                                {{ col }}
                                                            </td>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td
                                                                v-for="row in 12"
                                                                :key="row"
                                                                class="tac cp"
                                                                :class="{
                                                                    'disabled-background': item.useYn === 'N',
                                                                    bcEBEDFF: item[`checkableActionSchedule${row}`] === 'Y'
                                                                }"
                                                                @click="onClickSchedule(row)"
                                                            >
                                                                <input :id="`check${row}`" v-input type="checkbox" :checked="item[`actionSchedule${row}`] === 'Y'" disabled />
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </OverlayScrollbarsComponent>
                                        </div>
                                        <div class="df fdc gap1rem mt1rem">
                                            <!-- 담당자 선택 -->
                                            <div class="field">
                                                <label required>
                                                    <span>{{ t('hrNm') }}</span>
                                                </label>
                                                <i-chips :required="item.isChecked" :chips="[{ id: item.hrId, name: item.hrNm }]" @popup="addPeople()" @removeChip="removePeople()" :disabled="item.useYn === 'N'" @change="chkData(item)" />
                                            </div>
                                            <!-- 필요 자원 -->
                                            <div class="field">
                                                <label required :for="'requiredResource' + index">
                                                    <span>{{ t('requiredResource') }}</span>
                                                </label>
                                                <textarea :required="item.isChecked" :id="'requiredResource' + index" v-input v-model="item.requiredResource" type="text" class="w100p radius minh10rem" placeholder="필요 자원을 입력하세요" :disabled="item.useYn === 'N'" @change="chkData(item)" />
                                            </div>
                                            <!-- 평가 방법 -->
                                            <div class="field">
                                                <label required :for="'evaluationMethod' + index">
                                                    <span>{{ t('evaluationMethod') }}</span>
                                                </label>
                                                <textarea :required="item.isChecked" :id="'evaluationMethod' + index" v-input v-model="item.evaluationMethod" type="text" class="w100p radius minh10rem" placeholder="평가 방법을 입력하세요" :disabled="item.useYn === 'N'" @change="chkData(item)" />
                                            </div>
                                            <!-- 비고 -->
                                            <div class="field">
                                                <label id="">
                                                    <span>비고</span>
                                                </label>
                                                <textarea id="remark" v-model="item.remark" type="text" class="w100p radius minh10rem" placeholder="비고를 입력하세요" :disabled="item.useYn === 'N'" @change="chkData(item)"> </textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="w100p mt2rem tac aic">
                        <button type="button" @click="handleAddRow">
                            <svg class="mr8px vam" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                            </svg>
                            <span class="fs1-5rem">안전보건 추진 목표 추가</span>
                        </button>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>

    <!-- 담당자 선택 팝업 -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup" />
            </div>
        </i-PopupDialog>

        <!-- 조직 지정 팝업 -->
        <i-PopupDialog ref="orgnPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :selectedIdList="safetyHealthStore.inputForm.orgnIdList" :uniqueKey="[safetyHealthStore.inputForm.orgnId]" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganizationInSafetyAndHealthObjectives" :fetch-param="fetchParam" @close="closeOrgn" />
            </div>
        </i-PopupDialog>

        <!-- 추진계획선택 팝업 -->
        <i-PopupDialog ref="safetyAndHealthObjectivesPopup">
            <!-- 최대높이 60rem으로 제한 -->
            <div class="contents form ui w1280px el-w100p">
                <SafetyAndHealthObjectivesPopup @close="closeSafetyAndHealthObjectives" @selected="onSelectPlanData" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import ISignature from '@/components/common/iSignature.vue';
// Overlay
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganizationInSafetyAndHealthObjectives } from '@/stores/safewiz/planning/api/safetyAndHealthObjectives';
// BaseView
import BaseView from '@/components/base/BaseView';
import { gsap } from 'gsap';
// Router
import router from '@/router';
import SafetyAndHealthObjectivesPopup from '@/views/system/base/popup/SafetyAndHealthObjectivesPopup.vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useSafetyHealthStore } from '@/stores/safewiz/planning/safetyAndHealthObjectives';
import { useUserInfoStore } from '@/stores/user.js';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import { useDetailedActionPopupStore } from '@/stores/safewiz/participation/detailedActionPopup.js';
const detailedActionPopupStore = useDetailedActionPopupStore();

import { useDetailedActionStore } from '@/stores/safewiz/participation/detailedActionPlan.js';
const detailedActionStore = useDetailedActionStore();

const { ref, onMounted, t, validationStore, nextTick, confirmMsg, alertMsg, toastPopup, btnBack, btnSearch, btnSave, btnDelete, btnAdd, btnDownload, getCompId, formatDate, formatDateForDB, setRouterParam } = BaseView();
const layoutStore = useButtonListStore();
const safetyHealthStore = useSafetyHealthStore();
const userStore = useUserInfoStore();
const activeSegments = ref({});
const accordionRefs = ref([]);
const overlayScrollbars = ref(null);
const safetyAndHealthObjectivesPopup = ref(null);
const peoplePopup = ref(null);
const gubun = ref();
const orgnPopup = ref(null); // 조직 팝업
const signatureComponent = ref(); // 결재
const selectedIndex = ref(null); // 선택된 ROW Index

const model = ref({
    orgnId: safetyHealthStore.inputForm.orgnId,
    writeYear: safetyHealthStore.inputForm.writeYear,
    revisedDt: '',
    enactedDt: '',
    model: '',
    useYnMain: ''
});

const pageMove = async item => {
    //디테일 조회
    console.log('세부', item);
    let data = item;
    detailedActionPopupStore.actionKey = data.writeYear + '_' + data.actionDocType + '_' + data.actionDocNo + '_' + data.compId + '_' + data.actionDocSeq + '_' + data.actionDocDetailSeq;
    detailedActionPopupStore.param = {
        writeYear: data.writeYear,
        docType: data.actionDocType,
        docNo: data.actionDocNo,
        compId: data.compId,
        docSeq: data.actionDocSeq,
        detailItemId: data.detailItemId
    };
    const res = await detailedActionPopupStore.getActionMonthlyDetailList(detailedActionPopupStore.param, false);
    detailedActionPopupStore.isInputActive = false;
    //페이지 이동
    detailedActionStore.actionKey = detailedActionPopupStore.actionKey;
    detailedActionStore.param = detailedActionPopupStore.param;
    detailedActionStore.inputForm = res.list;
    detailedActionStore.isInputActive = false;
    router.push({
        name: 'DetailedActionPlanDetail'
    });
};
/* ---------- 조직 팝업 ---------- */
// 조직 팝업 오픈

const fetchParam = ref({
    compId: getCompId(),
    writeYear: safetyHealthStore.inputForm.writeYear
});

const addOrgn = el => {
    fetchParam.value.writeYear = safetyHealthStore.inputForm.writeYear;
    console.log('# fetchParam.value', fetchParam.value);
    orgnPopup.value.onOpen();
};

const closeOrgn = e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    if (e.length !== 0) {
        if (safetyHealthStore.inputForm.orgnId && safetyHealthStore.inputForm.orgnId !== e[0].orgnId) {
            // 조직 변경시 카드 내부 정보 초기화
            resetInfo(e[0].orgnId);
        }
        safetyHealthStore.orgnItem = [];
        safetyHealthStore.inputForm.orgnId = e[0].orgnId;

        for (let dt of e) {
            safetyHealthStore.orgnItem.push({
                id: dt.orgnId,
                name: dt.orgnNm
            });
            model.value.orgnId = dt.orgnId;
        }
    }
    orgnPopup.value.onClose();
};

// 카드 내부 정보 초기화
const resetInfo = orgnId => {
    let newForm = { ...safetyHealthStore.inputForm };
    detailedActionPopupStore.inputForm = [];
    newForm.isChecked = true;
    newForm.useYn = 'Y';
    newForm.orgnId = orgnId ? orgnId : '';
    safetyHealthStore.detailList[selectedIndex.value] = newForm;
};
/* ---------- 담당자(인원) 팝업 ---------- */
// 팝업 오픈
const addPeople = el => {
    gubun.value = el;
    peoplePopup.value.onOpen();
};

// 팝업 닫기
const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

// 담당자 선택
const selectPeople = e => {
    safetyHealthStore.detailList[selectedIndex.value].hrId = e.hrId;
    safetyHealthStore.detailList[selectedIndex.value].hrNm = e.hrNm;

    safetyHealthStore.detailList[selectedIndex.value].isChecked = true;

    closePeoplePopup();
};

// 담당자 제거(x버튼 클릭 시)
const removePeople = () => {
    safetyHealthStore.detailList[selectedIndex.value].hrId = '';
    safetyHealthStore.detailList[selectedIndex.value].hrNm = '';
};

// 로우 추가
const handleAddRow = async () => {
    const newForm = { ...safetyHealthStore.inputForm };
    newForm.isChecked = true;
    newForm.useYn = 'Y';
    newForm.cmd = 'I';
    safetyHealthStore.detailList.push(newForm);
    await toggleAccordion(safetyHealthStore.detailList.length - 1); // 신규 아코디언 오픈
    setTimeout(() => {
        const index = safetyHealthStore.detailList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 200);
};

// 개별 아코디언 토글 함수

const toggleAccordion = async (index, event = {}) => {
    if (event?.target?.tagName === 'LABEL') {
        return; // 체크박스 클릭인 경우 아무 작업도 하지 않음
    }
    // 선택 시 Focus 지정
    selectedIndex.value = index;

    activeSegments.value[index] = !activeSegments.value[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

const notifyGoBack = () => {
    safetyHealthStore.initInputForm();
    router.push({ name: 'SafetyAndHealthObjectives' });
};

/* --------- 버튼 클릭 이벤트 --------- */
btnSearch(async () => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속하시겠습니까?', '', {
        fun: searchDetailAction,
        param: safetyHealthStore.searchParam
    });
});
const searchDetailAction = async param => {
    await safetyHealthStore.searchDetail(param, true);
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
};

btnBack(() => {
    //상세화면에서 저장버튼을 누르지 않고 목록으로 돌아갈때
    if (!safetyHealthStore.isSaved) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속하시겠습니까?', '', { fun: notifyGoBack });
    }
    //저장버튼을 누르고 목록으로 돌아갈때
    else {
        notifyGoBack();
    }
});

btnSave(async () => {
    safetyHealthStore.signature = signatureComponent.value;
    if (safetyHealthStore.inputForm.useYnMain != 'Y') {
        safetyHealthStore.inputForm.useYnMain = 'N';
    }
    //신규저장시
    if (safetyHealthStore.inputForm.cmd === 'I') {
        const saveData = safetyHealthStore.detailList.filter(item => item.isChecked);

        if (saveData.length === 0) {
            alertMsg('warning', '선택된 데이터가 없습니다.');
            return false;
        }

        const isValid = await validationStore.validateAllFields('form', true); //상단 입력값 체크
        if (isValid && safetyHealthStore.inputForm.cmd === 'I') {
            for (let i of saveData) {
                i.compId = getCompId();
                i.orgnId = model.value.orgnId;
                i.revisedDt = model.value.revisedDt !== '' ? formatDateForDB(model.value.revisedDt) : null;
                i.enactedDt = formatDateForDB(model.value.enactedDt);
                i.writeYear = safetyHealthStore.inputForm.writeYear;
                i.docType = 'SHO';
                i.userId = userStore.userId;
                i.useYnMain = safetyHealthStore.inputForm.useYnMain;
            }
            let check = false; // 기본값을 false로 설정

            for (let i = 0; i < saveData.length; i++) {
                check = false; // 각 i 반복마다 check를 초기화

                for (let j = 0; j <= 12; j++) {
                    if (saveData[i][`actionSchedule${j}`] === 'Y') {
                        check = true; // 하나라도 'Y'이면 check를 true로 변경
                    } else if (saveData[i][`actionSchedule${j}`] === null) {
                        saveData[i][`actionSchedule${j}`] = 'N';
                    }
                }
            }
            if (!check) {
                alertMsg('warning', '추진일정은 필수값 입니다.');
                return false;
            }
            confirmMsg('info', '저장하시겠습니까?', '', {
                fun: safetyHealthStore.saveAction,
                param: saveData
            });
        }
    }
    //수정시
    else {
        const saveData = safetyHealthStore.detailList.filter(item => item.isChecked);

        if (saveData.length === 0) {
            // 해당 조직이 목표 및 중점추진사항의 담당조직이 아니면서 사용여부가 사용인 경우 조회
            const actionOrgnNList = safetyHealthStore.detailList.filter(item => item.actionOrgnYn === 'N' && item.useYn === 'Y');
            if (actionOrgnNList.length > 0 && safetyHealthStore.beforeUseYnMain === 'N' && safetyHealthStore.inputForm.useYnMain === 'Y') {
                alertMsg('warning', `[${model.value.orgnNm}]은 목표 및 중점추진사항에\n등록되어 있지 않아 사용여부를 변경할 수 없습니다.`);
                return;
            }

            let param = {
                writeYear: safetyHealthStore.inputForm.writeYear,
                docType: 'SHO',
                docNo: safetyHealthStore.inputForm.docNo,
                revisedDt: model.value.revisedDt !== '' ? formatDateForDB(model.value.revisedDt) : null,
                useYnMain: safetyHealthStore.inputForm.useYnMain
            };
            console.log('param', param);
            confirmMsg('info', '저장하시겠습니까?', '', {
                fun: safetyHealthStore.saveActionTop,
                param: param
            });
        } else {
            const isValid = await validationStore.validateAllFields('form', true); //상단 입력값 체크
            if (isValid) {
                for (let i of saveData) {
                    i.compId = getCompId();
                    i.orgnId = model.value.orgnId;
                    i.revisedDt = model.value.revisedDt !== '' ? formatDateForDB(model.value.revisedDt) : null;
                    i.enactedDt = formatDateForDB(model.value.enactedDt);
                    i.writeYear = safetyHealthStore.inputForm.writeYear;
                    i.docType = 'SHO';
                    i.userId = userStore.userId;
                    i.useYnMain = safetyHealthStore.inputForm.useYnMain;
                }
                let check = false; // 기본값을 false로 설정

                for (let i = 0; i < saveData.length; i++) {
                    check = false; // 각 i 반복마다 check를 초기화

                    for (let j = 0; j <= 12; j++) {
                        if (saveData[i][`actionSchedule${j}`] === 'Y') {
                            check = true; // 하나라도 'Y'이면 check를 true로 변경
                        } else if (saveData[i][`actionSchedule${j}`] === null) {
                            saveData[i][`actionSchedule${j}`] = 'N';
                        }
                    }
                }

                if (!check) {
                    alertMsg('warning', '추진일정은 필수값 입니다.');
                    return false;
                }

                // 수정하는 목표가 목표 및 중점추진사항에서 해당 조직이 담당조직으로 등록되지 않은 경우 조회
                const actionOrgnNList = saveData.filter(item => item.actionOrgnYn === 'N' && item.useYn === 'Y');
                if (actionOrgnNList.length > 0) {
                    alertMsg('warning', `[${model.value.orgnNm}]은 목표 및 중점추진사항에\n등록되어 있지 않아 사용여부를 변경할 수 없습니다.`);
                    return;
                }

                confirmMsg('info', '저장하시겠습니까?', '', {
                    fun: safetyHealthStore.saveAction,
                    param: saveData
                });
            }
        }
    }
});

btnDelete(() => {
    //수정시 삭제버튼
    safetyHealthStore.signature = signatureComponent.value;
    if (safetyHealthStore.inputForm.cmd === 'U') {
        const deleteData = safetyHealthStore.detailList.filter(item => item.isChecked);

        if (deleteData.length === 0) {
            alertMsg('warning', '선택된 데이터가 없습니다.');
            return false;
        }

        for (let i of deleteData) {
            i.compId = getCompId();
            i.userId = userStore.userId;
        }
        confirmMsg('info', '삭제하시겠습니까?', '', {
            fun: safetyHealthStore.deleteDetailAction,
            param: deleteData
        });
    }
    //신규등록시 삭제버튼
    else {
        const notCheckedData = safetyHealthStore.detailList.filter(item => !item.isChecked); //체크안된 데이터
        const checkedData = safetyHealthStore.detailList.filter(item => item.isChecked); //체크된 데이터
        if (checkedData.length === 0) {
            //체크된 데이터가 없으면
            alertMsg('warning', '선택된 데이터가 없습니다.');
            return false;
        }
        confirmMsg('info', '삭제하시겠습니까?', '', {
            fun: safetyHealthStore.deleteDetaionActionInsert,
            param: notCheckedData
        });
    }
});

btnDownload(() => {
    const downloadData = safetyHealthStore.detailList.filter(item => item.isChecked);

    if (downloadData.length > 0) {
        //아코디언을 체크하고 출력버튼을 눌렀을때 -> 체크된 데이터에 대해서만 출력
        confirmMsg('info', t('msgCheckedPrint'), '', {
            fun: safetyHealthStore.downloadReport,
            param: downloadData
        });
    } else {
        //아코디언에 아무 체크 안하고 출력버튼을 눌렀을때 -> 사용여부가 Y인 데이터만 출력
        confirmMsg('info', t('msgPrint'), '', {
            fun: safetyHealthStore.downloadReportUseYn,
            param: safetyHealthStore.detailList
        });
    }
});

btnAdd(() => {
    handleAddRow();
});

// const openSelectPlan = (param) => {
//     if (!safetyHealthStore.inputForm.writeYear) {
//         toastPopup('작성년도를 선택하여 주십시오.', 'error');
//         return false;
//     }
//
//     if (param) {
//         safetyAndHealthObjectivesPopup.value.onOpen();
//     }
//     // if (param) {
//     //     const popupData = safetyAndHealthObjectivesPopup.value.popupData; // 팝업의 데이터 확인
//     //     if (!popupData || Object.keys(popupData).length === 0) {
//     //         // 데이터가 없는 상황에 대한 처리
//     //         toastPopup('해당 팝업에 필요한 데이터가 없습니다.', 'error');
//     //         return;
//     //     }
//     //
//     //     safetyAndHealthObjectivesPopup.value.onOpen(); // 팝업 열기
//     // }
// };
const openSelectPlan = async param => {
    if (!safetyHealthStore.inputForm.writeYear) {
        toastPopup('작성년도를 선택하여 주십시오.', 'error');
        return false;
    }

    console.log(safetyHealthStore.orgnItem);

    if (safetyHealthStore.orgnItem.length === 0) {
        alertMsg('warning', '조직을 선택하여 주십시오.');
        return false;
    }

    try {
        detailedActionPopupStore.searchParam.writeYear = safetyHealthStore.inputForm.writeYear;
        detailedActionPopupStore.searchParam.orgnId = safetyHealthStore.inputForm.orgnId;
        const response = await detailedActionPopupStore.getActionMonthlyList(true);

        if (!response?.list || response.list.length === 0) {
            // 데이터가 없을 경우
            confirmMsg('warning', '안전보건 목표를 찾을 수 없습니다.');
            return false; // 팝업을 열지 않음
        } else {
            // 데이터가 있는 경우 팝업 열기

            if (param) {
                safetyAndHealthObjectivesPopup.value.onOpen();
            }
        }
    } catch (error) {
        // toastPopup(`데이터 로드 중 오류가 발생했습니다: ${error.message}`, 'error');
    }
};

// 추진계획선택 데이터
const onSelectPlanData = el => {
    onClickConfirmPlanData(el);
};

// 확인 클릭 이벤트
const onClickConfirmPlanData = el => {
    // 안전보건목표 텍스트 세팅
    safetyHealthStore.detailList[selectedIndex.value].safetyHealthGoal = el.detailPlan;
    safetyHealthStore.detailList[selectedIndex.value].actionDocType = el.docType;
    safetyHealthStore.detailList[selectedIndex.value].actionDocNo = el.docNo;
    safetyHealthStore.detailList[selectedIndex.value].actionDocSeq = el.docSeq;
    safetyHealthStore.detailList[selectedIndex.value].actionDocDetailSeq = el.docDetailSeq;

    // 선택 가능 컬럼 세팅
    for (const key in el) {
        if (key.startsWith('actionSchedule') && !key.endsWith('O')) {
            const number = key.match(/\d+$/)[0];
            const correspondingKey = `checkableActionSchedule${number}`;

            if (safetyHealthStore.detailList && safetyHealthStore.detailList[selectedIndex.value]) {
                if (Object.prototype.hasOwnProperty.call(safetyHealthStore.detailList[selectedIndex.value], correspondingKey)) {
                    safetyHealthStore.detailList[selectedIndex.value][correspondingKey] = el[key];
                }
            }
        }
    }

    // 닫기
    safetyAndHealthObjectivesPopup.value.onClose();
};

// 추진일정 선택
const onClickSchedule = month => {
    let val = safetyHealthStore.detailList[selectedIndex.value];

    // if (val.actionDocNo && !val[`checkableActionSchedule${month}`]) {
    //     return toastPopup('선택', '지정된 추진 일정만 선택이 가능합니다.', 'error');
    // }

    if (val[`actionSchedule${month}`] === 'N' || !val[`actionSchedule${month}`]) {
        val[`actionSchedule${month}`] = 'Y';
    } else {
        val[`actionSchedule${month}`] = 'N';
    }
    safetyHealthStore.detailList[selectedIndex.value].isChecked = true;
};

// 값 변경시 자동 체크
const chkData = item => {
    item.isChecked = true;
};

// 추진계획팝업 닫기
const closeSafetyAndHealthObjectives = () => {
    safetyAndHealthObjectivesPopup.value.onClose();
};

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    console.log('param', param);
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        safetyHealthStore.readonlyValueEnactedDt = true; //제정일 편집불가
        safetyHealthStore.readonlyValueRevisedDt = false; //개정일 편집가능

        param.compId = getCompId();

        await safetyHealthStore.searchDetail(param);

        model.value.orgnId = safetyHealthStore.inputForm.orgnId;
        model.value.orgnNm = safetyHealthStore.inputForm.orgnNm;

        safetyHealthStore.orgnItem = [
            {
                id: safetyHealthStore.inputForm.orgnId,
                name: safetyHealthStore.inputForm.orgnNm
            }
        ];

        model.value.revisedDt = formatDate(safetyHealthStore.inputForm.revisedDt);
        model.value.enactedDt = formatDate(safetyHealthStore.inputForm.enactedDt);
        model.value.useYn = safetyHealthStore.inputForm.useYn;

        // 상세 항목에서 해당 docSeq를 가진 index 찾아서 아코디언 오픈
        if (param.docSeq != null) {
            const index = safetyHealthStore.detailList.findIndex(item => item.docSeq === param.docSeq);
            if (index !== -1) {
                toggleAccordion(index);
            }
        }
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (!safetyHealthStore.inputForm || !safetyHealthStore.inputForm.cmd) {
        router.push('SafetyAndHealthObjectives');
        return;
    } else {
        // 추가버튼으로 왔을 때
        safetyHealthStore.requiredEnactedDt = true; //제정일필수입력
        safetyHealthStore.readonlyValueEnactedDt = false; //제정일 readonly false
        layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
        safetyHealthStore.inputForm.useYnMain = 'Y';
        safetyHealthStore.detailList = [];
        safetyHealthStore.orgnItem = [];
        handleAddRow();
    }

    minDt.value = `${safetyHealthStore.inputForm.writeYear}.01.01`;
    maxDt.value = `${safetyHealthStore.inputForm.writeYear}.12.31`;
    safetyHealthStore.signature = signatureComponent.value;
});

//작성년도 변경 감지
// watch(
//     () => safetyHealthStore.inputForm.writeYear,
//     () => {
//         //신규등록시 작성년도 변경감지
//         if (safetyHealthStore.inputForm.cmd === 'I') {
//             confirmMsg('info', '작성된 정보가 초기화됩니다.', '그래도 계속하시겠습니까?', {
//                 fun: initToggleAccordion,
//                 param: true,
//             });
//         }
//     },
// );

//신규등록시 작성년도를 변경했을때 아코디언을 초기화 하는 함수
const initToggleAccordion = () => {
    safetyHealthStore.detailList = [];
    const newForm = { ...safetyHealthStore.inputForm };

    newForm.isChecked = true;

    safetyHealthStore.detailList.push(newForm);

    toggleAccordion(0); // 신규 아코디언 오픈
};

const minDt = ref('');
const maxDt = ref('');
// 작성년도 변경 시 제정/개정일 범위 수정 및 조직 초기화
const changedWriteYear = () => {
    minDt.value = `${safetyHealthStore.inputForm.writeYear}.01.01`;
    maxDt.value = `${safetyHealthStore.inputForm.writeYear}.12.31`;
    safetyHealthStore.orgnItem = [];
};
</script>

<style scoped>
.disabled-background {
    background-color: #ecf0f4;
}
</style>
