<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="br4px bcFFFFFF bd1pxsolidE1E6ED">
                <!-- 결재란 -->
                <div class="pa2-2rem bdb1pxsolidE1E6ED">
                    <ISignature ref="signatureComponent" :cmd="risksAndOppStore.inputForm.cmd" targetType="RAO" :writeYear="risksAndOppStore.inputForm.writeYear" :docNo="risksAndOppStore.inputForm.docNo" :useYn="risksAndOppStore.model.useYn" />
                </div>
                <!-- 검색 필드 -->
                <div class="pa2-2rem br4px bd1px bcFFFFFF">
                    <div class="control-field ui form mb2-2rem" id="form">
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="writeYear" required>
                                        <span>작성년도</span>
                                    </label>
                                    <input id="writeYear" type="text" v-model="risksAndOppStore.model.writeYear" v-calendar="'yyyy'" year class="datepicker w100p radius" :readonly="risksAndOppStore.inputForm.cmd !== 'I'" required />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="orgnItem" required>
                                        <span>조직</span>
                                    </label>
                                    <!-- <input id="orgnItem" title="조직" type="hidden" v-input v-model="risksAndOppStore.orgnItem" required/> -->
                                    <i-chips :chips="risksAndOppStore.orgnItem" @popup="addOrgn" class="w100p" :readonly="risksAndOppStore.inputForm.cmd !== 'I'" required />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>등록일자</span>
                                    </label>
                                    <input v-input type="text" v-calendar="getDateFormat()" v-model="risksAndOppStore.model.createdAt" class="datepicker w100p radius" disabled />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" :checked="risksAndOppStore.model.useYn === 'Y'" v-model="risksAndOppStore.model.useYn" true-value="Y" false-value="N" type="checkbox" class="switch" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 아코디언 영역 (이 화면은 디자인 착수가 필요한 화면입니다. 작업을 위해 우선 디자인없이 마크업만 진행하겠습니다.) -->
                    <div class="accordion form ui df fdc rg8px">
                        <!-- 1 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion(0)" :class="['df jcsb aic', { active: activeSegments[0] }]">
                                <em>리스크</em>
                                <div>
                                    <button type="button" @click.stop="btnInformation('R')">
                                        <span class="fs1-3rem px1-1rem pt4px pb4px cFFFFFF bc3248F6 br2rem mr1-4rem">리스크 등급 정보</span>
                                    </button>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </div>
                            </button>
                            <div id="accordion0" class="segment oh bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem" v-for="(item, index) in risksAndOppStore.riskDetailList" :key="index">
                                    <div class="br4px bcFFFFFF bd1pxsolidE1E6ED" :id="item.isCheckedRisk ? 'formDetailRisk' + index : null">
                                        <div class="df aic es-fww">
                                            <div class="w5rem h5rem lh5rem tac shrink0 es-w100p">
                                                <!-- :disabled="risksAndOppStore.inputForm.cmd === 'I' && index === 0" -->
                                                <input type="checkbox" v-model="item.isCheckedRisk" v-input />
                                            </div>
                                            <div class="w100p pa2-2rem bdl1pxsolidE1E6ED es-bdl0pxsolidE1E6ED es-bdt1pxsolidE1E6ED">
                                                <div class="field df aic jcsb">
                                                    <label :for="'riskOrgn' + index" required><span>구분</span></label>
                                                    <input type="checkbox" :checked="item.riskUseYn === 'Y'" v-model="item.riskUseYn" true-value="Y" false-value="N" v-input="'사용여부'" class="switch" />
                                                </div>
                                                <div class="field">
                                                    <select :id="'riskOrgn' + index" required class="w100p radius" v-select v-model="item.riskOrgn">
                                                        <option v-for="item in commonOrgnList" :value="item.minorCd" :key="item.minorCd">{{ item.minorNm }}</option>
                                                    </select>
                                                    <!-- <input type="hidden" :id="'riskOrgn'+index" title="리스크 구분" v-model="item.riskOrgn" required/> -->
                                                </div>

                                                <div class="field">
                                                    <label :for="'risk_desc' + index" required><span>리스크 내용</span></label>
                                                    <textarea rows="5" :id="'risk_desc' + index" class="br4px minh10rem" placeholder="리스크 내용을 입력하세요." v-model="item.riskDesc" required></textarea>
                                                </div>
                                                <div class="field">
                                                    <label :for="'present_level' + index" required><span>현재 리스크 (조치 전 리스크)</span></label>
                                                    <div class="bcF9FAFF pa2-2rem br4px">
                                                        <div class="row flex gutters1rem">
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label :for="`presentPos${index}`">발생 가능성</label>
                                                                    <select :id="`presentPos${index}`" required class="w100p radius" v-select @change="onPosChange(item)" v-model="item.presentPos">
                                                                        <option v-for="item in risksAndOppStore.pList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                    </select>
                                                                    <!-- <input type="hidden" :id="'present_level'+index" title="현재 리스크 (조치 전 리스크)" v-model="item.presentLevel" required/> -->
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label :for="`presentSev${index}`">심각성</label>
                                                                    <select :id="`presentSev${index}`" required class="w100p radius" v-select @change="onSevChange(item)" v-model="item.presentSev">
                                                                        <option v-for="item in risksAndOppStore.sList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label for="">등급</label>
                                                                    <!-- 리스크 등급 표시 (설정 한 등급에 따라 색상 인라인 스타일로 색상을 바인딩 처리 부탁드립니다.) 작업자 : 유대경 2024.11.26  -->
                                                                    <template v-if="risksAndOppStore.rcList.length > 0">
                                                                        <i class="grade aic dif jcc w4-4rem h4-4rem br4px fs1-5rem" :style="getActionTypeStyle(item.presentLevel)">
                                                                            {{ item.presentLevel }}
                                                                            <!-- <v-input class="w20rem" v-model="item.presentLevel" readonly>
                                                                        {{ item.presentLevel }}
                                                                    </v-input> -->
                                                                        </i>
                                                                    </template>
                                                                    <template v-else>
                                                                        <select v-select v-model="item.presentLevel">
                                                                            <option v-for="item in risksAndOppStore.rlList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                        </select>
                                                                    </template>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="field">
                                                    <label>조치 계획 내용</label>
                                                    <textarea rows="5" class="br4px minh10rem" placeholder="조치 계획 내용을 입력하세요." v-model="item.riskMeasureDesc"></textarea>

                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for="riskMeasureStDt" required>조치 예정일</label>
                                                                <input id="riskMeasureStDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker radius" v-model="item.riskMeasureStDt" />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for="riskMeasureEdDt" required>조치 완료일</label>
                                                                <input id="riskMeasureEdDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker radius" v-model="item.riskMeasureEdDt" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-12 sm-grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label>잔존 리스크 (조치 후 추정 리스크)</label>
                                                            </div>
                                                            <div class="bcF9FAFF pa2-2rem br4px">
                                                                <div class="row flex gutters1rem">
                                                                    <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                        <div class="field">
                                                                            <label>발생 가능성</label>
                                                                            <select class="br4px" v-select v-model="item.measurePos" @change="onMposChange(item)">
                                                                                <option v-for="item in risksAndOppStore.pList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                    <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                        <div class="field">
                                                                            <label for="">심각성</label>
                                                                            <select class="br4px" v-select v-model="item.measureSev" @change="onMsevChange(item)">
                                                                                <option v-for="item in risksAndOppStore.sList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                    <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                        <div class="field">
                                                                            <label for="">등급</label>
                                                                            <!-- 리스크 등급 표시 (설정 한 등급에 따라 색상 인라인 스타일로 색상을 바인딩 처리 부탁드립니다.) 작업자 : 유대경 2024.11.26 -->
                                                                            <template v-if="risksAndOppStore.rcList.length > 0">
                                                                                <i class="grade aic dif jcc w4-4rem h4-4rem br4px fs1-5rem" :style="getActionTypeStyle(item.measureLevel)">
                                                                                    {{ item.measureLevel }}
                                                                                    <!-- <v-input class="w20rem" v-model="item.measureLevel" readonly>
                                                                                {{ item.measureLevel }}
                                                                            </v-input> -->
                                                                                </i>
                                                                            </template>
                                                                            <template v-else>
                                                                                <select v-select v-model="item.measureLevel">
                                                                                    <option v-for="item in risksAndOppStore.rlList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                                </select>
                                                                            </template>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="w100p mb1-6rem tac">
                                    <button v-button @click="riskHandleAddRow">
                                        <svg class="vam mr8px" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                        리스크 추가
                                    </button>
                                </div>
                            </div>
                        </div>
                        <!-- 2 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion(1)" :class="['df jcsb aic', { active: activeSegments[1] }]">
                                <em>기회</em>
                                <div>
                                    <button type="button" @click.stop="btnInformation('O')">
                                        <span class="fs1-3rem px1-1rem pt4px pb4px cFFFFFF bc3248F6 br2rem mr1-4rem">기회 등급 정보</span>
                                    </button>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </div>
                            </button>
                            <div id="accordion1" class="segment oh bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem" v-for="(item, index) in risksAndOppStore.oppDetailList" :key="index">
                                    <div class="br4px bcFFFFFF bd1pxsolidE1E6ED" :id="item.isCheckedOpp ? 'formDetailOpp' + index : null">
                                        <div class="df aic es-fww">
                                            <div class="w5rem h5rem lh5rem tac shrink0 es-w100p">
                                                <!-- :disabled="risksAndOppStore.inputForm.cmd === 'I' && index === 0" -->
                                                <input type="checkbox" v-model="item.isCheckedOpp" v-input />
                                            </div>
                                            <div class="w100p pa2-2rem bdl1pxsolidE1E6ED es-bdl0pxsolidE1E6ED es-bdt1pxsolidE1E6ED">
                                                <div class="field df aic jcsb">
                                                    <label :for="'oppOrgn' + index" required>
                                                        <span>구분</span>
                                                    </label>
                                                    <input type="checkbox" :checked="item.oppUseYn === 'Y'" v-model="item.oppUseYn" true-value="Y" false-value="N" v-input="'사용여부'" class="switch" required />
                                                </div>

                                                <div class="field">
                                                    <select :id="'oppOrgn' + index" required class="w100p radius" v-select v-model="item.oppOrgn">
                                                        <option v-for="item in commonOrgnList" :value="item.minorCd" :key="item.minorCd">{{ item.minorNm }}</option>
                                                    </select>
                                                    <!-- <input type="hidden" :id="'oppOrgn'+index" title="기회 구분" v-model="item.oppOrgn" required/> -->
                                                </div>

                                                <div class="field">
                                                    <label :for="'opp_desc' + index" required>
                                                        <span>기회 내용</span>
                                                    </label>
                                                    <textarea :id="'opp_desc' + index" rows="5" class="br4px minh10rem" placeholder="기회 내용을 입력하세요." v-model="item.oppDesc" required></textarea>
                                                </div>

                                                <div class="field">
                                                    <label :for="'opp_level' + index" required><span>기회 분석 및 평가</span></label>
                                                    <div class="bcF9FAFF pa2-2rem br4px">
                                                        <div class="row flex gutters1rem">
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label :for="'oppConcern' + index">관심도</label>
                                                                    <select class="br4px" :id="'oppConcern' + index" required v-select v-model="item.oppConcern" @change="oppConChange(item)">
                                                                        <option v-for="item in risksAndOppStore.cList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                    </select>
                                                                    <!-- <input type="hidden" :id="'opp_level'+index" title="기회 분석 및 평가" v-model="item.oppLevel" required/> -->
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label :for="'oppImportance' + index">중요도</label>
                                                                    <select class="br4px" :id="'oppImportance' + index" required v-select v-model="item.oppImportance" @change="oppImpChange(item)">
                                                                        <option v-for="item in risksAndOppStore.iList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label :for="'oppInfluence' + index">영향도</label>
                                                                    <select class="br4px" :id="'oppInfluence' + index" required v-select v-model="item.oppInfluence" @change="oppInfChange(item)">
                                                                        <option v-for="item in risksAndOppStore.aList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                                <!-- 리스크 등급 표시 (설정 한 등급에 따라 색상 인라인 스타일로 색상을 바인딩 처리 부탁드립니다.) 작업자 : 유대경 2024.11.26  -->
                                                                <div class="field">
                                                                    <label for="">등급</label>
                                                                    <template v-if="risksAndOppStore.olList.length > 0">
                                                                        <i class="grade aic dif jcc w4-4rem h4-4rem br4px fs1-5rem" :style="getActionTypeStyle(item.oppLevel)">
                                                                            {{ item.oppLevel }}
                                                                        </i>
                                                                    </template>
                                                                    <template v-else>
                                                                        <select class="br4px" v-select v-model="item.oppLevel">
                                                                            <option v-for="item in risksAndOppStore.olList" :value="item.content1" :key="item.detailSeq">{{ item.content1 }}</option>
                                                                        </select>
                                                                    </template>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="field">
                                                    <label class="fs1-5rem pb2rem">조치 계획 내용</label>
                                                    <textarea rows="5" class="br4px minh10rem" placeholder="조치 계획 내용을 입력하세요." v-model="item.oppMeasureDesc"></textarea>

                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                            <div class="field">
                                                                <label for="oppMeasureStDt" required>조치 예정일</label>
                                                                <input id="oppMeasureStDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker radius" v-model="item.oppMeasureStDt" />
                                                            </div>
                                                        </div>
                                                        <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                            <div class="field">
                                                                <label for="oppMeasureEdDt" required>조치 완료일</label>
                                                                <input id="oppMeasureEdDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker radius" v-model="item.oppMeasureEdDt" />
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="field">
                                                        <label>조치 결과의 효과성 평가</label>
                                                        <textarea rows="5" class="br4px minh10rem" placeholder="조치 결과의 효과성 평가 내용을 입력하세요." v-model="item.measureResultEffect"></textarea>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="w100p mb1-6rem tac">
                                    <button v-button @click="oppHandleAddRow">
                                        <svg class="vam mr8px" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                        기회 추가
                                    </button>
                                </div>
                            </div>
                        </div>
                        <!-- 3 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion(2)" :class="['df jcsb aic', { active: activeSegments[2] }]">
                                <em>참여자</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div id="accordion2" class="segment oh bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem br4px" v-for="(item, index) in risksAndOppStore.parDetailList" :key="index">
                                    <div class="df aic bd1pxsolidE1E6ED br4px bcFFFFFF es-fww" :id="item.isCheckedPar ? 'formDetailPar' + index : null">
                                        <div class="w5rem h5rem lh5rem tac shrink0 es-w100p">
                                            <!-- :disabled="risksAndOppStore.inputForm.cmd === 'I' && index === 0" -->
                                            <input type="checkbox" v-model="item.isCheckedPar" v-input class="mr4px" />
                                        </div>
                                        <div class="w100p pa2-2rem bdl1pxsolidE1E6ED es-bdl0pxsolidE1E6ED es-bdt1pxsolidE1E6ED">
                                            <div class="field df jcsb aic">
                                                <label for="">담당업무</label>
                                                <input type="checkbox" :checked="item.parUseYn === 'Y'" v-model="item.parUseYn" :true-value="'Y'" :false-value="'N'" v-input="'사용여부'" class="switch" @change="item.isCheckedPar = true" />
                                            </div>
                                            <textarea rows="5" class="br4px minh10rem" placeholder="담당 업무 내용을 입력하세요." v-model="item.duties" @input="item.isCheckedPar = true"></textarea>

                                            <div class="field">
                                                <label :for="'hrId' + index" required><span>성명</span></label>
                                                <label v-show="false">참여자</label>
                                                <div class="grid12-3 lg-grid12-4 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <i-hr-chips-sign required ref="parMember" :cmd="risksAndOppStore.inputForm.cmd" targetType="RAO" type="company" single @popup="peoplePopupOpen(index)" @chipRemoved="removePeople(index)" :writeYear="risksAndOppStore.searchParamRiskDetail.writeYear" :docNo="risksAndOppStore.searchParamRiskDetail.docNo" :docSeq="item.parDocSeq" :useYn="item.parUseYn" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="w100p mb1-6rem tac">
                                    <button v-button @click="parHandleAddRow">
                                        <svg class="vam mr8px" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                        참여자 추가
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <!-- 담당자 선택 팝업 -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePopup" :selected="leaderComp?.getSelectedHrId"></selectUser>
                <!--                <div class="form ui tar mt2-5rem">-->
                <!--                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">-->
                <!--                        <span>닫기</span>-->
                <!--                    </button>-->
                <!--                </div>-->
            </div>
        </i-PopupDialog>

        <!-- 조직 지정 팝업 -->
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui sm">
                <base-select-popup :title="'조직'" :selectedIdList="risksAndOppStore.inputForm.orgnIdList" :uniqueKey="[risksAndOppStore.inputForm.orgnId]" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
            </div>
        </i-PopupDialog>

        <!-- 등급 정보제공 팝업 -->
        <i-PopupDialog ref="riskAndOppPopup">
            <RiskAndOppPopup @close="closeRiskAndOppPopup" :type="propsType" />
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { gsap } from 'gsap';
import router from '@/router';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { validationStore, ref, t, btnBack, onMounted, alertMsg, watch, getCurrentDate, getCompId, btnDownload, btnSearch, btnSave, btnDelete, toastPopup, confirmMsg, setRouterParam } = BaseView();

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
const compId = getCompId();

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];

import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
const risksAndOppStore = useRisksAndOppStore();

import RiskAndOppPopup from '@/views/system/base/popup/RiskAndOppPopup.vue';

import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import ISignature from '@/components/common/iSignature.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const selectedIndex = ref(null);

import _ from 'lodash';
import { nextTick } from 'vue';
import { useRoute } from 'vue-router';

//기획 분석 및 평가 등급 트리거
const oppTrigger = ref(false);

//등급정보제공 팝업
const riskAndOppPopup = ref();

// 조직 팝업
const orgnPopup = ref(null);

// 결재
const signatureComponent = ref();
const parMember = ref();
const peoplePopup = ref();

/* ---------- 조직 팝업 ---------- */
// 조직 팝업 오픈
const addOrgn = () => {
    orgnPopup.value.onOpen();
};

const closeOrgn = e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    if (e.length !== 0) {
        risksAndOppStore.orgnItem = [];
        risksAndOppStore.inputForm.orgnId = e[0].orgnId;

        for (let dt of e) {
            risksAndOppStore.orgnItem.push({
                id: dt.orgnId,
                name: dt.orgnNm
            });
            risksAndOppStore.model.orgnId = dt.orgnId;
        }
    }
    orgnPopup.value.onClose();
};

/* ---------- 담당자(인원) 팝업 ---------- */
// 팝업 오픈

const closePopup = () => {
    peoplePopup.value.onClose();
};
const peoplePopupOpen = index => {
    selectedIndex.value = index;
    peoplePopup.value.onOpen();
};

const selectPeople = e => {
    risksAndOppStore.parDetailList[selectedIndex.value].hrId = e.hrId;
    risksAndOppStore.parDetailList[selectedIndex.value].hrNm = e.hrNm;
    parMember.value[selectedIndex.value].selectPeople();

    risksAndOppStore.parDetailList[selectedIndex.value].isCheckedPar = true;
    closePopup();
};

const removePeople = index => {
    risksAndOppStore.parDetailList[index].hrId = '';
    risksAndOppStore.parDetailList[index].hrNm = '';
    risksAndOppStore.parDetailList[index].isCheckedPar = true;
};

/* ---------- 등급 정보제공 팝업 ---------- */

const propsType = ref();
const btnInformation = type => {
    propsType.value = type;
    riskAndOppPopup.value.onOpen();
};

const closeRiskAndOppPopup = () => {
    riskAndOppPopup.value.onClose();
};

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const activeSegments = ref({});

const toggleAccordion = (index, isOpen) => {
    if (isOpen) {
        activeSegments.value[index] = true;
    } else {
        activeSegments.value[index] = !activeSegments.value[index];
    }

    // 동적 ID 생성
    const segmentId = `accordion${index}`;
    const segment = document.getElementById(segmentId);

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
// 등급 자동계산

// 선택된 detailSeq 값을 저장하는 상태
const selectedDetails = ref({
    //조지전 리스크
    presentPos: null,
    presentSev: null,
    presentLevel: null,
    //조치후 리스크
    measurePos: null,
    measureSev: null,
    measureLevel: null,
    //기회 분석 평가
    oppConcern: null,
    oppImportance: null,
    oppInfluence: null,
    oppLevel: null
});

// 리스크 등급기준 바인딩
const riskBindSelect = async items => {
    await risksAndOppStore.getAsmtList();

    if (items) {
        const prePosSeq = selectedDetails.value.presentPos ? risksAndOppStore.pList.find(pItem => pItem.content1 === selectedDetails.value.presentPos)?.detailSeq : risksAndOppStore.pList.find(pItem => pItem.content1 === items.presentPos)?.detailSeq;
        const preSevSeq = selectedDetails.value.presentSev ? risksAndOppStore.sList.find(sItem => sItem.content1 === selectedDetails.value.presentSev)?.detailSeq : risksAndOppStore.sList.find(sItem => sItem.content1 === items.presentSev)?.detailSeq;

        const measurePosSeq = selectedDetails.value.measurePos ? risksAndOppStore.pList.find(pItem => pItem.content1 === selectedDetails.value.measurePos)?.detailSeq : risksAndOppStore.pList.find(pItem => pItem.content1 === items.measurePos)?.detailSeq;
        const measureSevSeq = selectedDetails.value.measureSev ? risksAndOppStore.sList.find(sItem => sItem.content1 === selectedDetails.value.measureSev)?.detailSeq : risksAndOppStore.sList.find(sItem => sItem.content1 === items.measureSev)?.detailSeq;

        // rcList에서 posSeq, sevSeq에 맞는 content3을 찾음
        const present = risksAndOppStore.rcList.find(rcItem => rcItem.content2 === prePosSeq && rcItem.content1 === preSevSeq)?.content3;
        const measuer = risksAndOppStore.rcList.find(rcItem => rcItem.content2 === measurePosSeq && rcItem.content1 === measureSevSeq)?.content3;

        items.presentLevel = present || null;
        items.measureLevel = measuer || null;

        if (_.some(risksAndOppStore.rlList, { content1: items.presentLevel })) {
            items.preRiskBackColor = _.find(risksAndOppStore.rlList, { content1: items.presentLevel }).content2;
        } else {
            items.preRiskBackColor = '';
        }

        if (_.some(risksAndOppStore.rlList, { content1: items.measureLevel })) {
            items.meaRiskBackColor = _.find(risksAndOppStore.rlList, { content1: items.measureLevel }).content2;
        } else {
            items.meaRiskBackColor = '';
        }
    } else {
        risksAndOppStore.riskDetailList.forEach(item => {
            const prePosSeq = selectedDetails.value.presentPos ? risksAndOppStore.pList.find(pItem => pItem.content1 === selectedDetails.value.presentPos)?.detailSeq : risksAndOppStore.pList.find(pItem => pItem.content1 === item.presentPos)?.detailSeq;
            const preSevSeq = selectedDetails.value.presentSev ? risksAndOppStore.sList.find(sItem => sItem.content1 === selectedDetails.value.presentSev)?.detailSeq : risksAndOppStore.sList.find(sItem => sItem.content1 === item.presentSev)?.detailSeq;

            const measurePosSeq = selectedDetails.value.measurePos ? risksAndOppStore.pList.find(pItem => pItem.content1 === selectedDetails.value.measurePos)?.detailSeq : risksAndOppStore.pList.find(pItem => pItem.content1 === item.measurePos)?.detailSeq;
            const measureSevSeq = selectedDetails.value.measureSev ? risksAndOppStore.sList.find(sItem => sItem.content1 === selectedDetails.value.measureSev)?.detailSeq : risksAndOppStore.sList.find(sItem => sItem.content1 === item.measureSev)?.detailSeq;

            // rcList에서 posSeq, sevSeq에 맞는 content3을 찾음
            const present = risksAndOppStore.rcList.find(rcItem => rcItem.content2 === prePosSeq && rcItem.content1 === preSevSeq)?.content3;
            const measuer = risksAndOppStore.rcList.find(rcItem => rcItem.content2 === measurePosSeq && rcItem.content1 === measureSevSeq)?.content3;

            item.presentLevel = present || null;
            item.measureLevel = measuer || null;

            risksAndOppStore.riskDetailList.forEach(row => {
                if (_.some(risksAndOppStore.rlList, { content1: row.presentLevel })) {
                    row.preRiskBackColor = _.find(risksAndOppStore.rlList, { content1: row.presentLevel }).content2;
                } else {
                    row.preRiskBackColor = '';
                }

                if (_.some(risksAndOppStore.rlList, { content1: row.measureLevel })) {
                    row.meaRiskBackColor = _.find(risksAndOppStore.rlList, { content1: row.measureLevel }).content2;
                } else {
                    row.meaRiskBackColor = '';
                }
            });
        });
    }
};

const oppBindSelect = async items => {
    if (items) {
        const oppConSeq = selectedDetails.value.oppConcern ? risksAndOppStore.cList.find(cItem => cItem.content1 === selectedDetails.value.oppConcern)?.content1 : risksAndOppStore.cList.find(cItem => cItem.content1 === items.oppConcern)?.content1;

        const oppImpSeq = selectedDetails.value.oppImportance ? risksAndOppStore.iList.find(iItem => iItem.content1 === selectedDetails.value.oppImportance)?.content1 : risksAndOppStore.iList.find(iItem => iItem.content1 === items.oppImportance)?.content1;

        const oppInfSeq = selectedDetails.value.oppInfluence ? risksAndOppStore.aList.find(aItem => aItem.content1 === selectedDetails.value.oppInfluence)?.content1 : risksAndOppStore.aList.find(aItem => aItem.content1 === items.oppInfluence)?.content1;

        const selectedGrades = [oppConSeq, oppImpSeq, oppInfSeq];

        // 각 content2의 등장 횟수를 계산
        const gradeCounts = {};
        selectedGrades.forEach(grade => {
            if (!grade) return;
            gradeCounts[grade] = (gradeCounts[grade] || 0) + 1;
        });

        // 기본값 설정 (content3가 null인 행)
        const defaultDecision = risksAndOppStore.olList.find(olItem => olItem.content3 === null)?.content1;

        // 우선순위에 따라 조건에 맞는 항목 찾기
        const matchingDecision = risksAndOppStore.olList
            .slice() // 복사본 생성
            .sort((a, b) => {
                const countA = parseInt(a.content3, 10) || 0;
                const countB = parseInt(b.content3, 10) || 0;
                return countB - countA; // content3 기준 내림차순 정렬
            })
            .find(olItem => {
                const grade = olItem.content2; // 등급 (H, M 등)
                const requiredCount = parseInt(olItem.content3, 10); // 필요한 최소 개수
                return requiredCount && gradeCounts[grade] >= requiredCount;
            });

        // 결과 설정
        const decision = matchingDecision?.content1 || defaultDecision;
        items.oppLevel = decision;
    } else {
        risksAndOppStore.oppDetailList.forEach(item => {
            const oppConSeq = selectedDetails.value.oppConcern ? risksAndOppStore.cList.find(cItem => cItem.content1 === selectedDetails.value.oppConcern)?.content1 : risksAndOppStore.cList.find(cItem => cItem.content1 === item.oppConcern)?.content1;

            const oppImpSeq = selectedDetails.value.oppImportance ? risksAndOppStore.iList.find(iItem => iItem.content1 === selectedDetails.value.oppImportance)?.content1 : risksAndOppStore.iList.find(iItem => iItem.content1 === item.oppImportance)?.content1;

            const oppInfSeq = selectedDetails.value.oppInfluence ? risksAndOppStore.aList.find(aItem => aItem.content1 === selectedDetails.value.oppInfluence)?.content1 : risksAndOppStore.aList.find(aItem => aItem.content1 === item.oppInfluence)?.content1;

            const selectedGrades = [oppConSeq, oppImpSeq, oppInfSeq];

            // 각 content2의 등장 횟수를 계산
            const gradeCounts = {};
            selectedGrades.forEach(grade => {
                if (!grade) return;
                gradeCounts[grade] = (gradeCounts[grade] || 0) + 1;
            });

            // 기본값 설정 (content3가 null인 행)
            const defaultDecision = risksAndOppStore.olList.find(olItem => olItem.content3 === null)?.content1;

            // 우선순위에 따라 조건에 맞는 항목 찾기
            const matchingDecision = risksAndOppStore.olList
                .slice() // 복사본 생성
                .sort((a, b) => {
                    const countA = parseInt(a.content3, 10) || 0;
                    const countB = parseInt(b.content3, 10) || 0;
                    return countB - countA; // content3 기준 내림차순 정렬
                })
                .find(olItem => {
                    const grade = olItem.content2; // 등급 (H, M 등)
                    const requiredCount = parseInt(olItem.content3, 10); // 필요한 최소 개수
                    return requiredCount && gradeCounts[grade] >= requiredCount;
                });

            // 결과 설정
            const decision = matchingDecision?.content1 || defaultDecision;
            item.oppLevel = decision;
        });
    }
};

// // 기회등급 관리기준 자동바인딩
// watch(
//     () => risksAndOppStore.oppDetailList,
//     newValue => {
//         console.log('risksAndOppStore.loadingTrigger',risksAndOppStore.loadingTrigger)
//         if(risksAndOppStore.loadingTrigger){
//             return;
//         }
//         if (!newValue || !Array.isArray(newValue)) return;

//         newValue.forEach(item => {
//             const oppConSeq = selectedDetails.value.oppConcern ? risksAndOppStore.cList.find(cItem => cItem.content1 === selectedDetails.value.oppConcern)?.content1 : risksAndOppStore.cList.find(cItem => cItem.content1 === item.oppConcern)?.content1;

//             const oppImpSeq = selectedDetails.value.oppImportance ? risksAndOppStore.iList.find(iItem => iItem.content1 === selectedDetails.value.oppImportance)?.content1 : risksAndOppStore.iList.find(iItem => iItem.content1 === item.oppImportance)?.content1;

//             const oppInfSeq = selectedDetails.value.oppInfluence ? risksAndOppStore.aList.find(aItem => aItem.content1 === selectedDetails.value.oppInfluence)?.content1 : risksAndOppStore.aList.find(aItem => aItem.content1 === item.oppInfluence)?.content1;

//             const selectedGrades = [oppConSeq, oppImpSeq, oppInfSeq];

//             // 각 content2의 등장 횟수를 계산
//             const gradeCounts = {};
//             selectedGrades.forEach(grade => {
//                 if (!grade) return;
//                 gradeCounts[grade] = (gradeCounts[grade] || 0) + 1;
//             });

//             // 기본값 설정 (content3가 null인 행)
//             const defaultDecision = risksAndOppStore.olList.find(olItem => olItem.content3 === null)?.content1;

//             // 우선순위에 따라 조건에 맞는 항목 찾기
//             const matchingDecision = risksAndOppStore.olList
//                 .slice() // 복사본 생성
//                 .sort((a, b) => {
//                     const countA = parseInt(a.content3, 10) || 0;
//                     const countB = parseInt(b.content3, 10) || 0;
//                     return countB - countA; // content3 기준 내림차순 정렬
//                 })
//                 .find(olItem => {
//                     const grade = olItem.content2; // 등급 (H, M 등)
//                     const requiredCount = parseInt(olItem.content3, 10); // 필요한 최소 개수
//                     return requiredCount && gradeCounts[grade] >= requiredCount;
//                 });

//             // 결과 설정
//             const decision = matchingDecision?.content1 || defaultDecision;
//             item.oppLevel = decision;
//         });
//     },
//     { immediate: true, deep: true }
// );

const onPosChange = async item => {
    await riskBindSelect(item);
};
const onSevChange = async item => {
    await riskBindSelect(item);
};
const onMposChange = async item => {
    await riskBindSelect(item);
};
const onMsevChange = async item => {
    await riskBindSelect(item);
};
const oppConChange = async item => {
    //item.oppConcern = selectedDetails.value.oppConcern;
    await oppBindSelect(item);
};
const oppImpChange = async item => {
    //item.oppImportance = selectedDetails.value.oppImportance;
    await oppBindSelect(item);
};
const oppInfChange = async item => {
    //item.oppInfluence = selectedDetails.value.oppInfluence;
    await oppBindSelect(item);
};

// 로우 추가
const riskHandleAddRow = async () => {
    risksAndOppStore.loadingTrigger = true;
    await nextTick();

    const newForm = { ...risksAndOppStore.inputForm };

    newForm.isCheckedRisk = true;
    newForm.riskUseYn = 'Y';
    newForm.cmd = 'I';
    newForm.change = true;

    risksAndOppStore.riskDetailList.push(newForm);
    risksAndOppStore.loadingTrigger = false;
};
const oppHandleAddRow = async () => {
    const newForm = { ...risksAndOppStore.inputForm };

    newForm.isCheckedOpp = true;
    newForm.oppUseYn = 'Y';
    newForm.cmd = 'I';

    risksAndOppStore.oppDetailList.push(newForm);
};
const parHandleAddRow = async () => {
    const newForm = { ...risksAndOppStore.inputForm };

    newForm.isCheckedPar = true;
    newForm.parUseYn = 'Y';
    newForm.cmd = 'I';
    newForm.hrId = '';
    newForm.hrNm = '';

    risksAndOppStore.parDetailList.push(newForm);
};

// const model = ref({
//     orgnId: risksAndOppStore.inputForm.orgnId,
//     createdAt: getCurrentDate(),
//     writeYear: risksAndOppStore.inputForm.writeYear,
//     model: ''
// });

//List 초기화
const initList = async () => {
    risksAndOppStore.riskDetailList = [];
    risksAndOppStore.oppDetailList = [];
    risksAndOppStore.parDetailList = [];
};

const route = useRoute();
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        risksAndOppStore.searchParamRiskDetail = {
            writeYear: param.writeYear,
            docNo: param.docNo,
            docType: param.docType
        };

        await Promise.all([toggleAccordion(0, true), toggleAccordion(1, true), toggleAccordion(2, true)]);
        await risksAndOppStore.btnDetailRisk(risksAndOppStore.searchParamRiskDetail);
    } else if (!risksAndOppStore.inputForm.cmd) {
        risksAndOppStore.loadingTrigger = true;
        await router.push('/RisksAndOpportunitiesManage');
        return;
    } else {
        oppTrigger.value = true;
        await initList();
        risksAndOppStore.model.useYn = 'Y';
        oppTrigger.value = false;
        risksAndOppStore.model.writeYear = risksAndOppStore.inputForm.writeYear;
        buttonListStore.useBtnList = ['btnBack', 'btnSave', 'btnDelete'];
    }

    await getCodeList();
    await riskBindSelect();
    await oppBindSelect();

    //참석자
    risksAndOppStore.peopleSignature = parMember.value;

    //결재
    risksAndOppStore.signature = signatureComponent.value;
    await nextTick();

    risksAndOppStore.loadingTrigger = false;
});
//구분 공통코드
const commonOrgnList = ref([]);
const getCodeList = async () => {
    const responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0034',
            compId: compId // compId 사용
        })
    ]);
    commonOrgnList.value = responses[0].list;
};

/* --------- 버튼 클릭 이벤트 --------- */

//목록
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: btnBackAction, param: '' });
});
const btnBackAction = () => {
    router.push('/RisksAndOpportunitiesManage');
    risksAndOppStore.btnSearchRisk();
};

//조회
btnSearch(async () => {
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    // await parMember.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    await risksAndOppStore.btnDetailRisk(risksAndOppStore.searchParamRiskDetail);
    // const riskFilteredData = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk === true).map(item => ({ ...item, type: 'RISK' }));
    // const oppFilteredData = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp === true).map(item => ({ ...item, type: 'OPP' }));
    // const parFilteredData = risksAndOppStore.parDetailList.filter(item => item.isCheckedPar === true).map(item => ({ ...item, type: 'PAR' }));
    // if (riskFilteredData.length > 0 || oppFilteredData.length > 0 || parFilteredData.length > 0) {
    //     confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: btnSearchAction, param: '' });
    // } else btnSearchAction();
});
const btnSearchAction = () => {
    risksAndOppStore.btnDetailRisk(risksAndOppStore.searchParamRiskDetail);
};

//저장
btnSave(async () => {
    if (!(await validationCheck())) {
        return;
    }

    confirmMsg('info', '저장하시겠습니까?', '', { fun: saveAction, param: null });
    // const riskCheckItem = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk);
    // const oppCheckItem = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp);
    // const parCheckItem = risksAndOppStore.parDetailList.filter(item => item.isCheckedPar);

    // if(riskCheckItem.length > 0 || oppCheckItem.length > 0 || parCheckItem.length >0){
    //     confirmMsg('info', '저장', '선택한 데이터를 저장하시겠습니까?', { fun: saveAction, param: null });
    // }else{
    //     alertMsg('warning', '선택된 항목이 없습니다.');
    // }
});

const riskValid = ref(false);
const oppValid = ref(false);
const parValid = ref(false);

const validationCheck = async () => {
    // 최상단 입력필드 유효성 검사
    const mainValid = await validationStore.validateAllFields('form', true);
    if (!mainValid) return false;

    const riskCheckItem = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk);
    const oppCheckItem = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp);
    const parCheckItem = risksAndOppStore.parDetailList.filter(item => item.isCheckedPar);

    if (riskCheckItem.length > 0 || oppCheckItem.length > 0 || parCheckItem.length > 0) {
        if (riskCheckItem.length > 0) {
            // 리스크 입력필드 유효성 검사
            for (let i in risksAndOppStore.riskDetailList) {
                if (!risksAndOppStore.riskDetailList[i].isCheckedRisk) continue;

                if (!(await validationStore.validateAllFields('formDetailRisk' + i, true))) {
                    riskValid.value = await validationStore.validateAllFields('formDetailRisk' + i, true);
                    return false;
                } else {
                    riskValid.value = await validationStore.validateAllFields('formDetailRisk' + i, true);
                }
            }
        }

        if (oppCheckItem.length > 0) {
            // 기회 입력필드 유효성 검사
            for (let i in risksAndOppStore.oppDetailList) {
                if (!risksAndOppStore.oppDetailList[i].isCheckedOpp) continue;
                if (!(await validationStore.validateAllFields('formDetailOpp' + i, true))) {
                    oppValid.value = await validationStore.validateAllFields('formDetailOpp' + i, true);
                    return false;
                } else {
                    oppValid.value = await validationStore.validateAllFields('formDetailOpp' + i, true);
                }
            }
        }

        if (parCheckItem.length > 0) {
            // 참여자 입력필드 유효성 검사
            for (let i in risksAndOppStore.parDetailList) {
                if (!risksAndOppStore.parDetailList[i].isCheckedPar) continue;
                if (!(await validationStore.validateAllFields('formDetailPar' + i, true))) {
                    parValid.value = await validationStore.validateAllFields('formDetailPar' + i, true);
                    return false;
                } else {
                    parValid.value = await validationStore.validateAllFields('formDetailPar' + i, true);
                }
            }
        }
    } else {
        alertMsg('warning', '선택된 항목이 없습니다.');
        return false;
    }

    return true;
};

const saveAction = async () => {
    const saveData = [];

    if (riskValid.value) {
        const riskFilteredData = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk === true).map(item => ({ ...item, type: 'RISK' }));
        saveData.push(...riskFilteredData);
    }
    // else {
    //     toggleAccordion(0, true);
    //     return;
    // }

    if (oppValid.value) {
        const oppFilteredData = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp === true).map(item => ({ ...item, type: 'OPP' }));
        saveData.push(...oppFilteredData);
    }
    // else {
    //     toggleAccordion(1, true);
    //     return;
    // }

    // console.log('parValid', parValid)
    if (parValid.value) {
        const parFilteredData = risksAndOppStore.parDetailList
            .filter((item, index) => {
                item.index = index;
                return item.isCheckedPar === true;
            })
            .map(item => ({ ...item, type: 'PAR' }));
        saveData.push(...parFilteredData);
    }
    //  else {
    //     toggleAccordion(2, true);
    //     return;
    // }

    saveData.push({ useYn: risksAndOppStore.model.useYn });

    saveData.forEach(item => {
        item.compId = getCompId();
        item.orgnId = risksAndOppStore.model.orgnId;
        item.useYn = risksAndOppStore.model.useYn;
        item.writeYear = risksAndOppStore.model.writeYear;
        item.docNo = risksAndOppStore.searchParamRiskDetail.docNo;
        item.docType = 'RAO';
    });
    risksAndOppStore.signature = signatureComponent.value;
    risksAndOppStore.peopleSignature = parMember.value;

    await risksAndOppStore.saveDetail(saveData);

    await riskBindSelect();
    await oppBindSelect();
};

//삭제
btnDelete(() => {
    const deleteData = [];
    const riskFilteredData = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk === true).map(item => ({ ...item, type: 'RISK' }));
    deleteData.push(...riskFilteredData);
    const oppFilteredData = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp === true).map(item => ({ ...item, type: 'OPP' }));
    deleteData.push(...oppFilteredData);
    const parFilteredData = risksAndOppStore.parDetailList.filter(item => item.isCheckedPar === true).map(item => ({ ...item, type: 'PAR' }));
    deleteData.push(...parFilteredData);
    if (deleteData.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return false;
    }
    confirmMsg('info', '삭제하시겠습니까?', '', { fun: risksAndOppStore.deleteDetail, param: deleteData });
});

//리포트
btnDownload(() => {
    const printData = [];
    const riskFilteredData = risksAndOppStore.riskDetailList.filter(item => item.isCheckedRisk === true);
    printData.push(...riskFilteredData);
    const oppFilteredData = risksAndOppStore.oppDetailList.filter(item => item.isCheckedOpp === true);
    printData.push(...oppFilteredData);
    const parFilteredData = risksAndOppStore.parDetailList.filter(item => item.isCheckedPar === true);
    printData.push(...parFilteredData);
    if (printData.length === 0) {
        // alertMsg('warning', t('msgNoItem'));
        // return false;
        printData.push({
            noCheckPrint: 'Y'
        });
        confirmMsg('info', t('msgPrint'), '', { fun: risksAndOppStore.downloadReport, param: printData });
    } else {
        confirmMsg('info', t('msgCheckedPrint'), '', { fun: risksAndOppStore.downloadReport, param: printData });
    }
});

const getActionTypeStyle = level => {
    const colorMap = {
        기각: '#ff3534',
        검토: '#ffbe3f',
        채택: '#3248F6',
        S: '#ff3534',
        A: '#ffbe3f',
        B: '#3248F6',
        C: '#42de6b'
    };

    const color = colorMap[level];
    if (!color) return {};

    // 기본 스타일
    const style = {
        color,
        border: `1px solid ${color}`,
        backgroundColor: '#fff',
        fontWeight: 'bold'
    };

    return style;
};
</script>
