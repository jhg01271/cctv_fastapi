<template>
    <div id="wrap">
        <OverlayScrollbarsComponent :options="{
            scrollbars: {
                autoHide: 'hover'
            }
        }">
            <loadingPanel />
            <Header />
            <BaseNavigation />
            <!-- 검토용 네비게이션 -->
            <!-- <TabNavigation /> -->
            <div class="dashboard df fdc">
                <RouterView v-slot="{ Component, route }" :key="viewKey">
                    <component :is="Component" :key="route?.fullPath" />
                </RouterView>
            </div>
        </OverlayScrollbarsComponent>
        <iQuickMenuBtn />
    </div>

    <ToastPopup />
    <!-- <ActionPopup @confirm="handleConfirm" @cancel="handleCancel" /> -->
</template>

<script setup>
import router from '@/router';
import iQuickMenuBtn from '@/components/common/iQuickMenuBtn.vue';
import buttonList from '@/components/common/iButtonList.vue';
import loadingPanel from '@/components/common/loadingPanel.vue';
import Header from './Header.vue';
import BaseNavigation from './BaseNavigation.vue';
import TabNavigation from './TabNavigation.vue';

import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
onMounted(() => {
    console.log(router);
});
const { ref } = BaseView();

const viewKey = ref(0);

import ToastPopup from '@/sdm/components/popup/ToastPopup.vue';
import { onMounted } from 'vue';
</script>
