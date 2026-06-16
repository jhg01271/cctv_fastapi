import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { ref, getCompId } = BaseView()
import { getrealTimeVideoSafetyObjectives } from '@/stores/safewiz/realTimeVideoSafetyManagement/api/realTimeVideoSafetyObjectivesApi.js'
import _ from 'lodash'
export const userealTimeVideoSafetyObjectivesStore = defineStore('realTimeVideoSafetyObjectives', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
  })
  const menuList = ref([]);
  const btnSearch = async (notify) => {
    getrealTimeVideoSafetyObjectives(searchParam.value, notify).then(res => {      
      res.list[0].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
      res.list[1].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
      res.list[2].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
      res.list[3].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
      res.list[4].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
      res.list[5].tooltipText = { Y: '실시간 영상 안전관리 등록을 진행할 수 있습니다.', N: '실시간 영상 안전관리 등록에 문제가 있습니다.' }
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