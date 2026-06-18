package kr.co.igns.business.support.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EducationTrainingVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;

    @Schema(description = "카드 제목", example = "안전보건경영방침")
    private String title;

    @Schema(description = "카드 메뉴 라우터 경로", example = "HsePolicy")
    private String route;

    @Schema(description = "카드 활성화 상태 값", example = "Y")
    private YesNo activeYn;

    @Schema(description = "설명", example = "안전보건경영방침은 ...")
    private String description;
}
