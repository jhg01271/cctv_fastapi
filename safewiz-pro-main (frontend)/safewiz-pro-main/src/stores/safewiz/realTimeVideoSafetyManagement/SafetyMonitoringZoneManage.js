import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import { getCCTVList, getImage, saveRoi} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringZoneManageApi.js'

export const useSafetyMonitoringZoneManageStore = defineStore("SafetyMonitoringZoneManageStore", () => {
  //#region 
  // ========= 변수 ==========
  const userCd = ref('');
  const cctvList = ref([]);
  const imageData = ref(null)
  const newPoints = ref([])
  //#endregion

  // 검색어 필터

  //#region 
  // ========== 함수 처리 ==========  

  // ========== API 처리 ==========
  /* CCTV 리스트 조회 */
  const searchListData = async (showMsg) => {
    return new Promise(resolve => {
      openLoading();
      getCCTVList(userCd.value, showMsg).then(res => {
        if (res.list.length > 0) {
          // 데이터 세팅
          cctvList.value = res.list;
        }

        resolve(); // 완료 알림
      }).finally(() => endLoading());
    });
  };

  const saveCCTVLayout = (cctvId) => {

    const cctvListData = cctvList.value.find(el => el.cctv_id === cctvId)
    if(cctvId){
      openLoading()
      const param = {
        cctv_id : cctvId,
        model_list: cctvListData.model_list.map(el => ({
          userCd: userCd.value,
          is_run : el.is_run,
          model_nm : el.model_nm,
          point_arr: el.model_nm == 'Pose' ? [] : JSON.stringify(newPoints.value.map(p=>[p.x,p.y]))
        }))
      }
      saveRoi([param], true).then(res => {
        if(res.success){
          searchListData()
        }
      }).finally(() => {
        endLoading()
      })
    }
  };

  const getCCTVImage = async(param) => {
    try{
      await getImage(param).then(res => {
      imageData.value = res
      if(res === undefined){
        imageData.value = null
      }
      endLoading()
    })
    }catch(error){
      imageData.value = null
      endLoading()
    }

  }
  //#endregion

  //#region ========== Master 기본 버튼 기능 ==========
  // 목록 이동 기능
  const btnBack = async () => {
    await goToBack();
  };

  // 검색 기능
  const btnSearch = async () => {
    await searchListData();
  };

  // 추가 버튼 기능
  const btnAdd = () => {
    addCard();
  };

  // 저장 버튼 기능
  const btnSave = async (cctvId ,points) => {
    newPoints.value = points
    if(points.length < 3){
      alertMsg('error', '최소 세 개 이상의 좌표를 지정해 주시기 바랍니다.');
      return
    }
    confirmMsg('info', t('msgSave'), null, { fun: saveCCTVLayout, param: cctvId });
  }
  //#endregion

  //#region ========== 라우터 이동 ==========
  // 이전화면으로 라우터 이동
  const goToBack = () => {
    router.push({ path: '/realTimeVideoSafetyObjectives' });
  }
  //#endregion

  return {
    btnBack, btnSearch, btnSave, btnAdd,
    goToBack, searchListData, getCCTVImage,
    userCd, cctvList, imageData
  };
});
