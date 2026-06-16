// src/stores/counter.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useButtonListStore = defineStore('buttonList', () => {

  const sample = ref(false)
  const search = ref(false)
  const add = ref(false)
  const del = ref(false)
  const save = ref(false)
  const back = ref(false)
  const edit = ref(false)
  const download = ref(false)
  const bnew = ref(false)
  const preYear = ref(false)
  const call = ref(false)
  const downloadForm = ref(false)
  const upload = ref(false)
  const suggestionSearch = ref(false)
  const suggestionAdd = ref(false)
  const copy = ref(false)
  const workerCheck = ref(false) //근로자 확인

  const states = {
    sample,
    search,
    back,
    save,
    add,
    del,
    edit,
    download,
    bnew,
    preYear,
    call,
    downloadForm,
    upload,
    suggestionSearch,
    suggestionAdd,
    copy,
    workerCheck
  };
  const downloadType = ref('pdf') // pdf, xls
  const useBtnList = ref([])

  const init = () => {
    useBtnList.value = []
  }

  const click = (type, newDownloadType = '') => {
    if (states[type]) {
      downloadType.value = newDownloadType
      states[type].value = !states[type].value
    }
  }
  return { click, search, back, save, add, del, edit, copy, download, bnew, preYear, call, downloadForm, upload, useBtnList, init, sample, suggestionSearch, suggestionAdd, downloadType, workerCheck }
})