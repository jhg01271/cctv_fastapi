package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class JobMgmtVO extends BaseVO {

    @Schema(description = "사업장 ID", example = "0001")
    private String compId;

    @Schema(description = "직무 코드", example = "0001")
    private String jobId;

    @Schema(description = "직무분야", example = "")
    private String jobCategory;

    @Schema(description = "직무 이름", example = "안전보건관리책임자")
    private String jobNm;

    @Schema(description = "비고", example = "")
    private String remark;

    @Schema(description = "순번", example = "")
    private Integer ordSeq;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
