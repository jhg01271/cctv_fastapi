package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardRiskAssessmentStatusVO {

    @Schema(description = "등록(완료)", example = "10")
    private int registeredCnt;

    @Schema(description = "미등록(진행)", example = "10")
    private int unregisteredCnt;
}
