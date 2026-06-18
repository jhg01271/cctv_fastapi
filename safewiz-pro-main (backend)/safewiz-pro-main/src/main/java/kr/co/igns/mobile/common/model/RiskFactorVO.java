package kr.co.igns.mobile.common.model;

import lombok.Data;

@Data
public class RiskFactorVO {
	private String potentialId;
	private boolean important;
	private String importantId;
	private String content;
	private String potentialMeasures;
	private String importantMeasures;
	private boolean effectiveMeasures;
	private String safetyCheckId;
	private boolean actioned;
	private String action;
	private String createdAt;
}
