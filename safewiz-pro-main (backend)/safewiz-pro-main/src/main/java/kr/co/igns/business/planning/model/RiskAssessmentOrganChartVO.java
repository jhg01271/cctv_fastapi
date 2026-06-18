package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class RiskAssessmentOrganChartVO extends BaseVO {
    @Schema(description = "차트ID", example = "")
    private String chartId;
    
	@Schema(description = "차트명", example = "")
    private String chartNm;
    
	@Schema(description = "조직 ID", example = "AAA01")
	private String orgnId;

	@Schema(description = "조직이름", example = "AAA01")
	private String orgnNm;
	
	@Schema(description = "확정여부", example = "Y")
    private String confirmYn;
	
	@Schema(description = "조직도", example = "")
	private String chartData;
	
	@Schema(description = "확정일", example = "")
    private String confirmDt;
	
	@Schema(description = "인원수", example = "")
	private String memberCount;
	
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
    
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

	 @Schema(description = "차트 blob 데이터", example = "data://")
    private String chartBlobData;
}
