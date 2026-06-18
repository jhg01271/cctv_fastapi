package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EmergencyResponseTrainingResultVO extends BaseVO {

    @Schema(description = "조직ID", example = "202411110001")
    private String orgnId;

    @Schema(description = "조직명", example = "ESG 사업부")
    private String orgnNm;

    @Schema(description = "비상사태명", example = "화재비상대피훈련")
    private String trainingNm;

    @Schema(description = "훈련일자", example = "20241112")
    private String actionDt;

    @Schema(description = "훈련명", example = "비상대피훈련")
    private String actionNm;

    @Schema(description = "훈련장소", example = "률영빌딩")
    private String actionLocation;

    @Schema(description = "훈련내용", example = "대피")
    private String actionContent;

    @Schema(description = "훈련결과", example = "잘 대피함")
    private String actionResult;

    @Schema(description = "목표 시간", example = "5")
    private int targetTime;

    @Schema(description = "측정 시간", example = "4")
    private int measurementTime;

    @Schema(description = "실제 행동 관찰", example = "잘 대피함")
    private String observationActualBehavior;

    @Schema(description = "비고", example = "..")
    private String remark;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "아코디언 타이틀", example = "2024.12.19-(미사용)")
    private String titleActionDt;
}
