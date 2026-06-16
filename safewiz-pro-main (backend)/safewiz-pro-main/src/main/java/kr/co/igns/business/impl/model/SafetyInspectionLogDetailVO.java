package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyInspectionLogDetailVO extends BaseVO {
    @Schema(description = "점검항목명")
    private String checkItem;
    @Schema(description = "점검사항")
    private String checkList;
    @Schema(description = "양호")
    private String acceptableYn;
    @Schema(description = "불량")
    private String nonCompliantYn;
    @Schema(description = "20250611")
    private String checkDt;
    @Schema(description = "순서")
    private int ordSeq;
    @Schema(description = "아이템 순서")
    private int itemordseq;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
