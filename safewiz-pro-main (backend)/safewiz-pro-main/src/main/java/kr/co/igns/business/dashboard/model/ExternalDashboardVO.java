package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ExternalDashboardVO {

    @Data
    @Builder
    @AllArgsConstructor
    public static class comp {
        @Schema(description = "사업장 ID", example = "202408200001")
        private String compId;

        @Schema(description = "사업장 명", example = "(주)일주지앤에스")
        private String compNm;

        @Schema(description = "우편번호", example = "28589")
        private String zipCd;

        @Schema(description = "주소", example = "충북 청주시 흥덕구 직지대로456번길 115")
        private String addrs1;

        @Schema(description = "상세주소", example = "(복대동)")
        private String addrs2;

        @Schema(description = "전화번호", example = "043-262-1944")
        private String telNo;

        @Schema(description = "업종 코드", example = "22299")
        private String bizCd;

        @Schema(description = "업종 명", example = "그 외 기타 플라스틱 제품 제조업")
        private String bizNm;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class dashboard {
        @Schema(description = "위험성평가 조직도")
        private List<DashboardRiskAssessmentStatusVO> riskAssessmentOrgn;

        @Schema(description = "안전보건정보 조사")
        private List<DashboardRiskAssessmentStatusVO> safetyAndHealthInfoSurvey;

        @Schema(description = "공정/설비/물질 구분")
        private List<DashboardRiskAssessmentStatusVO> classProcEquipMsds;

        @Schema(description = "유해위험요인 분류")
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

    }
}
