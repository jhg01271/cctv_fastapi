import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
import { getTrainingPlanImpl, getTrainingPlanImplDetail, saveTrainingPlanImpl, deleteTrainingPlanImpl, deleteTrainingPlanImpls, getTrainingPlanImplReport, getTrainingPlanImplCurrentAndPreviousYear, saveTrainingPlanImplCurrentAndPreviousYear } from '@/stores/safewiz/support/api/SAndHTrainingImplApi.js';
const { formatDate, baseDownload, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

import _ from 'lodash';
export const useSAndHTrainingImplStore = defineStore('SAndHApi', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));

    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value
    });

    const checkedList = ref([]);
    const currentInputForm = ref({});
    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            writeYear: writeYear.value,
            docType: 'TPI',
            docNo: '',
            title: '',
            useYn: 'Y',
            createdAt: getCurrentDate(),
            hrList: [],
            trainingDate: '',
            trainingAt: '',
            trainingDuration: '',
            trainingType: '',
            budget: '',
            requiredDetails: '',
            trainingGoal: '',
            expectedEffect: '',
            materialsEtc: '',
            materials: '',
            projector: '',
            vtr: '',
            trainingContent: '',
            trainingInstitute: '',
            trainingInstructor: '',
            trainingLocation: '',
            qnaYn: 'N',
            reportYn: 'N',
            deliveryTrainingYn: 'N',
            testYn: 'N',
            effectivenessEtc: '',
            remark: '',
            chargeList: []
        };
    };
    const safetyHealthTrainingPlans = ref([]);
    const currentSafetyHealthTrainingPlans = ref([]);

    // btn function
    const btnSearch = async notify => {
        searchParam.value.writeYear = writeYear.value;
        openLoading();
        getTrainingPlanImpl(searchParam.value, notify)
            .then(res => {
                afterSearch(res);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const getCurrentAndPreviousYear = async notify => {
        searchParam.value.writeYear = writeYear.value;
        openLoading();
        getTrainingPlanImplCurrentAndPreviousYear(searchParam.value, notify)
            .then(res => {
                if (res.list.length > 0) {
                    res.list.forEach(el => {
                        if (el.writeYear !== searchParam.value.writeYear) {
                            el.checked = true;
                        }
                        let countNm = el.hrList.length > 1 ? ` 외 ${el.hrList.length - 1} 명` : ``;
                        let hrName = el.hrList.length > 0 ? el.hrList[0].hrNm : '';
                        el.detail = {
                            교육일자: formatDate(el.trainingDate),
                            인원: hrName + countNm,
                            '과목/과정': el.trainingTypeNm,
                            교육장소: el.trainingLocationNm
                        };
                    });
                    safetyHealthTrainingPlans.value.push(...res.list);
                    addMonthList();
                    addTrainingTypeList();
                    loadPreviousYn.value = true;
                } else {
                    alertMsg('warning', '전년도 데이터가 없습니다.'); // 메시지 띄우기
                    loadPreviousYn.value = false;
                    return;
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const afterSearch = res => {
        res.list.forEach(el => {
            el.checked = false;
            const hrCount = el.hrList.length;
            const hrName = hrCount > 0 ? el.hrList[0].hrNm : '';
            const countNm = hrCount > 1 ? ` 외 ${hrCount - 1} 명` : '';

            // detail 생성
            el.detail = {
                교육일자: formatDate(el.trainingDate),
                인원: hrName + countNm,
                '과목/과정': el.trainingTypeNm,
                교육장소: el.trainingLocationNm
            };
        });

        // 데이터 업데이트
        safetyHealthTrainingPlans.value = res.list;
        currentSafetyHealthTrainingPlans.value = _.cloneDeep(res.list);

        // 그룹화 함수 호출
        addMonthList();
        addTrainingTypeList();
    };

    const filteredByMonthList = ref({});
    const addMonthList = () => {
        const result = {};

        safetyHealthTrainingPlans.value.forEach(list => {
            const monthKey = list.trainingDate ? list.trainingDate.substring(4, 6) + '월' : '미설정';

            if (!result[monthKey]) result[monthKey] = [];
            result[monthKey].push(list);
        });

        // result가 빈값이면 현재 달 빈값 추가
        if (Object.keys(result).length === 0) {
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            result[currentMonth] = [];
        }

        // 정렬
        const sortedKeys = Object.keys(result).sort((a, b) => {
            const aIsNumber = /^\d/.test(a);
            const bIsNumber = /^\d/.test(b);
            if (aIsNumber && bIsNumber) return b.localeCompare(a, undefined, { numeric: true });
            if (aIsNumber) return 1;
            if (bIsNumber) return -1;
            return b.localeCompare(a);
        });

        // 정렬된 키로 결과 재구성
        const sortedResult = {};
        sortedKeys.forEach(key => {
            sortedResult[key] = result[key];
        });
        filteredByMonthList.value = _.cloneDeep(sortedResult);
        filteredByMonthListBySearch.value = _.cloneDeep(sortedResult);
    };

    const filteredByTrainingTypeList = ref({});
    const addTrainingTypeList = () => {
        const result = {};
        safetyHealthTrainingPlans.value.forEach(list => {
            const typeKey = list.trainingTypeNm || '미설정';

            if (!result[typeKey]) result[typeKey] = [];
            result[typeKey].push(list);
        });

        // result가 빈값이면 현재 달 빈값 추가
        if (Object.keys(result).length === 0) {
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            result[currentMonth] = [];
        }

        // 정렬
        const sortedKeys = Object.keys(result).sort((a, b) => {
            const aIsNumber = /^\d/.test(a);
            const bIsNumber = /^\d/.test(b);
            if (aIsNumber && bIsNumber) return a.localeCompare(b, undefined, { numeric: true });
            if (aIsNumber) return -1;
            if (bIsNumber) return 1;
            return a.localeCompare(b);
        });

        // 정렬된 키로 결과 재구성
        const sortedResult = {};
        sortedKeys.forEach(key => {
            sortedResult[key] = result[key];
        });

        filteredByTrainingTypeList.value = _.cloneDeep(sortedResult);
        filteredByTrainingTypeListBySearch.value = _.cloneDeep(sortedResult);
    };

    //상세 조회
    const btnDetail = async (data, notify = true) => {
        let param = _.cloneDeep(searchParam.value);
        param.writeYear = data.writeYear;
        param.docType = 'TPI'; //data.docType
        param.docNo = data.docNo;
        openLoading();
        getTrainingPlanImplDetail(param, notify)
            .then(res => {
                res.list.trainingDate = formatDate(res.list.trainingDate);
                res.list.hrList.forEach(i => {
                    (i.id = i.hrId), (i.name = i.hrNm);
                });
                currentInputForm.value = _.cloneDeep(res.list);
                inputForm.value = res.list;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnSave = () => {
        let saveParam = _.cloneDeep(inputForm.value);
        if (saveParam.trainingDate == inputForm.value.trainingDate) {
            saveParam.trainingDate = formatDateForDB(inputForm.value.trainingDate);
        }
        // trainingPeriod 값을 '~'을 기준으로 분리하여 시작 시간과 종료 시간 추출
        if (saveParam.trainingPeriod) {
            const [trainingStart, trainingEnd] = saveParam.trainingPeriod.split(' ~ ').map(time => time.trim());

            // 추출된 값을 saveParam에 저장
            saveParam.trainingStart = trainingStart;
            saveParam.trainingEnd = trainingEnd;
        }
        confirmMsg('info', t('msgSave'), null, { fun: save, param: [saveParam] });
    };

    const save = async data => {
        openLoading();
        saveTrainingPlanImpl(data, true)
            .then(res => {
                if (res.result) {
                    // 신규일때 서명정보 저장
                    if (data[0].cmd === 'I') {
                        signature.value.setApprovalInfo('TPI', res.result.writeYear, res.result.docNo);
                    } else {
                        signature.value.updateTaskUseYn('TPI', res.result.writeYear, res.result.docNo);
                    }
                }
                btnDetail(res.result, false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
                layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
            });
    };

    //전년도 불러오기 저장
    const btnSavePreviousYear = () => {
        let param = getCheckedList();

        // 체크된거 넘겨주는거 해야됨
        if (loadPreviousYn.value) {
            confirmMsg('info', t('msgSave'), '', {
                fun: () => {
                    openLoading();
                    saveTrainingPlanImplCurrentAndPreviousYear(param, true)
                        .then(res => {
                            loadPreviousYn.value = false;
                            btnSearch(false);
                        })
                        .catch(() => {
                            endLoading();
                        })
                        .finally(() => {
                            endLoading();
                        });
                }
            });
        }
    };

    // 서명 컴포넌트
    const signature = ref(null);

    const btnAdd = async () => {
        if (loadPreviousYn.value) {
            confirmMsg('info', t('msgPreviousConfirm'), '', { fun: btnAddAction, param: '' });
            return;
        } else {
            btnAddAction();
        }
    };
    const btnAddAction = () => {
        loadPreviousYn.value = false;
        initInputForm();
        router.push('/SAndHTrainingImplPlanDetail');
    };
    const btnDelete = () => {
        let param = getCheckedList();
        if (!param.length) {
            alertMsg('error', t('msgNoItem'));
            return;
        }
        if (param.some(el => el.useYn === 'N')) {
            alertMsg('error', t('msgDeletedItem'));
            return;
        }
        confirmMsg('warning', t('msgDelete'), ``, {
            fun: deleteApi,
            param: param
        });
    };
    const deleteApi = async param => {
        openLoading();
        deleteTrainingPlanImpls(param)
            .then(async res => {
                for (let item of param) {
                    await signatureStore.forcedUpdateTaskUseYn('N', 'TPI', item.writeYear, item.docNo);
                }
                btnSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDownload = async (list, isDetail = false) => {
        if (isDetail) {
            confirmMsg('info', t('msgPrint'), null, { fun: downloadReports, param: list });
        } else {
            if (list.length > 0) {
                confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports, param: list });
            } else {
                alertMsg('warning', t('msgNoItem'));
            }
        }
    };
    const downloadReports = async list => {
        let searchVO = _.cloneDeep(searchParam.value);
        searchVO.docType = 'TPI';
        searchVO.checkedList = list;
        baseDownload(getTrainingPlanImplReport, searchVO);
    };

    //전년도 불러오기
    const loadPreviousYn = ref(false);
    const btnPreYear = async () => {
        getCurrentAndPreviousYear(false);
    };

    const btnDeleteDetail = async () => {
        let deleteParam = _.cloneDeep(inputForm.value);
        confirmMsg('info', t('msgDelete'), null, { fun: deleteDetailApi, param: deleteParam });
    };

    const deleteDetailApi = async data => {
        openLoading();
        deleteTrainingPlanImpl(data, true)
            .then(async res => {
                await signatureStore.forcedUpdateTaskUseYn('N', 'TPI', data.writeYear, data.docNo);
                btnDetail(res.result, false);
            })
            .finally(() => {
                endLoading();
            });
    };

    // etc function
    const getCheckedList = () => {
        let checkedData = [];
        if (currentFilter.value === 'calendar') {
            Object.keys(filteredByMonthListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByMonthListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        } else if (currentFilter.value === 'subjects') {
            Object.keys(filteredByTrainingTypeListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByTrainingTypeListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        }
        return checkedData;
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    const filteredByMonthListBySearch = ref({});
    const filteredByTrainingTypeListBySearch = ref({});

    const applyFilter = () => {
        if (!searchTerm.value) {
            safetyHealthTrainingPlans.value = _.cloneDeep(currentSafetyHealthTrainingPlans.value);
            // 그룹화 함수 호출
            addMonthList();
            addTrainingTypeList();
            return;
        }
        // 조직
        if (currentFilter.value == 'calendar') {
            for (const key in filteredByMonthList.value) {
                // 모든 필드를 비교하여 필터링
                const filteredData = filteredByMonthList.value[key].filter(item => key.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.title.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.trainingDate).toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.location?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingLocationNm?.toLowerCase().includes(searchTerm.value.toLowerCase()));

                // 필터 결과 저장
                filteredByMonthListBySearch.value[key] = filteredData;
            }
            // 빈 배열인 항목 제거
            const filteredData = Object.fromEntries(Object.entries(filteredByMonthListBySearch.value).filter(([key, value]) => Array.isArray(value) && value.length > 0));
            filteredByMonthListBySearch.value = filteredData;
        }

        // 과목/과정 필터
        if (currentFilter.value == 'subjects') {
            for (const key in filteredByTrainingTypeList.value) {
                const filteredData = filteredByTrainingTypeList.value[key].filter(item => key.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.title.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.trainingDate).toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.location?.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingLocationNm?.toLowerCase().includes(searchTerm.value.toLowerCase()));
                filteredByTrainingTypeListBySearch.value[key] = filteredData;
            }
        }
    };

    // 필터 관리
    const searchTerm = ref('');
    const filterDivList = ref([
        { id: 'calendar', name: '월별' },
        { id: 'subjects', name: '교육' }
    ]);
    const currentFilter = ref('calendar');

    return {
        writeYear,
        searchParam,
        inputForm,
        currentInputForm,
        searchTerm,
        filterDivList,
        currentFilter,
        filteredByMonthList,
        filteredByTrainingTypeList,
        filteredByMonthListBySearch,
        filteredByTrainingTypeListBySearch,
        checkedList,
        loadPreviousYn,
        signature,
        // function
        initInputForm,
        applyFilter,
        getCheckedList,
        toggleUseYn,
        // api
        btnSearch,
        btnDetail,
        btnAdd,
        btnSave,
        btnDelete,
        btnDownload,
        btnPreYear,
        btnSavePreviousYear,
        btnDeleteDetail
    };
});
