<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem gap1rem es-fww">
            <div class="row flex gutters2rem">
                <div class="grid12-3 sm-grid12-6">
                    <div class="field">
                        <input v-model="hseStore.sugSearchParam.searchFrom" v-input type="text" v-calendar="getDateFormat()" class="datepicker w50p radius" />
                    </div>
                </div>
                <div class="grid12-3 sm-grid12-6">
                    <div class="field">
                        <input v-model="hseStore.sugSearchParam.searchTo" v-input type="text" v-calendar="getDateFormat()" class="datepicker w50p radius" />
                    </div>
                </div>
                <div class="grid12-6 sm-grid12-12">
                    <div class="field df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="hseStore.searchSugTerm" @keyup.enter="hseStore.applySugFilter" />
                        <button type="submit" class="shrink0">
                            <img src="/assets/img/common/icon_search.svg" alt />
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
            <!-- 아코디언 영역 -->

            <div class="accordion" v-if="Object.keys(hseStore.filteredSugList).length > 0">
                <!-- 1 -->
                <div v-for="(title, index) in Object.keys(hseStore.filteredSugList)" :key="index" class="mb1-8rem" :class="{ list: hseStore.filteredSugList[title].length > 0 }">
                    <button v-if="hseStore.filteredSugList[title].length > 0" type="button" class="pr df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <i class="pa w100p h1px bcE1E6ED l0 neg-b1px"></i>
                        <em>{{ getTitle(hseStore.filteredSugList[title][0]) }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh bcFFFFFF">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem bcFFFFFF us-pa1rem" v-for="(sug, sugIdx) in hseStore.filteredSugList[title]" :key="sug.hsePolicySugId + sugIdx">
                            <div class="title-box">
                                <div class="df aic jcsb">
                                    <p>
                                        {{ `작성일 : ${formatDate(sug.createdAt)}` }}<span class="dib mx2rem w2px h1-4rem bcE1E6ED es-dn"></span><span v-if="sug.updatedAt" class="es-db es-mt5px">{{ `수정일 : ${formatDate(sug.updatedAt)}` }}</span>
                                    </p>
                                    <div>
                                        <input :checked="sug.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" disabled />
                                    </div>
                                </div>

                                <div class="pa2rem">
                                    <span class="fs1-6rem fw500">근로자의견</span>
                                    <p class="fs1-5rem lh1-5 fw300 mt1-2rem" v-html="sug.workerSuggestion.replaceAll('\n', '<br />')"></p>
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
import { nextTick } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { ref, alertMsg, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnSuggestionAdd } = BaseView();
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSuggestionAdd'];
import { useHsePolicyStore } from '@/stores/safewiz/participation/hsePolicy.js';
const hseStore = useHsePolicyStore();
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
onMounted(async () => {
    if (!hseStore.inputForm.dataType) {
        // 새로고침시 이전 화면으로 이동.
        router.push('/HsePolicy');
        return;
    }
    hseStore.btnSugSearch();
});
const segments = ref([{ year: '김철수 (일주지앤에스, 책임)' }, { year: '이영희 (일주지앤에스, 선임)' }]);

const activeSegments = ref({});
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    activeSegments.value[index] = !activeSegments.value[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
const getTitle = item => {
    let title = '';
    if (item.jbrpNm) {
        title = `${item.hrNm} (${item.compNm}, ${item.jbrpNm})`;
    } else {
        title = `${item.hrNm} (${item.compNm})`;
    }
    return title;
};
btnBack(() => {
    router.go(-1);
});
btnSearch(() => {
    hseStore.btnSugSearch();
});
btnSuggestionAdd(() => {
    hseStore.btnHistSearch();
    router.push('/HsePolicySuggestionsDetail');
});
</script>
