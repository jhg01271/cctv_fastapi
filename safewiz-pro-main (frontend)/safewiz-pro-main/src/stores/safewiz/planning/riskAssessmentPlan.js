import { defineStore } from 'pinia'
import BaseView from '@/components/base/BaseView.js'
const { openLoading, endLoading, ref, reactive, getCompId, getCurrentDate, t, getFormattedDate, alertMsg, formatDateForDB } = BaseView()
import {
    getRiskAssessmentPlanListApi,
    insertRiskAssessmentPlanApi,
    getRiskAssessmentPlanDetailApi,
    updateRiskAssessmentPlanApi,
    deleteRiskAssessmentPlanApi,
    getRiskAvgCount,
    insertRiskAssessmentPrevPlanApi
} from '@/stores/safewiz/planning/api/riskAssessmentPlanApi';
import router from '@/router';
import { useTaskStore } from '@/stores/safewiz/task/task.js';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { useButtonListStore } from '@/stores/buttonList';
import _ from 'lodash';

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useRiskAssessmentPlanStore = defineStore('riskAssessmentPlanStore', () => {

    const signatureComponent = ref(null);
    const layoutStore = useButtonListStore();
    const loadPreviousYn = ref(false);

    // 데이터 리스트
    const riskAssessmentPlanList = ref([]);

    // RiskAssessmentInfoPopup - 선택한 이전 계획
    const riskPlanPrevList = ref([]);

    const getSystemCodeList = async param => {
        //openLoading();
        try {
            const res = await getSystemCode(param);
            return res.list || []; // list만 반환
        } catch (error) {
            console.error('getSystemCode API 호출 오류:', error);
            throw error;
        } finally {
            //endLoading();
        }
    };
    
    /**
     * 시스템 코드
     * major_cd [ C0026 ]
     * minor_cd [ Initial: 최초평가 / regular: 정기평가 / occasional: 수시평가]
     */
    const riskAssessmentGubunList = ref([]);

    /**
     * 시스템 코드
     * major_cd [ C0027 ]
     * minor_cd [ 3b3: 3*3 / 5b4: 5*4 ]
     */
    const riskAssessmentStandardsList = ref([]);

    /**
     * 시스템 코드
     * major_cd [ C0040 ]
     */
    const rawRiskAllowanceStandardsList = ref([]); // 원본 저장용
    const riskAllowanceStandardsList = ref([]);
    const allowanceCodes = ref([]);

    const initRiskAssessmentSystemCode = async () => {
        openLoading();
        riskAssessmentGubunList.value = await getSystemCodeList({ majorCd: 'C0026' });
        riskAssessmentStandardsList.value = (await getSystemCodeList({ majorCd: 'C0027' })).reverse();
    
        // 허용 가능한 위험성 기준 원본 저장
        allowanceCodes.value = await getSystemCodeList({ majorCd: 'C0040' });
        rawRiskAllowanceStandardsList.value = allowanceCodes.value;


        if(inputForm.value.cmd) {
            filterAllowanceList(inputForm.value.riskAllowanceStandards);
        }
        endLoading();
    };
       
    // 허용 가능한 위험성 기준 셀렉트 박스 필터링
    const filterAllowanceList = async (standardCd) => {
        standardCd = standardCd || inputForm.value.riskAssessmentStandards;
        if (standardCd.includes(riskAssessmentStandardsList.value[0].minorCd)) { // 상/중/하
            const baseCd = standardCd.split('_')[0]; // "3a" 추출
            const pattern = new RegExp(`^${baseCd}_`, 'i'); // "3a_"로 시작하는 모든 것

            riskAllowanceStandardsList.value = rawRiskAllowanceStandardsList.value
                .filter(item => pattern.test(item.minorCd))
                .map(item => ({ ...item }));
        }else{
            // standardCd = 5b4_12, pattern에는 5b4가 포함되어있는 데이터 중에서 숫자만 출력할 수 있도록 정규화
            const baseCd = standardCd.split('_')[0]; // '5b4'
            const pattern = new RegExp(`^${baseCd}_([0-9]+)$`, 'i');
            
            const result = [];
            rawRiskAllowanceStandardsList.value.forEach(item => {
                const match = item.minorCd.match(pattern);
                if (match) {
                    result.push({
                        ...item,
                        minorNm: match[1], // 숫자만 추출
                        minorCd: item.minorCd
                    });
                }
            });
        
            riskAllowanceStandardsList.value = result;
        }
    };
    
    
    // 검색 파라미터
    const searchParam = ref({
        // 연도
        searchText : getCurrentDate().slice(0, 4),
        // 검색어
        searchText2 : '',
    });
    const initSearchParam = () => {
        searchParam.value.docNo = '';
        searchParam.value.docType = '';
    }

    // 검색어 필터링
    const searchFilter = (data) => {
        return data.filter(item =>
            item.planNm.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            item.orgnNm.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            riskAssessmentGubunList.value.find(e => e.minorCd === item.riskAssessmentGubun).minorNm
                .toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            item.startDate.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            item.endDate.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            item.assessmentDate.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            item.assessmentComplDate.toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            (item.implPercentage+'%'+' ('+item.reductionCount+'/'+item.implCount+')')
                .toLowerCase().includes(searchParam.value.searchText2.toLowerCase()) ||
            (item.reductionPercentage+'%'+' ('+item.reductionCompletedCount+'/'+item.reductionAllCount+')')
                .toLowerCase().includes(searchParam.value.searchText2.toLowerCase())
        );
    }

    // 목록 조회
    const getRiskAssessmentPlanList = () => {
        openLoading();
        return getRiskAssessmentPlanListApi(searchParam.value, true)
            .then(res => {
                if (res && res.success) {

                    res.list.map(item => {
                        // 결재 상태 넣기
                        let signStatus;
                        switch (item.signStatus) {
                            case 'writer': signStatus = 'W'; break;
                            case 'reviewer': signStatus = 'I'; break;
                            case 'approver': signStatus = 'Y'; break;
                            default:
                                signStatus = 'C';
                        }

                        item.signStatus = signStatus;
                    });

                    // 검색어 필터링
                    riskAssessmentPlanList.value = searchFilter(res.list).map(item => ({
                        ...item,
                        detail : {
                            [t('organizational')]: item.orgnNm,
                            [t('assessment_gubun')]: riskAssessmentGubunList.value.find(e => e.minorCd === item.riskAssessmentGubun).minorNm,
                            [t('riskAssessment_date')]: getFormattedDate(item.startDate) + '~' + getFormattedDate(item.endDate),
                            [t('assessment_date')]: getFormattedDate(item.assessmentDate) || '-',
                            [t('assessment_compl_date')]: getFormattedDate(item.assessmentComplDate) || '-',
                            [t('riskReductionRegisterRate')]: item.implPercentage+'%'+' ('+item.reductionCount+'/'+item.implCount+')',
                            [t('improvementImplementationRate')]: item.reductionPercentage+'%'+' ('+item.reductionCompletedCount+'/'+item.reductionAllCount+')',
                        }
                    }));
                }
            }).finally(() => {
                loadPreviousYn.value = false;
                endLoading()
            });
    }

    // 조직 저장용
    const orgnItem = ref();
    const initOrgnItem = () => {
        orgnItem.value = {};
        inputForm.value.orgnId = '';
    }

    // 위험성평가 조직도 저장용
    const riskAssessmentChartItem = ref();
    const initRiskAssessmentChartItem = () => {
        riskAssessmentChartItem.value = {};
        inputForm.value.riskAssessmentChartId = '';
    }

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            compId : getCompId(),
            writeYear: searchParam.value.searchText,
            orgnId : '',
            planNm : '',
            startDate : '',
            endDate : '',
            assessmentDate : '',
            assessmentComplDate : '',
            riskAssessmentChartId : '',
            riskAssessmentGubun : 'Initial',
            riskAssessmentStandards : '3a',
            riskAllowanceStandards : '',
            desc : '',
            useYn : 'Y',
            cmd : 'I',
            docType: 'RAP',
            createdAt: getCurrentDate(),
        }
        // 저장용 데이터 초기화
        initOrgnItem();
        initRiskAssessmentChartItem();
        // 유해위험요인 감소대책 등록현황 초기화
        riskAvgCount.value = {};
    }

    // inputForm 수정전 데이터 저장용
    const inputFormBefore = ref();

    // 등록 버튼
    const btnAdd = () => {
        loadPreviousYn.value = false;
        //데이터 초기화
        initInputForm();
        // 추가 플래그
        inputForm.value.cmd = 'I';
        router.push({
            path : 'RiskAssessmentPlanDetail'
        });
    }

    // 등록 함수
    const insertRiskAssessmentPlan = async data => {
        openLoading();
        const saveParams = _.cloneDeep(data)
        saveParams.startDate = formatDateForDB(saveParams.startDate)
        saveParams.endDate = formatDateForDB(saveParams.endDate)
        if(saveParams.assessmentDate !== null){
            saveParams.assessmentDate = formatDateForDB(saveParams.assessmentDate)
        }
        if(saveParams.assessmentComplDate !== null){
            saveParams.assessmentComplDate = formatDateForDB(saveParams.assessmentComplDate)
        }
        return insertRiskAssessmentPlanApi(saveParams, true)
            .then(res => {
                if (res && res.success) {
                    // data.signatureComponent.setApprovalInfo(res.result.writeYear + res.result.docNo);
                    signatureComponent.value?.setApprovalInfo("RAP", res.result.writeYear, res.result.docNo);
                    searchParam.value.writeYear = res.result.writeYear;
                    searchParam.value.docNo = res.result.docNo;
                    searchParam.value.docType = "RAP";
                    // 등록/수정 플래그
                    inputForm.value.cmd = 'U';

                    // 버튼 재구성
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
                    // 수정전 데이터를 다시 설정
                    inputFormBefore.value = res.result;
                }
                endLoading();
            }).finally(() => {
                fnSearch();
                endLoading();
            });
    }

    // 전년도 불러오기
    const btnPreYear = () => {
        const prevWriteYearParam = {
            searchText: searchParam.value.searchText - 1, // 전년도
            compId: searchParam.value.compId
        }
        
        openLoading();
        
        return getRiskAssessmentPlanListApi(prevWriteYearParam, true)
            .then(res => {
                if (res && res.success) {

                    if(res.list.length > 0){

                        const filteredList = res.list.filter(item => item.useYn === 'Y');

                        // 데이터 처리
                        const assessmentList = filteredList.map(item => {
                            // 결재 상태 처리
                            let signStatus = 'C';
                            
                            // 새로운 항목들 처리
                            const assessmentItem = {
                                ...item,
                                signStatus,
                                docNo: null,
                                assessmentDate: null,
                                assessmentComplDate: null,
                                cmd: 'I',
                                checked: true,
                                compId: prevWriteYearParam.compId,
                                createdBy: null
                            };

                            // detail 객체 생성
                            return {
                                ...assessmentItem,
                                detail: {
                                    [t('organizational')]: assessmentItem.orgnNm,
                                    [t('assessment_gubun')]: riskAssessmentGubunList.value.find(e => e.minorCd === assessmentItem.riskAssessmentGubun)?.minorNm || '-',
                                    [t('riskAssessment_date')]: getFormattedDate(assessmentItem.startDate) + '~' + getFormattedDate(assessmentItem.endDate),
                                    [t('assessment_date')]: getFormattedDate(assessmentItem.assessmentDate) || '-',
                                    [t('assessment_compl_date')]: getFormattedDate(assessmentItem.assessmentComplDate) || '-',
                                    [t('riskReductionRegisterRate')]: '0%' + ' (' + 0 + '/' + 0 + ')',
                                    [t('improvementImplementationRate')]: '0%' + ' (' + 0 + '/' + 0 + ')',
                                }
                            };
                        });

                        // 검색어 필터링 적용
                        const filteredForDataFilterList = searchFilter(assessmentList);
                        
                        // 기존 리스트에 추가
                        riskAssessmentPlanList.value = [...riskAssessmentPlanList.value, ...filteredForDataFilterList];

                        loadPreviousYn.value = true;
                    }else{
                        alertMsg('warning', t('msgPreviouseNoData'));
                    }
 
                }
            })
            .finally(() => endLoading());
    }

    // 전년도 불러오기 저장
    const btnSavePreviousYear = async (data) => {
        data.forEach(item => item.writeYear = searchParam.value.searchText);
        
        openLoading();
        return insertRiskAssessmentPrevPlanApi(data, true)
            .then(res => {
                if (res && res.success) {
                    initRiskAssessmentSystemCode();
                    getRiskAssessmentPlanList();
                }
                endLoading();
            }).finally(() => {
                loadPreviousYn.value = false;
                endLoading();
            });
    }

    const riskAvgCount = ref({});   // 유해요인, 감소대책, 평균 위험도 정보

    const taskStore = useTaskStore();

    // 조회 함수
    const fnSearch = async () => {

        // select 초기화 
        riskAllowanceStandardsList.value = [];
        inputForm.value.riskAllowanceStandards = '';

        riskAssessmentStandardsList.value = [];
        inputForm.value.riskAssessmentStandards = '';
        
        riskAssessmentGubunList.value = [];
        inputForm.value.riskAssessmentGubun = '';

        // 데이터 조회
        await getRiskAssessmentPlanDetail(searchParam.value, false);
        await initRiskAssessmentSystemCode();
    };

    // 상세조회 함수
    const getRiskAssessmentPlanDetail = (data, notify) => {
    searchParam.value.writeYear = data.writeYear;
    searchParam.value.docNo = data.docNo;
    searchParam.value.docType = data.docType;
    riskAvgCount.value = {};
    openLoading();

    return getRiskAssessmentPlanDetailApi(searchParam.value, notify)
        .then(async(res) => {
            if (res && res.success) {
                inputForm.value = res.result;
                // 등록/수정 플래그
                inputForm.value.cmd = 'U';
                // 조직 세팅
                orgnItem.value = {
                    id: res.result.orgnId,
                    name: res.result.orgnNm,
                    orgnId: res.result.orgnId,
                };
                // 위험성평가 조직 세팅
                riskAssessmentChartItem.value = {
                    id: res.result.riskAssessmentChartId,
                    name: res.result.chartNm,
                    chartData: res.result.chartData,
                    chartId: res.result.riskAssessmentChartId,
                };

                // 유해위험요인, 감소대책 등록 건수, 평균 위험도 조회
                const param = {
                    searchText: res.result.riskAssessmentStandards,
                    docNo: data.docNo,
                    writeYear: res.result.writeYear
                }
                //날짜 포맷
                inputForm.value.startDate = getFormattedDate(inputForm.value.startDate)
                inputForm.value.endDate = getFormattedDate(inputForm.value.endDate)
                if(inputForm.value.assessmentDate !== null){
                    inputForm.value.assessmentDate = getFormattedDate(inputForm.value.assessmentDate)
                }
                if(inputForm.value.assessmentComplDate !== null){
                    inputForm.value.assessmentComplDate = getFormattedDate(inputForm.value.assessmentComplDate)
                }
                // 조회 API 호출
                const riskAvg = await getRiskAvgCount(param, false);
                if (riskAvg.list != null) {
                    riskAvgCount.value = riskAvg.list;
                    let prevScore = getRiskScoreAvg(res.result.riskAssessmentStandards, riskAvg.list.implAvgRisk);
                    let afterScore = getRiskScoreAvg(res.result.riskAssessmentStandards, riskAvg.list.reduAvgRisk);
                    riskAvgCount.value.prevScore = prevScore;
                    riskAvgCount.value.afterScore = afterScore;
                }

                // 수정전 데이터 설정
                inputFormBefore.value = JSON.parse(JSON.stringify(inputForm.value));
            }
        }).finally(() => endLoading());
    }

    // 평균 값 구하기
    const getRiskScoreAvg = (type, riskScore) => {
        // 평가 방식별 임계값과 시스템 코드 매핑
        const rules = {
            '3b3': { thresholds: [4, 6], codes: ['1', '4', '6'] },
            '5b4': { thresholds: [4, 6, 8, 9, 10, 12, 15, 16, 17], codes: ['1','4','6','8','9','10','12','15','16','20'] },
            '3a': { thresholds: [3, 6], codes: ['l', 'm', 'h'] }
        };
        
        const rule = rules[type];

        // 점수가 없거나 평가 방식이 잘못된 경우 return
        if (!riskScore || !rule) return '-';
        
        // 위험성 점수가 속하는 구간의 인덱스 찾기
        const idx = rule.thresholds.findIndex(t => riskScore < t);

        // 임계값 내에 있으면 해당 인덱스의 코드 사용
        const code = rule.codes[idx >= 0 ? idx : rule.codes.length - 1];
        
        // 해당 코드의 정보 조회
        const foundCode = allowanceCodes.value.find(c => c.minorCd === `${type}_${code}`);
        if (!foundCode) return '-';

        const modifiedRiskScore = foundCode.minorNm;
        
        return modifiedRiskScore;
    };

    // 수정 함수
    const updateRiskAssessmentPlan = async data => {
        openLoading();
        const saveParams = _.cloneDeep(data)
        saveParams.startDate = formatDateForDB(saveParams.startDate)
        saveParams.endDate = formatDateForDB(saveParams.endDate)
        if(saveParams.assessmentDate !== null){
            saveParams.assessmentDate = formatDateForDB(saveParams.assessmentDate)
        }
        if(saveParams.assessmentComplDate !== null){
            saveParams.assessmentComplDate = formatDateForDB(saveParams.assessmentComplDate)
        }
        return updateRiskAssessmentPlanApi(saveParams, true)
            .then(async res => {
                if (res && res.success) {
                    // data.signatureComponent.setApprovalInfo(data.writeYear + data.docNo);
                    await signatureComponent.value?.setApprovalInfo("RAP", data.writeYear, data.docNo);
                    await signatureComponent.value?.updateTaskUseYn("RAP", data.writeYear, data.docNo);
                    searchParam.value.writeYear = res.result.writeYear;
                    searchParam.value.docNo = res.result.docNo;
                    searchParam.value.docType = "RAP";
                    // 수정전 데이터를 다시 설정
                    inputFormBefore.value = JSON.parse(JSON.stringify(res.result));
                }
            })
            .finally(() => {
                fnSearch();
                endLoading();
            });
    }

    // 삭제 함수
    const deleteRiskAssessmentPlan = data => {
        openLoading();
        return deleteRiskAssessmentPlanApi(data, true)
            .then(async res => {
                if (res && res.success) {
                    // 상세페이지에서는 목록으로 이동하도록
                    if (router.currentRoute.value.name === 'RiskAssessmentTable') {
                        getRiskAssessmentPlanList();
                    } else {
                        router.push({
                            path: 'RiskAssessmentTable'
                        });
                    }
                    
                    for (let i = 0; i < data.length; i++) {
                        await signatureStore.forcedUpdateTaskUseYn('N','RAP', data[i].writeYear, data[i].docNo);
                    }
                }
            }).finally(() => endLoading());
    }


    return {
        riskAssessmentGubunList,
        riskAssessmentStandardsList,
        riskAssessmentPlanList,
        searchParam,
        getRiskAssessmentPlanList,
        inputForm,
        initInputForm,
        inputFormBefore,
        orgnItem,
        riskAssessmentChartItem,
        btnAdd,
        insertRiskAssessmentPlan,
        getRiskAssessmentPlanDetail,
        updateRiskAssessmentPlan,
        deleteRiskAssessmentPlan,
        initOrgnItem,
        initRiskAssessmentChartItem,
        initSearchParam,
        riskAvgCount,
        initRiskAssessmentSystemCode,
        getSystemCodeList,
        riskAllowanceStandardsList,
        filterAllowanceList,
        fnSearch,
        signatureComponent,
        riskPlanPrevList,
        btnPreYear,
        loadPreviousYn,
        btnSavePreviousYear
    }
});
