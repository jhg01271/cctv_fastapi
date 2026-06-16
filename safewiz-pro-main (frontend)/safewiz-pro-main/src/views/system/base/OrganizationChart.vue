<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic w100p bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm"  @keyup.enter="applyFilter"/>
                <button type="submit" class="shrink0">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter"/>
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
                                <div class="pa2-2rem">
                                    <div class="row flex gutters2rem">
                                        <div class="grid12-6 es-grid12-12" v-for="(item, index) in confirmedDegree" :key="index">
                                            <i-card-order :v-model="item.selected" :confirm="true" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, true)" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="title-box fg1">
                                <p>차수 리스트</p>
                                <div class="pa2-2rem">
                                    <div class="row flex gutters2rem">
                                        <div class="grid12-6 es-grid12-12" v-for="(item, index) in unConfirmedDegreeList" :key="index">
                                            <i-card-order :v-model="item.selected" :confirm="false" :title="item.chartNm" :data="item" :disabled="item.useYn === 'N'" :confirmable="!confirmedDegree.length > 0" @confirm="confirm(item, $event)" @preview="clickDegree(item)" @editor="showDetail(item.chartId, false)" />
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
                                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addOrganChart') }}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
            <div class="grid12-5 ul-grid12-12 ul-h100p">
                <div class="box h100p ul-h50p">
                    <VueFlow v-model="elements" class="customnodeflow" :default-zoom="vfZoom" :nodesDraggable="false" fit-view-on-init>
                        <MiniMap pannable zoomable />
                        <template #node-default="defaultNodeProps">
                            <div class="node">
                                <!-- <Handle v-if="true" type="source" :position="Position.Top" /> -->
                                {{ defaultNodeProps.label }}
                                <!-- <Handle v-if="true" type="target" :position="Position.Bottom" /> -->
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
import { confirmOrganizationChart, deleteOrganizationChart, getOrganizationChart, getOrganizationChartDetail } from '@/stores/system/base/api/organizationApi';
const { openLoading, endLoading, onMounted, router, ref, t, btnAdd, getCompId, btnDelete, btnSearch, confirmMsg, alertMsg, formatDate } = BaseView();
import _ from 'lodash';
import { VueFlow, useVueFlow } from '@vue-flow/core';
const { fitView } = useVueFlow();
import { nextTick } from 'vue';

import { Background, MiniMap } from '@vue-flow/additional-components';
const vfZoom = ref(50);
import { useOrganizationChartStore } from '@/stores/system/base/orgChart';
const chartStore = useOrganizationChartStore();

btnSearch(() => {
    init();
});
btnAdd(() => {
    addAction();
});
btnDelete(() => {
    let selected = unConfirmedDegreeList.value.filter(el => el.selected);
    if (selected == null || selected.length == 0) {
        alertMsg('warning', '선택한 데이터가 없습니다');
        return;
    }
    confirmMsg('warning', '선택한 조직도를 삭제하시겠습니까?', null, { fun: deleteAction, param: selected });
});

const addAction = () => {
    chartStore.cmd = 'I';
    chartStore.inputData = {};
    router.push('/OrganizationChartDetail');
};

const deleteAction = selected => {
    deleteOrganizationChart(selected, true).then(() => {
        filteredDegreeList.value = [];
        init();
    });
};

const searchTerm = ref('');
const filteredDegreeList = ref([]);

// 조직 구성도 검색
const applyFilter = () => {
    const term = searchTerm.value.toLowerCase();
    filteredDegreeList.value = degreeList.value.filter(item =>
      item.chartNm?.toLowerCase().includes(term) ||
      item.docNo?.toLowerCase().includes(term) ||
      item.orgCount?.toLowerCase().includes(term) ||
      item.createdAt?.toLowerCase().includes(term) ||
      item.confirmDt?.toLowerCase().includes(term)
    );
    initFilter(filteredDegreeList.value);
}

const initFilter = (list) => {
    confirmedDegree.value = list
    .filter(item => item.confirmYn === 'Y')
    .map(el => ({
      ...el,
      detail: {
        등록일자: formatDate(el.createdAt),
        확정일자: el.confirmDt ? formatDate(el.confirmDt) : '-',
        '조직 수': el.orgCount
      }
    }));

  unConfirmedDegreeList.value = list
    .filter(item => item.confirmYn === 'N')
    .map(el => ({
      ...el,
      detail: {
        등록일자: formatDate(el.createdAt),
        확정일자: el.confirmDt ? formatDate(el.confirmDt) : '-',
        '조직 수': el.orgCount
      }
    }));
  };

const confirm = (item, param) => {
    const params = { chartId: item.chartId, confirmYn: param };
    confirmMsg('info', param == 'Y' ? `[${item.chartNm}] \n 확정 하시겠습니까?` : `[${item.chartNm}] \n 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: params });
};

const confirmAction = item => {
    openLoading();
    confirmOrganizationChart({ chartId: item.chartId, confirmYn: item.confirmYn })
        .then(() => {
            init();
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};

const confirmedDegree = computed(() => {
    return filteredDegreeList.value.filter(el => {
        return el.confirmYn == 'Y';
    });
});

const unConfirmedDegreeList = computed(() => {
    return filteredDegreeList.value.filter(el => {
        return el.confirmYn == 'N';
    });
});

const showDetail = (e, confirmed) => {
    getOrganizationChartDetail(e).then(res => {
        chartStore.inputData = res.list;
        chartStore.cmd = confirmed ? 'C' : 'U';
        router.push('/OrganizationChartDetail');
    });
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
const init = () => {
    elements.value = [];
    getOrganizationChart(searchParam.value, true).then(res => {
        res.list.forEach(el => {
            el.detail = {
                등록일자: formatDate(el.createdAt),
                확정일자: el.confirmDt ? formatDate(el.confirmDt) : '-',
                '조직 수': el.orgCount
            };
        });
        degreeList.value = res.list;
        filteredDegreeList.value = _.cloneDeep(res.list);
        if (confirmedDegree.value.length) {
            clickDegree(confirmedDegree.value[0]);
            // elements.value = JSON.parse(confirmedDegree.value[0].chartData);
        }
    });
};
onMounted(() => {
    init();
});
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { computed } from 'vue';
import { endOfDay } from 'date-fns';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnAdd', 'btnSearch', 'btnDelete'];
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
</script>
