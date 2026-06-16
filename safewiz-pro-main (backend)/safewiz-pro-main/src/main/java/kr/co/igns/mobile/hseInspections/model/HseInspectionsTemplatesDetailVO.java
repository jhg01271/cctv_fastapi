package kr.co.igns.mobile.hseInspections.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HseInspectionsTemplatesDetailVO {
	@Schema(description = "안전점검 점검표 템플릿 ID", example = "")
	private String id;
	@Schema(description = "안전점검 템플릿 제목", example = "")
	private String title;
	@Schema(description = "점검 대상", example = "")
	private Map<String, Object> checkTarget;
	@Schema(description = "점검자 정보", example = "")
	private List<Map<String, Object>> checkList;
}
