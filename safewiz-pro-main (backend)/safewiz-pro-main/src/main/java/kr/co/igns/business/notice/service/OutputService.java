package kr.co.igns.business.notice.service;

import kr.co.igns.business.evaluation.service.EvaluationCorrectiveActionRequestService;
import kr.co.igns.business.evaluation.service.MonitoringManageService;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
import kr.co.igns.business.impl.service.EmergencyControlTaskAsgmtService;
import kr.co.igns.business.impl.service.EmergencyResponseTrainingResultService;
import kr.co.igns.business.impl.service.HseKeyPerformanceResultService;
import kr.co.igns.business.improvement.service.NonconformityCorrectiveService;
import kr.co.igns.business.notice.dao.postgres.OutputDAO;
import kr.co.igns.business.notice.model.OutputVO;
import kr.co.igns.business.orgstatus.service.EmployeesAndStakeholdersService;
import kr.co.igns.business.orgstatus.service.OrganizationStatusService;
import kr.co.igns.business.participation.service.ActPlanService;
import kr.co.igns.business.participation.service.HsePolicyService;
import kr.co.igns.business.planning.service.*;
import kr.co.igns.business.support.service.AnnualTrainingPlanService;
import kr.co.igns.business.support.service.JobCompetencyAssessmentService;
import kr.co.igns.business.support.service.TrainingPlanImplService;
import kr.co.igns.business.support.service.TrainingResultReportService;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OutputService {
    private final OutputDAO outputDAO;

    private final UtilsService utilsService;
    private final ReportService reportService;

    /*****************************************
     * TBM
     *****************************************/
    private final ToolBoxMeetingService tbmService;

    /*****************************************
     * 위험성평가
     *****************************************/
    private final SafetyAndHealthInfoSurveyService safetyAndHealthInfoSurveyService; // 안전보건정보 조사
    private final ClassificationProcessEquipMsdsService classificationProcessEquipMsdsService; // 공정/설비/물질 구분
    private final ClassificationOfHazardsService classificationOfHazardsService; // 유해 위험요인 분류
    private final WorkersOpinionsOnHazardsService workerOpinionsOnHazardsService; // 유해 위험요인 근로자 의견
    private final ResultOfRiskAssessmentTrainingService resultOfRiskAssessmentTrainingService; // 위험성평가 교육/참여 결과
    private final ImplementationOfRiskAseessmentService implementationOfRiskAseessmentService; // 위험성평가 이행

    /*****************************************
     * 비상대응 훈련
     *****************************************/
    private final EmergencyControlOrganChartService emergencyControlOrganChartService; // 비상통제 조직도
    private final EmergencyControlTaskAsgmtService emergencyControlTaskAsgmtService; // 비상통제 조직별 업무 분장
    private final EmergencyResponseTrainingResultService emergencyResponseTrainingResultService; // 비상대응훈련

    /*****************************************
     * 조직의 상황
     *****************************************/
    private final OrganizationStatusService organizationStatusService; // 조직의 상황
    private final EmployeesAndStakeholdersService employeesAndStakeholdersService; // 근로자 및 이해관계자

    /*****************************************
     * 법규 관리 및 준수평가
     *****************************************/
    private final LegalManageService legalManageService; // 법규 관리
    private final LegalReviewService legalReviewService; // 법규 검토서

    /*****************************************
     * 리스크와 기회
     *****************************************/
    private final RisksAndOpportunitieService risksAndOpportunitieService; // 리스크와 기회 관리대장

    /*****************************************
     * 시정조치 요구서
     *****************************************/
    private final NonconformityCorrectiveService nonconformityCorrectiveService; // 부적합 및 개선사항 > 시정조치 요구서
    private final EvaluationCorrectiveActionRequestService evaluationCorrectiveActionRequestService; // 성과평가>모니터링 및 측정관리>시정조치 요구서

    /*****************************************
     * 모니터링 및 측정 관리
     *****************************************/
    private final MonitoringManageService monitoringManageService; // 모니터링, 성과측정 및 평가 결과서

    /*****************************************
     * 교육 훈련
     *****************************************/
    private final JobCompetencyAssessmentService jobCompetencyAssessmentService; // 직무 적격성 평가서
    private final AnnualTrainingPlanService annualTrainingPlanService; // 안전보건 연간 교육 계획서
    private final TrainingPlanImplService trainingPlanImplService; //안전보건 교육실시 계획서
    private final TrainingResultReportService trainingResultReportService; // 안전보건 교육결과 보고서

    /*****************************************
     * HSE 목표 및 안전보건환경 예산
     *****************************************/
    private final HsePolicyService hsePolicyService; // 안전보건경영방침
    private final ActPlanService actPlanService; // 목표 및 중점추진사항, 세부계획, 예산

    /*****************************************
     * 안전보건목표 및 추진실적
     *****************************************/
    private final SafetyAndHealthObjectivesService safetyAndHealthObjectivesService; // 안전보건목표
    private final HseKeyPerformanceResultService hseKeyPerformanceResultService; // HSE 중점 추진실적

    // 출력 항목 조회
    public List<OutputVO> getOutputList(SpSearchVO vo) throws Throwable {
        List<OutputVO> resultList = outputDAO.getOutputList(vo); // 출력가능한 문서 목록 (시스템코드 C0043)

        for (OutputVO result : resultList) {
            // 상세 항목 조회
            result.setWriteYear(vo.getWriteYear());
            result.setCompId(vo.getCompId());
            List<OutputVO> detailList = new ArrayList<>();
            result.setDetailList(detailList);
            if ("orgn".equals(result.getDetailCd())) {
                // 동적으로 조직이 떠야할 때 (조직의 상황)
                detailList = getExistOrgnData(result);

            } else if ("month".equals(result.getDetailCd())) {
                // 동적으로 조직이 떠야할 때 (월별)
                detailList = getExistMonthData(result);
            } else {
                // 상세 출력 항목이 있는 경우 출력가능여부 세팅
                detailList = getExistDetailData(result);
            }
            for (OutputVO detail : detailList) {
                result.setTotalPrintableCnt(result.getTotalPrintableCnt() + detail.getPrintableCnt());
            }
            result.setDetailList(detailList);
        }
        return resultList;
    }

    // 하위 레벨이 조직일 경우
    public List<OutputVO> getExistOrgnData(OutputVO result) throws Throwable {
        List<OutputVO> detailList = new ArrayList<>();
        switch (result.getRoute()) {
            case "OrganizationStatus":
                // 조직의 상황 [ tb_orgn_status, tb_employees ]
                detailList = outputDAO.getORGSTList(result);
                break;
            case "RisksAndOpportunitiesManage":
                // 리스크와 기회 [ tb_risks_and_opportunities ]
                detailList = outputDAO.getRAOList(result);
                break;
            case "CorrectiveActionRequest":
                // 시정조치 요구서 [ tb_corrective_action_request ]
                detailList = outputDAO.getCARList(result);
                break;
            default:
                break;
        }
        return detailList;
    }

    // 하위 레벨이 월별일 경우
    public List<OutputVO> getExistMonthData(OutputVO result) throws Throwable {
        List<OutputVO> detailList = outputDAO.getDetailList(result);
        switch (result.getRoute()) {
            case "ToolBoxMeeting":
                // TBM [ tb_toolboxmeeting ]
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(outputDAO.getCountTBM(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            case "MonitoringManage":
                // 모니터링 및 측정 관리 [ tb_evaluation_report ]
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(outputDAO.getCountER(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            case "EducationTraining":
                // 교육훈련 [ esg.tb_job_competency_assessment, tb_training_plan_annual, tb_training_plan_implementation, tb_training_result_report ]
                // 직무적격성, 연관 교육계획서는 count에서 제외, 출력 시에 상단에 1회 붙여서 나옴
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(outputDAO.getCountEDU(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            default:
                for (OutputVO detail : detailList) {
                    detail.setPrintableCnt(0); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
        }
        return detailList;
    }

    // 출력가능 여부 확인하는 메서드(하위 메뉴가 있을 때)
    public List<OutputVO> getExistDetailData(OutputVO result) throws Throwable {
        List<OutputVO> detailList = outputDAO.getDetailList(result);
        switch (result.getRoute()) {
            case "RiskAssessment":
                // 위험성평가
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(getRiskAssessmentCnt(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            case "EmergencyResponse":
                // 비상시 대비 및 대응
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(getEmergencyResponseCnt(detail)); // 문서 존재 여부 확인하여 상태값 저장
                    if("ECOC".equals(detail.getId())) {
                        // 비상통제 조직도인 경우
                        List<String> chartIdList = outputDAO.getECOCData(detail);
                        detail.setChartIdList(chartIdList);
                        detail.setPrintableCnt(chartIdList.size());
                    }
                }
                break;
            case "LegalMgmtAndComplianceEvaluation":
                // 법규 관리 및 준수평가
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(getLegalCnt(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            case "HseObjectives":
                // Hse 목표 및 안전보건환경 예산
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(getHseCnt(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            case "SafetyAndHealthObjectives":
                // 안전보건목표 및 추진실적
                for (OutputVO detail : detailList) {
                    detail.setWriteYear(result.getWriteYear());
                    detail.setCompId(result.getCompId());
                    detail.setPrintableCnt(getShoCnt(detail)); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
            default:
                for (OutputVO detail : detailList) {
                    detail.setPrintableCnt(0); // 문서 존재 여부 확인하여 상태값 저장
                }
                break;
        }
        return detailList;
    }

    // 위험성평가 메뉴 별 출력 가능여부
    public int getRiskAssessmentCnt(OutputVO detail) {
        int cnt = 0;
        switch (detail.getId()) {
            case "SAHIS":
                // 안전보건 정보 조사 [ tb_safety_and_health_info_survey ]
                cnt = outputDAO.getCountSAHIS(detail);
                break;
            case "WOOHA":
                // 유해 위험요인 근로자 의견 [ tb_workers_opinions_on_hazards_info ]
                cnt = outputDAO.getCountWOOHA(detail);
                break;
            case "CPE":
                // 공정/설비/물질 구분 [ tb_classification_process_equip_msds ]
                cnt = outputDAO.getCountCPE(detail);
                break;
            case "RORAT":
                // 위험성평가 교육/참여 결과 [ tb_result_of_risk_assessment_training ]
                cnt = outputDAO.getCountRORAT(detail);
                break;
            case "CHD":
                // 유해 위험요인 분류 [ tb_classification_hazards ]
                cnt = outputDAO.getCountCHD(detail);
                break;
            case "RAP":
                // 위험성평가 이행 [ tb_risk_assessment_plan, tb_risk_assessment_implementation ]
                cnt = outputDAO.getCountRAP(detail);
                break;
            default:
                break;
        }
        return cnt;
    }

    // 비상대응 훈련 메뉴 별 출력 가능여부
    public int getEmergencyResponseCnt(OutputVO detail) {
        int cnt = 0;
        switch (detail.getId()) {
            case "ECOC":
                // 비상통제조직도 [ tb_emergency_control_org_chart ]
                break;
            case "ECA":
                // 비상통제 조직별 업무분장 [ tb_emergency_control_task_asgmt ]
                cnt = outputDAO.getCountECA(detail);
                break;
            case "ERTF":
                // 비상대응 훈련 - 상반기 [ tb_emergency_response_training, tb_emergency_response_training_result ]
                detail.setDocType("first");
                cnt = outputDAO.getCountERT(detail);
                break;
            case "ERTS":
                // 비상대응 훈련 - 하반기 [ tb_emergency_response_training, tb_emergency_response_training_result ]
                detail.setDocType("second");
                cnt = outputDAO.getCountERT(detail);
                break;
            default:
                break;
        }
        return cnt;
    }

    // 법규 관리 및 준수평가 메뉴 별 출력 가능여부
    public int getLegalCnt(OutputVO detail) {
        int cnt = 0;
        switch (detail.getId()) {
            case "LGM":
                // 법규관리 [ tb_legal_manage ]
                cnt = outputDAO.getCountLGM(detail);
                break;
            case "LGR":
                // 법규 검토서 [ tb_legal_review ]
                cnt = outputDAO.getCountLGR(detail);
                break;
            default:
                break;
        }
        return cnt;
    }

    // HSE 목표 및 안전보건환경 예산 메뉴 별 출력 가능여부
    public int getHseCnt(OutputVO detail) {
        int cnt = 0;
        switch (detail.getId()) {
            case "PLC":
                // 안전보건경영방침 [ tb_hse_policy ]
                cnt = outputDAO.getCountPLC(detail);
                break;
            case "OBJ":
                // 목표 및 중점추진사항 [ tb_action_master, tb_action_objective_plan ]
                cnt = outputDAO.getCountOBJ(detail);
                if(cnt>0) {
                    cnt = 1;
                }
                break;
            case "OBJM":
                // 중점추진사항별 세부계획 [ tb_action_master, tb_action_detail_plan ]
                cnt = outputDAO.getCountOBJM(detail);
                if(cnt>0) {
                    cnt = 1;
                }
                break;
            case "OBJB":
                // 안전보건환경 예산 [ tb_action_master, tb_action_detail_plan ]
                cnt = outputDAO.getCountOBJB(detail);
                if(cnt>0) {
                    cnt = 1;
                }
                break;
            default:
                break;
        }
        return cnt;
    }

    // 안전보건목표 및 추진실적 메뉴 별 출력 가능여부
    public int getShoCnt(OutputVO detail) {
        int cnt = 0;
        switch (detail.getId()) {
            case "SHO":
                // 안전보건경영방침 [ tb_safety_health_objectives ]
                cnt = outputDAO.getCountSHO(detail);
                break;
            case "SHOP":
                // 목표 및 중점추진사항 [ tb_safety_health_objectives ]
                cnt = outputDAO.getCountSHOP(detail);
                break;
            default:
                break;
        }
        return cnt;
    }

    public void getOutputReport(HttpServletRequest request, HttpServletResponse response, List<OutputVO> voList) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        SpSearchVO frontReportVO = new SpSearchVO();
        frontReportVO.setExtra1(voList.get(0).getWriteYear() + "년도");
        JasperPrint JasperFrontReport = utilsService.getFrontReport(frontReportVO, "안전보건정보 출력");
        reportList.add(JasperFrontReport);

        List<Map<String, Object>> indexList = new ArrayList<>();

        int page = 1;
        int subPage = 1;
        for (OutputVO vo : voList) {
            int localPage = 1;
            List<SpSearchVO> params = new ArrayList<>(); // service 연결을 위한 파라미터
            if (voList.size() > 1) {
                Map<String, Object> indexParam = new HashMap<>();
                // 카테고리가 하나 이상일 경우 목차 레포트 출력
                indexParam.put("index", String.valueOf(subPage));
                indexParam.put("title", vo.getNm());
                indexParam.put("page", String.valueOf(page));
                indexList.add(indexParam);
            }
            // 카드 타이틀 페이지
            SpSearchVO titleVO = new SpSearchVO();
            titleVO.setPage(page);
            titleVO.setSubPage(subPage);
            titleVO.setLocalPage(1);
            String title = "";
            JasperPrint JasperTitleReport = new JasperPrint();
            switch (vo.getRoute()) {
                /*****************************************
                 * TBM
                 *****************************************/
                case "ToolBoxMeeting":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                                params.add(searchVO);
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                            params.add(searchVO);
                        }
                    }
                    List<JasperPrint> tbmReport = tbmService.getAllReport(request, response, params);
                    reportList.addAll(tbmReport);
                    for (JasperPrint report : tbmReport) {
                        page += report.getPages().size();
                    }
                    break;
                /*****************************************
                 * 위험성평가
                 *****************************************/
                case "RiskAssessment":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> riskAssessmentReport = new ArrayList<>();
                        if (searchVO.getWriteYear() != null) {
                            switch (detail.getId()) {
                                case "SAHIS":
                                    // 안전보건 정보 조사 [ tb_safety_and_health_info_survey ]
                                    riskAssessmentReport = safetyAndHealthInfoSurveyService.getAllReport(request, response, searchVO);
                                    break;
                                case "WOOHA":
                                    // 유해 위험요인 근로자 의견 [ tb_workers_opinions_on_hazards_info ]
                                    riskAssessmentReport = workerOpinionsOnHazardsService.getAllReport(request, response, searchVO);
                                    break;
                                case "CPE":
                                    // 공정/설비/물질 구분 [ tb_classification_process_equip_msds ]
                                    riskAssessmentReport = classificationProcessEquipMsdsService.getAllReport(request, response, searchVO);
                                    break;
                                case "RORAT":
                                    // 위험성평가 교육/참여 결과 [ tb_result_of_risk_assessment_training ]
                                    riskAssessmentReport = resultOfRiskAssessmentTrainingService.getAllReport(request, response, searchVO);
                                    break;
                                case "CHD":
                                    // 유해 위험요인 분류 [ tb_classification_hazards ]
                                    riskAssessmentReport = classificationOfHazardsService.getAllReport(request, response, searchVO);
                                    break;
                                case "RAP":
                                    // 위험성평가 이행 [ tb_risk_assessment_plan, tb_risk_assessment_implementation ]
                                    riskAssessmentReport = implementationOfRiskAseessmentService.getAllReport(request, response, searchVO);
                                    break;
                                default:
                                    break;
                            }
                        }
                        reportList.addAll(riskAssessmentReport);
                        for (JasperPrint report : riskAssessmentReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * 비상시 대비 및 대응
                 *****************************************/
                case "EmergencyResponse":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();

                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getDocType());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getDocType());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> emergencyResponseReport = new ArrayList<>();
                        if (searchVO.getWriteYear() != null) {
                            switch (detail.getId()) {
                                case "ECOC":
                                    // 비상통제조직도 [ tb_emergency_control_org_chart ]
                                    emergencyResponseReport = emergencyControlOrganChartService.getAllReport(request, response, searchVO);
                                    break;
                                case "ECA":
                                    // 비상통제 조직별 업무분장 [ tb_emergency_control_task_asgmt ]
                                    emergencyResponseReport = emergencyControlTaskAsgmtService.getAllReport(request, response, searchVO);
                                    break;
                                case "ERTF":
                                    // 비상대응 훈련 - 상반기 [ tb_emergency_response_training, tb_emergency_response_training_result ]
                                    emergencyResponseReport = emergencyResponseTrainingResultService.getAllReport(request, response, searchVO);
                                    break;
                                case "ERTS":
                                    // 비상대응 훈련 - 하반기 [ tb_emergency_response_training, tb_emergency_response_training_result ]
                                    emergencyResponseReport = emergencyResponseTrainingResultService.getAllReport(request, response, searchVO);
                                    break;
                                default:
                                    break;
                            }
                        }
                        reportList.addAll(emergencyResponseReport);
                        for (JasperPrint report : emergencyResponseReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * 조직의 상황
                 *****************************************/
                case "OrganizationStatus":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(vo.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(vo.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }

                        // 조직 별 타이틀
                        JasperPrint JasperOrgnTitleReport = utilsService.getFrontReport(searchVO, detail.getNm());
                        reportList.add(JasperOrgnTitleReport);
                        page += JasperOrgnTitleReport.getPages().size();
                        localPage += JasperOrgnTitleReport.getPages().size();

                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);

                        List<JasperPrint> orgnStatusReport = organizationStatusService.getAllReport(request, response, searchVO);
                        reportList.addAll(orgnStatusReport);
                        for (JasperPrint report : orgnStatusReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }

                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);

                        // 근로자 및 이해관계자
                        List<JasperPrint> orgnEmpReport = employeesAndStakeholdersService.getAllReport(request, response, searchVO);
                        reportList.addAll(orgnEmpReport);
                        for (JasperPrint report : orgnEmpReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                        //선택되지 않은 데이터일 경우 추가된 frontReport 제거
                        if(searchVO.getWriteYear() == null){
                            reportList.remove(reportList.size() - 1);
                        }
                    }
                    break;
                /*****************************************
                 * 법규 관리 및 준수평가
                 *****************************************/
                case "LegalMgmtAndComplianceEvaluation":
                    // 법규 관리 및 준수평가
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> legalReport = new ArrayList<>();
                        if (searchVO.getWriteYear() != null) {
                            switch (detail.getId()) {
                                case "LGM":
                                    // 법규 관리 [ tb_legal_manage ]
                                    legalReport = legalManageService.getAllReport(request, response, searchVO);
                                    break;
                                case "LGR":
                                    // 법규 검토서 [ tb_legal_review ]
                                    legalReport = legalReviewService.getAllReport(request, response, searchVO);
                                    break;
                                default:
                                    break;
                            }
                        }
                        reportList.addAll(legalReport);
                        for (JasperPrint report : legalReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * 리스크와 기회
                 *****************************************/
                case "RisksAndOpportunitiesManage":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();


                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(vo.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(vo.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }

                        // 조직 별 타이틀
                        JasperPrint JasperOrgnTitleReport = utilsService.getFrontReport(searchVO, detail.getNm());
                        reportList.add(JasperOrgnTitleReport);
                        page += JasperOrgnTitleReport.getPages().size();
                        localPage += JasperOrgnTitleReport.getPages().size();

                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);
                        List<JasperPrint> raoReport = risksAndOpportunitieService.getAllReport(request, response, searchVO);
                        reportList.addAll(raoReport);
                        for (JasperPrint report : raoReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                        //선택되지 않은 데이터일 경우 추가된 frontReport 제거
                        if(searchVO.getWriteYear() == null){
                            reportList.remove(reportList.size() - 1);
                        }
                    }
                    break;
                /*****************************************
                 * 시정조치 요구서
                 *****************************************/
                case "CorrectiveActionRequest":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(vo.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(vo.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        // 조직 별 타이틀
                        JasperPrint JasperCARTitleReport = utilsService.getFrontReport(searchVO, detail.getNm());
                        reportList.add(JasperCARTitleReport);
                        page += JasperCARTitleReport.getPages().size();
                        localPage += JasperCARTitleReport.getPages().size();

                        // 개선
                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);
                        List<JasperPrint> carReport = nonconformityCorrectiveService.getAllReport(request, response, searchVO);
                        reportList.addAll(carReport);
                        for (JasperPrint report : carReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                        // 성과평가
                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);
                        List<JasperPrint> carReport2 = evaluationCorrectiveActionRequestService.getAllReport(request, response, searchVO);
                        reportList.addAll(carReport2);
                        for (JasperPrint report : carReport2) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                        //선택되지 않은 데이터일 경우 추가된 frontReport 제거
                        if(searchVO.getWriteYear() == null){
                            reportList.remove(reportList.size() - 1);
                        }
                    }
                    break;
                /*****************************************
                 * 모니터링 및 측정 관리
                 *****************************************/
                case "MonitoringManage":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(vo.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(vo.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> erReport = monitoringManageService.getAllReport(request, response, searchVO);
                        reportList.addAll(erReport);
                        for (JasperPrint report : erReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }

                    // 법규 준수평가표 출력
                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(vo.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(vo.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }

                        List<JasperPrint> lceReport = monitoringManageService.getAllEvaluationComplianceReport(request, response, searchVO);
                        reportList.addAll(lceReport);
                        for (JasperPrint report : lceReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * 교육 훈련
                 *****************************************/
                case "EducationTraining":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();
                    SpSearchVO newSearchVO = new SpSearchVO(); // 출력하기위한 파라미터
                    newSearchVO.setWriteYear(vo.getDetailList().get(0).getWriteYear());
                    newSearchVO.setPage(page);
                    newSearchVO.setSubPage(subPage);
                    newSearchVO.setLocalPage(localPage);

                    newSearchVO.setDocType("JCA");
                    // 직무 적격성 평가서
                    List<JasperPrint> jReport = jobCompetencyAssessmentService.getAllReport(request, response, newSearchVO);
                    reportList.addAll(jReport);
                    for (JasperPrint report : jReport) {
                        page += report.getPages().size();
                        localPage += report.getPages().size();
                    }
                    newSearchVO.setPage(page);
                    newSearchVO.setLocalPage(localPage);
                    newSearchVO.setDocType("ATP");
                    // 안전보건 연간 교육 계획서
                    List<JasperPrint> aReport = annualTrainingPlanService.getAnnualReport(request, response, newSearchVO);
                    reportList.addAll(aReport);
                    for (JasperPrint report : aReport) {
                        page += report.getPages().size();
                        localPage += report.getPages().size();
                    }

                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setSearchText(detail.getId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setSearchText(detail.getId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        // 안전보건 교육실시 계획서
                        searchVO.setDocType("TPI");
                        List<JasperPrint> planReport = trainingPlanImplService.getAllReport(request, response, searchVO);
                        reportList.addAll(planReport);
                        for (JasperPrint report : planReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                        searchVO.setPage(page);
                        searchVO.setLocalPage(localPage);

                        // 안전보건 교육결과 보고서
                        searchVO.setDocType("TRR");
                        List<JasperPrint> resultReport = trainingResultReportService.getAllReport(request, response, searchVO);
                        reportList.addAll(resultReport);
                        for (JasperPrint report : resultReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * HSE 목표 및 안전보건환경 예산
                 *****************************************/
                case "HseObjectives":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();

                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        searchVO.setPrintAll(true);
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> hseReport = new ArrayList<>();
                        if (searchVO.getWriteYear() != null) {
                            switch (detail.getId()) {
                                case "PLC":
                                    // 안전보건경영방침 [ tb_hse_policy ]
                                    hseReport = hsePolicyService.getAllReport(request, response, searchVO);
                                    break;
                                case "OBJ":
                                    // 목표 및 중점추진사항 [ tb_action_master, tb_action_objective_plan ]
                                    hseReport = actPlanService.getAllReportOBJ(request, response, searchVO);
                                    break;
                                case "OBJM":
                                    // 중점추진사항별 세부계획 [ tb_action_master, tb_action_detail_plan ]
                                    hseReport = actPlanService.getAllReportOBJM(request, response, searchVO);
                                    break;
                                case "OBJB":
                                    // 안전보건환경 예산 [ tb_action_master, tb_action_detail_plan ]
                                    hseReport = actPlanService.getAllReportOBJB(request, response, searchVO);
                                    break;
                            }
                        }
                        reportList.addAll(hseReport);
                        for (JasperPrint report : hseReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                /*****************************************
                 * 안전보건목표 및 추진실적
                 *****************************************/
                case "SafetyAndHealthObjectives":
                    title = vo.getNm();
                    JasperTitleReport = utilsService.getFrontReport(titleVO, title);
                    reportList.add(JasperTitleReport);
                    page = page + JasperTitleReport.getPages().size();
                    localPage = localPage + JasperTitleReport.getPages().size();

                    for (OutputVO detail : vo.getDetailList()) {
                        SpSearchVO searchVO = new SpSearchVO(); // 출력하기위한 파라미터
                        if (vo.isCheckedPrint()) {
                            // 선택된 항목만 출력할 때
                            if (detail.isChecked()) {
                                searchVO.setWriteYear(detail.getWriteYear());
                                searchVO.setCompId(vo.getCompId());
                                searchVO.setPage(page);
                                searchVO.setSubPage(subPage);
                                searchVO.setLocalPage(localPage);
                                searchVO.setType(vo.getType());
                            }
                        } else if (detail.getPrintableCnt() > 0) {
                            // 선택안하고 출력 시 출력 가능한 항목만 출력하도록 함
                            searchVO.setWriteYear(detail.getWriteYear());
                            searchVO.setCompId(vo.getCompId());
                            searchVO.setPage(page);
                            searchVO.setSubPage(subPage);
                            searchVO.setLocalPage(localPage);
                            searchVO.setType(vo.getType());
                        }
                        List<JasperPrint> shoReport = new ArrayList<>();
                        if (searchVO.getWriteYear() != null) {
                            switch (detail.getId()) {
                                case "SHO":
                                    // 안전보건목표 [ tb_safety_health_objectives ]
                                    shoReport = safetyAndHealthObjectivesService.getAllReport(request, response, searchVO);
                                    break;
                                case "SHOP":
                                    // HSE 중점 추진실적 [ tb_safety_health_objectives ]
                                    shoReport = hseKeyPerformanceResultService.getAllReport(request, response, searchVO);
                                    break;
                            }
                        }
                        reportList.addAll(shoReport);
                        for (JasperPrint report : shoReport) {
                            page += report.getPages().size();
                            localPage += report.getPages().size();
                        }
                    }
                    break;
                default:
                    break;
            }
            subPage++;
        }
        if (voList.size() > 1) {
            // 카테고리가 하나 이상일 경우 목차 레포트 출력
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            ReportVO indexReportVO = new ReportVO();

            indexReportVO.setJrxmlPath("report/utils/basicIndex.jrxml");

            Map<String, Object> indexes = new HashMap<>();
            indexes.put("title", "목차");
            indexes.put("logo", logo);
            indexes.put("indexList", new JRBeanCollectionDataSource(indexList));
            indexReportVO.setParameter(indexes);
            JasperPrint JasperIndexReport = reportService.allReportJasperPrint(indexReportVO);
            reportList.add(1, JasperIndexReport);
        }
        String fileNm = "(" + voList.get(0).getWriteYear() + ") 안전보건정보 출력";

        reportService.exportReportAll(request, response, reportList, voList.get(0).getType(), fileNm);
    }

}
