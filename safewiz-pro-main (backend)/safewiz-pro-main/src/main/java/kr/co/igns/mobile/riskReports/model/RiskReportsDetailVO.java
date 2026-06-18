package kr.co.igns.mobile.riskReports.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RiskReportsDetailVO {
	@Schema(description = "근로자 참여 ID", example = "")
	private String id;
	@Schema(description = "근로자 참여 내용", example = "")
	private String content;
	@Schema(description = "검토자 의견", example = "")
	private String reviewerContent;
	@Schema(description = "작업장", example = "")
	private Map<String, Object> workplace;
	@Schema(description = "작성일자", example = "")
	private String registrationDate;
	@Schema(description = "경험담", example = "")
	private List<Map<String, Object>> experiences;
	@Schema(description = "작성자 정보", example = "")
	private Map<String, Object> writer;
	@Schema(description = "검토자 정보", example = "")
	private Map<String, Object> reviewer;
	@Schema(description = "생성일시", example = "")
	private String createdAt;
}
