<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            class="pr0-5rem"
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
                    <i-card-menu :menuList="realTimeVideoSafetyObjectivesStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, t, onMounted, btnSearch } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';

import { userealTimeVideoSafetyObjectivesStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/realTimeVideoSafetyObjectives.js';
const realTimeVideoSafetyObjectivesStore = userealTimeVideoSafetyObjectivesStore();

//-----------------------------------------------
// [스토어]
import { useDetailedActionStore } from '@/stores/safewiz/participation/detailedActionPlan.js';
const detailedActionStore = useDetailedActionStore();
import { useActionPlanStore } from '@/stores/safewiz/participation/actionPlan.js';
const actionPlanStore = useActionPlanStore();

//-----------------------------------------------
onMounted(async () => {
    realTimeVideoSafetyObjectivesStore.btnSearch(true);
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
    console.log('년도', route);
    if ((route === '/ObjectivesAndActionPlan')) {
        //아코디언 초기화
        actionPlanStore.accordionIdx = null;
        actionPlanStore.writeYear = realTimeVideoSafetyObjectivesStore.searchParam.searchText;
    }
    if ((route === '/DetailedActionPlan')) {
        console.log('년도', detailedActionStore.writeYear);
        detailedActionStore.writeYear = realTimeVideoSafetyObjectivesStore.searchParam.searchText;
    }
};
btnSearch(() => {
    realTimeVideoSafetyObjectivesStore.btnSearch(true);
});
</script>
