<template>
    <div id="popup" class="form ui">
        <h3>필요안전기구 관리</h3>
        <div class="form ui field df aic mb2-2rem">
            <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
            <button type="submit">
                <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
            </button>
            <button class="btn base ml1rem radius w15rem" type="submit" @click="btnSearch">조회</button>
        </div>

        <!-- title -->
        <div class="my2rem">
            <div class="row flex gutters1rem">
                <div class="grid10-3 es-grid10-10">
                    <div class="field df jcsb">
                        <label for> <span>필요안전기구 유형</span></label>
                    </div>
                </div>
                <div class="grid10-7 es-dn">
                    <div class="field">
                        <label for>유형별 필요안전기구</label>
                    </div>
                </div>
                <div class="grid10-3 es-grid10-10">
                    <div class="pr bd1pxsolidE1E6ED br4px h30vh">
                        <OverlayScrollbarsComponent
                            class="h100pcalc4-5rem maxh40rem pa2rem es-pa1rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem">
                                <button v-for="(eType, index) in filteredTypeList" :key="eType.inspectionId" class="btn br4px" v-button @click="typeClick(eType)" :id="`type_${index}`">{{ eType.safetyEqNm }}</button>
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

                <div v-if="filteredTypeList.length !== 0" class="grid10-7 es-grid10-10 df">
                    <div class="pa2rem bd1pxsolidE1E6ED br4px oh es-pa1rem fg1">
                        <div class="row flex gutters1rem">
                            <div class="grid10-6 es-grid10-10">
                                <div class="field">
                                    <label for="safetyEqNm" required> <span>필요안전기구 유형명</span></label>
                                    <input type="text" class="br4px" v-input v-model="selectedType.safetyEqNm" placeholder="필요안전기구 유형명을 입력하세요." @change="selectedType.checked = true" required id="safetyEqNm" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for>순서</label>
                                    <input  type="number" class="br4px" v-input v-model="selectedType.ordSeq" min="0" max="99" placeholder="0" @change="selectedType.checked = true" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :true-value="'Y'" :false-value="'N'" :checked="selectedType.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedType.useYn" @change="selectedType.checked = true" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <OverlayScrollbarsComponent
                                        class="maxh28-7rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw50rem">
                                            <thead>
                                                <tr>
                                                    <th class="w5rem">선택</th>
                                                    <th>필요안전기구</th>
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
                                                        <input type="text" class="br4px" v-input v-model="role.safetyEqItemNm" @change="detailValueChanged(role)" />
                                                    </td>
                                                    <td>
                                                        <input type="number" class="br4px tac" v-input v-model="role.ordSeq" min="0" max="99" placeholder="0" @change="detailValueChanged(role)" />
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
                                                        <button v-button @click="btnAddDetail">
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
                <div v-else class="grid10-7 es-grid10-10">
                    <div class="h100p pa2rem bd1pxsolidE1E6ED br4px oh es-pa1rem df jcc aic">
                        {{ t('msgNoData') }}
                    </div>
                </div>
            </div>
        </div>

        <div class="df jcsb">
            <button type="button" class="btn w7-4rem radius delete" v-button @click="btnDelete">
                <span>삭제</span>
            </button>
            <div class="df gap8px">
                <button type="button" class="btn w7-4rem radius active" v-button @click="btnSaveDetail">
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
const { onMounted, ref, t, router, alertMsg, getCompId, confirmMsg, validationStore } = BaseView();
import { getSafetyEquipment, getSafetyEquipmentDetail, saveSafetyEquipment, deleteSafetyEquipmentDetail } from '@/stores/safewiz/impl/api/permitToWorkApi.js';
import _ from 'lodash';
const typeList = ref([]);
const filteredTypeList = ref([]);
const compId = getCompId();
onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    getSafetyEquipment({ compId: compId }, notify).then(res => {
        console.log('조회', res.list);
        if (res.list.length > 0) {
            selectedType.value = res.list[0];
        }
        typeList.value = _.cloneDeep(res.list);
        filteredTypeList.value = _.cloneDeep(res.list);
    });
};

// 유형 추가
const btnAdd = () => {
    addAction();
};
const addAction = () => {
    filteredTypeList.value.push({
        checked: true,
        compId: getCompId(),
        cmd: 'I',
        safetyEqNm: '',
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
    clickAction(type);
};
const clickAction = type => {
    selectedType.value = type;
};
// 상세 추가
const btnAddDetail = () => {
    if (selectedType.value.itemList) {
        selectedType.value.itemList.push({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            safetyEqId: selectedType.value.safetyEqId,
            safetyEqItemNm: '',
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
                safetyEqItemNm: '',
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
    console.log('유효성', form);
    if (isValid) {
        if (selectedType.value.checked) {
            confirmMsg('info', `[${selectedType.value.safetyEqNm}]\n${t('msgSave')}`, null, { fun: saveAction, param: true });
        } else {
            alertMsg('warning', '저장할 데이터가 없습니다.');
        }
    }
};
const saveAction = notify => {
    let saveData = _.cloneDeep(selectedType.value);
    saveData.itemList = saveData.itemList.filter(el => el.checked);
    saveSafetyEquipment(saveData, notify)
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
    deleteSafetyEquipmentDetail(param, true)
        .then(res => {
            selectedType.value.checked = false;
            selectedType.value.itemList.forEach(el => (el.checked = false));
        })
        .finally(() => {
            btnSearch(false);
        });
};

const emit = defineEmits(['close']);
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

// 검색
const searchTerm = ref('');
const applyFilter = () => {
    if (searchTerm.value === '') {
        filteredTypeList.value = _.cloneDeep(typeList.value);
        typeClick(filteredTypeList.value[0]);
        return;
    }
    filteredTypeList.value.forEach(el => {
        el.itemList = el.itemList.filter(role => role.safetyEqItemNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    });
    const filteredData = filteredTypeList.value.filter(item => item.itemList.length !== 0 || item.safetyEqNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    filteredTypeList.value = filteredData;
    if (filteredTypeList.value.length > 0) {
        typeClick(filteredTypeList.value[0]);
    } else {
        selectedType.value = {};
    }
};
</script>
<style></style>
