/**
 * ============================================================================================================================================================
 * 👉 다국어 (한국어) - 커스텀
 * ============================================================================================================================================================
 */
const locale_ko = {
    //region 메시지
    //전년도 불러오기
    msgPreviousConfirm: '전년도 데이터가 저장되지 않았습니다.\n그래도 계속하시겠습니까?',
    msgPreviousError: '전년도 데이터가 저장되지 않았습니다.\n저장 후 진행해주세요.',
    msgPreviousSave: '전년도 불러오기 이후 사용할 수 있습니다.',
    msgPreviousSaveError: '전년도 불러오기 중에는 전년도 데이터만 선택할 수 있습니다.',
    msgPreviouseNoData: '전년도 데이터가 없습니다.',
    msgPreviousLoad: '전년도 데이터를 불러오시겠습니까?',
    //조직도
    msgCopyConfirm: '현재 조직도를 복사하여 신규 작성하시겠습니까?',
    //endregion

    //#region 사용자 정보 변경
    mypage_title: '사용자 정보 변경',
    mypage_signpopup_title: '서명 변경',
    mypage_signature: '서명',
    mypage_changePassword: '비밀번호 변경',
    mypage_newPassword: '새 비밀번호',
    mypagee_confirmNewPassword: '새 비밀번호 확인',
    phoneNumber: '전화번호',
    gender: '성별',
    male: '남',
    female: '여',
    //endregion
    //#region 대시보드
    dashboard_actionPending: '미조치',
    dashboard_actionCompleted: '조치',
    dashboard_nonConformance: '부적합',
    dashboard_notConfigured: '미설정',
    dashboard_compliance: '준수',
    dashboard_high: '높음',
    dashboard_medium: '보통',
    dashboard_low: '낮음',
    dashboard_completed: '완료',
    dashboard_inProgress: '진행',
    dashboard_pending: '대기',
    dashboard_titleCompliance: '컴플라이언스',
    dashboard_legalManageReview: '법규 관리 · 검토',
    dashboard_legalManage: '법규관리',
    dashboard_legalReviewReport: '법규 검토서',
    dashboard_riskAndOpportunity: '리스크와 기회',
    dashboard_risk: '리스크',
    dashboard_opportunity: '기회',
    dashboard_correctiveRequestPrevention: '시정 조치 · 재발 방지',
    dashboard_correctiveRequestPreventionCnt: '시정 조치 · 재발 방지 등록 수',
    dashboard_actionRate: '조치율',
    dashboard_byOrganization: '조직별',
    dashboard_registrationStatus: '등록현황',
    dashboard_continuousMonitoring: '지속적 모니터링',
    dashboard_monitoringAndEvaluation1: '모니터링, 성과측정 및',
    dashboard_monitoringAndEvaluation2: '평가 결과서 항목 수',
    dashboard_taskStatus: 'Task 현황',
    dashboard_dateSelection: '날짜 선택',
    dashboard_taskStatusByPeriod: '기간별 Task 현황',
    dashboard_myTaskStatusByPeriod: '기간별 My Task 현황',
    dashboard_internalAudit: '내부심사',
    dashboard_riskAssessment: '위험성평가',
    dashboard_hazardRegistrationStatus: '유해위험요인 등록현황',
    dashboard_riskReductionRegister: '감소대책 등록 현황',
    dashboard_riskReductionImplementation: '감소대책 이행 현황',
    dashboard_nearMiss: '아차사고',
    dashboard_completionRate: '완료율',
    dashboard_hseObjectives: 'HSE 목표',
    dashboard_safetyAndHealthPolicy: '안전보건경영 방침',
    dashboard_safetyAndHealthObjectives: '안전보건 목표 수',
    dashboard_budget: '예산 (만원)',
    dashboard_performance: '실적 (만원)',
    dashboard_hseObjectivesPerformance: 'HSE 목표 추진 실적 현황',
    dashboard_targetRate: '목표율',
    dashboard_progressRate: '추진율',
    dashboard_attainmentRate: '달성률',
    dashboard_registerRate: '등록률',
    dashboard_transitionRate: '이행률',
    dashboard_progressPerformance: '추진 실적 수',
    dashboard_tbmStatus: 'TBM 현황',
    dashboard_tbmCreated: '작성',
    dashboard_tbmSignatureCompleted: '서명 완료',
    dashboard_educationStatus: '교육 현황',
    dashboard_educationHr: '계획인원',
    dashboard_educationPerformedHr: '수행인원',
    dashboard_annualEducationPlanRegistered: '안전보건 연간 교육 계획서 : 등록',
    dashboard_annualEducationPlanNotRegistered: '안전보건 연간 교육 계획서 : 미등록',
    dashboard_safetyCheckStatus: '안전점검 현황',
    dashboard_equipmentSafetyCheck: '설비 안전점검',
    dashboard_good: '양호',
    dashboard_bad: '불량',
    dashboard_goodRate: '양호율',
    dashboard_equipmentTypeChecklistRegistered: '설비유형별 안전점검표 등록 수 : ',
    dashboard_equipmentTypeChecklistNotRegistered: '설비유형별 안전점검표 등록 수 : 미등록',
    dashboard_riskAssessmentOrganization: '위험성평가 조직도',
    dashboard_notRegistered: '미등록',
    dashboard_safetyAndHealthInfoSurvey: '안전보건정보 조사',
    dashboard_registered: '등록',
    dashboard_processEquipmentMaterial: '공정/설비/물질 구분',
    dashboard_hazardClassification: '유해위험요인 분류',
    dashboard_riskAssessmentPlan: '위험성평가 계획',
    dashboard_riskReductionReg: '감소대책 등록',
    dashboard_riskReductionImpl: '감소대책 이행',
    dashboard_riskAssessmentCompleted: '위험성평가 완료',
    dashboard_hazardsClassification: '유해위험요인별 현황',
    dashboard_hazardsClassificationRegistered: '유해위험요인 등록 수 : ',
    dashboard_hazardsClassificationNotRegistered: '유해위험요인 등록 수 : 미등록',
    dashboard_rejected: '기각',
    dashboard_reviewed: '검토',
    dashboard_accepted: '채택',
    dashboard_lastCheckTime: '마지막 조회시간',
    dashboard_renewalCycle: '갱신주기',
    dashboard_minute: '분',

    //#endregion

    //#region 메뉴 상태 및 툴팁
    menuY: '등록',
    menuN: '미등록',
    menuTootipY: '문서가 등록되었습니다.',
    menuTootipN: '등록된 문서가 없습니다.',
    //#endregion

    //#region 신규 작성 카드 명칭
    card_addOrgn: '신규 조직 추가',
    card_addPartner: '신규 협력사 추가',
    card_addHr: '신규 인원 추가',
    card_addWorkplace: '신규 작업장 추가',
    card_addProcess: '신규 공정 추가',
    card_addEquipment: '신규 설비 추가',
    card_addOrganizationStatus: '신규 조직의 상황 추가',
    card_addWorkerStakeholder: '신규 근로자 및 이해관계자 추가',
    card_addHseJobAssignment: '신규 HSE 업무분장 추가',
    card_addHsePolicy: '신규 안전보건경영방침 추가',
    card_addHseObjectives: '신규 목표 및 중점추진사항 추가',
    card_addDetailedActionPlan: '신규 중점추진사항별 세부계획 추가',
    card_addOhsCommittee: '신규 산업안전보건위원회 추가',
    card_addPartnerCommittee: '신규 협력업체위원회 추가',
    card_addMSDS: '신규 MSDS 추가',
    card_addSafetyAndHealthInfoSurvey: '신규 안전보건정보 추가',
    card_addWorkersOpinionsOnHazard: '신규 유해위험요인 근로자 의견 추가',
    card_addClassificationProcessEquipMsds: '신규 공정/설비/물질 구분 추가',
    card_addResultOfRiskAssessmentTraining: '신규 위험성평가 교육/참여 결과 추가',
    card_addClassificationHazards: '신규 유해위험요인 분류 추가',
    card_addRiskAssessmentPlan: '신규 위험성평가 계획 추가',
    card_addRiskAndOpsAsmtCriteria: '신규 리스크 및 기회 평가 기준 추가',
    card_addRiskOpportunities: '신규 리스크 및 기회 추가',
    card_addRegal: '신규 법규 추가',
    card_addRegalReview: '신규 법규 검토서 추가',
    card_addTbm: '신규 TBM 추가',
    card_addSafetyAndHealthObjectives: '신규 안전보건 목표 추가',
    card_addJobCompetencyAssessment: '신규 직무 적격성 평가 추가',
    card_addSAndHTrainingImplPlan: '신규 안전보건 교육 실시 계획 추가',
    card_addSAndHTrainingResultsPlan: '신규 안전보건 교육 결과 추가',
    card_addQualificationCertificationAssessment: '신규 내부심사원 자격인증 평가 추가',
    card_addSafetyDutiesDesigDocument: '신규 안전업무 지정서 추가',
    card_addSAndHQualificationCertRegister: '신규 안전보건 자격인증 추가',
    card_addSafetyAndHealthInfoRegister: '신규 안전보건정보 추가',
    card_addSafetyAndHealthCommPlan: '신규 안전보건 의사소통 계획 추가',
    card_addDocument: '신규 문서 추가',
    card_addWorkerHealthGuide: '신규 근로자 건강관리 지침 추가',
    card_addSafetyChecklist: '신규 안전점검표 추가',
    card_addPermitToWork: '신규 안전작업 허가서 추가',
    card_addHazMatsInspection: '신규 위험물/유해화학물질 점검 추가',
    card_addHazChemsInOut: '신규 유해화학물질 입출고 추가',
    card_addMgmtOfChange: '신규 변경 추가',
    card_addContractorInvestigation: '신규 계약 대상 업체 조사 추가',
    card_addContractorSAndHAssmt: '신규 협력업체 안전보건 평가 추가',
    card_addEmergencyTask: '신규 비상통제 조직별 업무분장 추가',
    card_addScenario: '신규 시나리오 추가',
    card_addEmergencyResponseTrainingResult: '신규 실시 보고 추가',
    card_addFacilityEquip: '신규 시설/설비 추가',
    card_addEvaluationReport: '신규 결과서 추가',
    card_addCorrectiveRequest: '신규 시정조치 요구 추가',
    card_addOhsInternalAuditExecutionPlan: '신규 안전보건 내부심사 수행계획 추가',
    card_addOhsCorrectiveActionRequest: '신규 안전보건 시정조치 요구서 추가',
    card_addLeaglComplianceEvaluationTable: '신규 법규 준수평가표 추가',
    card_addCorrectiveActionRequest: '신규 시정조치 요구 추가',
    card_addNearMiss: '신규 아차사고 추가',
    card_addIncident: '신규 재해발생 추가',
    card_addPPEMgmt: '신규 안전보호구 지급 및 관리 추가',
    card_addSafetyMgmtOfHazardousMachinery: '신규 위험기계 및 기구 안전관리 추가',
    card_addEmergencyControlOrganChart: '신규 비상통제 조직도 추가',
    card_addOrganChart: '신규 조직 구성도 추가',
    card_safetyAndHealthOrganChart: '신규 안전 보건 조직도 추가',
    card_RiskAssessmentOrganChart: '신규 위험성평가 조직도 추가',
    card_ContractorRegisterRegister: '신규 협력업체 등록대장 추가',
    card_AnnualOhsInternalAuditPlan: '신규 연간 안전보건 내부심사 실시계획서 추가',
    //#endregion

    //#region 고객사 관리
    toList: '목록으로',
    viewDetails: '상세보기',
    clientManage: '고객사 관리',
    addClientManage: '고객사 관리 추가',
    clientManageDetail: '고객사 관리 상세',
    viewDetail: '상세보기',
    clntName: '고객사 명',
    businessRegistrationNumber: '사업자등록번호',
    exponent: '대표자',
    zipCode: '우편번호',
    address: '주소',
    maxHrCount: '최대 접속 인원',
    detailAddress: '상세주소',
    email: '이메일',
    telNo: '전화번호',
    fax: 'FAX번호',
    industry: '업종',
    industryCode: '업종코드',
    use: '사용',
    addressSearch: '주소 검색',
    customerLogo: '고객사 로고',
    compLogo: '사업장 로고',
    createdAt: '등록일자',
    birthDate: '생년월일',
    etc: '기타',
    //#endregion

    //#region 사업장 관리
    client: '고객사',
    compNm: '사업장 명',
    compManage: '사업장 관리',
    addCompManage: '사업장 관리 추가',
    compManageDetail: '사업장 관리 상세',
    constructionClassification: '공사분류',
    constructionName: '공사명',
    useYn: '사용여부',
    array: '정렬',
    //#endregion

    //#region 고객사별 사업장 관리
    clinetComp_clntTitle: '고객사 목록',
    clinetComp_compTitle: '고객사별 사업장 목록',
    clinetComp_compClntId: '고객사',
    clinetComp_clntCompId: '사업장',
    clinetComp_clntNm: '고객사명',
    clinetComp_compNm: '사업장명',
    clinetComp_rpstNm: '대표자',
    clinetComp_telNo: '연락처',
    //#endregion

    //#region 사용자 관리
    user: '사용자',
    userManage: '사용자 관리',
    userManageDetail: '사용자관리 상세',
    addUserManage: '사용자관리 추가',
    id: '아이디',
    password: '비밀번호',
    verifyPassword: '비밀번호 확인',
    userNm: '사용자명',
    authority: '권한',
    note: '비고',
    lastAccessDate: '마지막접속날짜',
    lastAccessOs: '마지막접속 OS',
    //#endregion

    //#region 그룹 관리
    group: '그룹',

    //#region 조직 관리
    orgnNm: '조직명',
    orgnParent: '상위 조직',
    orgnPerson: '조직 인원',
    orgnDesc: '조직 설명',
    personList: '인원 리스트',
    orgn_createdAt: '생성일',
    orgn_resultCnt: '인원수',
    //#endregion

    //#region 협력사 관리
    partnerManage: '협력사 관리',
    partnerManageDetail: '협력사관리 상세',
    addpartnerManage: '협력사관리 추가',
    partnerNm: '협력사명',
    mainBusiness: '주요사업',
    partnerTransactionItem: '거래 품목',
    partnerLogo: '협력사 로고',
    partnerLocation: '협력사 소재지',
    partnerRegDt: '등록일자',
    //#endregion

    //#region 인원 관리
    affCompNm: '소속사업장',
    position: '직위',
    orgn: '조직',
    partner: '협력사',

    //#region  작업장 관리
    workplaceManage: '작업장 관리 ',
    workplaceManageDetail: '작업장 관리 상세',
    addWorkplaceManageDetail: '작업장 관리 추가',
    workplace: '작업장',
    workplaceNm: '작업장명',
    headHrNm: '담당자(정)',
    secondHrNm: '담당자(부)',
    workplaceArea: '구역명',
    workplaceAlias: '별칭',
    workplaceRemark: '작업장 설명',
    //#endregion

    //#region  설비 관리
    equipmentManage: '설비 관리 ',
    equipmentManageDetail: '설비 관리 상세',
    typeofEquipment: '설비 유형',
    typeofEquipmentManage: '설비 유형 관리',
    equipmentNm: '설비명',
    equip_div_orgn: '조직',
    equip_div_workplace: '작업장',
    equip_div_etc: '본사/도급/혼재',
    //#endregion

    //#region  조직과 상황
    orgnStat_createdAt: '작성일',
    orgnStat_createdBy: '작성자',
    orgnStat_cnt: '작성건수',
    //#endregion

    //#region 근로자 및 이해관계자
    empStake_stakeholders: '이해관계자',
    empStake_equirements: '요구사항',
    empStake_obligation: '준수 의무사항',
    empStake_risk: '리스크',
    empStake_chance: '기회',
    empStake_createdAt: '작성일',
    empStake_createdBy: '작성자',
    empStake_cnt: '작성건수',
    empStake_approvalStatus: '결재상태',
    //#endregion

    //#region Hse 목표
    hsePolicy: '안전보건경영방침',
    hsePolicyDesc: '개인사업주 또는 경영책임자등은 개인사업주나 법인 또는 기관이 실질적으로 지배·운영·관리하는 사업 또는 사업장의 특성 및 규모 등을 고려하여 종사자의 안전·보건상 유해 또는 위험을 방지하기 위한 안전·보건에 관한 목표와 경영방침을 설정한다.',
    objectivesAndActionPlan: '목표 및 중점추진사항',
    objectivesAndActionPlanDesc: '안전보건경영 방침을 달성하기 위하여 안전보건 법규 및 조직이 동의한 그 밖의 요구사항, 이해관계자의 요구사항, 최고경영자의 지시사항 등을 고려하여 안전보건 목표 및 세부목표를 설정하고, 그 목표를 달성하기 위한 세부계획을 설정한다.',
    detailedActionPlan: '중점추진사항별 세부계획',
    detailedActionPlanDesc: '안전보건경영 방침을 달성하기 위하여 안전보건 법규 및 조직이 동의한 그 밖의 요구사항, 이해관계자의 요구사항, 최고경영자의 지시사항 등을 고려하여 안전보건 목표 및 세부목표를 설정하고, 그 목표를 달성하기 위한 세부계획을 설정한다.',
    //#endregion

    //#region 목표 및 중점추진사항
    objectives_schedule: '목표일정',
    objectives_budget: '예산',
    objectives_budget_won: '예산(만원)',
    objectives_performance: '성과목표',
    //#endregion

    //region 공지사항
    notice_viewDetails: '상세보기',
    notice_title: '공지사항 목록',
    notice_detail: '공지사항 상세',
    notice_clntId: '고객사 ID',
    notice_writeDt: '작성일자',
    notice_docNo: '문서 번호',
    notice_subject: '제목',
    notice_content: '내용',
    notice_hrId: '작성자 ID',
    notice_createdAt: '생성일자',
    notice_updatedAt: '수정일자',
    notice_dispStDate: '게시 시작일',
    notice_dispEdDate: '게시 종료일',
    notice_useYn: '사용 여부',
    //endregion

    //#region 계획수립 - MSDS 관리
    msds_btnAdd: '신규 MSDS 등록',
    msds_casNo: 'CAS NO.',
    msds_name: '화학물질명',
    msds_name_placeholder: '화학물질명을 입력해주세요.',
    msds_addImage: 'MSDS 사진',
    msds_synonym: '관용명/동의어',
    msds_dailyVolume: '취급량/일',
    msds_dailyTime: '취급시간',
    msds_workplace: '작업장',
    msds_work: '공정/작업내용',
    msds_desc: '화학물질 설명',
    msds_desc_placeholder: '화학물질 설명을 입력해주세요.',
    msds_btnPrev: '이전 MSDS',
    msds_btnNext: '다음 MSDS',
    safety_and_health_act: '산업안전보건법',
    chemical_act: '화학물질관리법',
    dangerous_act: '위험물안전관리법',
    recent_data: '최신 데이터',
    public_data: '공공 데이터',
    update_date: '갱신일',
    msds_unit: '단위',
    //#endRegion

    //#region 계획수립 - 위험성평가 카드
    preRiskAssessment: '위험성평가 사전 준비자료',
    preRiskAssessmentDesc: '1. 각 조직장은 해당사업장 및 부서별 표준작업절차에 안전보건상 위험정보를 파악한다.\n2. 각 조직장은 별첨 안전보건상 위험정보를 작성한다.',
    classificationProcessEquipMsds: '공정/설비/물질 구분',
    classificationProcessEquipMsdsDesc: '1. 각 조직장은 공정을 분석하여 공정, 설비, 물질(MSDS)정보를 파악한다.\n2. 각 조직장은 공정별로 작성하는 것을 원칙으로 한다.',
    ResultOfRiskAssessmentTraining: '위험성평가 교육/참여 결과',
    ResultOfRiskAssessmentTrainingDesc: '1. 각 조직장은 위험성평가를 위한 아래의 내용을 교육 및 관리한다.\n    - 방침과 추진목표\n    - 사전준비 및 유해·위험요인 파악 방법\n    - 유해·위험요인에 대한 위험성 추정 및 결정방법\n    - 감소대책 수립 및 실행의 절차와 기록유지 방법\n    - 위험성 평가 계획 수립의 적정성',
    riskAssessmentTable: '위험성평가표',
    riskAssessmentTableDesc: '1. 각 조직장은 안전보건상 위험정보를 기준으로 위험성평가 점수환산표를 참고하여 작업표준 및 공정별 위험성평가표를 작성한다.\n2. 위험성 평가표는 공정별로 작성하는 것을 원칙으로 한다.',
    improvementAndImplementation: '개선 실행 및 결과',
    improvementAndImplementationDesc: '1. 각 조직은 계획대로 개선 실행한 후 위험성 감소 또는 예방이 되었는지 확인한다.\n2. 추가 조치가 필요한 경우 해당 조직에 추가 감소 대책을 수립한다.',
    //#endregion

    //#region 위험성평가 - 위험성평가 교육/참여 결과
    asmnt_date: '교육/참여 일자',
    asmnt_time: '교육/참여 시간',
    asmnt_place: '교육/참여 장소',
    asmnt_place_placeholder: '교육/참여 장소를 입력하세요.',
    asmnt_content: '교육/참여 내용',
    asmnt_content_placeholder: '교육/참여 내용을 입력하세요.',
    asmnt_result: '교육/참여 실시 결과',
    asmnt_result_reference: '(회의 사진 또는 회의 자료 등)',
    asmnt_attendees: '참석자 명단',
    //#endregion

    //#region 위험성평가사전준비자료 안전보건정보조사
    safehealth_revNo: '작업내용 차수',
    safehealth_revNm: '작업내용 차수명',
    safehealth_workCnt: '작업내용 수',
    safehealth_equipCnt: '기계∙기구/설비 수',
    safehealth_msdsCnt: '화학물질 수',
    safehealth_confirmDt: '확정일자',
    safehealth_writeYear: '작성년도',
    safehealth_notConfigured: '미설정',
    safehealth_writeDt: '작성일자',
    //#endregion

    //#region 위험성평가사전준비자료 유해 위험 근로자의견
    workersOpinion_createdAt: '최근작성일자',
    workersOpinion_cnt: '작성 건수',
    workersOpinion_writeComplete: '작성 완료 건수',
    workersOpinion_reviewComplete: '검토 완료 건수',
    workersOpinion_approval: '결재 상태',
    //#endregion

    //#region Hse 업무분장
    hseAssign_assignTask: '담당업무',
    hseAssign_backupHrNm: '업무대행자',
    hseAssign_writeYear: '작성년도',
    hseAssign_notCompletedTasks: '미완료 Task',
    //#endregion

    //#region 협의 및 참여
    ohsCommittee: '산업안전보건위원회',
    ohsCommitteeDesc: '1. 안전보건경영시스템을 운영하는 데 있어서 모든 계층과 관련 근로자 또는 근로자 대표의 참여 협의를 위한 책임사항 및 요구사항에 대한 관련 활동을 규정',
    partnerCommittee: '협력업체위원회',
    partnerCommitteeDesc: '1. 안전보건경영시스템을 운영하는 데 있어서 모든 계층과 관련 근로자 또는 근로자 대표의 참여 협의를 위한 책임사항 및 요구사항에 대한 관련 활동을 규정',
    //#endregion

    //#region hse  조직도
    safetyAndHealthOrganChart: '안전보건조직도',
    safetyAndHealthOrganChartDesc: '안전보건 관리 체계에서 조직 내 구성원이 어떤 책임과 권한이 있는지 명확히 하여 법적 요구사항을 준수하고, 보고 체계와 의사소통 경로를 명확히 하여 안전보건과 관련된 문제를 효율적으로 해결할 수 있도록 안전보건 조직을 구성한다.',
    //#endregion

    //#region 교육 훈련
    jobCompetencyAssessment: '직무 적격성 평가서',
    jobCompetencyAssessmentDesc: '1.업무 분장표에 따라 설정된 직무 기준에 따라 직무 적격성 평가를 실시한다. \n 2. 직무 적격성 평가 결과, 직무 적격성이 결여된 작업자에 대해서 해당 직무 적격성을 충족할 수 있도록 안전보건 교육훈련 계획에 반영하여 교육을 실시한다.',
    annualSAndHTrainingPlan: '안전보건 연간교육 계획서',
    annualSAndHTrainingPlanDesc: '1. 산업안전보건법 제 29조가 포함된 안전보건 관련 모든 교육을 말한다. 2. 년도별 안전보건 교육훈련 계획을 수립한다.',
    sAndHTrainingImplPlan: '안전보건 교육실시 계획서',
    sAndHTrainingImplPlanDesc: '1. 산업안전보건법 제 29조가 포함된 안전보건 관련 모든 교육을 말한다. 2. 교육 일정과 시간, 강사, 장소, 교육내용 등을 교육인원에게 전달한다.',
    sAndHTrainingResultsPlan: '안전보건 교육결과 보고서',
    sAndHTrainingResultsPlanDesc: '1. 안전보건 교육실시 계획서 일정에 맞춰 교육결과를 등록한다. 2. 교육인원들의 교육 확인 서명을 등록하고 관리한다.',
    educationStatus: '교육 현황',
    educationStatusDesc: '1. 교육 대상자의 교육 이수 시간, 횟수 등 전반적인 교육 내역을 확인한다. \n2. 교육 현황을 모니터링하고, 누락된 교육을 식별하여 보완 계획 수립에 활용한다.',

    //#endregion

    //#region 자격관리
    qualificationCertificationAssessment: '내부심사원 자격인증 평가표',
    qualificationCertificationAssessmentDesc: '내부심사원으로서의 자격을 평가하기 위한 기준 및 평가 내용을 기록한 평가표로, 심사원 역량, 교육 이수 여부, 실무 경험 등을 종합적으로 검토한다.',
    safetyDutiesDesigDocument: '안전업무 지정서',
    safetyDutiesDesigDocumentDesc: '안전 업무 범위 및 책임을 명확히 지정하고 문서화한 자료로, 안전 업무 관련 역할과 권한을 규정하고 안전 업무 담당자 선임,해임 이력을 관리한다.',
    sAndHQualificationCertRegister: '안전보건 자격인증 관리대장',
    sAndHQualificationCertRegisterDesc: '안전보건 관련 자격증, 인증서 보유 현황을 체계적으로 관리하여, 지속적인 적격성 유지를 지원한다.',
    //#endregion

    //#region 의사소통
    safetyAndHealthInfoRegister: '안전보건정보 관리대장',
    safetyAndHealthInfoRegisterDesc: '근로자와 작업장의 안전을 보장하기 위해 필요한 정보를 수집, 기록, 보존하는 데 활용 \n관리대장은 각종 안전활동 기록을 문서화하여 근로자의 안전의식을 높이고 사업장에서 발생할 수 있는 재해와 사고를 예방',
    mySafetyAndHealthInfoRegister: 'My 안전보건정보 관리대장',
    mySafetyAndHealthInfoRegisterDesc: '근로자와 작업장의 안전을 보장하기 위해 필요한 정보를 수집, 기록, 보존하는 데 활용 \n관리대장은 각종 안전활동 기록을 문서화하여 근로자의 안전의식을 높이고 사업장에서 발생할 수 있는 재해와 사고를 예방',
    safetyAndHealthCommPlan: '안전보건 의사소통 계획',
    safetyAndHealthCommPlanDesc: '사업장의 근로자들과 안전보건 정보를 원활하게 소통하여 재해를 예방하고, 재해 발생 시 신속한 대응을 가능하게 하는 체계 구축',
    //#endregion

    //#region 법규관리
    legalManage: '법규 관리',
    legalManageDesc: '안전보건경영시스템 운영에 직접 적용되는 관련법규, 규약, 기타요건(이하 "법규"라 한다)의 입수, 등록, 배포, 교육 및 준수평가를 위해 법규를 관리한다.',
    legalReview: '법규 검토서',
    legalReviewDesc: '위험요인, 안전보건 관련 리스크 및 안전보건 시스템에 적용할 수 있는 최신의 법적 요구사항 및 기타 요구사항에 대해서 공사 적용 여부를 파악하여 표준 및 지침류를 제/개정하여 작성하여야 한다.',

    legalNm: '법규명',
    revisionAt: '제정/개정 일자',
    articleTitle: '법규 조항',

    relatedLaws: '관련 법규',
    effectiveDate: '제정/개정 일자',
    //#endregion

    //#region Hse 목표
    riskAsmt: '리스크 및 기회 평가 기준표',
    riskAsmtDesc: '1. 발생가능성, 심각성 및 등급을 기준으로 리스크 분석 및 평가에 활용할 리스크 평가 기준표를 작성한다.\n2. 기회에 대한 관심도, 중요도 및 영향도를 기준으로 기회 분석 및 평가에 활용할 기회 평가 기준표를 작성한다.',
    riskOpp: '리스크와 기회 관리대장',
    riskOppDesc: '1. 각 조직은 조직의 상황, 근로자 및 이해관계자에서 파악, 식별된 리스크와 기회에 대해 리스크 및 기회 평가 기준표에 따라 분석 및 평가한다.\n2. 각 조직은 리스크 및 기회 평가 결과에 따라 조치 계획을 수립한다.',
    //#endregion

    //#region 지속적 개섬
    provisionAndMgmtPPE: '안전보호구 지급 및 관리',
    provisionAndMgmtPPEDesc: '재해나 건강장해를 방지하기 위한 목적으로 작업자가 착용하여 작업을 하는 기구나 장치\n' +
        '안전보호구는 산업안전보건법 제84조에서 규정하는 안전인증에 합격된 보호구만 사용하여야 한다.',
    //#endregion

    //#region 문서관리
    doc_seq: '문서번호',
    doc_writeDt: '작성일자',
    doc_writer: '작성자',
    doc_cnt: '등록건수',
    //#endregion

    //#region 실행(운영)
    emergencyControlOrganChart: '비상통제 조직도',
    emergencyControlOrganChartDesc: '조직이 위기 상황(화재, 자연재해, 산업 재해, 안전사고 등)에 신속하고 효과적으로 대응하기 위해 미리 구성된 조직 체계와 역할 분담을 시각적으로 나타낸 도식',
    emergencyControlTaskAsgmt: '비상통제 조직별 업무분장',
    emergencyControlTaskAsgmtDesc: '조직이 비상 상황 발생 시 효율적으로 대응할 수 있도록 각 부서와 개인이 수행해야 할 구체적인 업무를 정리한 것',
    emergencyResponseTraining: '비상대응 훈련',
    emergencyResponseTrainingDesc: '실제 상황에서 발생할 수 있는 다양한 위험을 미리 대비하고, 대응 능력을 점검하고 향상시키기 위해 정기적으로 실시',
    facilityEquipStatusTable: '시설/설비별 특징 현황표 ',
    facilityEquipStatusTableDesc: '시설/설비별 특징 현황표 ',
    //#endregion

    //#region 비상대응훈련
    ert_newScenario: '신규 시나리오 추가',
    ert_orgnNm: '조직',
    ert_trainingNm: '비상사태명',
    ert_trainingLocation: '장소',
    ert_useYn: '사용여부',
    ert_trainingContent: '발생내용',
    ert_anticipatedDamage: '예상되는 피해 (안전보건경영 영향)',
    ert_situation: '상황(경과)',
    ert_targetTime: '경과시간(분)',
    ert_performer: '임무수행',
    ert_methodAction: '행동요령',
    ert_remark: '비고',
    ert_uploadFile: '관련 자료',

    ert_newResult: '신규 실시 보고서 추가',
    ert_trainingCnt: '훈련 횟수',
    ert_new: '신규',
    ert_scenario: '시나리오',
    ert_actionNm: '훈련명',
    ert_actionDt: '훈련일자',
    ert_actionLocation: '훈련장소',
    ert_actionContent: '훈련내용',
    ert_actionResult: '훈련결과',
    ert_goalTargetTime: '목표경과시간(분)',
    ert_minute: '분',
    ert_measurementTime: '측정시간',
    ert_observationActualBehavior: '실제 행동 관찰',

    ert_tooltipscenario: '결재 승인 완료된 비상대응 훈련 시나리오만 표시됩니다.',
    //#endregion

    //#region 위험성평가 > 공정/설비/물질 구분
    evaluationPeriod: '평가 진행 기간',
    processOverview: '공정개요',
    participants: '참여자',
    processCount: '공정 개수',
    classProcConfirmDt: '확정일자',
    classProcWriteYear: '작성년도',
    ClassificationProcessEquipMsds_msgCopyConfirm: '현재 공정/설비/물질 구분을\n복사하여 신규 작성하시겠습니까?',
    classProcWritemDt: '작성일자',
    classProcInfoMsg: '[기준 정보 > 공정 관리]\n1. 선택한 조직으로 설정된 공정이 있는지 확인하세요.\n\n[안전보건정보 조사]\n1. 선택한 조직의 공정 정보를 작성했는지 확인하세요.\n2. 확정차수인지 확인하세요.',
    //#endregion

    //#region Hse 목표
    workerHealthMgmtGuidelines: '근로자 건강관리 지침',
    workerHealthMgmtGuidelinesDesc: '근로자의 건강 점검 및 직업병을 예방함으로써 육체적, 정신적, 심리적 건강을 유지‧증진 시킨다.',
    //#endregion

    //#region Hse 실적
    hseKeyPerformanceResults: 'HSE 중점 추진실적',
    hseKeyPerformanceResultsDesc: '1. 안전보건경영시스템을 운영하는 데 있어서 모든 계층과 관련 근로자 또는 근로자 대표의 참여 협의를 위한 책임사항 및 요구사항에 대한 관련 활동을 규정',
    hseKeyPerformanceResults_requiredPerformance: '추진 실적은 필수입니다.',
    hseKeyPerformanceResults_enactedDt: '제정일',
    hseKeyPerformanceResults_revisedDt: '개정일',
    hseKeyPerformanceResults_docCount: '등록 건수',
    hseKeyPerformanceResults_resCount: '실적 건수',
    //#endregion

    //#region 근로자 건강관리 지침
    whmg_name: '성명',
    whmg_group: '조직',
    whmg_healthDiary: '건강 상담일지',
    whmg_improvementConsultationLog: '개선요청 상담일지',
    //#endregion

    //#region 시정조치 요구서
    corrective_docNo: '문서발행번호',
    corrective_title: '제목',
    corrective_useAt: '사용여부',
    corrective_hostOrg: '주관조직',
    corrective_actionOrg: '조치조직',
    corrective_regDt: '작성일',
    corrective_returnDt: '회신요구일',
    corrective_check: '부적합 사항(시정조치 요구사항)',
    corrective_analyticAndPrevent: '원인 분석 및 재발 방지 대책',
    corrective_validate_check: '시정조치 결과 유효성 확인',
    corrective_checkDt: '확인일',
    //#endregion

    //#region 안전관리 지침
    safetyMgmtGuidelines: '안전관리 지침(점검일지 등)',
    safetyMgmtGuidelinesDesc: '근로자의 업무 수행 중 발생하는 재해를 미연에 방지하기 위한 작업 조건과 환경개선 및 안전 방호대책을 강구하며, 종업원의 안전을 보장하고 업무 능률을 향상시켜 공사의 재산을 보호하며 재해 발생에 대한 책임 한계를 명확히 한다.',
    safety_itemCnt: '점검항목수',
    safety_detailCnt: '점검사항수',
    work_jobType: '작업종류',
    work_jobTime: '작업시간',
    work_jobPlace: '작업장소',
    work_jobEquipment: '작업설비',
    work_approvalStatus: '결재상태',
    //#endregion

    //#region 위험성평가 - 위험성평가표 - 유해위험요인분류
    hazardsClassification_cnt: '유해위험요인 수 ',
    hazardsClassification_revNo: '작업내용 차수',
    hazardsClassification_revNm: '작업내용 차수명',
    hazardsClassification_writeDt: '작성일자',
    //#endregion

    //#region 위험성평가 - 위험성평가표 - 위험성평가 계획
    riskAssessmentPlan_name: '위험성평가 계획명',
    riskAssessmentPlan_title: '위험성평가 계획',
    riskAssessment_date: '위험성평가 기간',
    assessment_date: '평가일자',
    assessment_compl_date: '완료일자',
    riskAssessment_orgnChart: '위험성평가 조직도',
    riskAssessment_gubun: '위험성평가 구분',
    riskAssessment_standards: '위험성 추정기준',
    organizational: '조직',
    desc: '설명',
    assessment_gubun: '평가 구분',
    riskReductionRegisterRate: '감소대책 등록률',
    improvementImplementationRate: '감소대책 이행률',
    riskAssessmentPlan_writeYear: '작성년도',

    //#endregion

    //#region 계획수립 - 안전보건목표
    revisedDt: '개정일',
    enactedDt: '제정일',
    docCount: '등록 건수',
    safetyHealthGoal: '안전보건목표',
    selectPropulsionPlan: '추진 계획 선택',
    detailGoalMethod: '안전보건 세부목표 및 달성방법',
    actionSchedule: '추진일정',
    hrNm: '담당자',
    requiredResource: '필요 자원',
    evaluationMethod: '평가 방법',
    remark: '비고',
    //#endregion

    //#region 작업장 안전기준 지침
    workplaceSafetyGuidelines: '작업장 안전기준 지침',
    workplaceSafetyGuidelinesDesc:
        '업무 활동 및 기타 제반 활동에 필요한 위험물 및 유해화학물질의 직·간접적인 위험요인 파악, 위험성 및 유해성 평가, 관리수단 결정 및 등록 관리에 관한 절차를 규정함으로써 안전사고를 예방하고 안전하고 쾌적한 작업환경을 유지한다. ',
    workplace_inspectionCnt: '점검건수',
    workplace_msdsList: '물질',
    //#endregion

    //#region 변경관리
    mgmtOfChange: '변경관리',
    mgmtOfChangeDesc: '안전보건경영시스템 운영에 직접 적용되는 관련 법규, 규약, 기타요건(이하 "법규"라 한다)등 안전보건성과에 영향을 주는 계획된 임시변경 및 영구적인 변경 실행과 관리 및 의도하지 않은 변경으로 인한 리스크를 줄이기 위한 변경 실행 및 관리를 위한  프로세스를 규정',
    //#endregion

    //#region 조달-->도급으로 변경 (2025.06.20)
    procurement: '도급',
    procurementDesc: '도급은 도급 프로세스에서 발생할 수 있는 안전 및 보건상의 위험을 사전에 평가하여, 협력업체(도급 대상업체)와의 계약 체결 과정에서 안전하고 적합한 업체를 선정하기 위한 활동.\n이는 협력업체의 안전관리 역량, 작업 환경의 안전성, 법적 요건 준수 여부를 확인하고, 도급 프로세스에서 발생할 수 있는 잠재적 위험을 최소화하기 위해 수행.',
    //#endregion

    //#region 계약 대상업체 조사표
    cif_new: '신규 조사표 추가',
    cif_enterDirectly: '직접 입력',
    cif_partCompNm: '업체명',
    cif_createdAt: '등록일자',
    cif_useYn: '사용여부',
    cif_partLocation: '협력사 소재지',
    cif_desc: '주요사업',
    cif_partCompItem: '공사와의 거래품목 (서비스내용)',
    cif_zipCd: '우편번호',
    cif_msg_zipCd: '우편번호 검색',
    cif_addrs1: '주소',
    cif_addrs2: '상세주소',
    cif_examDt: '심사일자',
    cif_investigator1: '조사자1',
    cif_investigator2: '조사자2',
    cif_msg_emergency_manage: '*점검사항을 먼저 등록하세요.',
    cif_emergency_manage: '점검사항 관리',
    cif_typeNm: '구분',
    cif_index: '번호',
    cif_detailContent: '점검사항',
    cif_allotment: '배점',
    cif_comment: '의견',
    cif_score: '득점',
    cif_upper: '상',
    cif_middle: '중',
    cif_lower: '하',
    cif_totalAllotment: '배점 합계',
    cif_totalScore: '득점 합계',
    cif_partComp: '업체',
    cif_item: '거래품목',
    //#endregion

    //#region TBM
    tbm_date: 'TBM 일자',
    tbm_time: 'TBM 시간',
    tbm_sameWorkDate: '작업날짜 동일 여부',
    tbm_rgstDate: '등록일자',
    tbm_whetherToUse: '사용여부',
    tbm_workNm: '작업명',
    tbm_workDetail: '작업내용',
    tbm_workPlace: 'TBM 장소',
    tbm_riskAssessmentConducted: '위험성평가 실시 여부',
    tbm_measures: '대책',
    tbm_keyRiskFactors: '중점위험요인',
    tbm_selected: '선정',
    tbm_checkTbmLeader: 'TBM 리더 확인',
    tbm_checkSafetyMeasuresBeforeWork: '작업 전 안전조치 확인',
    tbm_potentialRiskFactors: '잠재위험요소',
    tbm_actionTaken: '조치여부',
    tbm_actionTakenIfNo: "'아니오'인 경우 조치내용",
    tbm_safetyInspectionBeforeWork: '작업 전 일일 안전점검 시행 결과',
    tbm_closingMeetingAfterWork: '작업 후 종료 미팅(중점대책의 실효성)',
    tbm_confirmAttendees: '참석자 확인',
    tbm_msgTimeRange: '시간의 범위가 올바르지 않습니다.',
    tbm_msgNewData: '신규데이터입니다.',
    tbm_msgEnterTbmDate: 'TBM 일자를 입력해주세요.',
    tbm_msgEnterTbmTime: 'TBM 시간을 입력해주세요.',
    tbm_msgEnterTaskName: '작업명을 입력해주세요.',
    tbm_msgOverlappingPotentialRiskFactor: '중복되는 잠재위험요소이 있습니다.',
    tbm_msgNoStorePotentialRiskFactor: '저장된 잠재위험요소이 없습니다.',
    tbm_msgNoRiskImpl: '등록된 위험성평가 이행 정보가 없습니다.',
    tbm_leader: 'TBM 리더',
    tbm_attend: '참석자 인원',
    tbm_01Month: '01월',
    tbm_02Month: '02월',
    tbm_03Month: '03월',
    tbm_04Month: '04월',
    tbm_05Month: '05월',
    tbm_06Month: '06월',
    tbm_07Month: '07월',
    tbm_08Month: '08월',
    tbm_09Month: '09월',
    tbm_10Month: '10월',
    tbm_11Month: '11월',
    tbm_12Month: '12월',
    tbm_tooltipMeasures: '제거 → 대체 → 통제 순서 고려',
    tbm_tooltipKeyRiskFactors: '잠재위험요소 1 ~ 3 중 중요위험 1개를 선정하여 기재함',
    tbm_tooltipSafetyInspectionBeforeWork: '위험요인 중 조치가 되지 않은 사항, 작업자의 TBM내용 숙지 여부 중점체크',
    tbm_tooltipConfirmAttendees: 'TBM에 참여하지 않은 작업자를 확인하여 미팅 참석 유도',
    tbm_msgOverlappingKeyRiskFactor: '중복되는 중점위험요인이 있습니다.',
    tbm_msgAlreadyAttend: '참석인원에 이미 존재하는 인원입니다.',
    tbm_msgAlreadyLeader: '리더인원에 이미 존재하는 인원입니다.',
    tbm_msgDplLeaderAttend: '리더와 참석자가 중복되는 인원이 있습니다.',
    //#endregion

    //#region 위험성평가 사전 준비 자료 - 이상걸
    workersOpinionsOnHazards_workplace: '작업장',
    workersOpinionsOnHazards_regDt: '작성일자',
    workersOpinionsOnHazards_useYn: '사용여부',
    workersOpinionsOnHazards_experience: '경험담',
    workersOpinionsOnHazards_sixPrinciple: '육하원칙(누가, 언제, 어디서, 무엇을, 어떻게, 왜)에 따라 작성',
    workersOpinionsOnHazards_writeExperience: '경험담을 입력하세요',
    workersOpinionsOnHazards_opinion: '의견',
    workersOpinionsOnHazards_workerOpinion: '근로자 의견',
    workersOpinionsOnHazards_reviewerOpinion: '검토자 의견',
    workersOpinionsOnHazards_workerGuide: '● 유해∙위험 경험의 원인과 개선해야 할 점',
    workersOpinionsOnHazards_reviewerGuide: '● 경험에 대한 조언',
    workersOpinionsOnHazards_writeOpinion: '의견을 입력하세요',
    workersOpinionsOnHazards_newRegister: '유해 위험 요인 근로자 의견 추가',
    //#endregion

    //#region 시설 설비 관리 - 이상걸
    facilityEquip_facilitisNm: '시설/설비명',
    facilityEquip_regDt: '등록일자',
    facilityEquip_ordSeq: '정렬',
    facilityEquip_useYn: '사용여부',
    facilityEquip_facilitisDesc: '시설/설비 설명',
    facilityEquip_emergencyInfo: '비상대피안내도',
    facilityEquip_register: '신규 설비 등록',
    //#endregion

    //#region 시설 설비 관리 - 이상걸
    safetyAndHealthInfoSurvey_process: '공정',
    safetyAndHealthInfoSurvey_revNo: '작업내용 차수',
    safetyAndHealthInfoSurvey_revNm: '작업내용 차수명',
    safetyAndHealthInfoSurvey_regDt: '등록일자',
    safetyAndHealthInfoSurvey_useYn: '사용여부',
    safetyAndHealthInfoSurvey_material: '원재료',
    safetyAndHealthInfoSurvey_procuct: '생산품',
    safetyAndHealthInfoSurvey_workerCnt: '근로자수',
    safetyAndHealthInfoSurvey_getData: '기준정보 반영',
    safetyAndHealthInfoSurvey_newRegister: '안전보건정보 조사 추가',
    safetyAndHealthInfoSurvey_msgCopyConfirm: '현재 안전 보건 정보 조사를\n복사하여 신규 작성하시겠습니까?',
    //#endregion

    //#region 아차사고 보고서
    nearMissReport: '아차사고 보고서',
    nearMissReportDesc: '잠재되어 있는 사고 요인을 작업자가 사전에 발견했거나 사고 조건이 부합되지 않아 실제 재해로 연결되지 않는 사고를 말한다.',
    //#endregion

    //#region 재해발생 보고서
    incidentReport: '재해발생 보고서',
    incidentReportDesc: '1. 각 부서장은 사건·사고 및 부적합사항 발생시 즉시 이를 안전보건주관부서장에게 보고한다(서면 기록 유지)\n2.안전보건 주관부서장은 임시조치를 취하고 이를 대표이사에게 보고한다.',
    //#endregion

    //#region 시정조치 요구서
    correctiveActionRequest: '시정조치 요구서',
    correctiveActionRequestDesc: '1. 해당부서장은 부적합 보고서에 시정조치 대책을 수립하고 필요한 경우 지속적인 안전확보를 할 수 있도록 리스크와 기회 및 위험성평가를 실시한다. \n2. 안전보건 부서는 수립된 시정조치 대책을 검토하여 적절성을 검토하고 유효성을 확인한다. \n3. 해당근로자에게 적절한 교육 및 훈련을 실시한다.',
    //#endregion

    //#region 모니터링 및 측정 관리
    evaluationReport: '모니터링, 성과측정 및 평가 결과서',
    evaluationReportDesc: '안전보건 주관부서장은 다음 사항에 대한 적합성 여부를 모니터링, 성과측정하고 그 결과를 분석한다.\n' +
        '  1. 설정된 안전보건목표 및 세부목표의 달성 실적  \n' +
        '  2. 안전보건경영추진계획의 이행상태\n' +
        '  3. 관련 안전보건법규와 그 밖의 요구사항의 준수 및 이행상태\n' +
        '  4. 심각한 안전보건측면과 안전보건요인에 관련된 운영절차의 이행상태',
    complianceEvaluationTable: '법규 준수평가표',
    complianceEvaluationTableDesc: '모니터링, 성과측정 및 평가 결과서의 항목사항별로 준수사항을 점검 내용을 구성하고 평가한다.',
    performanceEvaluationTable: '성과평가표',
    performanceEvaluationTableDesc: '모니터링, 성과측정 및 평가 결과서의 평가항목별로 성과의 평가 기준과 방법을 구성하고 평가한다.',
    evaluationCorrectiveActionRequest: '시정조치 요구서',
    evaluationCorrectiveActionRequestDesc: '모니터링, 성과측정 및 평가 결과서의 부적합 항목에 대해서 시정하여 재발 방지 대책을 수립한다.',
    //#endregion

    //#region 개선 - 지속적개선 - 위험기계 및 기구 안전관리
    safetyMgmtOfHazardousMachinery: '위험기계 및 기구 안전관리',
    safetyMgmtOfHazardousMachineryDesc: '작업 도중 유해 또는 위험한 상황이 발생할 우려가 있거나 동력에 의하여 작동하는 기계 기구',
    equipment: '설비',
    mgmtNum: '관리번호',
    mfSpec: '규격 및 용량',
    inspectionCycle: '점검주기',
    inspectionExpiryDate: '검사 유효기간',
    inspectionDate: '검사일',
    nextInspectionDate: '차기 검사일',

    //#region 개선 - 지속적개선 - 위험기계 및 기구 안전관리 - 자체검사원 등록부 (팝업)
    voluntarySafetyInspector_popupTopTitle: '자체검사원 등록부 (팝업)',
    voluntarySafetyInspector_popupTitle: '자체검사원 등록부',
    voluntarySafetyInspector_search: '조회',
    voluntarySafetyInspector_name: '성명',
    voluntarySafetyInspector_workingAt: '입사일',
    voluntarySafetyInspector_hasCertifiCate: '관련 자격증 보유 여부',
    voluntarySafetyInspector_order: '순번',
    voluntarySafetyInspector_print: '출력',
    voluntarySafetyInspector_duplicateMessage: '중복된 인원이 존재합니다.',
    //#endregion

    //#region 내부 심사
    annualOhsInternalAuditPlan: '연간 안전보건 내부심사 실시계획서',
    annualOhsInternalAuditPlanDesc: '조직 내 안전보건 활동의 효과성과 적정성을 점검하고 개선하기 위한 연간 계획',
    ohsInternalAuditExecutionPlan: '안전보건 내부심사 수행계획서',
    ohsInternalAuditExecutionPlanDesc: '조직 내 안전보건 관리체계의 적정성과 이행 상태를 점검하고 개선하기 위한 내부 심사의 실행 계획을 담은 문서',
    ohsInternalAuditResultsReport: '안전보건 내부심사 결과보고서',
    ohsInternalAuditResultsReportDesc: '내부 심사를 통해 확인된 안전보건 관리체계의 성과와 개선점을 정리한 문서',
    ohsCorrectiveActionRequest: '안전보건 시정조치 요구서',
    ohsCorrectiveActionRequestDesc: '안전보건 시정조치 요구서 메뉴는 작업장에서 발생할 수 있는 안전 및 보건 관련 문제를 개선하기 위해 필요한 조치를 명시하고 이를 문서화한 메뉴입니다.',
    //#endregion

    //#region 버튼 컴포넌트
    btnBack: '목록',
    btnSample: '예시보기',
    btnSearch: '조회',
    btnAdd: '추가',
    btnSave: '저장',
    btnClose: '닫기',
    btnDelete: '삭제',
    btnEdit: '편집',
    btnDownload: '출력',
    btnNew: '신규',
    btnPreYear: '전년도',
    btnCall: '불러오기',
    btnSuggestionSearch: '의견보기',
    btnSuggestionAdd: '의견작성',
    btnExcelForm: '엑셀양식',
    download: '다운로드',
    btnExcel: '엑셀',
    upload: '업로드',
    btnCopy: '복사',
    btnWorker: '근로자',
    check: '확인',
    //#endregion

    //#region 개선 및 실행
    imprvAndImplmntt_process: '공정',
    imprvAndImplmntt_workDetail: '작업내용',
    imprvAndImplmntt_classification: '분류',
    imprvAndImplmntt_numberOfImprovementActions: '개선 조치 수',
    imprvAndImplmntt_cause: '원인',
    imprvAndImplmntt_harmFulRiskFactors: '유해·위험요인',
    imprvAndImplmntt_relatedEvidence: '관련근거',
    imprvAndImplmntt_currentSafetyMeasures: '현재 안전보건 조치',
    imprvAndImplmntt_currentRiskLevel: '현재 위험성 수준',
    imprvAndImplmntt_riskEstimationCriteria: '위험성 추정기준',
    imprvAndImplmntt_likelihoodFrequency: '가능성(빈도)',
    imprvAndImplmntt_seriousnessIntensity: '중대성(강도)',
    imprvAndImplmntt_riskDetermination: '위험성 결정',
    imprvAndImplmntt_reductionMeasures: '감소대책',
    imprvAndImplmntt_completionDate: '완료일',
    imprvAndImplmntt_manager: '담당자',
    imprvAndImplmntt_expectedImprovementDate: '개선 예정일',
    imprvAndImplmntt_improvementCompletionDate: '개선 완료일',
    imprvAndImplmntt_residualRiskLevel: '개선 위험성 수준',
    imprvAndImplmntt_remedialAction: '개선 조치',
    imprvAndImplmntt_beforeImprovement: '개선 전',
    imprvAndImplmntt_afterImprovement: '개선 후',
    imprvAndImplmntt_executionDetails: '실행내용',
    imprvAndImplmntt_importance: '중대성',
    imprvAndImplmntt_standard: '기준',
    imprvAndImplmntt_riskLevel: '위험성 수준',
    imprvAndImplmntt_managementStandards: '관리 기준',
    imprvAndImplmntt_possibility: '가능성',
    //#endregion

    //#region 실시간 영상 안전관리
    SafetyMonitoring: '안전모니터링',
    SafetyMonitoringDesc: '안전모니터링 화면',
    SafetyMonitoringHistory: '안전 감지 이력',
    SafetyMonitoringHistoryDesc: '안전 모니터링 이력 및 상세 정보 조회 화면',
    SafetyVideoServerManage: '영상 서버관리',
    SafetyVideoServerManageDesc: 'AI 서버 등록 및 관리 화면',
    SafetyDetectionCameraManage: 'CCTV 설정',
    SafetyDetectionCameraManageDesc: 'CCTV(카메라) 등록 및 관리 화면',
    SafetyMonitoringManage: '안전 모니터링 설정',
    SafetyMonitoringManageDesc: '안전모니터링 영역 설정 및 저장 관리 화면',
    SafetyMonitoringZoneManage: '안전 감시 구역 설정',
    SafetyMonitoringZoneManageDesc: 'CCTV(카메라)별 위험구역 좌표 외 기타 설정 및 관리 화면',          
    //#endregion

    //#region 영상 서버관리
    viewDetails: '상세보기',
    server_id: '서버 ID',
    server_name: '서버 이름',
    server_ip: '서버 IP',
    restapi_port: '플라스크 PORT',
    mediamtx_port: '스트림서버 포트',
    remark: '비고',
    created_by: '작성자',
    created_at: '작성일',   
    updated_by: '수정자',
    updated_at: '수정일',        
    //#endregion

    //#region 카메라 설정
    cctv_id: 'CCTV ID',
    cctv_name: 'CCTV 명',
    camera_desc: 'CCTV 설명',
    rtsp_add: 'CCTV 주소',
    event_type_name: '이벤트 종류',  
    event_time : '발생일시',
    event_desc : '이벤트 설명',
    //#endregion 

    //#region 즐겨찾기
    favorites: '즐겨찾기',
    favorites_example: '즐겨찾기(예시)',
    //#endregion
};
export default locale_ko;
