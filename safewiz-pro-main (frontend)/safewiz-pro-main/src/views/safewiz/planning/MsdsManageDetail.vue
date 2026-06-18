<template>
    <!-- MSDS상세 -->
    <div v-if="msdsStore && msdsStore.inputForm" class="contents">
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
                        <div class="grid12-3 lg-grid12-6">
                            <div class="field">
                                <label for="casNo" required>
                                    <span>{{ t('msds_casNo') }}</span>
                                </label>
                                <i-chips :chips="[{ id: msdsStore.inputForm.casNo, name: msdsStore.inputForm.casNo }]" @popup="addMsds" @removeChip="removeMsds"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6">
                            <div class="field">
                                <label for="msdsNm" required>
                                    <span>{{ t('msds_name') }}</span>
                                </label>
                                <input v-model="msdsStore.inputForm.msdsNm" v-input type="text" class="w100p radius" id="msdsNm" required oninvalid="return false;" oninput="return false;" :placeholder="t('msds_name_placeholder')" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="h100p df aife">
                                <div class="row flex gutters1rem fg1">
                                    <div class="grid12-6">
                                        <button class="w100p h4-4rem fs1-6rem br4px cFFFFFF bc3248F6 btn active radius" @click="openMsdsDetailPopup" :disabled="!msdsStore.inputForm.casNo">MSDS</button>
                                    </div>
                                    <div class="grid12-6">
                                        <button class="w100p h4-4rem fs1-6rem br4px cFFFFFF bc3248F6" @click="openGSHLink">GSH 링크</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input v-input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(msdsStore.inputForm.createdAt)" readonly />
                            </div>
                        </div>
                        <div class="grid12-1 lg-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="msdsStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="msdsStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 lg-grid12-12">
                            <div class="field h100p df fdc fg1">
                                <label for>{{ t('msds_addImage') }}</label>

                                <!-- 첨부파일 -->
                                <div class="box df aic jcc fg1">
                                    <iFileImage ref="imageFileList" targetType="msds" :targetId="msdsStore.inputForm.fileId" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-9 lg-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-9 us-grid12-12">
                                    <div class="field">
                                        <label for="msdsSynonym">{{ t('msds_synonym') }}</label>
                                        <input v-model="msdsStore.inputForm.msdsSynonym" v-input class="w100p radius" id="msdsSynonym" />
                                    </div>
                                </div>
                                <div class="grid12-3 es-grid12-6">
                                    <div class="field">
                                        <label for="ordSeq">{{ t('array') }}</label>
                                        <input v-model="msdsStore.inputForm.ordSeq" v-input type="number" min="0" class="w100p radius" id="ordSeq" value="99" />
                                    </div>
                                </div>
                                <div class="grid12-5 es-grid12-6">
                                    <div class="field">
                                        <label for="hrNm" required>
                                            <span>{{ t('hrNm') }}</span>
                                        </label>
                                        <i-chips :chips="msdsStore.inputForm.hrList" @popup="addPeople('head')" required></i-chips>
                                    </div>
                                </div>

                                <div class="grid12-2 es-grid12-6">
                                    <div class="field">
                                        <label for="dailyVolume" required>
                                            <span>{{ t('msds_dailyVolume') }}</span>
                                        </label>
                                        <input v-model="msdsStore.inputForm.dailyVolume" v-input.float type="number" min="0.0" class="w100p radius" id="dailyVolume" required oninvalid="return false;" oninput="return false;" />
                                    </div>
                                </div>
                                <div class="grid12-2 es-grid12-6">
                                    <div class="field">
                                        <label for="dailyVolumeUnit">
                                            <span>{{ t('msds_unit') }}</span>
                                        </label>
                                        <i-chips :chips="msdsStore.inputForm.unitVo" @popup="addSystemPopup" @removeChip="removeUnit"></i-chips>
                                    </div>
                                </div>
                                <div class="grid12-2 es-grid12-6">
                                    <div class="field">
                                        <label for="dailyTime" required>
                                            <span>{{ t('msds_dailyTime') }}</span>
                                        </label>
                                        <input v-model="msdsStore.inputForm.dailyTime" v-input.float type="number" min="0" max="24" class="w100p radius" id="dailyTime" required oninvalid="return false;" oninput="return false;" />
                                    </div>
                                </div>
                                <div class="grid12-1 es-grid12-6">
                                    <div class="field">
                                        <label for="dailyVolume">
                                            <span>{{ t('msds_unit') }}</span>
                                        </label>
                                        <input value="시간" class="w100p radius" readonly />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 ul-grid12-12">
                            <div class="field">
                                <label for>{{ t('msds_workplace') }}</label>
                                <i-chips :chips="msdsStore.inputForm.workplaceList" @popup="addWorkplace" @removeChip="removeWorkplace"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-6 ul-grid12-12">
                            <div class="field">
                                <label for>{{ t('msds_work') }}</label>
                                <i-chips :chips="msdsStore.inputForm.workList" @popup="addWork" @removeChip="removeWork"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label for="desc">{{ t('safety_and_health_act') }}</label>
                                <i-chips :chips="msdsStore.inputForm.safetyAndHealthAct"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label for="desc">{{ t('chemical_act') }}</label>
                                <i-chips :chips="msdsStore.inputForm.chemicalAct"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label for="desc">{{ t('dangerous_act') }}</label>
                                <i-chips :chips="msdsStore.inputForm.dangerousAct"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <label for="desc">{{ t('msds_desc') }}</label>
                                <textarea v-model="msdsStore.inputForm.desc" rows="5" class="br4px minh10rem" id="desc" :placeholder="t('msds_desc_placeholder')" />
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="">{{ t('ert_uploadFile') }}</label>
                                <IFileList ref="fileList" targetType="msdsFile" :targetId="msdsStore.inputForm.files" />
                            </div>
                        </div>
                    </div>
                </form>
                <div v-if="msdsStore.inputForm.msdsId !== 'temp'" class="df jcfe sticky t100p rg-t0 rg-b0-5rem tty8px">
                    <button type="button" class="btn line active radius w20rem mr8px" @click="changeMsds('prev')" :disabled="msdsStore.selectedMsdsId <= 0">
                        <svg class="mr1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M5 1L1.20974 4.5286C0.930086 4.78894 0.930086 5.21106 1.20974 5.4714L5 9" stroke="#3248F6" stroke-linecap="round" />
                        </svg>

                        <span>{{ t('msds_btnPrev') }}</span>
                    </button>
                    <button type="button" class="btn line active radius w20rem" @click="changeMsds('next')" :disabled="msdsStore.mergedMsdsByType.length === msdsStore.selectedMsdsId + 1">
                        <span>{{ t('msds_btnNext') }}</span>

                        <svg class="ml1rem" width="6" height="10" viewBox="0 0 6 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M1 9L4.79026 5.4714C5.06991 5.21106 5.06991 4.78894 4.79026 4.5286L1 1" stroke="#3248F6" stroke-linecap="round" />
                        </svg>
                    </button>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 작업 내용 지정 팝업 -->
            <i-PopupDialog ref="workPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectWork :single="false" @selected="selecteWork" :selectedList="msdsStore.inputForm.workList"></selectWork>

                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px active" @click="selectWorkPopup">
                            <span>{{ t('select') }}</span>
                        </button>
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeWorkPopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>

            <!-- 작업장 지정 팝업 -->
            <i-PopupDialog ref="workplacePopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'작업장'" :selectedIdList="msdsStore.inputForm.workplaceIdList" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getWp" @close="closeWorkplacePopup" @apply="applyWorkplacePopup" />
                </div>
            </i-PopupDialog>

            <!-- MSDS 팝업 -->
            <i-PopupDialog ref="addMsdsPopup">
                <div class="contents form ui w70rem md-w100p">
                    <msdsPopup :options="{ label: 'MSDS', readonly: false, type: 'msds' }" @close="closeMsdsPopup" @selected="selectMsdsPopup" />
                </div>
            </i-PopupDialog>

            <!-- MSDS Detail 팝업 -->
            <i-PopupDialog ref="msdsDetailPopup">
                <div class="contents form ui w1024px md-w100p">
                    <msdsDetailPoup ref="msdsInfo" @close="closeMsdsDetailPopup" @selected="selectMsdsPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="msdsStore.inputForm.hrList.map(el => el.hrId)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="systemPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <!-- trainingInstitute -->
                    <base-select-popup :title="'단위'" uniqueKey="unitId" filterKey="unitNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="fetchUnit" :selectedIdList="unitList.map(el => el.id)" @close="closeSystemPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, validationStore, onMounted, onBeforeMount, t, watch, formatDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, getCompId } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useMsdsStore } from '@/stores/safewiz/planning/msds.js';
const msdsStore = useMsdsStore();

import msdsPopup from '@/views/system/base/popup/MsdsPopup.vue';
import msdsDetailPoup from '@/views/safewiz/planning/popup/MsdsDetailPopup.vue';
import selectWork from '@/views/base/work/SelectWork/Index.vue';
import iFileImage from '@/components/file/iFileImage.vue';
import IFileList from '@/components/file/iFileList.vue';

import { getChemList, getChemDetail15, getChemDetail03 } from '@/views/safewiz/planning/api/chemInfo';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

import { getMsdsUnit } from '@/views/safewiz/planning/api/msdsUnitApi';

const selectUserStore = useSelectUserStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSave']
const uButtonList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload']
const imageFileList = ref(); // 이미지 첨부
const fileList = ref(); // 첨부 파일
const componentKey = ref(0);

const unitList = ref([]);

const fetchUnit = async () => {
    const searchParam = {
        compId: getCompId()
    };
    const data = await getMsdsUnit(searchParam, false);
    unitList.value = data.list.filter(item => item.id === msdsStore.inputForm.dailyVolumeUnit);
    return data;
};
const systemPopup = ref(null);
const addSystemPopup = () => {
    systemPopup.value.onOpen();
};

const closeSystemPopup = e => {
    if (e && e[0]) {
        msdsStore.inputForm.unitVo = e;
        msdsStore.inputForm.dailyVolumeUnit = e[0].id;
        unitList.value = e;
    }
    systemPopup.value.onClose();
};

onBeforeMount(async () => {
  if (!msdsStore.inputForm || !Array.isArray(msdsStore.inputForm.unitVo) || msdsStore.inputForm.unitVo[0] == null) {
    msdsStore.inputForm = { ...(msdsStore.inputForm || {}),
      unitVo: [
        {
          id: '',
          nm: ''
        }
      ]
    };
  }
});


onMounted(async () => {
    if (!msdsStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    msdsStore.imageFileInfo(imageFileList);
    if (msdsStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList
    } else {
        layoutStore.useBtnList = uButtonList
    }
    // 파일조회
    imageFileList.value.fnSearch();
    fileList.value.fnSearch();
    msdsStore.headItem = { id: msdsStore.inputForm.headHrId, name: msdsStore.inputForm.headHrNm };
    msdsStore.secondItem = { id: msdsStore.inputForm.secondHrId, name: msdsStore.inputForm.secondHrNm };
    msdsStore.inputForm.stdEq = [{ id: msdsStore.inputForm.stdEqId, name: msdsStore.inputForm.stdEqNm }];
});

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    // msdsId가 있을시 ( 데이터 조회 ) 데이터 set
    if (msdsStore.inputForm.msdsId && msdsStore.inputForm.msdsId !== 'temp') {
        await msdsStore.btnDetail(msdsStore.inputForm.msdsId, true);
        imageFileList.value.fnSearch();
        fileList.value.fnSearch();
        if (msdsStore.inputForm.unitVo[0] == null) {
            msdsStore.inputForm.unitVo = [
                {
                    id: '',
                    nm: ''
                }
            ];
        }
    }
});

btnBack(() => {
    imageFileList.value.fnRemove();
    msdsStore.btnSearch();
    router.go(-1);
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        msdsStore.fileList.editFiles.delete = fileList.value.editFiles.delete;
        msdsStore.fileList.editFiles.insert = fileList.value.editFiles.insert;
        if (msdsStore.inputForm.unitVo.length == 0 || msdsStore.inputForm.unitVo[0].id == '') {
            msdsStore.inputForm.unitVo = null;
        }
        const result = await msdsStore.btnSave();
        if (result) {
            await msdsStore.btnDetail(msdsStore.inputForm.msdsId, true);
            await fileList.value.fnSearch();
            await imageFileList.value.fnSearch();
            layoutStore.useBtnList = uButtonList;
        }
    }
});

btnAdd(() => {
    imageFileList.value.fnRemove();
    msdsStore.btnAdd('temp');
    componentKey.value++;
});

btnDelete(() => {
    msdsStore.btnDelete('D');
});

btnDownload(() => {
    const searchParams = {
        // 검색 키워드
        searchText: msdsStore.inputForm.msdsId,
        // 검색 구분 0: 화학물질명, 1: CAS No.
        searchCd: 1,
        // RowNum
        searchCd2: 10,
        // pageNum
        searchCd3: 1
    };
    msdsStore.btnDownload([searchParams]);
});

const openGSHLink = () => {
    window.open('https://msds.kosha.or.kr/MSDSInfo/kcic/msds/msds.do?page=ghs02', '_blank');
};

// 작업 팝업
import { useSelectWorkStore } from '@/views/base/work/SelectWork/SelectWork';
const selectWorkStore = useSelectWorkStore();
const workPopup = ref(null);
const addWork = () => {
    workPopup.value.onOpen();
};

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

const selectPeople = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();
        msdsStore.inputForm.hrList = [];
        users.forEach(user => {
            msdsStore.inputForm.hrList.push({
                id: user.hrId,
                name: user.hrNm,
                hrId: user.hrId,
                hrNm: user.hrNm,
                orgnNm: user.orgnNm,
                jbrpNm: user.jbrpNm
            });
        });
    }
};

const selectWorkPopup = () => {
    msdsStore.selecteWorkValues = [];
    if (workPopup.value) {
        // 체크상태인 값들을 불러와서 msdsStore.selecteWorkValues 담기
        selectWorkStore.filteredProcWorkList.forEach(el => {
            let selectWorkValue = el.workList.filter(work => work.selected);
            if (selectWorkValue.length > 0) {
                msdsStore.selecteWorkValues = [...msdsStore.selecteWorkValues, ...selectWorkValue];
            }
        });

        msdsStore.selecteWorkValues.forEach(el => {
            el.title = el.processNm;
            el.id = el.prcsWorkId;
            el.nm = el.workContent;
        });
        msdsStore.inputForm.workList = msdsStore.selecteWorkValues;
        msdsStore.inputForm.workIdList = msdsStore.inputForm.workList.map(el => el.prcsWorkId);
        msdsStore.workItems = msdsStore.selecteWorkValues;

        workPopup.value.onClose();
    }
};
const closeWorkPopup = () => {
    if (workPopup.value) {
        workPopup.value.onClose();
    }
};
const selecteWork = e => {
    msdsStore.selecteWorkValues = e;
};
const removeWork = index => {
    msdsStore.inputForm.workIdList.splice(index, 1);
};

// MSDS 이동 버튼 이벤트
const changeMsds = async e => {
    validationStore.clearInvalidClasses();

    imageFileList.value.fnRemove();
    if (e === 'prev') {
        // 이전 작업장
        if (msdsStore.selectedMsdsId > 0) msdsStore.selectedMsdsId--;
    } else if (e === 'next') {
        // 다음 작업장
        if (msdsStore.selectedMsdsId + 1 <= msdsStore.mergedMsdsByType.length) msdsStore.selectedMsdsId++;
    }
    const msdsId = msdsStore.mergedMsdsByType[msdsStore.selectedMsdsId].msdsId;
    await msdsStore.btnDetail(msdsId, true);

    msdsStore.imageFileInfo(imageFileList);

    //파일조회
    imageFileList.value.fnSearch();
    fileList.value.fnSearch();
};

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getWp } from '@/stores/system/base/api/workplaceApi';
// 작업장 팝업
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
    msdsStore.inputForm.workplaceList = data;
    msdsStore.inputForm.workplaceIdList = data.map(el => el.workplaceId);

    data.forEach(el => {
        el.id = el.workplaceId;
        el.nm = el.workplaceNm;
    });
    workplacePopup.value.onClose();
};

// 작업장 요소 제거(x버튼 클릭)
const removeWorkplace = index => {
    msdsStore.inputForm.workplaceIdList.splice(index, 1);
};
const removeUnit = index => {
    msdsStore.inputForm.unitVo.splice(index, 1);
    msdsStore.inputForm.dailyVolumeUnit = '';
};

// msds 팝업
const addMsdsPopup = ref(null);
const addMsds = () => {
    addMsdsPopup.value.onOpen();
};

const removeMsds = () => {
    msdsStore.inputForm.casNo = null;
    msdsStore.inputForm.msdsNm = null;
    msdsStore.inputForm.msdsSynonym = null;
    msdsStore.inputForm.hrList = [];
    msdsStore.inputForm.dailyVolume = null;
    msdsStore.inputForm.unitVo = [];
    msdsStore.inputForm.dailyTime = null;
    msdsStore.inputForm.workList = [];
    msdsStore.inputForm.safetyAndHealthAct = [];
    msdsStore.inputForm.chemicalAct = [];
    msdsStore.inputForm.dangerousAct = [];
    msdsStore.inputForm.files = [];
    msdsStore.inputForm.desc = null;
    imageFileList.value.fnRemove();
};

const closeMsdsPopup = () => {
    addMsdsPopup.value.onClose();
};

const selectMsdsPopup = (chemNameKor, casNo) => {
    msdsStore.inputForm.casNo = casNo;
    msdsStore.inputForm.msdsNm = chemNameKor;
    addMsdsPopup.value.onClose();
};

const msdsDetailPopup = ref(null);

const openMsdsDetailPopup = () => {
    msdsDetailPopup.value.onOpen();
};

const closeMsdsDetailPopup = () => {
    msdsDetailPopup.value.onClose();
};
watch(
    () => msdsStore.inputForm?.cmd,
    async () => {
        if (msdsStore.inputForm?.cmd === 'I') {
            layoutStore.useBtnList = ['btnBack', 'btnSave'];
        }
    }
);

watch(
    () => msdsStore.inputForm?.casNo,
    async () => {
        if (msdsStore.inputForm.casNo == '' || msdsStore.inputForm.casNo == null || msdsStore.inputForm.casNo == undefined) {
            return;
        } else {
            const casNo = msdsStore.inputForm.casNo;
            const searchParams = {
                // 검색 키워드
                searchText: msdsStore.inputForm.casNo,
                // 검색 구분 0: 화학물질명, 1: CAS No.
                searchCd: 1,
                // RowNum
                searchCd2: 10,
                // pageNum
                searchCd3: 1
            };

            const listRes = await getChemList(searchParams, false);
            const chemList = listRes.items.find(x => x.casNo === casNo);
            const chemId = chemList.chemId;
            const detailParams = { searchCd5: chemId };
            getChemDetail15(detailParams, false).then(res => {
                let legal1Data = res.items.find(x => x.msdsItemCode == 'O02').itemDetail; //산업안전보건법에 의한 규제

                let legal2Data = res.items.find(x => x.msdsItemCode == 'O04').itemDetail; //화학물질관리법에 의한 규제

                let legal3Data = res.items.find(x => x.msdsItemCode == 'O06').itemDetail; //위험물안전관리법에 의한 규제

                msdsStore.inputForm.safetyAndHealthAct = legal1Data.split('|').map(str => ({ msdsItemCode: '002', msdsItemNameKor: '산업안전보건법에 의한 규제', itemDetail: str.replace(/\s*\(.*?\)/g, '').trim(), id: str.replace(/\s*\(.*?\)/g, '').trim(), nm: str.replace(/\s*\(.*?\)/g, '').trim() }));
                msdsStore.inputForm.chemicalAct = legal2Data.split('|').map(str => ({ msdsItemCode: '004', msdsItemNameKor: '화학물질관리법에 의한 규제', itemDetail: str.replace(/\s*\(.*?\)/g, '').trim(), id: str.replace(/\s*\(.*?\)/g, '').trim(), nm: str.replace(/\s*\(.*?\)/g, '').trim() }));
                msdsStore.inputForm.dangerousAct = legal3Data.split('|').map(str => ({ msdsItemCode: '006', msdsItemNameKor: '위험물안전관리법에 의한 규제', itemDetail: str.replace(/\s*\(.*?\)/g, '').trim(), id: str.replace(/\s*\(.*?\)/g, '').trim(), nm: str.replace(/\s*\(.*?\)/g, '').trim() }));
            });

            getChemDetail03(detailParams, false).then(res => {
                let synonym = res.items.find(x => x.msdsItemCode == 'C04').itemDetail; //관용명명

                msdsStore.inputForm.msdsSynonym = synonym;
            });
        }
    },
    { immediate: false }
);
</script>
