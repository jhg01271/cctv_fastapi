import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { ref, getCompId, getCurrentDate } = BaseView()
import { getMonitoringCtrl } from '@/stores/safewiz/evaluation/api/monitoringManageApi.js'
export const useMonitoringManageStore = defineStore('monitoringManage', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getMonitoringCtrl(searchParam.value, notify).then(res => {
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
