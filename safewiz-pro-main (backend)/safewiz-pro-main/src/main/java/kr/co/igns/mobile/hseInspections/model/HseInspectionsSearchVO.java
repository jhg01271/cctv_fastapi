package kr.co.igns.mobile.hseInspections.model;

import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.Data;

@Data
public class HseInspectionsSearchVO {
	private String id;
	private String compId;
	private String hrId;
	private String startDate;
	private String endDate;
	private String date;
	private String eqId;
	private String writeYear;
	private String docNo;
	private String docType;
	private String updatedBy = SecurityUtil.getCurrentMemberId();
}
