// 화 면 명 :   재해발생보고서
// 작 성 자 :   마환구
// 시작일자 : 2024.11.05

import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';

const { ref, toastPopup, confirmMsg, getCompId, getCurrentDate, alertMsg, baseDownload, t, formatDate, formatDateForDB } = BaseView();
import { getIncidentDetailList, getIncidentList, getIncidentDetailManage, getIncidentDetailExtent, getIncidentDetailOpinion, getIncidentState, getIcidentDocNo, insertIncidentReport, insertIncidentReportDetail, insertIncidentReportManage, insertIncidentReportOpinion, insertIncidentReportStatment, updateIncidentReport, updateIncidentReportDetail, UpdateIncidentReportManage, UpdateIncidentReportOpinion, updateIncidentReportStatment, deleteIncidentReport, deleteIncidentReports, IncidentReportPrint, IncidentReportStatementPrint, WitnessReportStatementPrint, IncidentReportCombine, deleteIncidentDetail, deleteIncidentHospi, deleteIncidentOpinion, IncidentReportPrintchecked, updateIncidentReportuseYn, DeteleStatement } from '@/stores/safewiz/improvement/api/incidentReportApi';
// import { getMsds, getMsdsDetail, insertMsds, updateMsds, deleteMsds, deleteMsdss } from '@/stores/safewiz/planning/api/msdsApi';

import _, { forEach } from 'lodash';
import { useUserInfoStore } from '@/stores/user.js';
import { downloadFile } from '@/api/base/system/file';
import IncidentReport from '@/views/safewiz/improvement/IncidentReport.vue';
import IncidentReportDetail from '@/views/safewiz/improvement/IncidentReportDetail.vue';
import { result } from 'lodash';
import { createVNode, render, nextTick } from 'vue';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

//import { M } from 'vite/dist/node/types.d-aGj9QkWt';
const userStore = useUserInfoStore();

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();

export const useIncidentReportStore = defineStore('IncidentReport', () => {
    const compId = getCompId();

    const checkedList = ref([]);
    const incident_item = ref([]);
    const withness_item = ref([]);
    const currentFilter = ref('Organ');
    const doc = ref(null);
    const StatmentdocSeq = ref();
    const gubun = ref();
    const incidentUseYn = ref();
    const incidentStateDelete = ref('N');
    const withnessStateDelete = ref('N');
    const Ideletestate = ref({});
    const Wdeletestate = ref({});

    // 보고서 리스트
    const IncidentReportList = ref([]);

    // 현재 선택된 재해발생 보고서 인덱스
    const selectedIncidentReportId = ref(-1);

    // 체크된 데이터들 담기위한 배열선언
    const detaillist = ref([]);
    const hospilist = ref([]);
    const opinionlist = ref([]);
    const prtchk = ref('N');

    const searchParam = ref({
        compId: compId,
        writeYear: new Date().getFullYear(),
        docType: '',
        docNo: ''
    });

    // 상단 필터링 목록 (작성조직, 조치조직)
    const IncidentReportDivList = ref([
        { id: 'Organ', name: '조직' },
        { id: 'place', name: '발생장소' }
    ]);

    const IncidentManagelist = ref([
        { id: 'W', name: '산재' },
        { id: 'I', name: '공상' }
    ]);

    // const usageTypeList = ref([]); // 사용처 시스템 코드 저장
    const initData = async () => {
        // let responses = await Promise.all([
        //   getSystemCode({
        //     majorCd: 'C0010',
        //     compId: compId
        //   })
        // ]);
        // if (responses) {
        //   usageTypeList.value = responses[0].list;
        // }
    };

    const incidentPerson = ref([]);
    const witnessPerson = ref([]);

    const StateList = ref({});
    const IncidentList = ref([]);

    const workItems = ref([]);
    const currentDate = ref(getCurrentDate());

    const filteredByCreatReportList = ref({});
    const detail = ref();
    const hospi = ref();
    const opinion = ref();

    //const pop = ref()

    const statementsign = ref();
    const wstatementsign = ref();
    const statementsigncmd = ref();
    const wstatementsigncmd = ref();

    const setSign = (val, val2) => {
        statementsign.value = val.value;
        wstatementsign.value = val2.value;
    };

    const pop = ref({});

    const inputForm = ref({});

    const initInputForm = async () => {
        inputForm.value = {
            cmd: '',
            compId: compId,
            writeYear: '',
            docNo: '',
            //사고자
            incidentPersonId: '',
            incidentPersonNm: '',
            orgnId: '',
            orgnNm: '',
            jbrpId: '',
            jbrpNm: '',
            birthDt: '',
            phone: '',
            workingAt: '',
            addrs: '',
            partComp: '',
            //목격자
            witnessPersonId: '',
            witnessPersonNm: '',
            worgnId: '',
            worgnNm: '',
            wjbrpId: '',
            wjbrpNm: '',
            wbirthDt: '',
            wphone: '',
            wworkingAt: '',
            waddrs: '',
            wpartComp: '',
            detail: [
                {
                    incidentDetail: '',
                    docSeq: '',
                    useYn: 'Y',
                    files: []
                }
            ],
            hospi: [
                {
                    docSeq: '',
                    hospitalNm: '',
                    hospitalTel: '',
                    hospitalPl: '',
                    hospOnfrDt: '',
                    hospIntoDt: '',
                    hospOutfrDt: '',
                    hospOuttoDt: '',
                    hospIn: '',
                    hospOut: '',
                    useYn: 'Y'
                }
            ],
            opinion: [
                {
                    docSeq: '',
                    preventiveMeasures: '',
                    supervisorsOpinion: '',
                    useYn: 'Y'
                }
            ],
            createdAt: currentDate,
            incidentHis: '',
            incidentExtent: '',
            compOpinion: '',
            incidentLocation: '',
            incidentType: 'I',
            incidentDt: getCurrentDate(),
            incidentTm: '',
            statementContent: '',
            writeDt: '',
            useYn: 'Y',
            ifileId: '',
            wfileId: '',
            img: '',
            wimg: '',
            incidentPmSo: ''
        };
        router.push('/IncidentReportDetail');
    };

    const addDetail = () => {
        if (inputForm.value.detail) {
            inputForm.value.detail.push({
                incidentDetail: '',
                check: true,
                docSeq: '',
                fileId: null,
                useYn: 'Y'
            });
        } else {
            inputForm.value.detail = [
                {
                    incidentDetail: '',
                    check: true,
                    docSeq: '',
                    fileId: null,
                    useYn: 'Y'
                }
            ];
        }
    };

    const addhospi = () => {
        if (inputForm.value.hospi) {
            inputForm.value.hospi.push({
                hospitalNm: '',
                hospitalTel: '',
                hospitalPl: '',
                hospinfrDt: '',
                hospIntoDt: '',
                hospOutfrDt: '',
                hospOuttoDt: '',
                hospIn: '',
                hospOut: '',
                docSeq: '',
                useYn: 'Y'
            });
        } else {
            inputForm.value.hospi = [
                {
                    hospitalNm: '',
                    hospitalTel: '',
                    hospitalPl: '',
                    hospinfrDt: '',
                    hospIntoDt: '',
                    hospOutfrDt: '',
                    hospOuttoDt: '',
                    hospIn: '',
                    hospOut: '',
                    docSeq: '',
                    useYn: 'Y'
                }
            ];
        }
    };

    const addopinion = () => {
        if (inputForm.value.opinion) {
            inputForm.value.opinion.push({
                preventiveMeasures: '',
                supervisorsOpinion: '',
                docSeq: '',
                useYn: 'Y'
            });
        } else {
            inputForm.value.opinion = [
                {
                    preventiveMeasures: '',
                    supervisorsOpinion: '',
                    docSeq: '',
                    useYn: 'Y'
                }
            ];
        }
    };

    const IncidentOrgnList = ref({});
    const IncidnetLocationList = ref({});

    // 원본 데이터를 별도의 변수에 저장
    const IncidentOrgnListOriginal = ref({});
    const IncidnetLocationListOriginal = ref({});

    // 재해발생 보고서 전체 데이터 저장
    function addCreatReportList() {
        const creatResult = {};
        const locationResult = {};

        IncidentList.value.forEach(val => {
            const orgn_nm = val.orgnNm;
            const location = val.incidentLocation || '미설정';

            // 조직별 데이터 구성
            if (!creatResult[orgn_nm]) creatResult[orgn_nm] = [];
            creatResult[orgn_nm].push(val);

            // 발생장소별 데이터 구성
            if (!locationResult[location]) locationResult[location] = [];
            locationResult[location].push(val);
        });

        IncidentOrgnList.value = _.cloneDeep(creatResult);
        IncidnetLocationList.value = _.cloneDeep(locationResult);

        // 원본 데이터 유지
        IncidentOrgnListOriginal.value = _.cloneDeep(creatResult);
        IncidnetLocationListOriginal.value = _.cloneDeep(locationResult);
    }

    let filteredByActReportList = ref({});

    const btnSearch = async notify => {
        return getIncidentList(searchParam.value, notify).then(res => {
            if (res && res.success) {
                IncidentList.value = res.list.map(item => ({
                    ...item,
                    detail: {
                        ['조직']: item.orgnNm,
                        ['사고 발생일']: formatDate(item.incidentDt),
                        ['발생장소']: item.incidentLocation,
                        ['입원']: item.hospIn + ' 일',
                        ['통원']: item.hospOut + ' 일'
                    }
                }));
                addCreatReportList();
            }
            return res; // res 반환
        });
        //return res[];
    };

    const btnAdd = incidentDt => {
        initInputForm();
        //inputForm.value.msdsId = msdsId;
        // 추가(I) 플래그 cmd
        inputForm.value.cmd = 'I';
        // default 값 세팅
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        inputForm.value.incidentDt = incidentDt ? incidentDt : currentDate;
        inputForm.value.writeYear = incidentDt ? incidentDt : '';
        incidentPerson.value = [];
        witnessPerson.value = [];
        router.push({
            name: 'IncidentReportDetail'
        });
    };

    // 상세 보기
    const btnDetail = async val => {
        try {
            inputForm.value.cmd = 'U';
            const res = await getIncidentDetailList({ writeYear: val[0], docNo: val[1] }, false);
            initInputForm();

            if (res && res.success) {
                console.log('### res', res.list);
                for (const item of res.list) {
                    Object.keys(inputForm.value).forEach(key => {
                        inputForm.value[key] = item[key];
                    });

                    inputForm.value.incidentDt = formatDate(inputForm.value.incidentDt);
                    inputForm.value.workingAt = formatDate(inputForm.value.workingAt);
                    inputForm.value.birthDt = formatDate(inputForm.value.birthDt);
                    inputForm.value.writeDt = formatDate(inputForm.value.writeDt);
                    console.log('#### inputForm.value => ', inputForm.value);
                    // incidentPerson 데이터 처리
                    incidentPerson.value = item.incidentPersonId
                        ? [
                              {
                                  id: item.incidentPersonId,
                                  name: item.incidentPersonNm,
                                  //img: incidentImg, // Blob 이미지
                                  fileId: item.ifileId
                              }
                          ]
                        : [];
                    // witnessPerson 데이터 처리
                    witnessPerson.value = item.witnessPersonId
                        ? [
                              {
                                  id: item.witnessPersonId,
                                  name: item.witnessPersonNm,
                                  //img: witnessImg, // Blob 이미지
                                  fileId: item.wfileId
                              }
                          ]
                        : [];

                    incidentUseYn.value = item.useYn;
                }
            }
        } catch (error) {}
    };

    // 추가
    const insertReportData = async param => {
        try {
            loadingPanelStore.openLoading();
            param.incidentDt = formatDateForDB(param.incidentDt);
            param.workingAt = formatDateForDB(param.workingAt);
            param.birthDt = formatDateForDB(param.birthDt);
            param.writeDt = formatDateForDB(param.writeDt);

            let formData = new FormData();
            formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
            const res = await insertIncidentReport(formData, true);

            if (res && res.success) {
                // 비동기 작업을 순차적으로 처리
                if (param.detail) {
                    const saveData = param.detail;
                    await insertReportDetailData(saveData);
                }

                if (param.hospi) {
                    const saveData = param.hospi.filter(el => el.check);
                    await insertReporManageData(saveData);
                }

                if (param.opinion) {
                    const saveData = param.opinion.filter(el => el.check);
                    await insertIncidentReportOpinionData(saveData);
                }

                // 모든 작업이 완료된 후 결과 반환
                //btnDetail([new Date().getFullYear().toString(), doc]);
                return { result: res.result };
            }
        } catch (err) {
        } finally {
            await btnDetail([param.incidentDt.substring(0, 4), doc.value]);
            await loadIncidentReport();
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
        }
    };
    //사건경위 추가
    const insertReportDetailData = async param => {
        try {
            // fileInfo.value가 배열인지 확인
            if (!Array.isArray(fileInfo.value)) {
                return { success: false, message: 'fileInfo.value is not an array.' };
            }

            // param 배열과 fileInfo.value를 순회하여 매핑 처리
            for (let i = 0; i < param.length; i++) {
                if (!param[i].check) {
                    continue;
                }
                const data = param[i];
                data.docNo = inputForm.value.docNo; // docNo 설정
                data.writeYear = inputForm.value.writeYear;
                const fileItem = fileInfo.value[i]; // fileInfo의 현재 항목 매핑
                // 데이터가 비어있는지 확인
                let isEmpty = true;

                // 사건 경위 데이터가 있는지 확인
                if (data.incidentDetail?.trim()) {
                    isEmpty = false;
                }

                // 파일 데이터가 있는지 확인
                if (fileItem && fileItem.editFiles && ((Array.isArray(fileItem.editFiles.insert) && fileItem.editFiles.insert.length > 0) || (Array.isArray(fileItem.editFiles.delete) && fileItem.editFiles.delete.length > 0))) {
                    isEmpty = false;
                }

                // 사건 경위와 파일이 모두 없는 경우 건너뜀
                if (isEmpty) {
                    console.warn(`Skipping docseq ${data.docseq} because both incident details and files are missing.`);
                    continue;
                }

                // 새 FormData 생성
                const formData = new FormData();

                // param 데이터에 deleteFiles 항목 추가
                const deleteFiles = fileItem && fileItem.editFiles && Array.isArray(fileItem.editFiles.delete) ? fileItem.editFiles.delete : [];

                // data 객체에 deleteFiles 필드를 추가
                data.deleteFiles = deleteFiles;

                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                if (fileItem && fileItem.editFiles && Array.isArray(fileItem.editFiles.insert)) {
                    fileItem.editFiles.insert.forEach((file, index) => {
                        if (file) {
                            formData.append('files', file);
                        }
                    });
                } else {
                    console.warn(`No valid files for docseq ${data.docseq}`);
                }
                // 서버로 전송
                await insertIncidentReportDetail(formData, false);

                // 응답 확인
            }
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };
    //사건처리 추가
    const insertReporManageData = async param => {
        try {
            // 각 항목에 대해 처리
            for (const data of param) {
                // 데이터가 비어있는지 확인
                let isEmpty = true;
                // data 객체 내 각 필드가 비어있는지 확인
                if (inputForm.value.hospi.length !== 0 && inputForm.value.hospi) {
                    isEmpty = false;
                }

                // 필드가 모두 비어 있으면 건너뜀
                if (isEmpty) {
                    continue;
                }

                // 빈 문자열을 null로 바꾸기
                data.hospInfrDt = data.hospInfrDt?.trim() ? formatDateForDB(data.hospInfrDt) : null;
                data.hospIntoDt = data.hospIntoDt?.trim() ? formatDateForDB(data.hospIntoDt) : null;
                data.hospOutfrDt = data.hospOutfrDt?.trim() ? formatDateForDB(data.hospOutfrDt) : null;
                data.hospOuttoDt = data.hospOuttoDt?.trim() ? formatDateForDB(data.hospOuttoDt) : null;

                // 추가 정보 설정
                data.docNo = doc;
                data.writeYear = inputForm.value.writeYear;

                // FormData 생성
                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                // 비동기 요청 처리
                const res = await insertIncidentReportManage(formData, false);
                // 응답 확인
                if (res && res.success) {
                } else {
                    console.error('Insert failed for:', data);
                }
            }

            // 모든 처리가 완료된 후 메시지 반환
            return { success: true, message: 'All inserts completed' };
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    //대책방안 및 의견추가
    const insertIncidentReportOpinionData = async param => {
        try {
            // 추가 데이터를 설정 (필요시 주석 해제)
            param.forEach(data => {
                // data.createdby = inputForm.value.createdby;
                data.docNo = doc;
                data.writeYear = inputForm.value.writeYear;
            });
            // 반복적으로 요청을 순차적으로 처리
            for (const data of param) {
                // 데이터가 비어있는지 확인
                let isEmpty = true;

                // data 객체 내 각 필드가 비어있는지 확인
                if (data.preventiveMeasures?.trim() || data.supervisorsOpinion?.trim()) {
                    isEmpty = false; // 하나라도 값이 있으면 isEmpty를 false로 설정
                }

                // 필드가 모두 비어 있으면 건너뜀
                if (isEmpty) {
                    continue;
                }

                // 필드에 값이 있으면 작업을 계속 진행

                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                // 비동기 함수 호출 (순차적으로 실행)
                await insertIncidentReportOpinion(formData, false);
            }

            // 모든 반복이 끝난 후 완료 메시지 반환
            return { success: true, message: 'All inserts completed' };
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    // 진술서추가
    const statemetSave = async param => {
        try {
            pop.value.forEach(data => {
                // `inputForm.value.pop`이 정의되었는지 확인 후 값 할당
                data.statementType = param;
                data.writeYear = inputForm.value.writeYear;
                data.writeDt = formatDateForDB(data.writeDt);
            });

            if (pop.value[0].docSeq === '') {
                await insertStatement(pop.value);
                StatmentdocSeq.value = '';
            } else {
                await updateStatement(pop.value);
            }

            pop.value[0].writeDt = formatDate(pop.value[0].writeDt);
        } catch (err) {}
    };

    const insertStatement = async param => {
        try {
            loadingPanelStore.openLoading();

            // 입력 데이터 설정
            param.forEach(data => {
                data.docNo = inputForm.value.docNo ? inputForm.value.docNo : doc; // docNo 설정
            });
            // 반복적으로 요청을 순차적으로 처리
            for (const data of param) {
                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                const res = await insertIncidentReportStatment(formData, true);

                // 응답 체크
                if (res && res.success) {
                    StatmentdocSeq.value = res.docSeq;
                } else {
                    console.error('Insert failed for:', data);
                }
            }
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
            // 모든 반복이 끝난 후 완료 메시지 반환
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    // 수정
    const updateReportData = async param => {
        try {
            loadingPanelStore.openLoading();
            param.incidentDt = formatDateForDB(param.incidentDt);
            param.workingAt = formatDateForDB(param.workingAt);
            param.birthDt = formatDateForDB(param.birthDt);

            let formData = new FormData();
            formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
            //editFiles.insert.forEach(element => {
            //formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
            //});
            const res = await updateIncidentReport(formData, true);
            if (res && res.success) {
                if (param.detail) {
                    const saveData = param.detail;
                    await UpdateReportDetailData(saveData);
                }

                if (param.hospi) {
                    const saveData = param.hospi.filter(el => el.check);
                    await UpdateReportManageData(saveData);
                }

                if (param.opinion) {
                    const saveData = param.opinion.filter(el => el.check);
                    await UpdateIncidentReportOpinionData(saveData);
                }

                if (incidentStateDelete.value == 'Y') {
                    await DeteleStatement(Ideletestate.value);
                    incidentStateDelete.value = 'N';
                }
                if (withnessStateDelete.value == 'Y') {
                    await DeteleStatement(Wdeletestate.value);
                    withnessStateDelete.value = 'N';
                }

                return { result: res.result };
            }
        } catch (err) {
        } finally {
            await btnDetail([inputForm.value.writeYear, inputForm.value.docNo]);
            await loadIncidentReport();
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
        }
    };

    const updateReportUse = async param => {
        try {
            loadingPanelStore.openLoading();

            let formData = new FormData();
            formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
            //editFiles.insert.forEach(element => {
            //formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
            //});
            const res = await updateIncidentReportuseYn(formData, true);
        } catch (err) {
        } finally {
            await btnDetail([inputForm.value.writeYear, inputForm.value.docNo]);
            await loadIncidentReport();
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
        }
    };

    const UpdateReportDetailData = async param => {
        try {
            // fileInfo.value가 배열인지 확인
            if (!Array.isArray(fileInfo.value)) {
                return { success: false, message: 'fileInfo.value is not an array.' };
            }

            param.forEach(data => {
                // data.createdby = inputForm.value.createdby;
                data.docNo = inputForm.value.docNo;
                data.writeYear = inputForm.value.writeYear;
            });

            // param 배열과 fileInfo.value를 순회하여 매핑 처리
            for (let i = 0; i < param.length; i++) {
                if (!param[i].check) {
                    continue;
                }
                const data = param[i];
                const fileItem = fileInfo.value[i]; // fileInfo의 현재 항목 매핑
                // 데이터가 비어있는지 확인
                let isEmpty = true;

                // 사건 경위 데이터가 있는지 확인
                if (data.incidentDetail?.trim()) {
                    isEmpty = false;
                }

                // 파일 데이터가 있는지 확인
                if (fileItem && fileItem.editFiles && ((Array.isArray(fileItem.editFiles.insert) && fileItem.editFiles.insert.length > 0) || (Array.isArray(fileItem.editFiles.delete) && fileItem.editFiles.delete.length > 0))) {
                    isEmpty = false;
                }

                // 사건 경위와 파일이 모두 없는 경우 건너뜀
                if (isEmpty) {
                    console.warn(`Skipping docseq ${data.docseq} because both incident details and files are missing.`);
                    continue;
                }

                // 새 FormData 생성
                const formData = new FormData();

                // param 데이터에 deleteFiles 항목 추가
                const deleteFiles = fileItem && fileItem.editFiles && Array.isArray(fileItem.editFiles.delete) ? fileItem.editFiles.delete : [];

                // data 객체에 deleteFiles 필드를 추가
                data.deleteFiles = deleteFiles;

                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                if (fileItem && fileItem.editFiles && Array.isArray(fileItem.editFiles.insert)) {
                    fileItem.editFiles.insert.forEach((file, index) => {
                        if (file) {
                            formData.append('files', file);
                        }
                    });
                } else {
                    console.warn(`No valid files for docseq ${data.docseq}`);
                }
                //let res
                if (!data.docSeq) {
                    //docseq가 없으면 Insert
                    await insertIncidentReportDetail(formData, false);
                } else {
                    //docseq가 있으면 Update
                    await updateIncidentReportDetail(formData, false);
                }
            }
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    const UpdateReportManageData = async param => {
        try {
            // 추가 데이터를 설정 (필요시 주석 해제)
            param.forEach(data => {
                // data.createdby = inputForm.value.createdby;
                data.docNo = inputForm.value.docNo;
                data.writeYear = inputForm.value.writeYear;
            });
            // 반복적으로 요청을 순차적으로 처리
            for (const data of param) {
                const isEmptyData = !data.hospitalNm?.trim() && !data.hospitalTel?.trim() && !data.hospitalPl?.trim() && !data.hospInfrDt?.trim() && !data.hospIntoDt?.trim() && !data.hospOutfrDt?.trim() && !data.hospOuttoDt?.trim();

                // 데이터가 모두 비어 있으면 건너뜀
                if (isEmptyData) {
                    continue;
                }

                // 빈 문자열을 null로 바꾸기
                data.hospInfrDt = data.hospInfrDt?.trim() ? formatDateForDB(data.hospInfrDt) : null;
                data.hospIntoDt = data.hospIntoDt?.trim() ? formatDateForDB(data.hospIntoDt) : null;
                data.hospOutfrDt = data.hospOutfrDt?.trim() ? formatDateForDB(data.hospOutfrDt) : null;
                data.hospOuttoDt = data.hospOuttoDt?.trim() ? formatDateForDB(data.hospOuttoDt) : null;

                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                // 파일이 추가되어야 한다면 주석 해제하여 사용
                // editFiles.insert.forEach(element => {
                //     formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
                // });

                // 비동기 함수 호출 (순차적으로 실행)
                let res;
                if (!data.docSeq) {
                    //docseq가 없으면 업데이트
                    res = await insertIncidentReportManage(formData, false);
                } else {
                    //docseq가 있으면 업데이트
                    res = await UpdateIncidentReportManage(formData, false);
                }
            }
            // 모든 반복이 끝난 후 완료 메시지 반환
            return { success: true, message: 'All inserts completed' };
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    const UpdateIncidentReportOpinionData = async param => {
        try {
            // 추가 데이터를 설정 (필요시 주석 해제)
            param.forEach(data => {
                // data.createdby = inputForm.value.createdby;
                data.docNo = inputForm.value.docNo;
                data.writeYear = inputForm.value.writeYear;
            });

            // 반복적으로 요청을 순차적으로 처리
            for (const data of param) {
                const isEmptyData = !data.hospitalNm?.trim() && !data.preventiveMeasures?.trim() && !data.supervisorsOpinion?.trim();

                // 데이터가 모두 비어 있으면 건너뜀
                if (isEmptyData) {
                    continue;
                }

                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                // 파일이 추가되어야 한다면 주석 해제하여 사용
                // editFiles.insert.forEach(element => {
                //     formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
                // });

                // 비동기 함수 호출 (순차적으로 실행)
                let res;
                if (!data.docSeq) {
                    //docseq가 없으면 업데이트
                    res = await insertIncidentReportOpinion(formData, false);
                } else {
                    //docseq가 있으면 업데이트
                    res = await UpdateIncidentReportOpinion(formData, false);
                }
            }
            // 모든 반복이 끝난 후 완료 메시지 반환
            return { success: true, message: 'All inserts completed' };
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    const updateStatement = async param => {
        try {
            loadingPanelStore.openLoading();
            // 입력 데이터 설정
            param.forEach(data => {
                data.docNo = inputForm.value.docNo ? inputForm.value.docNo : doc; // docNo 설정
            });
            // 반복적으로 요청을 순차적으로 처리
            for (const data of param) {
                const formData = new FormData();
                formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

                // 파일이 추가되어야 한다면 주석 해제하여 사용
                // editFiles.insert.forEach(element => {
                //     formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
                // });

                // 비동기 함수 호출 (순차적으로 실행)
                const res = await updateIncidentReportStatment(formData, true);
            }
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
            // 모든 반복이 끝난 후 완료 메시지 반환
            return { success: true, message: 'All inserts completed' };
        } catch (err) {
            console.error('Insert error:', err);
            return { success: false, message: 'Error during insert', error: err };
        }
    };

    //문서번호 할당
    const getDoc = writeYear => {
        try {
            const param = {
                writeYear: writeYear
            };
            getIcidentDocNo(param).then(res => {
                if (res) {
                    doc.value = res;
                }
            });
        } catch (err) {}
    };

    const btnSave = async param => {
        try {
            if (param == 'I') {
                if (inputForm.value.cmd === 'I') {
                    //추가
                    getDoc(inputForm.value.writeYear);
                    inputForm.value.docNo = doc;
                    await insertReportData(inputForm.value);
                } else {
                    //수정
                    updateReportData(inputForm.value);
                }
            } else if (param == 'U') {
                updateReportUse(inputForm.value);
            }
            return true;
        } catch (error) {}
    };

    //사고처리 조회
    const searchManage = param => {
        try {
            return getIncidentDetailManage({ writeYear: param[0], docNo: param[1] }, false).then(res => {
                if (res && res.success) {
                    if (res.list.length > 0) {
                        // 날짜 형식 포맷팅
                        res.list.forEach(item => {
                            item.hospInfrDt = formatDate(item.hospInfrDt) || '';
                            item.hospIntoDt = formatDate(item.hospIntoDt) || '';
                            item.hospOutfrDt = formatDate(item.hospOutfrDt) || '';
                            item.hospOuttoDt = formatDate(item.hospOuttoDt) || '';
                        });

                        inputForm.value.hospi = res.list;
                    } else {
                        inputForm.value.hospi = [
                            {
                                hospitalNm: '',
                                hospitalTel: '',
                                hospitalPl: '',
                                hospInfrDt: '',
                                hospIntoDt: '',
                                hospOutfrDt: '',
                                hospOuttoDt: '',
                                hospIn: '',
                                hospOut: '',
                                useYn: 'Y'
                            }
                        ];
                    }
                } else {
                    inputForm.value.hospi = [
                        {
                            hospitalNm: '',
                            hospitalTel: '',
                            hospitalPl: '',
                            hospInfrDt: '',
                            hospIntoDt: '',
                            hospOutfrDt: '',
                            hospOuttoDt: '',
                            hospIn: '',
                            hospOut: '',
                            useYn: 'Y'
                        }
                    ];
                }
                return res;
            });
        } catch (err) {}
    };

    //예방의견조회
    const searchOpinion = param => {
        try {
            return getIncidentDetailOpinion({ writeYear: param[0], docNo: param[1] }, false).then(res => {
                if (res && res.success) {
                    if (res.list.length > 0) {
                        inputForm.value.opinion = res.list;
                    } else {
                        inputForm.value.opinion = [
                            {
                                preventiveMeasures: '',
                                supervisorsOpinion: '',
                                useYn: 'Y'
                            }
                        ];
                    }
                } else {
                    inputForm.value.opinion = [
                        {
                            preventiveMeasures: '',
                            supervisorsOpinion: '',
                            useYn: 'Y'
                        }
                    ];
                }

                return res;
            });
        } catch (err) {}
    };

    //사고경위조회
    const searchDetail = param => {
        return getIncidentDetailExtent({ writeYear: param[0], docNo: param[1] }, false)
            .then(res => {
                if (res && res.success) {
                    if (res.list.length > 0) {
                        inputForm.value.detail = res.list;
                        inputForm.value.detail.forEach(data => {
                            // data.createdby = inputForm.value.createdby;
                            data.writeYear = param[0];
                            data.docNo = param[1];
                        });
                    } else {
                        inputForm.value.detail = [
                            {
                                incidentDetail: '',
                                useYn: 'Y'
                            }
                        ];
                    }
                } else {
                    inputForm.value.detail = [
                        {
                            incidentDetail: '',
                            useYn: 'Y'
                        }
                    ];
                }

                return res;
            })
            .finally(() => {
                for (let index = 0; inputForm.value.detail.length > index; index++) {
                    // 파일 조회
                    if (fileInfo.value[index] && typeof fileInfo.value[index].fnSearch === 'function') {
                        fileInfo.value[index].fnSearch();
                    }
                }
            });
    };

    const searchState = param => {
        const type = param[2] === 'I' ? 'incidentsign' : 'witnesssign';
        return getIncidentState({ writeYear: param[0], docNo: param[1], statementType: param[2], type: type }, false).then(res => {
            if (res && res.success) {
                if (res.list.length > 0) {
                    res.list[0].writeDt = formatDate(res.list[0].writeDt);
                    pop.value = res.list;
                    if (param[2] === 'I') {
                        statementsigncmd.value = pop.value[0].targetId;
                    } else {
                        wstatementsigncmd.value = pop.value[0].targetId;
                    }
                    StatmentdocSeq.value = res.list[0].docSeq;
                } else {
                    pop.value = {
                        statementContent: '',
                        writeCt: '',
                        docSeq: ''
                    };
                    if (param[2] === 'I') {
                        statementsigncmd.value = 'I';
                    } else {
                        wstatementsigncmd.value = 'I';
                    }
                }
            } else {
                pop.value = {
                    statementContent: '',
                    writeCt: '',
                    docSeq: ''
                };

                if (param[2] === 'I') {
                    statementsigncmd.value = 'I';
                } else {
                    wstatementsigncmd.value = 'I';
                }
            }
            return res;
        });
    };

    const fileInfo = ref([]);
    const file = file => {
        fileInfo.value = file.value;
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };
    const toggleDetailUseYn = (index, event) => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.detail[index].useYn = event.target.checked ? 'Y' : 'N';
        inputForm.value.detail[index].check = true;
    };
    const toggleHospiUseYn = (index, event) => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.hospi[index].useYn = event.target.checked ? 'Y' : 'N';
    };
    const toggleOpinionUseYn = (index, event) => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.opinion[index].useYn = event.target.checked ? 'Y' : 'N';
    };

    //일괄 삭제
    const btnDelete = async item => {
        try {
            if (item === 'D') {
                //개별삭제
                // if(detaillist.value.length==0 && hospilist.value.length==0 && opinionlist.value.length==0)
                if (prtchk.value == 'N') {
                    alertMsg('warning', '선택된 항목이 없습니다.');
                    return;
                } else {
                    if (detaillist.value.some(el => el.useYn === 'N')) {
                        //toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                        alertMsg('error', '사고 발생 경위에 <br />이미 삭제된 항목이 선택되었습니다.');
                        return;
                    } else if (hospilist.value.some(el => el.useYn === 'N')) {
                        //toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                        alertMsg('error', '치료 기간에 이미 <br />삭제된 항목이 선택되었습니다.');
                        return;
                    } else if (opinionlist.value.some(el => el.useYn === 'N')) {
                        //toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                        alertMsg('error', '예방 대책 및 의견에 <br />이미 삭제된 항목이 선택되었습니다.');
                        return;
                    } else {
                        confirmMsg('warning', '삭제 하시겠습니까?', null, {
                            fun: async () => {
                                loadingPanelStore.openLoading();
                                await deleteIncidentReportDetail({ docNo: inputForm.value.docNo });
                                nextTick(() => {
                                    loadingPanelStore.endLoading();
                                });
                            }
                        });
                    }
                }
            } else {
                let param = checkedList.value; // rowKey로 행 데이터를 가져옴

                if (!param.length) {
                    //toastPopup('선택된 항목이 없습니다.', 'error');
                    alertMsg('warning', '선택된 항목이 없습니다.');
                    return;
                }
                if (param.some(el => el.useYn === 'N')) {
                    //toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                    alertMsg('error', '이미 삭제 처리된 항목입니다.');
                    return;
                }
                const incidentPersonNm = param.map(el => el.incidentPersonNm);
                if (incidentPersonNm.length === 1) {
                    confirmMsg('warning', '삭제 하시겠습니까?', ``, {
                        fun: async () => {
                            loadingPanelStore.openLoading();
                            await deleteIncidentReport({ updateBy: userStore.userId, docNo: param[0].docNo, writeYear: param[0].writeYear });
                            btnSearch(true);
                            nextTick(() => {
                                loadingPanelStore.endLoading();
                            });
                        }
                    });
                } else {
                    confirmMsg('warning', '삭제 하시겠습니까?', ``, {
                        fun: async () => {
                            loadingPanelStore.openLoading();

                            await deleteIncidentReports(param);
                            btnSearch(true);

                            nextTick(() => {
                                loadingPanelStore.endLoading();
                            });
                        }
                    });
                }
            }
        } catch (error) {
            console.error('삭제 처리 중 오류 발생:', error);
        }
        // finally {
        //     // 삭제 후 무조건 재조회
        //     if (item === 'D') {
        //         await btnDetail([inputForm.value.writeYear, inputForm.value.docNo])
        //         await loadIncidentReport()
        //     }
        //     else {
        //         btnSearch(true);
        //     }
        // }
    };
    const deleteIncidentReportDetail = async () => {
        try {
            if (detaillist.value.length > 0) {
                await deleteIncidentDetail(detaillist.value);
            }
            if (hospilist.value.length > 0) {
                await deleteIncidentHospi(hospilist.value);
            }
            if (opinionlist.value.length > 0) {
                await deleteIncidentOpinion(opinionlist.value);
            }
        } catch (err) {
        } finally {
            await btnDetail([inputForm.value.writeYear, inputForm.value.docNo]);
            await loadIncidentReport();
        }
    };

    const btnPrint = async item => {
        try {
            let param = [];
            // = checkedList.value
            if (item === 'A') {
                if (checkedList.value.length === 0) {
                    alertMsg('warning', t('msgNoItem'));
                    return;
                }

                // loadingPanelStore.openLoading();
                confirmMsg('info', '출력 하시겠습니까?', null, {
                    fun: async () => {
                        // 선택된 항목을 한 번에 처리하도록 param 구성
                        const chkList = checkedList.value.map(el => ({
                            writeYear: el.writeYear,
                            docType: el.docType,
                            docNo: el.docNo,
                            compId: el.compId
                        }));

                        // 다중 항목을 하나의 요청으로 처리
                        try {
                            let searchVO = { checkedObjList: chkList };
                            // const res = await IncidentReportCombine({ checkedObjList: chkList }, true);
                            baseDownload(IncidentReportCombine, searchVO); // 다운로드
                        } catch (error) {
                            console.error('Error processing items:', error);
                            loadingPanelStore.endLoading();
                        }
                    }
                });
                // nextTick(() => {
                //     loadingPanelStore.endLoading();
                // });
            } else if (item === 'D') {
                // `confirmMsg` 추가
                confirmMsg('info', '출력 하시겠습니까?', '', {
                    fun: async () => {
                        if (prtchk.value === 'N') {
                            // 체크된 게 없어서 use_yn이 Y인 것만 출력
                            // param = inputForm.value;
                            // const res = await IncidentReportPrint({ checkedObjList: param });
                            let chkList = [
                                {
                                    writeYear: inputForm.value.writeYear,
                                    docType: 'ICR',
                                    docNo: inputForm.value.docNo,
                                    compId: inputForm.value.compId
                                }
                            ];
                            let searchVO = { checkedObjList: chkList };
                            baseDownload(IncidentReportPrint, searchVO);
                        } else if (prtchk.value === 'Y') {
                            // 체크된 것만 출력

                            const inputList = {
                                writeYear: inputForm.value.writeYear,
                                docNo: inputForm.value.docNo
                            };
                            param = [inputList, detaillist.value, hospilist.value, opinionlist.value];
                            param[0].type = buttonListStore.downloadType;
                            // const res = await IncidentReportPrintchecked(param);
                            // downloadReport(res);

                            baseDownload(IncidentReportPrintchecked, param);
                        }
                    }
                });
            }
        } catch (err) {
            console.error(err);
        }
    };

    const statemetPrint = param => {
        try {
            const chk = ref([]);
            let chklist = [];
            const isDocNoAvailable = inputForm.value.docNo !== '';
            let docseq = '';
            const dataSource = isDocNoAvailable ? pop : checkedList.value[0];

            pop.value.forEach(data => {
                data.statementType = param;
                data.writeYear = isDocNoAvailable ? inputForm.value.writeYear : dataSource.writeYear;
                data.docNo = isDocNoAvailable ? inputForm.value.docNo : dataSource.docNo;
                docseq = data.docSeq;
            });

            chklist = [
                {
                    writeYear: inputForm.value.writeYear,
                    docSeq: docseq,
                    statementType: param,
                    docNo: inputForm.value.docNo
                }
            ];

            chk.value.push(pop.value);
            const dataToPrint = chk.value[0];

            const processPrint = printFunction => {
                return new Promise(resolve => {
                    printFunction(dataToPrint).then(res => {
                        resolve({ result: res.result, success: res.success });

                        const link = document.createElement('a');
                        const objectUrl = window.URL.createObjectURL(res.data);
                        link.href = objectUrl;
                        link.download = res.headers.filename;
                        link.click();
                    });
                });
            };

            if (param === 'I') {
                return processPrint(() => IncidentReportStatementPrint({ writeYear: inputForm.value.writeYear, checkedObjList: chklist }));
            } else if (param === 'W') {
                return processPrint(() => WitnessReportStatementPrint({ writeYear: inputForm.value.writeYear, checkedObjList: chklist }));
            }
        } catch (err) {
            console.error(err);
        }
    };

    const loadIncidentReport = () => {
        if (inputForm.value.docNo != '') {
            searchManage([inputForm.value.writeYear, inputForm.value.docNo], false);
            searchDetail([inputForm.value.writeYear, inputForm.value.docNo], false);
            searchOpinion([inputForm.value.writeYear, inputForm.value.docNo], false);
        } else {
            getDoc(inputForm.value.writeYear);
            const today = new Date();
            const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
            const day = today.getDate().toString().padStart(2, '0'); // 01~31
            const incidentDt = formatDate(searchParam.value.writeYear + month + day);
            inputForm.value.incidentDt = incidentDt;
        }
        prtchk.value = 'N';
        detaillist.value = [];
        hospilist.value = [];
        opinionlist.value = [];
    };

    const searchTerm = ref('');
    const filteredByCreatReportListBySearch = ref({});
    const filteredByActReportListBySearch = ref({});

    // 검색어 필터링
    const applyFilter = () => {
        const term = searchTerm.value.trim().toLowerCase();

        if (!term) {
            // 검색어가 없으면 원본 데이터로 복원
            IncidentOrgnList.value = _.cloneDeep(IncidentOrgnListOriginal.value);
            IncidnetLocationList.value = _.cloneDeep(IncidnetLocationListOriginal.value);
            return;
        }

        // 검색어가 있을 때만 필터링 수행

        // 조직 기준
        IncidentOrgnList.value = Object.entries(IncidentOrgnListOriginal.value).reduce((filtered, [key, items]) => {
            const matchedItems = items.filter(item => (item.incidentPersonNm && item.incidentPersonNm.toLowerCase().includes(term)) || (item.orgnNm && item.orgnNm.toLowerCase().includes(term)) || (item.incidentLocation && item.incidentLocation.toLowerCase().includes(term)) || (item.incidentDt && formatDate(item.incidentDt).includes(term)));
            if (matchedItems.length) filtered[key] = matchedItems;
            return filtered;
        }, {});

        // 발생 장소 기준
        IncidnetLocationList.value = Object.entries(IncidnetLocationListOriginal.value).reduce((filtered, [key, items]) => {
            const matchedItems = items.filter(item => (item.incidentPersonNm && item.incidentPersonNm.toLowerCase().includes(term)) || (item.orgnNm && item.orgnNm.toLowerCase().includes(term)) || (item.incidentLocation && item.incidentLocation.toLowerCase().includes(term)) || (item.incidentDt && formatDate(item.incidentDt).includes(term)));
            if (matchedItems.length) filtered[key] = matchedItems;
            return filtered;
        }, {});
    };

    return {
        initInputForm,
        inputForm,
        checkedList,
        currentFilter,
        IncidentReportDivList,
        searchParam,
        selectedIncidentReportId,
        StateList,
        fileInfo,
        file,
        workItems,
        // function
        initData,
        toggleUseYn,
        IncidentReportList,
        filteredByCreatReportList,
        filteredByCreatReportListBySearch,
        filteredByActReportList,
        filteredByActReportListBySearch,
        IncidentManagelist,
        IncidentOrgnList,
        IncidnetLocationList,
        getDoc,
        //버튼
        btnSearch,
        btnAdd,
        btnDelete,
        btnSave,
        btnDetail,
        btnPrint,
        addDetail,
        addhospi,
        addopinion,
        searchDetail,
        searchManage,
        searchOpinion,
        searchState,
        statemetSave,
        statemetPrint,
        setSign,
        loadIncidentReport,
        toggleDetailUseYn,
        toggleHospiUseYn,
        toggleOpinionUseYn,
        //사고자, 목격자
        incident_item,
        withness_item,
        detail,
        hospi,
        opinion,
        pop,
        statementsigncmd,
        wstatementsigncmd,
        incidentPerson,
        witnessPerson,
        StatmentdocSeq,
        gubun,
        doc,
        detaillist,
        hospilist,
        opinionlist,
        prtchk,
        incidentUseYn,
        incidentStateDelete,
        withnessStateDelete,
        Ideletestate,
        Wdeletestate,
        applyFilter,
        searchTerm
    };
});
