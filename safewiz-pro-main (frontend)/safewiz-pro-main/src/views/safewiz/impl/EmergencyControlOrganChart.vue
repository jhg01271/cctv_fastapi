<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="chartStore.searchTerm" @keyup.enter="chartStore.applyFilter" />
                <button type="submit" class="shrink0" @click="chartStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
        </div>

        <div class="row flex gutters2rem">
            <div class="grid12-7 h100p ul-grid12-12 ul-h50p">
                <OverlayScrollbarsComponent
                    class="h100p"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <div class="oh df fdc minh100p">
                        <div class="df fdc rg2-2rem fg1">
                            <div class="title-box fg1">
                                <p>확정차수</p>
                                <div class="row flex gutters2rem">
                                    <div class="grid12-6 es-grid12-12" v-for="(item, index) in confirmedDegreeList" :key="index">
                                        <i-card-order :v-model="item.checked" :confirm="true" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, true)" />
                                    </div>
                                </div>
                            </div>
                            <div class="title-box fg1">
                                <p>차수 리스트</p>
                                <div class="row flex gutters2rem">
                                    <div class="grid12-6 es-grid12-12" v-for="(item, index) in unConfirmedDegreeList" :key="index">
                                        <i-card-order :v-model="item.checked" :confirm="false" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" :confirmable="item.confirmable" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, false)" />
                                    </div>

                                    <!-- 추가 -->
                                    <div class="grid12-6 es-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="addAction">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_addEmergencyControlOrganChart') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>

            <div class="grid12-5 ul-grid12-12 ul-h50p">
                <div class="box h100p">
                    <VueFlow v-model="elements" class="customnodeflow" :default-zoom="vfZoom" :nodesDraggable="false" fit-view-on-init>
                        <MiniMap pannable zoomable />
                        <!-- <Background variant="lines" gap="50" size="1.2" /> -->
                        <template #node-resizable="resizableNodeProps">
                            <NodeResizer v-if="false" min-width="100" min-height="30" />
                            <div>{{ resizableNodeProps.label }}</div>
                        </template>
                        <template #node-default="defaultNodeProps">
                            <div class="node">
                                <Handle v-if="false" type="source" :position="Position.Bottom" />
                                {{ defaultNodeProps.label }}
                                <Handle v-if="false" type="target" :position="Position.Top" />
                            </div>
                        </template>
                    </VueFlow>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { formatDate, onMounted, t, router, ref, btnBack, btnAdd, getCompId, btnDelete, btnSearch, confirmMsg, alertMsg } = BaseView();
import _ from 'lodash';
import { Background, MiniMap } from '@vue-flow/additional-components';
import { VueFlow, useVueFlow } from '@vue-flow/core';
const { fitView } = useVueFlow();
import { nextTick } from 'vue';
const vfZoom = ref(50);
import { useEmerCtrlOrgChartStore } from '@/stores/safewiz/impl/emerCtrlOrgChart.js';
const chartStore = useEmerCtrlOrgChartStore();

const confirmedDegreeList = computed(() => {
    // 확정 차수 항목
    return chartStore.filteredDegreeList.filter(el => el.confirmYn == 'Y');
});

const unConfirmedDegreeList = computed(() => {
    // 확정이 아닌 차수 항목
    const returnData = chartStore.filteredDegreeList.filter(el => el.confirmYn == 'N');
    returnData.forEach(el => {
        el.confirmable = !chartStore.confirmTypeList.includes(el.emergencyType);
    });
    return returnData;
});

btnBack(() => {
    router.push('/EmergencyResponse');
});

btnSearch(() => {
    chartStore.btnSearch(true);
});
btnAdd(() => {
    addAction();
});
btnDelete(() => {
    let checkedList = unConfirmedDegreeList.value.filter(el => el.checked);
    if (checkedList.length == 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: checkedList });
});

const addAction = () => {
    // 신규 조직도 추가
    chartStore.initInputForm();
    router.push('/EmergencyControlOrganChartDetail');
};

const deleteAction = checkedList => {
    chartStore.btnDelete(checkedList);
};

const confirm = (item, confirmYn) => {
    const param = { chartId: item.chartId, confirmYn: confirmYn, writeYear: item.writeYear };
    confirmMsg('info', confirmYn == 'Y' ? `[${item.chartNm}] \n 확정 하시겠습니까?` : `[${item.chartNm}] \n 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: param });
};

const confirmAction = param => {
    chartStore.btnConfirm(param, true);
};

const showDetail = (chartId, confirmed) => {
    const toRouter = {
        name: 'EmergencyControlOrganChartDetail',
        state: { chartId: chartId, confirmed: confirmed }
    };
    router.push(toRouter);
    // chartStore.btnDetail(chartId, confirmed);
};
const elements = ref([]);
const searchParam = ref({
    compId: getCompId()
});
const degreeList = ref([]);

const clickDegree = async item => {
    elements.value = JSON.parse(item.chartData);
    await nextTick();
    await fitView({
        padding: 0.2, // 노드와 뷰포트 간 여백 비율
        includeHiddenNodes: true, // 숨겨진 노드 포함 여부
        duration: 100 // 뷰포트 이동 애니메이션 시간
    }); // 조직도 뷰포트 맞추기
};
import { getEmergencyOrgChartList } from '@/stores/safewiz/impl/api/emerCtrlOrgChartApi.js';

import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();

onMounted(async () => {
    chartStore.confirmTypeList = [];
    chartStore.searchParam.writeYear = emergencyResponseStore.searchParam.searchText;
    getEmergencyOrgChartList(chartStore.searchParam, true)
        .then(res => {
            res.list.forEach(el => {
                el.detail = {
                    등록일자: formatDate(el.createdAt),
                    확정일자: el.confirmAt ? formatDate(el.confirmAt) : '-',
                    비상사태유형: el.emergencyTypeNm,
                    '조직 수': el.orgCount
                };
                if (el.confirmYn == 'Y') {
                    if (!chartStore.confirmTypeList.includes(el.emergencyType)) {
                        chartStore.confirmTypeList.push(el.emergencyType);
                    }
                }
            });
            chartStore.degreeList = _.cloneDeep(res.list);
            chartStore.filteredDegreeList = _.cloneDeep(res.list);
        })
        .finally(() => {
            if (confirmedDegreeList.value.length > 0) {
                elements.value = JSON.parse(confirmedDegreeList.value[0].chartData);
            } else if (unConfirmedDegreeList.value.length > 0) {
                elements.value = JSON.parse(unConfirmedDegreeList.value[0].chartData);
            }
        });
});
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { computed } from 'vue';
import { write } from 'xlsx';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSearch', 'btnDelete'];
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
</script>
