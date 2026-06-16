<template>
    <div class="form ui h100p df fdc oh el-ha">
        <div class="mb1rem el-pb4rem">
            <!-- 🎼 section1 -->
            <div class="row flex gutters1rem el-pr">
                <div class="grid12-3 us-grid12-12">
                    <div class="field">
                        <label for="writeYear">
                            <span> {{ t('riskAssessmentPlan_writeYear') }} </span>
                        </label>
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="riskImplStore.writeYear" @input="searchDataByYear" class="datepicker w20rem radius es-w100p" />
                    </div>
                </div>

                <div class="grid12-9 us-grid12-12">
                    <div class="field">
                        <label for="">
                            <span>{{ t('dashboard_riskAssessmentPlan') }} </span>
                            <span v-if="riskImplStore.writeYear === currentYear && riskImplStore.selectedPlanData?.confirmYn === 'N'" class="fs1-5rem cFF3534"> (공정/설비/물질 구분 확정 여부 확인 필요)</span>
                        </label>
                        <div class="df aic gap1rem">
                            <i-chips id="planNm" :chips="riskImplStore.planItem" @popup="addPlan" :readonlyType="'chips'"></i-chips>
                            <div class="w55rem df aic jcsb gap1rem el-pa el-l0 el-w100p el-pa0-5rem el-neg-b4-3rem">
                                <div class="shrink0">
                                    <input v-input="['전체 펼침', '전체 접음']" type="checkbox" class="switch df" @click="toggleAllAccordions" />
                                </div>
                                <a @click="pageMove" class="df aic wsn fs1-5rem c3248F6 cp">
                                    {{ t('classificationProcessEquipMsds') }} 화면으로 이동
                                    <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="h100p fg1 maxh100p oh el-ha">
            <!-- 🎹 section2 Accordion -->
            <div class="row flex gutters1rem h100p el-ha">
                <!-- 좌측 아코디언 -->
                <div class="grid12-3 el-grid12-12 h100p df fdc">
                    <OverlayScrollbarsComponent
                        class="bcF8F9FB h100p"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div class="accordion df fdc rg8px">
                            <div class="list" v-for="(prcs, num) in riskImplStore.selectedPlanData.classPrcsList" :key="prcs.processId">
                                <button type="button" class="df jcsb aic" id="processAccordion" @click="toggleAccordion">
                                    <!--🐸 A공정 -->
                                    <p>{{ prcs.processNm }}</p>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div class="segment oh wbka lh1-5">
                                    <!-- 🦖1-2 Accordion Content -->
                                    <ul class="pa1-2rem df fdc gap8px bcFFFFFF" v-if="prcs?.processList?.[0]?.prcsCnfmId != null">
                                        <li class="card-work" :class="{ active: selectedProcessIndex == num && selectedWorkIndex == index }" v-for="(list, index) in prcs.processList" :key="index" @click="onClickRow(prcs, num, index)">
                                            <p>{{ list.workContent }}</p>
                                            <div class="data">
                                                <span
                                                    >등록 요인<i>{{ list.registerCount || 0 }}</i></span
                                                >
                                                <span
                                                    >완료 요인<i>{{ list.completedCount || 0 }}</i></span
                                                >
                                            </div>
                                        </li>
                                    </ul>
                                    <ul class="pa1-2rem df fdc gap8px bcFFFFFF" v-else>
                                        <p>작업 내용이 없습니다.</p>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>

                <!-- 우측 아코디언 -->
                <div class="grid12-9 el-grid12-12 h100p df fdc">
                    <OverlayScrollbarsComponent
                        class="fg1"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div class="accordion minh8rem">
                            <div class="list">
                                <button type="button" class="df jcsb aic" id="implAccordion" @click="toggleAccordion">
                                    <!--🐟 2024년 위험성평가 A > A공정 > A작업 -->
                                    <span class="df aic" v-show="riskImplStore.riskPlanList == null && riskImplStore.selectedPlanData.planNm == null" @click="goRiskPlan">
                                        위험성평가 계획을 먼저 등록하세요.
                                        <span class="init m pa r2rem">
                                            <i class="mr4px fs1-5rem">바로가기</i>
                                            <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </span>
                                    </span>
                                    <span class="df aic" v-show="riskImplStore.riskPlanList != null && riskImplStore.selectedPlanData.planNm != null && (riskImplStore.selectedPlanData.classPrcsList?.[0]?.revNo || null) == null" @click="goClassPrcss">
                                        공정/설비/물질 구분을 먼저 등록하세요.
                                        <span class="init m pa r2rem">
                                            <i class="mr4px fs1-5rem">바로가기</i>
                                            <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </span>
                                    </span>
                                    <span class="df aic" v-show="riskImplStore.riskPlanList != null && riskImplStore.selectedPlanData.planNm != null && riskImplStore.selectedPlanData.classPrcsList != null && riskImplStore.selectedPlanData.classPrcsList?.[0]?.revNo != null && (riskImplStore.implList.list?.length || 0) == 0" @click="goHazards">
                                        유해위험요인 분류를 먼저 등록하세요.
                                        <span class="init m pa r2rem">
                                            <i class="mr4px fs1-5rem">바로가기</i>
                                            <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </span>
                                    </span>
                                    <span class="df aic" v-show="riskImplStore.riskPlanList != null && riskImplStore.selectedPlanData.planNm != null && riskImplStore.selectedPlanData.classPrcsList != null && (riskImplStore.implList.list?.length || 0) > 0">
                                        {{ riskImplStore.selectedPlanData.planNm }}
                                        <img src="/assets/img/common/icon_step.svg" alt="" />
                                        {{ riskImplStore.selectedPlanData.classPrcsList?.[selectedProcessIndex || 0]?.processNm }}
                                        <img src="/assets/img/common/icon_step.svg" alt="" />
                                        {{ riskImplStore.selectedPlanData.classPrcsList?.[selectedProcessIndex || 0]?.processList?.[selectedWorkIndex]?.workContent }}
                                    </span>
                                    <!-- 위험성평가 계획 미 선택 시 노출 -->
                                    <span class="df aic" v-show="riskImplStore?.planItem?.[0]?.id == null"> 위험성평가 계획을 선택하세요. </span>
                                </button>
                                <div class="segment oh">
                                    <div class="pa2-2rem df fdc gap8px es-gap4px">
                                        <!-- 🐳2-2 Accordion -->
                                        <div class="accordion" v-for="(impl, index) in riskImplStore.implList.list" :key="index">
                                            <div class="list">
                                                <button type="button" class="df jcsb aic tal us-neg-ls0-5px" @click="toggleAccordion">
                                                    <p>
                                                        {{ impl.hazardsTitle }}
                                                        <br class="dn es-db" /><span>(</span>
                                                        <span class="summary">
                                                            유해요인
                                                            <i class="c3248F6">{{ impl?.implRiskAssList.filter(data => data.docSeq && data.useYn === 'Y').length || 0 }}</i>
                                                        </span>
                                                        <span>/</span>
                                                        <span class="summary">
                                                            완료
                                                            <i class="c3248F6">{{ impl ? getCompletedCnt(index) : 0 }}</i>
                                                        </span>
                                                        <span>)</span>
                                                        <br class="dn us-db" />
                                                        <span>(</span>
                                                        <span class="summary">
                                                            감소대책
                                                            <i class="c3248F6">
                                                                {{ impl.reductionCount }}
                                                            </i>
                                                        </span>
                                                        <span>/</span>
                                                        <span class="summary">
                                                            완료
                                                            <i class="c3248F6">
                                                                {{ impl.reductionCompletedCount }}
                                                            </i>
                                                        </span>
                                                        <span>)</span>
                                                    </p>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                                <div class="segment oh">
                                                    <div class="pa2-2rem df fdc gap8px bcF8F9FB es-pa1-2rem">
                                                        <div class="accordion" v-for="(item, num) in impl.implRiskAssList" :key="num">
                                                            <div class="list" v-if="item.createdAt != null">
                                                                <button type="button" class="df jcsb aic" @click="toggleAccordion" :id="`impl${index.toString()}${num.toString()}`">
                                                                    <!--🐟 분류 : 협착 위험부분 - 손끼임 사고 (감소대책 : 2, 완료 1) -->
                                                                    <div class="df aic gap8px tal es-gap4px">
                                                                        <input class="init" type="checkbox" v-input v-model="item.checked" />
                                                                        <div :class="item.useYn === 'Y' ? '' : 'c8e8e8e'">
                                                                            {{ item.hazardsFactor }}
                                                                            <br v-if="item.hazardsFactor" class="dn es-db" />
                                                                            <span>(</span>
                                                                            <span class="summary">
                                                                                감소대책<i :class="item.useYn === 'Y' ? 'c3248F6' : 'c8e8e8e'">{{ item?.implementReduList.filter(data => data.docSeqDetail && data.useYn === 'Y').length || 0 }}</i>
                                                                            </span>
                                                                            <span>/</span>
                                                                            <span class="summary">
                                                                                완료<i :class="item.useYn === 'Y' ? 'c3248F6' : 'c8e8e8e'">{{ item?.implementReduList.filter(data => data.completedDate != null && data.docSeqDetail != null && data.useYn === 'Y').length || 0 }}</i>
                                                                            </span>
                                                                            <span>) </span>
                                                                            <br v-if="item.hazardsDelYn" class="dn es-db" />
                                                                            <span v-if="item.hazardsDelYn">(분류에서 삭제됨)</span>
                                                                            <br v-if="item.useYn === 'N'" class="dn es-db" />
                                                                            <span v-if="item.useYn === 'N'">(미사용) </span>
                                                                            <br v-if="riskImplStore.isRiskAllowance?.[index.toString() + num.toString()]" class="dn es-db" />
                                                                            <span v-if="riskImplStore.isRiskAllowance?.[index.toString() + num.toString()]" class="dib c00129F bc50-72-246-10 px5px py3px br4px fs1-4rem es-mt5px">{{ riskImplStore.riskAllowance?.[index.toString() + num.toString()] }}</span>
                                                                        </div>
                                                                    </div>
                                                                    <svg class="minw2-4rem minh2-4rem" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                        <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                                                    </svg>
                                                                </button>
                                                                <RiskAssessmentImplementationComponent :key="item" :factorList="impl.factorList" :implData="item" :reductionList="item.implementReduList" :frequencyList="riskImplStore.filteredFrequencyList" :impactList="riskImplStore.filteredImpactList" :riskScoreList="riskImplStore.filteredRiskScoreList" :fileList="fileList" :fileList2="fileList2" :index="index.toString() + num.toString()" @update:implData="handleImplDataUpdate(index, num, $event)"> </RiskAssessmentImplementationComponent>
                                                            </div>
                                                        </div>
                                                        <!-- 유해위험요인 추가 -->
                                                        <button type="button" class="df jcc aic gap8px w100p radius" @click="addImpl(impl, index)">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                            <span class="fs1-5rem fw400">유해위험요인 추가</span>
                                                        </button>
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
            </div>
        </div>
    </div>

    <teleport to="body">
        <i-PopupDialog ref="planPopup">
            <!-- 단일 그리드 -->
            <div class="contents w60rem md-w100p">
                <base-select-popup :title="'위험성평가 계획'" filterKey="planNm" :fetch-data="getRiskImplList" :fetch-param="{ writeYear: riskImplStore.writeYear, compId: getCompId() }" :single="true" @close="planPopupClose" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import router from '@/router';
import { ref, nextTick, watch, reactive, defineProps } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

// baseview
import BaseView from '@/components/base/BaseView';
const { validationStore, t, onMounted, alertMsg, confirmMsg, getCompId, getCurrentDate, btnBack, btnSearch, btnDelete, btnSave, btnDownload, downloadReport, goRouter } = BaseView();

// Button List
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];

// component
import RiskAssessmentImplementationComponent from './component/RiskAssessmentImplementationComponent.vue';

// riskImpl API
import { getRiskImplList, getRiskAssessmentDetail, saveRiskImpl, saveRiskImplImage, getReportAll, checkRemoveRiskImplList } from '@/stores/safewiz/planning/api/implementationOfRiskAseessmentApi';
// riskImplStore
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
const riskImplStore = useRiskAssessmentImplementation();
const riskPlanList = ref([...riskImplStore.riskPlanList]);

// loading panel
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

// calendar year
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
const riskAStore = useRiskAStore();

const currentYear = getCurrentDate().substring(0, 4);

/**********************************
 * 파일 업로드 관련
 **********************************/
const fileList = ref([]); // 개선 전 이미지
const fileList2 = ref([]); // 개선 후 이미지

/**********************************
 * 계획 선택 팝업 관련
 **********************************/
const planPopup = ref(null);
// 계획 팝업 노출
const addPlan = () => {
    riskImplStore.planPopup.onOpen();
};
// 플랜 팝업 닫기
const planPopupClose = async e => {
    if (e && e.length) {
        // 위험성 결정 데이터가 없는 경우에만 조회
        if (riskImplStore.frequencyList.length === 0 || riskImplStore.impactList.length === 0 || riskImplStore.riskScoreList.length === 0) {
            await riskImplStore.setSystemCodeList();
        }

        await riskImplStore.filteredSystemCodeList(e[0].riskAssessmentStandards);

        riskImplStore.planItem = [
            {
                id: e[0].docNo,
                name: e[0].planNm,
                writeYear: e[0].writeYear,
                docType: e[0].docType,
                docNo: e[0].docNo,
                cpemWriteYear: e[0].cpemWriteYear,
                cpemDocNo: e[0].cpemDocNo,
                cpemConfirmWriteYear: e[0].cpemConfirmWriteYear,
                cpemConfirmDocNo: e[0].cpemConfirmDocNo,
                compId: e[0].compId
            }
        ];
        riskImplStore.riskAllowanceStandards = e[0].riskAllowanceStandards; // 허용 가능 기준 추가
    }
    riskImplStore.planPopup.onClose();
};

onMounted(async () => {
    // await riskImplStore.getPlanData(riskImplStore.selectedPlanData.docNo || null);
    riskImplStore.writeYear = riskAStore.searchParam.searchText;
    riskImplStore.setPlanPopup(planPopup);
});

const getCompletedCnt = index => {
    let cnt = 0;
    const implList = riskImplStore.implList.list[index]?.implRiskAssList || [];

    implList.forEach(item => {
        if (item.docNo && item.useYn === 'Y') {
            // 요인 감소 대책 개수
            const reductionMeasures = item.implementReduList?.filter(data => data.docSeqDetail != null && data.useYn === 'Y') || [];
            // 완료 감소 대책 개수
            const endReductionMeasures = item.implementReduList?.filter(data => data.completedDate != null && data.docSeqDetail != null && data.useYn === 'Y') || [];

            let isAllowRiskScore = false;

            const riskScore = item.riskScore;
            const allowance = riskImplStore.riskAllowanceStandards;

            if (riskScore && allowance) {
                const [riskPrefix, riskLevel] = riskScore.split('_');
                const [allowPrefix, allowLevel] = allowance.split('_');

                // 상중하/빈도/강도 기준 일치 시 비교
                if (riskPrefix === allowPrefix) {
                    const rankMap = { l: 1, m: 2, h: 3 };
                    const riskValue = isNaN(riskLevel) ? rankMap[riskLevel] ?? parseInt(riskLevel) : parseInt(riskLevel);
                    const allowValue = isNaN(allowLevel) ? rankMap[allowLevel] ?? parseInt(allowLevel) : parseInt(allowLevel);

                    if (!isNaN(riskValue) && !isNaN(allowValue)) {
                        isAllowRiskScore = riskValue <= allowValue;
                    }
                }
            }
            // 감소대책과 완료 감소 대책 두개가 같으면서 길이가 0임에도 isAllowRiskScore true
            if ((reductionMeasures.length > 0 && reductionMeasures.length === endReductionMeasures.length) || isAllowRiskScore) {
                if (isAllowRiskScore && reductionMeasures.length !== endReductionMeasures.length) {
                    // 허용 가능한 위험이지만, 등록된 감소 대책 중 이행되지 않은 항목은 카운트에서 제외
                    return;
                }
                cnt++;
            }
        }
    });
    return cnt;
};
/**********************************
 * 화면 이동 이벤트
 **********************************/
const props = defineProps({
    // 위험성평가 계획 컴포넌트 호출
    goRiskPlan: {
        type: Function,
        required: true
    },
    // 유해위험요인 분류 컴포넌트 호출
    goHazards: {
        type: Function,
        required: true
    }
});
// 위험성평가 계획 컴포넌트 호출
const goRiskPlan = () => {
    props.goRiskPlan();
};
// 유해위험요인 분류 컴포넌트 호출
const goHazards = () => {
    props.goHazards();
};
// 공정/설비/물질 구분 목록 화면 이동
const goClassPrcss = () => {
    router.push('/ClassificationProcessEquipMsds');
};

/**********************************
 * 년도 변경 이벤트
 **********************************/
const searchDataByYear = async () => {
    // 년도 변경 시 이미 선택된 계획이 있는 경우는 watch를 통해 데이터가 변경되므로 return
    if (riskImplStore.planItem.length > 0) {
        riskImplStore.planItem = [];
        return;
    }

    await riskImplStore.getPlanData(null);
};

/**********************************
 * 위험성평가 계획 (좌측 리스트)
 **********************************/
const selectedPlan = ref(''); // 선택한 계획
const selectedProcessIndex = ref(0); // 선택한 공정의 index 값 (최초 진입 시 0)
const selectedWorkIndex = ref(0); // 선택한 작업의 index 값 (최초 진입 시 0)
// 상세 조회 시 필요 파라미터
let searchDetailParam = {
    writeYear: riskImplStore.writeYear,
    docNo: riskImplStore.selectedPlanData?.docNo || null,
    processId: riskImplStore.selectedPlanData?.classPrcsList?.[0]?.processId || null,
    revNo: riskImplStore.selectedPlanData?.classPrcsList?.[0]?.revNo || null,
    compId: getCompId(),
    prcsWorkId: parseInt(riskImplStore?.selectedPlanData?.prcsWorkId) || null
};

watch(
    () => riskImplStore.selectedPlanData,
    (newItem, oldItem) => {
        searchDetailParam.value = {
            writeYear: riskImplStore.writeYear,
            docNo: riskImplStore.selectedPlanData?.docNo || null,
            processId: riskImplStore.selectedPlanData?.classPrcsList?.[0]?.processId || null,
            revNo: riskImplStore.selectedPlanData?.classPrcsList?.[0]?.revNo || null,
            compId: getCompId(),
            prcsWorkId: parseInt(riskImplStore?.selectedPlanData?.prcsWorkId) || null
        };
    }
);

watch(
    () => riskImplStore.planItem,
    async (newItem, oldItem) => {
        // 계획 클릭 시 조회하는 경우
        if ((newItem?.[0]?.id || null) != (oldItem?.[0]?.id || null) && sessionStorage.getItem('riskPlanDocNo') == null) {
            await riskImplStore.getPlanData(newItem?.[0]?.id);
            selectedProcessIndex.value = 0;
            selectedWorkIndex.value = 0;
            openAccordion();
        }
        // 계획 화면에서 조회하는 경우
        if (newItem?.[0]?.id != null && sessionStorage.getItem('riskPlanDocNo') != null) {
            sessionStorage.removeItem('riskPlanDocNo');
            openAccordion();
        }
    }
);

const pageMove = () => {
    if (riskImplStore.planItem?.[0]?.id) {
        // 기존에 저장한 공정 데이터를 가지고 있는 경우
        // 상세 조회 시 필요 파라미터
        const param = {
            writeYear: riskImplStore.planItem[0].cpemWriteYear ? riskImplStore.planItem[0].cpemWriteYear : riskImplStore.planItem[0].cpemConfirmWriteYear,
            docType: 'CPE',
            docNo: riskImplStore.planItem[0].cpemDocNo ? riskImplStore.planItem[0].cpemDocNo : riskImplStore.planItem[0].cpemConfirmDocNo
        };

        goRouter('ClassificationProcessEquipMsdsDetail', param);
    } else {
        router.push('/ClassificationProcessEquipMsds');
    }
};

// 아코디언 오픈
const openAccordion = () => {
    loadingPanelStore.endLoading();
    if ((riskImplStore?.selectedPlanData?.classPrcsList?.length || 0) > 0) {
        toggleAccordion(null, 'processAccordion', true);
    }
    if ((riskImplStore.implList.list?.length || 0) > 0) {
        toggleAccordion(null, 'implAccordion', true);
    }
};

let selectedPrcs = ref(); // 선택된 작업 정보
// 작업 클릭 이벤트
const onClickRow = async (prcs, num, index) => {
    selectedPrcs.value = prcs.processList[index];
    selectedProcessIndex.value = num;
    selectedWorkIndex.value = index;

    riskImplStore.prcsWorkId = parseInt(prcs.processList[index].prcsWorkId);
    riskImplStore.processId = prcs.processId;
    riskImplStore.revNo = prcs.revNo;
    riskImplStore.selectedPlanData.processNm = prcs.processNm;

    riskImplStore.selectedPlanData.prcsWorkNm = prcs.processList[index].workContent;
    riskImplStore.selectedPlanData.prcsWorkId = prcs.processList[index].prcsWorkId;

    // 클릭 시 이행 데이터 조회
    searchDetailParam.value = {
        writeYear: riskImplStore.writeYear,
        docNo: riskImplStore.selectedPlanData.docNo,
        processId: prcs.processId,
        revNo: prcs.revNo,
        compId: getCompId(),
        prcsWorkId: parseInt(prcs.processList[index].prcsWorkId)
    };
    await riskImplStore.getRiskAssDetail(searchDetailParam.value);
    loadingPanelStore.endLoading();
};

/**********************************
 * 자식 컴포넌트 이벤트 핸들러
 **********************************/
// implData 업데이트 핸들러
const handleImplDataUpdate = (implIndex, itemIndex, updatedData) => {
    if (riskImplStore.implList.list[implIndex]?.implRiskAssList[itemIndex]) {
        // 업데이트된 데이터를 store에 반영
        Object.assign(riskImplStore.implList.list[implIndex].implRiskAssList[itemIndex], updatedData);
    }
};

/**********************************
 * 이행 추가 관련 이벤트
 **********************************/
const addImpl = async (impl, index) => {
    const row = impl.implRiskAssList.length.toString();
    await impl.implRiskAssList.push({
        docType: 'RAP',
        useYn: 'Y',
        hazardsClassification: '',
        implementReduList: [{ docType: 'RAP', useYn: true, hrList: [] }],
        riskAssessmentStandards: impl.riskAssessmentStandards,
        // 저장 전까지 createdAt는 생성 안 되어 있지만, 요인 노출 기준이 createdAt 유무이기 때문에 임시처리
        createdAt: getCurrentDate(),
        checked: true
    });

    // 요인이 추가되면 아코디언 펼쳐지게 함
    toggleAccordion(null, 'impl' + index.toString() + row);
};

/******************************
 * 우측 버튼 list
 ******************************/
// 목록 버튼
btnBack(() => {
    router.push('/RiskAssessment');
});
// 조회 버튼
btnSearch(async () => {
    // 클릭 시 이행 데이터 조회
    if (riskImplStore.selectedPlanData.docNo == null) return;
    const param = {
        writeYear: riskImplStore.writeYear || getCurrentDate().substring(0, 4),
        compId: getCompId(),
        docNo: riskImplStore.selectedPlanData.docNo,
        searchText: riskImplStore.planItem ? riskImplStore.planItem[0]?.cpemWriteYear : null
    };
    await getRiskImplList(param, true).then(async res => {
        riskImplStore.calcPlanData(res, riskImplStore.selectedPlanData.docNo, selectedProcessIndex.value, selectedWorkIndex.value);
    });

    loadingPanelStore.endLoading();
});
// 삭제 버튼
btnDelete(() => {
    if (riskImplStore.implList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    const copyData = ref([riskImplStore.implList.list]);

    const filteredData = copyData.value[0]
        .map((item, j) => {
            // implRiskAssList에서 조건에 맞는 항목만 필터링
            const filteredImplRiskAssList = item.implRiskAssList
                ? item.implRiskAssList
                      .map((impl, index) => {
                          // 필요 데이터 세팅
                          impl.writeYear = riskImplStore.writeYear;
                          impl.docType = 'RAP';
                          impl.docNo = riskImplStore.selectedPlanData.docNo;
                          impl.prcsWorkId = riskImplStore.prcsWorkId;
                          impl.processId = riskImplStore.processId;
                          impl.relatedEvidence = impl.relatedEvidenceItem?.[0]?.name || null;

                          // reductionList도 필터링
                          const filteredReductionList = impl.implementReduList
                              ? impl.implementReduList.filter((reduction, num) => {
                                    // 필요 데이터 세팅
                                    reduction.writeYear = riskImplStore.writeYear;
                                    reduction.docNo = riskImplStore.selectedPlanData.docNo;
                                    reduction.docType = 'RAP';
                                    reduction.docSeq = impl.docSeq || null;
                                    reduction.useYn = 'N';
                                    return reduction.checked && reduction.docSeqDetail != null;
                                })
                              : [];

                          // 저장된 분류(이행)은 check 안 되어 있고 감소대책은 check 되어 있는 경우 cmd null 처리
                          // cmd null 된 건은 upsert 로직 실행 안 됨
                          if (impl.checked == null && filteredReductionList.length > 0 && impl.docSeq != null) {
                              impl.cmd = null;
                          }
                          // 저장된 분류(이행)이 check 되어 있는 경우 사용여부 N으로 수정
                          else if (impl.checked && impl.docSeq != null) {
                              impl.useYn = 'N';
                          }

                          // 필터링된 reductionList가 있으면 해당 impl 객체와 함께 반환
                          if (filteredReductionList.length > 0 || (impl.checked && impl.docSeq != null)) {
                              return (impl = {
                                  ...impl,
                                  implementReduList: filteredReductionList
                              });
                          }
                          return null; // 필터링된 항목이 없으면 null 반환
                      })
                      .filter(impl => impl != null)
                : []; // null을 제거

            // 필터링된 implRiskAssList가 있으면 해당 item 객체와 함께 반환
            if (filteredImplRiskAssList.length > 0) {
                return {
                    ...item, // 기존의 item을 유지하고
                    implRiskAssList: filteredImplRiskAssList // 필터링된 implRiskAssList를 덮어씀
                };
            }
            return null; // 필터링된 항목이 없으면 null 반환
        })
        .filter(item => item != null); // null을 제거

    if (filteredData.length > 0) {
        let formData = new FormData();
        formData.append('reqVo', new Blob([JSON.stringify(filteredData)], { type: 'application/json' }));
        formData.append('plan', new Blob([JSON.stringify(riskImplStore.planItem[0])], { type: 'application/json' }));
        saveRiskImpl(formData, true).then(res => {});
    } else {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
});

let isSaving = ref(true); // 중복 저장 방지 플래그
// 저장 버튼
btnSave(async () => {
    let conMsg = '';
    let isImplVal = true;
    let isReudctionVal = true;

    // 검증/필터링 로직은 반드시 "직렬"로 실행해서 (await 포함) 호출/로그 순서를 보장한다.
    const filteredData = [];
    const implList = riskImplStore.implList.list || [];

    for (let j = 0; j < implList.length; j++) {
        const item = implList[j];
        if (!item?.implRiskAssList) continue;

        const filteredImplRiskAssList = [];

        for (let index = 0; index < item.implRiskAssList.length; index++) {
            const impl = item.implRiskAssList[index];

            // 필요 데이터 세팅
            impl.writeYear = riskImplStore.writeYear;
            impl.docType = 'RAP';
            impl.docNo = riskImplStore.selectedPlanData.docNo;
            impl.prcsWorkId = riskImplStore.prcsWorkId;
            impl.processId = riskImplStore.processId;

            // 관련 근거
            impl.legalId = impl.relatedEvidenceItem?.[0]?.id || null;
            impl.relatedEvidence = impl.relatedEvidenceItem?.[0]?.name || null;

            // 위험성 수준은 필수 값이지만 만약 입력이 안 된 경우 '위험성평가 계획 > 평균 위험도' 조회 때문에 null로 들어가게 함.
            impl.frequencyScore = impl?.frequencyScore || null;
            impl.impactScore = impl?.impactScore || null;

            // 개선 위험성 수준은 필수 값이지만 만약 입력이 안 된 경우 '위험성평가 계획 > 평균 위험도' 조회 때문에 null로 들어가게 함.
            impl.afterFrequencyScore = impl?.afterFrequencyScore || null;
            impl.afterImpactScore = impl?.afterImpactScore || null;

            // reductionList도 필터링 (직렬 처리)
            const filteredReductionList = [];
            if (impl.implementReduList) {
                for (let num = 0; num < impl.implementReduList.length; num++) {
                    const reduction = impl.implementReduList[num];
                    // 필요 데이터 세팅
                    reduction.writeYear = riskImplStore.writeYear;
                    reduction.docNo = riskImplStore.selectedPlanData.docNo;
                    reduction.docType = 'RAP';
                    reduction.docSeq = impl.docSeq || null;
                    reduction.completedDate = reduction.completedDate == '' || reduction.completedDate == null ? null : reduction.completedDate;
                    reduction.expectedDate = reduction.expectedDate == '' || reduction.expectedDate == null ? null : reduction.expectedDate;

                    // 저장 validation 확인 : 감소대책 필수 값 입력 확인
                    // reductionCreatedAt이 필요한 이유가 뭔지 확인 필요
                    if (reduction.checked && isReudctionVal && reduction.reductionCreatedAt != null) {
                        isReudctionVal = await riskImplStore.saveValidationFileds(`form2` + j.toString() + index.toString() + num.toString());
                        if (!isReudctionVal) break;
                    }

                    if (reduction.checked && reduction.reductionMeasures != null) {
                        filteredReductionList.push(reduction);
                    }
                }
            }

            if (!isReudctionVal) break;

            // 저장된 분류(이행)은 check 안 되어 있고 감소대책은 check 되어 있는 경우 cmd null 처리
            // cmd null 된 건은 upsert 로직 실행 안 됨
            if (impl.checked == null && filteredReductionList.length > 0 && impl.docSeq != null) {
                impl.cmd = null;
            }

            // 저장 validation 확인 : 이행 필수 값 입력 확인
            if (impl.checked && isImplVal) {
                isImplVal = await riskImplStore.saveValidationFileds(`form` + j.toString() + index.toString());
                if (!isImplVal) break;
            }

            // 저장 validation 확인 : 미 저장된 분류 미선택 + 미저장된 감소대책 체크박스 선택
            if (impl.checked == null && filteredReductionList.length > 0 && impl.docSeq == null) {
                conMsg = '유해위험요인을 선택하지 않으면 감소대책도 저장되지 않습니다. \n그래도 계속하시겠습니까?';
            }

            // 필터링된 reductionList가 있으면 해당 impl 객체와 함께 반환
            if (filteredReductionList.length > 0 || (impl.checked && impl.hazardsClassification != null)) {
                filteredImplRiskAssList.push({
                    ...impl,
                    implementReduList: filteredReductionList
                });
            }
        }

        if (!isImplVal || !isReudctionVal) break;

        // 필터링된 implRiskAssList가 있으면 해당 item 객체와 함께 반환
        if (filteredImplRiskAssList.length > 0) {
            filteredData.push({
                ...item, // 기존의 item을 유지하고
                implRiskAssList: filteredImplRiskAssList // 필터링된 implRiskAssList를 덮어씀
            });
        }
    }

    // 저장 validation 확인 : 선택한 분류, 감소 대책이 없는 경우

    // 저장 필수 값 입력 안된 것이 있다면 return
    if (!isImplVal || !isReudctionVal) {
        return;
    }

    if (filteredData.length == 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    } else if (conMsg != '') {
        confirmMsg('info', conMsg, null, null);
        return;
    }

    confirmMsg('info', t('msgSave'), null, { fun: saveRiskImplAction, param: filteredData });
});

const saveRiskImplAction = async filteredData => {
    let formData = new FormData();
    formData.append('reqVo', new Blob([JSON.stringify(filteredData)], { type: 'application/json' }));
    formData.append('plan', new Blob([JSON.stringify(riskImplStore.planItem[0])], { type: 'application/json' }));

    if (isSaving.value) {
        // loading panel open
        loadingPanelStore.openLoading();
        isSaving.value = false;
        await saveRiskImpl(formData, true).then(res => {
            searchDetailParam.value = {
                writeYear: riskImplStore.writeYear,
                docNo: riskImplStore.selectedPlanData.docNo,
                processId: riskImplStore.processId,
                revNo: riskImplStore.revNo,
                compId: getCompId(),
                prcsWorkId: riskImplStore.prcsWorkId
            };
            // 이미지 저장 시 docNo, docSeq, docSeqDetail 값이 필요하고
            // 이미지와 필수 값이 1:1 매칭이 되어야 하기 때문에 detail 재조회 함
            getRiskAssessmentDetail(searchDetailParam.value, false).then(async response => {
                let deleteFileList = [];
                let prevFiles = [];
                let afterFiles = [];
                // reductionList도 필터링
                response.list.forEach((item, index) => {
                    item.implRiskAssList.forEach((risk, j) => {
                        // 개선 전 이미지 데이터 관련
                        let resultList = risk.implementReduList.filter((reduction, num) => {
                            reduction.writeYear = riskImplStore.writeYear;
                            reduction.docNo = riskImplStore.selectedPlanData.docNo;
                            reduction.docSeq = risk.docSeq;
                            reduction.docDetailSeq = reduction.docSeqDetail;

                            // 개선 전 fileInfo 정보가 없는 경우 {}
                            const files = riskImplStore?.fileInfo[index.toString() + j.toString()] || {};
                            // 추가적으로, files가 객체로서 editFiles가 있는지 확인
                            const insertFiles = files[num]?.editFiles?.insert || [];

                            // 개선 전 삭제되는 파일 확인
                            const deleteFile = files[num]?.editFiles?.delete || [];
                            if (deleteFile.length > 0) {
                                deleteFileList.push(deleteFile[0]);
                            }

                            // 개선 전 이미지 정보가 빈 배열이 아닌 경우만 formData에 추가
                            if (insertFiles.length > 0) {
                                formData.append('prevFiles', insertFiles[0] ? insertFiles[0] : new Blob([], { type: 'application/octet-stream' }));
                            }
                            return (insertFiles?.length || 0) > 0;
                        });

                        if (resultList.length > 0) {
                            prevFiles.push(resultList[0]);
                        }

                        // 개선 후 이미지 데이터 관련
                        const resultList2 = risk.implementReduList.filter((reduction, num) => {
                            reduction.writeYear = riskImplStore.writeYear;
                            reduction.docNo = riskImplStore.selectedPlanData.docNo;
                            reduction.docSeq = risk.docSeq;
                            reduction.docDetailSeq = reduction.docSeqDetail;
                            // 개선 후 fileInfo 정보가 없는 경우 {}
                            const files2 = riskImplStore?.fileInfo2[index.toString() + j.toString()] || {};
                            // 추가적으로, files가 객체로서 editFiles가 있는지 확인
                            const insertFiles2 = files2[num]?.editFiles?.insert || [];

                            // 개선 후 삭제되는 파일 확인
                            const deleteFile = files2[num]?.editFiles?.delete || [];
                            if (deleteFile.length > 0) {
                                deleteFileList.push(deleteFile[0]);
                            }
                            // 개선 후 이미지 정보가 빈 배열이 아닌 경우만 formData에 추가
                            if (insertFiles2.length > 0) {
                                formData.append('afterFiles', insertFiles2[0] ? insertFiles2[0] : new Blob([], { type: 'application/octet-stream' }));
                            }
                            return (insertFiles2?.length || 0) > 0;
                        });
                        if (resultList2.length > 0) {
                            afterFiles.push(resultList2[0]);
                        }
                    });
                });
                formData.append('prevVo', new Blob([JSON.stringify(prevFiles)], { type: 'application/json' }));
                formData.append('afterVo', new Blob([JSON.stringify(afterFiles)], { type: 'application/json' }));
                formData.append('deletFiles', new Blob([JSON.stringify(deleteFileList)], { type: 'application/json' }));

                // 이미지 저장 혹은 삭제 할 건이 있으면 api 호출
                if (prevFiles.length > 0 || afterFiles.length > 0 || deleteFileList.length > 0) {
                    await saveRiskImplImage(formData, false).then(res => {
                        riskImplStore.getRiskAssDetail(searchDetailParam.value, false);
                    });
                    setPrcsCount();
                    isSaving.value = true;
                } else {
                    await riskImplStore.getRiskAssDetail(searchDetailParam.value, false);
                    setPrcsCount();
                    isSaving.value = true;
                }
            });
        });
    }
};

// 저장 후 작업 요인 count 세팅
const setPrcsCount = async () => {
    let implCount = 0;
    let completedCount = 0;

    await riskImplStore.implList.list.forEach((item, i) => {
        item.implRiskAssList.forEach((data, j) => {
            if (data.useYn === 'Y' && data.docSeq != null) {
                implCount += 1;

                const reductionMeasures = data.implementReduList?.filter(res => res.useYn === 'Y' && res.docSeqDetail != null) || [];
                const completedReductions = data.implementReduList?.filter(res => res.useYn === 'Y' && res.docSeqDetail != null && res.completedDate != null) || [];

                const riskScore = data.riskScore;
                const allowance = riskImplStore.riskAllowanceStandards;
                let isAllowRiskScore = false;

                if (riskScore && allowance) {
                    const [scorePrefix, scoreLevel] = riskScore.split('_');
                    const [allowPrefix, allowLevel] = allowance.split('_');

                    if (scorePrefix === allowPrefix) {
                        const rankMap = { l: 1, m: 2, h: 3 };
                        const scoreValue = isNaN(scoreLevel) ? rankMap[scoreLevel] ?? parseInt(scoreLevel) : parseInt(scoreLevel);
                        const allowValue = isNaN(allowLevel) ? rankMap[allowLevel] ?? parseInt(allowLevel) : parseInt(allowLevel);

                        if (!isNaN(scoreValue) && !isNaN(allowValue)) {
                            isAllowRiskScore = scoreValue <= allowValue;
                        }
                    }
                }
                let result = (reductionMeasures.length > 0 && reductionMeasures.length === completedReductions.length) || isAllowRiskScore ? 1 : 0;
                if (isAllowRiskScore && reductionMeasures.length !== completedReductions.length) {
                    // 허용 가능한 위험이지만, 등록된 감소 대책 중 이행되지 않은 항목은 카운트에서 제외
                    result = 0;
                }
                completedCount += result;
            }
        });
    });

    if (selectedPrcs.value == null) {
        riskImplStore.selectedPlanData.classPrcsList[0].processList[0].completedCount = completedCount;
        riskImplStore.selectedPlanData.classPrcsList[0].processList[0].registerCount = implCount;
    } else {
        selectedPrcs.value.completedCount = completedCount;
        selectedPrcs.value.registerCount = implCount;
    }

    nextTick(() => {
        loadingPanelStore.endLoading();
    });
};

// 출력 버튼
btnDownload(() => {
    confirmMsg('info', '출력하시겠습니까?', '', { fun: print });
});

const print = async () => {
    //##########(기존 코드)
    // 선택한 processId를 가진 classPrcsList를 필터링
    // let data = [];
    // const classPrcsList = riskImplStore.selectedPlanData.classPrcsList.filter(item => item.processId === riskImplStore.processId);

    // await classPrcsList[0].processList.forEach(item => {
    //     if (item.registerCount > 0) {
    //         let row = {
    //             writeYear: riskImplStore.writeYear,
    //             docNo: riskImplStore.selectedPlanData.docNo,
    //             processId: riskImplStore.processId,
    //             revNo: riskImplStore.revNo,
    //             compId: getCompId(),
    //             prcsWorkId: item.prcsWorkId,
    //             processNm: riskImplStore.selectedPlanData.processNm,
    //             prcsWorkNm: item.workContent,
    //             type: buttonListStore.downloadType
    //         };
    //         data.push(row);
    //     }
    // });

    //##########(전체 출력되도록 수정한 코드)
    const data = riskImplStore.riskPlanList.flatMap(main =>
        main.classPrcsList.flatMap(sub =>
            sub.processList
                .filter(sub2 => sub2.registerCount > 0)
                .map(sub2 => ({
                    writeYear: main.writeYear,
                    docNo: main.docNo,
                    processId: sub.processId,
                    revNo: sub.revNo,
                    compId: getCompId(),
                    prcsWorkId: sub2.prcsWorkId,
                    processNm: sub.processNm,
                    prcsWorkNm: sub.workContent,
                    type: buttonListStore.downloadType
                }))
        )
    );

    // loading panel open
    await loadingPanelStore.openLoading();
    if (data.length > 0) {
        // 위험성평가표(공정별로 작성) 출력
        await getReportAll(data, true).then(res => {
            downloadReport(res);
        });
    } else {
        alertMsg('warning', '등록된 유해위험요인이 없습니다.');
    }

    nextTick(() => {
        // loading panel open
        loadingPanelStore.endLoading();
    });
};

/******************************
 * 아코디언 관련
 ******************************/
// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 요소의 중첩 레벨을 구하는 함수
const getDepth = element => {
    let depth = 0;
    while (element.parentElement) {
        if (element.parentElement.classList.contains('accordion')) break;
        if (element.parentElement.classList.contains('list')) depth++;
        element = element.parentElement;
    }
    return depth;
};

// 개별 아코디언 토글 함수
const toggleAccordion = async (event, fieldId) => {
    let button = null;
    if (fieldId != null) {
        button = document.getElementById(`${fieldId}`);
    } else {
        button = event.currentTarget;
    }

    if (button) {
        // const button = event.currentTarget;
        const segmentElement = button.nextElementSibling;

        const isOpen = button.classList.toggle('active');
        await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
        animateAccordion(segmentElement, isOpen);
    }
};

// 모든 아코디언 열기/닫기 토글 함수
const toggleAllAccordions = event => {
    const allButtons = Array.from(document.querySelectorAll('.accordion .list > button'));
    const allSegments = Array.from(document.querySelectorAll('.accordion .segment'));

    // 현재 상태 확인 true = 전체 펼침, false = 전체 접음
    const isExpand = event.target.checked;

    // 모든 버튼의 활성화 상태를 상태에 따라 설정
    allButtons.forEach(button => button.classList.toggle('active', isExpand));

    // 세그먼트를 깊이별로 그룹화
    const segmentsByDepth = {};
    allSegments.forEach(segment => {
        const depth = getDepth(segment);
        if (!segmentsByDepth[depth]) segmentsByDepth[depth] = [];
        segmentsByDepth[depth].push(segment);
    });

    // 깊이 순서대로 그룹화된 세그먼트를 순차적으로 애니메이션
    Object.keys(segmentsByDepth)
        .sort((a, b) => a - b) // 깊이 순서대로 정렬
        .forEach((depth, depthIndex) => {
            segmentsByDepth[depth].forEach((segment, segmentIndex) => {
                // 깊이별로 딜레이를 적용하여 하위 아코디언이 이전 깊이가 완료된 후 열리도록 함
                const delay = depthIndex * 0.6 + segmentIndex * 0.1;
                gsap.delayedCall(delay, () => {
                    animateAccordion(segment, isExpand);
                });
            });
        });
};
</script>

<style lang="scss" scoped>
// 좌측 공정 아코디언 속 박스
.card-work {
    border-radius: 4px;
    border: 1px solid #e1e6ed;
    background: #fff;
    padding: 1.6rem;
    &.active {
        border: 1px solid rgba(235, 237, 255, 0.5);
        background: rgba(235, 237, 255, 0.5);
    }

    & {
        font-size: 1.5rem;
        font-weight: 400;
    }

    p {
        font-size: 1.6rem;
        font-weight: 500;
        margin-bottom: 8px;
    }

    .data {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
        gap: 6px;
        span {
            display: flex;
            align-items: center;
            gap: 6px;
        }
        i {
            border-radius: 4px;
            background: rgba(50, 72, 246, 0.1);
            color: #3248f6;
            padding: 3px 6px;
        }
    }
}
</style>
