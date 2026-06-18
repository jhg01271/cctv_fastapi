<template>
    <div class="contents df fdc">
        <div class="tab w100p">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: activeTab === authMenuList[0].tabNm }" @click="activeTab = authMenuList[0].tabNm">
                <span>유해위험요인 분류 </span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: activeTab === authMenuList[1].tabNm }" @click="activeTab = authMenuList[1].tabNm">
                <span>위험성평가 계획</span>
            </button>
            <button v-if="authMenuList[2].visible" type="button" :class="{ active: activeTab === authMenuList[2].tabNm }" @click="changeTab">
                <span>위험성평가 이행</span>
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
            <!-- 탭별로 콘텐츠 표시 -->
            <div class="oh">
                <template v-if="activeTab === authMenuList[0].tabNm && authMenuList[0].visible"><ClassificationOfHazards :store="classificationOfHardsStore"></ClassificationOfHazards></template>
                <template v-if="activeTab === authMenuList[1].tabNm && authMenuList[1].visible"><RiskAssessmentPlan></RiskAssessmentPlan></template>
            </div>
            <template v-if="activeTab === authMenuList[2].tabNm && authMenuList[2].visible"><ImplementationOfRiskAssessment :goRiskPlan="goRiskPlan" :goHazards="goHazards"></ImplementationOfRiskAssessment></template>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ClassificationOfHazards from './ClassificationOfHazards.vue';
import ImplementationOfRiskAssessment from './ImplementationOfRiskAssessment.vue';
import RiskAssessmentPlan from './RiskAssessmentPlan.vue';
// BaseView
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, onBeforeMount, getTabMenuAuth } = BaseView();
// classificationOfHardsStore
import { useClassificationOfHardsStore } from '@/stores/safewiz/planning/classificationOfHazrds';
const classificationOfHardsStore = useClassificationOfHardsStore();
// riskImplStore
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
const riskImplStore = useRiskAssessmentImplementation();

const activeTab = ref(''); // 기본 탭을 'info'로 설정
const authMenuList = ref([
    {
        menuId: 'ClassificationOfHazards',
        visible: false,
        tabNm: 'info'
    },
    {
        menuId: 'RiskAssessmentPlan',
        visible: false,
        tabNm: 'plan'
    },
    {
        menuId: 'ImplementationOfRiskAssessment',
        visible: false,
        tabNm: 'implement'
    }
]);

onBeforeMount(() => {
    // 세션에 있는 tab 우선적으로 가져오기
    const tabFromSession = sessionStorage.getItem('riskAssessmentTab');
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
    });

    if (tabFromSession && authMenuList.value.find(el => el.tabNm === tabFromSession && el.visible)) {
        activeTab.value = tabFromSession;
    } else {
        const firstVisible = authMenuList.value.find(el => el.visible);
        if (firstVisible) activeTab.value = firstVisible.tabNm;
    }
});
onMounted(async () => {
    activeTab.value = sessionStorage.getItem('riskAssessmentTab') || 'info';
    if (activeTab.value === 'implement') {
        changeTab();
    }
});

const changeTab = async () => {
    // 이행 데이터 초기화
    riskImplStore.resetImplStore();
    sessionStorage.removeItem('riskAssessmentTab');

    activeTab.value = 'implement';
};

/*************************
 *위험성평가 이행 화면에서 계획 화면 넘어갈 때 사용
 *************************/
// 유해위험요인 분류 컴포넌트 호출
const goHazards = () => {
    // 이행 데이터 초기화
    riskImplStore.resetImplStore();
    activeTab.value = 'info';
};
// 위험성평가 계획 컴포넌트 호출
const goRiskPlan = () => {
    // 이행 데이터 초기화
    riskImplStore.resetImplStore();

    activeTab.value = 'plan';
};
</script>
