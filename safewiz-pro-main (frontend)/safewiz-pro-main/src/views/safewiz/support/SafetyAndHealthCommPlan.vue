<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 타이틀은 공통 레이아웃으로 이동 될 예정입니다. -->
        <!-- <h3>안전보건경영방침</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="shCommPlanStore.searchTerm" @keyup.enter="shCommPlanStore.applyFilter" />
                <button type="submit" class="shrink0" @click="shCommPlanStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
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
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px">
                <!-- 1 -->
                <div v-for="(year, index) in Object.keys(shCommPlanStore.filteredShCommPlanListBySearch)" :key="index" class="list">
                    <button type="button" class="df jcsb aic Abtn" @click="toggleAccordion(index)" :class="{ active: shCommPlanStore.filteredShCommPlanListBySearch[index], active: activeButtons.includes(index) }">
                        <em>{{ year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(item, yIndex) in shCommPlanStore.filteredShCommPlanListBySearch[year]" :key="yIndex">
                                    <i-card v-model="item.checked" :data="item" :disabled="item.useYn === 'N'" :type="'basic'" :title="item.title" @editor="shCommPlanStore.btnDetail(item, true)" />
                                </template>
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="shCommPlanStore.btnAdd(year)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addSafetyAndHealthCommPlan') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-if="Object.keys(shCommPlanStore.filteredShCommPlanListBySearch).length === 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="shCommPlanStore.btnAdd(getCurrentDate().substring(0, 4))">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <span class="db fs2rem c999999">{{ t('card_addSafetyAndHealthCommPlan') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, alertMsg, computed, getCurrentDate, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnAdd, btnDelete, btnDownload } = BaseView();
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnDownload'];

import { useShCommPlanStore } from '@/stores/safewiz/support/safetyAndHealthCommPlan.js';
const shCommPlanStore = useShCommPlanStore();

import { useCommunicationStore } from '@/stores/safewiz/support/communication.js';
const communicationStore = useCommunicationStore();

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const activeButtons = ref([0]);
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    shCommPlanStore.shCommPlanSegments[index] = !shCommPlanStore.shCommPlanSegments[index];
    if (activeButtons.value.includes(index)) {
        activeButtons.value = activeButtons.value.filter(i => i !== index);
    } else {
        activeButtons.value.push(index);
    }

    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
    updateTextColor(index);
};
const initAccordion = async (initIndex = null) => {
    if (initIndex === -1) {
        shCommPlanStore.filteredShCommPlanListBySearch[communicationStore.searchParam.searchText + '년도'] = [];
        const sortedYearData = new Map(Object.entries(shCommPlanStore.filteredShCommPlanListBySearch).sort((a, b) => parseInt(b[0]) - parseInt(a[0])));

        shCommPlanStore.filteredShCommPlanListBySearch = Object.fromEntries(sortedYearData);
        initIndex = await Object.keys(shCommPlanStore.filteredShCommPlanListBySearch).findIndex(el => el.substring(0, 4) === communicationStore.searchParam.searchText);
    }
    Object.keys(shCommPlanStore.filteredShCommPlanListBySearch).forEach((el, index) => {
        shCommPlanStore.shCommPlanSegments[index] = false;
    });

    shCommPlanStore.shCommPlanSegments[initIndex] = !shCommPlanStore.shCommPlanSegments[initIndex];
    accordionSet(initIndex);
};
const accordionSet = (index, duration) => {
    const segment = accordionRefs.value[index];
    if (segment) {
        gsap.to(segment, {
            height: shCommPlanStore.shCommPlanSegments[index] ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
const updateTextColor = index => {
    const button = document.querySelectorAll('.Abtn')[index]; // 버튼 선택
    if (shCommPlanStore.shCommPlanSegments[index]) {
        gsap.to(button, { color: '#3248F6', duration: 0.5 }); // active 상태일 때 색상 변경
    } else {
        gsap.to(button, { color: '#000', duration: 0.5 }); // 기본 색상
    }
};

// 조회 버튼 이벤트 함수
btnSearch(() => {
    shCommPlanStore.btnSearch(true);
});

btnBack(() => {
    shCommPlanStore.shCommPlanSegments = [];
    router.push('/Communication');
});

btnDelete(async () => {
    shCommPlanStore.btnDelete('');
});

btnAdd(() => {
    shCommPlanStore.btnAdd();
});

btnDownload(() => {
    shCommPlanStore.btnDownload();
});

// 체크된 데이터 관리
// const handleEvent = e => {
//     const { writeYear, docNo, docType, checked } = e;
//     if (checked) {
//         shCommPlanStore.checkedList.push(e);
//     } else {
//         shCommPlanStore.checkedList = shCommPlanStore.checkedList.filter(item => item.writeYear !== writeYear && item.docNo !== docNo && item.docType !== docType);
//     }
// };

import { getShCommPlanList } from '@/stores/safewiz/support/api/safetyAndHealthCommPlanApi.js';
import _ from 'lodash';

onMounted(async () => {
    // hseStore.initInputForm();
    // shCommPlanStore.btnSearch(true);
    console.log('@@@@@ ', router);
    shCommPlanStore.searchTerm = '';
    openLoading();
    getShCommPlanList(shCommPlanStore.searchParam, true)
        .then(res => {
            let planList = res.list;
            shCommPlanStore.shCommPlanList = planList.reduce((acc, item) => {
                item.checked = false;
                item.docTitle = `${item.writeYear}-${item.docType}-${item.docNo}`;
                item.innerCnt = item.innerList.filter(el => el.useYn === 'Y').length + '건';
                item.externalCnt = item.externalList.filter(el => el.useYn === 'Y').length + '건';
                item.detail = {
                    문서번호: item.docTitle,
                    작성일자: formatDate(item.writeDt),
                    작성자: item.hrNm,
                    '내부 건수': item.innerCnt,
                    '외부 건수': item.externalCnt
                };
                // createdAt에서 년도만 추출
                // const year = new Date(item.createdAt).getFullYear() + '년도';
                const year = item.writeYear + '년도';

                // 해당 년도가 없으면 새로운 배열 생성
                if (!acc[year]) {
                    acc[year] = [];
                }
                // 년도에 맞는 배열에 데이터 추가
                acc[year].push(item);

                return acc;
            }, {});
            shCommPlanStore.filteredShCommPlanList = _.cloneDeep(shCommPlanStore.shCommPlanList);
            shCommPlanStore.filteredShCommPlanListBySearch = _.cloneDeep(shCommPlanStore.shCommPlanList);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
            if (shCommPlanStore.shCommPlanSegments.length < 1) {
                let initIndex = Object.keys(shCommPlanStore.filteredShCommPlanListBySearch).findIndex(el => el.substring(0, 4) === communicationStore.searchParam.searchText);
                console.log('@ initIndex', initIndex);
                initAccordion(initIndex);
            } else {
                if (!shCommPlanStore.filteredShCommPlanListBySearch[communicationStore.searchParam.searchText + '년도']) {
                    shCommPlanStore.filteredShCommPlanListBySearch[communicationStore.searchParam.searchText + '년도'] = [];
                    const sortedYearData = new Map(Object.entries(shCommPlanStore.filteredShCommPlanListBySearch).sort((a, b) => parseInt(b[0]) - parseInt(a[0])));

                    shCommPlanStore.filteredShCommPlanListBySearch = Object.fromEntries(sortedYearData);
                }
                shCommPlanStore.shCommPlanSegments.forEach((el, index) => {
                    accordionSet(index);
                });
            }
        });
});
</script>
