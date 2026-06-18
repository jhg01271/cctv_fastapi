<template>
    <h1>home</h1>
    <div>{{ $route.path }}</div>
    <button @click="goAboutPage">About으로 이동ffffffffffffff</button>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted } = BaseView();
// import { useRouter } from 'vue-router';
import router from '@/router';
import { layoutInit } from '@/api/base/common/auth';

const goSingleSitePage = () => {
    router.push('/page/admin/fems/master/sensor');
};

const goAdminPage = () => {
    router.replace('/page/admin/fems/master/EquipmentMgt');
};

const goMultiSitePage = () => {
    router.push('/page/comp/company/compSelect');
};

onMounted(async () => {
    try {
        const result = await layoutInit();
        console.log(result);

        if (result.result.role == 'ADMIN') {
            goAdminPage();
        } else {
            if (result.totalCount > 1) {
                goMultiSitePage();
            } else {
                goSingleSitePage();
            }
        }

        console.log('부모 검색후');
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>

<style lang="scss" scoped></style>
