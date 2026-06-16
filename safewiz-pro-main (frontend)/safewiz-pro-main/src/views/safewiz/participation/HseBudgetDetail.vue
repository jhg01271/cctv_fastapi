<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="hseBudgetStore.inputForm" class="contents df fdc">
        <div id="form" class="box form ui">
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
                <form @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-4 es-grid12-12">
                            <div class="field">
                                <label for>
                                    <span>작성년도</span>
                                </label>
                                <input type="text" class="datepicker w100p radius" id="writeYear" :value="hseBudgetStore.searchParam.writeYear" disabled required placeholder="ex) 2024" />
                            </div>
                        </div>
                        <div class="grid12-4 es-grid12-12">
                            <div class="field">
                                <label for="createdAt">등록일자</label>
                                <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="hseBudgetStore.currentDate" />
                            </div>
                        </div>
                        <div class="grid12-4 es-grid12-12">
                            <div class="field">
                                <label for="email">정렬</label>
                                <input type="number" class="w100p radius" id="ordSeq" v-model="hseBudgetStore.inputForm.ordSeq" disabled />
                            </div>
                        </div>
                    </div>

                    <div class="bcF9FAFF br4px pa2-2rem pt1rem mt2-2rem es-pa1rem">
                        <div class="row flex">
                            <div class="grid12-12">
                                <div class="field">
                                    <label class="pr" for required>
                                        목표
                                        <a @click="pageMove" class="pa r0 fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                            중점추진사항별 세부계획 화면으로 이동
                                            <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </a>
                                    </label>
                                    <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.companyObjective" disabled />
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for required>중점 추진 목표</label>
                                    <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.actionObjective" disabled />
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for required>세부 항목</label>
                                    <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.detailItem" disabled />
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for required>세부 계획</label>
                                    <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.detailPlan" disabled />
                                </div>
                            </div>
                        </div>

                        <hr class="my2rem" />

                        <div class="field">
                            <label>세부 추진 계획</label>
                            <label for required>담당조직</label>
                            <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.orgnNm" disabled />
                        </div>

                        <div class="field">
                            <label>성과지표</label>
                        </div>

                        <div class="bcFFFFFF br4px pa2-2rem pt1rem">
                            <div class="row flex gutters1rem">
                                <div class="grid12-3 sm-grid12-12">
                                    <div class="field">
                                        <label for>성과구분</label>
                                        <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.performanceTypeNm" disabled />
                                    </div>
                                </div>
                                <div class="grid12-3 sm-grid12-12">
                                    <div class="field">
                                        <label for>성과 반복</label>
                                        <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.performanceRepeatNm" disabled />
                                    </div>
                                </div>
                                <div class="grid12-3 sm-grid12-12">
                                    <div class="field">
                                        <label for>성과건수</label>
                                        <input type="number" class="w100p radius" step="1" min="0" v-model="hseBudgetStore.inputForm.performanceCnt" disabled />
                                    </div>
                                </div>
                                <div class="grid12-3 sm-grid12-12">
                                    <div class="field">
                                        <label for>직접 입력</label>
                                        <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.performanceCustom" disabled />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="field">
                            <label for>비고</label>
                            <input type="text" class="w100p radius" v-model="hseBudgetStore.inputForm.remark" disabled />
                        </div>

                        <div class="field">
                            <label>
                                <span>추진실적</span>
                            </label>
                        </div>
                        <OverlayScrollbarsComponent
                            class="br4px"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <table class="tac minw70rem">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule1 === 'Y' }">1</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule2 === 'Y' }">2</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule3 === 'Y' }">3</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule4 === 'Y' }">4</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule5 === 'Y' }">5</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule6 === 'Y' }">6</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule7 === 'Y' }">7</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule8 === 'Y' }">8</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule9 === 'Y' }">9</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule10 === 'Y' }">10</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule11 === 'Y' }">11</th>
                                        <th :class="{ active: hseBudgetStore.inputForm.actionSchedule12 === 'Y' }">12</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>실적</td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance1 === 'Y'" type="checkbox" name="actionPerformance1" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance2 === 'Y'" type="checkbox" name="actionPerformance2" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance3 === 'Y'" type="checkbox" name="actionPerformance3" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance4 === 'Y'" type="checkbox" name="actionPerformance4" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance5 === 'Y'" type="checkbox" name="actionPerformance5" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance6 === 'Y'" type="checkbox" name="actionPerformance6" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance7 === 'Y'" type="checkbox" name="actionPerformance7" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance8 === 'Y'" type="checkbox" name="actionPerformance8" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance9 === 'Y'" type="checkbox" name="actionPerformance9" v-input class="checkbox" @change="toggleCheckYn($event)" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance10 === 'Y'" type="checkbox" name="actionPerformance10" v-input class="checkbox" @change="toggleCheckYn($event)" :disabled="hseBudgetStore.inputForm.actionSchedule10 !== 'Y'" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance11 === 'Y'" type="checkbox" name="actionPerformance11" v-input class="checkbox" @change="toggleCheckYn($event)" :disabled="hseBudgetStore.inputForm.actionSchedule11 !== 'Y'" />
                                        </td>
                                        <td>
                                            <input :checked="hseBudgetStore.inputForm.actionPerformance12 === 'Y'" type="checkbox" name="actionPerformance12" v-input class="checkbox" @change="toggleCheckYn($event)" :disabled="hseBudgetStore.inputForm.actionSchedule12 !== 'Y'" />
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </OverlayScrollbarsComponent>

                        <hr class="my2rem" />

                        <div class="field">
                            <label>예산/실적 관리</label>
                        </div>
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 sm-grid12-12">
                                <div class="field">
                                    <label for required>{{ t('objectives_budget') + ' (원)' }}</label>
                                    <input type="text" v-input class="w100p radius" min="0" step="1" :value="formatToAmt(hseBudgetStore.inputForm.budget)" @blur="updateBudget" />
                                </div>
                            </div>
                            <div class="grid12-3 sm-grid12-12">
                                <div class="wsn field">
                                    <label for required>실적 (원)</label>
                                    <input type="text" class="w100p radius" :value="formatToManWon(totalPerformanceAmount)" disabled />
                                </div>
                            </div>
                            <div class="grid12-6 sm-grid12-12">
                                <div class="field">
                                    <label for>담당자</label>
                                    <i-chips :chips="hseBudgetStore.inputForm.hrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople" @removeChip="removePeople"></i-chips>
                                </div>
                            </div>
                        </div>

                        <div class="field">
                            <label for required>실적 내용</label>
                        </div>
                        <div class="bcFFFFFF br4px pa2-2rem es-pa1rem">
                            <div class="row flex gutters2rem">
                                <div class="grid12-6 lg-grid12-12" v-for="(item, index) in hseBudgetStore.inputForm.performanceList" :key="index" :class="[{ selected: item.checked }, { disabled: item.useYn == 'N' }]">
                                    <div class="df es-fww">
                                        <div class="checkbox-detail w5rem shrink0 df aic jcc bd1pxsolidE1E6ED es-h5rem es-bdr1pxsolidE1E6ED es-bdb0pxsolidE1E6ED es-w100p">
                                            <input type="checkbox" v-input v-model="item.checked" @click="handleCheckData(index)" />
                                            <!-- @change="handleCheckboxChange(index)" -->
                                        </div>

                                        <div class="grid-table w100p oh es-bdl1pxsolidE1E6ED">
                                            <div class="df us-fww">
                                                <dl>
                                                    <dt class="field">
                                                        <label :for="'contents' + index" required="true">
                                                            <span>내용</span>
                                                        </label>
                                                    </dt>

                                                    <dd class="field">
                                                        <label :for="'contents' + index" v-show="false" :required="item.checked">
                                                            <span>내용</span>
                                                        </label>
                                                        <input type="text" v-input class="w100p radius" @input="chkData(index)" v-model="item.contents" :required="item.checked" :id="'contents' + index" :readonly="item.useYn === 'N'" />
                                                    </dd>
                                                </dl>
                                                <dl>
                                                    <dt class="wsn field">
                                                        <label :for="'performanceAmount' + index" required="true">
                                                            <span>실적 (원)</span>
                                                        </label>
                                                    </dt>
                                                    <dd class="field">
                                                        <label :for="'performanceAmount' + index" v-show="false" :required="item.checked">
                                                            <span>실적</span>
                                                        </label>
                                                        <input type="text" v-input class="w100p radius" min="0" step="1" :id="'performanceAmount' + index" :required="item.checked" @input="updatePerform(index, item)" :value="formatToAmt(item.performanceAmount)" :readonly="item.useYn === 'N'" />
                                                    </dd>
                                                </dl>
                                                <dl>
                                                    <dt class="field">
                                                        <label>정렬</label>
                                                    </dt>
                                                    <dd>
                                                        <input type="number" class="w100p radius" min="0" max="99" step="1" placeholder="99" @input="handleNullToZero(item, index)" @change="chkData(index)" v-model="item.ordSeq" :readonly="item.useYn === 'N'" />
                                                    </dd>
                                                </dl>
                                                <dl>
                                                    <dt class="wsn field">
                                                        <label for>사용여부</label>
                                                    </dt>
                                                    <dd>
                                                        <input
                                                            v-input="'사용'"
                                                            type="checkbox"
                                                            :checked="item.useYn === 'Y'"
                                                            class="switch w100p radius"
                                                            @change="
                                                                event => {
                                                                    toggleUseYn(index, event);
                                                                    chkData(index);
                                                                }
                                                            "
                                                        />
                                                    </dd>
                                                </dl>
                                            </div>
                                            <div>
                                                <dl>
                                                    <dt class="field">
                                                        <label>실적 증빙 자료</label>
                                                    </dt>
                                                    <dd class="df jcc aic">
                                                        <iFileImage :ref="el => (fileListRefs[index] = el)" targetType="OBJ" :targetId="hseBudgetStore.inputForm.performanceList[index].fileId" @fileAdd="chkData(index)" @fileDel="chkData(index)" />
                                                    </dd>
                                                </dl>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- 추가 -->
                                <div class="grid12-6 ul-grid12-6 lg-grid12-12">
                                    <button class="box w100p h100p minh37rem df aic jcc gap8px" @click="hseBudgetStore.btnAdd()">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>

                                        <span class="fs1-5rem">실적 내용 추가</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
            <teleport to="body">
                <i-PopupDialog ref="peoplePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <h3>인원</h3>
                        <selectUser :single="false" :selected="hseBudgetStore.inputForm.hrList?.map(hr => hr.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                    </div>
                </i-PopupDialog>
            </teleport>
            <!-- 팝업 -->
            <!-- <i-PopupDialog>
        <div class="contents w1024px md-w100p">
            <h3>실적 증빙 자료 미리보기</h3>
            <div class="form ui">
                <table class="tac fixed mt1rem">
                    <tbody>
                        <tr>
                            <th>
                                <em>번호</em>
                            </th>
                            <th>
                                <em>내용</em>
                            </th>
                            <th>
                                <em>실적</em>
                            </th>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>안전모 구입</td>
                            <td>500,000원</td>
                        </tr>
                        <tr>
                            <th colspan="3">
                                <em>실적 증빙 자료</em>
                            </th>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <img src alt />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="df aic jcfe mt1rem gap8px">
                    <button type="button" class="btn radius active w7rem">
                        <span>이전</span>
                    </button>
                    <button type="button" class="btn radius active w7rem">
                        <span>다음</span>
                    </button>
                    <button type="button" v-button class="btn w80px radius bright-grey">
                        <span>닫기</span>
                    </button>
                </div>
            </div>
        </div>
            </i-PopupDialog>-->
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, confirmMsg, btnSearch, btnBack, btnAdd, btnSave, btnDelete, formatToManWon } = BaseView();
import router from '@/router';
import iFileImage from '@/components/file/iFileImage.vue';
import _ from 'lodash';

import { useHseBudgetStore } from '@/stores/safewiz/participation/hseBudget.js';
const hseBudgetStore = useHseBudgetStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

import { useDetailedActionStore } from '@/stores/safewiz/participation/detailedActionPlan.js';
const detailedActionStore = useDetailedActionStore();

const pageMove = async () => {
    //디테일 조회
    detailedActionStore.actionKey = hseBudgetStore.inputForm.writeYear + '_' + hseBudgetStore.inputForm.docType + '_' + hseBudgetStore.inputForm.docNo + '_' + hseBudgetStore.inputForm.compId + '_' + hseBudgetStore.inputForm.docSeq + '_' + hseBudgetStore.inputForm.docDetailSeq;
    detailedActionStore.param = {
        writeYear: hseBudgetStore.inputForm.writeYear,
        docType: hseBudgetStore.inputForm.docType,
        docNo: hseBudgetStore.inputForm.docNo,
        compId: hseBudgetStore.inputForm.compId,
        docSeq: hseBudgetStore.inputForm.docSeq,
        detailItemId: hseBudgetStore.inputForm.detailItemId
    };
    await detailedActionStore.getActionMonthlyDetailList(detailedActionStore.param, false);
    detailedActionStore.isInputActive = false;
    //페이지 이동
    router.push({
        name: 'DetailedActionPlanDetail'
    });

    // //페이지 이동
    // router.push({
    //     path: page
    // });

    // safetyHealthInfoStore.tempAccordion = activeSegments.value;
};

//실적 증빙 자료 파일 관리
const fileListRefs = ref([]);
const overlayScrollbars = ref(null);

// 조회 버튼 이벤트 함수
btnBack(() => {
    if (hseBudgetStore.inputForm) {
        // 추가 페이지로 이동
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: hseBudgetStore.goObject });
    }
});

btnSearch(() => {
    hseBudgetStore.getActionPerformanceDetailList(null, true);
});

btnAdd(() => {
    hseBudgetStore.btnAdd();
});
btnDelete(() => {
    hseBudgetStore.btnDelete('D');
});
btnSave(() => {
    detailSave();
});

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await hseBudgetStore.btnSave();
    }
};
// ---------------------------------------------------
//-----------------------------------------------
//useYn 체크

const toggleUseYn = (index, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    hseBudgetStore.inputForm.performanceList[index].useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
};

//-----------------------------------------------
//-----------------------------------------------
// [토글버튼]
const toggleCheckYn = event => {
    const field = event.target.name;
    const isChecked = event.target.checked;

    hseBudgetStore.inputForm[field] = isChecked ? 'Y' : 'N';
};
// ---------------------------------------------------
// ---------------------------------------------------
// [새로고침시 라우터 이동]
onMounted(async () => {
    if (Object.keys(hseBudgetStore.inputForm).length == 0) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    await initializeItems();
});

const initializeItems = async () => {
    //조직별 실적 신규 입력일 경우, 버튼 변경
    layoutStore.useBtnList = hseBudgetStore.buttonList;

    hseBudgetStore.setRefs(fileListRefs);

    fileListRefs.value.forEach(file => {
        if (file && file.fnSearch) {
            file.fnSearch();
        }
    });
};
//-----------------------------------------------

// 체크박스의 change 이벤트에서 scrollTop을 기록
const handleCheckboxChange = index => {
    const osInstance = overlayScrollbars.value?.osInstance;
    const element = overlayScrollbars.value.$el.querySelectorAll('.box')[index]; // 체크박스의 부모 요소 선택

    if (osInstance && element) {
        element.scrollIntoView({ behavior: 'auto', block: 'center' }); // 부드럽게 스크롤 이동
    }
    fileListRefs.value[index].checked = hseBudgetStore.inputForm.performanceList[index].checked;
};

// ---------------------------------------------------
const formatToAmt = amount => {
    if (typeof amount !== 'number' || amount === 0) {
        return null; // 숫자가 아닐 경우 예외 처리
    }

    // 숫자를 000 단위로 쉼표를 붙여서 변환
    const formattedAmount = amount.toLocaleString('ko-KR');

    // 뒤에 '만원'을 붙여서 반환
    return formattedAmount;
};

const updateBudget = event => {
    // 입력된 값에서 숫자 이외의 문자를 제거
    const inputValue = event.target.value;

    // 입력값이 숫자인지 확인 (정수 또는 소수점 포함)
    const numericValue = inputValue.replace(/[^0-9.]/g, ''); // 숫자와 소수점만 남김

    if (numericValue === '') {
        // 숫자가 하나도 없으면 null 반환
        hseBudgetStore.inputForm.budget = null;
    } else {
        // 숫자 변환 후 저장
        hseBudgetStore.inputForm.budget = parseFloat(numericValue);
    }
};

const totalPerformanceAmount = computed(() => {
    return hseBudgetStore.inputForm.performanceList
        ? hseBudgetStore.inputForm.performanceList.reduce((total, item) => {
              return total + (item.useYn === 'Y' ? item.performanceAmount || 0 : 0);
          }, 0)
        : 0;
});

const updatePerform = (index, item) => {
    chkData(index);
    const inputValue = event.target.value;

    // 숫자와 소수점만 남김
    const numericValue = inputValue.replace(/[^0-9.]/g, '');

    if (numericValue === '') {
        // 숫자가 하나도 없으면 null로 설정
        hseBudgetStore.inputForm.performanceList[index].performanceAmount = null;
    } else {
        // 숫자값으로 변환 후 저장
        hseBudgetStore.inputForm.performanceList[index].performanceAmount = parseFloat(numericValue);
    }
};

// 담당자 지정 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const peoplePopup = ref(null);
const addPeople = () => {
    peoplePopup.value.onOpen();
};
const removePeople = index => {
    hseBudgetStore.inputForm.hrList.splice(index, 1);
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
        //
        hseBudgetStore.inputForm.hrList = e;
    }
};

// ---------------------------------------------------
// 값이 바뀌면 해당 데이터 checked
const chkData = index => {
    hseBudgetStore.inputForm.performanceList[index].checked = true;
    // console.log('체크', fileListRefs.value);
    // fileInfoList를 업데이트하여 연동
    fileListRefs.value[index].checked = true; // fileInfoList.value와 같은 index 값을 사용한다고 가정
};

const handleCheckData = index => {
    hseBudgetStore.inputForm.performanceList[index].checked = !hseBudgetStore.inputForm.performanceList[index].checked;
};

const handleFileAdded = index => {
    chkData(index);
    // 파일 추가에 대한 처리를 추가할 수 있습니다.
};

// ---------------------------------------------------

const handleNullToZero = (data, index) => {
    if (data.ordSeq === null || data.ordSeq === '') {
        hseBudgetStore.inputForm.performanceList[index].ordSeq = 99;
    }
};
</script>
<style lang="scss" scoped>
.active {
    background-color: #ebedff;
    color: black;
}

.checkbox-detail {
    border-radius: 4px 0 0 4px;
}

.grid-table {
    border-radius: 0 4px 4px 0;

    div {
        dl {
            width: 100%;
            font-weight: 300;

            dt {
                border: 1px solid #e1e6ed;
                padding: 1.5rem;
                font-size: 1.5rem;
                background: #f8f9fb;
                color: #9ea1a6;
                text-align: center;
                border-bottom: none;
                border-left: none;
            }

            dd {
                border: 1px solid #e1e6ed;
                padding: 1rem;
                color: #111;
                min-height: 6.6rem;
                display: flex;
                align-items: center;
                border-left: none;
            }
        }

        &:last-of-type dt {
            border-top: none;
        }
    }

    @media (max-width: 420px) {
        div:first-of-type dl {
            display: flex;
            width: 100%;

            dt {
                width: 30%;
                line-height: 2.2;
            }

            dd {
                width: 70%;
                border-bottom: none;
            }
        }

        div:last-of-type dt {
            border-top: 1px solid #e1e6ed;
        }
    }
}
</style>
