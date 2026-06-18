import { createRouter, createWebHashHistory, createWebHistory, START_LOCATION } from 'vue-router'
import { AesEncrypt, AesDecrypt } from '@/utils/aes256';

import BaseLayout from '@/layouts/BaseLayout/Layout.vue'

const currentRoute = [
  {
    path: "/",
    name: 'Base',
    component: BaseLayout,
    children: [{
      path: '/myPage',
      name: 'Mypage',
      // TODO 다국어 적용 필요 - mypage_title
      meta: { menuNm: "사용자 정보 변경", savAh: "Y", requiresAuth: true },
      component: () => import('@/views/system/user/my/MyPageForm.vue'),
    }
    ]
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/layouts/BaseLayout/DashboardLayout.vue'),  // BaseLayout 대신 DashboardLayout 사용
    children: [
      {
        path: '',
        name: 'DashboardPage',
        component: () => import('@/views/safewiz/dashboard/Dashboard.vue'),
      }
    ]
  },
  {
    path: "/login",
    name: "Signin",
    component: () => import('@/views/base/common/BaseSignin.vue')
  },
  {
    path: "/findUserId",
    name: "FindUserId",
    component: () => import('@/views/system/user/FindUserId.vue')
  },
  {
    path: "/findUserPs",
    name: "FindUserPs",
    component: () => import('@/views/system/user/FindUserPs.vue')
  },
]
const router = createRouter({
  history: createWebHashHistory(),
  routes: currentRoute,
  data: {
    menu: [],
  }
})
async function setMenuList() {
  await searchMenu(searchParam)
    .then(async res => {
      router.options.data.menu = []
      await dynamicRoute(res.list.filter(el => { return el.routerPath }))
    })
}
async function dynamicRoute(list) {
  router.options.data.menu = list

  // DashboardLayout 을 사용할 view 목록 (name 기준)
  const dashboardRouteNames = new Set([
    'SafetyMonitoring'
  ])

  for (let el of handleRoute(list)) {
    if (dashboardRouteNames.has(el.name)) {
      router.addRoute('Dashboard', el)
    } else {
      router.addRoute('Base', el)
    }
  }
}
function handleRoute(list) {
  const modules = import.meta.glob('../views/**/*.vue')
  // modules : 정확한 경로의 컴포넌트가 없을시 undefined
  return list.reduce((acc, item) => {
    const { auth, routerPath, routerNm, viewPath } = item
    if (routerPath !== '' && routerNm !== '') {


      // 사용자별 메뉴 권한 오류로 인한 버튼 권한 임시 모두 허용 (박주형)
      // item.delYn='Y'
      // item.exlAh='Y'
      // item.savAh='Y'
      // ----------------------------------------------------------

      let config = {
        path: `/${viewPath}`,
        name: routerNm,
        meta: item,
        component: modules[`../views${routerPath}/${viewPath}.vue`]
      }
      if (auth) {
        config.meta = { requiresAuth: false }
      }
      if (item.query) {
        config.path = config.path + item.query
      }
      acc.push(config)
    }
    return acc
  }, [])
}

import { useMenuStore } from '@/stores/menu.js'
import { useFavoritesMenuStore } from '@/stores/favoritesMenu.js';
import { useButtonListStore } from '@/stores/buttonList';
import { searchMenu } from '@/stores/system/setting/api/Menu.js';
import { getAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
const searchParam = {
  listSize: 100,
  curPage: 1,
  sortKey: '',
  asc: true
};


var reStart = false
import { customCookies } from '@/utils/token'
import { getToken } from '@/utils/token'
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
import { useValidationStore } from '@/sdm/stores/validationStore';
import { useUserInfoStore } from '@/stores/user';
import { useFcmStore } from '@/firebase/fcmService.js' //FCM TOKEN 권한 허용 여부
router.beforeEach(async (to, from, next) => {
  const menuStore = useMenuStore()
  const favoritesMenuStore = useFavoritesMenuStore();
  //// 컴포넌트 스토어 초기화
  const layoutStore = useButtonListStore()
  layoutStore.init()
  const selectUserStore = useSelectUserStore()
  selectUserStore.flush()
  const userInfoStore = useUserInfoStore();
  const fcmStore = useFcmStore()
  try {
    if (userInfoStore.loginId !== "") {
      getAlarm({}, false).then(res => {
        if (res && res[0]) {
          fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length
        } else {
          fcmStore.alarmCount = 0;
        }
      })
    }

    if ((from === START_LOCATION && !reStart && to.path != '/login' && getToken())) { // 새로고침 시, 메뉴 구성
      window.history.replaceState(null, '', window.location.pathname);
      reStart = true
      await setMenuList()
      menuStore.setMenu()
      next(to.path)
    } else if (to.path == '/dashboard' && getToken()) { // getToken() 체크 추가
      await setMenuList()
      menuStore.setMenu()
      //로그인 성공 시, 만약 fromPush가 true일 경우
      if (from.redirectedFrom?.fullPath?.includes('fromPush=true')) {
        const fcmPayload = JSON.parse(from.redirectedFrom.query.fcmPayload)
        if (from.redirectedFrom.path !== '/') { //크롬이 실행되고 있는 중이며 세이프위즈창만 없을 경우
          from.redirectedFrom.path = from.redirectedFrom.path.replace('/', '')
        } else {//크롬이 아예 실행되고 있지 않을 경우
          if (fcmPayload.type == 'notice') {
            from.redirectedFrom.path = 'noticeDetail'
          }
        }
        next({ path: from.redirectedFrom.path, query: from.redirectedFrom.query })
      } else {
        next()
      }

    }
    else if (to.path == '/') {
      // let autoLogin = customCookies('get', 'AUTOLOGIN')
      if (getToken() == null) {
        next('/login')
        // }else if(from.path!='/login' && AesDecrypt(autoLogin)!='Y'){
      } else if (from.path != '/login') {
        next('/login')
      } else {
        await setMenuList()
        menuStore.setMenu()
        next('/dashboard')
      }
    }
    else if (from === START_LOCATION && !getToken() && to.path !== '/login') {
      // 새창에서 URL 직접입력 시 로그인 페이지로 이동 (로그인 페이지는 제외)
      next('/login')
    }
    else {
      const existMenu = router.options.data.menu.find(el => el.viewPath.toLowerCase() == to.path.replaceAll('/', '').toLowerCase())
      if (existMenu) {
        next()
      } else {
        if (to.path !== '/login' && to.path !== '/myPage' && to.path !== '/pageNotFound' && to.path !== '/findUserId' && to.path !== '/findUserPs') { // 로그인 페이지는 예외처리
          // 메뉴권한이 없는경우 대시보드로 이동
          alert("경로가 잘못되었거나, 권한이 없습니다.")
          next('/dashboard')
        } else {
          next()
        }
        // next()
      }
    }
  } catch (e) {
    console.log('No Auth Token')
    next('/login')
  }
})

// 2024.12.05 dkyoo 라우터 이동 시 모든 유효성 검증 초기화
router.afterEach((to, from) => {
  // 탭 네비게이션에 메뉴 추가
  const menuStore = useMenuStore();
  // 검토용
  // menuStore.setTabNaviMenu(to);

  const validationStore = useValidationStore();
  validationStore.clearAllErrors();
});

export default router
