import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
import { getTrainingResult, getTrainingResultReportDetail, saveTrainingResultReport, deleteTrainingResultReport, deleteTrainingResultReports, getTrainingResultReport, geTtrainingResultCurrentAndPreviousYear, saveTrainingResultCurrentAndPreviousYear } from '@/stores/safewiz/support/api/SAndHTrainingResultApi.js';
const { formatDate, baseDownload, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, openLoading, endLoading } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { getTrainingPlanImplCurrentAndPreviousYear, saveTrainingPlanImplCurrentAndPreviousYear } from '@/stores/safewiz/support/api/SAndHTrainingImplApi.js';
import { useTaskStore } from '@/stores/safewiz/task/task.js';
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
export const useSAndHTrainingResultStore = defineStore('SAndHTrainingResult', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));
    // 서명 컴포넌트
    const signature = ref(null);
    //인원 hrChip 서명
    const inspector = ref(null);
    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value,
        docType: 'TRR',
        docNo: null
    });

    const checkedList = ref([]);

    // 아코디언 관리
    const jobCompAssessSegments = ref([]);

    const inputForm = ref({});

    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            writeYear: writeYear.value,
            docType: 'TRR',
            docNo: null,
            title: '',
            useYn: 'Y',
            createdAt: getCurrentDate(),
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
            trainingStart: null,
            trainingEnd: null,
            trainingPeriod: null,
            hrList: [],
            trainingContent: '',
            trainingInstitute: '',
            trainingInstructor: '',
            trainingLocation: '',
            qnaYn: 'N',
            reportYn: 'N',
            deliveryTrainingYn: 'N',
            testYn: 'N',
            materialsYn: 'N',
            projectorYn: 'N',
            effectivenessEtc: '',
            goodCondition: 'N',
            normalCondition: 'N',
            noConcern: 'N',
            remark: '',
            chargeList: []
        };
    };
    const safetyHealthTrainingPlans = ref([]);
    // btn function
    const btnSearch = async notify => {
        searchParam.value.writeYear = writeYear.value;
        //전년도 불러오기 진행중일때 메시지 띄움
        if (loadPreviousYn.value) {
            confirmMsg('info', t('msgSearch'), t('msgPreviousConfirm'), {
                fun: () => {
                    openLoading();
                    getTrainingResult(searchParam.value, notify)
                        .then(res => {
                            afterSearch(res);
                            loadPreviousYn.value = false;
                        })
                        .catch(() => {
                            endLoading();
                        })
                        .finally(() => {
                            endLoading();
                        });
                }
            });
        } else {
            openLoading();
            getTrainingResult(searchParam.value, notify)
                .then(res => {
                    afterSearch(res);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                });
        }
    };

    const getCurrentAndPreviousYear = async notify => {
        searchParam.value.writeYear = writeYear.value;
        openLoading();
        geTtrainingResultCurrentAndPreviousYear(searchParam.value, notify)
            .then(res => {
                res.list.forEach(el => {
                    if (el.writeYear !== searchParam.value.writeYear) {
                        el.checked = true;
                    }
                    el.detail = {
                        교육일자: formatDate(el.trainingDate),
                        인원: el.resultCnt + '명',
                        '과목/과정': el.trainingTypeNm,
                        교육장소: el.trainingLocationNm
                    };
                });
                safetyHealthTrainingPlans.value = res.list;
                addMonthList();
                addTrainingTypeList();
                loadPreviousYn.value = true;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    //조회 후 데이터 가공
    const afterSearch = res => {
        safetyHealthTrainingPlans.value = res.list.map(el => ({
            ...el,
            checked: false,
            detail: {
                교육일자: formatDate(el.trainingDate),
                인원: `${el.resultCnt}명`,
                '과목/과정': el.trainingTypeNm,
                교육장소: el.trainingLocationNm
            }
        }));

        updateFilteredLists();
    };

    const filteredByOrgnList = ref({});
    const filteredByJobList = ref({});

    // 공통 유틸리티 함수: 그룹화 및 정렬
    const groupAndSort = (data, keyExtractor, sortFn) => {
        const grouped = data.reduce((acc, item) => {
            const key = keyExtractor(item) || '해당 없음';
            if (!acc[key]) acc[key] = [];
            acc[key].push(item);
            return acc;
        }, {});

        const sortedKeys = Object.keys(grouped).sort(sortFn);
        return sortedKeys.reduce((acc, key) => {
            acc[key] = grouped[key];
            return acc;
        }, {});
    };

    // 월별 필터링
    const addMonthList = () => {
        const result = groupAndSort(
            safetyHealthTrainingPlans.value,
            item => (item.trainingDate ? `${item.trainingDate.substring(4, 6)}월` : null),
            (a, b) => {
                const numA = parseInt(a, 10);
                const numB = parseInt(b, 10);
                if (isNaN(numA)) return -1;
                if (isNaN(numB)) return 1;
                return numB - numA;
            }
        );

        // result가 빈값이면 현재 달 빈값 추가
        if (Object.keys(result).length === 0) {
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            result[currentMonth] = [];
        }

        filteredByOrgnList.value = result;
        filteredByOrgnListBySearch.value = { ...result }; // 복제
    };

    // 직무별 필터링
    const addTrainingTypeList = () => {
        const result = groupAndSort(
            safetyHealthTrainingPlans.value,
            item => item.trainingTypeNm,
            (a, b) => a.localeCompare(b, 'ko')
        );

        if (Object.keys(result).length === 0) {
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            result[currentMonth] = [];
        }

        filteredByJobList.value = result;
        filteredByJobListBySearch.value = { ...result }; // 복제
    };

    // 필터 업데이트
    const updateFilteredLists = () => {
        addMonthList();
        addTrainingTypeList();
    };

    //상세 조회
    const getTrainingResultRptDetail = notify => {
        openLoading();
        return new Promise(resolve => {
            getTrainingResultReportDetail(searchParam.value, notify)
                .then(async res => {
                    if (res.success && res.list) {
                        res.list.trainingDate = formatDate(res.list.trainingDate);
                        inputForm.value = _.cloneDeep(res.list);
                        await signature.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
                        await inspector.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
                        resolve(true);
                    }
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                });
        });
    };

    const fileInfo = file => {
        fileInfo.value = file.value;
    };

    const btnSave = async data => {
        //openLoading();
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        data[0].deleteFiles = editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        editFiles.insert.forEach(file => {
            formData.append('files', file ? file : new Blob([], { type: 'application/octet-stream' }));
        });
        return new Promise(resolve => {
            saveTrainingResultReport(formData, true)
                .then(async res => {
                    if (res.result) {
                        try {
                            // 서명 정보 저장
                            if(inputForm.value.cmd === 'I') {
                                await signature.value.setApprovalInfo('TRR', res.result.writeYear, res.result.docNo);
                            } 
                            await inspector.value.setHrChipsApprovalInfo('TRR', res.result.writeYear, res.result.docNo, 1);
                            await signature.value.updateTaskUseYn('TRR', res.result.writeYear, res.result.docNo);
                            await inspector.value.updateTaskUseYn('TRR', res.result.writeYear, res.result.docNo, 1);
                            
                            resolve(true);
                        } catch (error) {
                            console.error('저장 중 오류 발생:', error);
                            throw error;
                        }
                    }
                    //저장후 재조회
                    await getTrainingResultRptDetail(false);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
                });
        }).catch(() => {
            openLoading();
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

    const btnAdd = async () => {
        searchParam.value.docNo = null;
        console.log('추가', searchParam.value);
        initInputForm();
        router.push('/SAndHTrainingResultsPlanDetail');
    };
    const btnDelete = () => {
        let param = getCheckedList();
        if (!param.length) {
            alertMsg('error', t('msgNoItem'))
            return;
        }
        if (param.some(el => el.useYn === 'N')) {
            alertMsg('error', t('msgDeletedItem'))
            return;
        }
        confirmMsg('warning', t('msgDelete'), ``, {
            fun: deleteApi,
            param: param
        });
    };
    const deleteApi = async param => {
        openLoading();
        deleteTrainingResultReports(param)
            .then(async res => {
                for (let item of param) {
                    await signatureStore.forcedUpdateTaskUseYn('N', 'TRR', item.writeYear, item.docNo);
                    await signatureStore.forcedUpdateTaskUseYn('N', 'TRR', item.writeYear, item.docNo, 1);
                }
                await btnSearch(false);
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
        }
        else {
            if (list.length > 0) {
                confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports, param: list });            
            } else {
                alertMsg('warning', t('msgNoItem'));
            }
        }
    };
    const downloadReports = async list => {
        let searchVO = _.cloneDeep(searchParam.value);
        searchVO.docType = 'TRR';
        searchVO.checkedList = list;
        baseDownload(getTrainingResultReport, searchVO);
        // openLoading()
        // getTrainingResultReport(searchVO).then(res => {
        //   downloadReport(res)
        // }).finally(() => {
        //   endLoading();
        // });
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
        deleteTrainingResultReport(data, true)
            .then(async res => {
                await signatureStore.forcedUpdateTaskUseYn('N', 'TRR', data.writeYear, data.docNo);
                await signatureStore.forcedUpdateTaskUseYn('N', 'TRR', data.writeYear, data.docNo, 1);
            })
            .catch(() => {
                endLoading();
            })
            .finally(async() => {
                await getTrainingResultRptDetail(false);
                endLoading();
            });
    };

    // etc function
    const getCheckedList = () => {
        let checkedData = [];
        if (currentFilter.value === 'calendar') {
            Object.keys(filteredByOrgnListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByOrgnListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        } else if (currentFilter.value === 'subjects') {
            Object.keys(filteredByJobListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByJobListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        }
        return checkedData;
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    const filteredByOrgnListBySearch = ref({});
    const filteredByJobListBySearch = ref({});
    const applyFilter = () => {
        // 캘린더
        if (currentFilter.value == 'calendar') {
            console.log('검색', filteredByOrgnList.value);
            for (const key in filteredByOrgnList.value) {
                const filteredData = filteredByOrgnList.value[key].filter(item => item.title.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.trainingDate).toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || key.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingLocationNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || (item.resultCnt !== null && item.resultCnt.toString().includes(searchTerm.value)));
                filteredByOrgnListBySearch.value[key] = filteredData;
            }
        }

        // 과목/과정 필터
        if (currentFilter.value == 'subjects') {
            // 과목/과정
            for (const key in filteredByJobList.value) {
                const filteredData = filteredByJobList.value[key].filter(item => item.title.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || key.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.trainingLocationNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || (item.resultCnt !== null && item.resultCnt.toString().includes(searchTerm.value)));
                filteredByJobListBySearch.value[key] = filteredData;
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
        jobCompAssessSegments,
        searchTerm,
        filterDivList,
        currentFilter,
        filteredByOrgnList,
        filteredByJobList,
        filteredByOrgnListBySearch,
        filteredByJobListBySearch,
        checkedList,
        loadPreviousYn,
        signature,
        inspector,
        getTrainingResultRptDetail,
        // function
        initInputForm,
        applyFilter,
        getCheckedList,
        toggleUseYn,
        fileInfo,
        // api
        btnSearch,
        btnAdd,
        btnSave,
        btnDelete,
        btnDownload,
        btnPreYear,
        btnSavePreviousYear,
        btnDeleteDetail
    };
});
