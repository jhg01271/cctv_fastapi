import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
import { getJobCompAssessList, getPreJobCompAssessList, saveJobCompAssess, saveJobCompAssessDetail, savePreJobCompAssess, deleteJobCompAssess, deleteJobCompAssessDetail, getJobCompAssessReport, 
         getJobManageList, getDataSetJobManageList, saveJobManageList, deleteJobManageList,
         getJobLevelManageList, getDataSetJobLevelManageList, saveJobLevelManageList, deleteJobLevelManageList, getCardJobManageList } from '@/stores/safewiz/support/api/JobCompAssessApi.js';
const { openLoading, endLoading, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, formatDate, downloadReport,baseDownload } = BaseView();
import router from '@/router';
import _ from 'lodash';

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
export const useJobCompAssessStore = defineStore('JobCompAssess', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value
    });

    const checkedList = ref([]);

    // 아코디언 관리
    const jobCompAssessSegments = ref([]);

    const inputForm = ref({});

    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            docType: 'JCA',
            hrId: '',
            orgnNm: '',
            jbrpNm: '',
            useYn: 'Y',
            detailList: [],
            writeYear: educationStore.searchParam.searchText,
            createdAt: getCurrentDate()
        };
        inputForm.value.detailList = [
            {
                writeYear: inputForm.value.writeYear,
                docType: inputForm.value.docType,
                docNo: inputForm.value.docNo,
                compId: inputForm.value.compId,
                useYn: inputForm.value.useYn,
                checked: true
            }
        ];
    };
    const jobCompAssessList = ref([]);
    // btn function

    const btnSearch = async notify => {
        searchParam.value.writeYear = educationStore.searchParam.searchText;
        jobCompAssessList.value = []
        const excuteSearch = () => {
            openLoading();
            getJobCompAssessList(searchParam.value, notify)
                .then(res => {
                    if (!res.list || res.list.length === 0) {
                        filteredByOrgnListBySearch.value = [];
                    } else {
                        afterSearch(res);
                    }
                    isSaved.value = true;
                })
                .catch(() => {
                    filteredByOrgnListBySearch.value = []; // 실패 시에도 초기화
                })
                .finally(() => {
                    endLoading();
                });
        };
    
        //전년도 불러오기 진행중일때 메시지 띄움
        if (!isSaved.value) {
            confirmMsg('info', t('msgPreviousConfirm'), '', {
                fun: excuteSearch
            });
        } else {
            excuteSearch();
        }
    };

    const afterSearch = res => {
        res.list.forEach(el => {
            el.checked = false;
            let countNm = el.cardJobNm.length > 1 ? ` 외 ${el.cardJobNm.length - 1} 개` : ``;
            jobCompAssessList.value = res.list.map(el => {
                return {
                ...el,
                detail: {
                    조직: el.orgnNm,
                    직무: el.cardJobNm[0] + countNm,
                    직위: el.jbrpNm,
                    등록일자: formatDate(el.createdAt)
                },
                detailWithBtn: [
                    {
                        label: '결재 상태',
                        value: el.approvalStatus,
                        button: [
                            {
                                value: 'Y', // 평가 서명이 있는 경우
                                label: '평가',
                                class: 'complete'
                            },
                            {
                                value: 'N', // 작성 서명이 있는 경우
                                label: '작성',
                                class: 'progress'
                            },
                            {
                                value: 'C', // 서명이 없는 경우 
                                label: '기안',
                                class: 'ready'
                            }
                        ]
                    },
                ]};
            })  
        });
        
        addOrgList();
        addJobList();
    };

    const filteredByOrgnList = ref({});
    const addOrgList = () => {
        filteredByOrgnList.value = {};
        const result = {};
        // 데이터 처리 함수
        jobCompAssessList.value.forEach(list => {
            // 각 부모의 list를 순회하며 그룹화 진행
            if (result[list.orgnNm]) {
                result[list.orgnNm].push(list);
            } else {
                result[list.orgnNm] = [list];
            }
        });
        filteredByOrgnList.value = _.cloneDeep(result);
        filteredByOrgnListBySearch.value = _.cloneDeep(result);
    };
    const filteredByJobList = ref({});
    const addJobList = () => {
        filteredByJobList.value = {};
        const result = {};
        // 데이터 처리 함수
        jobCompAssessList.value.forEach(list => {
            // 각 부모의 list를 순회하며 그룹화 진행
            list.cardJobNm.forEach(job => {
                if (result[job]) {
                    result[job].push(list);
                } else {
                    result[job] = [list];
                }
            });
        });
        // 중복 그룹들을 하나의 키로 합치는 부분
        // 모든 작업장과 그에 해당하는 workplaceId를 순회
        for (const [j, jobList] of Object.entries(result)) {
            jobList.forEach(job => {
                // 같은 equipmentId를 가진 조직들을 찾아서 그룹화
                const matchingOrgs = Object.keys(result).filter(org => result[org].some(el => el === job));

                // 그룹으로 묶인 직무명을 '|'로 연결하여 key 생성
                const groupKey = matchingOrgs.sort().join(' | ');

                // 그룹 키가 이미 존재하면, equipmentId 추가
                if (!filteredByJobList.value[groupKey]) {
                    filteredByJobList.value[groupKey] = [];
                }

                // 동일한 equipmentId를 가진 장비가 중복되지 않도록 추가
                if (!filteredByJobList.value[groupKey].some(el => el === job)) {
                    filteredByJobList.value[groupKey].push(job);
                }
            });
        }

        filteredByJobListBySearch.value = _.cloneDeep(filteredByJobList.value);
    };

    const btnDetail = async (data, notify = true) => {
        let param = _.cloneDeep(searchParam.value);
        param.writeYear = data.writeYear;
        param.docType = data.docType;
        param.docNo = data.docNo;
        openLoading();
        getJobCompAssessList(param, notify)
            .then(res => {
                inputForm.value = res.list[0];
                if (!inputForm.value.detailList) {
                    inputForm.value.detailList = [
                        {
                            writeYear: inputForm.value.writeYear,
                            docType: inputForm.value.docType,
                            docNo: inputForm.value.docNo,
                            compId: inputForm.value.compId,
                            useYn: inputForm.value.useYn
                        }
                    ];
                }
                //저장된 직무분야의 업무 내용 조회
                inputForm.value.detailList.forEach(val => {
                    getCardJobManageList({hrId : inputForm.value.hrId}, false).then(res => {
                        res.list.forEach(item => {
                            if(val.jobId === item.jobId && val.jobCategory === item.jobCategory){
                                val.content = item.remark
                            }
                        })
                    })
                })
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnMasterSave = async notify => {
        let param = getCheckedList();
        openLoading();
        savePreJobCompAssess(param, notify)
            .then(res => {
                btnSearch();
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });

        isSaved.value = true;
    };
    const btnSave = async notify => {
        let saveParam = _.cloneDeep(inputForm.value);
        saveParam.detailList = inputForm.value.detailList.filter(el => el.checked);
        openLoading();
        saveJobCompAssessDetail(saveParam, notify)
            .then(res => {
                //
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    // 서명 컴포넌트
    const signature = ref(null);

    const btnAdd = async () => {
        initInputForm();
        router.push('/JobCompetencyAssessmentDetail');
    };
    const btnDelete = () => {
        let param = getCheckedList();
        if (!param.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        if (param.some(el => el.useYn === 'N')) {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }
        confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: param });
    };
    const deleteApi = async param => {
        deleteJobCompAssess(param).then(async res => {
            for(let item of param) {
                await signatureStore.forcedUpdateTaskUseYn('N', 'JCA', item.writeYear, item.docNo);
            }
            btnSearch(false);
        });
    };

    const btnDownload = async (list, docNo = null) => {
        // 목록 화면 출력에서의 list는 doc_no의 배열
        // 상세 화면 출력에서의 list는 선택한 doc_seq의 배열, doc_no는 해당 상세 문서번호, 선택하지 않고 출력 시에는 목록 화면의 출력과 동일한 파라미터
        if (list.length > 0) {
            let searchVO = _.cloneDeep(searchParam.value);
            searchVO.docType = 'JCA';
            searchVO.checkedList = list;
            if (docNo) {
                // 상세화면에서 출력
                searchVO.docNo = docNo;
            }
            // openLoading();
            // getJobCompAssessReport(searchVO)
            //     .then(res => {
            //         downloadReport(res);
            //     })
            //     .catch(() => {
            //         endLoading();
            //     })
            //     .finally(() => {
            //         endLoading();
            //     });

            baseDownload(getJobCompAssessReport, searchVO)
        } else {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
    };

    const isSaved = ref(true);
    const btnPreYear = async () => {
        openLoading();
        let searchParams = _.cloneDeep(searchParam.value)
        searchParams.writeYear = searchParams.writeYear - 1
        getPreJobCompAssessList(searchParams, true)
            .then(res => {
                res.list.forEach(el => {
                    el.type = 'pre'
                    let countNm = el.detailList.length > 1 ? ` 외 ${el.detailList.length - 1} 개` : ``;
                    el.detail = {
                        조직: el.orgnNm,
                        직무: el.detailList[0].jobNm + countNm,
                        직위: el.jbrpNm
                    };
                    el.cardJobNm = el.detailList.map(detail => detail.jobFieldNm);
                    if (el.writeYear !== searchParam.value.writeYear) el.checked = true; // 불러온 데이터만 체크
                });

                if (res.list.length > 0 && res.list !== null) {
                const filterPreDataList = res.list.filter(item => item.useYn === 'Y')
                    jobCompAssessList.value.push(...filterPreDataList); // 기존 년도의 데이터에 전년도 데이터 추가
                    addOrgList();
                    addJobList();
                }else{
                    alertMsg('error', t('msgPreviouseNoData'));
                    isSaved.value = true;
                    endLoading();
                    return
                }           
                isSaved.value = false;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDeleteDetail = async () => {
        let param = getCheckedDetailList();
        if (!param.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        if (param.some(el => el.useYn === 'N')) {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }
        confirmMsg('warning', t('msgDelete'), ``, { fun: deleteDetailApi, param: param });
    };
    const deleteDetailApi = async param => {
        openLoading();
        deleteJobCompAssessDetail(param)
            .then(res => {
                btnDetail(inputForm.value);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    // etc function
    const getCheckedList = () => {
        let checkedData = [];
        if (currentFilter.value === 'orgn') {
            Object.keys(filteredByOrgnListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByOrgnListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        } else if (currentFilter.value === 'job') {
            Object.keys(filteredByJobListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByJobListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        }
        return checkedData;
    };

    const getCheckedDetailList = () => {
        let checkedData = [];
        checkedData = inputForm.value.detailList.filter(el => el.checked);
        return checkedData;
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    const filteredByOrgnListBySearch = ref({});
    const filteredByJobListBySearch = ref({});
    const applyFilter = () => {
        const search = searchTerm.value.trim().toLowerCase();
    
        // 초기화
        filteredByOrgnListBySearch.value = {};
        filteredByJobListBySearch.value = {};
    
        // 조직 필터링
        for (const key in filteredByOrgnList.value) {
            const filteredData = filteredByOrgnList.value[key].filter(item => {
                return (item.hrNm?.toLowerCase().includes(search) || item.orgnNm?.toLowerCase().includes(search) || item.cardJobNm?.[0]?.toLowerCase().includes(search) || item.jbrpNm?.toLowerCase().includes(search) || formatDate(item.createdAt).toLowerCase().includes(search)
                );
            });
    
            if (filteredData.length > 0) {
                filteredByOrgnListBySearch.value[key] = filteredData;
            }
        }
    
        // 직무 필터링
        for (const key in filteredByJobList.value) {
            const filteredData = filteredByJobList.value[key].filter(item => {
                return (item.hrNm?.toLowerCase().includes(search) || item.orgnNm?.toLowerCase().includes(search) || item.cardJobNm?.[0]?.toLowerCase().includes(search) || item.jbrpNm?.toLowerCase().includes(search) || formatDate(item.createdAt).toLowerCase().includes(search)
                );
            });
    
            if (filteredData.length > 0) {
                filteredByJobListBySearch.value[key] = filteredData;
            }
        }
    };
//######################################################################################################################
//직무관리 팝업-----------------------------------------------------------------------------------------------------------
//######################################################################################################################

    const jobManageList = ref([])
    const currentJobManageList = ref([])

    //직무 관리 데이터 초기화
    const initJobManageList = (notify) => {
        getJobManageList({compId : getCompId()}, notify).then(res => {
            jobManageList.value = res.list
            currentJobManageList.value = _.cloneDeep(res.list)
        })
    }

    //직무 관리 필터 조회
    const filterJobManageList = text => {
        jobManageList.value = currentJobManageList.value;
        const filteredData = jobManageList.value.filter(el => el.jobNm?.toLowerCase().includes(text) || el.ordSeq == text || el.jobCategory?.toLowerCase().includes(text) || el.remark?.toLowerCase().includes(text));
        jobManageList.value = filteredData;
    }

    //직무 관리 예시 데이터 조회
    const searchDataSetJobManageList = () => {
        openLoading()
        getDataSetJobManageList({compId : getCompId()}, true).then(async res => {
            if (res.list.length == 0) {
                alertMsg('error', '등록 가능한 샘플 데이터가 없습니다.');
                return;
            }
            await res.list.forEach(val => {
                val.jobId = ''
                val.cmd = 'I'
                val.checked = true;
                if (val.orgnRoleId && !jobManageList.value.some(item => item.orgnRoleId === val.orgnRoleId && item.orgnRoleType === val.orgnRoleType)) {
                    jobManageList.value.push(val);
                }
            });
            endLoading()
        }).catch((err) => {
            endLoading()
        })
    }

    //직무 관리 데이터 추가
    const addJobManageList = () => {
        const newData = {
            jobId : '',
            jobCategory : '',
            jobNm : '',
            orgnRoleId: '',
            ordSeq: '',
            useYn: 'Y',
            cmd : 'I',
            checked: true
        };
        jobManageList.value.push(newData);
    }
 
    //직무 관리 데이터 추가 및 수정
    const saveJobManageData = () => {
        openLoading()
        const checkedList = jobManageList.value.filter(val => val.checked == true);
        saveJobManageList(checkedList, true).then(res => {
            if(res.success){

                initJobManageList()
                endLoading()
            }
        }).catch((err) => {
            endLoading()
        })
    }

    //직무 관리 데이터 삭제
    const deleteJobManageData = () => {
        openLoading()
        const checkedList = jobManageList.value.filter(val => val.checked == true);
        deleteJobManageList(checkedList, true).then(res => {
            if(res.success){
                initJobManageList()
                endLoading()
            }
        }).catch((err) => {
            endLoading()
        })
    }
//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################


//######################################################################################################################
// //직무 수준 관리 팝업--------------------------------------------------------------------------------------------------
//######################################################################################################################
    const jobLevelManageList = ref([])
    const currentJobLevelManageList = ref([])

    //직무 수준 관리 데이터 초기화
    const initJobLevelManageList = (notify) => {
        getJobLevelManageList({compId : getCompId()}, notify).then(res => {
            jobLevelManageList.value = res.list
            currentJobLevelManageList.value = _.cloneDeep(res.list)
        })
    }

    //직무 수준 관리 필터 조회
    const filterJobLevelManageList = text => {
        jobLevelManageList.value = currentJobLevelManageList.value;
        const filteredData = jobLevelManageList.value.filter(el => el.jobLevelNm?.toLowerCase().includes(text) || el.ordSeq == text || el.remark?.toLowerCase().includes(text));
        jobLevelManageList.value = filteredData;
    }

    //직무 수준 관리 데이터 저장 및 수정
    const saveJobLevelManageData = () => {
        openLoading()
        const checkedList = jobLevelManageList.value.filter(val => val.checked == true);
        saveJobLevelManageList(checkedList, true).then(res => {
            if(res.success){
                initJobLevelManageList()
                endLoading()
            }
        }).catch((err) => {
            endLoading()
        })
    }

    //직무 수준 관리 데이터 추가
    const addJobLevelManageList = () => {
        const newData = {
            jobLevelId : '',
            jobLevelNm : '',
            remark : '',
            ordSeq : '',
            cmd : 'I',
            useYn: 'Y',
            checked: true
        };
        jobLevelManageList.value.push(newData);
    }
    
    //직무 수준 관리 데이터 삭제
    const deleteJobLevelManageData = () => {
        openLoading()
        const checkedList = jobLevelManageList.value.filter(val => val.checked == true);
        deleteJobLevelManageList(checkedList, true).then(res => {
            if(res.success){
                initJobLevelManageList()
                endLoading()
            }
        }).catch((err) => {
            endLoading()
        })
    }
//######################################################################################################################
//----------------------------------------------------------------------------------------------------------------------
//######################################################################################################################
    // 필터 관리
    const searchTerm = ref('');
    const filterDivList = ref([
        { id: 'orgn', name: '조직' },
        { id: 'job', name: '직무' }
    ]);
    const currentFilter = ref('orgn');

    return {
        writeYear,
        searchParam,
        inputForm,
        jobCompAssessSegments,
        searchTerm,
        filterDivList,
        currentFilter,
        jobCompAssessList,
        filteredByOrgnList,
        filteredByJobList,
        filteredByOrgnListBySearch,
        filteredByJobListBySearch,
        checkedList,
        isSaved,
        signature,
        currentJobManageList,
        jobManageList,
        currentJobLevelManageList,
        jobLevelManageList,
        // function
        toggleUseYn,
        initInputForm,
        applyFilter,
        getCheckedList,
        initJobManageList,
        filterJobManageList,
        searchDataSetJobManageList,
        addJobManageList,
        saveJobManageData,
        deleteJobManageData,
        initJobLevelManageList,
        filterJobLevelManageList,
        addJobLevelManageList,
        saveJobLevelManageData,
        deleteJobLevelManageData,
        // api
        btnSearch,
        btnDetail,
        btnAdd,
        btnMasterSave,
        btnSave,
        btnDelete,
        btnDownload,
        btnPreYear,
        btnDeleteDetail
    };
});
