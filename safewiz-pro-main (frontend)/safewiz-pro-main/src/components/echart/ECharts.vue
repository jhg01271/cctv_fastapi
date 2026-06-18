<template>
    <v-chart ref="chartRef" :option="props.options" :theme="props.theme" :autoresize="props.autoresize" />
</template>

<script setup>
/**
 * ============================================================================================================================================================
 * Vue EChart 컴포넌트
 * ============================================================================================================================================================
 */
import VChart, { THEME_KEY } from 'vue-echarts';
import { provide, ref, onMounted, onUnmounted } from 'vue';

provide({ [THEME_KEY]: 'dark' });

const props = defineProps({
    options: Object,
    theme: [String, Object],
    initOptions: Object,
    group: String,
    autoresize: { type: Boolean, default: true },
    watchShallow: Boolean,
    manualUpdate: Boolean
});

/**
 * resize
 */
// v-chart 컴포넌트를 참조
const chartRef = ref(null);

onMounted(() => {
    const chartInstance = chartRef.value; // ECharts 인스턴스 가져오기

    // Resize 이벤트 핸들러
    const handleResize = () => {
        chartInstance.resize();
    };

    window.addEventListener('resize', handleResize);

    // 컴포넌트 언마운트 시 이벤트 제거
    onUnmounted(() => {
        window.removeEventListener('resize', handleResize);
    });
});
</script>
