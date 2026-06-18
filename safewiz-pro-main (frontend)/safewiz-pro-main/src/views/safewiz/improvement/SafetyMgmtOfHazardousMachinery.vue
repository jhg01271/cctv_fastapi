<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="form ui mb2-2rem lg-mb1rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input v-model="safetyMgmtStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="safetyMgmtStore.btnSearch" />
                </div>
                <div class="grid12-5 lg-grid12-9 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" v-model="safetyMgmtStore.searchParam.content" @keydown.enter="safetyMgmtStore.search" class="radius w100p search" placeholder="검색어를 입력하세요" />
                        <button type="button">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 lg-grid12-6">
                    <button type="button" class="btn active radius w100p" @click="voluntarySafetyInspector.onOpen()">자체검사원 등록부</button>
                </div>
                <div class="grid12-2 lg-grid12-6">
                    <div class="toggle w100p">
                        <button v-for="div in safetyMgmtStore.useSafetyMgmtOfHazardousMachineryDivList" :key="div.id" type="button" :class="{ active: safetyMgmtStore.currentFilter === div.id }" @click="safetyMgmtStore.currentFilter = div.id">
                            <span>{{ div.name }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- 메인 출력 필드 -->
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
            v-if="safetyMgmtStore.currentFilter === 'organization'"
        >
            <div class="df fdc gap1-2rem" v-if="safetyMgmtStore.orgnList.length > 0 && safetyMgmtStore.orgnList">
                <div v-for="(orgn, index) in safetyMgmtStore.orgnList" :key="index" class="title-box" >
                    <p class="fw500">{{ orgn }}</p>
                    <div class="pa1rem">
                        <div class="row flex gutters2rem">
                            <template v-for="(item, idx) in safetyMgmtStore.safetyMgmtCardList" :key="idx">
                                <i-card v-if="item.orgn === orgn" :v-model="item.checked" :data="item" :disabled="item.useYn == 'N'" :title="item.title" @editor="btnDetail"></i-card>
                            </template>
                            <!-- 추가 -->
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="safetyMgmtStore.goWrite">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>

                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyMgmtOfHazardousMachinery') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 추가 -->
            <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-else>
                <div class="card h100p df aic jcc cp" @click="safetyMgmtStore.goWrite">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>

                        <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyMgmtOfHazardousMachinery') }}</span>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
            v-if="safetyMgmtStore.currentFilter === 'equipment'"
        >
            <div class="df fdc gap1-2rem">
                <div v-for="(equip, index) in safetyMgmtStore.equipList" :key="index" class="title-box">
                    <p class="fw500">{{ equip.stdEqNm }}</p>
                    <div class="pa1rem">
                        <div class="row flex gutters2rem">
                            <template v-for="(item, idx) in safetyMgmtStore.safetyMgmtCardList" :key="idx">
                                <i-card v-if="item.stdEqId.includes(equip.stdEqId)" :v-model="item.checked" :data="item" :disabled="item.useYn == 'N'" :title="item.title" @editor="btnDetail"></i-card>
                            </template>
                            <!-- 추가 -->
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="safetyMgmtStore.goWrite">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>

                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyMgmtOfHazardousMachinery') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <!-- 자체검사원 등록부 팝업 -->
    <teleport to="body">
        <i-PopupDialog ref="voluntarySafetyInspector">
            <div class="contents w1024px md-w100p">
                <voluntary-safety-inspector-popup @close="voluntarySafetyInspector.onClose()" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { useSafetyMgmtOfHazardousMachineryStore } from '@/stores/safewiz/improvement/safetyMgmtOfHazardousMachinery';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref } from 'vue';
import VoluntarySafetyInspectorPopup from '@/views/system/base/popup/VoluntarySafetyInspectorPopup.vue';
// import { gsap } from 'gsap';
import { useContinualImprovementStore } from '@/stores/safewiz/improvement/continualImprovement';

const layoutStore = useButtonListStore();
const { btnAdd, btnSearch, btnDelete, btnBack, onMounted, t, btnDownload, alertMsg, confirmMsg } = BaseView();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const continualImprovementStore = useContinualImprovementStore();
const safetyMgmtStore = useSafetyMgmtOfHazardousMachineryStore();

const voluntarySafetyInspector = ref(null);

// 아코디언
const activeSegments = ref({});
const accordionRefs = ref([]);

// const toggleAccordion = async index => {
//     activeSegments.value[index] = !activeSegments.value[index];

//     await nextTick(); // DOM 업데이트 후 실행

//     const segment = accordionRefs.value[index];

//     if (segment) {
//         gsap.to(segment, {
//             height: activeSegments.value[index] ? 'auto' : 0,
//             duration: 0.5,
//             ease: 'customEase'
//         });
//     } else {
//         console.warn(`GSAP target for index ${index} not found`);
//     }
// };

// 해당 뷰 출력시 최초 한번 작동
onMounted(() => {
    if (safetyMgmtStore.searchParam.writeYear === "") {
        safetyMgmtStore.searchParam.writeYear = continualImprovementStore.searchParam.searchText;
    }
    safetyMgmtStore.initList();
    safetyMgmtStore.btnSearch(true);
});

// 목록 버튼
btnBack(() => {
    safetyMgmtStore.btnBack();
});

// 조회 버튼
btnSearch(() => {
    safetyMgmtStore.btnSearch(true);
});

// 추가 버튼
btnAdd(() => {
    console.log(safetyMgmtStore.cmd);
    safetyMgmtStore.goWrite();
});

// 삭제 버튼
btnDelete(() => {
    safetyMgmtStore.btnDelete();
});

btnDownload(() => {
    //데이터가 아예 존재하지 않을 경우 예외처리
    if(safetyMgmtStore.safetyMgmtCardList.length === 0){
        alertMsg('error',t('msgNoPrintData'))
        return
    }
    const checkedList = safetyMgmtStore.safetyMgmtCardList.filter(item => item.checked === true)
    if (checkedList.length === 0) {
        confirmMsg('warning', t('msgPrint'), null, { fun: safetyMgmtStore.btnDownload, param: safetyMgmtStore.safetyMgmtCardList });
        return
    }
    confirmMsg('warning', t('msgCheckedPrint'), null, { fun: safetyMgmtStore.btnDownload, param: checkedList });
});

// 카드 상세정보 보기 버튼
const btnDetail = detail => {
    safetyMgmtStore.goDetail(detail);
};
</script>
