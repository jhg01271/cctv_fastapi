<template>
    <div class="riskAssessmentInfoPopup">
        <h3>{{ title }}</h3>
        <div v-if="showPrevBtn" class="row flex gutters1rem">
            <div class="grid12-3 lg-grid12-12">
                <div class="field">
                    <label for>{{ t('riskAssessmentPlan_writeYear') }}</label>
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="selectedYear" class="datepicker w20rem radius es-w100p" @input="changedYear" />
                </div>
            </div>

            <div class="grid12-9 lg-grid12-12">
                <div class="field">
                    <label for>{{ t('riskAssessmentPlan_title') }} {{ `(${t('riskAssessment_standards')} : ${riskAssessmentPlanStore.riskAssessmentStandardsList.filter(item => item.minorCd === props.riskPlanData?.riskAssessmentStandards)[0]?.minorNm})` }}</label>
                    <i-chips :chips="selectedPlan ? [selectedPlan] : []" @popup="openRiskAssesmentPlan" @removeChip="removeRiskAssesmentPlan" :readonly="!selectedYear"></i-chips>
                </div>
            </div>
            <div class="grid12-12">
                <div class="df mb1rem">
                    <input v-model="searchTerm" v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilter" />
                    <button type="submit" class="shrink0" @click.stop="applyFilter">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent class="maxh60rem" :options="{ scrollbars: { x: 'visible', y: 'hidden' } }">
            <div class="bd1pxsolide1e6ed br4px" v-if="implRiskAssListNullCheck">
                <OverlayScrollbarsComponent class="pa1-6rem maxh30rem">
                    <div class="df aic jcc minh20rem fs2rem c999999">
                        {{ '데이터가 없습니다.' }}
                    </div>
                </OverlayScrollbarsComponent>
            </div>
            <div class="df fdc gap8px es-gap4px" v-else>
                <div class="accordion" v-for="(impl, index) in implList.list" :key="index">
                    <div class="list" v-if="impl?.implRiskAssList.length !== 0">
                        <button type="button" class="df jcsb aic tal us-neg-ls0-5px" @click="toggleAccordion($event, 'impl' + index)">
                            <p class="lh1-5">
                                {{ impl.hazardsTitle }}<br class="dn es-db" />
                                <span>(</span>
                                <span class="summary">
                                    유해요인 <i class="c3248F6">{{ impl.implRiskAssList.filter(d => d.docSeq && d.useYn === 'Y').length }}</i>
                                </span>
                                <span>/</span>
                                <span class="summary">
                                    완료
                                    <i class="c3248F6">{{ getCompletedCnt(index) }}</i>
                                </span>
                                <span>)</span>
                                <span>
                                    <br class="dn us-db" />
                                    (
                                </span>
                                <span class="summary">
                                    감소대책
                                    <i class="c3248F6">
                                        {{ impl.reductionCount || 0 }}
                                    </i>
                                </span>
                                <span>/</span>
                                <span class="summary">
                                    완료
                                    <i class="c3248F6">
                                        {{ impl.reductionCompletedCount || 0 }}
                                    </i>
                                </span>
                                <span>)</span>
                            </p>
                            <svg width="24" height="24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M16 11L12.471 14.79c-.26.28-.683.28-.943 0L8 11" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh">
                            <div class="pa2-2rem df fdc gap8px bcF8F9FB">
                                <div class="accordion form ui" v-for="(item, num) in impl.implRiskAssList" :key="num">
                                    <div class="list" v-if="item.createdAt">
                                        <button type="button" class="df jcsb aic tal" @click="toggleAccordion($event, 'impl' + index + num)">
                                            <div class="df aic gap8px es-gap4px">
                                                <input v-if="showSelectedBtn" type="checkbox" v-model="item.checked" v-input :true-value="true" :false-value="false" @change="event => handleAccordionSelection(index, num, item, event)" @click.stop :disabled="props.single ? false : isCheckboxDisabled(item)" />

                                                <p class="lh1-5 summary-style" :class="isCheckboxDisabled(item) ? 'c8e8e8e' : ''">
                                                    {{ item.hazardsFactor }}<br class="dn es-db" />
                                                    <span>(</span>
                                                    <span class="summary">
                                                        감소대책
                                                        <i class="c3248F6">{{ item.implementReduList.filter(r => r.docSeqDetail && r.useYn).length }}</i>
                                                    </span>
                                                    <span>/</span>
                                                    <span class="summary">
                                                        완료
                                                        <i class="c3248F6">{{ item.implementReduList.filter(r => r.completedDate && r.docSeqDetail && r.useYn).length }}</i>
                                                    </span>
                                                    <span>) </span>
                                                    <span v-if="item.hazardsDelYn">(분류에서 삭제됨)</span>
                                                    <span class="dib c00129F bc50-72-246-10 px5px py3px br4px fs1-4rem es-mt5px">{{ item.processNm + ' (' + item.workContent + ')' }}</span>
                                                </p>
                                            </div>
                                            <svg class="minw2-4rem minh2-4rem" width="24" height="24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M16 11L12.471 14.79c-.26.28-.683.28-.943 0L8 11" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <component :is="cardComponent" :implData="item" :reductionList="item.implementReduList" :frequencyList="riskImplStore.filteredFrequencyList" :impactList="riskImplStore.filteredImpactList" :riskScoreList="riskImplStore.filteredRiskScoreList" :index="`${index}${num}`" :readonly="true" :showPrevBtn="props.showPrevBtn" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <div class="mt2rem df jcfe">
            <button v-if="showSelectedBtn" class="btn active mr1rem w74px radius" type="button" @click="emitSelect" :disabled="!selectedRiskData">
                <span>{{ single ? t('select') : t('save') }}</span>
            </button>
            <button class="btn w74px radius bright-grey" type="button" @click="emitClose">
                <span>{{ t('close') }}</span>
            </button>
        </div>

        <teleport to="body">
            <!-- 위험성 평가 계획 선택 팝업 -->
            <i-PopupDialog ref="riskPlanPopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'위험성평가 계획'" :uniqueKey="'docNo'" filterKey="planNm" :excluded-value="'N'" useYnKey="useYn" :single="true" :selectedIdList="selectedPlan ? [selectedPlan.id] : []" :fetch-data="getRiskAssessmentPlanPrevPopupListApi" :fetch-param="param" @close="closeRiskAssesmentPlan" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import RiskAssessmentImplementationComponent from '@/views/safewiz/planning/component/RiskAssessmentImplementationComponent.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getRiskAssessmentPlanPrevPopupListApi } from '@/stores/safewiz/planning/api/riskAssessmentPlanApi';
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
import { useRiskAssessmentPlanStore } from '@/stores/safewiz/planning/riskAssessmentPlan.js';

const { ref, computed, defineExpose, defineProps, defineEmits, nextTick, onMounted, t, getCurrentDate, getCompId, watch } = BaseView();

// Props 정의
const props = defineProps({
    title: { type: String, default: '' }, // 팝업 제목
    single: { type: Boolean, default: true }, // 싱글 여부 (ex. single은 TBM, multi는 위험성 평가 계획 이전 위험성 정보 선택 버튼)
    showPrevBtn: { type: Boolean, default: false }, // 이전 위험성 정보 선택 버튼
    orgnItem: { type: Object }, // 조직 데이터
    implList: { type: Object, default: () => ({}) }, // 이행 데이터
    searchText: { type: String, default: '' }, // 검색어
    showSelectedBtn: { type: Boolean, default: true }, // 선택 버튼
    potentialRiskIndex: { type: Number, default: 0 }, // 인덱스
    selectedRiskInfo: { type: Object, default: () => ({}) }, // 선택한 데이터
    getCompletedCnt: { type: Function }, // 완료 개수 계산
    selectedAccordion: { type: Function }, // 아코디언 선택 이벤트
    cardComponent: { type: [Object, Function], default: () => RiskAssessmentImplementationComponent }, // 카드 컴포넌트 : 디폴트는 위험성 평가 이행
    riskPlanPrevList: { type: Array, default: () => [] }, // 선택한 위험성 평가 이행 리스트
    referenceRiskImplList: { type: Array, default: () => [] }, // 해당 계획이 이미 등록한 이행 데이터
    riskPlanData: { type: Array, default: () => [] } // 해당 위험성 계획 상세 데이터
});

// Emits
const emit = defineEmits(['filter', 'search', 'select', 'close', 'change']);

// GSAP
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// Store
const riskAssessmentPlanStore = useRiskAssessmentPlanStore();
const riskImplStore = useRiskAssessmentImplementation();

// ===== 반응형 데이터 =====
// 팝업 및 UI 관련
const popupRef = ref();
const riskPlanPopup = ref('');
const searchTerm = ref(props.searchText ?? '');

// 선택 관련 데이터
const selectedYear = ref(getCurrentDate().substring(0, 4));
const selectedPlan = ref(null);
const selectedRiskData = ref(null);

// API 파라미터
const param = ref();

// ===== 계산된 속성 =====
const implRiskAssListNullCheck = computed(() => {
    return !props.implList?.list?.some(val => val.implRiskAssList?.length);
});

// 이미 등록된 데이터의 경우 체크박스 비활성화
const isCheckboxDisabled = computed(() => {
    return item => {
        // 해당 계획(위험성 평가 상세 화면의 계획)에 이미 등록된 이행 데이터인지 판별
        const isReferencedItem = props.referenceRiskImplList.some(refItem => refItem.implWriteYear === item.planWriteYear && refItem.implDocNo === item.docNo && refItem.implDocSeq === item.docSeq);

        return isReferencedItem;
    };
});

// ===== UI 제어 =====
const open = () => popupRef.value?.onOpen();
const close = () => popupRef.value?.onClose();

const applyFilter = () => {
    emit('filter', searchTerm.value);
};

const emitSelect = () => {
    if (!selectedRiskData.value) {
        return;
    }

    if (props.single) {
        emit('select', {
            index: props.potentialRiskIndex,
            risk: selectedRiskData.value.risk,
            measures: selectedRiskData.value.measures,
            planData: selectedPlan.value
        });
    } else {
        emit('select', {
            selectedRiskData: selectedRiskData.value,
            planData: selectedPlan.value
        });
    }
};

const emitClose = () => {
    emit('close');
};

// ===== 아코디언 관련 =====
const toggleAccordion = async (event, fieldId) => {
    const target = event.target;

    // 체크박스 또는 label 클릭 시 아코디언 토글 방지
    if (target.closest('input[type="checkbox"]') || target.closest('label')) {
        return;
    }

    const button = event.currentTarget;
    const segment = button.nextElementSibling;
    const isOpen = button.classList.toggle('active');

    await nextTick();

    gsap.to(segment, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'power2.out'
    });
};

// ===== 선택 관련 =====
const handleAccordionSelection = (index, num, item, event) => {
    const checkbox = event?.target;
    const isActuallyChecked = checkbox ? checkbox.checked : item.checked;

    item.checked = isActuallyChecked;

    // 체크 여부 판별
    if (isActuallyChecked) {
        handleItemSelection(index, num, item);
    } else {
        handleItemDeselection(item);
    }
};

// 체크 박스 선택
const handleItemSelection = (index, num, item) => {
    let measures = '';
    item.implementReduList.forEach(val => {
        if (measures === '') {
            measures = val.reductionMeasures;
        } else {
            measures += ', ' + val.reductionMeasures;
        }
    });

    if (props.single) {
        handleSingleSelection(index, num, item, measures);
    } else {
        handleMultipleSelection(item);
    }
};

// single인 경우
const handleSingleSelection = (index, num, item, measures) => {
    // 다른 체크박스들 해제
    if (props.implList?.list) {
        props.implList.list.forEach((impl, i) => {
            impl.implRiskAssList.forEach((riskItem, j) => {
                if (i !== index || j !== num) {
                    riskItem.checked = false;
                }
            });
        });
    }

    const risk = item.hazardsFactor;
    selectedRiskData.value = {
        risk: risk,
        measures: measures,
        index: index,
        num: num
    };
};

// multi 인 경우
const handleMultipleSelection = item => {
    if (!selectedRiskData.value) {
        selectedRiskData.value = [];
    }

    // 해당 이행의 key를 통해 중복 판별
    const itemKey = `${item.docNo}_${item.docSeq}_${item.docType}`;
    const isDuplicate = selectedRiskData.value.some(selected => {
        const selectedKey = `${selected.docNo}_${selected.docSeq}_${selected.docType}`;
        return selectedKey === itemKey;
    });

    if (!isDuplicate) {
        selectedRiskData.value.push(item);
        updateRiskPlanPrevList();
    }
};

// 체크 박스 해제하는 경우
const handleItemDeselection = item => {
    if (props.single) {
        selectedRiskData.value = null;
    } else {
        if (selectedRiskData.value && Array.isArray(selectedRiskData.value)) {
            const itemKey = `${item.docNo}_${item.docSeq}_${item.docType}`;
            const itemIndex = selectedRiskData.value.findIndex(selected => {
                const selectedKey = `${selected.docNo}_${selected.docSeq}_${selected.docType}`;
                return selectedKey === itemKey;
            });

            if (itemIndex > -1) {
                selectedRiskData.value.splice(itemIndex, 1);

                if (selectedRiskData.value.length === 0) {
                    selectedRiskData.value = null;
                    removeFromRiskPlanPrevList();
                } else {
                    updateRiskPlanPrevList();
                }
            }
        }
    }
};

// ===== 계획 관련 =====
// 년도 변경
const changedYear = () => {
    selectedPlan.value = null;
    selectedRiskData.value = null;
    emit('change', null);
};

// 계획 팝업 오픈
const openRiskAssesmentPlan = () => {
    if (!selectedYear.value) {
        return;
    }

    param.value = {
        compId: getCompId(),
        writeYear: selectedYear.value,
        orgnId: props.orgnItem?.orgnId,
        docNo: props.riskPlanData?.docNo,
        riskAssessmentStandards: props.riskPlanData?.riskAssessmentStandards
    };

    riskPlanPopup.value.onOpen();
};

// 계획 chips 제거
const removeRiskAssesmentPlan = () => {
    selectedPlan.value = null;
    selectedRiskData.value = null;
    emit('change', null);
};

// 계획 팝업 닫기
const closeRiskAssesmentPlan = async el => {
    selectedPlan.value = null;
    selectedRiskData.value = null;

    if (el && el.length > 0) {
        selectedPlan.value = {
            writeYear: el[0].writeYear,
            docType: el[0].docType,
            docNo: el[0].docNo,
            id: el[0].docNo,
            name: `${el[0].planNm}`
        };

        riskImplStore.riskAllowanceStandards = el[0].planNm;
        emit('change', selectedPlan.value);
    } else {
        emit('change', null);
    }

    riskPlanPopup.value.onClose();
};

// ===== 데이터 관리 =====
// 위험성 평가 이행 데이터 변경
const updateRiskPlanPrevList = () => {
    if (!selectedPlan.value || !selectedRiskData.value) return;

    const selectionData = {
        selectedRiskData: [...selectedRiskData.value],
        planData: selectedPlan.value
    };

    const existingIndex = riskAssessmentPlanStore.riskPlanPrevList.findIndex(prev => prev.planData?.docNo === selectedPlan.value?.docNo);

    if (existingIndex >= 0) {
        riskAssessmentPlanStore.riskPlanPrevList[existingIndex] = selectionData;
    } else {
        riskAssessmentPlanStore.riskPlanPrevList.push(selectionData);
    }
};

// 위험성 평가 이행 데이터 제거
const removeFromRiskPlanPrevList = () => {
    if (!selectedPlan.value) return;

    const removeIndex = riskAssessmentPlanStore.riskPlanPrevList.findIndex(prev => prev.planData?.docNo === selectedPlan.value?.docNo);

    if (removeIndex >= 0) {
        riskAssessmentPlanStore.riskPlanPrevList.splice(removeIndex, 1);
    }
};

// 이 전에 선택한 데이터를 체크
const checkPreviouslySelectedData = () => {
    if (!props.implList?.list || !riskAssessmentPlanStore.riskPlanPrevList?.length || !selectedPlan.value) {
        return;
    }

    // 선택한 위험성 평가 계획 팝업 데이터
    const currentPlanDocNo = selectedPlan.value.docNo;
    const currentPlanDocType = selectedPlan.value.docType;
    const currentPlanWriteYear = selectedPlan.value.writeYear;

    const matchingPrevPlan = riskAssessmentPlanStore.riskPlanPrevList.find(prevPlan => {
        if (!prevPlan.planData) return false;

        return prevPlan.planData.docNo === currentPlanDocNo && prevPlan.planData.docType === currentPlanDocType && prevPlan.planData.writeYear === currentPlanWriteYear;
    });

    if (!matchingPrevPlan || !matchingPrevPlan.selectedRiskData) {
        return;
    }

    selectedRiskData.value = null;

    // 선택한 계획 중에서 선택한 이행 데이터가 있는지 판별
    props.implList.list.forEach((impl, implIndex) => {
        impl.implRiskAssList.forEach((riskItem, riskIndex) => {
            if (Array.isArray(matchingPrevPlan.selectedRiskData)) {
                const isSelected = matchingPrevPlan.selectedRiskData.some(selected => {
                    return selected.docNo === riskItem.docNo && selected.docSeq === riskItem.docSeq && selected.docType === riskItem.docType;
                });

                if (isSelected) {
                    riskItem.checked = true;

                    if (!selectedRiskData.value) {
                        selectedRiskData.value = [];
                    }

                    const isDuplicate = selectedRiskData.value.some(existing => existing.docNo === riskItem.docNo && existing.docSeq === riskItem.docSeq && existing.docType === riskItem.docType);

                    if (!isDuplicate) {
                        selectedRiskData.value.push(riskItem);
                    }
                }
            }
        });
    });
};

// ===== watchers =====
// 계획 chips 변경 감지
watch(
    () => props.selectedRiskInfo,
    newVal => {
        if (newVal) {
            selectedRiskData.value = newVal;
        }
    },
    { deep: true }
);

// 이행 데이터 변경 감지
watch(
    () => props.implList,
    newImplList => {
        if (newImplList && newImplList.list) {
            checkPreviouslySelectedData();
        }
    },
    { deep: true }
);

// ===== 생명주기 =====
onMounted(async () => {
    if (props.selectedRiskInfo) {
        selectedRiskData.value = props.selectedRiskInfo;
    }

    checkPreviouslySelectedData();
});

// ===== defineExpose =====
defineExpose({ open, close });
</script>
