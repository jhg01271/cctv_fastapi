/**
 * ============================================================================================================================================================
 * 👉 breadcrumbs route 상태관리자(Pinia)
 * ============================================================================================================================================================
 * 작성자 : 데이터플랫폼팀 이우빈
 * 작성일 : 2023-02-23
 * ============================================================================================================================================================
 */
import router from '@/router'
import { defineStore } from 'pinia'
import {  customCookies } from '@/utils/token'

import { useUserInfoStore } from '@/stores/user.js'
import {  refresh,
  } from '@/stores/system/setting/api/authApi.js'
  import { setToken } from '@/utils/token'; // 토큰
export const useAuthStore = defineStore('auth', () => {
  const userStore = useUserInfoStore()
  /**
   * 👉 로그인 / 로그아웃
  */
  
  /**
   * 👉 유저 확인
   */
  
  
  /**
   * 👉 자동로그인 (token 저장)
   */
  
  /**
   * 👉 유저 재인증
   */
  const handleRefreshUserAuth = async () => {
    return await new Promise(resolve => {
      refresh()
      .then(res => {
        if(res.success) {
          setToken(res.accessToken); // 재발급한 액세스토큰으로 세션스토리지 갱신
          resolve(true)
        } else {
          resolve(false)
        }
      })
      .catch(() => {
        router.push('/login')
        handleLogout()
        resolve(false)
      })
    })
  }
   
 
  
  /**
   * 👉 회원가입
   */
  
  /**
   * 👉 로그아웃
   */
  const handleLogout = () => {
    sessionStorage.removeItem('Token')
    customCookies('remove', 'cs')
    router.push({ path : '/login'})
  }
  /**
   * 👉 유저 정보 찾기 / 아이디 중복체크
   */
  
  return { handleLogout,handleRefreshUserAuth }
})
