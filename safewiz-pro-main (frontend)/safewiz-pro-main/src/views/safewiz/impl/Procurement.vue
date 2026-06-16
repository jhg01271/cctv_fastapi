<template>
    <div class="contents df fdc">
        <div class="tab w100p">
            <button v-if="authMenuList[0].visible" type="button" :class="{ active: procurementStore.activeTab === authMenuList[0].tabNm }" @click="procurementStore.tabChanged(authMenuList[0].tabNm)">
                <span>협력업체 등록대장</span>
            </button>
            <button v-if="authMenuList[1].visible" type="button" :class="{ active: procurementStore.activeTab === authMenuList[1].tabNm }" @click="procurementStore.tabChanged(authMenuList[1].tabNm)">
                <span>계약 대상 업체 조사표</span>
            </button>
            <button v-if="authMenuList[2].visible" type="button" :class="{ active: procurementStore.activeTab === authMenuList[2].tabNm }" @click="procurementStore.tabChanged(authMenuList[2].tabNm)">
                <span>협력업체 안전보건 평가 보고서</span>
            </button>
        </div>

        <div class="oh h100p df fdc">
            <!-- h100p 추가 -->
            <!-- 탭별로 콘텐츠 표시 -->

            <template v-if="procurementStore.activeTab === authMenuList[0].tabNm && authMenuList[0].visible">
                <ContractorRegister />
            </template>
            <template v-if="procurementStore.activeTab === authMenuList[1].tabNm && authMenuList[1].visible">
                <ContractorInvestigationForm />
            </template>
            <template v-if="procurementStore.activeTab === authMenuList[2].tabNm && authMenuList[2].visible">
                <ContractorSAndHAssmtReport />
            </template>
        </div>
    </div>
</template>

<script setup>
// 스토어
import { useProcurementStore } from '@/stores/safewiz/impl/procurement.js';
const procurementStore = useProcurementStore();

import ContractorRegister from '@/views/safewiz/impl/ContractorRegister.vue';
import ContractorInvestigationForm from '@/views/safewiz/impl/ContractorInvestigationForm.vue';
import ContractorSAndHAssmtReport from '@/views/safewiz/impl/ContractorSAndHAssmtReport.vue';

import BaseView from '@/components/base/BaseView';
const { ref, onBeforeMount, onMounted, getTabMenuAuth } = BaseView();

const authMenuList = ref([
    {
        menuId: 'ContractorRegisterDetail',
        visible: false,
        tabNm: 'contractorRegister'
    },
    {
        menuId: 'ContractorInvestigationFormDetail',
        visible: false,
        tabNm: 'contractorInvestigationForm'
    },
    {
        menuId: 'ContractorSAndHAssmtReportDetail',
        visible: false,
        tabNm: 'contractorSAndHAssmt'
    }
]);

onBeforeMount(() => {
    authMenuList.value.forEach(el => {
        el.visible = getTabMenuAuth(el.menuId);
        if (procurementStore.activeTab === '' && el.visible) procurementStore.activeTab = el.tabNm;
    });
});

onMounted(() => {
    //조달 화면 열릴 때 협력업체 등록대장 탭으로 초기화
    if (procurementStore.activeTab === '') procurementStore.tabChanged('contractorRegister');
});
</script>
