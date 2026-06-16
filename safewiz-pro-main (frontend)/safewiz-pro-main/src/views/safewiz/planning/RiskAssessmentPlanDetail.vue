<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="riskAssessmentPlanStore && riskAssessmentPlanStore.inputForm" class="contents df fdc">
        <div id="form" class="box form ui">
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
                <!-- 서명 -->
                <div class="pa2-2rem" :key="resetSignature">
                    <ISignature class="mb0" ref="signatureComponent" :cmd="riskAssessmentPlanStore.inputForm.cmd" targetType="RAP" :writeYear="riskAssessmentPlanStore.inputForm.writeYear" :docNo="riskAssessmentPlanStore.inputForm.docNo" :useYn="riskAssessmentPlanStore.inputForm.useYn" />
                </div>
                <hr />

                <!-- 상세 -->
                <div class="pa2-2rem">
                    <!-- 폼 -->
                    <div class="control-field ui form">
                        <div class="row flex gutters1rem">
                            <div class="grid12-2 sm-grid12-6 es-grid12-8">
                                <div class="field">
                                    <label for="writeYear" required="required">
                                        <span>{{ '작성년도' }}</span>
                                    </label>
                                    <input v-model="riskAssessmentPlanStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label title="위험성평가 계획명" for="planNm" required>
                                        <span>{{ t('riskAssessmentPlan_name') }}</span>
                                    </label>
                                    <input v-input type="text" class="w100p radius" id="planNm" v-model="riskAssessmentPlanStore.inputForm.planNm" required maxlength="100" />
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label title="조직" for="orgnId" required>
                                        <span>{{ t('organizational') }}</span>
                                    </label>
                                    <i-chips :chips="[{ id: riskAssessmentPlanStore.orgnItem?.id, nm: riskAssessmentPlanStore.orgnItem?.name }]" @popup="openOrgn" @removeChip="removeOrgn" required />
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6 es-grid12-4">
                                <div class="field">
                                    <label title="사용여부" for="useYn">{{ t('useYn') }}</label>
                                    <div class="df aic h4-4rem">
                                        <input title="사용여부" v-input="'사용'" type="checkbox" class="df switch" :checked="riskAssessmentPlanStore.inputForm.useYn" v-model="riskAssessmentPlanStore.inputForm.useYn" true-value="Y" false-value="N" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label title="위험성평가 조직도" for="riskAssessmentChartId" required>
                                        <span>{{ t('riskAssessment_orgnChart') }}</span>
                                    </label>
                                    <i-chips :chips="[{ id: riskAssessmentPlanStore.riskAssessmentChartItem?.id, nm: riskAssessmentPlanStore.riskAssessmentChartItem?.name }]" @popup="openRiskAssessmentChart" @removeChip="removeRiskAssessmentChart" required />
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label title="위험성평가 기간" for="startDate" required>
                                        <span>{{ t('riskAssessment_date') }}</span>
                                    </label>
                                    <div class="df aic">
                                        <input title="위험성평가 시작기간" v-input type="text" class="datepicker w100p radius mr1rem" v-calendar="getDateFormat()" id="startDate" v-model="riskAssessmentPlanStore.inputForm.startDate" maxlength="10" required />
                                        &#126;
                                        <input title="위험성평가 종료기간" v-input type="text" :min="riskAssessmentPlanStore.inputForm.startDate" class="datepicker w100p radius ml1rem" v-calendar="getDateFormat()" id="endDate" v-model="riskAssessmentPlanStore.inputForm.endDate" maxlength="10" required />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label title="위험성평가 일자" for="assessmentDate">{{ t('assessment_date') }}</label>
                                    <input title="위험성평가 일자" v-input type="text" class="datepicker w100p radius" v-calendar="getDateFormat()" id="assessmentDate" v-model="riskAssessmentPlanStore.inputForm.assessmentDate" maxlength="10" />
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label title="위험성평가 완료일자" for="assessmentComplDate">
                                        {{ t('assessment_compl_date') }}
                                    </label>
                                    <input title="위험성평가 완료일자" v-input type="text" class="datepicker w100p radius" v-calendar="getDateFormat()" id="assessmentComplDate" v-model="riskAssessmentPlanStore.inputForm.assessmentComplDate" maxlength="10" />
                                </div>
                            </div>

                            <div class="grid12-2 sm-grid12-4 es-grid12-12">
                                <div class="field" :key="riskAssessmentPlanStore.inputForm.riskAssessmentGubun">
                                    <label title="위험성평가 구분" for="riskAssessmentGubun" required>
                                        <span>{{ t('riskAssessment_gubun') }}</span>
                                    </label>
                                    <select title="위험성평가 구분" id="riskAssessmentGubun" v-select class="radius" v-model="riskAssessmentPlanStore.inputForm.riskAssessmentGubun" required>
                                        <option v-for="item in riskAssessmentPlanStore.riskAssessmentGubunList" :key="item.minorCd" :value="item.minorCd">
                                            {{ item.minorNm }}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-4 es-grid12-6">
                                <div class="field" :key="riskAssessmentPlanStore.inputForm.riskAssessmentStandards">
                                    <label title="위험성 추정기준" for="riskAssessmentStandards" required>
                                        <span>{{ t('riskAssessment_standards') }}</span>
                                    </label>
                                    <select title="위험성 추정기준" id="riskAssessmentStandards" v-select class="radius" v-model="riskAssessmentPlanStore.inputForm.riskAssessmentStandards" required @change="changeStandard">
                                        <option v-for="item in riskAssessmentPlanStore.riskAssessmentStandardsList" :key="item.minorCd" :value="item.minorCd">
                                            {{ item.minorNm }}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-4 es-grid12-6">
                                <div class="field" :key="riskAssessmentPlanStore.inputForm.riskAllowanceStandards">
                                    <label title="허용 가능한 위험성 기준" for="riskAllowanceStandards" required>
                                        <span>허용 가능한 위험성 기준</span>
                                    </label>
                                    <select title="허용 가능한 위험성 기준" id="riskAllowanceStandards" v-select class="radius" v-model="riskAssessmentPlanStore.inputForm.riskAllowanceStandards" required>
                                        <option v-for="item in riskAssessmentPlanStore.riskAllowanceStandardsList" :key="item.minorCd" :value="item.minorCd">
                                            {{ item.minorNm }}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6">
                                <button type="button" class="btn w100p radius active mt4-4rem sm-mt0" @click="riskAssessmentInfoPopupOpen(idx)" :disabled="riskAssessmentPlanStore.inputForm.riskAssessmentGubun !== 'regular' || !riskAssessmentPlanStore.orgnItem.id || riskAssessmentPlanStore.inputForm.cmd !== 'U'">
                                    <span>이전 위험성평가 정보 선택</span>
                                </button>
                            </div>
                            <div class="grid12-2 sm-grid12-6">
                                <button type="button" class="btn w100p radius active mt4-4rem sm-mt0" @click="btnGoImplement" :disabled="!riskAssessmentPlanStore.orgnItem?.id || riskAssessmentPlanStore.inputForm.cmd !== 'U'">
                                    <span>이행 화면으로 이동</span>
                                </button>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label title="설명" for="desc">{{ t('desc') }}</label>
                                    <textarea title="설명" name="desc" id="desc" class="minh10rem radius" placeholder="설명을 입력하세요." v-model="riskAssessmentPlanStore.inputForm.desc"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 이미지 & 테이블 -->
                    <div class="mt1-2rem">
                        <div class="row flex gutters2rem">
                            <div class="grid12-6 sm-grid12-12">
                                <div class="dndflow pr">
                                    <VueFlow
                                        v-model="elements"
                                        class="customnodeflow"
                                        :style="vfStyle"
                                        zoom="1"
                                        :default-viewport="{
                                            x: -100,
                                            y: 0,
                                            zoom: 0.5
                                        }"
                                        :nodesDraggable="false"
                                        fit-view-on-init
                                    >
                                        <Background variant="lines" gap="5" size="1.2" />
                                    </VueFlow>
                                </div>
                            </div>
                            <div class="grid12-6 sm-grid12-12 wbka">
                                <table class="tac">
                                    <thead>
                                        <tr>
                                            <th colspan="3">유해위험요인 감소대책 등록 현황</th>
                                        </tr>
                                        <tr>
                                            <th class="w35p">감소대책 등록 요인수</th>
                                            <th class="w30p">유해요인 건수</th>
                                            <th class="w35p">감소대책 등록률(%)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.reductionCount || 0 }}</td>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.implCount || 0 }}</td>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.implPercentage || 0 }}</td>
                                        </tr>
                                    </tbody>
                                </table>

                                <table class="tac mt1-2rem">
                                    <thead>
                                        <tr>
                                            <th colspan="3">감소대책 이행 현황</th>
                                        </tr>
                                        <tr>
                                            <th class="w35p">개선조치 건수</th>
                                            <th class="w30p">감소대책 건수</th>
                                            <th class="w35p">개선 이행률(%)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.reductionCompletedCount || 0 }}</td>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.reductionAllCount || 0 }}</td>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.reductionPercentage || 0 }}</td>
                                        </tr>
                                    </tbody>
                                </table>

                                <table class="tac mt1-2rem">
                                    <thead>
                                        <tr>
                                            <th colspan="2">평균 위험도</th>
                                        </tr>
                                        <tr>
                                            <th class="w50p">개선 전</th>
                                            <th>개선 후</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.prevScore || '-' }}</td>
                                            <td>{{ riskAssessmentPlanStore?.riskAvgCount?.afterScore || '-' }}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
    <teleport to="body">
        <!-- 조직 팝업 -->
        <i-PopupDialog ref="orgnPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" filterKey="orgnNm" useYnKey="useYn" excludedValue="N" :single="true" :fetch-data="getRiskAssessmentPlanOrgnPopupListApi" :fetch-param="{ compId: getCompId(), writeYear: riskAssessmentPlanStore.searchParam.searchText }" uniqueKey="orgnId" :selectedIdList="[riskAssessmentPlanStore.orgnItem]" @close="closeOrgn" />
            </div>
        </i-PopupDialog>

        <!-- 위험성평가 조직도 팝업 -->
        <i-PopupDialog ref="riskAssessmentChartPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup title="위험성평가 조직도" filterKey="chartNm" useYnKey="confirmYn" excludedValue="N" :single="true" :fetch-data="getRiskAssessmentOrganChartList" uniqueKey="chartId" :selectedIdList="[riskAssessmentPlanStore.riskAssessmentChartItem]" @close="closeRiskAssessmentChart" />
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="workerCheckPopup" class>
            <div class="contents w1000px md-w100p">
                <h3>위험성평가 근로자 확인</h3>
                <div class="form ui tar mt2-5rem">
                    <div class="grid12-12">
                        <div class="field">
                            <div class="accordion br4px">
                                <div class="list">
                                    <button class="df jcsb aic" id="signAccordion" @click="toggleAccordion">
                                        <div class="df">
                                            <em class="w100p wbka ellipsis">{{ t('근로자 확인') }}</em>
                                        </div>
                                        <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="segment bcf8f9fb">
                                        <div class="pa1rem">
                                            <!-- 검색 창 -->
                                            <div class="control-field mb8px">
                                                <div class="df bcffffff">
                                                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="signSearchTerm" @keyup.enter="applySignFilter" />
                                                    <button type="submit" class="shrink0" @click="applySignFilter">
                                                        <img src="/assets/img/common/icon_search.svg" alt />
                                                    </button>
                                                </div>
                                            </div>
                                            <i-hr-chips-sign type="worker" ref="workerCheck" :cmd="riskAssessmentPlanStore.inputForm.cmd" targetType="RAP" :writeYear="riskAssessmentPlanStore.inputForm.writeYear" :docNo="riskAssessmentPlanStore.inputForm.docNo" :signOnly="true" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey mt2-5rem" @click="() => workerCheckPopup.onClose()">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </i-PopupDialog>

        <i-PopupDialog ref="riskAssessmentInfoPopup">
            <div class="contents minw1000px form ui md-minw100p">
                <RiskAssessmentInfoPopup :title="'위험성평가 정보'" :single="false" :implList="implList" :orgnItem="riskAssessmentPlanStore.orgnItem" :searchText="searchText" :showSelectedBtn="true" :showPrevBtn="true" :riskPlanPrevList="riskPlanPrevList" :potentialRiskIndex="potentialRiskIndex" :selectedRiskInfo="selectedRiskInfo" :getCompletedCnt="getCompletedCnt" :referenceRiskImplList="referenceData" :riskPlanData="riskAssessmentPlanStore.inputForm" @filter="searchFilterData" @select="saveRiskassessmentPrevInfo" @close="riskAssessmentInfoPopupClose" @change="onPlanChange" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import ISignature from '@/components/common/iSignature.vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, nextTick, endLoading, setRouterParam, ref, t, watch, onMounted, validationStore, confirmMsg, formatDate, router, btnBack, btnSearch, btnAdd, btnSave, btnDelete, alertMsg, btnWorkerCheck, getCompId, toastPopup, goRouter, getFormattedDate } = BaseView();
const layoutStore = useButtonListStore();
import { gsap } from 'gsap';
import { useButtonListStore } from '@/stores/buttonList.js';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { getRiskAssessmentOrganChartList } from '@/stores/safewiz/planning/api/riskAssessmentOrganChartApi.js';
import { getRiskAssessmentPlanOrgnPopupListApi } from '@/stores/safewiz/planning/api/riskAssessmentPlanApi';
import { getRiskAssessmentDetailPopupAll, saveMainRiskImpl, getReferenceRiskImplListApi, deleteWorkerApprovalInfoAll } from '@/stores/safewiz/planning/api/implementationOfRiskAseessmentApi';
import { getHr } from '@/stores/system/base/api/hrApi';
import { getPartner } from '@/stores/system/base/api/partnerApi';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import RiskAssessmentInfoPopup from './popup/RiskAssessmentInfoPopup.vue';
import { VueFlow } from '@vue-flow/core';
import { Background } from '@vue-flow/additional-components';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import _ from 'lodash';
import { useRiskAssessmentPlanStore } from '@/stores/safewiz/planning/riskAssessmentPlan.js';
const riskAssessmentPlanStore = useRiskAssessmentPlanStore();
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
import { getDateFormat } from '@/utils/dateUtil.js';
const riskImplStore = useRiskAssessmentImplementation();

// signatureComponent
const signatureComponent = ref();
const resetSignature = ref(0);

const workerCheck = ref(null);

// vueFlow
const elements = ref([]);
const vfStyle = ref({ backgroundColor: '#ffffff', height: '100%' });

// RiskAssessmentInfoPopup 관련 변수들 추가
const riskAssessmentInfoPopup = ref(); // 팝업
const implList = ref([]); // 팝업 출력용
const currentImplList = ref(null); // 원본 데이터 보관용
const searchText = ref(null); // 팝업 검색어
const selectedRiskInfo = ref(null); // 선택한 팝업 데이터
const potentialRiskIndex = ref(null); // 이행 인덱스
const riskPlanPrevList = ref([]); // 선택된 이전 계획 리스트
const referenceData = ref(null);

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    riskAssessmentPlanStore.riskAllowanceStandardsList = [];
    if (param) {
        riskAssessmentPlanStore.inputForm = null;
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await riskAssessmentPlanStore.getRiskAssessmentPlanDetail(param, false);
        layoutStore.useBtnList.push('btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnWorkerCheck');
    } else if (!riskAssessmentPlanStore.inputForm || Object.keys(riskAssessmentPlanStore.inputForm).length === 0) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        sessionStorage.setItem('riskAssessmentTab', 'plan');
        router.push({
            path: '/RiskAssessmentTable',
            state: { activeTab: 'plan' }
        });
        return;
    } else {
        // 추가버튼으로 왔을 때
        riskAssessmentPlanStore.initInputForm();

        layoutStore.useBtnList.push('btnBack', 'btnSave');
    }
    if (riskAssessmentPlanStore.inputForm.chartData) {
        elements.value = riskAssessmentPlanStore.riskAssessmentChartItem.chartData ? JSON.parse(riskAssessmentPlanStore.riskAssessmentChartItem.chartData) : [];
    }

    await riskAssessmentPlanStore.initRiskAssessmentSystemCode();
});

/**
 * 조직 팝업
 */

const orgnPopup = ref(null);

// 조직 팝업 열기
const openOrgn = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    orgnPopup.value.onOpen();
};

// 조직 select 삭제 버튼
const removeOrgn = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    // 저장된 조직 삭제
    riskAssessmentPlanStore.initOrgnItem();
};

// 조직 팝업 닫기
const closeOrgn = item => {
    // 조직 추가
    if (item.length > 0) {
        riskAssessmentPlanStore.inputForm.orgnId = item[0].orgnId;
        // chips에 맞게 데이터 조정
        riskAssessmentPlanStore.orgnItem = {
            id: item[0].orgnId,
            name: item[0].orgnNm,
            orgnId: item[0].orgnId
        };
    }
    orgnPopup.value.onClose();
};

/**
 * 위험성평가 조직도 팝업
 */

const riskAssessmentChartPopup = ref(null);

// 위험성평가 조직도 팝업 열기
const openRiskAssessmentChart = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    riskAssessmentChartPopup.value.onOpen();
};

// 위험성평가 조직도 select 삭제 버튼
const removeRiskAssessmentChart = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    // 저장된 위험성평가 조직도 삭제
    riskAssessmentPlanStore.initRiskAssessmentChartItem();
};

// 위험성평가 조직도 팝업 닫기
const closeRiskAssessmentChart = item => {
    // 위험성평가 조직도 추가
    if (item.length > 0) {
        riskAssessmentPlanStore.inputForm.riskAssessmentChartId = item[0].chartId;
        // chips에 맞게 데이터 조정
        riskAssessmentPlanStore.riskAssessmentChartItem = {
            id: item[0].chartId,
            name: item[0].chartNm,
            chartData: item[0].chartData,
            chartId: item[0].chartId
        };
    }
    riskAssessmentChartPopup.value.onClose();
};

// 위험성평가 조직도 데이터가 변경될때마다 vueFlow 반영
watch(
    () => riskAssessmentPlanStore.riskAssessmentChartItem,
    newItem => {
        if (newItem.chartData) {
            elements.value = JSON.parse(newItem.chartData);
        } else {
            elements.value = [];
        }
    },
    { deep: true }
);

/**
 * 이전 위험성평가 정보 선택 버튼
 */

// 이전 위험성평가 정보 선택 팝업 호출
const riskAssessmentInfoPopupOpen = async () => {
    openLoading();
    potentialRiskIndex.value = null;

    // 데이터 초기화
    implList.value = [];
    riskPlanPrevList.value = [];
    currentImplList.value = null;
    await loadReferenceData();
    await riskAssessmentInfoPopup.value.onOpen();
    endLoading();
};

// 이전 위험성평가 정보 선택에서 이미 저장한 이행 조회
const loadReferenceData = async () => {
    try {
        const res = await getReferenceRiskImplListApi({
            writeYear: riskAssessmentPlanStore.inputForm.writeYear,
            docNo: riskAssessmentPlanStore.inputForm.docNo,
            compId: getCompId()
        });

        if (res && res.success) {
            // props로 전달할 참조 데이터
            referenceData.value = res.list || [];
        }
    } catch (error) {
        console.error('참조 데이터 조회 실패:', error);
        referenceData.value = [];
    }
};

// 이전 위험성평가 정보 선택 계획 변경 이벤트 핸들러
const onPlanChange = async selectedPlan => {
    openLoading();

    // 위험성 결정 데이터가 없는 경우에만 조회
    if (riskImplStore.frequencyList.length === 0 || riskImplStore.impactList.length === 0 || riskImplStore.riskScoreList.length === 0) {
        await riskImplStore.setSystemCodeList();
    }

    await riskImplStore.filteredSystemCodeList(riskAssessmentPlanStore.inputForm.riskAssessmentStandards);

    try {
        // 모든 데이터 초기화
        implList.value = [];
        currentImplList.value = null;
        selectedRiskInfo.value = null;
        searchText.value = '';

        if (!selectedPlan || !selectedPlan.docNo) {
            // 계획이 선택 해제되거나 없어질 때는 초기화만 하고 종료
            endLoading();
            return;
        }

        // 선택된 계획의 이행 데이터 조회
        const implParam = {
            compId: getCompId(),
            docNo: selectedPlan.docNo,
            writeYear: selectedPlan.writeYear
        };

        const implResult = await getRiskAssessmentDetailPopupAll(implParam, true);
        implResult.list
            .flatMap(main => main.implRiskAssList || [])
            .flatMap(sub => sub.implementReduList)
            .forEach(redu => {
                if (redu.expectedDate !== null) {
                    redu.expectedDate = getFormattedDate(redu.expectedDate);
                }
                if (redu.completedDate !== null) {
                    redu.completedDate = getFormattedDate(redu.completedDate);
                }
            });
        // 새로운 데이터로 교체
        implList.value = implResult || { list: [] };
        currentImplList.value = _.cloneDeep(implResult || { list: [] });

        // 선택된 계획을 riskPlanPrevList에 추가/업데이트
        const existingIndex = riskPlanPrevList.value.findIndex(item => item.docNo === selectedPlan.docNo);
        if (existingIndex >= 0) {
            riskPlanPrevList.value[existingIndex] = selectedPlan;
        } else {
            riskPlanPrevList.value = [selectedPlan]; // 배열 교체
        }

        await setImplList();
    } catch (error) {
        alertMsg('error', '위험성평가 이행 데이터를 조회할 수 없습니다.');

        implList.value = [];
        currentImplList.value = null;
        selectedRiskInfo.value = null;
    } finally {
        endLoading();
    }
};

// 이전 위험성 정보 선택 데이터 적용
const setImplList = async () => {
    if (implList.value !== null && implList.value.list) {
        await implList.value.list.forEach(async data => {
            data.docType = 'RAP';

            let reductionCount = 0;
            let reductionCompletedCount = 0;

            data.implRiskAssList.forEach(item => {
                item.writeYear = data.writeYear;

                if (!item.riskAllowanceStandards) {
                    item.implRiskAllowanceStandards = item.riskAssessmentStandards;
                } else {
                    item.implRiskAllowanceStandards = item.riskAllowanceStandards;
                }

                item.implementReduList.forEach(impl => {
                    if (item.useYn != 'N' && impl.useYn == 'Y') reductionCount += 1;
                    if (item.useYn != 'N' && impl.useYn == 'Y' && impl.completedDate != null) reductionCompletedCount += 1;

                    if (!item.riskAllowanceStandards) {
                        impl.implRiskAllowanceStandards = item.riskAssessmentStandards;
                    } else {
                        impl.implRiskAllowanceStandards = item.riskAllowanceStandards;
                    }
                    impl.riskAssessmentStandards = item.riskAssessmentStandards;

                    let result = [];
                    impl.hrList.forEach(hr => {
                        result.push({ id: hr.hrId, name: hr.hrNm, hrId: hr.hrId });
                        impl.hrList = result;
                    });
                });

                if (item.relatedEvidence != null)
                    item.relatedEvidenceItem = [
                        {
                            id: item.legalId,
                            name: item.relatedEvidence,
                            legalNm: item.legalNm
                        }
                    ];
            });

            data.reductionCount = reductionCount;
            data.reductionCompletedCount = reductionCompletedCount;
        });
    }
};

// 이전 위험성 정보 선택 완료 개수 계산
const getCompletedCnt = index => {
    const planImplList = implList.value.list[index]?.implRiskAssList || [];

    return planImplList.reduce((cnt, item) => {
        if (!item.docNo) return cnt;

        // 감소 대책 존재 여부
        const reductionMeasures = item.implementReduList?.filter(data => data.docSeqDetail != null && data.useYn === 'Y') || [];

        // 감소 대책 완료 여부
        const completedMeasures = item.implementReduList?.filter(data => data.completedDate != null && data.docSeqDetail != null && data.useYn === 'Y') || [];

        const isReductionCompleted = reductionMeasures.length > 0 && reductionMeasures.length === completedMeasures.length;

        // 위험 점수 허용 기준 확인
        let isRiskScoreAllowed = false;
        const { riskScore, afterRiskScore, riskAllowanceStandards: allowance } = item;

        if (riskScore && allowance) {
            const targetScore = afterRiskScore || riskScore;
            const [, riskLevel] = targetScore.split('_');
            const [, allowLevel] = allowance.split('_');

            if (allowance.includes('3a')) {
                // 랭크 맵 방식 (l, m, h)
                const rankMap = { l: 1, m: 2, h: 3 };
                const riskValue = isNaN(riskLevel) ? (rankMap[riskLevel] ?? parseInt(riskLevel)) : parseInt(riskLevel);
                const allowValue = isNaN(allowLevel) ? (rankMap[allowLevel] ?? parseInt(allowLevel)) : parseInt(allowLevel);

                if (!isNaN(riskValue) && !isNaN(allowValue)) {
                    isRiskScoreAllowed = riskValue <= allowValue;
                }
            } else {
                // 숫자 방식
                const riskValue = parseInt(riskLevel) || 0;
                const allowValue = parseInt(allowLevel) || 0;

                if (!isNaN(riskValue) && !isNaN(allowValue)) {
                    isRiskScoreAllowed = riskValue <= allowValue;
                }
            }
        }

        return isReductionCompleted || isRiskScoreAllowed ? cnt + 1 : cnt;
    }, 0);
};

// 이전 위험성 정보 선택 검색
const searchFilterData = async searchTerm => {
    searchText.value = searchTerm;

    if (!currentImplList.value) {
        return;
    }

    if (!searchText.value) {
        // 검색어가 없으면 원본 데이터로 복원
        implList.value = _.cloneDeep(currentImplList.value);
        await setImplList();
    } else {
        // 검색어가 있으면 필터링
        implList.value = _.cloneDeep(currentImplList.value);
        implList.value.list = implList.value.list.map(item => ({
            ...item,
            implRiskAssList:
                item.implRiskAssList?.filter(val => {
                    const inHazards = val.hazardsFactor?.includes(searchText.value);
                    const inReduction = val.implementReduList?.some(redu => redu.reductionMeasures?.includes(searchText.value));
                    return inHazards || inReduction;
                }) || []
        }));
        await setImplList();
    }
};

// 이전 위험성 정보 선택 해당 유해위험 요소 선택
const saveRiskassessmentPrevInfo = async () => {
    if (riskAssessmentPlanStore.riskPlanPrevList.length <= 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    confirmMsg('info', t('msgSave'), '', { fun: saveRiskassessmentPrevimpl });
};

const saveRiskassessmentPrevimpl = async () => {
    // 선택 버튼 클릭 시에는 별도 처리 없이 팝업만 닫기
    selectedRiskInfo.value = null;

    // riskPlanPrevList의 데이터를 기준으로 저장 데이터 구성
    const filteredData = [];

    if (riskAssessmentPlanStore.riskPlanPrevList && riskAssessmentPlanStore.riskPlanPrevList.length > 0) {
        riskAssessmentPlanStore.riskPlanPrevList.forEach(prevPlan => {
            if (prevPlan.selectedRiskData && prevPlan.selectedRiskData.length > 0) {
                // 분류별로 그룹화
                const groupedByHazardsType = {};

                prevPlan.selectedRiskData.forEach(selectedItem => {
                    // 필요 데이터 세팅
                    const processedItem = {
                        ...selectedItem,
                        writeYear: riskAssessmentPlanStore.inputForm.writeYear,
                        docType: 'RAP',
                        docNo: riskAssessmentPlanStore.inputForm.docNo, // 해당 계획의 docNo
                        docSeq: null, // 새로 저장하기 위해 null
                        prcsWorkId: selectedItem?.prcsWorkId,
                        processId: selectedItem?.processId,
                        relatedEvidence: selectedItem.relatedEvidenceItem?.[0]?.name || null,
                        frequencyScore: selectedItem?.frequencyScore || null,
                        impactScore: selectedItem?.impactScore || null,
                        implementReduList: [], // 감소대책은 저장하지 않음
                        implWriteYear: prevPlan.planData?.writeYear,
                        implDocNo: selectedItem?.docNo,
                        implDocSeq: selectedItem?.docSeq,
                        legalId: selectedItem?.legalId
                    };

                    // hazardsType으로 그룹화
                    const hazardsType = selectedItem.hazardsType;
                    if (!groupedByHazardsType[hazardsType]) {
                        groupedByHazardsType[hazardsType] = {
                            hazardsType: hazardsType,
                            implRiskAssList: []
                        };
                    }

                    groupedByHazardsType[hazardsType].implRiskAssList.push(processedItem);
                });

                // 그룹화된 데이터를 filteredData에 추가
                Object.values(groupedByHazardsType).forEach(group => {
                    filteredData.push(group);
                });
            }
        });
    }

    // 저장 validation 확인 : 선택한 항목이 없는 경우
    if (filteredData.length === 0) {
        alertMsg('warning', t('msgSaveFail'), t('msgNoItem'));
        return;
    }

    openLoading();

    // JSON 방식으로 이행 데이터만 전송
    await saveMainRiskImpl(filteredData, true)
        .then(async res => {
            if (res) {
                // 저장 성공 후 riskPlanPrevList 초기화
                riskAssessmentPlanStore.riskPlanPrevList = [];
                // await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
                // // 상세 조회 파라미터가 있을시에만 동작
                if (riskAssessmentPlanStore.searchParam.docNo) {
                    await riskAssessmentPlanStore.fnSearch();
                }
                // toastPopup(t('msgTitleSaveSuccess'), t('msgSaveSuccess'));
            }
        })
        .catch(() => {
            alertMsg('warning', t('msgSaveFail'));
        })
        .finally(() => {
            endLoading();
        });

    riskAssessmentInfoPopup.value.onClose();
};

// 이전 위험성 정보 선택 팝업 닫기
const riskAssessmentInfoPopupClose = () => {
    selectedRiskInfo.value = null;

    // 팝업 닫을 때 선택한 데이터 초기화
    riskAssessmentPlanStore.riskPlanPrevList = [];
    riskAssessmentInfoPopup.value.onClose();
};

/**
 * 이행 화면으로 이동 버튼
 */
const btnGoImplement = async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    // 이행 데이터 초기화
    riskImplStore.resetImplStore();

    // 위험성 결정 데이터가 없는 경우에만 조회
    if (riskImplStore.frequencyList.length === 0 || riskImplStore.impactList.length === 0 || riskImplStore.riskScoreList.length === 0) {
        await riskImplStore.setSystemCodeList();
    }

    await riskImplStore.filteredSystemCodeList(riskAssessmentPlanStore.inputForm.riskAssessmentStandards);

    riskImplStore.writeYear = riskAssessmentPlanStore.searchParam.searchText;
    riskImplStore.selectedPlanData.docNo = riskAssessmentPlanStore.inputForm.docNo;
    riskImplStore.riskAllowanceStandards = riskAssessmentPlanStore.inputForm.riskAllowanceStandards;

    riskImplStore.planItem = [
        {
            id: riskAssessmentPlanStore.inputForm.docNo,
            name: riskAssessmentPlanStore.inputForm.planNm,
            writeYear: riskAssessmentPlanStore.inputForm.writeYear,
            docType: riskAssessmentPlanStore.inputForm.docType,
            docNo: riskAssessmentPlanStore.inputForm.docNo,
            cpemWriteYear: riskAssessmentPlanStore.inputForm.cpemWriteYear,
            cpemDocNo: riskAssessmentPlanStore.inputForm.cpemDocNo,
            cpemConfirmWriteYear: riskAssessmentPlanStore.inputForm.cpemConfirmWriteYear,
            cpemConfirmDocNo: riskAssessmentPlanStore.inputForm.cpemConfirmDocNo,
            compId: riskAssessmentPlanStore.inputForm.compId
        }
    ];

    // 세션 설정
    sessionStorage.setItem('riskPlanDocNo', riskAssessmentPlanStore.inputForm.docNo);
    sessionStorage.setItem('riskAssessmentTab', 'implement');

    router.push({ name: 'RiskAssessmentTable', state: { activeTab: 'implement' } });
};

/**
 * 우측 버튼 이벤트
 */

// 변경 여부 확인 함수
const checkUpdate = fun => {
    if (JSON.stringify(riskAssessmentPlanStore.inputFormBefore) !== JSON.stringify(riskAssessmentPlanStore.inputForm)) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: fun, param: '' });
    } else {
        fun();
    }
};

// 위험성 추정 기준 변경 시 이벤트 호출
const changeStandard = async e => {
    const selectedMinorCd = e.target.value;
    riskAssessmentPlanStore.riskAllowanceStandardsList = [];
    riskAssessmentPlanStore.inputForm.riskAllowanceStandards = '';
    await riskAssessmentPlanStore.filterAllowanceList(selectedMinorCd);
};

const goBack = () => {
    sessionStorage.setItem('riskAssessmentTab', 'plan');
    router.push({ name: 'RiskAssessmentTable', state: { activeTab: 'plan' } });
};

// 우측 목록 버튼
btnBack(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    checkUpdate(goBack);
});

// 우측 조회 버튼
btnSearch(async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    // 상세 조회 파라미터가 있을시에만 동작
    if (riskAssessmentPlanStore.searchParam.docNo) {
        checkUpdate(riskAssessmentPlanStore.fnSearch);
    }
});

// 우측 추가 버튼
btnAdd(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    // 서명 리랜더링하기 위한 값변경
    resetSignature.value++;
    checkUpdate(btnAddConfirmObj);
});

// 추가버튼 실행 함수
const btnAddConfirmObj = () => {
    layoutStore.useBtnList.length = 0;
    layoutStore.useBtnList.push('btnBack', 'btnSave');
    riskAssessmentPlanStore.btnAdd();
};

// 우측 저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);

    let chkValid = true;
    if (riskAssessmentPlanStore.inputForm.startDate > riskAssessmentPlanStore.inputForm.endDate) {
        alertMsg('warning', '위험성평가 기간 시작일이 종료일보다 큽니다.');
        return;
    }

    if (isValid && chkValid) {
        // 서명 컴포넌트 담아서 같이 파라미터로 보내기
        riskAssessmentPlanStore.signatureComponent = signatureComponent.value;

        if (riskAssessmentPlanStore.inputForm.cmd === 'I') {
            // 등록
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: riskAssessmentPlanStore.insertRiskAssessmentPlan,
                param: riskAssessmentPlanStore.inputForm
            });
        } else {
            //수정
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: riskAssessmentPlanStore.updateRiskAssessmentPlan,
                param: riskAssessmentPlanStore.inputForm
            });
        }
    }
});

// 우측 삭제 버튼
btnDelete(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    checkUpdate(fnDelete);
});

// 삭제 함수
const fnDelete = () => {
    riskAssessmentPlanStore.signatureComponent = signatureComponent.value;
    confirmMsg('warning', t('msgDelete'), null, {
        fun: riskAssessmentPlanStore.deleteRiskAssessmentPlan,
        param: [riskAssessmentPlanStore.inputForm]
    });
};

/**
 * 근로자 확인 팝업
 */
const workerCheckPopup = ref(null);

btnWorkerCheck(async () => {
    let pass = true;
    signatureComponent.value.getSignatureData().forEach(val => {
        //작성, 검토, 승인 중 사인이 하나라도 없을 경우 pass = false
        if (val.signature == null) {
            pass = false;
        }
    });
    if (pass) {
        openLoading();
        await workerCheckPopup.value.onOpen();
        signSearchTerm.value = '';
        const searchParam = {
            compId: getCompId()
        };
        let responses = await getHr(searchParam, false);
        if (workerCheck.value?.userList.length === 0) {
            //데이터가 없을 경우
            //조직의 서명 데이터가 없을 경우 세팅 후 저장
            const filteredData = responses.list.filter(item => item.orgnId === riskAssessmentPlanStore.orgnItem.orgnId); //위험성평가 계획 상세의 조직 데이터 기준으로 필터
            await workerCheck.value.initPeople(filteredData);
            await workerCheck.value.setHrChipsApprovalInfo(riskAssessmentPlanStore.inputForm.docType, riskAssessmentPlanStore.inputForm.writeYear, riskAssessmentPlanStore.inputForm.docNo);
            workerCheck.value.Search();
        } else {
            //데이터가 있을 경우
            if (riskAssessmentPlanStore.inputForm.orgnNm !== workerCheck.value.userList[0].orgnNm) {
                //해당 데이터가 현재 조직의 인원과 일치하지 않을 경우 기존 조직을 삭제 후, 현재 조직 인원으로 다시 채워 넣음
                const deleteParam = {
                    targetType: 'RAP',
                    targetId: riskAssessmentPlanStore.inputForm.writeYear + riskAssessmentPlanStore.inputForm.docNo
                };
                await deleteWorkerApprovalInfoAll(deleteParam, false);
                const filteredData = responses.list.filter(item => item.orgnId === riskAssessmentPlanStore.orgnItem.orgnId); //위험성평가 계획 상세의 조직 데이터 기준으로 필터
                await workerCheck.value.initPeople(filteredData);
                await workerCheck.value.setHrChipsApprovalInfo(riskAssessmentPlanStore.inputForm.docType, riskAssessmentPlanStore.inputForm.writeYear, riskAssessmentPlanStore.inputForm.docNo);
            }
        }
        await activeAnimation();
        endLoading();
    } else {
        alertMsg('warning', '승인까지 서명이 완료되어야 \n근로자 확인이 가능합니다.');
        endLoading();
        return;
    }
});

//참석자 확인 필터
const signSearchTerm = ref('');
const applySignFilter = () => {
    workerCheck.value?.applyFilter(signSearchTerm.value);
};

// 개별 아코디언 토글 함수
const toggleAccordion = async e => {
    const button = e.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

//근로자 확인 버튼 클릭 시, 자동으로 아코디언 펼침
const activeAnimation = () => {
    const accordion = document.getElementById('signAccordion');
    const container = accordion.parentNode;
    const segment = container.querySelector('.segment');
    const isOpen = accordion.classList.toggle('active');
    const alreadyOpen = accordion.classList.toggle('expanded');
    if (alreadyOpen === true) {
        animateAccordion(segment, isOpen);
    }
};
</script>
<style lang="scss" scoped>
table {
    tr:nth-child(2) {
        th {
            color: #000;
            font-weight: 400;
        }
    }
}
.img {
    border: 1px solid #e1e6ed;
    background-color: #f8f9fb;
    border-radius: 4px;
    height: 100%;
    min-height: 30rem;
    font-size: 1.6rem;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
