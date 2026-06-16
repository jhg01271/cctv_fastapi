package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardRiskAssessmentFactorVO {

    @Schema(description = "유해위험요인 카테고리 코드", example = "factor_1")
    private String hazardsType;

    @Schema(description = "유해위험요인 카테고리명", example = "작업환경 요인")
    private String hazardsTypeNm;

    @Schema(description = "카테고리별 유해위험요인 갯수", example = "10")
    private int hazardsCnt;
}
