//#region 공통 import----------------------------------------------------------------------------------

// import './assets/main.css'
// import 'bootstrap/dist/css/bootstrap.min.css'
// import 'bootstrap-icons/font/bootstrap-icons.css'
// import 'bootstrap/dist/js/bootstrap.js'
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import VueKonva from 'vue-konva';
const app = createApp(App);

//=========== sdm 추가 ==============
/**
 * 스타일 가져오기 SDM
 */
import '@/assets/css/style.scss';

/**
 * 컴포넌트 가져오기 SDM
 */
import Popup from '@/sdm/components/popup/Popup.vue';
import actionPopup from '@/sdm/components/popup/actionPopup.vue';
import ToastPopup from '@/sdm/components/popup/ToastPopup.vue';

/**
 * 커스텀 디렉티브 가져오기 SDM
 */
import inputWrapper from '@/sdm/directives/inputWrapper';
import rippleAnimation from '@/sdm/directives/ripple';
import tooltip from '@/sdm/directives/tooltip';
import selectWrapper from '@/sdm/directives/selectWrapper';
import calendarDirective from '@/sdm/directives/calendar.js';

/**
 * 스크롤 라이브러리 가져오기 SDM (Lenis로 변경 예정)
 */
import GambitSmoothScroll from '@/sdm/utils/gambitSmoothScroll';
new GambitSmoothScroll(); //FIXME? 작동 방법 확인

import iCard from '@/components/common/iCard.vue';
import iCardMenu from '@/components/common/iCardMenu.vue';
import iCardOrder from '@/components/common/iCardOrder.vue';
import iChips from '@/components/common/iChips.vue';
import iHrChips from '@/components/common/iHrChips.vue';
import iPopupButtonList from '@/components/common/iPopupButtonList.vue';
import iPopupDialog from '@/sdm/components/popup/Popup.vue';
import iDataGrid from '@/components/grid/iDataGrid.vue';
import iDataGridPage from '@/components/grid/iDataGridPage.vue';

import iFileList from '@/components/file/iFileList.vue'
import iFileImage from '@/components/file/iFileImage.vue'
/**
 * 전역 컴포넌트 등록
 * @param {string} name - 컴포넌트 이름
 * @param {Component} component - Vue 컴포넌트
 */
app.component('PopupDialog', Popup);
app.component('actionPopup', actionPopup);
app.component('ToastPopup', ToastPopup);
app.component('iCard', iCard);
app.component('iCardMenu', iCardMenu);
app.component('iCardOrder', iCardOrder);
app.component('iChips', iChips);
app.component('iHrChips', iHrChips);
app.component('i-PopupButtonList', iPopupButtonList);
app.component('i-PopupDialog', iPopupDialog);
app.component('iDataGrid', iDataGrid);
app.component('iDataGridPage', iDataGridPage);
app.component('iFileList', iFileList);
app.component('iFileImage', iFileImage);

/**
 * 커스텀 디렉티브를 등록합니다.
 * @param {string} name - 디렉티브 이름
 * @param {Directive} directive - Vue 디렉티브
 */
app.directive('input', inputWrapper);
app.directive('button', rippleAnimation);
app.directive('tooltip', tooltip);
app.directive('select', selectWrapper);
app.directive(
    'calendar',
    calendarDirective({
        theme: {
            primary: '#3248F6'
        }
    })
);

//=========== dev 추가 ==============
/** ✏️ EChart5 */
import ECharts from 'vue-echarts';
import echartCore from '@/components/echart/echart-core';

/** ✏️ Sweetalert2 (Confirm) & theme */
import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
/** ✏️ Daum Postcode */
import VueDaumPostcode from 'vue-daum-postcode';
app.use(VueDaumPostcode);
const pinia = createPinia();
app.use(pinia); //pinia
export { pinia };
import '@/firebase/fcmService.js'
import messages from './i18n';
import { customCookies } from '@/utils/token';
import { createI18n } from 'vue-i18n';
console.log(customCookies('get', 'locale'));
const i18n = createI18n({
    legacy: false,
    locale: customCookies('get', 'locale') || 'ko',
    fallbackLocale: 'en',
    messages
});
export { i18n };
app.use(i18n);

import VueSignature from 'vue-signature-pad';
app.use(VueSignature);
try {
    app.use(router);
} catch (e) {
    window.location.reload()
    console.error('Router initialization failed:', e);
    // 에러에 따라 적절한 대체 동작을 정의하세요.
    // window.history.go(0);
    // window.location.href = '/login';
}

/** ✏️ Common Components */
import iComponents from '@/components/index';
app.use(iComponents);

app.use(VueSweetalert2); //sweetalert
app.config.globalProperties.$echarts = echartCore;
app.component(ECharts);
import { firebaseApp } from './firebase/firebaseInit' 

app.use(firebaseApp);

if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/firebase-messaging-sw.js')
    .then((registration) => {
      console.log('Service Worker 등록 성공:', registration);
    })
    .catch((err) => {
      console.error('Service Worker 등록 실패:', err);
    });
}

//실시간 영상 안전관리 화면에서 사용될 v-stage 기능 추가
app.use(VueKonva)

app.mount('#app');

//#endregion 공통 import

//#region Toast UI Grid 설정 -------------------------------------------------------------------------

/**
 * tui-grid style을 <head>의 맨 위로 이동시킵니다.
 * 이는 tui-grid 스타일 우선순위를 조정하기 위해 사용며, 동적으로 추가된 스타일 태그가 우선 적용되지 않도록 하기 위함입니다.
 *
 * @param {string} styleId - 이동할 스타일 태그의 ID
 * @param {number} intervalTime - 폴링 간격 (밀리초)
 */
// const moveStyleToTop = (styleId, intervalTime) => {
//     const interval = setInterval(() => {
//         const styleTag = document.getElementById(styleId);
//         if (styleTag) {
//             const newStyle = document.createElement('style'); // 기존 스타일 태그 내용을 복사하여 새로운 스타일 태그 생성
//             newStyle.textContent = styleTag.textContent;
//             newStyle.id = styleId; // 동일한 ID를 유지
//
//             document.head.prepend(newStyle); // 새로운 스타일 태그를 <head>의 맨 위에 추가
//
//             styleTag.remove(); // 기존 스타일 태그를 제거
//
//             clearInterval(interval); // 작업이 완료되면 폴링 중지
//         }
//     }, intervalTime); // 지정한 간격으로 체크
// };

/**
 * DOMContentLoaded 시 스타일 태그 이동
 */
document.addEventListener('DOMContentLoaded', () => {
    // moveStyleToTop('tui-grid-theme-style', 100); // ID와 폴링 간격 설정
    const observer = new MutationObserver(() => {
        const styleTag = document.getElementById('tui-grid-theme-style');
        if (styleTag) {
            const newStyle = document.createElement('style');
            newStyle.textContent = styleTag.textContent;
            newStyle.id = 'tui-grid-theme-style';

            document.head.prepend(newStyle);
            styleTag.remove();
            observer.disconnect(); // 작업 완료 후 감시 중지
        }
    });

    observer.observe(document.head, { childList: true });
});
//#endregion Toast UI Grid 설정
