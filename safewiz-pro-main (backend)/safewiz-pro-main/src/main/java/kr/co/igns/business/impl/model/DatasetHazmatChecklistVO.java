package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class DatasetHazmatChecklistVO extends BaseVO {

    @Schema(description = "점검사항 ID", example = "0001")
    private String checklistId;
    @Schema(description = "점검사항명", example = "")
    private String checklistNm;
    @Schema(description = "점검사항 항목 ID", example = "0001")
    private String checklistItemId;
    @Schema(description = "점검사항 항목명", example = "")
    private String checklistItemNm;
    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "점검사항 항목", example = "[...]")
    private List<DatasetHazmatChecklistVO> itemList;
}
