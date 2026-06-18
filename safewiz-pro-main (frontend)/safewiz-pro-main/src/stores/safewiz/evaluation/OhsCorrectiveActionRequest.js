import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { ref, getCurrentDate, baseDownload, formatDate } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { getCorrectiveRequestReport,getCorrectiveRequestDetail } from '@/stores/safewiz/evaluation/api/OhsCorrectiveActionRequestApi';
import { nextTick } from 'vue';
import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();
export const useCorrectiveRequestStore = defineStore('correctiveRequest', () => {
    
    const inputForm = ref({})
    const writeYear =ref(auditStore.writeYear);
    const hrList = ref([])

    const cmd = ref()
    const showDetail= async (param)=>{
        cmd.value='U'
        await getCorrectiveRequestDetail({
            writeYear:param.writeYear,
            docNo:param.docNo,
            docType:param.docType,
            docSeq:param.docSeq,
            docDetailSeq:param.docDetailSeq ? param.docDetailSeq : param.docDetail
        },true).then((res)=>{
            // inputForm.value = res.result
            res.result.writeDt = formatDate(res.result.writeDt);
            res.result.finalCheckDt = formatDate(res.result.finalCheckDt);
            res.result.reqDt = formatDate(res.result.reqDt);

            Object.assign(inputForm.value, res.result);
            hrList.value = res.result.auditHrList.map((el)=>{
                return {
                    id:el.hrId,
                    nm:el.hrNm
                }
            })
        })
    }

    const addDetail=(writeYear)=>{
        cmd.value='I'
        const today = new Date();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
        const day = today.getDate().toString().padStart(2, '0');          // 01~31
        const writeDt = formatDate(writeYear  + month + day)
        inputForm.value={
            writeYear:writeYear,
            docNo:'',
            docType:'OCAR',
            docSeq:'',
            checked:true,
            writeDt: writeDt,
            useYn:'Y',
            cmd:'I',
            validate:'D',
            required:'D'
        }
    }

    const btnDownload = list => {
      let searchVO = { writeYear: writeYear.value, checkedObjList: list }
      baseDownload(getCorrectiveRequestReport, searchVO)
        // getCorrectiveRequestReport({ writeYear: writeYear.value, checkedObjList: list }, false).then(res => {
        //       downloadReport(res);
        //   });
    };

    return {
        cmd,
        btnDownload,
        addDetail,
        hrList,
        writeYear,
        inputForm,
        showDetail,
    };
});
