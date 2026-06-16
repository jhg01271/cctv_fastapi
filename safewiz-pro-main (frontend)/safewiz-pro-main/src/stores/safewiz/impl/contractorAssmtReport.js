import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, getFormattedDate } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { getAssmtReportDetail } from './api/contractorAssmtReportApi';
export const useContractorAssmtReportStore = defineStore('contractorAssmtReport', () => {
    const writeYear = ref('')
    const inputForm = ref([]);
    const cmd = ref()
    const initInputForm = () => {
        inputForm.value = []
        cmd.value='I'
        inputForm.value.push({
            cmd: 'I',
            docNo : null,
            docType:'CSHAR',
            writeYear: writeYear,
            useYn: 'Y',
            checkYn : 'Y',
            assmtHrList:[],
            detailDataList:[]
        });
    };
    const btnDetail= async (data)=>{
        writeYear.value = data.writeYear
        inputForm.value = []
        await getAssmtReportDetail({
            writeYear:data.writeYear,
            docType:data.docType,
            partCompId:data.partCompId
        },true).then((res)=>{
            if(res.length !== 0){
                let i = 0
                res.result.forEach(val => {
                    val.checkYn = 'N'
                    val.index = i
                    if(val.assmtHrList == null){
                        val.assmtHrList = []
                    }
                    if(val.index === 0){//맨 첫번째 아코디언은 선택되어 자동으로 열리도록 설정
                        val.checkYn = 'Y'
                    }
                    val.assmtDt = getFormattedDate(val.assmtDt)
                    inputForm.value.push(val)
                    i++
                })
                cmd.value = 'U'
            }else{
                btnAdd()
            }
        })
    }
    const btnAdd = (writeYear) => {
        const today = new Date()
        const mm = String(today.getMonth() + 1).padStart(2, '0')
        const dd = String(today.getDate()).padStart(2, '0')
        inputForm.value.unshift({
            assmtDt : writeYear + mm + dd,
            cmd: 'I',
            docNo : null,
            docType:'CSHAR',
            writeYear: writeYear,
            useYn: 'Y',
            checkYn: 'Y',
            index : inputForm.value.length,
            assmtHrList:[],
            detailDataList:[],
            partCompId: inputForm.value[0].partCompId,
            partCompNm : inputForm.value[0].partCompNm,
        })
    }
    const removePeople = (type, index, chipIndex) => {
        if(type === 'A'){
            console.log('chipIndex',chipIndex)
            inputForm.value[index].assmtHrList.splice(chipIndex,1)
        }else{
            inputForm.value[index].assmtManagerHrId = ''
            inputForm.value[index].assmtManagerHrNm = ''
        }
       
    }
    const removePartner = (index) => {
        inputForm.value[index].partCompId = ''
        inputForm.value[index].partCompNm = ''
    }
    return {
        btnDetail,
        cmd,
        writeYear,
        inputForm,
        // function
        initInputForm,
        btnAdd,
        removePeople,
        removePartner
    };
});
