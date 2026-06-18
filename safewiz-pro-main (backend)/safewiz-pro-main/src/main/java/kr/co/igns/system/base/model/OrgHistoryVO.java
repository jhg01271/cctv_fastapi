package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class OrgHistoryVO extends BaseVO {
    @Schema(description = "인원 ID")
    private String hrId;
    @Schema(description = "조직 ID")
    private String orgnId;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "조직 변경 시작일")
    private String stDate;
    @Schema(description = "조직 변경 종료일")
    private String edDate;

}
