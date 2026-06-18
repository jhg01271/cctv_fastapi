package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardTrainingVO {
    @Schema(description = "교육 일자", example = "202412")
    private String trainingMonth;

    @Schema(description = "계획 인원", example = "10")
    private int planHr;

    @Schema(description = "교육 인원", example = "10")
    private int completeHr;
}
