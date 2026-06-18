package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class GroupVO extends BaseVO {
    @Schema(description = "사업장 아이디", example = "IGNS")
    private String compId;
    @Schema(description = "권한 코드", example = "202504230001")
    private String grpId;
    @Schema(description = "권한 코드", example = "202504230001")
    private String grpCd;
    @Schema(description = "권한 코드명", example = "코드 이름")
    private String grpNm;
    @Schema(description = "순번", example = "1")
    private int grpOdr;

    // 데이터셋
     @Schema(description = "그룹코드", example = "202504230001")
    private String tempGrpId;
    @Schema(description = "그룹에 대한 설명", example = "1")
    private String desc;
    @Schema(description = "순번", example = "1")
    private int ordSeq;
    @Schema(description = "유저 ID", example = "1")
    private String hrId;
    @Schema(description = "유저명", example = "1")
    private String hrNm;


    private int count;
    private YesNo useYn;
}
