<template>
    <div class="contents df fdc">
        <div class="tab">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: preRiskAssessmentStore.activeTab === authMenuList[0].tabNm }" @click="preRiskAssessmentStore.activeTab = authMenuList[0].tabNm">
                <span>안전보건정보 조사</span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: preRiskAssessmentStore.activeTab === authMenuList[1].tabNm }" @click="preRiskAssessmentStore.activeTab = authMenuList[1].tabNm">
                <span>유해 위험 요인 근로자 의견</span>
            </button>
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
            <div class="oh">
                <div v-if="preRiskAssessmentStore.activeTab === authMenuList[0].tabNm && authMenuList[0].visible">
                    <SafetyAndHealthInfoSurvey :store="safetyHealthInfoStore" />
                </div>
                <div v-if="preRiskAssessmentStore.activeTab === authMenuList[1].tabNm && authMenuList[1].visible">
                    <WorkersOpinionsOnHazards :store="workerOpinionStore" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, getTabMenuAuth } = BaseView();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import SafetyAndHealthInfoSurvey from '@/views/safewiz/planning/SafetyAndHealthInfoSurvey.vue';
import { usePreRiskAssessmentStore } from '@/stores/safewiz/planning/PreRiskAssessment.js';
import { useSafetyHealthInfoStore } from '@/stores/safewiz/planning/safetyAndHealthInfoSurvey.js';
import WorkersOpinionsOnHazards from '@/views/safewiz/planning/WorkersOpinionsOnHazards.vue';
import { useWorkerOpinionStore } from '@/stores/safewiz/planning/workersOpinionsOnHazards.js';

const preRiskAssessmentStore = usePreRiskAssessmentStore();
const safetyHealthInfoStore = useSafetyHealthInfoStore();
const workerOpinionStore = useWorkerOpinionStore();

const authMenuList = ref([
    {
        menuId: 'SafetyAndHealthInfoSurvey',
        visible: false,
        tabNm: 'info'
    },
    {
        menuId: 'WorkersOpinionsOnHazards',
        visible: false,
        tabNm: 'opinion'
    }
]);

onMounted(async () => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (preRiskAssessmentStore.activeTab === '' && el.visible) preRiskAssessmentStore.activeTab = el.tabNm;
    });
});
</script>
