package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EvaluationChecklistVO extends BaseVO {
    @Schema(description = "평가항목 ID", example = "0001")
    private String evaluationId;
    @Schema(description = "평가항목명", example = "")
    private String evaluationNm;
    @Schema(description = "평가사항 ID", example = "0001")
    private String evaluationItemId;
    @Schema(description = "평가사항명", example = "")
    private String evaluationItemNm;
    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "평가항목 사항 리스트", example = "[...]")
    private List<EvaluationChecklistVO> itemList;

    private List<EvaluationChecklistVO> detailList;
}
