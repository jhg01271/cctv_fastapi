<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div id="form" class="box form ui bcFFFFFF">
            <OverlayScrollbarsComponent
                class="h100p pa2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div ref="myForm">
                    <div class="field">
                        <label for>
                            <span>작성년도</span>
                        </label>
                        <input type="text" class="datepicker w100p radius" id="writeYear" :value="detailedActionStore.searchParam.writeYear" disabled required placeholder="ex) 2024" />
                    </div>

                    <div class="w100p mt2rem pa2-2rem bcF9FAFF br4px" v-if="detailedActionStore.inputForm && detailedActionStore.inputForm[0]">
                        <div class="row flex gutters1rem">
                            <div class="grid12-12">
                                <div class="field">
                                    <label class="pr" for required>
                                        <span>목표</span>
                                        <a @click="pageMove" class="pa r0 fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                            목표 및 중점 추진사항 화면으로 이동
                                            <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </a>
                                    </label>
                                    <input type="text" class="w100p radius" id="companyObjective" :value="detailedActionStore.inputForm[0].companyObjective" disabled required />
                                </div>
                            </div>

                            <div class="grid12-12">
                                <div class="field">
                                    <label for required>
                                        <span>중점 추진 목표</span>
                                    </label>
                                    <input type="text" class="w100p radius" id="actionObjective" :value="detailedActionStore.inputForm[0].actionObjective" disabled required />
                                </div>
                            </div>

                            <div class="grid12-12">
                                <div class="field">
                                    <label for="detailItem" required>
                                        <span>세부 항목</span>
                                    </label>
                                    <input v-input type="text" class="w100p radius" v-model="detailedActionStore.inputForm[0].detailItem" id="detailItem" required placeholder="세부 항목을 입력하세요." maxlength="200" />
                                </div>
                            </div>

                            <!-- 선택된 경우 아래 .box 요소에 .selected 클래스를 토글 시켜주시면 됩니다. -->
                            <div class="grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>세부 추진 계획</span>
                                    </label>
                                </div>
                                <div class="field" v-if="detailedActionStore.inputForm[0].cmd == 'I'">
                                    <span class="fs1-5rem lh1-4">* 세부 추진 계획은 세부 항목 저장 후 입력 가능합니다.</span>
                                </div>
                            </div>
                        </div>
                        <div v-show="detailedActionStore.inputForm[0].cmd !== 'I'" v-for="(result, indexA) in detailedActionStore.inputForm" :key="indexA" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: result.checked }, { disabled: result.useYn == 'N' }]">
                            <div class="df aic jcc w5rem shrink0 bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000" :key="result.checked">
                                <input type="checkbox" v-input v-model="result.checked" />
                            </div>
                            <div class="w100pcalc5rem pa2-2rem sm-w100p">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-4 lg-grid12-5 es-grid12-12">
                                        <div class="field">
                                            <label for="createdAt">{{ t('createdAt') }}</label>
                                            <input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(result.createdAt)" disabled />
                                        </div>
                                    </div>
                                    <div class="grid12-4 lg-grid12-5 es-grid12-6">
                                        <div class="field">
                                            <label>
                                                <span>{{ t('array') }}</span>
                                            </label>

                                            <input v-input type="number" v-model="result.ordSeq" class="w100p radius" min="0" max="99" step="1" placeholder="99" @change="chkData(result)" @input="handleNullToZero(result, indexA)" />
                                        </div>
                                    </div>
                                    <div class="grid12-4 lg-grid12-2 es-grid12-6">
                                        <div class="field" :key="result.useYn">
                                            <label for="useYn">{{ t('useYn') }}</label>
                                            <div class="df aic h4-4rem">
                                                <input
                                                    v-input="'사용'"
                                                    type="checkbox"
                                                    :checked="result.useYn === 'Y'"
                                                    class="df switch"
                                                    @change="
                                                        event => {
                                                            toggleUseYn(indexA, event);
                                                            chkData(result);
                                                        }
                                                    "
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label :for="'detailPlan' + indexA" required>
                                                <span>세부 계획</span>
                                            </label>
                                            <input @input="chkData(result)" type="text" v-input :id="'detailPlan' + indexA" :required="result.checked && detailedActionStore.inputForm[0].cmd == 'U'" class="w100p radius" v-model="result.detailPlan" placeholder="세부계획을 입력하세요" />
                                        </div>
                                    </div>

                                    <div class="grid12-9 es-grid12-12">
                                        <div class="field">
                                            <label>
                                                <span>담당 조직</span>
                                            </label>
                                            <i-chips :chips="result.orgnList" @popup="addOrgn(indexA)" :readonly="true"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-3 es-grid12-12">
                                        <div class="field">
                                            <label>{{ t('objectives_budget_won') }}</label>
                                            <input v-input type="text" :value="formatToAmt(result.budget)" class="w100p radius oh" readonly />
                                        </div>
                                    </div>

                                    <div class="grid10-2 lg-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label :for="'performanceType' + indexA" required>
                                                <span>성과 구분</span>
                                            </label>
                                            <div :key="result.performanceType">
                                                <select
                                                    @change="
                                                        event => {
                                                            detailedActionStore.changeHandle(result.performanceType, indexA);
                                                            chkData(result);
                                                        }
                                                    "
                                                    v-select
                                                    :id="'performanceType' + indexA"
                                                    v-model="result.performanceType"
                                                    :required="result.checked && detailedActionStore.inputForm[0].cmd == 'U'"
                                                >
                                                    <option v-for="item in detailedActionStore.filteredGubunList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid10-2 lg-grid12-6 es-grid12-12">
                                        <div class="field" :key="result.performanceType">
                                            <label :for="'performanceRepeat' + indexA" required>
                                                <span>성과 반복</span>
                                            </label>
                                            <select
                                                @change="
                                                    event => {
                                                        chkData(result);
                                                    }
                                                "
                                                :disabled="result.performanceType !== '0001'"
                                                v-select
                                                :id="'performanceRepeat' + indexA"
                                                v-model="result.performanceRepeat"
                                                :required="result.checked && result.performanceType === '0001' && detailedActionStore.inputForm[0].cmd == 'U'"
                                            >
                                                <option v-for="item in detailedActionStore.filteredRepeatList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="grid10-2 lg-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label :for="'performanceCnt' + indexA" required>
                                                <span>성과 건수</span>
                                            </label>
                                            <input type="number" v-input v-model="result.performanceCnt" class="w100p radius" min="0" :id="'performanceCnt' + indexA" :required="result.checked && detailedActionStore.inputForm.cmd == U && (result.performanceType == '0001' || result.performanceType == '0002')" :disabled="result.performanceType !== '0001' && result.performanceType !== '0002'" @change="chkData(result)" />
                                        </div>
                                    </div>
                                    <div class="grid10-2 lg-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for required>
                                                <span>직접입력</span>
                                            </label>
                                            <input v-input type="text" class="w100p radius" v-model="result.performanceCustom" :disabled="result.performanceType !== '0000'" :required="result.checked && detailedActionStore.inputForm.cmd == U && result.performanceType == '0000'" @change="chkData(result)" />
                                        </div>
                                    </div>
                                    <div class="grid10-2 lg-grid12-12">
                                        <div class="field mt4-4rem lg-mt0">
                                            <label for>
                                                <!-- 건수 -->
                                                <span v-show="result.performanceType !== '0000'">
                                                    {{ result.performanceCnt === 0 ? '' : `${result.performanceCnt} ${result.performanceCnt ? '건 /' : ''}` }}
                                                </span>
                                                <span v-show="result.performanceType !== '0000'">{{ performanceRepeatName(result) }} {{ performanceRepeatName(result) ? '/' : '' }}</span>

                                                <span v-show="result.performanceType !== '0000'">{{ performanceTypeName(result) }}</span>
                                                <span>{{ result.performanceType === '0000' ? result.performanceCustom : '' }}</span>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for required>
                                                <span>추진일정</span>
                                            </label>

                                            <OverlayScrollbarsComponent
                                                class="br4px"
                                                ref="overlayScrollbars"
                                                :options="{
                                                    scrollbars: {
                                                        //autoHide: 'hover',
                                                        x: 'visible',
                                                        y: 'hidden'
                                                    }
                                                }"
                                            >
                                                <table class="tac minw950px">
                                                    <thead>
                                                        <tr>
                                                            <th v-for="month in TableMonths" :key="month">{{ month }}</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule1O === 'Y' }">
                                                                <input :checked="result.actionSchedule1 === 'Y'" type="checkbox" name="actionSchedule1" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule1O === 'Y' }">
                                                                <input :checked="result.actionSchedule2 === 'Y'" type="checkbox" name="actionSchedule2" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule1O === 'Y' }">
                                                                <input :checked="result.actionSchedule3 === 'Y'" type="checkbox" name="actionSchedule3" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule2O === 'Y' }">
                                                                <input :checked="result.actionSchedule4 === 'Y'" type="checkbox" name="actionSchedule4" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule2O === 'Y' }">
                                                                <input :checked="result.actionSchedule5 === 'Y'" type="checkbox" name="actionSchedule5" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule2O === 'Y' }">
                                                                <input :checked="result.actionSchedule6 === 'Y'" type="checkbox" name="actionSchedule6" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule3O === 'Y' }">
                                                                <input :checked="result.actionSchedule7 === 'Y'" type="checkbox" name="actionSchedule7" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule3O === 'Y' }">
                                                                <input :checked="result.actionSchedule8 === 'Y'" type="checkbox" name="actionSchedule8" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule3O === 'Y' }">
                                                                <input :checked="result.actionSchedule9 === 'Y'" type="checkbox" name="actionSchedule9" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule4O === 'Y' }">
                                                                <input :checked="result.actionSchedule10 === 'Y'" type="checkbox" name="actionSchedule10" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule4O === 'Y' }">
                                                                <input :checked="result.actionSchedule11 === 'Y'" type="checkbox" name="actionSchedule11" v-input class="checkbox cp" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                            <td :class="{ active: detailedActionStore.inputForm[0].actionSchedule4O === 'Y' }">
                                                                <input :checked="result.actionSchedule12 === 'Y'" type="checkbox" name="actionSchedule12" v-input class="checkbox" @change="toggleCheckYn(indexA, $event)" />
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </OverlayScrollbarsComponent>
                                        </div>
                                    </div>

                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>비고</label>
                                            <input v-input type="text" @input="chkData(result)" v-model="result.remark" class="w100p radius" placeholder="비고를 입력하세요." />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="w100p df aic jcc mt1rem" v-if="detailedActionStore.inputForm[0].cmd === 'U'">
                            <button type="button" @click="detailedActionStore.addPlan()" class="radius pt2rem pb2rem df aic">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                                <span class="mx1rem">세부추진계획 추가</span>
                            </button>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->

            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :inputForm="detailedActionStore.inputForm.orgnList" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getOrganization" @close="closeOrgn" @apply="applyOrgn" />
            </div>
        </i-PopupDialog>
    </div>
</template>
<script setup>
import { ref, onMounted /* computed, defineProps, defineModel */ } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, confirmMsg, formatToAmt, getCompId, formatDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete /* confirmMsg, getFormattedDate, watch */ } = BaseView();
import router from '@/router';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import _ from 'lodash';
//시스템코드 조회
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

// [조직 조회]
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';

//-----------------------------------------------
// [스토어]
import { useDetailedActionStore } from '@/stores/safewiz/participation/detailedActionPlan.js';
const detailedActionStore = useDetailedActionStore();
//-----------------------------------------------

const actionPlanStore = useActionPlanStore();
import { useActionPlanStore } from '@/stores/safewiz/participation/actionPlan.js';

//-----------------------------------------------
// [버튼리스트]
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const overlayScrollbars = ref(null);

const pageMove = async () => {
    let e = detailedActionStore.inputForm[0];
    //디테일 조회
    // actionPlanStore.objectiveId = e.objectiveId;
    actionPlanStore.actionKey = e.writeYear + '_' + e.docType + '_' + e.docNo + '_' + e.compId + '_' + e.docSeq;
    actionPlanStore.param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo,
        compId: e.compId,
        docSeq: e.docSeq
    };
    await actionPlanStore.getActPlanDetailList(actionPlanStore.param, false);
    actionPlanStore.isInputActive = false;
    //페이지 이동
    router.push({
        name: 'ObjectivesAndActionPlanDetail'
    });
};

// 조회 버튼 이벤트 함수
btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: detailedActionStore.goObject, param: '' });
});

btnSearch(() => {
    detailedActionStore.getActionMonthlyDetailList(detailedActionStore.param, true);
});
btnAdd(() => {
    detailedActionStore.addPlan();
});
btnDelete(() => {
    detailedActionStore.btnDelete('D');
});
btnSave(() => {
    detailSave();
});
// ---------------------------------------------------

const TableMonths = Array.from({ length: 12 }, (_, i) => i + 1);
// ---------------------------------------------------

const compId = getCompId();

onMounted(async () => {
    if (detailedActionStore.inputForm && detailedActionStore.inputForm[0]?.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnSave'];
    }
    //성과 select box 리스트
    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0011',
            compId: compId
        }),
        getSystemCode({
            majorCd: 'C0012',
            compId: compId
        })
    ]);
    detailedActionStore.gubunList = responses[0].list;
    detailedActionStore.repeatList = responses[1].list;

    // [새로고침시 라우터 이동]
    if (!detailedActionStore.inputForm || !detailedActionStore.inputForm[0]) {
        // 새로고침 시 이전 화면으로 이동
        router.push('/DetailedActionPlan');
    }
});

// ---------------------------------------------------
// 값이 바뀌면 해당 데이터 checked
const chkData = data => {
    data.checked = true;
};
// ---------------------------------------------------
const orgnPopup = ref();
const orgnIdx = ref(null);
const addOrgn = index => {
    orgnIdx.value = index;
    orgnPopup.value.onOpen();
};

const applyOrgn = e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    orgnPopup.value.onClose();
    if (e && e.length) {
        // multi 일때
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm
        }));
        detailedActionStore.inputForm[orgnIdx.value].orgnList = refinedItems;
    }
};

const closeOrgn = () => {
    orgnPopup.value.onClose();
};

// ---------------------------------------------------
const performanceTypeName = result => {
    const type = detailedActionStore.filteredGubunList.find(item => item.minorCd === result.performanceType);
    return type ? type.minorNm : ''; // 값이 없으면 빈 문자열 반환
};

const performanceRepeatName = result => {
    const type = detailedActionStore.filteredRepeatList.find(item => item.minorCd === result.performanceRepeat);
    return type ? type.minorNm : ''; // 값이 없으면 빈 문자열 반환
};

//-----------------------------------------------
// [토글버튼]
const toggleCheckYn = (index, event) => {
    detailedActionStore.inputForm[index].checked = true;
    const field = event.target.name;
    const isChecked = event.target.checked;

    detailedActionStore.inputForm[index][field] = isChecked ? 'Y' : 'N';
};

//-----------------------------------------------
//useYn 체크

const toggleUseYn = (index, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    detailedActionStore.inputForm[index].useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
};

//-----------------------------------------------

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await detailedActionStore.btnSave();
    }
};

const handleNullToZero = (data, index) => {
    if (data.ordSeq === null || data.ordSeq === '') {
        detailedActionStore.inputForm[index].ordSeq = 99;
    }
};
// ---------------------------------------------------

// ---------------------------------------------------
</script>
<style scoped>
.active {
    background-color: #ebedff;
    /* 원하는 배경색으로 변경 */
    color: black;
    /* 텍스트 색상 변경 (선택 사항) */
}
</style>
