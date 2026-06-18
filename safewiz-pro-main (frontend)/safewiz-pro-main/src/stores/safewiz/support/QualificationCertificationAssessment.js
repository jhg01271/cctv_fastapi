import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
import { getReport, saveEvaluationList, getEvaluationList, getAllEvaluationDetailList } from './api/QualificationCertificationAssessmentApi';
import { watchEffect } from 'vue';
import { useUserInfoStore } from '@/stores/user';

import { useQualificationManagementStore } from './QualificationManagement';
const { openLoading, endLoading, baseDownload, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, getDate, formatDate } = BaseView();
export const useQualificationCertStore = defineStore('QualificationCertStore', () => {
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const inputForm = ref({});
    const columnList = ref([]); //평가항목
    const hrTypeList = ref([
        {
            label: '내부심사원',
            value: '0001'
        },
        {
            label: '내부심사팀장',
            value: '0002'
        }
    ]);
    const docList = ref([]);    //평가기준

    const currentColumn = ref(null);

    const clickColumn = el => {
        currentColumn.value = el;
    };

    const etcCount = computed(() => {
        let filteredList = docList.value.filter(el => {
            return el.itemId == '202501010006' || el.itemId == '202501010007';
        });
        return filteredList.length;
    });

    const filteredDocList = computed(() => {
        return docList.value.filter(el => {
            return el.targetColumn == currentColumn.value.column;
        });
    });

    const docListByParam = param => {
        return docList.value.filter(el => {
            return el.itemId == param;
        });
    };

    const docListByColumn = param => {
        return docList.value.filter(el => {
            return el.column == param;
        });
    }

    const saveColumn = () => {
        docList.value.forEach(item => {
            item.compId = getCompId();
        });
        saveEvaluationList(docList.value, true);
    };

    const initColumn = async () => {
        await getEvaluationList().then(res => {
            let cnt = 0
            let itemId = ''
            res.list.forEach(val => {
                if (val.itemType === 'item') {
                    if (val.itemId !== itemId) {
                        cnt++
                        itemId = val.itemId
                    }
                    val.column = 'column' + cnt
                }if(val.itemId === '202501010006'){
                    val.column = 'columnEtc1'
                    val.type = '0001'
                    val.result = true
                }else if(val.itemId === '202501010007'){
                    val.column = 'columnEtc2'
                    val.type = '0002'
                    val.result = true
                }
            })
            columnList.value = res.list
            currentColumn.value = res.list
        })
        await getAllEvaluationDetailList().then(res => {
            let cnt = 0
            let itemId = ''
            res.list.forEach(val => {
                if (val.itemType === 'item') {
                    if (val.itemId !== itemId) {
                        cnt++
                        itemId = val.itemId
                    }
                    val.column = 'column' + cnt
                }
            })
            docList.value = res.list.filter(item => item.useYn === 'Y');
            let arr1 = []
            if(inputForm.value?.columnEtc1?.length > 0){
                arr1 = inputForm.value?.columnEtc1?.split(',');
            }
            if (arr1) {
                docList.value.forEach(item => {
                    if (item.itemId === '202501010006' && arr1.includes(item.criteriaId)) {
                        item.checked = true;
                    }
                });
                inputForm.value.columnEtc1 = arr1;
            }

            let arr2 = []
            if(inputForm.value?.columnEtc2?.length > 0){
                arr2 = inputForm.value?.columnEtc2?.split(',');
            }
            if (arr2) {
                docList.value.forEach(item => {
                    if (item.itemId === '202501010007' && arr2.includes(item.criteriaId)) {
                        item.checked = true;
                    }
                });
                inputForm.value.columnEtc2 = arr2;
            }
        });
    };

    const removeColumn = seq => {
        docList.value = docList.value.filter(el => {
            return el.docSeq != seq;
        });
    };
    const qualificationManageStore = useQualificationManagementStore();
    const init = () => {
        inputForm.value = {
            hrId: '',
            writeDate: formatDate(qualificationManageStore.writeYear+getDate().replace(/^\d{4}/, "")),
            writeYear: qualificationManageStore.writeYear,
            columnEtc1: [],
            columnEtc2: [],
            remark: '',
            cmd: 'I',
            useYn: 'Y',
            hrType: '0001'
        };
        cmd.value = 'I';
    };
    const userInfoStore = useUserInfoStore();
    const cmd = ref();
    watchEffect(() => {
        if (cmd.value == 'I') {
            inputForm.value.writerId = userInfoStore.userId;
            inputForm.value.writerNm = userInfoStore.userName;
            inputForm.value.writerOrgn = userInfoStore.userOrgnNm;
        }
    });

    const btnDownload = list => {
        let searchVO = { writeYear: writeYear.value, checkedObjList: list };
        baseDownload(getReport, searchVO);
    };

    const signature = ref(null);

    return { cmd, signature, btnDownload, removeColumn, inputForm, columnList, hrTypeList, clickColumn, filteredDocList, docList, currentColumn, saveColumn, initColumn, init, docListByParam, docListByColumn, etcCount };
});
