package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class OrganizationVO extends BaseVO {
    @Schema(description = "사업장 ID")
    private String compId;
    @Schema(description = "조직 ID")
    private String orgnId;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "조직 설명")
    private String orgnDesc;
    @Schema(description = "정렬순서")
    private int ordSeq;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

    //인원 Mapping
    @Schema(description = "조직장")
    private String headHrId;
    @Schema(description = "부장")
    private String secondHrId;
    @Schema(description = "조직장명")
    private String headHrNm;
    @Schema(description = "부장명")
    private String secondHrNm;
    @Schema(description = "직위명")
    private String jbrpNm;

    //region 카드 이미지 처리
//    @Schema(description = "card의 logo")
//    private String logoId;
//    @Schema(description = "card의 Thumbnail")
//    private String thumbnailId;
    @Schema(description = "card의 정")
    private String primaryId;
    @Schema(description = "card의 부")
    private String secondaryId;
    //endregion
}
