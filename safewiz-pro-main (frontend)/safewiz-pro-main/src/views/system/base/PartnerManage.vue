<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-10 ul-grid12-6 lg-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="partnerStore.searchTerm" @keyup.enter="partnerStore.applyFilter" />
                        <button type="submit">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 ul-grid12-6 lg-grid12-12">
                    <button type="button" class="w100p btn radius active us-w100p" @click="openItemManagePopup">
                        <span>거래 품목 관리</span>
                    </button>
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
                <!-- 기본형 -->
                <div class="row flex gutters2rem">
                    <template v-for="(partner, index) in partnerStore.dataFilterList" :key="index">
                        <i-card :v-model="partner.checked" :data="partner" :disabled="partner.useYn == 'N'" :type="'partner'" :title="partner.partCompNm" :first-hr-nm="partner.partnerHrNmH" :second-hr-nm="partner.partnerHrNmS" :log-img="partner.logoId" @handle="handleEvent" @editor="btnDetail"></i-card>
                    </template>

                    <!-- 추가 -->
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="partnerStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>

                                <span class="db fs2rem c999999">{{ t('card_addPartner') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <input ref="fileInput" type="file" @change="handleFileUpload" accept=".xlsx, .xls" class="dn" />
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="partCompItemPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <!-- <base-select-popup :title="'직위'" uniqueKey="minorCd" filterKey="minorNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ majorCd: 'C0005' }" :fetch-data="getSystemCode" @close="closeSystemPopup" /> -->
                    <!-- 디폴트 형태의 DataSetManagePopup인 경우 :cardComponet 를 지정하지 않아도 됨. 다른 형태인 경우 :cardComponet 지정 필수  -->
                    <DataSetManagePopup :title="'거래 품목 관리'" :subTitle="'거래 품목명'" id="partCompItemId" nm="partCompItemNm" :datasetYn="'N'" :popupDataList="popupDataList" :cardComponent="RemarkDataSetManageComponent" @filter="filterItemManagePopup" @search="searchItemManagePopup" @sample="sampleItemManagePopup" @add="addItemManagePopup" @delete="deleteItemManagePopup" @save="saveItemManagePopup" @close="closeItemManagePopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="partCompItemSamplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'거래 품목 예시'" filterKey="partCompItemNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSamplePartCompItemDataset" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { validationStore, openLoading, endLoading, getCompId, ref, t, computed, onMounted, formatDate, btnSearch, btnAdd, btnDelete, btnUpload, btnDownloadForm, confirmMsg, alertMsg, getDuplicatedData, nextTick } = BaseView();
import router from '@/router';
// 협력사 store
import { usePartnerStore } from '@/stores/system/base/partner.js';
const partnerStore = usePartnerStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnUpload', 'btnDownloadForm'];

const fileInput = ref(null);
import * as XLSX from 'xlsx';
// 조회 버튼 이벤트 함수
btnSearch(() => {
    partnerStore.getPartnerList(true);
});
btnAdd(() => {
    partnerStore.btnAdd();
});
btnDelete(() => {
    partnerStore.btnDelete();
});

//엑셀 양식 다운로드
btnDownloadForm(() => downloadFileFormat());

//엑셀 업로드
btnUpload(() => triggerFileInput());

const triggerFileInput = () => {
    fileInput.value.click();
};

const downloadFileFormat = () => {
    partnerStore.getPartnerTemplate('PartnerManage01');
};

onMounted(async () => {
    let responses = await partnerStore.getPartnerList();
    if (responses && responses.list) {
        partnerStore.partnerList.value = responses.list;
        partnerStore.dataFilterList = partnerStore.partnerList;
        partnerStore.initData();
    }
});
//-----------------------------------------------  //공사와의 거래품목 팝업
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import RemarkDataSetManageComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import { getPartCompItemDataset, getSamplePartCompItemDataset, savePartCompItemDataset, deletePartCompItemDataset } from '@/stores/system/base/api/partnerApi.js';
import _ from 'lodash';
const partCompItemPopup = ref(null);
const originPopupData = ref([]);
const popupDataList = ref([]);

const popupCheckedList = computed(() => {
    return popupDataList.value.filter(el => el.checked);
});
const openItemManagePopup = () => {
    searchActionItemManagePopup(true);
    partCompItemPopup.value.onOpen();
};

// -- 검색 -----------------------
const filterItemManagePopup = text => {
    const filteredData = originPopupData.value.filter(el => el.partCompItemNm.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);
    popupDataList.value = filteredData;
};
// -- 예시보기 -----------------------
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
const partCompItemSamplePopup = ref(null);
const sampleItemManagePopup = () => {
    // 예시보기 버튼 클릭
    partCompItemSamplePopup.value.onOpen();
};
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(popupDataList.value, data, 'partCompItemId', 'partCompItemNm'); // 중복된 항목 추출
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
    popupDataList.value = [...popupDataList.value, ...dataList];
    originPopupData.value = _.cloneDeep(popupDataList.value);
    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(`list_${popupDataList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    partCompItemSamplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    partCompItemSamplePopup.value.onClose();
};
// -- 추가 -----------------------
const addItemManagePopup = () => {
    const newData = {
        cmd: 'I',
        partCompItemId: '',
        partCompItemNm: '',
        ordSeq: 99,
        useYn: 'Y',
        remark: '',
        checked: true
    };
    popupDataList.value.push(newData);
    const index = popupDataList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};
// -- 삭제 -----------------------
const deleteItemManagePopup = () => {
    if (popupCheckedList.value.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionItemManagePopup, param: popupCheckedList.value });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};
const deleteActionItemManagePopup = checkedList => {
    openLoading();
    deletePartCompItemDataset(checkedList, true)
        .then(res => {
            endLoading();
            searchActionItemManagePopup(false);
        })
        .catch(() => {
            endLoading();
        });
};
// -- 조회 -----------------------
const searchItemManagePopup = () => {
    if (popupCheckedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchActionItemManagePopup, param: true });
    } else {
        searchActionItemManagePopup(true);
    }
};
const searchActionItemManagePopup = notify => {
    openLoading();
    return new Promise(resolve => {
        getPartCompItemDataset({ compId: getCompId() }, notify)
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
// -- 저장 -----------------------
const saveItemManagePopup = async () => {
    if (popupCheckedList.value.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    console.log('### 저장데이터 ', popupCheckedList.value);
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionItemManagePopup, param: popupCheckedList.value });
        return;
    }
};
const saveActionItemManagePopup = checkedList => {
    openLoading();
    savePartCompItemDataset(checkedList, true)
        .then(res => {
            endLoading();
            searchActionItemManagePopup(false);
        })
        .catch(() => {
            endLoading();
        });
};

const closeItemManagePopup = () => {
    if (popupCheckedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: closeActionItemManagePopup, param: '' });
    } else {
        closeActionItemManagePopup();
    }
};
const closeActionItemManagePopup = () => {
    originPopupData.value = [];
    popupDataList.value = [];
    partCompItemPopup.value.onClose();
};

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        partnerStore.checkedList.push(e);
    } else {
        partnerStore.checkedList = partnerStore.checkedList.filter(item => item.partCompItem !== e.partCompItem);
    }
};

//-----------------------------------------------  //상세보기 버튼
const btnDetail = async e => {
    partnerStore.partCompId = e.partCompId;
    await partnerStore.getPartDetail(partnerStore.partCompId, false);
    //페이지 이동
    router.push({
        name: 'partnerManageDetail'
    });
};

//---------------------------------------------- //엑셀 업로드
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

            partnerStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }
    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: partnerStore.createPartnerExcel, param: '' });
};
</script>
