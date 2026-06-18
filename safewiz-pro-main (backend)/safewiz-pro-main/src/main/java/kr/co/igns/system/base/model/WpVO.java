package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class WpVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
    @Schema(description = "작업장 ID")
    private String workplaceId;
    @Schema(description = "작업장명", example = "작업장#1")
    private String workplaceNm;
    @Schema(description = "작업장명", example = "작업장#1") // 컴포넌트 사용용도
    private String name;
    @Schema(description = "구역명")
    private String workplaceArea;
    @Schema(description = "별칭")
    private String workplaceAlias;
    @Schema(description = "정렬순서", example = "99")
    private int ordSeq=99;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

    //인원 Mapping
    @Schema(description = "담당자(정)")
    private List<HrVO> headHrList;
    @Schema(description = "담당자(부)")
    private List<HrVO> secondHrList;

    //region 카드 이미지 처리
//    @Schema(description = "card의 logo")
//    private String logoId;
    @Schema(description = "card의 Thumbnail")
    private String thumbnailId;
    //endregion
}
