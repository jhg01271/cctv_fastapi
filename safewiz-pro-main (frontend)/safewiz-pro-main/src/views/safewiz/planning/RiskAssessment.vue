<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="riskAStore.searchParam.searchText" class="datepicker w20rem radius es-w100p" @input="riskAStore.btnSearch(true)" />
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
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="riskAStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
import router from '@/router/index.js';

const { ref, t, onMounted, btnSearch } = BaseView();
const riskAStore = useRiskAStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];

onMounted(async () => {
    riskAStore.btnSearch(true);
});

const onChange = () => {
    console.log('onChange');
};
const btnDetail = async route => {
    //디테일 조회
    // hrStore.hrId = e.hrId;
    // await hrStore.getHrDetailList(hrStore.hrId, false);
    if (route == '/RiskAssessmentTable') {
        sessionStorage.removeItem('riskAssessmentTab');
    }
    // 페이지 이동
    router.push(route);
};

btnSearch(() => {
    riskAStore.btnSearch(true);
});
</script>
