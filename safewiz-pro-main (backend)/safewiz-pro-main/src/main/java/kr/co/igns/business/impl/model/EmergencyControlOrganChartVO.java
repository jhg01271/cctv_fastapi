package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EmergencyControlOrganChartVO extends BaseVO {

    @Schema(description = "차수", example = "202410310001")
    private String chartId;

    @Schema(description = "차수명", example = "2024년도 10월 1차수")
    private String chartNm;

    @Schema(description = "확정일자", example = "20241031")
    private String confirmAt;

    @Schema(description = "조직 수", example = "0")
    private int orgCount;

    @Schema(description = "차트정보", example = "...")
    private String chartData;

    @Schema(description = "비상사태유형", example = "0001")
    private String emergencyType;

    @Schema(description = "비상사태유형명", example = "화재 폭발 대비")
    private String emergencyTypeNm;

    @Schema(description = "확정여부", example = "Y")
    private String confirmYn;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "조직도 blob 형식 데이터 (출력물에 사용)", example = "data:image/png;base64..")
    private String chartBlobData;

}
