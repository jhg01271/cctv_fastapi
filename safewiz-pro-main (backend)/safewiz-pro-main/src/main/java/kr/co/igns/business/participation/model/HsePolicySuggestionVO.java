package kr.co.igns.business.participation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class HsePolicySuggestionVO extends BaseVO {
    @Schema(description = "고객사 ID", example = "202408200001")
    private String clntId;

    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;

    @Schema(description = "근로자의견 ID", example = "202408200001")
    private String hsePolicySugId;

    @Schema(description = "안전보건경영방침 ID", example = "202410070001")
    private String hsePolicyId;

    @Schema(description = "사업장 이름", example = "일주지앤에스")
    private String compNm;

    @Schema(description = "사용자 ID", example = "202408200001")
    private String userId;

    @Schema(description = "근로자 ID", example = "202410070001")
    private String hrId;

    @Schema(description = "근로자 이름", example = "김정엽")
    private String hrNm;

    @Schema(description = "근로자 직책", example = "대표이사")
    private String jbrpNm;

    @Schema(description = "근로자 의견", example = "근로자 의견")
    private String workerSuggestion;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "조회 시작일", example = "2024.10.15")
    private String searchFrom;

    @Schema(description = "조회 종료일", example = "2024.10.17")
    private String searchTo;
}
