import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, reactive, toastPopup, getCompId, getCurrentDate, alertMsg, watch, formatDate, openLoading, endLoading, getFormattedDate, formatDateForDB } = BaseView();
import { nextTick } from 'vue';
import { gsap } from 'gsap';
import { getRiskMain, getRiskAndOpsAsmtCriteria, getRiskAndOpsAsmtCriteriaDetailList, getRisksAndOpportunities, getRiskDetail, getOppDetail, getParDetail, saveRiskAndOpsAsmtCriteria, saveMainConfirm, saveRisksAndOpportunities, deleteRiskAndOpsAsmtCriteriaMain, deleteRiskAndOpsAsmtCriteriaDetail, deleteRisksAndOpportunitesMain, deleteRisksAndOpportunitesDetail, getRisksAndOpportunitiesReport, getRisksAndOpportunitiesCardReport, getAsmtApi } from '@/stores/safewiz/planning/api/risksAndOpportunitiesApi';
import _ from 'lodash';
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
import { useTaskStore } from '@/stores/safewiz/task/task.js';
const loadingPanelStore = useLoadingPanelStore();

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useRisksAndOppStore = defineStore('risksAndOpp', () => {
    const compId = getCompId();
    //전체 메인화면
    const menuList = ref([]);

    //평가기준표 메인화면
    const riskAndOpsAsmtList = ref([]);
    //평가기준표 디테일화면
    const riskAndOpsAsmtDetailList = ref([]);
    const detailData = ref([]);

    //관리대장 메인화면
    const riskOpportunitiesList = ref([]);
    //관리대장 디테일화면
    const riskDetailList = ref([]);
    const oppDetailList = ref([]);
    const parDetailList = ref([]);
    //관리대장 조직
    const orgnItem = ref([]);
    const orgnPopup = ref();

    const inputForm = ref([]);
    const partCompId = ref('');
    //현재날짜
    const currentDate = ref(getCurrentDate());

    const selectedPartners = reactive([]);

    //결제
    const signature = ref(null); // 결재
    const peopleSignature = ref();

    //아코디언 관리 <평가 기준표>
    const asmtSegments = ref([]);
    const filteredRiskAndOpsAsmtList = ref({});
    const confirmedYList = ref({});
    const confirmedNList = ref({});
    const confirmedYn = ref('');
    //아코디언 관리 <관리대장>
    const riskSegments = ref([]);
    const activeSegments = ref({});

    //추가 버튼 Type - R : 우측버튼, C : 카드
    const addBtnType = ref('');

    //loading trigger
    const loadingTrigger = ref(false);

    //watch trigger
    const watchTrigger = ref(false);

    // 데이터 값 변경 여부
    const dataChange = ref(false);

    // 예시 불러오기 수행 여부
    const isSampleAction = ref(false);

    const model = ref({
        orgnId: inputForm.value.orgnId,
        createdAt: getCurrentDate(),
        writeYear: inputForm.value.writeYear,
        model: ''
    });

    //List 초기값
    let riskDetailListState = JSON.parse(JSON.stringify(riskDetailList.value));
    let oppDetailListState = JSON.parse(JSON.stringify(oppDetailList.value));
    let parDetailListState = JSON.parse(JSON.stringify(parDetailList.value));

    //-----------------------------------------------
    //전체 메인페이지

    const searchParamAllMain = ref({
        compId: compId,
        searchText: getCurrentDate().substring(0, 4)
    });

    // 메인 조회 이벤트
    const btnMainSearch = async notify => {
        getRiskMain(searchParamAllMain.value, notify).then(res => {
            menuList.value = res.list;
            console.log('리스크와 기회 메인', menuList.value);
        });
    };

    //평가기준표
    //-----------------------------------------------
    const searchParam = ref({
        compId: compId
    });
    //평가기준표 조회
    const btnSearch = async notify => {
        openLoading();
        await getRiskAndOpsAsmtCriteria(searchParam.value, notify).then(res => {
            endLoading();
            riskAndOpsAsmtList.value = res.list;
            if (Object.keys(riskAndOpsAsmtList.value).length) {
                // 원본 리스트를 깊은 복사
                filteredRiskAndOpsAsmtList.value = _.cloneDeep(riskAndOpsAsmtList.value);
                confirmedYList.value = filteredRiskAndOpsAsmtList.value.filter(item => item.confirmedYn === 'Y');
                confirmedNList.value = filteredRiskAndOpsAsmtList.value.filter(item => item.confirmedYn === 'N');
                initConfirmedList(confirmedYList.value);
                initConfirmedList(confirmedNList.value);
            } else {
                filteredRiskAndOpsAsmtList.value = {};
                confirmedYList.value = [];
                confirmedNList.value = [];
            }
        });
    };

    const initConfirmedList = list => {
        list.forEach(el => {
            el.detail = {
                차수: el.criteriaId,
                등록일자: formatDate(el.createdAt),
                확정일자: el.confirmedDt ? formatDate(el.confirmedDt) : '-'
            };
        });
    };

    const detailParam = ref({});
    //평가기준표 상세 조회
    const btnDetail = async (param, notify) => {
        openLoading();
        await getRiskAndOpsAsmtCriteriaDetailList(param, notify).then(res => {
            endLoading();
            if (res && res.success) {
                // 데이터를 가져와서 복사
                riskAndOpsAsmtDetailList.value = res.list;
                detailData.value = _.cloneDeep(riskAndOpsAsmtDetailList.value);
                console.log('detailData.value', detailData.value);
            }
        });

        isSampleAction.value = false;
    };

    // 평가기준표 상세 이동
    const goAsmtDetail = item => {
        inputForm.value.cmd = 'U';
        detailParam.value = {
            compId: compId,
            criteriaId: item.criteriaId,
            criteriaType: item.criteriaType,
            createdAt: item.createdAt,
            confirmedDt: item.confirmedDt,
            //confirmedYn: item.confirmedYn,
            useYn: item.useYn
        };
        btnDetail(detailParam.value);
        confirmedYn.value = item.confirmedYn;
        console.log('confirmedYn', confirmedYn);
        router.push({
            name: 'RiskAndOpsAsmtCriteriaDetail'
        });
    };

    // 평가기준표 발생가능성 추가
    const pGridAdd = () => {
        const newRow = {
            criteriaType: 'R',
            detailType: 'P',
            detailSeq: '',
            content1: '',
            content2: '',
            content3: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    //리스크
    // 평가기준표 심각성 추가
    const sGridAdd = () => {
        const newRow = {
            criteriaType: 'R',
            detailType: 'S',
            detailSeq: '',
            content1: '',
            content2: '',
            content3: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    // 평가기준표 리스크 등급 추가
    const rlGridAdd = () => {
        const newRow = {
            criteriaType: 'R',
            detailType: 'RL',
            detailSeq: '',
            content1: '',
            content2: '',
            content3: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };

    //기회
    // 평가기준표 관심도 추가
    const cGridAdd = () => {
        const newRow = {
            criteriaType: 'O',
            detailType: 'C',
            detailSeq: '',
            content1: '',
            content2: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    // 평가기준표 중요도 추가
    const iGridAdd = () => {
        const newRow = {
            criteriaType: 'O',
            detailType: 'I',
            detailSeq: '',
            content1: '',
            content2: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    // 평가기준표 판정 기준 추가
    const aGridAdd = () => {
        const newRow = {
            criteriaType: 'O',
            detailType: 'A',
            detailSeq: '',
            content1: '',
            content2: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    // 평가기준표 관리 기준 추가
    const olGridAdd = () => {
        const newRow = {
            criteriaType: 'O',
            detailType: 'OL',
            detailSeq: '',
            content1: '',
            content2: '',
            content3: '',
            content4: '',
            ordSeq: '',
            detailUseYn: 'Y',
            cmd: 'I',
            isChecked: true,
            createdBy: ''
        };
        detailData.value.push(newRow);
    };
    //관리대장
    //-----------------------------------------------

    //관리대장 조회
    const btnSearchRisk = async () => {
        await getRisksAndOpportunities(searchParamAllMain.value, true).then(res => {
            riskOpportunitiesList.value = res.list;
            searchClientGrid(riskOpportunitiesList.value);
        });
    };
    // 아코디언 세팅
    const searchClientGrid = async val => {
        riskSegments.value = [];
        activeSegments.value = {};

        for (let i of val) {
            const matchingYears = riskSegments.value.filter(item => item.year.includes(i.writeYear));

            i.detail = {
                ['등록일자']: formatDate(i.createdAt),
                ['리스크']: i.riskDetailCount,
                ['기회']: i.oppDetailCount,
                ['참여자']: i.parDetailCount
            };

            if (matchingYears.length == 0) {
                riskSegments.value.push({
                    year: i.writeYear + '년도',
                    dataList: [i]
                });
            } else {
                matchingYears[0].dataList.push(i);
            }
        }
        const currentYear = new Date().getFullYear(); // 현재 년도

        // 현재 년도에 해당하는 index를 찾기
        let index = riskSegments.value.findIndex(item => item.year.slice(0, 4) == currentYear);

        // 만약 일치하는 항목이 없으면 index를 0으로 설정
        if (index === -1) {
            const currentYear = String(new Date().getFullYear());
            // 조회 데이터가 없을 시 현재 연도 빈 아코디언 세팅
            riskSegments.value.unshift({ year: currentYear + '년도' });

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

    //관리대장 상세 조회
    const btnDetailRisk = async (param, notify) => {
        try {
            inputForm.value.cmd = 'U'; // 수정
            param['compId'] = compId;
            await getRisksAndOpportunities(param, true).then(res => {
                riskOpportunitiesList.value = res.list;
                searchClientGrid(riskOpportunitiesList.value);
                model.value.orgnId = res.list[0].orgnId;
                model.value.orgnNm = res.list[0].orgnNm;
                model.value.docNo = res.list[0].docNo;
                model.value.createdAt = formatDate(res.list[0].createdAt);
                // model.value.createdAt = inputForm.value.createdAt;
                model.value.useYn = res.list[0].useYn;
                model.value.writeYear = res.list[0].writeYear;
            });
            // riskDetailList.value = [];
            // oppDetailList.value = [];
            // parDetailList.value = [];

            const [riskRes, oppRes, parRes] = await Promise.all([getRiskDetail(param, notify), getOppDetail(param, notify), getParDetail(param, notify)]);
            riskRes.list.forEach(val => {
                if (val.riskMeasureStDt !== null) {
                    val.riskMeasureStDt = getFormattedDate(val.riskMeasureStDt);
                }
                if (val.riskMeasureEdDt !== null) {
                    val.riskMeasureEdDt = getFormattedDate(val.riskMeasureEdDt);
                }
            });

            oppRes.list.forEach(val => {
                if (val.oppMeasureStDt !== null) {
                    val.oppMeasureStDt = getFormattedDate(val.oppMeasureStDt);
                }
                if (val.oppMeasureEdDt !== null) {
                    val.oppMeasureEdDt = getFormattedDate(val.oppMeasureEdDt);
                }
            });

            parRes.list.forEach(val => {
                if (val.riskMeasureStDt !== null) {
                    val.riskMeasureStDt = getFormattedDate(val.riskMeasureStDt);
                }
                if (val.riskMeasureEdDt !== null) {
                    val.riskMeasureEdDt = getFormattedDate(val.riskMeasureEdDt);
                }
            });

            const updateListSafely = (listRef, newData) => {
                if (JSON.stringify(listRef.value) !== JSON.stringify(newData)) {
                    listRef.value = newData;
                }
            };

            // orgnId와 orgnNm을 가져올 수 있는 데이터 탐색 (riskRes -> oppRes -> parRes 순서)
            let orgnId, orgnNm;

            if (riskRes?.success && riskRes.list.length > 0) {
                ({ orgnId, orgnNm } = riskRes.list[0]);
            } else if (oppRes?.success && oppRes.list.length > 0) {
                ({ orgnId, orgnNm } = oppRes.list[0]);
            } else if (parRes?.success && parRes.list.length > 0) {
                ({ orgnId, orgnNm } = parRes.list[0]);
            }

            // 값이 존재하면 orgnItem.value 업데이트
            if (orgnId && orgnNm) {
                orgnItem.value = [{ id: orgnId, name: orgnNm }];
            }
            // model.value.orgnId = risksAndOppStore.inputForm.orgnId;
            //     model.value.createdAt = risksAndOppStore.inputForm.createdAt;
            //     model.value.useYn = risksAndOppStore.inputForm.useYn;
            //     model.value.writeYear = risksAndOppStore.inputForm.writeYear;

            // 기존 데이터와 다를 경우에만 업데이트
            updateListSafely(riskDetailList, riskRes?.success ? riskRes.list : []);
            updateListSafely(oppDetailList, oppRes?.success ? oppRes.list : []);
            updateListSafely(parDetailList, parRes?.success ? parRes.list : []);

            if (peopleSignature.value) {
                await nextTick(() => {
                    peopleSignature.value.forEach((signature, index) => {
                        signature.Search();
                        console.log(`Index ${index}:`, signature);
                    });
                });
            }

            // 필요시 추가 작업 수행
            getAsmtList(); // 조회 후 평가 목록 갱신
        } catch (error) {
            console.error('Error fetching details:', error);
            if (notify) {
                notify('error', '상세 정보를 가져오는 중 오류가 발생했습니다.');
            }
        }
    };

    const asmtList = ref([]);
    const pList = ref([]);
    const sList = ref([]);
    const rlList = ref([]);
    const rcList = ref([]);
    const cList = ref([]);
    const iList = ref([]);
    const aList = ref([]);
    const olList = ref([]);
    const getAsmtList = async () => {
        const responses = await Promise.all([
            getAsmtApi({
                compId: compId // compId 사용
            })
        ]);
        asmtList.value = responses[0].list;

        pList.value = asmtList.value.filter(item => item.detailType === 'P');
        sList.value = asmtList.value.filter(item => item.detailType === 'S');
        rlList.value = asmtList.value.filter(item => item.detailType === 'RL');
        rcList.value = asmtList.value.filter(item => item.detailType === 'RC');
        cList.value = asmtList.value.filter(item => item.detailType === 'C');
        iList.value = asmtList.value.filter(item => item.detailType === 'I');
        aList.value = asmtList.value.filter(item => item.detailType === 'A');
        olList.value = asmtList.value.filter(item => item.detailType === 'OL');
    };

    const initInputForm = async () => {
        inputForm.value = {
            cmd: '',
            docType: 'RAO',
            docNo: '',
            orgnId: '',
            compId: getCompId(),
            writeYear: getCurrentDate().substring(0, 4),
            useYn: '',
            riskDocSeq: '',
            riskOrgn: '',
            riskDesc: '',
            presentPos: '',
            presentSev: '',
            riskMeasureDesc: '',
            riskMeasureStDt: '',
            riskMeasureEdDt: '',
            measurePos: '',
            measureSev: '',
            presentLevel: '',
            measureLevel: '',
            riskUseYn: '',
            oppDocSeq: '',
            oppOrgn: '',
            oppDesc: '',
            oppConcern: '',
            oppImportance: '',
            oppInfluence: '',
            oppLevel: '',
            oppMeasureDesc: '',
            oppMeasureStDt: '',
            oppMeasureEdDt: '',
            measureResultEffect: '',
            oppUseYn: '',
            parDocSeq: '',
            duties: '',
            hrId: '',
            parUseYn: ''
        };
        searchParamRiskDetail.value = {};
        orgnItem.value = [];
    };
    const searchParamRiskDetail = ref({});
    const taskStore = useTaskStore();
    // 관리대장 상세 이동
    const goDetail = async item => {
        await initInputForm();
        searchParamRiskDetail.value = {
            writeYear: item.writeYear,
            docNo: item.docNo,
            docType: item.docType
            // orgnId: item.orgnId,
        };
        // inputForm 바인딩
        for (let i in item) {
            inputForm.value[i] = item[i];
        }
        inputForm.value.cmd = 'U'; // 수정
    };

    const setRefs = orgn => {
        orgnPopup.value = orgn.value;
    };

    const closeOrgn = e => {
        //chips에 넣기위해 id:'', name:'' 으로 세팅
        if (e.length !== 0) {
            orgnItem.value = [];
            inputForm.value.orgnId = e[0].orgnId;
            for (var dt of e) {
                orgnItem.value.push({
                    id: dt.orgnId,
                    name: dt.orgnNm
                });
            }
        }
        orgnPopup.value.onClose();
    };

    // 관리대장 메인 데이터 삭제
    const deleteMainAction = async data => {
        deleteRisksAndOpportunitesMain(data).then(async res => {
            await btnSearchRisk();
        });
    };
    //-----------------------------------------------

    // 검색기능

    const searchTerm = ref('');
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };
    //-----------------------------------------------
    //목록으로 이동
    const goBackAsmt = () => {
        //검색어 초기화
        confirmedYn.value = 'Y';
        detailData.value = [];
        detailParam.value = {};
        isSampleAction.value = false;
        router.push('/RiskAndOpsAsmtCriteria');
    };
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';

        router.push({ path: 'risksAndOpportunitiesMain' });
    };
    // 유효성 검사
    const riskValidationCheck = async data => {
        for (const item of data) {
            if (!item.riskOrgn) {
                toastPopup('저장에 실패하였습니다.', '구분을 입력해주세요.', 'error');
                return false;
            }
            if (!item.riskMeasureStDt) {
                toastPopup('저장에 실패하였습니다.', '조치 예정일을 입력해주세요.', 'error');
                return false;
            }
            if (!item.riskMeasureEdDt) {
                toastPopup('저장에 실패하였습니다.', '조치 완료일을 입력해주세요.', 'error');
                return false;
            }
            return true;
        }
    };
    // 유효성 검사
    const oppValidationCheck = async data => {
        for (const item of data) {
            if (!item.oppOrgn) {
                toastPopup('저장에 실패하였습니다.', '구분을 입력해주세요.', 'error');
                return false;
            }
            if (!item.oppMeasureStDt) {
                toastPopup('저장에 실패하였습니다.', '조치 예정일을 입력해주세요.', 'error');
                return false;
            }
            if (!item.oppMeasureEdDt) {
                toastPopup('저장에 실패하였습니다.', '조치 완료일을 입력해주세요.', 'error');
                return false;
            }
            return true;
        }
    };
    // 유효성 검사
    const parValidationCheck = async data => {
        for (const item of data) {
            if (!item.hrId) {
                toastPopup('저장에 실패하였습니다.', '참여자를 입력해주세요.', 'error');
                return false;
            }
            return true;
        }
    };
    // 관리대장 상세 저장
    const saveDetail = async saveData => {
        openLoading();
        await saveData.forEach(val => {
            if (val.type === 'RISK') {
                if (val.riskMeasureStDt !== null) {
                    val.riskMeasureStDt = formatDateForDB(val.riskMeasureStDt);
                }
                if (val.riskMeasureEdDt !== null) {
                    val.riskMeasureEdDt = formatDateForDB(val.riskMeasureEdDt);
                }
            } else if (val.type === 'OPP') {
                if (val.oppMeasureStDt !== null) {
                    val.oppMeasureStDt = formatDateForDB(val.oppMeasureStDt);
                }
                if (val.oppMeasureEdDt !== null) {
                    val.oppMeasureEdDt = formatDateForDB(val.oppMeasureEdDt);
                }
            }
        });
        await saveRisksAndOpportunities(saveData)
            .then(async res => {
                // parameter 재정의
                searchParamRiskDetail.value.orgnId = res.result[0].orgnId;
                searchParamRiskDetail.value.docType = res.result[0].docType;
                searchParamRiskDetail.value.docNo = res.result[0].docNo;
                searchParamRiskDetail.value.writeYear = res.result[0].writeYear;

                await signature.value.setApprovalInfo(searchParamRiskDetail.value.docType, searchParamRiskDetail.value.writeYear, searchParamRiskDetail.value.docNo);
                await signature.value.updateTaskUseYn(searchParamRiskDetail.value.docType, searchParamRiskDetail.value.writeYear, searchParamRiskDetail.value.docNo);
                const tempPar = _.filter(res.result, { type: 'PAR' }); // 참여자 필터링
                if (tempPar.length === 0) {
                    // 참여자가 선택되어있지 않더라도 useYn 값이 변경되면 TASK에서 변경되어야하기 때문에 강제로 실행함
                    parDetailList.value.forEach(async item => {
                        if(item.parUseYn ==='Y') {
                            await signatureStore.forcedUpdateTaskUseYn(model.value.useYn, 'RAO', item.writeYear, item.docNo, item.parDocSeq);
                        }
                    });
                } else {
                    tempPar.forEach(async item => {
                        // console.log('item.index', item.index)
                        if (typeof peopleSignature.value[item.index].setHrChipsApprovalInfo === 'function') {
                            // console.log('item.index2', item.index)
                            await peopleSignature.value[item.index].setHrChipsApprovalInfo('RAO', res.result[0].writeYear, res.result[0].docNo, item.parDocSeq);
                            if(model.value.useYn !== 'N') await peopleSignature.value[item.index].updateTaskUseYn('RAO', res.result[0].writeYear, res.result[0].docNo, item.parDocSeq);
                        }
                    });
                }
                buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSearch', 'btnDelete', 'btnCopy', 'btnAdd'];
                await btnDetailRisk(searchParamRiskDetail.value);
                endLoading();
            })
            .catch(err => {
                endLoading();
            });
    };
    const deleteDetail = async deleteData => {
        deleteRisksAndOpportunitesDetail(deleteData).then(async res => {
            deleteData.forEach(async item => {
                if(item.type === 'PAR') {
                    await signatureStore.forcedUpdateTaskUseYn('N', 'RAO', item.writeYear, item.docNo, item.parDocSeq);
                }
            });
            await btnDetailRisk(searchParamRiskDetail.value); // 상세 조회
        });
    };
    //평가기준표 상세 저장
    const btnAsmtSave = async saveData => {
        openLoading();
        //여기
        saveRiskAndOpsAsmtCriteria(saveData, true).then(res => {
            endLoading();
            if (res && res.success) {
                detailData.value = [];
                detailParam.value.criteriaId = res.result[0].criteriaId;
                btnDetail(detailParam.value, true);
                //저장후 수정화면으로 전환
                inputForm.value.cmd = 'U';
                detailParam.value = {
                    compId: compId,
                    criteriaId: res.result[0].criteriaId,
                    criteriaType: res.result[0].criteriaType,
                    createdAt: res.result[0].createdAt,
                    confirmedDt: res.result[0].confirmedDt,
                    //confirmedYn: item.confirmedYn,
                    useYn: res.result[0].useYn
                };
                buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSearch', 'btnDelete', 'btnCopy', 'btnAdd'];
            }
        });
    };
    // 메인확정여부 저장
    const btnMainConfirm = async confirmData => {
        openLoading();
        saveMainConfirm(confirmData, true).then(res => {
            endLoading();
            btnSearch();
        });
    };
    // 메인삭제
    const btnDelete = async deleteData => {
        openLoading();
        deleteRiskAndOpsAsmtCriteriaMain(deleteData, true).then(res => {
            endLoading();
            btnSearch();
        });
    };
    // 디테일삭제
    const btnAsmtDelete = async deleteData => {
        openLoading();
        deleteRiskAndOpsAsmtCriteriaDetail(deleteData, true).then(res => {
            endLoading();
            detailData.value = [];
            btnDetail(detailParam.value, true);
        });
    };

    //-----------------------------------------------

    const checkedList = ref([]);

    //-----------------------------------------------

    //-----------------------------------------------

    const chipsItem = ref([]); // Chip 리스트
    const indusItem = ref([]); // Chip 리스트

    // 리포트 출력
    const downloadReport = item => {
        openLoading();
        const param = [];

        for (let i of item) {
            param.push({
                writeYear: searchParamRiskDetail.value.writeYear,
                docType: searchParamRiskDetail.value.docType,
                docNo: searchParamRiskDetail.value.docNo,
                orgnId: orgnItem.value[0].id,
                orgnNm: orgnItem.value[0].name,
                riskDocSeq: i.riskDocSeq,
                oppDocSeq: i.oppDocSeq,
                parDocSeq: i.parDocSeq,
                noCheckPrint: i.noCheckPrint == undefined ? 'N' : i.noCheckPrint,
                type: buttonListStore.downloadType
            });
        }

        // console.log('param', param)

        openLoading();
        getRisksAndOpportunitiesReport(param)
            .then(res => {
                let link = document.createElement('a');
                // 객체를 만들어서 생성

                let objectUrl = window.URL.createObjectURL(res.data);
                link.href = objectUrl;
                link.download = res.headers.filename;
                link.click();
                endLoading();
            })
            .catch(err => {
                endLoading();
                console.log('Error', err);
                // alertMsg('error', '에러', err)
            });
    };

    // 리포트 출력(카드)
    const downloadCardReport = row => {
        const param = [];

        for (let i of row) {
            param.push({
                writeYear: i.writeYear,
                docType: i.docType,
                docNo: i.docNo,
                orgnId: i.orgnId,
                orgnNm: i.orgnNm,
                type: buttonListStore.downloadType
            });
        }

        openLoading();
        getRisksAndOpportunitiesCardReport(param)
            .then(res => {
                let link = document.createElement('a');
                // 객체를 만들어서 생성

                let objectUrl = window.URL.createObjectURL(res.data);
                link.href = objectUrl;
                link.download = res.headers.filename;
                link.click();
                endLoading();
            })
            .catch(err => {
                endLoading();
                console.log('Error', err);
                // alertMsg('error', '에러', err)
            });
    };

    // 리스크 변경값
    watch(
        riskDetailList,
        (newArray, oldArray) => {
            if (loadingTrigger.value || watchTrigger.value) {
                return;
            }

            if (oldArray.length > 0 && newArray.length) {
                dataChange.value = true;

                newArray.forEach((newItem, index) => {
                    const oldItem = riskDetailListState[index];

                    if (oldItem) {
                        if (JSON.stringify(newItem) !== JSON.stringify(oldItem)) {
                            if (newItem.isCheckedRisk == false && oldItem.isCheckedRisk == true) {
                                riskDetailList.value[index].isCheckedRisk = false;
                            } else {
                                riskDetailList.value[index].isCheckedRisk = true;
                            }
                            nextTick();
                        }
                    }
                });
                riskDetailListState = JSON.parse(JSON.stringify(newArray));
            }
        },
        { deep: true }
    );

    // 기회 변경값
    watch(
        oppDetailList,
        (newArray, oldArray) => {
            if (loadingTrigger.value) {
                return;
            }

            if (oldArray.length > 0 && newArray.length) {
                dataChange.value = true;

                newArray.forEach((newItem, index) => {
                    const oldItem = oppDetailListState[index];

                    if (oldItem) {
                        if (JSON.stringify(newItem) !== JSON.stringify(oldItem)) {
                            if (newItem.isCheckedOpp == false && oldItem.isCheckedOpp == true) {
                                oppDetailList.value[index].isCheckedOpp = false;
                            } else {
                                oppDetailList.value[index].isCheckedOpp = true;
                            }
                            nextTick();
                        }
                    }
                });
            }
            oppDetailListState = JSON.parse(JSON.stringify(newArray));
        },
        { deep: true }
    );

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
        toggleAccordion,
        partCompId,
        asmtSegments,
        riskSegments,
        searchParam,
        checkedList,
        selectedPartners,
        searchTerm,
        filteredRiskAndOpsAsmtList,
        confirmedYList,
        confirmedNList,
        detailData,
        currentDate,
        searchParamAllMain,
        menuList,
        riskAndOpsAsmtList,
        riskDetailList,
        oppDetailList,
        parDetailList,
        searchParamRiskDetail,
        downloadCardReport,
        toggleUseYn,
        confirmedYn,
        addBtnType,
        pList,
        sList,
        rlList,
        rcList,
        cList,
        iList,
        aList,
        olList,
        asmtList,
        //라우터
        goBack,
        goBackAsmt,
        compId,
        //버튼
        btnAsmtSave,
        btnAsmtDelete,
        btnDelete,
        goAsmtDetail,
        goDetail,
        btnMainSearch,
        btnMainConfirm,
        deleteDetail,
        //api
        btnSearch,
        btnDetail,
        btnSearchRisk,
        btnDetailRisk,
        deleteMainAction,
        closeOrgn,
        setRefs,
        orgnPopup,
        orgnItem,
        chipsItem,
        indusItem,
        inputForm,
        detailParam,
        signature,
        peopleSignature,
        riskDetailListState,
        oppDetailListState,
        parDetailListState,
        watchTrigger,
        loadingTrigger,
        pGridAdd,
        sGridAdd,
        rlGridAdd,
        saveDetail,
        cGridAdd,
        iGridAdd,
        aGridAdd,
        olGridAdd,
        searchClientGrid,
        riskOpportunitiesList,
        initInputForm,
        downloadReport,
        getAsmtList,
        riskValidationCheck,
        oppValidationCheck,
        parValidationCheck,
        model,
        initConfirmedList,
        isSampleAction
    };
});
