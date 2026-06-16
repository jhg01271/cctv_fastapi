package kr.co.igns.business.dashboard.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DashboardPerVO {
    @Schema(description = "명칭", example = "A")
    private String name;

    @Schema(description = "갯수", example = "10")
    private int cnt;
}
