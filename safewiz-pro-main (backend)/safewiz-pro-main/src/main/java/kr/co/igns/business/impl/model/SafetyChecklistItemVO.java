package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyChecklistItemVO extends BaseVO {
    @Schema(description = "점검항목명")
    private String checkItem;
    @Schema(description = "순서")
    private int ordSeq;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

}
