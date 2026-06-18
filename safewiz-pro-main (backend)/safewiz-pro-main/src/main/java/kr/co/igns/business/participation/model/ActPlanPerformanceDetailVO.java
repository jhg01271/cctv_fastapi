package kr.co.igns.business.participation.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ActPlanPerformanceDetailVO extends BaseVO {
    private String docPerformanceSeq;
    private String docPerformanceDetailSeq;

    @Schema(description = "내용")
    private String contents;
    @Schema(description = "실적금액(합계)")
    private int performanceAmount;
    @Schema(description = "순번")
    private int ordSeq;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
}
