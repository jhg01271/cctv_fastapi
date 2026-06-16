<template>
        <div id="popup" class="w100p maxh100p form ui sm-df sm-fdc">
        <h3>위험물/유해화학물질 점검사항 관리</h3>
        <div class="form ui mb2-2rem">
            <div class="df gap8px es-fww">
                <div class="df fg1 es-w60p">
                    <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                    <button type="submit">
                        <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                    </button>
                </div>
                <button class="btn radius w7-4rem active" type="submit" @click="btnSearch">조회</button>
                <button class="btn radius w10rem active es-w100p" type="submit" @click="btnExample" :disabled="props.select">예시불러오기</button>
            </div>
        </div>

        <!-- title -->
        <h4 class="mb1rem">점검항목</h4>
        <div class="maxh100p mb2rem sm-oya sm-oxh">
            <div class="row flex gutters1rem h100p">
                <div class="grid10-3 sm-grid10-10">
                    <div class="pr bd1pxsolidE1E6ED br4px h100p df fdc">
                        <OverlayScrollbarsComponent
                            class="pa1-2rem es-pa1rem fg1"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem mb1rem" v-for="(eType, index) in filteredTypeList" :key="eType.checklistId" :id="`type_${index}`">
                                <div v-if="eType.useYn == 'Y'" class="list-btn h4-4rem px1-2rem df aic gap8px br4px init" :class="{ active: selectedType === eType }">
                                    <input type="checkbox" v-input v-model="eType.checked" @click="selectItem(eType)" />
                                    <button class="h4-4rem w100p df aic gap8px br4px tal" v-button @click="typeClick(eType)">{{ eType.checklistNm }}</button>
                                </div>
                                <div v-else class="list-btn h4-4rem px1-2rem df aic gap8px br4px">
                                    <input type="checkbox" v-input disabled v-model="eType.checked" @click="selectItem(eType)" />
                                    <button class="list-btn w100p df aic gap8px btn br4px disabled" v-button @click="typeClick(eType)">{{ eType.checklistNm }}</button>
                                </div>
                            </div>
                            <!-- disabled TEST 영역 시작 (확인 후 삭제 부탁드립니다.)-->

                            <!-- disabled TEST 영역 끝 -->
                        </OverlayScrollbarsComponent>
                        <button class="w100p py1-2rem bdt1pxsolidE1E6ED" v-button @click="btnAdd" :disabled="props.select">
                            <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="#EBEDFF" />
                                <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                            </svg>
                        </button>
                    </div>
                </div>

                <div v-if="filteredTypeList.length !== 0" class="grid10-7 sm-grid10-10">
                    <div class="h100p df fdc pa2rem bd1pxsolidE1E6ED br4px oh es-pa1rem">
                        <div class="row flex gutters1rem">
                            <div class="grid10-6 es-grid10-10">
                                <div class="field">
                                    <label for="checklistNm" required>
                                        <span>점검항목명</span>
                                    </label>
                                    <input v-input type="text" class="br4px" v-model="selectedType.checklistNm" id="checklistNm" required placeholder="점검항목명을 입력하세요." @change="selectedType.checked = true" :disabled="props.select" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for>순서</label>
                                    <input type="number" class="br4px" v-input v-model="selectedType.ordSeq" @change="selectedType.checked = true" min="0" max="99" placeholder="0" :disabled="props.select" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :true-value="'Y'" :false-value="'N'" :checked="selectedType.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedType.useYn" @change="selectedType.checked = true" :disabled="props.select" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <OverlayScrollbarsComponent
                                        class="maxh34-8rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw50rem">
                                            <thead>
                                                <tr>
                                                    <th class="w5rem">선택</th>
                                                    <th>위험물/유해화학물질 점검사항</th>
                                                    <th class="w8rem">순번</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(role, index) in selectedType?.itemList" :key="role.type + '_' + role.role" :id="`role_${index}`">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="role.checked" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px" v-input v-model="role.checklistItemNm" @change="detailValueChanged(role)" :disabled="props.select" />
                                                    </td>
                                                    <td>
                                                        <input type="number" class="br4px" :min="0" v-input v-model="role.ordSeq" @change="detailValueChanged(role)" :disabled="props.select" />
                                                    </td>
                                                    <td>
                                                        <div class="df aic h4-4rem">
                                                            <input :true-value="'Y'" :false-value="'N'" :checked="role.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="role.useYn" @change="detailValueChanged(role)" :disabled="props.select" />
                                                        </div>
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td class="bcEBEDFF" colspan="10">
                                                        <button type="button" class="w100p" v-button @click="btnAddDetail" :disabled="props.select">
                                                            <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                                                <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
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
                <div v-else class="grid10-7 es-grid10-10">
                    <div class="h100p pa2rem bd1pxsolidE1E6ED br4px oh es-pa1rem df jcc aic">
                        {{ t('msgNoData') }}
                    </div>
                </div>
            </div>
        </div>

        <div class="df jcsb">
            <button type="button" class="btn delete w7-4rem radius" v-button @click="btnDelete" :disabled="props.select">
                <span>삭제</span>
            </button>
            <div class="df gap8px">
                <button type="button" v-button class="btn w7-4rem radius bright-grey" @click="btnSelect" :disabled="!props.select">
                    <span>{{ t('select') }}</span>
                </button>
                <button type="button" class="btn w7-4rem radius active" v-button @click="btnSaveDetail" :disabled="props.select">
                    <span>저장</span>
                </button>
                <button type="button" v-button class="btn w7-4rem radius bright-grey" @click="btnClose">
                    <span>{{ t('close') }}</span>
                </button>
        </div>
    </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { onMounted, ref, t, router, alertMsg, getCompId, confirmMsg, validationStore, openLoading, endLoading } = BaseView();
import { getDatasetHazmatChecklist, getDatasetHazmatChecklistDetail, saveDatasetHazmatChecklist, deleteDatasetHazmatChecklistDetail, getBaseDatasetHazmatChecklist } from '@/stores/safewiz/impl/api/hazMatsInspectionApi.js';
import _ from 'lodash';
const typeList = ref([]);
const filteredTypeList = ref([]);
const compId = getCompId();

import { defineProps, watch } from 'vue';

const props = defineProps({
    select: {
        type: Boolean,
        default: true
    }
});

onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    openLoading();
    getDatasetHazmatChecklist({ compId: compId }, notify)
        .then(res => {
            if (res.list.length > 0) {
                selectedType.value = res.list[0];
            }
            typeList.value = _.cloneDeep(res.list);
            filteredTypeList.value = _.cloneDeep(res.list);
            // 첫번째 클릭상태
            if (filteredTypeList.value[0]) {
                typeClick(filteredTypeList.value[0]);
            }
        })
        .finally(() => endLoading());
};

//예시불러오기append
const btnExample = () => {
    const hasUnsavedChanges = filteredTypeList.value.some(item => item.cmd === 'I');
    if (hasUnsavedChanges) {
        alertMsg('warning', '신규 입력중인 값이 있습니다. \n 저장후 추가 하세요.');
        return; // 조건이 만족되면 함수 실행 중단
    }

    openLoading();
    getBaseDatasetHazmatChecklist({ compId: compId }, true)
        .then(res => {
            // res.list와 itemList 내부에 cmd 속성 추가
            const updatedList = res.list.map(item => ({
                ...item,
                cmd: 'I',
                checked: true,
                compId: compId,
                itemList: item.itemList.map(subItem => ({
                    ...subItem,
                    cmd: 'I',
                    compId: compId,
                    checked: true
                }))
            }));

            // 기존 데이터에 새로운 데이터 추가
            typeList.value = _.cloneDeep([...typeList.value, ...updatedList]);
            filteredTypeList.value = _.cloneDeep([...filteredTypeList.value, ...updatedList]);

            // 신규 데이터의 첫번째 데이터 선택
            if (updatedList.length > 0) {
                const targetIndex = filteredTypeList.value.length - updatedList.length;
                typeClick(filteredTypeList.value[targetIndex]);
            }
        })
        .finally(() => endLoading());
};

// 유형 추가
const btnAdd = () => {
    const hasUnsavedChanges = filteredTypeList.value.some(item => item.cmd === 'I');
    if (hasUnsavedChanges) {
        alertMsg('warning', '신규 입력중인 값이 있습니다. \n 저장후 추가 하세요.');
        return; // 조건이 만족되면 함수 실행 중단
    }
    addAction();
};
const addAction = () => {
    filteredTypeList.value.push({
        checked: true,
        compId: getCompId(),
        cmd: 'I',
        checklistNm: '',
        useYn: 'Y',
        ordSeq: 99,
        itemList: []
    });
    setTimeout(() => {
        const element = document.getElementById(`type_${filteredTypeList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
    typeClick(filteredTypeList.value[filteredTypeList.value.length - 1]);
};

const selectedType = ref({});
// 유형 클릭
const typeClick = type => {
    filteredTypeList.value.forEach(item => (item.active = item === type ? !item.active : false));
    clickAction(type);
};
const clickAction = type => {
    console.log('선택', type);
    console.log('');
    selectedType.value = type;
};
// 상세 추가
const btnAddDetail = () => {
    if (selectedType.value.itemList) {
        selectedType.value.itemList.push({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            checklistId: selectedType.value.checklistId,
            checklistItemNm: '',
            ordSeq: 99,
            useYn: 'Y'
        });
        setTimeout(() => {
            const element = document.getElementById(`role_${selectedType.value.itemList.length - 1}`);
            if (element) {
                element.scrollIntoView({ block: 'center' });
            }
        }, 100);
    } else {
        selectedType.value.itemList = [
            {
                checked: true,
                compId: getCompId(),
                cmd: 'I',
                checklistItemNm: '',
                ordSeq: 99,
                useYn: 'Y'
            }
        ];
    }
};
// 상세 데이터 변경 이벤트
const detailValueChanged = role => {
    selectedType.value.checked = true;
    role.checked = true;
};

// 상세 데이터 저장
const btnSaveDetail = async () => {
    const isValid = await validationStore.validateAllFields('popup', true);
    if (isValid){
        const checkedList = filteredTypeList.value.filter(item => item.checked == true)
        if (checkedList.length !== 0) {
            confirmMsg('info', t('msgSave'), null, { fun: saveAction, param: true });
        } else {
            alertMsg('warning', '선택된 데이터가 없습니다.');
        }
    }
};

const saveAction = async notify => {
    const saveDataList = filteredTypeList.value?.filter(el => el.checked);
    if (saveDataList.length > 0) {
        for (const saveData of saveDataList) {
            saveData.itemList = saveData.itemList.filter(el => el.checked);
        }

        openLoading();
        await saveDatasetHazmatChecklist(saveDataList, notify)
            .then(res => {
                selectedType.value.checked = false;
                selectedType.value.itemList.forEach(el => (el.checked = false));
            })
            .catch(() => {
                endLoading();
            });
        endLoading();
        btnSearch(false);
    }
};

const btnDelete = () => {
    let saveData = _.cloneDeep(selectedType.value);
    saveData.itemList = saveData.itemList.filter(el => el.checked);
    if (saveData.itemList == 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), '', {
        fun: deleteAction,
        param: saveData.itemList
    });
};

const deleteAction = param => {
    openLoading();
    deleteDatasetHazmatChecklistDetail(param, true)
        .then(res => {
            selectedType.value.checked = false;
            selectedType.value.itemList.forEach(el => (el.checked = false));
        })
        .finally(() => {
            endLoading();
            btnSearch(false);
        });
};

const emit = defineEmits(['close', 'select']);
const btnClose = () => {
    let checkedList = filteredTypeList.value.filter(el => el.checked);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), null, { fun: closeAction, param: null });
    } else {
        closeAction();
    }
};
const closeAction = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    emit('close');
};

// 선택
const btnSelect = () => {
    filteredTypeList.value = filteredTypeList.value
        .filter(el => el.useYn === 'Y')
        .map(el => ({
            ...el,
            itemList: el.itemList.filter(row => row.checked === true && row.useYn === 'Y')
        }));
    emit('select', filteredTypeList.value);
};
// 검색
const searchTerm = ref('');
const applyFilter = () => {
    if (searchTerm.value === '') {
        filteredTypeList.value = _.cloneDeep(typeList.value);
        typeClick(filteredTypeList.value[0]);
        return;
    }
    filteredTypeList.value.forEach(el => {
        el.itemList = el.itemList.filter(role => role.checklistItemNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    });
    const filteredData = filteredTypeList.value.filter(item => item.itemList.length !== 0 || item.checklistNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    filteredTypeList.value = filteredData;
    if (filteredTypeList.value.length > 0) {
        typeClick(filteredTypeList.value[0]);
    } else {
        selectedType.value = {};
    }
};

//항목 선택시, 해당 항목의 사항 전체 체크, 미체크 처리
const selectItem = item => {
    item.itemList.forEach(i => {
        i.checked = !item.checked;
    });
    clickAction(item);
};
</script>
<style lang="scss" scoped>
.form {
    .list-btn {
        border: none;
        background: #f0f3f6;
        transition: all 0.3s ease;
        &.active {
            background-color: #ebedff;
            color: #3248f6;
            border: none;
            button {
                color: #3248f6;
                font-weight: 700;
            }
        }
        &.disabled {
            color: #9ea1a6;
        }
    }
}
</style>
