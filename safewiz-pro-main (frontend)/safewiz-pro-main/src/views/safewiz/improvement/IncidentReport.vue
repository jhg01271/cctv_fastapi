<!-- 화 면 명 :   재해발생보고서 -->
<!-- 작 성 자 :   마환구        -->
<!-- 시작일자 : 2024.11.05      -->

<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-6 us-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="nonconformityCStore.searchParam.writeYear" @update:model-value="yearChanged" class="datepicker w20rem radius es-w100p" />
                </div>
                <div class="grid12-6 es-grid12-6 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="IncidentReportStore.searchTerm" @keyup.enter="IncidentReportStore.applyFilter" />
                        <button type="button" class="shrink0" @click="IncidentReportStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>

                <div class="grid12-3 es-grid12-12">
                    <div class="toggle df">
                        <template v-for="div in IncidentReportStore.IncidentReportDivList" :key="div.id">
                            <button type="button" :class="{ active: IncidentReportStore.currentFilter === div.id }" @click="IncidentReportStore.currentFilter = div.id">
                                <span>{{ div.name }}</span>
                            </button>
                        </template>
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
                <!-- 조직 -->
                <div v-if="IncidentReportStore.currentFilter === 'Organ'">
                    <template v-for="(creatReport, i) in Object.keys(IncidentReportStore.IncidentOrgnList)" :key="i">
                        <div v-if="IncidentReportStore.IncidentOrgnList[creatReport].length > 0" class="segment mb2-2rem box">
                            <div class="pt2-2rem pb1-2rem px2-2rem">
                                <h3>{{ creatReport }}</h3>
                            </div>
                            <hr />
                            <div class="pa2-2rem">
                                <div class="row flex gutters2rem">
                                    <template v-for="(creat, index) in IncidentReportStore.IncidentOrgnList[creatReport]" :key="index">
                                        <i-card :v-model="creat.checked" :data="creat" :disabled="creat.useYn === 'N'" :title="creat.incidentPersonNm" :use-thumbnail="false" :thumbnail-img="creat.fileId" @handle="handleEvent" @editor="btnDetail"></i-card>
                                    </template>
                                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear, IncidentReportStore.IncidentOrgnList[creatReport][0].creatReportList)">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_addIncident') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 작성조직 탭 재해발생 보고서 등록 버튼-->
                    <div v-if="!Object.values(IncidentReportStore.IncidentOrgnList).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear)">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addIncident') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 발생장소 -->
                <div v-else-if="IncidentReportStore.currentFilter === 'place'">
                    <template v-for="(actReport, i) in Object.keys(IncidentReportStore.IncidnetLocationList)" :key="i">
                        <div v-if="IncidentReportStore.IncidnetLocationList[actReport].length > 0" class="segment mb2-2rem box">
                            <div class="pt2-2rem pb1-2rem px2-2rem">
                                <h3>{{ actReport }}</h3>
                            </div>
                            <hr />
                            <div class="pa2-2rem">
                                <div class="row flex gutters2rem">
                                    <template v-for="(act, index) in IncidentReportStore.IncidnetLocationList[actReport]" :key="index">
                                        <i-card :v-model="act.checked" :data="act" :disabled="act.useYn === 'N'" :title="act.incidentPersonNm" :use-thumbnail="false" :thumbnail-img="act.fileId" @handle="handleEvent" @editor="btnDetail"></i-card>
                                    </template>
                                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear, IncidentReportStore.IncidnetLocationList[actReport][0].actReportList)">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_addIncident') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 조치조직 탭 재해발생 보고서 등록 버튼-->
                    <div v-if="!Object.values(IncidentReportStore.IncidnetLocationList).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear)">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>

                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addIncident') }}</span>
                                </div>
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
import { useIncidentReportStore } from '@/stores/safewiz/improvement/IncidentReport.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router/index.js';

const { ref, t, onMounted, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, goRouter } = BaseView();
const nonconformityCStore = useNonconformityCStore();
const IncidentReportStore = useIncidentReportStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

onMounted(async () => {
    IncidentReportStore.searchParam.writeYear = nonconformityCStore.searchParam.writeYear;
    IncidentReportStore.btnSearch(true);
    IncidentReportStore.checkedList = [];
});

//목록 버튼
btnBack(() => {
    // nonconformityCStore.searchParam.writeYear = IncidentReportStore.searchParam.writeYear;
    router.push({ name: 'NonconformityCorrective' });
});

btnAdd(() => {
    IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear);
});

//삭제 버튼
btnDelete(async () => {
    await IncidentReportStore.btnDelete();
});

btnSearch(() => {
    IncidentReportStore.btnSearch(true);
});

btnDownload(() => {
    IncidentReportStore.btnPrint('A');
});

const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };

    goRouter('IncidentReportDetail', param);
};

const handleEvent = e => {
    if (e.checked) {
        IncidentReportStore.checkedList.push(e);
    } else {
        IncidentReportStore.checkedList = IncidentReportStore.checkedList.filter(item => item.docNo !== e.docNo);
    }
};

const yearChanged = () => {
    IncidentReportStore.searchParam.writeYear = nonconformityCStore.searchParam.writeYear;
    IncidentReportStore.btnSearch(true);
};
</script>
