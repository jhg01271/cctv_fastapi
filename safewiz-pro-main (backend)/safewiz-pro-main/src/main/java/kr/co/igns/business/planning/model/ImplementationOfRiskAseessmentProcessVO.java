package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ImplementationOfRiskAseessmentProcessVO extends BaseVO {
	
	@Schema(description = "", example = "")
	private String prcsWorkId;
	
	@Schema(description = "", example = "")
	private String prcsCnfmId;
	
	@Schema(description = "", example = "")
	private String workContent;
	
	@Schema(description = "등록 요인 개수", example = "")
	private String registerCount;
	
	@Schema(description = "완료 요인 개수", example = "")
	private String completedCount;
}
