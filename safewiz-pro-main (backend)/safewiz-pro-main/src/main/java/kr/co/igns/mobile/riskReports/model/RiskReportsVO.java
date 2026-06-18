package kr.co.igns.mobile.riskReports.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RiskReportsVO {
	@Schema(description = "근로자 참여 ID", example = "")
	private String id;
	@Schema(description = "근로자 참여 내용", example = "")
	private String content;
	@Schema(description = "작업장", example = "")
	private Map<String, Object> workplace;
	@Schema(description = "작성자", example = "")
	private Map<String, Object> writer;
	@Schema(description = "검토자", example = "")
	private Map<String, Object> reviewer;
	@Schema(description = "작성일자", example = "")
	private String registrationDate;
	@Schema(description = "createdAt", example = "")
	private String createdAt;
}
