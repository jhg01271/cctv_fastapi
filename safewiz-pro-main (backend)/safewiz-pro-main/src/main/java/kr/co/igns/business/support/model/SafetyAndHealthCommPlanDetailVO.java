package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyAndHealthCommPlanDetailVO extends BaseVO {

    @Schema(description = "내/외부 구분", example = "0001(내부)")
    private String contentType;

    @Schema(description = "내용", example = "...")
    private String content;

    @Schema(description = "시기", example = "...")
    private String period;

    @Schema(description = "대상자", example = "...")
    private String subject;

    @Schema(description = "방법", example = "...")
    private String method;

    @Schema(description = "정렬", example = "99")
    private int ordSeq=99;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;
}
