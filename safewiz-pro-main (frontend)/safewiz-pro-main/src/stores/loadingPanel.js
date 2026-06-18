// src/stores/counter.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLoadingPanelStore = defineStore('loadingPanel', () => {

  const isLoading = ref(false);

  const openLoading = () => {
    isLoading.value = true
  }
  const endLoading = () => {
    isLoading.value = false
  }
  return { isLoading, openLoading, endLoading }
})