package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ClientCompVO extends BaseVO {
    @Schema(description = "사업장의 고객사 id")
    private String compClntId;
    @Schema(description = "사업장의 고객사 id")
    private String clntCompId;
    @Schema(description = "사업장명", example = "")
    private String compNm;
    @Schema(description = "대표자", example = "김일주")
    private String rpstNm;
    @Schema(description = "전화번호", example = "")
    private String telNo;
    @Schema(description = "사용유무", example = "N")
    private String useYn;
}
