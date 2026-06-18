import { defineStore } from "pinia";
import { ref } from "vue";
import router from '@/router';
import { getSafetyDutiesDocDetail, getReport } from "./api/safetyDutiesDocApi";
import BaseView from "@/components/base/BaseView";
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
export const useSafetyDutiesDocStore = defineStore("safetyDutiesDoc", () => {
  const { getCurrentDate, openLoading, endLoading, baseDownload } = BaseView();
  const inputForm = ref({
    commDt: '',
    commAt: '',
    createdAt: '',
    contents: [{
      percent: '',
      ordSeq: 0,
      content: ''
    }]
  })

  const addContent = () => {
    inputForm.value.contents.push({
      percent: '',
      ordSeq: 0,
      content: ''
    })
  }
  
  const toastPopupStore =  useToastPopupStore();

  const refresh = async (item, notify = true)=>{
    await getSafetyDutiesDocDetail(item,notify).then((res) => {
      inputForm.value = res.list
      if(inputForm.value.memberList){
          inputForm.value.cardMemberList = inputForm.value.memberList
          inputForm.value.memberList.map(async (user) => {
            user.name = user.hrNm
            user.id = user.hrId
            user.fileId = user.logoId
            user.checked = false
            user.appointmentDt = user.appointmentDt != null ? user.appointmentDt : ""
            user.dismissalDt = user.dismissalDt != null ? user.dismissalDt : ""
            user.useYn = user.useYn
            user.cmd = 'U'
            return user; // 로드된 이미지를 포함한 user 반환
          })
      }else{
        inputForm.value.memberList=[];
      }
    })
  }
  

  const showDetail = async (item, param) => {
    cmd.value = 'U'
    await refresh(item)
    router.push('/safetyDutiesDesigDocumentDetail')
  
  }

  const cmd = ref()

  const init = () => {
    inputForm.value = {
      writeYear : new Date().getFullYear(),
      writeDate : getCurrentDate(),
      memberList:[],
      createdAt: '',
      docType: 'SDA',
      useYn: 'Y',
      cmd: 'I'
    }
    cmd.value = 'I'
    router.push('/safetyDutiesDesigDocumentDetail')
  }
  
  const writeYear = ref(getCurrentDate().substring(0,4))
  const btnDownload = (param) => {
    let searchVO = { writeYear: writeYear.value, checkedObjList: param }
    baseDownload(getReport, searchVO)
        // openLoading();
        // getReport({ writeYear: writeYear.value, checkedObjList: param }).then(res => {
        //   downloadReport(res);
        // }).catch(() => {
        //   endLoading();
        // }).finally(() => {
        //   endLoading();
        // });
  }
  return {refresh, inputForm, addContent, showDetail, cmd, init, btnDownload }
})