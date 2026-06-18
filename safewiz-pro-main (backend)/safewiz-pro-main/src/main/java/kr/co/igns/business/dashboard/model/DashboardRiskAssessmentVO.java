package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardRiskAssessmentVO {
    @Schema(description = "조직명", example = "A")
    private String orgnNm;

    @Schema(description = "유해위험요인 등록현황", example = "10")
    private int riskRegCnt;

    @Schema(description = "감소대책 등록현황(작성된 감소대책 건수)", example = "10")
    private int reductRegCnt;

    @Schema(description = "감소대책 조치현황(개선조치가 작성된 감소대책 건수)", example = "10")
    private int reductActCnt1;

    @Schema(description = "감소대책 조치현황(작성된 감소대책 건수)", example = "10")
    private int reductActCnt2;
}
