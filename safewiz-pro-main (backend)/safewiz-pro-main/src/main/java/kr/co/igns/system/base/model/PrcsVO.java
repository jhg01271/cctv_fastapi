package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class PrcsVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
    @Schema(description = "공정 ID")
    private String processId;
    @Schema(description = "공정명", example = "공정#1")
    private String processNm;
    @Schema(description = "사용처(본사/도급/사급)")
    private String usageType;
    @Schema(description = "사용처(본사/도급/사급)")
    private String usageTypeNm;
    @Schema(description = "정렬순서", example = "99")
    private int ordSeq;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;
    //endregion

    //Mapping
    @Schema(description = "작업장 ID 리스트")
    private List<String> workplaceIdList;
    @Schema(description = "작업장 리스트(조회용)")
    private List<BaseMapVO> workplaceList;
    @Schema(description = "작업장 ID")
    private String workplaceId;

    @Schema(description = "조직 ID 리스트")
    private List<String> orgnIdList;
    @Schema(description = "조직 리스트(조회용)")
    private List<BaseMapVO> orgnList;
    @Schema(description = "조직 ID")
    private String orgnId;

    //인원 Mapping
    @Schema(description = "담당자(정)")
    private List<HrVO> headHrList;
    @Schema(description = "담당자(부)")
    private List<HrVO> secondHrList;

}
