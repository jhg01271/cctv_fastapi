<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-12 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="myHseJobAssignmentStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘dd" />
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
            <div class="oh">
                <div class="row flex gutters2rem">
                    <template v-for="jobAssign in myHseJobAssignmentStore.dataFilterList" :key="jobAssign.jobAssignId">
                        <i-card :v-model="jobAssign.checked" :modelValue="jobAssign.checked" :data="jobAssign" :disabled="jobAssign.useYn === 'N'" :title="jobAssign.hrNm" @handle="handleEvent" @editor="btnDetail" />
                    </template>

                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div v-if="router.currentRoute.value?.meta?.savAh == 'Y'" class="card h100p df aic jcc cp" @click="myHseJobAssignmentStore.btnAdd()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addHseJobAssignment') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { onMounted } from 'vue';
import { useMyHseJobAssignmentStore } from '@/stores/safewiz/participation/myHseJobAssignment.js';
import { useButtonListStore } from '@/stores/buttonList';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import { useTaskStore } from '@/stores/safewiz/task/task.js';

const { t, btnSearch, btnAdd, btnDelete, getPreMonth, getCurrentDate, formatDateForDB } = BaseView();
const myHseJobAssignmentStore = useMyHseJobAssignmentStore();
const layoutStore = useButtonListStore();
const taskStore = useTaskStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete'];

// 조회 버튼
btnSearch(() => myHseJobAssignmentStore.getJobAssignList(false));

// 추가 버튼
btnAdd(() => myHseJobAssignmentStore.btnAdd());

// 삭제 버튼
btnDelete(() => myHseJobAssignmentStore.btnDelete());

// 초기 데이터 로딩
onMounted(async () => {
    myHseJobAssignmentStore.searchParam.writeYear = null;
    myHseJobAssignmentStore.getJobAssignList(false);
});

// 체크된 데이터 관리
const handleEvent = e => {
    if (e.checked) {
        // 체크된 데이터를 checkedList에 추가
        myHseJobAssignmentStore.checkedList.push(e);
    } else {
        // e의 writeYear, docNo, docType 값이 일치하는 데이터를 제외한 리스트 생성
        myHseJobAssignmentStore.checkedList = myHseJobAssignmentStore.checkedList.filter(item => item.writeYear !== e.writeYear || item.docNo !== e.docNo || item.docType !== e.docType);
    }
};
// 검색 핸들러
const handleSearch = () => myHseJobAssignmentStore.applyFilter();

// 상세보기 버튼
const btnDetail = async e => {
    taskStore.startDay = getPreMonth(1);
    taskStore.endDay = getCurrentDate();
    myHseJobAssignmentStore.searchParam.writeYear = e.writeYear;
    myHseJobAssignmentStore.searchParam.docNo = e.docNo;
    myHseJobAssignmentStore.searchParam.docType = e.docType;
    myHseJobAssignmentStore.searchParam.hrId = e.hrId;
    myHseJobAssignmentStore.searchParam.userId = e.userId;
    await myHseJobAssignmentStore.getMyJobAssignDetailList(false);
    taskStore.getTaskList({
        writeYear: myHseJobAssignmentStore.searchParam.writeYear,
        docType: myHseJobAssignmentStore.searchParam.docType,
        docNo: myHseJobAssignmentStore.searchParam.docNo,
        searchText: myHseJobAssignmentStore.inputForm.userId,
        searchFrom: formatDateForDB(taskStore.startDay),
        searchTo: formatDateForDB(taskStore.endDay)
    });
    router.push({ name: 'MyHseJobAssignmentDetail' });
};
</script>
