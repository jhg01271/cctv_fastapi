<template>
    <!-- 검색 필드 -->
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem es-fww">
            <div class="row flex gutters1rem">
                <div class="grid12-3 us-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" v-model="detailedActionStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" year @input="changeWriteYear" />
                </div>
                <div class="grid12-9 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="detailedActionStore.searchTerm" @keyup.enter="detailedActionStore.initData" />
                        <button type="submit" class="shrink0" @click="detailedActionStore.initData">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            class="box"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <div :key="detailedActionStore.searchParam.writeYear + detailedActionStore.searchParam.docNo">
                    <div class="pa2rem" v-if="detailedActionStore.dataFilterList && detailedActionStore.dataFilterList.length > 0">
                        <ISignature ref="signatureComponent" :isWriter="false" :cmd="detailedActionStore.signCmd" :targetType="'OBJP'" :writeYear="detailedActionStore.searchParam.writeYear" :docNo="detailedActionStore.searchParam.docNo"></ISignature>
                    </div>

                    <hr />
                    <!-- 아코디언 영역 -->
                    <div class="accordion df fdc rg8px pa2rem">
                        <!-- 1차 아코디언 -->
                        <div v-for="(segment, index) in detailedActionStore.dataFilterList" :key="index" class="list wsn">
                            <button type="button" class="df jcsb aic bcFFFFFF es-fww" :id="`accordion-btn_${index}`" @click="detailedActionStore.toggleAccordion">
                                <em class="ellipsis">{{ segment.name }}</em>
                                <div class="df aic es-w100p es-jcsb">
                                    <i class="fs1-5rem mr1-2rem es-mt1rem">
                                        {{ t('objectives_budget_won') }}
                                        <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(segment.totalAmt) }}</span>
                                    </i>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </div>
                            </button>
                            <div class="segment oh">
                                <div class="pa2rem df fdc gap8px bcF8F9FB">
                                    <!-- 2차 아코디언 -->
                                    <div v-for="(id, index) in segment.data" :key="index" class="list">
                                        <button type="button" class="df jcsb aic bcFFFFFF es-fww" @click="detailedActionStore.toggleAccordion">
                                            <em class="ellipsis">{{ id.name }}</em>
                                            <div class="df aic es-w100p es-jcsb">
                                                <i class="fs1-5rem mr1-2rem es-mt1rem">
                                                    {{ t('objectives_budget_won') }}
                                                    <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(id.totalAmt1) }}</span>
                                                </i>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                                </svg>
                                            </div>
                                        </button>
                                        <!-- 3차 아코디언 -->
                                        <div v-if="id.name" class="segment oh">
                                            <!-- 세부 내용 -->
                                            <div class="row flex gutters1rem">
                                                <div class="w100p df pa2rem gap1-4rem fww" :key="id.data">
                                                    <template v-for="(item, index) in id.data" :key="index">
                                                        <i-card v-if="item.name" :v-model="item.checked" :modelValue="item.checked" :data="item" :disabled="item.useYn == 'N'" :title="item.name" @handle="handleEvent" @editor="btnDetail"></i-card>
                                                    </template>
                                                    <!-- 추가 -->
                                                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                                        <div class="card h100p df aic jcc cp" @click="detailedActionStore.btnAdd(id.data[0])">
                                                            <div class="tac">
                                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                                </svg>

                                                                <span class="db mt1rem fs2rem c999999 wspl">{{ t('card_addDetailedActionPlan') }}</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 추가 중점 추진 목표 영역 유지 -->
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
const { setRouterParam, ref, onMounted, nextTick, t, formatToAmt, btnSearch, btnBack, btnDelete, btnDownload /* computed, validationStore, getCompId, confirmMsg, formatDate, getFormattedDate, btnSave */ } = BaseView();
import router from '@/router';

import _ from 'lodash';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';

//-----------------------------------------------
// [스토어]
import { useDetailedActionStore } from '@/stores/safewiz/participation/detailedActionPlan.js';
const detailedActionStore = useDetailedActionStore();

import { useHseObjectivesStore } from '@/stores/safewiz/participation/hseObjectives.js';
const hseObjectivesStore = useHseObjectivesStore();
//-----------------------------------------------
// [버튼리스트]
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnDelete', 'btnDownload'];

// 조회 버튼 이벤트 함수
btnBack(() => {
    detailedActionStore.goBack();
    hseObjectivesStore.searchParam.searchText = detailedActionStore.searchParam.writeYear;
});

btnSearch(async () => {
    detailedActionStore.getActionMonthlyList(true);
    if (detailedActionStore.dataFilterList !== null) {
        signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }
});
btnDelete(() => {
    detailedActionStore.btnDelete();
});
btnDownload(() => {
    detailedActionStore.btnDetailDownload();
});

//-----------------------------------------------

//-----------------------------------------------
// [onMounted]
const signatureComponent = ref();
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        detailedActionStore.searchParam.writeYear = param.writeYear;
        detailedActionStore.searchParam.docType = 'OBJ';
        detailedActionStore.searchParam.docNo = param.docNo;
    } else if (!detailedActionStore.writeYear) {
        //현재날짜 세팅
        detailedActionStore.writeYear = detailedActionStore.currentDate.slice(0, 4);
    }
    const success = await detailedActionStore.getActPlanMasterList(false);
    if (success) {
        detailedActionStore.searchParam.writeYear = success.writeYear;
        detailedActionStore.searchParam.docNo = success.docNo;
    }
    console.log('writeYear: ', detailedActionStore.searchParam.writeYear);
    console.log('docNo: ', detailedActionStore.searchParam.docNo);
    detailedActionStore.signature = signatureComponent.value;
    await nextTick();
    signatureComponent.value?.Search();

    //조회
    // detailedActionStore.getActionMonthlyList(true);
});

const changeWriteYear = async () => {
    const success = await detailedActionStore.getActPlanMasterList(false);
    if (success) {
        detailedActionStore.searchParam.writeYear = success.writeYear;
        detailedActionStore.searchParam.docNo = success.docNo;
    }
    console.log('writeYear: ', detailedActionStore.searchParam.writeYear);
    console.log('docNo: ', detailedActionStore.searchParam.docNo);
    await nextTick();
    signatureComponent.value.Search();
};

//-----------------------------------------------
//-----------------------------------------------

//-----------------------------------------------
// [체크박스 데이터]
const handleEvent = e => {
    // 체크된 데이터를 checkedList에 추가
    console.log('e', e);
    if (e.checked) {
        detailedActionStore.checkedList.push(...e.data);
    } else {
        // e.data 안의 detailItemId 값들을 추출
        const detailItemIds = e.data.map(item => item.detailItemId);

        // checkedList에서 detailItemId가 detailItemIds 배열에 포함되지 않은 항목만 유지
        detailedActionStore.checkedList = detailedActionStore.checkedList.filter(item => !detailItemIds.includes(item.detailItemId));
    }
};

//-----------------------------------------------

// ---------------------------------------------------
// 아코디언

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 개별 아코디언 토글 함수
// const toggleAccordion = async event => {
//     const button = event.currentTarget;
//     const segmentElement = button.nextElementSibling;
//     const isOpen = button.classList.toggle('active');
//     await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
//     animateAccordion(segmentElement, isOpen);
// };

//-----------------------------------------------
// [상세보기 버튼]
const btnDetail = async e => {
    //디테일 조회
    console.log('세부', e);
    let data = e.data[0];
    detailedActionStore.actionKey = data.writeYear + '_' + data.docType + '_' + data.docNo + '_' + data.compId + '_' + data.docSeq + '_' + data.docDetailSeq;
    detailedActionStore.param = {
        writeYear: data.writeYear,
        docType: data.docType,
        docNo: data.docNo,
        compId: data.compId,
        docSeq: data.docSeq,
        detailItemId: data.detailItemId
    };
    await detailedActionStore.getActionMonthlyDetailList(detailedActionStore.param, false);
    detailedActionStore.isInputActive = false;
    //페이지 이동
    router.push({
        name: 'DetailedActionPlanDetail'
    });
};
</script>
