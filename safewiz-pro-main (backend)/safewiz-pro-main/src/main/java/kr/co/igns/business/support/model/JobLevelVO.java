package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class JobLevelVO extends BaseVO {

    @Schema(description = "사업장 ID", example = "0001")
    private String compId;

    @Schema(description = "안전업무 구분 타입", example = "")
    private String jobLevelId;

    @Schema(description = "안전업무 구분 타입", example = "")
    private String jobLevelNm;

    @Schema(description = "비고", example = "")
    private String remark;

    @Schema(description = "순번", example = "")
    private Integer ordSeq;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
