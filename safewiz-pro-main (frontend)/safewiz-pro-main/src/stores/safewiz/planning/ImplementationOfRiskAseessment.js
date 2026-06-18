import { defineStore } from 'pinia';
import { gsap } from 'gsap';
import BaseView from '@/components/base/BaseView';
import { getRiskImplList, getRiskAssessmentDetail } from './api/implementationOfRiskAseessmentApi';
const { validationStore, ref, getCompId, getCurrentDate, openLoading, endLoading, alertMsg, nextTick, getFormattedDate } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
// loading panel
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
// calendar year
import { useRiskAStore } from '@/stores/safewiz/planning/riskAssessment.js';
const riskAStore = useRiskAStore();
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
import { selectHazardsList } from './api/ClassificationOfHazardsApi';

export const useRiskAssessmentImplementation = defineStore('RiskAssessmentImplementation', () => {
    const compId = getCompId(); // 사업장 id
    const writeYear = ref(riskAStore.searchParam.searchText || getCurrentDate().substring(0, 4)); // 생성년도
    const cmd = ref(null); // 상태 값 (I: 신규생성, U: 수정)
    let riskPlanList = ref([]); // 위험성평가 계획 리스트
    const selectedPlanData = ref({}); // 선택한 계획 정보
    let implList = ref([]); // 이행 정보
    const prcsWorkId = ref(''); // 선택된 작업 id
    const processId = ref(''); // 선택된 공정 id
    const riskAssessmentStandards = ref(''); // 위험성추정기준(3b3, 5b4)
    const fileInfo = ref([]); // 개선 전 파일 관련
    const fileInfo2 = ref([]); // 개선 후 파일 관련
    const revNo = ref(''); // revNo
    const systemCode = ref([]); // systemCode
    const planPopup = ref(); // 계획 팝업
    const planItem = ref(); // 계획 iChips 정보
    const riskAllowanceStandards = ref(); // 계획 허용 가능한 위험성 기준 정보
    const implRiskAllowanceStandards = ref(); // 이행 허용 가능한 위험성 기준 정보
    const riskAllowance = ref([]); // 이행 허용 가능, 불가능 메시지
    const isRiskAllowance = ref([]); // 허용 가능한 위험인지 판별
    const frequencyList = ref([]);
    const impactList = ref([]);
    const riskScoreList = ref([]);
    const filteredFrequencyList = ref([]);
    const filteredImpactList = ref([]);
    const filteredRiskScoreList = ref([]);
    const factorList = ref([]);

    let errors = {}; // 각 필드의 오류 메시지를 저장하는 객체

    const setSelectedPlanData = () => {
        selectedPlanData.value = ref({
            docNo: null,
            planNm: null,
            classPrcsList: [],
            prcsWorkId: null
        });
    };

    // 위험성 결정 시스템 코드
    const setSystemCodeList = async () => {
        frequencyList.value = await getSystemCodeList({ majorCd: 'C0038' });
        impactList.value = await getSystemCodeList({ majorCd: 'C0039' });
        riskScoreList.value = await getSystemCodeList({ majorCd: 'C0040' });
    };

    // 해당 위험성 수준에 맞는 시스템 코드 필터링
    const filteredSystemCodeList = async riskAssessmentStandards => {
        filteredFrequencyList.value = frequencyList.value.list.filter(item => {
            return item.minorCd.toLowerCase().includes(riskAssessmentStandards);
        });
        filteredImpactList.value = impactList.value.list.filter(item => {
            return item.minorCd.toLowerCase().includes(riskAssessmentStandards);
        });
        filteredRiskScoreList.value = riskScoreList.value.list.filter(item => {
            return item.minorCd.toLowerCase().includes(riskAssessmentStandards);
        });
    };

    // factorList 조회
    const setFactorList = async param => {
        factorList.value = await selectHazardsList(param);
    };

    // 플랜 팝업 세팅
    const setPlanPopup = plan => {
        planPopup.value = plan.value;
    };

    // index, 개선 전 파일, 개선 후 파일, 감소대책
    const setRefs = (index, file, file2, reductionList) => {
        fileInfo.value[index] = file.value;
        fileInfo2.value[index] = file2.value;
        // 감소대책 개수만큼 빈 배열 추가
        fileInfo[index] = Array(reductionList.length).fill([]);
        fileInfo2[index] = Array(reductionList.length).fill([]);
    };

    const getSystemCodeList = async param => {
        openLoading();
        try {
            return await getSystemCode(param);
        } catch (error) {
            console.error('getSystemCode API 호출 오류:', error);
            throw error;
        } finally {
            endLoading();
        }
    };

    // 위험성평가 계획 리스트 조회
    const getPlanData = async docNo => {
        if (docNo == null) {
            planItem.value = [];
        }

        const param = {
            writeYear: writeYear.value || getCurrentDate().substring(0, 4),
            compId: getCompId(),
            docNo: docNo,
            searchText: planItem.value ? planItem.value[0]?.cpemWriteYear : null
        };

        setSelectedPlanData(); // 선택한 계획 정보 초기화
        riskPlanList.value = []; // 위험성평가 계획 리스트 초기화

        implList.value = [];

        // 조회 API
        const res = await getRiskImplList(param, true);
        if (res.list.length === 0 || docNo == null) {
            return;
        }
        await calcPlanData(res, docNo);

        // 버튼 리스트 설정
        buttonListStore.useBtnList = implList.value.list.length === 0 ? ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'] : ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
    };
    const calcPlanData = async (res, docNo, selectedProcessIndex=0, selectedWorkIndex=0) => {
        // 기본 데이터 설정
        
        const planData = res.list[0];
        riskAssessmentStandards.value = planData.riskAssessmentStandards;
        riskPlanList.value = res.list;
        revNo.value = planData?.classPrcsList[0]?.revNo || null;

        // 필터링
        const filteredClassPrcsList = planData.classPrcsList.filter(data => data.processNm);
        // selectedPlanData 설정
        const firstProcess = filteredClassPrcsList[selectedProcessIndex];
        const firstWork = firstProcess?.processList[selectedWorkIndex];

        selectedPlanData.value = {
            planNm: planData.planNm,
            docNo: planData.docNo,
            cpemWriteYear: planData.cpemWriteYear,
            cpemDocNo: planData.cpemDocNo,
            classPrcsList: filteredClassPrcsList,
            prcsWorkId: firstWork?.prcsWorkId,
            prcsWorkNm: firstWork?.workContent,
            processNm: firstProcess?.processNm,
            confirmYn: planData.confirmYn
        };

        // 상세 조회 파라미터 설정
        const detailParam = {
            writeYear: writeYear.value,
            docNo: planData.docNo,
            processId: firstProcess.processId,
            revNo: firstProcess.revNo,
            compId: getCompId(),
            prcsWorkId: parseInt(firstWork.prcsWorkId)
        };

        processId.value = firstProcess.processId;
        prcsWorkId.value = parseInt(firstWork.prcsWorkId);

        // planItem 설정
        planItem.value = [
            {
                id: docNo,
                name: planData.planNm,
                writeYear: planData.writeYear,
                docNo: planData.docNo,
                docType: planData.docType,
                cpemWriteYear: planData.cpemWriteYear,
                cpemDocNo: planData.cpemDocNo,
                cpemConfirmWriteYear: planData.cpemConfirmWriteYear,
                cpemConfirmDocNo: planData.cpemConfirmDocNo,
                compId: planData.compId,
                orgnId: planData.orgnId
            }
        ];

        // 유해위험분류 설정
        await Promise.all([setFactorList(detailParam)]);

        await getRiskAssDetail(detailParam);
    };

    // 선택된 위험성평가 계획에 따른 이행 상세 조회
    const getRiskAssDetail = async (param, notify) => {
        try {
            openLoading();
            const riskAssessmentDetailList = await getRiskAssessmentDetail(param, notify);
            await setFactorList(param);
            const filteredImplList = setImplList(riskAssessmentDetailList);
            implList.value = filteredImplList;

            if (selectedPlanData.value.planNm && selectedPlanData.value.classPrcsList?.[0]?.revNo && !implList.value.list?.length) {
                alertMsg('info', `${selectedPlanData.value.processNm} 공정의 유해위험요인 분류를 먼저 등록하세요.`);
            }

            await nextTick();
        } finally {
            endLoading();
        }
    };

    // 이행 필터링
    const setImplList = riskAssessmentDetailList => {
        // 원본 데이터를 보호하기 위해 깊은 복사 수행
        const implData = JSON.parse(JSON.stringify(riskAssessmentDetailList));
        //감소대책 개선 예정일, 개선 완료일 날짜 포맷팅
        implData.list
            .flatMap(main => main.implRiskAssList || [])
            .flatMap(sub => sub.implementReduList || [])
            .forEach(redu => {
                if (redu.expectedDate !== null) {
                    redu.expectedDate = getFormattedDate(redu.expectedDate);
                }
                if (redu.completedDate !== null) {
                    redu.completedDate = getFormattedDate(redu.completedDate);
                }
            });
        const commonData = {
            writeYear: writeYear.value,
            docType: 'RAP',
            docNo: selectedPlanData.value.docNo,
            prcsWorkId: prcsWorkId.value,
            riskAssessmentStandards: riskAssessmentStandards.value
        };

        // factorList 변환
        const plainFactorList = JSON.parse(JSON.stringify(factorList.value.list));
        const factorMap = new Map();

        plainFactorList.forEach(factor => {
            // factorId별로 요인들을 그룹핑
            if (!factorMap.has(factor.factorId)) {
                factorMap.set(factor.factorId, []);
            }
            factorMap.get(factor.factorId).push(factor);
        });

        implData.list.forEach(data => {
            // 공통 데이터 할당
            Object.assign(data, commonData);

            let reductionCount = 0;
            let reductionCompletedCount = 0;

            data.factorList = factorMap.get(data.hazardsType) || [];

            if (data.implRiskAssList) {
                data.implRiskAssList.forEach(item => {
                    item.writeYear = commonData.writeYear;
                    item.riskAssessmentStandards = commonData.riskAssessmentStandards;

                    // 분류 삭제 여부 판단
                    item.hazardsDelYn = data.factorList.length === 0 || !data.factorList.some(fac => fac.hazardsClassification === item.hazardsClassification);
                    if (item.relatedEvidence) {
                        item.relatedEvidenceItem = [
                            {
                                id: item.legalId,
                                name: item.relatedEvidence,
                                legalNm: item.legalNm
                            }
                        ];
                    }

                    const isItemActive = item.useYn !== 'N';

                    if (item.implementReduList) {
                        item.implementReduList.forEach(impl => {
                            const isImplActive = impl.useYn === 'Y';

                            // 카운트 계산
                            if (isItemActive && isImplActive) {
                                reductionCount++;
                                if (impl.completedDate) reductionCompletedCount++;
                            }

                            impl.riskAssessmentStandards = commonData.riskAssessmentStandards;

                            // HR 정보 최적화
                            if (impl.hrList?.length > 0) {
                                impl.hrList.forEach(hr => {
                                    if (!hr.id) hr.id = hr.hrId;
                                    if (!hr.name) hr.name = hr.hrNm;
                                });
                            }
                        });
                    }
                });
            }

            data.reductionCount = reductionCount;
            data.reductionCompletedCount = reductionCompletedCount;
        });
        return implData;
    };

    // 저장 validation
    const saveValidationFileds = async formId => {
        const isValid = await validationStore.validateAllFields(formId, true);
        if (isValid) {
            return true;
        } else {
            return false;
        }
    };
    const resetImplStore = () => {
        cmd.value = 'I';
        selectedPlanData.value = {
            docNo: null,
            planNm: null,
            classPrcsList: [],
            prcsWorkId: null
        };
        planItem.value = [];
        implList.value = [];
        processId.value = '';
        prcsWorkId.value = '';
        revNo.value = '';
        fileInfo.value = [];
        fileInfo2.value = [];
        riskAllowance.value = [];
        isRiskAllowance.value = [];
        systemCode.value = [];
        implRiskAllowanceStandards.value = '';
    };

    return {
        compId,
        writeYear,
        cmd,
        revNo,
        getPlanData,
        getRiskAssDetail,
        riskPlanList,
        selectedPlanData,
        setSelectedPlanData,
        calcPlanData,
        implList,
        processId,
        prcsWorkId,
        setRefs,
        fileInfo,
        fileInfo2,
        systemCode,
        setPlanPopup,
        planPopup,
        planItem,
        saveValidationFileds,
        riskAllowanceStandards,
        riskAllowance,
        isRiskAllowance,
        implRiskAllowanceStandards,
        resetImplStore,
        frequencyList,
        impactList,
        riskScoreList,
        filteredFrequencyList,
        filteredImpactList,
        filteredRiskScoreList,
        setSystemCodeList,
        filteredSystemCodeList,
        factorList
    };
});
