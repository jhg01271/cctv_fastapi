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
                <div ref="myForm">
                    <div v-if="safetyChecklistStore.inputForm" class="control-field ui form pa2-2rem">
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 sm-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>설비유형</span>
                                    </label>
                                    <i-chips :chips="[{ id: safetyChecklistStore.inputForm.stdEqId, name: safetyChecklistStore.inputForm.stdEqNm }]" @popup="addTypeofEquipment" @removeChip="removeTypeofEquipment()" :readonly="safetyChecklistStore.inputForm.cmd === 'U'" required></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-12">
                                <div class="field">
                                    <label for required>
                                        <span>제목</span>
                                    </label>
                                    <input class="br4px" type="text" v-input v-model="safetyChecklistStore.inputForm.title" required id="제목" />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>등록일자</span>
                                    </label>
                                    <input :value="formatDate(safetyChecklistStore.inputForm.createdAt)" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                                </div>
                            </div>

                            <div class="grid12-1 ul-grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for>정렬</label>
                                    <input class="br4px" type="number" min="0" step="1" placeholder="99" v-input v-model="safetyChecklistStore.inputForm.ordSeq" />
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" type="checkbox" :checked="safetyChecklistStore.inputForm.useYn === 'Y'" @change="changedUseYn()" class="df switch" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-3 es-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>점검주기</span>
                                    </label>
                                    <select v-select v-model="safetyChecklistStore.inputForm.inspectionCycle" id="inspectionCycle">
                                        <option v-for="item in checkDivList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-3 es-grid12-6">
                                <div class="h100p field df aife">
                                    <button class="w100p h4-4rem fs1-6rem br4px cFFFFFF bc3248F6" @click="safetyCheckInspectorPopUp">안전점검 담당자 관리</button>
                                </div>
                            </div>
                            <div class="grid12-6 es-grid12-6"></div>
                        </div>
                        <!-- 🐳 accordion 1 -->
                        <div class="accordion mt2rem">
                            <div class="list">
                                <button type="button" class="radius w15rem df jcsb aic es-minw50px" @click="toggleAccordion">
                                    <div>
                                        <!--🐸 title -->
                                        <em>{{ `점검항목 (${safetyChecklistStore.inputForm.itemList?.length ?? 0}건)` }}</em>
                                    </div>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <!--🦖 contents -->
                                <div class="segment oh">
                                    <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                        <div class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(item, indexI) in safetyChecklistStore.inputForm.itemList" :key="indexI">
                                            <div class="w5rem df jcc aic bdr1pxsolidE1E6ED us-w100p us-h5rem us-bdr0pxsolidE1E6ED us-bdb1pxsolidE1E6ED">
                                                <input type="checkbox" v-input v-model="item.checked" />
                                            </div>
                                            <div class="w100pcalc5rem us-w100p">
                                                <div class="pa1rem">
                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-9 ul-grid12-8 es-grid12-12">
                                                            <div class="field">
                                                                <label :for="'checkItem' + indexI" required>
                                                                    <span>점검항목명</span>
                                                                </label>
                                                                <input class="br4px" type="text" v-input :id="'checkItem' + indexI" :required="item.checked" v-model="item.checkItem" @input="chkData(indexI)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-1 ul-grid12-2 es-grid12-6">
                                                            <div class="field">
                                                                <label for>순서</label>
                                                                <input class="br4px" type="number" min="0" step="1" placeholder="99" v-input v-model="item.ordSeq" @input="chkData(indexI)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-2 ul-grid12-2 es-grid12-6">
                                                            <div class="field">
                                                                <label :for="'item.useYn' + indexI">사용여부</label>
                                                                <div class="df aic h4-4rem" :key="item.useYn">
                                                                    <input
                                                                        v-input="'사용'"
                                                                        :checked="item.useYn === 'Y'"
                                                                        :id="'item.useYn' + indexI"
                                                                        type="checkbox"
                                                                        class="switch"
                                                                        @change="
                                                                            event => {
                                                                                // 현재 상태의 반대값으로 변경
                                                                                handleChange($event, indexI);
                                                                                chkData(indexI); // 추가 작업
                                                                            }
                                                                        "
                                                                    />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="w100p df aic jcc">
                                            <button type="button" class="df aic" @click="safetyChecklistStore.itemAdd">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                                <span class="mx1rem">추가</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 🐳 accordion 2 -->
                        <div class="accordion mt8px">
                            <div class="list">
                                <button type="button" class="radius w15rem df jcsb aic" @click="toggleAccordion">
                                    <div>
                                        <!--🐸 title -->
                                        <em>{{ `점검사항 (${safetyChecklistStore.inputForm.detailList?.length ?? 0}건)` }}</em>
                                        <span v-show="safetyChecklistStore.inputForm.cmd === 'I'"> (점검항목을 먼저 등록,저장 한 후 점검사항을 입력할 수 있습니다.)</span>
                                    </div>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div class="segment oh">
                                    <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                        <div class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(detail, indexD) in safetyChecklistStore.inputForm.detailList" :key="indexD">
                                            <div class="w5rem df jcc aic bdr1pxsolidE1E6ED us-w100p us-h5rem us-bdr0pxsolidE1E6ED us-bdb1pxsolidE1E6ED">
                                                <input type="checkbox" v-input :id="'checkDetail' + indexD" :required="detail.checked" v-model="detail.checked" @input="chkDataD(indexD)" />
                                            </div>
                                            <div class="w100pcalc5rem pa2-2rem us-w100p">
                                                <div class="row flex gutters1rem">
                                                    <div v-show="detail.itemUseYn == 'N'" class="grid12-9 ul-grid12-8 es-grid12-12">
                                                        <div class="field">
                                                            <label :for="'checkItemSeq' + indexD" required>
                                                                <span>점검항목명 (점검항목이 삭제됨)</span>
                                                            </label>
                                                            <input class="br4px" type="text" v-input v-model="detail.checkItem" :disabled="detail.itemUseYn == 'N'" />
                                                        </div>
                                                    </div>
                                                    <div v-show="detail.itemUseYn == 'Y'" class="grid12-9 ul-grid12-8 es-grid12-12">
                                                        <div class="field" :key="safetyChecklistStore.filteredYN">
                                                            <label :for="'checkItemSeq' + indexD" required>
                                                                <span>점검항목명</span>
                                                            </label>
                                                            <select name v-select :id="'checkItemSeq' + indexD" v-model="detail.checkItemSeq" :required="detail.checked" @change="chkDataD(indexD)">
                                                                <option v-for="item in safetyChecklistStore.filteredYN" :key="item.checkItemSeq" :value="item.checkItemSeq">{{ item.checkItemNm }}</option>
                                                            </select>
                                                        </div>
                                                    </div>

                                                    <div class="grid12-1 ul-grid12-2 es-grid12-6">
                                                        <div class="field">
                                                            <label for>순서</label>
                                                            <input class="br4px" type="number" min="0" step="1" placeholder="99" v-input v-model="detail.ordSeq" @input="chkDataD(indexD)" />
                                                        </div>
                                                    </div>

                                                    <div class="grid12-2 ul-grid12-2 es-grid12-6">
                                                        <div class="field">
                                                            <label :for="'detail.useYn' + indexD">사용여부</label>
                                                            <div class="df aic h4-4rem" :key="detail.itemUseYn">
                                                                <input
                                                                    v-input="'사용'"
                                                                    :checked="detail.itemUseYn == 'Y'"
                                                                    :id="'detail.useYn' + indexD"
                                                                    type="checkbox"
                                                                    class="switch"
                                                                    @change="
                                                                        event => {
                                                                            // 현재 상태의 반대값으로 변경
                                                                            safetyChecklistStore.inputForm.itemList[indexD].itemUseYn = detail.itemUseYn ? 'N' : 'Y';
                                                                            chkDataD(indexD); // 추가 작업
                                                                        }
                                                                    "
                                                                />
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label :for="'checkList' + indexD" required>
                                                                <span>점검사항</span>
                                                            </label>
                                                            <textarea class="minh10rem br4px" name :id="'checkList' + indexD" placeholder="점검사항을 입력해주세요." v-model="detail.checkList" :required="detail.checked" @input="chkDataD(indexD)"></textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="w100p df aic jcc" v-show="safetyChecklistStore.inputForm.cmd === 'U'">
                                            <button type="button" class="df aic" @click="safetyChecklistStore.detailAdd">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                                <span class="mx1rem">추가</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <OverlayScrollbarsComponent
                            class="mt1-5rem h100p"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="field">
                                <label for>
                                    <span>점검주기 변경 이력</span>
                                </label>
                            </div>
                            <table class="lg-minw700px wbka lh1-4">
                                <colgroup>
                                    <col class="w30p" />
                                    <col />
                                    <col />
                                </colgroup>
                                <thead>
                                    <tr class="h4-4rem">
                                        <th>{{ t('변경일시') }}</th>
                                        <th>{{ t('변경된 주기') }}</th>
                                        <th>{{ t('변경자') }}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <template v-for="(hist, index) in safetyChecklistStore.inputForm.historyList" :key="index">
                                        <tr class="h4-4rem" id="tableForm">
                                            <td class="tac">
                                                {{ hist.createdAt.split('.')[0]?.replaceAll('-', '.') }}
                                            </td>
                                            <td class="tac">
                                                {{ hist.inspectionCycleNm }}
                                            </td>
                                            <td class="tac">
                                                {{ hist.hrNm }}
                                            </td>
                                        </tr>
                                    </template>
                                </tbody>
                            </table>
                        </OverlayScrollbarsComponent>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <!-- 설비유형 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="typeofEquipmentPopup">
                <!-- 단일 그리드 -->

                <div class="contents w500px md-w100p">
                    <!-- <TypeofEquipmentPopup :selected="safetyChecklistStore.inputForm.stdEqId" :options="{ label: '설비 유형', readonly: false }" @close="closeTypeofEquipmentPopup" /> -->
                    <base-select-popup :title="'설비 유형'" uniqueKey="stdEqId" filterKey="stdEqNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getStdEqList" :btnInfo="{ close: true }" @close="closeTypeofEquipmentPopup" />
                    <!-- 
                :fetchParam="sAndHTrainingImplStore.searchParam"-->
                </div>
            </i-PopupDialog>

            <!-- 안전점검 담당자 관리 팝업 -->
            <i-PopupDialog ref="safetyCheckInspectorPopup">
                <div class="contents form ui w680px md-w100p">
                    <CheckInspectorPopup @close="closesafetyCheckInspector" @select="selectSafetyCheckInspector" :type-name="safetyChecklistStore.inputForm.stdEqNm" :type-id="safetyChecklistStore.inputForm.stdEqId" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, nextTick, toRaw } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { gsap } from 'gsap';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getDateFormat } from '@/utils/dateUtil.js';

//-----------------------------------------------
// [스토어]
import { useSafetyChecklistStore } from '@/stores/safewiz/impl/safetyChecklist.js';
const safetyChecklistStore = useSafetyChecklistStore();

const { confirmMsg, alertMsg, onMounted, validationStore, t, formatDate, getCompId, btnSearch, btnBack, btnSave, btnDelete, btnCopy } = BaseView();
const layoutStore = useButtonListStore();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnCopy'];
const iButtonList = ['btnBack', 'btnSave'];

onMounted(async () => {
    if (history.state.stdEqId) {
        // 다른 라우터에서 넘어왔을 경우 (ex.안전점검일지관리 상세)
        await safetyChecklistStore.getChecklistDetail({ searchText: history.state.stdEqId }, true);
    } else if (Object.keys(safetyChecklistStore.inputForm).length === 0) {
        // 새로고침시 이전 화면으로 이동.
        router.push('SafetyMgmtGuidelines');
        return;
    }
    if (safetyChecklistStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
    setSystemCode();
});

const changedUseYn = () => {
    if (safetyChecklistStore.inputForm.useYn == 'Y') {
        safetyChecklistStore.inputForm.useYn = 'N';
    } else if (safetyChecklistStore.inputForm.useYn == 'N') {
        safetyChecklistStore.inputForm.useYn = 'Y';
    }
};

//-----------------------------------------------
// [시스템코드 조회]
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
let checkDivList = ref();
const setSystemCode = () => {
    getSystemCode({
        majorCd: 'C0046',
        compId: getCompId()
    }).then(res => {
        checkDivList.value = res.list;
    });
};

//-----------------------------------------------
//-----------------------------------------------

// 아코디언

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
const handleChange = (event, index) => {
    const newValue = safetyChecklistStore.inputForm.itemList[index].useYn === 'Y' ? 'N' : 'Y';
    safetyChecklistStore.inputForm.itemList[index].useYn = newValue;

    // 'N'일 때만 confirmMsg를 띄우기
    if (newValue === 'N') {
        alertMsg('info', '항목 삭제시 해당 점검사항도 사용할 수 없습니다.');
    }
};
//-----------------------------------------------
// 안전점검 담당자 관리 팝업
import CheckInspectorPopup from '@/views/system/base/popup/CheckInspectorPopup.vue';

const safetyCheckInspectorPopup = ref(null);

const safetyCheckInspectorPopUp = () => {
    safetyCheckInspectorPopup.value.onOpen();
};
const closesafetyCheckInspector = () => {
    safetyCheckInspectorPopup.value.onClose();
};

const selectSafetyCheckInspector = selectedItem => {
    //closePopup(JobUsePop);
};

// 설비유형 팝업
import TypeofEquipmentPopup from '@/views/system/base/popup/TypeofEquipmentPopup.vue';
import { getStdEqList } from '@/stores/system/base/api/equipmentApi';
const typeofEquipmentPopup = ref(null);

const addTypeofEquipment = el => {
    typeofEquipmentPopup.value.onOpen();
};
// 설비 유형 제거 (x버튼 클릭 시)
const removeTypeofEquipment = () => {
    safetyChecklistStore.inputForm.stdEqId = null;
};

// 설비 유형 팝업 닫기
const closeTypeofEquipmentPopup = e => {
    if (e && e[0]) {
        safetyChecklistStore.inputForm.stdEqId = e[0].stdEqId;
        safetyChecklistStore.inputForm.stdEqNm = e[0].stdEqNm;
    }
    typeofEquipmentPopup.value.onClose();
    safetyChecklistStore.getSafetyCheckYn({ searchText: safetyChecklistStore.inputForm.stdEqId, writeYear: safetyChecklistStore.searchParam.writeYear }, false);
};
//-----------------------------------------------
//-----------------------------------------------

// 자동체크함수
const chkData = index => {
    safetyChecklistStore.inputForm.itemList[index].checked = true;
};

const chkDataD = index => {
    safetyChecklistStore.inputForm.detailList[index].checked = true;
};

//-----------------------------------------------
//-----------------------------------------------
// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(safetyChecklistStore.originData);
    const currentData = toRaw(safetyChecklistStore.inputForm);
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
    // 초기 데이터를 저장
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', safetyChecklistStore.goBack);
});

btnSearch(() => {
    const initialData = toRaw(safetyChecklistStore.originData);
    const currentData = toRaw(safetyChecklistStore.inputForm);
    if (JSON.stringify(initialData) !== JSON.stringify(currentData)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', '', { fun: searchAction, param: '' });
    } else {
        searchAction();
    }
});

const searchAction = () => {
    const searchParam = {
        compId: getCompId(),
        docNo: safetyChecklistStore.inputForm.docNo,
        docType: safetyChecklistStore.inputForm.docType,
        searchText: safetyChecklistStore.searchParam.searchText,
        writeYear: safetyChecklistStore.inputForm.writeYear
    };
    safetyChecklistStore.getChecklistDetail(searchParam, true);
};

btnCopy(() => {
    handleDataChange('작성한 내용이 덮어집니다. \n 예시를 불러오시겠습니까?', safetyChecklistStore.btnCopy, safetyChecklistStore.inputForm);
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await safetyChecklistStore.btnSave();
    }
});
btnDelete(() => {
    if (safetyChecklistStore.inputForm.cmd === 'U') {
        safetyChecklistStore.btnDetailDelete();
    } else {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
    }
});
</script>
