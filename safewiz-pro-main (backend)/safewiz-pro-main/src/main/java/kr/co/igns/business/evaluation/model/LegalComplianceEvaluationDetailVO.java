package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class LegalComplianceEvaluationDetailVO extends BaseVO {
    @Schema(description = "법규 검토서 년도")
    private String reviewWriteYear;
    @Schema(description = "법규 검토서 타입")
    private String reviewDocType;
    @Schema(description = "법규 검토서 ID")
    private String reviewDocNo;
    @Schema(description = "법규 검토서 시퀀스")
    private String reviewDocSeq;
    @Schema(description = "법규 검토서명")
    private String legalReviewNm;
    @Schema(description = "법규 검토서 법규명")
    private String legalNm;
    @Schema(description = "법규 검토서 법규 조항명")
    private String legalArticleNm;
    @Schema(description = "평가(적합 : Y, 위반 : N)")
    private YesNo legalReviewYn;
    @Schema(description = "평가사항")
    private String contents;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "조직")
    private List<LegalComplianceEvaluationDetailOrgnVO> legalComplianceDetailOrgnList;
}
