package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EmergencyResponseTrainingVO extends BaseVO {

    @Schema(description = "조직ID", example = "202411110001")
    private String orgnId;

    @Schema(description = "조직명", example = "ESG 사업부")
    private String orgnNm;

    @Schema(description = "비상사태명", example = "화재비상대피훈련")
    private String trainingNm;

    @Schema(description = "장소", example = "률영빌딩 5층")
    private String trainingLocation;

    @Schema(description = "발생내용", example = "화재발생")
    private String trainingContent;

    @Schema(description = "예상되는 피해", example = "사무실 소실")
    private String anticipatedDamage;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "결재상태", example = "I")
    private String approvalStatus;

    @Schema(description = "시나리오 리스트", example = "[...]")
    private List<EmergencyResponseTrainingScenarioVO> scenarioList;

    @Schema(description = "실시 보고서 리스트", example = "[...]")
    private List<EmergencyResponseTrainingResultVO> resultList;

    @Schema(description = "실시 보고서 실시 횟수", example = "0")
    private int resultCnt;

    @Schema(description = "목표시간(분)", example = "14")
    private int targetTime;
}
