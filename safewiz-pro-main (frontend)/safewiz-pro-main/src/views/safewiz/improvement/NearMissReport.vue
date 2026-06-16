<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form df mb2-2rem">
            <div class="df aic w50p jcfe gap4px">
                <!-- <template v-for="div in nearStore.nearMissReportDivList" :key="div.id">
                    <button type="button" class="btn radius w12rem" :class="{ active: nearStore.currentFilter === div.id }" @click="nearStore.currentFilter = div.id">
                        <span>{{ div.name }}</span>
                    </button>
                </template> -->
            </div>
        </div>
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-6 us-grid12-12">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="nearStore.searchParam.writeYear" @update:model-value="yearChanged" class="datepicker w20rem radius es-w100p" />
                    </div>
                </div>
                <div class="grid12-6 es-grid12-6 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="nearStore.searchParam.content" @keyup.enter="nearStore.applyFilter" />
                        <button type="button" class="shrink0">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <div class="toggle w100p">
                        <button type="button" v-for="div in nearStore.nearMissReportDivList" :key="div.id" @click="nearStore.currentFilter = div.id" :class="{ active: nearStore.currentFilter === div.id }">
                            <span>{{ div.name }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 작성조직 -->
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh df fdc gap2rem" v-if="nearStore.currentFilter === 'creationOrgan'">
                <template v-for="(wp, i) in Object.keys(nearStore.nearCreatCardList)" :key="i">
                    <div class="segment title-box">
                        <p>{{ wp }}</p>
                        <div v-if="wp.length > 0" class="row flex gutters2rem">
                            <template v-for="(near, index) in nearStore.nearCreatCardList[wp]" :key="index">
                                <i-card :v-model="near.checked" :data="near" :disabled="near.useYn === 'N'" :title="near.writeDt" @handle="handleEvent" @editor="btnDetail"> </i-card>
                            </template>
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="nearStore.btnAdd('temp', true)">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addNearMiss') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
                <!-- 데이터가 없을 때 작성조직 탭 아차사고 보고서 등록 버튼-->
                <div v-if="!Object.values(nearStore.nearCreatCardList).length > 0">
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="nearStore.btnAdd('temp')">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db mt1rem fs2rem c999999">{{ t('card_addNearMiss') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 조치조직 -->
            <div class="oh" v-else-if="nearStore.currentFilter === 'actionOrgan'">
                <template v-for="(wp, i) in Object.keys(nearStore.nearActionCardList)" :key="i">
                    <div class="segment mb2-2rem title-box" v-if="wp != null && wp != ''">
                        <p>{{ wp }}</p>
                        <div v-if="wp.length > 0" class="row flex gutters2rem">
                            <template v-for="(near, index) in nearStore.nearActionCardList[wp]" :key="index">
                                <i-card :v-model="near.checked" :data="near" :disabled="near.useYn === 'N'" :title="near.writeDt" @handle="handleEvent" @editor="btnDetail"></i-card>
                            </template>
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="nearStore.btnAdd('temp', true)">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addNearMiss') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
                <!-- 데이터가 없을 때 조치조직 탭 아차사고 보고서 등록 버튼-->
                <div v-if="!Object.values(nearStore.nearCreatCardList).length > 0">
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="nearStore.btnAdd('temp')">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db mt1rem fs2rem c999999">{{ t('card_addNearMiss') }}</span>
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
import { useButtonListStore } from '@/stores/buttonList';
import { useNonconformityCStore } from '@/stores/safewiz/improvement/nonconformityCorrective.js';
import { useNearMissReportStore } from '@/stores/safewiz/improvement/nearMissReport.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router/index.js';

// loading panel
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
const { t, onMounted, btnAdd, btnBack, btnSearch, btnDelete, btnDownload, confirmMsg, goRouter, alertMsg } = BaseView();
const nonconformityCStore = useNonconformityCStore();
const nearStore = useNearMissReportStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

onMounted(async () => {
    nearStore.searchParam.writeYear = nonconformityCStore.searchParam.writeYear;
    await nearStore.btnSearch(true);
});

//목록 버튼
btnBack(() => {
    router.push({ name: 'NonconformityCorrective' });
    nonconformityCStore.searchParam.writeYear = nearStore.searchParam.writeYear;
});

//조회 버튼
btnSearch(() => {
    loadingPanelStore.openLoading();
    nearStore.btnSearch(true);
});

//추가 버튼
btnAdd(() => {
    nearStore.btnAdd();
});

//삭제 버튼
btnDelete(async () => {
    loadingPanelStore.openLoading();
    await nearStore.btnDelete();
    loadingPanelStore.endLoading();
});

//출력 버튼
btnDownload(() => {
    if(nearStore.checkedList.length === 0){
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('info', t('msgPrint'), null, { fun: printAction });
});

const printAction = async () => {
    await nearStore.btnPrint('main');
};

const btnDetail = async e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };

    goRouter('NearMissReportDetail', param);
};

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    if (e.checked) {
        nearStore.checkedList.push(e);
    } else {
        nearStore.checkedList = nearStore.checkedList.filter(item => item.docNo !== e.docNo);
    }
};

const yearChanged = () => {
    nearStore.btnSearch(true);
};
</script>
