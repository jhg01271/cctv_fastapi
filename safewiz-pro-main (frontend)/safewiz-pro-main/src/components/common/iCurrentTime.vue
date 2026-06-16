<template>
    <!-- 현재시간 컴포넌트 -->
    <span class="mr2rem lg-dn minw20rem">{{ `${todayDate} ${today} ${todayTime}` }}</span>
</template>

<script setup>
import { defineProps } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, onMounted, onUnmounted, ref, getCurrentDate } = BaseView();

defineProps({});
const clockTimerInterval = ref(null);
onMounted(() => {
    clockTimerInterval.value = setInterval(() => {
        getCurrentDateTime();
    }, 1000);
});
onUnmounted(() => {
    clearInterval(clockTimerInterval.value);
});

// 시간
const today = ref('');
const todayDate = ref('');
const todayTime = ref('');

const getDay = date => {
    const week = ['SUN', 'MON', 'TUE', 'WEB', 'THU', 'FRI', 'SAT'];
    //const week = ['일', '월', '화', '수', '목', '금', '토']
    return t(week[new Date(date).getDay()]);
};
const getCurrentDateTime = () => {
    let js_date = new Date();
    var hh = js_date.getHours();
    var mm = js_date.getMinutes();
    var ss = js_date.getSeconds();

    // 최종 포맷 (ex - '2021.10.08')
    todayDate.value = getCurrentDate();
    today.value = getDay(js_date);
    todayTime.value = ('00' + hh).slice(-2) + ':' + ('00' + mm).slice(-2) + ':' + ('00' + ss).slice(-2);
};
</script>

<style></style>
