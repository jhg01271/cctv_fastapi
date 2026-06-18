package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EvaluationReportComplianceVO extends BaseVO {
    @Schema(description = "평가항목 id")
    private String evaluationId;
    @Schema(description = "평가항목명")
    private String evaluationNm;
    @Schema(description = "평가(적합 : Y, 위반 : N)")
    private YesNo evaluationYn;
    @Schema(description = "평가사항")
    private String contents;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
