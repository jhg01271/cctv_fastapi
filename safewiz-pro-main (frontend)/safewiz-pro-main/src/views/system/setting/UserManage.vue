<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div class="control-field ui form mb2-4rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" placeholder="아이디, 사용자명" v-model="userManageStore.searchParam.searchWords" @keyup.enter="userManageStore.searchUserGrid(true)" />
                <button type="submit" class="shrink0" @click="userManageStore.searchUserGrid(true)">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
        </div>
        <!-- 토스트 그리드 -->
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    x: 'visible',
                    y: 'visible'
                }
            }"
        >
            <i-DataGrid class="minw1800px" ref="gridUser" gridId="gridUser" :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { frozenCount: 3, frozenBorderWidth: 2, minWidth: 150 } }" :columns="userManageStore.gridColumns" @rowEdit="userManageStore.editCode('M', $event)" />
        </OverlayScrollbarsComponent>
        <i-DataGridPage ref="gridPageUser" :pageOptions="userManageStore.pageOptions" @beforeMove="userManageStore.beforeMovePageUser" />
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t, btnSearch, btnAdd, btnDelete } = BaseView();

import { useUserManageStore } from '@/stores/system/setting/userManage.js';
const userManageStore = useUserManageStore();

import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete'];

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

const gridUser = ref(null);
const gridPageUser = ref(null);

// 조회 버튼 이벤트 함수
btnSearch(() => {
    userManageStore.searchUserGrid(true);
});
btnAdd(() => {
    userManageStore.addUser();
});
btnDelete(() => {
    userManageStore.btnDelete();
});

onMounted(async () => {
    userManageStore.searchUserGrid(true);
    userManageStore.setRefs(gridUser, gridPageUser);
});

//-----------------------------------------------
</script>
