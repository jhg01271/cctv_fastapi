<template>
    <div id="form">
        <ul class="df fdc gap8px wbka">
            <template v-for="(item, index) in dataList" :key="index">
                <li class="df gap8px" v-show="item.visible !== false">
                    <input type="checkbox" v-input v-model="item.selected" @click="selectItem(item)" />
                    {{ item[props.filterKey] }}
                </li>
            </template>
        </ul>
    </div>
</template>

<script setup>
import { defineProps, onMounted } from 'vue';
const emit = defineEmits(['close', 'select']);
const props = defineProps({
    dataList: { type: Array, default: () => [] }, // 🔹 dataList props로 받음
    filterKey: { type: String, required: true } // 🔹 필터링 기준을 props에 추가
});
onMounted(() => {});

const selectItem = item => {
    emit('select', item);
};
</script>
<style scoped lang="scss"></style>
