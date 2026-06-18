<template>
    <!-- <Header></Header> -->
    <RouterView />
</template>

<script setup>
import Header from './Header.vue';
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, router } = BaseView();

onMounted(() => {
    console.log(2);
});
</script>

<style lang="scss" scoped></style>
