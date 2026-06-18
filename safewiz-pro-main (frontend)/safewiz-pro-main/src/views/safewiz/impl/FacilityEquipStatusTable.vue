<template>
    <div class="contents df fdc">
        <div class="tab">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: activeTab === authMenuList[0].tabNm }" @click="activeTab = authMenuList[0].tabNm">
                <span>시설/설비 관리 </span>
            </button>
        </div>

        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- 탭별로 콘텐츠 표시 -->
            <!-- <template v-if="activeTab === 'scenario'">
                <EmergencyResponseTrainingScenario></EmergencyResponseTrainingScenario>
            </template> -->
            <div v-if="activeTab === authMenuList[0].tabNm && authMenuList[0].visible">
                <FacilityEquipManage></FacilityEquipManage>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import FacilityEquipManage from './FacilityEquipManage.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { ref, onBeforeMount, getTabMenuAuth } = BaseView();

const activeTab = ref('');
const authMenuList = ref([
    {
        menuId: 'FacilityEquipManage',
        visible: false,
        tabNm: 'manage'
    }
]);
onBeforeMount(() => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (activeTab.value === '' && el.visible) activeTab.value = el.tabNm;
    });
});
</script>
