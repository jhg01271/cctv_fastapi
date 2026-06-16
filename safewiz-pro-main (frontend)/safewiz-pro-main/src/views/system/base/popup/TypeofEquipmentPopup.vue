<template>
    <div>
        <h3>{{ props.options.label }}</h3>
        <div class="form ui field">
            <div class="row flex gutters1rem">
                <div class="grid12-7 df aic us-grid12-9">
                    <input v-input type="text" class="radius search es-w100p" placeholder="통합검색" v-model="searchTerm" @keyup.enter="applyFilter" />
                    <button type="submit" @click="applyFilter">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
                <div class="grid12-2 us-grid12-3">
                    <button class="btn w100p active radius" type="submit" @click="btnSearch">조회</button>
                </div>
                <div class="grid12-3 us-grid12-12">
                    <button class="btn base w100p radius" type="submit" @click="btnSample">예시 불러오기</button>
                </div>
            </div>

            <div id="form">
                <OverlayScrollbarsComponent
                    class="mt2-2rem maxh35rem br4px table-sticky"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <!-- TODO -->
                    <!-- 퍼블리싱 작업이 필요합니다. (min-height)-->
                    <table class="tac sm-minw45rem" v-if="filteredData.length <= 0">
                        <!-- 데이터가 없을 경우 컬럼을 병합하여 하나의 컬럼안에 안내문구 출력되도록 함-->
                        <tbody>
                            <tr>
                                <td colspan="4">데이터가 존재하지 않습니다.</td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="4" class="bcEBEDFF">
                                    <button class="w100p" v-button @click="btnAdd">
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
                    <table class="tac sm-minw45rem" v-else>
                        <colgroup>
                            <col class="w5rem" />
                            <col class="w30rem" />
                            <col class="w10rem" />
                            <col class="w10rem" />
                        </colgroup>
                        <thead>
                            <tr>
                                <th>선택</th>
                                <th>유형</th>
                                <th>순번</th>
                                <th>사용여부</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in filteredData" :key="item.stdEqId + index" :id="`list_${index}`">
                                <td>
                                    <input v-model="item.selected" type="checkbox" v-input class="shrink0" />
                                </td>
                                <td>
                                    <div class="field">
                                        <label v-show="false" :for="'stdEqNm' + index">유형</label>
                                        <input v-model="item.stdEqNm" v-input type="text" class="w100p radius" :id="'stdEqNm' + index" placeholder="유형을 입력하세요" @change="onValueChanged(item)" required/>
                                    </div>
                                </td>
                                <td>
                                    <input v-model="item.ordSeq" v-input type="number" class="w100p radius tac" placeholder @change="onValueChanged(item)" />
                                </td>
                                <td>
                                    <div class="df aic h4-4rem">
                                        <input :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="onValueChanged(item, 'toggle')" />
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="4" class="bcEBEDFF">
                                    <button class="w100p" v-button @click="btnAdd">
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
            <div class="pr df jcsb gap8px mt2rem">
                <div>
                    <button class="btn w7-4rem radius delete" @click="btnDelete">{{ t('delete') }}</button>
                </div>

                <div class="df gap8px jcfe us-jcc">
                    <button class="btn active radius w74px" @click="btnSave">{{ t('save') }}</button>
                    <button class="btn radius w74px" @click="btnClose">{{ t('close') }}</button>
                </div>
            </div>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="sampleSelectPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'설비 유형 예시'" :checkFetchDataEmpty="true" uniqueKey="stdEqId" filterKey="stdEqNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :selectedIdList="gridData.map(el => el.stdEqId)" :fetch-param="{compId: getCompId()}" :fetch-data="getTypeofEquipmentList" @apply="applyTypeofEquipment" @close="closeTypeofEquipment"/>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, getCompId, confirmMsg, alertMsg, openLoading, endLoading, validationStore } = BaseView();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getStdEquips, saveStdEquips, deleteStdEquips } from '@/stores/system/base/api/equipmentApi';

import { useDatasetStore } from '@/stores/safewiz/dataset/dataset.js';
const datasetStore = useDatasetStore();

import _ from 'lodash';

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const sampleSelectPopup = ref('');

const emit = defineEmits(['close']);

const props = defineProps({
    selected: {
        type: String,
        default: () => ''
    },
    options: {
        type: Object,
        default: () => ({})
    }
});
const gridData = ref([]);
const searchTerm = ref('');
const compId = getCompId();

onMounted(async () => {
    btnSearch();
});
const selectedValues = computed(() => {
    let list = filteredData.value.filter(el => el.selected === true);
    return list;
});
// 조회
const btnSearch = async (notify = true) => {  
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    if (selectedValues.value.length > 0) {
        // alertMsg('warning', '저장하지 않은 정보가 있습니다.');
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction, param: notify });
    } else {
        searchAction(notify);
    }
};
const searchAction = notify => {        
    searchTerm.value = '';
    openLoading();
    getStdEquips({ compId: compId }, notify)
        .then(res => {
            gridData.value = res.list;
            filteredData.value = _.cloneDeep(res.list);
        })
        .catch(err => {
            console.error(err);
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
// 예시불러오기
import { getTypeofEquipmentList } from '@/stores/safewiz/dataset/api/datasetApi';
const btnSample = async () => {    
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    sampleSelectPopup.value.onOpen();
};

const applyTypeofEquipment = el => {
    // 기존 stEqId 리스트 추출
    const existingIds = gridData.value.map(item => item.stdEqId);

    // 중복되지 않는 항목만 필터링 + cmd = 'I' 설정
    const newItems = el.filter(item => !existingIds.includes(item.stdEqId)).map(item => ({ ...item, cmd: 'I' }));

    // 추가
    gridData.value = [...gridData.value, ...newItems];
    filteredData.value = _.cloneDeep(gridData.value);

    moveScroll();
    sampleSelectPopup.value.onClose();
};

const closeTypeofEquipment = () =>{
    sampleSelectPopup.value.onClose();
}
//-----------------------------------------------
//클릭 이벤트

let filteredData = ref([]);
// 필터 적용 함수
const applyFilter = () => {
    // 필터링된 데이터로 그리드를 업데이트
    filteredData.value = gridData.value.filter(item => item.stdEqNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
};
// 새로 추가된 항목으로 포커스 이동
const moveScroll = () => {
    const index =  gridData.value.length - 1; //마지막 항목
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 추가
const btnAdd = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchTerm.value = '';
    let newData = { stdEqId: '', stdEqNm: '', ordSeq: 0, useYn: 'Y', selected: true, cmd:'I' };
    gridData.value = [...gridData.value, newData]; // 마지막에 추가
    applyFilter();
    moveScroll();
};

// 저장
const btnSave = async () => {
    if (selectedValues.value.length > 0) {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        const valid = await validationStore.validateAllFields('form', true);

        if(valid){
            confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: '' });
        }
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};
const saveAction = () => {
    openLoading();
    saveStdEquips(compId, selectedValues.value, true).then(res => {
        if (res && res.success) {
            searchAction(false);
        }
    });
};
const btnClose = () => {  
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (selectedValues.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: emitClose, param: '' });
    } else {
        emit('close', null);
    }
};

// 삭제
const btnDelete = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (selectedValues.value.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteAction, param: '' });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};
const deleteAction = () => {
    deleteStdEquips(compId, selectedValues.value, true).then(res => {
        if (res && res.success) {
            searchAction(false);
        }
    });
};
const emitClose = () => {
    emit('close', null);
};
const onValueChanged = (item, type) => {
    if (type === 'toggle') {
        item.useYn = item.useYn === 'N' ? 'Y' : 'N';
    }
    item.selected = true;
};
</script>
