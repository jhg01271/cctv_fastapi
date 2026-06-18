package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class LegalComplianceEvaluationVO extends BaseVO {
    @Schema(description = "평가일자")
    private String legalEvaluationDt;
    @Schema(description = "평가항목명")
    private String legalEvaluationNm;
    @Schema(description = "평가사항")
    private String contents;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "평가항목 수")
    private int complianceCnt;
    @Schema(description = "위반 수")
    private int legalReviewNotCompliedCnt;
    @Schema(description = "미평가 수")
    private int legalReviewNotEvaluatedCnt;
}
