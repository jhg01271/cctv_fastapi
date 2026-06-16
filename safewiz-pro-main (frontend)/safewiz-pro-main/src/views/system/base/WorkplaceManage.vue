<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic w100p bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="workplaceStore.searchTerm" @keyup.enter="workplaceStore.applyFilter" />
                <button type="submit" class="shrink0" @click="workplaceStore.applyFilter"><img src="/assets/img/common/icon_search.svg" alt /></button>
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
                <!-- 썸네일형 -->
                <div class="row flex gutters2rem">
                    <template v-for="(workplace, index) in workplaceStore.filteredWpList" :key="index">
                        <i-card :v-model="workplace.checked" :data="workplace" :disabled="workplace.useYn == 'N'" :title="workplace.workplaceNm" :user-hr="true" :use-thumbnail="true" :thumbnail-img="workplace.thumbnailId" :headHrList="workplace.headHrList" :secondHrList="workplace.secondHrList" @handle="handleEvent" @editor="btnDetail"></i-card>
                    </template>

                    <!-- 추가 -->
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="workplaceStore.btnAdd('temp')">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>

                                <span class="db mt1rem fs2rem c999999">{{ t('card_addWorkplace') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <input ref="fileInput" type="file" @change="handleFileUpload" accept=".xlsx, .xls" class="dn" />
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, onMounted, t, confirmMsg, formatDate, watch, btnSearch, btnAdd, btnDelete, btnDownloadForm, btnUpload } = BaseView();
import router from '@/router';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownloadForm', 'btnUpload'];

import { useWorkplaceStore } from '@/stores/system/base/workplace.js';
const workplaceStore = useWorkplaceStore();

const workplace = ref(null); // PopupDialog에 대한 ref

onMounted(async () => {
    let responses = await workplaceStore.getWorkplaceList(true);
    if (responses && responses.list) {
        workplaceStore.workplaceList = responses.list;
        workplaceStore.filteredWpList = responses.list;
    }
    workplaceStore.applyFilter();
});

// 폼 초기화 후 팝업 열기 2024.07.01
const addWorkplace = () => {
    workplace.value.onOpen();
};

// 팝업 닫기
const closeWorkplace = () => {
    if (workplace.value) {
        workplace.value.onClose();
    }
};

// 조회 버튼 이벤트 함수
btnSearch(() => {
    workplaceStore.getWorkplaceList(true);
});
btnAdd(async () => {
    workplaceStore.workplaceId = 'temp';
    await workplaceStore.btnAdd(workplaceStore.workplaceId);
});
btnDelete(() => {
    workplaceStore.btnDelete();
});

// 양식 다운로드 및 업로드
btnDownloadForm(() => {
    downloadFileFormat();
});
btnUpload(() => {
    triggerFileInput();
});

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

            workplaceStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }

    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: workplaceStore.createExcel, param: '' });
};

const downloadFileFormat = () => {
    workplaceStore.getTemplate('WorkplaceManage01');
};
const fileInput = ref(null);
const triggerFileInput = () => {
    fileInput.value.click();
};
//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        workplaceStore.checkedList.push(e);
    } else {
        workplaceStore.checkedList = workplaceStore.checkedList.filter(item => item.workplaceId !== e.workplaceId);
    }
};

//-----------------------------------------------
//상세보기 버튼
const btnDetail = async e => {
    //디테일 조회
    workplaceStore.workplaceId = e.workplaceId;
    await workplaceStore.getWorkplaceDetail(workplaceStore.workplaceId, false);
    //페이지 이동
    router.push({
        name: 'WorkplaceManageDetail'
    });
};
</script>
