package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class HrJbrpVO extends BaseVO {
    @Schema(description = "직무 ID")
    private String jbrpId;
    @Schema(description = "직무 명")
    private String jbrpNm;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "정렬순서")
    private Integer ordSeq = 99;
    @Schema(description = "선택여부")
    private boolean checked;
}
