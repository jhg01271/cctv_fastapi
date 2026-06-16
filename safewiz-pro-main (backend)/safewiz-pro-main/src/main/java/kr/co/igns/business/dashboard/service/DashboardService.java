package kr.co.igns.business.dashboard.service;

import kr.co.igns.business.dashboard.dao.postgres.DashboardDAO;
import kr.co.igns.business.dashboard.model.ExternalDashboardVO;
import kr.co.igns.business.dashboard.model.DashboardVO;
import kr.co.igns.business.task.service.TaskService;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardDAO dashboardDao;
    private final TaskService taskService;

    public DashboardVO getDashboard(SpSearchVO searchVo) throws Exception {
        if (searchVo.getSearchText2() == null || searchVo.getSearchText2().equals("")) {
            //기간(일자) default 설정  - 7일
            searchVo.setSearchText2("7");
        }

        DashboardVO dashboardVO = new DashboardVO();

        //region 법규 관리/검토
        //등록된 법규 관리 카드 수
        dashboardVO.setLegalManageCnt(dashboardDao.getLegalManageDashboard(searchVo));
        //등록된 법규검토서 카드 수
        dashboardVO.setLegalReviewCnt(dashboardDao.getLegalReviewDashboard(searchVo));
        //endregion

        //region 리스크와 기회
        //리스크
        dashboardVO.setRiskCnt(dashboardDao.getRiskDashboard(searchVo));
        dashboardVO.setRiskPresentPer(dashboardDao.getRiskPerPresentDashboard(searchVo));
        dashboardVO.setRiskmMasurePer(dashboardDao.getRiskPerMasureDashboard(searchVo));
        //기회
        dashboardVO.setOpportunityCnt(dashboardDao.getOppDashboard(searchVo));
        dashboardVO.setOpportunityPer(dashboardDao.getOppPerDashboard(searchVo));
        //endregion

        //region 시정 조치/재발 방지
        //시정 조치/재발 방지 등록 건수
        dashboardVO.setCorrectiveActionRequestCnt(dashboardDao.getCorrectiveActionRequestDashboard(searchVo));
        //부적합 - 조치율
        dashboardVO.setCorrectiveActionRequestPer(dashboardDao.getCorrectiveActionRequestPerDashboard(searchVo));
        //시정 조치/재발 방지 - 조치조직별
        dashboardVO.setCorrectiveActionRequestOrgn(dashboardDao.getCorrectiveActionRequestOrgnDashboard(searchVo));
        //모니터링 - 조치율
        dashboardVO.setMonitoringActionRequestPerDashboard(dashboardDao.getMonitoringActionRequestPerDashboard(searchVo));
        //내부심사 - 조치율
        dashboardVO.setOhsActionRequestPerDashboard(dashboardDao.getOhsActionRequestPerDashboard(searchVo));
        
        dashboardVO.setMonitoringActionRequestOrgn(dashboardDao.getMonitoringActionRequestOrgnDashboard(searchVo));
        
        dashboardVO.setOhsActionRequestOrgn(dashboardDao.getOhsActionRequestOrgnDashboard(searchVo));
        
        
        //endregion

        //region 지속적 모니터링
        //모니터링, 성과측정 및 평가 결과서 항목 수
        dashboardVO.setEvaluationDetailCnt(dashboardDao.getEvaluationDetailDashboard(searchVo));
        //모니터링, 성과측정 및 평가 결과서 항목별 달성률
        dashboardVO.setEvaluationDetailPer(dashboardDao.getEvaluationDetailPerDashboard(searchVo));
        //endregion

        //region Task 현황
        //월별 Task 현황
        dashboardVO.setMonthlyTaskPer(taskService.getTaskDashboard(searchVo));
        //월별 My Task 현황
        searchVo.setSearchText3(searchVo.getCreatedBy());
        dashboardVO.setMonthlyMyTaskPer(taskService.getTaskDashboard(searchVo));

        //조직별 내부심사
        dashboardVO.setAuditExecutionPer(dashboardDao.getAuditExecutionDashboard(searchVo));

        //조직별 위험성평가
        dashboardVO.setRiskAssessment(dashboardDao.getRiskAssessmentDashboard(searchVo));

        //아차사고
        dashboardVO.setNearMissPer(dashboardDao.getNearMissDashboard(searchVo));

        //endregion

        //region HSE 목표
        //안전보건경영 방침
        dashboardVO.setHsePolicyCnt(dashboardDao.getHsePolicyDashboard(searchVo));
        //안전보건 목표 수
        dashboardVO.setSafetyAndHealthObjectivesCnt(dashboardDao.getSafetyAndHealthObjectivesDashboard(searchVo));
        //추진 실적 현황
        dashboardVO.setHseKeyPerformance(dashboardDao.getHseKeyPerformanceDashboard(searchVo));
        //예산
        dashboardVO.setBudget(dashboardDao.getBudgetDashboard(searchVo));
        //실적
        dashboardVO.setPerformance(dashboardDao.getPerformanceDashboard(searchVo));
        //endregion

        //region TBM/교육/안전점검
        //TBM 현황
        dashboardVO.setToolBoxMeeting(dashboardDao.getToolBoxMeetingDashboard(searchVo));

        //교육 현황
        dashboardVO.setAnnualTrainingPlanCnt(dashboardDao.getAnnualTrainingPlanDashboard(searchVo));
        dashboardVO.setAnnualTrainingHr(dashboardDao.getAnnualTrainingHrDashboard(searchVo));

        //안전점검 현황
        dashboardVO.setSafetyChecklistCnt(dashboardDao.getSafetyChecklistDashboard(searchVo));
        dashboardVO.setSafetyInspection(dashboardDao.getSafetyInspectionDashboard(searchVo));
        //endregion

        //region 위험성평가
        //위험성평가 조직도
        dashboardVO.setRiskAssessmentOrgn(dashboardDao.getRiskAssessmentOrgnDashboard(searchVo));
        //안전보건정보 조사
        dashboardVO.setSafetyAndHealthInfoSurvey(dashboardDao.getSafetyAndHealthInfoSurveyDashboard(searchVo));
        //공정/설비/물질 구분
        dashboardVO.setClassProcEquipMsds(dashboardDao.getClassProcEquipMsdsDashboard(searchVo));
        //유해위험요인 분류
        dashboardVO.setClassHazards(dashboardDao.getClassHazardsDashboard(searchVo));
        //위험성평가 계획
        dashboardVO.setRiskAssessmentPlan(dashboardDao.getRiskAssessmentPlanDashboard(searchVo));
        //감소대책 등록
        dashboardVO.setRiskAssessmentReduction(dashboardDao.getRiskAssessmentReductionDashboard(searchVo));
        //감소대책 이행
        dashboardVO.setRiskAssessmentImplementation(dashboardDao.getRiskAssessmentImplementationDashboard(searchVo));
        //위험성평가 완료
        dashboardVO.setRiskAssessmentComplete(dashboardDao.getRiskAssessmentCompleteDashboard(searchVo));

        //유해위험요인 별 현황
        dashboardVO.setRiskAssessmentFactor(dashboardDao.getRiskAssessmentFactorDashboard(searchVo));
        //endregion

        return dashboardVO;
    }

    // 청주산단 통합관제 모니터링 대시보드 연동 API
    public List<ExternalDashboardVO.comp> getCompDashboard() throws Exception {
        return dashboardDao.getCompDashboard();
    }

    public boolean isValidYear(String writeYear) {
        if (writeYear == null || writeYear.length() != 4) return false;
        if (!writeYear.matches("\\d{4}")) return false;

        int year = Integer.parseInt(writeYear);
        int currentYear = LocalDate.now().getYear();
        return year >= 1900 && year <= currentYear + 100;  // 예: 1900년 ~ +100년까지 허용
    }

    public boolean isValidComp(String compId) throws Exception {
        boolean existComp = dashboardDao.getCompValidation(compId);
        return existComp;
    }


    public ExternalDashboardVO.dashboard getDashboardExternal(SpSearchVO searchVo) throws Exception {
        ExternalDashboardVO.dashboard result = new ExternalDashboardVO.dashboard();
        // 위험성평가 조직도
        result.setRiskAssessmentOrgn(dashboardDao.getRiskAssessmentOrgnDashboard(searchVo));

        //안전보건정보 조사
        result.setSafetyAndHealthInfoSurvey(dashboardDao.getSafetyAndHealthInfoSurveyDashboard(searchVo));

        //공정/설비/물질 구분
        result.setClassProcEquipMsds(dashboardDao.getClassProcEquipMsdsDashboard(searchVo));

        //유해위험요인 분류
        result.setClassHazards(dashboardDao.getClassHazardsDashboard(searchVo));

        //위험성평가 계획
        result.setRiskAssessmentPlan(dashboardDao.getRiskAssessmentPlanDashboard(searchVo));

        //감소대책 등록
        result.setRiskAssessmentReduction(dashboardDao.getRiskAssessmentReductionDashboard(searchVo));

        //감소대책 이행
        result.setRiskAssessmentImplementation(dashboardDao.getRiskAssessmentImplementationDashboard(searchVo));

        //위험성평가 완료
        result.setRiskAssessmentComplete(dashboardDao.getRiskAssessmentCompleteDashboard(searchVo));

        //유해위험요인 별 현황
        result.setRiskAssessmentFactor(dashboardDao.getRiskAssessmentFactorDashboard(searchVo));

        //TBM 현황
        result.setToolBoxMeeting(dashboardDao.getToolBoxMeetingDashboard(searchVo));

        //교육 현황
        result.setAnnualTrainingPlanCnt(dashboardDao.getAnnualTrainingPlanDashboard(searchVo));
        result.setAnnualTrainingHr(dashboardDao.getAnnualTrainingHrDashboard(searchVo));

        //안전점검 현황
        result.setSafetyChecklistCnt(dashboardDao.getSafetyChecklistDashboard(searchVo));
        result.setSafetyInspection(dashboardDao.getSafetyInspectionDashboard(searchVo));

        return result;
    }
}
