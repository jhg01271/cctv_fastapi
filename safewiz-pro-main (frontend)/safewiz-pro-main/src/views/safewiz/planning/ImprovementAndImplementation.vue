<template>
    <div class="contents df fdc">
        <div class="control-field form ui mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-6">
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="thisStore.searchParam.writeYear" class="datepicker radius es-w100p" @input="searchData" />
                </div>
                <div class="grid12-9 es-grid12-6">
                    <div class="df bcffffff">
                        <input v-model="thisStore.searchParam.searchText" v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" @keyup.enter="thisStore.dataFilter" />
                        <button type="submit" class="shrink0" @click.stop="thisStore.dataFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
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
            <div class="accordion df fdc gap8px">
                <div v-for="(segment, index) in thisStore.seg" :key="index" class="list">
                    <button type="button" :id="`accordion-btn_${index}`" class="df jcsb aic form ui" @click="thisStore.toggleAccordion">
                        <div class="init df aic gap2rem">
                            <input type="checkbox" v-input @change="thisStore.accordionChecked(segment, $event)" :checked="segment.planDetail?.filter(el => el.checked).length === segment.planDetail?.length && segment.planDetail?.length !== 0" />
                            <em>{{ segment.planNm }}</em>
                        </div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>

                    <div ref="accordionRefs" class="segment oh">
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.planDetail" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :modelValue="data.checked" :data="data" :type="'basic'" :title="data.hazardsFactor" @handle="thisStore.chkEvent(segment, $event)" @editor="thisStore.goDetail(segment, data)" :useUseYN="false"></i-card>
                                </template>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>
<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import { useImprovementAndImplementationStore } from '@/stores/safewiz/planning/ImprovementAndImplementation.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';

const { ref, onMounted, t, btnBack, btnSearch, btnDownload, getCurrentDate } = BaseView();
const thisStore = useImprovementAndImplementationStore();
const riskAStore = useRiskAStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload'];

const searchData = () => {
    riskAStore.searchParam.searchText = thisStore.searchParam.writeYear;
    thisStore.searchData(true);
};

btnBack(() => {
    thisStore.goBack();
});

btnSearch(() => {
    thisStore.searchData(true);
});

btnDownload(() => {
    thisStore.print();
});

onMounted(async () => {
    // 최초 진입 시 바로 조회
    if (riskAStore.searchParam.searchText) {
        thisStore.searchParam.writeYear = riskAStore.searchParam.searchText;
    } else {
        thisStore.searchParam.writeYear = await getCurrentDate().substring(0, 4);
    }
    thisStore.searchData(true);
});
</script>
<style lang="scss" scoped>
/*.list {
    text-align: center;
}*/
.year {
    width: 95%;
}
.expansion-panel {
    border: 1px solid #ddd;
    margin: 10px;
    border-radius: 4px;
    overflow: hidden;
}

.panel-header {
    background-color: #43d491;
    border: none;
    padding: 10px 15px;
    text-align: center;
    width: 100%;
    cursor: pointer;
    outline: none;
    font-size: 16px;
}

.panel-content {
    padding: 15px;
    display: none;
    background-color: white;
}

.panel-content.open {
    display: block;
}
</style>
