/**
 * ============================================================================================================================================================
 * 👉 breadcrumbs route 상태관리자(Pinia)
 * ============================================================================================================================================================
 * 작성자 : 데이터플랫폼팀 이우빈
 * 작성일 : 2023-02-23
 * ============================================================================================================================================================
 */

import { ref, computed } from 'vue'
import router from '@/router'
import { defineStore } from 'pinia'
import { customCookies } from '@/utils/token'

import { searchMyPage } from '@/stores/system/user/my/MyPage.js';
import { loadFileFromServer } from '@/utils/iFileLoader.js'

export const useUserInfoStore = defineStore('user', () => {
  const tmpUser = ref('') // 임시 사용자
  const user = ref({ // 사용자 정보
    id: '',
    name: '',
    email: '',
    phone: '',
    postNo: '',
    addrBasic: '',
    addrDetail: '',
    recommendCode: ''
  })
  const id = ref('')
  const userId = computed(() => id.value)
  const signature = ref('')
  const name = ref('')
  const userName = computed(() => name.value)
  const orgnNm = ref('')
  const userOrgnNm = computed(() => orgnNm.value)
  const orgnId = ref('')
  const userOrgnId = computed(() => orgnId.value)
  const userImg = ref(null);
  const isLogin = ref(false)
  const isUserLogin = computed(() => isLogin.value)
  const orgnHeadId = ref('') //사용자의 조직장 ID
  const userOrgnHeadId = computed(() => orgnHeadId.value)
  const orgnHeadNm = ref('') //사용자의 조직장 명
  const userOrgnHeadNm = computed(() => orgnHeadNm.value)
  const jbrpId = ref('') // 사용자의 직위 ID
  const userJbrpId = computed(() => jbrpId.value)
  const jbrpNm = ref('') // 사용자의 직위명
  const userJbrpNm = computed(() => jbrpNm.value)
  const fcmToken = ref('')// 사용자의 FCM TOKEN
  const userFcmToken = computed(() => fcmToken.value)
  const loginId = ref('')// 사용자가 로그인할 때 입력하는 ID
  const userLoginId = computed(( loginId.value))
  const compAddrs = ref(''); // 사업장 주소. 날씨 정보 호출 시 사용함
  /**
   * 👉 정상 유저 체크 로직
   * -> 라우터 변경시마다 확인
   * ---------------------
   * ❗ 보완 필요 : 추가 조건 필요
   */
  const getUserInfo = async () => {
    try {
      let res = await searchMyPage();
      orgnId.value = res.result.orgnId
      orgnNm.value = res.result.orgnNm;
      id.value = res.result.hrId;
      name.value = res.result.userNm;
      signature.value=res.result.signature;
      userImg.value = await loadFileFromServer(res.result.logoId); // 
      orgnHeadId.value = res.result.orgnHeadId
      orgnHeadNm.value = res.result.orgnHeadNm
      jbrpId.value = res.result.jbrpId
      jbrpNm.value = res.result.jbrpNm
      fcmToken.value =res.result.fcmToken
      loginId.value = res.result.userId
      return res
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }
  const checkLogin = (param) => {
    isLogin.value = param
  }
  const userEtc = ref({
    alert: 0,
    account: '',
  })
  const userInfo = computed(() => user.value)
  const userEtcInfo = computed(() => userEtc.value)
  const handleUserData = (param) => {
    id.value = param.email
    name.value = param.name
    user.value = param
  }
  const clearUser = () => {
    isLogin.value.isLogin = false
    isLogin.value.type = ''
    id.value = ''
    name.value = ''
  }
  const handleUserInfo = userProps => {
    user.value = userProps
    let session_page = localStorage.getItem('session_page')
    if (!session_page) {
      router.push({ path: '/' })
    } else {
      router.push({ path: session_page })
    }
  }
  /**
   * 👉 아이디 불러오기
   */
  const rememberUser = () => {
    // 쿠키
    if (customCookies('get', 'UID')) {
      return customCookies('get', 'UID')
    } else {
      return ''
    }
  }

  return {
    getUserInfo, checkLogin, user, userName, userImg, userInfo, userEtc,
    userEtcInfo, handleUserInfo, tmpUser, isUserLogin,userOrgnNm,userOrgnId,
    rememberUser, userId, handleUserData, clearUser,signature, userOrgnHeadId, 
    userOrgnHeadNm, userJbrpId, userJbrpNm, userFcmToken, fcmToken, loginId, compAddrs
  }
})
