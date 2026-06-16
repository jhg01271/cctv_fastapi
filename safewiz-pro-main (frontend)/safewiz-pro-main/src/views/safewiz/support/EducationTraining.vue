<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-model="educationStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" @input="educationStore.btnSearch(true)" year />
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
            <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="educationStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, t, onMounted, formatDate, btnSearch } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();

//-----------------------------------------------
// [스토어]

import { useJobCompAssessStore } from '@/stores/safewiz/support/JobCompAssess.js';
const jobCompAssessStore = useJobCompAssessStore();

import { useAnnualTrainingPlanStore } from '@/stores/safewiz/support/annualTrainingPlan.js';
const annualTrainingPlanStore = useAnnualTrainingPlanStore();

import { useSAndHTrainingImplStore } from '@/stores/safewiz/support/SAndHTrainingImpl.js';
const sAndHTrainingImplStore = useSAndHTrainingImplStore();

import { useSAndHTrainingResultStore } from '@/stores/safewiz/support/SAndHTrainingResult.js';
const sAndHTrainingResultStore = useSAndHTrainingResultStore();

//-----------------------------------------------
onMounted(async () => {
    educationStore.btnSearch(true);
    console.log('년도', educationStore.searchParam.searchText);
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
    if ((route = '/JobCompetencyAssessment')) {
        jobCompAssessStore.writeYear = educationStore.searchParam.searchText;
    }

    if ((route = '/AnnualSAndHTrainingPlan')) {
        annualTrainingPlanStore.searchParam.writeYear = educationStore.searchParam.searchText;
    }

    if ((route = '/SAndHTrainingImplPlan')) {
        sAndHTrainingImplStore.writeYear = educationStore.searchParam.searchText;
    }

    if ((route = '/SAndHTrainingResultsPlan')) {
        sAndHTrainingResultStore.writeYear = educationStore.searchParam.searchText;
    }
};
btnSearch(() => {
    educationStore.btnSearch(true);
});
</script>
