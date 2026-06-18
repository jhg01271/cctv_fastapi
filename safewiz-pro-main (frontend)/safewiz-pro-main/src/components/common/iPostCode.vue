<!-- 주소 검색 컴포넌트 -->
<template>
    <div>
        <VueDaumPostcode @complete="onPostcodeComplete" />
        <div class="form ui pr">
            <button type="button" class="btn w100p bright-grey" @click="closePostcodeDialog">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { defineProps, watch, watchEffect, defineExpose, computed } from 'vue';

const { t, ref, onMounted } = BaseView();
const props = defineProps({});
const emit = defineEmits(['complete', 'close']);

const onPostcodeComplete = data => {
    emit('complete', data);
};
const closePostcodeDialog = () => {
    emit('close');
};
defineExpose({});
</script>
