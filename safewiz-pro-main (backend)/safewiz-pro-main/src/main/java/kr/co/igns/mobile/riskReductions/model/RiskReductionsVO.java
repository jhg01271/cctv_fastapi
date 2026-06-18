package kr.co.igns.mobile.riskReductions.model;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data

public class RiskReductionsVO {
	@Schema(description = "감소대책 ID", example = "")
	private String id;
	@Schema(description = "감소대책 대응", example = "")
	private String content;
	@Schema(description = "담담자", example = "")
	private Map<String, Object> riskAssessment;
	@Schema(description = "유해위험요인", example = "")
	private Map<String, Object> hazardFactor;
	@Schema(description = "위험성평가", example = "")
	private Map<String, Object> process;
	@Schema(description = "공정", example = "")
	private Map<String, Object> work;
	@Schema(description = "작업", example = "")
	private List<Map<String, Object>> assignees;
	@Schema(description = "개선 예정일", example = "")
	private Timestamp expectedAt;
	@Schema(description = "개선 완료일", example = "")
	private Timestamp completedAt;
	@Schema(description = "위원회 ID", example = "")
	private Timestamp createdAt;
}
