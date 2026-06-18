package kr.co.igns.mobile.hsePolicy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HsePolicySearchVO {
	private String compId;
	
	private String year;
	
	private String hrId;
	
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
	private String content;
	
	private String next_doc_seq;
}
