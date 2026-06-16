package kr.co.igns.mobile.riskReductions.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RiskReductionsDetailVO {
	@Schema(description = "감소대책 ID", example = "")
	private String id;
	@Schema(description = "감소대책 내용", example = "")
	private String content;
	@Schema(description = "담담장 정보", example = "")
	private List<Map<String, Object>> assignees;
	@Schema(description = "개선전 위험성", example = "")
	private String beforeRiskLevel;
	@Schema(description = "개선후 위험성", example = "")
	private String afterRiskLevel;
	@Schema(description = "유해위험요인 상세 정보", example = "")
	private Map<String, Object> hazardFactor;
	@Schema(description = "위험성평가 정보", example = "")
	private Map<String, Object> riskAssessment;
	@Schema(description = "공정 정보", example = "")
	private Map<String, Object> process;
	@Schema(description = "작업 정보", example = "")
	private Map<String, Object> work;
	@Schema(description = "개선조치 내용", example = "")
	private String implementation;
	@Schema(description = "개선전 첨부파일", example = "")
	private List<Map<String, Object>> beforeFiles;
	@Schema(description = "개선후 첨부파일", example = "")
	private List<Map<String, Object>> afterFiles;
	@Schema(description = " 개선 예정일", example = "")
	private Timestamp expectedAt;
	@Schema(description = " 개선 예정일", example = "")
	private Timestamp completedAt;
	@Schema(description = "생성일", example = "")
	private Timestamp createdAt;
}
