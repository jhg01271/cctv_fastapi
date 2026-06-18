import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, computed, confirmMsg, alertMsg, formatDate, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, baseDownload } = BaseView();
import router from '@/router';
import _ from 'lodash';

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();

import { getAuditExecutionPlan, deleteAuditExecutionPlan, getAuditExecutionPlanReport } from '@/stores/safewiz/evaluation/api/auditExecutionPlanApi.js';

export const useAuditExecutionPlanStore = defineStore('auditExecutionPlan', () => {
    const writeYear = ref(auditStore.writeYear);
    const dataList = ref([]);
    const filteredData = ref([]);
    const auditDivList = ref([]);
    const inputForm = ref({});
    const initInputForm = () => {
        const today = new Date();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
        const day = today.getDate().toString().padStart(2, '0');          // 01~31
        const writeDt = formatDate(auditStore.writeYear  + month + day)
        inputForm.value = {
            cmd: 'I',
            writeYear: auditStore.writeYear,
            writeDt: writeDt, // 작성일자
            docType: 'AXP', // 작성일자
            auditNm: '', // 심사명
            auditDiv: '', // 심사구분 (C0042)
            auditPurposeScope: '', // 심사 목적 및 범위
            auditRequest: '', // 심사 준비 요청사항
            distribute: '', // 배포처
            useYn: 'Y',
            auditOrgnList: [] // 심사일정
        };
    };
    const btnSearch = async notify => {
        openLoading();
        getAuditExecutionPlan({ writeYear: auditStore.writeYear }, notify)
            .then(res => {
                res.list.forEach(el => {
                    el.writeDt = formatDate(el.writeDt);
                    el.detail = {
                        작성일자: el.writeDt,
                        심사구분: el.auditDivNm,
                        '대상 조직 수': el.auditOrgnList.length
                    };
                });
                dataList.value = _.cloneDeep(res.list);
                filteredData.value = _.cloneDeep(res.list);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const btnDetail = (param, notify) => {
        console.log('@@@ 상세 조회');
        openLoading();
        getAuditExecutionPlan(param, notify)
            .then(res => {
                inputForm.value = res.list[0];
                inputForm.value.writeDt = formatDate(inputForm.value.writeDt);
                inputForm.value.auditOrgnList.forEach(el => {
                    el.auditDt = formatDate(el.auditDt);
                    el.auditTime = formatTime(el.auditStTime, el.auditEdTime);
                });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                clearChecked();
                endLoading();
                router.push('/OhsInternalAuditExecutionPlanDetail');
            });
    };
    const formatTime = (stChar4, edChar4) => {
        if (stChar4 && edChar4) {
            return `${stChar4.substring(0, 2)}:${stChar4.substring(2, 4)} ~ ${edChar4.substring(0, 2)}:${edChar4.substring(2, 4)}`;
        } else return '';
    };
    const clearChecked = () => {
        inputForm.value.auditOrgnList.forEach(el => {
            el.checked = false;
        });
    };
    const btnDelete = list => {
        console.log('@@@ 삭제');
        if (!list.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        if (list.some(el => el.useYn === 'N')) {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }
        let auditNmList = list.map(el => el.auditNm);
        if (auditNmList.length === 1) {
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: list });
        } else {
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: list });
        }
    };
    const deleteApi = list => {
        openLoading();
        deleteAuditExecutionPlan(list, true)
            .then(res => {
                if (res.success) {
                    if (router.currentRoute.value.name === 'OhsInternalAuditExecutionPlanDetail') {
                        // 상세보기일 경우 router 전환
                        router.go(-1);
                    } else {
                        btnSearch();
                    }
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const btnDownload = (list, checkedList= [], msg = 'msgCheckedPrint') => {
        console.log('@@@ 출력');
        if (!list.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        // if (list.some(el => el.useYn === 'N')) {
        //     alertMsg('warning', t('msgDeletedItem'));
        //     return;
        // }
        list = list.map(el => ({
            writeYear: el.writeYear,
            docType: el.docType,
            docNo: el.docNo,
            compId: el.compId
        }));
        confirmMsg('info', t(msg), null, { fun: downloadAction, param: {list:list, checkedList: checkedList} });
    };
    const downloadAction = param => {
      let searchVO = { writeYear: auditStore.writeYear, checkedObjList: param.list, checkedList: param.checkedList }
      baseDownload(getAuditExecutionPlanReport, searchVO)
        // openLoading();
        // getAuditExecutionPlanReport({ writeYear: auditStore.writeYear, checkedObjList: param.list, checkedList:param.checkedList }, false)
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

    return {
        writeYear,
        dataList,
        filteredData,
        auditDivList,
        inputForm,
        initInputForm,
        clearChecked,
        btnSearch,
        btnDetail,
        btnDelete,
        btnDownload
    };
});
