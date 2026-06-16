import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import {
  getGroupList, getMonitoringList
} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringApi.js'

import {getEventGroupHistoryList ,getEventHistoryList} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringHistoryApi.js'

export const useSafetyMonitoringStore = defineStore("SafetyMonitoringStore", () => {
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

  const issueAlertList = ref([])

  const issueAlertCountList = ref([
    { id: 'E001', text: '안전모 미착용', color: 'yellow', icon: 'e001' },
    { id: 'E002', text: '위험구역 출입', color: 'yellow', icon: 'e002' },
    { id: 'E003', text: '작업자 쓰러짐', color: 'red', icon: 'e003' },
])
  //#endregion

  // 검색어 필터

  //#region 
  // ========== 함수 처리 ==========  

  // ========== API 처리 ==========
  // 조회 처리
  const getDataGrid = async () => {
    try {
      layout.value = [];
      const res = await getGroupList(userCd.value, true);
      if (res.success) {
        cards.value = res.list;
        if (res.list.length > 0) {
          if (Object.keys(selectedCard.value).length === 0) {
              selectedCard.value = cards.value[0];
          }
          getDetailDataGrid(selectedCard.value);
        }
      }
    } catch (error) {
      console.error('Error fetching datas:', error);
    }
  };

  // 조회 처리
  const getDetailDataGrid = async (item) => {
    try {
      openLoading();
      console.log('getDetailDataGrid#1:', item);
      console.log('getDetailDataGrid#2:', item.Profile_id);
      const param = item.Profile_id;

      const res = await getMonitoringList(param, true);
      if (res.success) {
        res.list.forEach(val => {
          val.w = val.w * 2
          val.x = val.x * 2
          val.y = val.y * 2
        })
        layout.value = res.list;
      }
      endLoading();
    } catch (error) {
      console.error('Error fetching datas:', error);
      layout.value = []
    }
  };
  //#endregion

  //#region ========== Master 기본 버튼 기능 ==========
  // 조회 기능
  const btnSearch = async () => {
    await getDataGrid();
    await searchGroupHistoryList()
  };

  const searchGroupHistoryList = () => {
    const param = {
      profile_id: selectedCard.value.Profile_id,
      userCd: userCd.value
    }
    
    getEventGroupHistoryList(param).then(res => {
      issueAlertList.value = res.list.map(val => {
        return {
          ...val
        }
      })
      issueAlertCountList.value = issueAlertCountList.value.map(val => {
        const count = issueAlertList.value.filter(item => item.event_type === val.id).length
        return {
          ...val,
          count
        }
      })
    })

  }
  //#endregion

  //#region ========== 라우터 이동 ==========

  //#endregion

  return {
    btnSearch,
    userCd,
    cards, selectedCard, issueAlertCountList, issueAlertList,
    getDetailDataGrid, searchGroupHistoryList, layout
  };
});
