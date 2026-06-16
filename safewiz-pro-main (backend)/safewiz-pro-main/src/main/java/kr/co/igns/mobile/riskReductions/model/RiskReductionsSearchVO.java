package kr.co.igns.mobile.riskReductions.model;

import lombok.Data;

@Data
public class RiskReductionsSearchVO {
	private String compId;
	
	private String hrId;
	
	private boolean onlyMine;
	
	private boolean onlyUndone;
	
	private String processId;
	
	private String riskAssessmentId;
	
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
	private String docSeq;
	
	private String docSeqDetail;
}
