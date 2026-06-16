<!-- 
 작성자 : SI3팀 김현재
 작성일 : 2024.11.13
 화면개요 : 위험성평가 이행 화면에서 추가 버튼을 눌렀을때 추가되는 컴포넌트
  -->
<template>
    <div class="segment oh">
        <div class="pa2-2rem pt1-4rem">
            <!-- 🐳2-3 Accordion Content -->
            <div :id="`form${props.index}`">
                <div class="field df aic jcsb">
                    <label for="" required><span>유해·위험요인</span></label>
                    <div class="field df aic gap8px">
                        <label for=""><span>사용여부</span></label> <input v-model="useYn" v-input="'사용'" true-value="Y" false-value="N" type="checkbox" class="df switch" @change="onInputChange('useYn', props.implData.useYn)" :disabled="props.readonly" />
                    </div>
                </div>
                <div class="field">
                    <label :for="'hazardsFactor' + props.index" required v-show="false"><span>유해·위험요인</span></label>
                    <textarea :id="'hazardsFactor' + props.index" label="유해·위험요인" class="radius minh8rem w100p" placeholder="유해·위험 요인을 입력해주세요." v-model="hazardsFactor" @input="onInputChange('hazardsFactor')" :readonly="props.readonly" required></textarea>
                </div>

                <div class="df aic gap1rem es-fww">
                    <div class="field w50p es-w100p">
                        <label required>
                            <span>분류</span>
                        </label>
                        <i-chips required :chips="props.implData.hazardsClassification ? [{ id: props.implData.hazardsClassification, name: props.implData.implementationNm }] : []" @popup="openFactorPopup" @removeChip="removeFactor" @close="closeFactorPopup" class="w100p" :readonly="props.readonly" />
                    </div>

                    <div class="field w50p es-w100p">
                        <label for="">
                            <span>관련근거</span>
                        </label>
                        <i-chips :chips="props.implData.relatedEvidenceItem ? [{ id: props.implData.relatedEvidenceItem[0]?.id, name: `${props.implData.relatedEvidenceItem[0]?.legalNm} - ${props.implData.relatedEvidenceItem[0]?.name}` }] : []" @popup="addLegalReviewPop" @removeChip="removeLegalReview" class="w100p" :readonly="props.readonly" />
                    </div>
                </div>
                <div class="field">
                    <label for="">
                        <span>원인</span>
                    </label>
                    <textarea class="radius minh8rem w100p" placeholder="유해·위험요인 원인을 입력해주세요." v-model="hazardsCause" @input="onInputChange('hazardsFactor')" :readonly="props.readonly"></textarea>
                </div>
                <div class="field">
                    <label for="">
                        <span>현재 안전보건 조치</span>
                    </label>
                    <textarea class="radius minh8rem w100p" placeholder="현재 안전보건 조치를 입력해주세요." v-model="currentSafetyMeasures" @input="onInputChange('currentSafetyMeasures')" :readonly="props.readonly"></textarea>
                </div>

                <!-- 현재 위험성 수준 -->
                <div class="my1rem df gap2rem aic us-w100p us-fww us-rg0">
                    <div class="field">
                        <label for="" required>
                            <span>{{ t('imprvAndImplmntt_currentRiskLevel') }}</span>
                        </label>
                    </div>
                    <button v-if="implData.riskAssessmentStandards == '3b3'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="openRistTimationCriteria">
                        <span>{{ t('riskAssessment_standards') }} 3 * 3</span>
                    </button>
                    <button v-if="implData.riskAssessmentStandards == '5b4'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="openRistTimationCriteria">
                        <span>{{ t('riskAssessment_standards') }} 5 * 4</span>
                    </button>
                    <button v-if="implData.riskAssessmentStandards == '3a'" type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="openRistTimationCriteria">
                        <span>{{ t('riskAssessment_standards') }} 상/중/하</span>
                    </button>
                </div>

                <!-- 현재 위험성 수준 - 3b3, 5b4 -->
                <div class="bcF9FAFF br4px pa2-2rem pt1rem" v-if="implData.riskAssessmentStandards !== '3a'">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label :for="'frequency' + props.index">
                                    <span>{{ t('imprvAndImplmntt_likelihoodFrequency') }}</span>
                                </label>
                                <select :id="'frequency' + props.index" :title="t('imprvAndImplmntt_likelihoodFrequency')" required v-select class="w100p" v-model="frequency" @change="setRiskScore(null, 'frequencyScore')">
                                    <option v-for="(item, index) in frequencyList" :key="index" :value="item.minorCd" :disabled="props.readonly">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label :for="'impact' + props.index">
                                    <span>{{ t('imprvAndImplmntt_seriousnessIntensity') }}</span>
                                </label>
                                <select :id="'impact' + props.index" :title="t('imprvAndImplmntt_seriousnessIntensity')" required v-select class="w100p" v-model="impact" @change="setRiskScore(null, 'impactScore')">
                                    <option v-for="(item, index) in impactList" :key="index" :value="item.minorCd" :disabled="props.readonly">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field" :key="riskScore">
                                <label for="">
                                    <span>{{ t('imprvAndImplmntt_riskDetermination') }}</span>
                                </label>
                                <input v-input type="text" :value="getRiskScoreName(riskScore)" :disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span
                                        >허용 기준 <span v-if="riskImplStore.implRiskAllowanceStandards" class="c00129F bc50-72-246-10 px5px py3px br4px">{{ riskImplStore.implRiskAllowanceStandards }}</span></span
                                    >
                                </label>
                                <input type="text" v-input class="w100p" readonly :value="riskImplStore.riskAllowance[props.index]" />
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 현재 위험성 수준 - 3a -->
                <div class="bcF9FAFF br4px pa2-2rem pt1rem" v-if="implData.riskAssessmentStandards === '3a'">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field" :key="riskScore">
                                <label :for="'riskScore' + props.index">
                                    <span>{{ t('imprvAndImplmntt_riskDetermination') }}</span>
                                </label>
                                <select :id="'riskScore' + props.index" :title="t('imprvAndImplmntt_riskDetermination')" required v-select class="w100p" v-model="riskScore" @change="setRiskScore(null, 'riskScore')">
                                    <option v-for="(item, index) in riskScoreList" :key="index" :value="item.minorCd" :disabled="props.readonly || implData.riskAssessmentStandards !== '3a'">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span
                                        >허용 기준 <span v-if="riskImplStore.implRiskAllowanceStandards" class="c00129F bc50-72-246-10 px5px py3px br4px">{{ riskImplStore.implRiskAllowanceStandards }}</span></span
                                    >
                                </label>
                                <input type="text" v-input class="w100p" readonly :value="riskImplStore.riskAllowance[props.index]" />
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 개선 위험성 수준 - 허용 불가능한 위험일 경우이거나, 감소대책이 있을 경우 출력 -->
                <div class="df gap1rem aic us-fww us-mb2-2rem" v-if="!riskImplStore.isRiskAllowance[props.index] || implData.implementReduList.filter(el => el.reductionCreatedAt != null).length > 0">
                    <div class="field">
                        <label for="" required>
                            <span>{{ t('imprvAndImplmntt_residualRiskLevel') }}</span>
                        </label>
                    </div>
                </div>
                <!-- 개선 위험성 수준 - 3b3, 5b4 -->
                <div class="bcF9FAFF br4px pa2-2rem pt1rem" v-if="(!riskImplStore.isRiskAllowance[props.index] && implData.riskAssessmentStandards !== '3a') || (implData.riskAssessmentStandards !== '3a' && implData.implementReduList.filter(el => el.reductionCreatedAt != null).length > 0)">
                    <div class="row flex gutters1rem">
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field">
                                <label :for="'afterFrequency' + props.index">
                                    <span>{{ t('imprvAndImplmntt_likelihoodFrequency') }}</span>
                                </label>
                                <select :id="'afterFrequency' + props.index" :title="t('imprvAndImplmntt_likelihoodFrequency')" required v-select class="w100p" v-model="afterFrequency" @change="setRiskScore(null, 'afterFrequencyScore')">
                                    <option v-for="(item, index) in frequencyList" :key="index" :value="item.minorCd" :disabled="props.readonly">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field">
                                <label :for="'afterImpact' + props.index">
                                    <span>{{ t('imprvAndImplmntt_seriousnessIntensity') }}</span>
                                </label>
                                <select :id="'afterImpact' + props.index" :title="t('imprvAndImplmntt_seriousnessIntensity')" required v-select class="w100p" v-model="afterImpact" @change="setRiskScore(null, 'afterImpactScore')">
                                    <option v-for="(item, index) in impactList" :key="index" :value="item.minorCd" :disabled="props.readonly">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field" :key="afterRiskScore">
                                <label for="">
                                    <span>{{ t('imprvAndImplmntt_riskDetermination') }}</span>
                                </label>
                                <input v-input type="text" :value="getAfterRiskScoreName(afterRiskScore)" :disabled="true" />
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 개선 위험성 수준 - 3a -->
                <div class="bcF9FAFF br4px pa2-2rem pt1rem" v-if="(!riskImplStore.isRiskAllowance[props.index] && implData.riskAssessmentStandards === '3a') || (implData.riskAssessmentStandards === '3a' && implData.implementReduList.filter(el => el.reductionCreatedAt != null).length > 0)">
                    <div class="row flex gutters1rem">
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field" :key="afterRiskScore">
                                <label :for="'afterRiskScore' + props.index">
                                    <span>{{ t('imprvAndImplmntt_riskDetermination') }}</span>
                                </label>
                                <select :id="'afterRiskScore' + props.index" :title="t('imprvAndImplmntt_riskDetermination')" required v-select class="w100p" v-model="afterRiskScore" @change="setRiskScore(null, 'afterRiskScore')">
                                    <option v-for="(item, index) in riskScoreList" :key="index" :value="item.minorCd" :disabled="props.readonly || implData.riskAssessmentStandards !== '3a'" required>{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 🐳2-4 Accordion-->
            <div v-for="(red, index) in implData.implementReduList" :key="index">
                <div class="accordion form ui mt2rem df fdc gap8px tal">
                    <div class="list" v-if="red.reductionCreatedAt != null">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion" :id="`reduction${props.index}${index}`">
                            <!--🐟 2-3 Accordion Title -->
                            <div class="df aic gap1rem es-fww es-rg1rem">
                                <div class="df aic gap8px tal">
                                    <input type="checkbox" v-input v-model="red.checked" :disabled="props.readonly" />
                                    <p :class="red.useYn === 'Y' ? '' : 'c8e8e8e'">
                                        감소대책 : {{ red.reductionMeasures }}
                                        <span v-if="red.useYn === 'N'">(미사용) </span>
                                    </p>
                                </div>
                                <!-- 완료일 노출 기준은 개선 완료일이 not null일때 노출 -->
                                <span v-if="red.completedDate != null && red.completedDate != ''" class="summary"
                                    >완료일<i class="c00129F">{{ red.completedDate }}</i></span
                                >
                            </div>
                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh" :id="`form2${props.index}${index}`">
                            <div class="pa2-2rem pt1-4rem bcFFFFFF">
                                <!-- 🐳2-4 Accordion Content -->
                                <div class="df aic gap1rem es-fww">
                                    <div class="field w80p es-w100p">
                                        <label :for="'reductionMeasures' + props.index + index" required>
                                            <span>감소대책</span>
                                        </label>
                                        <textarea :id="'reductionMeasures' + props.index + index" class="w100p minh8rem radius wbka" placeholder="감소대책을 입력해주세요." v-model="red.reductionMeasures" @input="onReudctionChange(index, 'reductionMeasures')" :readonly="props.readonly" required></textarea>
                                    </div>
                                    <div class="field w20p es-w100p">
                                        <label for="">사용여부</label>
                                        <div class="df aic h4-4rem">
                                            <input v-model="red.useYn" id="" v-input="'사용'" true-value="Y" false-value="N" type="checkbox" class="df switch" @change="onReudctionChange(index, 'useYn')" :disabled="props.readonly" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row flex gutters2rem sm-gutters0">
                                    <div class="grid12-6 sm-grid12-12">
                                        <div class="field">
                                            <label for="">
                                                <span>담당자</span>
                                            </label>
                                            <i-chips :chips="red.hrList || []" @popup="addPeople(index)" :readonly="props.readonly"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-12">
                                        <div class="field">
                                            <label for="">
                                                <span>개선 예정일</span>
                                            </label>
                                            <input v-model="red.expectedDate" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :placeholder="today" @input="onReudctionChange(index, 'expectedDate')" :readonly="props.readonly" />
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-12">
                                        <div class="field">
                                            <label for="">
                                                <span>개선 완료일</span>
                                            </label>
                                            <input v-model="red.completedDate" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :placeholder="today" @input="onReudctionChange(index, 'completedDate')" :readonly="props.readonly" />
                                        </div>
                                    </div>
                                </div>
                                <div class="field">
                                    <label for="">
                                        <span>개선 조치</span>
                                    </label>
                                    <OverlayScrollbarsComponent
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="es-minw64rem">
                                            <tbody>
                                                <tr class="tac">
                                                    <th class="w50p">개선 전</th>
                                                    <th class="w50p">개선 후</th>
                                                </tr>
                                                <tr class="tac">
                                                    <td>
                                                        <!-- 개선 전 첨부파일 -->
                                                        <div class="df aic jcc fg1">
                                                            <iFileImage ref="localFileList" targetType="RAP" :targetId="red.prevFileId" :readonly="props.readonly" />
                                                        </div>
                                                        <!-- 이미지 첨부 개발 후 퍼블리싱 예정 -->
                                                    </td>
                                                    <td>
                                                        <!-- 개선 후 첨부파일 -->
                                                        <div class="df aic jcc fg1">
                                                            <iFileImage ref="localFileList2" targetType="RAP" :targetId="red.afterFileId" :readonly="props.readonly" />
                                                        </div>
                                                        <!-- 이미지 첨부 개발 후 퍼블리싱 예정 -->
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                                <div class="field">
                                    <label for="">
                                        <span>실행내용</span>
                                    </label>
                                    <textarea class="radius minh8rem w100p" placeholder="실행내용을 입력해주세요." v-model="red.implContent" @input="onReudctionChange(index, 'implContent')" :readonly="props.readonly"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 감소대책 추가 -->
            <button type="button" class="df jcc aic gap8px w100p radius mt1-5rem" @click="addReduction" v-if="props.readonly !== true">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                </svg>
                <span class="fs1-5rem fw400">감소대책 추가</span>
            </button>
        </div>
    </div>
    <!-- 위험성 추정 기준 팝업 3*3 -->
    <teleport to="body">
        <i-PopupDialog ref="riskEstimationCriteria">
            <div class="contents w800px md-w100p">
                <div class="form ui">
                    <em v-if="implData.riskAssessmentStandards == '3b3'" class="fs1-8rem">위험성 추정 기준 3 * 3</em>
                    <em v-if="implData.riskAssessmentStandards == '5b4'" class="fs1-8rem">위험성 추정 기준 5 * 4</em>
                    <em v-if="implData.riskAssessmentStandards == '3a'" class="fs1-8rem">위험성 추정 기준 상/중/하</em>

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
                            <div class="field" v-if="implData.riskAssessmentStandards !== '3a'">
                                <label for="">가능성 (빈도)</label>
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
                                                <th class="w20p tac">가능성</th>
                                                <th class="tac">기준</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in frequencyList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                            <div class="field" v-if="implData.riskAssessmentStandards !== '3a'">
                                <label for="">중대성 (빈도)</label>
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
                                                <th class="w20p tac">중대성</th>
                                                <th class="tac">기준</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in impactList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                            <div class="field">
                                <label for="">위험성 결정</label>
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
                                                <th class="w20p tac">위험성 수준</th>
                                                <th class="tac">관리 기준</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in riskScoreStandardsList" :key="index">
                                                <td class="tac">{{ item.minorNm }}</td>
                                                <td class="tal">{{ item.remark }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </OverlayScrollbarsComponent>
                    </div>
                    <div class="df aic jcfe">
                        <button type="button" class="btn radius w8rem fr mt2-2rem" @click="riskEstimationCriteria.onClose()">닫기</button>
                    </div>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>

    <teleport to="body">
        <!-- 인원 선택 팝업 -->
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser @selected="selectPeople" @close="handlePopupClose"></selectUser>
            </div>
        </i-PopupDialog>
        <!-- 유해위험요인 분류 팝업 -->
        <i-PopupDialog ref="factorPopup">
            <div class="contents w500px md-w100p">
                <BaseSelectPopup
                    :title="'유해위험요인 분류'"
                    filterKey="classNm"
                    useYnKey="useYn"
                    :excluded-value="'N'"
                    :single="true"
                    :fetch-data="
                        () => {
                            return { list: factorList };
                        }
                    "
                    :btnInfo="{ close: true }"
                    @close="closeFactorPopup"
                />
            </div>
        </i-PopupDialog>
        <!-- 관련 근거 팝업 -->
        <i-PopupDialog ref="legalReviewPopup">
            <div class="contents w800px md-w100p">
                <LegalReviewPopup @close="closeLegalReviewPop" @selected="onSelectLawData" />
                <div class="form ui tar mt2-5rem">
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeLegalReviewPop">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { nextTick, onMounted, defineProps, ref, watch, computed } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// baseview
import BaseView from '@/components/base/BaseView';
const { t, getCurrentDate, validationStore } = BaseView();
// select User
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();
// legalReview Popup
import LegalReviewPopup from '@/views/system/base/popup/LegalReviewPopup.vue';
import { useLegalReviewDetailStore } from '@/stores/safewiz/planning/LegalReviewDetail';
const legalReviewDetailStore = useLegalReviewDetailStore();
// riskImplStore
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
const riskImplStore = useRiskAssessmentImplementation();
// file
import iFileImage from '@/components/file/iFileImage.vue';
//props 선언부
const props = defineProps({
    // 분류 list
    factorList: {
        type: Array,
        default: () => []
    },
    // data implData
    implData: {
        type: Object,
        default: () => ({})
    },
    // 감소대책 데이터
    reductionList: {
        type: Array,
        default: () => []
    },
    // 개선 전 파일 업로드
    fileList: {
        type: Array,
        default: () => []
    },
    // 개선 후 파일 업로드
    fileList2: {
        type: Array,
        default: () => []
    },
    // index
    index: {
        type: String,
        default: null
    },
    readonly: {
        type: Boolean,
        default: false
    },
    showPrevBtn: {
        type: Boolean,
        default: false
    },
    frequencyList: {
        type: Array,
        default: () => []
    },
    impactList: {
        type: Array,
        default: () => []
    },
    riskScoreList: {
        type: Array,
        default: () => []
    }
});

// emit 선언
const emit = defineEmits(['update:implData']);

// computed 속성 - props를 직접 수정하지 않도록
const hazardsFactor = computed({
    get: () => props.implData.hazardsFactor,
    set: value => {
        emit('update:implData', { ...props.implData, hazardsFactor: value });
    }
});

const hazardsClassification = computed({
    get: () => props.implData.hazardsClassification,
    set: value => {
        emit('update:implData', { ...props.implData, hazardsClassification: value });
    }
});

const implementationNm = computed({
    get: () => props.implData.implementationNm,
    set: value => {
        emit('update:implData', { ...props.implData, implementationNm: value });
    }
});

const useYn = computed({
    get: () => props.implData.useYn,
    set: value => {
        emit('update:implData', { ...props.implData, useYn: value });
    }
});

const hazardsCause = computed({
    get: () => props.implData.hazardsCause,
    set: value => {
        emit('update:implData', { ...props.implData, hazardsCause: value });
    }
});

const currentSafetyMeasures = computed({
    get: () => props.implData.currentSafetyMeasures,
    set: value => {
        emit('update:implData', { ...props.implData, currentSafetyMeasures: value });
    }
});

const implementData = ref(JSON.parse(JSON.stringify(props.implData))); // 데이터 변경 감지를 위한 copy data
const reductionListCopy = ref(JSON.parse(JSON.stringify(props.implData.implementReduList))); // 데이터 변경 감지를 위한 copy data
const riskEstimationCriteria = ref(null); // 위험성추정기준표 (3*3, 5*4)
const frequencyList = ref(props.frequencyList); // 위험성 가능성(빈도)
const impactList = ref(props.impactList); // 위험성 중대성(강도)
const riskScoreList = ref(props.riskScoreList); // 위험성 결정 결과 리스트
const riskScoreStandardsList = ref([]); // 위험성 결정 팝업 리스트

const legalReviewPopup = ref(null); // 관련 근거 팝업
const peoplePopup = ref(null); // 담장자 선택 팝업

const frequency = ref(props.implData.frequencyScore || ''); // 선택된 위험성 가능성(빈도)
const impact = ref(props.implData.impactScore || ''); // 선택된 위험성 중대성(강도)
const riskScore = ref(props.implData.riskScore || ''); // 선택된 유해 위험 요인 위험성 결정

const afterFrequency = ref(props.implData.afterFrequencyScore || ''); // 선택된 개선 위험성 가능성(빈도)
const afterImpact = ref(props.implData.afterImpactScore || ''); // 선택된 개선 위험성 중대성(강도)
const afterRiskScore = ref(props.implData.afterRiskScore || ''); // 선택된 개선 유해 위험 요인 위험성 결정

const hrIndex = ref('');
const isUpdatingHrList = ref(false); // 담당자 변경 중 무한 루프 방지 플래그
let localFileList = ref([...props.fileList]); // props에서 받은 개선 전 이미지 fileList를 컴포넌트로 관리
let localFileList2 = ref([...props.fileList2]); // props에서 받은 개선 후 이미지 fileList를 컴포넌트로 관리
const today = getCurrentDate(); // 개선 예정일, 완료일 placeholder 들어갈 오늘 날짜

onMounted(async () => {
    // 위험성 허용 기준 필터링
    riskImplStore.implRiskAllowanceStandards = '';

    if (riskImplStore.riskAllowanceStandards || props.implData.implRiskAllowanceStandards) {
        const fullStandard = props.implData.implRiskAllowanceStandards || riskImplStore.riskAllowanceStandards;
        const [standardType, allowLevel] = fullStandard.split('_');
        riskImplStore.riskAllowanceStandards = fullStandard;

        // '허용 가능한 위험'인지 판단에 필요한 기준 설정
        if (standardType === '3a') {
            // '3a'는 상/중/하 기준이므로 minorNm로 매핑
            const targetItem = riskScoreList.value.find(item => item.minorCd === fullStandard);
            riskImplStore.implRiskAllowanceStandards = targetItem?.minorNm || '';
        } else {
            // '3b3', '5b4' 등의 경우엔 그대로 점수만 사용
            riskImplStore.implRiskAllowanceStandards = allowLevel;
        }
    }

    // 현재 위험성 수준 결정 세팅x
    setRiskScore(null);

    // 개선 전 이미지 조회
    localFileList.value.forEach(item => {
        item.fnSearch();
    });
    // 개선 후 이미지 조회
    localFileList2.value.forEach(item => {
        item.fnSearch();
    });

    await riskImplStore.setRefs(props.index, localFileList, localFileList2, props.reductionList);
});

/*********************************
 * 값 변경 감지
 *********************************/
// 요인 값 변경 감지
const changeHandlers = {
    frequencyScore: () => frequency.value !== props.implData.frequencyScore,
    impactScore: () => impact.value !== props.implData.impactScore,
    riskScore: () => riskScore.value !== props.implData.riskScore,
    afterFrequencyScore: () => afterFrequency.value !== props.implData.afterFrequencyScore,
    afterImpactScore: () => afterImpact.value !== props.implData.afterImpactScore,
    afterRiskScore: () => afterRiskScore.value !== props.implData.afterRiskScore
};

const onInputChange = type => {
    const hasChanged = changeHandlers[type] ? changeHandlers[type]() : implementData.value[type] !== props.implData[type];

    if (hasChanged) {
        implementData.value.checked = true;
        emit('update:implData', { ...props.implData, checked: true });
    }
};
// 요인 > 관련근거 값 변경 감지
watch(
    () => implementData.value.relatedEvidenceItem,
    () => {
        emit('update:implData', { ...props.implData, checked: true });
    }
);
// 감소대책 값 변경 감지
const onReudctionChange = (index, type) => {
    if (props.reductionList?.[index]?.[type] != reductionListCopy?.value?.[index]?.[type]) {
        const updatedReduList = [...props.implData.implementReduList];
        updatedReduList[index] = { ...updatedReduList[index], checked: true };
        emit('update:implData', { ...props.implData, implementReduList: updatedReduList });
    }
};
// 감소대책 > 담당자 값 변경 감지
watch(
    () => props.implData.implementReduList,
    newList => {
        // isUpdatingHrList가 true면 watch 실행 중단 (무한 루프 방지)
        if (isUpdatingHrList.value) {
            return;
        }

        newList.forEach((newItem, index) => {
            const oldItem = implementData.value.implementReduList[index] || { hrList: [] }; // oldItem이 없을 경우 빈 배열로 초기화
            // id만 추출하여 Set 객체로 변환
            const newIds = new Set(newItem.hrList.map(item => item.id));
            const oldIds = new Set(oldItem.hrList.map(item => item.id));
            // 추가된 항목이 있는지 확인
            const addedIds = [...newIds].filter(id => !oldIds.has(id));
            // 삭제된 항목이 있는지 확인
            const removedIds = [...oldIds].filter(id => !newIds.has(id));

            // 담당자가 추가되거나 삭제된 경우
            if (addedIds.length > 0 || removedIds.length > 0) {
                // 변경 됐으면 감소대책 checked true
                const updatedReduList = [...props.implData.implementReduList];
                updatedReduList[index] = { ...updatedReduList[index], checked: true };
                emit('update:implData', { ...props.implData, implementReduList: updatedReduList });
                // implementData 업데이트하여 다음 비교에 사용
                implementData.value.implementReduList[index] = { ...updatedReduList[index] };
            }
        });
    },
    { deep: true }
);

watch(
    () => riskImplStore.fileInfo[props.index]?.map(item => JSON.parse(JSON.stringify(item.editFiles))), // 깊은 복사
    (newItem, oldItem) => {
        // 새로운 아이템에서 editFiles를 추출
        newItem?.forEach((item, index) => {
            const currentEditFiles = item;
            const copiedEditFiles = oldItem?.[index];

            // insert 배열 비교 (파일 정보 기준)
            const insertChanged = arraysEqual(currentEditFiles?.insert || [], copiedEditFiles?.insert || [], 'insert');
            // delete 배열 비교 (파일 ID 기준)
            const deleteChanged = arraysEqual(currentEditFiles?.delete || [], copiedEditFiles?.delete || [], 'delete');
            if (insertChanged || deleteChanged) {
                // checked 값을 true로 설정
                const updatedReduList = [...props.implData.implementReduList];
                updatedReduList[index] = { ...updatedReduList[index], checked: true };
                emit('update:implData', { ...props.implData, implementReduList: updatedReduList });
            }
        });
    },
    { deep: true }
);

watch(
    () => riskImplStore.fileInfo2[props.index]?.map(item => JSON.parse(JSON.stringify(item.editFiles))), // 깊은 복사
    (newItem, oldItem) => {
        // 새로운 아이템에서 editFiles를 추출
        newItem?.forEach((item, index) => {
            const currentEditFiles = item;
            const copiedEditFiles = oldItem?.[index];

            // insert 배열 비교 (파일 정보 기준)
            const insertChanged = arraysEqual(currentEditFiles?.insert || [], copiedEditFiles?.insert || [], 'insert');
            // delete 배열 비교 (파일 ID 기준)
            const deleteChanged = arraysEqual(currentEditFiles?.delete || [], copiedEditFiles?.delete || [], 'delete');
            if (insertChanged || deleteChanged) {
                // checked 값을 true로 설정
                const updatedReduList = [...props.implData.implementReduList];
                updatedReduList[index] = { ...updatedReduList[index], checked: true };
                emit('update:implData', { ...props.implData, implementReduList: updatedReduList });
            }
        });
    },
    { deep: true }
);

// 두 배열이 동일한지 비교하는 함수
function arraysEqual(arr1, arr2, type) {
    if (type == 'delete') {
        if (arr1[0] == arr2[0]) return false;
    } else if (type == 'insert') {
        if (arr1[0]?.previewUrl == arr2[0]?.previewUrl) return false;
    }
    return true;
}

/*********************************
 * 유해위험요인 분류 팝업 관련
 *********************************/
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const factorPopup = ref(null);
const openFactorPopup = () => {
    factorPopup.value.onOpen();
};
const removeFactor = () => {
    hazardsClassification.value = null;
    implementationNm.value = null;
    onInputChange('hazardsClassification');
};
const closeFactorPopup = e => {
    if (e.length > 0) {
        hazardsClassification.value = e[0].hazardsClassification;
        implementationNm.value = e[0].classNm;
        onInputChange('hazardsClassification');
    }
    factorPopup.value.onClose();
};
/*********************************
 * 관련근거 팝업 관련
 *********************************/
const addLegalReviewPop = () => {
    legalReviewPopup.value.onOpen();
};

const onSelectLawData = async el => {
    el.writeYear = getCurrentDate().substring(0, 4);

    legalReviewDetailStore.filteredLegalReviewDetailList.push({
        legalWriteYear: '',
        legalDocType: '',
        legalDocNo: '',
        legalDocSeq: '',
        legalArticleNm: '',
        currentlaws: ''
    });

    await legalReviewDetailStore.setLegalManageData(el, 0);

    // i-chips 용
    implementData.value.relatedEvidenceItem = [
        {
            id: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalId,
            name: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalArticleNm,
            legalNm: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalNm
        }
    ];

    // 저장 용 - emit으로 변경
    emit('update:implData', {
        ...props.implData,
        relatedEvidenceItem: [
            {
                id: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalId,
                name: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalArticleNm,
                legalNm: legalReviewDetailStore.filteredLegalReviewDetailList[0].legalNm
            }
        ]
    });

    if (legalReviewPopup.value) {
        legalReviewPopup.value.onClose();
    }

    legalReviewPopup.value.onClose();
};

const closeLegalReviewPop = () => {
    legalReviewPopup.value.onClose();
};

const removeLegalReview = async index => {
    implementData.value.relatedEvidenceItem = [];

    // 저장 용 - emit으로 변경
    emit('update:implData', {
        ...props.implData,
        relatedEvidenceItem: []
    });
    await legalReviewDetailStore.resetLegalManageData(index);
};

/*********************************
 * 위험성 추정 기준 팝업 관련
 *********************************/
const openRistTimationCriteria = () => {
    const groupedResult = {};

    props.riskScoreList.forEach(item => {
        if (item.majorCd !== 'C0040') return;
        if (!item.minorCd.includes(props.implData.riskAssessmentStandards)) return;

        let key = '';
        let value = '';
        const remark = item.remark;

        if (props.implData.riskAssessmentStandards === '3a') {
            // "3a_l", "3a_m", "3a_h"
            const grade = item.minorNm; // 하, 중, 상

            if (!grade) return;

            key = `${grade}|${remark}`;
            value = grade; // 등급을 value로 사용
        } else {
            // 일반 케이스
            const match = item.minorNm.match(/^(\d+)\[(.+)\]$/);
            if (!match) return;

            const num = parseInt(match[1]); // 1
            const grade = match[2]; // 매우 낮음

            key = `${grade}|${remark}`;
            value = num;
        }

        if (!groupedResult[key]) {
            groupedResult[key] = [];
        }

        groupedResult[key].push(value);
    });

    const resultList = Object.entries(groupedResult).map(([key, values]) => {
        const [grade, remark] = key.split('|');

        let label = '';

        if (props.implData.riskAssessmentStandards === '3a') {
            label = grade;
        } else {
            if (values.length === 1) {
                label = `${values[0]}[${grade}]`;
            } else {
                label = `${values[0]}-${values[values.length - 1]}[${grade}]`;
            }
        }

        return {
            minorNm: label,
            remark: remark
        };
    });

    riskScoreStandardsList.value = resultList;
    riskEstimationCriteria.value.onOpen();
};

/*********************************
 * 담당자 팝업 관련
 *********************************/
// 인원 팝업 오픈
const handlePopupOpen = popupRef => {
    if (popupRef.value) popupRef.value.onOpen();
};
// 인원 팝업 이벤트
const addPeople = el => {
    hrIndex.value = el;
    handlePopupOpen(peoplePopup);
};
// 인원 팝업 닫기
const handlePopupClose = () => closePopup(peoplePopup);
// 팝업 닫기
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};
// 인원 선택 이벤트
const selectPeople = () => {
    let userList = selectUserStore.getSelectedUser();
    const updatedReduList = [...props.implData.implementReduList];

    if (updatedReduList[hrIndex.value].hrList.length == 0) {
        const newHrList = [];
        userList.forEach(data => {
            let row = {
                id: data.hrId,
                name: data.hrNm,
                hrId: data.hrId
            };
            newHrList.push(row);
        });
        updatedReduList[hrIndex.value] = {
            ...updatedReduList[hrIndex.value],
            hrList: newHrList
        };
    } else {
        const hrIdSet = new Set(updatedReduList[hrIndex.value].hrList.map(item => item.id));
        const newHrList = [...updatedReduList[hrIndex.value].hrList];
        userList.forEach(user => {
            if (!hrIdSet.has(user.hrId)) {
                let row = {
                    id: user.hrId,
                    name: user.hrNm,
                    hrId: user.hrId
                };
                newHrList.push(row);
            }
        });
        updatedReduList[hrIndex.value] = {
            ...updatedReduList[hrIndex.value],
            hrList: newHrList
        };
    }

    if (JSON.stringify(updatedReduList) !== JSON.stringify(props.implData.implementReduList)) {
        isUpdatingHrList.value = true; // 플래그 설정
        emit('update:implData', { ...props.implData, implementReduList: updatedReduList });
        // 다음 틱에서 플래그 해제
        nextTick(() => {
            isUpdatingHrList.value = false;
        });
    }
    handlePopupClose(peoplePopup);
};

/*********************************
 * 현재, 잔여 위험성 관련
 *********************************/
// 위험성 결정 결과 노출 이벤트
// 위험성 수준 결정 메인 함수
const setRiskScore = (index = null, type) => {
    if (index == null && type != null) {
        onInputChange(type);
    } else {
        onReudctionChange(index, type);
    }

    // type으로 현재/개선 위험성 구분
    if (type && type.startsWith('after')) {
        // after 관련 타입들도 onInputChange 호출
        setAfterRiskScore();
    } else if (index == null) {
        setCurrentRiskScore();
    } else {
        setAfterRiskScore();
    }
};

// 현재 위험성 수준 설정
const setCurrentRiskScore = () => {
    const scoreResult = parseInt(frequency?.value.substring(4, 5) || 1) * parseInt(impact?.value.substring(4, 5) || 1);

    // 위험성 점수 계산
    let score = calculateRiskScore(scoreResult, frequency.value, impact.value);

    // 상중하가 아닌 경우에 코드 값 설정
    if (props.implData.riskAssessmentStandards !== '3a') {
        riskScore.value = score[0]?.minorCd;
    }

    // 허용 가능 여부 판단 및 최적 위험성 수준 결정
    const finalRiskData = determineFinalRiskLevel();

    // 최종 위험성 데이터 적용
    applyFinalRiskData(finalRiskData);

    // 데이터 emit으로 전송
    const updatedData = {
        ...props.implData,
        frequencyScore: frequency.value,
        impactScore: impact.value,
        riskScore: riskScore.value || ''
    };

    // finalRiskData에 afterScore 정보가 있으면 포함
    if (finalRiskData && finalRiskData.afterFrequencyScore !== undefined) {
        updatedData.afterFrequencyScore = finalRiskData.afterFrequencyScore;
        updatedData.afterImpactScore = finalRiskData.afterImpactScore;
        updatedData.afterRiskScore = finalRiskData.afterRiskScore;
    }

    // showPrevBtn일 때 개선 위험성 수준 데이터 초기화
    if (props.showPrevBtn && finalRiskData) {
        updatedData.afterFrequencyScore = null;
        updatedData.afterImpactScore = null;
        updatedData.afterRiskScore = null;
    }

    emit('update:implData', updatedData);
};

// 개선 위험성 수준 설정
const setAfterRiskScore = () => {
    const scoreResult = (parseInt(afterFrequency.value?.substring(4, 5)) || 1) * (parseInt(afterImpact.value?.substring(4, 5)) || 1);

    let score = calculateRiskScore(scoreResult, afterFrequency.value, afterImpact.value);
    if (props.implData.riskAssessmentStandards !== '3a') {
        afterRiskScore.value = score[0]?.minorCd;
    }

    // 데이터 emit으로 전송
    emit('update:implData', {
        ...props.implData,
        afterFrequencyScore: afterFrequency.value,
        afterImpactScore: afterImpact.value,
        afterRiskScore: afterRiskScore.value
    });

    clearValidationStore();
};

// 위험성 점수 계산 공통 함수
const calculateRiskScore = (scoreResult, frequencyValue, impactValue) => {
    let score = '';

    // 위험성 3*3인 경우
    if (props.implData.riskAssessmentStandards == '3b3') {
        if (frequencyValue == '' || impactValue == '' || frequencyValue == null || impactValue == null) {
            score = '';
        } else {
            score = riskScoreList.value.filter(item => item.minorNm.toLowerCase().includes(scoreResult) && item.minorCd.toLowerCase().includes(props.implData.riskAssessmentStandards.toLowerCase()));
        }
    }
    // 위험성 5*4인 경우
    else if (props.implData.riskAssessmentStandards == '5b4') {
        if (frequencyValue == '' || impactValue == '' || frequencyValue == null || impactValue == null) {
            score = '';
        } else {
            score = riskScoreList.value.filter(item => item.minorNm.toLowerCase().includes(scoreResult) && item.minorCd.toLowerCase().includes(props.implData.riskAssessmentStandards.toLowerCase()));
        }
    }

    return score;
};

// 최종 위험성 수준 결정 (허용 가능 여부 및 완료된 감소대책 고려)
const determineFinalRiskLevel = () => {
    // 현재 위험성이 허용 가능한지 확인
    const isCurrentRiskAllowable = checkIfRiskAllowable(frequency.value, impact.value, riskScore.value);

    // 허용 가능한 위험인 경우 현재 정보 반환
    if (isCurrentRiskAllowable) {
        riskImplStore.riskAllowance[props.index] = '허용 가능한 위험';
        riskImplStore.isRiskAllowance[props.index] = true;

        // 허용 가능한 위험의 경우 개선 위험성 초기화
        // afterFrequency.value = null;
        // afterImpact.value = null;
        // afterRiskScore.value = null;
        return {
            frequency: frequency.value,
            impact: impact.value,
            riskScore: riskScore.value,
            isCurrentRisk: true
        };
    } else {
        // 허용 불가능한 위험인 경우
        riskImplStore.riskAllowance[props.index] = '허용 불가능한 위험';
        riskImplStore.isRiskAllowance[props.index] = false;

        // 개선 위험성 정보가 있는지 확인
        if (afterRiskScore.value !== '') {
            // 기존 화면의 경우 현재 위험성 그대로 출력
            if (!props.showPrevBtn) {
                return {
                    frequency: frequency.value,
                    impact: impact.value,
                    riskScore: riskScore.value,
                    isCurrentRisk: true
                };
            } else {
                // 위험성 평가 계획 - 이전 위험성 정보 선택
                // 개선 위험성이 허용 가능한지 확인
                const isAfterRiskAllowable = checkIfRiskAllowable(afterFrequency.value, afterImpact.value, afterRiskScore.value);

                riskImplStore.riskAllowance[props.index] = isAfterRiskAllowable ? '허용 가능한 위험' : '허용 불가능한 위험';
                riskImplStore.isRiskAllowance[props.index] = isAfterRiskAllowable ? true : false;

                return {
                    frequency: afterFrequency.value,
                    impact: afterImpact.value,
                    riskScore: afterRiskScore.value,
                    isCurrentRisk: true
                };
            }
        }
    }
};

// 위험성이 허용 가능한지 확인
const checkIfRiskAllowable = (frequency, impact, riskScore) => {
    if (!riskScore || !riskImplStore?.riskAllowanceStandards) {
        return false;
    }

    const [, frequencyLevel] = frequency.split('_');
    const [, impactLevel] = impact.split('_');
    const [, riskScoreLevel] = riskScore.split('_');
    const [, allowLevel] = riskImplStore.riskAllowanceStandards.split('_');

    let scoreValue = '';
    let allowValue = '';
    if (props.implData.riskAssessmentStandards === '3a') {
        const rankMap = { l: 1, m: 2, h: 3 };
        scoreValue = isNaN(riskScoreLevel) ? rankMap[riskScoreLevel] ?? parseInt(riskScoreLevel) : parseInt(riskScoreLevel);
        allowValue = isNaN(allowLevel) ? rankMap[allowLevel] ?? parseInt(allowLevel) : parseInt(allowLevel);
    } else {
        scoreValue = parseInt(frequencyLevel) * parseInt(impactLevel);
        allowValue = isNaN(allowLevel) ? allowLevel ?? parseInt(allowLevel) : parseInt(allowLevel);
    }

    if (isNaN(scoreValue) || isNaN(allowValue)) {
        return false;
    }

    return scoreValue <= allowValue;
};

// 최종 위험성 데이터 적용
const applyFinalRiskData = finalRiskData => {
    // 위험성 평가 계획 - 이전 위험성 정보 선택 버튼
    if (props.showPrevBtn) {
        if (finalRiskData) {
            frequency.value = finalRiskData.frequency;
            impact.value = finalRiskData.impact;
            riskScore.value = finalRiskData.riskScore;
        }
    }
};

// 현재 위험성 수준 계산된 위험성 결정 표시 - 3b3, 5b4 계산
const getRiskScoreName = riskScore => {
    const filtered = riskScoreList.value.filter(score => score.minorCd === riskScore);
    return filtered.length > 0 ? filtered[0].minorNm : '';
};

// 개선 위험성 수준 계산된 위험성 결정 표시 - 3b3, 5b4 계산
const getAfterRiskScoreName = afterRiskScore => {
    const filtered = riskScoreList.value.filter(score => score.minorCd === afterRiskScore);
    return filtered.length > 0 ? filtered[0].minorNm : '';
};

// 감소대책 추가 버튼 클릭 이벤트
const addReduction = async () => {
    let row = props.implData.implementReduList.length.toString();

    const updatedReduList = [
        ...props.implData.implementReduList,
        {
            docType: 'RAP',
            useYn: 'Y',
            hrList: [],
            reductionCreatedAt: getCurrentDate(),
            checked: true
        }
    ];
    const first = props.index.charAt(0); // 첫째 자리
    const rest = props.index.substring(1) || '0'; // 나머지 (없으면 "0" 처리)
    riskImplStore.implList.list[first].implRiskAssList[rest].checked = true;
    emit('update:implData', { ...props.implData, implementReduList: updatedReduList });

    await nextTick();
    toggleAccordion(null, 'reduction' + props.index + row);
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

// 개별 아코디언 토글 함수
const toggleAccordion = async (event, fieldId) => {
    let button = null;
    if (fieldId != null) {
        button = document.getElementById(`${fieldId}`);
    } else {
        button = event.currentTarget;
    }
    // const button = event.currenttarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

//-----------------------------------------------
//-----------------------------------------------

// validation 초기화
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
/*********************************
 * 읽기 전용 관련
 *********************************/
</script>

<style lang="scss" scoped>
.form table tbody tr:first-child th {
    border-top: 1px solid #e1e6ed;
}
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
        &::before {
            display: inline-block;
            width: 1px;
            height: 0.8em;
            background-color: #e1e6ed;
            left: 0;
        }
        @media (max-width: 640px) {
            padding-left: 32px;
            margin-left: 0;
            &::before {
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
</style>
