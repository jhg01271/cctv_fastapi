<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="orgManageStore.searchTerm" @keyup.enter="handleSearch" />
                <button type="button" class="shrink0" @click="handleSearch">
                    <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                </button>
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
                <div class="row flex gutters2rem">
                    <template v-for="org in orgManageStore.orgnList" :key="org.orgnId">
                        <!-- <i-card :v-model="org.checked" :data="org" :disabled="org.useYn === 'N'" :title="org.orgnNm" :user-hr="true" :first-hr-nm="org.headHrNm" :first-hr-image-id="org.primaryId" :second-hr-nm="org.secondHrNm" :second-hr-image-id="org.secondaryId" @handle="handleEvent" @editor="btnDetail" /> -->
                        <i-card :v-model="org.checked" :data="org" :disabled="org.useYn === 'N'" :title="org.orgnNm" :user-hr="true" :headHrList="org.headHrId ? [{ id: org.headHrId, hrNm: org.headHrNm, logoId: org.primaryId }] : []" :secondHrList="org.secondHrId ? [{ id: org.secondHrId, hrNm: org.secondHrNm, logoId: org.secondaryId }] : []" @handle="handleEvent" @editor="btnDetail" />
                    </template>

                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="orgManageStore.addOrg()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addOrgn') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <input ref="fileInput" type="file" @change="handleFileUpload" accept=".xlsx, .xls" class="dn" />
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useOrganizationStore } from '@/stores/system/base/organization.js';
import { useButtonListStore } from '@/stores/buttonList';
import router from '@/router';

const { btnSearch, btnAdd, btnDelete, btnUpload, btnDownloadForm, t, ref, confirmMsg } = BaseView();
const orgManageStore = useOrganizationStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnUpload', 'btnDownloadForm'];

const fileInput = ref(null);
import * as XLSX from 'xlsx';
import { useHrStore } from '@/stores/system/base/hr.js';
const hrStore = useHrStore();

// 조회 버튼
btnSearch(() => orgManageStore.getOrgnList(true));

// 추가 버튼
btnAdd(() => orgManageStore.addOrg());

// 삭제 버튼
btnDelete(() => orgManageStore.btnDelete());

//엑셀 양식 다운로드
btnDownloadForm(() => downloadFileFormat());

//엑셀 업로드
btnUpload(() => triggerFileInput());

const downloadFileFormat = () => {
    orgManageStore.getOrgnTemplate('OrgnManage01');
};

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

            orgManageStore.excelData = jsonData; // 데이터를 배열로 저장
        };

        reader.readAsArrayBuffer(file);
        event.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    }
    //엑셀 데이터 저장
    confirmMsg('info', t('msgSave'), '', { fun: orgManageStore.createOrgnExcel, param: '' });
};

// 초기 데이터 로딩
onMounted(async () => {
    const responses = await orgManageStore.getOrgnList(true);
    if (responses?.list) {
        orgManageStore.organizationList.value = responses.list;
        orgManageStore.orgnList = orgManageStore.organizationList;
        orgManageStore.initData();
    }
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { orgnId, checked } = e;
    if (checked) {
        orgManageStore.checkedList.push(e);
    } else {
        orgManageStore.checkedList = orgManageStore.checkedList.filter(item => item.orgnId !== orgnId);
    }
};

// 검색 핸들러
const handleSearch = () => orgManageStore.applyFilter();

// 상세보기 버튼
const btnDetail = async e => {
    orgManageStore.orgnId = e.orgnId;
    router.push({ name: 'organizationManageDetail' });
};
</script>
