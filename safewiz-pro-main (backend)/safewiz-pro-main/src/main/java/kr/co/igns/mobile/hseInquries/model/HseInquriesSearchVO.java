package kr.co.igns.mobile.hseInquries.model;

import lombok.Data;

@Data
public class HseInquriesSearchVO {
	private String compId;
	private String hrId;
	private String year;
	private boolean onlyMine;
	
	private String writeYear;
	private String docType;
	private String docNo;
	
	private String updatedBy;
	
    @Data
	public static class Id {
		private String id;
	}

}
