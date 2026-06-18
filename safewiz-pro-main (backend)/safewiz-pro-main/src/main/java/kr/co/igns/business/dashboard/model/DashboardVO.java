package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DashboardVO {

    //법규 관리/검토
    @Schema(description = "법규 관리 카드 수", example = "1")
    private int legalManageCnt;

    @Schema(description = "법규검토서 카드 수", example = "1")
    private int legalReviewCnt;

    //리스크와 기회
    @Schema(description = "리스크 등록 건수", example = "1")
    private int riskCnt;

    @Schema(description = "기회 등록 건수", example = "1")
    private int opportunityCnt;

    @Schema(description = "리스크 등급 백분율 - 현재")
    private List<DashboardPerVO> riskPresentPer;
    @Schema(description = "리스크 등급 백분율 - 조치 후")
    private List<DashboardPerVO> riskmMasurePer;

    @Schema(description = "기회 백분율")
    private List<DashboardPerVO> opportunityPer;

    //시정 조치/재발 방지
    @Schema(description = "시정 조치/재발 방지 등록 건수", example = "1")
    private int correctiveActionRequestCnt;

    @Schema(description = "부적합 - 조치율")
    private List<DashboardPerVO> correctiveActionRequestPer;
    @Schema(description = "모니터링 - 조치율")
    private List<DashboardPerVO> monitoringActionRequestPerDashboard;
    @Schema(description = "내부심사 - 조치율")
    private List<DashboardPerVO> ohsActionRequestPerDashboard;
    @Schema(description = "시정 조치/재발 방지 - 조치조직 백분율")
    private List<DashboardPerVO> monitoringActionRequestOrgn;
    @Schema(description = "시정 조치/재발 방지 - 조치조직 백분율")
    private List<DashboardPerVO> correctiveActionRequestOrgn;
    @Schema(description = "시정 조치/재발 방지 - 조치조직 백분율")
    private List<DashboardPerVO> ohsActionRequestOrgn;

    //지속적 모니터링
    @Schema(description = "모니터링, 성과측정 및 평가 결과서 항목 수", example = "1")
    private int evaluationDetailCnt;

    @Schema(description = "모니터링, 성과측정 및 평가 결과서 항목별 달성률")
    private List<DashboardPerVO> evaluationDetailPer;

    //Task 현황
    @Schema(description = "월별 Task 현황")
    private List<DashboardPerVO> monthlyTaskPer;
    @Schema(description = "월별 My Task 현황")
    private List<DashboardPerVO> monthlyMyTaskPer;

    @Schema(description = "내부심사")
    private List<DashboardByOrgnVO> auditExecutionPer;

    @Schema(description = "위험성평가")
    private List<DashboardRiskAssessmentVO> riskAssessment;

    @Schema(description = "아차사고")
    private List<DashboardByOrgnVO> nearMissPer;

    //HSE 목표
    @Schema(description = "안전보건경영 방침", example = "1")
    private int hsePolicyCnt;

    @Schema(description = "안전보건 목표 수", example = "1")
    private int safetyAndHealthObjectivesCnt;
    @Schema(description = "예산", example = "100000")
    private int budget;
    @Schema(description = "실적", example = "100000")
    private int performance;
    @Schema(description = "월별 Task 현황")
    private List<DashboardHseKeyPerformanceVO> hseKeyPerformance;

    //TBM/교육/안전점검
    @Schema(description = "TBM 현황")
    private List<DashboardTBMVO> toolBoxMeeting;

    @Schema(description = "교육 현황 - 안전보건 연간 교육 계획 건수")
    private int annualTrainingPlanCnt;
    @Schema(description = "교육 현황 - 안전보건 연간 교육 인원")
    private List<DashboardTrainingVO> annualTrainingHr;

    @Schema(description = "안전점검표 등록 건수")
    private int safetyChecklistCnt;
    @Schema(description = "설비 안전점검")
    private List<DashboardSafetyInspectionVO> safetyInspection;

    //위험성평가
    @Schema(description = "위험성평가 조직도")
    private List<DashboardRiskAssessmentStatusVO> riskAssessmentOrgn;

    @Schema(description = "안전보건정보 조사")
    private List<DashboardRiskAssessmentStatusVO> safetyAndHealthInfoSurvey;

    @Schema(description = "공정/설비/물질 구분")
    private List<DashboardRiskAssessmentStatusVO> classProcEquipMsds;

    @Schema(description = "유해화학요인 분류")
    private List<DashboardRiskAssessmentStatusVO> classHazards;

    @Schema(description = "위험성평가 계획")
    private List<DashboardRiskAssessmentStatusVO> riskAssessmentPlan;

    @Schema(description = "감소대책 등록")
    private List<DashboardRiskAssessmentStatusVO> riskAssessmentReduction;

    @Schema(description = "감소대책 이행")
    private List<DashboardRiskAssessmentStatusVO> riskAssessmentImplementation;

    @Schema(description = "위험성평가 완료")
    private List<DashboardRiskAssessmentStatusVO> riskAssessmentComplete;

    @Schema(description = "유해위험요인 별 현황")
    private List<DashboardRiskAssessmentFactorVO> riskAssessmentFactor;

}
