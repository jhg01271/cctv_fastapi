package kr.co.igns.mobile.committees.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommitteesSearchVO {
	private String compId;
	
	private String year;
	
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
	private String hrId;
	
	private String startDate;
	
	private String endDate;
	
	private String category;
	
	private boolean onlyMine;
}
