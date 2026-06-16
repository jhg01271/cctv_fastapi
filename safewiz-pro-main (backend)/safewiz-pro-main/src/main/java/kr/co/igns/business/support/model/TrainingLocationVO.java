package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TrainingLocationVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "")
    private String compId;

    @Schema(description = "교육장소 ID", example = "")
    private String locationId;

    @Schema(description = "교육장소명", example = "")
    private String locationNm;

    @Schema(description = "순번", example = "N")
    private String remark;

    @Schema(description = "정렬 순서", example = "99")
    private Integer ordSeq;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
