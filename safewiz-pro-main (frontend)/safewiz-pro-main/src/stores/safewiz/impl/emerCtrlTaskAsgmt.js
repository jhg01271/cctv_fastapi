import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, confirmMsg, ref, t, router, onMounted, getCompId, formatDate, getCurrentDate, baseDownload } = BaseView();

import { getEmergencyControlTaskAsgmtDetail, deleteEmergencyControlTaskAsgmt, getEmergencyControlTaskAsgmtReport } from '@/stores/safewiz/impl/api/emerCtrlTaskAsgmtApi.js';

export const useEmerCtrlTaskAsgmt = defineStore('emerCtrlTaskAsgmt', () => {
    const compId = getCompId();
    const init = ref(true);
    const inputForm = ref({
        chartId: '',
        compId: compId,
        writeYear: '',
        docType: 'ECA',
        useYn: 'Y',
        writeDt: getCurrentDate()
    });
    const initInputForm = (year, readonly = false) => {
        const today = new Date();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
        const day = today.getDate().toString().padStart(2, '0');          // 01~31
        const writeDt = formatDate(year  + month + day)
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            writeYear: year,
            docType: 'ECA',
            chartId: '',
            useYn: 'Y',
            writeDt: writeDt,
            readonly: readonly
        };
    };

    const activeSegments = ref([]);

    // 버튼 이벤트
    const btnDetail = (data, notify = true) => {
        let param = {
            compId: data.compId,
            writeYear: data.writeYear,
            docType: data.docType,
            docNo: data.docNo
        };
        openLoading();
        getEmergencyControlTaskAsgmtDetail(param, notify)
            .then(res => {
                res.list.roleList.forEach(el => {
                    el.hrList = el.hrList.map(user => ({
                        writeYear: el.writeYear,
                        docType: el.docType,
                        docNo: el.docNo,
                        compId: el.compId,
                        id: user.hrId,
                        name: user.hrNm,
                        hrId: user.hrId,
                        hrNm: user.hrNm
                    }));
                });
                inputForm.value = res.list;
                inputForm.value.writeDt = formatDate(inputForm.value.writeDt);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
                router.push('/EmergencyControlTaskAsgmtDetail');
            });
    };

    const btnDelete = list => {
        openLoading();
        deleteEmergencyControlTaskAsgmt(list, true)
            .then(res => {
                router.push('/EmergencyControlTaskAsgmt');
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDownload = (list, msg='msgCheckedPrint') => {
        let param = {
            checkedObjList: list.map(el => ({
                writeYear: el.writeYear,
                docType: el.docType,
                docNo: el.docNo,
                compId: el.compId
            }))
        };
        confirmMsg('info', t(msg), null, { fun: downloadAction, param: param });
    };
    const downloadAction = (param) =>{
      baseDownload(getEmergencyControlTaskAsgmtReport, param)
        // openLoading();
        // getEmergencyControlTaskAsgmtReport(param, false)
        //     .then(res => {
        //         downloadReport(res);
        //     })
        //     .catch(() => {
        //         endLoading();
        //     })
        //     .finally(() => {
        //         endLoading();
        //     });
    }

    return {
        inputForm,
        activeSegments,
        init, 
        // function
        initInputForm,
        // button
        btnDetail,
        btnDelete,
        btnDownload
    };
});
