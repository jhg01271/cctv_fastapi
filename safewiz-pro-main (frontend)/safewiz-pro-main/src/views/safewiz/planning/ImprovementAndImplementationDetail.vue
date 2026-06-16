<template>
    <div class="contents">
        <div class="box pa2-2rem us-pa1rem">
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
                <div class="accordion form ui">
                    <div class="list">
                        <button type="button" id="accordion-btn_0" class="df jcsb aic wbka" @click="thisStore.toggleAccordion">
                            <span class="df aic">
                                {{ thisStore.inputForm.planNm }}
                                <img src="/assets/img/common/icon_step.svg" alt="" />
                                {{ thisStore.inputForm.processNm }}
                                <img src="/assets/img/common/icon_step.svg" alt="" />
                                {{ thisStore.inputForm.workContent }}
                            </span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh">
                            <div class="pa2-2rem bcF8F9FB">
                                <div class="field">
                                    <label for="">{{ t('imprvAndImplmntt_harmFulRiskFactors') }}</label>
                                    <textarea class="radius minh8rem w100p" v-model="thisStore.inputForm.hazardsFactor" readonly></textarea>
                                </div>

                                <div class="df aic gap1rem es-fww">
                                    <div class="field w50p es-w100p">
                                        <label for="">{{ t('imprvAndImplmntt_classification') }}</label>
                                        <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.hazardsClassificationNm" />
                                    </div>
                                    <div class="field w50p es-w100p">
                                        <label for="">{{ t('imprvAndImplmntt_relatedEvidence') }}</label>
                                        <input type="text" v-input class="w100p radius" readonly :value="thisStore.inputForm.relatedEvidence ? `${thisStore.inputForm.legalNm} - ${thisStore.inputForm.relatedEvidence}` : ''" />
                                    </div>
                                </div>
                                <div class="field">
                                    <label for="">{{ t('imprvAndImplmntt_cause') }}</label>
                                    <textarea class="radius minh8rem w100p" v-model="thisStore.inputForm.hazardsCause" readonly></textarea>
                                </div>
                                <div class="field">
                                    <label for="">{{ t('imprvAndImplmntt_currentSafetyMeasures') }}</label>
                                    <textarea class="radius minh8rem w100p" v-model="thisStore.inputForm.currentSafetyMeasures" readonly></textarea>
                                </div>

                                <div class="df gap1rem aic us-fww us-mb2-2rem">
                                    <div class="field">
                                        <label for="">{{ t('imprvAndImplmntt_currentRiskLevel') }}</label>
                                    </div>
                                    <button v-if="thisStore.inputForm.riskAssessmentStandards == '3b3'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="riskPopup.onOpen()">
                                        {{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 3 * 3` }}
                                    </button>
                                    <button v-if="thisStore.inputForm.riskAssessmentStandards == '5b4'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="riskPopup.onOpen()">
                                        {{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 5 * 4` }}
                                    </button>
                                    <button v-if="thisStore.inputForm.riskAssessmentStandards == '3a'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="riskPopup.onOpen()">
                                        {{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 상/중/하` }}
                                    </button>
                                </div>
                                <div class="bcF9FAFF br4px pa2-2rem pt1rem">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-3 sm-grid12-12" v-if="thisStore.inputForm.riskAssessmentStandards != '3a'">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_likelihoodFrequency') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.frequencyScore" />
                                            </div>
                                        </div>
                                        <div class="grid12-3 sm-grid12-12" v-if="thisStore.inputForm.riskAssessmentStandards != '3a'">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_seriousnessIntensity') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.impactScore" />
                                            </div>
                                        </div>
                                        <div class="grid12-3 sm-grid12-12">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_riskDetermination') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.riskScore" />
                                            </div>
                                        </div>
                                        <div class="grid12-3 sm-grid12-12">
                                            <div class="field">
                                                <label for="">
                                                    <span
                                                        >허용 기준 <span v-if="thisStore.riskAllowanceStandards" class="c00129F bc50-72-246-10 px5px py3px br4px">{{ thisStore.riskAllowanceStandards }}</span></span
                                                    >
                                                </label>
                                                <input type="text" v-input class="w100p" readonly :value="thisStore.riskAllowance" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="df gap1rem aic us-fww us-mb2-2rem" v-if="!thisStore.isRiskAllowance || thisStore.inputForm.workDetail.length > 0">
                                    <div class="field">
                                        <label for="">{{ t('imprvAndImplmntt_residualRiskLevel') }}</label>
                                    </div>
                                </div>
                                <div class="bcF9FAFF br4px pa2-2rem pt1rem" v-if="!thisStore.isRiskAllowance || thisStore.inputForm.workDetail.length > 0">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-4 sm-grid12-12" v-if="thisStore.inputForm.riskAssessmentStandards != '3a'">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_likelihoodFrequency') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.afterFrequencyScore" />
                                            </div>
                                        </div>
                                        <div class="grid12-4 sm-grid12-12" v-if="thisStore.inputForm.riskAssessmentStandards != '3a'">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_seriousnessIntensity') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.afterImpactScore" />
                                            </div>
                                        </div>
                                        <div class="grid12-4 sm-grid12-12">
                                            <div class="field">
                                                <label for="">{{ t('imprvAndImplmntt_riskDetermination') }}</label>
                                                <input type="text" v-input class="w100p radius" readonly v-model="thisStore.inputForm.afterRiskScore" />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- 🐳2-4 Accordion-->
                                <div class="accordion form ui mt2rem df fdc rg8px">
                                    <div v-for="(work, index) in thisStore.inputForm.workDetail" :key="`work_${index}`" class="list">
                                        <button type="button" class="df jcsb aic" @click="thisStore.toggleAccordion">
                                            <!--🐟 2-3 Accordion Title -->
                                            <div class="df aic wbka es-fww es-tal">
                                                <p class="es-tal">{{ `${t('imprvAndImplmntt_reductionMeasures')}: ${work.reductionMeasures}` }}</p>
                                                <span class="summary es-mt1rem"
                                                    >{{ t('imprvAndImplmntt_completionDate') }}<i class="c00129F">{{ work.completedDate }}</i></span
                                                >
                                            </div>
                                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <div class="segment oh">
                                            <div class="pa2-2rem pt1-4rem">
                                                <!-- 🐳2-4 Accordion Content -->
                                                <div class="field">
                                                    <label for="">{{ t('imprvAndImplmntt_reductionMeasures') }}</label>
                                                    <textarea class="radius minh8rem w100p" v-model="work.reductionMeasures" readonly></textarea>
                                                </div>
                                                <div class="row flex gutters1rem">
                                                    <div class="grid12-6 sm-grid12-12">
                                                        <div class="field">
                                                            <label for="">{{ t('imprvAndImplmntt_manager') }}</label>
                                                            <i-chips readonly :chips="work.workDetailHr"></i-chips>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 sm-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label for="">{{ t('imprvAndImplmntt_expectedImprovementDate') }}</label>
                                                            <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly v-model="work.expectedDate" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 sm-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label for="">{{ t('imprvAndImplmntt_improvementCompletionDate') }}</label>
                                                            <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly v-model="work.completedDate" />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="field">
                                                    <label for="">{{ t('imprvAndImplmntt_remedialAction') }}</label>
                                                    <table>
                                                        <tbody>
                                                            <tr>
                                                                <th class="w50p">{{ t('imprvAndImplmntt_beforeImprovement') }}</th>
                                                                <th class="w50p">{{ t('imprvAndImplmntt_afterImprovement') }}</th>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <div class="df aic jcc fg1">
                                                                        <iFileImage ref="prevFileList" targetType="RAP" :targetId="work.prevFileId" readonly />
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div class="df aic jcc fg1">
                                                                        <iFileImage ref="afterFileList" targetType="RAP" :targetId="work.afterFileId" readonly />
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <div class="field">
                                                    <label for="">{{ t('imprvAndImplmntt_executionDetails') }}</label>
                                                    <textarea class="radius minh8rem w100p" v-model="work.implContent" readonly></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div></div
            ></OverlayScrollbarsComponent>
        </div>
    </div>

    <teleport to="body">
        <i-PopupDialog ref="riskPopup">
            <div class="contents w800px md-w100p">
                <div class="form ui">
                    <em v-if="thisStore.inputForm.riskAssessmentStandards == '3b3'" class="fs1-8rem">{{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 3 * 3` }}</em>
                    <em v-if="thisStore.inputForm.riskAssessmentStandards == '5b4'" class="fs1-8rem">{{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 5 * 4` }}</em>
                    <em v-if="thisStore.inputForm.riskAssessmentStandards == '3a'" class="fs1-8rem">{{ `${t('imprvAndImplmntt_riskEstimationCriteria')} 상/중/하` }}</em>
                    <div class="df fdc jcsb wbka">
                        <OverlayScrollbarsComponent
                            class="maxh80rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="field" v-if="thisStore.inputForm.riskAssessmentStandards !== '3a'">
                                <label for="">{{ t('imprvAndImplmntt_likelihoodFrequency') }}</label>
                                <OverlayScrollbarsComponent
                                    class="table-sticky"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <table class="es-minw64rem">
                                        <thead>
                                            <tr>
                                                <th class="w20p tac">{{ t('imprvAndImplmntt_possibility') }}</th>
                                                <th class="tac">{{ t('imprvAndImplmntt_standard') }}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in thisStore.sysFrequencyList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                            <div class="field" v-if="thisStore.inputForm.riskAssessmentStandards !== '3a'">
                                <label for="">{{ t('imprvAndImplmntt_seriousnessIntensity') }}</label>
                                <OverlayScrollbarsComponent
                                    class="table-sticky"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <table class="es-minw64rem">
                                        <thead>
                                            <tr>
                                                <th class="w20p tac">{{ t('imprvAndImplmntt_importance') }}</th>
                                                <th class="tac">{{ t('imprvAndImplmntt_standard') }}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in thisStore.sysImpactList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                            <div class="field">
                                <label for="">{{ t('imprvAndImplmntt_riskDetermination') }}</label>
                                <OverlayScrollbarsComponent
                                    class="table-sticky"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <table class="es-minw64rem">
                                        <thead>
                                            <tr>
                                                <th class="w20p tac">{{ t('imprvAndImplmntt_riskLevel') }}</th>
                                                <th class="tac">{{ t('imprvAndImplmntt_managementStandards') }}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in thisStore.sysRiskScorePopupList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </OverlayScrollbarsComponent>
                        <div class="df aic jcfe">
                            <button type="button" class="btn radius w8rem fr mt2-2rem" @click="riskPopup.onClose()">{{ t('close') }}</button>
                        </div>
                    </div>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useImprovementAndImplementationDetailStore } from '@/stores/safewiz/planning/ImprovementAndImplementationDetail.js';
import { useButtonListStore } from '@/stores/buttonList';
import iFileImage from '@/components/file/iFileImage.vue';
import { getDateFormat } from '@/utils/dateUtil.js';

const { ref, onMounted, t, btnBack, btnDownload, btnSearch } = BaseView();
const thisStore = useImprovementAndImplementationDetailStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload'];

const prevFileList = ref([]);
const afterFileList = ref([]);
const riskPopup = ref(null);

btnBack(() => {
    thisStore.goBack();
});

btnSearch(() => {
    thisStore.searchData(true, prevFileList, afterFileList);
});

btnDownload(() => {
    thisStore.print();
});

onMounted(() => {
    thisStore.riskAllowanceStandards = '';
    thisStore.searchData(true, prevFileList, afterFileList);
});
</script>
<style scoped lang="scss">
.accordion {
    // 아코디언 헤더 버튼 요약 정보
    .summary {
        font-size: 1.4rem;
        font-weight: 400;
        display: flex;
        align-items: center;
        gap: 6px;
        padding-left: 12px;
        margin-left: 12px;
        position: relative;
        @media (max-width: 640px) {
            padding-left: 0;
            margin-left: 0;
        }
        &::before {
            display: inline-block;
            width: 1px;
            height: 0.8em;
            background-color: #e1e6ed;
            left: 0;
            @media (max-width: 640px) {
                display: none;
            }
        }

        i {
            border-radius: 4px;
            padding: 3px 6px;
            position: relative;

            &::before {
                display: inline-block;
                width: 100%;
                height: 100%;
                left: 0;
                top: 0;
                border-radius: 4px;
                background-color: currentColor;
                opacity: 0.1;
            }
        }
    }
}

.form {
    table {
        tbody {
            tr:first-child {
                th {
                    border-top: 1px solid #e1e6ed;
                }
            }
        }
    }
}
</style>
