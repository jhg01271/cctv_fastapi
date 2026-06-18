package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class MsdsHrVO extends BaseVO {
    @Schema(description = "MSDS ID", example="작성 연/월/일 + 순서 | YYYYMMDD0001")
    private String msdsId;
    @Schema(description = "msds 담당자 id")
    private String hrId;
    @Schema(description = "msds 담당자")
    private String hrNm;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "직책/직위")
    private String jbrpNm;
    @Schema(description = "msds 담당자 id")
    private String id;
    @Schema(description = "msds 담당자")
    private String nm;
}
