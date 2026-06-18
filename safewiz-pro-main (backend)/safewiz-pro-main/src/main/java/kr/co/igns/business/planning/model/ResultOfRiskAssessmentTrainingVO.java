package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ResultOfRiskAssessmentTrainingVO extends BaseVO {

    @Schema(description = "평가일", example = "YYYY-MM-DD")
    private String asmntDate;

    @Schema(description = "평가시작시간", example = "HH:mm")
    private String asmntStartTime;

    @Schema(description = "평가종료시간", example = "HH:mm")
    private String asmntEndTime;

    @Schema(description = "평가장소")
    private String asmntPlace;

    @Schema(description = "평가내용")
    private String asmntContent;

    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

    // 참석자 명단
    @Schema(description = "참석자 목록")
    private List<SignatureDto> asmntAttendees;
}
