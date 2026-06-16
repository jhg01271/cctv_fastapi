package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ImplementationOfRiskAseessmentReductionHrVO extends BaseVO {
	
	@Schema(description = "감소대책 담당자 문서번호", example = "00001")
	private String docSeqDetail2;
	
	@Schema(description = "담당자 ID", example = "")
	private String hrId;
	
	@Schema(description = "담당자명", example = "")
	private String hrNm;
}
