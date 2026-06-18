package kr.co.igns.mobile.hseInspections.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HseInspectionsVO {
	@Schema(description = "안전점검 달력 count", example = "")
    private List<Map<String, Integer>> doneCount;
	@Schema(description = "안전점검 달력 count", example = "")
    private List<Map<String, Integer>> undoneCount;
	@Schema(description = "안전점검 ID", example = "")
	private String id;
	@Schema(description = "안전점검 템플릿 제목", example = "")
	private String title;
	@Schema(description = "점검 완료 여부 (서명 여부)", example = "")
	private boolean done;
	@Schema(description = "점검 대상", example = "")
	private Map<String, Object> checkTarget;
	@Schema(description = "점검자 정보 (nullable)", example = "")
	private Map<String, Object> checker;
	@Schema(description = "점검 시작일시 ", example = "")
	private String startedAt;
	@Schema(description = "점검 종료일시", example = "")
	private String endedAt;
	@Schema(description = "작성일시", example = "")
	private String createdAt;
	
	private String compId;
	private String hrId;
	private String userId;
	private String clntId;
	private String writeYear;
	private String docNo;
	private String docType;
}
