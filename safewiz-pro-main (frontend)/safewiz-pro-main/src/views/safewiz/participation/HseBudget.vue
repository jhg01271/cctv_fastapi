<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb1-6rem es-fww">
            <div class="row flex gutters1rem">
                <div class="grid12-3 us-grid12-12">
                    <input v-model="hseBudgetStore.searchParam.writeYear" @input="writeYearChange" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" year />
                </div>
                <div class="grid12-9 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="hseBudgetStore.searchTerm" @keyup.enter="hseBudgetStore.initData" />
                        <button type="submit" class="shrink0">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            class="h100p box"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <!-- 우측 상단 작성, 검토, 승인 -->
                <div class="pa2-2rem" v-if="hseBudgetStore.dataFilterList && hseBudgetStore.dataFilterList.length > 0" :key="hseBudgetStore.signCmd">
                    <ISignature ref="signatureComponent" :cmd="hseBudgetStore.signCmd" targetType="BDG" :writeYear="hseBudgetStore.searchParam.writeYear" :docNo="hseBudgetStore.budgetList[0].docNoO"></ISignature>
                </div>
                <hr />
                <!-- 아코디언 영역 -->
                <div class="df fdc gap1rem pa2-2rem">
                    <div class="accordion df fdc rg8px">
                        <!-- 1차 아코디언 -->
                        <div v-for="(segment, index) in hseBudgetStore.dataFilterList" :key="index" class="list wsn">
                            <button type="button" class="df jcsb aic es-fww" :id="`accordion-btn_${index}`" @click="hseBudgetStore.toggleAccordion">
                                <em class="ellipsis es-w100p es-tal">{{ segment.name }}</em>

                                <div class="df aic es-w100p es-jcsb">
                                    <i class="fs1-5rem mr1-2rem es-mt1rem">
                                        {{ t('objectives_budget_won') }}
                                        <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(segment.totalAmt) }}</span>
                                    </i>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </div>
                            </button>
                            <div class="segment oh bcF8F9FB">
                                <div class="pa2-2rem df fdc gap1rem">
                                    <!-- 2차 아코디언 -->
                                    <div v-for="(id, index) in segment.data" :key="index" class="list wsn">
                                        <button type="button" class="df jcsb aic es-fww" @click="hseBudgetStore.toggleAccordion">
                                            <em class="ellipsis es-w100p es-tal">{{ id.name }}</em>
                                            <div class="df aic es-w100p es-jcsb">
                                                <i class="fs1-5rem mr1-2rem es-mt1rem">
                                                    {{ t('objectives_budget_won') }}
                                                    <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(id.totalAmt) }}</span>
                                                </i>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                                </svg>
                                            </div>
                                        </button>
                                        <!-- 3차 아코디언 -->
                                        <div class="segment oh bcFFFFFF">
                                            <div class="pa2-2rem df fdc gap1rem">
                                                <div v-for="(idDetail, index) in id.data" :key="index" class="list">
                                                    <button type="button" class="df jcsb aic es-fww" @click="hseBudgetStore.toggleAccordion">
                                                        <em class="ellipsis es-w100p es-tal">{{ '(' + idDetail.subTitle + ') ' }}{{ idDetail.name }}</em>

                                                        <div class="df aic es-w100p es-jcsb">
                                                            <i class="fs1-5rem mr1-2rem es-mt1rem">
                                                                {{ t('objectives_budget_won') }}
                                                                <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(idDetail.totalAmt) }}</span>
                                                            </i>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </button>
                                                    <!-- 세부 내용 -->
                                                    <div v-show="idDetail.name" ref="thirdAccordion" class="segment oh bcF8F9FB">
                                                        <div class="pa2-2rem">
                                                            <OverlayScrollbarsComponent
                                                                :options="{
                                                                    scrollbars: {
                                                                        autoHide: 'hover',
                                                                        x: 'visible',
                                                                        y: 'hidden'
                                                                    }
                                                                }"
                                                            >
                                                                <div class="oh us-minw42rem">
                                                                    <div class="row flex gutters2rem" :key="idDetail.data">
                                                                        <template v-for="(item, index) in idDetail.data" :key="index">
                                                                            <i-card v-if="item.orgnNm" :v-model="item.checked" :data="item" :disabled="item.useYn == 'N'" :title="item.orgnNm" @handle="handleEvent" @editor="btnDetail"></i-card>
                                                                        </template>
                                                                    </div>
                                                                </div>
                                                            </OverlayScrollbarsComponent>
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
const { setRouterParam, ref, onMounted, nextTick, t, formatToAmt, btnSearch, btnDownload } = BaseView();

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import ISignature from '@/components/common/iSignature.vue';

//-----------------------------------------------
// [스토어]
import { useHseBudgetStore } from '@/stores/safewiz/participation/hseBudget.js';
const hseBudgetStore = useHseBudgetStore();

//-----------------------------------------------
// [버튼리스트]
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnDownload'];

btnSearch(async () => {
    await hseBudgetStore.getActionPerformanceList(true);
    if (signatureComponent.value != null) {
        // 데이터 없을때는 서명 조회안함
        await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }
});

// 출력 버튼
btnDownload(() => hseBudgetStore.btnDownload());
//-----------------------------------------------

const signatureComponent = ref();

//-----------------------------------------------

//-----------------------------------------------
// [onMounted]
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        hseBudgetStore.searchParam.writeYear = param.writeYear;
    } else if (!hseBudgetStore.searchParam.writeYear) {
        //현재날짜 세팅
        hseBudgetStore.searchParam.writeYear = hseBudgetStore.currentDate.slice(0, 4);
    }
    //조회
    hseBudgetStore.getActionPerformanceList(true);
    hseBudgetStore.signature = signatureComponent.value;
});

const writeYearChange = async () => {
    await hseBudgetStore.getActionPerformanceList(true);
    signatureComponent.value?.Search();
};

//-----------------------------------------------
// [체크박스 데이터]
const handleEvent = e => {
    const { writeYear, docNo, docType } = e;
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        hseBudgetStore.checkedList.push(e);
    } else {
        hseBudgetStore.checkedList = hseBudgetStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
    }
};

// ---------------------------------------------------

// Accordion
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
// const animateAccordion = (element, isOpen) => {
//     gsap.to(element, {
//         height: isOpen ? 'auto' : 0,
//         duration: 0.5,
//         ease: 'customEase'
//     });
// };

// 요소의 중첩 레벨을 구하는 함수

// 개별 아코디언 토글 함수
// const toggleAccordion = async event => {
//     const button = event.currentTarget;
//     const segmentElement = button.nextElementSibling;
//
//     const isOpen = button.classList.toggle('active');
//     await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
//     animateAccordion(segmentElement, isOpen);
// };

//-----------------------------------------------
// [상세보기 버튼]
const btnDetail = async e => {
    //디테일 조회
    hseBudgetStore.searchParam = {
        writeYear: e.writeYearO,
        docType: e.docTypeO,
        docNo: e.docNoO,
        docSeq: e.docSeqO,
        docDetailSeq: e.docDetailSeqO,
        orgnId: e.orgnIdO
    };

    await hseBudgetStore.getActionPerformanceDetailList(e, false);
};
</script>

<style scoped>
.segment {
    overflow: hidden;
}
</style>
