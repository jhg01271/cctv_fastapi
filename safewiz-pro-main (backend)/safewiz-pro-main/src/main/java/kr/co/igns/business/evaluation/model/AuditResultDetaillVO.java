package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AuditResultDetaillVO extends BaseVO {

    @Schema(description = "상세 번호")
    private String docSeqDetail;

    @Schema(description = "대상업무")
    private String targetJob;

    @Schema(description = "시정조치 여부")
    private String actionYn;

    @Schema(description = "심사 결과 및 결론")
    private String result;

    @Schema(description = "비고")
    private String remark;

    @Schema(description = "사용여부", example = "Y")
    private String useYn;
}
