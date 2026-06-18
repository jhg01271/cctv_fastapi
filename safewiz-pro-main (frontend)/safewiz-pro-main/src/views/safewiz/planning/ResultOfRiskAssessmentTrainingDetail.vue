<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="resultOfRiskAssessmentTrainingStore && resultOfRiskAssessmentTrainingStore.inputForm" class="contents df fdc">
        <div id="form" class="box">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem pb10rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="control-field ui form mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="writeYear" required="required">
                                    <span>{{ '작성년도' }}</span>
                                </label>
                                <input v-model="resultOfRiskAssessmentTrainingStore.searchParam.searchText" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="asmntDate" required="required">
                                    <span>{{ t('asmnt_date') }}</span>
                                </label>
                                <input type="text" v-calendar="getDateFormat()" class="datepicker radius w50p" id="asmntDate" v-model="resultOfRiskAssessmentTrainingStore.inputForm.asmntDate" required maxlength="10" :min="resultOfRiskAssessmentTrainingStore.searchParam.searchText + '.01.01'" :max="resultOfRiskAssessmentTrainingStore.searchParam.searchText + '.12.31'" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-12">
                            <div class="field">
                                <label for="asmntTime" required="required">
                                    <span>{{ t('asmnt_time') }}</span>
                                </label>
                                <input type="text" v-calendar startTime endTime class="datepicker w100p" id="asmntTime" v-model="resultOfRiskAssessmentTrainingStore.inputForm.asmntTime" required pattern="^(?:[01]\d|2[0-3]):[0-5]\d\s*~\s*(?:[01]\d|2[0-3]):[0-5]\d$" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="createdAt">
                                    <span>{{ t('createdAt') }}</span>
                                </label>
                                <input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(resultOfRiskAssessmentTrainingStore.inputForm.createdAt)" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" :checked="resultOfRiskAssessmentTrainingStore.inputForm.useYn === 'Y'" type="checkbox" class="df switch" v-model="resultOfRiskAssessmentTrainingStore.inputForm.useYn" true-value="Y" false-value="N" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="asmntPlace">{{ t('asmnt_place') }}</label>
                                <input type="text" v-input class="w100p radius" id="asmntPlace" v-model="resultOfRiskAssessmentTrainingStore.inputForm.asmntPlace" :placeholder="t('asmnt_place_placeholder')" />
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="asmntContent">{{ t('asmnt_content') }}</label>
                                <textarea v-model="resultOfRiskAssessmentTrainingStore.inputForm.asmntContent" id="asmntContent" rows="5" class="minh15rem radius" :placeholder="t('asmnt_content_placeholder')"></textarea>
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="asmntResult">
                                    {{ t('asmnt_result') }}
                                    <span class="c9EA1A6 fw400">{{ t('asmnt_result_reference') }}</span>
                                </label>
                                <!-- 첨부파일 -->
                                <div class="bd1pxsolidE1E6ED pa2-2rem br4px">
                                    <IFileList ref="fileList" targetType="RORAT" :targetId="resultOfRiskAssessmentTrainingStore.inputForm.files" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <div class="accordion br4px">
                                    <div class="list">
                                        <button class="df jcsb aic" id="signAccordion" @click="toggleAccordion" :title="errorMessage">
                                            <div class="df">
                                                <em class="w100p wbka ellipsis">{{ t('asmnt_attendees') }}</em>
                                            </div>
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
                                                <i-hr-chips-sign type="ATTENDEES" ref="attendeesComponent" :cmd="resultOfRiskAssessmentTrainingStore.inputForm.cmd" targetType="RORAT" :writeYear="resultOfRiskAssessmentTrainingStore.inputForm.writeYear" :docNo="resultOfRiskAssessmentTrainingStore.inputForm.docNo" :useYn="resultOfRiskAssessmentTrainingStore.inputForm.useYn" />
                                                <button class="w100p mt1rem py8px" type="button" @click="attendeesPopupOpen('attendees')">
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
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>

    <!-- 참석자 팝업 -->
    <teleport to="body">
        <i-PopupDialog ref="attendeesPopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser :single="false" :selected="attendeesComponent.userList?.map(el => el.hrId)" @close="closeAttendeesPopup" @selected="selectAttendees"></selectUser>
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import BaseView from '@/components/base/BaseView';
import { useResultOfRiskAssessmentTraining } from '@/stores/safewiz/planning/ResultOfRiskAssessmentTraining';
const resultOfRiskAssessmentTrainingStore = useResultOfRiskAssessmentTraining();
import IFileList from '@/components/file/iFileList.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
const { setRouterParam, ref, t, onMounted, validationStore, confirmMsg, formatDate, router, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, nextTick } = BaseView();
import { gsap } from 'gsap';
const fileList = ref(null);
// 우측 버튼 생성
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

// 우측 목록 버튼
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/ResultOfRiskAssessmentTraining');
};

// 우측 조회 버튼
btnSearch(async () => {
    signSearchTerm.value = ''; // 참석자 명단 검색어 초기화
    await attendeesComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    // 상세 조회 파라미터가 있을시에만 동작
    if (resultOfRiskAssessmentTrainingStore.searchParam.docNo) {
        await resultOfRiskAssessmentTrainingStore.getResultOfRiskAssessmentTraining(resultOfRiskAssessmentTrainingStore.searchParam, true);

        if (fileList.value && typeof fileList.value.fnSearch === 'function') {
            await fileList.value.fnSearch();
        }
    }
});

// 우측 추가 버튼
btnAdd(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', {
        fun: initData,
        param: ''
    });
});

const initData = async () => {
    await resultOfRiskAssessmentTrainingStore.initInputForm();
    await fileList.value.fnSearch('');
    await attendeesComponent.value.Search('');
};

// 우측 저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        // 참가자 컴포넌트 담아서 같이 파라미터로 보내기
        resultOfRiskAssessmentTrainingStore.inputForm.attendeesComponent = attendeesComponent;
        // 파일 컴포넌트도 넣기
        resultOfRiskAssessmentTrainingStore.inputForm.fileList = fileList;

        // 시작시간, 종료시간 나눠서 넣기
        [resultOfRiskAssessmentTrainingStore.inputForm.asmntStartTime, resultOfRiskAssessmentTrainingStore.inputForm.asmntEndTime] = resultOfRiskAssessmentTrainingStore.inputForm.asmntTime.split('~').map(time => time.trim());

        if (resultOfRiskAssessmentTrainingStore.inputForm.cmd === 'I') {
            // 등록
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: resultOfRiskAssessmentTrainingStore.insertResultOfRiskAssessmentTraining,
                param: resultOfRiskAssessmentTrainingStore.inputForm
            });
        } else {
            //수정
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: resultOfRiskAssessmentTrainingStore.updateResultOfRiskAssessmentTraining,
                param: resultOfRiskAssessmentTrainingStore.inputForm
            });
        }
    }
});

// 우측 삭제 버튼
btnDelete(() => {
    confirmMsg('warning', '삭제 하시겠습니까?', '', {
        fun: resultOfRiskAssessmentTrainingStore.deleteResultOfRiskAssessmentTraining,
        param: [resultOfRiskAssessmentTrainingStore.inputForm]
    });
});

btnDownload(() => {
    resultOfRiskAssessmentTrainingStore.getResultOfRiskAssessmentTrainingReport([resultOfRiskAssessmentTrainingStore.inputForm]);
});

// 참석자
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useRoute } from 'vue-router';

const attendeesPopup = ref();
const attendeesComponent = ref();

const attendeesPopupOpen = () => {
    attendeesPopup.value.onOpen();
};
const closeAttendeesPopup = () => {
    attendeesPopup.value.onClose();
};

const selectAttendees = () => {
    // 참가자 선택 함수
    attendeesComponent.value.selectPeople();
    closeAttendeesPopup();
};
const route = useRoute();
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await resultOfRiskAssessmentTrainingStore.getResultOfRiskAssessmentTraining(param, false);
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (!resultOfRiskAssessmentTrainingStore.inputForm || Object.keys(resultOfRiskAssessmentTrainingStore.inputForm).length === 0) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('ResultOfRiskAssessmentTraining');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
    resultOfRiskAssessmentTrainingStore.fileInfo(fileList);
    if (fileList.value && typeof fileList.value.fnSearch === 'function') {
        fileList.value.fnSearch();
    }
    activeAnimation();
    await attendeesComponent.value.Search();
});

//참석자 확인 필터
const signSearchTerm = ref('');
const applySignFilter = () => {
    attendeesComponent.value?.applyFilter(signSearchTerm.value);
};

// 개별 아코디언 토글 함수
const toggleAccordion = async e => {
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

//화면 최초 진입 시, 자동으로 아코디언 펼침
const activeAnimation = () => {
    const accordion = document.getElementById('signAccordion');
    const container = accordion.parentNode;
    const segment = container.querySelector('.segment');
    const isOpen = accordion.classList.toggle('active');
    const alreadyOpen = accordion.classList.toggle('expanded');
    if (alreadyOpen === true) {
        animateAccordion(segment, isOpen);
    }
};
</script>
