<template>
    <div class="contents df fdc">
        <div class="tab w100p">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: trainingStore.activeTab === 'scenario' }" @click="trainingStore.tabChanged('scenario')">
                <span>시나리오</span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: trainingStore.activeTab === 'result' }" @click="trainingStore.tabChanged('result')">
                <span>실시 보고서</span>
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
                <!-- 탭별로 콘텐츠 표시 -->
                <template v-if="trainingStore.activeTab === authMenuList[0].tabNm && authMenuList[0].visible">
                    <EmergencyResponseTrainingScenario></EmergencyResponseTrainingScenario>
                </template>
                <template v-if="trainingStore.activeTab === authMenuList[1].tabNm && authMenuList[1].visible">
                    <EmergencyResponseTrainingResult></EmergencyResponseTrainingResult>
                </template>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import EmergencyResponseTrainingScenario from './EmergencyResponseTrainingScenario.vue';
import EmergencyResponseTrainingResult from './EmergencyResponseTrainingResult.vue';
// 스토어
import { useTrainingStore } from '@/stores/safewiz/impl/training.js';
const trainingStore = useTrainingStore();

// 권한 유무에 따라 탭 가시화여부 설정
import BaseView from '@/components/base/BaseView';
const { ref, onBeforeMount, getTabMenuAuth } = BaseView();

const authMenuList = ref([
    {
        menuId: 'EmergencyResponseTrainingScenario',
        visible: false,
        tabNm: 'scenario'
    },
    {
        menuId: 'EmergencyResponseTrainingResult',
        visible: false,
        tabNm: 'result'
    }
]);
onBeforeMount(() => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (trainingStore.activeTab === '' && el.visible) trainingStore.activeTab = el.tabNm;
    });
});
</script>
