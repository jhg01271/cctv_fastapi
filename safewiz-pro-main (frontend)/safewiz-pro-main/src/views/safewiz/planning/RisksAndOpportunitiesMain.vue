<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-model="risksAndOppStore.searchParamAllMain.searchText" type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" @input="risksAndOppStore.btnMainSearch(true)" />
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
                    <i-card-menu :menuList="risksAndOppStore.menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { t, btnSearch, onMounted } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
// 협력사 store
import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
const risksAndOppStore = useRisksAndOppStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList.push('btnSearch');

onMounted(() => {
    risksAndOppStore.btnMainSearch(true);
});

// 조회 버튼 이벤트 함수
btnSearch(() => {
    risksAndOppStore.btnMainSearch(true);
});

const btnDetail = async route => {
    router.push(route);
};
</script>
