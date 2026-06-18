import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading,formatDate, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import _ from 'lodash'
// import { deleteTraining } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';

import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
import { getResultDetail, getResultList, getResultMaster } from '@/stores/safewiz/impl/api/trainingResultApi.js';
import { nextTick } from 'vue';
const emergencyResponseStore = useEmergencyResponseStore();

// import { getTrainingDetail } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';

export const useTrainingResultStore = defineStore('trainingResult', () => {

  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const inputForm = ref({
  });
  const initInputForm = () =>{
    inputForm.value = {
      cmd:'I',
      docType: 'ERT',
      writeYear: emergencyResponseStore.searchParam.searchText,
      resultList:[{
        cmd:'I',
        writeYear: emergencyResponseStore.searchParam.searchText,
        targetTime:0,
        measurementTime:0,
        useYn:'Y',
        checked: true
      }]
    }
  }
  const resultForm = ref({
    cmd:'I',
    writeYear: emergencyResponseStore.searchParam.searchText,
    targetTime:0,
    measurementTime:0,
    useYn:'Y',
    checked: true
  });
  const originResultList = ref([]);
  const actionDtList = ref([]);
  const btnDetail = async (data, notify) => {
  //   let param = {
  //     writeYear: data.writeYear,
  //     docType: data.docType,
  //     docNo: data.docNo
  // };
  // // getTrainingDetail(param, notify)
  // //     .then(res => {
  // //         inputForm.value = res.list;
  // //     })
  }
  const btnDelete = (list, notify) =>{
    // deleteTraining(list,notify).then(res=>{
    //   router.push('/EmergencyResponseTraining')
    // })
  }
  //#region [아코디언을 포함할 마스터 정보를 불러옴] Sim 25. 3. 13.
  const getMaster = async (param, notify) => {
    openLoading();
    try {
      const res = await getResultMaster(param, notify);
      if (res.list.length > 0) {
        Object.assign(inputForm.value, res.list);
      }
    } catch (error) {
      console.error('getMaster error:', error);
    } finally {
      endLoading();
    }
  };
  //#endregion

  //#region [아코디언 안에 들어갈 개별 항목들 불러옴] Sim 25. 3. 13.
  const getDetail = async (param, notify, fileList) => {
    openLoading();
    getResultDetail(param, notify).then(async res=>{
      res.list.forEach((el, index) => {
        el.actionDt = formatDate(el.actionDt);
        el.key = index;
      });

      inputForm.value['resultList'] = null;
      inputForm.value['resultList'] = res.list;

      //#region [내가 찾는 아코디언의 자동으로 아코디언 열기 처리] Sim 25. 3. 13.
      // 특정 문서를 찾아 자동으로 아코디언 열기
      if (param.writeYear && param.docNo && param.docSeq) {
        await nextTick(); // Vue의 DOM 업데이트 후 실행 보장

        const targetIndex = res.list.findIndex(
            (detail) =>
                detail.writeYear === param.writeYear &&
                detail.docNo === param.docNo &&
                detail.docSeq === param.docSeq
        );

        if (targetIndex !== -1) {

          // 아코디언 버튼을 정확히 선택
          const targetButton = document.querySelector(`button[data-index="${targetIndex}"]`);

          if (targetButton) {
            targetButton.click(); // 아코디언 열기
          } else {
            console.warn('해당하는 아코디언 버튼을 찾을 수 없음');
          }
        }
      }
      //#endregion

      await nextTick();
    }).catch((error)=>{

      console.error('getDetail error:', error);
    }).finally(()=>{

      inputForm.value.resultList.forEach((el, index) => {
        fileList[index]?.fnSearch();
      });
      endLoading();
    })

    // try {
    //   const res = await getResultDetail(param, notify);
    //   console.log('@@res', res);
    //   res.list.forEach((el, index) => {
    //     el.actionDt = formatDate(el.actionDt);
    //     el.key = index;
    //   });

    //   inputForm.value['resultList'] = null;
    //   inputForm.value['resultList'] = res.list;

    //   //#region [내가 찾는 아코디언의 자동으로 아코디언 열기 처리] Sim 25. 3. 13.
    //   // 특정 문서를 찾아 자동으로 아코디언 열기
    //   if (param.writeYear && param.docNo && param.docSeq) {
    //     await nextTick(); // Vue의 DOM 업데이트 후 실행 보장

    //     const targetIndex = res.list.findIndex(
    //         (detail) =>
    //             detail.writeYear === param.writeYear &&
    //             detail.docNo === param.docNo &&
    //             detail.docSeq === param.docSeq
    //     );

    //     if (targetIndex !== -1) {

    //       // 아코디언 버튼을 정확히 선택
    //       const targetButton = document.querySelector(`button[data-index="${targetIndex}"]`);

    //       if (targetButton) {
    //         targetButton.click(); // 아코디언 열기
    //       } else {
    //         console.warn('해당하는 아코디언 버튼을 찾을 수 없음');
    //       }
    //     }
    //   }
    //   //#endregion

    //   await nextTick();
    // } catch (error) {
    //   console.error('getDetail error:', error);
    // } finally {
    //   inputForm.value.resultList.forEach((el, index) => {
    //     fileList[index]?.fnSearch();
    //   });
    //   endLoading();
    // }
  };
  //#endregion

  return {
    searchParam, inputForm,originResultList, actionDtList,resultForm,
    // function
    initInputForm,

    getDetail,
    getMaster,
    // api
    btnDetail, btnDelete
  }
})
