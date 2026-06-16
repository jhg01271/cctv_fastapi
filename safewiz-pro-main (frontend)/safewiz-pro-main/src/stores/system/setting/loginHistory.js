import { defineStore } from "pinia"
import { ref } from "vue"


import { 
    getLoginHistoryList, getLastLoginDetails
} from "@/stores/system/setting/api/loginHistoryApi"


export const useLoginHistoryStore = defineStore("loginHistory", () => {

    //로그인 이력 리스트
    const loginHistoryLists =ref()

    const getLoginHistoryLists = (searchParam) => {
    return new Promise((resolve) => {
      getLoginHistoryList(searchParam, true)
          .then(res => {
              loginHistoryLists.value = res.list
              resolve({ list: res.list, totalCount: res.totalCount  })
        })
    })
    }

    // 마지막 로그인 이력 userId로 가져올 수 있도록 수정하기 --!
    const getLoginHistoryDetail = (userId) => {  
        return new Promise((resolve) => {
            getLastLoginDetails({ userId }) // userId를 객체로 전달
                .then(res => {
                    loginHistoryLists.value = res.list;
                    resolve({ list: res.list, totalCount: res.totalCount  })
                });
        });
    };
    
    

    return {
        loginHistoryLists, getLoginHistoryLists, getLoginHistoryDetail
  }
})
