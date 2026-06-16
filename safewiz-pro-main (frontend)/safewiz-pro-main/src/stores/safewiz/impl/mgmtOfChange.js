import { defineStore } from "pinia";
import { ref } from "vue";
import { getMgmtOfChangeDetail, getMgmtOfChangeReport } from '@/stores/safewiz/impl/api/mgmtOfChangeApi';
import ToastPopup from "@/sdm/components/popup/ToastPopup.vue";
const { baseDownload, getCurrentDate, formatDate } = BaseView();
import BaseView from "@/components/base/BaseView";
const {toastPopup} = BaseView();
import _ from 'lodash';
export const useMgmtOfChangeStore = defineStore("mgmtOfChange", () => {
  const inputForm = ref({});
  const currentInputForm = ref({})
  const writeYear = ref(getCurrentDate().substring(0, 4));
  
  const addContent = () => {
    inputForm.value.contents.push({
      percent: '',
      ordSeq: 0,
      content: ''
    })
  }

  const cmd = ref()
  
  const reqOrgn=ref([])
  const changeOrgn=ref([])
  const init = () => {
    inputForm.value = {
      createdAt: '',
      docType: 'MOC',
      useYn: 'Y',
      regCheckYn:'N',
      cancleCheckYn:'N',
      cmd: 'I',
      writeYear: writeYear.value
    }
    cmd.value = 'I'
  }
  
  const showDetail = async (e, )=>{
    await getMgmtOfChangeDetail(e, false).then((res)=>{
      if(res.success){
        toastPopup('조회성공','조회되었습니다.')
        inputForm.value = res.list;
        currentInputForm.value = _.cloneDeep(res.list);

        currentInputForm.value.reqDt = formatDate(currentInputForm.value.reqDt);
        currentInputForm.value.changeDt = formatDate(currentInputForm.value.changeDt);
        currentInputForm.value.reviewDt = formatDate(currentInputForm.value.reviewDt);
        
        inputForm.value.changeDt = formatDate(res.list.changeDt);
        inputForm.value.reviewDt = formatDate(res.list.reviewDt);
        inputForm.value.reqDt = formatDate(res.list.reqDt);

        cmd.value = 'U'
        reqOrgn.value = [{
            id: res.list.reqOrgnId,
            name: res.list.reqOrgnNm
        }];
        changeOrgn.value = [{
            id: res.list.changeOrgnId,
            name: res.list.changeOrgnNm
        }];

        // 변경 검토자
        inputForm.value.changeHrNm = res.list.changeHrNm || ''; 
    }
      })
  }

  const downloadReport = (param)=>{
    let searchVO = { checkedObjList: param };
    baseDownload(getMgmtOfChangeReport, searchVO)
  //     return new Promise(resolve => {
  //         getMgmtOfChangeReport({checkedObjList:param}).then(res => {
  //             resolve({ result: res.result, success: res.success });
  //             let link = document.createElement('a');
  //             let objectUrl = window.URL.createObjectURL(res.data);
  //             link.href = objectUrl;
  //             link.download = res.headers.filename;
  //             link.click();
  //         });
  // });
  
  }

  return { inputForm,currentInputForm, addContent,  cmd, init,reqOrgn,changeOrgn,showDetail,downloadReport, writeYear }
})