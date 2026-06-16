<template>
    <aside>
        <div class="side-panel">
            <h3>안전보건 조직 역할</h3>
            <div class="nodes">
                <div v-for="(item, index) in orgManageStore.safetyOrgList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'safetyGroup')">{{ item.orgnRoleNm }}</div>
            </div>
            <span v-if="orgManageStore.safetyOrgList.length === 0" class="ml1rem fs1-5rem fw300"><br />역할을 등록하세요.</span>
            <h3>인원</h3>
            <div class="control-field ui form mb12px">
                <div class="df bcffffff">
                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="chartSearchTerm" @keyup.enter="applyChartFilter" />
                    <button type="button" class="shrink0" @click="applyChartFilter">
                        <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                    </button>
                </div>
            </div>
            <div class="nodes">
                <div v-for="(item, index) in filteredUserList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'organization')">{{ item.hrNm }}</div>
            </div>
        </div>
    </aside>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { onMounted, t, ref, getCompId, nextTick, watch } = BaseView();
import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();
const onDragStart = (event, data, type) => {
    if (event.dataTransfer) {
        event.dataTransfer.setData(`application/${type}`, JSON.stringify(data));
        event.dataTransfer.effectAllowed = 'move';
    }
};
watch(
    () => orgManageStore.userList,
    newVal => {
        filteredUserList.value = orgManageStore.userList.filter(el => el.useYn === 'Y' && el.partCompId === '');
    }
);
onMounted(async () => {
    await orgManageStore.btnSearch();
    chartSearchTerm.value = '';
});
const filteredUserList = ref([]);

// --- 인원 검색 기능
const chartSearchTerm = ref('');
const applyChartFilter = async () => {
    const filteredData = orgManageStore.userList.filter(el => el.hrNm.toLowerCase().includes(chartSearchTerm.value.toLowerCase()));
    filteredUserList.value = await filteredData;
};
</script>
