import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getHseObjectives } from '@/stores/safewiz/participation/api/hseObjectivesApi.js'
import _ from 'lodash'
export const useHseObjectivesStore = defineStore('hseObjectives', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getHseObjectives(searchParam.value, notify).then(res => {      
      res.list[0].tooltipText = { Y: '문서가 등록되었습니다.', N: '등록된 문서가 없거나, 확정된 문서가 없습니다.' }
      res.list[1].tooltipText = { Y: '문서가 등록되었습니다.', N: '등록된 문서가 없거나, 승인자의 서명이 완료된 문서가 없습니다.' }
      res.list[2].tooltipText = { Y: '문서가 등록되었습니다.', N: '등록된 문서가 없거나, 승인자의 서명이 완료된 문서가 없습니다.' }
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