import { defineStore } from "pinia";
const { confirmMsg, alertMsg, openLoading, endLoading, t, ref, getCurrentDate, computed, downloadReport} = BaseView();
import router from '@/router';
import BaseView from "@/components/base/BaseView.js";
import { getOutputList,getOutputReport } from '@/stores/safewiz/notice/api/outputApi.js'

import { useButtonListStore } from '@/stores/buttonList';
const buttonStore = useButtonListStore();

export const useOutputStore = defineStore("output", () => {

  const writeYear = ref(getCurrentDate().substring(0,4))
  const outputList = ref([])

  const getCheckedData = computed(() => {
    // 리스트 중 선택된 항목
    return outputList.value.filter(el=>el.checked);
});

  const btnSearch=(notify)=>{
    openLoading()
    getOutputList({writeYear: writeYear.value}, notify).then(res=>{
      console.log('@@ res',res)
      outputList.value = res.list
      endLoading()
    }).catch(()=>{
      endLoading()
    })
    //
  }
  const btnDownload=()=>{
    let param = getCheckedData.value;
    if(param.length > 0) {
      // 선택한 상태 => 선택만 출력
      param.forEach(el=>{
        el.checkedPrint = true
      })
      console.log('@@@@@@@@@@@ 선택 출력 ' ,param)
      confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadAction, param: param });
    } else {
      let allParam = outputList.value.filter(el=>el.printYn == 'Y')
      allParam.forEach(el=>{
        el.checkedPrint = false
      })
      console.log('@@@@@@@@@@@ 전체출력 ' ,allParam)
      // 선택하지 않은 상태 => 전부 출력
      confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: allParam });
    } 
  }
  const downloadAction = (param) =>{
    console.log('@@param', param)
    param.forEach(el=>{
      el.type = buttonStore.downloadType
    })
    openLoading()
    startTimer()
    getOutputReport(param).then(res=>{
      downloadReport(res)
    }).catch(()=>{
      clearTimer()
      endLoading()
    }).finally(()=>{
      clearTimer()
      endLoading()
    })
  }

  const downloadInterval = ref(null);
  const isPrinting = ref(false);
  const minute = ref(0);
  const second = ref(0);

  const startTimer = () =>{
    isPrinting.value = true
    downloadInterval.value = setInterval(() => {
        second.value++;
        if(second.value >= 60) {
          minute.value++;
          second.value = 0;
        }
    }, 1000);
  }
  const clearTimer = async () =>{
    isPrinting.value = false
    minute.value = 0
    second.value = 0
    await clearInterval(downloadInterval.value);
  }

  return {
    writeYear,outputList,downloadInterval, minute, second, isPrinting, 
    btnSearch,btnDownload,clearTimer
    };
});
