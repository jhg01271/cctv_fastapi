package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.Data;

@Data
public class ImplementationOfRiskAseessmentVO extends BaseVO {
	
	@Schema(description = "위험요인 분류ID", example = "")
	private String hazardsClassification;
	
	@Schema(description = "원인", example = "")
	private String hazardsCause;
	
	@Schema(description = "유해 위험요인", example = "")
	private String hazardsFactor;
	
	@Schema(description = "타입", example = "")
	private String hazardsType;
	
	@Schema(description = "공정ID", example = "202410080003")
	private String processId;

	@Schema(description = "공정명", example = "202410080003")
	private String processNm;
	
	@Schema(description = "분류명", example = "")
	private String implementationNm;

	@Schema(description = "관련 법규 ID", example = "")
	private String legalId;

	@Schema(description = "관련 법규명", example = "")
	private String legalNm;

	@Schema(description = "관련근거", example = "")
	private String relatedEvidence;
	
	@Schema(description = "사용여부", example = "Y")
	private String useYn;
	
	@Schema(description = "공정작업ID", example = "")
	private Integer prcsWorkId;

	@Schema(description = "위험성평가 가능성", example = "")
	private String frequencyScore;
	
	@Schema(description = "위험성평가 중대성", example = "")
	private String impactScore;
	
	@Schema(description = "위험성평가 위험성 결정", example = "")
	private String riskScore;

	@Schema(description = "위험성평가 가능성", example = "")
	private String afterFrequencyScore;

	@Schema(description = "위험성평가 중대성", example = "")
	private String afterImpactScore;

	@Schema(description = "위험성평가 위험성 결정", example = "")
	private String afterRiskScore;
	
	@Schema(description = "현재안전보건조치", example = "")
	private String currentSafetyMeasures;
	
	@Schema(description = "위험성 시스템코드 데이터", example = "")
	List<SystemMinorCodeVO> systemCodeList;

	@Schema(description = "작업 내용", example = "")
	private String workContent;

	@Schema(description = "위험성 평가 계획 기준", example = "")
	private String riskAssessmentStandards;

	@Schema(description = "위험성 평가 계획 허용 기준", example = "")
	private String riskAllowanceStandards;
	
	@Schema(description = "위험성 계획 작성년도", example = "")
	private String planWriteYear;
	
	@Schema(description = "참조 작성년도", example = "")
	private String implWriteYear;

	@Schema(description = "참조 문서 번호", example = "")
	private String implDocNo;

	@Schema(description = "참조 위헝성 평가 이행 문서번호", example = "")
	private String implDocSeq;

	@Schema(description = "위험성평가 공정/설비/물질 참조 작성년도", example = "")
	private String cpemWriteYear;

	@Schema(description = "위험성평가 공정/설비/물질 참조 문서 번호", example = "")
	private String cpemDocNo;

	@Schema(description = "제거되는 위험성 평가 이행의 개수", example = "")
	private int riskImplRemoveCount;

	@Schema(description = "감소대책 리스트", example = "")
	List<ImplementationOfRiskAseessmentReductionVO> implementReduList;
	
}
