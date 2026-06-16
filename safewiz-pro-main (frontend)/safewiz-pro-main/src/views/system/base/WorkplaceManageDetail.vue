<template>
    <div v-if="workplaceStore && workplaceStore.inputForm" class="contents">
        <div id="form" class="box form ui df fdc">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem pr"
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
                        <div class="grid12-6 ul-grid12-6">
                            <div class="field">
                                <label for="workplaceNm" required>
                                    <span>{{ t('workplaceNm') }}</span>
                                </label>
                                <input v-model="workplaceStore.inputForm.workplaceNm" v-input type="text" class="w100p radius" id="workplaceNm" required placeholder="작업장명을 입력해주세요." />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input v-input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(workplaceStore.inputForm.createdAt)" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-6">
                            <div class="field">
                                <label for="array">{{ t('array') }}</label>
                                <input v-input type="number" class="w100p radius" id="array" v-model="workplaceStore.inputForm.ordSeq" min="0" step="1" max="99" placeholder="99" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="workplaceStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="workplaceStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 ul-grid12-12">
                            <div class="field h100p df fdc">
                                <label for>작업장 사진</label>
                                <!-- 첨부파일 -->
                                <div class="box df aic jcc w100p fg1">
                                    <iFileImage ref="fileList" targetType="workplaceThumbnail" :targetId="workplaceStore.inputForm.thumbnailId" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 ul-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6 ul-grid12-12">
                                    <div class="field">
                                        <label for="headHrNm">{{ t('headHrNm') }}</label>
                                        <i-chips :chips="workplaceStore.inputForm.headHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('head')" @removeChip="removePeople('head', $event)"></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-6 ul-grid12-12">
                                    <div class="field">
                                        <label for="secondHrNm">{{ t('secondHrNm') }}</label>
                                        <i-chips :chips="workplaceStore.inputForm.secondHrList?.map(hr => ({ id: hr.hrId, nm: hr.hrNm }))" @popup="addPeople('second')" @removeChip="removePeople('second', $event)"></i-chips>
                                    </div>
                                </div>
                                <div class="grid12-6 ul-grid12-12">
                                    <div class="field">
                                        <label for="workplaceArea">{{ t('workplaceArea') }}</label>
                                        <input v-model="workplaceStore.inputForm.workplaceArea" v-input type="text" id="workplaceArea" class="w100p radius" placeholder="구역명을 입력해주세요." />
                                    </div>
                                </div>
                                <div class="grid12-6 ul-grid12-12">
                                    <div class="field">
                                        <label for="workplaceAlias">{{ t('workplaceAlias') }}</label>
                                        <input v-model="workplaceStore.inputForm.workplaceAlias" v-input type="text" id="workplaceAlias" class="w100p radius" placeholder="별칭을 입력해주세요." />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label for="workplaceRemark">{{ t('workplaceRemark') }}</label>
                                <input v-model="workplaceStore.inputForm.remark" v-input type="text" class="w100p radius" placeholder="작업장 설명을 입력해주세요." />
                            </div>
                        </div>
                    </div>
                </form>
                <!-- 추가 요청사항 이전 / 다음 -->
                <div v-if="workplaceStore.inputForm.workplaceId != 'temp'" class="df jcfe sticky t100p rg-t0 rg-b0-5rem tty15px">
                    <button type="button" class="btn line active radius w20rem mr8px" @click="changeWorkplace('prev')" :disabled="workplaceStore.selectedWorkplaceIdx <= 0">
                        <svg class="mr1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M5 1L1.20974 4.5286C0.930086 4.78894 0.930086 5.21106 1.20974 5.4714L5 9" stroke="#3248F6" stroke-linecap="round" />
                        </svg>

                        <span>이전 작업장</span>
                    </button>
                    <button type="button" class="btn line active radius w20rem" @click="changeWorkplace('next')" :disabled="workplaceStore.filteredWpList?.length === workplaceStore.selectedWorkplaceIdx + 1">
                        <span>다음 작업장</span>

                        <svg class="ml1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M1 9L4.79026 5.4714C5.06991 5.21106 5.06991 4.78894 4.79026 4.5286L1 1" stroke="#3248F6" stroke-linecap="round" />
                        </svg>
                    </button>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <h3>인원</h3>
                <selectUser :single="false" :selected="gubun === 'head' ? workplaceStore.inputForm.headHrList.map(el => el.hrId) : workplaceStore.inputForm.secondHrList.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, confirmMsg, validationStore, onMounted, t, /* confirmMsg, */ formatDate, /* watch, */ btnSearch, btnBack, btnAdd, btnSave, btnDelete } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
// import { useHrStore } from '@/stores/system/base/hr.js';
// const hrStore = useHrStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
import selectUser from '@/views/base/member/SelectUser/Index.vue';

import { useWorkplaceStore } from '@/stores/system/base/workplace.js';
import IFileImage from '@/components/file/iFileImage.vue';
const fileList = ref(null);

const workplaceStore = useWorkplaceStore();
import _ from 'lodash';
onMounted(() => {
    if (!workplaceStore.inputForm.workplaceId) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (workplaceStore.inputForm.cmd == 'U') layoutStore.useBtnList = uButtonList;
    else layoutStore.useBtnList = iButtonList;
    workplaceStore.fileInfo(fileList);
    //파일조회
    fileList.value.fnSearch();
});

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    if (JSON.stringify(workplaceStore.originData) !== JSON.stringify(workplaceStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: searchAction, param: true });
    } else {
        searchAction(true);
    }
});
const searchAction = async notify => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    await workplaceStore.getWorkplaceDetail(workplaceStore.workplaceId, true);
    await fileList.value.fnSearch();
};

btnBack(() => {
    if (JSON.stringify(workplaceStore.originData) !== JSON.stringify(workplaceStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: goBack, param: '' });
    } else {
        goBack('');
    }
});
const goBack = () => {
    fileList.value.fnRemove();
    router.go(-1);
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await workplaceStore.btnSave();
        if (workplaceStore.inputForm.cmd === 'I') {
            //추가
            confirmMsg('info', t('msgSave'), '', { fun: insertAction });
        } else {
            //수정
            confirmMsg('info', t('msgSave'), '', { fun: updateAction });
        }
    }
});
const insertAction = () => {
    workplaceStore.insertWorkplace(workplaceStore.inputForm).then(res => {
        layoutStore.useBtnList = uButtonList;
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        workplaceStore.workplaceId = res.result.workplaceId;
        workplaceStore.getWorkplaceDetail(res.result.workplaceId, false);
    });
};
const updateAction = () => {
    workplaceStore.updateWorkplace(workplaceStore.inputForm).then(res => {
        layoutStore.useBtnList = uButtonList;
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        workplaceStore.getWorkplaceDetail(res.result.workplaceId, false);
    });
};

btnAdd(() => {
    if (!_.isEqual(workplaceStore.originData, workplaceStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: addAction, param: '' });
    } else {
        addAction();
    }
});
const addAction = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    workplaceStore.btnAdd('temp');
    //파일초기화
    fileList.value.fnRemove();
    layoutStore.useBtnList = iButtonList;
};

btnDelete(() => {
    workplaceStore.btnDelete('D');
});

// import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
// const selectUserStore = useSelectUserStore();

// 인원 팝업 구분값 담는 변수
const gubun = ref();
const peoplePopup = ref(null); // PopupDialog에 대한 ref
const addPeople = el => {
    gubun.value = el;
    peoplePopup.value.onOpen();
};
const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e.length > 0) {
        if (gubun.value === 'head') {
            workplaceStore.inputForm.headHrList = e;
        } else if (gubun.value === 'second') {
            workplaceStore.inputForm.secondHrList = e;
        }
    }
};

// 인원 제거 (x버튼 클릭 시)
const removePeople = (type, index) => {
    if (type === 'head') {
        workplaceStore.inputForm.headHrList.splice(index, 1);
    } else if (type === 'second') {
        workplaceStore.inputForm.secondHrList.splice(index, 1);
    }
};
// 작업장 이동 버튼 이벤트
const changeWorkplace = async e => {
    if (!_.isEqual(workplaceStore.originData, workplaceStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: changeWorkplaceAction, param: e });
    } else {
        changeWorkplaceAction(e);
    }
};
const changeWorkplaceAction = async e => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    fileList.value.fnRemove();
    if (e === 'prev') {
        // 이전 작업장
        if (workplaceStore.selectedWorkplaceIdx > 0) workplaceStore.selectedWorkplaceIdx--;
    } else if (e === 'next') {
        // 다음 작업장
        if (workplaceStore.selectedWorkplaceIdx + 1 <= workplaceStore.filteredWpList.length) workplaceStore.selectedWorkplaceIdx++;
    }
    const workplaceId = workplaceStore.filteredWpList[workplaceStore.selectedWorkplaceIdx].workplaceId;
    workplaceStore.workplaceId = workplaceId;
    await workplaceStore.getWorkplaceDetail(workplaceStore.workplaceId, true);

    workplaceStore.fileInfo(fileList);

    //파일조회
    fileList.value.fnSearch();
};
</script>
