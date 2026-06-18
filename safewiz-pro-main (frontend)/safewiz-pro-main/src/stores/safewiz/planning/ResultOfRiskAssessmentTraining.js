import { defineStore } from 'pinia';
import BaseView from '@/components/base/BaseView.js';
const { openLoading, endLoading, ref, getCompId, getCurrentDate, t, baseDownload, getFormattedDate, formatDateForDB } = BaseView();
import { getResultOfRiskAssessmentTrainingListAPI, insertResultOfRiskAssessmentTrainingAPI, getResultOfRiskAssessmentTrainingAPI, updateResultOfRiskAssessmentTrainingAPI, deleteResultOfRiskAssessmentTrainingAPI, getResultOfRiskAssessmentTrainingReportAPI } from '@/stores/safewiz/planning/api/ResultOfRiskAssessmentTrainingApi';
import _ from 'lodash';
import router from '@/router';
import { loadFileFromServer } from '@/utils/iFileLoader.js';
import { useTaskStore } from '@/stores/safewiz/task/task.js';

import { useButtonListStore } from '@/stores/buttonList';

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useResultOfRiskAssessmentTraining = defineStore('ResultOfRiskAssessmentTraining', () => {
    const resultOfRiskAssessmentTrainingList = ref([]);
    const segments = ref([]);

    // 검색 파라미터
    const searchParam = ref({
        // 연도
        searchText: getCurrentDate().slice(0, 4),
        // 검색어
        searchText2: ''
    });

    // 검색어 필터링
    const searchFilter = data => {
        return data.filter(item => item.asmntDate.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) || item.asmntStartTime.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) || item.asmntEndTime.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) || item.asmntPlace.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) || item.asmntContent.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()));
    };

    // 목록 조회
    const getResultOfRiskAssessmentTrainingList = () => {
        openLoading();

        return new Promise(resolve => {
            getResultOfRiskAssessmentTrainingListAPI(searchParam.value, true)
                .then(res => {
                    if (res && res.success) {
                        segments.value = [];

                        // 검색어 필터링 후 월별 리스트에 넣기
                        for (let item of searchFilter(res.list)) {
                            // 카드 정보 생성
                            item.detail = {
                                [t('asmnt_time')]: item.asmntStartTime + ' ~ ' + item.asmntEndTime,
                                [t('asmnt_place')]: item.asmntPlace || '-',
                                [t('asmnt_content')]: item.asmntContent || '-'
                            };
                            // 월별 단위 구분
                            const matchingMonth = segments.value.filter(segment => segment.month.includes(item.asmntDate.slice(4, 6)));
                            if (matchingMonth.length > 0) {
                                // 월별 리스트 존재 하면 해당 월에 데이터 추가
                                matchingMonth[0].monthList.push(item);
                            } else {
                                // 월별 리스트가 존재하지 않으면 해당 월 추가 후 리스트에 데이터 추가
                                segments.value.push({
                                    month: item.asmntDate.slice(4, 6) + '월',
                                    monthList: [item]
                                });
                            }
                        }
                        resultOfRiskAssessmentTrainingList.value = res.list;
                    }
                    resolve({ result: res.list });
                })
                .finally(() => endLoading());
        });
    };

    const inputForm = ref({});
    const initInputForm = async () => {
        inputForm.value = {
            cmd: 'I',
            compId: getCompId(),
            asmntDate: '',
            asmntTime: '',
            asmntStartTime: '',
            asmntEndTime: '',
            asmntPlace: '',
            asmntContent: '',
            fileId: '',
            createdAt: getCurrentDate(),
            useYn: 'Y'
        };
    };

    // 등록 버튼
    const btnAdd = () => {
        // 데이터 초기화
        initInputForm();
        // 추가 플래그
        inputForm.value.cmd = 'I';
        router.push({
            path: 'ResultOfRiskAssessmentTrainingDetail'
        });
    };

    const fileInfo = file => {
        fileInfo.value = file.value;
    };

    const layoutStore = useButtonListStore();
    // 등록 함수
    const insertResultOfRiskAssessmentTraining = async data => {
        const saveParams = _.cloneDeep(data);
        saveParams.asmntDate = formatDateForDB(saveParams.asmntDate);
        saveParams.writeYear = searchParam.value.searchText;
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(saveParams)], { type: 'application/json' }));
        editFiles.insert.forEach(file => {
            formData.append('files', file ? file : new Blob([], { type: 'application/octet-stream' }));
        });
        openLoading();
        return new Promise(resolve => {
            insertResultOfRiskAssessmentTrainingAPI(formData, true)
                .then(async res => {
                    if (res && res.success) {
                        inputForm.value.writeYear = res.writeYear;
                        inputForm.value.docNo = res.docNo;
                        // 참가자 저장
                        // data.attendeesComponent.setHrChipsApprovalInfo(res.writeYear + res.docNo);
                        await data.attendeesComponent.setHrChipsApprovalInfo('RORAT', res.writeYear, res.docNo);
                        inputForm.value.cmd = 'U';
                        await getResultOfRiskAssessmentTraining(res, false);
                        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
                        resolve({ result: res.result });
                        // 파일 재조회
                        await data.fileList.fnSearch();
                        await data.attendeesComponent.Search();
                    }
                })
                .finally(() => endLoading());
        });
    };

    const taskStore = useTaskStore();
    // 상세보기 함수
    const getResultOfRiskAssessmentTraining = (data, notify) => {
        searchParam.value.writeYear = data.writeYear;
        searchParam.value.docNo = data.docNo;
        searchParam.value.docType = data.docType;
        openLoading();
        return getResultOfRiskAssessmentTrainingAPI(searchParam.value, notify)
            .then(res => {
                if (res && res.success) {
                    res.result.asmntDate = getFormattedDate(res.result.asmntDate);
                    // 조회된 데이터 넣기 + 시작시간, 종료시간 합치기
                    inputForm.value = {
                        ...res.result,
                        asmntTime: res.result.asmntStartTime + ' ~ ' + res.result.asmntEndTime
                    };
                    inputForm.value.cmd = 'U';

                    // 참석자 넣기
                    inputForm.value.asmntAttendees.map(async item => {
                        item.name = item.hrNm;
                        item.img = await loadFileFromServer(item.logoId); // 로드한 이미지를 user 객체에 저장
                        return item; // 로드된 이미지를 포함한 user 반환
                    });

                    taskStore.goToPageIfNotCurrent('ResultOfRiskAssessmentTrainingDetail');
                }
            })
            .finally(() => endLoading());
    };

    // 수정 함수
    const updateResultOfRiskAssessmentTraining = async data => {
        const saveParams = _.cloneDeep(data);
        saveParams.asmntDate = formatDateForDB(saveParams.asmntDate);
        saveParams.writeYear = searchParam.value.searchText;
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        inputForm.value.deleteFiles = editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(saveParams)], { type: 'application/json' }));
        editFiles.insert.forEach(file => {
            formData.append('files', file ? file : new Blob([], { type: 'application/octet-stream' }));
        });
        openLoading();
        return new Promise(resolve => {
            updateResultOfRiskAssessmentTrainingAPI(formData, true)
                .then(async res => {
                    if (res && res.success) {
                        // 참가자 저장
                        await data.attendeesComponent.setHrChipsApprovalInfo(res.docType, res.writeYear, res.docNo);
                        await data.attendeesComponent.updateTaskUseYn(res.docType, res.writeYear, res.docNo);
                        // 파일 재조회
                        await getResultOfRiskAssessmentTraining(res, false);
                        await data.fileList.fnSearch();
                        resolve({ result: res.result });
                    }
                })
                .finally(() => endLoading());
        });
    };

    // 삭제 함수
    const deleteResultOfRiskAssessmentTraining = async data => {
        openLoading();
        return deleteResultOfRiskAssessmentTrainingAPI(data, true)
            .then(async res => {
                if (res && res.success) {
                    // 상세페이지에서는 목록으로 이동하도록
                    if (router.currentRoute.value.name === 'ResultOfRiskAssessmentTraining') {
                        getResultOfRiskAssessmentTrainingList();
                    } else {
                        router.push({
                            path: 'ResultOfRiskAssessmentTraining'
                        });
                    }
                    for (let i = 0; i < data.length; i++) {
                        await signatureStore.forcedUpdateTaskUseYn('N',data[i].docType, data[i].writeYear, data[i].docNo);
                    }
                }
            })
            .finally(() => endLoading());
    };

    // 출력 함수
    const getResultOfRiskAssessmentTrainingReport = async data => {
        const param = {
            writeYear: searchParam.searchText,
            checkedObjList: data.map(el => ({
                writeYear: el.writeYear,
                docType: el.docType,
                docNo: el.docNo,
                compId: el.compId
            }))
        };
        baseDownload(getResultOfRiskAssessmentTrainingReportAPI, param);
        // openLoading();
        // return await new Promise((resolve) => {
        //     getResultOfRiskAssessmentTrainingReportAPI(param, true)
        //         .then(res => {
        //             resolve({ result: res.result, success: res.success });
        //             let link = document.createElement('a');
        //             // 객체를 만들어서 생성
        //             link.href = window.URL.createObjectURL(res.data);
        //             link.download = res.headers.filename;
        //             link.click();
        //         }).finally(() => endLoading());
        // });
    };

    return {
        getCompId,
        getCurrentDate,
        initInputForm,
        inputForm,
        searchParam,
        getResultOfRiskAssessmentTrainingList,
        btnAdd,
        fileInfo,
        insertResultOfRiskAssessmentTraining,
        updateResultOfRiskAssessmentTraining,
        resultOfRiskAssessmentTrainingList,
        getResultOfRiskAssessmentTraining,
        segments,
        deleteResultOfRiskAssessmentTraining,
        getResultOfRiskAssessmentTrainingReport
    };
});
