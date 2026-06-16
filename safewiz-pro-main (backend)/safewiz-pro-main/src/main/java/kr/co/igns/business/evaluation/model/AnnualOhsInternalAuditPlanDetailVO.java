package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AnnualOhsInternalAuditPlanDetailVO extends BaseVO {

    @Schema(description = "비고 월", example = "11")
    private int month;

    @Schema(description = "비고", example = "")
    private String remark;
}
