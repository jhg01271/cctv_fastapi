package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardHseKeyPerformanceVO {
    @Schema(description = "명칭", example = "A")
    private String safetyHealthGoal;

    @Schema(description = "조직명", example = "A")
    private String orgnNm;

    @Schema(description = "실적 갯수", example = "10")
    private int performanceCnt;

    @Schema(description = "목표율", example = "10")
    private int objectivePer;

    @Schema(description = "추진율", example = "10")
    private int per;
}
