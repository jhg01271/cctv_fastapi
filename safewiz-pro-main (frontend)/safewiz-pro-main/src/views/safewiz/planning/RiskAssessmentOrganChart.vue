<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic bcffffff">
                <input v-input v-model="chartParam" type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="init" />
                <button type="submit" class="shrink0" @click="init">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
        </div>

        <div class="row flex gutters2rem">
            <div class="grid12-7 h100p ul-h50p ul-grid12-12">
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
                    <div class="oh df fdc minh100p ul-ha">
                        <div class="df fdc rg2-2rem h100p fg1">
                            <div class="title-box fg1">
                                <p>확정차수</p>
                                <div class="row flex gutters2rem" v-if="confirmedDegree.length > 0">
                                    <div class="grid12-6 es-grid12-12" v-for="(item, index) in confirmedDegree" :key="index">
                                        <i-card-order :v-model="item.checked" :confirm="true" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, true)" />
                                    </div>
                                </div>
                                <!-- 확정 치수가 없는 경우 노출 -->
                                <span class="db fs2rem c999999" v-else>확정 차수를 등록하세요</span>
                            </div>
                            <div class="title-box fg1">
                                <p>차수 리스트</p>
                                <div class="row flex gutters2rem">
                                    <div class="grid12-6 es-grid12-12" v-for="(item, index) in filteredList" :key="index">
                                        <i-card-order :v-model="item.checked" :confirm="false" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, false)" />
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
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_RiskAssessmentOrganChart') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
            <div class="grid12-5 h100p ul-h50p ul-grid12-12">
                <div class="box h100p">
                    <VueFlow v-model="elements" class="customnodeflow" :style="vfStyle" :default-zoom="vfZoom" :nodesDraggable="false" fit-view-on-init>
                        <MiniMap pannable zoomable />
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
import { getRiskAssessmentOrganChartList, confirmRiskAssessmentOrganChart, deleteRiskAssessmentOrganChart, getRiskAssessmentOrganChartDetail } from '@/stores/safewiz/planning/api/riskAssessmentOrganChartApi';
import { Background, MiniMap } from '@vue-flow/additional-components';
import _ from 'lodash';
import { VueFlow, useVueFlow } from '@vue-flow/core';
const { fitView } = useVueFlow();
import { nextTick } from 'vue';
import { useRiskAssessmentOrganStore } from '@/stores/safewiz/planning/riskAssessmentOrganChart';

const { onMounted, router, ref, t, btnAdd, getCompId, btnDelete, btnSearch, confirmMsg, alertMsg, formatDate, openLoading, endLoading } = BaseView();
const chartStore = useRiskAssessmentOrganStore();
const elements = ref([]);
const searchParam = ref({
    compId: getCompId()
});
const chartParam = ref('');
const degreeList = ref([]);
const vfStyle = ref({ backgroundColor: '#ffffff', height: '100%' });
const vfZoom = ref(50);
let filteredList = ref([]); // 초기 필터링된 차수 리스트

const init = () => {
    elements.value = [];
    openLoading();
    getRiskAssessmentOrganChartList(searchParam.value, true)
        .then(res => {
            res.list.forEach(el => {
                el.detail = {
                    등록일자: formatDate(el.createdAt),
                    확정일자: el.confirmDt ? formatDate(el.confirmDt) : '-',
                    조직: el.orgnNm,
                    '인원 수': el.memberCount
                };
            });
            degreeList.value = applyFilter(res.list);
            filteredList.value = unConfirmedDegreeList.value;
            if (confirmedDegree.value.length) {
                elements.value = JSON.parse(confirmedDegree.value[0].chartData);
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

onMounted(() => {
    init();
});

// 확정차수
const confirmedDegree = computed(() => {
    return degreeList.value.filter(el => el.confirmYn === 'Y');
});

// 차수 리스트
const unConfirmedDegreeList = computed(() => {
    return degreeList.value.filter(el => el.confirmYn === 'N');
});

// 위험성평가 차수 리스트 검색 이벤트
const applyFilter = data => {
    return data.filter(el => {
        return (
            el.chartNm.trim().includes(chartParam.value.trim()) ||
            el.chartId.trim().includes(chartParam.value.trim()) ||
            formatDate(el.createdAt.substring(0, 10)).trim().includes(chartParam.value.trim()) ||
            formatDate(el.confirmDt?.substring(0, 10) ?? '')
                .trim()
                .includes(chartParam.value.trim()) ||
            el.orgnNm.trim().includes(chartParam.value.trim()) ||
            el.memberCount.trim().includes(chartParam.value.trim())
        );
    });
};

/***************************************************
 * 카드 버튼 클릭 이벤트
 ***************************************************/
// 확정 및 확정 취소 이벤트
const confirm = (item, param) => {
    let data = _.cloneDeep(item);
    data.confirmYn = param;
    if (param === 'Y') {
        if (hasOrgnChart(item.orgnId)) {
            confirmMsg('info', `[${item.chartNm}] \n 확정 하시겠습니까?`, null, { fun: confirmAction, param: data });
        } else {
            alertMsg('warning', '이미 확정된 조직입니다.');
        }
    } else {
        confirmMsg('info', `[${item.chartNm}] \n 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: data });
    }
};
// 확정 및 확정 취소 함수
const confirmAction = item => {
    openLoading();
    confirmRiskAssessmentOrganChart({ chartId: item.chartId, confirmYn: item.confirmYn })
        .then(() => {
            init();
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
// 상세화면 이동
const showDetail = (e, confirmed) => {
    getRiskAssessmentOrganChartDetail(e).then(res => {
        if (res.list.orgnNm != null && res.list.orgnId != null) {
            chartStore.orgnItem = [{ id: res.list.orgnId, name: res.list.orgnNm, orgnId: res.list.orgnId, orgnNm: res.list.orgnNm }];
            chartStore.getMemberList({ compId: getCompId(), orgnId: res.list.orgnId });
        }
        chartStore.memberList.value = [];
        chartStore.memberAllList.value = [];
        chartStore.inputData = res.list;
        chartStore.cmd = confirmed ? 'C' : 'U';
        router.push('/RiskAssessmentOrganChartDetail');
    });
};
// 미리보기 클릭 이벤트
const clickDegree = async item => {
    elements.value = JSON.parse(item.chartData);
    await nextTick();
    await fitView({
        padding: 0.2, // 노드와 뷰포트 간 여백 비율
        includeHiddenNodes: true, // 숨겨진 노드 포함 여부
        duration: 100 // 뷰포트 이동 애니메이션 시간
    }); // 조직도 뷰포트 맞추기
};

// 확정 차수에 같은 조직이 있는지 확인 여부
const hasOrgnChart = orgnId => {
    const result = confirmedDegree.value.filter(data => data.orgnId == orgnId);
    return result.length <= 0;
};

/***************************************************
 * 우측 버튼 List
 ***************************************************/
// 조회 버튼 클릭 이벤트
btnSearch(() => {
    init();
});
const goDetail = () => {
    chartStore.cmd = 'I';
    chartStore.inputData = {};
    chartStore.orgnItem = [];
    chartStore.memberList.value = [];
    chartStore.memberAllList.value = [];
    router.push('/RiskAssessmentOrganChartDetail');
};
// 추가 버튼 클릭 이벤트
btnAdd(() => {
    addAction();
});
// 삭제 버튼 클릭 이벤트
btnDelete(() => {
    let selected = unConfirmedDegreeList.value.filter(el => el.selected);
    // 선택한 차트가 없는 경우 모달 노출 및 return
    if (selected == null || selected.length == 0) {
        alertMsg('warning', '선택한 데이터가 없습니다');
        return;
    }
    // 선택한 차트가 있는 경우 모달 노출
    else {
        confirmMsg('warning', '선택한 조직도를 삭제하시겠습니까?', null, { fun: deleteAction, param: selected });
    }
});

const addAction = () => {
    chartStore.cmd = 'I';
    chartStore.inputData = {};
    chartStore.orgnItem = [];
    chartStore.memberList.value = [];
    chartStore.memberAllList.value = [];
    router.push('/RiskAssessmentOrganChartDetail');
};

// 차트 삭제 함수
const deleteAction = selected => {
    deleteRiskAssessmentOrganChart(selected, true).then(() => {
        init();
    });
};

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { computed, watch } from 'vue';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnAdd', 'btnSearch', 'btnDelete'];
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
</script>
