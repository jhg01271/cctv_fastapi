package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class HazmatChecklistDetailVO extends BaseVO {
    @Schema(description = "점검항목 id")
    private String checklistId;
    @Schema(description = "점검항목명")
    private String checklistNm;
    @Schema(description = "점검사항 id")
    private String checklistItemId;
    @Schema(description = "점검사항명")
    private String checklistItemNm;
    @Schema(description = "점검결과")
    private String checklistResult;
    @Schema(description = "양호")
    private String acceptableYn;
    @Schema(description = "불량")
    private String nonCompliantYn;
    @Schema(description = "정비필요")
    private String requireCheckYn;
    @Schema(description = "조치")
    private String checklistAction;
}
