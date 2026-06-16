<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="actionPlanStore && actionPlanStore.inputForm" class="contents df fdc">
        <div id="form" class="box form ui bcFFFFFF">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div v-if="actionPlanStore && actionPlanStore.inputForm" class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for>
                                    <span>작성년도</span>
                                </label>
                                <input type="text" class="datepicker w100p radius" id="writeYear" :value="actionPlanStore.searchParam.writeYear" disabled required placeholder="ex) 2024" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div v-if="actionPlanStore.inputForm.createdAt" class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(actionPlanStore.inputForm.createdAt)" disabled required placeholder="ex) 2024.11.20" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="ordSeq">{{ t('array') }}</label>
                                <input v-input type="number" v-model="actionPlanStore.inputForm.ordSeq" class="w100p radius" id min="0" max="99" step="1" placeholder="99" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field" :key="actionPlanStore.inputForm.useYn">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" :checked="actionPlanStore.inputForm.useYn === 'Y'" @change="actionPlanStore.toggleUseYn" class="df switch" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt2-2rem w100p bcF9FAFF pa2-2rem pt0 br4px">
                        <div class="row flex gutters1rem">
                            <div class="grid12-9 es-grid12-12">
                                <div class="field">
                                    <label for required :key="actionPlanStore.isInputActive">
                                        <span>목표</span>
                                    </label>
                                    <div v-if="actionPlanStore.inputForm.cmd === 'U' || actionPlanStore.isInputActive">
                                        <div>
                                            <input v-input type="text" class="w100p radius" v-model="actionPlanStore.inputForm.companyObjective" required id="compObjectiveId" placeholder="목표를 입력하세요." :readonly="!actionPlanStore.isInputActive" maxlength="200" />
                                        </div>
                                    </div>
                                    <div v-show="actionPlanStore.inputForm.cmd !== 'U'" v-if="!actionPlanStore.isInputActive">
                                        <select class="br4px" name="compObjectiveId" id="compObjectiveId" required v-select v-model="actionPlanStore.inputForm.compObjectiveId" :disabled="actionPlanStore.isInputActive">
                                            <option v-for="item in actionPlanStore.filteredObjectList" :key="item.compObjectiveId" :value="item.compObjectiveId">{{ item.companyObjective }}</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-3 es-grid12-12">
                                <div class="field mt4-4rem df aic gap1rem es-mt1rem">
                                    <div class="h4-4rem df aic">
                                        <input :checked="actionPlanStore.isInputActive" type="checkbox" v-input v-model="actionPlanStore.isInputActive" class="checkbox" />
                                    </div>
                                    <label v-if="actionPlanStore.inputForm.cmd !== 'U'" for>
                                        <span>신규</span>
                                    </label>
                                    <label v-else for>
                                        <span>수정</span>
                                    </label>
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for="actionObjective" required>
                                        <span>중점 추진 목표</span>
                                    </label>
                                    <input v-input type="text" class="w100p radius" id="actionObjective" v-model="actionPlanStore.inputForm.actionObjective" required placeholder="중점 추진 목표를 입력하세요." maxlength="200" />
                                </div>
                            </div>
                        </div>

                        <hr class="w100p h1px bcE1E6ED my2-2rem" />

                        <p class="fs1-6rem fw500 mb1rem">추진계획</p>

                        <div class="row flex gutters1rem">
                            <div class="grid12-9 sm-grid12-12">
                                <div class="field">
                                    <label for required>
                                        <span>담당조직</span>
                                    </label>
                                    <i-chips :chips="actionPlanStore.inputForm.orgnList" @popup="addOrgn" required></i-chips>
                                </div>
                            </div>
                            <div class="grid12-3 sm-grid12-12">
                                <div class="field">
                                    <label for required>{{ t('objectives_budget_won') }}</label>
                                    <input v-input type="text" :value="formatToAmt(actionPlanStore.inputForm.budget)" class="w100p radius" readonly />
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label>성과지표</label>
                                </div>

                                <div class="bcFFFFFF br4px pa2-2rem pt0">
                                    <div class="row flex gutters1rem">
                                        <div class="grid10-2 sm-grid12-12">
                                            <div class="field" :key="actionPlanStore.inputForm.performanceType">
                                                <label for required>
                                                    <span>성과 구분</span>
                                                </label>
                                                <select name id v-select v-model="actionPlanStore.inputForm.performanceType" @change="actionPlanStore.changeHandle(actionPlanStore.inputForm.performanceType)">
                                                    <option v-for="item in actionPlanStore.filteredGubunList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="grid10-2 sm-grid12-12">
                                            <div class="field" :key="actionPlanStore.inputForm.performanceRepeat">
                                                <label for="performanceRepeat" required>
                                                    <span>성과 반복</span>
                                                </label>
                                                <select name id="performanceRepeat" v-select v-model="actionPlanStore.inputForm.performanceRepeat" :disabled="actionPlanStore.inputForm.performanceType !== '0001'" :required="actionPlanStore.inputForm.performanceType == '0001'">
                                                    <option v-for="item in actionPlanStore.filteredRepeatList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="grid10-2 sm-grid12-12">
                                            <div class="field">
                                                <label for required>
                                                    <span>성과건수</span>
                                                </label>
                                                <input type="number" v-input v-model="actionPlanStore.inputForm.performanceCnt" class="w100p radius" min="0" placeholder="0" :disabled="!['0001', '0002'].includes(actionPlanStore.inputForm.performanceType)" @input="validateInput(actionPlanStore.inputForm.resultCnt, $event)" />
                                            </div>
                                        </div>
                                        <div class="grid10-2 sm-grid12-12">
                                            <div class="field">
                                                <label for="performanceType" :required="actionPlanStore.inputForm.performanceType == '0000'">
                                                    <span>직접입력</span>
                                                </label>
                                                <input v-input type="text" class="w100p radius" id="performanceType" :required="actionPlanStore.inputForm.performanceType == '0000'" v-model="actionPlanStore.inputForm.performanceCustom" :disabled="actionPlanStore.inputForm.performanceType !== '0000'" />
                                            </div>
                                        </div>
                                        <div class="grid10-2 sm-grid12-12">
                                            <div class="field mt4-4rem lg-mt0">
                                                <label for>
                                                    <!-- 건수 -->
                                                    <span v-show="actionPlanStore.inputForm.performanceType !== '0000'">
                                                        {{ actionPlanStore.inputForm.performanceCnt === 0 ? '' : `${actionPlanStore.inputForm.performanceCnt} ${actionPlanStore.inputForm.performanceCnt ? '건 /' : ''}` }}
                                                    </span>
                                                    <!-- 성과 반복 -->
                                                    <span v-show="actionPlanStore.inputForm.performanceType !== '0000'">{{ performanceRepeatName }} {{ performanceRepeatName ? '/' : '' }}</span>

                                                    <!-- 직접입력 -->
                                                    <span v-show="actionPlanStore.inputForm.performanceType !== '0000'">{{ performanceTypeName }}</span>
                                                    <span>{{ actionPlanStore.inputForm.performanceType === '0000' ? actionPlanStore.inputForm.performanceCustom : '' }}</span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>추진일정</span>
                                    </label>
                                </div>
                                <table class="tac">
                                    <thead>
                                        <tr>
                                            <th>1분기</th>
                                            <th>2분기</th>
                                            <th>3분기</th>
                                            <th>4분기</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <input type="checkbox" v-input :checked="actionPlanStore.inputForm.actionSchedule1 === 'Y'" name="actionSchedule1" @change="toggleCheckYn" />
                                            </td>
                                            <td>
                                                <input type="checkbox" v-input :checked="actionPlanStore.inputForm.actionSchedule2 === 'Y'" name="actionSchedule2" @change="toggleCheckYn" />
                                            </td>
                                            <td>
                                                <input type="checkbox" v-input :checked="actionPlanStore.inputForm.actionSchedule3 === 'Y'" name="actionSchedule3" @change="toggleCheckYn" />
                                            </td>
                                            <td>
                                                <input type="checkbox" v-input :checked="actionPlanStore.inputForm.actionSchedule4 === 'Y'" name="actionSchedule4" @change="toggleCheckYn" />
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for>비고</label>
                                    <input v-input type="text" class="w100p radius" v-model="actionPlanStore.inputForm.remark" placeholder="비고를 입력하세요" />
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" :selectedIdList="actionPlanStore.inputForm.orgnList?.map(el => el.id)" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getOrganization" @close="closeOrgn" @apply="applyOrgn" />
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
        </teleport>
    </div>
</template>
<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, getCompId, confirmMsg, formatDate, formatToAmt, btnSearch, btnBack, btnAdd, btnSave, btnDelete } = BaseView();
import router from '@/router';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
// import _ from 'lodash';

// [시스템코드 조회]
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

// [목표 조회]
import { getObjective } from '@/stores/safewiz/participation/api/actionPlanApi.js';

import { getOrganization } from '@/stores/system/base/api/organizationApi.js';

// [스토어 연결]
import { useActionPlanStore } from '@/stores/safewiz/participation/actionPlan.js';
const actionPlanStore = useActionPlanStore();

//-----------------------------------------------
// [버튼리스트]

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
const iButtonList = ['btnBack', 'btnSave'];

btnBack(() => {
    if (JSON.stringify(actionPlanStore.originData) !== JSON.stringify(actionPlanStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: actionPlanStore.goObject, param: '' });
    } else {
        actionPlanStore.goObject();
    }
});

btnSearch(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (JSON.stringify(actionPlanStore.originData) !== JSON.stringify(actionPlanStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: actionPlanStore.getActPlanDetailList, param: actionPlanStore.param });
    } else {
        actionPlanStore.param = {
            writeYear: actionPlanStore.inputForm.writeYear,
            docType: actionPlanStore.inputForm.docType,
            docNo: actionPlanStore.inputForm.docNo,
            docSeq: actionPlanStore.inputForm.docSeq,
            compId: actionPlanStore.inputForm.compId
        };
        actionPlanStore.getActPlanDetailList(actionPlanStore.param, true);
    }
});
btnAdd(() => {
    if (JSON.stringify(actionPlanStore.originData) !== JSON.stringify(actionPlanStore.inputForm)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: actionPlanStore.btnAdd, param: actionPlanStore.inputForm });
    } else {
        actionPlanStore.btnAdd(actionPlanStore.inputForm);
    }
});
btnDelete(() => {
    actionPlanStore.btnDelete('D');
});
btnSave(() => {
    detailSave();
});

//-----------------------------------------------
// [onMounted]

const compId = getCompId();

onMounted(async () => {
    console.log('actionPlanStore.inputForm', actionPlanStore.inputForm);
    if (Object.keys(actionPlanStore.inputForm).length === 0) {
        // 새로고침 시
        router.push('/ObjectivesAndActionPlan');
    }
    if (actionPlanStore.inputForm?.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else if (actionPlanStore.inputForm?.cmd === 'U') {
        layoutStore.useBtnList = uButtonList;
    }

    try {
        //성과 select box 리스트
        let responses = await Promise.all([
            getSystemCode({
                majorCd: 'C0011',
                compId: compId
            }),
            getSystemCode({
                majorCd: 'C0012',
                compId: compId
            }),
            getObjective()
        ]);
        actionPlanStore.gubunList = responses[0].list;
        actionPlanStore.repeatList = responses[1].list;
        actionPlanStore.objectList = responses[2].list;
    } catch (error) {
        console.error('데이터를 가져오는 중 오류 발생:', error);
    }
});

//-----------------------------------------------
const performanceTypeName = computed(() => {
    const type = actionPlanStore.filteredGubunList.find(item => item.minorCd === actionPlanStore.inputForm.performanceType);
    return type ? type.minorNm : ''; // 값이 없을 경우 빈 문자열 반환
});

// performanceRepeatName 계산
const performanceRepeatName = computed(() => {
    const repeat = actionPlanStore.filteredRepeatList.find(item => item.minorCd === actionPlanStore.inputForm.performanceRepeat);
    return repeat ? repeat.minorNm : ''; // 값이 없을 경우 빈 문자열 반환
});

//-----------------------------------------------
// [조직 팝업]
const orgnPopup = ref(null);

// 팝업열기
const addOrgn = () => {
    orgnPopup.value.onOpen();
};

// 팝업닫기
const closeOrgn = () => {
    orgnPopup.value.onClose();
};

// 선택버튼
const applyOrgn = e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    orgnPopup.value.onClose();
    if (e && e.length) {
        // multi 일때
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm
        }));
        actionPlanStore.inputForm.orgnList = refinedItems;
    }
};
//-----------------------------------------------
// [토글버튼]
const toggleCheckYn = event => {
    const field = event.target.name;
    const isChecked = event.target.checked;

    actionPlanStore.inputForm[field] = isChecked ? 'Y' : 'N';
};
// ---------------------------------------------------

//-----------------------------------------------
// [숫자입력 폼]
const validateInput = (item, event) => {
    const value = event.target.value; // 입력된 값을 가져옴

    // 입력값이 숫자가 아닐 경우
    if (isNaN(value) || value.trim() === '') {
        item = 0; // 기본값으로 리셋
        return; // 유효성 검사 종료
    }

    // 문자열을 숫자로 변환
    const numericValue = parseInt(value);

    // 0 미만일 경우
    if (numericValue < 0) {
        item = 0; // 기본값으로 리셋
    } else {
        item = numericValue; // 유효한 값으로 설정
    }
};
//-----------------------------------------------

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await actionPlanStore.btnSave();
    }
};

// ---------------------------------------------------
// [새로고침시 라우터 이동]
onMounted(async () => {
    if (!actionPlanStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
});
// ---------------------------------------------------
</script>
