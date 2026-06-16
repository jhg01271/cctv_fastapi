package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class PermitToWorkDetailVO extends BaseVO {
    @Schema(description = "안전작업 점검사항 ID")
    private String inspectionId;
    @Schema(description = "안전작업 점검사항 항목")
    private String inspectionItem;
    @Schema(description = "안전작업 점검사항 항목명")
    private String inspectionItemNm;
    @Schema(description = "양호")
    private String acceptableYn;
    @Schema(description = "불량")
    private String nonCompliantYn;
    @Schema(description = "허용불가")
    private String unacceptableYn;
    @Schema(description = "번호")
    private int ordSeq;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    private String detailCmd = "U";

    //안전기구 매핑
    @Schema(description = "안전기구 리스트")
    private List<SafetyEquipmentVO> safetyEqList;
}
