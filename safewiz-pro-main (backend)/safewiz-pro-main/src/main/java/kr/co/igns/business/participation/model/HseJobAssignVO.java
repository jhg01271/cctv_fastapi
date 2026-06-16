package kr.co.igns.business.participation.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class HseJobAssignVO extends BaseVO {
    @Schema(description = "담당자")
    private String hrId;
    @Schema(description = "담당자")
    private String userId;
    @Schema(description = "담당자명")
    private String hrNm;
    @Schema(description = "업무대행자")
    private String backupHrId;
    @Schema(description = "업무대행자명")
    private String backupHrNm;
    @Schema(description = "담당업무")
    private String assignTask;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "조직")
    private String orgnId;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "직위")
    private String jbrpId;
    @Schema(description = "직위명")
    private String jbrpNm;
    @Schema(description = "미완료 수")
    private int notCompletedCnt;
}
