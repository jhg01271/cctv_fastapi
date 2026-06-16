import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, router, formatDate, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, baseDownload } = BaseView();
import _ from 'lodash';
import { getInvestigationFormList, getFinalUseInspectionType, deleteInvestigationForm, getInvestigationReport, savePassScore } from '@/stores/safewiz/impl/api/contractorInvestigationFormApi.js';

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
import { useTaskStore } from '@/stores/safewiz/task/task.js';
const planningCtrlStore = usePlanningCtrlStore();
const taskStore = useTaskStore();
export const useContractorInvestigationFormStore = defineStore('contractorInvestigationForm', () => {
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const inputForm = ref({});
    const currentInputForm = ref({
        detailList : []
    })
    const chipsItem = ref([]) 
    const initInputForm = () => {
        const today = new Date();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
        const day = today.getDate().toString().padStart(2, '0');          // 01~31
        const regDt = formatDate(planningCtrlStore.searchParam.searchText  + month + day)
        inputForm.value = {
            cmd: 'I',
            writeYear: planningCtrlStore.searchParam.searchText,
            docType: 'CIF',
            useYn : 'Y',
            detailList: finalUseDetailList.value,
            regDt: regDt,
        };
    };
    const partCompList = ref([]); // 중복체크를 위한 업체명 리스트
    const finalUseDetailList = ref([]); // 점검항목 데이터
    // 점검항목 조회(사용여부 Y인 항목 조회)
    const btnSearchInspectionTypeList = () => {
        getFinalUseInspectionType({}, false).then(res => {
            finalUseDetailList.value = res.list;
        });
    };

    const dataList = ref([]);
    const filteredData = ref([]);
    const btnSearch = notify => {
        openLoading();
        getInvestigationFormList({ writeYear: planningCtrlStore.searchParam.searchText, useYn : planningCtrlStore.searchParam.useYn}, notify)
            .then(res => {
                res.list.forEach(el => {
                    (el.examDtTitle = el.examDt ? formatDate(el.examDt) : '-'), (el.investigator1NmTitle = el.investigator1Nm ? el.investigator1Nm : '-'), (el.investigator2NmTitle = el.investigator2Nm ? el.investigator2Nm : '-');
                    el.detail = {
                        [t('cif_examDt')]: el.examDt ? formatDate(el.examDt) : '-',
                        [t('cif_investigator1')]: el.investigator1Nm ? el.investigator1Nm : '-',
                        [t('cif_investigator2')]: el.investigator2Nm ? el.investigator2Nm : '-'
                    };
                });
                dataList.value = res.list;
                filteredData.value = _.cloneDeep(res.list);
                partCompList.value = res.list.map(el => ({
                    docNo: el.docNo,
                    partCompNm: el.partCompNm
                }));
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
        btnSearchInspectionTypeList();
    };
    const fetchInvestigationDetail = data => {
        let param = {
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo
        };
        if (data) {
            param = {
                writeYear: data.writeYear,
                docType: data.docType,
                docNo: data.docNo
            };
        }
        openLoading();
        return getInvestigationFormList(param, true)
            .then(res => {
                inputForm.value = res.list[0];
                currentInputForm.value = _.cloneDeep(res.list[0])
                console.log("쿼런트 인풋 폼 밸류@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",currentInputForm.value)
                inputForm.value.examDt = formatDate(inputForm.value.examDt);
                inputForm.value.regDt = formatDate(inputForm.value.regDt)  //공사와의 거래 품목 데이터 바인딩
                chipsItem.value = [{ id: inputForm.value.partCompItem, name: inputForm.value.partCompItemNm }];
                console.log('# chipsItem.value',chipsItem.value)
            
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
                taskStore.goToPageIfNotCurrent('ContractorInvestigationFormDetail');   
            });
    };
    const btnDelete = list => {
        return deleteApi(list)
    };
    const deleteApi = list => {
        openLoading();
        return deleteInvestigationForm(list, true)
            .then(res => {
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDownload = (list, msg = 'msgCheckedPrint') => {
        if (!list.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        confirmMsg('info', t(msg), null, { fun: downloadAction, param: list });
    };
    const downloadAction = list => {
        list = list.map(el => ({
            writeYear: el.writeYear,
            docType: el.docType,
            docNo: el.docNo,
            compId: el.compId
        }));
        let searchVO = { writeYear: planningCtrlStore.searchParam.searchText, checkedObjList: list };
        baseDownload(getInvestigationReport, searchVO);
        // openLoading();
        // getInvestigationReport({ writeYear: planningCtrlStore.searchParam.searchText, checkedObjList: list }, false)
        //     .then(res => {
        //         downloadReport(res);
        //     })
        //     .catch(() => {
        //         endLoading();
        //     })
        //     .finally(() => {
        //         endLoading();
        //     });
    };
    const btnAddPassScore = () => {
        if(planningCtrlStore.passScore.length >= 3){
            planningCtrlStore.passScore = '100'
        }
        const params = {
            compId : getCompId(),
            writeYear : planningCtrlStore.searchParam.searchText,
            score : planningCtrlStore.passScore
        }
        confirmMsg('warning', t('msgSave'), '', { fun: btnSavePassScore, param: params });
    }
    const btnSavePassScore = (params) => {
        savePassScore(params, true).then(res => {
        })
    }
    return {
        writeYear,
        currentInputForm,
        inputForm,
        dataList,
        filteredData,
        finalUseDetailList,
        partCompList,
        chipsItem,
        // function
        initInputForm,
        // btn
        btnSearchInspectionTypeList,
        btnSearch,
        btnDelete,
        btnDownload,
        btnAddPassScore,
        // 상세화면 데이터를 가져온다
        fetchInvestigationDetail
    };
});
