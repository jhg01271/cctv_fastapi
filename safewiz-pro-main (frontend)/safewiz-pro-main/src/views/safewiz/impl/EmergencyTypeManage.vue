<template>
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">조회</button>
            <button class="btn radius w11rem active es-w100p" type="submit" @click="btnSample">예시 불러오기</button>
        </div>
    </div>

    <OverlayScrollbarsComponent
        class="my2rem es-maxh50rem"
        :options="{
            scrollbars: {
                autoHide: 'hover',
                x: 'hidden',
                y: 'visible'
            }
        }"
    >
        <!-- title -->
        <div class="oh">
            <div class="row flex gutters1rem">
                <div class="grid10-3 es-grid12-12">
                    <div class="field df jcsb">
                        <label for="">비상사태 유형</label>
                    </div>
                </div>
                <div class="grid10-7 es-order1">
                    <div class="field">
                        <label for="">비상통제 역할</label>
                    </div>
                </div>
                <div class="grid10-3 es-grid12-12">
                    <div class="pr h100p pa2rem bd1pxsolidE1E6ED br4px">
                        <OverlayScrollbarsComponent
                            class="h100pcalc4-5rem maxh40rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem">
                                <button v-for="(eType, index) in filteredTypeList" :key="eType.type" class="btn br4px" v-button @click="typeClick(eType)" :id="`type_${index}`">{{ eType.typeNm }}</button>
                            </div>
                        </OverlayScrollbarsComponent>
                        <div class="pa w100p l0 b0">
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

                <div class="grid10-7 df es-grid12-12 es-order2">
                    <div class="pa2rem bd1pxsolidE1E6ED br4px oh fg1">
                        <div class="row flex gutters1rem">
                            <div class="grid10-6 us-grid10-10">
                                <div class="field">
                                    <label for="">비상사태 유형명</label>
                                    <input type="text" class="br4px" v-input v-model="selectedType.typeNm" placeholder="비상사태 유형명을 입력하세요." @change="selectedType.checked = true" />
                                </div>
                            </div>
                            <div class="grid10-2 us-grid10-5">
                                <div class="field">
                                    <label for="">순서</label>
                                    <input type="text" class="br4px" v-input v-model="selectedType.ordSeq" @change="selectedType.checked = true" />
                                </div>
                            </div>
                            <div class="grid10-2 us-grid10-5">
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
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw40rem">
                                            <thead>
                                                <tr>
                                                    <th class="w5rem">선택</th>
                                                    <th>비상통제 역할</th>
                                                    <th class="w8rem">순번</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(role, index) in selectedType?.roles" :key="role.type + '_' + role.role" :id="`role_${index}`">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="role.checked" @change="selectedType.checked = role.checked" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px" v-input v-model="role.roleNm" @change="detailValueChanged(role)" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px tac" v-input v-model="role.ordSeq" @change="detailValueChanged(role)" />
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
    </OverlayScrollbarsComponent>

    <div class="df jcfe gap6px">
        <button type="button" class="btn w7-4rem radius active" v-button @click="btnSaveDetail">
            <span>저장</span>
        </button>
        <button type="button" v-button class="btn w7-4rem radius bright-grey" @click="btnClose">
            <span>{{ t('close') }}</span>
        </button>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { onMounted, ref, t, router, alertMsg, getCompId, confirmMsg } = BaseView();
import { getEmergencyType, getEmergencyTypeDataset, saveEmergencyType } from '@/stores/safewiz/impl/api/emerCtrlOrgChartApi.js';
import _ from 'lodash';
const typeList = ref([]);
const filteredTypeList = ref([]);
onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    getEmergencyType(notify).then(res => {
        if (res.list.length > 0) {
            // if (notify) selectedType.value = res.list[0];
            selectedType.value = res.list[0];
        }
        typeList.value = _.cloneDeep(res.list);
        filteredTypeList.value = _.cloneDeep(res.list);
    });
};
// 예시보기
const btnSample = () => {
    confirmMsg('warning', '예시 데이터를 불러오시겠습니까?', null, { fun: getSample, param: true });
};
const getSample = notify => {
    getEmergencyTypeDataset(notify).then(res => {
        res.list.forEach(el => {
            el.roles.forEach(role => {
                role.checked = true;
                role.cmd = 'I';
            });
            el.cmd = 'I';
            el.checked = true;
        });
        filteredTypeList.value = [...filteredTypeList.value, ...res.list];
        if (res.list.length > 0) {
            selectedType.value = res.list[0];
        }
    });
};

// 유형 추가
const btnAdd = () => {
    // let checkedList = selectedType.value.roles.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: null });
    //     return;
    // }
    addAction();
};
const addAction = () => {
    filteredTypeList.value.push({
        checked: true,
        compId: getCompId(),
        cmd: 'I',
        typeNm: '',
        useYn: 'Y',
        ordSeq: 99,
        roles: []
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
// 유형 클릭
const typeClick = type => {
    // let checkedList = selectedType.value.roles.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: type });
    //     return;
    // }
    clickAction(type);
};
const clickAction = type => {
    selectedType.value = type;
};
// 상세 추가
const btnAddDetail = () => {
    if (selectedType.value.roles) {
        selectedType.value.roles.push({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            roleNm: '',
            ordSeq: 99,
            useYn: 'Y'
        });
        setTimeout(() => {
            const element = document.getElementById(`role_${selectedType.value.roles.length - 1}`);
            if (element) {
                element.scrollIntoView({ block: 'center' });
            }
        }, 100);
    } else {
        selectedType.value.roles = [
            {
                checked: true,
                compId: getCompId(),
                cmd: 'I',
                roleNm: '',
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
const btnSaveDetail = () => {
    console.log('@@', selectedType.value);
    if (selectedType.value.checked) {
        confirmMsg('info', `[${selectedType.value.typeNm}]\n${t('msgSave')}`, null, { fun: saveAction, param: true });
    } else {
        alertMsg('warning', '저장할 데이터가 없습니다.');
    }
};
const saveAction = notify => {
    let saveData = _.cloneDeep(filteredTypeList.value.filter(el => el.checked));
    saveData[0].roles = saveData[0].roles.filter(el => el.checked);
    saveEmergencyType(saveData[0], notify).then(res => {
        // btnSearch(false);
        selectedType.value.type = res.result.type;
        selectedType.value.checked = false;
        selectedType.value.cmd = 'U';
        selectedType.value.roles.forEach(el => {
            el.cmd = 'U';
            el.checked = false;
        });
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
        el.roles = el.roles.filter(role => role.roleNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    });
    const filteredData = filteredTypeList.value.filter(item => item.roles.length !== 0 || item.typeNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    filteredTypeList.value = filteredData;
    if (filteredTypeList.value.length > 0) {
        typeClick(filteredTypeList.value[0]);
    } else {
        selectedType.value = {};
    }
};
</script>
