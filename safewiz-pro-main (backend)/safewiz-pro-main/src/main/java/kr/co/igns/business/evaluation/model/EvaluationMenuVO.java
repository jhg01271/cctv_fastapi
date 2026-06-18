package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EvaluationMenuVO extends BaseVO {
    @Schema(description = "평가항목 ID")
    private String evaluationId;
    @Schema(description = "이동 경로")
    private String funcPath;
    @Schema(description = "보낼 docType")
    private String sendDocType;
    @Schema(description = "HSE 업무 링크 구분")
    private String linkDiv;
    @Schema(description = "메뉴 ID")
    private String menuId;
    @Schema(description = "메뉴명")
    private String menuNm;


}
