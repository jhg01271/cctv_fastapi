<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc" :key="rerenderKey">
        <div class="box">
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
                <!-- 검색 필드 -->
                <div class="control-field ui form">
                    <div class="row flex gutters1rem">
                        <div class="grid12-7 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for required>
                                    <span>차수</span>
                                </label>
                                <input v-input type="text" v-model="risksAndOppStore.detailParam.criteriaId" class="radius" disabled />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>
                                    <span>등록일자</span>
                                </label>
                                <input v-input type="text" v-model="formattedCreatedAt" v-calendar="getDateFormat()" class="datepicker w100p radius" disabled />
                            </div>
                        </div>

                        <div class="grid12-2 sm-grid12-6 sm-order1 us-order4">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="risksAndOppStore.detailParam.useYn === 'Y'" v-model="paramSave.mainUseYn" @change="risksAndOppStore.toggleUseYn" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-7 sm-grid12-6 us-grid12-12 us-order2">
                            <div class="field">
                                <label for required>
                                    <span>평가 기준 구분</span>
                                </label>
                                <div>
                                    <div v-if="risksAndOppStore.detailParam.criteriaType != null">
                                        <select v-select v-model="risksAndOppStore.detailParam.criteriaType" class="radius" disabled>
                                            <option v-if="risksAndOppStore.detailParam.criteriaType == 'R'" value="R">{{ '리스크 평가 기준' }}</option>
                                            <option v-if="risksAndOppStore.detailParam.criteriaType == 'O'" value="O">{{ '기회 평가 기준' }}</option>
                                        </select>
                                    </div>
                                    <div v-else>
                                        <select v-model="accNewChangerTemp" @change="handleAccChanger" class="radius">
                                            <option v-for="item in selectList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>
                                    <span>확정일자</span>
                                </label>
                                <input v-input type="text" v-model="confirmedDtFormatted" v-calendar="getDateFormat()" class="datepicker w100p radius" disabled />
                            </div>
                        </div>

                        <!-- <div class="grid12-2 sm-grid12-6 us-order3">
                            <div class="field">
                                <label for="useYn">확정여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'확정'" type="checkbox" class="df switch" :checked="risksAndOppStore.detailParam.confirmedYn === 'Y'" disabled />
                                </div>
                            </div>
                        </div>-->
                    </div>
                </div>
                <div class="control-field ui form mt2-2rem">
                    <button type="button" @click="onExample" :disabled="risksAndOppStore.confirmedYn == 'Y'" class="btn active radius w20rem es-w100p">
                        <span>예시 불러오기</span>
                    </button>

                    <!-- 아코디언 영역 (이 화면은 디자인 착수가 필요한 화면입니다. 작업을 위해 우선 디자인없이 마크업만 진행하겠습니다.) -->
                    <div class="accordion form ui mt2-2rem df fdc gap8px" v-if="risksAndOppStore.detailParam.criteriaType == 'R' || (accNewChanger == 'R' && risksAndOppStore.detailParam.criteriaType != 'O')">
                        <!-- 1 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>발생가능성</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh41-4rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col class="w45rem" />
                                                <col class="w45rem" />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th>단계</th>
                                                    <th>정도</th>
                                                    <th>설명</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'P'">
                                                    <td>
                                                        <input type="checkbox" :checked="false" v-input v-model="item.isChecked" class="radius" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content3" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="6">
                                                        <button v-button @click="risksAndOppStore.pGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 2 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>심각성</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh41-2rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col class="w45rem" />
                                                <col class="w45rem" />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th>단계</th>
                                                    <th>정도</th>
                                                    <th>설명</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'S'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" class="radius" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content3" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="6">
                                                        <button v-button @click="risksAndOppStore.sGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 3 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>리스크 등급</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh34-5rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col class="w45rem" />
                                                <col class="w45rem" />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th colspan="2">등급</th>
                                                    <th>대응방안</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'RL'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="color" value="#" title="Choose color" v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content3" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="6">
                                                        <button v-button @click="risksAndOppStore.rlGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 4 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>리스크 등급 기준</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment">
                                <div class="field pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac minw1024px">
                                            <tr>
                                                <th class="bcF8F9FB" :rowspan="severities.length + 1">심각성</th>
                                            </tr>
                                            <tr v-for="(severity, severityIndex) in severities" :key="'severity-' + severity.detailSeq">
                                                <th class="bcF8F9FB">{{ severity.content1 }}</th>
                                                <td v-for="(col, colIndex) in columns" :key="'column-' + col.detailSeq">
                                                    <select v-select v-model="selectedGrades[severityIndex][colIndex]" class="grades" :disabled="risksAndOppStore.confirmedYn == 'Y'">
                                                        <option v-for="grade in grades" :key="'grade-' + grade" :value="grade">{{ grade }}</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tfoot>
                                                <tr>
                                                    <td class="bcF8F9FB"></td>
                                                    <td class="bcF8F9FB"></td>
                                                    <th class="bcF8F9FB" v-for="col in columns" :key="'footer-column-' + col.detailSeq">{{ col.content1 }}</th>
                                                </tr>
                                                <tr>
                                                    <td class="bcF8F9FB"></td>
                                                    <td class="bcF8F9FB"></td>
                                                    <th class="bcF8F9FB" colspan="10">발생가능성</th>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="accordion form ui mt2-2rem df fdc gap8px" v-if="risksAndOppStore.detailParam.criteriaType == 'O' || accNewChanger == 'O'">
                        <!-- 1 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>관심도</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh28-4rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th>등급</th>
                                                    <th>판정 기준</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'C'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="7">
                                                        <button v-button @click="risksAndOppStore.cGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 2 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>중요도</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="field pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh28-3rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th>등급</th>
                                                    <th>판정 기준</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'I'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="5">
                                                        <button v-button @click="risksAndOppStore.iGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 3 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>영향도</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="field pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh28-3rem table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>선택</th>
                                                    <th>등급</th>
                                                    <th>판정 기준</th>
                                                    <th>순서</th>
                                                    <th>사용여부</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'A'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.content2" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" value v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="5">
                                                        <button v-button @click="risksAndOppStore.aGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                        <!-- 4 -->
                        <div class="list">
                            <button type="button" @click="toggleAccordion" class="df jcsb aic">
                                <em>기회 등급</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="field pa2rem bcF8F9FB">
                                    <OverlayScrollbarsComponent
                                        class="maxh39-3rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac es-minw70rem">
                                            <colgroup>
                                                <col class="w5rem" />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                                <col />
                                                <col class="w10rem" />
                                                <col class="w10rem" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th rowspan="2">선택</th>
                                                    <th rowspan="2">등급</th>
                                                    <th colspan="2">판정 기준</th>
                                                    <th rowspan="2">관리 기준</th>
                                                    <th rowspan="2">순서</th>
                                                    <th rowspan="2">사용여부</th>
                                                </tr>
                                                <tr>
                                                    <th>등급</th>
                                                    <th>이상</th>
                                                </tr>
                                            </thead>
                                            <tbody v-for="(item, index) in risksAndOppStore.detailData" :key="index">
                                                <tr v-if="item.detailType === 'OL'">
                                                    <td>
                                                        <input type="checkbox" v-input v-model="item.isChecked" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" v-model="item.content1" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" v-model="item.content2" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="number" v-model="item.content3" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" v-model="item.content4" class="radius" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td>
                                                        <input type="text" v-model="item.ordSeq" placeholder="99" class="radius tac" :readonly="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                    <td class="init m">
                                                        <input type="checkbox" v-input class="switch" :checked="item.detailUseYn === 'Y'" @change="toggleUseYn($event, index)" :disabled="risksAndOppStore.confirmedYn == 'Y'" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <!-- 추가 -->
                                                <tr>
                                                    <td colspan="7">
                                                        <button v-button @click="risksAndOppStore.olGridAdd">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { btnBack, onMounted, confirmMsg, computed, btnSearch, btnSave, btnDelete, btnCopy, btnAdd, watch, watchEffect, getCompId, alertMsg, t, validationStore, formatDate, formatDateForDB } = BaseView();
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSearch', 'btnDelete', 'btnCopy', 'btnAdd'];
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
const compId = getCompId();
import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
const risksAndOppStore = useRisksAndOppStore();
import { getDatasetAsmtList } from '@/stores/safewiz/planning/api/risksAndOpportunitiesApi';
import { getDateFormat } from '@/utils/dateUtil.js';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

import _ from 'lodash';

const rerenderKey = ref(0);

const paramSave = ref([]);
const accChanger = ref(null);
const selectList = ref([]);

const datasetAsmtDataList = ref([]);
const formattedCreatedAt = computed({
    get() {
        const input = risksAndOppStore.detailParam.createdAt;
        if (!input) return '';

        return formatDate(input);
    },
    set(value) {
        // 입력 값을 다시 `yyyymmdd` 형식으로 저장
        risksAndOppStore.detailParam.createdAt = formatDateForDB(risksAndOppStore.detailParam.createdAt);
    }
});

const confirmedDtFormatted = computed({
    get() {
        const date = risksAndOppStore.detailParam.confirmedDt;
        return formatDate(date);
    },
    set(value) {
        risksAndOppStore.detailParam.confirmedDt = formatDateForDB(risksAndOppStore.detailParam.confirmedDt); // 포맷을 제거하고 원래 형식으로 저장
    }
});

onMounted(async () => {
    if (!risksAndOppStore.detailParam.createdAt) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (risksAndOppStore.detailParam.criteriaType != null) {
        accNewChanger.value = risksAndOppStore.detailParam.criteriaType;
    }
    if (risksAndOppStore.inputForm.cmd == 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    } else if (risksAndOppStore.confirmedYn == 'Y') {
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnCopy', 'btnAdd'];
    } else {
        buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSearch', 'btnDelete', 'btnCopy', 'btnAdd'];
    }
    await getCodeList();
});

//평가구분기준 공통코드
const getCodeList = async () => {
    const responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0028',
            compId: compId // compId 사용
        })
    ]);
    selectList.value = responses[0].list;

    // selectList 로드 완료 후 초기 accChanger 값 설정
    if (!accNewChanger.value && selectList.value.length) {
        accNewChanger.value = selectList.value[0].minorCd;
    }
};

// selectbox선택 후 취소 버튼을 클릭했을때, 아코디언목록과 selectbox이전값을 위해서 작성함
const accNewChanger = ref('');
const accNewChangerTemp = ref('R');
const handleAccChanger = () => {
    const checkPlag = risksAndOppStore.detailData
        .filter(row => row.isChecked) // 체크된 신규 행과 수정 행 모두 필터링
        .map(row => {
            return {
                ...row,
                ...JSON.parse(JSON.stringify(risksAndOppStore.detailParam))
            };
        });
    if (checkPlag.length > 0) {
        confirmMsg('warning', '변경 시 이전에 작성한 데이터가 사라집니다. 계속하시겠습니까?', null, { fun: accEvent, param: '' }, { fun: cancelEvent });
    } else accEvent();
};

const accEvent = async () => {
    await clearFormData();
    accNewChanger.value = accNewChangerTemp.value;
};
const cancelEvent = async () => {
    accNewChangerTemp.value = accNewChangerTemp.value === 'R' ? 'O' : 'R';
};
const clearFormData = () => {
    risksAndOppStore.detailData = [];
};

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

const severities = ref([]); // 심각성 행 수
const columns = ref([]); // 발생가능성 열 수
const grades = ref([]); // `item.detailType === 'RL'`에 대한 옵션 값들
const selectedGrades = ref([]);

const gradesInfo = ref([]); // 등급 정보 (등급명, 컬러)
const setGradesColorInit = async () => {
    // 등급이 업데이트될 때 컬러 업데이트
    gradesInfo.value = risksAndOppStore.detailData
        .filter(item => item.detailType === 'RL' && item.detailUseYn === 'Y')
        .map(item => ({ content1: item.content1, content2: item.content2 }))
        .filter((value, index, self) => index === self.findIndex(t => t.content1 === value.content1 && t.content2 === value.content2));

    setGradesColor(); // 등급 셀렉트에 대해 컬러 설정
};
//리스크 등급기준 색상적용
const setGradesColor = async (changeSelect = false) => {
    const selectedElements = document.querySelectorAll('.grades select + button'); // 선택된 등급 요소
    const optionsElements = document.querySelectorAll('.select-options > li > button'); // 등급 옵션 요소

    // gradesinfo에 등급명, 컬러 정보 저장
    gradesInfo.value = risksAndOppStore.detailData
        .filter(item => item.detailType === 'RL' && item.detailUseYn === 'Y')
        .map(item => ({ content1: item.content1, content2: item.content2 }))
        .filter((value, index, self) => index === self.findIndex(t => t.content1 === value.content1 && t.content2 === value.content2));

    // selectedElement에 대한 컬러 설정
    selectedElements.forEach(el => {
        const matchingGrade = gradesInfo.value.find(grade => grade.content1 === el.innerText);
        if (matchingGrade) el.style.color = matchingGrade.content2; // 컬러를 content2로 설정
    });

    // select값이 바꼈을 때는 옵션컬러는 변경안함
    if (!changeSelect) {
        // 셀렉트 옵션에 대한 컬러 설정
        optionsElements.forEach(el => {
            const matchingGrade = gradesInfo.value.find(grade => grade.content1 === el.getAttribute('rel'));
            if (matchingGrade) el.style.color = matchingGrade.content2; // 컬러를 content2로 설정
        });
    }
};
//리스크 등급기준 색상적용
watch(
    selectedGrades,
    () => {
        setGradesColor(true);
    },
    { deep: true }
);
//리스크 등급기준 데이터 바인딩
watchEffect(() => {
    selectedGrades.value = severities.value.map((severity, severityIndex) =>
        columns.value.map((column, colIndex) => {
            const matchingDetail = risksAndOppStore.detailData.find(item => item.detailType === 'RC' && item.content1 === severity.detailSeq && item.content2 === column.detailSeq);
            return matchingDetail ? matchingDetail.content3 : null; // 데이터가 있을 때는 content3 값을, 없으면 null을 반환
        })
    );
    setTimeout(() => {
        setGradesColorInit(); // 등급 정보 변경에 대한 등급 컬러 정보 세팅
    }, 100); //  100ms 대기
});

// store의 detailData를 감시해 심각성, 발생가능성, 선택 값 초기화
watch(
    () => risksAndOppStore.detailData,
    newData => {
        // 심각성 및 발생 가능성 데이터 업데이트
        severities.value = newData
            .filter(item => item.detailType === 'S' && item.detailUseYn === 'Y')
            .map(item => item) // content1 값을 포함한 심각성 데이터 객체를 추가
            .reverse();

        columns.value = newData
            .filter(item => item.detailType === 'P' && item.detailUseYn === 'Y' && item.detailSeq != null)
            .map(item => item) // content1 값을 포함한 발생 가능성 데이터 객체를 추가
            .filter((value, index, self) => index === self.findIndex(t => t.content1 === value.content1))
            .sort((a, b) => a.ordSeq - b.ordSeq);

        // 'RL' 타입의 content1 값을 grades 옵션으로 설정
        grades.value = newData
            .filter(item => item.detailType === 'RL' && item.detailUseYn === 'Y')
            .map(item => item.content1)
            .filter((value, index, self) => self.indexOf(value) === index); // 중복 제거

        selectedGrades.value = severities.value.map(severity => {
            return columns.value.map(column => {
                // `detailType === 'RC'`의 데이터를 찾아 content3 값을 `selectedGrade`에 배치
                const matchedData = newData.find(item => item.detailType === 'RC' && item.content1 === severity.detailSeq && item.content2 === column.detailSeq);
                return matchedData ? matchedData.content3 : null; // 존재하지 않으면 기본값은 null
            });
        });
        // console.log(selectedGrades.value);
    },
    { immediate: true, deep: true }
);

let previousArrayState = JSON.parse(JSON.stringify(risksAndOppStore.detailData));
watch(
    () => risksAndOppStore.detailData,
    newArray => {
        newArray.forEach((newItem, index) => {
            const oldItem = previousArrayState[index];

            if (oldItem && newItem.criteriaId === oldItem.criteriaId) {
                // isChecked를 제외한 나머지 속성만 비교
                const { isChecked: newChecked, ...newItemWithoutChecked } = newItem;
                const { isChecked: oldChecked, ...oldItemWithoutChecked } = oldItem;

                if (JSON.stringify(newItemWithoutChecked) !== JSON.stringify(oldItemWithoutChecked)) {
                    // 변경 사항이 있을 경우 isChecked를 true로 설정
                    newItem.isChecked = true;
                }
            }
        });
        // 비교 후, 이전 상태를 새 상태로 깊은 복사하여 갱신
        previousArrayState = JSON.parse(JSON.stringify(newArray));
    },
    { deep: true }
);

const toggleUseYn = (event, index) => {
    risksAndOppStore.detailData[index].detailUseYn = event.target.checked ? 'Y' : 'N';
};

//조회
btnSearch(() => {
    const checkPlag = risksAndOppStore.detailData
        .filter(row => row.isChecked) // 체크된 신규 행과 수정 행 모두 필터링
        .map(row => {
            return {
                ...row,
                ...JSON.parse(JSON.stringify(risksAndOppStore.detailParam))
            };
        });
    if (checkPlag.length > 0) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', {
            fun: btnSearchAction,
            param: ''
        });
    } else {
        btnSearchAction();
    }
});
const btnSearchAction = async () => {
    risksAndOppStore.detailData = [];
    risksAndOppStore.btnDetail(risksAndOppStore.detailParam, true);
};

//저장
btnSave(() => {
    const saveData = risksAndOppStore.detailData
        .filter(row => row.isChecked) // 체크된 신규 행과 수정 행 모두 필터링
        .map(row => {
            return {
                ...row,
                ...JSON.parse(JSON.stringify(risksAndOppStore.detailParam))
            };
        });
    if (saveData.length === 0 && paramSave.value && paramSave.value.mainUseYn !== undefined) {
        saveData.push({ mainUseYn: paramSave.value.mainUseYn, criteriaId: risksAndOppStore.detailParam.criteriaId });
    }

    severities.value.forEach((severity, severityIndex) => {
        columns.value.forEach((column, colIndex) => {
            const selectedGrade = selectedGrades.value[severityIndex][colIndex]; // 선택된 값 가져오기
            if (selectedGrade) {
                // 선택된 값이 있을 때만
                const detail = {
                    compId: compId,
                    criteriaId: risksAndOppStore.detailParam.criteriaId,
                    confirmedDt: risksAndOppStore.detailParam.confirmedDt,
                    // confirmedYn: risksAndOppStore.detailParam.confirmedYn,
                    mainUseYn: paramSave.value.mainUseYn,
                    criteriaType: 'R',
                    detailType: 'RC', // 원하는 detailType 설정
                    content1: severity.detailSeq, // 심각성의 detailSeq
                    content2: column.detailSeq, // 발생 가능성의 detailSeq
                    content3: selectedGrade, // 선택된 리스크 등급 기준(RL)
                    cmd: 'I',
                    detailUseYn: 'Y',
                    confirmedYn: risksAndOppStore.confirmedYn
                };

                saveData.push(detail); // 임시 detail 데이터에 추가
            }
        });
    });

    if (saveData.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    for (const item of saveData) {
        if ((item.detailType === 'P' || item.detailType === 'S') && !item.content1) {
            alertMsg('warning', '단계를 입력해주세요.');
            return;
        }
        if (['RL', 'C', 'I', 'A', 'OL'].includes(item.detailType) && !item.content1) {
            alertMsg('warning', '등급을 입력해주세요.');
            return;
        }
    }
    // 저장 확인 메시지 표시
    confirmMsg('info', '저장 하시겠습니까?', null, { fun: risksAndOppStore.btnAsmtSave, param: saveData });
});

//삭제
btnDelete(() => {
    const deleteData = risksAndOppStore.detailData
        .filter(row => row.isChecked) // 체크된 신규 행과 수정 행 모두 필터링
        .map(row => {
            return {
                ...row,
                ...JSON.parse(JSON.stringify(risksAndOppStore.detailParam))
            };
        });

    // 저장 확인 메시지 표시
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: risksAndOppStore.btnAsmtDelete, param: deleteData });
});

//복사
btnCopy(() => {
    confirmMsg('info', '현재 평가기준을 복사하여 신규 작성하시겠습니까?', null, {
        fun: () => {
            forceRerender(rerenderKey.value);
            const today = getToday();

            // 데이터를 복사하여 criteriaId와 관련 정보를 초기화
            const copiedData = risksAndOppStore.detailData.map(item => ({
                ...item,
                compId: risksAndOppStore.compId,
                criteriaId: '',
                confirmedYn: 'N',
                confirmedDt: '',
                createdAt: today,
                cmd: 'I'
            }));

            // risksAndOppStore의 데이터 필드 초기화 및 새 데이터 설정
            risksAndOppStore.detailParam = {
                ...risksAndOppStore.detailParam,
                compId: risksAndOppStore.compId,
                criteriaId: '',
                confirmedYn: 'N',
                confirmedDt: '',
                createdAt: today
            };

            risksAndOppStore.detailData = copiedData;
        },
        param: rerenderKey.value
    });
});

//추가
btnAdd(() => {
    const checkPlag = risksAndOppStore.detailData
        .filter(row => row.isChecked) // 체크된 신규 행과 수정 행 모두 필터링
        .map(row => {
            return {
                ...row,
                ...JSON.parse(JSON.stringify(risksAndOppStore.detailParam))
            };
        });
    if (checkPlag.length > 0) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', {
            fun: btnAddAction,
            param: ''
        });
    } else {
        btnAddAction();
    }
});

const btnAddAction = async () => {
    const today = getToday();
    buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnAdd'];
    risksAndOppStore.inputForm.cmd = 'I';
    risksAndOppStore.confirmedYn = 'N';
    risksAndOppStore.detailParam = {
        compId: risksAndOppStore.compId,
        criteriaId: '',
        createdAt: today,
        useYn: 'Y',
        confirmedYn: 'N',
        confirmedDt: ''
    };
    risksAndOppStore.detailData = [];
};

//목록
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', {
        fun: risksAndOppStore.goBackAsmt,
        param: ''
    });
});

//강제렌더링
const forceRerender = item => {
    let forceItem = parseFloat(item.value);
    return (forceItem += 1);
};

//복사,추가 버튼 클릭시 현재날짜
const getToday = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
};

// 예시 불러오기
const onExample = async () => {
    if (risksAndOppStore.confirmedYn == 'Y') {
        return;
    }

    if (risksAndOppStore.isSampleAction) {
        alertMsg('warning', '신규 입력중인 값이 있습니다.\n저장후 추가 하세요.');
        return;
    }

    confirmMsg('warning', '예시 데이터를 불러오시겠습니까?', null, { fun: btnSampleAction, param: rerenderKey.value });
};
const btnSampleAction = async () => {
    await getDatasetAsmtList().then(res => {
        if (res && res.success) {
            datasetAsmtDataList.value = res.list;

            const today = getToday();
            const newData = datasetAsmtDataList.value.map(item => ({
                ...item,
                compId: risksAndOppStore.compId,
                criteriaId: '',
                criteriaType: accNewChanger.value,
                confirmedYn: 'N',
                confirmedDt: '',
                createdAt: today,
                detailUseYn: 'Y',
                isChecked: true,
                cmd: 'I'
            }));
            const filteredData = newData.filter(item => {
                if (accNewChanger.value === 'R') {
                    return ['P', 'S', 'RL'].includes(item.detailType);
                } else if (accNewChanger.value === 'O') {
                    return ['C', 'I', 'A', 'OL'].includes(item.detailType);
                }
                return false;
            });

            if (risksAndOppStore.detailParam.criteriaId) {
                risksAndOppStore.detailData = [...risksAndOppStore.detailData, ...filteredData];
            } else {
                risksAndOppStore.detailData = filteredData;
            }

            forceRerender(rerenderKey.value);
            risksAndOppStore.isSampleAction = true;
        }
    });
};
</script>
<style lang="scss" scoped>
.form {
    .table-sticky {
        table {
            tbody {
                tr:first-child {
                    td {
                        border-bottom: 1px solid #e1e6ed;
                    }
                }
            }
        }
    }
}
</style>
