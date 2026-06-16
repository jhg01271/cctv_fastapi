package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyChecklistHrVO extends BaseVO {
    @Schema(description = "user id")
    private String userId;
    @Schema(description = "설비 유형 id")
    private String stdEqId;
    @Schema(description = "설비 id")
    private String equipmentId;
    @Schema(description = "설비명")
    private String equipmentNm;

    // 담당자 Mapping
    @Schema(description = "담당자")
    private List<HrVO> hrList;

}
