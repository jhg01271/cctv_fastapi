package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyChecklistDetailVO extends BaseVO {
    @Schema(description = "점검항목 seq")
    private String checkItemSeq;
    @Schema(description = "점검항목명")
    private String checkItemSeqNm;
    @Schema(description = "점검사항")
    private String checkList;
    @Schema(description = "순서")
    private int ordSeq;
    @Schema(description = "사용 여부", example = "Y")
    private String useYn;
    
    @Schema(description = "점검명")
    private String checkItem;
    @Schema(description = "점검순서")
    private int itemOrdSeq;
    @Schema(description = "점검사항 사용여부")
    private String itemUseYn;
    
}
