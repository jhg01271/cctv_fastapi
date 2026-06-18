<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->

        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-8 ul-grid12-6 lg-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="hrStore.searchTerm" @keyup.enter="hrStore.initData" />
                        <button type="submit" class="shrink0" @click.stop="hrStore.initData">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="df gap1rem us-fww">
                        <button type="button" class="w30p btn radius active us-w100p" @click="jbrpManageClick">
                            <span>직위 관리</span>
                        </button>
                        <div class="toggle w70p us-w100p">
                            <button type="button" @click="hrStore.filterTypeBtn('jbrp')" :class="{ active: hrStore.activeFilter === 'jbrp' }">
                                <span>직위</span>
                            </button>
                            <button type="button" @click="hrStore.filterTypeBtn('orgn')" :class="{ active: hrStore.activeFilter === 'orgn' }">
                                <span>조직</span>
                            </button>
                            <button type="button" @click="hrStore.filterTypeBtn('com')" :class="{ active: hrStore.activeFilter === 'com' }">
                                <span>본사·협력사</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <!-- 버튼으로 파일 업로드 -->
                <!-- <button
                    @click="triggerFileInput"
                    type="button"
                    class="w30p btn radius active us-w100p"
                >엑셀 업로드</button>-->
                <input ref="fileInput" type="file" @change="handleFileUpload" accept=".xlsx, .xls" class="dn" />
                <!-- 버튼으로 양식 다운로드 -->
                <!-- <button
                    @click="downloadFileFormat"
                    type="button"
                    class="w30p btn radius active us-w100p"
                >양식 다운로드</button>-->
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
            <div v-if="hrStore.dataFilterList !== null && hrStore.dataFilterList.length > 0" class="df fdc gap2rem">
                <template v-for="(hr, index) in hrStore.dataFilterList" :key="index">
                    <div class="title-box segment">
                        <p>{{ hr.title }}</p>

                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(i, idx) in hr.data" :key="idx">
                                    <i-card :v-model="i.checked" :data="i" :disabled="i.useYn == 'N'" :type="'profile'" :title="i.hrNm" :log-img="i.logoId ? i.logoId : ''" @handle="handleEvent" @editor="btnDetail"></i-card>
                                </template>
                                <!-- 추가 -->
                                <div v-if="hrStore.dataFilterList" class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="hrStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db fs2rem c999999">{{ t('card_addHr') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
            <div v-else>
                <!-- 추가 -->
                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="hrStore.btnAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db fs2rem c999999">{{ t('card_addHr') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="jbrpManagePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <!-- <base-select-popup :title="'직위'" uniqueKey="minorCd" filterKey="minorNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ majorCd: 'C0005' }" :fetch-data="getSystemCode" @close="closeSystemPopup" /> -->
                    <!-- 디폴트 형태의 DataSetManagePopup인 경우 :cardComponet 를 지정하지 않아도 됨. 다른 형태인 경우 :cardComponet 지정 필수  -->
                    <DataSetManagePopup :title="'직위 관리'" :datasetYn="'Y'" id="jbrpId" nm="jbrpNm" :popupDataList="popupDataList" :cardComponent="DefaultDataSetManageComponet" @filter="filterJbrpManagePopup" @search="searchJbrpManagePopup" @sample="sampleJbrpManagePopup" @add="addJbrpManagePopup" @delete="deleteJbrpManagePopup" @save="saveJbrpManagePopup" @close="closeJbrpManagePopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="jbrpSamplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'직위 예시 데이터'" filterKey="jbrpNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getDatasetHrJbrp" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, computed, onMounted, btnSearch, btnAdd, btnDelete, btnDownloadForm, btnUpload, confirmMsg, alertMsg, t, openLoading, endLoading, validationStore, getDuplicatedData, nextTick } = BaseView();
import router from '@/router';

import { useHrStore } from '@/stores/system/base/hr.js';
const hrStore = useHrStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnUpload', 'btnDownloadForm'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    hrStore.getHrList(true);
});
btnAdd(() => {
    hrStore.btnAdd();
});
btnDelete(() => {
    hrStore.btnDelete();
});
btnUpload(() => {
    triggerFileInput();
});

btnDownloadForm(() => {
    downloadFileFormat();
});

onMounted(async () => {
    hrStore.getHrList();
});

import * as XLSX from 'xlsx';
const fileInput = ref(null);
const triggerFileInput = () => {
    fileInput.value.click();
};
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

            hrStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }

    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: hrStore.createHrExcel, param: '' });
};
const downloadFileFormat = () => {
    hrStore.getHrTemplate('HRManage01');
};

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        hrStore.checkedList.push(e);
    } else {
        hrStore.checkedList = hrStore.checkedList.filter(item => item.hrId !== e.hrId);
    }
};

//-----------------------------------------------
//상세보기 버튼
const btnDetail = async e => {
    //디테일 조회
    hrStore.hrId = e.hrId;
    await hrStore.getHrDetailList(hrStore.hrId, false);
    //페이지 이동
    router.push({
        name: 'HRManageDetail'
    });
};

//-----------------------------------------------------------
//-- 직위관리
//-----------------------------------------------------------
// ------------------ 예시 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
const jbrpSamplePopup = ref(null);
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(popupDataList.value, data, 'jbrpId', 'jbrpNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        console.log('# filteredData', filteredData);
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
    });
    // 예시 팝업 적용 버튼 클릭
    popupDataList.value = [...popupDataList.value, ...dataList];
    originPopupData.value = _.cloneDeep(popupDataList.value);
    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(`list_${popupDataList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    jbrpSamplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    jbrpSamplePopup.value.onClose();
};
//
//-----------------------------------------------------------
//-- 직무관리
//-----------------------------------------------------------
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import { getHrJbrp, getDatasetHrJbrp, saveHrJbrp, deleteHrJbrp } from '@/stores/system/base/api/hrApi';
import _ from 'lodash';
const jbrpManagePopup = ref(null);
const originPopupData = ref([]);
const popupDataList = ref([]);
const checkedList = computed(() => {
    return popupDataList.value.filter(el => el.checked);
});
// --------------- 팝업 호출
const jbrpManageClick = () => {
    openLoading();
    searchActionJbrpManagePopup(true).then(res => {
        jbrpManagePopup.value.onOpen();
    });
};
// --------------- 검색
const filterJbrpManagePopup = text => {
    const filteredData = originPopupData.value.filter(el => el.jbrpNm.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);
    popupDataList.value = filteredData;
};
// --------------- 조회
const searchJbrpManagePopup = () => {
    if (checkedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchActionJbrpManagePopup, param: true });
    } else {
        searchActionJbrpManagePopup(true);
    }
};
const searchActionJbrpManagePopup = notify => {
    // 저장된 직무 데이터 조회
    openLoading();
    return new Promise(resolve => {
        getHrJbrp(notify)
            .then(res => {
                popupDataList.value = res.list;
                endLoading();
                resolve({ result: res.list, success: res.success });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                originPopupData.value = _.cloneDeep(popupDataList.value);
                setTimeout(() => {
                    const element = document.getElementById('list_0');
                    if (element) {
                        element.scrollIntoView({ block: 'center' });
                    }
                }, 100);
            });
    });
};

// --------------- 예시 불러오기
const sampleJbrpManagePopup = () => {
    jbrpSamplePopup.value.onOpen();
};

// --------------- 추가
const addJbrpManagePopup = () => {
    const newData = {
        cmd: 'I',
        jbrpId: '',
        jbrpNm: '',
        ordSeq: '',
        useYn: 'Y',
        checked: true
    };
    popupDataList.value.push(newData);

    // 새로 추가된 항목으로 포커스 이동
    const index = popupDataList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// --------------- 삭제
const deleteJbrpManagePopup = () => {
    if (checkedList.value.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionJbrpManagePopup, param: checkedList.value });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};
const deleteActionJbrpManagePopup = checkedList => {
    openLoading();
    deleteHrJbrp(checkedList, true)
        .then(res => {
            endLoading();
            searchActionJbrpManagePopup(false);
        })
        .catch(() => {
            endLoading();
        });
};
// --------------- 저장
const saveJbrpManagePopup = async () => {
    if (checkedList.value.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionJbrpManagePopup, param: checkedList.value });
        return;
    }
};
const saveActionJbrpManagePopup = checkedList => {
    openLoading();
    saveHrJbrp(checkedList, true)
        .then(res => {
            endLoading();
            searchActionJbrpManagePopup(false);
        })
        .catch(() => {
            endLoading();
        });
};
// --------------- 닫기
const closeJbrpManagePopup = () => {
    if (checkedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: closeActionJbrpManagePopup, param: '' });
    } else {
        closeActionJbrpManagePopup();
    }
};
const closeActionJbrpManagePopup = () => {
    popupDataList.value = [];
    jbrpManagePopup.value.onClose();
};

const toggleUseYn = (index, event) => {
    popupDataList.value[index].useYn = event.target.checked ? 'Y' : 'N';
};
// 값 변경에 따라 자동으로 체크상태 부여여
const chkData = item => {
    item.checked = true;
};
</script>
