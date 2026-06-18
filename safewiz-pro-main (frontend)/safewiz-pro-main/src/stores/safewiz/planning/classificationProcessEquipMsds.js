import { defineStore } from 'pinia';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, confirmMsg, getCompId, getCurrentDate, t, alertMsg, baseDownload, formatDate, formatDateForDB,validationStore } = BaseView();
import { getClassProcEquipMsdsList, getClassProcEquipMsdsDetail, deleteClassProcEquipMsds, confirmClassProcs, getReport } from '@/stores/safewiz/planning/api/classificationProcessEquipMsdsApi';
import { insertOrganization, updateOrganization } from '@/stores/safewiz/planning/api/riskAssessmentOrganChartApi';

// calendar year
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
const riskAStore = useRiskAStore();

export const useClassProcEquipMsdsStore = defineStore('classProcEquipMsds', () => {
    // 조직 리스트
    const organizationList = ref([]);
    const compItem = ref([]); // Chip 리스트
    const headerList = ref([]);
    const compId = getCompId();
    const orgnId = ref('');
    const peopleList = ref([]);
    const orgnPopup = ref(); // 조직팝업
    const orgnItem = ref([]); // 조직 Chip 리스트
    const headItem = ref([]); // 조직장 Chip 리스트
    const cmd = ref(null); // 상태 값 (I: 신규생성, U: 수정)
    const participantList = ref([]); // 참여자 정보
    const processList = ref([]); // 공정분석 정보
    const writeYear = ref(riskAStore.searchParam.searchText); // 년도

    // 확정/미확정 리스트
    const confirmedClassProcs = ref([]);
    const unConfirmedClassProcs = ref([]);

    // 차수 데이터 적용
    const initConfirmedClassProcs = list => {
        confirmedClassProcs.value = list
            .filter(item => item.confirmYn === 'Y')
            .map(el => ({
                ...el,
                detail: {
                    [t('evaluationPeriod')]: formatDate(el.startAt) + ' ~ ' + formatDate(el.endAt),
                    [t('processOverview')]: el.processOverview,
                    // 참여자가 2명 이상이면 'ㅇㅇㅇ 외 n명' 노출, 1명이면 'ㅇㅇㅇ(이름만)' 노출
                    [t('participants')]: el.hrList.length > 1 ? el.hrList[0].hrNm + ' 외 ' + (el.hrList?.length - 1 || '') + '명' : el.hrList.length == 1 ? el.hrList[0].hrNm : '',
                    [t('processCount')]: el.processList.length > 0 ? el.processList.length : 0,
                    [t('classProcWritemDt')]: formatDate(el.writeDt),
                    [t('classProcConfirmDt')]: el.confirmDt ? formatDate(el.confirmDt) : '-'
                }
            }));

        unConfirmedClassProcs.value = list
            .filter(item => item.confirmYn === 'N')
            .map(el => ({
                ...el,
                detail: {
                    [t('evaluationPeriod')]: formatDate(el.startAt) + ' ~ ' + formatDate(el.endAt),
                    [t('processOverview')]: el.processOverview,
                    // 참여자가 2명 이상이면 'ㅇㅇㅇ 외 n명' 노출, 1명이면 'ㅇㅇㅇ(이름만)' 노출
                    [t('participants')]: el.hrList.length > 1 ? el.hrList[0].hrNm + ' 외 ' + (el.hrList?.length - 1 || '') + '명' : el.hrList.length == 1 ? el.hrList[0].hrNm : '',
                    [t('processCount')]: el.processList.length > 0 ? el.processList.length : 0,
                    [t('classProcWritemDt')]: formatDate(el.writeDt),
                    [t('safehealth_confirmDt')]: el.confirmDt ? formatDate(el.confirmDt) : '-'
                }
            }));
    };

    // 초기화 함수
    const inputForm = ref({
        cmd: '',
        cmdArray: '',
        docNo: '',
        docType: '',
        writeYear: '',
        compId: compId,
        compList: [
            {
                compId: '',
                nm: ''
            }
        ],
        userId: '',
        hrId: '',
        hrNm: '',
        jbrp: '', // 직위
        jbrpNm: '', // 직위명
        orgnId: '',
        orgnNm: '',
        partCompId: '',
        upPrntOrgnId: '',
        orgnDesc: '',
        prntOrgnId: '',
        orgnLv: '',
        ordSeq: '',
        headHrId: '',
        headHrNm: '',
        primaryId: null,
        secondHrId: '',
        secondHrNm: '',
        secondaryId: null,
        updatedAt: null,
        updatedBy: null,
        createdAt: null,
        createdBy: null,
        checked: '',
        useYn: 'Y',
        memberCount: ''
    });

    const currentDate = ref(getCurrentDate());
    const searchTerm = ref('');
    const classProcsList = ref(null);
    const checkedList = ref([]);
    const manager = ref({ id: null, hrNm: '', signature: '', img: '' });
    const subManager = ref({ id: null, hrNm: '', signature: '', img: '' });

    // 조회 조건
    let searchParam = ref({
        compId: getCompId(),
        writeYear: writeYear.value,
        docType: 'CPE'
    });

    // 조직 조회
    const getClassProcList = async notify => {
        // 목록 초기화
        organizationList.value = [];
        classProcsList.value = [];
        const res = await getClassProcEquipMsdsList(searchParam.value, notify);
        if (res?.success) {
            organizationList.value = res.list;
            applyFilter();
        }
        return res; // res 반환
    };

    // 조직 상세 조회
    const getClassProcDetail = async (data, notify) => {
        const res = await getClassProcEquipMsdsDetail(data, notify);
        // 값 세팅 전 기존 데이터 초기화
        inputForm.value = null;
        orgnItem.value = [];
        headItem.value = [];
        participantList.value = [];
        processList.value = [];
        if (res?.success) {
            inputForm.value = res.list;
            inputForm.value.confirmDt = inputForm.value.confirmDt ? formatDate(inputForm.value.confirmDt) : '';
            inputForm.value.startAt = inputForm.value.startAt ? formatDate(inputForm.value.startAt) : '';
            inputForm.value.endAt = inputForm.value.endAt ? formatDate(inputForm.value.endAt) : '';
            inputForm.value.writeDt = inputForm.value.writeDt ? formatDate(inputForm.value.writeDt) : '';
            // 조직 정보
            orgnItem.value = [{ id: res.list.orgnId, name: res.list.orgnNm }];
            // 조직장 정보
            headItem.value.push({ id: res.list.headId, name: res.list.headNm, memberCount: res.list?.hrList.length });
            // 참여자 정보
            if (res.list.hrList.length > 0) {
                res.list.hrList.forEach(data => {
                    let row = {
                        id: data.hrId,
                        name: data.hrNm,
                        hrId: data.hrId,
                        hrNm: data.hrNm
                    };
                    participantList.value.push(row);
                });
            }
            // 공정분석 정보
            if (res.list.processList.length > 0) {
                res.list.processList.forEach(data => {
                    if (data.procDocNo != null && data.procDocSeq != null) {
                        let row = {
                            checked: false,
                            id: data.processId,
                            name: data.processNm,
                            processDesc: data?.processDesc || '',
                            processId: data.processId,
                            processNm: data.processNm,
                            revNo: data.revNo,
                            revNm: data.revNm,
                            isUsed: data.isUsed,
                            equip: [],
                            msds: []
                        };
                        data.itemList.forEach(item => {
                            if (item.type.toUpperCase() == 'EQUIP') {
                                let eq = { id: item.itemId, name: item.itemNm };
                                row.equip.push(eq);
                            } else {
                                let ms = { id: item.itemId, name: item.itemNm };
                                row.msds.push(ms);
                            }
                        });
                        processList.value.push(row);
                    }
                });
            }
        }
        return res; // res 반환
    };

    // 조직 일괄 삭제
    const deleteClassProcList = async data => {
        searchParam.value = {
            compId: getCompId(),
            writeYear: data.writeYear,
            docType: 'CPE'
        };

        const res = await deleteClassProcEquipMsds(data, true);
        if (res?.success) {
            classProcsList.value = []; // 리스트 초기화
            checkedList.value = []; // 체크리스트 초기화
            getClassProcList();
        }
    };

    // 조직 추가
    const createOrgn = async data => {
        const res = await insertOrganization(data, true);
        return res?.success ? { result: res.result } : null;
    };

    // 조직 수정
    const updateOrgn = async data => {
        const res = await updateOrganization(inputForm.value.orgnId, data, true);
        return res?.success ? { result: res.result } : null;
    };

    // 입력 검증
    const checkNumberInput = event => {
        event.target.value = event.target.value.replace(/[^0-9]/g, '');
        inputForm.value.ordSeq = event.target.value;
    };

    // 필터 적용
    const applyFilter = () => {
        const filteredData = organizationList.value.filter(
            item =>
                item.orgnNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || // 조직명
                item.startAt?.toLowerCase().includes(searchTerm.value.toLowerCase()) || // 평가 시작일
                item.endAt?.toLowerCase().includes(searchTerm.value.toLowerCase()) || // 평가 종료일
                item.processOverview?.toLowerCase().includes(searchTerm.value.toLowerCase()) || // 주요업무내용(공정개요)
                (item?.hrList && item?.hrList[0] && item?.hrList[0].hrNm.toLowerCase().includes(searchTerm.value.toLowerCase())) ||
                (item?.processList && item?.processList.length == searchTerm.value.toLowerCase()) ||
                item.createdAt?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                formatDateForDB(item.createdAt)?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                formatDate(item.confirmDt)?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                formatDateForDB(item.confirmDt)?.toLowerCase().includes(searchTerm.value.toLowerCase())
        );
        classProcsList.value = filteredData;

        initConfirmedClassProcs(classProcsList.value);
    };

    const setRefs = orgn => {
        orgnPopup.value = orgn.value;
    };

    const closeOrgn = e => {
        if (e && e.length) {
            // 선택한 조직 데이터가 있는 경우
            if (orgnItem.value.length > 0) {
                // 기존 조직 데이터가 있는 경우
                confirmMsg('info', '변경 시 이전에 작성한 데이터가 사라집니다.\n계속하시겠습니까?', '', { fun: changeOrgn, param: e });
            } else {
                changeOrgn(e);
            }
            validationStore.clearInvalidClasses();
            validationStore.clearAllErrors();
        } else {
            // 선택한 조직 데이터가 없는 경우 팝업 닫음
            orgnPopup.value.onClose();
        }
    };

    // 조직 데이터 변경
    const changeOrgn = e => {
        headItem.value = [];
        processList.value = [];
        //chips에 넣기위해 id:'', name:'' 으로 세팅
        orgnPopup.value.onClose();
        if (e && e.length) {
            // multi 일때
            const refinedItems = e.map(el => ({
                id: el.orgnId,
                name: el.orgnNm,
                memberCount: el.resultCnt
            }));
            orgnItem.value = refinedItems;
            // 조직장
            headItem.value.push({ id: e[0].headHrId, name: e[0].headHrNm, memberCount: e[0].resultCnt });
        } else if (e.length !== 0) {
            // if (e.length !== 0) 값이있을때 선택안하 팝업을닫으면 초기화 되는 상황 예외처리
            // single 일때
            const refinedItems = [
                {
                    id: e.orgnId,
                    name: e.orgnNm,
                    memberCount: e.resultCnt
                }
            ];
            orgnItem.value = refinedItems;
            headItem.value.push({ id: e.headHrId, name: e.headHrNm, memberCount: e.resultCnt });
        }
    };

    // 추가 버튼
    const addOrg = () => {
        peopleList.value = []; // 인원 리스트 초기화
        manager.value = {}; // 조직장 초기화
        subManager.value = {}; // 부장 초기화
        inputForm.value.cmd = 'I'; // 추가 플래그
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        orgnItem.value = [];
        router.push({ path: '/organizationManageDetail' });
    };

    // 저장
    const btnSave = async () => {
        // chips 저장로직
        const assignId = (source, targetKey) => {
            inputForm.value[targetKey] = source.value.length > 0 ? source.value[0].id : '';
        };
        assignId(orgnItem, 'orgnId');

        const action = inputForm.value.cmd === 'I' ? createOrgn : updateOrgn;
        confirmMsg('info', t('msgSave'), '', { fun: action, param: inputForm.value });
    };

    const updateConfirmClassProcs = item => {
        return new Promise(resolve => {
            confirmClassProcs(item).then(res => {
                if (res?.success) {
                    classProcsList.value = []; // 리스트 초기화
                    checkedList.value = []; // 체크리스트 초기화
                    getClassProcList();
                    resolve({ result: res.result, success: res.success });
                }
            });
        });
    };

    // 일괄 삭제
    const btnDelete = async () => {
        const unConfirmedParam = unConfirmedClassProcs.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
        const confirmedParam = confirmedClassProcs.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
        if (confirmedParam.length) {
            confirmedParam.forEach(item => {
                item.selected = false;
                item.checked = false;
            });
            return alertMsg('warning', t('msgCantDeleteConfirmedOrder'));
        }
        if (!unConfirmedParam.length) {
            return alertMsg('warning', t('msgNoItem'));
        }
        if (unConfirmedParam.some(el => el.useYn === 'N')) {
            return toastPopup('warning', t('msgDeletedItem'));
        }
        confirmMsg('warning', t('msgDelete'), ``, { fun: deleteClassProcList, param: unConfirmedParam });
    };

    // useYn 체크
    const toggleUseYn = event => {
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    const closeComp = e => {
        //chips에 넣기위해 id:'', name:'' 으로 세팅
        compPopup.value.onClose();

        if (e && e.length) {
            // multi 일때
            const refinedItems = e.map(el => ({
                id: el.compId,
                name: el.compNm
            }));
            compItem.value = refinedItems;
        } else if (e.length !== 0) {
            // if (e.length !== 0) 값이있을때 선택안하 팝업을닫으면 초기화 되는 상황 예외처리
            // single 일때
            const refinedItems = [
                {
                    id: e.compId,
                    name: e.compNm
                }
            ];
            compItem.value = refinedItems;
        }
    };

    // 공정/설비/물질 구분 출력물 다운로드
    const reportDownload = async data => {
        const param = {
            writeYear: getCurrentDate().substring(0, 4),
            docType: 'CPE',
            docNo: data.docNo,
            orgnId: data.orgnId,
            compId: getCompId(),
            checkedList: data.map(el => el.docNo) // 다운로드 할 docNo ['00001', '00002']
        };
        baseDownload(getReport, param);
        // getReport(inputForm.value, false).then(res => {
        //   downloadReport(res);
        //   param = [];
        // })
    };

    return {
        setRefs,
        closeOrgn,
        inputForm,
        orgnPopup,
        orgnItem,
        headItem,
        cmd,
        orgnId,
        manager,
        subManager,
        searchParam,
        toggleUseYn,
        checkedList,
        searchTerm,
        applyFilter,
        classProcsList,
        addOrg, // 라우터
        btnSave,
        btnDelete, // 버튼
        organizationList,
        createOrgn,
        updateOrgn,
        deleteClassProcList,
        getClassProcDetail,
        headerList,
        currentDate,
        checkNumberInput,
        peopleList,
        closeComp,
        getClassProcList,
        participantList,
        processList,
        reportDownload,
        writeYear,
        confirmedClassProcs,
        unConfirmedClassProcs,
        updateConfirmClassProcs
    };
});
