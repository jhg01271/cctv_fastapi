package kr.co.igns.business.dataset.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TypeofbusinessVO extends BaseVO {
    @Schema(description = "구성(공사) ID", example = "")
    private String constructId;

    @Schema(description = "공사명", example = "")
    private String constructNm;

    @Schema(description = "정렬 순번", example = "")
    private String constructionOrder_by;

    @Schema(description = "고용업종코드(세세분류)", example = "")
    private String detailCd;
    
    @Schema(description = "고용업종명(세세분류)", example = "")
    private String detailNm;

    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

}
