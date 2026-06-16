import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import {
  getGroupList, saveGroup, deleteGroup, getMonitoringList, setMonitoringList, deleteMonitoringList, getCCTVList 
} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringManageApi.js'

export const useSafetyMonitoringManageStore = defineStore("SafetyMonitoringManageStore", () => {
  //#region 
  // ========= 변수 ==========
  const userCd = ref('');

  const cards = ref([]);
  cards.value = {
    cmd: '',
    Profile_id: '', // PROFILE ID
    Profile_name: '', // PROFILE 명
    comp_id: '', // 사업장명
    isEditing: false,
    edit_name: '',
    selected: false,
    created_by: '', // 작성자
    created_at: '', // 작성일
    updated_by: '', // 수정자
    updated_at: '', // 수정일
  }
  const selectedCard = ref({});
  const deletedCards = ref([]);

  const cctvList = ref([])

  const layout = ref([]);
  layout.value = {
    cmd: 'U',
    profile_id: '',
    cctv_id: '',
    cctv_play_url: '',
    title: '',
    ratio: '',
    i: '',
    x: '',
    y: '',
    w: '',
    h: '',
    created_at: '',
    created_by: '',
    updated_at: '',
    updated_by: ''
  }
  const deleteLayout = ref([])
  //#endregion

  // 검색어 필터

  //#region 
  // ========== 함수 처리 ==========  
  let idSeq = 0;

  const addCard = async() => {
    cards.value.push({
      cmd: 'I',
      Profile_id: idSeq++,
      Profile_name: `CCTV Group#${idSeq}`,
      isEditing: false,
      edit_name: ''
    });
    const addCard =[{
      Profile_name: `CCTV Group#${idSeq}`,
      userCd : userCd.value,
      __created__ : true
    }]
    const res = await saveGroup(addCard, true);
    if(res.success){
      btnSearch()
    }
  }

  /* 그룹명 변경 시작 */
  const startEdit = (card) => {
    card.isEditing = true;
    card.edit_name = card.Profile_name;
  }

  /* 그룹명 변경 완료 */
  const finishEdit = (card) => {
    if (!card.isEditing) return

    confirmMsg('info', t('msgSave'), null, { fun: saveCard, param: card }, { fun: cancelEdit, param: card });
  }

  /* CCTV 그룹 삭제 */
  const deleteEdit = (card) => {
    confirmMsg('info', t('msgDelete'), null, { fun: deleteCard, param: card });
  }

  /* 그룹명 변경 취소 */
  const cancelEdit = (card) => {
    card.isEditing = false;
    card.edit_name = '';
  }

  // ========== API 처리 ==========
  // 조회 처리
  const getDataGrid = async () => {
    openLoading()
    try {
      layout.value = [];
      const res = await getGroupList(userCd.value, true);
      if (res.success) {
        cards.value = res.list;
        if (res.list.length > 0) {
          if (Object.keys(selectedCard.value).length === 0) {
              selectedCard.value = cards.value[0];
          }
          await getDetailDataGrid(selectedCard.value);
        }
      }
    } catch (error) {
      console.error('Error fetching datas:', error);
      endLoading()
    }
  };

  // CCTV 그룹 저장
  const saveCard = async (card) => {
    const param = [{
      userCd: userCd.value,
      Profile_id : card.Profile_id,
      Profile_name: card.edit_name,
      isEditing: false,
      cmd: 'U'
    }];
    // 그룹 저장 API 리턴
    const res = await saveGroup(param, true);
    if (res.success) {
      await getDataGrid();
    }
    else {
      alertMsg('error', 'AI 서버 리턴 이상.');
    }
  }

  /* CCTV 그룹 삭제 */
  const deleteCard = async (card) => {
    layout.value = [];
    // 삭제 API 리턴
    const res = await deleteGroup([card.Profile_id], true);
    if (res.success) {
      await getDataGrid();
    }
    else {
      alertMsg('error', 'AI 서버 리턴 이상.');
    }
  }

  // 조회 처리
  const getDetailDataGrid = async (item) => {
    openLoading()
    try {
      const param = item.Profile_id;

      const res = await getMonitoringList(param, true);
      if (res.success) {
        layout.value = res.list;
        endLoading();
      }
      
    } catch (error) {
      console.error('Error fetching datas:', error);
      layout.value = []
      endLoading();
    }
  };

  // 선택 그룹의 CCTV 레이아웃 저장 API 리턴
  const saveCCTVLayout = async () => {
    openLoading()
    let layoutList = []
    layout.value.forEach(val => {
      layoutList.push({
        cctv_id : val.cctv_id,
        h : val.h,
        i : val.i,
        profile_id : val.profile_id,
        title : val.title,
        userCd : userCd.value,
        w : val.w,
        x : val.x,
        y : val.y,
        cmd : val.cmd === 'I' ? 'I' : 'U'
      })
    })
    
    const param = layoutList
    if(deleteLayout.value.length !== 0){
      const isEmptyLayout = param.length == 0 ? true : false
      const deleteParam = deleteLayout.value.map(val => `${selectedCard.value.Profile_id}_${val.i}`)
      deleteMonitoringList(deleteParam, isEmptyLayout).then(res => {
        if(res.success){
          deleteLayout.value = []
        }
      })
    }
    // 그룹 저장 API 리턴
    if(param.length !== 0){
      await setMonitoringList(param, true).then(async res => {
        if (res.success) {
          await getDetailDataGrid(selectedCard);
          await btnSearch()
        }
      })
    }
    endLoading()
  };

  const searchCCTVList = (showMsg) => {
    openLoading()
    getCCTVList(userCd.value, showMsg).then(res => {
      if (res.list.length > 0) {
        // 데이터 세팅
        res.list.forEach(val => {
          val.isPolygon = true
        })
        cctvList.value = res.list;
        endLoading()
      }
    });
  }

  //#endregion

  //#region ========== Master 기본 버튼 기능 ==========
  // 목록 이동 기능
  const btnBack = async () => {
    await goToBack();
  };

  // 검색 기능
  const btnSearch = async () => {
    await getDataGrid();
  };

  // 추가 버튼 기능
  const btnAdd = () => {
    addCard();
  };

  // 저장 버튼 기능
  const btnSave = async () => {
    confirmMsg('info', t('msgSave'), null, { fun: saveCCTVLayout, param: '' });
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
    userCd, goToBack,
    cards, selectedCard, startEdit, finishEdit, cancelEdit, deleteEdit,
    getDetailDataGrid, searchCCTVList, layout, deleteLayout, cctvList
  };
});
