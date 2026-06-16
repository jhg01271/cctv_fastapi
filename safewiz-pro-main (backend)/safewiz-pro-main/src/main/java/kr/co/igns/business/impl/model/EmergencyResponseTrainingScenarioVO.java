package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EmergencyResponseTrainingScenarioVO extends BaseVO {

    @Schema(description = "목표시간(분)", example = "14")
    private int targetTime;

    @Schema(description = "상황(경과)", example = "대피")
    private String situation;

    @Schema(description = "임무수행", example = "대피안내")
    private String performer;

    @Schema(description = "행동요령", example = "계단이용")
    private String methodAction;

    @Schema(description = "비고", example = "")
    private String remark;
}
