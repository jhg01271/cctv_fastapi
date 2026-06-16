package kr.co.igns.business.planning.model;

import java.util.List;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ImprovementAndImplementationVO extends BaseVO {
	private String planNm;
	private String hazardsCause;
	private String processNm;
	private String workContent;
	private String hazardsClassificationNm;
	private int improvementCnt;
	private String hazardsFactor;
	private String legalNm;
	private String relatedEvidence;
	private String reductionMeasures;
	private String expectedDate;
	private String completedDate;
	private String implContent;
	private String hrId;
	private String hrNm;
	private String frequencyScore;
	private String impactScore;
	private String riskScore;
	private String afterFrequencyScore;
	private String afterImpactScore;
	private String afterRiskScore;
	private String riskAssessmentStandards;
	private String riskAllowanceStandards;
	private String workDetailHrs;
	private String prevFileId;
	private String afterFileId;
	private String currentSafetyMeasures;
	
	private List<ImprovementAndImplementationVO> planDetail;
	private List<ImprovementAndImplementationVO> workDetail;
	private List<ImprovementAndImplementationVO> workDetailHr;
}