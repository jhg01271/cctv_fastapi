<template>
    <div id="pupup" class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">조회</button>
            <button class="btn radius w10rem active es-w100p" type="submit" @click="btnExample">예시불러오기</button>
        </div>

        <!-- title -->
        <div class="mb2rem">
            <div class="row flex gutters1rem">
                <div class="grid10-3 es-grid12-12">
                    <div class="field df jcsb">
                        <label for>평가항목</label>
                    </div>
                </div>
                <div class="grid10-7">
                    <div class="field">
                        <!-- <label for>평가항목 관리명</label> -->
                    </div>
                </div>
                <div class="grid10-3 fg1 es-grid12-12">
                    <div class="pr bd1pxsolidE1E6ED br4px h100p">
                        <OverlayScrollbarsComponent
                            class="h100pcalc4-5rem pa1-2rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem">
                                <button v-for="(eType, index) in filteredTypeList" :key="eType.evaluationNm" :id="`type_${index}`" class="btn br4px" :style="{ borderColor: eType.evaluationId === selectedType.evaluationId ? '#3248F6' : '' }" v-button @click="typeClick(eType, index)">{{ eType.evaluationNm }}</button>
                            </div>
                        </OverlayScrollbarsComponent>
                        <div class="pa w100p b0">
                            <button class="w100p py1-2rem bdt1pxsolidE1E6ED bcFFFFFF" v-button @click="btnAdd">
                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                    <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="grid10-7 es-grid12-12">
                    <div class="h100p pa2rem bd1pxsolidE1E6ED br4px oh">
                        <div class="row flex gutters1rem">
                            <div class="grid10-6 us-grid12-12">
                                <div class="field">
                                    <label for="evaluationNm" required>
                                        <span>평가항목명</span>
                                    </label>
                                    <input type="text" id="evaluationNm" class="br4px" required v-input v-model="selectedType.evaluationNm" placeholder="평가항목명을 입력하세요." @change="selectedType.checked = true" />
                                </div>
                            </div>
                            <div class="grid10-2 us-grid12-6">
                                <div class="field">
                                    <label for>순서</label>
                                    <input type="text" class="br4px" v-input v-model="selectedType.ordSeq" @change="selectedType.checked = true" />
                                </div>
                            </div>
                            <div class="grid10-2 us-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :true-value="'Y'" :false-value="'N'" :checked="selectedType.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedType.useYn" @change="filteredTypeList[selectedIndex].checked = true" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <OverlayScrollbarsComponent
                                        class="maxh35rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="tac sm-minw60rem">
                                            <thead>
                                                <tr>
                                                    <th class="w5rem">선택</th>
                                                    <th>평가사항</th>
                                                    <th class="w7rem">순번</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(role, index) in selectedType?.itemList" :key="role.type + '_' + role.role" :id="`role_${index}`">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="role.checked" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px" v-input v-model="role.evaluationItemNm" @change="detailValueChanged(role)" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px" v-input v-model="role.ordSeq" @change="detailValueChanged(role)" />
                                                    </td>
                                                    <td>
                                                        <div class="df aic h4-4rem">
                                                            <input :true-value="'Y'" :false-value="'N'" :checked="role.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="role.useYn" @change="detailValueChanged(role)" />
                                                        </div>
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="4">
                                                        <button type="button" v-button @click="btnAddDetail">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
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
            </div>
        </div>

        <div class="df jcsb gap8px">
            <button type="button" class="btn w7rem radius delete" v-button @click="btnDelete">
                <span>삭제</span>
            </button>
            <div class="df gap8px">
                <button type="button" v-button class="btn w7rem radius bright-grey" @click="btnSelect" :disabled="!props.select">
                    <span>{{ '적용' }}</span>
                </button>

                <button type="button" class="btn w7rem radius active us-w7rem" v-button @click="btnSaveDetail">
                    <span>저장</span>
                </button>
                <button type="button" v-button class="btn w7rem radius bright-grey us-w7rem" @click="btnClose">
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
import { getEvaluationChecklist, getBaseEvaluationChecklist, getEvaluationChecklistDetail, saveEvaluationChecklist, deleteEvaluationChecklistDetail } from '@/stores/safewiz/evaluation/api/evaluationReportApi.js';
import _ from 'lodash';
const typeList = ref([]);
const filteredTypeList = ref([]);
const compId = getCompId();

import { defineProps, watch } from 'vue';

const props = defineProps({
    select: {
        type: Boolean,
        default: true
    },
    checkedList: { type: Array, default: [] } // 현재 적용중인 항목 리스트
});

onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    openLoading();
    getEvaluationChecklist({ compId: compId }, notify)
        .then(res => {
            console.log('조회', res.list);
            if (res.list.length > 0) {
                selectedType.value = res.list[0];
            }
            typeList.value = _.cloneDeep(res.list);
            filteredTypeList.value = _.cloneDeep(res.list);
            // 첫번째 클릭상태
            if (filteredTypeList.value[0]) {
                typeClick(filteredTypeList.value[0]);
            }
            console.log('checkedMap', filteredTypeList.value);
        })
        .finally(() => {
            endLoading();
            if (props.checkedList.length > 0) {
                // 유효 키 생성
                const checkedMap = new Map(props.checkedList.map(el => [`${el.evaluationNm}_${el.evaluationItemId}`, true]));
                console.log('checkedMap', checkedMap);
                filteredTypeList.value.forEach(item => {
                    item.itemList.forEach(detail => {
                        const key = `${detail.evaluationNm}_${detail.evaluationItemId}`;
                        console.log('체크리스트', key);
                        if (checkedMap.has(key)) {
                            // 순회하며 키 중복 체크
                            detail.checked = true;
                        }
                    });
                });
            }
        });
};

//예시불러오기
const btnExample = () => {
    openLoading();
    getBaseEvaluationChecklist({ compId: compId }, true)
        .then(res => {
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

            if (updatedList.length > 0) {
                selectedType.value = updatedList[0];
            }

            // 기존 데이터에 새로운 데이터 추가
            typeList.value = _.cloneDeep([...typeList.value, ...updatedList]);
            filteredTypeList.value = _.cloneDeep([...filteredTypeList.value, ...updatedList]);
            console.log('조회', filteredTypeList.value);
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
        evaluationNm: '',
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
    typeClick(filteredTypeList.value.length - 1);
};

const selectedType = ref({});
const selectedIndex = ref(0);
// 유형 클릭
const typeClick = (type, index) => {
    if (index) {
        selectedIndex.value = index;
    }
    clickAction(type);
    console.log('타입', type);
};
const clickAction = eType => {
    selectedType.value = eType;
};
// 상세 추가
const btnAddDetail = () => {
    if (selectedType.value.itemList) {
        selectedType.value.itemList.push({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            evaluationId: selectedType.value.evaluationId,
            evaluationItemNm: '',
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
                evaluationItemNm: '',
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
    console.log('길이', selectedType.value);
    const isValid = await validationStore.validateAllFields('pupup', true); // 기본 폼
    if (isValid) {
        if (selectedType.value.checked) {
            confirmMsg('info', `[${selectedType.value.evaluationNm}]\n${t('msgSave')}`, null, { fun: saveAction, param: true });
        } else {
            alertMsg('warning', '저장할 데이터가 없습니다.');
        }
    }
};
const saveAction = notify => {
    console.log('123', selectedType.value);
    let saveData = _.cloneDeep(selectedType.value);
    saveData.itemList = saveData.itemList.filter(el => el.checked);
    saveEvaluationChecklist(saveData, notify)
        .then(res => {
            selectedType.value.checked = false;
            selectedType.value.itemList.forEach(el => (el.checked = false));
        })
        .finally(() => {
            btnSearch(false);
        });
};

const btnDelete = () => {
    let saveData = _.cloneDeep(selectedType.value);
    saveData.itemList = saveData.itemList.filter(el => el.checked);
    confirmMsg('warning', t('msgDelete'), '', {
        fun: deleteAction,
        param: saveData.itemList
    });
};

const deleteAction = param => {
    deleteEvaluationChecklistDetail(param, true)
        .then(res => {
            selectedType.value.checked = false;
            selectedType.value.itemList.forEach(el => (el.checked = false));
        })
        .finally(() => {
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
    filteredTypeList.value.forEach(el => {
        el.itemList = el.itemList.filter(row => row.checked == true);
    });
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
        console.log('아이템', el.itemList);
        el.itemList = el.itemList.filter(role => role.evaluationItemNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    });
    const filteredData = filteredTypeList.value.filter(item => item.itemList.length !== 0 || item.evaluationNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    filteredTypeList.value = filteredData;
    if (filteredTypeList.value.length > 0) {
        typeClick(filteredTypeList.value[0]);
    } else {
        selectedType.value = {};
    }
};
</script>
<style></style>
