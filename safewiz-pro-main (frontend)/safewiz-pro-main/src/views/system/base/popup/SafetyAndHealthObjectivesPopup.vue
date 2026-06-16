<template>
    <em class="fs1-8rem">추진 계획 선택</em>
    <div class="form ui mt1-4rem maxh60rem bd1pxsolidE1E6ED br4px">
        <!-- <OverlayScrollbarsComponent
            class="maxh60rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        > -->
        <div class="row flex">
            <div class="grid2-1 ha bdr1pxsolidE1E6ED rg-grid2-2 rg-bdr0px rg-bdr0pxsolidE1E6ED rg-bdb1pxsolidE1E6ED">
                <!-- 아코디언 영역 -->
                <!-- maxh40rem 으로 높이 수정 가능합니다. -->
                <OverlayScrollbarsComponent
                    class="maxh40rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <div ref="popupAccordion">
                        <div class="accordion">
                            <!-- 1차 아코디언 -->
                            <div v-for="(segment, index) in detailedActionStore.dataFilterList" :key="index" class="list w100p wbka">
                                <button type="button" class="df jcsb aic gap8px px1-2rem py1-4rem w100p" @click="toggleAccordion" :id="`${index}`">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 22 22" fill="none">
                                        <path d="M13.75 9.16732L10.5155 12.6417C10.2768 12.8981 9.88987 12.8981 9.65121 12.6417L6.41667 9.16732" stroke="black" stroke-width="0.916667" stroke-linecap="round" />
                                    </svg>
                                    <p class="w100p tal fs1-5rem w100p">{{ segment.name }}</p>
                                </button>
                                <div ref="firstAccordion" class="segment oh">
                                    <!-- 2차 아코디언 -->
                                    <div ref="subAccordionRef" v-for="(id, index) in segment.data" :key="index" class="list">
                                        <button type="button" class="df jcsb aic gap8px bcF8F9FB w100p h4-6rem px1-2rem" @click="subToggleAccordion">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 22 22" fill="none">
                                                <path d="M13.75 9.16732L10.5155 12.6417C10.2768 12.8981 9.88987 12.8981 9.65121 12.6417L6.41667 9.16732" stroke="black" stroke-width="0.916667" stroke-linecap="round" />
                                            </svg>
                                            <svg class="sub" xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 22 22" fill="none">
                                                <path d="M14.6662 11.0007L17.6846 14.0191C18.0426 14.3771 18.0426 14.9575 17.6846 15.3155L14.6662 18.334M7.33282 4.58398L7.33282 12.834C7.33282 13.8465 8.15363 14.6673 9.16615 14.6673L17.4162 14.6673" stroke="#9EA1A6" stroke-linecap="round" />
                                            </svg>
                                            <p class="w100p tal fs1-5rem">{{ id.name }}</p>
                                        </button>
                                        <!-- 3차 아코디언 -->
                                        <div v-if="id.name" ref="secondAccordion" class="segment oh bcFFFFFF third">
                                            <!-- 세부 내용 -->
                                            <div class="w100p df fdc gap1-6rem py1-4rem">
                                                <div v-for="(item, index) in id.data" :key="index" @click="onClickRows(item)" class="w100p cp df aic gap8px pl6rem">
                                                    <svg class="sub" xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 22 22" fill="none">
                                                        <path d="M14.6662 11.0007L17.6846 14.0191C18.0426 14.3771 18.0426 14.9575 17.6846 15.3155L14.6662 18.334M7.33282 4.58398L7.33282 12.834C7.33282 13.8465 8.15363 14.6673 9.16615 14.6673L17.4162 14.6673" stroke="#9EA1A6" stroke-linecap="round" />
                                                    </svg>
                                                    <p class="fs1-5rem">{{ item.name }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
            <!-- 선택 영역 -->
            <div class="grid2-1 rg-grid2-2">
                <!-- maxh40rem 으로 높이 수정 가능합니다. -->
                <OverlayScrollbarsComponent
                    class="maxh40rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <div class="pa1-6rem df fdc gap8px wbka">
                        <div v-for="(item, index) in detailedActionStore.inputForm" :key="index" class="df gap1rem px12px py8px bd1pxsolidE1E6ED br4px">
                            <input class="pt1rem" v-input type="checkbox" @click="onClickItem(item)" />
                            <input v-input type="text" v-model="item.detailPlan" class="mr4px" readonly />
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
        </div>
        <!-- </OverlayScrollbarsComponent> -->
    </div>
    <!-- 닫기 버튼 -->
    <div class="form ui tar mt1rem">
        <button type="button" v-button class="btn w80px radius bright-grey" @click="btnClose">
            <span>{{ t('close') }}</span>
        </button>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t } = BaseView();
const popupAccordion = ref(null);
import { gsap } from 'gsap';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

// Store
import { useDetailedActionPopupStore } from '@/stores/safewiz/participation/detailedActionPopup.js';
// import { useSafetyHealthStore } from '@/stores/safewiz/planning/safetyAndHealthObjectives';

// const safetyHealthStore = useSafetyHealthStore();
const detailedActionStore = useDetailedActionPopupStore();

const emit = defineEmits(['close', 'selected']);

const firstAccordion = ref(null);
const secondAccordion = ref(null);
const subAccordionRef = ref([]);

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 아코디언 세팅
const toggleAccordion = async event => {
    let button = event.currentTarget;

    const segmentElement = button.nextElementSibling;
    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

const subToggleAccordion = async event => {
    let button = event.currentTarget;

    const segmentElement = button.nextElementSibling;
    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

// 상세 조회
const onClickRows = async item => {
    detailedActionStore.inputForm = item.data;
};

const onClickItem = async item => {
    emit('selected', item);
};

// 팝업 닫기
const btnClose = () => {
    detailedActionStore.inputForm = ''; //팝업 닫을때 초기화
    emit('close');
};

onMounted(async () => {
    // if (!detailedActionStore.writeYear) {
    //     // 현재날짜 세팅
    //     detailedActionStore.writeYear = detailedActionStore.currentDate.slice(0, 4);
    // }
    //
    // detailedActionStore.searchParam.searchText = detailedActionStore.writeYear;
    // let writeYear = detailedActionStore.searchParam.searchText.slice(0, 4);
    // detailedActionStore.searchParam.writeYear = safetyHealthStore.inputForm.writeYear; //2024.12.10 김현재 작성 팝업오픈시 writeYear(작성년도)가 없으면 조회안되어서 넣어줌
    // detailedActionStore.searchParam.orgnId = safetyHealthStore.inputForm.orgnId;
    // let responses = await Promise.all([detailedActionStore.getActionMonthlyList(true)]);
    const buttons = popupAccordion.value.querySelectorAll('.accordion button');
    await buttons.forEach(button => {
        button.classList.add('active');
        const segment = button.nextElementSibling;
        animateAccordion(segment, true);
    });
});
</script>
<style scoped></style>
