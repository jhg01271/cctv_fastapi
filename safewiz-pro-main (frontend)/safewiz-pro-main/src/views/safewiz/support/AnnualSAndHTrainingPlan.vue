<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh">
                    <div class="control-field ui form">
                        <div class="pa2-2rem">
                            <ISignature :ref="el => setSignatureRef(annualTrainingPlanStore.searchParam.writeYear, el)" :cmd="annualTrainingPlanStore.cmd" targetType="ATP" :writeYear="annualTrainingPlanStore.searchParam.writeYear" :docNo="annualTrainingPlanStore.compId"></ISignature>
                        </div>
                    </div>
                </div>
                <hr class="h1px" />
                <form id="inputForm" @submit.prevent ref="myForm">
                    <div class="control-field ui form mb2-2rem form" id="annualForm">
                        <div class="pa2-2rem pt1rem">
                            <div class="row flex">
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for>작성년도</label>
                                        <div class="df aic gap0-5rem">
                                            <input v-model="annualTrainingPlanStore.searchParam.writeYear" @input="writeYearChanged" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" year />

                                            <button type="button" class="btn-edu w4-4rem h4-4rem bd1pxsolid3248f6 br4px shrink0" :title="annualTrainingPlanStore.eduTimeTitle" @click="eduHelp('edu01')">
                                                <img src="/assets/img/common/icon_schedule.svg" alt />
                                            </button>
                                            <button type="button" class="btn-edu w4-4rem h4-4rem bd1pxsolid3248f6 br4px shrink0" :title="annualTrainingPlanStore.eduContentTitle" @click="eduHelp('edu02')">
                                                <img src="/assets/img/common/icon_educate.svg" alt />
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="mt2-2rem pa2-2rem bcF9FAFF br4px">
                                <div class="segment">
                                    <div class="pr accordion mb2rem">
                                        <div class="list not-accrdion">
                                            <button type="button" class="radius w15rem df jcfe aic gap8px" @click="toggleAccordion">
                                                <!--🐸 과목/과정 관리 -->
                                                <p>관리 기능</p>
                                                <svg class="sub" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <path d="M11.6249 12.3752L11.6249 11.6252M12.3749 12.3752L12.3749 11.6252M11.6249 19.3752L11.6249 18.6252M12.3749 19.3752L12.3749 18.6252M11.6249 5.37524L11.6249 4.62524M12.3749 5.37524L12.3749 4.62524M12 11C12.5523 11 13 11.4477 13 12C13 12.5523 12.5523 13 12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11ZM12 18C12.5523 18 13 18.4477 13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19C11 18.4477 11.4477 18 12 18ZM12 4C12.5523 4 13 4.44772 13 5C13 5.55228 12.5523 6 12 6C11.4477 6 11 5.55228 11 5C11 4.44772 11.4477 4 12 4Z" stroke="black" stroke-width="2" stroke-linecap="round" />
                                                </svg>
                                            </button>
                                            <div class="pa t3rem r0 segment oh zi2">
                                                <div class="df fdc jcfe bd1pxsolid3248F6 br4px oh">
                                                    <!-- 🦖 과목/과정 관리 Content -->
                                                    <button type="button" class="w15rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openDataSetMngPopup('subjects')">과목/과정 관리</button>
                                                    <button type="button" class="w15rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openDataSetMngPopup('edu')">교육기관 관리</button>
                                                    <button type="button" class="w15rem bcFFFFFF c3248F6 pa1rem" @click="openDataSetMngPopup('inst')">강사 관리</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Data Card -->
                                <div v-for="(item, index) in annualTrainingPlanStore.inputForm" :key="index" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: item.checked }, { disabled: item.useYn == 'N' }]">
                                    <div class="df w100p br4px bcFFFFFF sm-fww" :id="`list_${index}`">
                                        <div class="w5rem df aic jcc bdr1pxsolidE1E6ED sm-h5rem sm-w100p sm-bdr0pxsolidE1E6ED sm-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="item.checked" @click="chkData(item)" />
                                        </div>
                                        <div class="w100p pa2-2rem oh">
                                            <div class="row flex gutters1rem">
                                                <div class="grid12-6 lg-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label class="minw8rem" required>
                                                            <span>과목/과정</span>
                                                        </label>
                                                        <i-chips :chips="[{ id: item.trainingCourse, name: item.trainingTypeNm }]" @popup="addSubject(index)" class="w100p" :required="item.checked" @click="chkData(item)" @removeChip="removeSubject(index)" />
                                                    </div>
                                                </div>
                                                <div class="grid12-2 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label>시간(분)</label>
                                                        <input @click="chkData(item)" v-input type="number" min="0" step="1" v-model="item.trainingDuration" class="w100p radius" id placeholder="0" />
                                                    </div>
                                                </div>
                                                <div class="grid12-2 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label>{{ t('createdAt') }}</label>
                                                        <input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(item.createdAt)" disabled />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 lg-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label for="ordSeq">{{ t('array') }}</label>
                                                        <input v-input type="number" class="w100p radius" id="ordSeq" v-model="item.ordSeq" placeholder="99" min="0" max="99" step="1" @change="chkData(item)" />
                                                    </div>
                                                </div>

                                                <div class="grid12-6 lg-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label for>교육기관</label>
                                                        <i-chips :chips="[{ id: item.trainingInstitute, name: item.trainingInstituteNm }]" @popup="addEdu(index)" class="w100p" @click="chkData(item)" @removeChip="removeEdu(index)" />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for>강사</label>
                                                        <i-chips :chips="[{ id: item.trainingInstructor, name: item.trainingInstructorNm }]" @popup="addInst(index)" class="w100p" @click="chkData(item)" @removeChip="removeInst(index)" />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label>
                                                            <label for="useYn">{{ t('useYn') }}</label>
                                                        </label>
                                                        <div class="df aic h4-4rem" :key="item.useYn">
                                                            <input
                                                                v-input="'사용'"
                                                                type="checkbox"
                                                                :checked="item.useYn === 'Y'"
                                                                class="df switch"
                                                                @change="
                                                                    event => {
                                                                        toggleUseYn(index, event);
                                                                        chkData(item);
                                                                    }
                                                                "
                                                            />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="grid12-12">
                                                    <div class="field">
                                                        <label>대상인원</label>
                                                        <OverlayScrollbarsComponent
                                                            :options="{
                                                                scrollbars: {
                                                                    x: 'visible',
                                                                    y: 'visible'
                                                                }
                                                            }"
                                                        >
                                                            <i-chips :chips="item.hrList" @popup="addPeople(index)" @click="chkData(item)" />
                                                        </OverlayScrollbarsComponent>
                                                    </div>
                                                </div>
                                                <div class="grid12-12">
                                                    <OverlayScrollbarsComponent
                                                        class="br4px"
                                                        :options="{
                                                            scrollbars: {
                                                                x: 'hidden',
                                                                y: 'visible'
                                                            }
                                                        }"
                                                    >
                                                        <table class="w100p tac">
                                                            <thead>
                                                                <tr>
                                                                    <th colspan="12">교육 일정</th>
                                                                </tr>
                                                                <tr>
                                                                    <td>1</td>
                                                                    <td>2</td>
                                                                    <td>3</td>
                                                                    <td>4</td>
                                                                    <td>5</td>
                                                                    <td>6</td>
                                                                    <td>7</td>
                                                                    <td>8</td>
                                                                    <td>9</td>
                                                                    <td>10</td>
                                                                    <td>11</td>
                                                                    <td>12</td>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth1 === 'Y'" type="checkbox" name="trainingMonth1" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth2 === 'Y'" type="checkbox" name="trainingMonth2" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth3 === 'Y'" type="checkbox" name="trainingMonth3" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth4 === 'Y'" type="checkbox" name="trainingMonth4" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth5 === 'Y'" type="checkbox" name="trainingMonth5" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth6 === 'Y'" type="checkbox" name="trainingMonth6" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth7 === 'Y'" type="checkbox" name="trainingMonth7" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth8 === 'Y'" type="checkbox" name="trainingMonth8" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth9 === 'Y'" type="checkbox" name="trainingMonth9" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth10 === 'Y'" type="checkbox" name="trainingMonth10" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth11 === 'Y'" type="checkbox" name="trainingMonth11" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                    <td>
                                                                        <input :checked="item.trainingMonth12 === 'Y'" type="checkbox" name="trainingMonth12" v-input class="checkbox" @change="toggleCheckYn(index, $event)" />
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </OverlayScrollbarsComponent>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- 추가 버튼 -->
                                <div class="py2rem w100p tac wsn">
                                    <button v-button @click="cardBtnAdd">
                                        <span>
                                            <svg class="mr8px vam" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                            안전보건 연간교육 계획서 카드 추가
                                        </span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>

        <!-- 인원 검색 팝업 컴포넌트 시작  -->
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="annualTrainingPlanStore.inputForm[peopleIdx].hrList?.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->

            <!-- 과목/과정 팝업 컴포넌트 -->
            <i-PopupDialog ref="subjectsMngPopup">
                <div class="contents w70rem md-w100p">
                    <DataSetManagePopup :title="'과목/과정 관리'" :subTitle="'이름'" :datasetYn="'Y'" id="coursesId" nm="coursesNm" :popupDataList="subjectsMngList" :cardComponent="remarkPopupComponent" @filter="filterDataSetMngPopup" @search="seacrhDataSetMngPopup" @sample="sampleDataSetMngPopup" @add="addDataSetMngPopup" @save="saveDataSetMngPopup" @delete="deleteDataSetMngPopup" @close="closeDataSetMngPopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <!-- 교육기관 팝업 컴포넌트 -->
            <i-PopupDialog ref="eduMngPopup">
                <div class="contents w70rem md-w100p">
                    <DataSetManagePopup :title="'교육기관 관리'" :subTitle="'이름'" :datasetYn="'Y'" id="centerId" nm="centerNm" :popupDataList="eduMngList" :cardComponent="remarkPopupComponent" @filter="filterDataSetMngPopup" @search="seacrhDataSetMngPopup" @sample="sampleDataSetMngPopup" @add="addDataSetMngPopup" @save="saveDataSetMngPopup" @delete="deleteDataSetMngPopup" @close="closeDataSetMngPopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <!-- 강사관리 팝업 컴포넌트 -->
            <i-PopupDialog ref="instMngPopup">
                <div class="contents w70rem md-w100p">
                    <DataSetManagePopup :title="'강사 관리'" :subTitle="'이름'" :datasetYn="'Y'" id="instructorId" nm="instructorNm" :popupDataList="instMngList" :cardComponent="remarkPopupComponent" @filter="filterDataSetMngPopup" @search="seacrhDataSetMngPopup" @sample="sampleDataSetMngPopup" @add="addDataSetMngPopup" @save="saveDataSetMngPopup" @delete="deleteDataSetMngPopup" @close="closeDataSetMngPopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <!-- 예시 불러오기 팝업-->
            <i-PopupDialog ref="subjectsMngSamplePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'과목/과정 예시'" filterKey="coursesNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="eduMngSamplePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'교육기관 예시'" filterKey="centerNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="instMngSamplePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'강사 예시'" filterKey="instructorNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
                </div>
            </i-PopupDialog>

            <!------------------------------카드 내부 팝업------------------------------>
            <i-PopupDialog ref="subjectsPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'과목/과정 관리'" :component="BaseSelectAccordionComponent" uniqueKey="coursesId" filterKey="coursesNm" useYnKey="useYn" :excluded-value="'N'" :selectedIdList="annualTrainingPlanStore.inputForm.map(el => el.trainingCourse)" :single="true" :fetch-data="getSubjectsListFunc" :btnInfo="{ close: true }" @close="applySubjectsPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="eduPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'교육기관 관리'" :component="BaseSelectAccordionComponent" uniqueKey="centerId" filterKey="centerNm" useYnKey="useYn" :excluded-value="'N'" :selectedIdList="annualTrainingPlanStore.inputForm.map(el => el.trainingInstitute)" :single="true" :fetch-data="getEduListFunc" :btnInfo="{ close: true }" @close="closeEduPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="instPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'강사 관리'" :component="BaseSelectAccordionComponent" uniqueKey="instructorId" filterKey="instructorNm" useYnKey="useYn" :excluded-value="'N'" :selectedIdList="annualTrainingPlanStore.inputForm.map(el => el.trainingInstructor)" :single="true" :fetch-data="getInstListFunc" :btnInfo="{ close: true }" @close="closeInstPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>

    <!-- 팝업 컴포넌트 -->
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { gsap } from 'gsap';
import ISignature from '@/components/common/iSignature.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

import _ from 'lodash';

const { ref, btnDownload, onMounted, t, alertMsg, confirmMsg, formatDate, btnBack, btnSearch, btnSave, btnAdd, btnDelete, btnPreYear, openLoading, endLoading, validationStore, getDuplicatedData, setRouterParam } = BaseView();

//store
import { useAnnualTrainingPlanStore } from '@/stores/safewiz/support/annualTrainingPlan.js';
const annualTrainingPlanStore = useAnnualTrainingPlanStore();

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();
//-----------------------------------------------
//-----------------------------------------------
// [안전보건교육 교육과정별 자료 다운로드]
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { downloadFileFromServer } from '@/utils/iFileLoader.js';
const eduHelp = minorParam => {
    getSystemCode({
        majorCd: 'CF001'
    }).then(async res => {
        const manualCd = res.list.find(el => el.minorCd === minorParam) || null;
        if (manualCd) {
            if (manualCd.files.length == 0) {
                alertMsg('warning', '파일이 등록되지 않았습니다.\n관리자에게 문의하세요.');
            } else {
                confirmMsg('info', `[${manualCd.minorNm}]\n파일을 다운로드 받으시겠습니까?`, null, { fun: eduDownloadAction, param: manualCd });
            }
        } else {
            alertMsg('warning', '오류가 발생하였습니다.\n관리자에게 문의하세요.');
        }
    });
};

// [안전보건교육 교육과정별 자료 다운로드 수행]
const eduDownloadAction = async manualCd => {
    openLoading();
    try {
        await Promise.all(manualCd.files.map(file => downloadFileFromServer(file.fileId)));
    } catch (error) {
        endLoading();
    } finally {
        endLoading();
    }
};

//-----------------------------------------------
//-----------------------------------------------
import remarkPopupComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import { getSubjectsMngList, getDataSetSubjectsMngList, saveSubjectsMngList, deleteSubjectsMngList, getEduMngList, getDataSetEduMngList, saveEduMngList, deleteEduMngList, getInstMngList, getDataSetInstMngList, saveInstMngList, deleteInstMngList } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';

const subjectIdx = ref(''); //카드 인덱스
const dataSetMngType = ref(null);
//######################################################################################################################
//과목/과정 관리, 교육기관 관리, 강사 관리 팝업----------------------------------------------------------------------------
//######################################################################################################################
const subjectsMngPopup = ref(null);
const subjectsMngSamplePopup = ref(null);
const subjectsMngList = ref([]);
const currentSubjectsMngList = ref([]);

const eduMngPopup = ref(null);
const eduMngSamplePopup = ref(null);
const eduMngList = ref([]);
const currentEduMngList = ref([]);

const instMngPopup = ref(null);
const instMngSamplePopup = ref(null);
const instMngList = ref([]);
const currentInstMngList = ref([]);

const openDataSetMngPopup = async type => {
    dataSetMngType.value = type;
    if (dataSetMngType.value === 'subjects') {
        await initDataSetMngPopup(true);
        subjectsMngPopup.value.onOpen();
    } else if (dataSetMngType.value === 'edu') {
        await initDataSetMngPopup(true);
        eduMngPopup.value.onOpen();
    } else if (dataSetMngType.value === 'inst') {
        await initDataSetMngPopup(true);
        instMngPopup.value.onOpen();
    }
};

const initDataSetMngPopup = async () => {
    if (dataSetMngType.value === 'subjects') {
        getSubjectsMngList({}, true).then(res => {
            subjectsMngList.value = res.list;
            currentSubjectsMngList.value = _.cloneDeep(res.list);
        });
    } else if (dataSetMngType.value === 'edu') {
        getEduMngList({}, true).then(res => {
            eduMngList.value = res.list;
            currentEduMngList.value = _.cloneDeep(res.list);
        });
    } else if (dataSetMngType.value === 'inst') {
        getInstMngList({}, true).then(res => {
            instMngList.value = res.list;
            currentInstMngList.value = _.cloneDeep(res.list);
        });
    }
};

const seacrhDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        if (JSON.stringify(currentSubjectsMngList.value) !== JSON.stringify(subjectsMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    } else if (dataSetMngType.value === 'edu') {
        if (JSON.stringify(currentEduMngList.value) !== JSON.stringify(eduMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    } else if (dataSetMngType.value === 'inst') {
        if (JSON.stringify(currentInstMngList.value) !== JSON.stringify(instMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initInstMngList, param: {} });
        } else {
            initDataSetMngPopup();
        }
    }
};

const filterDataSetMngPopup = async text => {
    if (dataSetMngType.value === 'subjects') {
        subjectsMngList.value = currentSubjectsMngList.value;
        const filteredData = subjectsMngList.value.filter(el => el.coursesNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        subjectsMngList.value = filteredData;
    } else if (dataSetMngType.value === 'edu') {
        eduMngList.value = currentEduMngList.value;
        const filteredData = eduMngList.value.filter(el => el.centerNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        eduMngList.value = filteredData;
    } else if (dataSetMngType.value === 'inst') {
        instMngList.value = currentInstMngList.value;
        const filteredData = instMngList.value.filter(el => el.centerNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        instMngList.value = filteredData;
    }
};

const addDataSetMngPopup = async () => {
    let index = null;
    let element = '';
    if (dataSetMngType.value === 'subjects') {
        const newData = {
            coursesId: '',
            coursesNm: '',
            remark: '',
            ordSeq: '',
            useYn: 'Y',
            cmd: 'I',
            checked: true
        };
        subjectsMngList.value.push(newData);

        index = subjectsMngList.value.length - 1;
    } else if (dataSetMngType.value === 'edu') {
        const newData = {
            centerId: '',
            centerNm: '',
            remark: '',
            ordSeq: '',
            useYn: 'Y',
            cmd: 'I',
            checked: true
        };
        eduMngList.value.push(newData);

        index = eduMngList.value.length - 1;
    } else if (dataSetMngType.value === 'inst') {
        const newData = {
            instructorId: '',
            instructorNm: '',
            remark: '',
            ordSeq: '',
            useYn: 'Y',
            cmd: 'I',
            checked: true
        };
        instMngList.value.push(newData);

        index = instMngList.value.length - 1;
    }
    setTimeout(() => {
        element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const saveDataSetMngPopup = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (dataSetMngType.value === 'subjects') {
        const checkedList = subjectsMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'edu') {
        const checkedList = eduMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'inst') {
        const checkedList = instMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    }
};

const saveActionDataSetMngPopup = checkedList => {
    openLoading();
    if (dataSetMngType.value === 'subjects') {
        saveSubjectsMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'edu') {
        saveEduMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'inst') {
        openLoading();
        saveInstMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    }
};

const deleteDataSetMngPopup = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (dataSetMngType.value === 'subjects') {
        const checkedList = subjectsMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'edu') {
        const checkedList = eduMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'inst') {
        const checkedList = instMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    }
};

const deleteActionDataSetMngPopup = checkedList => {
    openLoading();
    if (dataSetMngType.value === 'subjects') {
        deleteSubjectsMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'edu') {
        deleteEduMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'inst') {
        openLoading();
        deleteInstMngList(checkedList, true)
            .then(res => {
                if (res.success) {
                    initDataSetMngPopup();
                    endLoading();
                }
            })
            .catch(err => {
                endLoading();
            });
    }
};

const closeDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        if (JSON.stringify(currentSubjectsMngList.value) !== JSON.stringify(subjectsMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            subjectsMngPopup.value.onClose();
        }
    } else if (dataSetMngType.value === 'edu') {
        if (JSON.stringify(currentEduMngList.value) !== JSON.stringify(eduMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            eduMngPopup.value.onClose();
        }
    } else if (dataSetMngType.value === 'inst') {
        if (JSON.stringify(currentInstMngList.value) !== JSON.stringify(instMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            instMngPopup.value.onClose();
        }
    }
};

const closeActionDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        subjectsMngPopup.value.onClose();
    } else if (dataSetMngType.value === 'edu') {
        eduMngPopup.value.onClose();
    } else if (dataSetMngType.value === 'inst') {
        instMngPopup.value.onClose();
    }
};

//######################################################################################################################
//과목/과정 관리, 교육기관 관리, 강사 관리 예시 불러오기 팝업---------------------------------------------------------------
//######################################################################################################################

const sampleDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        subjectsMngSamplePopup.value.onOpen();
    } else if (dataSetMngType.value === 'edu') {
        eduMngSamplePopup.value.onOpen();
    } else if (dataSetMngType.value === 'inst') {
        instMngSamplePopup.value.onOpen();
    }
};

const getSampleDataSetListFunc = async () => {
    openLoading();
    let resData = { list: [] };
    if (dataSetMngType.value === 'subjects') {
        await getDataSetSubjectsMngList({}, true)
            .then(async res => {
                if (res.list.length == 0) {
                    alertMsg('error', '등록 가능한 샘플 데이터가 없습니다.');
                    return null;
                }
                res.list.forEach(val => {
                    val.desc = val.remark;
                    resData.list.push(val);
                });
                endLoading();
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'edu') {
        await getDataSetEduMngList({}, true)
            .then(async res => {
                if (res.list.length == 0) {
                    alertMsg('error', '등록 가능한 샘플 데이터가 없습니다.');
                    return null;
                }
                res.list.forEach(val => {
                    val.desc = val.remark;
                    resData.list.push(val);
                });
                endLoading();
            })
            .catch(err => {
                endLoading();
            });
    } else if (dataSetMngType.value === 'inst') {
        await getDataSetInstMngList({}, true)
            .then(async res => {
                if (res.list.length == 0) {
                    alertMsg('error', '등록 가능한 샘플 데이터가 없습니다.');
                    return null;
                }
                res.list.forEach(val => {
                    val.desc = val.remark;
                    resData.list.push(val);
                });
                endLoading();
            })
            .catch(err => {
                endLoading();
            });
    }
    return resData;
};

const applySampleDataSetMngPopup = async selectItems => {
    if (dataSetMngType.value === 'subjects') {
        // 예시 팝업 적용 버튼 클릭
        const filteredData = getDuplicatedData(subjectsMngList.value, selectItems, 'coursesId', 'coursesNm'); // 중복된 항목 추출
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
    } else if (dataSetMngType.value === 'edu') {
        const filteredData = getDuplicatedData(eduMngList.value, selectItems, 'centerId', 'centerNm'); // 중복된 항목 추출
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
    } else if (dataSetMngType.value === 'inst') {
        const filteredData = getDuplicatedData(instMngList.value, selectItems, 'instructorId', 'instructorNm'); // 중복된 항목 추출
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
    }
};

const applyActionSampleDataSetMngList = async confirmList => {
    let index = null;
    let element = null;
    if (dataSetMngType.value === 'subjects') {
        await confirmList.forEach(val => {
            val.cmd = 'I';
            subjectsMngList.value.push(val);
        });
        subjectsMngSamplePopup.value.onClose();

        index = subjectsMngList.value.length - 1;
    } else if (dataSetMngType.value === 'edu') {
        await confirmList.forEach(val => {
            val.cmd = 'I';
            eduMngList.value.push(val);
        });
        eduMngSamplePopup.value.onClose();

        index = eduMngList.value.length - 1;
    } else if (dataSetMngType.value === 'inst') {
        await confirmList.forEach(val => {
            val.cmd = 'I';
            instMngList.value.push(val);
        });
        instMngSamplePopup.value.onClose();

        index = instMngList.value.length - 1;
    }
    setTimeout(() => {
        element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const closeSamplDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        subjectsMngSamplePopup.value.onClose();
    } else if (dataSetMngType.value === 'edu') {
        eduMngSamplePopup.value.onClose();
    } else if (dataSetMngType.value === 'inst') {
        instMngSamplePopup.value.onClose();
    }
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################

//card 과목/과정 팝업*******************************************************************************************************************************
const subjectsPopup = ref(null);
const eduPopup = ref(null);
const instPopup = ref(null);

const addSubject = idx => {
    subjectIdx.value = idx;
    subjectsPopup.value.onOpen();
};

const getSubjectsListFunc = async () => {
    let data;
    await getSubjectsMngList().then(res => {
        res.list.forEach(val => {
            val.desc = val.remark;
        });
        data = res;
    });
    return data;
};

const applySubjectsPopup = row => {
    if (subjectsPopup.value) {
        subjectsPopup.value.onClose();
    }
    if (row && row[0]) {
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingCourse = row[0].coursesId;
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingTypeNm = row[0].coursesNm;
    }
};

const removeSubject = index => {
    annualTrainingPlanStore.inputForm[index].trainingCourse = '';
    annualTrainingPlanStore.inputForm[index].trainingTypeNm = '';
};

//card 교육기관 팝업*******************************************************************************************************************************
const addEdu = idx => {
    subjectIdx.value = idx;
    eduPopup.value.onOpen();
};

const getEduListFunc = async () => {
    let data;
    await getEduMngList().then(res => {
        res.list.forEach(val => {
            val.desc = val.remark;
        });
        data = res;
    });
    return data;
};

// 팝업 닫기
const closeEduPopup = row => {
    if (eduPopup.value) {
        eduPopup.value.onClose();
    }

    if (row && row[0]) {
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingInstitute = row[0].centerId;
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingInstituteNm = row[0].centerNm;
    }
};

const removeEdu = index => {
    annualTrainingPlanStore.inputForm[index].trainingInstitute = '';
    annualTrainingPlanStore.inputForm[index].trainingInstituteNm = '';
};

//card 강사 팝업*******************************************************************************************************************************
const addInst = idx => {
    subjectIdx.value = idx;
    instPopup.value.onOpen();
};

const getInstListFunc = async () => {
    let data;
    await getInstMngList().then(res => {
        res.list.forEach(val => {
            val.desc = val.remark;
        });
        data = res;
    });
    return data;
};

const closeInstPopup = row => {
    if (instPopup.value) {
        instPopup.value.onClose();
    }
    if (row && row[0]) {
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingInstructor = row[0].instructorId;
        annualTrainingPlanStore.inputForm[subjectIdx.value].trainingInstructorNm = row[0].instructorNm;
    }
};

const removeInst = index => {
    annualTrainingPlanStore.inputForm[index].trainingInstructor = '';
    annualTrainingPlanStore.inputForm[index].trainingInstructorNm = '';
};

//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref
const peopleIdx = ref('');

const addPeople = idx => {
    peopleIdx.value = idx;
    peoplePopup.value.onOpen();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const selectPeople = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }

    const users = selectUserStore.getSelectedUser();

    users.forEach(user => {
        annualTrainingPlanStore.inputForm[peopleIdx.value].hrList = users.map(user => ({
            id: user.hrId,
            name: user.hrNm,
            hrId: user.hrId,
            targetType: 'ATP'
        }));
    });
};

const formattedHrList = item => {
    const users = selectUserStore.getSelectedUser();

    return item.map(hr => ({
        id: hr.hrId, // hrList의 id를 hrId로 매핑
        name: hr.hrNm // hrList의 hrNm을 name으로 매핑
    }));
};

//-----------------------------------------------
// [아코디언]

import { nextTick } from 'vue';
import CustomEase from 'gsap/CustomEase';

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

//-----------------------------------------------

// [토글버튼]
const toggleCheckYn = (index, event) => {
    annualTrainingPlanStore.inputForm[index].checked = true;
    const field = event.target.name;
    const isChecked = event.target.checked;

    annualTrainingPlanStore.inputForm[index][field] = isChecked ? 'Y' : 'N';
};

//-----------------------------------------------
//-----------------------------------------------
//useYn 체크

const toggleUseYn = (index, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    annualTrainingPlanStore.inputForm[index].useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
};

//-----------------------------------------------
// ---------------------------------------------------
// 값이 바뀌면 해당 데이터 checked
const chkData = data => {
    if (annualTrainingPlanStore.loadPreviousYn && data.writeYear === annualTrainingPlanStore.searchParam.writeYear) {
        alertMsg('error', t('msgPreviousSaveError'));
        data.checked = false;
        return;
    } else {
        data.checked = true;
    }
};
// ---------------------------------------------------

/* ------------------------------------- */
//[버튼리스트]

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnPreYear'];

// 데이터 변경 여부를 확인하는 함수
const isDataModified = (originData, inputForm) => {
    // 두 배열 비교: docSeq가 같은 항목에 대해 모든 필드 비교
    const isModified = originData.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = inputForm.find(currentItem => currentItem.docNo === originItem.docNo);

        return (
            matchingItem &&
            Object.keys(originItem).some(key => {
                const originValue = originItem[key];
                const matchingValue = matchingItem[key];

                // 깊은 비교를 위해 JSON.stringify 사용
                return JSON.stringify(originValue) !== JSON.stringify(matchingValue);
            })
        );
    });

    // cmd 값이 'I'인 항목이 하나라도 있는지 확인
    const hasInsertedItems = inputForm.some(item => item.cmd === 'I');

    return isModified || hasInsertedItems;
};

// 목록으로
btnBack(() => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        confirmMsg('warning', t('msgPreviousConfirm'), null, { fun: annualTrainingPlanStore.btnBack, param: '' });
        return;
    }
    if (isDataModified(annualTrainingPlanStore.originData, annualTrainingPlanStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: annualTrainingPlanStore.btnBack, param: '' });
    } else {
        annualTrainingPlanStore.btnBack();
    }

    educationStore.searchParam.searchText = annualTrainingPlanStore.searchParam.writeYear;
});
const setSignatureRef = (year, el) => {
    if (el) {
        annualTrainingPlanStore.signatureComponent[year] = el;
    }
};

const writeYearChanged = async () => {
    annualTrainingPlanStore.btnSearch(true);
};
// 조회 버튼
btnSearch(() => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        confirmMsg('warning', t('msgPreviousConfirm'), null, { fun: searchAction, param: '' });
        return;
    }
    if (isDataModified(annualTrainingPlanStore.originData, annualTrainingPlanStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: searchAction, param: '' });
    } else {
        searchAction();
    }
});
const searchAction = async () => {
    await annualTrainingPlanStore.btnSearch();
    // await signatureComponent.value[annualTrainingPlanStore.searchParam.writeYear].Search();
};
// 저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('inputForm', true);
    if (isValid) {
        annualTrainingPlanStore.btnSave();
    }
});

//추가 버튼
btnAdd(async () => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        confirmMsg('warning', t('msgPreviousConfirm'), null, { fun: btnAddAction, param: '' });
        return;
    } else {
        btnAddAction();
    }
});

const btnAddAction = type => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        //전년도 불러오기 후 데이터 추가 시, 전년도 데이터 제거
        annualTrainingPlanStore.inputForm = annualTrainingPlanStore.inputForm.filter(item => item.writeYear === annualTrainingPlanStore.searchParam.writeYear || item.cmd !== 'U');
        annualTrainingPlanStore.loadPreviousYn = false;
    }
    annualTrainingPlanStore.btnAdd();
    accordionFocusing();
};

// 삭제 버튼
btnDelete(() => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    let saveData = annualTrainingPlanStore.inputForm.filter(el => el.checked === true);
    if (saveData.length < 1) {
        alertMsg('error', t('msgNoItem'));
        return;
    }
    confirmMsg('info', '삭제 하시겠습니까?', '', { fun: btnDeleteAction, param: '' });
});

const btnDeleteAction = () => {
    annualTrainingPlanStore.btnDelete();
};

// 출력 버튼
btnDownload(() => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    }
    if (isDataModified(annualTrainingPlanStore.originData, annualTrainingPlanStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: annualTrainingPlanStore.btnDownload, param: '' });
    } else {
        annualTrainingPlanStore.btnDownload();
        // confirmMsg('info', t('msgPrint'), null, { fun: annualTrainingPlanStore.btnDownload });
    }
});

// 전년도 불러오기 버튼
btnPreYear(() => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        alertMsg('error', t('msgPreviousError'));
        return;
    } else {
        if (annualTrainingPlanStore.inputForm.filter(item => item.cmd === 'I').length > 0) {
            confirmMsg('warning', t('msgSaveContinue'), null, { fun: btnPreYearAction, param: '' });
            return;
        }
        confirmMsg('info', t('msgPreviousLoad'), '', { fun: btnPreYearAction, param: '' });
    }
});

const btnPreYearAction = () => {
    //전년도 데이터 불러오기 전 추가된 데이터들은 제거
    annualTrainingPlanStore.inputForm = annualTrainingPlanStore.inputForm.filter(item => item.cmd !== 'I');
    annualTrainingPlanStore.btnPreYear();
    accordionFocusing();
};

const cardBtnAdd = async () => {
    if (annualTrainingPlanStore.loadPreviousYn) {
        confirmMsg('warning', t('msgPreviousConfirm'), null, { fun: btnAddAction, param: '' });
        return;
    } else {
        btnAddAction();
    }
};

const accordionFocusing = () => {
    const index = annualTrainingPlanStore.inputForm.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};
/* ------------------------------------- */

onMounted(() => {
    annualTrainingPlanStore.loadPreviousYn = false;
    annualTrainingPlanStore.inputForm = {};
    // 필요한 초기 로직이 있다면 여기에서 처리
    const param = setRouterParam();
    if (param) {
        annualTrainingPlanStore.inputForm.writeYear = param.writeYear;
        annualTrainingPlanStore.searchParam.writeYear = param.writeYear;
    }
    if (!annualTrainingPlanStore.searchParam.writeYear) {
        //현재날짜 세팅
        annualTrainingPlanStore.searchParam.writeYear = sAndHApiStore.currentDate;
    }
    // annualTrainingPlanStore.signature = signatureComponent.value[annualTrainingPlanStore.searchParam.writeYear];
    annualTrainingPlanStore.btnSearch(true);
});
</script>

<style lang="scss" scoped>
.selected-border {
    border: 1px solid blue;
}

.text-right {
    text-align: right;
}
.accordion {
    .not-accrdion {
        background: transparent;
        border: none;
        > button {
            padding: 0;
        }
    }
}
.btn-edu {
    transition: all 0.4s;
    &:hover {
        background: #ebedff;
    }
}
</style>
