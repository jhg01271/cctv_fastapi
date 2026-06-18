<template>
    <div id="implPlanform" class="contents df fdc">
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
            <form @submit.prevent ref="myForm">
                <div class="control-field ui form box">
                    <div class="pa2-2rem">
                        <ISignature ref="signatureComponent" :cmd="sAndHTrainingImplStore.inputForm.cmd" targetType="TPI" :writeYear="sAndHTrainingImplStore.inputForm.writeYear" :docNo="sAndHTrainingImplStore.inputForm.docNo" :useYn="sAndHTrainingImplStore.inputForm.useYn"></ISignature>
                    </div>
                    <hr class="h1px solid bcE1E6ED" />
                    <div class="pa2-2rem">
                        <div class="row flex gutters1rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for required>
                                        <span>제목</span>
                                    </label>
                                    <input class="br4px" type="text" v-input v-model="sAndHTrainingImplStore.inputForm.title" placeholder="제목을 입력하세요." id="제목" required />
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>담당자</span>
                                    </label>
                                    <i-chips :chips="sAndHTrainingImplStore.inputForm.chargeList" @popup="addChargePeople('head')" @removeChip="removeHr" required></i-chips>
                                </div>
                            </div>
                            <div class="grid12-2 lg-grid12-4 es-grid12-8">
                                <div class="field">
                                    <label for>
                                        <span>등록일자</span>
                                    </label>
                                    <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(sAndHTrainingImplStore.inputForm.createdAt)" />
                                </div>
                            </div>
                            <div class="grid12-1 lg-grid12-4 es-grid12-4">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :checked="sAndHTrainingImplStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="sAndHTrainingImplStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 lg-grid12-4 es-grid12-6">
                                <div class="field">
                                    <label for=""></label>
                                    <button :class="['btn w15rem radius', { active: sAndHTrainingImplStore.inputForm.cmd === 'U' }]" @click="beforeSendAlarm" :disabled="sAndHTrainingImplStore.inputForm.cmd !== 'U'">알림 발송</button>
                                </div>
                            </div>
                        </div>

                        <div class="mt1-7rem pa2-2rem bcF9FAFF br4px">
                            <div class="pr accordion mb2rem">
                                <div class="list not-accordion">
                                    <button type="button" class="radius w15rem df jcfe aic gap8px" @click="toggleAccordion">
                                        <!--🐸 title -->
                                        <p class="fw500">관리 기능</p>
                                        <svg class="sub" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <path d="M11.6249 12.3752L11.6249 11.6252M12.3749 12.3752L12.3749 11.6252M11.6249 19.3752L11.6249 18.6252M12.3749 19.3752L12.3749 18.6252M11.6249 5.37524L11.6249 4.62524M12.3749 5.37524L12.3749 4.62524M12 11C12.5523 11 13 11.4477 13 12C13 12.5523 12.5523 13 12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11ZM12 18C12.5523 18 13 18.4477 13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19C11 18.4477 11.4477 18 12 18ZM12 4C12.5523 4 13 4.44772 13 5C13 5.55228 12.5523 6 12 6C11.4477 6 11 5.55228 11 5C11 4.44772 11.4477 4 12 4Z" stroke="black" stroke-width="2" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="pa t3rem r0 segment oh zi2">
                                        <div class="df fdc jcfe bd1pxsolid3248F6 br4px oh">
                                            <!-- 🦖 Content -->
                                            <button type="button" class="w25rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openHseTrainingMng">안전보건 연간교육 계획서 불러오기</button>
                                            <button type="button" class="w25rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openDataSetMngPopup('subjects')">과목/과정 관리</button>
                                            <button type="button" class="w25rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openDataSetMngPopup('edu')">교육기관 관리</button>
                                            <button type="button" class="w25rem bcFFFFFF c3248F6 bdb1pxsolid3248F6 pa1rem" @click="openDataSetMngPopup('inst')">강사 관리</button>
                                            <button type="button" class="w25rem bcFFFFFF c3248F6 pa1rem" @click="openDataSetMngPopup('loc')">교육장소 관리</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="box pa2-2rem pt1rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-3 lg-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for="trainingDate" required>
                                                <span>교육 일자</span>
                                            </label>
                                            <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" id="trainingDate" :min="`${sAndHTrainingImplStore.inputForm.writeYear}.01.01`" :max="`${sAndHTrainingImplStore.inputForm.writeYear}.12.31`" required v-model="sAndHTrainingImplStore.inputForm.trainingDate" />
                                        </div>
                                    </div>
                                    <div class="grid12-3 mta lg-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label for="trainingPeriod" required>
                                                <span>교육 시간 {{ calculateMinutes(sAndHTrainingImplStore.inputForm.trainingPeriod) }}(분)</span>
                                            </label>
                                            <input v-input startTime endTime type="text" v-calendar="''" id="trainingPeriod" :value="sAndHTrainingImplStore.inputForm.trainingPeriod" @input="onDateInput(index, $event)" class="datepicker w100p radius" required />
                                        </div>
                                    </div>
                                    <div class="grid12-6 lg-grid12-12">
                                        <div class="field">
                                            <label for>
                                                <span>교육 구분</span>
                                            </label>
                                            <i-chips :chips="[{ id: sAndHTrainingImplStore.inputForm.trainingType, name: sAndHTrainingImplStore.inputForm.trainingTypeNm }]" @popup="addSubject" class="w100p" @removeChip="removeSubject" />
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>
                                                <span>교육인원</span>
                                            </label>
                                            <i-chips :chips="sAndHTrainingImplStore.inputForm.hrList" @popup="addPeople" class="w100p" />
                                        </div>
                                    </div>
                                    <div class="grid12-12 es-grid12-12">
                                        <div class="field">
                                            <label for>예산비용 (원)</label>
                                            <input class="br4px" type="text" :value="formatToManWon(sAndHTrainingImplStore.inputForm.budget)" @input="updateBudget" v-input placeholder="예산비용을 입력" />
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>소요내역</label>
                                            <textarea v-model="sAndHTrainingImplStore.inputForm.requiredDetails" class="minh10rem br4px" name id placeholder="소요내역을 입력하세요." maxlength="255"></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>교육목표</label>
                                            <textarea v-model="sAndHTrainingImplStore.inputForm.trainingGoal" class="minh10rem br4px" name id placeholder="교육목표를 입력하세요." maxlength="255"></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>기대효과</label>
                                            <textarea v-model="sAndHTrainingImplStore.inputForm.expectedEffect" class="minh10rem br4px" name id placeholder="기대효과를 입력하세요." maxlength="255"></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>교육자료</label>
                                            <div class="df aic gap3-6rem el-fww">
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.materials === 'Y'" v-input="'교안'" class="checkbox" @change="toggleCheckYn('materials', $event)" />
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.projector === 'Y'" v-input="'프로젝터'" class="checkbox" @change="toggleCheckYn('projector', $event)" />
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.vtr === 'Y'" v-input="'VTR'" class="checkbox" @change="toggleCheckYn('vtr', $event)" />
                                                <input type="checkbox" :checked="isCheckboxChecked" v-input="'기타'" class="checkbox" @change="handleCheckboxChange" />
                                                <div class="fg1 h1rem df aic fs1-8rem">
                                                    (&nbsp;
                                                    <input class="br4px" type="text" v-model="sAndHTrainingImplStore.inputForm.materialsEtc" @input="handleTextInput" placeholder="입력" />&nbsp;)
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>교육내용</label>
                                            <textarea v-model="sAndHTrainingImplStore.inputForm.trainingContent" class="minh10rem br4px" name id placeholder="교육내용을 입력하세요." maxlength="1000"></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-4 md-grid12-12">
                                        <div class="field">
                                            <label for>교육기관</label>
                                            <i-chips :chips="[{ id: sAndHTrainingImplStore.inputForm.trainingInstitute, name: sAndHTrainingImplStore.inputForm.trainingInstituteNm }]" @popup="addEdu" class="w100p" @removeChip="removeEdu" />
                                        </div>
                                    </div>
                                    <div class="grid12-4 md-grid12-12">
                                        <div class="field">
                                            <label for>강사</label>
                                            <i-chips :chips="[{ id: sAndHTrainingImplStore.inputForm.trainingInstructor, name: sAndHTrainingImplStore.inputForm.trainingInstructorNm }]" @popup="addInst" class="w100p" @removeChip="removeInst" />
                                        </div>
                                    </div>
                                    <div class="grid12-4 md-grid12-12">
                                        <div class="field">
                                            <label for>교육장소</label>
                                            <i-chips :chips="[{ id: sAndHTrainingImplStore.inputForm.trainingLocation, name: sAndHTrainingImplStore.inputForm.trainingLocationNm }]" @popup="addLoc" class="w100p" @removeChip="removeLoc" />
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>교육훈련 효과성 파악 방법</label>
                                            <div class="df aic gap3-6rem el-fww">
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.qnaYn === 'Y'" v-input="'질의응답'" class="checkbox" @change="toggleCheckYn('qnaYn', $event)" />
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.reportYn === 'Y'" v-input="'보고서'" class="checkbox" @change="toggleCheckYn('reportYn', $event)" />
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.deliveryTrainingYn === 'Y'" v-input="'전달 교육'" class="checkbox" @change="toggleCheckYn('deliveryTrainingYn', $event)" />
                                                <input type="checkbox" :checked="sAndHTrainingImplStore.inputForm.testYn === 'Y'" v-input="'테스트'" class="checkbox" @change="toggleCheckYn('testYn', $event)" />
                                                <input type="checkbox" :checked="isCheckboxChecked2" v-input="'기타'" class="checkbox" @change="handleCheckboxChange2" />
                                                <div class="fg1 h1rem df aic fs1-8rem">
                                                    (&nbsp;
                                                    <input class="br4px" type="text" v-model="sAndHTrainingImplStore.inputForm.effectivenessEtc" @input="handleTextInput2" placeholder="입력" />&nbsp;)
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for>특기사항</label>
                                            <textarea v-model="sAndHTrainingImplStore.inputForm.remark" name id class="minh10rem br4px" placeholder="특기사항을 입력하세요."></textarea>
                                            <!-- maxlength="255" -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </OverlayScrollbarsComponent>

        <!-- 연간교육 계획서 불러오기 팝업 컴포넌트 -->
        <teleport to="body">
            <i-PopupDialog ref="hseTrainingPopup">
                <div class="contents w70rem md-w100p">
                    <!--                     :selectedIdList="empStakeholdersStore.inputForm.orgnIdList" -->
                    <base-select-popup :title="'안전보건 연간교육 계획서 불러오기'" uniqueKey="trainingCourse" filterKey="trainingTypeNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="sAndHTrainingImplStore.searchParam" :fetch-data="getAnnualTrainingPlan" :btnInfo="{ close: true }" @close="closeHseTrainingMng" />
                </div>
            </i-PopupDialog>

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

            <!-- 교육장소 관리 팝업 컴포넌트 -->
            <i-PopupDialog ref="locMngPopup">
                <div class="contents w70rem md-w100p">
                    <DataSetManagePopup :title="'교육장소 관리'" :subTitle="'교육장소명'" :datasetYn="'Y'" id="locationId" nm="locationNm" :popupDataList="locMngList" :cardComponent="remarkPopupComponent" @filter="filterDataSetMngPopup" @search="seacrhDataSetMngPopup" @sample="sampleDataSetMngPopup" @add="addDataSetMngPopup" @save="saveDataSetMngPopup" @delete="deleteDataSetMngPopup" @close="closeDataSetMngPopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <!-- 예시 불러오기 팝업 ---------------------------------------------------------------------------------------------------------------------------------------------------------->
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

            <i-PopupDialog ref="locMngSamplePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'교육장소 예시'" filterKey="locationNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleDataSetListFunc" @apply="applySampleDataSetMngPopup" @close="closeSamplDataSetMngPopup" />
                </div>
            </i-PopupDialog>
            <!-- ---------------------------------------------------------------------------------------------------------------------------------------------------------->
            <i-PopupDialog ref="chargePeoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="sAndHTrainingImplStore.inputForm.chargeList.map(el => el.hrId)" @selected="selectChargePeople" @close="closeChargePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>

            <!------------------------------카드 내부 팝업------------------------------>
            <i-PopupDialog ref="subjectsPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'과목/과정'" :component="BaseSelectAccordionComponent" uniqueKey="coursesId" filterKey="coursesNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getSubjectsListFunc" :btnInfo="{ close: true }" @close="applySubjectsPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="eduPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'교육기관'" :component="BaseSelectAccordionComponent" uniqueKey="centerId" filterKey="centerNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getEduListFunc" :btnInfo="{ close: true }" @close="closeEduPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="instPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'강사'" :component="BaseSelectAccordionComponent" uniqueKey="instructorId" filterKey="instructorNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getInstListFunc" :btnInfo="{ close: true }" @close="closeInstPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="locPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'교육장소'" :component="BaseSelectAccordionComponent" uniqueKey="locationId" filterKey="locationNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getLocListFunc" :btnInfo="{ close: true }" @close="closeLocPopup" />
                </div>
            </i-PopupDialog>

            <!-- 인원 검색 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="sAndHTrainingImplStore.inputForm.hrList?.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { gsap } from 'gsap';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
// BaseView
import BaseView from '@/components/base/BaseView';
const { t, openLoading, endLoading, setRouterParam, validationStore, alertMsg, calculateMinutes, formatToManWon, ref, onMounted, computed, confirmMsg, btnBack, btnSearch, btnSave, btnDelete, btnDownload, btnAdd, formatDate, onBeforeMount, getDuplicatedData, formatDateForDB } = BaseView();
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getAnnualTrainingPlan } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';
import { useRoute } from 'vue-router';
const route = useRoute();
import { sendTrainingPlanImplMsg, updateReadAlarm, getAlarm, getSelectedTokenUserInfo } from '@/stores/safewiz/alarm/api/alarmApi.js';
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore();
// Button List
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];
import ISignature from '@/components/common/iSignature.vue';
// Router
import router from '@/router';

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
// [스토어]
import { useSAndHTrainingImplStore } from '@/stores/safewiz/support/SAndHTrainingImpl.js';
const sAndHTrainingImplStore = useSAndHTrainingImplStore();

//-----------------------------------------------
/* --------- 버튼 클릭 이벤트 --------- */

btnSearch(async () => {
    sAndHTrainingImplStore.btnDetail(sAndHTrainingImplStore.inputForm, true);
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
});

btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/SAndHTrainingImplPlan');
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('implPlanform', true);
    if (isValid) {
        sAndHTrainingImplStore.signature = signatureComponent.value;
        await sAndHTrainingImplStore.btnSave();
    }
});

btnDelete(() => {
    sAndHTrainingImplStore.signature = signatureComponent.value;
    sAndHTrainingImplStore.btnDeleteDetail();
});

btnDownload(() => {
    let list = [sAndHTrainingImplStore.inputForm.docNo];
    sAndHTrainingImplStore.btnDownload(list, true);
});

btnAdd(() => {
    sAndHTrainingImplStore.initInputForm();
    layoutStore.useBtnList = iButtonList;
});

//-----------------------------------------------
//시간 체크
const onDateInput = (index, event) => {
    sAndHTrainingImplStore.inputForm.trainingPeriod = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};
//-----------------------------------------------
//useYn 체크

//-----------------------------------------------
//-----------------------------------------------

const updateBudget = event => {
    // 입력된 값에서 숫자 이외의 문자를 제거
    const numericValue = parseFloat(event.target.value.replace(/[^0-9]/g, '')) || 0;

    // 실제 숫자 값을 budget에 저장
    sAndHTrainingImplStore.inputForm.budget = numericValue;

    // 천 단위로 포맷된 값 업데이트
    // hseBudgetStore.inputForm.budget = formatToAmt(numericValue);
};

//-----------------------------------------------
//-----------------------------------------------
// 체크박스가 체크될지 여부를 computed로 정의
const isCheckboxChecked = computed(() => {
    const value = sAndHTrainingImplStore.inputForm.materialsEtc;
    return value !== null && value !== '' && value !== undefined;
});

const isCheckboxChecked2 = computed(() => {
    const value = sAndHTrainingImplStore.inputForm.effectivenessEtc;
    return value !== null && value !== '' && value !== undefined;
});

// 체크박스 상태 변경 처리
const handleCheckboxChange = event => {
    if (!event.target.checked) {
        sAndHTrainingImplStore.inputForm.materialsEtc = '';
    }
};

// 체크박스 상태 변경 처리
const handleCheckboxChange2 = event => {
    if (!event.target.checked) {
        sAndHTrainingImplStore.inputForm.effectivenessEtc = '';
    }
};

// 텍스트 입력 시 체크박스 상태 동기화
const handleTextInput = event => {
    if (event.target.value.trim() === '') {
        sAndHTrainingImplStore.inputForm.materialsEtc = '';
    }
};

// 텍스트 입력 시 체크박스 상태 동기화
const handleTextInput2 = event => {
    if (event.target.value.trim() === '') {
        sAndHTrainingImplStore.inputForm.effectivenessEtc = '';
    }
};

//-----------------------------------------------
//-----------------------------------------------
// [토글버튼]
const toggleCheckYn = (item, event) => {
    const isChecked = event.target.checked;

    sAndHTrainingImplStore.inputForm[item] = isChecked ? 'Y' : 'N';
};

/* ------------------------------------- */
const signatureComponent = ref();
onMounted(async () => {
    if (route.query.fromPush === 'true') {
        route.query.fcmPayload = JSON.parse(route.query.fcmPayload);
        await sAndHTrainingImplStore.btnDetail(route.query.fcmPayload, true);
        layoutStore.useBtnList = uButtonList;
        await setTimeout(() => {
            const params = {
                clntId: route.query.fcmPayload.clntId,
                alarmSeq: route.query.fcmPayload.alarmSeq,
                alarmDt: formatDateForDB(route.query.fcmPayload.alarmDt),
                menuId: route.query.fcmPayload.menuId
            };
            updateReadAlarm(params, true).then(res => {
                if (res) {
                    getAlarm().then(res => {
                        if (res && res[0]) {
                            fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
                        } else {
                            fcmStore.alarmCount = 0;
                        }
                    });
                    router.replace({
                        query: {
                            fcmPayload: null,
                            fromPush: null
                        }
                    });
                }
            });
        }, 100);
    } else {
        const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
        if (param) {
            // 상세보기 or 라우터 이동인 경우 state를 전달받음
            await sAndHTrainingImplStore.btnDetail(param, true);
            layoutStore.useBtnList = uButtonList;
        } else if (!sAndHTrainingImplStore.inputForm.cmd) {
            // 새로고침시 이전 화면으로 이동.
            router.push('SAndHTrainingImplPlan');
            return;
        } else {
            // 추가버튼으로 왔을 때
            layoutStore.useBtnList = iButtonList;
        }
    }
    setTimeout(() => {
        signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }, 100);
    sAndHTrainingImplStore.signature = signatureComponent.value;
});
//-----------------------------------------------
// [관리 공통 팝업]
const hseTrainingPopup = ref(null);

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

const openHseTrainingMng = () => openPopup(hseTrainingPopup);

const closeHseTrainingMng = e => {
    if (hseTrainingPopup.value) {
        hseTrainingPopup.value.onClose();
    }
    if (e?.[0]) {
        const data = {
            ...e[0],
            trainingType: e[0].trainingCourse,
            cmd: 'I',
            docType: 'TPI'
        };

        data.hrList.forEach(el => {
            el.id = el.hrId;
            el.nm = el.hrNm;
        });
        data.chargeList = [];
        sAndHTrainingImplStore.inputForm = data;
    }
};
//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref

const addPeople = () => {
    // peopleIdx.value = idx;
    peoplePopup.value.onOpen();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const selectPeople = list => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();

        const users = list;
        sAndHTrainingImplStore.inputForm.hrList = [];
        users.forEach(() => {
            sAndHTrainingImplStore.inputForm.hrList = users.map(user => ({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                targetType: 'TPI'
            }));
        });
    }
};

//-----------------------------------------------

const selectChargePeople = list => {
    if (chargePeoplePopup.value) {
        chargePeoplePopup.value.onClose();
        const users = list;
        sAndHTrainingImplStore.inputForm.chargeList = [];
        users.forEach(user => {
            sAndHTrainingImplStore.inputForm.chargeList.push({
                id: user.hrId,
                nm: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm
            });
        });
    }
};

const chargePeoplePopup = ref(null); // PopupDialog에 대한 ref

const closeChargePeoplePopup = () => {
    if (chargePeoplePopup.value) {
        chargePeoplePopup.value.onClose();
    }
};

const gubun = ref();

const addChargePeople = el => {
    gubun.value = el;
    selectUserStore.currentTab = 'orgn';
    chargePeoplePopup.value.onOpen();
};

onBeforeMount(async () => {
    if (sAndHTrainingImplStore.inputForm.chargeList === null) {
        sAndHTrainingImplStore.inputForm.chargeList = [
            {
                id: '',
                nm: ''
            }
        ];
    }
});

import remarkPopupComponent from '@/views/system/base/popup/popupComponent/RemarkDataSetManageComponent.vue';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { getSubjectsMngList, getDataSetSubjectsMngList, saveSubjectsMngList, deleteSubjectsMngList, getEduMngList, getDataSetEduMngList, saveEduMngList, deleteEduMngList, getInstMngList, getDataSetInstMngList, saveInstMngList, deleteInstMngList } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';
import { getLocMngList, getDataSetLocMngList, saveLocMngList, deleteLocMngList } from '@/stores/safewiz/support/api/SAndHTrainingImplApi.js';
import _ from 'lodash';
//######################################################################################################################
//과목/과정 관리, 교육기관 관리, 강사 관리, 교육장소 관리 예시 불러오기 팝업-------------------------------------------------
//######################################################################################################################
const dataSetMngType = ref(null);
//과목/과정 관리============================================================================================================================================
const subjectsMngPopup = ref(null);
const subjectsMngSamplePopup = ref(null);
const subjectsMngList = ref([]);
const currentSubjectsMngList = ref([]);
//교육기관 관리============================================================================================================================================
const eduMngPopup = ref(null);
const eduMngSamplePopup = ref(null);
const eduMngList = ref([]);
const currentEduMngList = ref([]);
//강사 관리============================================================================================================================================
const instMngPopup = ref(null);
const instMngSamplePopup = ref(null);
const instMngList = ref([]);
const currentInstMngList = ref([]);
//교육장소 관리============================================================================================================================================
const locMngPopup = ref(null);
const locMngSamplePopup = ref(null);
const locMngList = ref([]);
const currentLocMngList = ref([]);

const openDataSetMngPopup = async type => {
    dataSetMngType.value = type;
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리
        await initDataSetMngPopup(true);
        subjectsMngPopup.value.onOpen();
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리
        await initDataSetMngPopup(true);
        eduMngPopup.value.onOpen();
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리
        await initDataSetMngPopup(true);
        instMngPopup.value.onOpen();
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리
        await initDataSetMngPopup(true);
        locMngPopup.value.onOpen();
    }
};

const initDataSetMngPopup = async () => {
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리
        getSubjectsMngList({}, true).then(res => {
            subjectsMngList.value = res.list;
            currentSubjectsMngList.value = _.cloneDeep(res.list);
        });
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리
        getEduMngList({}, true).then(res => {
            eduMngList.value = res.list;
            currentEduMngList.value = _.cloneDeep(res.list);
        });
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리
        getInstMngList({}, true).then(res => {
            instMngList.value = res.list;
            currentInstMngList.value = _.cloneDeep(res.list);
        });
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리
        getLocMngList({}, true).then(res => {
            locMngList.value = res.list;
            currentLocMngList.value = _.cloneDeep(res.list);
        });
    }
};

const seacrhDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리============================================================================================================================================
        if (JSON.stringify(currentSubjectsMngList.value) !== JSON.stringify(subjectsMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        if (JSON.stringify(currentEduMngList.value) !== JSON.stringify(eduMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        if (JSON.stringify(currentInstMngList.value) !== JSON.stringify(instMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        if (JSON.stringify(currentLocMngList.value) !== JSON.stringify(locMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: initDataSetMngPopup, param: {} });
        } else {
            initDataSetMngPopup();
        }
    }
};

const filterDataSetMngPopup = async text => {
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리============================================================================================================================================
        subjectsMngList.value = currentSubjectsMngList.value;
        const filteredData = subjectsMngList.value.filter(el => el.coursesNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        subjectsMngList.value = filteredData;
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        eduMngList.value = currentEduMngList.value;
        const filteredData = eduMngList.value.filter(el => el.centerNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        eduMngList.value = filteredData;
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        instMngList.value = currentInstMngList.value;
        const filteredData = instMngList.value.filter(el => el.instructorNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        instMngList.value = filteredData;
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        locMngList.value = currentLocMngList.value;
        const filteredData = locMngList.value.filter(el => el.locationNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        locMngList.value = filteredData;
    }
};

const addDataSetMngPopup = async () => {
    let index = null;
    let element = '';
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리============================================================================================================================================
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
        //교육기관 관리============================================================================================================================================
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
        //강사 관리============================================================================================================================================
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
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        const newData = {
            locationId: '',
            locationNm: '',
            remark: '',
            ordSeq: '',
            useYn: 'Y',
            cmd: 'I',
            checked: true
        };
        locMngList.value.push(newData);
        index = locMngList.value.length - 1;
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
        //과목/과정 관리============================================================================================================================================
        const checkedList = subjectsMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        const checkedList = eduMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        const checkedList = instMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        const checkedList = locMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        const isValid = await validationStore.validateAllFields('form', true);
        if (isValid) {
            confirmMsg('info', t('msgSave'), '', { fun: saveActionDataSetMngPopup, param: checkedList });
        }
    }
};

const saveActionDataSetMngPopup = checkedList => {
    //과목/과정 관리============================================================================================================================================
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
        //교육기관 관리============================================================================================================================================
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
        //강사 관리============================================================================================================================================
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
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        saveLocMngList(checkedList, true)
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
        //과목/과정 관리============================================================================================================================================
        const checkedList = subjectsMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        const checkedList = eduMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        const checkedList = instMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        const checkedList = locMngList.value.filter(val => val.checked == true);
        if (checkedList.length == 0) {
            alertMsg('error', '선택된 항목이 없습니다.');
            return;
        }
        const isValid = await validationStore.validateAllFields('form', true);
        if (isValid) {
            confirmMsg('info', t('msgDelete'), '', { fun: deleteActionDataSetMngPopup, param: checkedList });
        }
    }
};

const deleteActionDataSetMngPopup = checkedList => {
    openLoading();
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리============================================================================================================================================
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
        //교육기관 관리============================================================================================================================================
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
        //강사 관리============================================================================================================================================
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
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        openLoading();
        deleteLocMngList(checkedList, true)
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
        //과목/과정 관리============================================================================================================================================
        if (JSON.stringify(currentSubjectsMngList.value) !== JSON.stringify(subjectsMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            subjectsMngPopup.value.onClose();
        }
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        if (JSON.stringify(currentEduMngList.value) !== JSON.stringify(eduMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            eduMngPopup.value.onClose();
        }
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        if (JSON.stringify(currentInstMngList.value) !== JSON.stringify(instMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            instMngPopup.value.onClose();
        }
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        if (JSON.stringify(currentLocMngList.value) !== JSON.stringify(locMngList.value)) {
            confirmMsg('info', '저장하지 않은 정보가 있습니다.\n 그래도 계속 하시겠습니까?', '', { fun: closeActionDataSetMngPopup, param: {} });
        } else {
            locMngPopup.value.onClose();
        }
    }
};

const closeActionDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리
        subjectsMngPopup.value.onClose();
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리
        eduMngPopup.value.onClose();
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리
        instMngPopup.value.onClose();
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리
        locMngPopup.value.onClose();
    }
};

//######################################################################################################################
//과목/과정 관리, 교육기관 관리, 강사 관리, 교육장소 관리 예시 불러오기 팝업-------------------------------------------------
//######################################################################################################################

const sampleDataSetMngPopup = () => {
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리
        subjectsMngSamplePopup.value.onOpen();
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리
        eduMngSamplePopup.value.onOpen();
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리
        instMngSamplePopup.value.onOpen();
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리
        locMngSamplePopup.value.onOpen();
    }
};

const getSampleDataSetListFunc = async () => {
    openLoading();
    let resData = { list: [] };
    if (dataSetMngType.value === 'subjects') {
        //과목/과정 관리============================================================================================================================================
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
        //교육기관 관리============================================================================================================================================
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
        //강사 관리============================================================================================================================================
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
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        await getDataSetLocMngList({}, true)
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
        //과목/과정 관리============================================================================================================================================
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
        //교육기관 관리============================================================================================================================================
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
        //강사 관리============================================================================================================================================
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
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        const filteredData = getDuplicatedData(locMngList.value, selectItems, 'locationId', 'locationNm'); // 중복된 항목 추출
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
        //과목/과정 관리============================================================================================================================================
        await confirmList.forEach(val => {
            val.cmd = 'I';
            subjectsMngList.value.push(val);
        });
        subjectsMngSamplePopup.value.onClose();

        index = subjectsMngList.value.length - 1;
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리============================================================================================================================================
        await confirmList.forEach(val => {
            val.cmd = 'I';
            eduMngList.value.push(val);
        });
        eduMngSamplePopup.value.onClose();

        index = eduMngList.value.length - 1;
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리============================================================================================================================================
        await confirmList.forEach(val => {
            val.cmd = 'I';
            instMngList.value.push(val);
        });
        instMngSamplePopup.value.onClose();

        index = instMngList.value.length - 1;
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리============================================================================================================================================
        await confirmList.forEach(val => {
            val.cmd = 'I';
            locMngList.value.push(val);
        });
        locMngSamplePopup.value.onClose();

        index = locMngList.value.length - 1;
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
        //과목/과정 관리
        subjectsMngSamplePopup.value.onClose();
    } else if (dataSetMngType.value === 'edu') {
        //교육기관 관리
        eduMngSamplePopup.value.onClose();
    } else if (dataSetMngType.value === 'inst') {
        //강사 관리
        instMngSamplePopup.value.onClose();
    } else if (dataSetMngType.value == 'loc') {
        //교육장소 관리
        locMngSamplePopup.value.onClose();
    }
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################
//card 과목/과정 팝업*******************************************************************************************************************************

const subjectsPopup = ref(null);

const addSubject = () => {
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
        sAndHTrainingImplStore.inputForm.trainingType = row[0].coursesId;
        sAndHTrainingImplStore.inputForm.trainingTypeNm = row[0].coursesNm;
    }
};

const removeSubject = () => {
    sAndHTrainingImplStore.inputForm.trainingType = '';
    sAndHTrainingImplStore.inputForm.trainingTypeNm = '';
};

//card 교육기관 팝업*******************************************************************************************************************************

const eduPopup = ref(null); // PopupDialog에 대한 ref

const addEdu = () => {
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
        sAndHTrainingImplStore.inputForm.trainingInstitute = row[0].centerId;
        sAndHTrainingImplStore.inputForm.trainingInstituteNm = row[0].centerNm;
    }
};

const removeEdu = () => {
    sAndHTrainingImplStore.inputForm.trainingInstitute = '';
    sAndHTrainingImplStore.inputForm.trainingInstituteNm = '';
};

//card 강사 팝업*******************************************************************************************************************************

const instPopup = ref(null); // PopupDialog에 대한 ref

const addInst = () => {
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
        sAndHTrainingImplStore.inputForm.trainingInstructor = row[0].instructorId;
        sAndHTrainingImplStore.inputForm.trainingInstructorNm = row[0].instructorNm;
    }
};

const removeInst = () => {
    sAndHTrainingImplStore.inputForm.trainingInstructor = '';
    sAndHTrainingImplStore.inputForm.trainingInstructorNm = '';
};
//card 교육장소 팝업*******************************************************************************************************************************

const locPopup = ref(null); // PopupDialog에 대한 ref

const addLoc = () => {
    locPopup.value.onOpen();
};

const getLocListFunc = async () => {
    let data;
    await getLocMngList({}, true).then(res => {
        res.list.forEach(val => {
            val.desc = val.remark;
        });
        data = res;
    });
    return data;
};

// 팝업 닫기
const closeLocPopup = row => {
    if (locPopup.value) {
        locPopup.value.onClose();
    }

    if (row && row[0]) {
        sAndHTrainingImplStore.inputForm.trainingLocation = row[0].locationId;
        sAndHTrainingImplStore.inputForm.trainingLocationNm = row[0].locationNm;
    }
};

const removeLoc = () => {
    sAndHTrainingImplStore.inputForm.trainingLocation = '';
    sAndHTrainingImplStore.inputForm.trainingLocationNm = '';
};

//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();
const beforeSendAlarm = () => {
    if (JSON.stringify(sAndHTrainingImplStore.inputForm.hrList) !== JSON.stringify(sAndHTrainingImplStore.currentInputForm.hrList)) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: () => sendAlarm() });
    } else {
        sendAlarm();
    }
};

const sendAlarm = () => {
    const hrList = sAndHTrainingImplStore.currentInputForm.hrList.map(item => item.userId);
    if (hrList.length === 0) {
        //등록된 교육 인원이 없을 경우
        alertMsg('error', t('msgSendAlarmNoHr'));
        return;
    }
    getSelectedTokenUserInfo(hrList, false).then(res => {
        if (res.success) {
            if (sAndHTrainingImplStore.currentInputForm.hrList.length === res.result.length) {
                //교육인원이 모두 토큰을 소지하고 있을 경우
                confirmMsg('info', t('msgSendAlarm'), '', { fun: sendTrainingMessage, param: '' });
            } else if (sAndHTrainingImplStore.currentInputForm.hrList.length > res.result.length && res.result.length !== 0) {
                //일부 교육인원이 토큰을 소지하고 있지 않은 경우
                let text = '';
                const UserIds = res.result.map(item => item.userId);
                const filteredData = sAndHTrainingImplStore.currentInputForm.hrList.filter(item => !UserIds.includes(item.userId));
                filteredData.forEach(val => {
                    if (text === '') {
                        text = '- ' + val.hrNm + '(' + (val.orgnNm != null ? val.orgnNm : '') + ', ' + (val.jbrpNm != null ? val.jbrpNm : '') + ')\n';
                    } else {
                        text += '- ' + val.hrNm + '(' + (val.orgnNm != null ? val.orgnNm : '') + ', ' + (val.jbrpNm != null ? val.jbrpNm : '') + ')\n';
                    }
                });
                confirmMsg('info', t('msgSendAlarmCantHr') + '\n' + t('msgContinue'), t('msgSendAlarmNeedSetting') + '\n' + text, { fun: () => sendTrainingMessage() });
            } else if (res.result.length === 0) {
                //모든 교육인원이 토큰을 소지하고 있지 않은 경우
                alertMsg('error', t('msgSendAlarmNoHr') + '\n' + t('msgSendAlarmNeedSetting'));
                return;
            }
        }
    });
};

const sendTrainingMessage = () => {
    openLoading();
    try {
        sendTrainingPlanImplMsg(sAndHTrainingImplStore.currentInputForm, sAndHTrainingImplStore.inputForm.title, router.currentRoute.value.meta.menuId, true).then(res => {
            if (res) {
                getAlarm().then(res => {
                    if (res && res[0]) {
                        console.log('알람 레스 확인', res);
                        fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
                    } else {
                        fcmStore.alarmCount = 0;
                    }
                });
                toastPopupStore.addToast(t('msgSendSuccess'), t('msgSendCompleted'), 'success');
                sAndHTrainingImplStore.inputForm.hrList = _.cloneDeep(sAndHTrainingImplStore.currentInputForm.hrList);
            }
        });
    } catch (err) {
        console.error('알림 발송 중 오류가 발생했습니다.', err);
    } finally {
        endLoading();
    }
};
</script>
<style scoped lang="scss">
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
