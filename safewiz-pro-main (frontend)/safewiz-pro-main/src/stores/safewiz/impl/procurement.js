import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import _ from 'lodash'
export const useProcurementStore = defineStore('procurement', () => {

  const activeTab = ref('')

  const searchTerm = ref('');
  const applyFilter = (list) => {
    let filteringData = [];
        filteringData = list
            .map(item => {
                const filteredDataList = item.dataList.filter(data => data.trainingNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.orgnNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.trainingLocation.toString().toLowerCase().includes(searchTerm.value.toLowerCase()));
                return filteredDataList.length > 0 ? { ...item, dataList: filteredDataList } : null;
            })
            .filter(item => item !== null);
    return filteringData;
};
const tabChanged = type => {
  searchTerm.value = ''
  activeTab.value = type;
};
  return {
    activeTab, searchTerm,
    applyFilter, tabChanged
  }
})