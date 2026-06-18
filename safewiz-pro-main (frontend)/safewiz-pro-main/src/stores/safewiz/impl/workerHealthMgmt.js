import { defineStore } from 'pinia';
import { nextTick } from 'vue';
import { gsap } from 'gsap';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, t, computed, formatDate, baseDownload, alertMsg, confirmMsg, getCompId, getCurrentDate, openLoading, endLoading, formatDateForDB } = BaseView();

import { getWHMgmtGuide, getWHMgmtGuideDetail, saveWHMgmtGuide, validateGuideByOrgn, saveWHMgmtGuideByOrgn, deleteWHMgmtGuide, deleteWHMgmtGuideDetail, getWHMgmtGuideReport, getWHMgmtGuideRequestReport } from '@/stores/safewiz/impl/api/workerHealthMgmtApi';
import { Exception } from 'sass';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi';
import _ from 'lodash';

import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

export const useWorkerHealthMgmtStore = defineStore('workerHealthMgmt', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const currentDate = ref(getCurrentDate());
    const fileInfo = ref([]);
    // 파일 ref
    const file = file => {
        fileInfo.value = file.value;
    };

    const originData = ref({});

    const inputForm = ref({});
    const currentInputForm = ref({});
    // const inputForm = ref({
    //     hrListH: ref([]),  // ⬅ 여기서 배열을 ref로 감싸기
    //     hrListR: ref([]),
    //     hrListC: ref([]),
    // });

    const checkedList = ref([]);
    const searchedData = ref([]);
    const filteredData = ref([]);
    const searchText = ref('');

    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value,
        docNo: '',
        docType: 'WHMG'
    });

    const signature = ref(null);
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            writeYear: writeYear,
            docType: 'WHMG',
            docNo: '',
            compId: compId,
            hrId: '',
            hrNm: '',
            orgnNm: '',
            sex: '',
            age: '',
            serviceYears: '',
            healthOpinion: null,
            aftercareOpinion: null,
            correctiveAction: null,
            workAptitudeYn: 'Y',
            counselReason: null,
            counselContent: null,
            counselResult: null,
            counselDt: currentDate.value,
            counselCnt: '',
            requestCnt: '',
            counselCmd: 'I',
            requestCmd: 'I',
            counselUseYn: 'Y',
            requestUseYn: 'Y',
            hrListH: [],
            hrListR: [],
            hrListC: [],
            useYn: 'Y',
            createdAt: null,
            createdBy: null,
            counselChecked: 'N',
            requestChecked: 'N'
        };
    };

    // 데이터 조회
    const getWHMgmtGuidList = notify => {
        openLoading();
        return new Promise(resolve => {
            getWHMgmtGuide(searchParam.value, notify)
                .then(res => {
                    searchedData.value = res.list;
                    const groupedData = groupByYear(searchedData.value);
                    filteredData.value = groupedData;

                    filter();
                    // resolve({ list: searchedData.value, totalCount: res.totalCount })
                })
                .finally(() => endLoading());
        });
    };
    // 배열 내 counselDt 값의 month 2자리를 추출하여 같은 값끼리 그룹화하는 함수
    const groupByYear = searchedData => {
        const grouped = searchedData.reduce((groups, item) => {
            const month = item.counselDt.substring(4, 6); // MM 추출
            if (!groups[month]) {
                groups[month] = [];
            }
            groups[month].push(item);
            return groups;
        }, {});

        // 월을 숫자로 변환해서 내림차순 정렬 후 매핑
        return Object.keys(grouped)
            .sort((a, b) => Number(b) - Number(a)) // 최근 월이 먼저
            .map(month => ({
                month: month + '월',
                data: grouped[month]
            }));
    };

    // 디테일 조회
    const getWHMgmtGuideDetailList = notify => {
        openLoading();
        return new Promise(resolve => {
            getWHMgmtGuideDetail(searchParam.value, notify)
                .then(res => {
                    if (res.list.hrListH.length === 0) {
                        // 상담자가 없을 때 자동으로 로그인 유저 세팅
                        res.list.hrListH = [{ hrId: userInfoStore.userId, hrNm: userInfoStore.userName, targetType: 'WHMG' }];
                    }
                    if (res.list.hrListR.length === 0) {
                        // 상담자가 없을 때 자동으로 로그인 유저 세팅
                        res.list.hrListR = [{ hrId: userInfoStore.userId, hrNm: userInfoStore.userName, targetType: 'WHMG' }];
                    }
                    if (res.list.hrListC.length === 0) {
                        // 내담자가 없을 때 자동으로 카드 근로자 유저 세팅팅
                        res.list.hrListC = [{ hrId: res.list.hrId, hrNm: res.list.hrNm, targetType: 'WHMG' }];
                    }
                    ['hrListH', 'hrListR', 'hrListC'].forEach(listKey => {
                        res.list[listKey].forEach(el => {
                            el.id = el.hrId;
                            el.name = el.hrNm;
                        });
                    });
                    res.list.counselDt = formatDate(res.list.counselDt);
                    originData.value = _.cloneDeep(res.list);

                    // 리스트에 따라 I, U 판별
                    if (res.list.hrListH && res.list.hrListH.length > 0) {
                        res.list.counselCmd = 'U';
                    } else {
                        res.list.counselCmd = 'I';
                    }

                    if (res.list.hrListR && res.list.hrListR.length > 0) {
                        res.list.requestCmd = 'U';
                    } else {
                        res.list.requestCmd = 'I';
                    }

                    inputForm.value = res.list;              
                    inputForm.value.sexDivNm = inputForm.value.sex === 'M' ? t('male') : inputForm.value.sex === 'W' ? t('female') : '';
                    currentInputForm.value = _.cloneDeep(inputForm.value)
                    resolve(true);
                })
                .finally(() => endLoading());
        });
    };

    // 조직 불러오기로 저장 시 유효성 검증(이미 데이터가 있는지, 없다면 몇명이 없는지지)
    const validateGuidePopup = param => {
        return new Promise(resolve => {
            // openLoading();
            validateGuideByOrgn({ orgnId: param.value.orgnId, counselDt: param.value.counselDt }, false)
                .then(async res => {
                    resolve({ data: res, success: res.success });
                })
                .finally(() => {
                    endLoading();
                });
        });
    };
    // 조직 불러오기로 저장
    const saveWHMgmtGuidePopup = saveParam => {
        openLoading();
        return new Promise(() => {
            saveWHMgmtGuideByOrgn(saveParam, true)
                .then(async res => {
                    if (res.success) {
                        await getWHMgmtGuidList(false);
                    }
                })
                .finally(() => {
                    endLoading();
                });
        });
    };
    // 삭제
    const delWHMgmtGuides = saveParam => {
        openLoading();
        return new Promise(resolve => {
            deleteWHMgmtGuide(saveParam, true)
                .then(res => {
                    getWHMgmtGuidList(false);
                    // resolve({ list: res.list, totalCount: res.totalCount })
                    resolve(res);
                })
                .finally(() => endLoading());
        });
    };

    // 디테일 삭제
    const delWHMgmtGuideDetail = saveParam => {
        openLoading();
        return new Promise(resolve => {
            deleteWHMgmtGuideDetail(saveParam, true)
                .then(res => {
                    getWHMgmtGuideDetailList(false);
                    // resolve({ list: res.list, totalCount: res.totalCount })
                    resolve(res);
                })
                .finally(() => endLoading());
        });
    };

    // -------------------------------------------------

    // 신규 추가 이동
    const btnAdd = () => {
        initInputForm();
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
        router.push({
            name: 'WorkerHealthMgmtGuidelinesDetail'
        });
    };

    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        initInputForm();
        router.push({ name: 'WorkerHealthMgmtGuidelines' });
    };

    // 상세보기 이동
    const goDetail = () => {
        (inputForm.value.cmd = 'U'),
            router.push({
                name: 'WorkerHealthMgmtGuidelinesDetail'
            });
        // getWHMgmtGuideDetailList(false);
    };

    const btnDownload = async list => {
            let searchVO = _.cloneDeep(searchParam.value);
            searchVO.docType = 'WHMG';
            searchVO.checkedList = list;
            baseDownload(getWHMgmtGuideReport, searchVO);
            // openLoading();
            // getWHMgmtGuideReport(searchVO).then(res => {
            //   downloadReport(res)
            // }).finally(() => endLoading());
    };

    const btnDetailDownload = async list => {
        let searchVO = _.cloneDeep(searchParam.value);
        searchVO.docType = 'WHMG';
        searchVO.checkedList = list;
        baseDownload(getWHMgmtGuideRequestReport, searchVO);
        // openLoading();
        // getWHMgmtGuideRequestReport(searchVO).then(res => {
        //   downloadReport(res)
        // }).finally(() => endLoading());
    };

    // -------------------------------------------------
    const btnDelete = async () => {
        const checkedList = segments.value.flatMap(el => el.data.filter(id => id.checked));
        segments.value.forEach(el => el.data.forEach(id => id.checked == true));

        if (checkedList.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        confirmMsg('warning', t('msgDelete'), '', {
            fun: delWHMgmtGuides,
            param: checkedList
        });
    };

    // -------------------------------------------------
    const btnDetailDelete = async () => {
        if (inputForm.value.counselChecked === 'Y' || inputForm.value.requestChecked === 'Y') {
            // if (inputForm.value.counselChecked, inputForm.value.requestChecked)
            confirmMsg('warning', t('msgDelete'), '', {
                fun: delWHMgmtGuideDetail,
                param: inputForm.value
            });
        } else {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
    };
    // -------------------------------------------------

    //저장
    const btnSave = async () => {
        openLoading();
        // 체크 x 시 'N'넘겨줌
        if (!inputForm.value.counselChecked) {
            inputForm.value.counselChecked = 'N';
        }
        if (!inputForm.value.requestChecked) {
            inputForm.value.requestChecked = 'N';
        }
        // cmd 값 I 일때 하위 I
        if (inputForm.value.cmd === 'I') {
            inputForm.value.counselCmd = 'I';
            inputForm.value.requestCmd = 'I';
        }

        // 리스트에 따라 I, U 판별
        if (inputForm.value.hrListH && inputForm.value.hrListH.length > 0) {
            inputForm.value.counselCmd = 'U';
        } else {
            inputForm.value.counselCmd = 'I';
        }

        if (inputForm.value.hrListR && inputForm.value.hrListR.length > 0) {
            inputForm.value.requestCmd = 'U';
        } else {
            inputForm.value.requestCmd = 'I';
        }

        //상담시간
        if (inputForm.value.counselTime) {
            const [counselStart, counselEnd] = inputForm.value.counselTime.split(' ~ ').map(time => time.trim());

            // 추출된 값을 saveParam에 저장
            inputForm.value.counselStart = counselStart;
            inputForm.value.counselEnd = counselEnd;
        }
        //저장시 상담일자 포맷팅
        inputForm.value.counselDt = inputForm.value.counselDt ? formatDateForDB(inputForm.value.counselDt) : null;

        const formData = new FormData();
        inputForm.value.deleteFiles = fileInfo.value.editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));
        fileInfo.value.editFiles.insert.forEach(file => {
            if (file) {
                formData.append('files', file); // 파일이 있을 경우 파일 추가
            } else {
                formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
            }
        });
        return new Promise(resolve => {
            saveWHMgmtGuide(formData, true)
                .then(async res => {
                    if (res.success) {
                        inputForm.value.cmd = 'U';

                        // 파라미터 세팅
                        searchParam.value.docNo = res.result.docNo;
                        searchParam.value.compId = res.result.compId;
                        searchParam.value.writeYear = res.result.writeYear;
                        searchParam.value.docType = res.result.docType;
                        resolve(true);
                    }
                })
                .finally(() => {
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
                    endLoading();
                });
        });
    };

    //-----------------------------------------------

    //적합여부 필터

    const yesNoList = ref([
        { workAptitudeYn: 'Y', workAptitudeYnNm: '여' },
        { workAptitudeYn: 'N', workAptitudeYnNm: '부' }
    ]);

    const filteredYN = computed(() => {
        let list = yesNoList.value.filter(element => {
            if (element.yesNoList === '') return true;
            return element.workAptitudeYn !== yesNoList.value.workAptitudeYnNm;
        });
        return list;
    });

    // -------------------------------------------------

    const segments = ref([]);
    const activeSegments = ref({});

    const filter = async () => {
        // 데이터 변환을 한번에 처리하여 성능 개선
        filteredData.value.forEach(el => {
            el.data = el.data.map(id => ({
                ...id,
                detail: {
                    [t('whmg_name')]: id.hrNm,
                    [t('whmg_group')]: id.orgnNm
                },
                detailWithBtn: [
                    {
                        // 건강 상담 일지
                        label: t('whmg_healthDiary'),
                        value: id.counselCnt,
                        button: [
                            {
                                value: 0, // 조직장 서명이 있는 경우
                                label: '미등록',
                                class: 'ready'
                            },
                            {
                                value: 1, // 조직장 서명이 없는 경우
                                label: '등록',
                                class: 'complete'
                            }
                        ]
                    },
                    {
                        // 개선 요청 상담 일지
                        label: t('whmg_improvementConsultationLog'),
                        value: id.requestCnt,
                        button: [
                            {
                                value: 0, // 조직장 서명이 있는 경우
                                label: '미등록',
                                class: 'ready'
                            },
                            {
                                value: 1, // 조직장 서명이 없는 경우
                                label: '등록',
                                class: 'complete'
                            }
                        ]
                    }
                ]
            }));
        });

        if (filteredData.value.length === 0) {
            // 현재 월을 구함 (0부터 시작하므로 +1 필요)
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            // 조회 데이터가 없을 시 현재 월 빈 아코디언 세팅
            filteredData.value[0] = { month: currentMonth };
        }

        segments.value = filteredData.value;

        const currentMonth = new Date().getMonth() + 1; // 현재 월 (0부터 시작하므로 +1을 해야 실제 월이 나옵니다)
        // 현재 해당하는 월에 해당하는 index를 찾기
        let index = filteredData.value.findIndex(item => item.month.slice(0, 2) == currentMonth);

        // 만약 일치하는 항목이 없으면 index를 0으로 설정
        if (index === -1) {
            index = 0;
        }

        // nextTick을 사용하여 DOM이 업데이트된 후 작업 실행
        nextTick(() => {
            const btn = document.getElementById(`accordion-btn_${index}`);
            if (btn != null) {
                const isActive = btn.classList.contains('active');

                if (!isActive) {
                    btn.click();
                }
            }
        });
    };

    //-----------------------------------------------
    //검색기능
    const dataFilter = () => {
        if (searchText.value) {
            const searchValue = searchText.value.toLowerCase(); // 검색어를 소문자로 변환
            const temp = filteredData.value
                .map(row => {
                    // `row.data`에서 검색 조건에 맞는 데이터 필터링
                    const filteredData = row.data.filter(col => {
                        return (
                            (col.orgnNm?.toLowerCase() || '').includes(searchValue) || // orgnNm 검색
                            (col.hrNm?.toLowerCase() || '').includes(searchValue) || // hrNm 검색
                            (row.month?.toLowerCase() || '').includes(searchValue) // month 검색
                        );
                    });

                    // 검색 조건에 맞는 데이터가 있을 경우만 반환
                    if (filteredData.length > 0) {
                        return {
                            ...row,
                            data: filteredData // 필터링된 데이터만 포함
                        };
                    }
                    return null; // 조건에 맞지 않는 row는 제외
                })
                .filter(Boolean); // `null` 값 제거

            segments.value = temp; // 검색된 데이터로 업데이트
        } else {
            // 검색어가 없을 경우 원본 데이터로 초기화
            segments.value = filteredData.value;
        }
    };

    //-----------------------------------------------
    //useYn 체크

    const toggleUseYn = (field, event) => {
        console.log('@@@ ');
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value[field] = _.cloneDeep(event.target.checked ? 'Y' : 'N');
    };

    //-----------------------------------------------
    //아코디언

    const toggleAccordion = async event => {
        const button = event.currentTarget;
        const segmentElement = button.nextElementSibling;

        const isOpen = button.classList.toggle('active');

        await nextTick();

        gsap.to(segmentElement, {
            height: isOpen ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    };

    return {
        inputForm,
        currentInputForm,
        initInputForm,
        checkedList,
        btnDelete,
        filter,
        dataFilter,
        searchText,
        signature,
        originData,
        toggleUseYn,
        toggleAccordion,
        searchParam,
        searchedData,
        currentDate,
        file,
        fileInfo,
        segments,
        activeSegments,
        filteredYN,
        //버튼
        btnAdd,
        goBack,
        goDetail,
        btnSave,
        btnDetailDelete,
        btnDownload,
        btnDetailDownload,
        //API
        getWHMgmtGuidList,
        getWHMgmtGuideDetailList,
        delWHMgmtGuides,
        delWHMgmtGuideDetail,
        validateGuidePopup,
        saveWHMgmtGuidePopup
    };
});
