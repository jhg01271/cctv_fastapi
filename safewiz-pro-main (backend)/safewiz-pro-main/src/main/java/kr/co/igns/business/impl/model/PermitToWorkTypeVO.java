package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class PermitToWorkTypeVO extends BaseVO {
    @Schema(description = "점검항목 seq")
    private String workTypeId;
    @Schema(description = "점검항목명")
    private String workTypeNm;
    @Schema(description = "점검사항")
    private YesNo checkYn;
    @Schema(description = "순번")
    private int ordSeq;
    @Schema(description = "기타 - 직접입력")
    private String additionalInfo;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
