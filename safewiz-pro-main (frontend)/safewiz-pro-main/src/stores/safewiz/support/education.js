import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getEduTraining } from '@/stores/safewiz/support/api/educationApi.js'
import _ from 'lodash'
export const useEducationStore = defineStore('education', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    openLoading();
    getEduTraining(searchParam.value, notify).then(res => {      
      res.list[1].tooltipText = { Y: '문서가 등록되었습니다.', N: '등록된 문서가 없거나, 승인자의 서명이 완료된 문서가 없습니다.' }
      menuList.value = res.list
    }).finally(() => {
      endLoading();
    });
  }

  return {
    searchParam, menuList,
    // function
    // api
    btnSearch
  }
})