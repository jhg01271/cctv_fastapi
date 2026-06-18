<template>
    <div class="contents df fdc">
        <div class="box form ui" id="riskForm">
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
                    <div class="grid12-2 us-grid12-12">
                        <div class="field">
                            <label for required>
                                <span>차수</span>
                            </label>
                            <input v-model="riskOrgManageStore.inputData.chartId" v-input type="text" class="w100p radius" readonly placeholder="저장 시 자동으로 채번" />
                        </div>
                    </div>
                    <div class="grid12-2 us-grid12-12">
                        <div class="field">
                            <label for="chart_name" required>
                                <span>차수명</span>
                            </label>
                            <!-- CMD 상태가 C(확정)이면 readonly, chartName 노출
                                 CMD 상태가 U(미확정)이면 chartName 노출, 상태가 I(신규)이면 placeholder 노출 -->
                            <input id="chart_name" v-model="chartName" v-input type="text" class="w100p radius" :placeholder="riskOrgManageStore.cmd === 'C' ? '' : '차수명을 입력하세요 ex) ESG 사업부 위험성평가 조직도'" :readonly="riskOrgManageStore.cmd === 'C'" required />
                        </div>
                    </div>
                    <div class="grid12-3 us-grid12-12">
                        <div class="field">
                            <label for="orgnItem" required>
                                <span>조직</span>
                            </label>
                            <i-chips :chips="riskOrgManageStore.orgnItem" readonly-type="chips" @popup="addOrgn" :readonly="riskOrgManageStore.cmd === 'C'" required></i-chips>
                        </div>
                    </div>
                    <div class="grid12-2 us-grid12-12">
                        <div class="field">
                            <label for="email">확정일자</label>
                            <input :value="formatDate(riskOrgManageStore.inputData.confirmDt)" type="text" class="w100p radius datepicker" readonly />
                        </div>
                    </div>
                    <div class="grid12-2 us-grid12-8">
                        <div class="field">
                            <label for="email">등록일자</label>
                            <input :value="formatDate(riskOrgManageStore.inputData.createdAt)" type="text" class="w100p radius datepicker" readonly />
                        </div>
                    </div>
                    <div class="grid12-1 us-grid12-4">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input :disabled="riskOrgManageStore.cmd === 'C'" :true-value="'Y'" :false-value="'N'" :checked="useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" v-model="useYn" />
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 조직도 VueFlow 시작 -->
                <h2 class="mt2-2rem">조직도</h2>
                <div class="dndflow pr box" @drop="onDrop">
                    <div class="pa r2rem t2rem zi3 us-r0 us-neg-t5-5rem" v-if="riskOrgManageStore.cmd != 'C'">
                        <button type="button" class="btn radius active w18rem" v-button @click="openRiskOrgPopup">
                            <span>위험성평가 조직 역할 관리</span>
                        </button>
                    </div>
                    <!-- C(확정차수인경우)가 아닌 경우 노출 -->
                    <sidebar v-if="riskOrgManageStore?.cmd != 'C'" />
                    <VueFlow v-model="elements" class="customnodeflow" :default-zoom="vfZoom" @dragover="onDragOver" edge-types="step" :nodesDraggable="riskOrgManageStore?.cmd != 'C'" fit-view-on-init :apply-default="false" delete-key-code="Delete">
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
                        <!-- 격자 표시 -->
                        <!-- <Background v-if="!isSave" variant="lines" gap="25" size="1" /> -->
                    </VueFlow>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>

    <teleport to="body">
        <!-- 조직 모달 팝업 컴포넌트 시작  -->
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :inputForm="riskOrgManageStore" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                <!-- 버튼 콤포넌트 영역 시작 -->
            </div>
        </i-PopupDialog>
        <!-- 모달 팝업 콤포넌트 끝  -->

        <i-PopupDialog ref="riskOrgPopup">
            <div class="contents form ui w70rem md-w100p">
                <DataSetManagePopup :title="'위험성평가 조직 역할 관리'" :subTitle="'위험성평가 조직 역할'" :datasetYn="'Y'" id="orgnRoleId" nm="orgnRoleNm" :popupDataList="riskOrgList" :cardComponent="remarkPopupComponent" @filter="filterRiskOrgPopup" @search="searchRiskOrgPopup" @sample="sampleRiskOrgPopup" @add="addRiskOrgPopup" @save="saveRiskOrgPopup" @delete="deleteRiskOrgPopup" @close="closeRiskOrgPopup"> </DataSetManagePopup>
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="riskOrgSamplePopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'위험성평가 조직 예시'" filterKey="orgnRoleNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import remarkPopupComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import { ref, onMounted, nextTick, watch, readonly } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { NodeResizer } from '@vue-flow/node-resizer';
import { Position, VueFlow, useVueFlow, Handle } from '@vue-flow/core';
import { saveRiskAssessmentOrganChart, getRiskAssessmentOrganChartDetail, getRiskAssessmentOrganChartReport, getRiskOrgList, getDataSetRiskOrgList, saveRiskOrgList } from '@/stores/safewiz/planning/api/riskAssessmentOrganChartApi';
// capture
import { useScreenshot } from '@/components/base/useScreenshot';
const { capture } = useScreenshot();

// Button List
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload'];

// Function Store
import { useRiskAssessmentOrganStore } from '@/stores/safewiz/planning/riskAssessmentOrganChart.js';
const riskOrgManageStore = useRiskAssessmentOrganStore();

// BaseView
import BaseView from '@/components/base/BaseView';
const { t, openLoading, endLoading, router, btnBack, btnSearch, btnSave, btnDownload, btnCopy, alertMsg, confirmMsg, getCompId, validationStore, formatDate, baseDownload, getDuplicatedData } = BaseView();

const orgnPopup = ref(null);
const riskAssessmentRoles = ref(null);
const elements = ref([]);
const chartName = ref(riskOrgManageStore.inputData.chartNm);
let rows = ref([]);

// 조직 삭제 시 전체인원 검색
watch(
    () => riskOrgManageStore.orgnItem,
    (newItem, oldItem) => {
        if (newItem.length === 0 && oldItem.length === 0) {
            riskOrgManageStore.orgnItem = [{}];
            riskOrgManageStore.memberList.value = [];
            riskOrgManageStore.getMemberList({ compId: getCompId() });
        }
    },
    { deep: true }
);

onMounted(() => {
    riskOrgManageStore.getRiskAssessmentRole();
    // U(미확정차수)일 때
    if (riskOrgManageStore.cmd == 'U') {
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload', 'btnCopy'];
        riskOrgManageStore.getOrgnList(false);
        elements.value = JSON.parse(riskOrgManageStore.inputData.chartData);
        useYn.value = riskOrgManageStore.inputData.useYn;
    }
    // C(확정차수)일 때
    else if (riskOrgManageStore.cmd == 'C') {
        // 확정 차수 일 때 조직 노출
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnCopy'];
        elements.value = JSON.parse(riskOrgManageStore.inputData.chartData);
        useYn.value = riskOrgManageStore.inputData.useYn;
    }
    // I(신규생성)일 때
    else if (riskOrgManageStore.cmd == 'I') {
        buttonListStore.useBtnList = ['btnBack', 'btnSave'];
        riskOrgManageStore.getOrgnList(false);
        riskOrgManageStore.inputData.createdAt = null;
        elements.value = [];
        // 생성 > 뒤로가기 > 생성 시 조직 데이터 남아 있어서 초기화
        riskOrgManageStore.orgnItem = [];
        useYn.value = 'Y';
    }
    // 그 외 목록으로 이동
    else {
        router.push('RiskAssessmentOrganChart');
    }
    riskOrgManageStore.chartSearchTerm = '';
});

/***************************************************
 * 위험성평가 조직 역할 구성 팝업
 ***************************************************/
const roleParam = ref('');
let rowList = ref([]);

// 조직 검색 팝업 노출
const addOrgn = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    orgnPopup.value.onOpen();
};

// 조직 팝업 선택 / 닫기
const closeOrgn = e => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    // 데이터 있을때만 조건 실행
    if (e && e.length) {
        // 기존 데이터가 빈값일땐 바로 설정

        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm,
            orgnId: el.orgnId,
            orgnNm: el.orgnNm
        }));
        riskOrgManageStore.orgnItem = refinedItems;
    }
    orgnPopup.value.onClose();
};

const selectOrgn = (e, isReset = true) => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    orgnPopup.value.onClose();
    if (e && e.length) {
        // multi 일때
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm,
            orgnId: el.orgnId,
            orgnNm: el.orgnNm
        }));
        // 조직 재조회
        riskOrgManageStore.getMemberList({ compId: getCompId(), orgnId: e[0].orgnId });
        riskOrgManageStore.orgnItem = refinedItems;
    }
    // 조직도 초기화
    if (isReset) {
        $reset();
    }
};
/***************************************************
 * 조직도 관련
 ***************************************************/
import sidebar from '@/views/safewiz/planning/RiskAssessmentOrganChartSidebar.vue';
import { Background, Controls, MiniMap } from '@vue-flow/additional-components';
import _ from 'lodash';
import { now } from 'lodash';

const vfZoom = ref(2);
const confirmYn = ref(riskOrgManageStore.inputData.confirmYn === 'Y' ? true : false);
const useYn = ref('Y');
const nodes = ref([]);
const { screenToFlowCoordinate, findNode, onConnect, addEdges, addNodes, removeNodes, project, vueFlowRef, onNodesChange, onEdgesChange, applyNodeChanges, applyEdgeChanges, $reset, fitView } = useVueFlow();

onNodesChange(async changes => {
    // C(확정차수)인 경우 return
    if (riskOrgManageStore.cmd == 'C') {
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

// node 삭제
const nodeDeleteAction = async changes => {
    // nodes에서도 삭제
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

onEdgesChange(async changes => {
    // C(확정 차수)인 경우 return
    if (riskOrgManageStore.cmd == 'C') {
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

// edge 삭제
const edgeDeleteAction = async changes => {
    await applyEdgeChanges(changes);
};

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
                id: data.id + Date.now(),
                position: {
                    x: 100,
                    y: 50
                },
                label: `${data.name}`,
                sourcePosition: Position.Top,
                targetPosition: Position.Bottom,
                parentNode: parentNode?.id,
                extent: 'parent'
            };
        } else {
            alertMsg('err', '역할 카드 안에 넣어주세요.');
            return;
            // newNode = {
            //     id: data.id,
            //     position,
            //     label: `${data.name}`,
            //     sourcePosition: Position.Top,
            //     targetPosition: Position.Bottom
            // };
        }
    } else {
        data = JSON.parse(event.dataTransfer?.getData('application/riskAssessmentOrgn'));
        console.log(data);
        // const isDuplicated = elements.value.some(el => el.id === 'role' + data.id);
        // if (isDuplicated) {
        //     alertMsg('err', '이미 존재하는 역할입니다.');
        //     return;
        // }
        newNode = {
            id: 'role' + data.id + '_' + Date.now(),
            position,
            type: 'resizable',
            label: `${data.role}`,
            style: { width: '200px', height: '145px' }
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

/***************************************************
 * 우측 버튼 List
 ***************************************************/
// 목록 버튼 클릭 이벤트
btnBack(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    router.go(-1);
});
// 조회 버튼 클릭 이벤트
btnSearch(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (riskOrgManageStore.cmd != 'I') {
        searchDetail(riskOrgManageStore.inputData.chartId);
    }
});

// 상세조회
const searchDetail = (chartId, notify = true) => {
    getRiskAssessmentOrganChartDetail(chartId, notify).then(res => {
        riskOrgManageStore.inputData = res.list;
        elements.value = JSON.parse(res.list.chartData); // 차트 갱신
        riskOrgManageStore.cmd = res.list.confirmYn == 'Y' ? 'C' : 'U';
        chartName.value = res.list.chartNm;
        useYn.value = res.list.useYn == 'Y' ? 'Y' : 'N';
        confirmYn.value = res.list.confirmYn == 'Y' ? true : false;
        riskOrgManageStore.orgnItem = [{ id: res.list.orgnId, name: res.list.orgnNm, orgnId: res.list.orgnId, orgnNm: res.list.orgnNm }];
    });
};

const isSave = ref(false);
// 저장 버튼 클릭 이벤트
btnSave(async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    // validation 체크
    const isValid = await validationStore.validateAllFields('riskForm', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
        return;
    }
});

const saveAction = async () => {
    // insert, update 시 필요 파라미터
    isSave.value = !isSave.value;
    openLoading();
    let param = {
        cmd: riskOrgManageStore.cmd, // cmd
        orgnId: riskOrgManageStore.orgnItem[0].orgnId, // 조직 ID
        compId: getCompId(), // 사업장 ID
        chartNm: chartName.value, // 차트 명
        chartData: JSON.stringify(elements.value), // 조직도
        memberCount: elements.value.filter(el => el.type == 'default').length, // 인원 수
        useYn: useYn.value === 'Y' ? 'Y' : 'N', // 사용여부
        confirmYn: confirmYn.value ? 'Y' : 'N', // 확정여부
        chartBlobData: await getBlobData()
    };
    // CMD 타입이 U(미확정 차수인 경우)인 경우 UPDATE
    if (riskOrgManageStore.cmd === 'U') {
        param.chartId = riskOrgManageStore.inputData.chartId;
        saveRiskAssessmentOrganChart(param, true)
            .then(res => {
                searchDetail(res.result.chartId, false);
            })
            .catch(() => {
                isSave.value = !isSave.value;
                endLoading();
            })
            .finally(() => {
                isSave.value = !isSave.value;
                endLoading();
            });
    }
    // CMD 타입이 I(신규생성인 경우)인 경우 INSERT
    else if (riskOrgManageStore.cmd === 'I') {
        saveRiskAssessmentOrganChart(param, true)
            .then(res => {
                searchDetail(res.result.chartId, false);
            })
            .catch(() => {
                isSave.value = !isSave.value;
                endLoading();
            })
            .finally(() => {
                isSave.value = !isSave.value;
                buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload', 'btnCopy'];
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
// 출력 버튼 클릭 이벤트
btnDownload(async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction });
});

const isDownload = ref(false); // 미니맵 가시화 여부 (출력 시에는 미니맵이 감춰질 예정)
const downloadAction = async () => {
    openLoading();
    let checkedObjList = [{ searchText: riskOrgManageStore.inputData.chartId }];
    baseDownload(getRiskAssessmentOrganChartReport, { checkedObjList: checkedObjList });

    // console.log('@vueFlowRef.value,', vueFlowRef.value);
    // isDownload.value = !isDownload.value; // 미니맵 숨기기
    // await fitView({
    //     padding: 0.2, // 노드와 뷰포트 간 여백 비율
    //     includeHiddenNodes: true, // 숨겨진 노드 포함 여부
    //     duration: 100 // 뷰포트 이동 애니메이션 시간
    // }); // 조직도 뷰포트 맞추기
    // openLoading();
    // await nextTick();
    // await capture(vueFlowRef.value, { shouldDownload: true, fileName: `위험성평가 조직도_${riskOrgManageStore.inputData.chartNm}` });
    // isDownload.value = !isDownload.value;
    // endLoading();
};
// 복사 버튼 클릭 이벤트
btnCopy(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    confirmMsg('info', '현재 조직도를 복사하여 신규 작성하시겠습니까?', null, { fun: copyAction });
});

const copyAction = () => {
    riskOrgManageStore.cmd = 'I';
    riskOrgManageStore.inputData.chartId = '';
    riskOrgManageStore.inputData.createdAt = '';
    riskOrgManageStore.inputData.updatedAt = '';
    riskOrgManageStore.inputData.chartNm = '';
    chartName.value = '';
    useYn.value = 'Y';
    riskOrgManageStore.inputData.useYn = 'Y';
    confirmYn.value = '';

    buttonListStore.useBtnList = ['btnBack', 'btnSave'];
};

//-----------------------------------------------------------
//-- 위험성평가 조직 역할
//-----------------------------------------------------------
const riskOrgPopup = ref();
const riskOrgSamplePopup = ref();
const currentRiskOrgList = ref();
const riskOrgList = ref([]);

// --------------- 데이터 세팅
const initRiskOrgList = () => {
    getRiskOrgList({ compId: getCompId() }, true).then(async res => {
        riskOrgManageStore.roleList = [];
        await res.list.forEach(val => {
            if (val.useYn == 'Y') riskOrgManageStore.roleList.push({ id: val.orgnRoleId, cmd: 'U', selected: false, order: val.ordSeq, role: val.orgnRoleNm, used: val.useYn == 'Y' ? true : false });
        });
        riskOrgList.value = res.list;
        currentRiskOrgList.value = _.cloneDeep(res.list);
    });
};

// --------------- 검색
const filterRiskOrgPopup = text => {
    riskOrgList.value = currentRiskOrgList.value;
    const filteredData = riskOrgList.value.filter(el => el.orgnRoleNm.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);
    riskOrgList.value = filteredData;
};

// --------------- 조회
const searchRiskOrgPopup = () => {
    if (JSON.stringify(currentRiskOrgList.value) !== JSON.stringify(riskOrgList.value)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: searchActionRiskOrgPopup, param: {} });
    } else {
        searchActionRiskOrgPopup();
    }
};

const searchActionRiskOrgPopup = () => {
    openLoading();
    initRiskOrgList();
    endLoading();
};

// --------------- 예시 불러오기
const sampleRiskOrgPopup = () => {
    riskOrgSamplePopup.value.onOpen();
};

// --------------- 추가
const addRiskOrgPopup = () => {
    const newData = {
        orgnRoleId: '',
        orgnRoleNm: '',
        ordSeq: '',
        useYn: 'Y',
        cmd: 'I',
        checked: true
    };
    riskOrgList.value.push(newData);

    // 새로 추가된 항목으로 포커스 이동
    const index = riskOrgList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// --------------- 저장, 수정
const saveRiskOrgPopup = async () => {
    const checkedList = riskOrgList.value.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionRiskOrgPopup, param: true });
    }
};

const saveActionRiskOrgPopup = async () => {
    openLoading();
    const checkedList = riskOrgList.value.filter(val => val.checked == true);
    await checkedList.forEach(val => {
        val.compId = getCompId(); //dataSet의 경우 comp_id가 존재하지 않기 때문에 본인의 comp_id로 수정
    });
    saveRiskOrgList(checkedList, true)
        .then(res => {
            if (res.success) {
                endLoading();
                initRiskOrgList();
            }
        })
        .catch(err => {
            endLoading();
        });
};

// --------------- 삭제
const deleteRiskOrgPopup = () => {
    const checkedList = riskOrgList.value.filter(val => val.checked == true);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionRiskOrgPopup, param: true });
    } else {
        alertMsg('error', '선택된 항목이 없습니다.');
    }
};

const deleteActionRiskOrgPopup = async () => {
    openLoading();
    const checkedList = riskOrgList.value.filter(val => val.checked == true);
    await checkedList.forEach(val => {
        val.useYn = 'N';
    });
    saveRiskOrgList(checkedList, true)
        .then(res => {
            if (res.success) {
                endLoading();
                initRiskOrgList();
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
const openRiskOrgPopup = async () => {
    openLoading();
    await initRiskOrgList();
    riskOrgPopup.value.onOpen();
    endLoading();
};

// --------------- 팝업 닫기
const closeRiskOrgPopup = () => {
    if (JSON.stringify(currentRiskOrgList.value) !== JSON.stringify(riskOrgList.value)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionRiskOrgPopup, param: {} });
    } else {
        closeActionRiskOrgPopup();
    }
};

const closeActionRiskOrgPopup = () => {
    riskOrgList.value = [];
    riskOrgPopup.value.onClose();
};

const getSampleDataSetListFunc = async () => {
    openLoading();
    let resData = { list: [] };
    await getDataSetRiskOrgList({}, true)
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
    const filteredData = getDuplicatedData(riskOrgList.value, selectItems, 'orgnRoleId', 'orgnRoleNm'); // 중복된 항목 추출
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
        riskOrgList.value.push(val);
    });
    riskOrgSamplePopup.value.onClose();

    index = riskOrgList.value.length - 1;
    setTimeout(() => {
        element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const closeSamplDataSetMngPopup = () => {
    riskOrgSamplePopup.value.onClose();
};
</script>
