package kr.co.igns.mobile.riskReductions.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RiskReductionsSaveVO {
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
	private String docSeq;
	
	private String docSeqDetail;
	
	private String content;
	
	private String updatedBy;
}
