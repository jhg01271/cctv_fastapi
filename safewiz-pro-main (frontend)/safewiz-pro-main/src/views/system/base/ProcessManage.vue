<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-8 el-grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="processStore.searchTerm" @keyup.enter="processStore.initData" />
                        <button type="submit" class="shrink0" @click="processStore.initData">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-4 el-grid12-6 es-grid12-12">
                    <!-- 추후 변경 예정 -->
                    <div class="toggle field df gap1rem">
                        <button type="button" @click="processStore.filterTypeBtn('orgn')" :class="{ active: processStore.activeFilter === 'orgn' }">
                            <span>조직</span>
                        </button>
                        <button type="button" @click="processStore.filterTypeBtn('work')" :class="{ active: processStore.activeFilter === 'work' }">
                            <span>작업장</span>
                        </button>
                        <button type="button" @click="processStore.filterTypeBtn('com')" :class="{ active: processStore.activeFilter === 'com' }">
                            <span>본사·도급·혼재</span>
                        </button>
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
            <div v-if="processStore.dataFilterList && processStore.dataFilterList[0]">
                <template v-for="(process, index) in processStore.dataFilterList" :key="index">
                    <div class="segment mb2-2rem title-box">
                        <div class="title" :title="process.title">
                            <p class="fs19px lc2-1-3">{{ process.title }}</p>
                        </div>
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(i, idx) in process.data" :key="idx">
                                    <i-card :v-model="i.checked" :data="i" :disabled="i.useYn == 'N'" :title="i.processNm" :user-hr="true" :use-thumbnail="true" :thumbnail-img="i.fileId" :headHrList="i.headHrList" :secondHrList="i.secondHrList" @handle="handleEvent" @editor="btnDetail"></i-card>
                                </template>
                                <!-- 추가 -->
                                <div v-if="processStore.dataFilterList" class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="processStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db fs2rem c999999">{{ t('card_addProcess') }}</span>
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
                    <div class="card h100p df aic jcc cp" @click="processStore.btnAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db fs2rem c999999">{{ t('card_addProcess') }}</span>
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
const { ref, onMounted, t, confirmMsg, formatDate, watch, btnSearch, btnAdd, btnDelete, btnDownloadForm, btnUpload } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';

import { useProcessStore } from '@/stores/system/base/process.js';
const processStore = useProcessStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownloadForm', 'btnUpload'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    processStore.getPrcsList(true);
});
btnAdd(() => {
    processStore.btnAdd();
});
btnDelete(() => {
    processStore.btnDelete();
});

onMounted(async () => {
    let responses = await processStore.getPrcsList(true);
    if (responses && responses.list) {
        processStore.prcsList = responses.list;
        processStore.initData();
    }
});

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        processStore.checkedList.push(e);
    } else {
        processStore.checkedList = processStore.checkedList.filter(item => item.processId !== e.processId);
    }
};

//-----------------------------------------------
//상세보기 버튼
const btnDetail = async e => {
    console.log('e', e);
    //디테일 조회
    processStore.processId = e.processId;
    await processStore.getPrcsDetailList(processStore.processId, false);
    //페이지 이동
    router.push({
        name: 'ProcessManageDetail'
    });
};

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

            processStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }

    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: processStore.createExcel, param: '' });
};

const downloadFileFormat = () => {
    processStore.getTemplate('PrcsManage01');
};
const fileInput = ref(null);
const triggerFileInput = () => {
    fileInput.value.click();
};
</script>
