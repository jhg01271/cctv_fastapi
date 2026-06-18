import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, computed, confirmMsg, alertMsg, toastPopup, formatDate, ref, t, getCompId, getCurrentDate, getPreMonth, baseDownload, formatDateForDB } = BaseView();
import router from '@/router';
import { getConfirmedHsePolicyList, getHsePolicyList, getHsePolicyDetail, getSampleHsePolicy, saveHsePolicy, saveHsePolicyFiles, updateHsePolicyConfirmYn, deleteHsePolicy, getHsePolicySug, saveHsePolicySug, deleteHsePolicySug, getHsePolicyReport } from '@/stores/safewiz/participation/api/hsePolicyApi.js';
import _ from 'lodash';
import { useHseObjectivesStore } from '@/stores/safewiz/participation/hseObjectives.js';
const hseObjectivesStore = useHseObjectivesStore();
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useHsePolicyStore = defineStore('hsePolicy', () => {
    const compId = getCompId();
    const inputForm = ref({});
    const initInputForm = (dataType, year) => {
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            dataType: dataType,
            docType: 'PLC',
            dataTypeNm: '',
            contentHeader: '',
            contentBody: '',
            contentFooter: '',
            useYn: 'Y',
            writeYear: year ? year : getCurrentDate().substring(0, 4)
        };
    };
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };
    const searchTerm = ref('');
    const filteredHsePolicyList = ref({});
    const filteredConfirmList = ref({});
    const applyFilter = () => {
        for (const key in hsePolicyList.value) {
            const filteredData = hsePolicyList.value[key].filter(item => 
                item.contentHeader?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
                formatDate(item.createdAt)?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
                item.dataTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()));
            filteredHsePolicyList.value[key] = filteredData;
        }

        filteredConfirmList.value = confirmedHsePolicyList.value?.filter(item => 
            item.contentHeader?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            formatDate(item.createdAt)?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.dataTypeNm?.toLowerCase().includes(searchTerm.value.toLowerCase()));
    };
    // 아코디언 관리

    const hsePolicySegments = ref([]);

    // APIs
    const hsePolicyList = ref({});

    const btnSearch = async notify => {
        getConfirmSearch(notify).then(async res => {
            await getHsePolicyList({}, false)
                .then(res => {
                    openLoading();
                    let policyList = res.list;
                    // 연도별로 분할하는 코드
                    hsePolicyList.value = policyList.reduce((acc, item) => {
                        // createdAt에서 년도만 추출
                        // const year = new Date(item.createdAt).getFullYear() + '년도';
                        const year = item.writeYear + '년도';
                        item.detail = {
                            등록일자: formatDate(item.createdAt),
                            구분: item.dataTypeNm,
                            '의견 수': item.workerSuggestionCnt + '건'
                        };
                        // 해당 년도가 없으면 새로운 배열 생성
                        if (!acc[year]) {
                            acc[year] = [];
                        }
                        // 년도에 맞는 배열에 데이터 추가
                        acc[year].push(item);

                        return acc;
                    }, {});
                    filteredHsePolicyList.value = _.cloneDeep(hsePolicyList.value);
                    if (!filteredHsePolicyList.value[hseObjectivesStore.searchParam.searchText + '년도']) {
                        filteredHsePolicyList.value[hseObjectivesStore.searchParam.searchText + '년도'] = [];
                        const sortedYearData = new Map(Object.entries(filteredHsePolicyList.value).sort((a, b) => parseInt(b[0]) - parseInt(a[0])));

                        filteredHsePolicyList.value = Object.fromEntries(sortedYearData);
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

    const confirmedHsePolicyList = ref([]);
    const getConfirmSearch = (notify = true) => {
        openLoading();
        return new Promise(resolve => {
            getConfirmedHsePolicyList({}, notify)
                .then(res => {
                    console.log('@@res', res.list);
                    if (res.list) {
                        res.list.forEach(el => {
                            el.detail = {
                                등록일자: formatDate(el.createdAt),
                                확정일자: formatDate(el.confirmDt),
                                구분: el.dataTypeNm,
                                '의견 수': el.workerSuggestionCnt + '건'
                            };
                        });
                        confirmedHsePolicyList.value = res.list;
                        filteredConfirmList.value = _.cloneDeep(confirmedHsePolicyList.value);
                    } else {
                        confirmedHsePolicyList.value = null;
                        filteredConfirmList.value = _.cloneDeep(confirmedHsePolicyList.value);
                    }
                    resolve();
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                });
        });
    };

    const btnConfirm = (item, confirmYn) => {
        console.log('@ confirmedHsePolicyList.value', confirmedHsePolicyList.value);
        if (confirmYn === 'Y' && confirmedHsePolicyList.value) {
            alertMsg('warning', '이미 확정된 안전보건경영방침이 있습니다. \n 확정 항목을 확정취소 후 진행해주세요.');
            return;
        }
        let data = _.cloneDeep(item);
        confirmMsg('info', confirmYn == 'Y' ? `[${item.contentHeader}] \n 확정 하시겠습니까?` : `[${item.contentHeader}] \n 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: { data: data, confirmYn: confirmYn } });
    };
    const confirmAction = item => {
        const { writeYear, docType, docNo } = item.data;
        let param = {
            writeYear: writeYear,
            docType: docType,
            docNo: docNo,
            confirmYn: item.confirmYn
        };
        openLoading();
        updateHsePolicyConfirmYn(param, true)
            .then(res => {})
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                btnSearch(false);
                endLoading();
            });
    };
    const originData = ref({});
    const btnDetail = async (item, notify) => {
        //
        let param = {
            compId: item.compId,
            writeYear: item.writeYear,
            docType: item.docType,
            docNo: item.docNo
        };
        openLoading();
        getHsePolicyDetail(param, notify)
            .then(res => {
                inputForm.value = { ...inputForm.value, ...res.list };
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
                originData.value = _.cloneDeep(inputForm.value);
                router.push('/HsePolicyDetail');
            });
    };
    const btnSample = async notify => {
        openLoading();
        getSampleHsePolicy(notify)
            .then(res => {
                inputForm.value.contentHeader = res.list.contentHeader;
                inputForm.value.contentBody = res.list.contentBody;
                inputForm.value.contentFooter = res.list.contentFooter;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const fileInfo = file => {
        fileInfo.value = file.value;
    };
    const additionalFileInfo = file => {
        additionalFileInfo.value = file.value;
    };

    const btnSave = async notify => {
        let formData = new FormData();
        if (inputForm.value.dataType === '0002') {
            let editFiles = fileInfo.value.editFiles;
            inputForm.value.deleteFiles = editFiles.delete;
            editFiles.insert.forEach(element => {
                formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
            });
        }
        formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));
        let param = {};
        openLoading();
        return new Promise(resolve => {
            saveHsePolicy(formData, notify)
                .then(res => {
                    if (res && res.success) {
                        //파일 파라미터 초기화
                        if (res.result.fileId != null) fileInfo.value.resetEditFiles(res.result.fileId);
                        param = {
                            compId: res.result.compId,
                            writeYear: res.result.writeYear,
                            docType: res.result.docType,
                            docNo: res.result.docNo
                        };
                        (inputForm.value.compId = res.result.compId), (inputForm.value.writeYear = res.result.writeYear), (inputForm.value.docType = res.result.docType), (inputForm.value.docNo = res.result.docNo);
                        resolve({ result: res.result });
                    }
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                    // getHsePolicyDetail(param, false).then(res => {
                    //     inputForm.value = res.list;
                    //     originData.value = _.cloneDeep(inputForm.value);
                    // });
                });
        });
    };
    const btnSaveFile = async (param, notify) => {
        let formData = new FormData();
        let editFiles = [];
        if (additionalFileInfo.value) {
            editFiles = additionalFileInfo.value.editFiles;
        }
        param.deleteFiles = editFiles.delete;
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
        openLoading();
        return new Promise(resolve => {
            saveHsePolicyFiles(formData, notify)
                .then(res => {
                    resolve(res.success);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                });
        });
    };
    const isCanAddPolicy = year => {
        let filteredKey = Object.keys(hsePolicyList.value).filter(el => el.substring(0, 4) === year);
        if (filteredKey.length === 0) return true;
        if (hsePolicyList.value[filteredKey].filter(el => el.useYn === 'Y').length > 0) {
            return false;
        } else return true;
    };
    const btnDelete = async item => {
        if (item === 'D') {
            confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deletePolicy, param: '' });
        } else {
            let param = getCheckedList(); // rowKey로 행 데이터를 가져옴
            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deletePolicys, param: param });
        }
    };
    const getCheckedList = () => {
        let checkedData = [];
        Object.keys(filteredHsePolicyList.value).forEach(el => {
            checkedData = [...checkedData, ...filteredHsePolicyList.value[el]];
        });
        checkedData = checkedData.filter(el => el.checked);
        return checkedData;
    };
    const deletePolicy = async () => {
        openLoading();
        deleteHsePolicy([inputForm.value], true)
            .then(async res => {
                btnSearch(false);
                if (inputForm.value.docNo) {
                    await signatureStore.forcedUpdateTaskUseYn('N', inputForm.value.docType, inputForm.value.writeYear, inputForm.value.docNo);
                    router.push('/HsePolicy');
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const deletePolicys = async list => {
        openLoading();
        deleteHsePolicy(list, true)
            .then(res => {
                list.forEach(async el => {
                    await signatureStore.forcedUpdateTaskUseYn('N', el.docType, el.writeYear, el.docNo);
                });
                btnSearch();
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDownload = () => {
        // openLoading();
        // getHsePolicyReport(inputForm.value, false).then(res => {
        //   downloadReport(res)
        // }).catch(() => {
        //   endLoading();
        // }).finally(() => {
        //   endLoading();
        // })
        const param = {
            compId: inputForm.value.compId,
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo
        };
        baseDownload(getHsePolicyReport, param);
    };
    /*****************************************************************/
    /**********************근로자의견 관련*****************************/
    /*****************************************************************/
    const sugSearchParam = ref({
        searchFrom: getPreMonth(3),
        searchTo: getCurrentDate()
    });
    const sugInputForm = ref({
        cmd: 'I',
        workerSuggestion: '',
        useYn: 'Y'
    });
    const initSugInputForm = () => {
        sugInputForm.value = {
            cmd: 'I',
            workerSuggestion: '',
            useYn: 'Y'
        };
    };
    const sugList = ref({});

    const btnSugSearch = (notify = true) => {
        sugSearchParam.value.myDataOnly = false; // 로그인 유저 본인의 근로자 의견 조회 상태값
        sugSearchParam.value = { ...sugSearchParam.value, ...inputForm.value };
        openLoading();
        let searchParam = {
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo,
            searchTo: formatDateForDB(sugSearchParam.value.searchTo),
            searchFrom: formatDateForDB(sugSearchParam.value.searchFrom),
            myDataOnly: sugSearchParam.value.myDataOnly
        };
        getHsePolicySug(searchParam, notify)
            .then(res => {
                sugList.value = res.list.reduce((acc, item) => {
                    // 해당 인원이 없으면 새로운 배열 생성
                    if (!acc[item.hrId]) {
                        acc[item.hrId] = [];
                    }
                    // hrId에 맞는 배열에 데이터 추가
                    acc[item.hrId].push(item);

                    return acc;
                }, {});
                filteredSugList.value = _.cloneDeep(sugList.value);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const mySugList = ref({});
    const btnHistSearch = (notify = true) => {
        sugSearchParam.value.myDataOnly = true; // 로그인 유저 본인의 근로자 의견 조회 상태값
        // sugSearchParam.value = { ...sugSearchParam.value, ...inputForm.value };
        sugSearchParam.value.writeYear = inputForm.value.writeYear;
        sugSearchParam.value.docType = inputForm.value.docType;
        sugSearchParam.value.docNo = inputForm.value.docNo;
        openLoading();
        let searchParam = {
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo,
            searchTo: formatDateForDB(sugSearchParam.value.searchTo),
            searchFrom: formatDateForDB(sugSearchParam.value.searchFrom),
            myDataOnly: sugSearchParam.value.myDataOnly
        };
        getHsePolicySug(searchParam, notify)
            .then(res => {
                mySugList.value = res.list.reduce((acc, item) => {
                    const date = formatDate(item.createdAt);

                    // 해당 데이터가 없으면 새로운 배열 생성
                    if (!acc[date]) {
                        acc[date] = [];
                    }
                    // 년도에 맞는 배열에 데이터 추가
                    acc[date].push(item);

                    return acc;
                }, {});
                initSugInputForm();
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const checkedSugs = computed(() => {
        let checked = [];
        Object.keys(mySugList.value).forEach(title => {
            checked = [...checked, ...mySugList.value[title].filter(el => el.checked)];
        });
        return checked;
    });
    const btnSugSave = notify => {
        sugInputForm.value = { ...inputForm.value, ...sugInputForm.value };
        let param = sugInputForm.value.workerSuggestion ? [...checkedSugs.value, ...[sugInputForm.value]] : checkedSugs.value;
        openLoading();
        saveHsePolicySug(param, notify)
            .then(res => {
                btnHistSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const btnSugDelete = notify => {
        let param = checkedSugs.value;
        openLoading();
        deleteHsePolicySug(param, notify)
            .then(res => {
                btnHistSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const searchSugTerm = ref('');
    const filteredSugList = ref({});
    const applySugFilter = () => {
        for (const key in sugList.value) {
            const filteredData = sugList.value[key].filter(item => item.hrNm.toLowerCase().includes(searchSugTerm.value.toLowerCase()) || item.workerSuggestion.toLowerCase().includes(searchSugTerm.value.toLowerCase()));
            filteredSugList.value[key] = filteredData;
        }
    };
    return {
        // 아코디언
        hsePolicySegments,

        inputForm,
        hsePolicyList,
        confirmedHsePolicyList,
        fileInfo,
        additionalFileInfo,
        searchTerm,
        filteredHsePolicyList,
        filteredConfirmList,
        originData,
        sugSearchParam,
        sugInputForm,
        sugList,
        filteredSugList,
        mySugList,
        checkedSugs,
        searchSugTerm,
        // function
        initInputForm,
        toggleUseYn,
        applyFilter,
        initSugInputForm,
        isCanAddPolicy,
        applySugFilter,
        // api
        btnSearch,
        btnConfirm,
        btnDetail,
        btnSample,
        btnSave,
        btnDelete,
        btnDownload,
        btnSugSearch,
        btnHistSearch,
        btnSugSave,
        btnSugDelete,
        btnSaveFile
    };
});
