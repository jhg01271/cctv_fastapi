<template>
    <!-- 콘텐츠 영역 -->
    <h3>{{ options.label }}</h3>

    <h3>안전보호구 품목 관리</h3>
    <div class="form ui field">
        <div class="row flex gutters1rem">
            <div class="grid12-10 us-grid12-8">
                <div class="df">
                    <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                    <button type="submit">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
            </div>
            <div class="grid12-2 us-grid12-4">
                <button class="btn base radius w100p" type="submit" @click="btnSearch">조회</button>
            </div>
        </div>

        <div class="box form ui mt2-2rem">
            <div class="row flex">
                <div class="grid12-12">
                    <OverlayScrollbarsComponent
                        class="maxh35-6rem br4px table-sticky"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <table class="tac es-minw60rem">
                            <colgroup>
                                <col class="w8p" />
                                <col />
                                <col />
                                <col />
                                <col class="w12p" />
                                <col class="w15p" />
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>품목명</th>
                                    <th>지급기준</th>
                                    <th>지급대상</th>
                                    <th>순번</th>
                                    <th v-if="props.type === 'manage'">사용여부</th>
                                </tr>
                            </thead>
                            <tbody id="form">
                                <tr v-for="(item, index) in dataFilter" :key="item.ppeId" :id="`list_${index}`">
                                    <td>
                                        <input type="checkbox" v-model="item.checked" v-input @click="selectPPE(item)" />
                                        <!-- :disabled="props.type === 'manage'? true: false" -->
                                    </td>
                                    <td>
                                        <label v-show="false" :for="'ppeNm' + index">품목명</label>
                                        <input v-model="item.ppeNm" v-input type="text" class="w100p radius" :id="'ppeNm' + index" placeholder="품목명" :readonly="!isEditable" @change="onValuechecked(item)" required />
                                    </td>
                                    <td>
                                        <input v-model="item.provisionStandard" v-input type="text" class="w100p radius" id placeholder="지급기준" :readonly="!isEditable" @change="onValuechecked(item)" />
                                    </td>
                                    <td>
                                        <input v-model="item.provisionTarget" v-input type="text" class="w100p radius" id placeholder="지급대상" :readonly="!isEditable" @change="onValuechecked(item)" />
                                    </td>
                                    <td>
                                        <input v-model="item.ordSeq" v-input type="number" class="w100p radius tac" id placeholder :readonly="!isEditable" @change="onValuechecked(item)" />
                                    </td>
                                    <td v-if="props.type === 'manage'">
                                        <div class="df aic h4-4rem">
                                            <input :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" @change="onValuechecked(item, 'toggle')" />
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="6">
                                        <button type="button" @click="btnAdd">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </OverlayScrollbarsComponent>
                    <!-- <div class="df jcc gap1rem my2-2rem" v-if="props.type === 'usage'">
                            <button class="btn active radius w15rem" @click="popupClose">닫기</button>
                        </div>
                        <div class="df jcc gap1rem my2-2rem" v-else-if="props.type === 'manage'"></div> -->
                </div>
            </div>
        </div>
        <div class="df jcsb gap8px mt1rem">
            <button class="btn delete radius w7rem" @click="btnDelete">삭제</button>
            <div class="df gap8px">
                <button class="btn radius w7rem" @click="btnDownload">출력</button>
                <button class="btn active radius w7rem" @click="btnSave">저장</button>
                <button class="btn radius w7rem" @click="popupClose">닫기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
import { getDatasetPPEList, getReportPPEManagement, saveDatasetPPE, deleteDatasetPPE, getReportPPEManagementchk } from '@/stores/safewiz/improvement/api/provisionAndMgmtPPEApi.js';
import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, t, getCompId, validationStore, downloadReport } = BaseView();
import _ from 'lodash';
const options = { label: '', readonly: false };

const searchTerm = ref('');
const dataFilter = ref([]);
const dataList = ref([]);

const props = defineProps({
    single: { type: Boolean, default: true },
    type: { type: String, default: 'manage' },
    searchDt: { type: String, default: '' }
});

const emit = defineEmits(['close', 'select']);

const checkedValues = computed(() => {
    let list = dataFilter.value.filter(el => el.checked === true);
    return list;
});

const isEditable = computed(() => {
    return props.type === 'manage';
});

// 버튼 이벤트
const btnSearch = notify => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchTerm.value = '';
    getDatasetPPEList({ compId: getCompId(), searchText: props.searchDt }, notify)
        .then(res => {
            dataList.value = res.list;
        })
        .finally(() => {
            if (props.type === 'usage') {
                dataList.value = dataList.value.filter(el => el.useYn === 'Y');
            }
            dataFilter.value = _.cloneDeep(dataList.value);
        });
};

// 추가
const btnAdd = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchTerm.value = '';
    let newData = { compId: getCompId(), ppeId: '', ppeNm: '', privisionStandard: '', provisionTarget: '', ordSeq: 99, useYn: 'Y', checked: true };
    dataList.value.push(newData);
    setTimeout(() => {
        const element = document.getElementById(`list_${dataList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    applyFilter();
};

// 저장
const btnSave = async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (checkedValues.value.length === 0) {
        alertMsg('warning', '선택된 데이터가 없습니다.');
        return;
    }

    const valid = await validationStore.validateAllFields('form', true);
    if (!valid) {
        return;
    }
    const isMinorDuplicated = dataList.value.some((item, index) => dataList.value.findIndex(obj => obj.ppeNm === item.ppeNm) !== index);
    if (isMinorDuplicated) {
        toastPopup('중복된 안전보호구가 있습니다.', 'error');
        return;
    }
    confirmMsg('info', '저장하시겠습니까?', '', { fun: emitSave, param: '' });
};
const emitSave = () => {
    saveDatasetPPE(checkedValues.value, true).then(res => {
        if (res && res.success) {
            btnSearch(false);
        }
    });
};

// 저장
const btnDelete = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (checkedValues.value.length === 0) {
        alertMsg('warning', '선택된 데이터가 없습니다.');
        return;
    }
    confirmMsg('info', '삭제하시겠습니까? (선택된 데이터의 사용여부가 미사용으로 변경됩니다.)', '', { fun: emitDelete, param: '' });
};
const emitDelete = () => {
    deleteDatasetPPE(checkedValues.value, true).then(res => {
        if (res && res.success) {
            btnSearch(false);
        }
    });
};

// 출력 버튼
const btnDownload = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (checkedValues.value.length > 0) {
        confirmMsg('info', t('msgCheckedPrint'), null, { fun: fnDownload });
    } else {
        confirmMsg('info', t('msgPrint'), null, { fun: fnDownload });
    }
};

const fnDownload = () => {
    if (checkedValues.value.length > 0) {
        getReportPPEManagementchk(checkedValues.value, true).then(res => {
            downloadReport(res);
        });
    } else {
        getReportPPEManagement({ compId: getCompId(), type: 'pdf' }, true).then(res => {
            downloadReport(res);
        });
    }
};

// 닫기
const popupClose = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (checkedValues.value.length > 0) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: emitClose, param: '' });
    } else {
        emit('close');
    }
};
const emitClose = () => {
    emit('close');
};

// 선택
const selectPPE = item => {
    emitSelect(item);
    if (props.type === 'usage') {
        emitClose();
    }
};

const emitSelect = item => {
    emit('select', item);
};

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = dataList.value.filter(item => item.ppeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.provisionStandard?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.provisionTarget?.toLowerCase().includes(searchTerm.value.toLowerCase()));
    dataFilter.value = filteredData;
};

// 사용여부 변경 함수
const onValuechecked = (item, type) => {
    if (type === 'toggle') {
        item.useYn = item.useYn === 'N' ? 'Y' : 'N';
    }
    item.checked = true;
};

const initcheckedValue = () => {
    dataList.value.forEach(el => {
        el.checked = false;
    });
};
onMounted(async () => {
    btnSearch(true);
});
</script>
