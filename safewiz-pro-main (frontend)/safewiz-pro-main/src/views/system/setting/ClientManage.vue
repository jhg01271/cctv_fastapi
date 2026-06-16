<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 카드 타이틀 -->
        <div class="form ui mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius search" placeholder="고객사명, 대표자" v-model="clientStore.searchParam.searchWords" @keyup.enter="clientStore.searchClientGrid(true)" />
                <button type="submit" class="shrink0" @click="clientStore.searchClientGrid(true)">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
            <!-- 📩 2025.06.24 선택없음 & 알림발송 팝업  -->
            <!-- <div class="mt2rem df aic gap1rem">
                <button type="button" class="btn radius" @click="openErrorPopup">서버 에러 팝업</button>
            </div> -->
        </div>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    x: 'visible',
                    y: 'visible'
                }
            }"
        >
            <!-- 토스트 그리드 -->
            <i-DataGrid class="minw1920px sm-minw530px" ref="gridClient" gridId="gridClient" :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { minWidth: 100 }, editing: false }" :columns="clientStore.gridColumns" @rowEdit="clientStore.editCode('M', $event)" />
        </OverlayScrollbarsComponent>
        <i-DataGridPage ref="gridPageClient" :pageOptions="clientStore.pageOptions" @beforeMove="clientStore.beforeMovePageClient" />
    </div>

    <!-- 📩 2025.06.24 server error popup START -->
    <teleport to="body">
        <iAlertErrorPopup ref="alertErrorPopup" />
    </teleport>
    <!-- 📩 2025.06.24 server error popup END -->
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, btnSearch, btnAdd, btnDelete } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iAlertErrorPopup from '@/components/common/iAlertErrorPopup.vue';

import { useClientStore } from '@/stores/system/setting/client.js';
import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete'];

const clientStore = useClientStore();
const gridClient = ref(null);
const gridPageClient = ref(null);

const alertErrorPopup = ref(null);

// 조회 버튼 이벤트 함수
btnSearch(() => {
    clientStore.searchClientGrid(true);
});
btnAdd(() => {
    clientStore.addClient();
});
btnDelete(() => {
    clientStore.btnDelete();
});

onMounted(async () => {
    clientStore.searchClientGrid(true);
    clientStore.setRefs(gridClient, gridPageClient);
});

// 📩 2025.06.24 server error popup START
const openErrorPopup = () => {
    // 에러 팝업 열기 (기본 메시지 사용)
    alertErrorPopup.value.openErrorPopup();
};

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
</script>

<style lang="scss" scoped>
.accordion .list > button {
    padding: 1.3rem;
    em {
        font-size: 1.6rem;
    }
}
</style>
<!-- 📩 2025.06.24 server error popup END -->
