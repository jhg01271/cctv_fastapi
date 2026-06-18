<template>
    <!-- 콘텐츠 영역 -->
    <h3>법규 관리 선택</h3>

    <div class="form ui field mb2-2rem">
        <div class="df aic">
            <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="legalReviewDetailStore.popSearchTerm" @keydown.enter="legalReviewDetailStore.popApplyFilter()" />
            <button type="submit" v-button @click="legalReviewDetailStore.popApplyFilter()">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
        </div>

        <div class="row flex">
            <div class="grid12-4 es-grid12-12">
                <div class="field">
                    <div class="my1-5rem">
                        <input id="confirmedYn" v-input="['확정', '전체']" type="checkbox" class="df aic switch" :checked="isConfirmed" @change="toggleConfirmed" />
                    </div>
                </div>
            </div>
        </div>

        <div class="bd1pxsolide1e6ed br4px wbka">
            <div class="row flex minh40rem">
                <div class="grid12-4 bdr1pxsolide1e6ed es-grid12-12 es-bdr0pxsolide1e6ed es-bdb1pxsolide1e6ed es-pb5rem">
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
                        <ul>
                            <li class="pa8px bdb1pxsolide1e6ed" v-for="(item, index) in legalReviewDetailStore.filteredLegalManageList" :key="index" :class="{ selected: item.selected }">
                                <button class="px1rem w100p h4-4rem c9ea1a6 fs1-5rem br4px tal" type="button" v-button @click="clickItem(item)">
                                    <span>{{ item.legalNm }} [{{ item.writeYear + '-' + item.docNo }}]</span>
                                </button>
                            </li>
                        </ul>
                    </OverlayScrollbarsComponent>
                </div>
                <div class="grid12-8 es-grid12-12">
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
                        <!-- 아코디언 영역 -->
                        <div class="accordion form ui pa1rem df fdc gap gap1-2rem wbka">
                            <!-- 1 -->
                            <div v-for="(item, index) in legalReviewDetailStore.filteredLegalManageDetailList" class="list lgmd" :key="index">
                                <button type="button" class="dib aic" @click="toggleAccordion(index)" :class="{ active: isAccordionState[index] }" :title="item.articleTitle">
                                    <div class="df aic gap1rem">
                                        <div @click.stop>
                                            <input class="fs0" type="checkbox" v-input v-model="item.checked" @click="selectItem(item)" />
                                        </div>
                                        <em class="ellipsis">{{ item.articleTitle }}</em>
                                        <div class="df jcfe fg1">
                                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </div>
                                    </div>
                                </button>

                                <div :ref="el => (accordionRefs[index] = el)" class="segment oh">
                                    <!-- 아코디언 래핑 요소 -->
                                    <div class="pa1rem bcF8F9FB">
                                        <div class="field df fdc">
                                            <!-- <label>법규 내용</label> -->
                                            <textarea class="mt4px minh15rem br4px" placeholder="" :value="item.articleContent" readOnly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, defineEmits, nextTick, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import _ from 'lodash';
import { useLegalReviewDetailStore } from '@/stores/safewiz/planning/LegalReviewDetail';
const legalReviewDetailStore = useLegalReviewDetailStore();
import { useLegalManageStore } from '@/stores/safewiz/planning/LegalManage.js';
const legalManageStore = useLegalManageStore();

const emit = defineEmits('selected');

onMounted(async () => {
    // 검색어 초기화
    legalReviewDetailStore.popSearchTerm = '';

    await legalReviewDetailStore.popClearData();
    await legalReviewDetailStore.getLegalManageList(true);

    // mount 직후 초기화
    cloneOriginalData();
    filterConfirmed();
});

// 원본 복제본 저장용
const originalManageList = ref([]);
const originalManageDetailList = ref([]);

const cloneOriginalData = () => {
    originalManageList.value = _.cloneDeep(legalReviewDetailStore.filteredLegalManageList);
    originalManageDetailList.value = _.cloneDeep(legalReviewDetailStore.filteredLegalManageDetailList);
};

const selectItem = item => {
    if (!item.selected) {
        legalReviewDetailStore.singleSelect(item);
        emit('selected', item);
    }
};

// index별 열림 상태를 저장
const isAccordionState = ref([]);
const toggleAccordion = async index => {
    isAccordionState.value[index] = !isAccordionState.value[index];

    await nextTick();
    await accordionSet(index, 0.5);
};

// 아코디언 토글 이벤트
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
const accordionRefs = ref([]);
const accordionSet = async (index, duration) => {
    const segment = accordionRefs.value[index];
    if (segment) {
        gsap.to(segment, {
            height: isAccordionState.value[index] ? segment.scrollHeight + 'px' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

// 확정/전체 토글 제어
const isConfirmed = ref(true); // true: 확정만, false: 전체
const toggleConfirmed = e => {
    isConfirmed.value = e.target.checked;
    filterConfirmed();
};

// 확정/전체 데이터 적용
const filterConfirmed = async () => {
    if (isConfirmed.value) {
        legalReviewDetailStore.filteredLegalManageList = legalReviewDetailStore.legalManageList.filter(item => item.confirmedYn === 'Y').map(item => ({ ...item, selected: false }));
    } else {
        legalReviewDetailStore.filteredLegalManageList = _.cloneDeep(legalReviewDetailStore.legalManageList).map(item => ({ ...item, selected: false }));
    }

    if (legalReviewDetailStore.filteredLegalManageList.length > 0) {
        await clickItem(legalReviewDetailStore.filteredLegalManageList[0]);
    } else {
        legalReviewDetailStore.filteredLegalManageDetailList = [];
    }
};

const clickItem = async item => {
    // 모든 소분류 체크 해제
    legalReviewDetailStore.filteredLegalManageDetailList.forEach(i => (i.checked = false));

    // 아코디언 닫기 상태로 초기화
    isAccordionState.value = [];

    await legalReviewDetailStore.clickItem(item);

    // 새로 로드된 세부항목 수 기준으로 false로 채움
    isAccordionState.value = legalReviewDetailStore.filteredLegalManageDetailList.map(() => false);

    await nextTick();

    // 모든 아코디언 닫기
    for (let i = 0; i < isAccordionState.value.length; i++) {
        await accordionSet(i, 0.5);
    }
};
</script>
<style lang="scss" scoped>
.form.ui {
    .accordion {
        .list {
            > button {
                padding: 1rem;
                em {
                    font-size: 1.5rem;
                }
            }
        }
    }
    ul {
        li {
            &.selected {
                button {
                    background: #f8f9fb;
                    color: #3248f6;
                }
            }
        }
    }
    .field {
        > label {
            font-size: 1.4rem;
            height: 21px;
            line-height: 21px;
        }
    }
    textarea {
        padding: 4px;
    }
}
</style>
