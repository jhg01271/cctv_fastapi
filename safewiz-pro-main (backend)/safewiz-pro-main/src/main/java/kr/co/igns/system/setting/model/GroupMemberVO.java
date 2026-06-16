package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class GroupMemberVO extends BaseVO {
    @Schema(description = "사업장 아이디", example = "IGNS")
    private String compId;
    @Schema(description = "권한 코드", example = "C0001")
    private String grpId;
    @Schema(description = "인사 코드", example = "C0001")
    private String hrId;
}
