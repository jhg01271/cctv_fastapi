package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyEquipmentVO extends BaseVO {

    @Schema(description = "안전기구 ID", example = "0001")
    private String safetyEqId;
    @Schema(description = "안전기구명", example = "")
    private String safetyEqNm;
    @Schema(description = "안전기구 항목 ID", example = "0001")
    private String safetyEqItemId;
    @Schema(description = "안전기구 항목명", example = "")
    private String safetyEqItemNm;
    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;
    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "안전기구 항목", example = "[...]")
    private List<SafetyEquipmentVO> itemList;
}
