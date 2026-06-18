package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardByOrgnVO {
    @Schema(description = "조직명", example = "A")
    private String orgnNm;

    @Schema(description = "완료", example = "10")
    private int complete;

    @Schema(description = "진행", example = "10")
    private int progress;

    @Schema(description = "대기", example = "10")
    private int waiting;
}
