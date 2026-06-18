<template>
    <!-- 콘텐츠 영역 -->
    <div class="df fdc">
        <!-- 검색 필드 -->
        <div class="control-field mb2-2rem form ui">
            <div class="row flex gutters1rem">
                <div class="grid12-12 us-grid12-12">
                    <div class="df aic">
                        <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="safetyChecklistStore.searchTerm" @keyup.enter="handleSearch" />
                        <button type="button" class="shrink0 bcffffff" @click="handleSearch">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
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
            <div class="row flex gutters2rem">
                <template v-for="(segment, index) in safetyChecklistStore.dataFilterList" :key="index">
                    <i-card :v-model="segment.checked" :modelValue="segment.checked" :data="segment" :type="'basic'" :title="segment.title" @handle="handleEvent" @editor="btnDetail" :disabled="segment.useYn == 'N'" />
                </template>

                <!-- 추가 -->
                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="safetyChecklistStore.btnAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyChecklist') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView.js';

const { t, btnSearch } = BaseView();
import { ref, onMounted, defineProps } from 'vue';
import { useSafetyChecklistStore } from '@/stores/safewiz/impl/safetyChecklist.js';
const safetyChecklistStore = useSafetyChecklistStore();

defineProps({
    store: { type: Object, default: Object } // Store
});

// 초기 데이터 로딩
onMounted(async () => {
    if (!safetyChecklistStore.searchParam.writeYear) {
        safetyChecklistStore.searchParam.writeYear = safetyChecklistStore.currentDate.substring(0, 4);
    }
    // if (!safetyChecklistStore.writeYear) {
    //     safetyChecklistStore.searchParam.writeYear = safetyChecklistStore.currentDate.substring(0, 4);
    // } else {
    //     safetyChecklistStore.searchParam.writeYear = safetyChecklistStore.writeYear;
    // }
    let responses = await safetyChecklistStore.searchSafetyChecklist();
    if (responses && responses.list) {
        safetyChecklistStore.initData();
    }
});

/* ------------------------------------- */
const checkedRow = ref([]);
// 체크박스 이벤트
const handleEvent = el => {
    if (el.checked) {
        checkedRow.value.push(el);
    } else {
        const index = checkedRow.value.indexOf(el);
        if (index > -1) {
            checkedRow.value.splice(index, 1); // 배열에서 해당 요소 제거
        }
    }
};

/* ------------------------------------- */
// 검색 핸들러
const handleSearch = () => safetyChecklistStore.applyFilter();

// 상세보기 버튼
btnSearch(() => {
    safetyChecklistStore.searchSafetyChecklist(true);
});
const btnDetail = async e => {
    safetyChecklistStore.searchParam.searchText = e.stdEqId;
    safetyChecklistStore.btnDetail(e);
};

// 점검일지 이동
// const btnMove = async e => {
//     safetyChecklistStore.btnMove(e);
// };
</script>
