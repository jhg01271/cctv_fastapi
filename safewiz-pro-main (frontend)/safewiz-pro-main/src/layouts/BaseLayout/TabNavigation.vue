<template>
    <div class="df aic pa1rem bdb1pxsolidE1E6ED oxa wsn">
        <div v-for="tab in menuStore.menuNaviList" :key="tab.path" class="df aic tab-item" :class="{ active: tab.path === menuStore.activeTabPath }" @click="moveToTab(tab)">
            <span>{{ tab.meta.menuNm }}</span>
            <i class="tab-close-btn" @click.stop="closeTab(tab)">×</i>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useMenuStore } from '@/stores/menu.js';

const router = useRouter();
const menuStore = useMenuStore();

const moveToTab = tab => {
    router.push(tab.path);
};

const closeTab = tab => {
    // menu.js 스토어의 removeTab 액션 호출
    menuStore.removeTab(tab, router);
};
</script>

<style scoped>
.tab-item {
    padding: 8px 12px;
    margin-right: 5px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background-color: #ffffff;
    cursor: pointer;
    transition: all 0.2s ease;
    font-size: 14px;
}

.tab-item:hover {
    background-color: #f0f2f5;
}

.tab-item.active {
    background-color: #409eff;
    color: #ffffff;
    border-color: #409eff;
}

.tab-close-btn {
    margin-left: 8px;
    font-style: normal;
    font-weight: bold;
    border-radius: 50%;
    padding: 0 4px;
    transition: all 0.2s ease;
}

.tab-item.active .tab-close-btn:hover {
    background-color: rgba(255, 255, 255, 0.3);
}

.tab-item:not(.active) .tab-close-btn:hover {
    background-color: #dcdfe6;
}
</style>
