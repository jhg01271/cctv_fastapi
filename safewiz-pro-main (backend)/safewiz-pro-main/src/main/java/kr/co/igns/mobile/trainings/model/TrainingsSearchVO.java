package kr.co.igns.mobile.trainings.model;

import lombok.Data;

@Data
public class TrainingsSearchVO {
	private String compId;
	
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
	private String hrId;
	
	private String startDate;
	
	private String endDate;
	
	private String category;
	
	private boolean onlyMine;
}
