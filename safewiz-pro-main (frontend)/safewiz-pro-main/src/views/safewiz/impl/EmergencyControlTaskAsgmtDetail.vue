<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc form ui">
        <!-- 검색 필드 -->

        <div class="box oh">
            <OverlayScrollbarsComponent
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="control-field pa2-2rem">
                    <div class="row flex gutters1rem" id="form">
                        <div class="grid12-2 lg-grid12-12">
                            <div class="field">
                                <label for="writeYear" required>
                                    <span>작성년도</span>
                                </label>
                                <input type="text" v-calendar="'yyyy'" v-model="taskAsgmtStore.inputForm.writeYear" id="writeYear" class="datepicker w100p radius" year :required="taskAsgmtStore.inputForm.cmd !== 'U'" :readonly="taskAsgmtStore.inputForm.cmd === 'U' || taskAsgmtStore.inputForm.readonly" @input="writeYearChanged" />
                            </div>
                        </div>
                        <!-- <div class="grid12-10 lg-grid12-6">
                        <div class="field">
                            <label for="">
                                <span></span>
                            </label>
                        </div>
                    </div> -->
                        <div class="grid12-5 lg-grid12-12">
                            <div class="field">
                                <label for="chartNm" required>
                                    <span>비상통제 조직도</span>
                                </label>
                                <i-chips :chips="[{ id: taskAsgmtStore.inputForm.chartId, nm: taskAsgmtStore.inputForm.chartNm }]" :readonly="taskAsgmtStore.inputForm.cmd === 'U'" @popup="addChart" @removeChip="removeChip" required> </i-chips>
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-12">
                            <div class="field">
                                <label for="">비상통제 조직도 차수</label>
                                <input type="text" v-input class="br4px" readonly :value="taskAsgmtStore.inputForm.chartId" />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for="">작성일자</label>
                                <input type="text" v-calendar="getDateFormat()" :min="taskAsgmtStore.inputForm.writeYear + '.01.01'" :max="taskAsgmtStore.inputForm.writeYear + '.12.31'" class="datepicker w100p br4px" v-model="taskAsgmtStore.inputForm.writeDt" @input="changeFlag = true" />
                            </div>
                        </div>
                        <div class="grid12-1 lg-grid12-4 us-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :true-value="'Y'" :false-value="'N'" v-input="'사용'" :checked="taskAsgmtStore.inputForm.useYn == 'Y'" type="checkbox" class="df switch br4px" v-model="taskAsgmtStore.inputForm.useYn" @change="changeFlag = true" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for="title" required><span>제목</span></label>
                                <input type="text" v-input class="br4px" v-model="taskAsgmtStore.inputForm.title" required id="title" @change="changeFlag = true" />
                            </div>
                        </div>
                    </div>
                    <!-- 아코디언 영역 -->
                    <div class="accordion">
                        <div class="list mt1-5rem" v-for="(item, index) in taskAsgmtStore.inputForm.roleList" :key="index">
                            <button type="button" class="df jcsb aic" @click="toggleAccordion">
                                <em>{{ item.roleNm }}</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh">
                                <div class="pa2-2rem bcF8F9FB">
                                    <div class="row flex gutters2rem">
                                        <div class="grid12-12">
                                            <div class="field">
                                                <label for="">담당업무</label>
                                                <textarea name="" id="" class="minh10rem br4px" v-model="item.task" @change="changeFlag = true"></textarea>
                                            </div>
                                            <div class="field">
                                                <label for="">담당자</label>
                                                <i-chips :chips="item.hrList" @popup="addPeople(index)" @removeChip="removePeople" @change="changeFlag = true"> ></i-chips>
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
        <teleport to="body">
            <i-PopupDialog ref="orgChartPopup">
                <!-- 비상사태 유형 선택 팝업 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'비상통제 조직도'" uniqueKey="chartId" filterKey="chartNm" useYnKey="confirmYn" :excluded-value="'N'" :single="true" :fetchParam="{ compId: taskAsgmtStore.inputForm.compId, writeYear: taskAsgmtStore.inputForm.writeYear }" :fetch-data="getEmergencyOrgChartList" @close="closePopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="taskAsgmtStore.inputForm.roleList[selectUserIdx]?.hrList?.map(el => el.hrId)" :valid-orgn-id-list="taskAsgmtStore.inputForm.roleList[selectUserIdx]?.orgnIdList" @selected="selectPeople" @close="closePeoplePopup"></selectUser>

                    <!-- <div class="form ui tar mt2-5rem">
                    <button type="button" class="btn w80px radius active" v-button @click="selectPeoplePopup">
                        <span>{{ t('select') }}</span>
                    </button>
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div> -->
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
// Overlay
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
// BaseView
import BaseView from '@/components/base/BaseView';
const { ref, t, watch, validationStore, toastPopup, formatDate, onMounted, nextTick, btnSearch, btnBack, btnDownload, btnSave, btnDelete, alertMsg, confirmMsg, formatDateForDB } = BaseView();
// Button List
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
layoutStore.useBtnList = iButtonList;
// Function Store
import { useEmerCtrlTaskAsgmt } from '@/stores/safewiz/impl/emerCtrlTaskAsgmt.js';
const taskAsgmtStore = useEmerCtrlTaskAsgmt();

import { getEmergencyOrgChartList } from '@/stores/safewiz/impl/api/emerCtrlOrgChartApi.js';
import { getOrgnChartData, saveEmergencyControlTaskAsgmt } from '@/stores/safewiz/impl/api/emerCtrlTaskAsgmtApi.js';

// Router
import router from '@/router';

import { gsap } from 'gsap';

import { getDateFormat } from '@/utils/dateUtil.js';

let isReverting = false; // revert 상태 여부를 추적하기 위한 플래그
watch(
    () => taskAsgmtStore.inputForm.writeDt,
    (newVal, oldVal) => {
        if (newVal && taskAsgmtStore.inputForm.cmd === 'U') {
            if (isReverting) {
                // 이전 값으로 되돌리는 중이면 watch를 실행하지 않음
                isReverting = false;
                return;
            }

            if (newVal === oldVal) return;

            if (newVal.substring(0, 4) !== oldVal.substring(0, 4)) {
                toastPopup('작성년도는 수정이 불가합니다.', 'error');

                // 값 재설정 시 무한 루프 방지를 위해 플래그 설정
                isReverting = true;
                taskAsgmtStore.inputForm.writeDt = oldVal;
            }
        }
    }
);

const changeFlag = ref(false);
/* ------------------------------------- */
// 아코디언
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

/* ------------------------------------- */
// 비상통제 조직도 팝업
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const orgChartPopup = ref(null);
const addChart = () => {
    orgChartPopup.value.onOpen();
};
const closePopup = data => {
    if (data && data[0]) {
        taskAsgmtStore.inputForm.chartId = data[0].chartId;
        taskAsgmtStore.inputForm.chartNm = data[0].chartNm;
        taskAsgmtStore.inputForm.chartWriteYear = data[0].writeYear;
        let orgnIdList = {};
        getOrgnChartData({ compId: taskAsgmtStore.inputForm.compId, searchText: data[0].chartId }, true)
            .then(res => {
                let chartData = JSON.parse(res.list.chartData);
                taskAsgmtStore.inputForm.roleList = [];
                console.log('@ chartData', chartData);
                chartData.forEach(el => {
                    if (el.isParent) {
                        taskAsgmtStore.inputForm.roleList.push({
                            cmd: 'I',
                            writeYear: taskAsgmtStore.inputForm.writeYear,
                            docType: taskAsgmtStore.inputForm.docType,
                            docNo: taskAsgmtStore.inputForm.docNo,
                            compId: taskAsgmtStore.inputForm.compId,
                            roleId: el.id,
                            roleNm: el.label,
                            task: '',
                            hrList: []
                        });
                    } else if (!el.isParent) {
                        if (orgnIdList[el.parentNode]) {
                            orgnIdList[el.parentNode].orgnIdList.push(el.id);
                        } else {
                            orgnIdList[el.parentNode] = {
                                orgnIdList: [el.id]
                            };
                        }
                    }
                });
            })
            .finally(() => {
                Object.entries(orgnIdList).forEach(([roleId, { orgnIdList: orgIds }]) => {
                    const role = taskAsgmtStore.inputForm.roleList.find(r => r.roleId === roleId);
                    if (role) {
                        role.orgnIdList = orgIds;
                    }
                });
                orgChartPopup.value.onClose();
            });
        changeFlag.value = true;
    } else {
        orgChartPopup.value.onClose();
    }
};
const removeChip = () => {
    taskAsgmtStore.inputForm.chartId = '';
    taskAsgmtStore.inputForm.chartNm = '';
    taskAsgmtStore.inputForm.roleList = [];
    changeFlag.value = true;
};
const writeYearChanged = () => {
    if (taskAsgmtStore.inputForm.chartWriteYear != taskAsgmtStore.inputForm.writeYear) {
        taskAsgmtStore.inputForm.chartId = '';
        taskAsgmtStore.inputForm.chartNm = '';
        taskAsgmtStore.inputForm.roleList = [];
    }
    changeFlag.value = true;
};
// 인원 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
import { write } from 'xlsx';
const selectUserStore = useSelectUserStore();
const peoplePopup = ref(null);

const selectUserIdx = ref(null);
const addPeople = index => {
    selectUserIdx.value = index;
    peoplePopup.value.onOpen();
};
const selectPeople = users => {
    console.log('### select ==> ', users);
    taskAsgmtStore.inputForm.roleList[selectUserIdx.value].hrList = users.map(user => ({
        docType: taskAsgmtStore.inputForm.docType,
        compId: taskAsgmtStore.inputForm.compId,
        id: user.hrId,
        name: user.hrNm,
        hrId: user.hrId,
        hrNm: user.hrNm
    }));
    changeFlag.value = true;

    peoplePopup.value.onClose();
};
// 인원 요소 제거(x버튼 클릭)
const removePeople = index => {
    console.log('### index ==> ', index);
    changeFlag.value = true;
};
// const selectPeoplePopup = () => {
//     const users = selectUserStore.getSelectedUser();
//     console.log('@@user,', users);
//     taskAsgmtStore.inputForm.roleList[selectUserIdx.value].hrList = users.map(user => ({
//         docType: taskAsgmtStore.inputForm.docType,
//         compId: taskAsgmtStore.inputForm.compId,
//         id: user.hrId,
//         name: user.hrNm,
//         hrId: user.hrId,
//         hrNm: user.hrNm
//     }));

//     peoplePopup.value.onClose();
// };

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
/* ------------------------------------- */

onMounted(() => {
    if (!taskAsgmtStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (taskAsgmtStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});

btnSearch(() => {
    if (changeFlag.value || taskAsgmtStore.inputForm.cmd === 'I') {
        confirmMsg('warning', t('msgSaveContinue'), ``, { fun: searchAction });
    } else searchAction();
});
const searchAction = () => {
    taskAsgmtStore.btnDetail(taskAsgmtStore.inputForm, true);
    changeFlag.value = false;
};
btnBack(() => {
    if (changeFlag.value || taskAsgmtStore.inputForm.cmd === 'I') {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});
const goBack = () => {
    router.push('/EmergencyControlTaskAsgmt');
};
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
    }
});
const saveAction = param => {
    // taskAsgmtStore.inputForm.writeYear = taskAsgmtStore.inputForm.writeDt.substring(0, 4);
    taskAsgmtStore.inputForm.writeDt = formatDateForDB(taskAsgmtStore.inputForm.writeDt);

    console.log('@@ 저장할 데이터', taskAsgmtStore.inputForm);
    saveEmergencyControlTaskAsgmt(taskAsgmtStore.inputForm, param)
        .then(res => {
            console.log('@@res.list', res);
            taskAsgmtStore.btnDetail(res.result, false);
        })
        .finally(() => {
            changeFlag.value = false;
            layoutStore.useBtnList = uButtonList;
        });
};
btnDelete(() => {
    confirmMsg('info', t('msgDelete'), '', { fun: deleteAction });
});
const deleteAction = () => {
    taskAsgmtStore.btnDelete([taskAsgmtStore.inputForm]);
    changeFlag.value = false;
};
btnDownload(() => {
    if (changeFlag.value || taskAsgmtStore.inputForm.cmd === 'I') {
        confirmMsg('warning', t('msgSaveContinue'), ``, { fun: downloadAction });
    } else downloadAction();
});
const downloadAction = () => {
    taskAsgmtStore.btnDownload([taskAsgmtStore.inputForm], 'msgPrint');
};
</script>
