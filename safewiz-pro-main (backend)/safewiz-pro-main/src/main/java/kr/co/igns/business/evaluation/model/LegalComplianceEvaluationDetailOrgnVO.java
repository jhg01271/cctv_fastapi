package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class LegalComplianceEvaluationDetailOrgnVO extends BaseVO {
    @Schema(description = "조직 ID", example = "")
    private String orgnId;

    @Schema(description = "조직명", example = "")
    private String orgnNm;
}
