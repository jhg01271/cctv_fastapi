<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <!-- 카드 타이틀 -->
        <!-- <h3>{{ t('compManage') }}</h3> -->
        <div class="form ui mb2-4rem">
            <!-- <div class="col12-9 df pl6px lg-col12-8 sm-col12-12 sm-pl0 sm-mt4px">
                <input
                    v-input
                    type="text"
                    class="radius mr6px w711px ul-w100p search"
                    placeholder="사업장명"
                    v-model="compStore.searchParam.searchWords"
                    @keyup.enter="compStore.searchCompGrid(true)"
                />
                <button
                    v-button
                    class="btn w60px radius davy-grey"
                    @click="compStore.searchCompGrid(true)"
                >
                    <span>{{t('search')}}</span>
                </button>
            </div>-->
            <div class="df bcffffff">
                <input v-input type="text" class="radius search" placeholder="사업장명" v-model="compStore.searchParam.searchWords" @keyup.enter="compStore.searchCompGrid(true)" />
                <button type="submit" class="shrink0" @click="compStore.searchCompGrid(true)">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
        </div>

        <!-- 토스트 그리드 -->
        <OverlayScrollbarsComponent
            class="us-maxh40rem"
            :options="{
                scrollbars: {
                    x: 'visible',
                    y: 'visible'
                }
            }"
        >
            <i-DataGrid class="minw1920px sm-minw530px" ref="gridComp" gridId="gridComp" :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { minWidth: 100 } }" :columns="compStore.gridColumns" @rowEdit="compStore.editCode('M', $event)" />
        </OverlayScrollbarsComponent>

        <i-DataGridPage ref="gridPageComp" :pageOptions="compStore.pageOptions" @beforeMove="compStore.beforeMovePageComp" />
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t, btnSearch, btnAdd, btnDelete } = BaseView();
import router from '@/router';

// 사업장 store
import { useCompStore } from '@/stores/system/setting/comp.js';
import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete'];

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

const compStore = useCompStore();
const gridComp = ref(null);
const gridPageComp = ref(null);

// 조회 버튼 이벤트 함수
btnSearch(() => {
    compStore.searchCompGrid(true);
});
btnAdd(() => {
    compStore.addComp();
});
btnDelete(() => {
    compStore.btnDelete();
});

onMounted(async () => {
    compStore.searchCompGrid(true);
    compStore.setRefs(gridComp, gridPageComp);
});
</script>
