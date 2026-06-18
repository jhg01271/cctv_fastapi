<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 sm-grid12-3 es-grid12-12">
                    <input type="text" v-model="educationStore.searchParam.searchText" v-calendar="'yyyy'" class="datepicker w100p radius" year @input="educationStatusStore.btnSearch(true)" />
                </div>
                <div class="grid12-5 sm-grid12-5 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                        <button type="button" class="shrink0" @click="applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                        </button>
                    </div>
                </div>
                <div class="grid12-4 sm-grid12-4 es-grid12-12">
                    <div class="toggle">
                        <button type="button" v-for="div in educationStatusStore.filterDivList" :key="div.id" :class="{ active: educationStatusStore.currentFilter === div.id }" @click="toggleDiv(div.id)">
                            <span>{{ div.title }}</span>
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
            <div class="accordion df fdc rg8px">
                <div v-for="(segment, index) in educationStatusStore.groupedDataList" :key="index">
                    <div v-show="segment.visible" class="list">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: educationStatusStore.activeSegments[index] }">
                            <em>{{ segment.title }}</em>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div ref="accordionRefs" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa1rem">
                                <div class="row flex gutters2rem">
                                    <!-- TODO 퍼블리싱 필요 -->
                                    <template v-for="(data, idx) in segment.dataList" :key="`card_${idx}`">
                                        <div v-show="data.visible" class="w100p us-w100p form">
                                            <div class="pa1rem">
                                                <div class="pa1rem bd1pxsolide1e6ed br4px">
                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-2 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>이름</span>
                                                                </label>
                                                                <input class="br4px" type="text" v-input v-model="data.hrNm" readonly />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-2 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>조직</span>
                                                                </label>
                                                                <input class="br4px" type="text" v-input v-model="data.orgnNm" readonly />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-1 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>직위</span>
                                                                </label>
                                                                <input class="br4px" type="text" v-input v-model="data.jbrpNm" readonly />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-2 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>안전보건 조직 역할</span>
                                                                </label>
                                                                <input class="br4px" type="text" v-input v-model="data.orgnRoleNm" readonly />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-2 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>총 교육 시간</span>
                                                                    <button v-tooltip="'서명이 완료된 교육 결과 보고서의 교육 시간 합계 / 교육 결과 보고서의 교육 시간 합계'">
                                                                        <img class="vam ml5px neg-tty2px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                                    </button>
                                                                </label>
                                                                <input class="br4px" type="text" v-input :value="`${getTimeTransfer(data.totalTrainingTimeY)} / ${getTimeTransfer(data.totalTrainingTime)}`" readonly />
                                                                <!-- <div class="df aic gap4px">
                                                                    <input class="br4px" type="text" v-input :value="getTimeTransfer(data.totalTrainingTimeY)" readonly />
                                                                    <span>/</span>
                                                                    <input class="br4px" type="text" v-input :value="getTimeTransfer(data.totalTrainingTime)" readonly />
                                                                </div> -->
                                                            </div>
                                                        </div>
                                                        <div class="grid12-1 ul-grid12-3 es-grid12-6">
                                                            <div class="field">
                                                                <label>
                                                                    <span>교육 건수</span>
                                                                    <button v-tooltip="'서명이 완료된 교육 결과 보고서 수 / 교육 결과 보고서 수'">
                                                                        <img class="vam ml5px neg-tty2px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                                    </button>
                                                                </label>
                                                                <input class="br4px" type="text" v-input :value="`${data.trainingListY.length} / ${data.trainingListY.length + data.trainingListN.length}`" readonly />
                                                                <!-- <div class="df aic gap4px">
                                                                    <input class="br4px" type="text" v-input :value="data.trainingListY.length" readonly />
                                                                    <span>/</span>
                                                                    <input class="br4px" type="text" v-input :value="data.trainingListY.length + data.trainingListN.length" readonly />
                                                                </div> -->
                                                            </div>
                                                        </div>
                                                        <div class="grid12-2 ul-grid12-3 es-grid12-12">
                                                            <div class="field">
                                                                <label></label>
                                                                <button class="btn active radius w100p" @click="getTraining(data)">교육 내역 확인</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- <div v-show="data.visible" class="grid12-12 ul-grid12-12 lg-grid12-12" @click="devTest(data)">
                                            <div class="card us-w100p df fdc gap4px">
                                                <span>이름: {{ data.hrNm }}</span>
                                                <span>조직: {{ data.orgnNm }}</span>
                                                <span>직위: {{ data.jbrpNm }}</span>
                                                <span>안전보건 조직 역할: {{ data.orgnRoleNm }}</span>
                                                <span>총교육 시간: {{ getTimeTransfer(data.totalTrainingTimeY) }} / {{ getTimeTransfer(data.totalTrainingTime) }}</span>
                                                <span>교육건수: {{ data.trainingListY.length }} / {{ data.trainingListN.length + data.trainingListY.length }}</span>
                                            </div>
                                        </div> -->
                                    </template>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="trainingPopup">
                <div class="contents form ui w800px wbka md-w100p">
                    <h3>{{ `안전보건 교육 결과 보고서 내역 (${selectedHrNm})` }}</h3>
                    <div class="df bcffffff mb1rem">
                        <input v-input type="text" v-model="popupSearchTerm" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="popupApplyFilter" />
                        <button type="button" class="shrink0">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" @click="popupApplyFilter" />
                        </button>
                    </div>
                    <OverlayScrollbarsComponent
                        class="maxh35rem br4px"
                        :options="{
                            scrollbars: {
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div class="df fdc rg1rem">
                            <!-- 1 -->
                            <button v-for="(item, index) in trainingList" :key="index" type="button" class="df jcsb aic gap1rem bd1pxsolide1e6ed br4px px2rem py1-3rem cd es-fww">
                                <div class="df aic">
                                    <em class="fs1-6rem tal ellipsis" :title="item.trainingNm">{{ item.trainingNm }}</em>
                                    <span class="w1px h1-3rem mx1-2rem bce1e6ed us-dn"></span>
                                </div>
                                <div class="minw14rem df aic jcsb gap1rem fg1 es-mt1rem es-w100p">
                                    <span v-if="item.educationStatus === 'Y'" class="fs1-3rem c3248f6 bc50-72-246-10 px5px py3px br4px">서명 완료</span>
                                    <span v-else class="fs1-3rem cff3534 bc255-53-52-10 px5px py3px br4px">서명 미완료</span>
                                    <span class="shortcut fs1-3rem fw500 cp" @click="goTraining(item)">바로가기</span>
                                </div>
                            </button>
                        </div>
                    </OverlayScrollbarsComponent>

                    <i-PopupButtonList :btnInfo="{ closeBtn: true, saveBtn: false, deleteBtn: false, applyBtn: false }" @close="closeTrainingPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { nextTick, ref, t, onMounted, btnBack, btnSearch } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();
import router from '@/router';
import { useEducationStatusStore } from '@/stores/safewiz/support/educationStatus.js';
const educationStatusStore = useEducationStatusStore();
import _ from 'lodash';
layoutStore.useBtnList = ['btnBack', 'btnSearch'];

onMounted(async () => {
    const success = await educationStatusStore.btnSearch(true);
    if (success) initAccordion();
});

btnBack(() => {
    router.push('/EducationTraining');
});
btnSearch(() => {
    educationStatusStore.btnSearch(true);
});
const getTimeTransfer = minutes => {
    const hour = Math.floor(minutes / 60);
    const minute = minutes % 60;
    return `${hour}${t('hour')} ${minute}${t('minute')}`;
};

// -------------------------- 교육 목록 팝업 --------------------------
const selectedHrNm = ref('');
const trainingList = ref({});
const currentTrainingList = ref({});
const trainingPopup = ref(null);
const getTraining = data => {
    selectedHrNm.value = data.hrNm;
    trainingList.value = [...data.trainingListN, ...data.trainingListY];
    currentTrainingList.value = [...data.trainingListN, ...data.trainingListY];
    trainingPopup.value.onOpen();
};
const closeTrainingPopup = () => {
    trainingPopup.value.onClose();
};
const goTraining = param => {
    const toRouter = {
        name: 'SAndHTrainingResultsPlanDetail',
        state: param
    };
    router.push(toRouter);
};

// -------------------------- 아코디언 --------------------------
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const initAccordion = () => {
    console.log('educationStatusStore.activeSegments', educationStatusStore.activeSegments);
    educationStatusStore.activeSegments.forEach((el, index) => {
        if (el) {
            accordionSet(index, 0.5);
        }
    });
};
const toggleAccordion = async index => {
    educationStatusStore.activeSegments[index] = !educationStatusStore.activeSegments[index];

    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
};
const accordionRefs = ref([]);
const accordionSet = (index, duration) => {
    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: educationStatusStore.activeSegments[index] ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
const toggleDiv = id => {
    educationStatusStore.toggleDiv(id);
    searchTerm.value = '';
};
const searchTerm = ref('');
const applyFilter = () => {
    const search = searchTerm.value.toLowerCase();

    educationStatusStore.groupedDataList.forEach(el => {
        let hasVisibleData = false;
        const titleMatch = el.title?.toLowerCase().includes(search);

        el.dataList.forEach(data => {
            const hrNmMatch = data.hrNm?.toLowerCase().includes(search);
            const orgnNmMatch = data.orgnNm?.toLowerCase().includes(search);
            const jbrpNmMatch = data.jbrpNm?.toLowerCase().includes(search);
            const roleNmMatch = data.orgnRoleNm?.toLowerCase().includes(search);

            const isVisible = hrNmMatch || orgnNmMatch || jbrpNmMatch || roleNmMatch;
            data.visible = isVisible;

            if (isVisible) hasVisibleData = true;
        });

        el.visible = titleMatch || hasVisibleData;
    });
};

const popupSearchTerm = ref('');
const popupApplyFilter = () => {
    trainingList.value = _.cloneDeep(currentTrainingList.value);
    const search = popupSearchTerm.value.toLowerCase();
    if (search !== '') {
        trainingList.value = trainingList.value.filter(item => item.trainingNm.includes(search));
    }
};
</script>
