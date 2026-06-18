package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class MsdsLawVO extends BaseVO {
    @Schema(description = "MSDS ID", example="작성 연/월/일 + 순서 | YYYYMMDD0001")
    private String msdsId;
    @Schema(description = "msds code")
    private String msdsItemCode;
    @Schema(description = "msds 대분류")
    private String msdsItemNameKor;
    @Schema(description = "msds 상세")
    private String itemDetail;
    @Schema(description = "msds id")
    private String id;
    @Schema(description = "msds 상세")
    private String nm;
}
