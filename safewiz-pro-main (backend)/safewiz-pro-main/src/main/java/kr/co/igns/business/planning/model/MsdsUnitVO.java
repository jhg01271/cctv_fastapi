package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class MsdsUnitVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
    @Schema(description = "MSDS 단위 ID", example = "MSDS#1")
    private String unitId;
    @Schema(description = "MSDS 단위 명", example = "MSDS#1")
    private String unitNm;
    @Schema(description = "설명")
    private String unitDesc;
    @Schema(description = "정렬순서", example = "99")
    private int ordSeq;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;
    
    @Schema(description = "msds 단위 id")
    private String id;
    @Schema(description = "msds 단위")
    private String nm;

}
