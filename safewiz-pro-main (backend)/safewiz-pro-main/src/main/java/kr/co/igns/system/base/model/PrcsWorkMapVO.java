package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class PrcsWorkMapVO extends BaseMapVO {
    @Schema(description = "공정 id")
    private String processId;

    public PrcsWorkMapVO(String targetType, String targetId, String prcsWorkId, String userNm,String processId) {
        super(targetType, targetId, prcsWorkId, userNm);
        this.processId = processId;
    }
}
