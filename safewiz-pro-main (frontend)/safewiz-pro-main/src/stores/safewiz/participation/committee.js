import { defineStore } from 'pinia';
import { ref } from 'vue';
import router from '@/router';
import { getParticipationDetail, getReport, getOhsReport } from './api/participation';
import BaseView from '@/components/base/BaseView';
const { getCurrentDate, openLoading, endLoading, baseDownload } = BaseView();

import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
export const useCommitteeStore = defineStore('committee', () => {
    const inputForm = ref({
        commDt: '',
        commAt: '',
        createdAt: '',
        contents: [
            {
                percent: '',
                ordSeq: 0,
                content: ''
            }
        ]
    });

    const addContent = () => {
        inputForm.value.contents.push({
            percent: '',
            ordSeq: 0,
            content: ''
        });
    };

    const writeYear = ref(getCurrentDate().substring(0, 4));

    const toastPopupStore = useToastPopupStore();
    const refresh = async (item, notify = true) => {
        return new Promise((resolve, reject) => {
            const param = {
                writeYear: item.writeYear,
                docType: item.docType,
                docNo: item.docNo
            };
            getParticipationDetail(param, notify).then(res => {
                inputForm.value = res.list;
                resolve(res);
            });
        });
    };

    const showDetail = async (item, param) => {
        cmd.value = 'U';
        // await refresh(item).then(res => {
          inputForm.value = item
            // if (param == 'O') {
            //     router.push('/ohsCommitteeDetail');
            // } else if (param == 'P') router.push('/partnerCommitteeDetail');
        // });
    };

    const cmd = ref();

    const init = type => {
        inputForm.value = {
            companyMember: [],
            employeeMember: [],
            partnerMember: [],
            commDt: getCurrentDate(),
            commAt: '',
            createdAt: '',
            docType: type,
            useYn: 'Y',

            contents: [
                {
                    percent: 0,
                    content: ''
                }
            ],
            cmd: 'I'
        };
        cmd.value = 'I';
    };
    const downloadPartReport = data => {
        let param = {
            writeYear: writeYear.value,
            checkedObjList: data
        };
        // openLoading();
        //   getReport(inputForm.value, false).then(res => {

        //     downloadReport(res);
        // }).catch(() => {
        //   endLoading();
        // }).finally(() => {
        //   endLoading();
        // });
        baseDownload(getReport, param);
    };
    const downloadOhsReport = data => {
        let param = {
            writeYear: writeYear.value,
            checkedObjList: data
        };
        // openLoading();
        //   getOhsReport(inputForm.value, false).then(res => {
        //     downloadReport(res);
        //   }).catch(() => {
        //     endLoading();
        //   }).finally(() => {
        //     endLoading();
        //   });
        baseDownload(getOhsReport, param);
    };
    return { refresh, inputForm, addContent, showDetail, cmd, init, downloadPartReport, downloadOhsReport, writeYear };
});
