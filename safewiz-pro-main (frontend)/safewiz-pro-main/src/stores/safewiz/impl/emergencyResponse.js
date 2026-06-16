import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getEmergencyResponse } from '@/stores/safewiz/impl/api/emergencyResponseApi.js'
import _ from 'lodash'
export const useEmergencyResponseStore = defineStore('emergencyResponse', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getEmergencyResponse(searchParam.value, notify).then(res => {
      res.list[0].tooltipText = { Y: '확정된 조직도가 존재합니다.', N: '확정된 조직도가 없거나, 조직도가 없습니다.' }
      res.list[2].tooltipText = { Y: '실시 보고서가 등록되었습니다.', N: '문서가 없거나, 검토자까지의 승인이 완료된 실시 보고서가 없습니다.' }
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