import { defineStore } from "pinia"
import BaseView from '@/components/base/BaseView';
const { ref} = BaseView();


export const useProcWorkStore = defineStore("ProcWork", () => {

  //작업내용 리스트
  const revList = ref([]);
  const confirmList = ref({});
  const workList = ref([]);

  //-----------------------------------------------

  return {
    revList, confirmList, workList
  }
})
