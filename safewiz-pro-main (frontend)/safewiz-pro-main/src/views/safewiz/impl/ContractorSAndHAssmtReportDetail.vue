<template>
    <div id="form" class="contents ui form">
        <OverlayScrollbarsComponent
            class="box h100p pa2-2rem"
            ref="overlayScrollbars"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <form ref="myForm">
                <div class="pb1rem grid12-2 lg-grid12-12">
                    <input v-input type="text" year v-calendar="'yyyy'" class="datepicker w100p radius" v-model="contractorAssmtReportStore.writeYear" readonly="true" />
                </div>
                <div class="oh">
                    <div class="accordion left df fdc rg8px">
                        <div class="list" v-for="(item, index) in contractorAssmtReportStore.inputForm" :key="item.index" @click="clickAccordion(index)">
                            <button type="button" class="df jcsb aic" :id="`${item.index}`" @click="toggleAccordion">
                                <span>
                                    <div class="df left" v-if="item.cmd === 'U'">
                                        <input type="checkbox" class="mr1rem shrink0" v-input v-model="item.checkYn" true-value="Y" false-value="N" />
                                        <em v-if="item.useYn === 'Y'" class="ellipsis">{{ item.assmtDt }}</em>
                                        <em v-else-if="item.useYn === 'N'" class="ellipsis">{{ item.assmtDt }} - (미사용)</em>
                                    </div>
                                    <div class="df left" v-else>
                                        <input type="checkbox" class="mr1rem" v-input v-model="item.checkYn" true-value="Y" false-value="N" />
                                        <em class="ellipsis">{{ getFormattedDate(item.assmtDt) }}</em>
                                    </div>
                                </span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh bcf8f9fb">
                                <div :id="item.checkYn ? 'form' : null">
                                    <div class="pa2-2rem">
                                        <ISignature ref="signatureComponent" :cmd="item.cmd" :types="signatureType" targetType="CSHAR" :writeYear="item.writeYear" :docNo="item.docNo" :docSeq="item.partCompId" />
                                    </div>
                                    <hr />
                                    <div class="pa2-2rem">
                                        <div class="row flex gutters1rem">
                                            <div class="grid12-6 sm-grid12-12">
                                                <div class="field">
                                                    <label required>
                                                        <span>심사업체</span>
                                                    </label>
                                                    <i-chips required :chips="[{ id: item.partCompId, nm: item.partCompNm }]" @popup="addPartner" @removeChip="removePartner(index)"></i-chips>
                                                </div>
                                            </div>

                                            <div class="grid12-3 sm-grid12-6">
                                                <div class="field">
                                                    <label :for="'assmtDt' + index" required>
                                                        <span>심사일자</span>
                                                    </label>
                                                    <input :id="'assmtDt' + index" required class="br4px datepicker" :min="contractorAssmtReportStore.writeYear + '.01.01'" :max="contractorAssmtReportStore.writeYear + '.12.31'" v-calendar="getDateFormat()" v-input type="text" :value="getFormattedDate(item.assmtDt)" @input="onDateInput(index, $event)" placeholder="2024.10.25" />
                                                </div>
                                            </div>
                                            <div class="grid12-3 sm-grid12-6">
                                                <div class="field">
                                                    <label for="useYn">
                                                        <span>사용여부</span>
                                                    </label>
                                                    <div class="df aic h4-4rem">
                                                        <input :true-value="'Y'" :false-value="'N'" :checked="item.useYn == 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="item.useYn" @change="item.checkYn = 'Y'" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="grid12-6">
                                                <div class="field">
                                                    <label for>심사자</label>
                                                    <i-chips :chips="item.assmtHrList.map(el => ({ id: el.hrId, nm: el.hrNm }))" @popup="addPeople('A')" @removeChip="removePeople('A', index, $event)" @change="item.checkYn = 'Y'"></i-chips>
                                                </div>
                                            </div>

                                            <div class="grid12-6">
                                                <div class="field">
                                                    <label for>심사책임자</label>
                                                    <i-chips :chips="[{ id: item.assmtManagerHrId, nm: item.assmtManagerHrNm }]" @popup="addPeople('M')" @removeChip="removePeople('M', index)" @change="item.checkYn = 'Y'"></i-chips>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>심사내용</label>
                                                    <textarea class="br4px minh10rem" v-model="item.assmtContent" @input="item.checkYn = 'Y'"></textarea>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>심사점수</label>
                                                    <div class="df gap1rem us-fww">
                                                        <input class="br4px" v-input type="text" :value="item.point" disabled @input="item.checkYn = 'Y'" />
                                                        <button type="button" class="w20rem btn radius active us-flex1-1-48p" @click="evaluation(index)">평가하기</button>
                                                        <button type="button" class="w30rem btn radius active us-flex1-1-48p" @click="evaluationManage">평가항목 관리</button>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>심사책임자 총평</label>
                                                    <textarea class="br4px minh10rem" v-model="item.remark" @input="item.checkYn = 'Y'"></textarea>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>개선이 필요한 부분</label>
                                                    <textarea class="br4px minh10rem" v-model="item.insufficientContent" @input="item.checkYn = 'Y'"></textarea>
                                                </div>
                                            </div>

                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>우수한 부분</label>
                                                    <textarea class="br4px minh10rem" v-model="item.superiorContent" @input="item.checkYn = 'Y'"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </OverlayScrollbarsComponent>
        <!-- 인원 검색 팝업 컴포넌트 시작 (단일선택) -->
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="peoplePopupMulty">
                <div class="contents w500px md-w100p">
                    <selectUser @selected="selectPeopleMulty" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
            <!-- 🐉 평가항목 관리 팝업 -->
            <i-PopupDialog ref="evaluationManagePopup">
                <!-- 단일 그리드 -->
                <div class="contents w1200px md-w100p">
                    <DataSetManageByItemPopup ref="managePopup" detailType="innerCard" :detailDivideColLastIndex="1" detailDivideColTitle="판정구분" title="평가항목 관리" :masterTitle="'평가항목'" :masterReadonly="false" :masterApi="getEvaluationType" :masterApiParam="{}" masterKey="evalTypeId" masterDisplayKey="evalTypeNm" additionalDisplayKey="weight" :detailApi="getEvaluationTypeDetail" detailKey="evalTypeId" :detailOption="detailOption" @sample="sampleEvaluationManagePopup" @delete="deleteEvaluationManagePopup" @save="saveEvaluationManagePopup" @close="closeEvaluationManagePopup">
                        <template #warningMsg>
                            <label class="pl0-5rem mt1-6rem cFF3534 es-ml0 es-neg-ls0-5px">* 평가항목별 가중치를 수정하여 100으로 맞추세요. </label>
                        </template>
                    </DataSetManageByItemPopup>
                    <!-- <EvaluationManage @close="closeEvaluationManagePopup"></EvaluationManage> -->
                </div>
            </i-PopupDialog>
            <!-- 평가항목 관리 예시 불러오기 팝업-->
            <i-PopupDialog ref="samplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponent" :title="'평가항목 예시'" filterKey="evalTypeNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getEvaluationTypeDataset" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
            <!-- 🐍 평가하기 팝업 -->
            <i-PopupDialog ref="evaluationPopup">
                <!-- 단일 그리드 -->
                <div class="contents maxw1200px md-w100p">
                    <Evaluation @close="closeEvaluationPopup" @save="saveEvaluationInfo"></Evaluation>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="partnerPopup">
                <!-- 협력사 업체 선택 팝업 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'업체'" filterKey="partCompNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="searchPartner" @close="closePartner" />
                </div>
            </i-PopupDialog>
        </teleport>
        <!-- 인원 검색 팝업 컴포넌트 끝  -->
    </div>
</template>

<script setup>
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { ref, onMounted, watchEffect } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import ISignature from '@/components/common/iSignature.vue';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';
// import router from '@/router';

import { getPartner } from '@/stores/system/base/api/partnerApi.js';
import { useContractorAssmtReportStore } from '@/stores/safewiz/impl/contractorAssmtReport';
const contractorAssmtReportStore = useContractorAssmtReportStore();
import { useProcurementStore } from '@/stores/safewiz/impl/procurement.js';
const procurementStore = useProcurementStore();
import { gsap } from 'gsap';
// =====================================================
import { getPartnerDetail } from '@/stores/system/base/api/partnerApi';

const searchPartner = async () => {
    let filterData = {
        list: []
    };
    await getPartner().then(res => {
        res.list.forEach(val => {
            if (val.regDt.slice(0, 4) <= contractorAssmtReportStore.writeYear) {
                filterData.list.push(val);
            }
        });
    });
    return filterData;
};
const signatureType = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const { validationStore, router, t, confirmMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, getFormattedDate, baseDownload, nextTick, alertMsg, formatDateForDB, getDuplicatedData, openLoading, endLoading, setRouterParam /* formatDate, watch, calculateMinutes, toastPopup, getCompId */ } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnAdd', 'btnSave'];

// =====================================================

btnDelete(() => {
    confirmMsg('info', t('msgCheckedDelete'), '', { fun: deleteAction });
});

const deleteAction = () => {
    const deleteParams = [];
    contractorAssmtReportStore.inputForm.forEach(val => {
        if (val.checkYn === 'Y') {
            deleteParams.push({
                writeYear: val.writeYear,
                docType: val.docType,
                docNo: val.docNo
            });
        }
    });
    deleteAssmtReport(deleteParams).then(() => {
        contractorAssmtReportStore.btnDetail(contractorAssmtReportStore.inputForm[0]);
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        activeAnimation();
    });
};

///심사업체

const partnerPopup = ref(null);
const addPartner = () => {
    partnerPopup.value.onOpen();
};

// 설비 요소 제거(x버튼 클릭)
const removePartner = index => {
    contractorAssmtReportStore.removePartner(index);
    contractorAssmtReportStore.inputForm[index].checkYn = 'Y';
};

const closePartner = e => {
    if (partnerPopup.value) {
        if (e && e.length) {
            getPartnerDetail(e[0].partCompId, false).then(res => {
                contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].partCompId = res.list.partCompId;
                contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].partCompNm = res.list.partCompNm;
                contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].checkYn = 'Y';
            });
        }
        partnerPopup.value.onClose();
    }
};

const saveEvaluationInfo = data => {
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].detailDataList = data;
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].point = 0;
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].superiorContent = '';
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].insufficientContent = '';
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].checkYn = 'Y';
    data.forEach(el => {
        el.detailDataList.forEach(item => {
            contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].point += Number(item.point ?? 0);
            if (Number(item.point ?? 0) / item.weight >= 0.9) {
                contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].superiorContent += `${item.evalDetailNm}\n`;
            } else if (Number(item.point ?? 0) / item.weight <= 0.6) {
                contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].insufficientContent += `${item.evalDetailNm}\n`;
            }
        });
    });
};

const signatureComponent = ref();

watchEffect(() => {
    if (contractorAssmtReportStore.cmd == 'U') {
        layoutStore.useBtnList = uButtonList;
    } else {
        layoutStore.useBtnList = iButtonList;
    }
});

btnSave(async () => {
    if (!contractorAssmtReportStore.inputForm.some(item => item.checkYn === 'Y')) {
        alertMsg('warning', '저장할 데이터를 선택해주세요.');
        return;
    }

    const selectedDatas = contractorAssmtReportStore.inputForm.filter(item => item.checkYn === 'Y'); // 저장하려는 데이터
    const existDateList = contractorAssmtReportStore.inputForm.filter(item => item.checkYn != 'Y' && item.cmd === 'U'); // 기존에 저장된 데이터
    // existDateList의 assmtDt 값들을 Set으로 추출
    const existDates = new Set(existDateList.map(item => formatDateForDB(item.assmtDt)));

    // selectedDatas 중 하나라도 겹치는 게 있으면 true
    const hasDuplicate = selectedDatas.some(item => existDates.has(item.assmtDt));
    const notDuplicationData = new Set(selectedDatas.map(item => item.assmtDt)); //assmtDt를 DISTINCT한 데이터
    if (hasDuplicate || notDuplicationData.size !== selectedDatas.length) {
        // 선택된 데이터끼리 날짜가 중복되는 경우
        alertMsg('warning', '날짜당 하나의 데이터만 등록할 수 있습니다.');
        return;
    }

    const message = `저장 하시겠습니까?`;
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', message, '', { fun: saveAction });
    }
});
const saveAction = () => {
    let saveParams = [];
    contractorAssmtReportStore.inputForm.forEach(val => {
        if (val.checkYn === 'Y') {
            val.assmtDt = formatDateForDB(val.assmtDt);
            saveParams.push(val);
        }
    });

    // openLoading();
    saveAssmtReport(saveParams)
        .then(async res => {
            activeAnimation();
            // 데이터를 unshift로 가공하기 때문에 서명은 반대로 저장
            signatureComponent.value = signatureComponent.value.slice().reverse();
            for (let i = 0; i < res.result.length; i++) {
                await signatureComponent.value[i].setApprovalInfo('CSHAR', res.result[i].writeYear, res.result[i].docNo, res.result[i].partCompId);
            }

            await contractorAssmtReportStore.btnDetail(res.result[0]);
            contractorAssmtReportStore.inputForm[0].checkYn = 'N'; // 아코디언 애니메이션 실행 후, checkYn 'N'
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};

btnAdd(async () => {
    await contractorAssmtReportStore.btnAdd(contractorAssmtReportStore.writeYear);
    validationStore.clearAllErrors();
    validationStore.updateErrorPositions();
    activeAnimation();
});

btnDownload(() => {
    const printParams = [];
    let saveDataCheck = true;
    let msgPrint = '';
    contractorAssmtReportStore.inputForm.forEach(val => {
        if (val.checkYn === 'Y' && val.cmd === 'U') {
            val.printAll = false;
            printParams.push(val);
        } else if (val.checkYn === 'Y' && val.docNo == null) {
            saveDataCheck = false;
        }
    });
    if (printParams.length === 0) {
        //아무것도 체크하지 않고 리포트 출력 시, 모든 데이터 출력
        contractorAssmtReportStore.inputForm.forEach(val => {
            val.printAll = true;
            if (val.docNo == null) {
                saveDataCheck = false;
            }
            if (printParams.length === 0) {
                //모든 데이터 출력은 compId만 있으면 되기때문에 1개의 값만 삽입
                printParams.push(val);
            }
        });
        msgPrint = '출력하시겠습니까?';
    } else {
        msgPrint = '선택한 항목을 출력하시겠습니까?';
    }
    if (saveDataCheck) {
        confirmMsg('info', msgPrint, null, { fun: btnDownloadConfirm, param: printParams });
    } else {
        alertMsg('warning', '저장되지 않은 데이터는 출력할 수 없습니다.');
        return;
    }
});

const btnDownloadConfirm = printParams => {
    let searchVO = { checkedObjList: printParams };
    baseDownload(getReport, searchVO);
};

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();
const peoplePopup = ref(null); // PopupDialog에 대한 ref
const peoplePopupMulty = ref(null);
const addPeople = type => {
    if (type === 'M') {
        peoplePopup.value.onOpen();
    } else {
        peoplePopupMulty.value.onOpen();
    }
};
const removePeople = (type, index, chipIndex) => {
    contractorAssmtReportStore.removePeople(type, index, chipIndex);
    contractorAssmtReportStore.inputForm[index].checkYn = 'Y';
};

btnSearch(async () => {
    await contractorAssmtReportStore.btnDetail(contractorAssmtReportStore.inputForm[0]);
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    activeAnimation();
});

//심사책임자 선택 이벤트
const selectPeople = param => {
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].assmtManagerHrId = param.hrId;
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].assmtManagerHrNm = param.hrNm;
    closePeoplePopup();
};

//심사자 선택 이벤트
const selectPeopleMulty = () => {
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].assmtHrList = [];
    let data = selectUserStore.getSelectedUser();
    // data.forEach(item => {
    //     (item.id = item.hrId), (item.nm = item.hrNm);
    // });
    contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].assmtHrList.push(...data);
    closePeoplePopup();
};

const closePeoplePopup = () => {
    peoplePopup.value.onClose();
    peoplePopupMulty.value.onClose();
};

// --- 평가항목 관리 팝업 --- //
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
import { getEvaluationType, getEvaluationTypeDetail, getEvaluationTypeDataset, saveEvaluationType, deleteEvaluationType } from '@/stores/safewiz/impl/api/contractorAssmtReportApi';

import BaseSelectComponent from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
const samplePopup = ref(null);
const managePopup = ref(null);
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(managePopup.value.masterDataList, data, 'evalTypeId', 'evalTypeNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
        el.weight = el.detailDataList.reduce((acc, cur) => acc + (Number(cur.weight) || 0), 0);
        el.detailDataList = el.detailDataList.map(detail => ({
            ...detail,
            checked: true,
            cmd: 'I'
        }));
    });
    managePopup.value.setMasterSampleData(dataList);

    samplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    samplePopup.value.onClose();
};

const evaluationManagePopup = ref(null);
const detailOption = ref([
    {
        fieldDisplayKey: 'evalDetailNm',
        fieldNm: '세부항목',
        fieldKey: 'evalDetailId',
        class: 'grid10-4 us-grid10-10',
        type: 'text',
        required: true
    },
    {
        fieldDisplayKey: 'weight',
        fieldNm: '가중치',
        fieldKey: 'weight',
        class: 'grid10-2 us-grid10-10',
        type: 'number',
        required: true
    },
    {
        fieldDisplayKey: '',
        fieldNm: '',
        fieldKey: '',
        class: 'grid10-6 us-grid10-4',
        type: 'number',
        required: false
    },
    {
        fieldDisplayKey: 'divA',
        fieldNm: 'A(5)',
        fieldKey: 'divA',
        class: 'grid10-2 sm-grid10-10',
        type: 'text',
        required: false
    },
    {
        fieldDisplayKey: 'divB',
        fieldNm: 'B(4)',
        fieldKey: 'divB',
        class: 'grid10-2 sm-grid10-10',
        type: 'text',
        required: false
    },
    {
        fieldDisplayKey: 'divC',
        fieldNm: 'C(3)',
        fieldKey: 'divA',
        class: 'grid10-2 sm-grid10-10',
        type: 'text',
        required: false
    },
    {
        fieldDisplayKey: 'divD',
        fieldNm: 'D(2)',
        fieldKey: 'divD',
        class: 'grid10-2 sm-grid10-10',
        type: 'text',
        required: false
    },
    {
        fieldDisplayKey: 'divE',
        fieldNm: 'E(1)',
        fieldKey: 'divE',
        class: 'grid10-2 sm-grid10-10',
        type: 'text',
        required: false
    }
]); // 우측 카드 에디팅 옵션 데이터

//평가항목 관리 팝업 열기
const evaluationManage = () => {
    evaluationManagePopup.value.onOpen();
};

// 평가항목 관리 예시 불러오기
const sampleEvaluationManagePopup = () => {
    samplePopup.value.onOpen();
};

// 평가항목 관리 저장
const saveEvaluationManagePopup = data => {
    const sum = managePopup.value.masterDataList.filter(el => el.useYn === 'Y').reduce((acc, cur) => acc + (Number(cur.weight) || 0), 0);
    if (sum != 100) {
        confirmMsg('warning', '가중치 합계가 100이 아닙니다. \n 그래도 계속하시겠습니까?', '', { fun: saveEvaluationManagePopupAction, param: data });
    } else {
        saveEvaluationManagePopupAction(data);
    }
};
const saveEvaluationManagePopupAction = data => {
    openLoading();
    saveEvaluationType(data, true)
        .then(res => {
            managePopup.value.searchMaster(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 평가항목 관리 삭제
const deleteEvaluationManagePopup = data => {
    deleteEvaluationType(data, true)
        .then(res => {
            managePopup.value.searchMaster(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

//평가항목 관리 팝업 닫기
const closeEvaluationManagePopup = () => {
    evaluationManagePopup.value.onClose();
};

import Evaluation from './EvaluationPopup.vue';
import { deleteAssmtReport, getReport, saveAssmtReport } from '@/stores/safewiz/impl/api/contractorAssmtReportApi';
const evaluationPopup = ref();

//평가하기 팝업 열기
const evaluation = index => {
    contractorAssmtReportStore.selectedIndex = index;
    evaluationPopup.value.onOpen();
};

//평가하기 팝업 닫기
const closeEvaluationPopup = () => {
    evaluationPopup.value.onClose();
};
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack, param: '' });
});
const goBack = async () => {
    await router.push('/Procurement');
    await procurementStore.tabChanged('contractorSAndHAssmt');
};

const onDateInput = (index, event) => {
    contractorAssmtReportStore.inputForm[index].assmtDt = event.target.value;
    contractorAssmtReportStore.inputForm[index].checkYn = 'Y';
};

/******************************
 * 아코디언 관련
 ******************************/

const clickAccordion = index => {
    contractorAssmtReportStore.selectedIndex = index;
};

// 개별 아코디언 토글 함수
const toggleAccordion = async (event, fieldId) => {
    if (event.target.tagName !== 'INPUT' && event.target.tagName !== 'LABEL') {
        //체크박스를 클릭했을 때는 아코디언 애니메이션 실행 x)
        let button = null;
        if (fieldId != null) {
            button = document.getElementById(`${fieldId}`);
        } else {
            button = event.currentTarget;
        }
        // const button = event.currentTarget;
        const segmentElement = button.nextElementSibling;
        const isOpen = button.classList.toggle('active');
        await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
        animateAccordion(segmentElement, isOpen);
    }
};

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

//추가버튼 클릭, 최초 조회시 아코디언 애니메이션
const activeAnimation = () => {
    contractorAssmtReportStore.inputForm.forEach(val => {
        if (val.checkYn === 'Y') {
            const accordion = document.getElementById(val.index); //1.선택된 아코디언의 <div>를 검색 => 결과 : <button type="button" class="df jcsb aic" :id="`${item.index}`" @click="toggleAccordion"> 부분
            const container = accordion.parentNode; //2.해당 태그의 부모 노드를 조회함. 부모 노드는 <div class="list" v-for="(item, index) in contractorAssmtReportStore.inputForm" :key="item.index" @click="clickAccordion(index)">임
            const segment = container.querySelector('.segment.oh'); //3.부모 태그 안에서 segment, oh 클래스를 가진 태그를 조회함
            const isOpen = accordion.classList.toggle('active'); //4.해당 태그에 active 클래스가 있으면 제거, 없으면 추가
            const alreadyOpen = accordion.classList.toggle('expanded'); //5.해당 태그에 expanded 클래스가 있으면 제거, 없으면 추가
            if (alreadyOpen === true) {
                //6.새로 추가된 데이터는 alreadyOpen True 그 이후 한 번 더 추가되면 false가 되어서 애니메이션 실행 x, 그런데 그 후에 다시 true가 되면서 아코디언이 접혀야될 것 같은데 접히지 않음. 이해불가
                animateAccordion(segment, isOpen);
            }
        }
    });
};

onMounted(async () => {
    activeAnimation();
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (contractorAssmtReportStore.cmd === 'U') {
        //아코디언 오픈 후 checkYn 'N' 처리
        contractorAssmtReportStore.inputForm[0].checkYn = 'N';
        contractorAssmtReportStore.selectedIndex = 0;
    }
    if (param) {
        //업무분장으로 넘어왔을 경우
        param.partCompId = param.docSeq;
        contractorAssmtReportStore.btnDetail(param);
    } else if (!contractorAssmtReportStore.cmd) {
        router.push('/Procurement');
    }
});
</script>

<style lang="scss" scoped>
table {
    thead {
        tr {
            th {
                span.required {
                    position: relative;
                    &::after {
                        position: absolute;
                        display: inline-block;
                        content: '*';
                        right: -1.3rem;
                        top: 0;
                        color: #ff3534;
                        line-height: 1;
                    }
                }
            }
        }
    }
}
</style>
