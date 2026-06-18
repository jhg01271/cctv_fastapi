package kr.co.igns.mobile.hseInspections.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HseInspectionsTemplatesVO {
	@Schema(description = "안전점검 점검표 템플릿 ID", example = "")
	private String id;
	@Schema(description = "안전점검 템플릿 제목", example = "")
	private String title;
	@Schema(description = "안전점검", example = "")
	private Map<String, Object> inspection;
	@Schema(description = "점검 대상", example = "")
	private Map<String, Object> checkTarget;
	@Schema(description = "점검자 정보", example = "")
	private Map<String, Object> checker;
}
