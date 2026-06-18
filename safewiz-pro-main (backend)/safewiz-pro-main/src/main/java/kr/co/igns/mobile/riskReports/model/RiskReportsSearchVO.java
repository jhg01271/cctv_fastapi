package kr.co.igns.mobile.riskReports.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RiskReportsSearchVO {
	private String id;
	
	private String compId;
	
	private String year;
	
	private String startDate;
	
	private String endDate;
	
	private boolean onlyMine;
	
	private String writeYear;
	
	private String hrId;
	
	private String docType;
	
	private String docNo;
	
	private String type;
	
	private String content;
	
	private String hrNm;
	
	private String orgnNm;
	
	private String jbrpNm;
	
	private String reviewerHrId;
	
	private String createdBy;
	
	private String updatedBy;
}
