<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="myHseJobAssignmentStore && myHseJobAssignmentStore.inputForm" class="contents df fdc">
        <div class="box form ui">
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
                <div id="form" class="control-field ui form mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="writeYear">{{ t('hseAssign_writeYear') }}</label>
                                <input type="text" class="datepicker w100p radius" v-calendar id="createdAt" :value="formatDate(myHseJobAssignmentStore.inputForm.writeYear)" disabled required />
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(myHseJobAssignmentStore.inputForm.createdAt)" disabled required />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" :checked="myHseJobAssignmentStore.inputForm.useYn === 'Y'" @change="myHseJobAssignmentStore.toggleUseYn" class="df switch" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 es-grid12-6"></div>

                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="useYn" required>
                                    <span>담당자</span>
                                </label>
                                <i-chips :chips="[{ id: myHseJobAssignmentStore.inputForm.hrId, name: myHseJobAssignmentStore.inputForm.hrNm }]" @popup="addPeople('head')" @removeChip="removePeople('head')" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="useYn">조직</label>
                                <input type="text" v-input class="w100p radius" v-model="myHseJobAssignmentStore.inputForm.orgnNm" disabled />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="useYn">직위</label>
                                <div class="df aic gap4px">
                                    <input type="text" v-input class="w100p radius" v-model="myHseJobAssignmentStore.inputForm.jbrpNm" disabled />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="useYn">업무대행자</label>
                                <div class="df aic gap4px">
                                    <i-chips :chips="[{ id: myHseJobAssignmentStore.inputForm.backupHrId, name: myHseJobAssignmentStore.inputForm.backupHrNm }]" @popup="addPeople('second')" @removeChip="removePeople('second')"></i-chips>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label>담당 업무</label>
                                <textarea v-model="myHseJobAssignmentStore.inputForm.assignTask" name id class="radius minh15rem" placeholder="내용을 입력하세요."></textarea>
                            </div>
                        </div>
                    </div>
                    <!-- Task 리스트 -->
                    <div class="field df aic gap1rem mt1rem">
                        <label>Task 리스트 | {{ `${t('dashboard_lastCheckTime')}: ${lastUpdated} (${t('dashboard_renewalCycle')}: ${refreshInterval / 1000 / 60}${t('dashboard_minute')})` }}</label>
                    </div>

                    <div class="field">
                        <div class="row flex gutters1rem">
                            <div class="grid12-5 es-grid12-12">
                                <div class="df aic gap1rem">
                                    <input v-input type="text" v-calendar="getDateFormat()" v-model="taskStore.startDay" class="datepicker w100p radius" />
                                    <span>~</span>
                                    <input v-input type="text" v-calendar="getDateFormat()" v-model="taskStore.endDay" class="datepicker w100p radius" />
                                </div>
                            </div>

                            <div class="grid12-7 es-grid12-12">
                                <div class="df bcffffff">
                                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="taskStore.searchTermTask" @keyup.enter="taskStore.applyFilterTask" />
                                    <button type="button" class="shrink0" @click="taskStore.applyFilterTask">
                                        <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <OverlayScrollbarsComponent
                            class="mt1-5rem h100p"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <table id="tableForm" class="lg-minw700px wbka lh1-4">
                                <colgroup>
                                    <col class="w15p" />
                                    <col class="w15p" />
                                    <col />
                                    <col />
                                    <col class="w10p" />
                                    <col class="w15p" />
                                    <col class="w10p" />
                                </colgroup>
                                <thead>
                                    <tr class="h4-4rem">
                                        <th>{{ t('사업장') }}</th>
                                        <th>{{ t('요청일시') }}</th>
                                        <th>{{ t('화면') }}</th>
                                        <th>{{ t('내역') }}</th>
                                        <th>{{ t('요청자') }}</th>
                                        <th>{{ t('완료일시') }}</th>
                                        <th>{{ t('화면 이동') }}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <template v-for="(task, index) in taskStore.filteredTaskList" :key="index">
                                        <tr class="h4-4rem" id="tableForm">
                                            <td class="tac">
                                                {{ task.compNm }}
                                            </td>
                                            <td class="tac">
                                                {{ task.reqDt.split('.')[0]?.replaceAll('-', '.') }}
                                            </td>
                                            <td>
                                                {{ task.reqMenuNm }}
                                            </td>
                                            <td class="tac">
                                                {{ task.taskDetailContent }}
                                            </td>
                                            <td class="tac">
                                                {{ task.reqUserNm }}
                                            </td>
                                            <td class="tac">
                                                {{ task.completeDt?.split('.')[0]?.replaceAll('-', '.') }}
                                            </td>
                                            <td class="tac">
                                                <button class="btn w7-4rem radius" @click="goMenu(task)">
                                                    <span>이동</span>
                                                </button>
                                            </td>
                                        </tr>
                                    </template>
                                </tbody>
                            </table>
                        </OverlayScrollbarsComponent>
                    </div>
                </div>
            </OverlayScrollbarsComponent>

            <teleport to="body">
                <!-- 담당자 지정 팝업 -->
                <i-PopupDialog ref="peoplePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <selectUser :single="true" :selected="[myHseJobAssignmentStore.inputForm.hrId]" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                    </div>
                </i-PopupDialog>
            </teleport>
        </div>
    </div>
</template>

<script setup lang="ts">
import BaseView from '@/components/base/BaseView';
import { getDateFormat } from '@/utils/dateUtil.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useMyHseJobAssignmentStore } from '@/stores/safewiz/participation/myHseJobAssignment.js';
import { useTaskStore } from '@/stores/safewiz/task/task.js';
const { watch, setRouterParam, ref, confirmMsg, onMounted, t, formatDate, router, btnSearch, btnBack, btnAdd, btnSave, btnDelete, alertMsg, validationStore, getCurrentDate, getPreMonth, onUnmounted, getFormattedDate, formatDateForDB } = BaseView();
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

// [화면 store]
const myHseJobAssignmentStore = useMyHseJobAssignmentStore();
const taskStore = useTaskStore();

import { useMenuStore } from '@/stores/menu.js';
const menuStore = useMenuStore();

watch(
    () => menuStore.selectedCompany,
    () => {
        // 사업장이 변경되고, 뒤로가기로 왔을 때 재 조회로직 수행
        refreshMyHseJobAssignment();
    }
);
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
let refreshInterval = 1000 * 60 * 5; // 10분 (1000ms * 60초 * 10)
let timeoutId = null;
const lastUpdated = ref(new Date().toLocaleTimeString('en-US'));
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    taskStore.startDay = getPreMonth(1);
    taskStore.endDay = getCurrentDate();
    console.log('#param', param);
    if (param) {
        myHseJobAssignmentStore.searchParam.docNo = '';
        myHseJobAssignmentStore.searchParam.docType = 'JOB';
        await myHseJobAssignmentStore.getMyJobAssignDetailList(false);
        await taskStore.getTaskList({
            docType: myHseJobAssignmentStore.searchParam.docType,
            docNo: myHseJobAssignmentStore.searchParam.docNo,
            searchFrom: formatDateForDB(taskStore.startDay),
            searchTo: formatDateForDB(taskStore.endDay)
        });
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
        getSystemCode({ majorCd: 'CI001' })
            .then(res => {
                const data = res.list.find(el => el.minorCd === 'HseJobPage');
                if (data) {
                    refreshInterval = parseInt(data.extra1) * 1000;
                }
            })
            .finally(() => {
                refreshMyHseJobAssignment();
            });
    } else if (!myHseJobAssignmentStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.push('/MyHseJobAssignment');
        return;
    } else {
        //신규 추가 경우, 버튼 [목록, 저장]만 보임
        if (myHseJobAssignmentStore.inputForm.cmd == 'I') {
            layoutStore.useBtnList = ['btnBack', 'btnSave'];
        } else if (myHseJobAssignmentStore.inputForm.cmd === 'U') {
            layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
        }
    }
});

const refreshMyHseJobAssignment = async () => {
    myHseJobAssignmentStore.getMyJobAssignDetailList(true);
    taskStore.getTaskList({
        writeYear: myHseJobAssignmentStore.searchParam.writeYear,
        docType: myHseJobAssignmentStore.searchParam.docType,
        docNo: myHseJobAssignmentStore.searchParam.docNo,
        searchText: myHseJobAssignmentStore.inputForm.userId,
        searchFrom: formatDateForDB(taskStore.startDay),
        searchTo: formatDateForDB(taskStore.endDay)
    });
    lastUpdated.value = new Date().toLocaleTimeString('en-US');
    timeoutId = setTimeout(() => {
        refreshMyHseJobAssignment();
    }, refreshInterval);
};

// 타임아웃 해제 함수
const clearRefresh = () => {
    if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
    }
};

// onUnmounted는 컴포넌트가 DOM에서 제거되기 직전에 실행
onUnmounted(() => {
    // 인터벌 해제: 메모리 누수를 방지
    clearRefresh();
});

// [버튼리스트]
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
// 조회 버튼 이벤트 함수
btnBack(() => {
    if (JSON.stringify(myHseJobAssignmentStore.originData) !== JSON.stringify(myHseJobAssignmentStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: myHseJobAssignmentStore.goObject, param: '' });
    } else {
        myHseJobAssignmentStore.goObject();
    }
});
btnSearch(() => {
    if (JSON.stringify(myHseJobAssignmentStore.originData) !== JSON.stringify(myHseJobAssignmentStore.inputForm)) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: myHseJobAssignmentStore.getMyJobAssignDetailList, param: true });
    } else {
        myHseJobAssignmentStore.getMyJobAssignDetailList(true);
        taskStore.getTaskList({
            writeYear: myHseJobAssignmentStore.searchParam.writeYear,
            docType: myHseJobAssignmentStore.searchParam.docType,
            docNo: myHseJobAssignmentStore.searchParam.docNo,
            searchText: myHseJobAssignmentStore.inputForm.userId,
            searchFrom: formatDateForDB(taskStore.startDay),
            searchTo: formatDateForDB(taskStore.endDay)
        });
    }
});
btnAdd(() => {
    if (JSON.stringify(myHseJobAssignmentStore.originData) !== JSON.stringify(myHseJobAssignmentStore.inputForm)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', {
            fun: () => {
                taskStore.filteredTaskList = [];
                myHseJobAssignmentStore.btnAdd();
                //신규 추가 경우, 버튼 [목록, 저장]만 보임
                if (myHseJobAssignmentStore.inputForm.cmd == 'I') {
                    layoutStore.useBtnList = myHseJobAssignmentStore.buttonList;
                }
            }
        });
    } else {
        taskStore.filteredTaskList = [];
        myHseJobAssignmentStore.btnAdd();
        if (myHseJobAssignmentStore.inputForm.cmd == 'I') {
            layoutStore.useBtnList = myHseJobAssignmentStore.buttonList;
        }
    }
});
btnDelete(() => {
    myHseJobAssignmentStore.btnDelete('D');
});
btnSave(() => {
    detailSave();
});

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await myHseJobAssignmentStore.btnSave();
    }
};
// ---------------------------------------------------

// [인원 팝업]
const gubun = ref();
const peoplePopup = ref(null); // PopupDialog에 대한 ref
const addPeople = el => {
    gubun.value = el;
    selectUserStore.currentTab = 'orgn';
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
    if (e && e.hrId) {
        if (gubun.value === 'head') {
            if (e.hrId == myHseJobAssignmentStore.inputForm.backupHrId) {
                alertMsg('warning', '담당자와 업무대행자는 동일할 수 없습니다.');
                return;
            }
            myHseJobAssignmentStore.inputForm.hrId = e.hrId;
            myHseJobAssignmentStore.inputForm.hrNm = e.hrNm;
            myHseJobAssignmentStore.inputForm.orgnNm = e.orgnNm;
            myHseJobAssignmentStore.inputForm.jbrpNm = e.jbrpNm;
        } else if (gubun.value === 'second') {
            if (e.hrId == myHseJobAssignmentStore.inputForm.hrId) {
                alertMsg('warning', '담당자와 업무대행자는 동일할 수 없습니다.');
                return;
            }
            myHseJobAssignmentStore.inputForm.backupHrId = e.hrId;
            myHseJobAssignmentStore.inputForm.backupHrNm = e.hrNm;
        }
    }
};

// 인원 제거 (x버튼 클릭 시)
const removePeople = type => {
    if (type === 'head') {
        myHseJobAssignmentStore.inputForm.hrId = '';
        myHseJobAssignmentStore.inputForm.hrNm = '';
    } else if (type === 'second') {
        myHseJobAssignmentStore.inputForm.backupHrId = '';
        myHseJobAssignmentStore.inputForm.backupHrNm = '';
    }
};
// ---------------------------------------------------

const goMenu = task => {
    taskStore.goToTask(task);
};
</script>
