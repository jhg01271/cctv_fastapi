package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class LegalMgmtAndComplianceEvaluationVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;

    @Schema(description = "카드 제목", example = "법규 관리 및 준수평가")
    private String title;

    @Schema(description = "카드 메뉴 라우터 경로", example = "LegalManage")
    private String route;

    @Schema(description = "카드 활성화 상태 값", example = "Y")
    private YesNo activeYn;

    @Schema(description = "설명", example = "법규 관리는 ...")
    private String description;
}
