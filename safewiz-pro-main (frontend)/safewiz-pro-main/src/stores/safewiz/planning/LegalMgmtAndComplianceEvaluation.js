import { defineStore } from "pinia"
import BaseView from '@/components/base/BaseView'
const { ref, getCurrentDate, getCompId } = BaseView()
import {getLegalMgmtAndComplianceEvaluation} from '@/stores/safewiz/planning/api/LegalMgmtAndComplianceEvaluationApi.js'

export const useLegalMgmtAndComplianceStore = defineStore("LegalMgmtAndCompliance", () => {
  
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })

  const menuList = ref([]);

  const btnSearch = async (notify) => {
    searchParam.value.searchText = searchParam.value.searchText.substring(0, 4)
    getLegalMgmtAndComplianceEvaluation(searchParam.value, notify).then(res => {
      menuList.value = res.list
    })
  }

  return {
    searchParam, menuList, btnSearch
  }
})