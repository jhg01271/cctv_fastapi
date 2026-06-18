<template>
    <!-- 로딩 패널 -->
    <!-- API 조회 시 사용될 로딩 패널입니다. -->
    <teleport to="body">
        <div v-if="loadingPanelStore.isLoading" class="loading-overlay">
            <div class="spinner"></div>
        </div>
    </teleport>
</template>

<script setup>
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
import router from '@/router';
</script>

<style>
/* 회색 배경 */
.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.2); /* 투명한 회색 */
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999; /* 최상위 표시 */
}

/* 로딩 스피너 */
.spinner {
    border: 8px solid #f3f3f3;
    border-top: 8px solid #16223b;
    border-radius: 50%;
    width: 60px;
    height: 60px;
    animation: spin 1s linear infinite;
    z-index: 99999;
}

/* 스피너 회전 애니메이션 */
@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}
</style>
