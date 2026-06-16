package kr.co.igns.business.dashboard.dao.postgres;

import kr.co.igns.business.dashboard.model.*;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DashboardDAO {
    int getLegalManageDashboard(SpSearchVO searchVo);
    int getLegalReviewDashboard(SpSearchVO searchVo);

    int getRiskDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getRiskPerPresentDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getRiskPerMasureDashboard(SpSearchVO searchVo);
    int getOppDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getOppPerDashboard(SpSearchVO searchVo);

    int getCorrectiveActionRequestDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getCorrectiveActionRequestPerDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getCorrectiveActionRequestOrgnDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getMonitoringActionRequestOrgnDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getOhsActionRequestOrgnDashboard(SpSearchVO searchVo);
    
    List<DashboardPerVO> getMonitoringActionRequestPerDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getOhsActionRequestPerDashboard(SpSearchVO searchVo);

    int getEvaluationDetailDashboard(SpSearchVO searchVo);
    List<DashboardPerVO> getEvaluationDetailPerDashboard(SpSearchVO searchVo);

    List<DashboardByOrgnVO> getAuditExecutionDashboard(SpSearchVO searchVo);

    List<DashboardRiskAssessmentVO> getRiskAssessmentDashboard(SpSearchVO searchVo);

    List<DashboardByOrgnVO> getNearMissDashboard(SpSearchVO searchVo);

    int getHsePolicyDashboard(SpSearchVO searchVo);
    int getSafetyAndHealthObjectivesDashboard(SpSearchVO searchVo);
    int getBudgetDashboard(SpSearchVO searchVo);
    int getPerformanceDashboard(SpSearchVO searchVo);
    List<DashboardHseKeyPerformanceVO> getHseKeyPerformanceDashboard(SpSearchVO searchVo);

    List<DashboardTBMVO> getToolBoxMeetingDashboard(SpSearchVO searchVo);
    int getAnnualTrainingPlanDashboard(SpSearchVO searchVo);
    List<DashboardTrainingVO> getAnnualTrainingHrDashboard(SpSearchVO searchVo);
    int getSafetyChecklistDashboard(SpSearchVO searchVo);
    List<DashboardSafetyInspectionVO> getSafetyInspectionDashboard(SpSearchVO searchVo);

    List<DashboardRiskAssessmentStatusVO> getRiskAssessmentOrgnDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getSafetyAndHealthInfoSurveyDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getClassProcEquipMsdsDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getClassHazardsDashboard(SpSearchVO searchVo);

    List<DashboardRiskAssessmentStatusVO> getRiskAssessmentPlanDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getRiskAssessmentReductionDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getRiskAssessmentImplementationDashboard(SpSearchVO searchVo);
    List<DashboardRiskAssessmentStatusVO> getRiskAssessmentCompleteDashboard(SpSearchVO searchVo);

    List<DashboardRiskAssessmentFactorVO> getRiskAssessmentFactorDashboard(SpSearchVO searchVo);

    // 청주산단 통합관제 모니터링 대시보드 연동 API
    List<ExternalDashboardVO.comp> getCompDashboard();
    boolean getCompValidation(String compId);
}
