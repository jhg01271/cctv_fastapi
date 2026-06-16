package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EmergencyRoleVO extends BaseVO {

    @Schema(description = "비상사태 유형 ID", example = "202501010001")
    private String typeId;

    @Schema(description = "비상사태 유형명", example = "화재 폭발 대비")
    private String typeNm;

    @Schema(description = "비상통제 역할 ID", example = "202501010001")
    private String roleId;

    @Schema(description = "비상통제 역할명", example = "비상지휘대장")
    private String roleNm;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "역할", example = "[...]")
    private List<EmergencyRoleVO> roles;
}
