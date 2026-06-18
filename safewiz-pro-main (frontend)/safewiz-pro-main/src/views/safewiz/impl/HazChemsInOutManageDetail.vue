<template>
    <div id="form" class="contents df fdc">
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
            <form ref="myForm">
                <div class="control-field ui form box pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>작업장</span>
                                </label>
                                <i-chips :chips="[{ id: hazChemsInoutStore.inputForm.workplaceId, name: hazChemsInoutStore.inputForm.workplaceNm }]" @popup="addWorkplace" @removeChip="removeWorkplace" required :readonly="hazChemsInoutStore.inputForm.cmd === 'U'"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-8 es-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>유해화학물질(MSDS)</span>
                                </label>
                                <i-chips :chips="[{ id: hazChemsInoutStore.inputForm.hazmatId, name: hazChemsInoutStore.inputForm.hazmatNm }]" @popup="addMsds" @removeChip="removeMsds" required :readonly="hazChemsInoutStore.inputForm.cmd === 'U'"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :true-value="'Y'" :false-value="'N'" :checked="hazChemsInoutStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="hazChemsInoutStore.inputForm.useYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for>CAS NO.</label>
                                <input class="br4px" type="text" v-input v-model="hazChemsInoutStore.inputForm.casNo" readonly />
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="field">
                                <label for>관용명/동의어</label>
                                <input class="br4px" type="text" v-input v-model="hazChemsInoutStore.inputForm.msdsSynonym" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for>취급량/일</label>
                                <input class="br4px" type="text" v-input v-model="hazChemsInoutStore.inputForm.dailyVolume" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for>취급시간</label>
                                <input class="br4px" type="text" v-input v-model="hazChemsInoutStore.inputForm.dailyTime" readonly />
                            </div>
                        </div>
                    </div>
                    <!-- 🐳 accordion 1 -->
                    <div class="accordion mt2rem">
                        <div class="list mb8px" v-for="(days, key) in hazChemsInoutStore.yearMonthMap" :key="key">
                            <button type="button" class="radius w15rem df jcsb aic" @click="toggleAccordion">
                                <div class="df gap1rem aic wbka us-fww">
                                    <input type="checkbox" v-model="hazChemsInoutStore.yearMonthMap[key].checked" v-input />

                                    <em>{{ formatYearMonth(key) }}</em>
                                    <p>
                                        (입출고 건수 :
                                        {{
                                            days
                                                .filter(el => el.totalQty != null && el.totalQty != 0) // totalInQty가 0이 아닌 값만 필터링
                                                .map(el => el.totalQty)[0] || 0
                                        }}
                                        / 재고량 :
                                        {{
                                            days
                                                .filter(el => el.storeQty != null && el.storeQty != 0) // totalStoreQty가 0이 아닌 값만 필터링
                                                .map(el => el.storeQty)
                                                .slice(-1)[0] || 0
                                        }}
                                        )
                                    </p>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <div class="pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        ref="overlayScrollbars"
                                        class="maxh27-5rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="es-minw34rem">
                                            <colgroup>
                                                <col class="w15p" />
                                                <col class="w20p" />
                                                <col class="w20p" />
                                                <col class="w20p" />
                                                <col class="w20p" />
                                            </colgroup>
                                            <thead>
                                                <tr class="h4-4rem">
                                                    <th>일자</th>
                                                    <th>입고량</th>
                                                    <th>출고량</th>
                                                    <th>재고량</th>
                                                    <th>비고</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <!--날짜를 1부터 31까지 순차적으로 표시 -->
                                                <tr v-for="(entry, index) in days" :key="entry.inoutDt">
                                                    <!--일자를 1부터 출력 -->
                                                    <td class="tac">{{ index + 1 }}</td>
                                                    <!-- 날짜를 1부터 31까지 출력 -->
                                                    <td>
                                                        <input class="tar" type="number" v-model="entry.inQty" @change="hazChemsInoutStore.markAsUpdated(entry, key, index)" />
                                                    </td>
                                                    <td>
                                                        <input class="tar" type="number" v-model="entry.outQty" @change="hazChemsInoutStore.markAsUpdated(entry, key, index)" />
                                                    </td>
                                                    <td>
                                                        <input class="tar" type="number" v-model="entry.storeQty" @change="hazChemsInoutStore.markAsUpdated(entry, key, index)" />
                                                    </td>
                                                    <td>
                                                        <input type="text" v-model="entry.remark" @change="hazChemsInoutStore.markAsUpdated(entry, key, index)" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr class="bcF8F9FB">
                                                    <td class="tac">
                                                        <em>합계</em>
                                                    </td>
                                                    <td class="tac">
                                                        <em>{{ getSum(days, 'inQty') }}</em>
                                                    </td>
                                                    <td class="tac">
                                                        <em>{{ getSum(days, 'outQty') }}</em>
                                                    </td>
                                                    <td class="tac">
                                                        <em>{{ getSum(days, 'storeQty') }}</em>
                                                    </td>
                                                    <td class="tac">
                                                        <em></em>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </OverlayScrollbarsComponent>
        <!-- 작업장 지정 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="workplacePopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'작업장'" :selectedIdList="[{ workplaceId: hazChemsInoutStore.inputForm.workplaceId, workplaceNm: hazChemsInoutStore.inputForm.workplaceNm }]" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getWp" @close="closeWorkplacePopup" />
                </div>
            </i-PopupDialog>

            <!-- msds 팝업 -->
            <i-PopupDialog ref="msdsPopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'유해화학물질(MSDS)'" :selectedIdList="[{ hazmatId: hazChemsInoutStore.inputForm.hazmatId, hazmatNm: hazChemsInoutStore.inputForm.hazmatNm }]" uniqueKey="msdsId" filterKey="msdsNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ workplaceId: hazChemsInoutStore.inputForm.workplaceId }" :fetch-data="getMsdsByWorkplace" @close="closeMsdsPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance, toRaw } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';

// 스토어
import { useHazChemsInoutStore } from '@/stores/safewiz/impl/hazChemsInout.js';
const hazChemsInoutStore = useHazChemsInoutStore();

// =====================================================

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const { t, validationStore, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, computed /* formatDate, watch, calculateMinutes, toastPopup, getCompId */ } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];

const days = Array.from({ length: 31 }, (_, i) => i + 1);

// =====================================================

// 년도와 월 형식 포맷
const formatYearMonth = key => {
    const year = key.slice(0, 4);
    const month = key.slice(4);
    return `${year}년 ${month}월`;
};

const getSum = (days, field) => {
    if (field === 'storeQty') {
        // storeQty의 경우, 배열을 뒤에서부터 순회하며 마지막 값을 반환
        for (let i = days.length - 1; i >= 0; i--) {
            if (days[i].storeQty !== null && days[i].storeQty !== '' && days[i].storeQty !== 0) {
                return days[i].storeQty;
            }
        }
        return 0; // storeQty 값이 없으면 기본값 0 반환
    } else {
        // 그 외의 경우 합계를 계산
        return days.reduce((sum, entry) => sum + Number(entry[field] || 0), 0);
    }
};

onMounted(async () => {
    // // Step 2: 연도별 월-일 데이터 맵 생성
    // await hazChemsInoutStore.createYearMonthHashMap();
    // Step 3: cmd 유효성 검사
    if (!hazChemsInoutStore.inputForm.cmd) {
        // cmd가 없을 경우 이전 화면으로 이동
        hazChemsInoutStore.goBack();
        return;
    }

    // Step 4: inputForm.cmd에 따라 버튼과 counselDt 설정
    if (hazChemsInoutStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
        hazChemsInoutStore.inputForm.counselDt = hazChemsInoutStore.currentDate;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});
// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(hazChemsInoutStore.originData);
    const currentData = toRaw(hazChemsInoutStore.inputForm);
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
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', hazChemsInoutStore.goBack);
    hazChemsInoutStore.resetAccordionState();
});

btnAdd(() => {
    confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: hazChemsInoutStore.btnAdd });
    hazChemsInoutStore.resetAccordionState();
});

btnSearch(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await hazChemsInoutStore.btnDetail();
    hazChemsInoutStore.resetAccordionState();
});
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await hazChemsInoutStore.btnSave(true);
    hazChemsInoutStore.resetAccordionState();
});
btnDelete(() => {
    hazChemsInoutStore.btnDeleteDetail();
    hazChemsInoutStore.resetAccordionState();
});
btnDownload(() => {
    if (hazChemsInoutStore.inputForm.useYn === 'N') {
        alertMsg('warning', '사용여부를 확인해주세요.');
        return;
    }
    if (hazChemsInoutStore.inputForm.cmd === 'I') {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
        return
    }
    confirmMsg('info', t('msgPrint'), null, { fun: btnDownloadAction, param: '' });
});

const btnDownloadAction = () => {
    let list = [hazChemsInoutStore.inputForm.docNo];
    hazChemsInoutStore.btnDetailDownload(list);
    hazChemsInoutStore.resetAccordionState();
}


//-----------------------------------------------
// [아코디언]
import { nextTick } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

//-----------------------------------------------
//-----------------------------------------------
// 작업장 팝업
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getWp } from '@/stores/system/base/api/workplaceApi';

const workplacePopup = ref(null);

const addWorkplace = () => {
    workplacePopup.value.onOpen();
};

const closeWorkplacePopup = data => {
    setSelectedWpData(data);
};

const setSelectedWpData = data => {
    if (data[0]) {
        hazChemsInoutStore.inputForm.workplaceId = data[0].workplaceId;
        hazChemsInoutStore.inputForm.workplaceNm = data[0].workplaceNm;
    }
    workplacePopup.value.onClose();
};

// 작업장 요소 제거(x버튼 클릭)
const removeWorkplace = index => {
    hazChemsInoutStore.inputForm.workplaceId = null;
    hazChemsInoutStore.inputForm.workplaceNm = null;
    hazChemsInoutStore.inputForm.msdsList = [];
};

//-----------------------------------------------
//-----------------------------------------------
// msds 팝업

import { getMsdsByWorkplace } from '@/stores/safewiz/impl/api/hazChemsInoutApi.js';

const msdsPopup = ref(null);

const addMsds = () => {
    if (hazChemsInoutStore.inputForm.workplaceId) {
        msdsPopup.value.onOpen();
    } else {
        alertMsg('warning', '작업장을 먼저 선택해주세요.');
        return;
    }
};

const closeMsdsPopup = data => {
    if (data[0]) {
        setSelectedMsdsData(data);
    } else msdsPopup.value.onClose();
};
const setSelectedMsdsData = data => {
    if (!data[0]) return;

    const { msdsId, msdsNm, msdsSynonym, casNo, dailyVolume, dailyTime } = data[0];
    const inputForm = hazChemsInoutStore.inputForm;

    // 데이터 설정
    Object.assign(inputForm, { hazmatId: msdsId, hazmatNm: msdsNm, msdsSynonym, casNo, dailyVolume, dailyTime });

    // 유효성 검사 후 동작
    if (!validateInputForm()) {
        hazChemsInoutStore.inputForm.hazmatId = null;
        hazChemsInoutStore.inputForm.hazmatNm = null;
    }

    msdsPopup.value.onClose();
};

const validateInputForm = () => {
    const { searchList, inputForm } = hazChemsInoutStore;
    const inputKey = `${inputForm.workplaceId}${inputForm.hazmatId}`;

    // 중복 검사
    if (searchList.some(({ workplaceId, hazmatId }) => `${workplaceId}${hazmatId}` === inputKey)) {
        alertMsg('warning', '중복된 데이터가 있습니다.');

        return false;
    }
    return true;
};

// 작업장 요소 제거(x버튼 클릭)
const removeMsds = index => {
    hazChemsInoutStore.inputForm.hazmatId = null;
    hazChemsInoutStore.inputForm.hazmatNm = null;
};

//-----------------------------------------------
//-----------------------------------------------

//-----------------------------------------------
</script>
