import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getPlanningCtrl } from '@/stores/safewiz/impl/api/planningAndControlApi.js'
import _ from 'lodash'
export const usePlanningCtrlStore = defineStore('planningAndControl', () => {
  const compId = getCompId();
  const passScore = ref('')
  const safetyActiveTab = ref('')
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
    useYn : 'Y'
  })
  
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getPlanningCtrl(searchParam.value, notify).then(res => {
      menuList.value = res.list
    })
  }

  return {
    safetyActiveTab,
    searchParam, menuList,passScore,
    // function
    // api
    btnSearch
  }
})