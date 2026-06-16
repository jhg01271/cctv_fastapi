<template>
    <div class="contents df fdc">
        <div class="box form ui">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="row flex gutters1rem">
                    <div class="grid12-2 ul-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for>
                                <span>차수</span>
                            </label>
                            <input v-input type="text" class="w100p radius" readonly :value="chartStore.inputData.chartId" />
                        </div>
                    </div>
                    <div class="grid12-4 ul-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for required>
                                <span>차수명</span>
                            </label>
                            <input v-input type="text" class="w100p radius" v-model="chartName" :disabled="chartStore.cmd == 'C'" />
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for="email">확정일자</label>
                            <input class="w100p radius" disabled :value="formatDate(chartStore.inputData.updatedAt)" />
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for="email">등록일자</label>
                            <input v-input class="w100p radius" disabled :value="formatDate(chartStore.inputData.createdAt)" />
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field" v-if="chartStore.cmd != 'C'">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input :true-value="'Y'" :false-value="'N'" :checked="useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="useYn" />
                            </div>
                        </div>
                    </div>
                </div>

                <p class="fs1-6rem fw500 lh44px mt1rem">조직도</p>

                <div class="dndflow box" @drop="onDrop">
                    <sidebar :list="orgManageStore.orgnList" v-if="chartStore?.cmd != 'C'" />
                    <VueFlow v-model="elements" class="customnodeflow" :style="vfStyle" :default-zoom="vfZoom" @dragover="onDragOver" edge-types="step" :nodesDraggable="chartStore?.cmd != 'C'" fit-view-on-init :apply-default="false" delete-key-code="Delete">
                        <MiniMap v-show="!isDownload" pannable zoomable />
                        <template #node-resizable="resizableNodeProps">
                            <NodeResizer v-if="!isSave" min-width="100" min-height="30" />
                            <div style="padding: 10px">{{ resizableNodeProps.label }}</div>
                        </template>
                        <template #node-default="defaultNodeProps">
                            <div class="node">
                                <Handle v-if="!isSave" type="source" :position="Position.Bottom" />
                                {{ defaultNodeProps.label }}
                                <Handle v-if="!isSave" type="target" :position="Position.Top" />
                            </div>
                        </template>
                    </VueFlow>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, t, btnSearch, btnBack, btnSave, router, alertMsg, getCompId, confirmMsg, btnCopy, btnDownload, baseDownload, formatDate } = BaseView();

import { useScreenshot } from '@/components/base/useScreenshot';
const { capture } = useScreenshot();
import { NodeResizer } from '@vue-flow/node-resizer';
import { onMounted, ref, nextTick, watch } from 'vue';
import { Controls, MiniMap } from '@vue-flow/additional-components';
import { Position, VueFlow, useVueFlow, Handle } from '@vue-flow/core';
//pinia =====================================================================
import sidebar from '@/views/system/base/OrganizationChartSidebar.vue';
const { findNode, onConnect, addEdges, addNodes, project, vueFlowRef, onNodesChange, onEdgesChange, applyNodeChanges, applyEdgeChanges, fitView } = useVueFlow();
import { getOrgChartReport } from '@/stores/system/base/api/organizationApi';

btnCopy(() => {
    confirmMsg('info', '현재 조직도를 복사하여 신규 작성하시겠습니까?', null, { fun: copyAction });
});

btnDownload(async () => {
    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction });
});
const isDownload = ref(false); // 미니맵 가시화 여부 (출력 시에는 미니맵이 감춰질 예정)
const downloadAction = async () => {
    console.log('### chartStore.inputForm', chartStore.inputData);
    // openLoading();
    let checkedObjList = [{ writeYear: chartStore.inputData.writeYear, searchText: chartStore.inputData.chartId }];
    baseDownload(getOrgChartReport, { writeYear: chartStore.inputData.writeYear, checkedObjList: checkedObjList });

    // isDownload.value = !isDownload.value; // 미니맵 숨기기
    // await fitView({
    //     padding: 0.2, // 노드와 뷰포트 간 여백 비율
    //     includeHiddenNodes: true, // 숨겨진 노드 포함 여부
    //     duration: 100 // 뷰포트 이동 애니메이션 시간
    // }); // 조직도 뷰포트 맞추기
    // openLoading();
    // await nextTick();
    // await capture(vueFlowRef.value, { shouldDownload: true, fileName: `조직구성도_${chartName.value}` });
    // isDownload.value = !isDownload.value;
    // endLoading();
};

const copyAction = () => {
    chartStore.cmd = 'I';
    chartStore.inputData.chartId = '';
    chartStore.inputData.createdAt = '';
    chartStore.inputData.updatedAt = '';
    chartStore.inputData.confirmYn = 'N';
    buttonListStore.useBtnList = ['btnSave', 'btnBack'];
};

onNodesChange(async changes => {
    if (chartStore.cmd == 'C') {
        return;
    }
    let deleteChanges = [];
    let nextChanges = [];
    for (const change of changes) {
        if (change.type === 'remove') {
            deleteChanges.push(change);
        } else {
            nextChanges.push(change);
        }
    }
    if (deleteChanges.length) {
        await confirmMsg('info', '삭제하시겠습니까?', null, { fun: nodeDeleteAction, param: deleteChanges });
    }
    applyNodeChanges(nextChanges);
});

onEdgesChange(async changes => {
    if (chartStore.cmd == 'C') {
        return;
    }
    let deleteChanges = [];
    let nextChanges = [];
    for (const change of changes) {
        if (change.type === 'remove') {
            deleteChanges.push(change);
        } else {
            nextChanges.push(change);
        }
    }
    if (deleteChanges.length) {
        await confirmMsg('info', '삭제하시겠습니까?', null, { fun: edgeDeleteAction, param: deleteChanges });
    }
    applyEdgeChanges(nextChanges);
});

const nodeDeleteAction = async changes => {
    const deletedNodeIds = changes.filter(change => change.type === 'remove').map(change => change.id);
    // 연결된 edge 찾기
    const connectedEdges = elements.value.filter(el => el.type === 'step' && (deletedNodeIds.includes(el.source) || deletedNodeIds.includes(el.target)));

    // 연결된 edge 삭제
    if (connectedEdges.length > 0) {
        await edgeDeleteAction(
            connectedEdges.map(edge => ({
                id: edge.id,
                type: 'remove'
            }))
        );
    }
    // 노드 삭제
    await applyNodeChanges(changes);
};
const edgeDeleteAction = async changes => {
    await applyEdgeChanges(changes);
};

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnCopy', 'btnDownload'];

btnSearch(() => {
    searchAction(true);
});
const searchAction = (notify = false) => {
    orgManageStore.getOrgnList(notify);
    elements.value = JSON.parse(chartStore.inputData.chartData);
    chartName.value = chartStore.inputData.chartNm;
    useYn.value = chartStore.inputData.useYn;
};

btnBack(() => {
    router.go(-1);
});
const chartName = ref('');
const isSave = ref(false);
btnSave(() => {
    if (chartName.value == '' || chartName.value == null) {
        alertMsg('warning', '차수명은 필수입니다.');
        return;
    }
    confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
});
const saveAction = async notify => {
    openLoading();
    isSave.value = !isSave.value;
    let param = {
        compId: getCompId(),
        chartNm: chartName.value,
        chartData: JSON.stringify(elements.value),
        orgCount: elements.value.filter(el => el.type == 'default').length,
        useYn: useYn.value,
        chartBlobData: await getBlobData()
    };
    if (chartStore.cmd == 'U') {
        param.chartId = chartStore.inputData.chartId;
        updateOrganizationChart(param, notify)
            .then(res => {
                chartStore.inputData = JSON.stringify(elements.value);
                isSave.value = !isSave.value;
                endLoading();
            })
            .catch(() => {
                isSave.value = !isSave.value;
                endLoading();
            });
    } else if (chartStore.cmd == 'I') {
        insertOrganizationChart(param, notify)
            .then(res => {
                if (res.success) {
                    getOrganizationChartDetail(res.result.chartId).then(res => {
                        chartStore.inputData = res.list;
                        chartStore.cmd = 'U';
                        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnCopy', 'btnDownload'];
                    });
                }
                isSave.value = !isSave.value;
                endLoading();
            })
            .catch(() => {
                isSave.value = !isSave.value;
                endLoading();
            });
    }
};
const getBlobData = async () => {
    isDownload.value = !isDownload.value; // 미니맵 숨기기
    await fitView({
        padding: 0.2, // 노드와 뷰포트 간 여백 비율
        includeHiddenNodes: true, // 숨겨진 노드 포함 여부
        duration: 100 // 뷰포트 이동 애니메이션 시간
    }); // 조직도 뷰포트 맞추기
    await nextTick();
    return new Promise(resolve => {
        capture(vueFlowRef.value, { shouldDownload: false, fileName: `비상통제조직도_${chartStore.inputData.chartNm}` })
            .then(res => {
                isDownload.value = !isDownload.value;
                endLoading();
                resolve(res);
            })
            .catch(() => endLoading());
    });
};

const vfZoom = ref(2);
import { insertOrganizationChart, updateOrganizationChart, getOrganizationChartDetail } from '@/stores/system/base/api/organizationApi';

import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();
import { useOrganizationChartStore } from '@/stores/system/base/orgChart';
const chartStore = useOrganizationChartStore();

const useYn = ref('Y');
const elements = ref([]);
onMounted(() => {
    if (chartStore.cmd == 'U') {
        orgManageStore.getOrgnList(true);
        elements.value = JSON.parse(chartStore.inputData.chartData);
        chartName.value = chartStore.inputData.chartNm;
        useYn.value = chartStore.inputData.useYn;
    } else if (chartStore.cmd == 'C') {
        buttonListStore.useBtnList = ['btnSearch', 'btnBack', 'btnCopy', 'btnDownload'];
        elements.value = JSON.parse(chartStore.inputData.chartData);
        chartName.value = chartStore.inputData.chartNm;
        useYn.value = chartStore.inputData.useYn;
    } else if (chartStore.cmd == 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
        orgManageStore.getOrgnList(true);
        elements.value = [];
    } else {
        router.go(-1);
    }
});

const onDragOver = event => {
    event.preventDefault();

    if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move';
    }
};
onConnect(params => {
    params.type = 'step';
    addEdges([params]);
});

const onDrop = event => {
    const type = event.dataTransfer?.getData('application/vueflow');
    let data = JSON.parse(type);
    const { left, top } = vueFlowRef.value.getBoundingClientRect();

    const position = project({
        x: event.clientX - left,
        y: event.clientY - top
    });

    const newNode = {
        id: data.orgnId,
        position,
        label: `${data.orgnNm}`,
        sourcePosition: Position.Top,
        targetPosition: Position.Bottom
    };
    addNodes([newNode]);

    nextTick(() => {
        const node = findNode(newNode.id);
        const stop = watch(
            () => node.dimensions,
            dimensions => {
                if (dimensions.width > 0 && dimensions.height > 0) {
                    node.position = {
                        x: node.position.x - node.dimensions.width / 2,
                        y: node.position.y - node.dimensions.height / 2
                    };
                    stop();
                }
            },
            { deep: true, flush: 'post' }
        );
    });
};
</script>
