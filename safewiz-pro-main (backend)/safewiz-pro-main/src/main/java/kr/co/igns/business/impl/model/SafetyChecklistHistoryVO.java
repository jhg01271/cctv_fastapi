package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyChecklistHistoryVO extends BaseVO {
    @Schema(description = "점검자")
    private String hrNm;
    @Schema(description = "주기명")
    private String inspectionCycleNm;
    
}
