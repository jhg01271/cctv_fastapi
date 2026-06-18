/**
 * ============================================================================================================================================================
 * 👉 Http 통신을 위한 설정 및 공통 모듈 페이지
 * -> axios를 활용한 Http 통신 관련 공통 인스턴스 정의
 *
 * -> 🗝️ Header 보안 셋팅
 * -> 🍂 Http Status 값에 따라 분기 처리
 *
 * -> 🔔 notify : kind 값에 따라 푸쉬 알람 사용 유무 처리
 * -> ex) notify : { kind: notify } => 푸쉬 알람 사용하지 않음
 * -> ex) notify : { kind: 'server' } => 서버단 메시지 사용
 * -> ex) notify : { kind: 'custom', type: 'success', title:'사용 변경', msg: '변경에 성공하였습니다' } => 클라이언트단 메시지 사용
 *
 * 2024-05-20 // loading 및 시간 처리  에 response.data => 백단에 ResponseEntity 추가로 response.data.data로 추가 limhs11
 * ============================================================================================================================================================
 */
import axios from 'axios';

import router from '@/router';
import { pinia } from '@/main';
import BaseView from '@/components/base/BaseView';
import { Buffer } from 'buffer';
// const { alertMsg } = BaseView();

import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
import { useAuthStore } from '@/stores/auth';
import { showAlertErrorPopup } from '@/utils/iAlertError.js';

let toastPopupStore;
// import { useLoadingPanelStore } from '@/stores/loadingPanel';
// let loadingPanelStore;

setTimeout(() => (toastPopupStore = useToastPopupStore(pinia)), 50);
// setTimeout(() =>
//   loadingPanelStore = useLoadingPanelStore(pinia), 50);
// console.log('팝업', pinia)

import global from '@/global'; // 프레임워크 전역 글로벌 설정
import { getToken } from '@/utils/token'; // 토큰

let baseUrl = global.buildUrl;
if (import.meta.env.NODE_ENV === 'development') {
    // 빌드 시스템의 환경 변수
    baseUrl = global.devUrl;
}

function _getDefaultMeta() {
    return {
        useTokenUpdate: false, // 받아온 response에서 token 값을 가져와 재 셋팅을 할 경우 true로 셋팅한다.
        useAuth: true, // 인증 모듈을 사용한다. false가 되었을 경우 401 상태에서도 Login 화면으로 가지 않는다. (i18n과 같은 특별한 경우에서만 사용)
        apiVersion: global.apiVersion, // API 버전을 호출 API 별로 정의 한다. 기본 값은 envs에 정의한 값으로 셋팅된다.
        useErrorMessage: true, // response 시, 에러가 날 경우 에러 메시지를 출력하는 여부를 셋팅한다. 기본은 true
        useResponseAll: false, // request 실행 이후, API로 돌아오는 Obj를 Axios response 전문으로 변경되어 전달한다. 기본은 false
        useProgress: true, // API 사용 간 Global Lock Progressive Bar 를 사용하는 것을 의미.
        useErrorConfirm: true // API 사용 간 에러가 발생할 경우, Error 전문 Confirm box가 출력되는 여부
    };
}

const instance = axios.create({
    baseURL: baseUrl, // URL 경로
    timeout: 50000 // Request 타임아웃
});
let isRefreshing = false; // 리프레시 중인지 여부
let refreshSubscribers = []; // 대기 중인 요청들을 관리할 배열

// 리프레시가 완료되면 대기 중인 요청들에 새 토큰을 전달
async function onRefreshed(newToken) {
    refreshSubscribers.forEach(cb => cb(newToken));
    refreshSubscribers = [];
}
// 요청실패 항목 저장
function subscribeTokenRefresh(cb) {
    refreshSubscribers.push(cb);
}

// 토큰 리프레시가 완료될 때까지 대기
function addRefreshSubscriber(callback) {
    refreshSubscribers.push(callback);
}
/** ✏️ axios의 reqeust 인터셉터 진행 부 */
instance.interceptors.request.use(
    config => {
        // if (loadingPanelStore) {
        //   loadingPanelStore.openLoading();
        // }

        let systemId = global.systemId;
        let channel = 'WEB_' + 'agentType';
        let vname = 'UI';
        let lang = 'ko';
        let mid = 'name';
        let xFrame = 'SAMEORIGIN';

        // meta 값 기본 정의
        if (!config.meta) config.meta = {};
        config.meta = Object.assign(_getDefaultMeta(), config.meta);
        // 해더 구성
        config.headers['Authorization'] = getToken() && 'Bearer ' + getToken();
        config.headers['COMPID'] = sessionStorage.getItem('COMP_ID') ? sessionStorage.getItem('COMP_ID') : '';
        config.headers['X-APIVERSION'] = config.meta.apiVersion;
        config.headers['X-CHANNEL'] = channel;
        config.headers['X-VNAME'] = vname;
        config.headers['X-LANG'] = lang;
        config.headers['X-Frame-Options'] = xFrame;
        config.headers['X-MID'] = mid;
        config.headers['X-CALLTYPE'] = '0';
        config.headers['X-APP'] = global.systemId;

        // config.notify = { kind: notify }
        return config;
    },
    error => {
        // 에러가 날경우 진행 부분
        Promise.reject(error);
        // 로딩 화면 닫기
        // initLoadingProgress()
        // if (loadingPanelStore) {
        //   loadingPanelStore.endLoading();
        // }
        console.log('error', 'Server Error', `[${error.request.status}] ${error.message}`);
    }
);

instance.interceptors.response.use(
    response => {
        let res = {};
        let meta = response.config.meta;

        // 파일 다운로드의 경우 처리

        if (response.headers['image']) {
            return response;
        }

        // header를 사용하기위해 report는 다르게 return 시킴
        if (response.headers.report === 'success') {
            // 한글 통신이 제대로 동작하지 않아 base64 인/디코딩을 통하여 파일명 통신

            response.headers.filename = Buffer.from(response.headers.filename, 'base64').toString('utf8');

            return response;
        }

        if (response.headers['content-disposition']) {
            const isDownload = response.headers['x-content-download'] !== 'false';
            if (isDownload) {
                const contentDisposition = response.headers['content-disposition'];
                const fileName = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];
                const decodedFileName = decodeURIComponent(fileName.replace(/['"]/g, ''));
                // 파일명만 추출
                const orgFileName = decodedFileName.match(/[^\\\/]+$/)[0];

                const blob = new Blob([response.data], { type: response.headers['content-type'] });

                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = orgFileName;
                // link.download = decodedFileName;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }

            return response;
        }

        if (response && response.data !== '' && response.data !== null) {
            if (response.data.success) {
                if (response.config.notify?.kind) {
                    switch (response.config.method) {
                        case 'get':
                            toastPopupStore.addToast('조회성공', '조회되었습니다.', 'success');
                            break;
                        case 'put':
                        case 'post':
                            toastPopupStore.addToast('저장성공', '저장되었습니다.', 'success');
                            break;
                        case 'delete':
                            toastPopupStore.addToast('삭제성공', '삭제되었습니다.', 'success');
                            break;
                        default:
                    }
                }

                if (typeof response.data.data == 'object') {
                    response.data.data.success = response.data.success;
                }
            } else {
                let responseURL = response.request.responseURL;

                // 로그인 시 실패 메세지는 로그인후 출력되므로 생략
                if (responseURL.includes('signin')) {
                    return;
                } else {
                    // 실패 메세지 출력

                    return BaseView().alertMsg('warning', '오류 발생', response.data.msg);
                }
            }
            if (response.config.url != '/igns/auth/refresh') isRefreshing = false; // 액세스토큰 재발급 필요 상태 활성화
            // 토큰이 유효하지 않을 때 => 재발급 요청 => 재발급한 토큰으로 다시 API 요청
            // 위 과정을 또다시 실행할지 말지 결정하는 변수값이 isRefreshing
        }
        // return res
        return response.data.data ?? response.data;
    },
    async error => {
        const { config } = error;
        const originalRequest = error.config;
        // response 부분 객체 내 meta 사용을 위한 기본 값 셋팅
        let meta = {};
        // request 기본 에러 값은 499로 설정한다.
        var msg = error.message;

        let displayErrorStatus;
        let displayErrorCode;
        let displayErrorMessage;
        let displayErrorTitle = '';

        if (error.response) {
            console.log('📌 error:', error);
            console.log('📌 error.response:', error.response);
            console.log('📌 error.response.data:', error.response?.data);
            if (error.response && error.response.data instanceof Blob && error.response.data.type === 'application/json') {
                // 이미지 파일의 경우 에러메시지 출력을 위해 형변환
                const text = await error.response.data.text();
                try {
                    const json = JSON.parse(text);
                    error.response.data = json; // 나중에 접근할 수 있도록 덮어쓰기
                } catch (e) {
                    console.error('⚠️ JSON 파싱 실패:', e);
                }
            }
            displayErrorStatus = error.response.status;
            displayErrorCode = error.response.data.code;
            displayErrorMessage = error.response.data.msg;

            /* RefreshToken Disabled */
            if (displayErrorStatus === 403) {
                // const authStore = useAuthStore()
                // authStore.handleUserCheck()
            }
            /* AccessToken 만료 시 RefreshToken으로 재발급 */
            if (displayErrorStatus === 401) {
                config._retry = true;
                /* 새 토큰 발급 중이면 대기열에 추가 */
                if (isRefreshing) {
                    return new Promise(resolve => {
                        subscribeTokenRefresh(newToken => {
                            config.headers = {
                                ...config.headers,
                                Authorization: `Bearer ${newToken}`
                            };
                            resolve(instance.request(config));
                        });
                    });
                }
                /* 액세스 재발급 요청이 필요할 때 (최초 401에러 발생 시) */
                isRefreshing = true;
                try {
                    const authStore = useAuthStore();
                    /* 리프레시토큰으로 엑세스 토큰 재발급 요청, 세션스토리지 갱신 */
                    const success = await authStore.handleRefreshUserAuth();
                    isRefreshing = false;

                    if (!success) {
                        return Promise.reject(error);
                    }

                    const newToken = getToken();
                    // 대기열 처리
                    await onRefreshed(newToken);

                    // 현재 요청 재시도
                    config.headers = {
                        ...config.headers,
                        Authorization: `Bearer ${newToken}`
                    };
                    return instance.request(config);
                } catch (err) {
                    isRefreshing = false;
                    return Promise.reject(err);
                }
            }
            // result에 메시지가 있을 경우 메시징 추가 처리
            if (error.response.data) {
                msg = displayErrorMessage;
            }
            // error.response가 없을 경우도 존재 - Network 문제
            if (meta.useAuth && displayErrorStatus === 401) {
                msg = '세션이 만료되었거나, 로그인이 실패되었습니다.';
            }
            // 404 에러 코드 처리
            if (displayErrorCode == null && displayErrorStatus === 404) {
                // 페이지 찾을 수 없음으로 이동함
                router.push('/pageNotFound');
            }
        } else {
            displayErrorStatus = '500';
            displayErrorCode = '-1000';
            displayErrorTitle = '[Axios Response]';
            displayErrorMessage = '서버로 부터 알 수 없는 오류가 발생했습니다. : ' + error.message;
        }
        switch (displayErrorStatus) {
            case 400:
                displayErrorTitle = '요청 실패';
                break;
            case 401:
                displayErrorTitle = '인증 실패';
                break;
            case 403:
                displayErrorTitle = '권한 없음';
                break;
            case 404:
                displayErrorTitle = 'API 주소를 찾을 수 없음';
                break;
            case 405:
                displayErrorTitle = '허용되지 않는 메소드';
                break;
            case 406:
                displayErrorTitle = '받을 수 없는 요청';
                break;
            case 407:
                displayErrorTitle = 'Proxy 서버 인증 오류';
                break;
            case 408:
                displayErrorTitle = '요청시간 초과';
                break;
            case 409:
                displayErrorTitle = '중복 체크';
                break;
            case 500:
                displayErrorTitle = '서버 에러 (Internal Server Error)';
                break;
            case 501:
                displayErrorTitle = '지원하지 않는 요청';
                break;
            case 502:
                displayErrorTitle = 'Bad Gateway';
                break;
            case 503:
                displayErrorTitle = '서버를 찾을 수 없습니다';
                break;
            case 504:
                displayErrorTitle = 'Gateway Timeout';
                break;
        }

        // 에러 NOTIFY 공통처리
        if (displayErrorCode == null) {
            msg = '서버와의 연결이 불안정합니다. 관리자에 문의하세요.';
        }
        // Excetion 메시징 처리

        if (displayErrorStatus !== 409 && displayErrorStatus !== 401) {
            let responseURL = error.response.request.responseURL;

            // 로그인 시 실패 메세지는 로그인후 출력되므로 생략
            if (!responseURL.includes('signin')) {
                //에러메시지
                if (displayErrorMessage == null) {
                    displayErrorMessage = msg;
                }

                BaseView().openLoading();
                showAlertErrorPopup(displayErrorTitle, displayErrorMessage);
                BaseView().endLoading();
                // toastPopupStore.addToast(displayErrorTitle, displayErrorMessage, 'error');
            }
        } else {
            switch (error.response.config.notify.kind) {
                case 'server':
                    toastPopupStore.addToast(displayErrorTitle, msg, 'error');
                    // console.log('error', displayErrorTitle, msg)
                    break;
                case 'custom':
                    toastPopupStore.addToast(error.response.config.notify.title, error.response.config.notify.msg, error.response.config.notify.type);
                    // console.log(error.response.config.notify.type, error.response.config.notify.title, error.response.config.notify.msg)
                    break;
                default:
                    break;
            }
        }

        return Promise.reject(error);
    }
);

export default instance;
