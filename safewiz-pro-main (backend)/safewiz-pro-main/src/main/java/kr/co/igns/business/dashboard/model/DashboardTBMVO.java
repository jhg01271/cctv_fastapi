package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardTBMVO {
    @Schema(description = "TBM 일자", example = "20241212")
    private String tbmDate;

    @Schema(description = "계획 건수", example = "10")
    private int countPerDay;

    @Schema(description = "완료 건수", example = "10")
    private int completePerDay;
}
