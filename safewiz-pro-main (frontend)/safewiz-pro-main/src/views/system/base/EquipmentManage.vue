<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-8 lg-grid12-6 es-grid12-12">
                    <div class="df aic bcffffff">
                        <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="equipmentStore.searchTerm" @keyup.enter="equipmentStore.applyFilter" />
                        <button type="submit" class="shrink0" @click="equipmentStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="df gap1rem us-fww">
                        <button type="button" class="w30p btn radius active us-w100p" @click="typeofEquipmentManagePopupOpen">
                            <span>{{ t('typeofEquipmentManage') }}</span>
                        </button>
                        <div class="toggle w80p us-w100p">
                            <button type="button" v-for="div in equipmentStore.equipDivList" :key="div.id" @click="clickTab(div)" :class="{ active: equipmentStore.currentFilter === div.id }">
                                <span>{{ div.name }}</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <div v-if="equipmentStore.dataFilterList && equipmentStore.dataFilterList[0]">
                    <template v-for="(equip, index) in equipmentStore.dataFilterList" :key="index">
                        <div class="segment mb2-2rem title-box">
                            <p>{{ equip.title }}</p>
                            <div class="pa2-2rem">
                                <div class="row flex gutters2rem">
                                    <template v-for="(i, idx) in equip.data" :key="idx">
                                        <i-card :v-model="i.checked" :data="i" :disabled="i.useYn == 'N'" :title="i.equipmentNm" :user-hr="true" :use-thumbnail="true" :thumbnail-img="i.fileId" :headHrList="i.headHrList" :secondHrList="i.secondHrList" @handle="handleEvent" @editor="equipmentStore.btnDetail(i.equipmentId)"></i-card>
                                    </template>
                                    <!-- 추가 -->
                                    <div v-if="equipmentStore.dataFilterList" class="grid12-4 ul-grid12-6 lg-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="equipmentStore.btnAdd('temp', equip.data)">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>

                                                <span class="db fs2rem c999999">{{ t('card_addEquipment') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </div>
                <div v-else>
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="equipmentStore.btnAdd('temp')">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addEquipment') }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input ref="fileInput" type="file" @change="handleFileUpload" accept=".xlsx, .xls" class="dn" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <!-- 설비 유형 관리 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="typeofEquipmentPopup">
                <div class="contents w70rem md-w100p">
                    <DataSetManagePopup :cardComponent="remarkPopupComponent" :title="t('typeofEquipmentManage')" :datasetYn="'Y'" id="stdEqId" nm="stdEqNm" :popupDataList="typeofEquipmentPopupDataList" @filter="filterTypeofEquipmentPopup" @search="searchTypeofEquipmentPopup" @sample="sampleTypeofEquipmentPopup" @add="addTypeofEquipmentPopup" @save="saveTypeofEquipmentPopup" @delete="deleteTypeofEquipmentPopup" @close="closeTypeofEquipmentPopup"></DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="sampleSelectPopup">
                <!-- 단일 그리드 -->
                <div class="contents w70rem md-w100p">
                    <base-select-popup :title="'설비 유형 예시'" :component="BaseSelectAccordionComponent" uniqueKey="stdEqId" filterKey="stdEqNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-param="{ compId: getCompId() }" :fetch-data="getTypeofEquipmentList" @apply="applyTypeofEquipmentSample" @close="closeTypeofEquipmentSample" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, onMounted, btnSearch, btnAdd, btnDelete, t, confirmMsg, btnDownloadForm, btnUpload, validationStore, computed, getCompId, openLoading, endLoading, alertMsg, getDuplicatedData, nextTick } = BaseView();

import { useEquipmentStore } from '@/stores/system/base/equipment.js';
const equipmentStore = useEquipmentStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownloadForm', 'btnUpload'];

import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import remarkPopupComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { getTypeofEquipmentList } from '@/stores/safewiz/dataset/api/datasetApi';
import { getStdEquips, saveStdEquips, deleteStdEquips } from '@/stores/system/base/api/equipmentApi';

import _ from 'lodash';

// 조회 버튼 이벤트 함수
btnSearch(() => {
    equipmentStore.btnSearch(true);
});
btnAdd(() => {
    equipmentStore.btnAdd('temp');
});
btnDelete(() => {
    equipmentStore.btnDelete();
});

onMounted(async () => {
    equipmentStore.initData();
    equipmentStore.btnSearch(true);
});

// 설비 유형 팝업
const typeofEquipmentPopup = ref(null);
const typeofEquipmentPopupDataList = ref([]);
const gridData = ref([]);
const searchTerm = ref('');
const compId = getCompId();

// 설비 유형 팝업 호출
const typeofEquipmentManagePopupOpen = () => {
    searchAction();
    typeofEquipmentPopup.value.onOpen();
};

// 설비 유형 팝업 선택 여부 확인
const selectedValues = computed(() => {
    let list = typeofEquipmentPopupDataList.value.filter(el => el.checked === true);
    return list;
});

// 설비 유형 팝업 필터 적용 함수
const filterTypeofEquipmentPopup = text => {
    openLoading();
    const keyword = text?.toLowerCase() || '';
    typeofEquipmentPopupDataList.value = gridData.value.filter(el => el.stdEqNm?.toLowerCase().includes(keyword) || el.remark?.toLowerCase().includes(keyword));
    endLoading();
};

// 설비 유형 팝업 조회
const searchTypeofEquipmentPopup = async (notify = true) => {
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
            typeofEquipmentPopupDataList.value = _.cloneDeep(res.list);
        })
        .catch(err => {
            console.error(err);
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 설비 유형 팝업 예시불러오기
const sampleTypeofEquipmentPopup = async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    sampleSelectPopup.value.onOpen();
};

// 설비 유형 팝업 추가
const addTypeofEquipmentPopup = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchTerm.value = '';
    let newData = { stdEqId: '', stdEqNm: '', ordSeq: 0, useYn: 'Y', checked: true, cmd: 'I' };
    gridData.value = [...gridData.value, newData]; // 마지막에 추가
    filterTypeofEquipmentPopup();
    moveScroll();
};
// 새로 추가된 항목으로 포커스 이동
const moveScroll = () => {
    const index = gridData.value.length - 1; //마지막 항목
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 설비 유형 팝업 저장
const saveTypeofEquipmentPopup = async () => {
    if (selectedValues.value.length > 0) {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        const valid = await validationStore.validateAllFields('form', true);

        if (valid) {
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

// 삭제
const deleteTypeofEquipmentPopup = () => {
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

// 설비 유형 팝업 닫기
const closeTypeofEquipmentPopup = e => {
    if (e) {
        equipmentStore.inputForm.stdEqId = e.stdEqId;
        equipmentStore.inputForm.stdEq = [{ id: e.stdEqId, name: e.stdEqNm }];
    }
    typeofEquipmentPopup.value.onClose();
};

// 설비유형 예시 팝업
const sampleSelectPopup = ref(null);

// 설비유형 예시 팝업 적용
const applyTypeofEquipmentSample = data => {
    const filteredData = getDuplicatedData(typeofEquipmentPopupDataList.value, data, 'stdEqId', 'stdEqNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.remark = el.desc;
    });

    // 예시 팝업 적용 버튼 클릭
    typeofEquipmentPopupDataList.value = [...typeofEquipmentPopupDataList.value, ...dataList];
    gridData.value = typeofEquipmentPopupDataList.value;

    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(`list_${typeofEquipmentPopupDataList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    sampleSelectPopup.value.onClose();
};
// 설비유형 예시 팝업 닫기
const closeTypeofEquipmentSample = () => {
    sampleSelectPopup.value.onClose();
};

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        equipmentStore.checkedList.push(e);
    } else {
        equipmentStore.checkedList = equipmentStore.checkedList.filter(item => item.equipmentId !== e.equipmentId);
    }
    console.log('@@ equipmentStore.checkedList'), equipmentStore.checkedList;
};

// 양식 다운로드 및 업로드
btnDownloadForm(() => {
    downloadFileFormat();
});
btnUpload(() => {
    triggerFileInput();
});

const clickTab = div => {
    if (equipmentStore.currentFilter === div.id) return;
    equipmentStore.currentFilter = div.id;
    equipmentStore.applyGrouping();
};
import * as XLSX from 'xlsx';
const handleFileUpload = event => {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();

        reader.onload = e => {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: 'array' });
            const firstSheetName = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[firstSheetName];
            let jsonData = XLSX.utils.sheet_to_json(worksheet, {
                header: 1, // 첫 번째 행을 헤더로 사용
                defval: '', // 빈 셀에 빈 문자열을 기본값으로 설정
                blankRows: false // 빈 행을 제외
            });
            // 빈 행을 제거
            jsonData = jsonData.filter(row => row.some(cell => cell !== ''));

            equipmentStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }

    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: equipmentStore.createExcel, param: '' });
};

const downloadFileFormat = () => {
    equipmentStore.getTemplate('EquipmentManage01');
};
const fileInput = ref(null);
const triggerFileInput = () => {
    fileInput.value.click();
};
</script>
