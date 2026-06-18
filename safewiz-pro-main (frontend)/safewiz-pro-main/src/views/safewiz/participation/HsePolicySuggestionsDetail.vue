<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc form ui">
        <div class="box form ui bcFFFFFF">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh"></div>
                <h3>근로자 의견 작성</h3>
                <div class="h8rem df jcc aic mb1rem br4px bcF9FAFF us-px1rem">
                    <i><img src="/assets/img/common/hpsd_intro_icon.svg" /></i>
                    <p class="fs1-5rem fw400">
                        안전보건경영에 대한<br class="dn us-db" />
                        여러분의 소중한 의견을 남겨주세요.
                    </p>
                </div>
                <textarea name id class="minh30rem br4px" placeholder="의견을 작성해 주세요" v-model="hseStore.sugInputForm.workerSuggestion"></textarea>

                <h3 class="mt3rem">지난 내 의견 보기</h3>
                <!-- 아코디언 영역 -->
                <div class="accordion" v-if="Object.keys(hseStore.mySugList).length > 0">
                    <!-- 1 -->
                    <div v-for="(title, index) in Object.keys(hseStore.mySugList)" :key="hseStore.mySugList[title][0].hsePolicyId + index" class="list mb1rem">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                            <div class="df aic gap2rem">
                                <input ref="titleRefs" type="checkbox" v-input class="shrink0" :checked="hseStore.mySugList[title]?.length === hseStore.mySugList[title].filter(el => el.checked)?.length" @change="checkBoxValueChanged(title, index)" />
                                <em>{{ title }}</em>
                            </div>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div ref="accordionRefs" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div v-for="(sug, sugIdx) in hseStore.mySugList[title]" :key="sug.hsePolicySugId + sugIdx">
                                <div class="pa2-2rem bcFFFFFF us-pa1rem">
                                    <div class="title-box">
                                        <div class="df jcsb aic">
                                            <div class="df aic gap2rem us-gap1rem">
                                                <input type="checkbox" v-input class="shrink0" v-model="sug.checked" />
                                                <p>
                                                    {{ `작성일 : ${formatDate(sug.createdAt)}` }}<span class="dib mx2rem w2px h1-4rem bcE1E6ED es-dn"></span><span v-if="sug.updatedAt" class="es-db es-mt5px">{{ `수정일 : ${formatDate(sug.updatedAt)}` }}</span>
                                                </p>
                                            </div>
                                            <input true-value="Y" false-value="N" :checked="sug.useYn === 'Y'" v-model="sug.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="dataChanged(sug)" />
                                        </div>
                                        <div class="pa2rem">
                                            <span class="fs1-6rem fw500">근로자의견</span>
                                            <textarea name id class="minh20rem br4px mt1-2rem" v-model="sug.workerSuggestion" @change="dataChanged(sug)"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, alertMsg, confirmMsg, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnSave, btnDelete } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];

import { useHsePolicyStore } from '@/stores/safewiz/participation/hsePolicy.js';
const hseStore = useHsePolicyStore();
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const segments = ref([{ year: '2024.10.08' }, { year: '2024.10.07' }]);

const activeSegments = ref({});
const accordionRefs = ref([]);

onMounted(async () => {
    if (!hseStore.inputForm.dataType) {
        // 새로고침시 이전 화면으로 이동.
        router.push('/HsePolicy');
        return;
    }
    hseStore.btnHistSearch();
});

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
btnBack(() => {
    let isExistSelected = false;
    Object.keys(hseStore.mySugList).forEach(el => {
        if (hseStore.mySugList[el].filter(el => el.checked).length > 0) isExistSelected = true;
    });
    if (hseStore.sugInputForm.workerSuggestion != '' || isExistSelected) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});
const goBack = () => {
    router.go(-1);
};
btnSearch(() => {
    let isExistSelected = false;
    Object.keys(hseStore.mySugList).forEach(el => {
        if (hseStore.mySugList[el].filter(el => el.checked).length > 0) isExistSelected = true;
    });
    if (hseStore.sugInputForm.workerSuggestion != '' || isExistSelected) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: searchAction });
    } else searchAction();
});
const searchAction = () => {
    hseStore.btnHistSearch();
};
btnSave(() => {
    console.log('@@@@@@@@@@@@@@@@@@ 저장 ');
    if (hseStore.checkedSugs.length === 0 && !hseStore.sugInputForm.workerSuggestion) {
        alertMsg('warning', '저장할 근로자 의견 데이터가 없습니다.');
        return;
    }
    confirmMsg('info', t('msgSave'), null, { fun: hseStore.btnSugSave, param: true });
    parentChecked.value = [];
});
btnDelete(() => {
    if (hseStore.checkedSugs.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    console.log('@@@ hseStore.checkedSugs', hseStore.checkedSugs);
    if (hseStore.checkedSugs.filter(el => el.useYn === 'N').length > 0) {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), null, { fun: hseStore.btnSugDelete, param: true });
});
const parentChecked = ref([]);
// const checkBoxValueChanged = (data, e, parent = null, index) => {
//     data.forEach(el => {
//         el.checked = e.target.checked;
//     });
//     if (parent && e.target.checked === false) {
//         // parentChecked[index] === undefined ? parentChecked.value.push({cfalse) : (parentChecked[index].value = false);
//     }
//     console.log(parentChecked[index]);
//     // hseStore.btnHistSearch();
// };
const titleRefs = ref({});
const checkBoxValueChanged = (title, index) => {
    console.log('@@@@', hseStore.mySugList[title]);
    console.log('@@@@', titleRefs.value[0].checked);
    hseStore.mySugList[title].forEach(el => {
        el.checked = titleRefs.value[index].checked;
    });
};
const dataChanged = data => {
    data.checked = true;
    // data.useYn = 'Y';
    // hseStore.btnHistSearch();
};
</script>
