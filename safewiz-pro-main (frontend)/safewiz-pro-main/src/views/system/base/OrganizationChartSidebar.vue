<template>
    <aside>
        <div class="side-panel">
            <h3>조직 목록</h3>
            <div class="nodes">
                <div class="ellipsis" v-for="(item, index) in orgManageStore.organizationList" :key="index" :draggable="true" @dragstart="onDragStart($event, item)">{{ item.orgnNm }}</div>
            </div>
        </div>
    </aside>
</template>

<script setup>
import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();
const onDragStart = (event, data) => {
    if (event.dataTransfer) {
        event.dataTransfer.setData('application/vueflow', JSON.stringify(data));
        event.dataTransfer.effectAllowed = 'move';
    }
};
</script>
