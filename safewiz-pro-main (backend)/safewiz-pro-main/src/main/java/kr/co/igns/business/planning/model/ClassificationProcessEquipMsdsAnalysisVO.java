package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.impl.model.HazmatChecklistVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ClassificationProcessEquipMsdsAnalysisVO extends BaseVO {
	@Schema(description = "공정 문서 번호", example = "")
	private String procDocNo;
	
	@Schema(description = "공전 문서 시퀀스", example = "")
	private String procDocSeq;
	
	@Schema(description = "공정 ID", example = "")
	private String processId;
	
	@Schema(description = "공정명", example = "AAA01")
	private String processNm;
	
	@Schema(description = "공정차수", example = "AAA01")
	private String revNo;

	@Schema(description = "공정차수명", example = "AAA01")
	private String revNm;
	
	@Schema(description = "공정설명", example = "")
	private String processDesc;
	
	@Schema(description = "공정(차수)", example = "")
	private String prcsNm;
	
	@Schema(description = "위험성평가 사전 준비자료 데이터 유무", example = "Y")
	private String isUsed;
	
	@Schema(description = "설비 또는 MSDS 정보", example = "")
	private List<ClassificationProcessEquipMsdsItemVO> itemList;
	
	@Schema(description = "작업 리스트", example = "")
	private List<ImplementationOfRiskAseessmentProcessVO> processList;
}
