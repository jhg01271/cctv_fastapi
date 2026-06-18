import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import _ from 'lodash'
import {getInternalAudit} from '@/stores/safewiz/evaluation/api/internalAuditApi.js'
export const useAuditStore = defineStore('audit', () => {
  const writeYear = ref(getCurrentDate().substring(0,4))

  const menuList = ref([]);
  const btnSearch = async (notify) => {
    console.log('@@@ 조회')
    getInternalAudit({writeYear: writeYear.value}, notify).then(res => {
      res.list[0].tooltipText = { Y: '문서가 등록되었습니다.', N: '검토자까지 서명이 완료된 문서가 없습니다.' }
      res.list[1].tooltipText = { Y: '문서가 등록되었습니다.', N: '검토자까지 서명이 완료된 문서가 없습니다.' }
      menuList.value = res.list
    })
  }

  return {
    writeYear, menuList,
    btnSearch
  }
})