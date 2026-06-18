package kr.co.igns.business.dataset.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TypeofEquipmentVO extends BaseVO {

    @Schema(description = "사업장 ID", example = "")
    private String compId;

    @Schema(description = "설비유형 ID", example = "")
    private String stdEqId;

    @Schema(description = "설비유형 한글명", example = "")
    private String stdEqNm;

    @Schema(description = "설비유형 영문명", example = "")
    private String stdEqEngNm;

    @Schema(description = "설비유형 설명", example = "")
    private String stdEqDesc;

    @Schema(description = "설비유형 설명", example = "")
    private String remark;

    @Schema(description = "설비유형 설명", example = "")
    private String desc;

    @Schema(description = "설비유형 순번", example = "")
    private int ordSeq;

    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

}
