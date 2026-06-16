<template>
    <div class="df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input type="text" v-model="hazChemsInoutStore.searchParam.writeYear" v-calendar="'yyyy'" class="datepicker w100p radius" year @input="hazChemsInoutStore.btnSearch(false)" />
                </div>
                <div class="grid12-9 es-grid12-12">
                    <div class="df aic w100p bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="hazChemsInoutStore.searchTerm" @keyup.enter="hazChemsInoutStore.applyFilter" />
                        <button type="button" class="shrink0" @click="hazChemsInoutStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 조직 필터 -->

        <div class="df fdc gap1-2rem">
            <div v-if="Object.keys(hazChemsInoutStore.filteredByOrgnListBySearch).length > 0">
                <template v-for="(key, i) in Object.keys(hazChemsInoutStore.filteredByOrgnListBySearch)" :key="i">
                    <div v-if="hazChemsInoutStore.filteredByOrgnListBySearch[key].length > 0" class="title-box">
                        <p class="fw500">{{ key }}</p>
                        <div class="pa1rem">
                            <div class="row flex gutters2rem" :key="hazChemsInoutStore.filteredByOrgnListBySearch[key]">
                                <template v-for="(value, index) in hazChemsInoutStore.filteredByOrgnListBySearch[key]" :key="index">
                                    <i-card :v-model="value.checked" :modelValue="value.checked" :data="value" :disabled="value.useYn === 'N'" :title="value.hazmatNm" :approvalStatus="value.approvalStatus" @handle="handleEvent" @editor="hazChemsInoutStore.btnDetail(value, true)" />
                                </template>
                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="hazChemsInoutStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addHazMatsInspection') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
            <div v-else>
                <div class="title-box">
                    <p class="fw500"></p>
                    <div class="pa1rem">
                        <div class="row flex gutters2rem" >
                            <!-- 추가 -->
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="hazChemsInoutStore.btnAdd()">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addHazMatsInspection') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { onMounted } from 'vue';
import { useButtonListStore } from '@/stores/buttonList';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';

const { t, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, alertMsg, confirmMsg, getCurrentDate } = BaseView();

//-----------------------------------------------
// [스토어]
import { useHazChemsInoutStore } from '@/stores/safewiz/impl/hazChemsInout.js';
const hazChemsInoutStore = useHazChemsInoutStore();

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// 조회 버튼
// btnSearch(() => {
//     hazChemsInoutStore.btnSearch(false);
// });

// 추가 버튼
btnAdd(() => {
    hazChemsInoutStore.btnAdd();
});

// 출력 버튼
btnDownload(() => {
    let checkedList = hazChemsInoutStore.getCheckedList();
    if(checkedList.length === 0){
        alertMsg('error', t('msgNoItem'))
        return
    }
    let param = checkedList.map(el => el.docNo);
    confirmMsg('info', '선택한 항목을 출력하시겠습니까?', null, { fun: btnDownloadAction, param: param });
});

const btnDownloadAction = param =>{
    hazChemsInoutStore.btnDownload(param);
}

btnDelete(() => {
    hazChemsInoutStore.btnDelete();
});

// 초기 데이터 로딩
onMounted(async () => {
    if (!hazChemsInoutStore.searchParam.writeYear) {
        //현재날짜 세팅
        hazChemsInoutStore.searchParam.writeYear = getCurrentDate().substring(0, 4);
    }
    hazChemsInoutStore.btnSearch(false);
});

// 체크된 데이터 관리
const handleEvent = e => {
    const { writeYear, docNo, docType, checked } = e;
    if (checked) {
        hazChemsInoutStore.checkedList.push(e);
    } else {
        hazChemsInoutStore.checkedList = hazChemsInoutStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};
</script>
