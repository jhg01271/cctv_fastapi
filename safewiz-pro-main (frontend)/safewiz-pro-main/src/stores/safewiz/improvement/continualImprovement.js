import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getContinualImprovementTitleMenu } from '@/stores/safewiz/improvement/api/continulaImprovementApi.js'
import _ from 'lodash'
export const useContinualImprovementStore = defineStore('continualImprovement', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getContinualImprovementTitleMenu(searchParam.value, notify).then(res => {
      menuList.value = res.list
    })
  }

  return {
    searchParam, menuList,
    // function
    // api
    btnSearch
  }
})