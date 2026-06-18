package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class PrcsCnfmVO extends BaseVO {
    @Schema(description = "공정 확정 id")
    private int prcsCnfmId;
    @Schema(description = "차수")
    private String revNo;
    @Schema(description = "차수명")
    private String revNm;
    @Schema(description = "공정 id")
    private String processId;
    @Schema(description = "확정일시")
    private String cnfmAt;
    @Schema(description = "확정여부")
    private YesNo cnfmYn;
    @Schema(description = "사용유무")
    private YesNo useYn;
    @Schema(description = "확정자")
    private String cnfmBy;
}
