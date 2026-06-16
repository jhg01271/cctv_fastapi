package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyWorkChecklistVO extends BaseVO {

    @Schema(description = "점검사항 ID", example = "0001")
    private String inspectionId;
    @Schema(description = "점검사항명", example = "")
    private String inspectionNm;
    @Schema(description = "점검사항 항목 ID", example = "0001")
    private String inspectionItemId;
    @Schema(description = "점검사항 항목명", example = "")
    private String inspectionItemNm;
    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "점검사항 항목", example = "[...]")
    private List<SafetyWorkChecklistVO> itemList;
}
