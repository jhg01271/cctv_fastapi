<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="box h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="control-field ui form" id="jobForm">
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="jobCompAssessStore.inputForm.cmd" :types="signatureType" targetType="JCA" :writeYear="jobCompAssessStore.inputForm.writeYear" :docNo="jobCompAssessStore.inputForm.docNo" :useYn="jobCompAssessStore.inputForm.useYn"></ISignature>
                </div>
                <hr />
                <!-- 입력 필드 -->

                <div class="pa2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="writeYear" required="required">
                                    <span>{{ '작성년도' }}</span>
                                </label>
                                <input v-model="jobCompAssessStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="성명" required>
                                    <span>성명</span>
                                </label>
                                <i-chips :chips="[{ id: jobCompAssessStore.inputForm.hrId, nm: jobCompAssessStore.inputForm.hrNm }]" @popup="addPeople" required @removeChip="removeHr"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label>
                                    <span>조직</span>
                                </label>
                                <input class="br4px" type="text" :value="jobCompAssessStore.inputForm.orgnNm" readonly placeholder="인원 선택 시 자동으로 기입됩니다." />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label>
                                    <span>직위</span>
                                </label>
                                <input class="br4px" type="text" :value="jobCompAssessStore.inputForm.jbrpNm" readonly placeholder="인원 선택 시 자동으로 기입됩니다." />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="createdAt">등록일자</label>
                                <input type="text" :value="formatDate(jobCompAssessStore.inputForm.createdAt)" class="datepicker w100p radius" id="createdAt" disabled />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="jobCompAssessStore.inputForm.useYn === 'Y'" v-input type="checkbox" class="df switch" @change="jobCompAssessStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt2-2rem pa2-5rem bcF9FAFF br4px">
                        <div class="pr accordion mb2rem">
                            <div class="list not-accordion">
                                <button type="button" class="radius w15rem df jcfe aic gap8px" @click="toggleAccordion(0)" :class="{ active: jobCompAssessStore.jobCompAssessSegments[1] }">
                                    <!--🐸 과목/과정 관리 -->
                                    <p>관리 기능</p>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M11.6249 12.3752L11.6249 11.6252M12.3749 12.3752L12.3749 11.6252M11.6249 19.3752L11.6249 18.6252M12.3749 19.3752L12.3749 18.6252M11.6249 5.37524L11.6249 4.62524M12.3749 5.37524L12.3749 4.62524M12 11C12.5523 11 13 11.4477 13 12C13 12.5523 12.5523 13 12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11ZM12 18C12.5523 18 13 18.4477 13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19C11 18.4477 11.4477 18 12 18ZM12 4C12.5523 4 13 4.44772 13 5C13 5.55228 12.5523 6 12 6C11.4477 6 11 5.55228 11 5C11 4.44772 11.4477 4 12 4Z" stroke="black" stroke-width="2" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div id="accordion0" class="pa t3rem r0 segment oh zi2">
                                    <div class="df fdc jcfe bd1pxsolid3248F6 br4px oh">
                                        <!-- 🦖 과목/과정 관리 Content -->
                                        <button type="button" v-button class="w15rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openJobManagePopup">직무 관리</button>
                                        <button type="button" v-button class="w15rem bcFFFFFF c3248F6 pa1rem" @click="openJobLevelManagePopup">직무 수준 관리</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Data Card -->
                        <div v-for="(item, index) in jobCompAssessStore.inputForm.detailList" :key="index" class="w100p df jcc bd1pxsolidE1E6ED box mb1-6rem es-fww" :class="{ 'selected-border': selectedIndex === index }">
                            <div class="w5rem df jcc aic bdr1pxsolidE1E6ED es-h5rem es-w100p es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                <input type="checkbox" v-input v-model="item.checked" />
                            </div>
                            <div class="w100p pa2-2rem" @click="selectedIndex = index">
                                <div class="row flex gutters1rem" @click="focusedCardChanged(index)">
                                    <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label required>
                                                <span>직무 분야</span>
                                            </label>
                                            <i-chips :chips="[{ id: item.jobId, nm: item.jobNm }]" @popup="addJob" :required="item.checked" @removeChip="removeJobField"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label required>
                                                <span>직무명</span>
                                            </label>
                                            <input class="br4px" v-model="item.jobNm" type="text" v-input placeholder="직무 분야 선택 시 자동으로 기입됩니다." readonly />
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-6 es-grid12-12" :key="componentKey">
                                        <div class="field">
                                            <label>
                                                <span>요구 수준</span>
                                            </label>
                                            <i-chips :chips="[{ id: item.demandLevelId, nm: item.demandLevelNm }]" @popup="addDemandLevel" :id="'jobLvlList' + index" @removeChip="removeDemandLevel"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-6 es-grid12-12" :key="componentKey">
                                        <div class="field">
                                            <label>
                                                <span>현재 수준</span>
                                            </label>
                                            <i-chips i-chips :chips="[{ id: item.currentLevelId, nm: item.currentLevelNm }]" @popup="addCurrentLevel" :id="'jobLvlList' + index" @removeChip="removeCurrentLevel"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label>
                                                <span>업무 내용</span>
                                            </label>
                                            <textarea class="minh10rem radius" v-model="item.content" placeholder="직무 분야 선택 시 자동으로 기입됩니다." readonly></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label>
                                                <span>교육 계획</span>
                                            </label>
                                            <i-chips :chips="[{ id: item.trainingCourse, nm: item.trainingCourseNm }]" @popup="addTraining" @removeChip="removeTrainingCourse"></i-chips>
                                        </div>
                                    </div>

                                    <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for="">교육기관</label>
                                            <input class="br4px" type="text" v-input v-model="item.trainingInstituteNm" placeholder="교육 계획 선택 시 자동 기입" readonly />
                                        </div>
                                    </div>

                                    <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for="useYn">사용 여부</label>
                                            <div class="df aic h4-4rem">
                                                <input :checked="item.useYn === 'Y'" :true-value="'Y'" :false-value="'N'" v-model="item.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="toggleDetailUseYn(item, $event)" />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="grid12-12">
                                    <div class="field">
                                        <label>
                                            <span>필요 교육 내용</span>
                                        </label>
                                        <textarea class="minh10rem radius" v-model="item.trainingContent" placeholder="필요 교육 내용을 입력하세요." @change="item.checked = true"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 추가 버튼 -->
                        <div class="w100p tac">
                            <button v-button @click="btnDetailAdd">
                                <svg class="mr8px vam" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                                <span class="fs1-5rem"> 직무 적격성 평가 추가 </span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <!-- 팝업 컴포넌트 -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w70rem md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser>

                <div class="form ui tar mt2-5rem">
                    <!-- <button type="button" class="btn w80px radius active" v-button @click="btnSave">
                        <span>저장</span>
                </button>-->
                    <!-- <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                    <span>{{ t('close') }}</span>
                </button> -->
                </div>
            </div>
        </i-PopupDialog>
        <!-- 팝업 컴포넌트 -->

        <i-PopupDialog ref="jobManagePopup">
            <div class="contents form ui w80rem md-w100p">
                <DataSetManagePopup :title="'직무 관리'" :subTitle="'직무분야'" :datasetYn="'N'" id="jobId" nm="jobNm" :popupDataList="jobCompAssessStore.jobManageList" :cardComponent="CategoryPopupComponent" @filter="filterJobManagePopup" @search="searchJobManagePopup" @sample="sampleJobManagePopup" @add="addJobManagePopup" @save="saveJobManagePopup" @delete="deleteJobManagePopup" @close="closeJobManagePopup"> </DataSetManagePopup>
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="jobUseManagePopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'직무 분야'" filterKey="jobText" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getJobListFunc" @close="closeJobUseManagePopup" />
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="jobLevelManagePopup">
            <div class="contents w70rem md-w100p">
                <!-- 📃 수준 관리 -->
                <DataSetManagePopup :title="'직무 수준 관리'" :subTitle="'이름'" :datasetYn="'Y'" id="jobLevelId" nm="jobLevelNm" :popupDataList="jobCompAssessStore.jobLevelManageList" :cardComponent="remarkPopupComponent" @filter="filterJobLevelManagePopup" @search="searchJobLevelManagePopup" @sample="sampleJobLevelManagePopup" @add="addJobLevelManagePopup" @save="saveJobLevelManagePopup" @delete="deleteJobLevelManagePopup" @close="closeJobLevelManagePopup"> </DataSetManagePopup>
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="jobLevelManageSamplePopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'직무 수준 예시'" filterKey="jobLevelNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getJobLevelManageSampleListFunc" @apply="applyJobLevelManageSamplePopup" @close="closeJobLevelManageSamplePopup" />
            </div>
        </i-PopupDialog>

        <i-PopupDialog ref="demandLevelPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'요구 수준'" filterKey="jobLevelNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getJobLevelManageListFunc" @close="closeDemandLevelPopup" />
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="currentLevelPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :component="BaseSelectAccordionComponent" :title="'현재 수준'" filterKey="jobLevelNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getJobLevelManageListFunc" @close="closeCurrentLevelPopup" />
            </div>
        </i-PopupDialog>

        <!-- 팝업 컴포넌트 -->
        <i-PopupDialog ref="trainingPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <!-- trainingInstitute -->
                <base-select-popup :title="'교육 계획'" filterKey="trainingTypeNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getAnnualTrainingPlan" :fetch-param="{ compId: jobCompAssessStore.inputForm.compId, writeYear: jobCompAssessStore.inputForm.writeYear }" @close="closeTrainingPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { nextTick } from 'vue';
import { gsap } from 'gsap';
// Overlay
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { saveJobCompAssess, saveJobCompAssessDetail, getDataSetJobLevelManageList } from '@/stores/safewiz/support/api/JobCompAssessApi.js';
// BaseView
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, setRouterParam, validationStore, ref, toastPopup, onMounted, t, confirmMsg, getCompId, formatDate, alertMsg, btnBack, btnSearch, btnSave, btnDelete, btnDownload, btnAdd, getDuplicatedData } = BaseView();
// Button List
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];

import { getSystemCodeCustom } from '@/stores/safewiz/dataset/api/datasetApi';

import { getJobLevelManageList } from '@/stores/safewiz/support/api/JobCompAssessApi.js';

import ISignature from '@/components/common/iSignature.vue';

import _ from 'lodash';
// Router
import router from '@/router';
// 인원관리 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const peoplePopup = ref(null);
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
// 직무관리 & 직무수준관리 팝업
import remarkPopupComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import CategoryPopupComponent from '@/views/system/base/popup/popupComponent/CategoryDataSetManageComponent.vue';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
const jobManagePopup = ref(null);
const jobUseManagePopup = ref(null);
const jobLevelManagePopup = ref(null);
const jobLevelManageSamplePopup = ref(null);

// 요구 수준 & 현재 수준 팝업
const demandLevelPopup = ref(null); //요구 수준
const currentLevelPopup = ref(null); //현재 수준

const selectedIndex = ref(null);

// 교육 계획 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getAnnualTrainingPlan } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';
const trainingPopup = ref(null);

//-----------------------------------------------
// [스토어]
import { useJobCompAssessStore } from '@/stores/safewiz/support/JobCompAssess.js';
const jobCompAssessStore = useJobCompAssessStore();

const focusedCardIdx = ref(0);
// [서명]
const signatureType = ref([
    {
        name: '작성', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '평가', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
const getSignatureList = param => {};
//-----------------------------------------------
//-----------------------------------------------
// [아코디언]
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    jobCompAssessStore.jobCompAssessSegments[index] = !jobCompAssessStore.jobCompAssessSegments[index];

    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
};
const initAccordion = () => {
    jobCompAssessStore.jobCompAssessSegments.forEach((el, index) => {
        accordionSet(index);
    });
};
const accordionSet = (index, duration) => {
    const segmentId = `accordion${index}`;
    const segment = document.getElementById(segmentId);

    if (segment) {
        gsap.to(segment, {
            height: jobCompAssessStore.jobCompAssessSegments[index] ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    }
};

// 팝업 열기 함수
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

// 기존 팝업 열기 및 닫기 함수를 통합하여 호출
// 인원 팝업
const addPeople = () => openPopup(peoplePopup);
const closePeoplePopup = () => closePopup(peoplePopup);

const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e && e.hrId) {
        jobCompAssessStore.inputForm.hrId = e.hrId;
        jobCompAssessStore.inputForm.hrNm = e.hrNm;
        jobCompAssessStore.inputForm.orgnId = e.orgnId; // 조직명
        jobCompAssessStore.inputForm.orgnNm = e.orgnNm; // 조직명
        jobCompAssessStore.inputForm.jbrpId = e.jbrp; // 직위
        jobCompAssessStore.inputForm.jbrpNm = e.jbrpNm; // 직위
    }
};
//######################################################################################################################
// 직무 관리 팝업--------------------------------------------------------------------------------------------------------
//######################################################################################################################

// 팝업 열기
const openJobManagePopup = async () => {
    openLoading();
    await initJobManageList(true);
    jobManagePopup.value.onOpen();
    endLoading();
};

// 팝업 닫기
const closeJobManagePopup = async () => {
    if (JSON.stringify(jobCompAssessStore.currentJobManageList) !== JSON.stringify(jobCompAssessStore.jobManageList)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionJobManagePopup, param: {} });
    } else {
        closeActionJobManagePopup();
    }
};

const closeActionJobManagePopup = async () => {
    jobManagePopup.value.onClose();
};

// 팝업 필터 조회
const filterJobManagePopup = text => {
    jobCompAssessStore.filterJobManageList(text);
};

// 팝업 데이터 초기화
const initJobManageList = async notify => {
    jobCompAssessStore.initJobManageList(notify);
};

// 팝업 데이터 조회
const searchJobManagePopup = () => {
    if (JSON.stringify(jobCompAssessStore.currentJobManageList) !== JSON.stringify(jobCompAssessStore.jobManageList)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initJobManageList, param: {} });
    } else {
        initJobManageList(true);
    }
};

// 팝업 예시 데이터 조회
const sampleJobManagePopup = async () => {
    //2025.06.10 미사용 처리, 추후 다시 사용될 수도 있음. 추후 사용하게 될 경우 dataset.tb_dataset_job_competency_assessment_jobmgmt 사용
    await jobCompAssessStore.searchDataSetJobManageList();
    // 새로 추가된 항목으로 포커스 이동

    setTimeout(() => {
        const index = jobCompAssessStore.jobManageList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 팝업 데이터 추가
const addJobManagePopup = async () => {
    await jobCompAssessStore.addJobManageList();
    setTimeout(() => {
        const index = jobCompAssessStore.jobManageList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 팝업 데이터 추가 및 수정
const saveJobManagePopup = async () => {
    const checkedList = jobCompAssessStore.jobManageList.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionJobManagePopup, param: true });
    }
};

const saveActionJobManagePopup = () => {
    jobCompAssessStore.saveJobManageData();
};

// 팝업 데이터 삭제
const deleteJobManagePopup = async () => {
    const checkedList = jobCompAssessStore.jobManageList.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('msgDelete'), '', { fun: deleteActionJobManagePopup, param: true });
};

const deleteActionJobManagePopup = () => {
    jobCompAssessStore.deleteJobManageData();
};

// 카드 리스트 직무 분야 baseSelectPopup---------------------------------------------------------------------------
import { getCardJobManageList } from '@/stores/safewiz/support/api/JobCompAssessApi.js';
const getJobListFunc = async () => {
    const resData = { list: [] };
    await getCardJobManageList({ hrId: jobCompAssessStore.inputForm.hrId }, false).then(res => {
        if (res.list.length > 0) {
            res.list.forEach(val => {
                val.desc = val.remark;
                val.jobText = val.jobCategory + ' | ' + val.jobNm;
                resData.list.push(val);
            });
        }
    });
    return resData;
};
//카드 리스트 직무 분야 추가
const addJob = async () => {
    if (jobCompAssessStore.inputForm.hrId == '') {
        alertMsg('error', '먼저 성명을 선택해 주세요.');
        return;
    }
    //openLoading();
    await initJobManageList(true);
    jobUseManagePopup.value.onOpen();
    //endLoading();
};

//팝업 선택 및 닫기 이벤트
const closeJobUseManagePopup = selectedItem => {
    if (selectedItem.length > 0) {
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobFieldId = selectedItem[0].jobId;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobCategory = selectedItem[0].jobCategory;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobId = selectedItem[0].jobId;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobNm = selectedItem[0].jobNm;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].checked = true;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].content = selectedItem[0].remark;
    }
    jobUseManagePopup.value.onClose();
};

//chips 삭제 이벤트
const removeJobField = () => {
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobFieldId = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobCategory = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobId = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].jobNm = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].content = '';
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################

//######################################################################################################################
// 직무 수준 관리 팝업---------------------------------------------------------------------------------------------------
//######################################################################################################################

const openJobLevelManagePopup = async () => {
    openLoading();
    await initJobLevelManageList();
    jobLevelManagePopup.value.onOpen();
    endLoading();
};

const closeJobLevelManagePopup = () => {
    if (JSON.stringify(jobCompAssessStore.currentJobLevelManageList) !== JSON.stringify(jobCompAssessStore.jobLevelManageList)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionJobLevelManagePopup, param: {} });
    } else {
        closeActionJobLevelManagePopup();
    }
};

const closeActionJobLevelManagePopup = () => {
    jobLevelManagePopup.value.onClose();
};

//팝업 데이터 초기화
const initJobLevelManageList = async () => {
    jobCompAssessStore.initJobLevelManageList(true);
};

const filterJobLevelManagePopup = text => {
    jobCompAssessStore.filterJobLevelManageList(text);
};

const searchJobLevelManagePopup = async () => {
    if (JSON.stringify(jobCompAssessStore.currentJobLevelManageList) !== JSON.stringify(jobCompAssessStore.jobLevelManageList)) {
        confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initJobLevelManageList, param: {} });
    } else {
        initJobLevelManageList();
    }
};

// 직무 수준 관리 팝업 예시 데이터 조회
const sampleJobLevelManagePopup = async () => {
    jobLevelManageSamplePopup.value.onOpen();
};

//직무 수준 관리 팝업 데이터 추가
const addJobLevelManagePopup = async () => {
    await jobCompAssessStore.addJobLevelManageList();
    setTimeout(() => {
        const index = jobCompAssessStore.jobLevelManageList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

//직무 수준 관리 팝업 저장 및 수정
const saveJobLevelManagePopup = async () => {
    const checkedList = jobCompAssessStore.jobLevelManageList.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionJobLevelManagePopup, param: true });
    }
};

const saveActionJobLevelManagePopup = () => {
    jobCompAssessStore.saveJobLevelManageData();
};

//직무 수준 관리 팝업 데이터 삭제 (사용안함 처리)
const deleteJobLevelManagePopup = async () => {
    const checkedList = jobCompAssessStore.jobLevelManageList.filter(val => val.checked == true);
    if (checkedList.length == 0) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('msgDelete'), '', { fun: deleteActionJobLevelManagePopup, param: true });
};

const deleteActionJobLevelManagePopup = () => {
    jobCompAssessStore.deleteJobLevelManageData();
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################

const focusedCardChanged = index => {
    focusedCardIdx.value = index;
};
//######################################################################################################################
//요구수준---------------------------------------------------------------------------------------------------------------
//######################################################################################################################
const getJobLevelManageListFunc = async () => {
    let data;
    await getJobLevelManageList().then(res => {
        res.list.forEach(val => {
            val.desc = val.remark;
        });
        data = res;
    });

    return data;
};

const addDemandLevel = async () => {
    demandLevelPopup.value.onOpen();
};

//팝업 선택 및 닫기 이벤트
const closeDemandLevelPopup = selectedItem => {
    if (selectedItem.length > 0) {
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].demandLevelId = selectedItem[0].jobLevelId;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].demandLevelNm = selectedItem[0].jobLevelNm;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].checked = true;
    }
    demandLevelPopup.value.onClose();
};

//chips 삭제 이벤트
const removeDemandLevel = () => {
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].demandLevelId = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].demandLevelNm = '';
};

const getJobLevelManageSampleListFunc = async () => {
    openLoading();
    let resData = { list: [] };
    await getDataSetJobLevelManageList({}, true)
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

    return resData;
};
const applyJobLevelManageSamplePopup = async selectItems => {
    const filteredData = getDuplicatedData(jobCompAssessStore.jobLevelManageList, selectItems, 'jobLevelId', 'jobLevelNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === selectItems.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applyActionJobLevelManageSampleList(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applyActionJobLevelManageSampleList(filteredData.deDuplicatedDataList) });
};

const applyActionJobLevelManageSampleList = async confirmList => {
    await confirmList.forEach(val => {
        val.cmd = 'I';
        jobCompAssessStore.jobLevelManageList.push(val);
    });
    jobLevelManageSamplePopup.value.onClose();
    setTimeout(() => {
        const index = jobCompAssessStore.jobLevelManageList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const closeJobLevelManageSamplePopup = () => {
    jobLevelManageSamplePopup.value.onClose();
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################

//######################################################################################################################
//현재수준---------------------------------------------------------------------------------------------------------------
//######################################################################################################################
const addCurrentLevel = async () => {
    currentLevelPopup.value.onOpen();
};

//팝업 선택 및 닫기 이벤트
const closeCurrentLevelPopup = selectedItem => {
    if (selectedItem.length > 0) {
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].currentLevelId = selectedItem[0].jobLevelId;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].currentLevelNm = selectedItem[0].jobLevelNm;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].checked = true;
    }
    currentLevelPopup.value.onClose();
};

//chips 삭제 이벤트
const removeCurrentLevel = () => {
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].currentLevelId = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].currentLevelNm = '';
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################

// 교육계획 팝업
const addTraining = () => openPopup(trainingPopup);
const closeTrainingPopup = selected => {
    if (selected.length > 0) {
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingCourse = selected[0].trainingCourse;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingCourseNm = selected[0].trainingTypeNm;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingInstitute = selected[0].trainingInstitute;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingInstituteNm = selected[0].trainingInstituteNm;
        jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].checked = true;
    }
    closePopup(trainingPopup);
};

const removeTrainingCourse = () => {
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingCourse = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingCourseNm = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingInstitute = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].trainingInstituteNm = '';
    jobCompAssessStore.inputForm.detailList[focusedCardIdx.value].checked = true;
};

const toggleDetailUseYn = (item, event) => {
    // item.useYn = item.useYn ? 'Y' : 'N';
    item.checked = true;
};

/* --------- 버튼 클릭 이벤트 --------- */

btnSearch(async () => {
    searchDetail(jobCompAssessStore.inputForm, true);
});

const searchDetail = async (param, notify) => {
    await jobCompAssessStore.btnDetail(param, notify);
    //저장된 직무분야의 업무 내용 조회
    setTimeout(() => {
        signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }, 100);
    jobCompAssessStore.inputForm.detailList?.forEach(val => {
        getCardJobManageList({ hrId: jobCompAssessStore.inputForm.hrId }, false).then(res => {
            res.list.forEach(item => {
                if (val.jobId === item.jobId && val.jobCategory === item.jobCategory) {
                    val.content = item.remark;
                }
            });
        });
    });
};
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/JobCompetencyAssessment');
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('jobForm', true);
    const saveDataList = jobCompAssessStore.inputForm.detailList.filter(el => el.checked);
    if (isValid && saveDataList.length === 0) {
        confirmMsg('info', '저장 하시겠습니까?', null, { fun: save, param: true });
    }

    if (isValid && saveDataList.length > 0) {
        //
        confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveDetail, param: true });
    }
});

const save = notify => {
    let saveParam = _.cloneDeep(jobCompAssessStore.inputForm);
    openLoading();
    saveJobCompAssess(saveParam, notify)
        .then(res => {
            jobCompAssessStore.signature.updateTaskUseYn('JCA', jobCompAssessStore.inputForm.writeYear, jobCompAssessStore.inputForm.docNo);
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};

const saveDetail = notify => {
    let saveParam = _.cloneDeep(jobCompAssessStore.inputForm);
    saveParam.detailList = jobCompAssessStore.inputForm.detailList.filter(el => el.checked);
    openLoading();
    saveJobCompAssessDetail(saveParam, notify)
        .then(res => {
            if (res.success) {
                layoutStore.useBtnList = uButtonList;
                jobCompAssessStore.inputForm.docNo = res.result.docNo;
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            // jobCompAssessStore.signature.setApprovalInfo(`${jobCompAssessStore.inputForm.writeYear}${jobCompAssessStore.inputForm.docNo}`);
            jobCompAssessStore.signature.setApprovalInfo('JCA', jobCompAssessStore.inputForm.writeYear, jobCompAssessStore.inputForm.docNo);
            // jobCompAssessStore.signature.updateTaskUseYn(jobCompAssessStore.inputForm.useYn, jobCompAssessStore.inputForm.writeYear, jobCompAssessStore.inputForm.docNo);
            searchDetail(jobCompAssessStore.inputForm, false);
            endLoading();
        });
};

btnDelete(() => {
    jobCompAssessStore.btnDeleteDetail();
});

btnDownload(() => {
    if (jobCompAssessStore.inputForm.detailList.filter(el => el.useYn === 'Y' && el.cmd === 'U').length === 0) {
        alertMsg('warning', '사용중인 항목이 없습니다.');
        return;
    }
    const checkedDetailList = jobCompAssessStore.inputForm.detailList.filter(el => el.checked && el.cmd === 'U');
    if (checkedDetailList.length === 0) {
        jobCompAssessStore.btnDownload([jobCompAssessStore.inputForm.docNo]);
    } else {
        jobCompAssessStore.btnDownload(
            checkedDetailList.map(el => el.docSeq),
            jobCompAssessStore.inputForm.docNo
        );
    }
});

const componentKey = ref(0);
btnAdd(() => {
    jobCompAssessStore.initInputForm();
    layoutStore.useBtnList = iButtonList;
    componentKey.value++;
});

const btnDetailAdd = () => {
    jobCompAssessStore.inputForm.detailList.push({
        writeYear: jobCompAssessStore.inputForm.writeYear,
        docType: jobCompAssessStore.inputForm.docType,
        docNo: jobCompAssessStore.inputForm.docNo,
        compId: jobCompAssessStore.inputForm.compId,
        useYn: 'Y',
        checked: true
    });
};

/* ------------------------------------- */
const signatureComponent = ref();
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await searchDetail(param, true);
        layoutStore.useBtnList = await uButtonList;
    } else if (!jobCompAssessStore.inputForm || !jobCompAssessStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('JobCompetencyAssessment');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
    jobCompAssessStore.signature = signatureComponent.value;
    initAccordion();
    initJobManageList(false);
});

const removeHr = () => {
    jobCompAssessStore.inputForm.hrId = '';
    jobCompAssessStore.inputForm.hrNm = '';
};
</script>

<style lang="scss" scoped>
.selected-border {
    border: 1px solid blue;
}

.text-right {
    text-align: right;
}
.accordion {
    .not-accordion {
        background: transparent;
        border: none;
        > button {
            padding: 0;
        }
    }
}
</style>
