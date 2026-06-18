package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class VoluntarySafetyInspectorVO extends BaseVO {

    @Schema(description = "인원 ID")
    private String hrId;

    @Schema(description = "조직명")
    private String orgnNm;

    @Schema(description = "직위")
    private String jbrp;

    @Schema(description = "입사일", example = "YYYYMMDD")
    private String workingAt;

    @Schema(description = "관련자격증 보유 여부", example = "Y")
    private YesNo hasCertifiCate;

    @Schema(description = "정렬순서")
    private int ordSeq;

    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

    /*
        데이터 불러오기 용
     */
    @Schema(description = "(인원관리) 이름")
    private String hrNm;
}
