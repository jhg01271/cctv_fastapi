<template>
    <!-- 공정관리 상세 -->
    <div v-if="processStore && processStore.inputForm" class="contents">
        <div id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 ul-grid12-6 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="hrNm" required>
                                    <span>공정명</span>
                                </label>
                                <input v-input v-model="processStore.inputForm.processNm" type="text" class="w100p radius" id="공정명" required placeholder="공정명을 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-3 ul-grid12-6 sm-grid12-6 es-grid12-12">
                            <div v-if="processStore.inputForm.usageType" class="field" :key="processStore.inputForm.usageType">
                                <label for="usageType">
                                    <span>사용처</span>
                                </label>
                                <select name id="usageType" v-select v-model="processStore.inputForm.usageType">
                                    <option v-for="item in processStore.filteredUseList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-4 sm-grid12-4 es-grid12-12">
                            <div class="field">
                                <label for>{{ t('createdAt') }}</label>
                                <input v-input id="createdAt" :value="formatDate(processStore.inputForm.createdAt)" disabled type="text" class="datepicker w100p radius" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-4 sm-grid12-4 es-grid12-6">
                            <div class="field">
                                <label for>{{ t('array') }}</label>
                                <input v-input type="number" v-model="processStore.inputForm.ordSeq" class="w100p radius" id placeholder="99" min="0" max="99" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-4 sm-grid12-4 es-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="processStore.inputForm.useYn === 'Y'" @change="processStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 lg-grid12-12">
                            <div class="field h100p df fdc">
                                <label for>공정 사진</label>
                                <!-- 첨부파일 -->
                                <div class="box df aic jcc w100p fg1">
                                    <iFileImage ref="fileList" targetType="process" :targetId="processStore.inputForm.fileId" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 lg-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6">
                                    <div class="field">
                                        <label for="headHrNm">
                                            <span>{{ t('headHrNm') }}</span>
                                        </label>
                                        <iChips :chips="processStore.inputForm.headHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('head')" @removeChip="removePeople('head', $event)"></iChips>
                                    </div>
                                </div>

                                <div class="grid12-6">
                                    <div class="field">
                                        <label for="secondHrNm">
                                            <span>{{ t('secondHrNm') }}</span>
                                        </label>
                                        <iChips :chips="processStore.inputForm.secondHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('second')" @removeChip="removePeople('second', $event)"></iChips>
                                    </div>
                                </div>

                                <div class="grid12-6">
                                    <div class="field">
                                        <label for>
                                            <span>조직</span>
                                        </label>

                                        <i-chips :chips="processStore.inputForm.orgnList" @popup="openOrgn"></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-6">
                                    <div class="field">
                                        <label for>작업장</label>
                                        <i-chips :chips="processStore.inputForm.workplaceList" @popup="addWorkplace" @removeChip="removeWorkplace"></i-chips>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label for>공정 설명</label>
                                <textarea name id class="w100p radius minh10rem" v-model="processStore.inputForm.remark" placeholder="공정 설명을 입력해주세요."></textarea>
                            </div>
                        </div>
                        <div class="grid12-12 pr">
                            <h3>작업 내용</h3>
                            <!-- <div class="df aic jcfe mt1-2rem"> -->
                            <button type="button" class="btn active radius w19rem" @click="openWorkContentPopup()" :disabled="processStore.inputForm.processId == 'temp'">
                                <span class="mr1rem">작업내용 차수 관리</span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 12 12" fill="none">
                                    <g clip-path="url(#clip0_378_5168)">
                                        <path d="M6 -2V14M14 6L-2 6" stroke="white" stroke-linecap="round" />
                                    </g>
                                    <defs>
                                        <clipPath id="clip0_378_5168">
                                            <rect width="12" height="12" fill="white" />
                                        </clipPath>
                                    </defs>
                                </svg>
                            </button>
                            <!-- </div> -->
                            <hr class="w100p h1px bcE2E6EE mt1rem mb1rem" />
                            <div v-if="processStore.prcsWork && processStore.prcsWork[0]" class="row flex gutters1rem">
                                <div class="grid12-4 md-grid12-12">
                                    <div class="field">
                                        <label for>차수</label>
                                        <input type="text" v-input :value="processStore.prcsWork[0].revNo" class="w100p radius" disabled="true" />
                                    </div>
                                </div>
                                <div class="grid12-4 md-grid12-6 us-grid12-12">
                                    <div class="field">
                                        <label for>등록일자</label>
                                        <input type="text" v-input :value="formatDate(processStore.prcsWork[0].createdAt)" class="w100p radius datepicker" disabled="true" />
                                    </div>
                                </div>
                                <div class="grid12-4 md-grid12-6 us-grid12-12">
                                    <div class="field">
                                        <label for>확정일자</label>
                                        <input type="text" v-input :value="formatDate(processStore.prcsWork[0].cnfmAt)" class="w100p radius datepicker" disabled="true" />
                                    </div>
                                </div>

                                <div class="grid12-12">
                                    <div class="field">
                                        <label for>작업내용</label>
                                    </div>
                                    <OverlayScrollbarsComponent
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <table class="tac minw60rem">
                                            <colgroup>
                                                <col class="w10p" />
                                                <col class="w45p" />
                                                <col class="w45p" />
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th>순서</th>
                                                    <th>작업내용</th>
                                                    <th>설명</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(work, index) in processStore.prcsWork" :key="index">
                                                    <td>{{ work.ordSeq }}</td>
                                                    <td>{{ work.workContent }}</td>
                                                    <td>{{ work.workDesc }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </OverlayScrollbarsComponent>
                                </div>
                            </div>
                            <div v-else class="row flex gutters1rem">
                                <sub class="fs1-4rem fw300">* 작업내용을 먼저 입력하세요.</sub>
                            </div>
                        </div>
                    </div>
                </form>

                <div v-if="processStore.inputForm.processId != 'temp'" class="df jcfe sticky t100p rg-t0 rg-b0-5rem tty10px">
                    <button type="button" class="btn line active radius w20rem mr8px" @click="changeWorkplace('prev')" :disabled="processStore.selectedWorkplaceIdx <= 0">
                        <svg class="mr1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M5 1L1.20974 4.5286C0.930086 4.78894 0.930086 5.21106 1.20974 5.4714L5 9" stroke="#3248F6" stroke-linecap="round" />
                        </svg>

                        <span>이전 공정</span>
                    </button>
                    <button type="button" class="btn line active radius w20rem" @click="changeWorkplace('next')" :disabled="processStore.combinedList.value.length === processStore.selectedWorkplaceIdx + 1">
                        <span>다음 공정</span>

                        <svg class="ml1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M1 9L4.79026 5.4714C5.06991 5.21106 5.06991 4.78894 4.79026 4.5286L1 1" stroke="#3248F6" stroke-linecap="round" />
                        </svg>
                    </button>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 인원 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="gubun === 'head' ? processStore.inputForm.headHrList.map(el => el.hrId) : processStore.inputForm.secondHrList.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>

            <!-- 작업내용 차수관리 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="workContentPopup">
                <workContent @close="closeWorkPopup" @search="processStore.getPrcsDetailList(processStore.inputForm.processId, true)" :processId="processStore.inputForm.processId"></workContent>
            </i-PopupDialog>

            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="orgnPopup">
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'조직'" :selectedIdList="processStore.inputForm.orgnList?.map(orgn => orgn.id)" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getOrganization" @close="closeOrgn" @apply="applyOrgn" />
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 작업장 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="workplacePopup">
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'작업장'" :selectedIdList="processStore.inputForm.workplaceList?.map(wp => wp.id)" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getWp" @close="closeWorkplacePopup" @apply="applyWorkplacePopup" />
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, getCompId, confirmMsg, formatDate, getFormattedDate, watch, btnSearch, btnBack, btnAdd, btnSave, btnDelete } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

//시스템코드 조회
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

//파일
import iFileImage from '@/components/file/iFileImage.vue';
const fileList = ref(null);

// 작업내용 팝업
import workContent from '@/views/system/base/popup/WorkContentPopup.vue';
const workContentPopup = ref(null);

import { useProcessStore } from '@/stores/system/base/process.js';
const processStore = useProcessStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    processStore.getPrcsDetailList(processStore.processId, true);
    fileList.value.fnSearch();
});

btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: processStore.goBack, param: '' });
});

btnSave(() => {
    detailSave();
});

btnAdd(() => {
    //파일 초기화
    if (processStore.inputForm) {
        // 추가 페이지로 이동
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: processStore.btnAdd, param: '' });
    }
});

btnDelete(() => {
    processStore.btnDelete('D');
});

const compId = getCompId();

onMounted(async () => {
    if (processStore.inputForm?.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
    }

    //사용처 select box 리스트
    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0010',
            compId: compId
        })
    ]);
    processStore.useList = responses[0].list;

    if (processStore.inputForm) {
        // 파일관련
        fileList.value.fnSearch();

        processStore.setRefs(fileList);
    }
});

// ---------------------------------------------------

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();

// 인원 팝업 구분값 담는 변수
const peoplePopup = ref(null); // PopupDialog에 대한 ref
const addPopup = ref(null);

const gubun = ref();

const addPeople = el => {
    gubun.value = el;
    peoplePopup.value.onOpen();
};

const closePeoplePopup = () => {
    peoplePopup.value.onClose();
};

const openWorkContentPopup = () => {
    workContentPopup.value.onOpen();
};

const closeWorkPopup = () => {
    workContentPopup.value.onClose();
};
// ---------------------------------------------------

const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e.length > 0) {
        if (gubun.value === 'head') {
            processStore.inputForm.headHrList = e;
        } else if (gubun.value === 'second') {
            processStore.inputForm.secondHrList = e;
        }
    }
};
const removePeople = (type, index) => {
    if (type === 'head') {
        processStore.inputForm.headHrList.splice(index, 1);
    } else if (type === 'second') {
        processStore.inputForm.secondHrList.splice(index, 1);
    }
};
// ---------------------------------------------------
// 조직 선택 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
const orgnPopup = ref(null);
const openOrgn = () => {
    orgnPopup.value.onOpen();
};
const applyOrgn = e => {
    if (e.length > 0) {
        e.forEach(el => {
            el.id = el.orgnId;
            el.name = el.orgnNm;
        });
        processStore.inputForm.orgnList = e;
    }
    orgnPopup.value.onClose();
};

const closeOrgn = () => {
    orgnPopup.value.onClose();
};
// ---------------------------------------------------
// 작업장 팝업
import { getWp } from '@/stores/system/base/api/workplaceApi';
const workplacePopup = ref(null);

const addWorkplace = () => {
    workplacePopup.value.onOpen();
};

const applyWorkplacePopup = data => {
    setSelectedWpData(data);
};
const closeWorkplacePopup = () => {
    workplacePopup.value.onClose();
};

const setSelectedWpData = data => {
    if (data[0]) {
        processStore.inputForm.workplaceList = data.map(item => ({
            id: item.workplaceId,
            nm: item.workplaceNm
        }));
    }
    workplacePopup.value.onClose();
};

// 작업장 요소 제거(x버튼 클릭)
const removeWorkplace = index => {
    processStore.inputForm.workplaceList = [];
};

// ---------------------------------------------------

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await processStore.btnSave();
    }
};

// 공통 함수: 입력된 값에서 숫자만 추출하여 YYYYMMDD로 저장
// ex) '2024.10.04' =>  yyyymmdd 형식으로 변환
const onDateInput = (field, event) => {
    const cleanedValue = event.target.value.replace(/\D/g, '').slice(0, 8); // 숫자만 추출
    processStore.inputForm[field] = cleanedValue; // 실제 데이터는 YYYYMMDD 형식
};

// 작업장 이동 버튼 이벤트
const changeWorkplace = async e => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    if (e === 'prev') {
        // 이전 작업장
        if (processStore.selectedWorkplaceIdx > 0) processStore.selectedWorkplaceIdx--;
    } else if (e === 'next') {
        // 다음 작업장
        if (processStore.selectedWorkplaceIdx + 1 <= processStore.combinedList.value.length) processStore.selectedWorkplaceIdx++;
    }

    const processId = processStore.combinedList.value[processStore.selectedWorkplaceIdx].processId;
    processStore.processId = processId;
    await processStore.getPrcsDetailList(processStore.processId, true);

    if (processStore.fileInfo) {
        processStore.fileInfo(fileList);
    } else {
        fileList.value.fnRemove();
    }

    //파일조회
    fileList.value.fnSearch();
};

// ---------------------------------------------------

onMounted(async () => {
    if (!processStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
});
</script>
