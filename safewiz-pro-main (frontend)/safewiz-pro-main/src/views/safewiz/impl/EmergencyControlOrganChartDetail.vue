<template>
    <div class="contents df fdc">
        <div class="box form ui" id="form">
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
                    <div class="grid12-2 ul-grid12-3 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for required>
                                <span>차수</span>
                            </label>
                            <input v-input type="text" class="w100p br4px" readonly :value="chartStore.inputForm.chartId" placeholder="저장 시 자동으로 생성됩니다." />
                        </div>
                    </div>
                    <div class="grid12-5 ul-grid12-9 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for="chartNm" required>
                                <span>차수명</span>
                            </label>
                            <input v-input type="text" class="w100p br4px" v-model="chartStore.inputForm.chartNm" placeholder="차수명을 입력하세요." id="chartNm" required @change="changeFlag = true" />
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-3 lg-grid12-5 sm-grid12-12">
                        <div class="field">
                            <label for="email">확정일자</label>
                            <input :value="formatDate(chartStore.inputForm.confirmAt)" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-3 lg-grid12-5 sm-grid12-12">
                        <div class="field">
                            <label for="email">등록일자</label>
                            <input :value="formatDate(chartStore.inputForm.createdAt)" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                        </div>
                    </div>
                    <div class="grid12-1 lg-grid12-2 lg-grid12-2 es-grid12-12" v-if="chartStore.inputForm.cmd != 'C'">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input :true-value="'Y'" :false-value="'N'" :checked="chartStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="chartStore.inputForm.useYn" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-3 ul-grid12-6 lg-grid12-12">
                        <div class="field">
                            <label for="emergencyType" required>
                                <span>비상사태 유형</span>
                            </label>
                            <i-chips :chips="[{ id: chartStore.inputForm.emergencyType, name: chartStore.inputForm.emergencyTypeNm }]" @removeChip="removeEmergencyType" @popup="addEmergencyType" required></i-chips>
                        </div>
                    </div>
                    <div class="grid12-9 ul-grid12-6 lg-grid12-12">
                        <div class="field">
                            <label></label>
                            <div class="df aic h4-4rem">
                                <button type="button" class="btn radius active" v-button @click="openEmergencyMngType">
                                    <span>비상사태 유형 관리</span>
                                </button>
                                <span class="ml1rem fs1-3rem fw300">비상사태 유형을 먼저 구성하세요.</span>
                            </div>
                        </div>
                    </div>
                </div>

                <h2 class="mt2-2rem">조직도</h2>
                <div class="dndflow pr box" @drop="onDrop">
                    <sidebar v-if="chartStore.inputForm.emergencyType && chartStore.inputForm.cmd != 'C'" :list="chartStore.orgnList" />
                    <VueFlow v-model="elements" class="customnodeflow" :style="vfStyle" :default-zoom="vfZoom" @dragover="onDragOver" edge-types="step" :nodesDraggable="chartStore.inputForm.cmd != 'C'" fit-view-on-init :apply-default="false" delete-key-code="Delete">
                        <!-- <Background variant="lines" gap="20" size="1.2" /> -->
                        <MiniMap v-show="!isDownload" pannable zoomable />
                        <template #node-resizable="resizableNodeProps">
                            <Handle v-if="!isSave" type="source" :position="Position.Bottom" />
                            <NodeResizer v-if="!isSave" min-width="100" min-height="30" />
                            <Handle v-if="!isSave" type="target" :position="Position.Top" />
                            <div>{{ resizableNodeProps.label }}</div>
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
            <teleport to="body">
                <i-PopupDialog ref="emergencyTypePopup">
                    <!-- 비상사태 유형 선택 팝업 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :title="'비상사태 유형'" uniqueKey="typeId" filterKey="typeNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ compId: chartStore.inputForm.compId }" :fetch-data="getEmergencyTypeList" @close="closeEmergencyType" />
                    </div>
                </i-PopupDialog>

                <i-PopupDialog ref="emergencyTypeMngPopup">
                    <!-- 비상사태 유형 관리 팝업-->
                    <div class="contents w1024px md-w100p">
                        <DataSetManageByItemPopup ref="managePopup" title="비상사태 유형 관리" :masterTitle="'비상사태 유형'" :masterReadonly="false" :masterApi="getEmergencyType" :masterApiParam="{}" masterKey="typeId" masterDisplayKey="typeNm" :detailTitle="'비상통제 역할'" :detailApi="getEmergencyTypeRole" detailKey="typeId" :detailOption="detailOption" @sample="btnSample" @delete="btnEmergencyMngDelete" @save="btnEmergencyMngSave" @close="closeEmergencyMng" />
                    </div>
                </i-PopupDialog>
                <i-PopupDialog ref="samplePopup">
                    <!-- 예시 불러오기 팝업 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :component="BaseSelectComponet" :title="'비상사태 유형 예시'" filterKey="typeNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getEmergencyTypeDataset" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                    </div>
                </i-PopupDialog>
            </teleport>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { computed, validationStore, openLoading, endLoading, t, formatDate, getCurrentDate, btnBack, btnSearch, btnSave, router, alertMsg, getCompId, confirmMsg, btnCopy, btnDownload, baseDownload, getDuplicatedData, nextTick } = BaseView();

import { useScreenshot } from '@/components/base/useScreenshot';
const { capture } = useScreenshot();
import { NodeResizer } from '@vue-flow/node-resizer';
import { onMounted, onBeforeUnmount, ref, watch } from 'vue';
import { Position, VueFlow, useVueFlow, Handle } from '@vue-flow/core';
//pinia =====================================================================
import sidebar from '@/views/safewiz/impl/EmergencyControlOrganChartSidebar.vue';
const { findNode, onConnect, addEdges, addNodes, project, vueFlowRef, onNodesChange, onEdgesChange, applyNodeChanges, applyEdgeChanges, fitView } = useVueFlow();

import { getEmergencyTypeList, getEmergencyOrgChartList, getEmergencyRoleList, getEmergencyOrgChartReport, getEmergencyType, getEmergencyTypeRole, getEmergencyTypeDataset, deleteEmergencyType, saveEmergencyType } from '@/stores/safewiz/impl/api/emerCtrlOrgChartApi.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

// ------- 비상사태 유형 관리 팝업 -------
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
const emergencyTypeMngPopup = ref(null);
const managePopup = ref(null);
const detailOption = ref([
    {
        fieldDisplayKey: 'roleNm',
        fieldNm: '비상통제 역할명',
        fieldKey: 'roleId',
        class: '',
        required: true
    }
]); // 우측 카드 에디팅 옵션 데이터
const openEmergencyMngType = async () => {
    // 팝업 열기
    emergencyTypeMngPopup.value.onOpen();
};

const btnSample = () => {
    samplePopup.value.onOpen();
};
const btnEmergencyMngDelete = deleteDataList => {
    deleteDataList.forEach(el => {
        el.roles = el.detailDataList;
    });
    deleteEmergencyType(deleteDataList, true).then(res => {
        managePopup.value.searchMaster(false);
    });
};
const btnEmergencyMngSave = saveDataList => {
    saveDataList.forEach(el => {
        (el.typeId = el.typeId ? el.typeId : ''), (el.roles = el.detailDataList);
    });
    saveEmergencyType(saveDataList, true).then(res => {
        managePopup.value.searchMaster(false);
    });
};
const closeEmergencyMng = () => {
    // 닫기 버튼 클릭
    emergencyTypeMngPopup.value.onClose();
};

const emergencyTypePopup = ref(null);
const addEmergencyType = () => {
    emergencyTypePopup.value.onOpen();
};

import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
const samplePopup = ref(null);
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(managePopup.value.masterDataList, data, 'typeId', 'typeNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
        el.detailDataList = el.roles.map(role => ({
            ...role,
            checked: true,
            cmd: 'I'
        }));
    });
    managePopup.value.setMasterSampleData(dataList);

    samplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    samplePopup.value.onClose();
};

// ------------------------------------------
const changeFlag = ref(false);
// 조직 요소 제거(x버튼 클릭)
const removeEmergencyType = index => {
    chartStore.inputForm.emergencyType = '';
    chartStore.inputForm.emergencyTypeNm = '';
    chartStore.roleList = [];
    changeFlag.value = true;
};

const closeEmergencyType = data => {
    if (data && data[0]) {
        const { typeId, typeNm, roles } = data[0];
        chartStore.inputForm.emergencyType = typeId;
        chartStore.inputForm.emergencyTypeNm = typeNm;
        chartStore.roleList = roles;
        changeFlag.value = true;
    }

    const popup = emergencyTypePopup.value;
    if (popup?.onClose) {
        popup.onClose();
    }
};

btnCopy(() => {
    if (chartStore.inputForm.confirmYn === 'Y') {
        confirmMsg('info', t('현재 조직도를 복사하여 신규 작성하시겠습니까?'), null, { fun: copyAction });
    } else if (isChanged.value) confirmMsg('info', t('msgSaveContinue'), '', { fun: copyAction, param: '' });
    else confirmMsg('info', t('현재 조직도를 복사하여 신규 작성하시겠습니까?'), null, { fun: copyAction });
});

btnDownload(() => {
    if (chartStore.inputForm.confirmYn === 'Y') {
        confirmMsg('info', t('msgPrint'), null, { fun: downloadAction });
    } else if (isChanged.value) confirmMsg('info', t('msgSaveContinue'), '', { fun: downloadAction, param: '' });
    else confirmMsg('info', t('msgPrint'), null, { fun: downloadAction });
});
const isDownload = ref(false); // 미니맵 가시화 여부 (출력 시에는 미니맵이 감춰질 예정)
const downloadAction = () => {
    openLoading();
    let checkedObjList = [{ writeYear: chartStore.inputForm.writeYear, searchText: chartStore.inputForm.chartId }];
    baseDownload(getEmergencyOrgChartReport, { writeYear: chartStore.inputForm.writeYear, checkedObjList: checkedObjList });
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
        capture(vueFlowRef.value, { shouldDownload: false, fileName: `(${emergencyResponseStore.searchParam.searchText})비상통제조직도_${chartStore.inputForm.chartNm}` })
            .then(res => {
                isDownload.value = !isDownload.value;
                endLoading();
                resolve(res);
            })
            .catch(() => endLoading());
    });
};

const emergencyTypeMng = () => {
    alertMsg('info', '팝업 컴포넌트 개발중입니다.');
};
const copyAction = () => {
    chartStore.inputForm.cmd = 'I';
    chartStore.inputForm.chartId = '';
    chartStore.inputForm.chartNm = '';
    chartStore.inputForm.confirmYn = 'N';
    chartStore.inputForm.useYn = 'Y';
    chartStore.inputForm.confirmAt = null;
    chartStore.inputForm.createdAt = getCurrentDate();
    buttonListStore.useBtnList = ['btnBack', 'btnSave'];
};

onNodesChange(async changes => {
    if (chartStore.inputForm.cmd == 'C') {
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
    if (chartStore.inputForm.cmd == 'C') {
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
import _ from 'lodash';
const isChanged = computed(() => changeFlag.value || !_.isEqual(chartStore.originChartData, JSON.stringify(elements.value)) || chartStore.inputForm.cmd === 'I');

buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSearch', 'btnCopy', 'btnDownload'];
btnBack(() => {
    if (chartStore.inputForm.confirmYn === 'Y') {
        goBack();
    } else if (isChanged.value) confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack, param: '' });
    else goBack();
});

const goBack = () => {
    router.push('/EmergencyControlOrganChart');
};
btnSearch(() => {
    if (isChanged.value) confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction, param: true });
    else searchAction(true);
});
const searchAction = (notify = false) => {
    let param = {
        compId: getCompId(),
        searchText: chartStore.inputForm.chartId
    };
    openLoading();
    getEmergencyOrgChartList(param, notify)
        .then(res => {
            changeFlag.value = false;
            chartStore.inputForm = res.list[0];
            elements.value = JSON.parse(chartStore.inputForm.chartData);
            if (res.list[0].confirmYn === 'Y') chartStore.inputForm.cmd = 'C';
            chartStore.originChartData = _.cloneDeep(res.list[0].chartData);
            param.type = chartStore.inputForm.value.emergencyType;
            getEmergencyRoleList(param).then(res => {
                chartStore.roleList = res.list;
            });
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (!isValid) {
        return;
    }
    confirmMsg('info', t('msgSave'), '', { fun: save, param: true });
});
import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();
const isSave = ref(false);
const save = async notify => {
    openLoading();
    isSave.value = !isSave.value;
    if (!chartStore.inputForm.writeYear) chartStore.inputForm.writeYear = emergencyResponseStore.searchParam.searchText;
    chartStore.inputForm.chartData = JSON.stringify(elements.value);
    chartStore.inputForm.chartBlobData = await getBlobData();
    chartStore.inputForm.orgCount = elements.value.filter(el => el.type == 'default').length;
    let success = chartStore.btnDetailSave(notify);
    if (success) {
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnCopy', 'btnDownload'];
        changeFlag.value = false;
        isSave.value = !isSave.value;
    } else {
        isSave.value = !isSave.value;
    }
};

const vfStyle = ref({ backgroundColor: '#ffffff', height: '100%' });
const vfZoom = ref(2);

import { useOrganizationStore } from '@/stores/system/base/organization.js';
const orgManageStore = useOrganizationStore();
import { useEmerCtrlOrgChartStore } from '@/stores/safewiz/impl/emerCtrlOrgChart.js';
const chartStore = useEmerCtrlOrgChartStore();

const elements = ref([]);
onMounted(async () => {
    // window.addEventListener('keydown', handleKeyDown);
    // window.addEventListener('keyup', handleKeyUp);
    const { chartId, confirmed } = history.state;
    if (chartId) {
        const res = await chartStore.btnDetail(chartId, confirmed);
        if (res) {
            elements.value = JSON.parse(chartStore.inputForm.chartData);
        }
    } else if (chartStore.inputForm.cmd == 'I') {
        buttonListStore.useBtnList = ['btnBack', 'btnSave'];
        elements.value = [];
    } else {
        router.push('/EmergencyControlOrganChart');
    }
});
// const isShiftPressed = ref(false);
// onBeforeUnmount(() => {
//     window.removeEventListener('keydown', handleKeyDown);
//     window.removeEventListener('keyup', handleKeyUp);
// });
// // 키보드 이벤트로 shift 감지
// function handleKeyDown(e) {
//     if (e.key === 'Shift') {
//         isShiftPressed.value = true;
//     }
// }

// function handleKeyUp(e) {
//     if (e.key === 'Shift') {
//         isShiftPressed.value = false;
//     }
// }

const onDragOver = event => {
    event.preventDefault();
    if (event.dataTransfer) {
        event.dataTransfer.dropEffect = 'move';
    }
};
onConnect(params => {
    // params.type = isShiftPressed.value ? 'straight' : 'step';
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
                id: data.orgnId,
                position: {
                    x: 100,
                    y: 50
                },
                label: `${data.orgnNm}`,
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
        data = JSON.parse(event.dataTransfer?.getData('application/emergencyRole'));
        const isDuplicated = elements.value.some(el => el.id === 'role' + data.roleId);
        if (isDuplicated) {
            alertMsg('err', '이미 존재하는 역할입니다.');
            return;
        }
        newNode = {
            id: 'role' + data.roleId,
            position,
            type: 'resizable',
            label: `${data.roleNm}`,
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
</script>
<style>
.node-wrapper {
    pointer-events: none; /* 전체 노드가 마우스 이벤트 받지 않음 */
}

.vue-flow__handle {
    pointer-events: auto !important; /* 핸들만 마우스 이벤트 허용 */
}
</style>
