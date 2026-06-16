<template>
    <div class="contents df fdc" id="safetyForm">
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
                    <div class="grid12-2 sm-grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="">
                                <span>차수</span>
                            </label>
                            <input v-input type="text" class="w100p radius" readonly :value="chartStore.inputData.chartId" placeholder="저장 시 자동으로 채번" />
                        </div>
                    </div>
                    <div class="grid12-4 sm-grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="chartName" required>
                                <span>차수명</span>
                            </label>
                            <input required id="chartName" v-input type="text" class="w100p radius" v-model="chartName" :disabled="chartStore.cmd == 'C'" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="email">확정일자</label>
                            <input class="w100p radius datepicker" disabled :value="chartStore.cmd == 'C' ? formatDate(chartStore.inputData.confirmDt) : ''" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6 us-grid12-8">
                        <div class="field">
                            <label for="email">등록일자</label>
                            <input v-input class="w100p radius datepicker" disabled :value="formatDate(chartStore.inputData.createdAt)" />
                        </div>
                    </div>
                    <div class="grid12-2 us-grid12-4">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input :disabled="chartStore.cmd == 'C'" :true-value="'Y'" :false-value="'N'" :checked="useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="useYn" />
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 조직도 VueFlow 시작 -->
                <h2 class="mt2-2rem">조직도</h2>
                <div class="dndflow pr box" @drop="onDrop">
                    <div class="pa r2rem t2rem zi3 us-r0 us-neg-t5-5rem">
                        <button type="button" class="btn radius active w18rem" v-button @click="openSafetyOrgPopup">
                            <span>안전보건 조직 역할 관리</span>
                        </button>
                    </div>
                    <!-- C(확정차수인경우)가 아닌 경우 노출 -->
                    <sidebar :list="orgManageStore.orgnList" v-if="chartStore?.cmd != 'C'" />
                    <VueFlow v-model="elements" class="customnodeflow" :style="vfStyle" :default-zoom="vfZoom" @dragover="onDragOver" edge-types="step" :nodesDraggable="chartStore?.cmd != 'C'" fit-view-on-init :apply-default="false" delete-key-code="Delete">
                        <MiniMap v-show="!isDownload" pannable zoomable />
                        <template #node-resizable="resizableNodeProps">
                            <Handle v-if="!isSave" type="source" :position="Position.Bottom" />
                            <NodeResizer v-if="!isSave" min-width="100" min-height="30" />
                            <div>{{ resizableNodeProps.label }}</div>
                            <Handle v-if="!isSave" type="target" :position="Position.Top" />
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
    <teleport to="body">
        <i-PopupDialog ref="safetyOrgPopup">
            <div class="contents form ui w70rem md-w100p">
                <DataSetManagePopup :title="'안전보건 조직 역할 관리'" :subTitle="'안전보건 조직 역할'" :datasetYn="'Y'" id="orgnRoleId" nm="orgnRoleNm" :popupDataList="safetyOrgList" :cardComponent="DefaultDataSetManageComponet" @filter="filterSafetyOrgPopup" @search="searchSafetyOrgPopup" @sample="sampleSafetyOrgPopup" @add="addSafetyOrgPopup" @save="saveSafetyOrgPopup" @delete="deleteSafetyOrgPopup" @close="closeSafetyOrgPopup"> </DataSetManagePopup>
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="safetyOrgSamplePopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'안전보건 조직 역할 예시'" filterKey="orgnRoleNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, btnSearch, validationStore, t, btnBack, btnSave, router, alertMsg, getCompId, confirmMsg, btnCopy, btnDownload, formatDate, baseDownload, getDuplicatedData } = BaseView();
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { useScreenshot } from '@/components/base/useScreenshot';
const { capture } = useScreenshot();
import { NodeResizer } from '@vue-flow/node-resizer';
import { onMounted, ref, nextTick, watch, watchEffect } from 'vue';
import { Background, Controls, MiniMap } from '@vue-flow/additional-components';
import { Position, VueFlow, useVueFlow, Handle } from '@vue-flow/core';
//pinia =====================================================================
import sidebar from '@/views/safewiz/participation/SafetyAndHealthOrganChartSidebar.vue';
const { findNode, onConnect, addEdges, addNodes, project, vueFlowRef, onNodesChange, onEdgesChange, applyNodeChanges, applyEdgeChanges, fitView } = useVueFlow();

onMounted(() => {
    orgManageStore.getUserList();
});

btnCopy(() => {
    confirmMsg('info', '현재 조직도를 복사하여 신규 작성하시겠습니까?', null, { fun: copyAction });
});
btnDownload(async () => {
    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction });
});

const isDownload = ref(false); // 미니맵 가시화 여부 (출력 시에는 미니맵이 감춰질 예정)
const downloadAction = async () => {
    openLoading();
    let checkedObjList = [{ searchText: chartStore.inputData.chartId }];
    baseDownload(getShOrgChartReport, { checkedObjList: checkedObjList });
    // isDownload.value = !isDownload.value; // 미니맵 숨기기
    // await fitView({
    //     padding: 0.2, // 노드와 뷰포트 간 여백 비율
    //     includeHiddenNodes: true, // 숨겨진 노드 포함 여부
    //     duration: 100 // 뷰포트 이동 애니메이션 시간
    // }); // 조직도 뷰포트 맞추기
    // openLoading();
    // await nextTick();
    // await capture(vueFlowRef.value, { shouldDownload: true, fileName: `안전보건 조직도_${chartStore.inputData.chartNm}` });
    // isDownload.value = !isDownload.value;
    // endLoading();
};

const copyAction = () => {
    chartStore.cmd = 'I';
    chartStore.inputData.chartId = '';
    chartStore.inputData.createdAt = '';
    chartStore.inputData.updatedAt = '';
    // 확정차수를 복사하는 경우 인원 재조회
    if (chartStore.inputData.confirmYn === 'Y') {
        orgManageStore.getUserList();
    }
    console.log('elements.value', elements.value);
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
        let deleteNodes = _.cloneDeep(deleteChanges);
        for (let i = 0; i < deleteChanges.length; i++) {
            const targetedNodes = elements.value.filter(el => el.parentNode === deleteChanges[i].id).map(el => ({ ...el, type: 'remove' }));

            deleteNodes = [...deleteNodes, ...targetedNodes];
        }
        await confirmMsg('info', t('msgDelete'), null, { fun: nodeDeleteAction, param: deleteNodes });
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
        await confirmMsg('info', t('msgDelete'), null, { fun: edgeDeleteAction, param: deleteChanges });
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
buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnCopy', 'btnDownload'];
btnBack(() => {
    router.go(-1);
});
const chartName = ref('');
const isSave = ref(false);
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('safetyForm', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), null, { fun: saveAction });
    }
});
const saveAction = async () => {
    openLoading();
    isSave.value = !isSave.value;
    let param = {
        compId: getCompId(),
        chartNm: chartName.value,
        chartData: JSON.stringify(elements.value),
        chartBlobData: await getBlobData(),
        orgCount: elements.value.filter(el => el.type == 'default').length,
        useYn: useYn.value
    };
    if (chartStore.cmd == 'U') {
        param.chartId = chartStore.inputData.chartId;
        updateOrganizationChart(param, true)
            .then(res => {
                endLoading();
                isSave.value = !isSave.value;
            })
            .catch(() => {
                isSave.value = !isSave.value;
                endLoading();
            });
    } else if (chartStore.cmd == 'I') {
        insertOrganizationChart(param, true)
            .then(res => {
                if (res.success) {
                    getOrganizationChartDetail(res.result.chartId).then(res => {
                        chartStore.inputData = res.list;
                        chartStore.cmd = 'U';
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
        capture(vueFlowRef.value, { shouldDownload: false, fileName: `` })
            .then(res => {
                isDownload.value = !isDownload.value;
                endLoading();
                resolve(res);
            })
            .catch(() => endLoading());
    });
};

const vfStyle = ref({ backgroundColor: '#ffffff', height: '100%' });
const vfZoom = ref(2);
import { getOrganizationChartDetail, insertOrganizationChart, updateOrganizationChart, getShOrgChartReport, getSafetyOrgList, getDataSetSafetyOrgList, saveSafetyOrgList } from '@/stores/safewiz/participation/api/safetyOrgChartApi';

import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();
import { useSafetyOrgnChartStore } from '@/stores/safewiz/participation/safetyOrgChart';
const chartStore = useSafetyOrgnChartStore();

const useYn = ref('Y');
const elements = ref([]);
watchEffect(() => {
    if (chartStore.cmd == 'U') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnCopy', 'btnDownload', 'btnSearch'];
        elements.value = JSON.parse(chartStore.inputData.chartData);
        chartName.value = chartStore.inputData.chartNm;
        useYn.value = chartStore.inputData.useYn;
    } else if (chartStore.cmd == 'C') {
        buttonListStore.useBtnList = ['btnBack', 'btnCopy', 'btnDownload', 'btnSearch'];
        elements.value = JSON.parse(chartStore.inputData.chartData);
        chartName.value = chartStore.inputData.chartNm;
        useYn.value = chartStore.inputData.useYn;
    } else if (chartStore.cmd == 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
        // elements.value = [];
    } else {
        router.go(-1);
    }
});

btnSearch(() => {
    getOrganizationChartDetail(chartStore.inputData.chartId, true).then(res => {
        chartStore.inputData = res.list;
        orgManageStore.getUserList();
        elements.value = JSON.parse(chartStore.inputData.chartData);
        chartName.value = chartStore.inputData.chartNm;
        useYn.value = chartStore.inputData.useYn;
    });
    initSafetyOrgList();
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
    const type = event.dataTransfer?.getData('application/organization');
    let data;
    let newNode;
    const { left, top } = vueFlowRef.value.getBoundingClientRect();

    const position = project({
        x: event.clientX - left,
        y: event.clientY - top
    });
    if (type) {
        data = JSON.parse(type);
        const parentNode = elements.value.find(node => {
            const distanceX = position.x - node.position.x;
            const distanceY = position.y - node.position.y;
            if (node.style) {
                return distanceX > 0 && distanceY > 0 && distanceX < extractNumber(node.style?.width) && distanceY < extractNumber(node.style?.height) && node.type == 'resizable'; // 임의의 범위
            } else {
                return false;
            }
        });
        if (parentNode) {
            newNode = {
                id: data.hrId,
                position: {
                    x: 100,
                    y: 50
                },
                label: `${data.hrNm}`,
                sourcePosition: Position.Top,
                targetPosition: Position.Bottom,
                parentNode: parentNode?.id,
                extent: 'parent'
            };
        } else {
            alertMsg('err', '역할 카드 안에 넣어주세요.');
            return;
        }
    } else {
        data = JSON.parse(event.dataTransfer?.getData('application/safetyGroup'));
        newNode = {
            id: 'role' + data.orgnRoleId,
            position,
            type: 'resizable',
            label: `${data.orgnRoleNm}`,
            style: { width: '200px', height: '200px' }
        };
    }
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

function extractNumber(str) {
    // 정규식을 사용하여 숫자만 추출
    const number = str.match(/\d+/);
    // 문자열을 숫자로 변환하여 반환
    return number ? Number(number[0]) : null;
}

//-----------------------------------------------------------
//-- 직무관리
//-----------------------------------------------------------
const safetyOrgPopup = ref();
const safetyOrgSamplePopup = ref();
const currentSafetyOrgList = ref();
const safetyOrgList = ref([]);

// --------------- 데이터 세팅
const initSafetyOrgList = () => {
    getSafetyOrgList({ compId: getCompId() }, true).then(async res => {
        orgManageStore.safetyOrgList = res.list.filter(item => item.useYn === 'Y');
        safetyOrgList.value = res.list;
        currentSafetyOrgList.value = res.list;
    });
};

// --------------- 검색
const filterSafetyOrgPopup = text => {
    if (text === '') {
        safetyOrgList.value = currentSafetyOrgList.value;
    } else {
        const filteredData = safetyOrgList.value.filter(el => el.jbrpNm.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);
        safetyOrgList.value = filteredData;
    }
};

// --------------- 조회
const searchSafetyOrgPopup = () => {
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchActionSafetyOrgPopup, param: true });
    } else {
        searchActionSafetyOrgPopup(true);
    }
};

const searchActionSafetyOrgPopup = () => {
    openLoading();
    initSafetyOrgList();
    endLoading();
};

// --------------- 예시 불러오기
const sampleSafetyOrgPopup = () => {
    safetyOrgSamplePopup.value.onOpen();
};

// --------------- 추가
const addSafetyOrgPopup = () => {
    const newData = {
        orgnRoleId: '',
        orgnRoleNm: '',
        ordSeq: '',
        useYn: 'Y',
        cmd: 'I',
        checked: true
    };
    safetyOrgList.value.push(newData);

    // 새로 추가된 항목으로 포커스 이동
    const index = safetyOrgList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// --------------- 저장, 수정
const saveSafetyOrgPopup = async () => {
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionSafetyOrgPopup, param: true });
    }
};

const saveActionSafetyOrgPopup = async () => {
    openLoading();
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    await checkedList.forEach(val => {
        val.compId = getCompId(); //dataSet의 경우 comp_id가 존재하지 않기 때문에 본인의 comp_id로 수정
    });
    saveSafetyOrgList(checkedList, true)
        .then(res => {
            if (res.success) {
                endLoading();
                initSafetyOrgList();
            }
        })
        .catch(err => {
            endLoading();
        });
};

// --------------- 삭제
const deleteSafetyOrgPopup = () => {
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionSafetyOrgPopup, param: true });
    } else {
        alertMsg('error', '선택된 항목이 없습니다.');
    }
};

const deleteActionSafetyOrgPopup = async () => {
    openLoading();
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    await checkedList.forEach(val => {
        val.useYn = 'N';
    });

    saveSafetyOrgList(checkedList, true)
        .then(res => {
            if (res.success) {
                endLoading();
                initSafetyOrgList();
            }
        })
        .catch(err => {
            checkedList.forEach(val => {
                val.useYn = 'Y';
                endLoading();
            });
        });
};

// --------------- 팝업 열기
const openSafetyOrgPopup = async () => {
    openLoading();
    await initSafetyOrgList();
    safetyOrgPopup.value.onOpen();
    endLoading();
};

// --------------- 팝업 닫기
const closeSafetyOrgPopup = () => {
    const checkedList = safetyOrgList.value.filter(val => val.checked == true);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: closeActionSafetyOrgPopup, param: '' });
    } else {
        closeActionSafetyOrgPopup();
    }
};

const closeActionSafetyOrgPopup = () => {
    safetyOrgList.value = [];
    safetyOrgPopup.value.onClose();
};

const getSampleDataSetListFunc = async () => {
    openLoading();
    let resData = { list: [] };
    await getDataSetSafetyOrgList({}, true)
        .then(async res => {
            res.list.forEach(val => {
                val.desc = val.remark;
                resData.list.push(val);
            });
            endLoading();
        })
        .catch(err => {
            endLoading();
        });
    return resData;
};

const applySampleDataSetMngPopup = async selectItems => {
    const filteredData = getDuplicatedData(safetyOrgList.value, selectItems, 'orgnRoleId', 'orgnRoleNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === selectItems.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applyActionSampleDataSetMngList(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applyActionSampleDataSetMngList(filteredData.deDuplicatedDataList) });
};

const applyActionSampleDataSetMngList = async confirmList => {
    let index = null;
    let element = null;

    await confirmList.forEach(val => {
        val.cmd = 'I';
        safetyOrgList.value.push(val);
    });
    safetyOrgSamplePopup.value.onClose();

    index = safetyOrgList.value.length - 1;
    setTimeout(() => {
        element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const closeSamplDataSetMngPopup = () => {
    safetyOrgSamplePopup.value.onClose();
};
</script>
