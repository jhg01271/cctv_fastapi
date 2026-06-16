/**
 * ============================================================================================================================================================
 * 👉 다국어 (영어) - 커스텀
 * ============================================================================================================================================================
 */
const locale_en = {
  //region 메시지
  // Load previous year data
  msgPreviousConfirm: 'Previous year data has not been saved.\nDo you want to continue?',
  msgPreviousError: 'Previous year data has not been saved.\nPlease save before proceeding.',
  msgPreviousSave: 'It can be used after importing the previous year.',
  msgPreviousSaveError: "You can only select data from the previous year while importing from the previous year.",
  msgPreviouseNoData: "There is no previous year's data.",
  msgPreviousLoad: 'Would you like to retrieve data from the previous year?',
  // Organization chart
  msgCopyConfirm: 'Do you want to copy the current organization chart to create a new one?',
  //endregion

  //#region 사용자 정보 변경
  mypage_title: 'Change User Info',
  mypage_signpopup_title: 'Change Signature',
  mypage_signature: 'Signature',
  mypage_changePassword: 'Change Password',
  mypage_newPassword: 'New Password',
  mypagee_confirmNewPassword: 'Confirm New Password',
  phoneNumber: 'Phone Number',
  gender: 'Gender',
  male: 'Male',
  female: 'Female',
  //endregion

  //#region 대시보드
  dashboard_actionPending: 'Action Pending',
  dashboard_actionCompleted: 'Action Completed',
  dashboard_nonConformance: 'Non Conformance',
  dashboard_notConfigured: 'Not Configured',
  dashboard_compliance: 'Compliance',
  dashboard_high: 'High',
  dashboard_medium: 'Medium',
  dashboard_low: 'Low',
  dashboard_completed: 'Completed',
  dashboard_inProgress: 'InProgress',
  dashboard_pending: 'Pending',
  dashboard_titleCompliance: 'Compliance',
  dashboard_legalManageReview: 'Legal Manage · Review',
  dashboard_legalManage: 'Legal Management',
  dashboard_legalReviewReport: 'Legal Review Report',
  dashboard_riskAndOpportunity: 'Risk and Opportunity',
  dashboard_risk: 'Risk',
  dashboard_opportunity: 'Opportunity',
  dashboard_correctiveRequestPrevention: 'Corrective Action · Recurrence Prevention',
  dashboard_correctiveRequestPreventionCnt: 'Corrective Action · Recurrence Prevention Count',
  dashboard_actionRate: 'Corrective Action Rate',
  dashboard_byOrganization: 'By Organization',
  dashboard_registrationStatus: 'Registration Status',
  dashboard_continuousMonitoring: 'Continuous Monitoring',
  dashboard_monitoringAndEvaluation1: 'Monitoring, Performance Measurement and',
  dashboard_monitoringAndEvaluation2: 'Evaluation Result Items Count',
  dashboard_taskStatus: 'Task Status',
  dashboard_dateSelection: 'Date Selection',
  dashboard_taskStatusByPeriod: 'Task Status by Period',
  dashboard_myTaskStatusByPeriod: 'My Task Status by Period',
  dashboard_internalAudit: 'Internal Audit',
  dashboard_riskAssessment: 'Risk Assessment',
  dashboard_hazardRegistrationStatus: 'Hazard Registration Status',
  dashboard_riskReductionRegister: 'Risk Reduction Registration Status',
  dashboard_riskReductionImplementation: 'Risk Reduction Implementation Status',
  dashboard_nearMiss: 'Near Miss',
  dashboard_completionRate: 'Completion rate',
  dashboard_hseObjectives: 'HSE Objectives',
  dashboard_safetyAndHealthPolicy: 'Safety and Health Policy',
  dashboard_safetyAndHealthObjectives: 'Safety and Health Objectives Count',
  dashboard_budget: 'Budget (in ten thousand KRW)',
  dashboard_performance: 'Performance (in ten thousand KRW)',
  dashboard_hseObjectivesPerformance: 'HSE Objectives Performance Status',
  dashboard_targetRate: 'Target Rate',
  dashboard_progressRate: 'Progress Rate',
  dashboard_attainmentRate: 'Attainment Rate',
  dashboard_registerRate: 'Register Rate',
  dashboard_transitionRate: 'Transition Rate',
  dashboard_progressPerformance: 'Progress Performance Count',
  dashboard_tbmStatus: 'TBM Status',
  dashboard_tbmCreated: 'Created',
  dashboard_tbmSignatureCompleted: 'Signature Completed',
  dashboard_educationStatus: 'Education Status',
  dashboard_educationHr: 'Planned Personnel',
  dashboard_educationPerformedHr: 'Performed Personnel',
  dashboard_annualEducationPlanRegistered: 'Annual Safety and Health Education Plan: Registered',
  dashboard_annualEducationPlanNotRegistered: 'Annual Safety and Health Education Plan: Not Registered',
  dashboard_safetyCheckStatus: 'Safety Check Status',
  dashboard_equipmentSafetyCheck: 'Equipment Safety Check',
  dashboard_good: 'Good',
  dashboard_bad: 'Bad',
  dashboard_goodRate: 'Good Rate',
  dashboard_equipmentTypeChecklistRegistered: 'Equipment Type Checklist Registered Count: ',
  dashboard_equipmentTypeChecklistNotRegistered: 'Equipment Type Checklist Not Registered Count: ',
  dashboard_riskAssessmentOrganization: 'Risk Assessment Organization',
  dashboard_notRegistered: 'Not Registered',
  dashboard_safetyAndHealthInfoSurvey: 'Safety and Health Information Survey',
  dashboard_registered: 'Registered',
  dashboard_processEquipmentMaterial: 'Process/Equipment/Material Classification',
  dashboard_hazardClassification: 'Hazard Classification',
  dashboard_riskAssessmentPlan: 'Risk Assessment Plan',
  dashboard_riskReductionReg: 'Risk Reduction Registration',
  dashboard_riskReductionImpl: 'Risk Reduction Implementation',
  dashboard_riskAssessmentCompleted: 'Risk Assessment Completed',
  dashboard_hazardsClassification: 'Hazards Classification Status',
  dashboard_hazardsClassificationRegistered: 'Hazards Registration Count: ',
  dashboard_hazardsClassificationNotRegistered: 'Hazards Not Registered Count: ',
  dashboard_rejected: 'Rejected',
  dashboard_reviewed: 'Reviewed',
  dashboard_accepted: 'Accepted',
  dashboard_lastCheckTime: 'Last Check Time',
  dashboard_renewalCycle: 'Renewal cycle',
  dashboard_minute: 'Minutes',
  //#endregion

  //#region 메뉴 상태 및 툴팁
  menuY: 'REG',
  menuN: 'NREG',
  menuTootipY: 'The document has been registered.',
  menuTootipN: 'There are no registered documents.',
  //#endregion

  //#region 신규 작성 카드 명칭
  card_addOrgn: 'Add',
  card_addPartner: 'Add',
  card_addHr: 'Add',
  card_addWorkplace: 'Add',
  card_addProcess: 'Add',
  card_addEquipment: 'Add',
  card_addOrganizationStatus: 'Add',
  card_addWorkerStakeholder: 'Add',
  card_addHseJobAssignment: 'Add',
  card_addHsePolicy: 'Add',
  card_addHseObjectives: 'Add',
  card_addDetailedActionPlan: 'Add',
  card_addOhsCommittee: 'Add',
  card_addPartnerCommittee: 'Add',
  card_addMSDS: 'Add',
  card_addSafetyAndHealthInfoSurvey: 'Add',
  card_addWorkersOpinionsOnHazard: 'Add',
  card_addClassificationProcessEquipMsds: 'Add',
  card_addResultOfRiskAssessmentTraining: 'Add',
  card_addClassificationHazards: 'Add',
  card_addRiskAssessmentPlan: 'Add',
  card_addRiskAndOpsAsmtCriteria: 'Add',
  card_addRiskOpportunities: 'Add',
  card_addRegal: 'Add',
  card_addRegalReview: 'Add',
  card_addTbm: 'Add',
  card_addSafetyAndHealthObjectives: 'Add',
  card_addJobCompetencyAssessment: 'Add',
  card_addSAndHTrainingImplPlan: 'Add',
  card_addSAndHTrainingResultsPlan: 'Add',
  card_addQualificationCertificationAssessment: 'Add',
  card_addSafetyDutiesDesigDocument: 'Add',
  card_addSAndHQualificationCertRegister: 'Add',
  card_addSafetyAndHealthInfoRegister: 'Add',
  card_addSafetyAndHealthCommPlan: 'Add',
  card_addDocument: 'Add',
  card_addWorkerHealthGuide: 'Add',
  card_addSafetyChecklist: 'Add',
  card_addPermitToWork: 'Add',
  card_addHazMatsInspection: 'Add',
  card_addHazChemsInOut: 'Add',
  card_addMgmtOfChange: 'Add',
  card_addContractorInvestigation: 'Add',
  card_addContractorSAndHAssmt: 'Add',
  card_addEmergencyTask: 'Add',
  card_addScenario: 'Add',
  card_addEmergencyResponseTrainingResult: 'Add',
  card_addFacilityEquip: 'Add',
  card_addEvaluationReport: 'Add',
  card_addCorrectiveRequest: 'Add',
  card_addOhsInternalAuditExecutionPlan: 'Add',
  card_addOhsCorrectiveActionRequest: 'Add',
  card_addLeaglComplianceEvaluationTable: 'Add',
  card_addCorrectiveActionRequest: 'Add',
  card_addNearMiss: 'Add',
  card_addIncident: 'Add',
  card_addPPEMgmt: 'Add',
  card_addMachcard_addSafetyMgmtOfHazardousMachineryineSafety: 'Add',
  card_addEmergencyControlOrganChart: 'Add',
  card_addOrganChart: 'Add',
  card_safetyAndHealthOrganChart: 'Add',
  card_RiskAssessmentOrganChart: 'Add',
  card_ContractorRegisterRegister: 'Add',
  card_AnnualOhsInternalAuditPlan: 'Add',  
  //#endregion

  //#region 고객사 관리
  toList: 'To list',
  viewDetails: 'View details',
  clientManage: 'Customer management',
  addClientManage: 'Add customer management',
  clientManageDetail: 'Customer management details',
  viewDetail: 'View details',
  clntName: 'Customer name',
  businessRegistrationNumber: 'Customer mission name',
  exponent: 'Exponent',
  zipCode: 'Zip code',
  address: 'Address',
  maxHrCount: 'Maximum number of people connected',
  detailAddress: 'Detailed address',
  email: 'Email',
  telNo: 'Phone Number',
  fax: 'Fax',
  industry: 'Industry',
  industryCode: 'Industry Code',
  use: 'Use',
  addressSearch: 'Address search',
  customerLogo: 'Customer logo',
  compLogo: 'workplace logo',
  createdAt: 'Registration date',
  birthDate: 'Birth date',
  etc: 'Etc',
  //#endregion

  //#region 사업장 관리
  client: 'Customer',
  compNm: 'Business name',
  compManage: 'Business site management',
  addCompManage: 'Add workplace management',
  compManageDetail: 'Business site management details',
  constructionClassification: 'Construction classification',
  constructionName: 'Construction name',
  useYn: 'Whether to use',
  array: 'Sort',
  //#endregion

  //#region 고객사별 사업장 관리
  clinetComp_clntTitle: 'Client List',
  clinetComp_compTitle: 'Company List',
  clinetComp_compClntId: 'Client ID',
  clinetComp_clntCompId: 'Company ID',
  clinetComp_clntNm: 'Client Name',
  clinetComp_compNm: 'Company Name',
  clinetComp_rpstNm: 'Representative',
  clinetComp_telNo: 'Tel No',
  //#endregion

  //#region 사용자 관리
  user: 'User',
  userManage: 'User management',
  userManageDetail: '사용자관리 상세',
  addUserManage: '사용자관리 추가',
  id: 'Id',
  password: 'Password',
  verifyPassword: 'Verify password',
  userNm: 'User Name',
  authority: 'Authority',
  note: 'note',
  lastAccessDate: 'Last access date',
  lastAccessOs: 'Last access os',
  //#endregion

  //#region 그룹 관리
  group: 'Group',

  //#region 조직 관리    
  orgnNm: 'Organization name',
  orgnParent: 'Parent organization',
  orgnPerson: 'Organization personnel',
  orgnDesc: 'Organization description',
  personList: 'Person list',
  orgn_createdAt: 'created at',
  orgn_resultCnt: 'count',
  //#endregion

  //#region 협력사 관리
  partnerManage: 'Partner management',
  partnerManageDetail: 'Partner management details',
  addpartnerManage: 'Add partner management',
  partnerNm: 'Partner company name',
  mainBusiness: 'Main business',
  partnerTransactionItem: 'Items traded with the Corporation',
  partnerLogo: 'Partner logo',
  partnerLocation: 'Partner company location',
  //#endregion

  //#region 인원 관리
  affCompNm: 'Affiliated company name',
  position: 'Position',
  orgn: 'Organization',
  partner: 'Partner',

  //#region  작업장 관리
  workplaceManage: 'Workplace management',
  workplaceManageDetail: 'Workplace management detail',
  addWorkplaceManageDetail: 'Add Workplace management',
  workplace: 'Workplace',

  workplaceNm: 'Workplace name',
  headHrNm: 'Person in charge(main)',
  secondHrNm: 'Person in charge(sub)',
  workplaceArea: 'Workplace area name',
  workplaceAlias: 'Workplace alias',
  workplaceRemark: 'Workplace remark',
  //#endregion

  //#region  설비 관리
  equipmentManage: 'Equipment management',
  equipmentManageDetail: 'Equipment Management Detail',
  typeofEquipment: 'Type of Equipment',
  typeofEquipmentManage: 'Type of Equipment Manage',
  equipmentNm: 'Equipment Name',
  equip_div_orgn: 'organization',
  equip_div_workplace: 'workplace',
  equip_div_etc: 'headquarters/contracting/mixed materials',
  //#endregion

  //#region  조직과 상황
  orgnStat_createdAt: '작성일',
  orgnStat_createdBy: '작성자',
  orgnStat_cnt: 'Entry Count',
  //#endregion

  //#region 근로자 및 이해관계자
  empStake_stakeholders: '이해관계자',
  empStake_equirements: '요구사항',
  empStake_obligation: '준수 의무사항',
  empStake_risk: '리스크',
  empStake_chance: '기회',
  empStake_createdAt: '작성일',
  empStake_createdBy: '작성자',
  empStake_cnt: 'Entry Count',
  empStake_approvalStatus: '결재상태',
  //#endregion

  //#region Hse 목표
  hsePolicy: 'HSE Policy',
  hsePolicyDesc: 'Individual business owners, managers, etc. shall establish safety and health goals and management policies to prevent hazards or risks in the safety and health of workers in consideration of the characteristics, size, etc. of a business or workplace that is substantially controlled, operated, and managed by individual business owners, corporations, or institutions.',
  objectivesAndActionPlan: 'Objectives & Action Plan',
  objectivesAndActionPlanDesc: '',
  detailedActionPlan: 'Detailed Action Plan',
  detailedActionPlanDesc: '',
  //#endregion

  //#region 목표 및 중점추진사항
  objectives_schedule: 'Target schedule',
  objectives_budget: 'Budget',
  objectives_budget_won: 'Budget(10,000 won)',
  objectives_performance: 'Performance goals',
  //#endregion

  //region 공지사항
  notice_viewDetails: 'View Details',
  notice_title: 'Notice List',
  notice_detail: 'Notice Detail',
  notice_clntId: 'Client ID',
  notice_writeDt: 'Write Date',
  notice_docNo: 'Document Number',
  notice_subject: 'Subject',
  notice_content: 'Content',
  notice_hrId: 'Author ID',
  notice_createdAt: 'Created At',
  notice_updatedAt: 'Updated At',
  notice_dispStDate: 'Display Start Date',
  notice_dispEdDate: 'Display End Date',
  notice_useYn: 'Usage Status',
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
  safety_and_health_act: 'Occupational Safety and Health Act',
  chemical_act: 'Chemicals Control Act',
  dangerous_act: 'Dangerous Goods Safety and Control Act',
  recent_data: 'recent data',
  public_data: 'public data',
  update_date: 'update date',
  msds_unit: 'unit',
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
  hseAssign_assignTask: 'Assign Task',
  hseAssign_backupHrNm: 'backup Hr',
  hseAssign_writeYear: 'Write Year',
  hseAssign_notCompletedTasks: 'Not completed tasks',
  //#endregion

  //#region 협의 및 참여
  ohsCommittee: 'Occupational Safety and Health Committee',
  ohsCommitteeDesc: '1. In operating the safety and health management system, stipulate activities related to responsibilities and requirements for participation and consultation of all classes and related workers or worker representatives.',
  partnerCommittee: 'PartnerCommittee',
  partnerCommitteeDesc: '1. In operating the safety and health management system, stipulate activities related to responsibilities and requirements for participation and consultation of all classes and related workers or worker representatives.',
  //#endregion

  //#region hse  조직도
  safetyAndHealthOrganChart: 'Safety and health organization chart',
  safetyAndHealthOrganChartDesc: 'In the safety and health management system, form a safety and health organization to comply with legal requirements by clarifying what responsibilities and authorities the members of the organization have, and to efficiently resolve issues related to safety and health by clarifying the reporting system and communication channels.',
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
  educationStatusDesc: '1. 교육 대상자의 교육 이수 시간, 횟수 등 전반적인 교육 내역을 확인할 수 있습니다. \n2. 교육 현황을 모니터링하고, 누락된 교육을 식별하여 보완 계획 수립에 활용할 수 있습니다.',
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
  safetyAndHealthInfoRegister: 'S.H Information Management Register',
  safetyAndHealthInfoRegisterDesc: `The management ledger shall collect, record, and preserve necessary information to ensure the safety of workers and workplaces. The management ledger shall document various safety activity records to raise workers' safety awareness and prevent accidents and accidents that may occur in the workplace`,
  mySafetyAndHealthInfoRegister: 'My S.H Information Management Register',
  mySafetyAndHealthInfoRegisterDesc: `The management ledger shall collect, record, and preserve necessary information to ensure the safety of workers and workplaces. The management ledger shall document various safety activity records to raise workers' safety awareness and prevent accidents and accidents that may occur in the workplace`,
  safetyAndHealthCommPlan: 'S.H Communication Plan',
  safetyAndHealthCommPlanDesc: 'Establishment of a system that facilitates communication of safety and health information with workers in the workplace to prevent disasters and enable rapid response in the event of a disaster',
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
  doc_seq: 'Document Number',
  doc_writeDt: 'Creation Date',
  doc_writer: 'Author',
  doc_cnt: 'Number of Registrations',
  //#endregion

  //#region 실행(운영)
  emergencyControlOrganChart: 'Emergency Control Organization Chart',
  emergencyControlOrganChartDesc: `A visual representation of the organization's pre-organized organization and division of roles to respond quickly and effectively to crisis situations (fire, natural, industrial, safety, etc.)`,
  emergencyControlTaskAsgmt: 'Emergency Control Org. Task Assignment',
  emergencyControlTaskAsgmtDesc: 'A summary of the specific tasks that each department and individual should perform to ensure that organizations respond efficiently in the event of an emergency',
  emergencyResponseTraining: 'Emergency Response Training',
  emergencyResponseTrainingDesc: 'To prepare for various risks that may arise in real situations and to check and improve response capabilities on a regular basis',
  facilityEquipStatusTable: '시설/설비별 특징 현황표 ',
  facilityEquipStatusTableDesc: '시설/설비별 특징 현황표 ',
  //#endregion

  //#region 비상대응훈련
  ert_newScenario: 'Add New Scenario',
  ert_orgnNm: 'Organization',
  ert_trainingNm: 'Emergency Name',
  ert_trainingLocation: 'Location',
  ert_useYn: 'Usage Status',
  ert_trainingContent: 'Details of Occurrence',
  ert_anticipatedDamage: 'Expected Damage (Safety and Health Management Impact)',
  ert_situation: 'Situation (overdue)',
  ert_targetTime: 'Time elapsed (minutes)',
  ert_performer: 'Performance of Duties',
  ert_methodAction: 'A way of behaving',
  ert_remark: 'Remark',
  ert_uploadFile: 'Upload files',

  ert_newResult: 'Add New Report',
  ert_trainingCnt: 'Number of training sessions',
  ert_new: 'New',
  ert_scenario: 'Scenario',
  ert_actionNm: 'Training Name',
  ert_actionDt: 'Training ',
  ert_actionLocation: 'Training Site',
  ert_actionContent: 'Training Content',
  ert_actionResult: 'Training Result',
  ert_goalTargetTime: 'Target elapsed time',
  ert_minute: 'minutes',
  ert_measurementTime: 'Measurement time',
  ert_observationActualBehavior: 'Observation of actual behavior',

  ert_tooltipscenario: 'Only the approved emergency response training scenario is displayed',
  //#endregion

  //#region 위험성평가 > 공정/설비/물질 구분
  evaluationPeriod: 'Evaluation Period',
  processOverview: 'Process Overview',
  participants: 'Participants',
  processCount: 'Number of Processes',
  classProcConfirmDt: 'Date of confirmation',
  classProcWriteYear: 'Write Year',
  ClassificationProcessEquipMsds_msgCopyConfirm: 'Do you want to copy the current Process/Facility/Material Classification to create a new one?',
  classProcWritemDt: 'Write Date',
  classProcInfoMsg: '[Standard Information > Process Management]\n1. Check if a process is set for the selected organization.\n\n[Safety and Health Information Survey]\n1. Check if the process information of the selected organization has been written.\n2. Check if it is a confirmed stage.',
  //#endregion

  //#region Hse 목표
  workerHealthMgmtGuidelines: 'Worker health management guidelines',
  workerHealthMgmtGuidelinesDesc: '근로자의 건강 점검 및 직업병을 예방함으로써 육체적, 정신적, 심리적 건강을 유지‧증진 시킨다.',
  //#endregion

  //#region Hse 실적
  hseKeyPerformanceResults: 'HSE Focus Performance',
  hseKeyPerformanceResultsDesc: `1. In operating the safety and health management system, the relevant activities for the responsibilities and requirements for consultation on the participation of all classes and related workers or workers' representatives shall be prescribed`,
  hseKeyPerformanceResults_requiredPerformance: 'Performance Results is required.',
  hseKeyPerformanceResults_enactedDt: 'Enacted Date',
  hseKeyPerformanceResults_revisedDt: 'Revised Date',
  hseKeyPerformanceResults_docCount: 'Doc Count',
  hseKeyPerformanceResults_resCount: 'Res Count',
  //#endregion

  //#region 근로자 건강관리 지침
  whmg_name: 'name',
  whmg_group: 'group',
  whmg_healthDiary: 'health consultation diary',
  whmg_improvementConsultationLog: 'Improvement request consultation log',
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
  safetyMgmtGuidelines: 'Safety management guidelines (inspection log, etc.)',
  safetyMgmtGuidelinesDesc: '근로자의 업무 수행 중 발생하는 재해를 미연에 방지하기 위한 작업 조건과 환경개선 및 안전 방호대책을 강구하며, 종업원의 안전을 보장하고 업무 능률을 향상시켜 공사의 재산을 보호하며 재해 발생에 대한 책임 한계를 명확히 한다.',
  safety_itemCnt: 'Number of Inspection Items',
  safety_detailCnt: 'Number of Checklist',
  work_jobType: 'Type of Work',
  work_jobTime: 'Working Hours',
  work_jobPlace: 'Work Place',
  work_jobEquipment: 'Work Equipment',
  work_approvalStatus: 'Payment Status',
  //#endregion

  //#region 위험성평가 - 위험성평가표 - 유해위험요인분류
  hazardsClassification_cnt: 'harmfulness Number of risk factors',
  hazardsClassification_revNo: 'Work content level',
  hazardsClassification_revNm: 'Work content name',
  hazardsClassification_writeDt: 'Work content Write Date',
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
  revisedDt: '제정일',
  enactedDt: '개정일',
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
  workplaceSafetyGuidelines: 'Workplace Safety Standards Guidelines',
  workplaceSafetyGuidelinesDesc: '업무 활동 및 기타 제반 활동에 필요한 위험물 및 유해화학물질의 직·간접적인 위험요인 파악, 위험성 및 유해성 평가, 관리수단 결정 및 등록 관리에 관한 절차를 규정함으로써 안전사고를 예방하고 안전하고 쾌적한 작업환경을 유지한다. ',
  workplace_inspectionCnt: 'Number Of Inspections',
  workplace_msdsList: 'MSDS List',
  //#endregion

  //#region 변경관리
  mgmtOfChange: '변경관리',
  mgmtOfChangeDesc: '안전보건경영시스템 운영에 직접 적용되는 관련 법규, 규약, 기타요건(이하 "법규"라 한다)등 안전보건성과에 영향을 주는 계획된 임시변경 및 영구적인 변경 실행과 관리 및 의도하지 않은 변경으로 인한 리스크를 줄이기 위한 변경 실행 및 관리를 위한  프로세스를 규정',
  //#endregion

  //#region 조달
  procurement: 'procurement',
  procurementDesc: 'Risk Assessment Procurement is an activity to evaluate safety and health risks that may arise in the procurement process in advance and to select safe and suitable companies in the process of signing contracts with suppliers (subject to procurement).\nThis is done to ensure the safety management capabilities of the suppliers, the safety of the work environment, compliance with legal requirements, and to minimize potential risks in the procurement process.',
  //#endregion

  //#region 계약 대상업체 조사표
  cif_new: 'new',
  cif_enterDirectly: 'Enter directly',
  cif_partCompNm: 'Company Name',
  cif_createdAt: 'Registration Date',
  cif_useYn: 'Usage Status',
  cif_partLocation: 'Partner Location',
  cif_desc: 'Main Business',
  cif_partCompItem: 'Items Traded with the Construction (Service Details)',
  cif_zipCd: 'Postal Code',
  cif_msg_zipCd: 'Search Postal Code',
  cif_addrs1: 'Address',
  cif_addrs2: 'Detailed Address',
  cif_examDt: 'Examination Date',
  cif_investigator1: 'Investigator 1',
  cif_investigator2: 'Investigator 2',
  cif_msg_emergency_manage: '*Please register inspection items first.',
  cif_emergency_manage: 'Inspection Item Management',
  cif_typeNm: 'Category',
  cif_index: 'Number',
  cif_detailContent: 'Inspection Details',
  cif_allotment: 'Allotment',
  cif_comment: 'Comments',
  cif_score: 'Score',
  cif_upper: 'High',
  cif_middle: 'Medium',
  cif_lower: 'Low',
  cif_totalAllotment: 'Total Allotment',
  cif_totalScore: 'Total Score',
  cif_partComp: 'Company',
  cif_item: 'Traded Items',
  //#endregion

  //#region TBM
  tbm_date: 'TBM date',
  tbm_time: 'TBM time',
  tbm_sameWorkDate: 'Is the work date the same?',
  tbm_rgstDate: 'Registration date',
  tbm_whetherToUse: 'Whether to use',
  tbm_workNm: 'job name',
  tbm_workDetail: 'work detail',
  tbm_workPlace: 'TBM location',
  tbm_riskAssessmentConducted: 'Whether risk assessment is conducted?',
  tbm_measures: 'measures',
  tbm_keyRiskFactors: 'Key risk factors',
  tbm_selected: 'selected',
  tbm_checkTbmLeader: 'Check TBM leader',
  tbm_checkSafetyMeasuresBeforeWork: 'Check safety measures before work',
  tbm_potentialRiskFactors: 'Potential risk factors',
  tbm_actionTaken: 'Action taken',
  tbm_actionTakenIfNo: 'Action taken if ‘No’',
  tbm_safetyInspectionBeforeWork: 'Results of daily safety inspection before work',
  tbm_closingMeetingAfterWork: 'Closing meeting after work (effectiveness of key measures)',
  tbm_confirmAttendees: 'Confirm attendees',
  tbm_msgTimeRange: 'The time range is incorrect.',
  tbm_msgNewData: 'This is new data.',
  tbm_msgEnterTbmDate: 'Please enter the TBM date.',
  tbm_msgEnterTbmTime: 'Please enter the TBM time.',
  tbm_msgEnterTaskName: 'Please enter the task name.',
  tbm_msgOverlappingPotentialRiskFactor: 'There are overlapping potential risk factors.',
  tbm_msgNoStorePotentialRiskFactor: 'There are no stored potential risk factors.',
  tbm_msgNoRiskImpl: 'There is no registered risk assessment implementation information.',
  tbm_leader: 'TBM leader',
  tbm_attend: 'number of attendees',
  tbm_01Month: 'January',
  tbm_02Month: 'February',
  tbm_03Month: 'March',
  tbm_04Month: 'April',
  tbm_05Month: 'May',
  tbm_06Month: 'June',
  tbm_07Month: 'July',
  tbm_08Month: 'August',
  tbm_09Month: 'September',
  tbm_10Month: 'October',
  tbm_11Month: 'November',
  tbm_12Month: 'December',
  tbm_tooltipMeasures: 'Consider the order of removal → replacement → control',
  tbm_tooltipKeyRiskFactors: 'Among potential risk factors 1 to 3, select one important risk and write it down.',
  tbm_tooltipSafetyInspectionBeforeWork: 'Among risk factors, we focus on checking whether measures have not been taken and whether workers are familiar with the TBM contents.',
  tbm_tooltipConfirmAttendees: 'Identify workers not participating in TBM and encourage them to attend meetings',
  tbm_msgOverlappingKeyRiskFactor: 'There are overlapping key risk factors.',
  tbm_msgAlreadyAttend: 'The number of attendees already exists.',
  tbm_msgAlreadyLeader: 'This person already exists in the leader count.',
  tbm_msgDplLeaderAttend: 'There is some overlap between leaders and attendees.',
  //#endregion

  //#region 위험성평가 사전 준비 자료 - 이상걸
  workersOpinionsOnHazards_workplace: 'Workshop',
  workersOpinionsOnHazards_regDt: 'Posted on',
  workersOpinionsOnHazards_useYn: 'Availability',
  workersOpinionsOnHazards_experience: 'Stories',
  workersOpinionsOnHazards_sixPrinciple: 'Written according to the six principles (who, when, where, what, how, and why)',
  workersOpinionsOnHazards_writeExperience: 'Enter your experience',
  workersOpinionsOnHazards_opinion: 'Opinion',
  workersOpinionsOnHazards_workerOpinion: "Workers' Opinions",
  workersOpinionsOnHazards_reviewerOpinion: 'Reviewer Comments',
  workersOpinionsOnHazards_workerGuide: '● Causes of harmful and dangerous experiences and points to be improved',
  workersOpinionsOnHazards_reviewerGuide: '● Advice on experience',
  workersOpinionsOnHazards_writeOpinion: 'Enter your feedback',
  workersOpinionsOnHazards_newRegister: 'Hazard Risk Factors Worker Comments Added',
  //#endregion

  //#region 시설 설비 관리 - 이상걸
  facilityEquip_facilitisNm: 'Facility/Facility Name',
  facilityEquip_regDt: 'Registration Date',
  facilityEquip_ordSeq: 'Sorting',
  facilityEquip_useYn: 'Availability',
  facilityEquip_facilitisDesc: 'Description of Facilities',
  facilityEquip_emergencyInfo: 'Emergency Evacuation Guide',
  facilityEquip_register: 'New Facility Registration',
  //#endregion

  //#region 시설 설비 관리 - 이상걸
  safetyAndHealthInfoSurvey_process: 'Process',
  safetyAndHealthInfoSurvey_revNo: 'Degree of work',
  safetyAndHealthInfoSurvey_revNm: 'Degree name of work',
  safetyAndHealthInfoSurvey_regDt: 'Registration Date',
  safetyAndHealthInfoSurvey_useYn: 'Availability',
  safetyAndHealthInfoSurvey_material: 'Raw materials',
  safetyAndHealthInfoSurvey_procuct: 'Products',
  safetyAndHealthInfoSurvey_workerCnt: 'Number of Employees',
  safetyAndHealthInfoSurvey_getData: 'Reflection of existing information',
  safetyAndHealthInfoSurvey_newRegister: 'Add Safety and Health Surveys',
  safetyAndHealthInfoSurvey_msgCopyConfirm: 'Do you want to copy the current Safety and Health Information Survey to create a new one?',
  //#endregion

  //#region 아차사고 보고서
  nearMissReport: 'Near Miss Report',
  nearMissReport_desc: 'It refers to an incident where a potential accident risk is identified in advance by a worker or where conditions for an accident are not met, preventing it from escalating into an actual disaster.',
  //#endregion

  //#region 재해발생 보고서
  incidentReport: 'IncidentReport',
  incidentReport_Desc: '1. Each department head shall immediately report any incidents, accidents, or nonconformities to the head of the safety and health supervising department (maintaining written records)\n2. The head of the safety and health supervising department shall take temporary measures and report them to the CEO',
  //#endregion

  //#region 시정조치 요구서
  correctiveActionRequest: 'correctiveActionRequest',
  correctiveActionRequestDesc: '1. The head of the relevant department shall establish corrective action measures in the nonconformity report and conduct risk, opportunity and risk assessment to ensure continuous safety, if necessary. \n2. The safety and health department reviews the established corrective action measures, reviews the appropriateness, and verifies the effectiveness. \n3. Provide appropriate education and training to the relevant workers.',
  //#endregion

  //#region 모니터링 및 측정 관리
  evaluationReport: 'Monitoring, performance measurement and evaluation results',
  evaluationReportDesc: '안전보건 주관부서장은 다음 사항에 대한 적합성 여부를 모니터링, 성과측정하고 그 결과를 분석한다.\n' +
    '  1. 설정된 안전보건목표 및 세부목표의 달성 실적  \n' +
    '  2. 안전보건경영추진계획의 이행상태\n' +
    '  3. 관련 안전보건법규와 그 밖의 요구사항의 준수 및 이행상태\n' +
    '  4. 심각한 안전보건측면과 안전보건요인에 관련된 운영절차의 이행상태',
  complianceEvaluationTable: 'Legal Compliance Evaluation Table',
  complianceEvaluationTableDesc: '모니터링, 성과측정 및 평가 결과서의 항목사항별로 준수사항을 점검 내용을 구성하고 평가한다.',
  performanceEvaluationTable: 'Performance Evaluation Table',
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
  annualOhsInternalAuditPlan: 'Annual Occupational Health and Safety Internal Audit Plan',
  annualOhsInternalAuditPlanDesc: 'An annual plan to review and improve the effectiveness and adequacy of occupational health and safety activities within the organization',
  ohsInternalAuditExecutionPlan: 'Occupational Health and Safety Internal Audit Execution Plan',
  ohsInternalAuditExecutionPlanDesc: 'A document outlining the execution plan for internal audits to evaluate and improve the adequacy and implementation of the organization’s occupational health and safety management system',
  ohsInternalAuditResultsReport: 'Occupational Health and Safety Internal Audit Results Report',
  ohsInternalAuditResultsReportDesc: 'A document summarizing the results and improvement points identified through internal audits of the occupational health and safety management system',
  ohsCorrectiveActionRequest: '안전보건 시정조치 요구서',
  ohsCorrectiveActionRequestDesc: '안전보건 시정조치 요구서 메뉴는 작업장에서 발생할 수 있는 안전 및 보건 관련 문제를 개선하기 위해 필요한 조치를 명시하고 이를 문서화한 메뉴입니다.',
  //#endregion

  //#region 버튼 컴포넌트
  btnBack: 'BACK',
  btnSample: 'SAMPLE',
  btnSearch: 'SEARCH',
  btnAdd: 'ADD',
  btnSave: 'SAVE',
  btnClose: 'CLOSE',
  btnDelete: 'DELETE',
  btnEdit: 'EDIT',
  btnDownload: 'DOWN',
  btnNew: 'NEW',
  btnPreYear: 'PRETEAR',
  btnCall: 'CALL',
  btnSuggestionSearch: 'SHOW SUG',
  btnSuggestionAdd: 'ADD SUG',
  btnExcelForm: 'FORM',
  download: 'DOWN',
  btnExcel: 'EXCEL',
  upload: 'UPLOAD',
  btnCopy: 'COPY',
  btnWorker: 'WORKER',
  check: 'CHECK',
  //#endregion

  //#region 개선 및 실행
  imprvAndImplmntt_process: 'process',
  imprvAndImplmntt_workDetail: 'work detail',
  imprvAndImplmntt_classification: 'classification',
  imprvAndImplmntt_numberOfImprovementActions: 'Number of improvement actions',
  imprvAndImplmntt_cause: 'cause',
  imprvAndImplmntt_harmFulRiskFactors: 'Harmful/Risk Factors',
  imprvAndImplmntt_relatedEvidence: 'Related evidence',
  imprvAndImplmntt_currentSafetyMeasures: 'Current safety & health action',
  imprvAndImplmntt_currentRiskLevel: 'Current risk level',
  imprvAndImplmntt_riskEstimationCriteria: 'Risk estimation criteria',
  imprvAndImplmntt_likelihoodFrequency: 'Likelihood(frequency)',
  imprvAndImplmntt_seriousnessIntensity: 'Seriousness(Intensity)',
  imprvAndImplmntt_riskDetermination: 'Risk Determination',
  imprvAndImplmntt_reductionMeasures: 'Reduction measures',
  imprvAndImplmntt_completionDate: 'completion date',
  imprvAndImplmntt_manager: 'manager',
  imprvAndImplmntt_expectedImprovementDate: 'Expected improvement date',
  imprvAndImplmntt_improvementCompletionDate: 'Improvement completion date',
  imprvAndImplmntt_residualRiskLevel: 'Residual risk level',
  imprvAndImplmntt_remedialAction: 'remedial action',
  imprvAndImplmntt_beforeImprovement: 'Before improvement',
  imprvAndImplmntt_afterImprovement: 'After improvement',
  imprvAndImplmntt_executionDetails: 'Execution details',
  imprvAndImplmntt_importance: 'importance',
  imprvAndImplmntt_standard: 'standard',
  imprvAndImplmntt_riskLevel: 'risk level',
  imprvAndImplmntt_managementStandards: 'management standards',
  imprvAndImplmntt_possibility: 'possibility',
  //#endregion

  //#region 실시간 영상 안전관리
  SafetyMonitoring: 'SafetyMonitoring',
  SafetyMonitoringDesc: 'SafetyMonitoring',
  SafetyMonitoringHistory: 'SafetyMonitoringHistory',
  SafetyMonitoringHistoryDesc: 'SafetyMonitoringHistory',
  SafetyVideoServerManage: 'SafetyVideoServerManage',
  SafetyVideoServerManageDesc: 'SafetyVideoServerManage',
  SafetyDetectionCameraManage: 'SafetyDetectionCameraManage',
  SafetyDetectionCameraManageDesc: 'SafetyDetectionCameraManage',
  SafetyMonitoringManage: 'SafetyMonitoringManage',
  SafetyMonitoringManageDesc: 'SafetyMonitoringManage',
  SafetyMonitoringZoneManage: 'SafetyMonitoringZoneManage',
  SafetyMonitoringZoneManageDesc: 'SafetyMonitoringZoneManage',          
  //#endregion

  //#region 영상 서버관리
  viewDetails: 'view Details',
  server_id: 'Server ID',
  server_name: 'Server Name',
  server_ip: 'Server IP',
  restapi_port: 'Restapi Port',
  mediamtx_port: 'Mediamtx Port',
  remark: 'remark',
  created_by: 'Created By',
  created_at: 'Created At',   
  updated_by: 'Updated By',
  updated_at: 'Updated At',        
  //#endregion

  //#region 카메라 설정
  cctv_id: 'CCTV ID',
  cctv_name: 'CCTV NAME',
  camera_desc: 'CCTV DESC',
  rtsp_add: 'CCTV ADDRESS',
  event_type_name: 'EVENT TYPE NAME',
  event_time : 'EVENT TIME',
  event_desc : 'EVENT DESC',      
  //#endregion  

  //#region 즐겨찾기
  favorites: 'favorites',
  favorites_example: 'favorites(example)',
  //#endregion
};
export default locale_en;
