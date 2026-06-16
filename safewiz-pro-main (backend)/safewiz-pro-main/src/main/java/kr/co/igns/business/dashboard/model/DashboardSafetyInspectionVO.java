package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardSafetyInspectionVO {
    @Schema(description = "점검 일자", example = "202412")
    private String checkMonth;

    @Schema(description = "양호 갯수", example = "10")
    private int acceptableCnt;

    @Schema(description = "불량 갯수", example = "10")
    private int nonCompliantCnt;
}
