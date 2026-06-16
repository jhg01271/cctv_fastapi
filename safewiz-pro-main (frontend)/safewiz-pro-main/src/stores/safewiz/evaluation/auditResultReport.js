import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, getCurrentDate, baseDownload, formatDate } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { getAuditResultDetail, getAuditResultReport } from './api/auditResultReportApi';

export const useAuditResultReportStore = defineStore('auditResultReport', () => {
    
    const inputForm = ref({})
    const writeYear =ref(getCurrentDate().substring(0, 4));
    const hrList = ref([])
    const cmd = ref()
    const showDetail= async (param)=>{
        cmd.value='U'
        await getAuditResultDetail({
            writeYear:param.writeYear,
            docNo:param.docNo,
            docType:param.docType,
            docSeq:param.docSeq
        }).then((res)=>{
            res.result.auditDt = formatDate(res.result.auditDt);
            inputForm.value = res.result
            if(!inputForm.value.detail){
                inputForm.value.detail=[]
            }
            hrList.value = res.result.auditHrList.map((el)=>{
                return {
                    id:el.hrId,
                    nm:el.hrNm
                }
            })
        })
    }

    function formatTimeRange(startTime, endTime) {
        
        function formatTime(hhmm) {
          const hours = hhmm.slice(0, 2);
          const minutes = hhmm.slice(2, 4);
          return `${hours}:${minutes}`;
        }
        if(startTime!=null && endTime!=null){
            return `${formatTime(startTime)} ~ ${formatTime(endTime)}`;
        }
      }

    const addDetail=()=>{
        
        inputForm.value.detail.push({
            checked:true,
            useYn:'Y',
            cmd:'I'
        })
    }

    const btnDownload = list => {
      let searchVO = { writeYear: writeYear.value, checkedObjList: list }
      baseDownload(getAuditResultReport, searchVO)
        // openLoading()
        // getAuditResultReport({ writeYear: writeYear.value, checkedObjList: list }, false).then(res => {
        //       downloadReport(res);
        // }).finally(()=>{
        //     endLoading()
        // });
    };
    return {
        cmd,
        btnDownload,
        addDetail,
        hrList,
        writeYear,
        inputForm,
        showDetail,
        formatTimeRange
    };
});
