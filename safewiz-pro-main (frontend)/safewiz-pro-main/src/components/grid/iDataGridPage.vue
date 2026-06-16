<template>
    <div id="placeholder-id" class="tui-pagination"></div>
</template>

<script setup>
/**
 * ============================================================================================================================================================
 * 작성자 : ESG 사업부 임현섭
 * 작성일 : 2024-05-27
 * ============================================================================================================================================================
 */

import { ref, onMounted, defineProps, defineEmits, defineExpose } from 'vue';
import Pagination from 'tui-pagination';
const props = defineProps({
    pageOptions: Object,
    id: String
});
const emit = defineEmits(['beforeMove']);
const pagination = ref();
defineExpose({
    pagination
});

onMounted(async () => {
    // 요소 선택 및 id 변경
    const element = document.getElementById('placeholder-id');
    if (element) {
        element.id = props.pageOptions.id;
    }

    pagination.value = new Pagination(props.pageOptions.id, props.pageOptions);
    //   pagination.value.on('beforeMove', async ev => {
    //     props.pageOptions.page = ev.page
    //     props.searchParam.curPage = ev.page
    //   })

    pagination.value.on('beforeMove', async ev => {
        emit('beforeMove', ev);
    });
});
</script>
