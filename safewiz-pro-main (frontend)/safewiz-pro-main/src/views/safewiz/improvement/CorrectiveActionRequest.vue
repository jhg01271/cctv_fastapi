<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <!----------------------- -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-6 us-grid12-12">
                    <!--달력-->
                    <div class="df aic fs1-7rem">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="nonconformityCStore.searchParam.writeYear" @input="(correctiveActionRequestStore.searchParam.content = ''), correctiveActionRequestStore.btnSearch(true)" class="datepicker w20rem radius es-w100p" />
                    </div>
                </div>
                <div class="grid12-6 us-grid12-12">
                    <!--검색-->
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="correctiveActionRequestStore.searchParam.content" @keyup.enter="correctiveActionRequestStore.applyFilter" />
                        <button type="button" class="shrink0" @click="correctiveActionRequestStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <!--탭-->
                    <div class="toggle df aic jcfe gap4px">
                        <template v-for="div in correctiveActionRequestStore.useCorrectiveActionRequestStoreDivList" :key="div.id">
                            <button type="button" :class="{ active: correctiveActionRequestStore.currentFilter === div.id }" @click="(correctiveActionRequestStore.currentFilter = div.id), correctiveActionRequestStore.applyFilter()">
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
                <!-- 주관조직 -->
                <div v-if="correctiveActionRequestStore.currentFilter === 'ctrlOrgan'">
                    <template v-for="(wp, i) in Object.keys(correctiveActionRequestStore.carCtrlCardList)" :key="i">
                        <div class="segment mb2-2rem title-box">
                            <p>{{ wp }}</p>
                            <div v-if="wp.length > 0" class="row flex gutters2rem">
                                <template v-for="(car, index) in correctiveActionRequestStore.carCtrlCardList[wp]" :key="index">
                                    <i-card :v-model="car.checked" :data="car" :disabled="car.useYn === 'N'" :title="car.title" @handle="handleEvent" @editor="btnDetail"></i-card>
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="correctiveActionRequestStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addCorrectiveActionRequest') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 주관조직 탭 시정조치 보고서 등록 버튼-->
                    <div v-if="!Object.values(correctiveActionRequestStore.carCtrlCardList).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="correctiveActionRequestStore.btnAdd()">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addCorrectiveActionRequest') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 조치조직 -->
                <div v-else-if="correctiveActionRequestStore.currentFilter === 'actionOrgan'">
                    <template v-for="(wp, i) in Object.keys(correctiveActionRequestStore.carActionCardList)" :key="i">
                        <div class="segment mb2-2rem title-box">
                            <p>{{ wp }}</p>
                            <div v-if="wp.length > 0" class="row flex gutters2rem">
                                <template v-for="(car, index) in correctiveActionRequestStore.carActionCardList[wp]" :key="index">
                                    <i-card :v-model="car.checked" :data="car" :disabled="car.useYn === 'N'" :title="car.title" @handle="handleEvent" @editor="btnDetail"></i-card>
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="correctiveActionRequestStore.btnAdd()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addCorrectiveActionRequest') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 조치조직 탭 시정조치 보고서 등록 버튼-->
                    <div v-if="!Object.values(correctiveActionRequestStore.carActionCardList).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="correctiveActionRequestStore.btnAdd()">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addCorrectiveActionRequest') }}</span>
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
import { useCorrectiveActionRequestStore } from '@/stores/safewiz/improvement/correctiveActionRequest.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router/index.js';

const { ref, t, onMounted, btnBack, btnAdd, btnSearch, btnDelete, btnDownload, goRouter } = BaseView();
const nonconformityCStore = useNonconformityCStore();
const correctiveActionRequestStore = useCorrectiveActionRequestStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

onMounted(async () => {
    await correctiveActionRequestStore.initSearchParam();
    correctiveActionRequestStore.btnSearch(true);
    correctiveActionRequestStore.currentFilter = 'ctrlOrgan';
});

//==============우측 메뉴 버튼 start=================
//목록 버튼
btnBack(() => {
    //nonconformityCStore.searchParam.writeYear = correctiveActionRequestStore.searchParam.writeYear;
    router.push({ name: 'NonconformityCorrective' });
});

//조회 버튼
btnSearch(() => {
    correctiveActionRequestStore.btnSearch(true);
});

//추가 버튼
btnAdd(() => {
    correctiveActionRequestStore.btnAdd();
});

//삭제 버튼
btnDelete(() => {
    correctiveActionRequestStore.btnDelete();
});

//출력 버튼
btnDownload(() => {
    correctiveActionRequestStore.btnPrint();
});

const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    }
    goRouter('CorrectiveActionRequestDetail', param);
}

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        correctiveActionRequestStore.checkedList.push(e);
    } else {
        correctiveActionRequestStore.checkedList = correctiveActionRequestStore.checkedList.filter(item => item.docNo !== e.docNo);
    }
};

//==============우측 메뉴 버튼 end=================
</script>
