/**
 * ============================================================================================================================================================
 * 👉 Base View
 * -> 공통으로 사용할 컴포지션 함수
 *
 *    [Vue Cycle] [라우터] [다국어] [푸쉬알람 플러그인] [확인 팝업창]
 *
 *  2024.06.04 confirmMsg 추가  임현섭
 * ============================================================================================================================================================
 */
import { format } from 'date-fns';
import globalEnvs from '@/global';
import router from '@/router';

import { storeToRefs } from 'pinia';
import { pinia } from '@/main';
import { AesDecrypt } from '@/utils/aes256';
import { ref, reactive, shallowRef, provide, inject, nextTick, onBeforeMount, onMounted, onBeforeUpdate, onUpdated, onBeforeUnmount, onUnmounted, watch, watchEffect, computed, mergeProps, defineExpose } from 'vue';

import { i18n } from '@/main';
import SdmView from '@/sdm/SdmView';
const t = param => {
    return i18n.global.t(param);
};
import swal from 'sweetalert2';
import { useButtonListStore } from '@/stores/buttonList';

// 로딩 패널
import { useLoadingPanelStore } from '@/stores/loadingPanel';
let loadingPanelStore;

// 사업장 변경 감지를 위한 메뉴 스토어
import { useMenuStore } from '@/stores/menu.js';
let menuStore;

// 출력 유틸 함수
import { iDownloadReport } from '@/utils/report/iReportUtils.js';

// 날짜 유틸 함수
import { getDateFormat } from '@/utils/dateUtil.js';

// 라우터
import { useRouter } from 'vue-router';

// 이미지 로더 유틸
import { clearImageCache } from '@/utils/iFileLoader.js';

setTimeout(() => {
    loadingPanelStore = useLoadingPanelStore(pinia);
    menuStore = useMenuStore(pinia);
    watch(
        () => menuStore.selectedCompany,
        (newVal, oldVal) => {
            if (oldVal && !menuStore.isTaskRoute) {
                clearImageCache();
                window.location.reload();
                // const layoutStore = useButtonListStore();
                // layoutStore.search = !layoutStore.search
            }
        }
    );
}, 50);

export default function BaseView() {
    const btnSearch = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.search,
            () => {
                fn();
            }
        );
    };

    const btnBack = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.back,
            () => {
                fn();
            }
        );
    };

    const btnAdd = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.add,
            () => {
                fn();
            }
        );
    };

    const btnSave = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.save,
            () => {
                fn();
            }
        );
    };

    const btnDelete = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.del,
            () => {
                fn();
            }
        );
    };

    const btnEdit = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.edit,
            () => {
                fn();
            }
        );
    };
    const btnDownload = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.download,
            () => {
                fn();
            }
        );
    };
    const btnNew = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.bnew,
            () => {
                fn();
            }
        );
    };
    const btnPreYear = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.preYear,
            () => {
                fn();
            }
        );
    };
    const btnCall = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.call,
            () => {
                fn();
            }
        );
    };
    const btnDownloadForm = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.downloadForm,
            () => {
                fn();
            }
        );
    };
    const btnUpload = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.upload,
            () => {
                fn();
            }
        );
    };
    const btnSample = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.sample,
            () => {
                fn();
            }
        );
    };
    const btnSuggestionSearch = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.suggestionSearch,
            () => {
                fn();
            }
        );
    };
    const btnSuggestionAdd = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.suggestionAdd,
            () => {
                fn();
            }
        );
    };
    const btnCopy = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.copy,
            () => {
                fn();
            }
        );
    };

    //근로자 확인
    const btnWorkerCheck = fn => {
        const layoutStore = useButtonListStore();
        watch(
            () => layoutStore.workerCheck,
            () => {
                fn();
            }
        );
    };
    const { toastPopup, validationStore } = SdmView();

    /** ✏️ 라우트 */
    // const route = useRoute()

    /** ✏️ 확인창 */
    const swalOption = reactive({
        title: `title`,
        text: `text`,
        imageUrl: '/assets/img/common/swal-icon.svg',
        imageWidth: 30,
        imageHeight: 30,
        imageAlt: 'notice',
        showClass: {
            popup: 'animate__animated animate__fadeIn i-swal'
        },
        hideClass: {
            popup: 'animate__animated animate__fadeOut i-swal'
        },
        // customClass: {
        //   icon: '',
        //   popup: '',
        //   title: '',
        //   content: '',
        //   confirmButton: '',
        //   cancelButton: '',
        // },
        showCancelButton: true,
        confirmButtonColor: '#43d491',
        confirmButtonText: '예',
        cancelButtonColor: '#eeeff2',
        cancelButtonText: '아니오',
        reverseButtons: true // 버튼 순서 거꾸로
    });

    //limhs11
    const confirmMsg = async function (typ, msg, desc, confirmObj, deniedObj) {
        if (desc) {
            msg += `<textarea readonly class="error-textarea w90p h10rem wbka mt1rem"></textarea>`;
        }else{
          msg +='<div class="mt1-5rem"></div>';
        }
        swal.fire({
            position: 'center',
            // icon: typ,
            imageUrl: '/assets/img/common/swal-icon_01.svg',
            imageWidth: 30,
            imageHeight: 30,
            imageAlt: 'notice',
            title: msg,
            confirmButtonColor: '#3248F6',
            cancelButtonColor: '#E1E6ED', // 취소 버튼 색상
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true, // 버튼 순서 거꾸로
            // timer: 5000,
            showCancelButton: true,
            didOpen: () => {
                const swalPopup = document.querySelector('.swal2-container');
                if (swalPopup) {
                    swalPopup.style.zIndex = '2500'; // Vuetify 다이얼로그 2400 보다 높은 값으로 설정
                    const cancelButton = swalPopup.querySelector('.swal2-cancel');
                    if (cancelButton) {
                        cancelButton.addEventListener('click', event => {
                            event.stopPropagation(); // 이벤트 전파 차단
                        });
                    }
                }
            }
        }).then(result => {
            if (result.isConfirmed) {
                if (confirmObj) {
                    confirmObj['fun'](confirmObj['param']);
                    console.log(result);
                }
            }
            if (result.isDismissed) {
                if (deniedObj) {
                    deniedObj['fun'](deniedObj['param']);
                    console.log(result);
                }
            }
        });
        await nextTick();

        const textarea = document.querySelector('.error-textarea');
        if (textarea) textarea.value = desc;
    };

    // donghajo
    const alertMsg = async function (typ, msg, desc) {
        if (desc) {
            msg += `<textarea class="error-textarea w90p h10rem wbka mt1rem" readonly></textarea>`;
        }else{
          msg +='<div class="mt1-5rem"></div>';
        }
        swal.fire({
            position: 'center',
            // icon: typ,
            imageUrl: '/assets/img/common/swal-icon_02.svg',
            imageWidth: 30,
            imageHeight: 30,
            imageAlt: 'notice',
            title: msg,
            confirmButtonColor: '#E1E6ED',
            confirmButtonText: '확인',
            didOpen: () => {
                const swalPopup = document.querySelector('.swal2-container');
                if (swalPopup) {
                    swalPopup.style.zIndex = '2500'; // Vuetify 다이얼로그 2400 보다 높은 값으로 설정
                }
            }
        });
        await nextTick();
        const textarea = document.querySelector('.error-textarea');
        if (textarea) textarea.value = desc;
    };

    // -----------------------------------------
    // kyj
    //[쿠키에서 compId 가져오기]

    function getCookie(name) {
        const value = `; ${document.cookie}`; // 모든 쿠키를 문자열로 가져옴
        const parts = value.split(`; ${name}=`); // 원하는 쿠키 이름 기준으로 분리
        if (parts.length === 2) return parts.pop().split(';').shift(); // 해당 쿠키 값 반환
        return null; // 쿠키가 없을 경우 null 반환
    }

    const getCompId = () => {
        // return AesDecrypt(getCookie('COMP_ID'));
        if(sessionStorage.getItem('COMP_ID')) {
            return AesDecrypt(sessionStorage.getItem('COMP_ID'));
        } return ''
    };
    // -----------------------------------------

    // -----------------------------------------
    // kyj
    //[배열 값 그룹화 및 정렬 기능]

    function groupAndSortByNm(dataList, filterKey, seq="seq") {
        // 그룹을 저장할 객체 생성
        const grouped = {};

        // prcsList를 순회하며 orgnList의 nm를 추출하고 그룹화
        dataList.forEach(data => {
            const nmList = data[filterKey]?.map(d => d.nm).sort() || []; // 데이터 목록 에서 nm 값을 가져와 정렬
            const key = nmList.length ? nmList.join(' | ') : '미설정'; // nmList가 없으면 '미설정'으로 처리
            
            if (!grouped[key]) {
                grouped[key] = [];
            }
            grouped[key].push(data); // 동일한 nmList를 가진 prcs 객체를 같은 그룹에 추가
        });

        // 그룹들을 배열로 변환하여 정렬
        const sortedGroups = Object.keys(grouped)
            .map(key => ({
                title: key,
                data: grouped[key],
                [seq]: grouped[key]?.[0]?.[filterKey]?.[0]?.[seq] ?? 99
            }))
            .sort((a, b) => {
                if (a.title === '미설정') return 1; // '미설정'을 뒤로 보냄
                if (b.title === '미설정') return -1;
                
                //seq가 존재하지 않을 경우 맨뒤로 보냄 
                return a[seq] - b[seq]; // seq 기준 오름차순
            });
        return sortedGroups;
    }
    // -----------------------------------------

    const downloadReport = blobData => {
        let link = document.createElement('a');
        let objectUrl = window.URL.createObjectURL(blobData.data);
        link.href = objectUrl;
        link.download = blobData.headers.filename;
        // 링크를 DOM에 추가
        document.body.appendChild(link);
        // 클릭 이벤트 발생
        link.click();
        // 링크를 DOM에서 제거
        document.body.removeChild(link);
    };

    const baseDownload = (fetchData, fetchParam) => {
        // 공통 출력물 함수
        // @param {function} fetchData // API 호출 함수
        // @param {Object} fetchParam // API 호출 함수 실행 시 필요한 파라미터 및 출력물 타입 (ex.pdf, xls 등)
        const buttonStore = useButtonListStore();
        fetchParam.type = buttonStore.downloadType;
        loadingPanelStore.openLoading();
        iDownloadReport(fetchData, fetchParam)
            .then(res => {
                loadingPanelStore.endLoading();
            })
            .catch(() => {
                loadingPanelStore.endLoading();
            });
    };

    // -----------------------------------------
    // TODO 수정한 formatDate가 잘 동작하면 주석 지울것
    // const formatDate = (dateString) => {
    //   if (!dateString) {
    //     // dateString이 undefined이거나 빈 문자열일 경우 빈 문자열을 반환
    //     return '';
    //   }

    //   // 날짜와 시간 부분을 분리하여 날짜 부분만 가져옴
    //   let datePart = dateString.split(' ')[0]; // '2024-09-11' 또는 '20240911'

    //   if (datePart.includes('-')) {
    //     // '-'가 있으면 그대로 '-'를 '.'로 변경하여 반환
    //     return datePart.replace(/-/g, '.'); // '2024.09.11'
    //   } else if (datePart.length === 8) {
    //     // '-'가 없고, 날짜 문자열이 8자 (YYYYMMDD)인 경우 'YYYY.MM.DD'로 포맷팅
    //     return `${datePart.slice(0, 4)}.${datePart.slice(4, 6)}.${datePart.slice(6)}`; // '2024.09.11'
    //   }

    //   // 그 외의 경우는 그대로 반환
    //   return datePart;
    // };

    // kyj
    //[날짜 변환 - ex) '2024-10-04 13:35:52.46649' =>  dateUtil.js에 설정된 날짜 포맷으로 변환]
    //[날짜 변환 - ex) '2024-10-04' =>  dateUtil.js에 설정된 날짜 포맷으로 변환]
    //[날짜 변환 - ex) '20241004' =>  dateUtil.js에 설정된 날짜 포맷으로 변환]
    const formatDate = dateString => {
        //console.log('1.formatDate='+dateString);

        // dateString이 undefined이거나 빈 문자열일 경우 원래 값 반환
        if (!dateString) return dateString;

        // 날짜와 시간 부분을 분리하여 날짜 부분만 가져옴
        let datePart = dateString.split(' ')[0]; // '2024-09-11' 또는 '20240911'

        // 숫자가 아닌 문자 제거
        let strDate = datePart.replace(/\D/g, '');

        // console.log('2.formatDate='+getFormattedDate(strDate));
        return getFormattedDate(strDate);
    };

    // kyj
    //[8자리 날짜 문자열을 날짜 포맷으로 변환하는 함수 - ex) '20241004' =>  dateUtil.js에 설정된 날짜 포맷]
    const getFormattedDate = field => {
        //console.log('1.getFormattedDate='+field);

        if (!field || field.length !== 8) return field; // 유효성 검사 실패하면 원래 값 반환

        const year = parseInt(field.substring(0, 4), 10);
        const month = parseInt(field.substring(4, 6), 10) - 1; // JS의 Month는 0부터 시작
        const day = parseInt(field.substring(6, 8), 10);
        //console.log('2.getFormattedDate='+year+'/'+month+'/'+day);

        const date = new Date(year, month, day);
        if (isNaN(date)) return field; // 올바른 Date가 아니라면 실패하면 원래 값 반환

        //console.log('3.getFormattedDate='+format(date, getDateFormat()));
        return format(date, getDateFormat()); // 설정된 날짜 포맷으로 반환
    };

    // kyj
    //[현재 날짜를 가져오는 함수 (dateUtil.js에 설정된 날짜 포맷으로 변환해서 반환)]
    const getCurrentDate = () => {
        const now = new Date();
        return format(now, getDateFormat()); // 설정된 날짜 포맷으로 반환
    };

    // 현재날짜로 부터 month만큼 이전 날짜 (dateUtil.js에 설정된 날짜 포맷으로 변환해서 반환)
    const getPreMonth = month => {
        const now = new Date();
        now.setMonth(now.getMonth() - month);
        return format(now, getDateFormat()); // 설정된 날짜 포맷으로 반환
    };

    // BHJ
    // [DB 저장을 위해 YYYYMMDD 포맷으로 변환하는 함수]
    const formatDateForDB = dateString => {
        // console.log('1.formatDateForDB='+dateString);

        // dateString이 undefined이거나 빈 문자열일 경우 원래 값 반환
        if (!dateString) return dateString;

        // 날짜와 시간 부분을 분리하여 날짜 부분만 가져옴
        let datePart = dateString.split(' ')[0]; // '2024-09-11' 또는 '20240911'

        // 숫자가 아닌 문자 제거 -> YYYYMMDD 만 남음
        let strDate = datePart.replace(/\D/g, '').slice(0, 8);

        // console.log('2.formatDateForDB='+strDate);
        return strDate;
    };

    const getDate = () => {
        const date = new Date();

        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0');
        const dd = String(date.getDate()).padStart(2, '0');

        const formatted = `${yyyy}${mm}${dd}`;
        return formatted
    }

    //-----------------------------------------------

    // -----------------------------------------
    // kyj

    //[금액 000단위 함수]

    const formatToManWon = amount => {
        if (typeof amount !== 'number' || amount === 0) {
            return ''; // 숫자가 아닐 경우 예외 처리
        }

        // 숫자를 000 단위로 쉼표를 붙여서 변환
        const formattedAmount = amount.toLocaleString('ko-KR');

        // 뒤에 '만원'을 붙여서 반환
        return formattedAmount;
    };

    // -----------------------------------------
    //[금액 만원생략 함수]
    const formatToAmt = amount => {
        // amount가 숫자가 아니면 숫자로 변환 시도
        if (typeof amount !== 'number') {
            amount = Number(amount);
        }

        // 변환 후에도 숫자가 아닌 경우 처리
        if (isNaN(amount)) {
            amount = 0;
        }

        // 숫자를 만원 단위로 변환
        return (amount / 10000).toLocaleString('ko-KR');
    };
    // -----------------------------------------
    // 시간 범위를 입력받아 분 단위 차이를 계산하는 함수
    const differenceInMinutes = ref();
    const calculateMinutes = timeRange => {
        if (!timeRange) {
            return;
        }
        if (!timeRange.includes('~')) {
            differenceInMinutes.value = 0;
            return;
        }

        // 시간 범위를 '~'로 분리
        const [startTime, endTime] = timeRange.split(' ~ ').map(str => str.trim());

        // 시각을 시와 분으로 분리하는 함수
        const parseTime = time => {
            const [hours, minutes] = time.split(':').map(Number);
            return { hours, minutes };
        };

        // 시작 시간과 종료 시간을 시와 분으로 분리
        const start = parseTime(startTime);
        const end = parseTime(endTime);

        // 시작 시간과 종료 시간을 분 단위로 변환
        const startTotalMinutes = start.hours * 60 + start.minutes;
        let endTotalMinutes = end.hours * 60 + end.minutes;

        // 종료 시간이 다음 날로 넘어가는 경우를 처리
        if (endTotalMinutes < startTotalMinutes) {
            endTotalMinutes += 24 * 60; // 24시간 추가
        }

        // 시간 차이 계산
        differenceInMinutes.value = endTotalMinutes - startTotalMinutes;
        return differenceInMinutes.value;
    };

    // -----------------------------------------
    // -----------------------------------------

    //limhs11
    const mediaSize = { mobile: false, tablet: false, pc: false };
    const medizWidth = window.innerWidth;
    if (medizWidth < 768) {
        mediaSize.mobile = false;
        mediaSize.tablet = true;
        mediaSize.pc = true;
    } else if (medizWidth >= 768 && medizWidth < 992) {
        mediaSize.mobile = false;
        mediaSize.tablet = false;
        mediaSize.pc = true;
    } else {
        mediaSize.mobile = false;
        mediaSize.tablet = false;
        mediaSize.pc = false;
    }

    //limhs11
    const searchPage = (res, gridEquipment, gridPageEquipment, searchParam, setPage) => {
        if (res && res.list && gridEquipment.value) {
            gridEquipment.value.resetData(res.list);
            if (gridPageEquipment.value) {
                gridPageEquipment.value.pagination.reset(res.totalCount);
                if (!setPage) {
                    gridPageEquipment.value.pagination.movePageTo(searchParam.value.curPage);
                }
            }
            if (res.totalCount == 0) {
                console.log(res.totalCount);
                toastPopup('0건', '검색 조건의 데이터가 없습니다.', 'info');
            }
        }
    };

    //limhs11
    const stringToHtml = htmlString => {
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = htmlString;
        return tempDiv.firstChild;
    };
    //limhs11
    const dateFormat = obj => {
        if (obj) {
            const dashObj = obj.replace(/-/g, '.');
            return dashObj.slice(0, 19);
        }
    };

    const numberFormat = obj => {
        if (obj.value) {
            const [integerPart, decimalPart] = String(obj.value).split('.');
            const formattedIntegerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            return decimalPart ? `${formattedIntegerPart}.${decimalPart}` : formattedIntegerPart;
        }
    };

    // 로딩 패널
    const openLoading = () => {
        return loadingPanelStore.openLoading();
    };

    const endLoading = () => {
        return loadingPanelStore.endLoading();
    };
    // 라우터 이동
    const routerObj = useRouter();
    const goRouter = (name, param) => {
        const toRouter = {
            name: name,
            state: param
        };
        routerObj.push(toRouter);
    };

    const setRouterParam = () => {
        if (history.state.writeYear) {
            const { writeYear, docType, docNo, docSeq, docDetailSeq, date } = history.state;
            const param = {
                writeYear: writeYear,
                docType: docType,
                docNo: docNo,
                docSeq: docSeq,
                docDetail: docDetailSeq,
                date: date
            };
            return param;
        }
        else if(history.state.server_id) {
            const { server_id } = history.state;
            const param = {
                server_id: server_id
            };
            return param;
        }
        else if(history.state.cctv_id) {
            const { cctv_id } = history.state;
            const param = {
                cctv_id: cctv_id
            };
            return param;
        }        
        return null;
    };

    // 같은 레벨의 서명을 순차적으로 저장
    // param = [서명컴포넌트ref1,서명컴포넌트ref2,서명컴포넌트ref3, ... ]
    const saveSignAsync = async (component, refs) => {
        if (component === 'iHrChipsSign') {
            // iHrChipsSign 컴포넌트일 경우
            for (let i = 0; i < refs.length; i++) {
                const { ref, docType, writeYear, docNo, docSeq, docDetailSeq } = refs[i];
                const success = await ref.setHrChipsApprovalInfo(docType, writeYear, docNo, docSeq, docDetailSeq);
                if (!success) break;
            }
            return true;
        } else if (component === 'iSignature') {
            // 기본 서명 컴포넌트일 경우
            for (let i = 0; i < refs.length; i++) {
                const { ref, docType, writeYear, docNo, docSeq, docDetailSeq } = refs[i];
                const success = await ref.setApprovalInfo(docType, writeYear, docNo, docSeq, docDetailSeq);
                if (!success) break;
            }
            return true;
        }
    };
    // 탭 메뉴생성 권한을 체크하기 위함
    // param = 메뉴ID (라우터명)
    const getTabMenuAuth = menuId => {
        return router.getRoutes().some(route => route.name === menuId);
    };

    // 중복된 데이터를 추출하여 중복된 데이터 목록과 중복되지 않은 데이터 return
    // @param
    // originDataList => 비교하고자 하는 원본 데이터 목록 (List<Object>)
    // checkDataList => 비교하고자 하는 선택된 데이터 목록 (List<Object>)
    // originKey => 비교를 위한 Key 값 (originDataList의 키값 ex.orgnId or [orgnId, compId])
    // nameKey => 메시지 출력을 위한 값 (originDataList의 이름값 ex.orgnNm)
    // checkKey => 비교를 위한 Key 값 (checkDataList 키값 ex.tempOrgnId)
const getDuplicatedData = (originDataList, checkDataList, originKey, nameKey, checkKey = null) => {
    if (checkKey == null) checkKey = originKey;

    let returnData = {
        text: '', // 중복된 데이터 리스트
        duplicatedDataList: [], // 중복된 데이터
        deDuplicatedDataList: [] // 중복되지않은 데이터
    };

    // 단일 키인 경우
    if (!Array.isArray(originKey)) {
        const originIds = originDataList.map(item => item[originKey]); // 기존 데이터 ID만 추출
        returnData.duplicatedDataList = checkDataList.filter(data => originIds.includes(data[checkKey])); // 적용하고자하는 ID를 기존 데이터에서 중복체크
        returnData.deDuplicatedDataList = checkDataList.filter(data => !originIds.includes(data[checkKey])); // 중복되어있지 않은데이터
    }else{
        // 복합 키인 경우
        const isDuplicated = (checkData) => originDataList.some(originData => originKey.every(key => originData[key] === checkData[key]));
        checkDataList.forEach(data => {(isDuplicated(data) ? returnData.duplicatedDataList : returnData.deDuplicatedDataList).push(data);});
    }

    if (returnData.duplicatedDataList.length > 0) {
        // 중복된 예시보기 데이터가 있을 경우
        returnData.text = returnData.duplicatedDataList.map(data => `- ${data[nameKey]}`).join('\n');
    }
    return returnData;
};

    /** ✏️ return */
    return {
        ref,
        reactive,
        shallowRef,
        provide,
        inject,
        nextTick,
        getFormattedDate,
        onBeforeMount,
        onMounted,
        onBeforeUpdate,
        onUpdated,
        onBeforeUnmount,
        onUnmounted,
        watch,
        watchEffect,
        computed,
        mergeProps,
        defineExpose,
        router,
        getCompId,
        // format, formatDistance, differenceInDays, subDays, subMonths,
        getCurrentDate,
        getPreMonth,
        formatDate,
        downloadReport,
        formatToManWon,
        formatToAmt,
        storeToRefs,
        swal,
        swalOption,
        globalEnvs,
        confirmMsg,
        alertMsg,
        mediaSize,
        groupAndSortByNm,
        searchPage,
        stringToHtml,
        dateFormat,
        numberFormat,
        toastPopup,
        t,
        validationStore,
        btnSearch,
        btnBack,
        btnSave,
        btnAdd,
        btnDelete,
        btnEdit,
        btnDownload,
        btnNew,
        btnPreYear,
        btnCall,
        btnDownloadForm,
        btnUpload,
        btnSample,
        btnSuggestionSearch,
        btnSuggestionAdd,
        btnCopy,
        btnWorkerCheck,
        calculateMinutes,
        openLoading,
        endLoading,
        baseDownload,
        formatDateForDB,
        getDate,
        goRouter,
        setRouterParam,
        saveSignAsync,
        getTabMenuAuth,
        getDuplicatedData
    };
}
