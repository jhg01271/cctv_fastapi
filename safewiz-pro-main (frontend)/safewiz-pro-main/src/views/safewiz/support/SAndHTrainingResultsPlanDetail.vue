<template>
    <div id="form" class="contents df fdc">
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
                <div @submit.prevent ref="myForm">
                    <div class="control-field ui form box">
                        <div class="pa2-2rem" :key="sAndHTrainingResultStore.inputForm.cmd">
                            <ISignature ref="signatureComponent" :cmd="sAndHTrainingResultStore.inputForm.cmd" targetType="TRR" :writeYear="sAndHTrainingResultStore.searchParam.writeYear" :docNo="sAndHTrainingResultStore.searchParam.docNo" :useYn="sAndHTrainingResultStore.inputForm.useYn"></ISignature>
                        </div>
                        <hr class="h1px" />
                        <div class="pa2-2rem">
                            <div class="row flex gutters1rem"></div>
                            <div class="row flex gutters1rem">
                                <div class="grid12-4 lg-grid12-12">
                                    <div class="field">
                                        <label required>
                                            <span>안전보건 교육실시 계획서</span>
                                        </label>
                                        <i-chips :chips="[{ id: sAndHTrainingResultStore.inputForm.planWriteYear + sAndHTrainingResultStore.inputForm.planDocNo, name: sAndHTrainingResultStore.inputForm.planTitle }]" @popup="openHseTrainingMng" class="w100p" @removeChip="removeHseTrainingMng" required />
                                    </div>
                                </div>
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for required>
                                            <span>제목</span>
                                        </label>
                                        <input :readonly="isReadonly" type="text" v-input class="radius" v-model="sAndHTrainingResultStore.inputForm.title" id="제목" required placeholder="제목을 입력하세요." />
                                    </div>
                                </div>
                                <div class="grid12-2 es-grid12-8">
                                    <div class="field">
                                        <label for>
                                            <span>등록일자</span>
                                        </label>
                                        <input :readonly="isReadonly" type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(sAndHTrainingResultStore.inputForm.createdAt)" />
                                    </div>
                                </div>
                                <div class="grid12-2 es-grid12-4">
                                    <div class="field">
                                        <label for="useYn">사용여부</label>
                                        <div class="df aic h4-4rem">
                                            <input :readonly="isReadonly" :checked="sAndHTrainingResultStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="sAndHTrainingResultStore.toggleUseYn" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row flex gutters1rem">
                                <div class="grid12-2 lg-grid12-6 es-grid12-12">
                                    <div class="field">
                                        <label for="trainingDate" required>
                                            <span>교육 일자</span>
                                        </label>
                                        <input :readonly="isReadonly" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" id="trainingDate" :min="`${sAndHTrainingResultStore.searchParam.writeYear}.01.01`" :max="`${sAndHTrainingResultStore.searchParam.writeYear}.12.31`" required v-model="sAndHTrainingResultStore.inputForm.trainingDate" />
                                    </div>
                                </div>
                                <div class="grid12-2 mta lg-grid12-6 es-grid12-12">
                                    <div class="field">
                                        <label for="trainingPeriod" required>
                                            <span>교육 시간 {{ calculateMinutes(sAndHTrainingResultStore.inputForm.trainingPeriod) }}(분)</span>
                                        </label>
                                        <input :readonly="isReadonly" v-input startTime endTime type="text" v-calendar="''" :value="sAndHTrainingResultStore.inputForm.trainingPeriod" @input="onDateInput(index, $event)" class="datepicker w100p radius" id="trainingPeriod" required />
                                    </div>
                                </div>

                                <div class="grid12-4 lg-grid12-12">
                                    <div class="field">
                                        <label for>
                                            <span>교육 구분</span>
                                        </label>
                                        <i-chips :readonly="isReadonly" :chips="[{ id: sAndHTrainingResultStore.inputForm.trainingType, name: sAndHTrainingResultStore.inputForm.trainingTypeNm }]" @popup="addSubject" class="w100p" @removeChip="removeSubject" />
                                    </div>
                                </div>
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label required> <span>담당자</span></label>
                                        <i-chips :readonly="isReadonly" :chips="sAndHTrainingResultStore.inputForm.chargeList" @popup="addChargePeople('head')" required></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-12">
                                    <div class="field">
                                        <div class="accordion br4px">
                                            <div class="list">
                                                <button class="df jcsb aic" @click="toggleAttendeeAccordion" ref="accordionRef">
                                                    <em class="w100p wbka ellipsis">{{ t('교육인원') }}</em>
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

                                                        <i-hr-chips-sign type="attendee" ref="inspector" :cmd="sAndHTrainingResultStore.inputForm.cmd" targetType="TRR" :writeYear="sAndHTrainingResultStore.searchParam.writeYear" :docNo="sAndHTrainingResultStore.searchParam.docNo" :docSeq="'1'" :useYn="sAndHTrainingResultStore.inputForm.useYn" />

                                                        <button v-if="!isReadonly" class="w100p mt1rem py8px" type="button" @click="peoplePopupOpen">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- <div class="field">
                                        <label for>
                                            <span>교육인원</span>
                                        </label>
                                        <div class="sign-chips" :key="sAndHTrainingResultStore.inputForm.cmd">
                                            <div class="maxw100p minh6-8rem pr6-8rem dif pr aife">
                                                <i-hr-chips-sign :readonly="isReadonly" type="attendee" ref="inspector" :cmd="sAndHTrainingResultStore.inputForm.cmd" targetType="TRR" :writeYear="sAndHTrainingResultStore.searchParam.writeYear" :docNo="sAndHTrainingResultStore.searchParam.docNo" :docSeq="1" />
                                                <button :disabled="isReadonly" type="button" @click="peoplePopupOpen" class="pa r0 t0 minw6-8rem minh6-8rem df aic jcc bcF9FAFF br4px shrink0">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                                                        <path d="M6.2832 13.3867V7.70312H0.628906V6.29688H6.2832V0.613281H7.68945V6.29688H13.373V7.70312H7.68945V13.3867H6.2832Z" fill="#3248F6" />
                                                    </svg>
                                                </button>
                                            </div>
                                        </div>
                                    </div> -->
                                </div>
                                <div class="grid12-12 sm-grid12-12">
                                    <div class="field">
                                        <label for>예산비용 (원)</label>
                                        <input :readonly="isReadonly" type="text" class="radius" :value="formatToManWon(sAndHTrainingResultStore.inputForm.budget)" @input="updateBudget" v-input placeholder="예산비용을 입력" />
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>소요내역</label>
                                        <textarea :readonly="isReadonly" v-model="sAndHTrainingResultStore.inputForm.requiredDetails" class="minh10rem br4px" name id placeholder="소요내역을 입력하세요." maxlength="255"></textarea>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>교육목표</label>
                                        <textarea :readonly="isReadonly" v-model="sAndHTrainingResultStore.inputForm.trainingGoal" class="minh10rem br4px" name id placeholder="교육목표를 입력하세요." maxlength="255"></textarea>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>기대효과</label>
                                        <textarea :readonly="isReadonly" v-model="sAndHTrainingResultStore.inputForm.expectedEffect" class="minh10rem br4px" name id placeholder="기대효과를 입력하세요." maxlength="255"></textarea>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>교육자료</label>
                                        <div class="df aic gap3-6rem el-fww">
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.materials === 'Y'" v-input="'교안'" class="checkbox" @change="toggleCheckYn('materials', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.projector === 'Y'" v-input="'프로젝터'" class="checkbox" @change="toggleCheckYn('projector', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.vtr === 'Y'" v-input="'VTR'" class="checkbox" @change="toggleCheckYn('vtr', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="isCheckboxChecked" v-input="'기타'" class="checkbox" @change="handleCheckboxChange" />
                                            <div class="fg1 h1rem df aic fs1-8rem">
                                                (&nbsp;
                                                <input :readonly="isReadonly" class="br4px" type="text" v-model="sAndHTrainingResultStore.inputForm.materialsEtc" @input="handleTextInput" />&nbsp;)
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>교육내용</label>
                                        <textarea :readonly="isReadonly" v-model="sAndHTrainingResultStore.inputForm.trainingContent" class="minh10rem br4px" name id placeholder="교육내용을 입력하세요." maxlength="1000"></textarea>
                                    </div>
                                </div>
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for>교육기관</label>
                                        <i-chips :readonly="isReadonly" :chips="[{ id: sAndHTrainingResultStore.inputForm.trainingInstitute, name: sAndHTrainingResultStore.inputForm.trainingInstituteNm }]" @popup="addEdu" class="w100p" @removeChip="removeEdu" />
                                    </div>
                                </div>
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for>강사</label>
                                        <i-chips :readonly="isReadonly" :chips="[{ id: sAndHTrainingResultStore.inputForm.trainingInstructor, name: sAndHTrainingResultStore.inputForm.trainingInstructorNm }]" @popup="addInst" class="w100p" @removeChip="removeInst" />
                                    </div>
                                </div>
                                <div class="grid12-4 es-grid12-12">
                                    <div class="field">
                                        <label for>교육장소</label>
                                        <i-chips :readonly="isReadonly" :chips="[{ id: sAndHTrainingResultStore.inputForm.trainingLocation, name: sAndHTrainingResultStore.inputForm.trainingLocationNm }]" @popup="addLoc" class="w100p br4px" @removeChip="removeLoc" />
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>교육훈련 효과성 파악 방법</label>
                                        <div class="df gap2rem aic lg-fww">
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.qnaYn === 'Y'" v-input="'질의응답'" class="checkbox" @change="toggleCheckYn('qnaYn', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.reportYn === 'Y'" v-input="'보고서'" class="checkbox" @change="toggleCheckYn('reportYn', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.deliveryTrainingYn === 'Y'" v-input="'전달 교육'" class="checkbox" @change="toggleCheckYn('deliveryTrainingYn', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="sAndHTrainingResultStore.inputForm.testYn === 'Y'" v-input="'테스트'" class="checkbox" @change="toggleCheckYn('testYn', $event)" />
                                            <input :disabled="isReadonly" type="checkbox" :checked="isCheckboxChecked2" v-input="'기타'" class="checkbox" @change="handleCheckboxChange2" />
                                            <div class="fg1 h1rem df aic fs1-8rem">
                                                (&nbsp;
                                                <input :readonly="isReadonly" type="text" class="br4px" v-model="sAndHTrainingResultStore.inputForm.effectivenessEtc" @input="handleTextInput2" />&nbsp;)
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>교육훈련 효과성 파악 결과</label>
                                        <div class="df gap2rem aic sm-fww">
                                            <input :disabled="isReadonly" type="radio" v-input="'양호'" :checked="sAndHTrainingResultStore.inputForm.goodCondition === 'Y'" @change="updateCondition('goodCondition')" value="N" />
                                            <input :disabled="isReadonly" type="radio" v-input="'보통'" :checked="sAndHTrainingResultStore.inputForm.normalCondition === 'Y'" @change="updateCondition('normalCondition')" value="N" />
                                            <input :disabled="isReadonly" type="radio" v-input="'효과없음'" :checked="sAndHTrainingResultStore.inputForm.noConcern === 'Y'" @change="updateCondition('noConcern')" value="N" />
                                        </div>
                                    </div>
                                </div>
                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>특기사항</label>
                                        <textarea :readonly="isReadonly" v-model="sAndHTrainingResultStore.inputForm.remark" name id class="minh10rem radius" placeholder="특기사항을 입력하세요." maxlength="255"></textarea>
                                    </div>
                                </div>
                                <div class="grid12-12 sm-grid12-12">
                                    <div class="field">
                                        <label for="asmntResult">
                                            {{ t('교육 실시 결과') }}
                                            <span class="c9EA1A6 fw400">{{ t('(교육 사진 또는 교육 자료 등)') }}</span>
                                        </label>
                                        <!-- 첨부파일 -->
                                        <div class="bd1pxsolidE1E6ED pa2-2rem br4px">
                                            <IFileList ref="fileList" targetType="TRR" :targetId="sAndHTrainingResultStore.inputForm.files" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <teleport to="body">
            <!-- 연간교육 계획서 불러오기 팝업 컴포넌트 -->
            <i-PopupDialog ref="hseTrainingPopup">
                <div class="contents w70rem md-w100p">
                    <!--                     :selectedIdList="empStakeholdersStore.inputForm.orgnIdList" -->
                    <base-select-popup :title="'안전보건 교육실시 계획서 불러오기'" uniqueKey="title" filterKey="title" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ writeYear: sAndHTrainingResultStore.searchParam.writeYear, validOnly: true }" :fetch-data="getTrainingPlanImpl" @close="closeHseTrainingMng" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="chargePeoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="sAndHTrainingResultStore.inputForm.chargeList?.map(el => el.id)" @selected="selectChargePeople" @close="closeChargePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>

            <!------------------------------카드 내부 팝업------------------------------>
            <i-PopupDialog ref="subjectsPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'과목/과정 관리'" :component="BaseSelectAccordionComponent" uniqueKey="coursesId" filterKey="coursesNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getSubjectsListFunc" :btnInfo="{ close: true }" @close="applySubjectsPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="eduPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'교육기관 관리'" :component="BaseSelectAccordionComponent" uniqueKey="centerId" filterKey="centerNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getEduListFunc" :btnInfo="{ close: true }" @close="closeEduPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="instPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'강사 관리'" :component="BaseSelectAccordionComponent" uniqueKey="instructorId" filterKey="instructorNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getInstListFunc" :btnInfo="{ close: true }" @close="closeInstPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="locPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'교육장소 관리'" :component="BaseSelectAccordionComponent" uniqueKey="locationId" filterKey="locationNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getLocListFunc" :btnInfo="{ close: true }" @close="closeLocPopup" />
                </div>
            </i-PopupDialog>

            <!-- 인원 검색 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="inspector?.getSelectedHrId" @selected="selectPeople" :valid-orgn-id-list="sAndHTrainingResultStore.inputForm.planHrList?.map(hr => hr.orgnId)" :valid-hr-id-list="sAndHTrainingResultStore.inputForm.planHrList?.map(hr => hr.hrId)" @close="closePeoplePopup"></selectUser>
                    <!--                    <selectUser :single="false" @selected="selectPeople" @close="closePeoplePopup"></selectUser>-->
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { gsap } from 'gsap';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
// BaseView
import BaseView from '@/components/base/BaseView';
// Button List
import { useButtonListStore } from '@/stores/buttonList';
import { getTrainingPlanImpl } from '@/stores/safewiz/support/api/SAndHTrainingImplApi.js';
import ISignature from '@/components/common/iSignature.vue';
// Router
import router from '@/router';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { nextTick } from 'vue';
import IFileList from '@/components/file/iFileList.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();
import _ from 'lodash';
const { setRouterParam, validationStore, t, calculateMinutes, formatToManWon, ref, onMounted, computed, confirmMsg, btnBack, btnSearch, btnSave, btnDelete, btnDownload, btnAdd, formatDate, onBeforeMount, formatDateForDB } = BaseView();
const layoutStore = useButtonListStore();
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];
const activeSegments = ref({});
const hseTrainingPopup = ref(null);
const toggleAccordion = index => {
    activeSegments.value[index] = !activeSegments.value[index];
    const segmentId = `accordion${index}`;
    const segment = document.getElementById(segmentId);

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    }
};
const fileList = ref(null);

//-----------------------------------------------
// [스토어]
import { useSAndHTrainingResultStore } from '@/stores/safewiz/support/SAndHTrainingResult.js';
const sAndHTrainingResultStore = useSAndHTrainingResultStore();

//-----------------------------------------------
/* --------- 버튼 클릭 이벤트 --------- */

btnSearch(async () => {
    signSearchTerm.value = '';
    searchAction(true);
});
const searchAction = async notify => {
    sAndHTrainingResultStore.searchParam.writeYear = sAndHTrainingResultStore.inputForm.writeYear;
    sAndHTrainingResultStore.searchParam.docNo = sAndHTrainingResultStore.inputForm.docNo;
    const res = await sAndHTrainingResultStore.getTrainingResultRptDetail(notify);
    if (res) {
        const isOpen = await accordionRef.value.classList.toggle('active');
        await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
        if (isOpen === true) {
            animateAccordion(accordionRef.value.nextElementSibling, isOpen);
        } else {
            accordionRef.value.classList.toggle('active');
        }
        await fileList.value.fnSearch();
    }
};
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/SAndHTrainingResultsPlan');
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), null, { fun: saveAction, param: null });
    }
});
const saveAction = async () => {
    sAndHTrainingResultStore.inputForm.fileList = fileList;
    let saveParam = _.cloneDeep(sAndHTrainingResultStore.inputForm);
    if (saveParam.trainingDate == sAndHTrainingResultStore.inputForm.trainingDate) {
        saveParam.trainingDate = formatDateForDB(sAndHTrainingResultStore.inputForm.trainingDate);
    }
    // trainingPeriod 값을 '~'을 기준으로 분리하여 시작 시간과 종료 시간 추출
    if (saveParam.trainingPeriod) {
        const [trainingStart, trainingEnd] = saveParam.trainingPeriod.split(' ~ ').map(time => time.trim());

        // 추출된 값을 saveParam에 저장
        saveParam.trainingStart = trainingStart;
        saveParam.trainingEnd = trainingEnd;
    }
    sAndHTrainingResultStore.signature = signatureComponent.value;
    sAndHTrainingResultStore.inspector = inspector.value;
    const result = await sAndHTrainingResultStore.btnSave([saveParam]);
    if (result) {
        searchAction(false);
    }
};
btnDelete(async () => {
    sAndHTrainingResultStore.signature = signatureComponent.value;
    sAndHTrainingResultStore.inspector = inspector.value;
    await sAndHTrainingResultStore.btnDeleteDetail();
});

btnDownload(() => {
    let list = [sAndHTrainingResultStore.inputForm.docNo];
    sAndHTrainingResultStore.btnDownload(list, true);
});

btnAdd(() => {
    sAndHTrainingResultStore.searchParam.docNo = null;
    sAndHTrainingResultStore.initInputForm();
    // inspectorComponent.value = [];
    layoutStore.useBtnList = iButtonList;
});
//-----------------------------------------------
//useYn 체크

//-----------------------------------------------
//-----------------------------------------------

const updateBudget = event => {
    // 입력된 값에서 숫자 이외의 문자를 제거
    const numericValue = parseFloat(event.target.value.replace(/[^0-9]/g, '')) || 0;

    // 실제 숫자 값을 budget에 저장
    sAndHTrainingResultStore.inputForm.budget = numericValue;

    // 천 단위로 포맷된 값 업데이트
    // hseBudgetStore.inputForm.budget = formatToAmt(numericValue);
};

//-----------------------------------------------
//-----------------------------------------------
// 체크박스가 체크될지 여부를 computed로 정의
const isCheckboxChecked = computed(() => {
    const value = sAndHTrainingResultStore.inputForm.materialsEtc;
    return value !== null && value !== '' && value !== undefined;
});

const isCheckboxChecked2 = computed(() => {
    const value = sAndHTrainingResultStore.inputForm.effectivenessEtc;
    return value !== null && value !== '' && value !== undefined;
});

// 체크박스 상태 변경 처리
const handleCheckboxChange = event => {
    if (!event.target.checked) {
        sAndHTrainingResultStore.inputForm.effectivenessEtc = '';
    }
};

// 체크박스 상태 변경 처리
const handleCheckboxChange2 = event => {
    if (!event.target.checked) {
        sAndHTrainingResultStore.inputForm.effectivenessEtc = '';
    }
};

// 텍스트 입력 시 체크박스 상태 동기화
const handleTextInput = event => {
    if (event.target.value.trim() === '') {
        sAndHTrainingResultStore.inputForm.materialsEtc = '';
    }
};

// 텍스트 입력 시 체크박스 상태 동기화
const handleTextInput2 = event => {
    if (event.target.value.trim() === '') {
        sAndHTrainingResultStore.inputForm.effectivenessEtc = '';
    }
};

//-----------------------------------------------
//-----------------------------------------------
// [토글버튼]
const toggleCheckYn = (item, event) => {
    const isChecked = event.target.checked;

    sAndHTrainingResultStore.inputForm[item] = isChecked ? 'Y' : 'N';
};

const updateCondition = condition => {
    // 선택 상태 초기화
    sAndHTrainingResultStore.inputForm.goodCondition = 'N';
    sAndHTrainingResultStore.inputForm.normalCondition = 'N';
    sAndHTrainingResultStore.inputForm.noConcern = 'N';
    // 선택된 값을 'Y'로 설정
    sAndHTrainingResultStore.inputForm[condition] = 'Y';
};

/* ------------------------------------- */
const signatureComponent = ref();
const inspector = ref();

onMounted(() => {
    sAndHTrainingResultStore.signature = signatureComponent.value;
    sAndHTrainingResultStore.inspector = inspector.value;

    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        layoutStore.useBtnList = uButtonList;
        sAndHTrainingResultStore.searchParam.writeYear = param.writeYear;
        sAndHTrainingResultStore.searchParam.docNo = param.docNo;
        sAndHTrainingResultStore.inputForm.writeYear = param.writeYear;
        sAndHTrainingResultStore.inputForm.docNo = param.docNo;
        searchAction(true);
        // sAndHTrainingResultStore.getTrainingResultRptDetail(true);

        isReadonly.value = false;
    } else if (!sAndHTrainingResultStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('SAndHTrainingResultsPlan');
        return;
    } else {
        isReadonly.value = true;
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
    sAndHTrainingResultStore.fileInfo(fileList);
});
//-----------------------------------------------
// [관리 공통 팝업]

// 팝업 열기 함수
const openPopup = () => {
    if (hseTrainingPopup.value) {
        hseTrainingPopup.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

const isReadonly = ref(true);
const openHseTrainingMng = () => {
    if (sAndHTrainingResultStore.inputForm.planWriteYear) {
        confirmMsg('info', '변경 시 이전에 작성한 데이터가 사라집니다.\n그래도 계속하시겠습니까?', '', { fun: openPopup });
    } else openPopup();
};
const closeHseTrainingMng = async e => {
    if (hseTrainingPopup.value) {
        closePopup(hseTrainingPopup);
    }
    sAndHTrainingResultStore.inputForm.planHrList = e[0]?.hrList;
    if (e && e[0]) {
        let data = {
            ...e[0],
            planDocNo: e[0].docNo,
            planDocType: e[0].docType,
            planWriteYear: e[0].writeYear
        };
        data.trainingDate = formatDate(data.trainingDate);
        data.cmd = sAndHTrainingResultStore.inputForm.cmd;
        data.docNo = sAndHTrainingResultStore.inputForm.docNo;
        data.docType = sAndHTrainingResultStore.inputForm.docType;
        data.writeYear = sAndHTrainingResultStore.inputForm.writeYear;
        sAndHTrainingResultStore.inputForm.planWriteYear = e[0].writeYear;
        sAndHTrainingResultStore.inputForm.planDocType = e[0].docType;
        sAndHTrainingResultStore.inputForm.planDocNo = e[0].docNo;
        sAndHTrainingResultStore.inputForm.planTitle = e[0].title;
        // sAndHTrainingResultStore.inputForm = data;
        Object.assign(sAndHTrainingResultStore.inputForm, data);

        await nextTick(); // ✅ UI가 업데이트될 때까지 기다림
        await inspector.value.initPeople([...sAndHTrainingResultStore.inputForm.hrList]);
        //서명상태 'I' 새로고침
        isReadonly.value = true;

        // layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
    }
};
const removeHseTrainingMng = () => {
    sAndHTrainingResultStore.inputForm.planWriteYear = '';
    sAndHTrainingResultStore.inputForm.planDocType = '';
    sAndHTrainingResultStore.inputForm.planDocNo = '';
    sAndHTrainingResultStore.inputForm.trainingPlanTitle = '';
    isReadonly.value = true;
};
//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업]

//-----------------------------------------------
const peoplePopup = ref();

const closePeoplePopup = () => {
    peoplePopup.value.onClose();
};
const peoplePopupOpen = () => {
    peoplePopup.value.onOpen();
};

const selectPeople = () => {
    inspector.value.selectPeople();

    closePeoplePopup();
};

import { getSubjectsMngList, getEduMngList, getInstMngList } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';
import { getLocMngList } from '@/stores/safewiz/support/api/SAndHTrainingImplApi.js';

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
        sAndHTrainingResultStore.inputForm.trainingType = row[0].coursesId;
        sAndHTrainingResultStore.inputForm.trainingTypeNm = row[0].coursesNm;
    }
};

const removeSubject = () => {
    sAndHTrainingResultStore.inputForm.trainingType = '';
    sAndHTrainingResultStore.inputForm.trainingTypeNm = '';
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
        sAndHTrainingResultStore.inputForm.trainingInstitute = row[0].centerId;
        sAndHTrainingResultStore.inputForm.trainingInstituteNm = row[0].centerNm;
    }
};

const removeEdu = () => {
    sAndHTrainingResultStore.inputForm.trainingInstitute = '';
    sAndHTrainingResultStore.inputForm.trainingInstituteNm = '';
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
        sAndHTrainingResultStore.inputForm.trainingInstructor = row[0].instructorId;
        sAndHTrainingResultStore.inputForm.trainingInstructorNm = row[0].instructorNm;
    }
};

const removeInst = () => {
    sAndHTrainingResultStore.inputForm.trainingInstructor = '';
    sAndHTrainingResultStore.inputForm.trainingInstructorNm = '';
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
        sAndHTrainingResultStore.inputForm.trainingLocation = row[0].locationId;
        sAndHTrainingResultStore.inputForm.trainingLocationNm = row[0].locationNm;
    }
};

const removeLoc = () => {
    sAndHTrainingResultStore.inputForm.trainingLocation = '';
    sAndHTrainingResultStore.inputForm.trainingLocationNm = '';
};

//-----------------------------------------------
//시간 체크
const onDateInput = (index, event) => {
    sAndHTrainingResultStore.inputForm.trainingPeriod = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};
//-----------------------------------------------

//-----------------------------------------------

const selectChargePeople = () => {
    if (chargePeoplePopup.value) {
        chargePeoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();
        sAndHTrainingResultStore.inputForm.chargeList = [];
        users.forEach(user => {
            sAndHTrainingResultStore.inputForm.chargeList.push({
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
const accordionRef = ref(null);
const toggleAttendeeAccordion = async e => {
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

onBeforeMount(async () => {
    if (sAndHTrainingResultStore.inputForm.chargeList === null) {
        sAndHTrainingResultStore.inputForm.chargeList = [
            {
                id: '',
                nm: ''
            }
        ];
    }
});

const signSearchTerm = ref('');
const applySignFilter = () => {
    inspector.value?.applyFilter(signSearchTerm.value);
};
</script>
<style lang="scss" scoped>
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
