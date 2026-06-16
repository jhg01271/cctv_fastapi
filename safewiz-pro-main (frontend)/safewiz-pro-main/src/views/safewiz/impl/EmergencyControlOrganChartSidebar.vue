<template>
    <aside>
        <div class="side-panel">
            <h3 class='tal'>{{ chartStore.inputForm.emergencyTypeNm }}<br/>비상통제 역할</h3>
            <div class="nodes" v-if="chartStore.roleList.length === 0">
                <div :draggable="false">{{ '없음' }}</div>
            </div>
            <div class="nodes" v-else>
                <div v-for="(item, index) in chartStore.roleList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'emergencyRole')">{{ item.roleNm }}</div>
            </div>

            <h3>조직 목록</h3>
            <div class="nodes">
                <div v-for="(item, index) in orgManageStore.organizationList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'organization')">{{ item.orgnNm }}</div>
            </div>
        </div>
    </aside>
</template>

<script setup>
const roleList = ref([]);
onMounted(async () => {
    orgManageStore.btnSearch(false);
});
import { onMounted, ref } from 'vue';

import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();

import { useEmerCtrlOrgChartStore } from '@/stores/safewiz/impl/emerCtrlOrgChart.js';
const chartStore = useEmerCtrlOrgChartStore();

const onDragStart = (event, data, type) => {
    if (event.dataTransfer) {
        event.dataTransfer.setData(`application/${type}`, JSON.stringify(data));
        event.dataTransfer.effectAllowed = 'move';
    }
};
</script>
